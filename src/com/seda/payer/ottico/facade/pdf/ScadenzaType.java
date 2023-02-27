package com.seda.payer.ottico.facade.pdf;

import java.io.Serializable;

public class ScadenzaType implements Serializable {

	private static final long serialVersionUID = 1L;
	
	private String progressivoRata;
    private String dataScadenza;
    private String codBollettino;
    private String impRataIniziale;
    private String impRataResiduoTributi;
    private String impRataResiduoNotifica;
    private String impRataResiduoComp;
    private String impRataResiduoIntMora;
    private String impTotaleResiduo;
    private String tipoRata;
    private String presenzaPagamentoInAcquisizione;
    private  String rataScaduta;

    public ScadenzaType() {
		this.progressivoRata = "";
		this.dataScadenza = "";
		this.codBollettino = "";
		this.impRataIniziale = "";
		this.impRataResiduoTributi = "";
		this.impRataResiduoNotifica = "";
		this.impRataResiduoComp = "";
		this.impRataResiduoIntMora = "";
		this.impTotaleResiduo = "";
		this.tipoRata = "";
		this.presenzaPagamentoInAcquisizione = "";
		this.rataScaduta = "";
    }

	public ScadenzaType(String progressivoRata, String dataScadenza,
			String codBollettino, String impRataIniziale,
			String impRataResiduoTributi, String impRataResiduoNotifica,
			String impRataResiduoComp, String impRataResiduoIntMora,
			String impTotaleResiduo, String tipoRata,
			String presenzaPagamentoInAcquisizione, String rataScaduta) {
		super();
		this.progressivoRata = progressivoRata;
		this.dataScadenza = dataScadenza;
		this.codBollettino = codBollettino;
		this.impRataIniziale = impRataIniziale;
		this.impRataResiduoTributi = impRataResiduoTributi;
		this.impRataResiduoNotifica = impRataResiduoNotifica;
		this.impRataResiduoComp = impRataResiduoComp;
		this.impRataResiduoIntMora = impRataResiduoIntMora;
		this.impTotaleResiduo = impTotaleResiduo;
		this.tipoRata = tipoRata;
		this.presenzaPagamentoInAcquisizione = presenzaPagamentoInAcquisizione;
		this.rataScaduta = rataScaduta;
	}
	
	/**
	 * @return the progressivoRata
	 */
	public String getProgressivoRata() {
		return progressivoRata;
	}
	/**
	 * @param progressivoRata the progressivoRata to set
	 */
	public void setProgressivoRata(String progressivoRata) {
		this.progressivoRata = progressivoRata;
	}
	/**
	 * @return the dataScadenza
	 */
	public String getDataScadenza() {
		return dataScadenza;
	}
	/**
	 * @param dataScadenza the dataScadenza to set
	 */
	public void setDataScadenza(String dataScadenza) {
		this.dataScadenza = dataScadenza;
	}
	/**
	 * @return the codBollettino
	 */
	public String getCodBollettino() {
		return codBollettino;
	}
	/**
	 * @param codBollettino the codBollettino to set
	 */
	public void setCodBollettino(String codBollettino) {
		this.codBollettino = codBollettino;
	}
	/**
	 * @return the impRataIniziale
	 */
	public String getImpRataIniziale() {
		return impRataIniziale;
	}
	/**
	 * @param impRataIniziale the impRataIniziale to set
	 */
	public void setImpRataIniziale(String impRataIniziale) {
		this.impRataIniziale = impRataIniziale;
	}
	/**
	 * @return the impRataResiduoTributi
	 */
	public String getImpRataResiduoTributi() {
		return impRataResiduoTributi;
	}
	/**
	 * @param impRataResiduoTributi the impRataResiduoTributi to set
	 */
	public void setImpRataResiduoTributi(String impRataResiduoTributi) {
		this.impRataResiduoTributi = impRataResiduoTributi;
	}
	/**
	 * @return the impRataResiduoNotifica
	 */
	public String getImpRataResiduoNotifica() {
		return impRataResiduoNotifica;
	}
	/**
	 * @param impRataResiduoNotifica the impRataResiduoNotifica to set
	 */
	public void setImpRataResiduoNotifica(String impRataResiduoNotifica) {
		this.impRataResiduoNotifica = impRataResiduoNotifica;
	}
	/**
	 * @return the impRataResiduoComp
	 */
	public String getImpRataResiduoComp() {
		return impRataResiduoComp;
	}
	/**
	 * @param impRataResiduoComp the impRataResiduoComp to set
	 */
	public void setImpRataResiduoComp(String impRataResiduoComp) {
		this.impRataResiduoComp = impRataResiduoComp;
	}
	/**
	 * @return the impRataResiduoIntMora
	 */
	public String getImpRataResiduoIntMora() {
		return impRataResiduoIntMora;
	}
	/**
	 * @param impRataResiduoIntMora the impRataResiduoIntMora to set
	 */
	public void setImpRataResiduoIntMora(String impRataResiduoIntMora) {
		this.impRataResiduoIntMora = impRataResiduoIntMora;
	}
	/**
	 * @return the impTotaleResiduo
	 */
	public String getImpTotaleResiduo() {
		return impTotaleResiduo;
	}
	/**
	 * @param impTotaleResiduo the impTotaleResiduo to set
	 */
	public void setImpTotaleResiduo(String impTotaleResiduo) {
		this.impTotaleResiduo = impTotaleResiduo;
	}
	/**
	 * @return the tipoRata
	 */
	public String getTipoRata() {
		return tipoRata;
	}
	/**
	 * @param tipoRata the tipoRata to set
	 */
	public void setTipoRata(String tipoRata) {
		this.tipoRata = tipoRata;
	}
	/**
	 * @return the presenzaPagamentoInAcquisizione
	 */
	public String getPresenzaPagamentoInAcquisizione() {
		return presenzaPagamentoInAcquisizione;
	}
	/**
	 * @param presenzaPagamentoInAcquisizione the presenzaPagamentoInAcquisizione to set
	 */
	public void setPresenzaPagamentoInAcquisizione(
			String presenzaPagamentoInAcquisizione) {
		this.presenzaPagamentoInAcquisizione = presenzaPagamentoInAcquisizione;
	}
	/**
	 * @return the rataScaduta
	 */
	public String getRataScaduta() {
		return rataScaduta;
	}
	/**
	 * @param rataScaduta the rataScaduta to set
	 */
	public void setRataScaduta(String rataScaduta) {
		this.rataScaduta = rataScaduta;
	}

	

}
