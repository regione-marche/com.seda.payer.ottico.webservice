/**
 * 
 */
package com.seda.payer.ottico.facade.handler;

import java.text.MessageFormat;
import java.util.ResourceBundle;

/**
 * @author mraimondo
 *
 */
public enum Template {
//	SMS,
//	MAIL_AUTORIZZAZIONE,
//	MAIL_AUTORIZZAZIONE_DATI_UTENTE,
//	MAIL_AUTORIZZAZIONE_DATI_TRANSAZIONE,
//	MAIL_AUTORIZZAZIONE_IMPORTI,
//	MAIL_BOLLETTINO,
//	MAIL_OGGETTO_AUT_BANCA,
//	MAIL_OGGETTO_BOLLETTINI,
//	MAIL_OGGETTO_SCARTO,
//	MAIL_SCARTO,
//	MAIL_SCARTO_DATI_UTENTE,
//	MAIL_SCARTO_DATI_TRANSAZIONE;
	
	MESSAGGIO_VALIDAZIONE_ERRATA,
	MESSAGGIO_FLOW_EXIST,
	SUBJECT;

    private static ResourceBundle rb;

    public String format( Object... args ) {
        synchronized(Template.class) {
            if(rb==null)
                rb = ResourceBundle.getBundle(Template.class.getName());
            return MessageFormat.format(rb.getString(name()),args);
        }
    }
}
