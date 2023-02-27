package com.seda.payer.ottico.facade.handler;

import java.sql.Connection;
import java.util.Properties;
import org.apache.log4j.Hierarchy;
import org.apache.log4j.Logger;

import com.seda.commons.logger.LoggerHierarchyServer;
import com.seda.commons.properties.tree.PropertiesTree;
//inizio LP PG200070 - 20200813
//import com.seda.compatibility.SystemVariable;
//fine LP PG200070 - 20200813
import com.seda.data.dao.DAOHelper;
import com.seda.j2ee5.jndi.JndiProxy;
import com.seda.j2ee5.jndi.JndiProxyException;
import com.seda.payer.integraottico.webservice.source.IntegraOtticoSOAPBindingStub;
import com.seda.payer.notifiche.webservice.source.NotificheSOAPBindingStub;
import com.seda.payer.pgec.webservice.commons.source.CommonsSOAPBindingStub;

/**
 * @author mmontisano
 * 
 */
public abstract class BaseFacadeHandler {

	protected PropertiesTree propertiesTree;
	//inizio LP PG200070 - 20200813
	protected Properties log4jConfiguration;
	//fine LP PG200070 - 20200813
	// private Connection connection;
	private String wsCommonsUrl;
	private String wsEMailSenderUrl;
	private String wsIntegraOtticoUrl;
	private String wsNotificheUrl;
	private String birtHomeEngine;
	private String birtHomeLogs;
	private String outputDirectoryPdf;
	private String birtDesignPath;
	private String pathLogoReport;
	//inizio LP PG200070 - 20200813
	//private PropertiesTree propsTree;
	//fine LP PG200070 - 20200813
	protected Logger logger;
	//protected SessionContext ctx;
	public final String DBSCHEMACODSOCIETA = "dbSchemaCodSocieta";
	
	/*
	public void ejbActivate() { }
	public void ejbPassivate() { }
	public void ejbRemove() { ctx = null; }
	*/
	/**
	 * @param arg0
	 */
	/*
	public void setSessionContext(SessionContext arg0) {
		try {
			ctx = arg0;
			applicationStartup();
		} catch (Exception e) {
			System.out.println("%%%% Error Inizialize Properties SessionBean %%%% " + e.getMessage());
		}
	}
	*/
	/**
	 * applicationStartup()
	 */
	//inizio LP PG200070 - 20200813
	//public BaseFacadeHandler() throws Exception {
	//	super();
	public BaseFacadeHandler(PropertiesTree propertiesTree, Properties log4jConfiguration) throws Exception {
		super();
		this.propertiesTree = propertiesTree;
		this.log4jConfiguration = log4jConfiguration;
		applicationStartup();
	}
	//fine LP PG200070 - 20200813
	
	protected void applicationStartup() throws Exception {
		//inizio LP PG200070 - 20200813
		//// get SystemVariable handler
		//SystemVariable sv = new SystemVariable();
		//// load the tree properties for this application 
		//String propertiesRootPath = sv.getSystemVariableValue(PrintStrings.ROOT.format());
		//fine LP PG200070 - 20200813
		try {
			//inizio LP PG200070 - 20200813
			//propertiesTree = new PropertiesTree(propertiesRootPath);
			//fine LP PG200070 - 20200813
			String catalogName = PropertiesPath.baseCatalogName.format();
			/* we set dataSourceName */
			/*this.dataSourceName = propertiesTree.getProperty(
					PropertiesPath.baseCatalogJndiAlias.format(catalogName));
			if (dataSourceName == null || dataSourceName.length() == 0)
				throw new Exception("DataSourceName properties for dataSource.name not found");

			 we set schema 
			this.schema = propertiesTree.getProperty(PropertiesPath.baseCatalogSchema.format(catalogName));
			if (this.schema == null || this.schema.length() == 0)
				throw new Exception("Schema value for dataSource.schema not found");*/	

			/* we set wsCommonsUrl */
			this.wsCommonsUrl = propertiesTree.getProperty(PropertiesPath.wsCommonsUrl.format(catalogName));
			if (this.wsCommonsUrl == null || this.wsCommonsUrl.length() == 0)
				throw new Exception("wsCommonsUrl value for ws.wsCommonsUrl not found");

			/* we set wsEMailSenderUrl */
			this.wsEMailSenderUrl = propertiesTree.getProperty(PropertiesPath.wsEMailSenderUrl.format(catalogName));
			if (this.wsEMailSenderUrl == null || this.wsEMailSenderUrl.length() == 0)
				throw new Exception("wsEMailSenderUrl value for ws.wsEMailSenderUrl not found");
			
			/* we set wsIntegraOtticoUrl */
			this.wsIntegraOtticoUrl = propertiesTree.getProperty(PropertiesPath.wsIntegraOtticoUrl.format(catalogName));
			if (this.wsIntegraOtticoUrl == null || this.wsIntegraOtticoUrl.length() == 0)
				throw new Exception("wsIntegraOtticoUrl value for ws.wsIntegraOtticoUrl not found");
			
			/* we set wsNotificheUrl */
			this.wsNotificheUrl = propertiesTree.getProperty(PropertiesPath.wsNotificheUrl.format(catalogName));
			if (this.wsNotificheUrl == null || this.wsNotificheUrl.length() == 0)
				throw new Exception("wsNotificheUrl value for ws.wsNotificheUrl not found");
			
			/* we set birtHomeEngine */
			this.birtHomeEngine = propertiesTree.getProperty(PropertiesPath.birtHomeEngine.format(catalogName));
			if (this.birtHomeEngine == null || this.birtHomeEngine.length() == 0)
				throw new Exception("birtHomeEngine value for birtHomeEngine not found");

			/* we set birtHomeLogs */
			this.birtHomeLogs = propertiesTree.getProperty(PropertiesPath.birtHomeLogs.format(catalogName));
			if (this.birtHomeLogs == null || this.birtHomeLogs.length() == 0)
				throw new Exception("birtHomeLogs value for ws.birtHomeLogs not found");
		
			/* we set outputDirectoryPdf */
			this.outputDirectoryPdf = propertiesTree.getProperty(PropertiesPath.outputDirectoryPdf.format(catalogName));
			if (this.outputDirectoryPdf == null || this.outputDirectoryPdf.length() == 0)
				throw new Exception("outputDirectoryPdf value for ws.outputDirectoryPdf not found");
			
			/* we set outputDirectoryPdf */
			this.birtDesignPath = propertiesTree.getProperty(PropertiesPath.birtDesignPath.format(catalogName));
			if (this.birtDesignPath == null || this.birtDesignPath.length() == 0)
				throw new Exception("birtDesignPath value not found");
			
			/* we set outputDirectoryPdf */
			this.pathLogoReport = propertiesTree.getProperty(PropertiesPath.pathLogoReport.format());
			if (this.pathLogoReport == null || this.pathLogoReport.length() == 0)
				throw new Exception("pathLogoReport value not found");
			
			/* we set initialContextFactory 
			this.initialContextFactory = propertiesTree.getProperty(
					PropertiesPath.baseCatalogInitialContextFactory.format(catalogName));
			
			 we set defaultListRows 
			this.defaultListRows = Integer.parseInt(propertiesTree.getProperty(
					PropertiesPath.defaultListRows.format(catalogName)));
			if (this.defaultListRows == 0)
				throw new Exception("Schema value for dataSource.defaultListRows not found");
*/
			/* we initialize logger */
//            Properties log4jConfiguration = propertiesTree.getProperties(PropertiesPath.baseLogger.format());                         
//            LoggerHierarchyServer loggerHierarchyServer = new LoggerHierarchyServer();
//            Hierarchy hierarchy = loggerHierarchyServer.configure(log4jConfiguration);
            
            logger = Logger.getLogger("FACADE");
            logger.info("<com.seda.payer.ottico.facade - applicationStartup()>");
//            this.propsTree = propertiesTree;
		} catch (Exception e) { throw e; }
    }
	/**
	 * @return the dataSourceName
	 */
	/*public String getDataSourceName() {
		return dataSourceName;
	}
	*//**
	 * @return the connection
	 *//*
	public Connection getConnection() throws JndiProxyException {
		return new JndiProxy().getSqlConnection(this.initialContextFactory, this.dataSourceName, true);
	}*/
	/**
	 * @return the connection
	 */
	public Connection getTransactionConnection(String dbSchemaCodSocieta) throws JndiProxyException {
		return new JndiProxy().getSqlConnection(getInitialContextFactory(dbSchemaCodSocieta),
				getDataSourceName(dbSchemaCodSocieta), false);
	}
	public Connection getConnection(String dbSchemaCodSocieta) throws JndiProxyException {
		return new JndiProxy().getSqlConnection(getInitialContextFactory(dbSchemaCodSocieta),
					getDataSourceName(dbSchemaCodSocieta), true);
	}
	public Connection getConnection(String dbSchemaCodSocieta, boolean autoCommit) throws JndiProxyException {
		return new JndiProxy().getSqlConnection(getInitialContextFactory(dbSchemaCodSocieta),
					getDataSourceName(dbSchemaCodSocieta), autoCommit);
	}
	/**
	 * @param connection
	 */
	public void closeConnection(Connection connection) {
		DAOHelper.closeIgnoringException(connection);
	}
	
	/**
	 * @return the initialContextFactory
	 *//*
	public String getInitialContextFactory() {
		return initialContextFactory;
	}*/
	/**
	 * @return the schema
	 */
	/*public String getSchema() {
		return schema;
	}*/
	/**
	 * @return the defaultListRows
	 */
	/*public int getDefaultListRows() {
		return defaultListRows;
	}*/
	/**
	 * @return the wsEMailSenderUrl
	 */
	public String getWsEMailSenderUrl() {
		return wsEMailSenderUrl;
	}
	/**
	 * @param wsEMailSenderUrl the wsEMailSenderUrl to set
	 */
	public void setWsEMailSenderUrl(String wsEMailSenderUrl) {
		this.wsEMailSenderUrl = wsEMailSenderUrl;
	}
	/**
	 * @return the birtHomeEngine
	 */
	public String getBirtHomeEngine() {
		return birtHomeEngine;
	}
	/**
	 * @param birtHomeEngine the birtHomeEngine to set
	 */
	public void setBirtHomeEngine(String birtHomeEngine) {
		this.birtHomeEngine = birtHomeEngine;
	}
	/**
	 * @return the birtHomeLogs
	 */
	public String getBirtHomeLogs() {
		return birtHomeLogs;
	}
	/**
	 * @param birtHomeLogs the birtHomeLogs to set
	 */
	public void setBirtHomeLogs(String birtHomeLogs) {
		this.birtHomeLogs = birtHomeLogs;
	}
	/**
	 * @return the outputDirectoryPdf
	 */
	public String getOutputDirectoryPdf() {
		return outputDirectoryPdf;
	}
	/**
	 * @param outputDirectoryPdf the outputDirectoryPdf to set
	 */
	public void setOutputDirectoryPdf(String outputDirectoryPdf) {
		this.outputDirectoryPdf = outputDirectoryPdf;
	}
	/**
	 * @return the birtDesignPath
	 */
	public String getBirtDesignPath() {
		return birtDesignPath;
	}
	/**
	 * @param birtDesignPath the birtDesignPath to set
	 */
	public void setBirtDesignPath(String birtDesignPath) {
		this.birtDesignPath = birtDesignPath;
	}
	public void setPathLogoReport(String pathLogoReport) {
		this.pathLogoReport = pathLogoReport;
	}
	public String getPathLogoReport() {
		return pathLogoReport;
	}
	/**
	 * @return the wsIntegraOtticoUrl
	 */
	public String getWsIntegraOtticoUrl() {
		return wsIntegraOtticoUrl;
	}
	/**
	 * @param wsIntegraOtticoUrl the wsIntegraOtticoUrl to set
	 */
	public void setWsIntegraOtticoUrl(String wsIntegraOtticoUrl) {
		this.wsIntegraOtticoUrl = wsIntegraOtticoUrl;
	}
	public void setWsNotificheUrl(String wsNotificheUrl) {
		this.wsNotificheUrl = wsNotificheUrl;
	}
	public String getWsNotificheUrl() {
		return wsNotificheUrl;
	}
	/**
	 * @return the wsCommonsUrl
	 */
	public String getWsCommonsUrl() {
		return wsCommonsUrl;
	}
	/**
	 * @param wsCommonsUrl the wsCommonsUrl to set
	 */
	public void setWsCommonsUrl(String wsCommonsUrl) {
		this.wsCommonsUrl = wsCommonsUrl;
	}
	/**
	 * @return
	 */
	//public PropertiesTree propertiesTree() {
	//	return this.propsTree;
	//}
	public String getInitialContextFactory(String dbSchemaCodSocieta) {
		return propertiesTree.getProperty(PropertiesPath.baseCatalogInitialContextFactory.format(dbSchemaCodSocieta));
	}
	public String getDataSourceName(String dbSchemaCodSocieta) {
		//System.out.println("datasource otticofacade" + propertiesTree.getProperty(PropertiesPath.baseCatalogJndiAlias.format(dbSchemaCodSocieta)));
		return propertiesTree.getProperty(PropertiesPath.baseCatalogJndiAlias.format(dbSchemaCodSocieta));
	}
	public String getSchema(String dbSchemaCodSocieta) {
		return propertiesTree.getProperty(PropertiesPath.baseCatalogSchema.format(dbSchemaCodSocieta));
	}
	public int getDefaultListRows(String dbSchemaCodSocieta) {
		return Integer.parseInt(propertiesTree.getProperty(PropertiesPath.defaultListRows.format(dbSchemaCodSocieta)));
	}
	
	public void setCodSocietaHeaderIntegraOttico(IntegraOtticoSOAPBindingStub stub, String dbSchemaCodSocieta) {
		if (dbSchemaCodSocieta.equals("000P4"))
			stub.setTimeout(30000);
		else 
			stub.setTimeout(8000); //TODO:VERIFICARE
		stub.clearHeaders();
		stub.setHeader("",DBSCHEMACODSOCIETA,dbSchemaCodSocieta);		
	}
	
	public void setCodSocietaHeaderCommons(CommonsSOAPBindingStub stub, String dbSchemaCodSocieta) {
		stub.clearHeaders();
		stub.setHeader("",DBSCHEMACODSOCIETA,dbSchemaCodSocieta);		
	}
	
	public void setCodSocietaHeaderNotifiche(NotificheSOAPBindingStub stub, String dbSchemaCodSocieta) {
		stub.clearHeaders();
		stub.setHeader("",DBSCHEMACODSOCIETA,dbSchemaCodSocieta);		
	}
	
}