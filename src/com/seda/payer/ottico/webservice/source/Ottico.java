package com.seda.payer.ottico.webservice.source;

import java.rmi.RemoteException;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.sql.Types;
import java.util.ArrayList;

import javax.sql.rowset.RowSetMetaDataImpl;

import com.seda.commons.string.Convert;
import com.seda.payer.commons.utility.StringUtility;
import com.seda.payer.ottico.facade.bean.OtticoFacadeBean;
import com.seda.payer.ottico.webservice.dto.BaseSearchKeys;
import com.seda.payer.ottico.webservice.dto.ConfigurazioneDto;
import com.seda.payer.ottico.webservice.dto.ConfigurazioneIdentify;
import com.seda.payer.ottico.webservice.dto.ElaboraFlussiImgState;
import com.seda.payer.ottico.webservice.dto.ImmagineDto;
import com.seda.payer.ottico.webservice.dto.InformazioniStampa;
import com.seda.payer.ottico.webservice.dto.WebServiceInfo;
import com.seda.payer.ottico.webservice.dto.WebServiceInfo.WebServiceInfoType;
import com.seda.payer.ottico.webservice.dati.ElaboraFlussiImgRequest;
import com.seda.payer.ottico.webservice.dati.ElaboraFlussiImgResponse;
import com.seda.payer.ottico.webservice.dati.ElaboraFlussiImgTypeKeys;
import com.seda.payer.ottico.webservice.dati.GetListaImmaginiResponse;
import com.seda.payer.ottico.webservice.dati.LogElaborazioneFlussiImgRequest;
import com.seda.payer.ottico.webservice.dati.LogElaborazioneFlussiImgResponse;
import com.seda.payer.ottico.webservice.dati.PresenzaFlussoTypeKeys;
import com.seda.payer.ottico.webservice.dati.RecuperaTemplateRequest;
import com.seda.payer.ottico.webservice.dati.RecuperaTemplateResponse;
import com.seda.payer.ottico.webservice.dati.ResponseType;
import com.seda.payer.ottico.webservice.dati.ResponseTypeRetCode;
import com.seda.payer.ottico.webservice.dati.StampaBollettinoRequest;
import com.seda.payer.ottico.webservice.dati.StampaBollettinoResponse;
import com.seda.payer.ottico.webservice.dati.StampaDocumentoRequest;
import com.seda.payer.ottico.webservice.dati.StampaDocumentoResponse;
import com.seda.payer.ottico.webservice.dati.StampaImmagineContenziosoRequest;
import com.seda.payer.ottico.webservice.dati.StampaImmagineContenziosoResponse;
import com.seda.payer.ottico.webservice.dati.StampaImmagineRequest;
import com.seda.payer.ottico.webservice.dati.StampaImmagineResponse;
import com.seda.payer.ottico.webservice.dati.StampaQuietanzaRequest;
import com.seda.payer.ottico.webservice.dati.StampaQuietanzaResponse;
import com.seda.payer.ottico.webservice.dati.StampaRelataRequest;
import com.seda.payer.ottico.webservice.dati.StampaRelataResponse;
import com.seda.payer.ottico.webservice.dati.VerificaConfigurazioneOtticoRequest;
import com.seda.payer.ottico.webservice.dati.VerificaConfigurazioneOtticoResponse;
import com.seda.payer.ottico.webservice.dati.VerificaFlussoRequest;
import com.seda.payer.ottico.webservice.dati.VerificaFlussoResponse;
import com.seda.payer.ottico.webservice.handler.WebServiceHandler;
import com.seda.payer.ottico.webservice.srv.FaultType;
import com.sun.rowset.WebRowSetImpl;

public class Ottico extends WebServiceHandler implements com.seda.payer.ottico.webservice.source.OtticoInterface {
	//inizio LP PG200070 - 20200813
	private OtticoFacadeBean otticoFacadeBean = null;

	private OtticoFacadeBean getFacadeService() throws Exception
	{
		if(otticoFacadeBean == null) {
			otticoFacadeBean = new OtticoFacadeBean(configuration, log4jConfiguration);
		}
		return otticoFacadeBean;
	}
	//fine LP PG200070 - 20200813
	/* (non-Javadoc)
	 * @see com.seda.payer.ottico.webservice.source.OtticoInterface#elaboraFlussiImg(com.seda.payer.ottico.webservice.dati.ElaboraFlussiImgRequest)
	 */
	public com.seda.payer.ottico.webservice.dati.ElaboraFlussiImgResponse elaboraFlussiImg(ElaboraFlussiImgRequest in) throws java.rmi.RemoteException, com.seda.payer.ottico.webservice.srv.FaultType {
		try{
			//we instantiate facade reference
			//inizio LP PG200070 - 20200813
			//OtticoFacadeBean bean = new OtticoFacadeBean();
			OtticoFacadeBean bean = getFacadeService();
			//fine LP PG200070 - 20200813
			// we invoke facade method
			ElaboraFlussiImgState state = bean.elaboraFlussiImg(dbSchemaCodSocieta);
			return new ElaboraFlussiImgResponse(
					state.format().compareTo(ElaboraFlussiImgState.Success.format()) == 0
						|| state.format().compareTo(ElaboraFlussiImgState.NotFound.format()) == 0 ? 
				new ResponseType(ResponseTypeRetCode.value1, ElaboraFlussiImgTypeKeys.parse(state.format())) : 
				new ResponseType(ResponseTypeRetCode.value2, ElaboraFlussiImgTypeKeys.parse(state.format())));

		} catch(Exception e) {
			error("elaboraFlussiImg failed, generic error due to: ", e);
			return new ElaboraFlussiImgResponse(new ResponseType(ResponseTypeRetCode.value2, "Generic Err"));
		}
    }

	/* (non-Javadoc)
	 * @see com.seda.payer.ottico.webservice.source.OtticoInterface#verificaConfigurazioneFlusso(com.seda.payer.ottico.webservice.dati.VerificaConfigurazioneFlussoRequest)
	 */
	public VerificaConfigurazioneOtticoResponse verificaConfigurazioneOttico(
			VerificaConfigurazioneOtticoRequest in) throws RemoteException,
			FaultType {
		try {
			//we instantiate facade reference
			//inizio LP PG200070 - 20200813
			//OtticoFacadeBean bean = new OtticoFacadeBean();
			OtticoFacadeBean bean = getFacadeService();
			//fine LP PG200070 - 20200813
			//we invoke facade method
			ConfigurazioneIdentify srcKey = new ConfigurazioneIdentify(in.getChiaveSocieta(), in.getChiaveUtente(), in.getCodiceEnte());
			ConfigurazioneDto dtoObj = bean.verificaConfigurazioneOttico(srcKey, dbSchemaCodSocieta);

			if (dtoObj != null)
			{
	
	//			VerificaConfigurazioneOtticoResponse resp = new VerificaConfigurazioneOtticoResponse(
	//					flagVisualizzaDocumento, flagVisualizzaRelata,
	//					flagVisualizzaBollettino, flagVisualizzaQuietanza,
	//					flagWebServiceOttico, indirizzoServerOttico, 
	//					userServerOttico, passwordServerOttico, response);
				
				return new VerificaConfigurazioneOtticoResponse(
						dtoObj.getDocType().getFlagDocumento(),
						dtoObj.getDocType().getFlagRelata(),
						dtoObj.getDocType().getFlagBollettino(),
						dtoObj.getDocType().getFlagQuietanza(),
						dtoObj.getWsInfo().getFlagWebserviceOttico(),
						dtoObj.getWsInfo().getIndirizzoWebService(),
						dtoObj.getWsInfo().getUserWebService(),
						dtoObj.getWsInfo().getPassWebService(),
						new ResponseType(ResponseTypeRetCode.value1, "Success"));
			}
			else
			{
				info("VerificaConfigurazioneOttico - Configurazione non trovata per l'ente selezionato");
				
				return new VerificaConfigurazioneOtticoResponse(
						null,null,null,null,null,null,null,null,
						new ResponseType(ResponseTypeRetCode.value3, "Configurazione non trovata"));
			}
		} catch(Exception e) {
			error("verificaConfigurazioneOttico failed, generic error due to: ", e);
			return new VerificaConfigurazioneOtticoResponse(
					null,null,null,null,null,null,null,null,
					new ResponseType(ResponseTypeRetCode.value2, "Generic Err"));
		}
	}
	
	/* (non-Javadoc)
	 * @see com.seda.payer.ottico.webservice.source.OtticoInterface#verificaFlusso(com.seda.payer.ottico.webservice.dati.VerificaFlussoRequest)
	 */
	public VerificaFlussoResponse verificaFlusso(VerificaFlussoRequest in) throws RemoteException, FaultType {
		try {
			//we istantiate facade reference
			//inizio LP PG200070 - 20200813
			//OtticoFacadeBean bean = new OtticoFacadeBean();
			OtticoFacadeBean bean = getFacadeService();
			//fine LP PG200070 - 20200813
			//we invoke facade method
			ConfigurazioneIdentify srcKey = new ConfigurazioneIdentify(in.getChiaveSocieta(), in.getChiaveUtente(), in.getChiaveEnte());
			Boolean result = bean.verificaFlusso(srcKey, in.getNomeFileDati(), dbSchemaCodSocieta);
			return result.booleanValue() ? new VerificaFlussoResponse(PresenzaFlussoTypeKeys.parse(String.valueOf(result.booleanValue())),
																	  new ResponseType(ResponseTypeRetCode.value1, "Success"))
										 : new VerificaFlussoResponse(PresenzaFlussoTypeKeys.parse(String.valueOf(false)),
												  new ResponseType(ResponseTypeRetCode.value1, "Failed"));

		} catch(Exception e) {
			error("verificaFlusso failed, generic error due to: ", e);
			return new VerificaFlussoResponse(
					PresenzaFlussoTypeKeys.parse(String.valueOf(false)),
					new ResponseType(ResponseTypeRetCode.value2, "Generic Err"));
		}
	}

	/* (non-Javadoc)
	 * @see com.seda.payer.ottico.webservice.source.OtticoInterface#logElaborazioneFlussiImg(com.seda.payer.ottico.webservice.dati.LogElaborazioneFlussiImgRequest)
	 */
	public LogElaborazioneFlussiImgResponse logElaborazioneFlussiImg(LogElaborazioneFlussiImgRequest in) throws RemoteException, FaultType {
		try {
			//we instantiate facade reference
			//inizio LP PG200070 - 20200813
			//OtticoFacadeBean bean = new OtticoFacadeBean();
			OtticoFacadeBean bean = getFacadeService();
			//fine LP PG200070 - 20200813
			bean.logElaborazioneFlussiImg(in.getTestoFileLog(), in.getDirectoryLog(), in.getNomeFileLog(), dbSchemaCodSocieta);
			return new LogElaborazioneFlussiImgResponse(new ResponseType(ResponseTypeRetCode.value1, "Success"));

		} catch (Exception e) {
			error("logElaborazioneFlussiImg failed, generic error due to: ", e);
			return new LogElaborazioneFlussiImgResponse(new ResponseType(ResponseTypeRetCode.value2, "Generic Err"));
		}		
	}

	/* (non-Javadoc)
	 * @see com.seda.payer.ottico.webservice.source.OtticoInterface#recuperaTemplate(com.seda.payer.ottico.webservice.dati.RecuperaTemplateRequest)
	 */
	public RecuperaTemplateResponse recuperaTemplate(RecuperaTemplateRequest in) throws RemoteException, FaultType {
		try {
			//inizio LP PG200070 - 20200813
			//OtticoFacadeBean bean = new OtticoFacadeBean();
			OtticoFacadeBean bean = getFacadeService();
			//fine LP PG200070 - 20200813
//			debug("TEST: WS-OTTICO-RECUPERA.TEMPLATE Parametri in ingresso:\n"+
//					"CSOC = " + in.getCodiceSocieta() + "\n" +
//					"CUTE = " + in.getCodiceUtente() + "\n" +
//					"CENT = " + in.getCodiceEnte() + "\n" +
//					"TBOL = " + in.getTipoBollettino() + "\n" +
//					"TDOC = " + in.getTipoDocumento() + "\n" +
//					"TSER = " + in.getTipologiaServizio()+ "\n" +
//					"DATA = " + in.getDataValidita());
			String riferimentoTemplate = bean.recuperaTemplate(in.getTipoDocumento(), 
					new ConfigurazioneIdentify(in.getCodiceSocieta(), in.getCodiceUtente(), in.getCodiceEnte()), 
					in.getTipologiaServizio(), in.getTipoBollettino(), 
					new Timestamp(in.getDataValidita().getTime().getTime()), dbSchemaCodSocieta
					);
			//in.getDataValidita() != null 
			//? new Timestamp(in.getDataValidita().getTime().getTime()) : new Timestamp(System.currentTimeMillis()));
			info("riferimentoTemplate - " + riferimentoTemplate);
			return new RecuperaTemplateResponse(StringUtility.isEmpty(riferimentoTemplate) ? "" : riferimentoTemplate, 
					new ResponseType(ResponseTypeRetCode.value1, "Success"));

		} catch(Exception e) {
			error("recuperaTemplate failed, generic error due to: ", e);
			return new RecuperaTemplateResponse("", new ResponseType(ResponseTypeRetCode.value2, "Generic Err"));
		}
	}

	public StampaBollettinoResponse stampaBollettino(StampaBollettinoRequest in) throws RemoteException, FaultType {
		
		String reportZipPath = null;
		StampaBollettinoResponse res = new StampaBollettinoResponse();
		
		try {

			//inizio LP PG200070 - 20200813
			//OtticoFacadeBean bean = new OtticoFacadeBean();
			OtticoFacadeBean bean = getFacadeService();
			//fine LP PG200070 - 20200813
			
			BaseSearchKeys baseSearchKeys = new BaseSearchKeys();
			baseSearchKeys.setCodiceSocieta(in.getCodiceSocieta());
			baseSearchKeys.setCodiceUtente(in.getCodiceUtente());
			baseSearchKeys.setChiaveEntePayer(in.getChiaveEnte());
			baseSearchKeys.setCodiceEnte(in.getCodiceEnte());
			
			WebServiceInfo wsInfo = new WebServiceInfo(in.getIndirizzoWebServiceOttico(), 
					in.getUserWebServiceOttico(), in.getPasswordWebServiceOttico(), "", 
					new WebServiceInfoType(in.getFlagWebServiceOttico()));
			
			InformazioniStampa infoStampa = new InformazioniStampa();
			infoStampa.setBaseSearchKeys(baseSearchKeys);
			infoStampa.setWebServiceOttico(wsInfo);
			infoStampa.setNumeroDocumento(in.getNumeroDocumento());
			infoStampa.setCachedRowSetMovimenti(in.getListaMovimentiXml());

			reportZipPath = bean.stampaBollettino(infoStampa, dbSchemaCodSocieta);
			if (reportZipPath != null && reportZipPath.length() > 0)
			{
				res.setFileNameZip(reportZipPath);
				res.setResponse(new ResponseType(ResponseTypeRetCode.value1, "Success"));
				return res;
			}
		
		} catch(Exception e) {
			error("stampaBollettino failed, generic error due to: ", e);
		}
		
		res.setFileNameZip(null);
		res.setResponse(new ResponseType(ResponseTypeRetCode.value2, "Generic Err"));
		return res;
	}

	public StampaDocumentoResponse stampaDocumento(StampaDocumentoRequest in) throws RemoteException, FaultType {
		
		String reportPath = null;
		StampaDocumentoResponse res = new StampaDocumentoResponse();
		
		try {

			//inizio LP PG200070 - 20200813
			//OtticoFacadeBean bean = new OtticoFacadeBean();
			OtticoFacadeBean bean = getFacadeService();
			//fine LP PG200070 - 20200813
			
			BaseSearchKeys baseSearchKeys = new BaseSearchKeys();
			baseSearchKeys.setCodiceSocieta(in.getCodiceSocieta());
			baseSearchKeys.setCodiceUtente(in.getCodiceUtente());
			baseSearchKeys.setChiaveEntePayer(in.getChiaveEnte());
			baseSearchKeys.setCodiceEnte(in.getCodiceEnte());
			
			WebServiceInfo wsInfo = new WebServiceInfo(in.getIndirizzoWebServiceOttico(), 
					in.getUserWebServiceOttico(), in.getPasswordWebServiceOttico(), "", 
					new WebServiceInfoType(in.getFlagWebServiceOttico()));
			
			InformazioniStampa infoStampa = new InformazioniStampa();
			infoStampa.setBaseSearchKeys(baseSearchKeys);
			infoStampa.setWebServiceOttico(wsInfo);
			
			infoStampa.setNumeroDocumento(in.getNumeroDocumento());
			infoStampa.setFlagDocumento(in.getFlagDocumento());
			
			infoStampa.setCachedRowSetDoc(in.getDettaglioDocumentoXml());
			infoStampa.setCachedRowSetMovimenti(in.getListaMovimentiXml());
			infoStampa.setCachedRowSetScadenze(in.getListaScadenzeXml());
			infoStampa.setCachedRowSetTributi(in.getListaTributiXml());
			
			infoStampa.setCodiceFiscale(in.getCodiceFiscale());
			infoStampa.setCodiceImpostaServizio(in.getCodiceImpostaServizio());

			reportPath = bean.stampaDocumento(infoStampa, dbSchemaCodSocieta);
			if (reportPath != null && reportPath.length() > 0)
			{
				res.setFileName(reportPath);
				res.setResponse(new ResponseType(ResponseTypeRetCode.value1, "Success"));
				return res;
			}
		
		} catch(Exception e) {
			error("stampaBollettino failed, generic error due to: ", e);
		}
		
		res.setFileName(null);
		res.setResponse(new ResponseType(ResponseTypeRetCode.value2, "Generic Err"));
		return res;
	}

	public StampaQuietanzaResponse stampaQuietanza(StampaQuietanzaRequest in) throws RemoteException, FaultType {
		String reportZipPath = null;
		StampaQuietanzaResponse res = new StampaQuietanzaResponse();
		
		try {

			//inizio LP PG200070 - 20200813
			//OtticoFacadeBean bean = new OtticoFacadeBean();
			OtticoFacadeBean bean = getFacadeService();
			//fine LP PG200070 - 20200813
			
			BaseSearchKeys baseSearchKeys = new BaseSearchKeys();
			baseSearchKeys.setCodiceSocieta(in.getCodiceSocieta());
			baseSearchKeys.setCodiceUtente(in.getCodiceUtente());
			baseSearchKeys.setChiaveEntePayer(in.getChiaveEnte());
			baseSearchKeys.setCodiceEnte(in.getCodiceEnte());
			
			InformazioniStampa infoStampa = new InformazioniStampa();
			infoStampa.setBaseSearchKeys(baseSearchKeys);
			infoStampa.setNumeroDocumento(in.getNumeroDocumento());
			infoStampa.setCachedRowSetMovimenti(in.getListaMovimentiXml());
			
			reportZipPath = bean.stampaAutorizzazioneBanca(infoStampa, dbSchemaCodSocieta);
			if (reportZipPath != null && reportZipPath.length() > 0)
			{
				res.setFileNameZip(reportZipPath);
				res.setResponse(new ResponseType(ResponseTypeRetCode.value1, "Success"));
				return res;
			}
		
		} catch(Exception e) {
			error("stampaQuietanza failed, generic error due to: ", e);
		}
		
		res.setFileNameZip(null);
		res.setResponse(new ResponseType(ResponseTypeRetCode.value2, "Generic Err"));
		return res;
	}


	public StampaRelataResponse stampaRelata(StampaRelataRequest in) throws RemoteException, FaultType 
	{
		String reportZipPath = null;
		StampaRelataResponse res = new StampaRelataResponse();
		
		try {

			//inizio LP PG200070 - 20200813
			//OtticoFacadeBean bean = new OtticoFacadeBean();
			OtticoFacadeBean bean = getFacadeService();
			//fine LP PG200070 - 20200813
			
			BaseSearchKeys baseSearchKeys = new BaseSearchKeys();
			baseSearchKeys.setCodiceSocieta(in.getCodiceSocieta());
			baseSearchKeys.setCodiceUtente(in.getCodiceUtente());
			baseSearchKeys.setChiaveEntePayer(in.getChiaveEnte());
			baseSearchKeys.setCodiceEnte(in.getCodiceEnte());
			
			WebServiceInfo wsInfo = new WebServiceInfo(in.getIndirizzoWebServiceOttico(), 
					in.getUserWebServiceOttico(), in.getPasswordWebServiceOttico(), "", 
					new WebServiceInfoType(in.getFlagWebServiceOttico()));
			
			InformazioniStampa infoStampa = new InformazioniStampa();
			infoStampa.setBaseSearchKeys(baseSearchKeys);
			infoStampa.setWebServiceOttico(wsInfo);
			infoStampa.setNumeroDocumento(in.getNumeroDocumento());

			reportZipPath = bean.stampaRelata(infoStampa, dbSchemaCodSocieta);
			if (reportZipPath != null && reportZipPath.length() > 0)
			{
				res.setFileNameZip(reportZipPath);
				res.setResponse(new ResponseType(ResponseTypeRetCode.value1, "Success"));
				return res;
			}
		
		} catch(Exception e) {
			error("stampaRelata failed, generic error due to: ", e);
		}
		
		res.setFileNameZip(null);
		res.setResponse(new ResponseType(ResponseTypeRetCode.value2, "Generic Err"));
		return res;
	}
	
	/*
	private OtticoFacade getOtticoFacade()
	{
		try {
			// we instantiate facade reference
			OtticoFacadeHome homeRef = (OtticoFacadeHome)ServiceLocator.getInstance().getRemoteHome(
					env.getProperty(Context.PROVIDER_URL),
					env.getProperty(Context.INITIAL_CONTEXT_FACTORY),
					null, null, 
			OtticoFacadeHome.JNDI_NAME, OtticoFacadeHome.class);
			return homeRef.create();
		} catch (Exception e) {}
		
		return null;
	}*/
	
	//PG150290_002 - inizio
	public com.seda.payer.ottico.webservice.dati.GetListaImmaginiResponse getListaImmagini(com.seda.payer.ottico.webservice.dati.GetListaImmaginiRequest in) throws java.rmi.RemoteException, com.seda.payer.ottico.webservice.srv.FaultType {
		GetListaImmaginiResponse res = new GetListaImmaginiResponse();
		ArrayList<ImmagineDto> listaImmagini = null;
		try {

			//inizio LP PG200070 - 20200813
			//OtticoFacadeBean bean = new OtticoFacadeBean();
			OtticoFacadeBean bean = getFacadeService();
			//fine LP PG200070 - 20200813
			WebServiceInfo wsInfo = new WebServiceInfo(in.getIndirizzoWebServiceOttico(), 
					in.getUserWebServiceOttico(), in.getPasswordWebServiceOttico(), "", 
					new WebServiceInfoType(in.getFlagWebServiceOttico()));
			listaImmagini = bean.getListaImmagini(in.getNumeroDocumento(), in.getFlagAccesso(), wsInfo, dbSchemaCodSocieta);
			if (listaImmagini != null && listaImmagini.size() > 0) {
				res.setListXmlImmagini(convertXMLListaImmagini(listaImmagini));
				res.setResponse(new ResponseType(ResponseTypeRetCode.value1, "Success"));
				return res;
			}
			if (listaImmagini != null && listaImmagini.size() == 0) {
				res.setListXmlImmagini("");
				res.setResponse(new ResponseType(ResponseTypeRetCode.value4, "Nessuna immagine disponibile"));
				return res;
			}
		
		} catch(Exception e) {
			error("getListaImmagini failed, generic error due to: ", e);
		}
		
		res.setListXmlImmagini(null);
		res.setResponse(new ResponseType(ResponseTypeRetCode.value2, "Generic Err"));
		return res;
    }
	
	   private String convertXMLListaImmagini(ArrayList<ImmagineDto> listaImmagini)
		{
			//inizio LP PG21XX04 Leak
			WebRowSetImpl rowSet = null;
			//fine LP PG21XX04 Leak
	    	try
			{
	    		String sXML = "";
				//info("convertXMLListaImmagini: request execute");
		    	if (listaImmagini != null && listaImmagini.size() > 0)
				{
					//inizio LP PG21XX04 Leak
					//WebRowSetImpl rowSet = new WebRowSetImpl();
					rowSet = new WebRowSetImpl();
					//fine LP PG21XX04 Leak
					RowSetMetaDataImpl rsMdData = new RowSetMetaDataImpl();
			
					// setto le colonne presenti
					//inizio LP OT200010
					//rsMdData.setColumnCount(5);
					rsMdData.setColumnCount(7);
					//fine LP OT200010
					rsMdData.setColumnType(1, Types.VARCHAR);
					rsMdData.setColumnType(2, Types.VARCHAR);
					rsMdData.setColumnType(3, Types.VARCHAR);
					rsMdData.setColumnType(4, Types.VARCHAR);
					rsMdData.setColumnType(5, Types.VARCHAR);
					//inizio LP OT200010
					rsMdData.setColumnType(6, Types.INTEGER);
					rsMdData.setColumnType(7, Types.INTEGER);
					//fine LP OT200010
					
					rowSet.setMetaData(rsMdData);
					
					// ciclo 
					// muovo il puntatore all'inizio
					rowSet.beforeFirst();
					rowSet.moveToInsertRow();
					for (ImmagineDto immagine : listaImmagini)
					{
						// inserisco le righe
						rowSet.updateString(1, immagine.getFilePath()== null ? "" : immagine.getFilePath());
						rowSet.updateString(2, immagine.getDataDocumento()== null ? "" : immagine.getDataDocumento());
						//15012016 GG - inizio
						//Sostituita descrizione "Cronologico" con "Esito" su richiesta del cliente
						//rowSet.updateString(3, immagine.getTipoImmagine()== null ? "" : immagine.getTipoImmagine());
						rowSet.updateString(3, immagine.getTipoImmagine()== null ? "" : (immagine.getTipoImmagine().trim().equalsIgnoreCase("Cronologico")?"Esito":immagine.getTipoImmagine()));
						//15012016 GG - fine
						rowSet.updateString(4, immagine.getIdDocumento()== null ? "" : immagine.getIdDocumento());
						rowSet.updateString(5, immagine.getFlagVisibilita()== null ? "" : immagine.getFlagVisibilita());
						//inizio LP OT200010
						rowSet.updateLong(6, immagine.getIndiceDoc());
						rowSet.updateLong(7, immagine.getNumAllegati());
						//fine LP OT200010
						rowSet.insertRow();
						// riposiziono all'inizio perchè vengono invertiti gli elementi 
						rowSet.afterLast();
						rowSet.moveToInsertRow();
					}
					
					// rimette il puntatore all'inzio
					rowSet.moveToCurrentRow();
					rowSet.beforeFirst();
					sXML = Convert.webRowSetToString(rowSet);
				}
		    	
		    	//info("convertXMLListaImmagini: Request execute successfully");
		    	return sXML;
			}
			catch (Exception ex)
			{
				error("convertXMLListaImmagini"+ex);
				return null;
			}
	    	finally {
				//inizio LP PG21XX04 Leak
		    	try {
		    		if(rowSet != null) {
		    			rowSet.close();
		    		}
		    	} catch (SQLException e) {
		    		e.printStackTrace();
				}
				//fine LP PG21XX04 Leak
	    	}
		}
	   
	   public StampaImmagineContenziosoResponse stampaImmagineContenzioso(StampaImmagineContenziosoRequest in) throws RemoteException, FaultType {
			
			String reportPath = null;
			StampaImmagineContenziosoResponse res = new StampaImmagineContenziosoResponse();
			
			try {
				//inizio LP PG200070 - 20200813
				//OtticoFacadeBean bean = new OtticoFacadeBean();
				OtticoFacadeBean bean = getFacadeService();
				//fine LP PG200070 - 20200813
				
				WebServiceInfo wsInfo = new WebServiceInfo(in.getIndirizzoWebServiceOttico(), 
						in.getUserWebServiceOttico(), in.getPasswordWebServiceOttico(), "", 
						new WebServiceInfoType(in.getFlagWebServiceOttico()));

				reportPath = bean.stampaImmagineContenzioso(in.getIdDocBarcode(), in.getFlagAccesso(), in.getPdfExt(), wsInfo, dbSchemaCodSocieta);
				if (reportPath != null && reportPath.length() > 0)
				{
					res.setFileName(reportPath);
					res.setResponse(new ResponseType(ResponseTypeRetCode.value1, "Success"));
					return res;
				}
			
			} catch(Exception e) {
				error("stampaImmagineContenzioso failed, generic error due to: ", e);
			}
			
			res.setFileName(null);
			res.setResponse(new ResponseType(ResponseTypeRetCode.value2, "Generic Err"));
			return res;
		}
	   
	   public StampaImmagineResponse stampaImmagine(StampaImmagineRequest in) throws RemoteException, FaultType {
			
			String reportPath = null;
			StampaImmagineResponse res = new StampaImmagineResponse();
			
			try {
				//inizio LP PG200070 - 20200813
				//OtticoFacadeBean bean = new OtticoFacadeBean();
				OtticoFacadeBean bean = getFacadeService();
				//fine LP PG200070 - 20200813
				
				WebServiceInfo wsInfo = new WebServiceInfo(in.getIndirizzoWebServiceOttico(), 
						in.getUserWebServiceOttico(), in.getPasswordWebServiceOttico(), "", 
						new WebServiceInfoType(in.getFlagWebServiceOttico()));

				reportPath = bean.stampaImmagine(in.getIdDocumento(), in.getFilePath(), wsInfo, dbSchemaCodSocieta);
				if (reportPath != null && reportPath.length() > 0)
				{
					res.setFileName(reportPath);
					res.setResponse(new ResponseType(ResponseTypeRetCode.value1, "Success"));
					return res;
				}
			
			} catch(Exception e) {
				error("stampaImmagine failed, generic error due to: ", e);
			}
			
			res.setFileName(null);
			res.setResponse(new ResponseType(ResponseTypeRetCode.value2, "Generic Err"));
			return res;
		}
	//PG150290_002 - fine
}