package com.seda.payer.ottico.facade.core;

import java.io.Serializable;
import java.util.Date;

import com.seda.payer.core.bean.Configurazione;
/**
 * @author aniello.labua
 */
public class FlowParameter implements Serializable{

	private static final long serialVersionUID = 1L;
	private String pathCompletoNomeFileDati;
	private String nomeFileDati;
	private String nomeFileImg;
	private Configurazione currentConfig;
	private Date startDate;
	/**/
	private String elabPath;
	private String elabImagePath;
	private String nomeFileDatiZip;
	private String tipologiaDocumenti;
	
	public FlowParameter(String pathCompletoNomeFileDati, String nomeFileDati,
			String nomeFileImg, Configurazione currentConfig, Date startDate, String elabPath, String elabImagePath,
			String nomeFileDatiZip, String tipologiaDocumenti) {
		super();
		this.pathCompletoNomeFileDati = pathCompletoNomeFileDati;
		this.nomeFileDati = nomeFileDati;
		this.nomeFileImg = nomeFileImg;
		this.currentConfig = currentConfig;
		this.startDate = startDate;
		/**/
		this.elabPath = elabPath;
		this.elabImagePath = elabImagePath;
		this.nomeFileDatiZip = nomeFileDatiZip;
		this.tipologiaDocumenti = tipologiaDocumenti;
	}

	public String getPathCompletoNomeFileDati() {
		return pathCompletoNomeFileDati;
	}

	public FlowParameter setPathCompletoNomeFileDati(String pathCompletoNomeFileDati) {
		this.pathCompletoNomeFileDati = pathCompletoNomeFileDati;
		return this;
	}

	public String getNomeFileDati() {
		return nomeFileDati;
	}

	public FlowParameter setNomeFileDati(String nomeFileDati) {
		this.nomeFileDati = nomeFileDati;
		return this;
	}

	public String getNomeFileImg() {
		return nomeFileImg;
	}

	public FlowParameter setNomeFileImg(String nomeFileImg) {
		this.nomeFileImg = nomeFileImg;
		return this;
	}

	public Configurazione getCurrentConfig() {
		return currentConfig;
	}

	public FlowParameter setCurrentConfig(Configurazione currentConfig) {
		this.currentConfig = currentConfig;
		return this;
	}

	public Date getStartDate() {
		return startDate;
	}

	public FlowParameter setStartDate(Date startDate) {
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
		return "FlowParameter [currentConfig=" + currentConfig + ", elabPath="
				+ elabPath + ", nomeFileDati=" + nomeFileDati
				+ ", nomeFileDatiZip=" + nomeFileDatiZip + ", nomeFileImg="
				+ nomeFileImg + ", pathCompletoNomeFileDati="
				+ pathCompletoNomeFileDati + ", startDate=" + startDate + ", tipologiaDocumenti=" + tipologiaDocumenti + "]";
	}
}