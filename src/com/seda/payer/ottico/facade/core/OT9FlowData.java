package com.seda.payer.ottico.facade.core;

import java.io.Serializable;
import java.util.Date;

import org.apache.log4j.Logger;
/**
 * @author aniello.labua
 */
public class OT9FlowData extends TracciatiOtticoInfo implements Serializable{

	private Logger log;

	private static final long serialVersionUID = 1L;
	private String flussoImg;
	private String record;


	public OT9FlowData (String stringFlow, Logger aLog){
		this.log = aLog;
		log.debug("Lunghezza flusso: " + stringFlow.length());
		super.setFlowType(stringFlow.substring(0,3).trim());
		super.setFlowCreationData(stringFlow.substring(3,13).trim());
		super.setCodiceEnte(stringFlow.substring(13,18).trim());
		super.setDocType(stringFlow.substring(18,21).trim());
		this.flussoImg = stringFlow.substring(21,71).trim();
		this.record = stringFlow.substring(71).trim();

		log.debug("OGGETTO OT9 COSTRUITO: " + this.toString());
	}


	public String getFlussoImg() {
		return flussoImg;
	}


	public void setFlussoImg(String flussoImg) {
		this.flussoImg = flussoImg;
	}


	public String getRecord() {
		return record;
	}


	public void setRecord(String record) {
		this.record = record;
	}

	public FlowMessages validate(OT0FlowData OT0Data, FlowInfo flowInfo, int lineNumber) {
		/**
		 * QUI CI METTIAMO TUTTI I CONTROLLI SUI DATI PRESENTI NEL RECORD IN QUESTIONE:
		 * - il campo Data produzione flusso deve essere una data valida
		 * - il campo Codice Ente deve essere un ente valido e presente nella tabella PYANETB accedendo 
		 *   con il campo ANE_CANECENT impostato con il campo "Codice Ente" del record OT0 ed i 
		 *   campi ANE_TANETUFF e ANE_CANECUFF a spazio e deve coincidere con il valore del campo CFO_KANEKENT.
		 * - il campo Tipologia Documenti deve assumere valore DOC ( Documenti ) o REL ( Relate ) o BOL ( Bollettini )
		 * - il campo Flusso Immagini deve essere valorizzato
		 * 
		 * ETC...
		 */
		FlowMessages messages = new FlowMessages();
		/* CONTROLLO DI VALIDAZIONE DATA */
		try { 

			Date OT9Date =java.sql.Date.valueOf(getFlowCreationData());
			Date OT0Date = java.sql.Date.valueOf(OT0Data.getFlowCreationData().trim());

			if(!OT9Date.equals(OT0Date))
				messages.addMessage("Record : " + lineNumber + ". La data " + getFlowCreationData() + " presente nel record OT9 non corrisponde alla data " + OT0Data.getFlowCreationData().trim() + " presente nel record OT0");

		} catch (Exception e) {
			messages.addMessage("Record : " + lineNumber + ". La data produzione flusso indicata nel record OT9 deve essere una data valida");
		}

		/*CONTROLLO CODICE ENTE*/
		if(!this.getCodiceEnte().trim().equals(OT0Data.getCodiceEnte().trim()))
			messages.addMessage("Record : " + lineNumber + ". Il codice ente " + getCodiceEnte().trim() + " presente nel record OT9 è diverso dal codice ente " + OT0Data.getCodiceEnte().trim() + " presente nel record OT0");

		/*CONTROLLO TIPO DOCUMENTO*/
		if(!this.getDocType().trim().equals(OT0Data.getDocType().trim()))
			messages.addMessage("Record : " + lineNumber + ". Il tipo documento " + getDocType().trim() + " presente nel record OT9 è diverso dal tipo documento " + OT0Data.getDocType().trim() + " presente nel record OT0");

		/*CONTROLLO NUMERO RECORD*/
		int sum = flowInfo.getNumberOfOT0()+flowInfo.getNumberOfOT1()+flowInfo.getNumberOfOT9();
		
		log.debug("SOMMA = " + sum + " - RECORD  IN OT9: " + Integer.parseInt(this.getRecord().trim()) + 
				"OT0: " +  flowInfo.getNumberOfOT0() + "OT1 " +  flowInfo.getNumberOfOT1() + "OT9" +  flowInfo.getNumberOfOT9() );
		if(Integer.parseInt(this.getRecord().trim())!=sum)
			messages.addMessage("Record : " + lineNumber + ". Il totale del numero dei record indicati nel record OT9 non corrisponde alla somma dei record presenti nel flusso");
		
		return messages;
	}

	public String toString() {
		return "OT9FlowData [flussoImg=" + flussoImg + ", record=" + record
		+ ", tracciatoInfo=" + super.toString() +"]";
	}

}
