package com.seda.payer.ottico.webservice.handler;

import java.util.Iterator;
import java.util.Properties;
import javax.xml.rpc.ServiceException;
import javax.xml.rpc.handler.soap.SOAPMessageContext;
import javax.xml.rpc.server.ServletEndpointContext;
import javax.xml.soap.SOAPException;
import javax.xml.soap.SOAPHeader;
import javax.xml.soap.SOAPHeaderElement;

import com.seda.commons.properties.tree.PropertiesTree;
import com.seda.j2ee5.webservice.spi.JaxRpc10WebServiceHandler;
import com.seda.payer.ottico.facade.handler.PropertiesPath;
import com.seda.payer.ottico.webservice.config.PrintStrings;

public abstract class WebServiceHandler extends JaxRpc10WebServiceHandler {

	protected String loggerContextName = PrintStrings.LOGGER_CONTEXT_NAME.format();
	protected String treeContextName = PrintStrings.TREE_CONTEXT_NAME.format();
	//inizio LP PG200070 - 20200813
	protected PropertiesTree configuration;
	protected Properties log4jConfiguration;
	//fine LP PG200070 - 20200813
	protected String dbSchemaCodSocieta; 

	@Override
	public void init(Object endPointContext) throws ServiceException {
		super.init(endPointContext);
		logger(loggerContextName);
    	propertiesTree(treeContextName);
    	//inizio LP PG200070 - 20200813
    	configuration = propertiesTree();
    	log4jConfiguration = configuration.getProperties(PropertiesPath.baseLogger.format());
    	//fine LP PG200070 - 20200813
    	setDbSchemaCodSocieta(endPointContext);
	}
	
	@SuppressWarnings("unchecked")
	private void setDbSchemaCodSocieta (Object endPointContext) {
		ServletEndpointContext ctx=null;
		
		if (javax.xml.rpc.server.ServletEndpointContext.class.isInstance(endPointContext))
			ctx = (ServletEndpointContext) endPointContext;

		if (ctx != null) {
			SOAPMessageContext mc = (SOAPMessageContext)ctx.getMessageContext();
			// process SOAP header as shown in the message handler
			try {
				SOAPHeader header = mc.getMessage().getSOAPPart().getEnvelope().getHeader();
				Iterator headers = header.examineAllHeaderElements();
				while (headers.hasNext()) {
					SOAPHeaderElement he = (SOAPHeaderElement)headers.next();
					if(he.getNodeName().equals("dbSchemaCodSocieta"))
						dbSchemaCodSocieta=new String(he.getValue());
				} 

			} catch (SOAPException e) {
				e.printStackTrace();
			}
		}
	}
}