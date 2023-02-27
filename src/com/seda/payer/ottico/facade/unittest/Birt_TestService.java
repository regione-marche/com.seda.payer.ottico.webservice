package com.seda.payer.ottico.facade.unittest;

import java.io.BufferedWriter;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.rmi.RemoteException;
import java.sql.SQLException;

import javax.xml.rpc.ServiceException;

import com.seda.payer.ottico.facade.pdf.DocumentoType;
import com.seda.payer.ottico.facade.pdf.ScadenzaType;
import com.seda.payer.pgec.webservice.commons.srv.FaultType;
import com.seda.payer.pgec.webservice.ec.dati.RecuperaDettDocRequest;
import com.seda.payer.pgec.webservice.ec.dati.RecuperaDettDocResponse;
import com.seda.payer.pgec.webservice.ec.dati.RecuperaListaDocRequest;
import com.seda.payer.pgec.webservice.ec.dati.RecuperaListaDocResponse;
import com.seda.payer.pgec.webservice.ec.source.EstrattoContoInterface;
import com.seda.payer.pgec.webservice.ec.source.EstrattoContoServiceLocator;

public class Birt_TestService {

	public Birt_TestService(){}

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

			System.out.println("[" + respDoc.getReqresp().getRetCode() + "] " + respDoc.getReqresp().getRetMessage());

			if(respDoc != null){
				//Questa istruzione ottiene un documento xml secondo lo standard WebCahedRowset 
				System.out.println("TEST: STAMPA LISTA DOC ");
				String listaDoc =  respDoc.getListXmlDocumenti();
				/*Conversione*/
				System.out.println("TEST: Conversione listaDoc ");
				javax.sql.rowset.CachedRowSet ecCached_ListaDoc = com.seda.commons.string.Convert.stringToWebRowSet(respDoc.getListXmlDocumenti());
				System.out.println("TEST: GRANDEZZA CACHED ROWSET LISTA DOCUMENTI: " + ecCached_ListaDoc.size());
				System.out.println("STAMPA LISTA DOC SU FILE");

				FileWriter fstream = new FileWriter("D:\\FileTemporanei\\Payer\\Log\\iargenio\\BirtReport\\listDocumentiCachedRowSet.xml");
				BufferedWriter out = new BufferedWriter(fstream);
				out.write(listaDoc);
				out.close();
				
				DocumentoType doc_Tmp = null;
				/*- CACHEDROWSET LIST DOCUMENTO*/
				if (ecCached_ListaDoc.size() > 0) {
					while (ecCached_ListaDoc.next()) {
						doc_Tmp = new DocumentoType(
								ecCached_ListaDoc.getString(1),
								ecCached_ListaDoc.getString(2),
								ecCached_ListaDoc.getString(3), 
								ecCached_ListaDoc.getString(4), 
								ecCached_ListaDoc.getString(5), 
								ecCached_ListaDoc.getString(6), 
								ecCached_ListaDoc.getString(7), 
								ecCached_ListaDoc.getString(11),
								ecCached_ListaDoc.getString(12),
								ecCached_ListaDoc.getString(13),
								ecCached_ListaDoc.getString(14),
								ecCached_ListaDoc.getString(15),
								ecCached_ListaDoc.getString(17),
								ecCached_ListaDoc.getString(21),
								ecCached_ListaDoc.getString(24),
								ecCached_ListaDoc.getString(27),
								ecCached_ListaDoc.getString(30),
								ecCached_ListaDoc.getString(33),
								ecCached_ListaDoc.getString(36));
					}
				}
				//inizio LP PG21XX04 Leak
		    	try {
		    		if(ecCached_ListaDoc != null) {
		    			ecCached_ListaDoc.close();
		    		}
		    	} catch (SQLException e) {
		    		e.printStackTrace();
				}
				//fine LP PG21XX04 Leak
				System.out.println("La lista documentyi ha "+ doc_Tmp + " elementi.");
				System.out.println("\n\n\n-------------------------------------\nDettaglio Documento\n");
				
				
			}

			RecuperaDettDocRequest reqDett = new RecuperaDettDocRequest();
			reqDett.setCodiceUtente("000TO");
			reqDett.setCodiceEnte("06954");
			reqDett.setTipoUfficio("");
			reqDett.setCodiceUfficio("");
			reqDett.setCodiceFiscale("BAUFBA74T23L219Z");
			reqDett.setDocumento("0810100211140000003");
			reqDett.setTipoServizio("EP");
		//	reqDett.setTipoRichiesta("I"); // eSubArea == SUBAREA_ESTRATTOCONTO.INSOLUTI ? "I" : "S");
			reqDett.setTipoRichiesta("I");
		//	reqDett.setTipoIntegrazione("I");
			reqDett.setTipoIntegrazione("I");
			reqDett.setUrlIntegrazione("I"); //http://10.10.80.105:8080/PgEcService/ec"); //ente.getUrlEC());

			RecuperaDettDocResponse res = ecCaller.recuperaDettDoc(reqDett);
			if (res != null)
			{System.out.println("TEST: Recupero dettaglio Documento: " + res.getReqresp().getDocumento());
			
			System.out.println("TEST: Risultato esito recuperaDettDoc " + res.getReqresp().getRetCode() + res.getReqresp().getRetMessage());
			//scadenze
			String scadenze = res.getListXmlScadenze();
			String movimenti = res.getListXmlMovimenti();
			String tributi =  res.getListXmlTributi();
			
			System.out.println("TEST: LISTE DEL DETTAGLIO DOCUMENTO: " + 
					"SCADENZE = " + scadenze.length() + "\n" +
					"MOVIMENTO = " + movimenti.length() + "\n" +
					"TRIBUTI = " + tributi.length() + "\n");
		
			if(scadenze==null)
				System.out.println("Le scadenze sono nulle");
			/*Conversione scadenzee*/

			/*Errore conversione
				 TEST: Recupero dettaglio Documento: 0810100120100103878
			 ** Parsing Error, line -1, uri null
				org.xml.sax.SAXParseException: Premature end of file.
					at org.apache.xerces.parsers.AbstractSAXParser.parse(Unknown Source)
					at org.apache.xerces.jaxp.SAXParserImpl$JAXPSAXParser.parse(Unknown Source)
			 */
//			javax.sql.rowset.CachedRowSet ecCached = null;
			javax.sql.rowset.CachedRowSet ecCached_Scadenze = null;
			javax.sql.rowset.CachedRowSet ecCached_Tributi = null;
			javax.sql.rowset.CachedRowSet ecCached_Movimenti = null;
			FileWriter fstream = null;
			BufferedWriter out = null;
			
			/*GESTIONE SCADENZE*/
			if(scadenze.length()>0){
			ecCached_Scadenze = com.seda.commons.string.Convert.stringToWebRowSet(res.getListXmlScadenze());
			System.out.println("TEST: GRANDEZZA CACHED ROWSET SCADENZE: " + ecCached_Scadenze.size());
			
			System.out.println("STAMPA SCADENZE SU FILE");
			fstream = new FileWriter("scadenze.xml");
			out = new BufferedWriter(fstream);
			out.write(scadenze);
			out.close();
			
			}else if(scadenze.length()<=0){
				System.out.println("Non ci sono scadenze");
			}
			
			/*GESTIONE TRIBUTI*/
			if(tributi.length()>0){
			ecCached_Tributi = com.seda.commons.string.Convert.stringToWebRowSet(res.getListXmlTributi());
			System.out.println("TEST: GRANDEZZA CACHED ROWSET TRIBUTI: " + ecCached_Tributi.size());
			
			System.out.println("STAMPA TRIBUTI SU FILE");
			fstream = new FileWriter("tributi.xml");
			out = new BufferedWriter(fstream);
			out.write(tributi);
			out.close();
			
			}else if(tributi.length()<=0){
				System.out.println("Non ci sono tributi");
			}
			
			if(movimenti.length()>0){
			ecCached_Movimenti = com.seda.commons.string.Convert.stringToWebRowSet(res.getListXmlMovimenti());
			System.out.println("TEST: GRANDEZZA CACHED ROWSET MOVIMENTI: " + ecCached_Movimenti.size());
			
			System.out.println("STAMPA MOVIMENTI SU FILE");
			fstream = new FileWriter("movimenti.xml");
			out = new BufferedWriter(fstream);
			out.write(movimenti);
			out.close();
			
			}else if(movimenti.length()<=0){
				System.out.println("Non ci sono movimenti");
			}
			
			/*CONVERSIONE SCADENZE*/
			ScadenzaType[] scadenzeLst = new ScadenzaType[ecCached_Scadenze.size()];
			ScadenzaType scadenza = null;
//			SimpleDateFormat inputFormat = new SimpleDateFormat("dd/MM/yyyy");
//			Date date1 = null;
			int i = 0;

			if (ecCached_Scadenze.size() > 0) {

				while (ecCached_Scadenze.next()) {
//					scadenza = new ScadenzaType(progressivoRata,
//							dataScadenza,
//							codBollettino, 
//							impRataIniziale, impRataResiduo, 
//							impRataResiduoNotifica, impRataResiduoComp,
//							impRataResiduoMora, impTotaleResiduo,
//							tipoRata, pagamentoInAcquisizione);
//					Calendar cal = Calendar.getInstance();;
//					try {
//						date1 = (Date)inputFormat.parse(ecCached_Scadenze.getString(2));
//						cal.setTime(date1);
//					} catch (ParseException e) {
//						e.printStackTrace();
//					}
//					
//					java.text.NumberFormat nf = java.text.DecimalFormat.getInstance(java.util.Locale.ITALIAN);
					//System.out.println(nf.format(21622.344)); 
						
					scadenza = new ScadenzaType(ecCached_Scadenze.getString(1),
							ecCached_Scadenze.getString(2),
							ecCached_Scadenze.getString(3),
							ecCached_Scadenze.getString(4),
							ecCached_Scadenze.getString(5),
							ecCached_Scadenze.getString(6),
							ecCached_Scadenze.getString(7),
							ecCached_Scadenze.getString(8),
							ecCached_Scadenze.getString(9),
							ecCached_Scadenze.getString(10),
							ecCached_Scadenze.getString(11),
							ecCached_Scadenze.getString(12)
							);
					scadenzeLst[i] = scadenza;
					i++;
					
//					System.out.println("\n--------------\nScansione Scadenze");
//					System.out.println("SCADENZA 01 = " + ecCached_Scadenze.getString(1));
//					System.out.println("SCADENZA 02 = " + ecCached_Scadenze.getString(2));
//					System.out.println("SCADENZA 03 = " + ecCached_Scadenze.getString(3));
//					System.out.println("SCADENZA 04 = " + ecCached_Scadenze.getString(4));
//					System.out.println("SCADENZA 05 = " + ecCached_Scadenze.getString(5));
//					System.out.println("SCADENZA 06 = " + ecCached_Scadenze.getString(6));
//					System.out.println("SCADENZA 07 = " + ecCached_Scadenze.getString(7));
//					System.out.println("SCADENZA 08 = " + ecCached_Scadenze.getString(8));
//					System.out.println("SCADENZA 09 = " + ecCached_Scadenze.getString(9));
//					System.out.println("SCADENZA 10 = " + ecCached_Scadenze.getString(10));
//					System.out.println("SCADENZA 11 = " + ecCached_Scadenze.getString(11));
//					System.out.println("SCADENZA 12 = " + ecCached_Scadenze.getString(12));
				}
			}
			//inizio LP PG21XX04 Leak
	    	try {
	    		if(ecCached_Tributi != null) {
	    			ecCached_Tributi.close();
	    		}
	    	} catch (SQLException e) {
	    		e.printStackTrace();
			}
	    	try {
	    		if(ecCached_Movimenti != null) {
	    			ecCached_Movimenti.close();
	    		}
	    	} catch (SQLException e) {
	    		e.printStackTrace();
			}
	    	try {
	    		if(ecCached_Scadenze != null) {
	    			ecCached_Scadenze.close();
	    		}
	    	} catch (SQLException e) {
	    		e.printStackTrace();
			}
			//fine LP PG21XX04 Leak
			
			/*CREAZIONE XML SCADENZE*/
			System.out.println("TEST: CREAZIONE DEL FILE XML PER LE SCADENZE");
			// access denied D:\\VIEWs\\PayER_AS_PG090210_iargenio\\PayER_VOB\\com.seda.payer.ottico.facade\\src\\com\\seda\\payer\\ottico\\facade\\pdf\\scadenze_runtime.xml
			FileOutputStream xml = new FileOutputStream("D:\\FileTemporanei\\Payer\\Log\\iargenio\\BirtReport\\scadenze_runtime.xml", false);
			PrintWriter print = new PrintWriter(xml);
			print.println("<?xml version=\"1.0\" encoding=\"UTF-8\"?>");
			print.println("<scadenze>");
			for(int scadCount = 0; scadCount < scadenzeLst.length; scadCount++){
				ScadenzaType s = scadenzeLst[scadCount];
			print.println("<scadenza>");
			print.println("<progressivorata>");
			print.println(s.getProgressivoRata());
			print.println("</progressivorata>");
			print.println("<datascadenza>");
			String data = s.getDataScadenza();
			print.println(data);
			print.println("</datascadenza>");
			print.println("<codicebollettino>");
			print.println(s.getCodBollettino());
			print.println("</codicebollettino>");
			print.println("<impratainiziali>");
			print.println(s.getImpRataIniziale());
			print.println("</impratainiziali>");
			print.println("</scadenza>");
			}
			print.println("</scadenze>");
			print.flush();
			print.close();
			xml.flush();
			xml.close();
			
			}
			
		} catch (FaultType e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (RemoteException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ServiceException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (NumberFormatException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}  
	}

	//	public static void main(String[] args) {
	//		EstrattoContoInterface ecCaller = null;
	//		try {
	//
	////			RecuperaListaDocRequest reqDoc = new RecuperaListaDocRequest(
	////					codiceUtente, codiceEnte, 
	////					tipoUfficio, codiceUfficio,
	////					codiceFiscale, tipoServizio,
	////					tipoRichiesta, documento, tipoIntegrazione,
	////					urlIntegrazione, retCode, retMessage,
	////					tipologiaServizio, impostaServizio);
	//
	//			EstrattoContoServiceLocator lsService = new EstrattoContoServiceLocator();
	//			lsService.setEstrattoContoPortEndpointAddress("http://10.10.80.105:8080/PgEcService/ec");
	//			ecCaller = (EstrattoContoInterface) lsService.getEstrattoContoPort();
	//			
	//			//DOCUMENTO
	//			RecuperaListaDocRequest reqDoc = new RecuperaListaDocRequest();
	//			reqDoc.setCodiceUtente("000TO");
	//			reqDoc.setCodiceEnte("06954");
	//			reqDoc.setTipoUfficio("A");
	//			reqDoc.setCodiceUfficio("AAAAAA");
	//			reqDoc.setCodiceFiscale("BAUFBA74T23L219Z");
	//			reqDoc.setDocumento("0810100120100103878");
	//			reqDoc.setTipoServizio("EP");
	//			reqDoc.setTipoRichiesta("I"); // eSubArea == SUBAREA_ESTRATTOCONTO.INSOLUTI ? "I" : "S");
	//			reqDoc.setTipoIntegrazione("I");
	//			reqDoc.setUrlIntegrazione(""); 
	//			
	//			RecuperaListaDocResponse respDoc = ecCaller.recuperaListaDoc(reqDoc);
	//			if(respDoc != null){
	//				//Questa istruzione ottiene un documento xml secondo lo standard WebCahedRowset 
	//				System.out.println("TEST: Recupero lista doc " + respDoc.getListXmlDocumenti());
	//				/*Conversione*/
	//				System.out.println("TEST: Conversione listaDoc ");
	//				javax.sql.rowset.CachedRowSet ecCached = com.seda.commons.string.Convert.stringToWebRowSet(respDoc.getListXmlDocumenti());
	//				System.out.println("Size conversione: " + ecCached.size());
	//				/*TODO: Convertire */
	//				/* colonne e dati della colonna*/
	//			System.out.println("\n\n\n-------------------------------------\nDettaglio Documento\n");
	//			//DETTAGLIO DOCUMENTO
	////Importato
	////			RecuperaDettDocRequest req = new RecuperaDettDocRequest();
	////			req.setCodiceUtente(doc.getCodUtente());
	////			req.setCodiceEnte(doc.getCodEnte());
	////			req.setTipoUfficio(doc.getTipoUfficio());
	////			req.setCodiceUfficio(doc.getCodUfficio());
	////			req.setCodiceFiscale(GenericsEC.getCodiceFiscaleContribuente(session));
	////			req.setDocumento(doc.getNumeroDocumento());
	////			req.setTipoServizio("EP");
	////			req.setTipoRichiesta(eSubArea == SUBAREA_ESTRATTOCONTO.INSOLUTI ? "I" : "S");
	////			req.setTipoIntegrazione(ente.getTipoIntegrazione());
	////			req.setUrlIntegrazione(ente.getUrlEC());
	////			
	//			RecuperaDettDocRequest reqDett = new RecuperaDettDocRequest();
	//			reqDett.setCodiceUtente("000TO");
	//			reqDett.setCodiceEnte("06954");
	//			reqDett.setTipoUfficio("A");
	//			reqDett.setCodiceUfficio("AAAAAA");
	//			reqDett.setCodiceFiscale("BAUFBA74T23L219Z");
	//			reqDett.setDocumento("0810100120100103878");
	//			reqDett.setTipoServizio("EP");
	//			reqDett.setTipoRichiesta("I"); // eSubArea == SUBAREA_ESTRATTOCONTO.INSOLUTI ? "I" : "S");
	//			reqDett.setTipoIntegrazione("I");
	//			reqDett.setUrlIntegrazione(""); //http://10.10.80.105:8080/PgEcService/ec"); //ente.getUrlEC());
	//			
	//			RecuperaDettDocResponse res = ecCaller.recuperaDettDoc(reqDett);
	//			if (res != null)
	//			{System.out.println("TEST: Recupero dettaglio Documento: " + res.getReqresp().getDocumento());
	//			
	//				//scadenze
	//				String scadenze = res.getListXmlScadenze();
	//				if(scadenze==null)
	//					System.out.println("Le scadenze sono nulle");
	//				/*Conversione scadenzee*/
	//				
	//				/*Errore conversione
	//				 TEST: Recupero dettaglio Documento: 0810100120100103878
	//				** Parsing Error, line -1, uri null
	//				org.xml.sax.SAXParseException: Premature end of file.
	//					at org.apache.xerces.parsers.AbstractSAXParser.parse(Unknown Source)
	//					at org.apache.xerces.jaxp.SAXParserImpl$JAXPSAXParser.parse(Unknown Source)
	//				 */
	//				ecCached = com.seda.commons.string.Convert.stringToWebRowSet(res.getListXmlScadenze());
	//				ScadenzaType[] scadenzeLst = new ScadenzaType[ecCached.size()];
	//				ScadenzaType scadenza = null;
	//
	//				int i = 0;
	//
	//				if (ecCached.size() > 0) {
	//
	//				while (ecCached.next()) {
	//
	//				System.out.println("SCADENZA CAHED = " + ecCached);
	//				//Costruisci bean
	//				//scadenza = this.convertScadenza(ecCached);
	//
	//				/*Usa scadenze*/
	//				//String codiceSocieta = ecCached.getString(1).trim();
	//
	//				// we retry descUtente by method Commons.recuperaUtente
	//
	////				RecuperaUtenteResponse recuperaUtenteResponse = commons.recuperaUtente(
	////
	////				new RecuperaUtenteRequest(codiceSocieta, ente.getCodiceUtente()));
	////
	////				ente.setDescUtente(recuperaUtenteResponse.getDescUtente());
	////
	////				listEnti[i]= ente;
	//
	//				i++;
	//
	//				}
	//
	//				}
	//			}
	//				/**/
	//				//System.out.println("TEST: stampa SCADENZE\n"+scadenze);
	//				
	//				//movimenti
	//				String movimenti = res.getListXmlMovimenti();
	//				System.out.println("TEST: stampa MOVIMENTI\n"+movimenti+
	//						"\n--------------------------------------------\n\n");
	//				
	//				//tributi
	//				String tributi = res.getListXmlTributi();
	//				System.out.println("TEST: stampa SCADENZE\n"+tributi+
	//						"\n--------------------------------------------\n\n");
	//			}


	//			IntegraEnteEcServiceLocator serviceLocator = new IntegraEnteEcServiceLocator();
	//			//LogWriter.logInfo("TEST: OtticoURL = " + serviceLocator.getOtticoPortAddress());
	//			//TMP http://10.10.80.6:8979/OtticoService/ottico
	//			 IntegraEnteEcInterface integraPort = serviceLocator.getIntegraenteEcPort(new URL("http://10.10.80.105:8080/IntegraenteService/integraenteec"));
	//			 RecuperaListDocRequest reqList = new RecuperaListDocRequest();
	//			 reqList.setCodiceUtente("000TO");
	//			 reqList.setCodiceEnte("06954");
	//			 reqList.setCodiceFiscale("");
	//			 reqList.setCodiceUfficio("A");
	//			 reqList.setDocumento("");
	//			 reqList.setEsitoRichiesta("");
	//			 reqList.setImpostaServizio("");
	//			 reqList.setMessaggioEsito("");
	//			 reqList.setTipoIntegrazione("D");
	//			 reqList.setTipologiaServizio("");
	//			 reqList.setTipoRichiesta("");
	//			 reqList.setTipoServizio("EP");
	//			 reqList.setTipoUfficio("");
	//			 
	//			 //respList.getListDoc(0).getListDoc().getAnnoEmissione()
	//				RecuperaListDocResponse respList = integraPort.recuperaListDoc(reqList);
	//				System.out.println("RESPLIST = " + (respList != null ? respList.getResp().getMessaggioEsito() : null));
	//				SingleDocumento[] doc = respList.getListDoc();
	//				System.out.println("TEST: SIZE =  " + doc.length);
	//System.out.println("OUT TEST INTEGRAENTE: " + respList.getListDoc(0).getListDoc().getAnnoEmissione());

	//			TemplateDocumentoDto templateDocumento = new TemplateDocumentoDto(new TemplateDocumentoDao(conn, getSchema()).doDetail(tipoDocumento, 
	//					"", "", "", 
	//					tipologiaServizio, tipoBolletino, dataValidita, dataValidita, null));
	//TEST SENDEMAIL
	//			TestService s = new TestService();
	//			System.out.println("INIZIO TEST MAIL");
	//			String emailAdmin = "aniello.la.bua@gmail.com";
	//			String text = Template.MESSAGGIO_VALIDAZIONE_ERRATA.format() + " TESTSERVICE";
	//			String subject = Template.SUBJECT.format();
	//			//this.notificaMancatoAllineamento(text, toList, ccList, ccnList, subject, attacchedFileList);
	//			s.notificaMancatoAllineamento(text, emailAdmin, "", "", subject, "");
	//			System.out.println("FINE TEST MAIL");
	// TEST TREESET
	//			TreeSet treeSet = 
	//				FileUtility.find("D:\\FileTemporanei\\Payer\\Ottico\\input\\", 
	//						Pattern.compile("^DAT00004000TO06954(.*?)[\\|/]*.(zip)"), FileUtility.ORDER_ASC);
	//
	//			System.out.println(treeSet.size());

	// TEST MOVE
	//			String path1 = "D:\\FileTemporanei\\Payer\\Ottico\\input\\";
	//			String path2 = "D:\\FileTemporanei\\Payer\\Ottico\\output\\";
	//			String fileName = "DAT00004000TO0695420110613001.zip";
	//			
	//			FileUtility.move(new File(path1+fileName),path2);

	// TEST DATE FORMATTAZIONE ITA
	//			DateFormat formatter = new SimpleDateFormat("E, dd MMM yyyy hh:mm:ss", Locale.ITALY);
	//			String s = formatter.format(new Date());
	//			System.out.println("OUT: " + s);


	//TEST REMOVE
	//FileUtility.rm(this.flowFileUtil.getFlowInfo().getElabPath(), filenameFilter, true);
	//			String remPath = "D:\\FileTemporanei\\Payer\\Ottico\\input\\tmp\\";
	//			FileUtility.rm(remPath, "*.*", true);
	//		} catch (Exception e) {
	//			e.printStackTrace();
	//		}
	//		
	//	}
	//	
	//	public String notificaMancatoAllineamento(String text, String toList, String ccList, String ccnList, String subject, String attacchedFileList)
	//	{
	//		String resultEmailSender="";
	//		EMailSenderInterface emailSenderPort = null;
	//		EMailSenderResponse emailSenderResponse =null;
	//		
	//		
	//		EMailSenderRequestType emailSenderRequestType = new EMailSenderRequestType();
	//		emailSenderRequestType.setEMailDataText(text.toString());
	//		emailSenderRequestType.setEMailDataTOList(toList);
	//		emailSenderRequestType.setEMailDataCCList(ccList);
	//		emailSenderRequestType.setEMailDataCCNList(ccnList);		
	//		emailSenderRequestType.setEMailDataSubject(subject.toString());		
	//		emailSenderRequestType.setEMailDataAttacchedFileList(attacchedFileList);
	//		
	//		EMailSenderServiceLocator emailSenderServiceLocator = new EMailSenderServiceLocator();
	//		try {
	//			emailSenderPort = emailSenderServiceLocator.getEMailSenderPort(new URL("http://10.10.80.105:8080/EMailSender/service"));
	//			emailSenderResponse = emailSenderPort.getEMailSender(emailSenderRequestType);
	//			resultEmailSender = emailSenderResponse.getValue();			
	//			
	//		} catch (MalformedURLException e) {
	//			
	//			e.printStackTrace();
	//		} catch (ServiceException e) {
	//			
	//			e.printStackTrace();		
	//		} catch (EMailSenderFaultType e) {
	//			
	//			e.printStackTrace();
	//		} catch (RemoteException e) {
	//			
	//			e.printStackTrace();
	//		}		
	//		return resultEmailSender;
	//	}
}
