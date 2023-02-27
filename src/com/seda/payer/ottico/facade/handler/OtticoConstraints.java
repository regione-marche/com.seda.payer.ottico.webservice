package com.seda.payer.ottico.facade.handler;
/**
 * @author aniello.labua
 */
public interface OtticoConstraints {

	/*we define email constraints*/
	public static final String MESSAGGIO_FLOW_EXIST = "Il flusso processato {0} è già presente.";
	public static final String MESSAGGIO_FLOW_ERROR = "Il flusso processato {0} non ha superato i controlli di validazione.<br/>Di seguito il dettaglio: <br/>";
	public static final String MESSAGGIO_FILE_IMG_NOT_EXIST = "Il flusso immagini indicato nel flusso processato {0} non è presente.";
	public static final String MESSAGGIO_CONFIGURATION_MALFORMED = "La tabella della configurazione non è correttamente impostata " +
			"per la Società {0}, Utente {1} e Ente {2}.<br/>Di seguito gli errori riscontrati: ";
	public static final String SUBJECT = "Servizio OTTICO: Risultati elaborazione";
}
