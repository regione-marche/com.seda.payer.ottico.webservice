package com.seda.payer.ottico.facade.pdf;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;
import java.util.TimeZone;
import java.util.logging.Level;

import org.apache.log4j.Logger;
import org.eclipse.birt.core.framework.Platform;
import org.eclipse.birt.report.engine.api.EngineConfig;
import org.eclipse.birt.report.engine.api.EngineConstants;
import org.eclipse.birt.report.engine.api.IReportEngine;
import org.eclipse.birt.report.engine.api.IReportEngineFactory;
import org.eclipse.birt.report.engine.api.IReportRunnable;
import org.eclipse.birt.report.engine.api.IRunAndRenderTask;
import org.eclipse.birt.report.engine.api.PDFRenderOption;

import com.seda.commons.properties.tree.PropertiesTree;
import com.seda.payer.ottico.webservice.dto.InformazioniStampa;
import com.seda.payer.ottico.webservice.dati.RecuperaTemplateResponse;

public class ReportCreator {

	private String birtHomeEngine = "";
	private String birtHomeLogs = "";
	private String outputDirectoryPdf;
	private String birtDesignPath;
	private String pathLogoReport;
	private Level level = Level.WARNING;
	private PropertiesTree propertiesTree;
	private Logger logger;
	protected String defaultTemplate;

	public Level getLevel() {
		return level;
	}

	public void setLevel(Level level) {
		this.level = level;
	}
	
	public void setPathLogoReport(String pathLogoReport) {
		this.pathLogoReport = pathLogoReport;
	}

	public String getPathLogoReport() {
		return pathLogoReport;
	}

	List<ByteArrayOutputStream> byteArrayOutputStreams = new ArrayList<ByteArrayOutputStream>();
	List<String> filePaths = new ArrayList<String>();

	/**
	 * Costruttore per la generazione dei bollettini
	 * 
	 * @param birtHomeEngine
	 *            Directory del Birt Engine
	 * @param birtHomeLogs
	 *            Directory dove verranno memorizzati i logs di Birt
	 * @param outputDirectoryPdf
	 *            Directory dove verranno memorizzati i pdf generati
	 * @param defaultTemplate
	 * 			  Template (rptdesign) di default
	 * @param logger
	 * @param propertiesTree
	 */
	public ReportCreator(String birtHomeEngine, String birtHomeLogs,
			String outputDirectoryPdf, String birtDesignPath, String pathLogoReport, Logger logger, PropertiesTree propertiesTree) {
		this.birtHomeEngine = birtHomeEngine;
		this.birtHomeLogs = birtHomeLogs;
		this.outputDirectoryPdf = outputDirectoryPdf;
		this.birtDesignPath = birtDesignPath;
		this.propertiesTree = propertiesTree;
		this.logger = logger;
		this.pathLogoReport = pathLogoReport;
	}
	
	/**
	 * 
	 * @param printInfo
	 * @param timestamp
	 * @param xmlData
	 * @param bTributiVisible
	 * @return
	 * @throws IOException
	 */
	@SuppressWarnings("unchecked")
	public String generaPDFDocumento(InformazioniStampa printInfo, String tipologiaServizio, String timestamp,
			String xmlData, boolean bTributiVisible) throws IOException {
		IReportEngine engine = null;
		EngineConfig config = null;
			
		String nomeFile = "";
		//inizio LP PG21XX04 Leak
		FileInputStream riferimentoTemplate = null; 
		//fine LP PG21XX04 Leak
		try {
			nomeFile = outputDirectoryPdf + printInfo.getNumeroDocumento() + "_documento_" + timestamp + ".pdf";
			// start up Platform
			config = new EngineConfig();
			config.setBIRTHome(birtHomeEngine);
			config.setLogConfig(birtHomeLogs, level);
			config.setLogFile(getFileName());
			Platform.startup(config);
			// create new Report Engine
			Platform.startup(config);
			IReportEngineFactory factory = (IReportEngineFactory) Platform.createFactoryObject(IReportEngineFactory.EXTENSION_REPORT_ENGINE_FACTORY);
			engine = factory.createReportEngine(config);
			
			
			
			/* Recupero del template personalizzato se presente */
			//inizio LP PG21XX04 Leak
			//FileInputStream riferimentoTemplate = recuperaTemplatePersonalizzato("D", 
			//		printInfo.getBaseSearchKeys().getCodiceSocieta(), 
			//		printInfo.getBaseSearchKeys().getCodiceUtente(), 
			//		printInfo.getBaseSearchKeys().getChiaveEntePayer(), 
			//		tipologiaServizio, 
			//		"", //per il tipo "D"=Documento, il tipo bollettino non va considerato
			//		Calendar.getInstance());
			riferimentoTemplate = recuperaTemplatePersonalizzato("D", 
					printInfo.getBaseSearchKeys().getCodiceSocieta(), 
					printInfo.getBaseSearchKeys().getCodiceUtente(), 
					printInfo.getBaseSearchKeys().getChiaveEntePayer(), 
					tipologiaServizio, 
					"", //per il tipo "D"=Documento, il tipo bollettino non va considerato
					Calendar.getInstance());
			//fine LP PG21XX04 Leak
			
			IReportRunnable design = null;	
			/* we check If custom template is not empty */
			if (riferimentoTemplate != null)
				design = engine.openReportDesign(riferimentoTemplate);
			else //template di default 
				design = engine.openReportDesign(this.birtDesignPath + PrintStrings.DOCUMENTO_RPTDESIGN.format());
			
			/*
			ReportDesignHandle report =(ReportDesignHandle)design.getDesignHandle( );
			dataSourceHandleDocumento = (OdaDataSourceHandle)report.findDataSource("DataSourceDocumento");
			Object objProp = dataSourceHandleDocumento.getProperty("FILELIST");

			dataSourceHandleScadenze = (OdaDataSourceHandle)report.findDataSource("DataSourceScadenze");
			dataSourceHandleScadenze.setProperty("FILELIST", xmlPathScadenze);
			dataSourceHandleTributi = (OdaDataSourceHandle)report.findDataSource("DataSourceTributi");
			dataSourceHandleTributi.setProperty("FILELIST", xmlPathTributi);*/
			
			// create RunandRender Task
			IRunAndRenderTask task = engine.createRunAndRenderTask(design);
			
			task.getAppContext().put(EngineConstants.APPCONTEXT_CLASSLOADER_KEY, this.getClass().getClassLoader()); 
			try {
				
				byte[] dataBytes = xmlData.getBytes();
				ByteArrayInputStream byteIs = new ByteArrayInputStream(dataBytes);
				
	            task.getAppContext().put("org.eclipse.datatools.enablement.oda.xml.inputStream", byteIs);
	        } 
			catch (Exception x) {
	             x.printStackTrace();                                                   
	        }
			
			/*task.setParameterValue("xmlPathDocumento", xmlPathDocumento);
			task.setParameterValue("xmlPathScadenze", xmlPathScadenze);
			task.setParameterValue("xmlPathTributi", xmlPathTributi);*/
		
			task.setParameterValue("tributiVisible", bTributiVisible ? "Y" : "N");
			task.setParameterValue("logo", this.pathLogoReport);

			// set render options including output type
			PDFRenderOption options = new PDFRenderOption();
			options.setOutputFileName(nomeFile);
			options.setOutputFormat("pdf");
			task.setRenderOption(options);
			task.run();
			
			//close and destroy objects
			task.close();
			engine.destroy();
		} catch (Exception ex) {
			ex.printStackTrace();
			return null;
		}
		//inizio LP PG21XX04 Leak
		finally {
	    	try {
	    		if(riferimentoTemplate != null) {
	    			riferimentoTemplate.close();
	    		}
	    	} catch (IOException e) {
	    		e.printStackTrace();
			}
		}
		//fine LP PG21XX04 Leak
		return nomeFile;
	}
	
	private static String getFileName() {
		InetAddress addr = null;
		String hostname = "";
		try {
			addr = InetAddress.getLocalHost();
		} catch (UnknownHostException e) {
			e.printStackTrace();
		}

		if (addr != null) {
			hostname = addr.getHostName();
		}

		Calendar calendar = Calendar.getInstance(TimeZone
				.getTimeZone("Europe/Rome"), Locale.ITALY);
		calendar.setTimeInMillis(System.currentTimeMillis());

		StringBuilder sb = new StringBuilder();
		sb.append(hostname);
		sb.append("_ReportEngine_");
		sb.append(calendar.get(Calendar.YEAR));
		sb.append("_");
		sb.append(calendar.get(Calendar.MONTH) + 1);
		sb.append("_");
		sb.append(calendar.get(Calendar.DAY_OF_MONTH));
		sb.append("_");
		sb.append(calendar.get(Calendar.HOUR_OF_DAY));
		sb.append("_");
		sb.append(calendar.get(Calendar.MINUTE));
		sb.append("_");
		sb.append(calendar.get(Calendar.SECOND));
		sb.append(".log");

		return sb.toString();
	}

	
	
	private FileInputStream recuperaTemplatePersonalizzato(String tipoDocumento, String codiceSocieta, String codiceUtente, String chiaveEnte,
			String codTipologiaServizio, String tipoBollettinoTemplate, Calendar dataEffettivoPagamentoSuGateway) throws FileNotFoundException
	{
		RecuperaTemplateResponse recuperaTemplate = 
			ReportHelper.recuperaTemplate(
			tipoDocumento,
			codiceSocieta, 
			codTipologiaServizio, 
			codiceUtente, 
			chiaveEnte, 
			"", 
			dataEffettivoPagamentoSuGateway, 
			this.propertiesTree);
	
		String rifTemplate = recuperaTemplate.getRiferimentoTemplate();
		if (rifTemplate != null && rifTemplate.length() > 0)
		{
			//controllo che il path definito nel campo Template corrisponda ad un file esistente
			boolean exists = (new File(rifTemplate)).exists();
			if (exists)
				return new FileInputStream(rifTemplate);
			else
			{
				logger.warn("Impossibile trovare il template " + rifTemplate + " definito per tipoDocumento " + tipoDocumento + 
						", codiceSocieta " + codiceSocieta + 
						", tipologiaServizio " + codTipologiaServizio + ", codiceUtente " + codiceUtente + 
						", chiaveEnte " + chiaveEnte + ", tipoBollettino "  + tipoBollettinoTemplate + 
						", dataValidità " + dataEffettivoPagamentoSuGateway + ". Verrà utilizzato il template di default.");
				//ritornando null prenderà il template di default
				return null;
			}
		}
		else 
		{
			//template personalizzato non trovato, si userà quello di default
			logger.warn("Nessun template trovato per tipoDocumento " + tipoDocumento + ", codiceSocieta " + codiceSocieta + 
					", tipologiaServizio " + codTipologiaServizio + ", codiceUtente " + codiceUtente + 
					", chiaveEnte " + chiaveEnte + ", tipoBollettino "  + tipoBollettinoTemplate + 
					", dataValidità " + dataEffettivoPagamentoSuGateway);
			return null;
		}
	}
}
