package com.seda.payer.ottico.facade.handler;

import java.text.MessageFormat;
import java.util.ResourceBundle;

public enum PrintStrings {
	EMPTY,
	ROOT,
	PROPERTIES_CONTEXT_NAME,
	PROPERTIES_EC_CONTEXT_NAME,
	TREE_CONTEXT_NAME,
	LOGGER_CONTEXT_NAME,
	LOGGER_PROPERTIES_NAME
	;
	
    private static ResourceBundle rb;

    public String format( Object... args ) {
        synchronized(PrintStrings.class) {
            if(rb==null)
                rb = ResourceBundle.getBundle(PrintStrings.class.getName());
            return MessageFormat.format(rb.getString(name()),args);
        }
    }
}
