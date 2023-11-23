package com.seda.payer.ottico.facade.core;

import java.io.Serializable;



import com.seda.commons.logger.LoggerWrapper;
import com.seda.payer.core.bean.Configurazione;
/**
 * @author aniello.labua
 */
public class OT0FlowData extends TracciatiOtticoInfo implements Serializable {

	private LoggerWrapper log;
	private static final long serialVersionUID = 1L;
	private String flussoImg;

	public OT0FlowData(String stringFlow, LoggerWrapper aLog){
		this.log = aLog;
		log.debug("Lunghezza flusso: " + stringFlow.length());
		super.setFlowType(stringFlow.substring(0,3).trim());
		super.setFlowCreationData(stringFlow.substring(3,13).trim());
		super.setCodiceEnte(stringFlow.substring(13,18).trim());
		super.setDocType(stringFlow.substring(18,21).trim());
		this.flussoImg = stringFlow.substring(21,71).trim();
		log.debug("OGGETTO OT0 COSTRUITO: " + this.toString());
	}

	public String getFlussoImg() {
		return flussoImg;
	}

	public OT0FlowData setFlussoImg(String flussoImg) {
		this.flussoImg = flussoImg;
		return this;
	}

	public FlowMessages validate(Configurazione currentConfig, int lineNumber) {
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
		try { java.sql.Date.valueOf(getFlowCreationData());
		} catch (Exception e) {
			messages.addMessage("Record : " + lineNumber + ". La data produzione flusso indicata nel record OT0 deve essere una data valida");
		}
		
		/*CONTROLLO CODICE ENTE*/
		if(!this.getCodiceEnte().trim().equals(currentConfig.getCodiceEnte())){
			messages.addMessage("Record : " + lineNumber + ". Il codice ente indicato nel record OT0 non appartiene ad un ente valido");
		}
		
		/*CONTROLLO TIPOLOGIA DOCUMENTO*/
		if(!this.getDocType().trim().equals("DOC")&&!this.getDocType().trim().equals("REL")&&!this.getDocType().trim().equals("BOL"))
			messages.addMessage("Record : " + lineNumber + ". La tipologia documento indicata nel record OT0 deve assumere valore DOC ( Documenti ) o REL ( Relate ) o BOL ( Bollettini )");
		
		/**/
		if(this.flussoImg.trim().equals("")||this.flussoImg==null)
			messages.addMessage("Record : " + lineNumber + ". Nel record OT0 non è valorizzato il campo relativo al flussso immagini");
		
		/* ALTRI CONTROLLI DI VALIDAZIONE NELLO STESSO 
		 * (OVVIAMENTE NON CON IL TRY & CATCH MA CON GLI IF SE NON SI TRATTA DI UNA DATA) */

		return messages;
	}
	
	public String toString() {
		return "OT0FlowData [flussoImg=" + flussoImg + ", tracciatoInfo="
		+ super.toString() + "]";
	}

}
