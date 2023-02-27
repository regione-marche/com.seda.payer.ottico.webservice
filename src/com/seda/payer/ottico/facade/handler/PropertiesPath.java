package com.seda.payer.ottico.facade.handler;

import java.text.MessageFormat;
import java.util.ResourceBundle;
/**
 * @author mmontisano
 *
 */
public enum PropertiesPath {
	baseCatalogName,
	baseCatalogInitialContextFactory,
	baseCatalogJndiAlias, 
	baseCatalogSchema, 
	defaultListRows,
	baseLogger,
	wsCommonsUrl,
	wsEMailSenderUrl,
	wsOtticoUrl,
	wsIntegraOtticoUrl,
	wsNotificheUrl,
	birtHomeEngine,
	birtHomeLogs,
	outputDirectoryPdf,
	birtDesignPath,
	pathLogoReport
	;

    private static ResourceBundle rb;

    public String format( Object... args ) {
        synchronized(PropertiesPath.class) {
            if(rb==null)
                rb = ResourceBundle.getBundle(PropertiesPath.class.getName());
            return MessageFormat.format(rb.getString(name()),args);
        }
    }
}
