package com.seda.payer.ottico.facade.pdf;

import java.text.MessageFormat;
import java.util.ResourceBundle;

public enum PrintStrings {
	EMPTY,
	DATE_FORMAT,
	RPTDESIGN_PATH,
	BOLLO_RPTDESIGN,
	CDSA_RPTDESIGN,
	FREA_RPTDESIGN,
	ICIM_RPTDESIGN,
	ISCM_RPTDESIGN,
	MAVA_RPTDESIGN,
	PREA_RPTDESIGN,
	SPOM_RPTDESIGN,
	QUIETANZA_RPTDESIGN,
	DOCUMENTO_RPTDESIGN,
	TIPO_BEAN_ICI,
	TIPO_BEAN_IV,
	TIPO_BEAN_FRECCIA,
	FLAG_SI,
	FLAG_NESSUNO	
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