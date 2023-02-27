package com.seda.payer.ottico.facade.pdf;

import java.io.Serializable;

public class DocumentoXMLGenerator implements Serializable {

	private static final long serialVersionUID = 1L;

	
	public DocumentoXMLGenerator() {
	}

	public String writeXMLListDocumento(DocumentoType doc, ScadenzaType[] scadenzeLst, TributoType[] tributoLst)
	{
		StringBuffer sbAll = new StringBuffer();
		sbAll.append("<?xml version=\"1.0\" encoding=\"UTF-8\"?>");
		sbAll.append("<listaDocumento>");
		sbAll.append(getXMLDocumento(doc));
		sbAll.append("<scadenze>");
		sbAll.append(getXMLScadenze(scadenzeLst));
		sbAll.append("</scadenze>");
		sbAll.append("<tributi>");
		sbAll.append(getXMLTributi(tributoLst));
		sbAll.append("</tributi>");
		sbAll.append("</listaDocumento>");
		
		return sbAll.toString();
	}
	
	private String getXMLDocumento(DocumentoType doc) 
	{
		StringBuffer sbDocumento = new StringBuffer();
			
		// DOCUMENTO
		sbDocumento.append("<documento>");
		sbDocumento.append("<annoEmissione>");
		sbDocumento.append(doc.getAnnoEmissione());
		sbDocumento.append("</annoEmissione>");
		sbDocumento.append("<numeroEmissione>");
		sbDocumento.append(doc.getNumeroEmissione());
		sbDocumento.append("</numeroEmissione>");
		sbDocumento.append("<idDocumento>");
		sbDocumento.append(doc.getIdDoc());
		sbDocumento.append("</idDocumento>");
		sbDocumento.append("<idCartella>");
		sbDocumento.append(doc.getIdCartella());
		sbDocumento.append("</idCartella>");
		sbDocumento.append("<tipologiaServizio>");
		sbDocumento.append(doc.getTipologiaServizio());
		sbDocumento.append("</tipologiaServizio>");
		sbDocumento.append("<descTipologiaServizio>");
		sbDocumento.append(doc.getDescTipologia());
		sbDocumento.append("</descTipologiaServizio>");
		sbDocumento.append("<tipoDocumento>");
		sbDocumento.append(doc.getTipoDocumento());
		sbDocumento.append("</tipoDocumento>");
		sbDocumento.append("<numeroBollettino>");
		sbDocumento.append(doc.getNumeroBollettino());
		sbDocumento.append("</numeroBollettino>");
		sbDocumento.append("<denominazione>");
		sbDocumento.append(doc.getDenominazione());
		sbDocumento.append("</denominazione>");
		sbDocumento.append("<impServizio>");
		sbDocumento.append(doc.getImpostaServizio());
		sbDocumento.append("</impServizio>");
		sbDocumento.append("<descImpostaServizio>");
		sbDocumento.append(doc.getDescImpServizio());
		sbDocumento.append("</descImpostaServizio>");
		sbDocumento.append("<descEnte>");
		sbDocumento.append(doc.getDescrizioneEnte());
		sbDocumento.append("</descEnte>");
		sbDocumento.append("<impInizialeDocumento>");
		sbDocumento.append(doc.getImpInizialeDocumento());
		sbDocumento.append("</impInizialeDocumento>");
		// DETTAGLIO DOCUMENTO
		sbDocumento.append("<impTributiIniziale>");
		sbDocumento.append(doc.getImpTributiIniziali());
		sbDocumento.append("</impTributiIniziale>");
		sbDocumento.append("<impDirNotificaIniziale>");
		sbDocumento.append(doc.getImpDriNotificaIniziale());
		sbDocumento.append("</impDirNotificaIniziale>");
		sbDocumento.append("<impCompRiscIniziale>");
		sbDocumento.append(doc.getImpComRiscIniziale());
		sbDocumento.append("</impCompRiscIniziale>");
		sbDocumento.append("<impIntMoraIniziale>");
		sbDocumento.append(doc.getImpIntMoraIniziale());
		sbDocumento.append("</impIntMoraIniziale>");
		sbDocumento.append("<impAltreSpeseIniziale>");
		sbDocumento.append(doc.getImpAltreSpeseIniziale());
		sbDocumento.append("</impAltreSpeseIniziale>");
		sbDocumento.append("<impTotaleDocIniziale>");
		sbDocumento.append(doc.getImpTotaleDocIniziale());
		sbDocumento.append("</impTotaleDocIniziale>");
		sbDocumento.append("</documento>");
		
		return sbDocumento.toString();
	}
	
	private String getXMLScadenze(ScadenzaType[] scadenzeLst) 
	{
		StringBuffer sbScadenze = new StringBuffer();
		
		for (ScadenzaType scadenza :  scadenzeLst) {
			sbScadenze.append("<scadenza>");
			sbScadenze.append("<progressivoRata>");
			sbScadenze.append(scadenza.getProgressivoRata());
			sbScadenze.append("</progressivoRata>");
			sbScadenze.append("<dataScadenza>");
			String data = scadenza.getDataScadenza();
			sbScadenze.append(data);
			sbScadenze.append("</dataScadenza>");
			sbScadenze.append("<codiceBollettino>");
			sbScadenze.append(scadenza.getCodBollettino());
			sbScadenze.append("</codiceBollettino>");
			sbScadenze.append("<impRataIniziali>");
			sbScadenze.append(scadenza.getImpRataIniziale());
			sbScadenze.append("</impRataIniziali>");
			sbScadenze.append("</scadenza>");
		}	
		
		return sbScadenze.toString();
	}

	private String getXMLTributi(TributoType[] tributoLst) 
	{
		StringBuffer sbTributi = new StringBuffer();
		
		for (TributoType tributo : tributoLst) {
			sbTributi.append("<tributo>");
			sbTributi.append("<codiceTributo>");
			sbTributi.append(tributo.getCodiceTributoNonAddizionali());
			sbTributi.append("</codiceTributo>");
			sbTributi.append("<annoTributo>");
			sbTributi.append(tributo.getAnnoTributo());
			sbTributi.append("</annoTributo>");
			sbTributi.append("<progressivoTributo>");
			sbTributi.append(tributo.getProgressivoTributo());
			sbTributi.append("</progressivoTributo>");
			
			String descrTributo = "";
			if (tributo.getTipoTributo().equals("I"))
				descrTributo = "Imposta";
			else if (tributo.getTipoTributo().equals("S"))
				descrTributo = "Sanzioni";
			else if (tributo.getTipoTributo().equals("T"))
				descrTributo = "Interessi";
			else if (tributo.getTipoTributo().equals("A"))
				descrTributo = "Altro";
			else if (tributo.getTipoTributo().equals("V"))
				descrTributo = "Interessi di MR";
			
			sbTributi.append("<tipoTributo>");
			sbTributi.append(descrTributo);
			sbTributi.append("</tipoTributo>");
			sbTributi.append("<importoTributo>");
			sbTributi.append(tributo.getImportoTributo());
			sbTributi.append("</importoTributo>");
			sbTributi.append("<impPagatoConSgravi>");
			sbTributi.append(tributo.getImpPagatoConSgravi());
			sbTributi.append("</impPagatoConSgravi>");
			sbTributi.append("<noteTributo>");
			sbTributi.append(tributo.getNoteTributo());
			sbTributi.append("</noteTributo>");
			sbTributi.append("</tributo>");
		}
			
		return sbTributi.toString();
	}
}