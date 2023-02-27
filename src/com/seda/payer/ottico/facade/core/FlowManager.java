package com.seda.payer.ottico.facade.core;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Serializable;

import org.apache.log4j.Logger;
/**
 * @author aniello.labua
 */
public class FlowManager implements Serializable {

	private static final long serialVersionUID = 1L;
	private Logger log;
	private DataInputStream inStream;
	private FlowFileUtility fileUtil;

	public FlowManager(FlowFileUtility fileUtil, Logger logger){
		this.fileUtil = fileUtil;
		this.log = logger;
		log.debug("TEST: IL FLOW MANAGER E' STATO AVVIATO CON LE SEGUENTI INFORMAZIONI: " + 
				this.fileUtil.getFlowInfo().toString());
	}
	/**
	 * @return
	 */
	public FlowState start() {
		FlowState flowState = new FlowState(FlowState.SUCCESS, null); 
		try {
			log.debug("TEST: INIZIO PROCESSO - AVVIATO METODO START" );
			BufferedReader br = this.getFileReader();
			
			String currentLine;
			int lineNumber = 0;
			boolean validate = true;
//			log.info("[Start] Processo di validazione formale");
			/*we analaize formal validatio*/
			FlowState formalValidState = this.formalValidation();
			log.debug("TEST: RISULTATO VALIDAZIONE FORMALE: " + formalValidState.getCode() + "[0 OK | 1 ERRORE]");
//			log.info("[End] Processo di validazione formale");
			if(formalValidState.getCode() == FlowState.DISCARDED) {
				flowState.setCode(FlowState.DISCARDED);
				flowState.setMessage(formalValidState.getMessage());
				//inizio LP PG21XX04 Leak
				br.close();
				//fine LP PG21XX04 Leak
				return flowState;
			}	
				
//			log.info("[Start] Processo di validazione sui dati presenti nei vari record");
			/*L'OT0 è utilizzato per validare gli altri elementi*/
			OT0FlowData currentOT0 = null;
			FlowMessages messages = null;
			
			while ((currentLine = br.readLine()) != null)   {
				lineNumber++;
				switch (this.flowType(currentLine)) {
				case 1:
//					log.info("Stai processando un record OT0");
					currentOT0 = new OT0FlowData(currentLine, log);
					messages = currentOT0.validate(this.fileUtil.getFlowInfo().getCurrentConfig(),lineNumber);
					//we manage validation result
					if (!messages.isEmpty()) {
						validate = false;
						log.info(messages.getMessages());
						flowState.addMessage(messages.getMessages());
					}
					break;
				case 2:
					log.debug("Stai processando un flusso OT1");
					if(currentOT0!=null){
						log.debug("TEST: -> PROCESSO OT1 AVENDO OT0");
						OT1FlowData currentOT1 = new OT1FlowData(currentLine,log);
						messages = currentOT1.validate(currentOT0,lineNumber);
						//we manage validation result
						if (!messages.isEmpty()) {
							validate = false;
							log.info(messages.getMessages());
							flowState.addMessage(messages.getMessages());
						}
					}

					break;
				case 3:
					log.debug("Stai processando un flusso OT9");
					if(currentOT0!=null){
						OT9FlowData ot9 = new OT9FlowData(currentLine,log);
						messages = ot9.validate(currentOT0,this.fileUtil.getFlowInfo(),lineNumber);
						//we manage validation result
						if (!messages.isEmpty()) {
							validate = false;
							log.info(messages.getMessages());
							flowState.addMessage(messages.getMessages());
						}
					}
					break;
				case -1:
					/*Controllo 1: tipi di flussi*/
					log.debug("Flusso errato alla linea " + lineNumber);
					break;
				}
			}
			this.inStream.close();
			log.info("[End] Processo di validazione sui dati presenti nei vari record");

			br.close();
			if (!validate)
				return flowState.setCode(FlowState.DISCARDED);
		

		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}

		return flowState;
	}

	/**
	 * Controllo del tipo di flusso
	 * @param flowString
	 * @return
	 */
	private int flowType(String flowString){

		if(flowString.startsWith("OT0"))
			return 1;
		if(flowString.startsWith("OT1"))
			return 2;
		if(flowString.startsWith("OT9"))
			return 3;
		else return -1;
	}

	/*Metodo per la validazioneFormale*/
	private FlowState formalValidation(){
		log.debug("TEST: INIZIO PROCESSO - AVVIATO METODO FORMALVALIDATION" );
		//we define info for file reading
		BufferedReader br = this.getFileReader();
		String currentLine = null;
		String previousLine = null;
		//we define validation object
		boolean validate = true;
		FlowMessages messages = new FlowMessages();
		FlowState flowState = new FlowState(FlowState.SUCCESS, null); 
		//we define flowfile info
		int lineNumber = 0;
		int numberOfOT0 = 0;
		int numberOfOT1 = 0;
		int numberOfOT9 = 0;
		OT0FlowData head = null;
		OT9FlowData tail = null;
		//
		
		try {
			while ((currentLine = br.readLine()) != null)   {
				lineNumber++;
				switch (this.flowType(currentLine)) {
				case 1:
					log.debug("Stai processando un record OTO");
					numberOfOT0++;
					if(numberOfOT0<=1)
						head = new OT0FlowData(currentLine,log);
					//we manage validation result
					if (previousLine != null) {
						log.debug("Flusso errato alla linea " + lineNumber + " Il record OT0 non è il primo record del flusso");
							validate = false;
							log.info(messages.getMessages());
							flowState.addMessage("Flusso errato alla linea " + lineNumber + " Il record OT0 non è il primo record del flusso");
					}
					break;
				case 2:
					log.debug("Stai processando un record OT1");
					numberOfOT1++;
					previousLine = currentLine;
					if (head == null || tail != null){
						log.debug("Flusso errato alla linea " + lineNumber + " Record OT1 prima del record OT0 o dopo il record OT9");
							validate = false;
						   log.info(messages.getMessages());
						   flowState.addMessage("Flusso errato alla linea " + lineNumber + " Record OT1 prima del record OT0 o dopo il record OT9");
					}
					break;
				case 3:
					log.debug("Stai processando un record OT9");
					numberOfOT9++;
					if(numberOfOT9<=1)
						tail = new OT9FlowData(currentLine,log);
					if (!(flowType(previousLine)==2)){
						log.debug("Flusso errato alla linea " + lineNumber + " Il record precedente al record OT9 non e' un record OT1");
							validate = false;
						   log.info(messages.getMessages());
						   flowState.addMessage("Flusso errato alla linea " + lineNumber + " Il record precedente al record OT9 non e' un record OT1");
					}
					break;
				case -1:
					/*Controllo 1: tipi di flussi*/
					log.debug("Flusso errato alla linea " + lineNumber + " - Record non consentito : " + currentLine);
						validate = false;
						log.info(messages.getMessages());
						flowState.addMessage("Flusso errato alla linea " + lineNumber + " - Record non consentito : " + currentLine);
					break;
				}
			}
			this.inStream.close();
			log.debug("TEST: FINE PROCESSO FORMALVALIDATION : RISULTATI:\n\tnumero linee: " + lineNumber
					+"\n\tnumOT0: " + numberOfOT0 + "\n\tnumOT1: " + numberOfOT1 + "\n\tnumOT9: " +numberOfOT9  );
			
			/*Controllo numero record*/
			if(Integer.parseInt(tail.getRecord().trim()) != (numberOfOT0 + numberOfOT1 + numberOfOT9)){
				log.debug("CONTROLLI FORMALI - CONTROLLO FALLITO SU NUMERO RECORD" );
				validate = false;
				flowState.addMessage("CONTROLLI FORMALI - CONTROLLO FALLITO SU NUMERO RECORD");
				flowState.setCode(FlowState.DISCARDED);
				br.close();
				return flowState;
			}
			/*CONTROLLO DI PRESENZA SOLO UNA COPPIA OT9 ED OT0*/
			if(this.fileUtil.getFlowInfo().getNumberOfOT0()>1&&this.fileUtil.getFlowInfo().getNumberOfOT9()>1){
				log.debug("CONTROLLI FORMALI - CONTROLLO COPPIA 0T0-0T9 FALLITO" );
				validate = false;
				flowState.addMessage("CONTROLLI FORMALI - CONTROLLO COPPIA 0T0-0T9 FALLITO");
				flowState.setCode(FlowState.DISCARDED);
				br.close();
				return flowState;
			}
			/*CONTROLLO DELLA STRUTTURA DEL FILE*/
			//this.fileStructureAnalisys();
			br.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		if (validate == false) {
			flowState.setCode(FlowState.DISCARDED);
		}
		//inizio LP PG21XX04 Leak
		try {
			br.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		//fine LP PG21XX04 Leak
		return flowState;
	}

	
	private BufferedReader getFileReader(){
		try{
			FileInputStream fstream = new FileInputStream(this.fileUtil.getFlowPath());
			//FlowFileUtility flowFileUtil = new FlowFileUtility(this.flowPath);
			this.inStream = new DataInputStream(fstream);
			BufferedReader br = new BufferedReader(new InputStreamReader(inStream));
			log.debug("File del flusso aperto con successo");
			return br;
			
		} catch (FileNotFoundException e) {
			log.debug("Il file del flusso non è stato trovato!");
			e.printStackTrace();
		}
		log.debug("Errore!!! Il file del flusso è nullo.");
		return null;
	}
}
