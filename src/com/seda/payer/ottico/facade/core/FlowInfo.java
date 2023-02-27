package com.seda.payer.ottico.facade.core;

import java.io.Serializable;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import com.seda.payer.core.bean.Configurazione;
/**
 * @author aniello.labua
 */
public class FlowInfo implements Serializable {

	private static final long serialVersionUID = 1L;
	//we define flow record info
	private int fileLinesNumber;
	private int numberOfOT0;
	private int numberOfOT1;
	private int numberOfOT9;
	//we define flow data info
	private String nomeFileDati;
	private String nomeFileImg;
	//we define head and tail record info
	private OT0FlowData OT0Data;
	private OT9FlowData OT9Data;
	//
	private int okImgRecord;
	private int koImgRecord;
	//
	private Configurazione currentConfig;
	private Date startDate;
	/**/
	private String elabPath;
	private String elabImagePath;
	private String nomeFileDatiZip;
	private String tipologiaDocumenti;	


	public FlowInfo(int fileLinesNumber, int numberOfOT0, int numberOfOT1,
			int numberOfOT9, String nomeFileDati, String nomeFileImg, OT0FlowData OT0Data,
			OT9FlowData OT9Data, int okImgRecord, int koImgRecord,
			Configurazione currentConfig, Date startDate, String elabPath, String elabImagePath,
			String nomeFileDatiZip, String tipologiaDocumenti) {
		super();
		this.fileLinesNumber = fileLinesNumber;
		this.numberOfOT0 = numberOfOT0;
		this.numberOfOT1 = numberOfOT1;
		this.numberOfOT9 = numberOfOT9;
		this.nomeFileDati = nomeFileDati;
		this.nomeFileImg = nomeFileImg;
		this.OT0Data = OT0Data;
		this.OT9Data = OT9Data;
		this.okImgRecord = okImgRecord;
		this.koImgRecord = koImgRecord;
		this.currentConfig = currentConfig;
		this.startDate = startDate;
		/**/
		this.elabPath = elabPath;
		this.elabImagePath = elabImagePath;
		this.nomeFileDatiZip = nomeFileDatiZip;
		this.tipologiaDocumenti = tipologiaDocumenti;
	}

	public int getFileLinesNumber() {
		return fileLinesNumber;
	}

	public FlowInfo setFileLinesNumber(int lineNumber) {
		this.fileLinesNumber = lineNumber;
		return this;
	}

	public int getNumberOfOT0() {
		return numberOfOT0;
	}

	public FlowInfo setNumberOfOT0(int numberOfOT0) {
		this.numberOfOT0 = numberOfOT0;
		return this;
	}

	public int getNumberOfOT1() {
		return numberOfOT1;
	}

	public FlowInfo setNumberOfOT1(int numberOfOT1) {
		this.numberOfOT1 = numberOfOT1;
		return this;
	}

	public int getNumberOfOT9() {
		return numberOfOT9;
	}

	public FlowInfo setNumberOfOT9(int numberOfOT9) {
		this.numberOfOT9 = numberOfOT9;
		return this;
	}

	public OT0FlowData getOT0Data() {
		return OT0Data;
	}

	public FlowInfo setOT0Data(OT0FlowData oT0Data) {
		OT0Data = oT0Data;
		return this;
	}

	public OT9FlowData getOT9Data() {
		return OT9Data;
	}

	public FlowInfo setOT9Data(OT9FlowData oT9Data) {
		OT9Data = oT9Data;
		return this;
	}

	public String getNomeFileImg() {
		return nomeFileImg;
	}

	public FlowInfo setNomeFileImg(String nomeFileImg) {
		this.nomeFileImg = nomeFileImg;
		return this;
	}
	
	public String getNomeFileDati() {
		return nomeFileDati;
	}

	public FlowInfo setNomeFileDati(String nomeFileDati) {
		this.nomeFileDati = nomeFileDati;
		return this;
	}

	public Configurazione getCurrentConfig() {
		return currentConfig;
	}

	public FlowInfo setCurrentConfig(Configurazione currentConfig) {
		this.currentConfig = currentConfig;
		return this;
	}
	
	public int getOkImgRecord() {
		return okImgRecord;
	}

	public FlowInfo setOkImgRecord(int okImgRecord) {
		this.okImgRecord = okImgRecord;
		return this;
	}

	public int getKoImgRecord() {
		return koImgRecord;
	}

	public FlowInfo setKoImgRecord(int koImgRecord) {
		this.koImgRecord = koImgRecord;
		return this;
	}

	public Date getStartDate() {
		return startDate;
	}
	/**/
	public String getITAFormattedDate(){
		  DateFormat formatter = new SimpleDateFormat("E, dd MMM yyyy hh:mm:ss", Locale.ITALY);
		  String itaDate = formatter.format(this.startDate);
		  return itaDate;
	}

	public FlowInfo setStartDate(Date startDate) {
		this.startDate = startDate;
		return this;
	}
	
	public String getElabPath() {
		return elabPath;
	}

	public void setElabPath(String elabPath) {
		this.elabPath = elabPath;
	}

	public String getNomeFileDatiZip() {
		return nomeFileDatiZip;
	}

	public void setNomeFileDatiZip(String nomeFileDatiZip) {
		this.nomeFileDatiZip = nomeFileDatiZip;
	}

	public String getElabImagePath() {
		return elabImagePath;
	}

	public void setElabImagePath(String elabImagePath) {
		this.elabImagePath = elabImagePath;
	}
	
	public String getTipologiaDocumenti() {
		return tipologiaDocumenti;
	}

	public void setTipologiaDocumenti(String tipologiaDocumenti) {
		this.tipologiaDocumenti = tipologiaDocumenti;
	}

	public String toString() {
		return "FlowInfo [OT0Data=" + OT0Data + ", OT9Data=" + OT9Data
				+ ", currentConfig=" + currentConfig + ", elabPath=" + elabPath
				+ ", elabImagePath=" + elabImagePath + ", fileLinesNumber=" + fileLinesNumber + 
				", okImgRecord=" + okImgRecord +	", koImgRecord="
				+ koImgRecord + ", nomeFileDati=" + nomeFileDati
				+ ", nomeFileDatiZip=" + nomeFileDatiZip + ", nomeFileImg="
				+ nomeFileImg + ", numberOfOT0=" + numberOfOT0
				+ ", numberOfOT1=" + numberOfOT1 + ", numberOfOT9="
				+ numberOfOT9 + ", okImgRecord=" + okImgRecord + ", startDate="
				+ startDate + ", tipologiaDocumenti=" + tipologiaDocumenti + "]";
	}
}