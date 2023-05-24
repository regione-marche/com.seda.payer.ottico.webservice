package com.seda.payer.ottico.facade.bean;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.math.BigDecimal;
import java.net.MalformedURLException;
import java.net.URL;
import java.rmi.RemoteException;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.MessageFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Properties;
import java.util.TreeSet;
import java.util.regex.Pattern;

import javax.sql.rowset.WebRowSet;
import javax.xml.rpc.ServiceException;

import com.seda.commons.properties.tree.PropertiesTree;
import com.seda.commons.string.Convert;
import com.seda.emailsender.webservices.dati.EMailSenderRequestType;
import com.seda.emailsender.webservices.dati.EMailSenderResponse;
import com.seda.emailsender.webservices.source.EMailSenderInterface;
import com.seda.emailsender.webservices.source.EMailSenderServiceLocator;
import com.seda.emailsender.webservices.srv.EMailSenderFaultType;
import com.seda.payer.commons.utility.FileUtility;
import com.seda.payer.commons.utility.GZiper;
import com.seda.payer.commons.utility.StringUtility;
import com.seda.payer.core.bean.AnagEnte;
import com.seda.payer.core.bean.Company;
import com.seda.payer.core.bean.Configurazione;
import com.seda.payer.core.bean.TestataFlussoOttico;
import com.seda.payer.core.bean.User;
import com.seda.payer.core.dao.AnagEnteDao;
import com.seda.payer.core.dao.CompanyDao;
import com.seda.payer.core.dao.ConfigurazioneDao;
import com.seda.payer.core.dao.DettaglioFlussoOtticoDao;
import com.seda.payer.core.dao.TemplateDocumentoDao;
import com.seda.payer.core.dao.TestataFlussoOtticoDao;
import com.seda.payer.core.dao.UserDao;
import com.seda.payer.integraottico.webservice.dati.Bollettino;
import com.seda.payer.integraottico.webservice.dati.Documento;
import com.seda.payer.integraottico.webservice.dati.FileEstensione;
import com.seda.payer.integraottico.webservice.dati.FiltriDiRicerca;
import com.seda.payer.integraottico.webservice.dati.GetImmagineContenziosoRequest;
import com.seda.payer.integraottico.webservice.dati.GetImmagineContenziosoResponse;
import com.seda.payer.integraottico.webservice.dati.GetImmagineEsitoRequest;
import com.seda.payer.integraottico.webservice.dati.GetImmagineEsitoResponse;
import com.seda.payer.integraottico.webservice.dati.GetImmagineRequest;
import com.seda.payer.integraottico.webservice.dati.GetImmagineResponse;
import com.seda.payer.integraottico.webservice.dati.GetImmaginiListRequest;
import com.seda.payer.integraottico.webservice.dati.GetImmaginiListResponse;
import com.seda.payer.integraottico.webservice.dati.Immagine;
import com.seda.payer.integraottico.webservice.dati.RecuperaImmagineRequest;
import com.seda.payer.integraottico.webservice.dati.RecuperaImmagineResponse;
import com.seda.payer.integraottico.webservice.dati.Relata;
import com.seda.payer.integraottico.webservice.source.IntegraOtticoSOAPBindingStub;
import com.seda.payer.integraottico.webservice.source.IntegraOtticoServiceLocator;
import com.seda.payer.notifiche.webservice.dati.GeneraPdfAutorizzazioniBancaResponse;
import com.seda.payer.notifiche.webservice.dati.GeneraPdfBollettiniResponse;
import com.seda.payer.notifiche.webservice.dati.GeneraPdfSingoloBollettinoRequest;
import com.seda.payer.notifiche.webservice.dati.GeneraPdfSingoloBollettinoRequestTipoBollettino;
import com.seda.payer.notifiche.webservice.source.NotificheSOAPBindingStub;
import com.seda.payer.notifiche.webservice.source.NotificheServiceLocator;
import com.seda.payer.ottico.facade.core.ElaboraFlussiImgHelper;
import com.seda.payer.ottico.facade.core.FlowFileUtility;
import com.seda.payer.ottico.facade.core.FlowManager;
import com.seda.payer.ottico.facade.core.FlowParameter;
import com.seda.payer.ottico.facade.core.FlowState;
import com.seda.payer.ottico.facade.core.OT0FlowData;
import com.seda.payer.ottico.facade.core.OT1FlowData;
import com.seda.payer.ottico.facade.core.OT9FlowData;
import com.seda.payer.ottico.webservice.dto.BaseSearchKeys;
import com.seda.payer.ottico.webservice.dto.CodiciDettaglio;
import com.seda.payer.ottico.webservice.dto.ConfigurazioneDto;
import com.seda.payer.ottico.webservice.dto.ConfigurazioneIdentify;
import com.seda.payer.ottico.webservice.dto.DateOtticoInfo;
import com.seda.payer.ottico.webservice.dto.DetailInfo;
import com.seda.payer.ottico.webservice.dto.DettaglioFlussoOtticoDto;
import com.seda.payer.ottico.webservice.dto.DirectoryInfo;
import com.seda.payer.ottico.webservice.dto.DocumentDetailInfo;
import com.seda.payer.ottico.webservice.dto.ElaboraFlussiImgState;
import com.seda.payer.ottico.webservice.dto.FilenameInfo;
import com.seda.payer.ottico.webservice.dto.ImmagineDto;
import com.seda.payer.ottico.webservice.dto.InformazioniAggiornamento;
import com.seda.payer.ottico.webservice.dto.InformazioniStampa;
import com.seda.payer.ottico.webservice.dto.TemplateDocumentoDto;
import com.seda.payer.ottico.webservice.dto.TestataFlussoOtticoDto;
import com.seda.payer.ottico.webservice.dto.TipoDocumento;
import com.seda.payer.ottico.webservice.dto.WebServiceInfo;
import com.seda.payer.ottico.webservice.dto.WebServiceInfo.WebServiceInfoType;
import com.seda.payer.ottico.webservice.exception.FacadeException;
import com.seda.payer.ottico.facade.handler.BaseFacadeHandler;
import com.seda.payer.ottico.facade.handler.OtticoConstraints;
import com.seda.payer.ottico.facade.pdf.DocumentoBeanCreator;
import com.seda.payer.ottico.facade.pdf.DocumentoType;
import com.seda.payer.ottico.facade.pdf.DocumentoXMLGenerator;
import com.seda.payer.ottico.facade.pdf.ReportCreator;
import com.seda.payer.ottico.facade.pdf.ScadenzaType;
import com.seda.payer.ottico.facade.pdf.TributoType;
import com.seda.payer.pgec.webservice.commons.dati.RecuperaTransazioniDocumentoECRequest;
import com.seda.payer.pgec.webservice.commons.dati.RecuperaTransazioniDocumentoECResponse;
import com.seda.payer.pgec.webservice.commons.dati.RecuperaTransazioniDocumentoECSingleResponse;
import com.seda.payer.pgec.webservice.commons.source.CommonsSOAPBindingStub;
import com.seda.payer.pgec.webservice.commons.source.CommonsServiceLocator;

public class OtticoFacadeBean extends BaseFacadeHandler {

	private static final long serialVersionUID = 1L;
	
	private FlowFileUtility flowFileUtil;
	private String logInfo = "";
	private String chiaveFlusso = "";
	private DirectoryInfo directory;
	//inizio LP PG200070 - 20200813
	//public OtticoFacadeBean() throws Exception {
	//	super();
	//	applicationStartup();
	//}
	public OtticoFacadeBean(PropertiesTree propertiesTree, Properties log4jConfiguration) throws Exception {
		super(propertiesTree, log4jConfiguration);
	}
	//fine LP PG200070 - 20200813

	@SuppressWarnings("unchecked")
	public ElaboraFlussiImgState elaboraFlussiImg(String dbSchemaCodSocieta) throws FacadeException {
		//logger.info("Inizio elaboraFlussoImg [");
		Connection conn = null;
		int numOfFileAligned = 0;
		int numOfFileDiscarded = 0;
		int numOfFileFounded = 0;
		int numOfConfigurationDiscarded = 0;
		try {
			
			conn = getConnection(dbSchemaCodSocieta);
			ConfigurazioneDao daoObj = new ConfigurazioneDao(conn, getSchema(dbSchemaCodSocieta));

			/* we retry all configurations */
			List confList = daoObj.doList();
			logger.debug("Numero di enti da processare - " + confList.size());
			Iterator listIterator = confList.iterator();
			while(listIterator.hasNext()) {
				Configurazione conf = (Configurazione)listIterator.next(); 
				logger.debug("Stai processando l'ente - " + conf);

				/* we get directory info */
				directory = ElaboraFlussiImgHelper.getDirectoryInfo(conf);
				logger.debug("Informazioni directory - " + directory);

				/* we retry user info */
				UserDao userDao = new UserDao(conn, getSchema(dbSchemaCodSocieta));
				User user = userDao.doDetail(conf.getCodiceSocieta(), conf.getCodiceUtente());

				/* we retry company info */
				CompanyDao companyDao = new CompanyDao(conn, getSchema(dbSchemaCodSocieta));
				Company company = companyDao.doDetail(conf.getCodiceSocieta());

				/* we retry ente info */
				AnagEnteDao anagEnteDao = new AnagEnteDao(conn, getSchema(dbSchemaCodSocieta));
				AnagEnte ente = anagEnteDao.doDetail(conf.getChiaveEnte());

				/* we notify incorrect directory */
				if (notifyMalformedDirectory(conf, directory, user, company, ente)) {
					/* we increase counter configuration discarded */
					numOfConfigurationDiscarded++;
					continue;
				}
				/* we create temporary directory to extract current elaboration if not exist */
				String directoryDiElaborazione = directory.getDirectoryFlussiDatiOtticoInput() + File.separator + "tmp" + File.separator;
				String directoryDiElaborazioneImage = directory.getDirectoryFlussiImmaginiOtticoInput() + File.separator + "tmp" + File.separator;
				
				logger.debug("Directory di elaborazione - " + directoryDiElaborazione );
				logger.debug("Directory di elaborazione image - " + directoryDiElaborazioneImage );

				/* we search DAT file */
				FileUtility.mkdir(directoryDiElaborazione);
				FileUtility.mkdir(directoryDiElaborazioneImage);

				String regExpr = "^DAT" + conf.getCodiceSocieta() + "" + conf.getCodiceUtente() + "" + conf.getCodiceEnte() + "(.*?)[\\|/]*.(zip)";
				logger.debug("Ricerca file - " + regExpr);
				TreeSet treeSet = FileUtility.find(directory.getDirectoryFlussiDatiOtticoInput() + File.separator, Pattern.compile(regExpr), FileUtility.ORDER_ASC);
				logger.debug("Numero file trovati - " + treeSet.size());

				/* we start processing */
				Iterator iter = treeSet.iterator();
				if (iter != null && !treeSet.isEmpty()) {
					while (iter.hasNext()) {
						File f = (File) iter.next();
						String nomeFileDatiZip = f.getName();
						logger.debug("Nome file zip - " + nomeFileDatiZip);
						String nomeFileDati = f.getName().substring(0, f.getName().indexOf(".")) + ".txt";
						String pathCompletoNomeFileDati = directoryDiElaborazione + File.separator + nomeFileDati;
						logger.debug("Nome file dati - " + nomeFileDati + " (path completo " + pathCompletoNomeFileDati + ")");
						/* we un_zip next f */
						logger.debug("Estrazione file dati - " + nomeFileDati + " (path completo " + pathCompletoNomeFileDati + ")");

						GZiper.unzip(directory.getDirectoryFlussiDatiOtticoInput() + File.separator + f.getName(), directoryDiElaborazione);
						/* we move file into process folder */
						FileUtility.move(f, directoryDiElaborazione);
						//logger.info("File dati da processare - " + pathCompletoNomeFileDati + "\n");

						/* we check If current flow is was exist */
						Boolean isWasExist = this.verificaFlusso(
								new ConfigurazioneIdentify(conf.getCodiceSocieta(), conf.getCodiceUtente(), conf.getChiaveEnte()), nomeFileDatiZip, dbSchemaCodSocieta);
						logger.debug("Risultato verifica flusso - " + isWasExist.booleanValue());
						/* we prepare flow info */
						FlowParameter flowStartInfo = new FlowParameter(pathCompletoNomeFileDati, nomeFileDati, null, conf, new Date(),
								directoryDiElaborazione, directoryDiElaborazioneImage, nomeFileDatiZip, null);
						logger.debug("Parametri iniziali per inizio processo - " + flowStartInfo.toString());
						flowFileUtil = new FlowFileUtility(flowStartInfo, logger);

						/* we elaborate pre_process info */
						OT0FlowData OT0Record = flowFileUtil.getOT0Line(pathCompletoNomeFileDati);
						String imgFileName = OT0Record.getFlussoImg();
						flowStartInfo.setNomeFileImg(imgFileName.trim());
						flowStartInfo.setTipologiaDocumenti(OT0Record.getDocType().trim());

						/* If flow exist */
						if (isWasExist.booleanValue()) {
							/**
							 * 1. SPOSTARE IL FILE DAT ESTRATTO PRECEDENTEMENTE NELLA DIRECTORY DI ELABORAZIONE (TMP) 
							 *    NELLA DIRECTORY DI SALVATAGGIO FLUSSO DATI
							 *    
							 * 2. SPOSTARE IL FILE IMG (imgFileName) NELLA DIRECTORY DI SALVATAGGIO FLUSSO IMMAGINI
							 * 
							 * 3. CANCELLARE LA DIRECTORY DI ELABORAZIONE (TMP) 
							 * 
							 * 4. INVIARE MAIL DI NOTIFICA MANCATO CARICAMENTO ALL'AMMINISTRATORE (conf.getIndirizzoEmailAmministratore())
							 */
							logger.debug("Entrato nell'IF di storicizzazione -> Il Flusso esiste sulla tabella PYTOTTB");
							// we move file into directorySalvataggoFlussiDatiOtticoInput
							FileUtility.move(new File(directory.getDirectoryFlussiDatiOtticoInput() + File.separator + "tmp" +File.separator + nomeFileDatiZip), directory.getDirectorySalvataggoFlussiDatiOtticoInput()+File.separator);
							/**
							 * ADESSO PER POTER SPOSTARE IL FILE ZIP DELLE IMMAGINI 
							 * NELLA DIRECTORY "directorySalvataggoFlussiImmaginiOtticoInput" 
							 * DOBBIAMO LEGGERE IL PRIMO RECORD (OT0) DEL FILE "nomeFileDati" 
							 * E DA QUESTO ESTRARRE, DALLA POSIZIONE 22 ALLA POSIZIONE 72
							 * IL NOME, SENZA ESTENZIONE, DEL FILE ZIP DELLE IMMAGINI. 
							 */
							FileUtility.move(new File(directory.getDirectoryFlussiImmaginiOtticoInput() + File.separator + imgFileName + ".zip"), directory.getDirectorySalvataggioFlussiImmaginiOtticoInput() + File.separator);
							new File(pathCompletoNomeFileDati).delete();
							new File(directory.getDirectoryFlussiImmaginiOtticoInput() + File.separator + imgFileName + ".zip").delete();
							/* we send email to administrator */
							String emailAdmin = conf.getIndirizzoEmailAmministratore();
							String text = MessageFormat.format(OtticoConstraints.MESSAGGIO_FLOW_EXIST, 
									new Object[] { directory.getDirectoryFlussiDatiOtticoInput() + File.separator + "tmp" + File.separator + nomeFileDatiZip });
							String subject = OtticoConstraints.SUBJECT;
							logger.debug("Stai inviando l'email all'admin con i seguenti parametri: \n" + 
									"Mail:  " + emailAdmin + "\n" +
									"Text: " + text + "\n" + 
									"Subject: " + subject);
							this.notificaMancatoAllineamento(text, emailAdmin, "", "", subject, "");
							continue;
						}
						if (!new File(directory.getDirectoryFlussiImmaginiOtticoInput() + File.separator + imgFileName + ".zip").isFile()){
							FileUtility.move(new File(directory.getDirectoryFlussiDatiOtticoInput() + File.separator + "tmp" + File.separator + nomeFileDatiZip), directory.getDirectorySalvataggoFlussiDatiOtticoInput() + File.separator);
							new File(pathCompletoNomeFileDati).delete();
							String emailAdmin = conf.getIndirizzoEmailAmministratore();
							String text = MessageFormat.format(OtticoConstraints.MESSAGGIO_FILE_IMG_NOT_EXIST, 
									new Object[] { directory.getDirectoryFlussiDatiOtticoInput() + File.separator + "tmp" + File.separator + nomeFileDatiZip });
							String subject = OtticoConstraints.SUBJECT;
							logger.debug("Stai inviando l'email all'admin con i seguenti parametri: \n" + 
									"Mail:  " + emailAdmin + "\n" +
									"Text: " + text + "\n" + 
									"Subject: " + subject);
							this.notificaMancatoAllineamento(text, emailAdmin, "", "", subject, "");
							continue;
						} 
						/* we increase counter file founded */
						numOfFileFounded++;
						logger.debug("Stai estraendo il file immagini: \n" +
								"Dir Input: " + directory.getDirectoryFlussiImmaginiOtticoInput() + "\n" +
								"FileImg: " + imgFileName + ".zip" + "\n" +
								"Dir Work: " + directoryDiElaborazioneImage);
						GZiper.unzip(directory.getDirectoryFlussiImmaginiOtticoInput() + File.separator + imgFileName + ".zip", directoryDiElaborazioneImage);
						FileUtility.move(new File(directory.getDirectoryFlussiImmaginiOtticoInput() + File.separator + imgFileName + ".zip"), directoryDiElaborazioneImage);

						/* we prepare info to process */
						flowFileUtil.prepareData(flowStartInfo.getPathCompletoNomeFileDati());
						flowFileUtil.getFlowInfo().setNomeFileImg(imgFileName);
						logger.debug("Parametri finali per inizio processo - " + flowStartInfo);

						/* we start flow process manager */
						logger.debug("Inizio processo di elaborazione...");
						FlowManager flow = new FlowManager(flowFileUtil, logger);
						FlowState processInfo = flow.start(); 
						logger.debug("Stato elaborazione - " + processInfo.getCode());

						/* we manage validation output */
						if (processInfo.getCode() == FlowState.DISCARDED) {
							/* we send mail to administrator */
							String emailAdmin = conf.getIndirizzoEmailAmministratore();
							String text = MessageFormat.format(OtticoConstraints.MESSAGGIO_FLOW_ERROR, 
									new Object[] { directory.getDirectoryFlussiDatiOtticoInput() + File.separator + "tmp" + File.separator + nomeFileDatiZip }) 
									+ "<br/>" + processInfo.getMessage();
							String subject = OtticoConstraints.SUBJECT;
							this.notificaMancatoAllineamento(text, emailAdmin, "", "", subject, "");
							/* Stiamo salvando i file scartati sulla cartella old*/
							FileUtility.move(new File(
									this.flowFileUtil.getFlowInfo().getElabPath()+this.flowFileUtil.getFlowInfo().getNomeFileDatiZip()), 
									this.flowFileUtil.getFlowInfo().getCurrentConfig().getDirectorySalvataggoFlussiDatiOtticoInput()+File.separator);
							FileUtility.move(new File(
									this.directory.getDirectoryFlussiImmaginiOtticoInput()+File.separator+"tmp"+File.separator+this.flowFileUtil.getFlowInfo().getNomeFileImg().trim()+".zip"), 
									this.flowFileUtil.getFlowInfo().getCurrentConfig().getDirectorySalvataggioFlussiImmaginiOtticoInput()+File.separator);
							/* we increase counter file discarded */
							numOfFileDiscarded++;
						} else {
							logger.debug("Inizio scansione file - " + this.flowFileUtil.getFlowPath());
							this.fileScan(this.flowFileUtil.getFlowPath(), dbSchemaCodSocieta);
							/* we increase counter file discarded */
							numOfFileAligned++;
						}
					} // end while file data founded 
				}
				FileUtility.rm(directoryDiElaborazione, "*.*", true);
				FileUtility.rm(directoryDiElaborazioneImage, "*.*", true);
			} // end while configurations
//			logger.info("] Fine elaboraFlussoImg");
			/* we check state all file data process and configurations */
			if (numOfConfigurationDiscarded != 0 
					&& numOfFileAligned == 0 
					&& numOfFileDiscarded == 0
					&& numOfFileFounded == 0)
				return ElaboraFlussiImgState.ErrorConfiguration;
			else if (numOfFileFounded != 0 && numOfFileAligned == 0)
				return ElaboraFlussiImgState.NotFound;
			else if (numOfFileDiscarded != 0 && numOfFileAligned == 0)
				return ElaboraFlussiImgState.Error;
			else return ElaboraFlussiImgState.Success;

		} catch (Exception ex) {
			logger.error("elaboraFlussiImg failed, generic error due to: ", ex);
			return ElaboraFlussiImgState.Error;
		} finally {
			//inizio LP PG21XX04 Leak
    		//closeConnection(conn);
	    	try {
	    		if(conn != null) {
	    			conn.close();
	    		}
	    	} catch (SQLException e) {
	    		e.printStackTrace();
			}
			//fine LP PG21XX04 Leak
		}
	}

	/*2. */
	/**
	 * @ejb.interface-method
	 * @ejb.transaction type="NotSupported"
	 */ 
	public String recuperaTemplate(String tipoDocumento,ConfigurazioneIdentify srcKey, String tipologiaServizio, String tipoBolletino,
			Timestamp dataValidita, String dbSchemaCodSocieta) throws FacadeException {
		Connection conn = null;
		try {	
			//inizio LP PG21XX04 Bug "You cannot commit with autocommit set" 
			//conn = getTransactionConnection(dbSchemaCodSocieta);
			conn = getConnection(dbSchemaCodSocieta);
			//fine LP PG21XX04 Bug "You cannot commit with autocommit set" 
//			logger.debug("\nTEST: FACADE-OTTICO-RECUPERA.TEMPLATE Stai recuperando il template con i seguenti parametri: " +
//					"DOCTYPE =  " + tipoDocumento + "\n" + 
//					"CSOC = " + srcKey.getCodiceSocieta() + "\n" + 
//					"CUTE = " + srcKey.getCodiceUtente() + "\n" + 
//					"CENT = " + srcKey.getCodiceEnte() + "\n" + 
//					"TSER = " + tipologiaServizio + "\n" + 
//					"TBOL = " + tipoBolletino + "\n" + 
//					"DAT = " + dataValidita + "\n" + 
//					"DAT = " + dataValidita + "\n" +
//					"-----------------------------------------\n\n"
			//);
			TemplateDocumentoDto templateDocumento = new TemplateDocumentoDto(new TemplateDocumentoDao(conn, getSchema(dbSchemaCodSocieta)).doDetail(tipoDocumento, 
					srcKey.getCodiceSocieta(), srcKey.getCodiceUtente(), srcKey.getCodiceEnte(), 
					tipologiaServizio, tipoBolletino, dataValidita, dataValidita, null));
//			logger.info("recuperaTemplate - " + templateDocumento);
			return !StringUtility.isEmpty(templateDocumento.getRiferimentoTemplate()) ? templateDocumento.getRiferimentoTemplate() : null;

		} catch (Exception ex) {
			logger.error("recuperaTemplate failed, generic error due to: ", ex);
			throw new FacadeException(ex);
		} finally {
			//inizio LP PG21XX04 Leak
    		//closeConnection(conn);
	    	try {
	    		if(conn != null) {
	    			conn.close();
	    		}
	    	} catch (SQLException e) {
	    		e.printStackTrace();
			}
			//fine LP PG21XX04 Leak
		}
	}

	/*3. */
	/**
	 * @ejb.interface-method
	 * @ejb.transaction type="NotSupported"
	 */ 
	public String stampaBollettino(InformazioniStampa printInfo, String dbSchemaCodSocieta) throws FacadeException {
		
		List<String> listaReportNames = new ArrayList<String>();
		try {

			//1.Verifico se ci sono pagamenti effettuati da PayER di cui stampare i bollettini (con template)
			if (printInfo.getCachedRowSetMovimenti() != null && !printInfo.getCachedRowSetMovimenti().equals(""))
			{
				listaReportNames = generaPdfTemplate(printInfo, "BOL", dbSchemaCodSocieta);
			}
					
			try
			{
				//2.Recupero immagini (integraOttico)
				RecuperaImmagineResponse response = recuperaImmagine(new RecuperaImmagineRequest(
						new FiltriDiRicerca(printInfo.getBaseSearchKeys().getCodiceSocieta(), 
								printInfo.getBaseSearchKeys().getCodiceUtente(), 
								printInfo.getBaseSearchKeys().getCodiceEnte(), 
								"BOL", 
								printInfo.getNumeroDocumento())), printInfo.getWebServiceOttico(), dbSchemaCodSocieta);
				
				if (response != null && response.getEsito().equals("00") && 
						response.getBollettini() != null && response.getBollettini().length > 0)
				{
					Bollettino[] bollettini = response.getBollettini();
					for (Bollettino bollettino : bollettini) {
						//String nomeReport = bollettino.getCodiceBollettino() + "_" + progressivoNomeReport + "." + response.getEstensioneImmagine();
						String nomeReport = bollettino.getNomeFisicoImmagine();
						FileUtility.wr(bollettino.getImmagineDocumento(), this.getOutputDirectoryPdf(), nomeReport);
						listaReportNames.add(this.getOutputDirectoryPdf() + nomeReport);
					}
				}
			} catch (Exception ex){
				logger.error("ERRORE stampaBollettino recuperaImmagine", ex);
			}
			
			if (listaReportNames.size() > 0)
			{
				//ZIPPO I FILE (template + immagini)
				String compressedReportsFileName = printInfo.getNumeroDocumento() + "_bollettini_" + System.currentTimeMillis() + ".zip";
				GZiper.zipFiles(listaReportNames.toArray(new String[listaReportNames.size()]), compressedReportsFileName, this.getOutputDirectoryPdf());
				return this.getOutputDirectoryPdf() + compressedReportsFileName;
			}
			
		} catch (MalformedURLException e) {
			e.printStackTrace();
		} catch (ServiceException e) {
			e.printStackTrace();
		} catch (RemoteException e) {
			e.printStackTrace();
		} catch (Exception ex) {
			logger.error("stampaBollettino failed, generic error due to: ", ex);
		}
		return "";
	}


	private List<String> generaPdfTemplate(InformazioniStampa printInfo, String tipoStampa, String dbSchemaCodSocieta) 
	{
		List<String> listFilePath = new ArrayList<String>();
		try
		{
			//1a.Recupero le transazioni relative al documento
			RecuperaTransazioniDocumentoECRequest req = new RecuperaTransazioniDocumentoECRequest();
			req.setCodiceSocieta(printInfo.getBaseSearchKeys().getCodiceSocieta());
			req.setCodiceUtente(printInfo.getBaseSearchKeys().getCodiceUtente());
			req.setChiaveEnte(printInfo.getBaseSearchKeys().getChiaveEntePayer());
			req.setNumeroDocumento(printInfo.getNumeroDocumento());
			
			CommonsServiceLocator commonsServiceLocator = new CommonsServiceLocator();
			CommonsSOAPBindingStub commonsPort = (CommonsSOAPBindingStub)commonsServiceLocator.getCommonsPort(new URL(getWsCommonsUrl()));
			setCodSocietaHeaderCommons(commonsPort, dbSchemaCodSocieta);
			
			RecuperaTransazioniDocumentoECResponse res = commonsPort.recuperaTransazioniDocumentoEC(req);
			if (res != null && res.getResponse().getRetCode().getValue().equals("00") && 
					res.getListaTransazioniDocumentoEC() != null && res.getListaTransazioniDocumentoEC().length > 0)
			{
				RecuperaTransazioniDocumentoECSingleResponse[] arrayTransazioni = res.getListaTransazioniDocumentoEC();
				
				//1b.Per ogni movimento devo verificare se c'è una corrispondente transazione di pari importo e data
				//1b1.Seleziono tutte le date pagamento distinte presenti nei movimenti
				//e per ogni data pagamento sommo tutti gli importi dei movimenti
				
				//chiave=data pagamento(dd/MM/yyyy) - valore=importo totale
				HashMap<String,BigDecimal> hmDateMovimenti = new HashMap<String,BigDecimal>(); 
				WebRowSet wrsListaMovimenti = Convert.stringToWebRowSet(printInfo.getCachedRowSetMovimenti());
				if (wrsListaMovimenti != null) 
				{
					/*
					 * 2.Data Pagamento (dd/MM/yyyy)
					 * 3.Tipo Operazione
					 * 4.Luogo Movimento
					 * 5.Tributi € (#,00)
					 * 6.Dir. Notifica € (#,00)
					 * 7.Compensi € (#,00)
					 * 8.Int. Mora € (#,00)
					 * 9.Altre Spese € (#,00)
					 */
					while (wrsListaMovimenti.next()) 
					{
						String dataPagamento = wrsListaMovimenti.getString(2);
						BigDecimal tributi = getBigDecimalFromString(wrsListaMovimenti.getString(5));
						BigDecimal dirNotifica = getBigDecimalFromString(wrsListaMovimenti.getString(6));
						BigDecimal compensi = getBigDecimalFromString(wrsListaMovimenti.getString(7));
						BigDecimal intMora = getBigDecimalFromString(wrsListaMovimenti.getString(8));
						BigDecimal altreSpese = getBigDecimalFromString(wrsListaMovimenti.getString(9));
						
						BigDecimal bdTot = tributi.add(dirNotifica).add(compensi).add(intMora).add(altreSpese);
						if (!hmDateMovimenti.containsKey(dataPagamento))
							hmDateMovimenti.put(dataPagamento, bdTot);
						else
							hmDateMovimenti.put(dataPagamento, hmDateMovimenti.get(dataPagamento).add(bdTot)); 
					}
					//inizio LP PG21XX04 Leak
					wrsListaMovimenti.close();
					//fine LP PG21XX04 Leak
				}
				
				
				//chiave=chiaveBollettino; valore=chiaveTransazione
				HashMap<String,String> hmChiaviBollettiniIV = new HashMap<String,String>();
				HashMap<String,String> hmChiaviBollettiniFreccia = new HashMap<String,String>();
				for (String dataPagamento : hmDateMovimenti.keySet())
				{
					BigDecimal bdImportoTotTransazioniData = BigDecimal.ZERO;
					HashMap<String,String> hmChiaviBollettiniIVTemp = new HashMap<String,String>();
					HashMap<String,String> hmChiaviBollettiniFrecciaTemp = new HashMap<String,String>();
					for (RecuperaTransazioniDocumentoECSingleResponse trans : arrayTransazioni)
					{
						//1b3.Ricerco tra le transazioni tutte quelle con data pagamento uguale a quella del movimento
						//e sommo gli importi
						String dataTrans = formatCalendarData(trans.getBeanTransazioni().getBeanTransazioni().getData_effettivo_pagamento_su_gateway(),
								"dd/MM/yyyy");
						if (dataTrans.equals(dataPagamento))
						{
							if (trans.getBeanTransazioniIV() != null)
							{
								bdImportoTotTransazioniData = bdImportoTotTransazioniData.add(trans.getBeanTransazioniIV().getImporto_totale_bollettino());
								hmChiaviBollettiniIVTemp.put(trans.getBeanTransazioniIV().getChiave_transazione_dettaglio(), trans.getBeanTransazioniIV().getChiave_transazione_generale());
							}
							else if (trans.getBeanTransazioniFreccia() != null)
							{
								bdImportoTotTransazioniData = bdImportoTotTransazioniData.add(trans.getBeanTransazioniFreccia().getImporto_totale_bollettino());
								hmChiaviBollettiniFrecciaTemp.put(trans.getBeanTransazioniFreccia().getChiave_transazione_dettaglio(), trans.getBeanTransazioniFreccia().getChiave_transazione_generale());
							}
						}
					}
					//1b4.Se data e importi corrispondono ritorno le chiavi transazioni / bollettino corrispondenti
					//(recuperandole da quelle salvate nella struttura temporanea)
					if (bdImportoTotTransazioniData.compareTo(hmDateMovimenti.get(dataPagamento)) == 0)
					{
						for (String chiave : hmChiaviBollettiniIVTemp.keySet())
							hmChiaviBollettiniIV.put(chiave, hmChiaviBollettiniIVTemp.get(chiave));
						
						for (String chiave : hmChiaviBollettiniFrecciaTemp.keySet())
							hmChiaviBollettiniFreccia.put(chiave, hmChiaviBollettiniFrecciaTemp.get(chiave));
					}
				}
			
				
				//1c.Se trovo la transazione corrispondente, genero il report
				NotificheServiceLocator notificheServiceLocator = new NotificheServiceLocator();
				NotificheSOAPBindingStub notifichePort = (NotificheSOAPBindingStub)notificheServiceLocator.getNotifichePort(new URL(getWsNotificheUrl()));
				setCodSocietaHeaderNotifiche(notifichePort, dbSchemaCodSocieta);
				
				if (tipoStampa.equals("BOL"))
				{
					List<GeneraPdfSingoloBollettinoRequest> listBollettini = null;
					
					//per ogni bollettino pagato con payer rigenero il pdf del bollettino
					for (String chiaveBoll : hmChiaviBollettiniIV.keySet())
					{
						GeneraPdfSingoloBollettinoRequest singleReq = new GeneraPdfSingoloBollettinoRequest();
						singleReq.setChiaveBollettino(chiaveBoll); 
						singleReq.setChiaveTransazione(hmChiaviBollettiniIV.get(chiaveBoll)); 
						singleReq.setTipoBollettino(GeneraPdfSingoloBollettinoRequestTipoBollettino.IV);
						if (listBollettini == null)
							listBollettini = new ArrayList<GeneraPdfSingoloBollettinoRequest>();
						listBollettini.add(singleReq);
					}
					
					for (String chiaveBoll : hmChiaviBollettiniFreccia.keySet())
					{
						GeneraPdfSingoloBollettinoRequest singleReq = new GeneraPdfSingoloBollettinoRequest();
						singleReq.setChiaveBollettino(chiaveBoll); 
						singleReq.setChiaveTransazione(hmChiaviBollettiniFreccia.get(chiaveBoll)); 
						singleReq.setTipoBollettino(GeneraPdfSingoloBollettinoRequestTipoBollettino.FRE);
						if (listBollettini == null)
							listBollettini = new ArrayList<GeneraPdfSingoloBollettinoRequest>();
						listBollettini.add(singleReq);
					}
					
					if (listBollettini != null && listBollettini.size() > 0)
					{
						GeneraPdfSingoloBollettinoRequest[] arrayBollettini = (GeneraPdfSingoloBollettinoRequest[])listBollettini.toArray(new GeneraPdfSingoloBollettinoRequest[listBollettini.size()]);
						GeneraPdfBollettiniResponse resPdf = notifichePort.generaPdfBollettini(arrayBollettini);
						if (resPdf != null && resPdf.getResponse().getRetcode().getValue().equals("00"))
						{
							if (resPdf.getListPdfFilePath() != null && resPdf.getListPdfFilePath().length > 0)
								listFilePath.addAll(Arrays.asList(resPdf.getListPdfFilePath()));
						}
					}
				}
				else if (tipoStampa.equals("NAB"))
				{
					//recupero le transazioni DISTINCT
					List<String> listChiaviTransazioni = new ArrayList<String>();
					for (String chiaveBoll : hmChiaviBollettiniIV.keySet())
					{
						if (!listChiaviTransazioni.contains(hmChiaviBollettiniIV.get(chiaveBoll)))
							listChiaviTransazioni.add(hmChiaviBollettiniIV.get(chiaveBoll));
					}
					for (String chiaveBoll : hmChiaviBollettiniFreccia.keySet())
					{
						if (!listChiaviTransazioni.contains(hmChiaviBollettiniFreccia.get(chiaveBoll)))
							listChiaviTransazioni.add(hmChiaviBollettiniFreccia.get(chiaveBoll));
					}
					
					//per ogni transazione payer rigenero il pdf di notifica autorizzazione banca
					if (listChiaviTransazioni != null && listChiaviTransazioni.size() > 0)
					{
						GeneraPdfAutorizzazioniBancaResponse resPdf = notifichePort.generaPdfAutorizzazioniBanca(listChiaviTransazioni.toArray(new String[listChiaviTransazioni.size()]));
						if (resPdf != null && resPdf.getResponse().getRetcode().getValue().equals("00"))
						{
							if (resPdf.getListPdfFilePath() != null && resPdf.getListPdfFilePath().length > 0)
								listFilePath.addAll(Arrays.asList(resPdf.getListPdfFilePath()));
						}
					}
				}
			}
			
		} catch (MalformedURLException e) {
			e.printStackTrace();
		} catch (ServiceException e) {
			e.printStackTrace();
		} catch (RemoteException e) {
			e.printStackTrace();
		} catch (Exception ex) {
			logger.error("generaPdfTemplate failed, generic error due to: ", ex);
		}
		return listFilePath;
	}
	
	private BigDecimal getBigDecimalFromString(String sImporto)
	{
		try
		{
			//importo = ZZZZ,YY
			String[] aImporto = sImporto.split("\\,");
			if (aImporto.length == 2)
			{
				String sParteIntera = aImporto[0];
				String sParteDecimale = aImporto[1];
				if (sParteIntera.equals(""))
					sParteIntera = "0";
				if (sParteDecimale.equals(""))
					sParteDecimale = "00";
				
				BigDecimal bdRes = new BigDecimal(0);
				if (sParteIntera.length()!=0 && sParteDecimale.length()!=0)
				{
					double dParteDecimale = Integer.valueOf(sParteDecimale) / 100.0;
					double dRes = Integer.valueOf(sParteIntera) + dParteDecimale;
					
					bdRes = new BigDecimal(dRes);
				}
				
				bdRes = bdRes.setScale(2, BigDecimal.ROUND_HALF_UP);
				return bdRes;
			}
			else
				return BigDecimal.ZERO;
		} catch (Exception e) {
			return BigDecimal.ZERO;
		}
	}
	
	private static String formatCalendarData(Calendar data, String sFormato)
	{
		if (data != null)
		{
			Calendar cal = Calendar.getInstance(Locale.ITALIAN);
			cal.setTime(data.getTime());
		
			SimpleDateFormat formatDateTime = new SimpleDateFormat(sFormato);
			return formatDateTime.format(cal.getTime());
		}
		else
			return "";
	}
	
	/**
	 * @ejb.interface-method
	 * @ejb.transaction type="NotSupported"
	 */ 
	public String stampaDocumento(InformazioniStampa printInfo, String dbSchemaCodSocieta) throws FacadeException{
		List<String> listaReportNames = new ArrayList<String>();
		String timestamp = String.valueOf(System.currentTimeMillis());
		try {
			// RP 21/08/2013 Inizio
			String nomeDocWallet="";
			// RP 21/08/2013 Fine
			if (printInfo.getFlagDocumento().equals("T")) 
			{
				//TEMPLATE
				//inizio LP PG200070 - 20200813
				//ReportCreator reportCreator = new ReportCreator(
				//		this.getBirtHomeEngine(), this.getBirtHomeLogs(), this.getOutputDirectoryPdf(), 
				//		this.getBirtDesignPath(), this.getPathLogoReport(), logger, propertiesTree());
				ReportCreator reportCreator = new ReportCreator(
						this.getBirtHomeEngine(), this.getBirtHomeLogs(), this.getOutputDirectoryPdf(), 
						this.getBirtDesignPath(), this.getPathLogoReport(), logger, propertiesTree);
				//fine LP PG200070 - 20200813
				// we instantiate DocumentoBeanCreator and DocumentoXMLWriter 
				DocumentoBeanCreator beanCreator = new DocumentoBeanCreator(logger);
				
				//DocumentoXMLWriter writer = new DocumentoXMLWriter(this.getOutputDirectoryPdf());
				DocumentoXMLGenerator xmlGenerator = new DocumentoXMLGenerator();
				
				// we prepare and write XML DocumentoType, ScadenzaType and TributoType 
				DocumentoType documento = beanCreator.createListaDoc(printInfo.getCachedRowSetDoc());
				ScadenzaType[] scadenze = beanCreator.createListaScadenze(printInfo.getCachedRowSetScadenze());
				TributoType[] tributi = beanCreator.createListaTributi(printInfo.getCachedRowSetTributi());
				/*String xmlPathDocumento = writer.writeXMLListDocumento(documento, printInfo.getNumeroDocumento(), timestamp);
				String xmlPathScadenze = writer.writeXMLScadenze(scadenze, printInfo.getNumeroDocumento(), timestamp);
				String xmlPathTributi = writer.writeXMLTributi(tributi, printInfo.getNumeroDocumento(), timestamp);*/
				String xmlData = xmlGenerator.writeXMLListDocumento(documento, scadenze, tributi);
				// we create PDF document 
				boolean bTributiVisible = tributi != null && tributi.length > 0;
				if (tributi.length == 1)
				{
					//verifico se l'unico elemento presente contiene dei dati
					bTributiVisible = tributi[0].getCodiceTributoNonAddizionali().length() > 0 ||
						tributi[0].getAnnoTributo().length() > 0 ||
						tributi[0].getProgressivoTributo().length() > 0 ||
						tributi[0].getTipoTributo().length() > 0 ||
						tributi[0].getImportoTributo().length() > 0 ||
						tributi[0].getImpPagatoConSgravi().length() > 0 ||
						tributi[0].getNoteTributo().length() > 0;
					
				}
				String pathReport = reportCreator.generaPDFDocumento(printInfo, documento.getTipologiaServizio(), timestamp, xmlData, bTributiVisible);
				if (pathReport != null)
				{
					listaReportNames.add(pathReport);
//					logger.info("stampaDocumento - report path - " + pathReport);
				}
			} 
			else if (printInfo.getFlagDocumento().equals("I")) 
			{
				//IMMAGINI
				//Recupero immagini documento (integraOttico)
				RecuperaImmagineResponse response = recuperaImmagine(new RecuperaImmagineRequest(
						new FiltriDiRicerca(printInfo.getBaseSearchKeys().getCodiceSocieta(), 
								printInfo.getBaseSearchKeys().getCodiceUtente(), 
								printInfo.getBaseSearchKeys().getCodiceEnte(), 
								"DOC", 
								printInfo.getNumeroDocumento(),
								printInfo.getCodiceFiscale(),
								printInfo.getCodiceImpostaServizio())), 
								printInfo.getWebServiceOttico(),dbSchemaCodSocieta);
				
				if (response != null && response.getEsito().equals("00") && 
						response.getDocumento() != null)
				{
					Documento documento = response.getDocumento();
					//String nomeReport = reportCreator.generaNomeFilePDFDocumento(printInfo.getNumeroDocumento());
					String nomeReport = documento.getNomeFisicoImmagine();
					FileUtility.wr(documento.getImmagineDocumento(), this.getOutputDirectoryPdf(), nomeReport);
					listaReportNames.add(this.getOutputDirectoryPdf() + nomeReport);
					//RP 21/08/2013 Inizio
					nomeDocWallet=documento.getNomeFisicoImmagine();
					
					//RP 21/08/2013 Fine	
				}
			} 
			else 
				throw new Exception("stampaDocumento: Il campo flagDocumento ha un valore non consentito (valori consentiti T - Template o I - Immagine)");
			
			//if (listaReportNames.size() > 0 && !nomeDocWallet.substring(0,5).equals(printInfo.getBaseSearchKeys().getCodiceEnte()) && !nomeDocWallet.startsWith("EC_"))
			
			if (listaReportNames.size() > 1 && !nomeDocWallet.substring(0,5).equals(printInfo.getBaseSearchKeys().getCodiceEnte()) && !nomeDocWallet.startsWith("EC_")) 
			{
				//ZIPPO IL FILE
				
					String compressedReportsFileName = printInfo.getNumeroDocumento() + "_documento_" + timestamp + ".zip";
					GZiper.zipFiles(listaReportNames.toArray(new String[listaReportNames.size()]), compressedReportsFileName, this.getOutputDirectoryPdf());
					return this.getOutputDirectoryPdf() + compressedReportsFileName;
				
				
			}
			//RP 21/08/2013 Inizio
			else if (listaReportNames.size() > 0) return this.getOutputDirectoryPdf()+nomeDocWallet;
			//RP 21/08/2013 Fine
		} catch (Exception ex) {
			ex.printStackTrace();
			logger.error("stampaDocumento failed, generic error due to: ", ex);
		} 
		
		return null;
	}

	
	public String stampaAutorizzazioneBanca(InformazioniStampa printInfo, String dbSchemaCodSocieta) throws FacadeException {
		List<String> listaReportNames = new ArrayList<String>();
		try {

			//1.Verifico se ci sono pagamenti effettuati da PayER di cui stampare i bollettini (con template)
			if (printInfo.getCachedRowSetMovimenti() != null && !printInfo.getCachedRowSetMovimenti().equals(""))
			{
				listaReportNames = generaPdfTemplate(printInfo, "NAB", dbSchemaCodSocieta);
			}
			if (listaReportNames.size() > 0)
			{
				//ZIPPO I FILE (template + immagini)
				String compressedReportsFileName = printInfo.getNumeroDocumento() + "_quietanza_" + System.currentTimeMillis() + ".zip";
				GZiper.zipFiles(listaReportNames.toArray(new String[listaReportNames.size()]), compressedReportsFileName, this.getOutputDirectoryPdf());
				return this.getOutputDirectoryPdf() + compressedReportsFileName;
			}
			
		} catch (MalformedURLException e) {
			e.printStackTrace();
		} catch (ServiceException e) {
			e.printStackTrace();
		} catch (RemoteException e) {
			e.printStackTrace();
		} catch (Exception ex) {
			logger.error("stampaBollettino failed, generic error due to: ", ex);
		}

		return null;
	}

	
	public String stampaRelata(InformazioniStampa printInfo, String dbSchemaCodSocieta) throws FacadeException 
	{
		List<String> listaReportNames = new ArrayList<String>();
		try
		{
			//Recupero immagini relata (integraOttico)
			RecuperaImmagineResponse response = recuperaImmagine(new RecuperaImmagineRequest(
					new FiltriDiRicerca(printInfo.getBaseSearchKeys().getCodiceSocieta(), 
							printInfo.getBaseSearchKeys().getCodiceUtente(), 
							printInfo.getBaseSearchKeys().getCodiceEnte(), 
							"REL", 
							printInfo.getNumeroDocumento())), 
							printInfo.getWebServiceOttico(), dbSchemaCodSocieta);
			
			if (response != null && response.getEsito().equals("00") && 
					response.getRelate() != null && response.getRelate().length > 0)
			{
				//int progressivoNomeReport = 1;
				Relata[] relate = response.getRelate();
				for (Relata relata : relate) {
					//String nomeReport = relata.getCodiceRelata().trim() + "_" + progressivoNomeReport + "." + response.getEstensioneImmagine().trim();
					String nomeReport = relata.getNomeFisicoImmagine();
					FileUtility.wr(relata.getImmagineRelata(), this.getOutputDirectoryPdf(), nomeReport);
					listaReportNames.add(this.getOutputDirectoryPdf() + nomeReport);
					//progressivoNomeReport++;		
				}
			}
			
			if (listaReportNames.size() > 0)
			{
				//ZIPPO I FILE
				String compressedReportsFileName = printInfo.getNumeroDocumento() + "_relate_" + System.currentTimeMillis() + ".zip";
				GZiper.zipFiles(listaReportNames.toArray(new String[listaReportNames.size()]), compressedReportsFileName, this.getOutputDirectoryPdf());
				return this.getOutputDirectoryPdf() + compressedReportsFileName;
			}
			
		} catch (Exception ex){
			ex.printStackTrace();
			logger.error("stampaRelata failed, generic error due to: ", ex);
		}
		return null;
	}

	/*7. */
	/**
	 * @ejb.interface-method
	 * @ejb.transaction type="NotSupported"
	 */
	public ConfigurazioneDto verificaConfigurazioneOttico(ConfigurazioneIdentify srcKey, String dbSchemaCodSocieta) throws FacadeException {
		Connection conn=null;
		try {
			conn=getConnection(dbSchemaCodSocieta);
			ConfigurazioneDao daoObj = new ConfigurazioneDao(conn, getSchema(dbSchemaCodSocieta));
			Configurazione bean = daoObj.verificaConfigurazioneOttico(srcKey.getCodiceSocieta(), srcKey.getCodiceUtente(), srcKey.getCodiceEnte());
			
			if (bean != null)
			{
				//we define base search keys
				ConfigurazioneIdentify pk = new ConfigurazioneIdentify(bean.getCodiceSocieta(),bean.getCodiceUtente(),bean.getCodiceEnte());
				//we define type doc
				TipoDocumento docType = new TipoDocumento(bean.getFlagVisualizzaBollettino(), bean.getFlagVisualizzaDocumento(),
						bean.getFlagVisualizzaQuietanza(), bean.getFlagVisualizzaRelata());
				//we define web service info    		
				WebServiceInfo wsInfo = new WebServiceInfo(bean.getIndirizzoServerOttico(), bean.getUserServerOttico(),
						bean.getPasswordServerOttico(), bean.getIndirizzoEmailAmministratore(), 
						WebServiceInfoType.parse(bean.getFlagWebServiceOttico()));
				//we define directory info
				DirectoryInfo dirInfo = new DirectoryInfo(bean.getDirectoryFlussiDatiOtticoInput(), bean.getDirectorySalvataggoFlussiDatiOtticoInput(),
						bean.getDirectoryFlussiImmaginiOtticoInput(), bean.getDirectorySalvataggioFlussiImmaginiOtticoInput(), 
						bean.getDirectoryLogElaborazione(), bean.getDirectoryImmaginiOtticoPerEstrattoConto());
				//we define update info
				InformazioniAggiornamento infoUpdt = new InformazioniAggiornamento(bean.getDataUltimoAggiornamento(), bean.getOperatoreUltimoAggiornamento());
				//we create dto object
				ConfigurazioneDto dtoObj = new ConfigurazioneDto(pk).setDocType(docType).setWsInfo(wsInfo).setInfoUpdate(infoUpdt).setDirInfo(dirInfo);
	
				return dtoObj;
			}
			else
				return null; //potrebbe non esistere la configurazione dell'ottico per l'ente richiesto 

		} catch (Exception ex) {
			logger.error("verificaConfigurazioneOttico failed, generic error due to: ", ex);
			throw new FacadeException(ex);
		} finally {
			//inizio LP PG21XX04 Leak
    		//closeConnection(conn);
	    	try {
	    		if(conn != null) {
	    			conn.close();
	    		}
	    	} catch (SQLException e) {
	    		e.printStackTrace();
			}
			//fine LP PG21XX04 Leak
		}
	}

	/*8. */
	/**
	 * @ejb.interface-method
	 * @ejb.transaction type="NotSupported"
	 */
	public Boolean verificaFlusso(ConfigurazioneIdentify srcKey, String nomeFileDati, String dbSchemaCodSocieta) throws FacadeException {
		Connection conn=null;
		try {
			conn=getConnection(dbSchemaCodSocieta);
			logger.debug("Chiamato metodo verificaFlusso con i parametri: CHIAVE: " + srcKey + "\nFILE DATI: " + nomeFileDati);
			TestataFlussoOtticoDao daoObj = new TestataFlussoOtticoDao(conn, getSchema(dbSchemaCodSocieta));
			TestataFlussoOttico bean = daoObj.verificaFlusso(srcKey.getCodiceSocieta(),srcKey.getCodiceUtente(),srcKey.getCodiceEnte(), nomeFileDati);

			return bean != null ? Boolean.TRUE : Boolean.FALSE;

		} catch (Exception ex) {
			logger.error("verificaFlusso failed, generic error due to: ", ex);
			throw new FacadeException(ex);
		} finally {
			//inizio LP PG21XX04 Leak
    		//closeConnection(conn);
	    	try {
	    		if(conn != null) {
	    			conn.close();
	    		}
	    	} catch (SQLException e) {
	    		e.printStackTrace();
			}
			//fine LP PG21XX04 Leak
		}
	}
	/**
	 * @ejb.interface-method
	 * @ejb.transaction type="NotSupported"
	 */
	public void logElaborazioneFlussiImg(String testoLog, String directoryLog, String nomeFileLog, String dbSchemaCodSocieta){
		//we define filename	
		String fileName = directoryLog+nomeFileLog;
		try {
			//we create or append file
			File logFile = new File(fileName);
			boolean result = logFile.createNewFile();
			logger.debug("STATUS FILE: " + result);
			//we write log file	
			String[] split = testoLog.split("\n");
			PrintWriter writer = new PrintWriter(new FileOutputStream(new File(fileName), false));
			for (int i = 0; i < split.length; i++) {
				writer.println(split[i]);
			}
			writer.flush();
			writer.close();

		} catch (IOException e) {
			e.printStackTrace();
		}		
	}

	/*INSERIMENTO TABELLE
	 *L'inserimento deve avvenire nella scansione del file*/
	private void fileScan(String flowPath, String dbSchemaCodSocieta){
		logger.debug("TEST: AVVIATO METODO FILESCAN");
		Connection connection = null;

		try{
			connection = getTransactionConnection(dbSchemaCodSocieta);
			logger.debug("TEST: PATH DI SCANZIONE FILE: " + flowPath);
			FileInputStream fstream = new FileInputStream(flowPath);
			DataInputStream in = new DataInputStream(fstream);
			BufferedReader br = new BufferedReader(new InputStreamReader(in));
 			StringWriter stringWriter = new StringWriter();
 			PrintWriter printWriter = new PrintWriter(stringWriter);

			
			String currentLine;
			int lineNumber = 0;
			OT0FlowData currentOT0 = null;

			while ((currentLine = br.readLine()) != null)   {
				lineNumber++;

				switch (this.flowFileUtil.flowType(currentLine)) {
				case 1:
					logger.debug("Stai processando un flusso OTO");
					currentOT0 = new OT0FlowData(currentLine,logger);
					OT1FlowData OT1Data = this.flowFileUtil.getRelatedOT1(currentOT0);
					boolean resultTOT = this.totTableInsertManager(currentOT0,OT1Data, dbSchemaCodSocieta);
					logger.debug("TEST: RISULTATO INSERIMENTO TOT: " + resultTOT);
					break;
				case 2:
					logger.debug("Stai processando un flusso OT1");
					OT1FlowData currentOT1 = new OT1FlowData(currentLine,logger);
					logger.debug("TEST: VERIFICA FILE IMG IN FILEINFO = " + this.flowFileUtil.getFlowInfo().getElabImagePath()+
							currentOT1.getNomeFisicoImg().trim());
					boolean result = new java.io.File(this.flowFileUtil.getFlowInfo().getElabImagePath()+
							currentOT1.getNomeFisicoImg().trim()).isFile();
					logger.debug("TEST: IL BOOLEAN DI VERIFICA ESISTENZA FILE IMG E': " + result);
					if(result){
						logger.debug("L'immagine indicata nel record è presente.");
						boolean resultDOT = this.dotTableInsertManager(currentOT1, currentOT0, dbSchemaCodSocieta);
						logger.debug("TEST: RISULTATO INSERIMENTO DOT: " + resultDOT);
						printWriter.println("L'IMMAGINE " + currentOT1.getNomeFisicoImg().trim() + " E' PRESENTE NEL FLUSSO IMMAGINI:");
						printWriter.println(currentLine);
						/*QUI FAI LA MOVE*/
						FileUtility.move(new File(this.flowFileUtil.getFlowInfo().getElabImagePath()+currentOT1.getNomeFisicoImg()),
							this.flowFileUtil.getFlowInfo().getCurrentConfig().getDirectoryImmaginiOtticoPerEstrattoConto()+File.separator);
					}
					else{
						logger.debug("ATTENZIONE!!! L'immagine indicata nel record non è presente.");
						printWriter.println("L'IMMAGINE " + currentOT1.getNomeFisicoImg().trim() + " NON E' PRESENTE NEL FLUSSO IMMAGINI:");
						printWriter.println(currentLine);
					}
					break;
				case 3:
					logger.debug("Stai processando un flusso OT9");
					new OT9FlowData(currentLine, logger);
					/*Parte 7: Nessuna elaborazione, occorre spostare i file secondo paht CFO*/
					logger.debug("TEST: MOVE FILE DATI: FROM " + this.flowFileUtil.getFlowInfo().getElabPath()+this.flowFileUtil.getFlowInfo().getNomeFileDatiZip()
							+ " A " + this.directory.getDirectorySalvataggioFlussiImmaginiOtticoInput() 		
					);
					logger.debug("MOVE FILE IMG: FROM " + this.directory.getDirectoryFlussiImmaginiOtticoInput() + File.separator + "tmp" + File.separator + this.flowFileUtil.getFlowInfo().getNomeFileImg().trim()+".zip"
							+ " A " + this.directory.getDirectorySalvataggioFlussiImmaginiOtticoInput()	);

					FileUtility.move(new File(
							this.flowFileUtil.getFlowInfo().getElabPath()+this.flowFileUtil.getFlowInfo().getNomeFileDatiZip()), 
							this.flowFileUtil.getFlowInfo().getCurrentConfig().getDirectorySalvataggoFlussiDatiOtticoInput()+File.separator);
					FileUtility.move(new File(
							this.directory.getDirectoryFlussiImmaginiOtticoInput()+File.separator+"tmp"+File.separator+this.flowFileUtil.getFlowInfo().getNomeFileImg().trim()+".zip"), 
							this.flowFileUtil.getFlowInfo().getCurrentConfig().getDirectorySalvataggioFlussiImmaginiOtticoInput()+File.separator);
					break;
				case -1:
					/*Controllo 1: tipi di flussi*/ 
					logger.debug("Flusso errato alla linea " + lineNumber);
					break;
				}
			}
			br.close();
			in.close();
			fstream.close();
			logger.debug("TEST: FINE PROCCESSO - TABLEPROCESSOR. PREPARAZIONE LOG INFO");
			/*Fine processo: Creazine dei log!*/
        	this.logInfo = stringWriter.getBuffer().toString();
        	stringWriter = null;

			/*INVOCAZIONE OTTICO EJB PER SCRITTURA LOG*/
			logger.debug("TEST: RICERCA INFORMAZIONI TOT " + 
					"CSOC = " + this.flowFileUtil.getFlowInfo().getCurrentConfig().getCodiceSocieta() + 
					"CUTE = " + this.flowFileUtil.getFlowInfo().getCurrentConfig().getCodiceUtente() +
					"CENT = " + this.flowFileUtil.getFlowInfo().getCurrentConfig().getChiaveEnte() +
					"FILE DATI = " + this.flowFileUtil.getFlowInfo().getNomeFileDati()
			);
			TestataFlussoOttico totRecord = new TestataFlussoOtticoDao(connection, getSchema(dbSchemaCodSocieta)).doDetail(this.flowFileUtil.getFlowInfo().getCurrentConfig().getCodiceSocieta(), 
					this.flowFileUtil.getFlowInfo().getCurrentConfig().getCodiceUtente(),
					this.flowFileUtil.getFlowInfo().getCurrentConfig().getChiaveEnte(), 
					this.flowFileUtil.getFlowInfo().getNomeFileDatiZip());
			logger.debug("TEST: DATI TOT = " + totRecord.toString());

			logger.debug("TEST: INIZIO SCRITTURA LOG");
			this.logElaborazioneFlussiImg(this.logRiepilogoContent(),
					this.flowFileUtil.getFlowInfo().getCurrentConfig().getDirectoryLogElaborazione() + File.separator,
					totRecord.getNomeFileLog(), dbSchemaCodSocieta);
			this.logElaborazioneFlussiImg(this.logDettaglioContent(), 
					this.flowFileUtil.getFlowInfo().getCurrentConfig().getDirectoryLogElaborazione() + File.separator,
					totRecord.getNomeFileLogDettaglio(), dbSchemaCodSocieta);

		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			//inizio LP PG21XX04 Leak
    		//closeConnection(connection);
	    	try {
	    		if(connection != null) {
	    			connection.close();
	    		}
	    	} catch (SQLException e) {
	    		e.printStackTrace();
			}
			//fine LP PG21XX04 Leak
		}
	}

	private boolean totTableInsertManager(OT0FlowData OT0Data, OT1FlowData OT1Data, String dbSchemaCodSocieta) throws Exception{
		logger.debug("Inserimento tabella TOT");
		Connection connection = null;
		try {
			connection = getTransactionConnection(dbSchemaCodSocieta);
			String codiceSocieta = this.flowFileUtil.getFlowInfo().getCurrentConfig().getCodiceSocieta();
			String codiceUtente= this.flowFileUtil.getFlowInfo().getCurrentConfig().getCodiceUtente(); 
			String codiceEnte= this.flowFileUtil.getFlowInfo().getCurrentConfig().getChiaveEnte();
			String codET = this.flowFileUtil.getFlowInfo().getCurrentConfig().getCodiceEnte();
			String nomeFileDati = this.flowFileUtil.getFlowInfo().getNomeFileDati();
			String operatoreUltimoAgg = "SedaBatchOttico";

			int numeroDettagliCaricati = this.flowFileUtil.getFlowInfo().getOkImgRecord();
			int numeroDettagliScartati = this.flowFileUtil.getFlowInfo().getKoImgRecord();
			logger.debug("TEST: INSERIEMNTO TABELLA TOT - DATIPRELIMINARI " +
					"CSOC = " + codiceSocieta + "\n" +
					"CUTE = " + codiceUtente + "\n" +
					"CENT = " + codiceEnte + "\n" +
					"EKEY = " + codET + "\n" +
					"NomeFileDati = " + nomeFileDati + "\n" +
					"Operatore = " + operatoreUltimoAgg + "\n" +
					"NOK = " + numeroDettagliCaricati + "\n" +
					"NKO = " + numeroDettagliScartati
			);
			//Millisecondi
			Calendar cal = Calendar.getInstance();
			String millis = ""+cal.getTime().getTime();

			String nameLogRiepilogo = "RIE"+codiceSocieta+codiceUtente+codET+OT0Data.getFlowCreationData().replaceAll("-", "")+millis;
			String nameLogDettaglio = "DET"+codiceSocieta+codiceUtente+codET+OT0Data.getFlowCreationData().replaceAll("-", "")+millis;
			/*we insert into table*/
			String totKey = com.seda.commons.security.TokenGenerator.generateUUIDToken();
			logger.debug("TEST: CHIAVE GENERATA: " + totKey);
			TestataFlussoOtticoDto dtoObj = new TestataFlussoOtticoDto()
			.setBaseSearchKeys(new BaseSearchKeys(codiceSocieta, codiceUtente, codiceEnte))
			.setDateOtticoInfo( new DateOtticoInfo(null, java.sql.Date.valueOf(OT0Data.getFlowCreationData()), null, null, null, null))
			.setTipologiaDocumento(OT0Data.getDocType())
			.setDetailInfo(new DetailInfo(this.flowFileUtil.getFlowInfo().getNumberOfOT1(),
					numeroDettagliCaricati, numeroDettagliScartati))
					.setFilenameInfo(new FilenameInfo(
							OT0Data.getFlussoImg(), nameLogRiepilogo+".log",
							nameLogDettaglio+".log", this.flowFileUtil.getFlowInfo().getNomeFileDatiZip()))
							.setInfoUpdt( new InformazioniAggiornamento(null, operatoreUltimoAgg));

			logger.debug("\n\nTEST: INSERIMENTO TABELLA TOT: " + dtoObj.toString()+"\n\n");

			return new TestataFlussoOtticoDao(connection, getSchema(dbSchemaCodSocieta)).doInsert(dtoObj.toBean(dtoObj));

		} catch (Exception ex) {
			logger.error("totTableInsertManager failed, generic error due to: ", ex);
			throw new Exception(ex);
		} finally {
			//inizio LP PG21XX04 Leak
    		//closeConnection(connection);
	    	try {
	    		if(connection != null) {
	    			connection.close();
	    		}
	    	} catch (SQLException e) {
	    		e.printStackTrace();
			}
			//fine LP PG21XX04 Leak
		}
	}

	private boolean dotTableInsertManager(OT1FlowData OT1Data, OT0FlowData OT0Data, String dbSchemaCodSocieta)throws Exception{
		logger.debug("Inserimento tabella DOT");
		Connection connection = null;
		try {
			connection = getTransactionConnection(dbSchemaCodSocieta);

			String codiceSocieta = this.flowFileUtil.getFlowInfo().getCurrentConfig().getCodiceSocieta();
			String codiceUtente= this.flowFileUtil.getFlowInfo().getCurrentConfig().getCodiceUtente(); 
			String codiceEnte= this.flowFileUtil.getFlowInfo().getCurrentConfig().getChiaveEnte();
			String operatoreUltimoAgg = "SedaBatchOttico";
		
			//select per ottenere la chiave flusso
			TestataFlussoOttico totRecord = new TestataFlussoOtticoDao(connection, getSchema(dbSchemaCodSocieta)).doDetail(codiceSocieta, codiceUtente, codiceEnte, this.flowFileUtil.getFlowInfo().getNomeFileDatiZip());
			this.chiaveFlusso = totRecord.getChiaveFlusso();

			logger.debug("TEST: INFORMAZIONI PRELIMINARI INSERIEMNT DOT: " + 
					"CSOC = " + codiceSocieta + "\n" +
					"CUTE = " + codiceUtente + "\n" +
					"CENT = " + codiceEnte + "\n" + 
					"CFLOW = " + chiaveFlusso  + "\n");
			logger.debug("--- OT0 ---\n" + OT0Data.toString() + "\n");
			logger.debug("--- OT1 ---\n" + OT1Data.toString() + "\n");

			DettaglioFlussoOtticoDto dtoObj = new DettaglioFlussoOtticoDto()
			.setChiaveFlussoOttico(chiaveFlusso)
			.setBaseSearchKeys(new BaseSearchKeys(codiceSocieta, codiceUtente, codiceEnte))
			.setDocumentDetailInfo(new DocumentDetailInfo(OT0Data.getDocType(),
					OT1Data.getDocumento(), OT1Data.getChiaveQuietanza()))
					.setCodiciDettaglio(new CodiciDettaglio(OT1Data.getNumeroBollettino(),
							OT1Data.getNumeroQuietanza(), OT1Data.getNumeroRelata()))
							.setNomeFileImg(OT1Data.getNomeFisicoImg())
							.setInfoUpdt( new InformazioniAggiornamento(null, operatoreUltimoAgg)	);

			logger.debug("TEST: INSERIMENTO TABELLA DOT: " + dtoObj.toString());
			return new DettaglioFlussoOtticoDao(connection, getSchema(dbSchemaCodSocieta)).doInsert(dtoObj.toBean(dtoObj));

		} catch (Exception ex) {
			logger.error("dotTableInsertManager failed, generic error due to: ", ex);
			throw new Exception(ex);
		} finally {
			//inizio LP PG21XX04 Leak
    		//closeConnection(connection);
	    	try {
	    		if(connection != null) {
	    			connection.close();
	    		}
	    	} catch (SQLException e) {
	    		e.printStackTrace();
			}
			//fine LP PG21XX04 Leak
		}
	}

	/*
	 * We write log info
	 */
	private String logRiepilogoContent(){
		/**/
		DateFormat formatter = new SimpleDateFormat("E, dd MMM yyyy hh:mm:ss", Locale.ITALY);
		String itaEndDate = formatter.format(new Date());

		String infoDate = "DATA INIZIO ELABORAZIONE: " + this.flowFileUtil.getFlowInfo().getITAFormattedDate() +
		"\nDATA FINE ELABORAZIONE: " + itaEndDate + "\n";
		String infoFileDati = "NOME FLUSSO DATI ELABORATO: " + this.flowFileUtil.getFlowInfo().getNomeFileDati() + "\n"; 
		String infoFileImg = "NOME FLUSSO IMMAGINI ELABORATO: " + this.flowFileUtil.getFlowInfo().getNomeFileImg() + "\n\n";
		String infoDirSaveDati = "DIRECTORY DI OUTPUT RELATIVA ALL'ELABORAZIONE DEL FLUSSO DATI: " +
		this.flowFileUtil.getFlowInfo().getCurrentConfig().getDirectorySalvataggoFlussiDatiOtticoInput() + "\n";
		String infoDirSaveImg = "DIRECTORY DI OUTPUT RELATIVA ALL'ELABORAZIONE DEL FLUSSO IMMAGINI: "+
		this.flowFileUtil.getFlowInfo().getCurrentConfig().getDirectorySalvataggioFlussiImmaginiOtticoInput() + "\n\n";
		String infoEnte = "CODICE ENTE: " + this.flowFileUtil.getFlowInfo().getCurrentConfig().getCodiceEnte() + "\n";
		String infoElabImg = "TIPO DI IMMAGINE ELABORATA: "+ this.flowFileUtil.getFlowInfo().getTipologiaDocumenti() + "\n";
		String infoNumOT1 = "NUMERO RECORD ELABORATI: " + 
		this.flowFileUtil.getFlowInfo().getNumberOfOT1() + "\n";
		String infoOT1OK = "NUMERO DI RECORD CON IMMAGINI: " + 
		this.flowFileUtil.getFlowInfo().getOkImgRecord() + "\n";
		String infoOT1KO = "NUMERO DI RECORD SENZA IMMAGINI: " + 
		this.flowFileUtil.getFlowInfo().getKoImgRecord() + "\n";
		String infoChiaveFlusso = "CHIAVE FLUSSO INTERNA: " + this.chiaveFlusso + "\n";

		String logContent = infoDate + infoFileDati + infoFileImg +
		infoDirSaveDati + infoDirSaveImg + infoEnte +
		infoElabImg + infoNumOT1 + infoOT1OK + infoOT1KO
		+ infoChiaveFlusso;

		logger.debug("TEST: TESTO DEL LOG RIEPILOGO:\n\t" + logContent);

		return logContent;
	}

	/*
	 * We write log detail info
	 */
	private String logDettaglioContent(){

		DateFormat formatter = new SimpleDateFormat("E, dd MMM yyyy hh:mm:ss", Locale.ITALY);
		String itaEndDate = formatter.format(new Date());

		String infoDate = "DATA INIZIO ELABORAZIONE: " + this.flowFileUtil.getFlowInfo().getITAFormattedDate() +
		"\nDATA FINE ELABORAZIONE: " + itaEndDate + "\n\n";
		String infoFileDati = "NOME FLUSSO DATI ELABORATO: " + this.flowFileUtil.getFlowInfo().getNomeFileDati() + "\n"; 
		String infoFileImg = "NOME FLUSSO IMMAGINI ELABORATO: " + this.flowFileUtil.getFlowInfo().getNomeFileImg() + "\n\n";
		String infoDirSaveDati = "DIRECTORY DI OUTPUT RELATIVA ALL'ELABORAZIONE DEL FLUSSO DATI: " +
		this.flowFileUtil.getFlowInfo().getCurrentConfig().getDirectorySalvataggoFlussiDatiOtticoInput() + "\n";
		String infoDirSaveImg = "DIRECTORY DI OUTPUT RELATIVA ALL'ELABORAZIONE DEL FLUSSO IMMAGINI: "+
		this.flowFileUtil.getFlowInfo().getCurrentConfig().getDirectorySalvataggioFlussiImmaginiOtticoInput() + "\n\n";
		String infoEnte = "CODICE ENTE: " + this.flowFileUtil.getFlowInfo().getCurrentConfig().getCodiceEnte() + "\n";
		String infoElabImg = "TIPO DI IMMAGINE ELABORATA: "+ this.flowFileUtil.getFlowInfo().getTipologiaDocumenti() + "\n";
		String infoNumOT1 = "NUMERO RECORD ELABORATI: " + 
		this.flowFileUtil.getFlowInfo().getNumberOfOT1() + "\n";
		String infoOT1OK = "NUMERO DI RECORD CON IMMAGINI: " + 
		this.flowFileUtil.getFlowInfo().getOkImgRecord() + "\n";
		String infoOT1KO = "NUMERO DI RECORD SENZA IMMAGINI: " + 
		this.flowFileUtil.getFlowInfo().getKoImgRecord() + "\n";
		String infoChiaveFlusso = "CHIAVE FLUSSO INTERNA: " + this.chiaveFlusso + "\n";
		String testataDettaglio = "\n" + "DETTAGLIO ELABORAZIONE:" + "\n";

		String logContent = infoDate + infoFileDati + infoFileImg +
		infoDirSaveDati + infoDirSaveImg + infoEnte +
		infoElabImg + infoNumOT1 + infoOT1OK + infoOT1KO
		+ infoChiaveFlusso + testataDettaglio + this.logInfo;

		logger.debug("TEST: TESTO DEL LOG DETTAGLIO:\n\t" + logContent);

		return logContent;

	}

	/**
	 * we send notify mail
	 * @param text
	 * @param toList
	 * @param ccList
	 * @param ccnList
	 * @param subject
	 * @param attacchedFileList
	 */
	private String notificaMancatoAllineamento(String text, String toList, String ccList, String ccnList, String subject, String attacchedFileList)
	{
		String resultEmailSender="";
		EMailSenderInterface emailSenderPort = null;
		EMailSenderResponse emailSenderResponse =null;


		EMailSenderRequestType emailSenderRequestType = new EMailSenderRequestType();
		emailSenderRequestType.setEMailDataText(text.toString());
		emailSenderRequestType.setEMailDataTOList(toList);
		emailSenderRequestType.setEMailDataCCList(ccList);
		emailSenderRequestType.setEMailDataCCNList(ccnList);		
		emailSenderRequestType.setEMailDataSubject(subject.toString());		
		emailSenderRequestType.setEMailDataAttacchedFileList(attacchedFileList);

		EMailSenderServiceLocator emailSenderServiceLocator = new EMailSenderServiceLocator();
		try {
			logger.debug("TEST: URL NEL METODO DI INVIO: " + getWsEMailSenderUrl());
			emailSenderPort = emailSenderServiceLocator.getEMailSenderPort(new URL(getWsEMailSenderUrl()));
			emailSenderResponse = emailSenderPort.getEMailSender(emailSenderRequestType);
			resultEmailSender = emailSenderResponse.getValue();			

		} catch (MalformedURLException e) {

			e.printStackTrace();
		} catch (ServiceException e) {

			e.printStackTrace();		
		} catch (EMailSenderFaultType e) {

			e.printStackTrace();
		} catch (RemoteException e) {

			e.printStackTrace();
		}		
		return resultEmailSender;
	}


	/**
	 * @param request
	 * @return
	 */
	private RecuperaImmagineResponse recuperaImmagine(RecuperaImmagineRequest request, WebServiceInfo wsInfo, String dbSchemaCodSocieta) {
		IntegraOtticoSOAPBindingStub integraOtticoPort = null;
		RecuperaImmagineResponse response = null;
		try {
			IntegraOtticoServiceLocator serviceLocator = new IntegraOtticoServiceLocator();
			if (wsInfo.getFlagWebserviceOttico().equals("P")) //richiamo l'integraottico locale (Payer)
			{
//				logger.info("Chiamata all'integraottico locale");
				integraOtticoPort = (IntegraOtticoSOAPBindingStub)serviceLocator.getIntegraOtticoPort(new URL(getWsIntegraOtticoUrl()));
			}
			else 
			{
				if(wsInfo.getUserWebService().trim().equals(""))
				{
					System.out.println("Chiamata all'integraottico dell'ente alla URL: " + wsInfo.getIndirizzoWebService());  
					integraOtticoPort = (IntegraOtticoSOAPBindingStub)serviceLocator.getIntegraOtticoPort(new URL(wsInfo.getIndirizzoWebService()));
				}
				else
				{
					System.out.println("Chiamata all'integraottico dell'ente alla URL: " + wsInfo.getIndirizzoWebService() + " User: " + wsInfo.getUserWebService() + " Pwd: " + wsInfo.getPassWebService());
					integraOtticoPort = (IntegraOtticoSOAPBindingStub)serviceLocator.getIntegraOtticoPort(new URL(wsInfo.getIndirizzoWebService()), wsInfo.getUserWebService(), wsInfo.getPassWebService());
				}
			}
			
			if (integraOtticoPort != null)
			{
				
				setCodSocietaHeaderIntegraOttico(integraOtticoPort, dbSchemaCodSocieta);
				response = integraOtticoPort.recuperaImmagine(request);
			}
			else
				logger.error("Errore durante l'inizializzazione del webservice integraottico");

		} catch (MalformedURLException e) {
			e.printStackTrace();
		} catch (ServiceException e) {
			e.printStackTrace();
		} catch (RemoteException e) {
			e.printStackTrace();
		}		
		return response;	
	}	

	
	/**
	 * @param conf
	 * @param directory
	 * @return
	 */
	private boolean notifyMalformedDirectory(Configurazione conf, DirectoryInfo directory, User user, Company company, AnagEnte ente) {
		boolean dirMalformed = false;
		String message = "";
		if ((directory.getDirectoryFlussiDatiOtticoInput()==null) || directory.getDirectoryFlussiDatiOtticoInput().equals("") || !new File(directory.getDirectoryFlussiDatiOtticoInput() + File.separator).isDirectory()) {
			message += "<br/> - La Dir. In Flussi Dati non è corretta: " + directory.getDirectoryFlussiDatiOtticoInput();
			dirMalformed = true;
		}
		if ((directory.getDirectorySalvataggoFlussiDatiOtticoInput()==null) || directory.getDirectorySalvataggoFlussiDatiOtticoInput().equals("") || !new File(directory.getDirectorySalvataggoFlussiDatiOtticoInput() + File.separator).isDirectory()) {
			message += "<br/> - La Dir. Out Flussi Dati non è corretta: " + directory.getDirectorySalvataggoFlussiDatiOtticoInput();
			dirMalformed = true;
		}
		if ((directory.getDirectoryFlussiImmaginiOtticoInput()==null) || directory.getDirectoryFlussiImmaginiOtticoInput().equals("") || !new File(directory.getDirectoryFlussiImmaginiOtticoInput() + File.separator).isDirectory()) {
			message += "<br/> - La Dir. In Flussi Img. non è corretta: " + directory.getDirectoryFlussiImmaginiOtticoInput();
			dirMalformed = true;
		}
		if ((directory. getDirectorySalvataggioFlussiImmaginiOtticoInput()==null) || directory.getDirectorySalvataggioFlussiImmaginiOtticoInput().equals("") || !new File(directory.getDirectorySalvataggioFlussiImmaginiOtticoInput() + File.separator).isDirectory()) {
			message += "<br/> - La Dir. Out Flussi Img. non è corretta: " + directory.getDirectorySalvataggioFlussiImmaginiOtticoInput();
			dirMalformed = true;
		}
		if ((directory.getDirectoryImmaginiOtticoPerEstrattoConto()==null) || directory.getDirectoryImmaginiOtticoPerEstrattoConto().equals("") || !new File(directory.getDirectoryImmaginiOtticoPerEstrattoConto() + File.separator).isDirectory()) {
			message += "<br/> - La Dir. Img. Elab. non è corretta: " + directory.getDirectoryImmaginiOtticoPerEstrattoConto();
			dirMalformed = true;
		}
		if ((directory.getDirectoryLogElaborazione()==null) || directory.getDirectoryLogElaborazione().equals("") || !new File(directory.getDirectoryLogElaborazione() + File.separator).isDirectory()) {
			message += "<br/> - La Dir. Log Elab. non è corretta: " + directory.getDirectoryLogElaborazione();
			dirMalformed = true;
		}
		if (dirMalformed) {
			String emailAdmin = conf.getIndirizzoEmailAmministratore();
			String text = MessageFormat.format(OtticoConstraints.MESSAGGIO_CONFIGURATION_MALFORMED, 
					new Object[] { company.getCompanyDescription(), 
								   user.getUserDescription(),
								   (ente.getDescrizioneEnte() + " " + ente.getDescrizioneUfficio()).trim() }) + message;
			String subject = OtticoConstraints.SUBJECT;
			this.notificaMancatoAllineamento(text, emailAdmin, "", "", subject, "");
		}
		return dirMalformed;
	}
	
//	private  IntegraEnteEcInterface getIntegraEnteRef(){
//		logger.debug("TEST: Entrato in getIntegraEnteEcRef");
//
//		IntegraEnteEcInterface integraPort = null;
//
//		try {
//			IntegraEnteEcServiceLocator serviceLocator = new IntegraEnteEcServiceLocator();
//			//LogWriter.logInfo("TEST: OtticoURL = " + serviceLocator.getOtticoPortAddress());
//			//TMP http://10.10.80.6:8979/OtticoService/ottico
//			integraPort = serviceLocator.getIntegraenteEcPort(new URL("http://10.10.80.105:8080/IntegraenteService/integraente"));
//
//		} catch (MalformedURLException e) {
//			e.printStackTrace();
//		} catch (ServiceException e) {
//			e.printStackTrace();
//		}	
//
//		return integraPort;
//	}
	
	//PG150290_002 - inizio
	public ArrayList<ImmagineDto> getListaImmagini(String identificativoDocumento, String flagAccesso, WebServiceInfo wsInfo, String dbSchemaCodSocieta) throws FacadeException{
		ArrayList<ImmagineDto> listaImmagini = null;
		try {
			//IMMAGINI
			//Recupero immagini documento (integraOttico)
			GetImmaginiListRequest request = new GetImmaginiListRequest(identificativoDocumento, flagAccesso);
			GetImmaginiListResponse response = null;
			IntegraOtticoSOAPBindingStub integraOtticoPort = null;
			try {
				IntegraOtticoServiceLocator serviceLocator = new IntegraOtticoServiceLocator();
				if (wsInfo.getFlagWebserviceOttico().equals("P")) //richiamo l'integraottico locale (Payer)
				{
//					logger.info("Chiamata all'integraottico locale");
					integraOtticoPort = (IntegraOtticoSOAPBindingStub)serviceLocator.getIntegraOtticoPort(new URL(getWsIntegraOtticoUrl()));
				}
				else 
				{
					if(wsInfo.getUserWebService().trim().equals(""))
					{
						logger.info("Chiamata all'integraottico dell'ente alla URL: " + wsInfo.getIndirizzoWebService());  
						integraOtticoPort = (IntegraOtticoSOAPBindingStub)serviceLocator.getIntegraOtticoPort(new URL(wsInfo.getIndirizzoWebService()));
					}
					else
					{
						logger.info("Chiamata all'integraottico dell'ente alla URL: " + wsInfo.getIndirizzoWebService() + " User: " + wsInfo.getUserWebService() + " Pwd: " + wsInfo.getPassWebService());
						integraOtticoPort = (IntegraOtticoSOAPBindingStub)serviceLocator.getIntegraOtticoPort(new URL(wsInfo.getIndirizzoWebService()), wsInfo.getUserWebService(), wsInfo.getPassWebService());
					}
				}
				
				if (integraOtticoPort != null)
				{
					setCodSocietaHeaderIntegraOttico(integraOtticoPort, dbSchemaCodSocieta);
					response = integraOtticoPort.getImmaginiList(request);
				}
				else
					logger.error("Errore durante l'inizializzazione del webservice integraottico");

			} catch (MalformedURLException e) {
				e.printStackTrace();
			} catch (ServiceException e) {
				e.printStackTrace();
			} catch (RemoteException e) {
				e.printStackTrace();
			}		
			
			if (response != null && response.getEsito().equals("00") && response.getImmagini() != null) 
			{
				listaImmagini = new ArrayList<ImmagineDto>();
				Immagine[] immagini = response.getImmagini();
	            //inizio LP OT200010
				//ImmagineDto[] immaginiDto = new ImmagineDto[immagini.length];
				long indDoc = 0;
				long nAllegati = 0;
				ImmagineDto immagineDtoOld = null;
				ImmagineDto immagineDto = null;
				//fine LP OT200010
				for (int i = 0; i<immagini.length; i++) {
		            //inizio LP OT200010
					//ImmagineDto immagineDto = new ImmagineDto(immagini[i].getFilePath(), immagini[i].getDataDocumento(), immagini[i].getTipoImmagine(), immagini[i].getIdDocumento(), immagini[i].getFlagVisibilita());
					if(immagini[i].getIdDocumento().startsWith("A")) {
						nAllegati++;
						immagineDto = new ImmagineDto(immagini[i].getFilePath(), immagini[i].getDataDocumento(), immagini[i].getTipoImmagine(), immagini[i].getIdDocumento(), immagini[i].getFlagVisibilita(), indDoc);
					} else {
						indDoc++;
						if(immagineDtoOld != null) {
							immagineDtoOld.setNumAllegati(nAllegati);
						}
						nAllegati = 0;
						immagineDto = new ImmagineDto(immagini[i].getFilePath(), immagini[i].getDataDocumento(), immagini[i].getTipoImmagine(), immagini[i].getIdDocumento(), immagini[i].getFlagVisibilita(), indDoc);
						immagineDtoOld = immagineDto;
					}
					//fine LP OT200010
		            //inizio LP OT200010
					//immaginiDto[i] = immagineDto;
					//listaImmagini.add(immaginiDto[i]);
					listaImmagini.add(immagineDto);
					//fine LP OT200010
				}
				if(immagineDtoOld != null && nAllegati > 0) {
					immagineDtoOld.setNumAllegati(nAllegati);
				}
			}
			
			if (response != null && response.getEsito().equals("04")) {	//nessuna immagine presente
				listaImmagini = new ArrayList<ImmagineDto>();
			} 
			
			return listaImmagini;	
		} catch (Exception ex) {
			ex.printStackTrace();
			logger.error("getListaImmagini failed, generic error due to: ", ex);
		} 
		return null;
	}
	
	/**
	 * @ejb.interface-method
	 * @ejb.transaction type="NotSupported"
	 */ 
	public String stampaImmagineContenzioso(String idDocBarcode, String flagAccesso, Boolean pdfExt, WebServiceInfo wsInfo, String dbSchemaCodSocieta) throws FacadeException{
		List<String> listaReportNames = new ArrayList<String>();
		try {	
			//IMMAGINI
			//Recupero immagini documento (integraOttico)
			GetImmagineContenziosoRequest request = new GetImmagineContenziosoRequest(idDocBarcode, flagAccesso, pdfExt);
			GetImmagineContenziosoResponse response = null;
			IntegraOtticoSOAPBindingStub integraOtticoPort = null;
			try {
				IntegraOtticoServiceLocator serviceLocator = new IntegraOtticoServiceLocator();
				if (wsInfo.getFlagWebserviceOttico().equals("P")) //richiamo l'integraottico locale (Payer)
				{
//					logger.info("Chiamata all'integraottico locale");
					integraOtticoPort = (IntegraOtticoSOAPBindingStub)serviceLocator.getIntegraOtticoPort(new URL(getWsIntegraOtticoUrl()));
				}
				else 
				{
					if(wsInfo.getUserWebService().trim().equals(""))
					{
						logger.info("Chiamata all'integraottico dell'ente alla URL: " + wsInfo.getIndirizzoWebService());  
						integraOtticoPort = (IntegraOtticoSOAPBindingStub)serviceLocator.getIntegraOtticoPort(new URL(wsInfo.getIndirizzoWebService()));
					}
					else
					{
						logger.info("Chiamata all'integraottico dell'ente alla URL: " + wsInfo.getIndirizzoWebService() + " User: " + wsInfo.getUserWebService() + " Pwd: " + wsInfo.getPassWebService());
						integraOtticoPort = (IntegraOtticoSOAPBindingStub)serviceLocator.getIntegraOtticoPort(new URL(wsInfo.getIndirizzoWebService()), wsInfo.getUserWebService(), wsInfo.getPassWebService());
					}
				}
				
				if (integraOtticoPort != null)
				{
					setCodSocietaHeaderIntegraOttico(integraOtticoPort, dbSchemaCodSocieta);
					response = integraOtticoPort.getImmagineContenzioso(request);
				}
				else
					logger.error("Errore durante l'inizializzazione del webservice integraottico");

			} catch (MalformedURLException e) {
				e.printStackTrace();
			} catch (ServiceException e) {
				e.printStackTrace();
			} catch (RemoteException e) {
				e.printStackTrace();
			}	
			
			if (response != null && response.getEsito().equals("00") && 
					response.getDocumentoContenzioso() != null)
			{
				FileEstensione fileEstensione = response.getDocumentoContenzioso().getFileEstensione();
				//String nomeReport = reportCreator.generaNomeFilePDFDocumento(printInfo.getNumeroDocumento());
				String nomeReport = response.getDocumentoContenzioso().getNomeFisicoImmagine();
				FileUtility.wr(fileEstensione.getFileByte(), this.getOutputDirectoryPdf(), nomeReport);
				listaReportNames.add(this.getOutputDirectoryPdf() + nomeReport);
				if (listaReportNames.size() > 0) return this.getOutputDirectoryPdf()+nomeReport;	
			}
		} catch (Exception ex) {
			ex.printStackTrace();
			logger.error("stampaImmagineContenzioso failed, generic error due to: ", ex);
		} 
		
		return null;
	}
	
	public String stampaImmagine(String identificativoDocumento, String filePath, WebServiceInfo wsInfo, String dbSchemaCodSocieta) throws FacadeException{
		List<String> listaReportNames = new ArrayList<String>();
		try {	
			//IMMAGINI
			//Recupero immagini documento (integraOttico)
			Immagine immagine = new Immagine();
			immagine.setIdDocumento(identificativoDocumento);
			immagine.setFilePath(filePath);
			GetImmagineRequest request = new GetImmagineRequest(immagine);
			GetImmagineResponse response = null;
			IntegraOtticoSOAPBindingStub integraOtticoPort = null;
			try {
				IntegraOtticoServiceLocator serviceLocator = new IntegraOtticoServiceLocator();
				if (wsInfo.getFlagWebserviceOttico().equals("P")) //richiamo l'integraottico locale (Payer)
				{
//					logger.info("Chiamata all'integraottico locale");
					integraOtticoPort = (IntegraOtticoSOAPBindingStub)serviceLocator.getIntegraOtticoPort(new URL(getWsIntegraOtticoUrl()));
				}
				else 
				{
					if(wsInfo.getUserWebService().trim().equals(""))
					{
						logger.info("Chiamata all'integraottico dell'ente alla URL: " + wsInfo.getIndirizzoWebService());  
						integraOtticoPort = (IntegraOtticoSOAPBindingStub)serviceLocator.getIntegraOtticoPort(new URL(wsInfo.getIndirizzoWebService()));
					}
					else
					{
						logger.info("Chiamata all'integraottico dell'ente alla URL: " + wsInfo.getIndirizzoWebService() + " User: " + wsInfo.getUserWebService() + " Pwd: " + wsInfo.getPassWebService());
						integraOtticoPort = (IntegraOtticoSOAPBindingStub)serviceLocator.getIntegraOtticoPort(new URL(wsInfo.getIndirizzoWebService()), wsInfo.getUserWebService(), wsInfo.getPassWebService());
					}
				}
				
				if (integraOtticoPort != null)
				{
					setCodSocietaHeaderIntegraOttico(integraOtticoPort, dbSchemaCodSocieta);
					response = integraOtticoPort.getImmagine(request);
				}
				else
					logger.error("Errore durante l'inizializzazione del webservice integraottico");

			} catch (MalformedURLException e) {
				e.printStackTrace();
			} catch (ServiceException e) {
				e.printStackTrace();
			} catch (RemoteException e) {
				e.printStackTrace();
			}	
			
			if (response != null && response.getEsito().equals("00") && 
					response.getDocumento() != null)
			{
				Documento documento = response.getDocumento();
				//String nomeReport = reportCreator.generaNomeFilePDFDocumento(printInfo.getNumeroDocumento());
				String nomeReport = documento.getNomeFisicoImmagine();
				FileUtility.wr(documento.getImmagineDocumento(), this.getOutputDirectoryPdf(), nomeReport);
				listaReportNames.add(this.getOutputDirectoryPdf() + nomeReport);
				if (listaReportNames.size() > 0) return this.getOutputDirectoryPdf()+documento.getNomeFisicoImmagine();	
			}
		} catch (Exception ex) {
			ex.printStackTrace();
			logger.error("stampaImmagine failed, generic error due to: ", ex);
		} 
		
		return null;
	}
	//PG150290_002 - fine
	
	//PAGONET-567 SB - INIZIO
	public String stampaImmagineEsito(String idDocBarcode, String flagAccesso, WebServiceInfo wsInfo, String dbSchemaCodSocieta) throws FacadeException{
		List<String> listaReportNames = new ArrayList<String>();
		try {	
			//IMMAGINI
			//Recupero immagini documento (integraOttico)
			GetImmagineEsitoRequest request = new GetImmagineEsitoRequest(idDocBarcode, flagAccesso);
			GetImmagineEsitoResponse response = null;
			IntegraOtticoSOAPBindingStub integraOtticoPort = null;
			try {
				IntegraOtticoServiceLocator serviceLocator = new IntegraOtticoServiceLocator();
				if (wsInfo.getFlagWebserviceOttico().equals("P")) //richiamo l'integraottico locale (Payer)
				{
					integraOtticoPort = (IntegraOtticoSOAPBindingStub)serviceLocator.getIntegraOtticoPort(new URL(getWsIntegraOtticoUrl()));
				}
				else 
				{
					if(wsInfo.getUserWebService().trim().equals(""))
					{
						logger.info("Chiamata all'integraottico dell'ente alla URL: " + wsInfo.getIndirizzoWebService());  
						integraOtticoPort = (IntegraOtticoSOAPBindingStub)serviceLocator.getIntegraOtticoPort(new URL(wsInfo.getIndirizzoWebService()));
					}
					else
					{
						logger.info("Chiamata all'integraottico dell'ente alla URL: " + wsInfo.getIndirizzoWebService() + " User: " + wsInfo.getUserWebService() + " Pwd: " + wsInfo.getPassWebService());
						integraOtticoPort = (IntegraOtticoSOAPBindingStub)serviceLocator.getIntegraOtticoPort(new URL(wsInfo.getIndirizzoWebService()), wsInfo.getUserWebService(), wsInfo.getPassWebService());
					}
				}
				
				if (integraOtticoPort != null)
				{
					setCodSocietaHeaderIntegraOttico(integraOtticoPort, dbSchemaCodSocieta);
					response = integraOtticoPort.getImmagineEsito(request);
				}
				else
					logger.error("Errore durante l'inizializzazione del webservice integraottico");

			} catch (MalformedURLException e) {
				e.printStackTrace();
			} catch (ServiceException e) {
				e.printStackTrace();
			} catch (RemoteException e) {
				e.printStackTrace();
			}	
			
			if (response != null && response.getEsito().equals("00") && 
					response.getDocumentoEsito() != null)
			{
				FileEstensione fileEstensione = response.getDocumentoEsito().getFileEstensione();
				String nomeReport = response.getDocumentoEsito().getNomeFisicoImmagine();
				FileUtility.wr(fileEstensione.getFileByte(), this.getOutputDirectoryPdf(), nomeReport);
				listaReportNames.add(this.getOutputDirectoryPdf() + nomeReport);
				if (listaReportNames.size() > 0) return this.getOutputDirectoryPdf()+nomeReport;	
			}
		} catch (Exception ex) {
			ex.printStackTrace();
			logger.error("stampaImmagineEsito failed, generic error due to: ", ex);
		} 
		
		return null;
	}
	//PAGONET-567 SB - FINE
}