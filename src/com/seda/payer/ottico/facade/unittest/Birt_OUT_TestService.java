package com.seda.payer.ottico.facade.unittest;

import java.rmi.RemoteException;

import javax.xml.rpc.ServiceException;


import com.seda.commons.logger.CustomLoggerManager;
import com.seda.commons.logger.LoggerWrapper;
import com.seda.payer.ottico.facade.pdf.DocumentoBeanCreator;
import com.seda.payer.ottico.facade.pdf.DocumentoType;
import com.seda.payer.ottico.facade.pdf.DocumentoXMLWriter;
import com.seda.payer.ottico.facade.pdf.ReportCreator;
import com.seda.payer.pgec.webservice.commons.srv.FaultType;
import com.seda.payer.pgec.webservice.ec.dati.RecuperaListaDocRequest;
import com.seda.payer.pgec.webservice.ec.dati.RecuperaListaDocResponse;
import com.seda.payer.pgec.webservice.ec.source.EstrattoContoInterface;
import com.seda.payer.pgec.webservice.ec.source.EstrattoContoServiceLocator;

public class Birt_OUT_TestService {

	final static String birtDesignPath = "D:/ConfigFiles/Payer/BirtReport/"; 
	final static String birtHomeEngine = "D:\\SWsviluppo\\birt-runtime-2_5_2\\ReportEngine\\";
	final static String birtHomeLogs = "D:\\FileTemporanei\\Payer\\Log\\BirtLog";
	final static String outputDirectoryPdf = "\\\\10.10.9.106\\BirtReport\\iargenio\\";
	final static String pathLogoReport = "D:/ConfigFiles/Payer/BirtReport/imglogo/LogoLepida.png";
	final static LoggerWrapper logger = CustomLoggerManager.get(Birt_OUT_TestService.class);
	public Birt_OUT_TestService() {
	}

	public static void main(String[] args) {

		EstrattoContoInterface ecCaller = null;

		try {
			EstrattoContoServiceLocator lsService = new EstrattoContoServiceLocator();
			lsService.setEstrattoContoPortEndpointAddress("http://10.10.80.105:8080/PgEcService/ec");
			ecCaller = (EstrattoContoInterface) lsService.getEstrattoContoPort();

			RecuperaListaDocRequest reqDoc = new RecuperaListaDocRequest(); {
				reqDoc.setCodiceUtente("000TO");
				reqDoc.setCodiceEnte("06954");
				reqDoc.setTipoUfficio("");
				reqDoc.setCodiceUfficio("");
				reqDoc.setCodiceFiscale("BAUFBA74T23L219Z");
				reqDoc.setDocumento("");
				reqDoc.setTipoServizio("EP");
				reqDoc.setTipoRichiesta("I"); // eSubArea == SUBAREA_ESTRATTOCONTO.INSOLUTI ? "I" : "S");
				reqDoc.setTipoIntegrazione("I");
				reqDoc.setUrlIntegrazione("I");
			}
			RecuperaListaDocResponse respDoc = ecCaller.recuperaListaDoc(reqDoc);
			
			
			DocumentoBeanCreator beanCreator = new DocumentoBeanCreator(logger);
			DocumentoXMLWriter writer = new DocumentoXMLWriter(birtDesignPath);

			if (respDoc != null) {
				
				/*// 1
				DocumentoType documento = beanCreator.createListaDoc(respDoc.getListXmlDocumenti());
				writer.writeXMLListDocumento(documento);
				ReportCreator reportCreator = new ReportCreator(birtHomeEngine, birtHomeLogs, outputDirectoryPdf, birtDesignPath,
						 pathLogoReport, null, null);
				String reportName = reportCreator.generaPDFDocumento("00000000001");
				System.out.println(reportName);
				
				// 2
				documento = beanCreator.createListaDoc(respDoc.getListXmlDocumenti());
				writer.writeXMLListDocumento(documento);
				reportCreator = new ReportCreator(birtHomeEngine, birtHomeLogs, outputDirectoryPdf, birtDesignPath, 
						 pathLogoReport, null, null);
				reportName = reportCreator.generaPDFDocumento("00000000002");
				System.out.println(reportName);	*/			
			}

//			RecuperaDettDocRequest reqDett = new RecuperaDettDocRequest();
//			reqDett.setCodiceUtente("000TO");
//			reqDett.setCodiceEnte("06954");
//			reqDett.setTipoUfficio("");
//			reqDett.setCodiceUfficio("");
//			reqDett.setCodiceFiscale("BAUFBA74T23L219Z");
//			reqDett.setDocumento("0810100211140000003");
//			reqDett.setTipoServizio("EP");
//			reqDett.setTipoRichiesta("I");
//			reqDett.setTipoIntegrazione("I");
//			reqDett.setUrlIntegrazione("I"); //http://10.10.80.105:8080/PgEcService/ec"); //ente.getUrlEC());
//
//			RecuperaDettDocResponse res = ecCaller.recuperaDettDoc(reqDett);
//			if (res != null)
//			{System.out.println("TEST: Recupero dettaglio Documento: " + res.getReqresp().getDocumento());
//
//			System.out.println("TEST: Risultato esito recuperaDettDoc " + res.getReqresp().getRetCode() + res.getReqresp().getRetMessage());
//			//scadenze
//			String scadenze = res.getListXmlScadenze();
//			String movimenti = res.getListXmlMovimenti();
//			String tributi =  res.getListXmlTributi();
//
//			System.out.println("TEST: LISTE DEL DETTAGLIO DOCUMENTO: " + 
//					"SCADENZE = " + scadenze.length() + "\n" +
//					"MOVIMENTO = " + movimenti.length() + "\n" +
//					"TRIBUTI = " + tributi.length() + "\n");
//
//			if(scadenze==null)
//				System.out.println("Le scadenze sono nulle");
//			
//			/*Gestione array*/
//			ScadenzaType[] sca_Lst = null;
//			TributoType[] tri_Lst = null;
//			
//			/*GESTIONE SCADENZE*/
//			if(scadenze.length()>0){
//			    sca_Lst = beanCreator.createListaScadenze(res.getListXmlScadenze());
//				writer.writeXMLScadenze(sca_Lst);
//			}else if(scadenze.length()<=0){
//				System.out.println("Non ci sono scadenze");
//			}
//
//			/*GESTIONE TRIBUTI*/
//			if(tributi.length()>0){
//			    tri_Lst = beanCreator.createListaTributi(res.getListXmlTributi());
//				writer.writeXMLTributi(tri_Lst);
//			}else if(tributi.length()<=0){
//				System.out.println("Non ci sono tributi");
//			}
//			
//			System.out.println("--- FINE CREAZIONE STRUTTURA PER REPORT");
//			ReportDocumentoManager.obj_ExecuteReport();
//
//			}
		} catch (FaultType e) {
			e.printStackTrace();
		} catch (RemoteException e) {
			e.printStackTrace();
		} catch (ServiceException e) {
			e.printStackTrace();
		} catch (NumberFormatException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}  
	}
}