package com.seda.payer.ottico.facade.core;

import java.io.Serializable;
import java.util.Date;



import com.seda.commons.logger.LoggerWrapper;
/**
 * @author aniello.labua
 */
public class OT1FlowData extends TracciatiOtticoInfo implements Serializable{
	
	private LoggerWrapper log;
	private static final long serialVersionUID = 1L;

	private String documento;
	private int chiaveQuietanza;
	private String numeroBollettino;
	private String numeroQuietanza;
	private String numeroRelata;
	private String nomeFisicoImg;
	
	private boolean hasImage;
	
	public OT1FlowData(String documento,
			int chiaveQuietanza, String numeroBollettino,
			String numeroQuietanza, String numeroRelata, String nomeFisicoImg) {
		
		this.documento = documento;
		this.chiaveQuietanza = chiaveQuietanza;
		this.numeroBollettino = numeroBollettino;
		this.numeroQuietanza = numeroQuietanza;
		this.numeroRelata = numeroRelata;
		this.nomeFisicoImg = nomeFisicoImg;
	}
	
	public OT1FlowData(String stringFlow, LoggerWrapper aLog){
		this.log = aLog;
		log.debug("Lunghezza flusso: " + stringFlow.length());
		super.setFlowType(stringFlow.substring(0,3).trim());
		super.setFlowCreationData(stringFlow.substring(3,13).trim());
		super.setCodiceEnte(stringFlow.substring(13,18).trim());
		super.setDocType(stringFlow.substring(18,21).trim());
		this.documento = stringFlow.substring(21,41).trim();
		try {
		    this.chiaveQuietanza = Integer.parseInt(stringFlow.substring(41,50));
		} catch (Exception ex){
			log.debug("Chiave quietanza non numerica, impostata a zero");
			this.chiaveQuietanza = 0; 
		}
		this.numeroBollettino = stringFlow.substring(50, 68).trim();
		this.numeroQuietanza = stringFlow.substring(68, 118).trim();
		this.numeroRelata = stringFlow.substring(118, 130).trim();
		this.nomeFisicoImg = stringFlow.substring(130).trim();
		this.hasImage = true;
		log.debug("OGGETTO OT1 COSTRUITO: " + this.toString());
		
	}

	public String getDocumento() {
		return documento;
	}

	public OT1FlowData setDocumento(String documento) {
		this.documento = documento;
		return this;
	}

	public int getChiaveQuietanza() {
		return chiaveQuietanza;
	}

	public OT1FlowData setChiaveQuietanza(int chiaveQuietanza) {
		this.chiaveQuietanza = chiaveQuietanza;
		return this;
	}

	public String getNumeroBollettino() {
		return numeroBollettino;
	}

	public OT1FlowData setNumeroBollettino(String numeroBollettino) {
		this.numeroBollettino = numeroBollettino;
		return this;
	}

	public String getNumeroQuietanza() {
		return numeroQuietanza;
	}

	public OT1FlowData setNumeroQuietanza(String numeroQuietanza) {
		this.numeroQuietanza = numeroQuietanza;
		return this;
	}

	public String getNumeroRelata() {
		return numeroRelata;
	}

	public OT1FlowData setNumeroRelata(String numeroRelata) {
		this.numeroRelata = numeroRelata;
		return this;
	}

	public String getNomeFisicoImg() {
		return nomeFisicoImg;
	}

	public OT1FlowData setNomeFisicoImg(String nomeFisicoImg) {
		this.nomeFisicoImg = nomeFisicoImg;
		return this;
	}

	public boolean isHasImage() {
		return hasImage;
	}

	public void setHasImage(boolean hasImage) {
		this.hasImage = hasImage;
	}

	public FlowMessages validate(OT0FlowData OT0Data, int lineNumber) {
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
			
			Date OT1Date =java.sql.Date.valueOf(getFlowCreationData());
			Date OT0Date = java.sql.Date.valueOf(OT0Data.getFlowCreationData());
			
			if(!OT1Date.equals(OT0Date))
				messages.addMessage("Record : " + lineNumber + ". La data " + getFlowCreationData() + " presente nel record OT1 non corrisponde alla data " + OT0Data.getFlowCreationData().trim() + " presente nel record OT0");
			
		} catch (Exception e) {
			messages.addMessage("Record : " + lineNumber + ". La data produzione flusso indicata nel record OT1 deve essere una data valida");
		}
		
		/*CONTROLLO CODICE ENTE*/
		if(!this.getCodiceEnte().trim().equals(OT0Data.getCodiceEnte().trim()))
				messages.addMessage("Record : " + lineNumber + ". Il codice ente " + getCodiceEnte().trim() + " presente nel record OT1 è diverso dal codice ente " + OT0Data.getCodiceEnte().trim() + " presente nel record OT0");
		
		/*CONTROLLO TIPO DOCUMENTO*/
		if(!this.getDocType().trim().equals(OT0Data.getDocType().trim()))
			messages.addMessage("Record : " + lineNumber + ". Il tipo documento " + getDocType().trim() + " presente nel record OT1 è diverso dal tipo documento " + OT0Data.getDocType().trim() + " presente nel record OT0");

		/*CONTROLLO DOCUMENTO*/
		if(this.getDocumento().trim().equals("")||this.getDocumento()==null)
			messages.addMessage("Record : " + lineNumber + ". Il campo documento presente nel record OT1 non è valorizzato");
		
		/*CONTROLLO CHIAVE QUIETANZA*/
		if((this.getDocType().trim().equals("BOL")||this.getDocType().trim().equals("QUI"))&&(this.getChiaveQuietanza()==0))
			messages.addMessage("Record : " + lineNumber + ". In presenza di tipo documento BOL o QUI, il campo chiave quietanza presente nel record OT1 deve essere valorizzato");
		
		if(!(this.getChiaveQuietanza()==0)&&!this.getDocType().trim().equals("BOL")&&!this.getDocType().trim().equals("QUI"))
			messages.addMessage("Record : " + lineNumber + ". In presenza di tipo documento diverso da BOL e QUI, il campo chiave quietanza presente nel record OT1 non deve essere valorizzato");
		
		/*CONTROLLO NUMERO BOLLETTINO*/
		//if(this.getNumeroBollettino().trim().equals("")||this.getNumeroBollettino()==null)
		if(this.getDocType().equals("BOL")&&(this.getNumeroBollettino().trim().equals("")||this.getNumeroBollettino()==null))
			messages.addMessage("Record : " + lineNumber + ". In presenza di tipo documento BOL, il campo numero bollettino presente nel record OT1 deve essere valorizzato");

		if(!this.getNumeroBollettino().trim().equals("")&&this.getNumeroBollettino()!=null&&!this.getDocType().equals("BOL"))
			messages.addMessage("Record : " + lineNumber + ". In presenza di tipo documento diverso da BOL, il campo numero bollettino presente nel record OT1 non deve essere valorizzato");

		/*CONTROLLO NUMERO QUIETANZA*/
		if(this.getDocType().equals("QUI")&&(this.getNumeroQuietanza().trim().equals("")||this.getNumeroQuietanza()==null))
			messages.addMessage("Record : " + lineNumber + ". In presenza di tipo documento QUI, il campo numero quietanza presente nel record OT1 deve essere valorizzato");
		
		if(!this.getNumeroQuietanza().trim().equals("")&&this.getNumeroQuietanza()!=null&&!this.getDocType().equals("QUI"))
			messages.addMessage("Record : " + lineNumber + ". In presenza di tipo documento diverso da QUI, il campo numero quietanza presente nel record OT1 non deve essere valorizzato");
		
		/*CONTROLLO NUMERO RELATA*/
		if(this.getDocType().trim().equals("REL")&&(this.getNumeroRelata().trim().equals("")||this.getNumeroRelata()==null))
			messages.addMessage("Record : " + lineNumber + ". In presenza di tipo documento REL, il campo numero relata presente nel record OT1 deve essere valorizzato");
		
		if(!this.getNumeroRelata().trim().equals("")&&this.getNumeroRelata()!=null&&!this.getDocType().trim().equals("REL"))
			messages.addMessage("Record : " + lineNumber + ". In presenza di tipo documento diverso da REL, il campo numero relata presente nel record OT1 non deve essere valorizzato");
		
		/*CONTROLLO NOME FISICO IMG*/
		if(this.getNomeFisicoImg().trim().equals("")||this.getNomeFisicoImg()==null){
			messages.addMessage("Record : " + lineNumber + ". Il campo nome fisico immagini non è valorizzato");
			this.hasImage = false;
			}

		return messages;
	}
	
	public String toString() {
		return "OT1FlowData [chiaveQuietanza=" + chiaveQuietanza
				+ ", documento=" + documento + ", nomeFisicoImg="
				+ nomeFisicoImg + ", numeroBollettino=" + numeroBollettino
				+ ", numeroQuietanza=" + numeroQuietanza + ", numeroRelata="
				+ numeroRelata + ", tracciatoInfo=" + super.toString() + "]";
	}
	
}
