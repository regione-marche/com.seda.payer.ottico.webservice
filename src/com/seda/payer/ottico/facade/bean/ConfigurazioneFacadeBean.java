package com.seda.payer.ottico.facade.bean;

import java.rmi.RemoteException;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.Properties;

import com.seda.commons.properties.tree.PropertiesTree;
import com.seda.payer.core.bean.Configurazione;
import com.seda.payer.core.bean.TemplateDocumento;
import com.seda.payer.core.dao.ConfigurazioneDao;
import com.seda.payer.core.dao.TemplateDocumentoDao;
import com.seda.payer.ottico.webservice.dto.CollectionDto;
import com.seda.payer.ottico.webservice.dto.ConfigurazioneDto;
import com.seda.payer.ottico.webservice.dto.ConfigurazioneIdentify;
import com.seda.payer.ottico.webservice.dto.TemplateDocumentoDto;
import com.seda.payer.ottico.webservice.exception.FacadeException;
import com.seda.payer.ottico.facade.handler.BaseFacadeHandler;

public class ConfigurazioneFacadeBean extends BaseFacadeHandler {
	//inizio LP PG200070 - 20200813
	//public ConfigurazioneFacadeBean() throws Exception {
	//	super();
	//}
	public ConfigurazioneFacadeBean(PropertiesTree propertiesTree, Properties log4jConfiguration) throws Exception {
		super(propertiesTree, log4jConfiguration);
	}
	//fine LP PG200070 - 20200813
	
	private static final long serialVersionUID = 1L;

	public ConfigurazioneDto dettaglioParametriOttico(String codiceSocieta, String codiceUtente, String chiaveEnte, String dbSchemaCodSocieta)throws FacadeException{
		
		Connection conn = null;
		//System.out.println("dettaglioParametriOttico EJB SCHEMA:" +  dbSchemaCodSocieta + "-" + codiceUtente + "-" + chiaveEnte);
		try {
			conn = getTransactionConnection(dbSchemaCodSocieta);
//			System.out.println("conn: " + conn);
//			System.out.println("dettaglioParametriOttico EJB 2");
			ConfigurazioneDao daoObj = new ConfigurazioneDao(conn, getSchema(dbSchemaCodSocieta));
//			System.out.println("dettaglioParametriOttico EJB 3");
			ConfigurazioneDto dtoObj =  new ConfigurazioneDto(daoObj.doDetail(codiceSocieta, codiceUtente, chiaveEnte));			 
//			System.out.println("dettaglioParametriOttico EJB 4" + dtoObj);
			return dtoObj;
			
		} catch (Exception ex) {
			logger.error("dettaglioParametiOttico failed, generic error due to: ", ex);
			throw new FacadeException(ex);
		} finally {
			//System.out.println("dettaglioParametriOttico EJB 5" );
			try {
				//System.out.println("coonection is colsed?" + conn.isClosed());
				conn.commit();
			} catch (SQLException e) {
				e.printStackTrace();
			}
			//inizio LP PG21XX04 Leak
    		//closeConnection(conn);
	    	try {
	    		if(conn != null) {
	    			conn.close();
	    		}
	    	} catch (SQLException e) {
	    		e.printStackTrace();
			}
			//fine LP PG21XX04 Leak
		}
	}
	
	public TemplateDocumentoDto dettaglioAssociaTemplate(String chiaveTemplate, String dbSchemaCodSocieta)throws FacadeException{
		Connection conn = null;
		try {
			conn = getConnection(dbSchemaCodSocieta);
			TemplateDocumentoDao daoObj = new TemplateDocumentoDao(conn, getSchema(dbSchemaCodSocieta));

			TemplateDocumento templateDocumento = daoObj.doDetail(null,null, null, null, null, null,null,null,chiaveTemplate); 
			logger.info(templateDocumento);

			TemplateDocumentoDto dettaglio = new TemplateDocumentoDto(templateDocumento);

			logger.info(dettaglio);

			return dettaglio;

		} catch (Exception ex) {
			logger.error("dettaglioAssociaTemplate failed, generic error due to: ", ex);
			throw new FacadeException(ex);
		} finally {
			//inizio LP PG21XX04 Leak
    		//closeConnection(conn);
	    	try {
	    		if(conn != null) {
	    			conn.close();
	    		}
	    	} catch (SQLException e) {
	    		e.printStackTrace();
			}
			//fine LP PG21XX04 Leak
		}
	}
	/**
	 * @ejb.interface-method
	 * @ejb.transaction type="NotSupported"
	 */
	public boolean verificaAssociaTemplate(String tipoDocumento, String codiceSocieta, String codiceUtente, String codiceEnte, 
										   String tipologiaServizio, Timestamp dataInizio, Timestamp dataFine, 
										   String chiaveTemplate, String dbSchemaCodSocieta) throws FacadeException {
		Connection conn = null;
		try {
			conn = getConnection(dbSchemaCodSocieta);
			TemplateDocumentoDao daoObj = new TemplateDocumentoDao(conn, getSchema(dbSchemaCodSocieta));
			boolean result = daoObj.isOverlay(tipoDocumento, codiceSocieta, codiceUtente, codiceEnte, tipologiaServizio, dataInizio, dataFine, chiaveTemplate); 
			logger.info("verificaAssociaTemplate - template " + (result ? "trovato" : "non trovato"));
			return result;

		} catch (Exception ex) {
			logger.error("verificaAssociaTemplate failed, generic error due to: ", ex);
			throw new FacadeException(ex);
		} finally {
			//inizio LP PG21XX04 Leak
    		//closeConnection(conn);
	    	try {
	    		if(conn != null) {
	    			conn.close();
	    		}
	    	} catch (SQLException e) {
	    		e.printStackTrace();
			}
			//fine LP PG21XX04 Leak
		}
	}
	/**
	 * @ejb.interface-method
	 * @ejb.transaction type="NotSupported"
	 */
    public CollectionDto listaParametriOttico(ConfigurazioneDto dto, int rowsPerPage, int pageNumber, String dbSchemaCodSocieta) throws FacadeException {
    	Connection connection = null;
    	try {
    		//logger.debug("DTO OBJ: " + dto.toString());
    		connection = getConnection(dbSchemaCodSocieta);
    		//System.out.println("connection = " + connection);
    		ConfigurazioneDao dao = new ConfigurazioneDao(connection, getSchema(dbSchemaCodSocieta));
    		dao.doWebRowSets(dto.toBean(dto), rowsPerPage <= 0 ? this.getDefaultListRows(dbSchemaCodSocieta) : rowsPerPage, pageNumber);
    		
    		return new CollectionDto(dao.getWebRowSetXml(ConfigurazioneDao.IDX_DOLIST_LISTA), dao.getPageInfo());
    		
		} catch (Exception ex) {
			logger.error("listaParametriOttico failed, generic error due to: ", ex);
			throw new FacadeException(ex);
		} finally {
			//inizio LP PG21XX04 Leak
    		//closeConnection(connection);
	    	try {
	    		if(connection != null) {
	    			connection.close();
	    		}
	    	} catch (SQLException e) {
	    		e.printStackTrace();
			}
			//fine LP PG21XX04 Leak
		}
    }
	/**
	 * @ejb.interface-method
	 * @ejb.transaction type="NotSupported"
	 */
	public CollectionDto listaAssociaTemplate(TemplateDocumentoDto dto, int rowsPerPage, int pageNumber, String dbSchemaCodSocieta) throws FacadeException {
		Connection connection = null;
    	try {
    		logger.debug("DTO OBJ: " + dto.toString());
    		connection = getConnection(dbSchemaCodSocieta);
    		TemplateDocumentoDao dao = new TemplateDocumentoDao(connection, getSchema(dbSchemaCodSocieta));
    		dao.doWebRowSets(dto.toBean(dto), rowsPerPage <= 0 ? this.getDefaultListRows(dbSchemaCodSocieta) : rowsPerPage, pageNumber);
    		
    		return new CollectionDto(dao.getWebRowSetXml(TemplateDocumentoDao.IDX_DOLIST_LISTA), dao.getPageInfo());
    		
		} catch (Exception ex) {
			logger.error("listaAssociaTemplate failed, generic error due to: ", ex);
			throw new FacadeException(ex);
		} finally {
			//inizio LP PG21XX04 Leak
    		//closeConnection(connection);
	    	try {
	    		if(connection != null) {
	    			connection.close();
	    		}
	    	} catch (SQLException e) {
	    		e.printStackTrace();
			}
			//fine LP PG21XX04 Leak
		}
	}
	/**
	 * @ejb.interface-method
	 * @ejb.transaction type="NotSupported"
	 */
	public void creaAssociaTemplate(TemplateDocumentoDto dto, String dbSchemaCodSocieta) throws FacadeException {
		Connection conn = null;
		try {
			conn = getTransactionConnection(dbSchemaCodSocieta);
			new TemplateDocumentoDao(conn, getSchema(dbSchemaCodSocieta)).doInsert(dto.toBean(dto));

		} catch (Exception ex) {
			logger.error("creaAssociaTemplate failed, generic error due to: ", ex);
			throw new FacadeException(ex);
		} finally {
			//inizio LP PG21XX04 Leak
    		//closeConnection(conn);
	    	try {
	    		if(conn != null) {
	    			conn.close();
	    		}
	    	} catch (SQLException e) {
	    		e.printStackTrace();
			}
			//fine LP PG21XX04 Leak
		}
	}
	/**
	 * @ejb.interface-method
	 * @ejb.transaction type="NotSupported"
	 */
	public void creaParametriOttico(ConfigurazioneDto dto, String dbSchemaCodSocieta) throws FacadeException{
		Connection conn = null;
		try {
			conn = getTransactionConnection(dbSchemaCodSocieta);
			logger.info("TEST INS CFO: " + dto.toString());
			
			Configurazione configurazione = dto.toBean(dto);
			
			logger.info(configurazione);
			
			new ConfigurazioneDao(conn, getSchema(dbSchemaCodSocieta)).doInsert(configurazione);

		} catch (Exception ex) {
			logger.error("creaParametriOttico failed, generic error due to: ", ex);
			throw new FacadeException(ex);
		} finally {
			//inizio LP PG21XX04 Leak
    		//closeConnection(conn);
	    	try {
	    		if(conn != null) {
	    			conn.close();
	    		}
	    	} catch (SQLException e) {
	    		e.printStackTrace();
			}
			//fine LP PG21XX04 Leak
		}
	}
	/**
	 * @ejb.interface-method
	 * @ejb.transaction type="NotSupported"
	 */
	public void modificaAssociaTemplate(TemplateDocumentoDto dto, String dbSchemaCodSocieta ) throws FacadeException{
		Connection conn = null;
		try {
			conn = getTransactionConnection(dbSchemaCodSocieta);
			logger.info("facade.modificaAssociaTemplate - " + dto);
			TemplateDocumento templateDocumento = dto.toBean(dto);
			logger.info("facade.modificaAssociaTemplate (bean) - " + templateDocumento);
			new TemplateDocumentoDao(conn, getSchema(dbSchemaCodSocieta)).doUpdate(templateDocumento);

		} catch (Exception ex) {
			logger.error("modificaAssociaTemplate failed, generic error due to: ", ex);
			throw new FacadeException(ex);
		} finally {
			//inizio LP PG21XX04 Leak
    		//closeConnection(conn);
	    	try {
	    		if(conn != null) {
	    			conn.close();
	    		}
	    	} catch (SQLException e) {
	    		e.printStackTrace();
			}
			//fine LP PG21XX04 Leak
		}
	}
	/**
	 * @ejb.interface-method
	 * @ejb.transaction type="NotSupported"
	 */
	public void modificaParametriOttico(ConfigurazioneDto dto, String dbSchemaCodSocieta) throws FacadeException {
		Connection conn = null;
		try {
			logger.info("modificaParametriOttico dto - " + dto.toString());
			
			Configurazione configurazione = dto.toBean(dto);
			
			logger.info("modificaParametriOttico bean - " + configurazione);
			
			conn = getTransactionConnection(dbSchemaCodSocieta);
			new ConfigurazioneDao(conn, getSchema(dbSchemaCodSocieta)).doUpdate(configurazione);

		} catch (Exception ex) {
			logger.error("modificaParametriOttico failed, generic error due to: ", ex);
			throw new FacadeException(ex);
		} finally {
			//inizio LP PG21XX04 Leak
    		//closeConnection(conn);
	    	try {
	    		if(conn != null) {
	    			conn.close();
	    		}
	    	} catch (SQLException e) {
	    		e.printStackTrace();
			}
			//fine LP PG21XX04 Leak
		}
	}
	/**
	 * @ejb.interface-method
	 * @ejb.transaction type="NotSupported"
	 */
	public void eliminaAssociaTemplate(String chiaveTemplate, String dbSchemaCodSocieta) throws FacadeException{
		Connection conn = null;
		try {
			conn = getTransactionConnection(dbSchemaCodSocieta);
			logger.debug("Entrato in eliminaAssociaTemplate con chiave: " + chiaveTemplate);
			new TemplateDocumentoDao(conn, getSchema(dbSchemaCodSocieta)).doDelete(chiaveTemplate); 

		} catch (Exception ex) {
			logger.error("eliminaAssociaTemplate failed, generic error due to: ", ex);
			throw new FacadeException(ex);
		} finally {
			//inizio LP PG21XX04 Leak
    		//closeConnection(conn);
	    	try {
	    		if(conn != null) {
	    			conn.close();
	    		}
	    	} catch (SQLException e) {
	    		e.printStackTrace();
			}
			//fine LP PG21XX04 Leak
		}
	}
	/**
	 * @ejb.interface-method
	 * @ejb.transaction type="NotSupported"
	 */
	public void eliminaParametriOttico(ConfigurazioneIdentify identify, String dbSchemaCodSocieta) throws FacadeException{
		Connection conn = null;
		try {
			conn = getTransactionConnection(dbSchemaCodSocieta);
			Configurazione bean = new Configurazione(); {
				bean.setCodiceSocieta(identify.getCodiceSocieta());
				bean.setCodiceUtente(identify.getCodiceUtente());
				bean.setCodiceEnte(identify.getCodiceEnte());
			}
			new ConfigurazioneDao(conn, getSchema(dbSchemaCodSocieta)).doDelete(bean);

		} catch (Exception ex) {
			logger.error("eliminaAssociaTemplate failed, generic error due to: ", ex);
			throw new FacadeException(ex);
		} finally {
			//inizio LP PG21XX04 Leak
    		//closeConnection(conn);
	    	try {
	    		if(conn != null) {
	    			conn.close();
	    		}
	    	} catch (SQLException e) {
	    		e.printStackTrace();
			}
			//fine LP PG21XX04 Leak
		}
	}
}