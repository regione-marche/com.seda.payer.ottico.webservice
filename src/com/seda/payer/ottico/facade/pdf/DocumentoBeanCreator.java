package com.seda.payer.ottico.facade.pdf;

import java.io.Serializable;
import java.sql.SQLException;

import com.seda.commons.logger.LoggerWrapper;
import com.seda.payer.commons.utility.StringUtility;
/**
 * @author marco.montisano
 */
public class DocumentoBeanCreator implements Serializable {

	private static final long serialVersionUID = 1L;
	private LoggerWrapper logger;
	/**
	 * Default constructor 
	 */
	public DocumentoBeanCreator(LoggerWrapper logger) {
		this.logger = logger;
	}
	/**
	 * @param cachedRowSetDoc
	 * @return
	 * @throws Exception
	 */
	public DocumentoType createListaDoc(String cachedRowSetDoc) {
		if (StringUtility.isEmpty(cachedRowSetDoc))
			return new DocumentoType();

		DocumentoType documento = new DocumentoType();
		//inizio LP PG21XX04 Leak
		javax.sql.rowset.CachedRowSet ecCached_ListaDoc = null;
		//fine LP PG21XX04 Leak
		try {
			//inizio LP PG21XX04 Leak
			//javax.sql.rowset.CachedRowSet ecCached_ListaDoc = null;
			//fine LP PG21XX04 Leak
			try { ecCached_ListaDoc = com.seda.commons.string.Convert.stringToWebRowSet(cachedRowSetDoc);	
			} catch (Exception e) {
				logger.warn("cachedRowSetDoc is not javax.sql.rowset.CachedRowSet return empty DocumentoType");
				return new DocumentoType();
			}
			
			if (ecCached_ListaDoc.size() > 0) {
				if (ecCached_ListaDoc.next()) {
					DocumentoType documentoTemp = new DocumentoType();
					// we set fields DOCUMENTO
					documentoTemp.setAnnoEmissione(ecCached_ListaDoc.getString(1));
					documentoTemp.setNumeroEmissione(ecCached_ListaDoc.getString(2));
					documentoTemp.setIdDoc(ecCached_ListaDoc.getString(3));
					documentoTemp.setIdCartella(ecCached_ListaDoc.getString(4));
					documentoTemp.setTipologiaServizio(ecCached_ListaDoc.getString(5));
					documentoTemp.setDescTipologia(ecCached_ListaDoc.getString(6));
					documentoTemp.setTipoDocumento(ecCached_ListaDoc.getString(7));
					documentoTemp.setNumeroBollettino(ecCached_ListaDoc.getString(11));
					documentoTemp.setDenominazione(ecCached_ListaDoc.getString(12));
					documentoTemp.setImpostaServizio(ecCached_ListaDoc.getString(13));
					documentoTemp.setDescImpServizio(ecCached_ListaDoc.getString(14));
					documentoTemp.setDescrizioneEnte(ecCached_ListaDoc.getString(15));
					documentoTemp.setImpInizialeDocumento(ecCached_ListaDoc.getString(17));
					// we set fields DETTAGLIO DOCUMENTO
					documentoTemp.setImpTributiIniziali(ecCached_ListaDoc.getString(21));
					documentoTemp.setImpDriNotificaIniziale(ecCached_ListaDoc.getString(24));
					documentoTemp.setImpComRiscIniziale(ecCached_ListaDoc.getString(27));
					documentoTemp.setImpIntMoraIniziale(ecCached_ListaDoc.getString(30));
					documentoTemp.setImpAltreSpeseIniziale(ecCached_ListaDoc.getString(33));
					documentoTemp.setImpTotaleDocIniziale(ecCached_ListaDoc.getString(36));
					documento = documentoTemp;
				}
			}
			return documento;
		} catch (Exception e) {
			if (documento != null && !StringUtility.isEmpty(documento.getNumeroBollettino())) {
				logger.error("cachedRowSetDoc is not javax.sql.rowset.CachedRowSet or malformed return default DocumentoType");
				return documento;
			} else {
				logger.error("cachedRowSetDoc is not javax.sql.rowset.CachedRowSet or malformed return empty DocumentoType");
				return new DocumentoType();
			}
		}
		//inizio LP PG21XX04 Leak
		finally {
	    	try {
	    		if(ecCached_ListaDoc != null) {
	    			ecCached_ListaDoc.close();
	    		}
	    	} catch (SQLException e) {
	    		e.printStackTrace();
			}
		}
		//fine LP PG21XX04 Leak
	}
	/**
	 * @param cachedRowSetScadenze
	 * @return
	 * @throws Exception
	 */
	public ScadenzaType[] createListaScadenze(String cachedRowSetScadenze) {
		if (StringUtility.isEmpty(cachedRowSetScadenze))
			return new ScadenzaType[] { new ScadenzaType() };

		ScadenzaType[] scadenzeLst = null;
		//inizio LP PG21XX04 Leak
		javax.sql.rowset.CachedRowSet ecCached_Scadenze = null;
		//fine LP PG21XX04 Leak
		try {
			//inizio LP PG21XX04 Leak
			//javax.sql.rowset.CachedRowSet ecCached_Scadenze = null;
			//fine LP PG21XX04 Leak
			try { ecCached_Scadenze = com.seda.commons.string.Convert.stringToWebRowSet(cachedRowSetScadenze);	
			} catch (Exception e) {
				logger.warn("cachedRowSetScadenze is not javax.sql.rowset.CachedRowSet return empty ScadenzaType[]");
				return new ScadenzaType[] { new ScadenzaType() };
			}
			scadenzeLst = new ScadenzaType[ecCached_Scadenze.size()];
			ScadenzaType scadenza = null;
			int i = 0;
			if (ecCached_Scadenze.size() > 0) {
				while (ecCached_Scadenze.next()) {
					scadenza = new ScadenzaType(ecCached_Scadenze.getString(1),
							ecCached_Scadenze.getString(2),
							ecCached_Scadenze.getString(3),
							ecCached_Scadenze.getString(4),
							ecCached_Scadenze.getString(5),
							ecCached_Scadenze.getString(6),
							ecCached_Scadenze.getString(7),
							ecCached_Scadenze.getString(8),
							ecCached_Scadenze.getString(9),
							ecCached_Scadenze.getString(10),
							ecCached_Scadenze.getString(11),
							ecCached_Scadenze.getString(12)
					);
					scadenzeLst[i] = scadenza;
					i++;
				}
			} else {
				logger.warn("cachedRowSetScadenze is empty return empty ScadenzaType[]");
				return new ScadenzaType[] { new ScadenzaType() };
			}
			return scadenzeLst;

		} catch (Exception e) {
			logger.error("cachedRowSetScadenze is not javax.sql.rowset.CachedRowSet or malformed return empty ScadenzaType[]");
			return new ScadenzaType[] { new ScadenzaType() };
		}
		//inizio LP PG21XX04 Leak
		finally {
	    	try {
	    		if(ecCached_Scadenze != null) {
	    			ecCached_Scadenze.close();
	    		}
	    	} catch (SQLException e) {
	    		e.printStackTrace();
			}
		}
		//fine LP PG21XX04 Leak
	}
	/**
	 * @param cachedRowSetTributi
	 * @return
	 * @throws Exception
	 */
	public TributoType[] createListaTributi(String cachedRowSetTributi) {
		if (StringUtility.isEmpty(cachedRowSetTributi))
			return new TributoType[] { new TributoType() };

		TributoType[] tributoLst = null;
		//inizio LP PG21XX04 Leak
		javax.sql.rowset.CachedRowSet ecCached_Tributi = null;
		//fine LP PG21XX04 Leak
		try {
			//inizio LP PG21XX04 Leak
			//javax.sql.rowset.CachedRowSet ecCached_Tributi = null;
			//fine LP PG21XX04 Leak
			try { ecCached_Tributi = com.seda.commons.string.Convert.stringToWebRowSet(cachedRowSetTributi);	
			} catch (Exception e) {
				logger.warn("cachedRowSetTributi is not javax.sql.rowset.CachedRowSet return empty TributoType[]");
				return new TributoType[] { new TributoType() };
			}
			tributoLst = new TributoType[ecCached_Tributi.size()];
			TributoType tributo = null;
			int i = 0;
			if (ecCached_Tributi.size() > 0) {
				while (ecCached_Tributi.next()) {
					tributo = new TributoType(ecCached_Tributi.getString(1),
							ecCached_Tributi.getString(2),
							ecCached_Tributi.getString(3),
							ecCached_Tributi.getString(4),
							ecCached_Tributi.getString(5),
							ecCached_Tributi.getString(6),
							ecCached_Tributi.getString(7)
					);
					tributoLst[i] = tributo;
					i++;
				}
			} else {
				logger.warn("cachedRowSetTributi is empty return empty TributoType[]");
				return new TributoType[] { new TributoType() };
			}
			return tributoLst;

		} catch (Exception e) {
			logger.error("cachedRowSetTributi is not javax.sql.rowset.CachedRowSet or malformed return empty TributoType[]");
			return new TributoType[] { new TributoType() };
		}
		//inizio LP PG21XX04 Leak
		finally {
	    	try {
	    		if(ecCached_Tributi != null) {
	    			ecCached_Tributi.close();
	    		}
	    	} catch (SQLException e) {
	    		e.printStackTrace();
			}
		}
		//fine LP PG21XX04 Leak
	}
}