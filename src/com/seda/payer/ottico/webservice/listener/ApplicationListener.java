package com.seda.payer.ottico.webservice.listener;

import javax.servlet.ServletContextEvent;
import com.seda.compatibility.SystemVariable;
import com.seda.j2ee5.listener.spi.ApplicationListenerHandler;
import com.seda.payer.ottico.webservice.config.PrintStrings;
/**
 * 
 * @author mmontisano
 *
 */
public class ApplicationListener extends ApplicationListenerHandler {
	/* (non-Javadoc)
	 * @see com.seda.j2ee5.listener.spi.ApplicationListenerHandler#contextDestroyed(javax.servlet.ServletContextEvent)
	 */
	public void contextDestroyed(ServletContextEvent event) { }
	/* (non-Javadoc)
	 * @see com.seda.j2ee5.listener.spi.ApplicationListenerHandler#contextInitialized(javax.servlet.ServletContextEvent)
	 */
	public void contextInitialized(ServletContextEvent event) {
		super.contextInitialized(event);
		
		String rootPath = event.getServletContext().getInitParameter(PrintStrings.CONFIG_FILE.format());
		//System.out.println("letto parametro configurazione :" + rootPath);
		if (rootPath==null) {
			SystemVariable sv = new SystemVariable();
			rootPath=sv.getSystemVariableValue(PrintStrings.ROOT.format());
			//System.out.println("letto parametro2 configurazione :" + rootPath);
			sv=null;
		} 
		if (rootPath!=null) {
			configurePropertiesTree(PrintStrings.TREE_CONTEXT_NAME.format(), rootPath);
			// initialize log4j for this application context
//			configureLogger(PrintStrings.LOGGER_CONTEXT_NAME.format(), 
//				propertiesTree().getProperties(PrintStrings.LOGGER_PROPERTIES_NAME.format()));
			//System.out.println("letto 3");
			//info("<com.seda.payer.ottico.webservice - contextInitialized()>");
			//info("<com.seda.payer.ottico.webservice - application is started>");
		}
	}
}