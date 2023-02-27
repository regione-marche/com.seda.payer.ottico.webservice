/**
 * ManagerSOAPBindingImpl.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package com.seda.payer.ottico.webservice.manager.source;

import java.sql.Timestamp;
import com.seda.data.spi.PageInfo;
import com.seda.payer.ottico.facade.bean.ManagerFacadeBean;
import com.seda.payer.ottico.facade.bean.OtticoFacadeBean;
import com.seda.payer.ottico.webservice.dto.BaseSearchKeys;
import com.seda.payer.ottico.webservice.dto.CollectionDto;
import com.seda.payer.ottico.webservice.dto.DateOtticoInfo;
import com.seda.payer.ottico.webservice.dto.TestataFlussoOtticoDto;
import com.seda.payer.ottico.webservice.handler.WebServiceHandler;
import com.seda.payer.ottico.webservice.manager.dati.ListResponseType;
import com.seda.payer.ottico.webservice.manager.dati.ListResponseTypePageInfo;
import com.seda.payer.ottico.webservice.manager.dati.ResponseType;
import com.seda.payer.ottico.webservice.manager.dati.ResponseTypeRetCode;
import com.seda.payer.ottico.webservice.manager.dati.RicercaListaElabOtticoResponse;
import com.seda.payer.ottico.webservice.srv.FaultType;

public class Manager extends WebServiceHandler implements com.seda.payer.ottico.webservice.manager.source.ManagerInterface{
	//inizio LP PG200070 - 20200813
	private ManagerFacadeBean managerFacadeBean = null;

	private ManagerFacadeBean getFacadeService() throws Exception
	{
		if(managerFacadeBean == null) {
			managerFacadeBean = new ManagerFacadeBean(configuration, log4jConfiguration);
		}
		return managerFacadeBean;
	}
	//fine LP PG200070 - 20200813
	/* <code></code>
	 * @see com.seda.payer.ottico.webservice.manager.source.ManagerInterface#ricercaListaElabOttico(com.seda.payer.ottico.webservice.manager.dati.RicercaListaElabOtticoRequest)
	 */
    public com.seda.payer.ottico.webservice.manager.dati.RicercaListaElabOtticoResponse ricercaListaElabOttico(com.seda.payer.ottico.webservice.manager.dati.RicercaListaElabOtticoRequest in) throws java.rmi.RemoteException, com.seda.payer.ottico.webservice.manager.srv.FaultType {
    	try {
    		//inizio LP PG200070 - 20200813
    		//ManagerFacadeBean bean = new ManagerFacadeBean();
    		ManagerFacadeBean bean = getFacadeService();
    		//fine LP PG200070 - 20200813
    		TestataFlussoOtticoDto tfoDto = new TestataFlussoOtticoDto()
	    		.setBaseSearchKeys(new BaseSearchKeys(in.getCodiceSocieta(), in.getCodiceUtente(), in.getCodiceEnte()))
	    		.setSiglaProvincia(in.getSiglaProvincia())
	    		.setDateOtticoInfo(new DateOtticoInfo(null, null, 
	    				in.getDataElaborazioneDa() == null ? null : new Timestamp(in.getDataElaborazioneDa().getTimeInMillis()),
	    				in.getDataElaborazioneA() == null ? null : new Timestamp(in.getDataElaborazioneA().getTimeInMillis()),
	    	    		in.getDataCreazioneDa() == null ? null : new Timestamp(in.getDataCreazioneDa().getTimeInMillis()),
	    	    	    in.getDataCreazioneA() == null ? null : new Timestamp(in.getDataCreazioneA().getTimeInMillis())))
	    		.setTipologiaDocumento(in.getTipologiaFlusso())
	    		.setStatoDocumento(in.getStatoFlusso());

    		CollectionDto result =	bean.ricercaListaElabOttico(tfoDto, in.getRowsPerPage(), in.getPageNumber(), in.getOrder(), dbSchemaCodSocieta);
    		PageInfo info = result.getPageInfo();
			RicercaListaElabOtticoResponse response = new RicercaListaElabOtticoResponse(); {
				ListResponseTypePageInfo pageInfo = new ListResponseTypePageInfo(); {
		    		pageInfo.setFirstRow(info.getFirstRow());
		    		pageInfo.setLastRow(info.getLastRow());
		    		pageInfo.setNumPages(info.getNumPages());
		    		pageInfo.setNumRows(info.getNumRows());
		     		pageInfo.setPageNumber(info.getPageNumber());
		     		pageInfo.setRowsPerPage(info.getRowsPerPage());
				}
	     		response.setListaOttico(new ListResponseType(result.getListXml(), pageInfo));
	     		response.setResponse(new ResponseType(ResponseTypeRetCode.value1, "Request Execute Succeful"));
	    	}
     		debug("Manager_WS: \n\tMetodo: ricerca \n\tStai ritornando un oggeeto di tipo response");
     		return response;

		} catch (Exception e) {
			error("ricercaListaElabOttico failed, generic error due to: ", e);
			throw new FaultType(505, e.getMessage());
		}
    }
}