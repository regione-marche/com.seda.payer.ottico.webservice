package com.seda.payer.ottico.facade.bean;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.Properties;
import com.seda.commons.properties.tree.PropertiesTree;
import com.seda.payer.core.dao.TestataFlussoOtticoDao;
import com.seda.payer.ottico.webservice.dto.BaseSearchKeys;
import com.seda.payer.ottico.webservice.dto.CollectionDto;
import com.seda.payer.ottico.webservice.dto.TestataFlussoOtticoDto;
import com.seda.payer.ottico.webservice.exception.FacadeException;
import com.seda.payer.ottico.facade.handler.BaseFacadeHandler;

public class ManagerFacadeBean extends BaseFacadeHandler {
	//inizio LP PG200070 - 20200813
	//public ManagerFacadeBean() throws Exception {
	//	super();
	//}
	public ManagerFacadeBean(PropertiesTree propertiesTree, Properties log4jConfiguration) throws Exception {
		super(propertiesTree, log4jConfiguration);
	}
	//fine LP PG200070 - 20200813

	private static final long serialVersionUID = 1L;

	public String recuperaLogElabDettaglio(BaseSearchKeys srcKeys, String percossoFlussoLog, String flussoLogDettaglio, String dbSchemaCodSocieta)  throws FacadeException{
		return null;
	}

	public String recuperaLogElabRiepilogo(String dbSchemaCodSocieta)  throws FacadeException {
		return null;
	}

	public CollectionDto ricercaListaElabOttico(TestataFlussoOtticoDto dto, int rowsPerPage, int pageNumber, String order, String dbSchemaCodSocieta) throws FacadeException{
		Connection connection = null;
		try {
			logger.debug("DTO OBJ: " + dto.toString());
			connection = getConnection(dbSchemaCodSocieta);
			TestataFlussoOtticoDao dao = new TestataFlussoOtticoDao(connection, getSchema(dbSchemaCodSocieta));
			dao.doWebRowSets(dto.toBean(dto), rowsPerPage <= 0 ? this.getDefaultListRows(dbSchemaCodSocieta) : rowsPerPage, pageNumber, order);
			return new CollectionDto(dao.getWebRowSetXml(TestataFlussoOtticoDao.IDX_DOLIST_LISTA), dao.getPageInfo());
			
		} catch (Exception ex) {
			logger.error("ricercaListaElabOttico failed, generic error due to: ", ex);
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
}