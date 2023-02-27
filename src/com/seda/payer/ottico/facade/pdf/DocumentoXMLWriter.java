package com.seda.payer.ottico.facade.pdf;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.Serializable;

public class DocumentoXMLWriter implements Serializable {

	private static final long serialVersionUID = 1L;
	private String birtOutputPath; 

	public DocumentoXMLWriter(String birtOutputPath) {
		this.birtOutputPath = birtOutputPath;
	}

	public String getBirtOutputPath() {
		return birtOutputPath;
	}

	public void setBirtOutputPath(String birtOutputPath) {
		this.birtOutputPath = birtOutputPath;
	}

	public String writeXMLListDocumento(DocumentoType doc, String numeroDocumento, String timestamp) {
		String xmlPath = "";
		try {
			xmlPath = birtOutputPath + "documento_" + numeroDocumento + "_" + timestamp + ".rptflt.xml";
			FileOutputStream xml = new FileOutputStream(xmlPath, false);
			PrintWriter print = new PrintWriter(xml); {
				print.println("<?xml version=\"1.0\" encoding=\"UTF-8\"?>");
				// DOCUMENTO
				print.println("<listaDocumento>");
				print.println("<documento>");
				print.println("<annoEmissione>");
				print.println(doc.getAnnoEmissione());
				print.println("</annoEmissione>");
				print.println("<numeroEmissione>");
				print.println(doc.getNumeroEmissione());
				print.println("</numeroEmissione>");
				print.println("<idDocumento>");
				print.println(doc.getIdDoc());
				print.println("</idDocumento>");
				print.println("<idCartella>");
				print.println(doc.getIdCartella());
				print.println("</idCartella>");
				print.println("<tipologiaServizio>");
				print.println(doc.getTipologiaServizio());
				print.println("</tipologiaServizio>");
				print.println("<descTipologiaServizio>");
				print.println(doc.getDescTipologia());
				print.println("</descTipologiaServizio>");
				print.println("<tipoDocumento>");
				print.println(doc.getTipoDocumento());
				print.println("</tipoDocumento>");
				print.println("<numeroBollettino>");
				print.println(doc.getNumeroBollettino());
				print.println("</numeroBollettino>");
				print.println("<denominazione>");
				print.println(doc.getDenominazione());
				print.println("</denominazione>");
				print.println("<impServizio>");
				print.println(doc.getImpostaServizio());
				print.println("</impServizio>");
				print.println("<descImpostaServizio>");
				print.println(doc.getDescImpServizio());
				print.println("</descImpostaServizio>");
				print.println("<descEnte>");
				print.println(doc.getDescrizioneEnte());
				print.println("</descEnte>");
				print.println("<impInizialeDocumento>");
				print.println(doc.getImpInizialeDocumento());
				print.println("</impInizialeDocumento>");
				// DETTAGLIO DOCUMENTO
				print.println("<impTributiIniziale>");
				print.println(doc.getImpTributiIniziali());
				print.println("</impTributiIniziale>");
				print.println("<impDirNotificaIniziale>");
				print.println(doc.getImpDriNotificaIniziale());
				print.println("</impDirNotificaIniziale>");
				print.println("<impCompRiscIniziale>");
				print.println(doc.getImpComRiscIniziale());
				print.println("</impCompRiscIniziale>");
				print.println("<impIntMoraIniziale>");
				print.println(doc.getImpIntMoraIniziale());
				print.println("</impIntMoraIniziale>");
				print.println("<impAltreSpeseIniziale>");
				print.println(doc.getImpAltreSpeseIniziale());
				print.println("</impAltreSpeseIniziale>");
				print.println("<impTotaleDocIniziale>");
				print.println(doc.getImpTotaleDocIniziale());
				print.println("</impTotaleDocIniziale>");
				print.println("</documento>");
				print.println("</listaDocumento>");
			}
			print.flush();
			print.close();
			xml.flush();
			xml.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		return xmlPath;
	}
	
	public String writeXMLScadenze(ScadenzaType[] scadenzeLst, String numeroDocumento, String timestamp) {
		String xmlPath = "";
		try {
			xmlPath = birtOutputPath + "scadenze_" + numeroDocumento + "_" + timestamp + ".rptflt.xml";
			FileOutputStream xml = new FileOutputStream(xmlPath, false);
			PrintWriter print = new PrintWriter(xml);
			print.println("<?xml version=\"1.0\" encoding=\"UTF-8\"?>");
			print.println("<scadenze>");
			for (int scadCount = 0; scadCount < scadenzeLst.length; scadCount++) {
				ScadenzaType s = scadenzeLst[scadCount];
				print.println("<scadenza>");
				print.println("<progressivoRata>");
				print.println(s.getProgressivoRata());
				print.println("</progressivoRata>");
				print.println("<dataScadenza>");
				String data = s.getDataScadenza();
				print.println(data);
				print.println("</dataScadenza>");
				print.println("<codiceBollettino>");
				print.println(s.getCodBollettino());
				print.println("</codiceBollettino>");
				print.println("<impRataIniziali>");
				print.println(s.getImpRataIniziale());
				print.println("</impRataIniziali>");
				print.println("</scadenza>");
			}
			print.println("</scadenze>");
			print.flush();
			print.close();
			xml.flush();
			xml.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		return xmlPath;
	}

	public String writeXMLTributi(TributoType[] tributoLst, String numeroDocumento, String timestamp) {
		String xmlPath = "";
		try {
			xmlPath = birtOutputPath + "tributi_"  + numeroDocumento + "_" + timestamp + ".rptflt.xml";
			FileOutputStream xml = new FileOutputStream(xmlPath, false);
			PrintWriter print = new PrintWriter(xml);
			print.println("<?xml version=\"1.0\" encoding=\"UTF-8\"?>");
			print.println("<tributi>");
			for (int tributoCount = 0; tributoCount < tributoLst.length; tributoCount++) {
				TributoType tributo = tributoLst[tributoCount];
				print.println("<tributo>");
				print.println("<codiceTributo>");
				print.println(tributo.getCodiceTributoNonAddizionali());
				print.println("</codiceTributo>");
				print.println("<annoTributo>");
				print.println(tributo.getAnnoTributo());
				print.println("</annoTributo>");
				print.println("<progressivoTributo>");
				print.println(tributo.getProgressivoTributo());
				print.println("</progressivoTributo>");
				
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
				
				print.println("<tipoTributo>");
				print.println(descrTributo);
				print.println("</tipoTributo>");
				print.println("<importoTributo>");
				print.println(tributo.getImportoTributo());
				print.println("</importoTributo>");
				print.println("<impPagatoConSgravi>");
				print.println(tributo.getImpPagatoConSgravi());
				print.println("</impPagatoConSgravi>");
				print.println("<noteTributo>");
				print.println(tributo.getNoteTributo());
				print.println("</noteTributo>");
				print.println("</tributo>");
			}
			print.println("</tributi>");
			print.flush();
			print.close();
			xml.flush();
			xml.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} 
		
		return xmlPath;
	}
}