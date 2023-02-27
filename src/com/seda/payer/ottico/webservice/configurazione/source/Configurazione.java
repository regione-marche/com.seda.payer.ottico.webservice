/**
 * ConfigurazioneSOAPBindingImpl.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package com.seda.payer.ottico.webservice.configurazione.source;

import java.rmi.RemoteException;
import java.sql.Timestamp;
import java.util.Calendar;
//inizio LP PG200070 - 20200813
//import javax.naming.Context;
//
//import com.seda.commons.logger.sysout.SysOutLogger;
//fine LP PG200070 - 20200813
import com.seda.data.spi.PageInfo;
//inizio LP PG200070 - 20200813
//import com.seda.j2ee5.maf.components.servicelocator.ServiceLocator;
//fine LP PG200070 - 20200813
import com.seda.payer.ottico.facade.bean.ConfigurazioneFacadeBean;
import com.seda.payer.ottico.webservice.dto.BaseSearchKeys;
import com.seda.payer.ottico.webservice.dto.CollectionDto;
import com.seda.payer.ottico.webservice.dto.ConfigurazioneDto;
import com.seda.payer.ottico.webservice.dto.ConfigurazioneIdentify;
import com.seda.payer.ottico.webservice.dto.DirectoryInfo;
import com.seda.payer.ottico.webservice.dto.InformazioniAggiornamento;
import com.seda.payer.ottico.webservice.dto.TemplateDocumentoDto;
import com.seda.payer.ottico.webservice.dto.TipoDocumento;
import com.seda.payer.ottico.webservice.dto.WebServiceInfo;
import com.seda.payer.ottico.webservice.dto.WebServiceInfo.WebServiceInfoType;
import com.seda.payer.ottico.webservice.configurazione.dati.CreaAssociaTemplateResponse;
import com.seda.payer.ottico.webservice.configurazione.dati.CreaParametriOtticoResponse;
import com.seda.payer.ottico.webservice.configurazione.dati.DettaglioAssociaTemplateRequest;
import com.seda.payer.ottico.webservice.configurazione.dati.DettaglioAssociaTemplateResponse;
import com.seda.payer.ottico.webservice.configurazione.dati.DettaglioParametriOtticoRequest;
import com.seda.payer.ottico.webservice.configurazione.dati.DettaglioParametriOtticoResponse;
import com.seda.payer.ottico.webservice.configurazione.dati.EliminaAssociaTemplateResponse;
import com.seda.payer.ottico.webservice.configurazione.dati.EliminaParametriOtticoResponse;
import com.seda.payer.ottico.webservice.configurazione.dati.ListResponseType;
import com.seda.payer.ottico.webservice.configurazione.dati.ListResponseTypePageInfo;
import com.seda.payer.ottico.webservice.configurazione.dati.ListaAssociaTemplateResponse;
import com.seda.payer.ottico.webservice.configurazione.dati.ListaParametriOtticoResponse;
import com.seda.payer.ottico.webservice.configurazione.dati.ModificaAssociaTemplateResponse;
import com.seda.payer.ottico.webservice.configurazione.dati.ModificaParametriOtticoResponse;
import com.seda.payer.ottico.webservice.configurazione.dati.ResponseType;
import com.seda.payer.ottico.webservice.configurazione.dati.ResponseTypeRetCode;
import com.seda.payer.ottico.webservice.configurazione.dati.VerificaAssociaTemplateRequest;
import com.seda.payer.ottico.webservice.configurazione.dati.VerificaAssociaTemplateResponse;
import com.seda.payer.ottico.webservice.handler.WebServiceHandler;
import com.seda.payer.ottico.webservice.srv.FaultType;

public class Configurazione extends WebServiceHandler implements com.seda.payer.ottico.webservice.configurazione.source.ConfigurazioneInterface{
	//inizio LP PG200070 - 20200813
	private ConfigurazioneFacadeBean beanFacade = null;

	private ConfigurazioneFacadeBean getFacadeService() throws Exception
	{
		if(beanFacade == null) {
			beanFacade = new ConfigurazioneFacadeBean(configuration, log4jConfiguration);
		}
		return beanFacade;
	}
	//fine LP PG200070 - 20200813
	/* <code></code>
	 * @see com.seda.payer.ottico.webservice.configurazione.source.ConfigurazioneInterface#dettaglioAssociaTemplate(com.seda.payer.ottico.webservice.configurazione.dati.DettaglioAssociaTemplateRequest)
	 */
	public DettaglioAssociaTemplateResponse dettaglioAssociaTemplate(
			DettaglioAssociaTemplateRequest in) throws RemoteException,
			com.seda.payer.ottico.webservice.configurazione.srv.FaultType {
		try {
			//we instantiate facade reference
			//inizio LP PG200070 - 20200813
			//ConfigurazioneFacadeBean bean = new ConfigurazioneFacadeBean();
			ConfigurazioneFacadeBean bean = getFacadeService();
			//fine LP PG200070 - 20200813
			//we invoke facade method
			TemplateDocumentoDto dtoObj = bean.dettaglioAssociaTemplate(in.getChiaveTemplate(), dbSchemaCodSocieta); 
			info("Il dettaglio Template è: " + dtoObj);
			if(dtoObj==null)
				throw new Exception("Non esistono informazioni di dettaglio.");
			//we build date object
			Calendar dataInizio = Calendar.getInstance(); {
				dataInizio.setTime(dtoObj.getDataInizio());
			}
			Calendar dataFine = Calendar.getInstance(); {
				dataFine.setTime(dtoObj.getDataFine());
			}
			return new DettaglioAssociaTemplateResponse(
					dtoObj.getSrcKey().getCodiceSocieta(),
					dtoObj.getSrcKey().getCodiceUtente(),
					dtoObj.getSrcKey().getCodiceEnte(), 
					dtoObj.getTipoBollettino(),
					dataInizio,
					dataFine,
					dtoObj.getTipologiaServizio(),
					dtoObj.getTipoDocumento(), 
					dtoObj.getRiferimentoTemplate(),
					new ResponseType(ResponseTypeRetCode.value1, "Request Execute Succeful"));
		} catch (Exception e) {
			error("dettaglioAssociaTemplate failed, generic error due to: ", e);
			throw new FaultType(505, e.getMessage());
		}
	}
	/* <code></code>
	 * @see com.seda.payer.ottico.webservice.configurazione.source.ConfigurazioneInterface#dettaglioParametriOttico(com.seda.payer.ottico.webservice.configurazione.dati.DettaglioParametriOtticoRequest)
	 */
	public DettaglioParametriOtticoResponse dettaglioParametriOttico(
			DettaglioParametriOtticoRequest in) throws RemoteException,
			com.seda.payer.ottico.webservice.configurazione.srv.FaultType {
		try {
			//we instantiate facade reference
			//inizio LP PG200070 - 20200813
			//ConfigurazioneFacadeBean bean = new ConfigurazioneFacadeBean();
			ConfigurazioneFacadeBean bean = getFacadeService();
			//fine LP PG200070 - 20200813
			//we invoke facade method
			
			ConfigurazioneDto dtoObj = bean.dettaglioParametriOttico(in.getCodiceSocieta(),
					in.getCodiceUtente(), in.getCodiceEnte(), dbSchemaCodSocieta);
			//System.out.println("tornato da EJB");
			//debug("Il dettaglioOttico è: " + dtoObj);
			
			if (dtoObj==null)
				throw new Exception("Non esistono informazioni di dettaglio.");

			DettaglioParametriOtticoResponse response = new DettaglioParametriOtticoResponse(
					dtoObj.getIdentify().getCodiceSocieta(), dtoObj.getIdentify().getCodiceUtente(), dtoObj.getIdentify().getCodiceEnte(),
					dtoObj.getDocType().getFlagDocumento(), dtoObj.getDocType().getFlagRelata(), dtoObj.getDocType().getFlagBollettino(), 
					dtoObj.getDocType().getFlagQuietanza(), 
					dtoObj.getWsInfo().getFlagWebserviceOttico(), dtoObj.getWsInfo().getIndirizzoWebService(),
					dtoObj.getWsInfo().getUserWebService(), dtoObj.getWsInfo().getPassWebService(), dtoObj.getWsInfo().getEmailAmministratore(), 
					dtoObj.getDirInfo().getDirectoryFlussiDatiOtticoInput(), dtoObj.getDirInfo().getDirectorySalvataggoFlussiDatiOtticoInput(),
					dtoObj.getDirInfo().getDirectoryFlussiImmaginiOtticoInput(),dtoObj.getDirInfo().getDirectorySalvataggioFlussiImmaginiOtticoInput(),
					dtoObj.getDirInfo().getDirectoryImmaginiOtticoPerEstrattoConto(), dtoObj.getDirInfo().getDirectoryLogElaborazione(), 
					new ResponseType(ResponseTypeRetCode.value1, "Request Execute Succeful"));
			//System.out.println("tornato da response");
			return response;
		} catch (Exception e) {
			error("dettaglioParametriOttico failed, generic error due to: ", e);
			throw new FaultType(505, e.getMessage());
		}
	}
	/* <code></code>
	 * @see com.seda.payer.ottico.webservice.configurazione.source.ConfigurazioneInterface#verificaAssociaTemplate(com.seda.payer.ottico.webservice.configurazione.dati.VerificaAssociaTemplateRequest)
	 */
	public VerificaAssociaTemplateResponse verificaAssociaTemplate(VerificaAssociaTemplateRequest in) throws RemoteException, com.seda.payer.ottico.webservice.configurazione.srv.FaultType {
		try {
			//inizio LP PG200070 - 20200813
			//ConfigurazioneFacadeBean bean = new ConfigurazioneFacadeBean();
			ConfigurazioneFacadeBean bean = getFacadeService();
			//fine LP PG200070 - 20200813
			boolean result = bean.verificaAssociaTemplate(
					in.getTipoDocumento(), in.getCodiceSocieta(), in.getCodiceUtente(), in.getCodiceEnte(), 
					   in.getTipologiaServizio(), new Timestamp(in.getDataInizio().getTime().getTime()), 
					   new Timestamp(in.getDataFine().getTime().getTime()), in.getChiaveTemplate(), dbSchemaCodSocieta); 
			info("webservices.verificaAssociaTemplate - " + (result ? "trovato" : "non trovato"));
			return new VerificaAssociaTemplateResponse(result, 
					result ? new ResponseType(ResponseTypeRetCode.value1, "Success") : new ResponseType(ResponseTypeRetCode.value2, "Failure"));
		} catch (Exception e) {
			error("verificaAssociaTemplate failed, generic error due to: ", e);
			throw new FaultType(505, e.getMessage());
		}
	}
	/* <code></code>
	 * @see com.seda.payer.ottico.webservice.configurazione.source.ConfigurazioneInterface#creaAssociaTemplate(com.seda.payer.ottico.webservice.configurazione.dati.CreaAssociaTemplateRequest)
	 */
	public com.seda.payer.ottico.webservice.configurazione.dati.CreaAssociaTemplateResponse creaAssociaTemplate(com.seda.payer.ottico.webservice.configurazione.dati.CreaAssociaTemplateRequest in) throws java.rmi.RemoteException, com.seda.payer.ottico.webservice.configurazione.srv.FaultType {
		try {
			//we instantiate facade reference
			//inizio LP PG200070 - 20200813
			//ConfigurazioneFacadeBean bean = new ConfigurazioneFacadeBean();
			ConfigurazioneFacadeBean bean = getFacadeService();
			//fine LP PG200070 - 20200813
			//we invoke facade method
			bean.creaAssociaTemplate(new TemplateDocumentoDto()
				.setSrcKey(new BaseSearchKeys(in.getCodiceSocieta(), in.getCodiceUtente(), in.getCodiceEnte()))
				.setTipoBollettino(in.getTipoBollettino())
				.setDataInizio(new Timestamp(in.getDataInizio().getTimeInMillis()))
				.setDataFine(new Timestamp(in.getDataFine().getTimeInMillis()))
				.setTipologiaServizio(in.getTipologiaServizio())
				.setTipoDocumento(in.getTipoDocumento())
				.setRiferimentoTemplate(in.getRiferimentoTemplate())
				.setInfoUpdate(new InformazioniAggiornamento(new Timestamp(in.getDataUltimoAgg().getTimeInMillis()), in.getOperatore())), dbSchemaCodSocieta);

			return new CreaAssociaTemplateResponse(new ResponseType(ResponseTypeRetCode.value1, "Request Execute Succeful"));

		} catch (Exception e) {
			error("creaAssociaTemplate failed, generic error due to: ", e);
			throw new FaultType(505, e.getMessage());
		}
	}
	/* <code></code>
	 * @see com.seda.payer.ottico.webservice.configurazione.source.ConfigurazioneInterface#creaParametriOttico(com.seda.payer.ottico.webservice.configurazione.dati.CreaParametriOtticoRequest)
	 */
	public com.seda.payer.ottico.webservice.configurazione.dati.CreaParametriOtticoResponse creaParametriOttico(com.seda.payer.ottico.webservice.configurazione.dati.CreaParametriOtticoRequest in) throws java.rmi.RemoteException, com.seda.payer.ottico.webservice.configurazione.srv.FaultType {
		try {
			//we instantiate facade reference

			info("creaParametriOttico documento - " + in.getFlagDocumento());
			info("creaParametriOttico relata - " + in.getFlagRelata());
			info("creaParametriOttico bollettino - " + in.getFlagBollettino());
			info("creaParametriOttico attestato di pagamento - " + in.getFlagQuietanza());
			
			//inizio LP PG200070 - 20200813
			//ConfigurazioneFacadeBean bean = new ConfigurazioneFacadeBean();
			ConfigurazioneFacadeBean bean = getFacadeService();
			//fine LP PG200070 - 20200813
			//we invoke facade method
			bean.creaParametriOttico(new ConfigurazioneDto(new ConfigurazioneIdentify( in.getCodiceSocieta(),
						in.getCodiceUtente(),
						in.getCodiceEnte()))
				.setDocType(new TipoDocumento(in.getFlagBollettino(), in.getFlagDocumento(),
						in.getFlagQuietanza(), in.getFlagRelata()))
				.setWsInfo(new WebServiceInfo(
						in.getIndirizzoWebServiceOttico(),
						in.getUserWebServiceOttico(),
						in.getPasswordWebServiceOttico(), 
						in.getEmailAmministratoreOttico(),
						new WebServiceInfoType(in.getFlagWebServiceOttico())))
				.setDirInfo(new DirectoryInfo(
						in.getDirectoryFlussiDatiOttico(),
						in.getDirectoryFlussiDatiOtticoOld(),
						in.getDirectoryFlussiImmaginiOttico(),
						in.getDirectoryFlussiImmaginiOtticoOld(),
						in.getDirectoryImmaginiOttico(),
						in.getDirectoryLogOtticoOld()))
				.setInfoUpdate(new InformazioniAggiornamento(new Timestamp(in.getDataUltimoAgg().getTimeInMillis()),
						in.getOperatore())), dbSchemaCodSocieta);

				return new CreaParametriOtticoResponse(new ResponseType(ResponseTypeRetCode.value1, "Request Execute Succeful"));

		} catch (Exception e) {
			error("creaParametriOttico failed, generic error due to: ", e);
			throw new FaultType(505, e.getMessage());
		}
	}
	/* <code></code>
	 * @see com.seda.payer.ottico.webservice.configurazione.source.ConfigurazioneInterface#eliminaAssociaTemplate(com.seda.payer.ottico.webservice.configurazione.dati.EliminaAssociaTemplateRequest)
	 */
	public com.seda.payer.ottico.webservice.configurazione.dati.EliminaAssociaTemplateResponse eliminaAssociaTemplate(com.seda.payer.ottico.webservice.configurazione.dati.EliminaAssociaTemplateRequest in) throws java.rmi.RemoteException, com.seda.payer.ottico.webservice.configurazione.srv.FaultType {
		try {
			//we instantiate facade reference
			//inizio LP PG200070 - 20200813
			//ConfigurazioneFacadeBean bean = new ConfigurazioneFacadeBean();
			ConfigurazioneFacadeBean bean = getFacadeService();
			//fine LP PG200070 - 20200813
			//we invoke facade method
			bean.eliminaAssociaTemplate(in.getChiaveTemplate(), dbSchemaCodSocieta);
			
			return new EliminaAssociaTemplateResponse(new ResponseType(ResponseTypeRetCode.value1, "Request Execute Succeful"));
		
		} catch (Exception e) {
			error("eliminaAssociaTemplate failed, generic error due to: ", e);
			throw new FaultType(505, e.getMessage());
		}
	}
	/* <code></code>
	 * @see com.seda.payer.ottico.webservice.configurazione.source.ConfigurazioneInterface#eliminaParametriOttico(com.seda.payer.ottico.webservice.configurazione.dati.EliminaParametriOtticoRequest)
	 */
	public com.seda.payer.ottico.webservice.configurazione.dati.EliminaParametriOtticoResponse eliminaParametriOttico(com.seda.payer.ottico.webservice.configurazione.dati.EliminaParametriOtticoRequest in) throws java.rmi.RemoteException, com.seda.payer.ottico.webservice.configurazione.srv.FaultType {
		try {
			//we instantiate facade reference
			//inizio LP PG200070 - 20200813
			//ConfigurazioneFacadeBean bean = new ConfigurazioneFacadeBean();
			ConfigurazioneFacadeBean bean = getFacadeService();
			//fine LP PG200070 - 20200813
			//we invoke facade method
			bean.eliminaParametriOttico(new ConfigurazioneIdentify(in.getCodiceSocieta(), in.getCodiceUtente(), in.getCodiceEnte()), dbSchemaCodSocieta);
			
			return new EliminaParametriOtticoResponse(new ResponseType(ResponseTypeRetCode.value1, "Request Execute Succeful"));

		} catch (Exception e) {
			error("eliminaParametriOttico failed, generic error due to: ", e);
			throw new FaultType(505, e.getMessage());
		}
	}
	/* <code></code>
	 * @see com.seda.payer.ottico.webservice.configurazione.source.ConfigurazioneInterface#listaAssociaTemplate(com.seda.payer.ottico.webservice.configurazione.dati.ListaAssociaTemplateRequest)
	 */
	public com.seda.payer.ottico.webservice.configurazione.dati.ListaAssociaTemplateResponse listaAssociaTemplate(com.seda.payer.ottico.webservice.configurazione.dati.ListaAssociaTemplateRequest in) throws java.rmi.RemoteException, com.seda.payer.ottico.webservice.configurazione.srv.FaultType {
		try {
			//we instantiate facade reference
			//inizio LP PG200070 - 20200813
			//ConfigurazioneFacadeBean bean = new ConfigurazioneFacadeBean();
			ConfigurazioneFacadeBean bean = getFacadeService();
			//fine LP PG200070 - 20200813
			//we define dto object
			TemplateDocumentoDto tdDto = new TemplateDocumentoDto()
				.setSrcKey(new BaseSearchKeys(in.getCodiceSocieta(), in.getCodiceUtente(), in.getCodiceEnte()))
				.setSiglaProvincia(in.getSiglaProvincia())
				.setDataInizio(in.getDataInizio() == null ? null : new Timestamp(in.getDataInizio().getTimeInMillis()))
				.setDataFine(in.getDataFine() == null ? null : new Timestamp(in.getDataFine().getTimeInMillis()))
				.setTipologiaServizio(in.getTipologiaServizio())
				.setTipoDocumento(in.getTipoDocumento());
			//we invoke facade method
			CollectionDto result =	bean.listaAssociaTemplate(tdDto, in.getRowsPerPage(),in.getPageNumber(), dbSchemaCodSocieta);
			ListaAssociaTemplateResponse response = new ListaAssociaTemplateResponse();
			ListResponseTypePageInfo pageInfo = new ListResponseTypePageInfo();
			PageInfo info = result.getPageInfo(); {
	    		pageInfo.setFirstRow(info.getFirstRow());
	    		pageInfo.setLastRow(info.getLastRow());
	    		pageInfo.setNumPages(info.getNumPages());
	    		pageInfo.setNumRows(info.getNumRows());
	     		pageInfo.setPageNumber(info.getPageNumber());
			}
     		ListResponseType lport = new ListResponseType(result.getListXml(), pageInfo);
     		//we prepare response
     		response.setListaTemplate(lport);
     		response.setResponse(new ResponseType(ResponseTypeRetCode.value1, "Request Execute Succeful"));
     		debug("Configurazione_WS: \n\tMetodo: listaAssociaTemplate \n\tStai ritornando un oggeeto di tipo response");
     		
     		return response;

		} catch (Exception e) {
			debug("Configurazione_WS: \n\tMetodo: listaAssociaTemplate \n\tStai ritornando NULL!!!");
			error("listaAssociaTemplate failed, generic error due to: ", e);
			throw new FaultType(505, e.getMessage());
		}
	}
	/* <code></code>
	 * @see com.seda.payer.ottico.webservice.configurazione.source.ConfigurazioneInterface#listaParametriOttico(com.seda.payer.ottico.webservice.configurazione.dati.ListaParametriOtticoRequest)
	 */
	public com.seda.payer.ottico.webservice.configurazione.dati.ListaParametriOtticoResponse listaParametriOttico(com.seda.payer.ottico.webservice.configurazione.dati.ListaParametriOtticoRequest in) throws java.rmi.RemoteException, com.seda.payer.ottico.webservice.configurazione.srv.FaultType {
		try {
			//we instantiate facade reference
			//inizio LP PG200070 - 20200813
			//ConfigurazioneFacadeBean bean = new ConfigurazioneFacadeBean();
			ConfigurazioneFacadeBean bean = getFacadeService();
			//fine LP PG200070 - 20200813
			//we define dto object
			ConfigurazioneDto cfgDto = new ConfigurazioneDto(new ConfigurazioneIdentify( in.getCodiceSocieta(),
					in.getCodiceUtente(),
					in.getCodiceEnte()))
				.setSiglaProvincia(in.getSiglaProvincia())
				.setWsInfo(new WebServiceInfo(
					null,
					null,
					null, 
					null,
					new WebServiceInfoType(in.getFlagWebServiceOttico())));
			//we invoke facade method
			CollectionDto result =	bean.listaParametriOttico(cfgDto, in.getRowsPerPage(),in.getPageNumber(), dbSchemaCodSocieta);
			ListaParametriOtticoResponse response = new ListaParametriOtticoResponse();
			ListResponseTypePageInfo pageInfo = new ListResponseTypePageInfo();
			PageInfo info = result.getPageInfo(); {
	    		pageInfo.setFirstRow(info.getFirstRow());
	    		pageInfo.setLastRow(info.getLastRow());
	    		pageInfo.setNumPages(info.getNumPages());
	    		pageInfo.setNumRows(info.getNumRows());
	     		pageInfo.setPageNumber(info.getPageNumber());
			}
     		ListResponseType lport = new ListResponseType(result.getListXml(), pageInfo);
     		//we prepare response
     		response.setListaOttico(lport);
     		response.setResponse(new ResponseType(ResponseTypeRetCode.value1, "Request Execute Succeful"));
     		debug("Configurazione_WS: \n\tMetodo: listaParametriOttico \n\tStai ritornando un oggeeto di tipo response");
     		
     		return response;

		} catch (Exception e) {
			debug("Configurazione_WS: \n\tMetodo: listaParametriOttico \n\tStai ritornando NULL!!!");
			error("listaParametriOttico failed, generic error due to: ", e);
			throw new FaultType(505, e.getMessage());
		}		
	}
	/* <code></code>
	 * @see com.seda.payer.ottico.webservice.configurazione.source.ConfigurazioneInterface#modificaAssociaTemplate(com.seda.payer.ottico.webservice.configurazione.dati.ModificaAssociaTemplateRequest)
	 */
	public com.seda.payer.ottico.webservice.configurazione.dati.ModificaAssociaTemplateResponse modificaAssociaTemplate(com.seda.payer.ottico.webservice.configurazione.dati.ModificaAssociaTemplateRequest in) throws java.rmi.RemoteException, com.seda.payer.ottico.webservice.configurazione.srv.FaultType {
		try {
			//we instantiate facade reference
			//inizio LP PG200070 - 20200813
			//ConfigurazioneFacadeBean bean = new ConfigurazioneFacadeBean();
			ConfigurazioneFacadeBean bean = getFacadeService();
			//fine LP PG200070 - 20200813
			//we invoke facade method
			bean.modificaAssociaTemplate(new TemplateDocumentoDto(in.getChiaveTemplate())
				.setSrcKey(new BaseSearchKeys(in.getCodiceSocieta(), in.getCodiceUtente(), in.getCodiceEnte()))
				.setTipoBollettino(in.getTipoBollettino())
				.setDataInizio(new Timestamp(in.getDataInizio().getTimeInMillis()))
				.setDataFine(new Timestamp(in.getDataFine().getTimeInMillis()))
				.setTipologiaServizio(in.getTipologiaServizio())
				.setTipoDocumento(in.getTipoDocumento())
				.setRiferimentoTemplate(in.getRiferimentoTemplate())
				.setInfoUpdate(new InformazioniAggiornamento(new Timestamp(in.getDataUltimoAgg().getTimeInMillis()), in.getOperatore())), dbSchemaCodSocieta);
			
				return new ModificaAssociaTemplateResponse(new ResponseType(ResponseTypeRetCode.value1, "Request Execute Succeful"));
			
		} catch (Exception e) {
			error("modificaAssociaTemplate failed, generic error due to: ", e);
			throw new FaultType(505, e.getMessage());
		}
	}
	/* 
	 * <code>[TD]</code> TipoDocumento td = new TipoDocumento(flagBollettino, flagDocumento, flagQuietanza, flagRelata, flagWebServiceOttico);
	 * <code>[CI]</code> ConfigurazioneIdentify ci = new ConfigurazioneIdentify(codiceSocieta, codiceUtente, codiceEnte);
	 * <code>[WS]</code> WebServiceInfo info = new WebServiceInfo(indirizzoWebService, userWebService, passWebService, emailAmministratore, flagWebserviceOttico);
	 * <code>[DI]</code> DirectoryInfo di = new DirectoryInfo(directoryFlussiDatiOtticoInput, directorySalvataggoFlussiDatiOtticoInput, directoryFlussiImmaginiOtticoInput, directorySalvataggioFlussiImmaginiOtticoInput, directoryLogElaborazione, directoryImmaginiOtticoPerEstrattoConto);
	 * <br />
	 * @see com.seda.payer.ottico.webservice.configurazione.source.ConfigurazioneInterface#modificaParametriOttico(com.seda.payer.ottico.webservice.configurazione.dati.ModificaParametriOtticoRequest)
	 */
	public com.seda.payer.ottico.webservice.configurazione.dati.ModificaParametriOtticoResponse modificaParametriOttico(com.seda.payer.ottico.webservice.configurazione.dati.ModificaParametriOtticoRequest in) throws java.rmi.RemoteException, com.seda.payer.ottico.webservice.configurazione.srv.FaultType {
		try {
			//we instantiate facade reference

			info("creaParametriOttico documento - " + in.getFlagDocumento());
			info("creaParametriOttico relata - " + in.getFlagRelata());
			info("creaParametriOttico bollettino - " + in.getFlagBollettino());
			info("creaParametriOttico attestato di pagamento - " + in.getFlagQuietanza());
			//inizio LP PG200070 - 20200813
			//ConfigurazioneFacadeBean bean = new ConfigurazioneFacadeBean();
			ConfigurazioneFacadeBean bean = getFacadeService();
			//fine LP PG200070 - 20200813
			
			//we invoke facade method
			bean.modificaParametriOttico(new ConfigurazioneDto(new ConfigurazioneIdentify( in.getCodiceSocieta(),
						in.getCodiceUtente(),
						in.getCodiceEnte()))
				.setDocType(new TipoDocumento(in.getFlagBollettino(),
						in.getFlagDocumento(), 
						in.getFlagQuietanza(),
						in.getFlagRelata()))
				.setWsInfo(new WebServiceInfo(
						in.getIndirizzoWebServiceOttico(),
						in.getUserWebServiceOttico(),
						in.getPasswordWebServiceOttico(), 
						in.getEmailAmministratoreOttico(),
						new WebServiceInfoType(in.getFlagWebServiceOttico())))
				.setDirInfo(new DirectoryInfo(
						in.getDirectoryFlussiDatiOttico(),
						in.getDirectoryFlussiDatiOtticoOld(),
						in.getDirectoryFlussiImmaginiOttico(),
						in.getDirectoryFlussiImmaginiOtticoOld(),
						in.getDirectoryLogOtticoOld(),
						in.getDirectoryImmaginiOttico()))
				.setInfoUpdate(new InformazioniAggiornamento(new Timestamp(in.getDataUltimoAgg().getTimeInMillis()),in.getOperatore())), dbSchemaCodSocieta);

				return new ModificaParametriOtticoResponse(new ResponseType(ResponseTypeRetCode.value1, "Request Execute Succeful"));

		} catch (Exception e) {
			error("modificaParametriOttico failed, generic error due to: ", e);
			throw new FaultType(505, e.getMessage());
		}
	}
}