package com.seda.payer.ottico.facade.core;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;

import org.apache.log4j.Logger;

/**
 * @author aniello.labua
 */
public class FlowFileUtility {

	private Logger log;
	private String flowPath;
	private FlowParameter parameter;
	private FlowInfo flowInfo;

	public FlowFileUtility(FlowParameter parameter, Logger logger){
		this.log = logger;
		log.debug("\nTEST - FLOW FILE UTILITY START: " + parameter);
		this.flowPath = parameter.getPathCompletoNomeFileDati();
		this.parameter = parameter;

	}

	/*Inizilizza tutte le info relative al file:
	/*numero linee, numeroRecord, head, tail e path*/
	public void prepareData(String filePath) {

		try {
			FileInputStream fstream = new FileInputStream(filePath);
			DataInputStream in = new DataInputStream(fstream);
			BufferedReader br = new BufferedReader(new InputStreamReader(in));

			String currentLine;
			int lineNumber = 0;
			int flowOT0 = 0;
			int flowOT1 = 0;
			int flowOT9 = 0;
			//OT1 Image analisys
			int OT1OKImageCounter = 0;
			int OT1KOImageCounter = 0;
			/**/
			OT0FlowData head = null;
			OT9FlowData tail = null;

			while ((currentLine = br.readLine()) != null)   {
				lineNumber++;
				if(currentLine.startsWith("OT0")){
					flowOT0++;
					if(flowOT0<=1)
						head = new OT0FlowData(currentLine,log);
				}
				if(currentLine.startsWith("OT1")){
					flowOT1++;
					OT1FlowData data = new OT1FlowData(currentLine,log);
					log.debug("TEST: VERIFICA FILE IMG IN FILEINFO = " + this.parameter.getElabImagePath()+
							data.getNomeFisicoImg().trim());
					boolean result = new java.io.File(
							this.parameter.getElabImagePath()+
							data.getNomeFisicoImg().trim()).isFile();
					log.debug("TEST: IL BOOLEAN DI VERIFICA ESISTENZA FILE IMG E': " + result);
					if(result){
						log.debug("Il flusso contiene immagini.");
						OT1OKImageCounter++;
					}
						else{
							log.debug("ATTENZIONE!!! Il flusso non contiene immagini.");
							OT1KOImageCounter++;
						}
				}
				if(currentLine.startsWith("OT9")){
					flowOT9++;
					if(flowOT9<=1)
						tail = new OT9FlowData(currentLine,log);
				}
			}
			log.debug("TEST: COSTRUZIONE DELLE INFORMAZIONI PER PROCESSARE IL FLUSSO - FLOWINFO");
			this.flowInfo = new FlowInfo(lineNumber, flowOT0, flowOT1, flowOT9,
					this.parameter.getNomeFileDati(),
					this.parameter.getNomeFileImg(),head,tail,
					OT1OKImageCounter,OT1KOImageCounter,
					this.parameter.getCurrentConfig(),
					this.parameter.getStartDate(),
					this.parameter.getElabPath(),
					this.parameter.getElabImagePath(),
					this.parameter.getNomeFileDatiZip(),
					this.parameter.getTipologiaDocumenti());
			in.close();

		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public FlowInfo getFlowInfo(){
		return this.flowInfo;
	}

	public OT0FlowData getOT0Line(String filePath) throws IOException{
		DataInputStream in = null;
		try {
			FileInputStream fstream = new FileInputStream(filePath);
			in = new DataInputStream(fstream);
			BufferedReader br = new BufferedReader(new InputStreamReader(in));

			String currentLine;

			while ((currentLine = br.readLine()) != null)   {

				if(currentLine.startsWith("OT0")){
					//inizio LP PG21XX04 Leak
					br.close();
					//fine LP PG21XX04 Leak
					return new OT0FlowData(currentLine, this.log);

				}
			}

		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}finally{
			try { if (in != null) in.close(); 
			} catch (Exception ignore) { }
		}

		log.debug("Attenzione: Stai ritornando null!!!");
		return null;
	}

	public OT1FlowData getRelatedOT1(OT0FlowData OT0Data) throws IOException{
		DataInputStream in = null;
		OT1FlowData OT1;

		try {
			FileInputStream fstream = new FileInputStream(this.flowPath);
			in = new DataInputStream(fstream);
			BufferedReader br = new BufferedReader(new InputStreamReader(in));

			String currentLine;

			while ((currentLine = br.readLine()) != null)   {

				if(currentLine.startsWith("OT1")){
					OT1 = new OT1FlowData(currentLine,log);
					if(OT1.getFlowCreationData().equals(OT0Data.getFlowCreationData()) &&
							OT1.getCodiceEnte().equals(OT0Data.getCodiceEnte())&&
							OT1.getDocType().equals(OT0Data.getDocType()))

						in.close();
					//inizio LP PG21XX04 Leak
					br.close();
					//fine LP PG21XX04 Leak
					return OT1;
				}
			}

		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}finally{
			in.close();
		}

		log.debug("Attenzione: Stai ritornando null!!!");
		return null;
	}

	public String getFlowPath() {
		return flowPath;
	}

	public void setFlowPath(String flowPath) {
		this.flowPath = flowPath;
	}

	public String toString() {
		return "FlowFileUtility [flowInfo=" + flowInfo + ", flowPath="
		+ flowPath + ", parameter=" + parameter + "]";
	}

	/*MOD - INSERT TABLE*/
	public int flowType(String flowString){

		if(flowString.startsWith("OT0"))
			return 1;
		if(flowString.startsWith("OT1"))
			return 2;
		if(flowString.startsWith("OT9"))
			return 3;
		else return -1;

	}


}
