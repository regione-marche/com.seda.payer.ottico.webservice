package com.seda.payer.ottico.facade.pdf;

import java.io.Serializable;

public class DocumentoType implements Serializable {

	private static final long serialVersionUID = 1L;
	private String annoEmissione;
	private String numeroEmissione;
	private String idDoc;
	private String idCartella;
	private String tipologiaServizio;
	private String descTipologia;
	private String tipoDocumento;
	private String numeroBollettino;
	private String denominazione;
	private String impostaServizio;
	private String descImpServizio;
	private String descrizioneEnte;
	private String impInizialeDocumento;
	
	/*Dettaglio documento*/
	private String impTributiIniziali;
	private String impDriNotificaIniziale;
	private String impComRiscIniziale;
	private String impIntMoraIniziale;
	private String impAltreSpeseIniziale;
	private String impTotaleDocIniziale;

	public DocumentoType() {
		this.annoEmissione = "";
		this.numeroEmissione = "";
		this.idDoc = "";
		this.idCartella = "";
		this.tipologiaServizio = "";
		this.descTipologia = "";
		this.tipoDocumento = "";
		this.numeroBollettino = "";
		this.denominazione = "";
		this.impostaServizio = "";
		this.descImpServizio = "";
		this.descrizioneEnte = "";
		this.impInizialeDocumento = "";
		this.impTributiIniziali = "";
		this.impDriNotificaIniziale = "";
		this.impComRiscIniziale = "";
		this.impIntMoraIniziale = "";
		this.impAltreSpeseIniziale = "";
		this.impTotaleDocIniziale = "";
	}

	public DocumentoType(String annoEmissione, String numeroEmissione,
			String idDoc, String idCartella, String tipologiaServizio,
			String descTipologia, String tipoDocumento,
			String numeroBollettino, String denominazione,
			String impostaServizio, String descImpServizio,
			String descrizioneEnte, String impInizialeDocumento,
			String impTributiIniziali, String impDriNotificaIniziale,
			String impComRiscIniziale, String impIntMoraIniziale,
			String impAltreSpeseIniziale, String impTotaleDocIniziale) {
		super();
		this.annoEmissione = annoEmissione;
		this.numeroEmissione = numeroEmissione;
		this.idDoc = idDoc;
		this.idCartella = idCartella;
		this.tipologiaServizio = tipologiaServizio;
		this.descTipologia = descTipologia;
		this.tipoDocumento = tipoDocumento;
		this.numeroBollettino = numeroBollettino;
		this.denominazione = denominazione;
		this.impostaServizio = impostaServizio;
		this.descImpServizio = descImpServizio;
		this.descrizioneEnte = descrizioneEnte;
		this.impInizialeDocumento = impInizialeDocumento;
		this.impTributiIniziali = impTributiIniziali;
		this.impDriNotificaIniziale = impDriNotificaIniziale;
		this.impComRiscIniziale = impComRiscIniziale;
		this.impIntMoraIniziale = impIntMoraIniziale;
		this.impAltreSpeseIniziale = impAltreSpeseIniziale;
		this.impTotaleDocIniziale = impTotaleDocIniziale;
	}

	/**
	 * @return the annoEmissione
	 */
	public String getAnnoEmissione() {
		return annoEmissione;
	}

	/**
	 * @param annoEmissione the annoEmissione to set
	 */
	public void setAnnoEmissione(String annoEmissione) {
		this.annoEmissione = annoEmissione;
	}

	/**
	 * @return the numeroEmissione
	 */
	public String getNumeroEmissione() {
		return numeroEmissione;
	}

	/**
	 * @param numeroEmissione the numeroEmissione to set
	 */
	public void setNumeroEmissione(String numeroEmissione) {
		this.numeroEmissione = numeroEmissione;
	}

	/**
	 * @return the idDoc
	 */
	public String getIdDoc() {
		return idDoc;
	}

	/**
	 * @param idDoc the idDoc to set
	 */
	public void setIdDoc(String idDoc) {
		this.idDoc = idDoc;
	}

	/**
	 * @return the idCartella
	 */
	public String getIdCartella() {
		return idCartella;
	}

	/**
	 * @param idCartella the idCartella to set
	 */
	public void setIdCartella(String idCartella) {
		this.idCartella = idCartella;
	}

	/**
	 * @return the tipologiaServizio
	 */
	public String getTipologiaServizio() {
		return tipologiaServizio;
	}

	/**
	 * @param tipologiaServizio the tipologiaServizio to set
	 */
	public void setTipologiaServizio(String tipologiaServizio) {
		this.tipologiaServizio = tipologiaServizio;
	}

	/**
	 * @return the descTipologia
	 */
	public String getDescTipologia() {
		return descTipologia;
	}

	/**
	 * @param descTipologia the descTipologia to set
	 */
	public void setDescTipologia(String descTipologia) {
		this.descTipologia = descTipologia;
	}

	/**
	 * @return the tipoDocumento
	 */
	public String getTipoDocumento() {
		return tipoDocumento;
	}

	/**
	 * @param tipoDocumento the tipoDocumento to set
	 */
	public void setTipoDocumento(String tipoDocumento) {
		this.tipoDocumento = tipoDocumento;
	}

	/**
	 * @return the numeroBollettino
	 */
	public String getNumeroBollettino() {
		return numeroBollettino;
	}

	/**
	 * @param numeroBollettino the numeroBollettino to set
	 */
	public void setNumeroBollettino(String numeroBollettino) {
		this.numeroBollettino = numeroBollettino;
	}

	/**
	 * @return the denominazione
	 */
	public String getDenominazione() {
		return denominazione;
	}

	/**
	 * @param denominazione the denominazione to set
	 */
	public void setDenominazione(String denominazione) {
		this.denominazione = denominazione;
	}

	/**
	 * @return the impostaServizio
	 */
	public String getImpostaServizio() {
		return impostaServizio;
	}

	/**
	 * @param impostaServizio the impostaServizio to set
	 */
	public void setImpostaServizio(String impostaServizio) {
		this.impostaServizio = impostaServizio;
	}

	/**
	 * @return the descImpServizio
	 */
	public String getDescImpServizio() {
		return descImpServizio;
	}

	/**
	 * @param descImpServizio the descImpServizio to set
	 */
	public void setDescImpServizio(String descImpServizio) {
		this.descImpServizio = descImpServizio;
	}

	/**
	 * @return the descrizioneEnte
	 */
	public String getDescrizioneEnte() {
		return descrizioneEnte;
	}

	/**
	 * @param descrizioneEnte the descrizioneEnte to set
	 */
	public void setDescrizioneEnte(String descrizioneEnte) {
		this.descrizioneEnte = descrizioneEnte;
	}

	/**
	 * @return the impInizialeDocumento
	 */
	public String getImpInizialeDocumento() {
		return impInizialeDocumento;
	}

	/**
	 * @param impInizialeDocumento the impInizialeDocumento to set
	 */
	public void setImpInizialeDocumento(String impInizialeDocumento) {
		this.impInizialeDocumento = impInizialeDocumento;
	}

	/**
	 * @return the impTributiIniziali
	 */
	public String getImpTributiIniziali() {
		return impTributiIniziali;
	}

	/**
	 * @param impTributiIniziali the impTributiIniziali to set
	 */
	public void setImpTributiIniziali(String impTributiIniziali) {
		this.impTributiIniziali = impTributiIniziali;
	}

	/**
	 * @return the impDriNotificaIniziale
	 */
	public String getImpDriNotificaIniziale() {
		return impDriNotificaIniziale;
	}

	/**
	 * @param impDriNotificaIniziale the impDriNotificaIniziale to set
	 */
	public void setImpDriNotificaIniziale(String impDriNotificaIniziale) {
		this.impDriNotificaIniziale = impDriNotificaIniziale;
	}

	/**
	 * @return the impComRiscIniziale
	 */
	public String getImpComRiscIniziale() {
		return impComRiscIniziale;
	}

	/**
	 * @param impComRiscIniziale the impComRiscIniziale to set
	 */
	public void setImpComRiscIniziale(String impComRiscIniziale) {
		this.impComRiscIniziale = impComRiscIniziale;
	}

	/**
	 * @return the impIntMoraIniziale
	 */
	public String getImpIntMoraIniziale() {
		return impIntMoraIniziale;
	}

	/**
	 * @param impIntMoraIniziale the impIntMoraIniziale to set
	 */
	public void setImpIntMoraIniziale(String impIntMoraIniziale) {
		this.impIntMoraIniziale = impIntMoraIniziale;
	}

	/**
	 * @return the impAltreSpeseIniziale
	 */
	public String getImpAltreSpeseIniziale() {
		return impAltreSpeseIniziale;
	}

	/**
	 * @param impAltreSpeseIniziale the impAltreSpeseIniziale to set
	 */
	public void setImpAltreSpeseIniziale(String impAltreSpeseIniziale) {
		this.impAltreSpeseIniziale = impAltreSpeseIniziale;
	}

	/**
	 * @return the impTotaleDocIniziale
	 */
	public String getImpTotaleDocIniziale() {
		return impTotaleDocIniziale;
	}

	/**
	 * @param impTotaleDocIniziale the impTotaleDocIniziale to set
	 */
	public void setImpTotaleDocIniziale(String impTotaleDocIniziale) {
		this.impTotaleDocIniziale = impTotaleDocIniziale;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	public String toString() {
		return "DocumentoType [annoEmissione=" + annoEmissione
				+ ", denominazione=" + denominazione + ", descImpServizio="
				+ descImpServizio + ", descTipologia=" + descTipologia
				+ ", descrizioneEnte=" + descrizioneEnte + ", idCartella="
				+ idCartella + ", idDoc=" + idDoc + ", impAltreSpeseIniziale="
				+ impAltreSpeseIniziale + ", impComRiscIniziale="
				+ impComRiscIniziale + ", impDriNotificaIniziale="
				+ impDriNotificaIniziale + ", impInizialeDocumento="
				+ impInizialeDocumento + ", impIntMoraIniziale="
				+ impIntMoraIniziale + ", impTotaleDocIniziale="
				+ impTotaleDocIniziale + ", impTributiIniziali="
				+ impTributiIniziali + ", impostaServizio=" + impostaServizio
				+ ", numeroBollettino=" + numeroBollettino
				+ ", numeroEmissione=" + numeroEmissione + ", tipoDocumento="
				+ tipoDocumento + ", tipologiaServizio=" + tipologiaServizio
				+ "]";
	}

}
