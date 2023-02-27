package com.seda.payer.ottico.facade.pdf;

import java.io.Serializable;

public class TributoType implements Serializable{

	private static final long serialVersionUID = 1L;
	private String codiceTributoNonAddizionali;
	private String annoTributo;
    private String progressivoTributo;
    private String tipoTributo;
    private String importoTributo;
    private String impPagatoConSgravi;
    private String noteTributo;
    
	public TributoType() {
		this.codiceTributoNonAddizionali = "";
		this.annoTributo = "";
		this.progressivoTributo = "";
		this.tipoTributo = "";
		this.importoTributo = "";
		this.impPagatoConSgravi = "";
		this.noteTributo = "";
	}

	public TributoType(String codiceTributoNonAddizionali, String annoTributo,
			String progressivoTributo, String tipoTributo,
			String importoTributo, String impPagatoConSgravi, String noteTributo) {
		this.codiceTributoNonAddizionali = codiceTributoNonAddizionali;
		this.annoTributo = annoTributo;
		this.progressivoTributo = progressivoTributo;
		this.tipoTributo = tipoTributo;
		this.importoTributo = importoTributo;
		this.impPagatoConSgravi = impPagatoConSgravi;
		this.noteTributo = noteTributo;
	}

	/**
	 * @return the codiceTributoNonAddizionali
	 */
	public String getCodiceTributoNonAddizionali() {
		return codiceTributoNonAddizionali;
	}

	/**
	 * @param codiceTributoNonAddizionali the codiceTributoNonAddizionali to set
	 */
	public void setCodiceTributoNonAddizionali(String codiceTributoNonAddizionali) {
		this.codiceTributoNonAddizionali = codiceTributoNonAddizionali;
	}

	/**
	 * @return the annoTributo
	 */
	public String getAnnoTributo() {
		return annoTributo;
	}

	/**
	 * @param annoTributo the annoTributo to set
	 */
	public void setAnnoTributo(String annoTributo) {
		this.annoTributo = annoTributo;
	}

	/**
	 * @return the progressivoTributo
	 */
	public String getProgressivoTributo() {
		return progressivoTributo;
	}

	/**
	 * @param progressivoTributo the progressivoTributo to set
	 */
	public void setProgressivoTributo(String progressivoTributo) {
		this.progressivoTributo = progressivoTributo;
	}

	/**
	 * @return the tipoTributo
	 */
	public String getTipoTributo() {
		return tipoTributo;
	}

	/**
	 * @param tipoTributo the tipoTributo to set
	 */
	public void setTipoTributo(String tipoTributo) {
		this.tipoTributo = tipoTributo;
	}

	/**
	 * @return the importoTributo
	 */
	public String getImportoTributo() {
		return importoTributo;
	}

	/**
	 * @param importoTributo the importoTributo to set
	 */
	public void setImportoTributo(String importoTributo) {
		this.importoTributo = importoTributo;
	}

	/**
	 * @return the impPagatoConSgravi
	 */
	public String getImpPagatoConSgravi() {
		return impPagatoConSgravi;
	}

	/**
	 * @param impPagatoConSgravi the impPagatoConSgravi to set
	 */
	public void setImpPagatoConSgravi(String impPagatoConSgravi) {
		this.impPagatoConSgravi = impPagatoConSgravi;
	}

	/**
	 * @return the noteTributo
	 */
	public String getNoteTributo() {
		return noteTributo;
	}

	/**
	 * @param noteTributo the noteTributo to set
	 */
	public void setNoteTributo(String noteTributo) {
		this.noteTributo = noteTributo;
	}
	
}
