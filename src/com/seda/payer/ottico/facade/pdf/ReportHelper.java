package com.seda.payer.ottico.facade.pdf;

import java.io.Serializable;
import java.net.MalformedURLException;
import java.net.URL;
import java.rmi.RemoteException;
import java.util.Calendar;
import javax.xml.rpc.ServiceException;
import com.seda.commons.properties.tree.PropertiesTree;
//import com.seda.payer.notifiche.facade.handler.PropertiesPath;
import com.seda.payer.ottico.facade.handler.PropertiesPath;
import com.seda.payer.ottico.webservice.dati.RecuperaTemplateRequest;
import com.seda.payer.ottico.webservice.dati.RecuperaTemplateResponse;
import com.seda.payer.ottico.webservice.source.OtticoInterface;
import com.seda.payer.ottico.webservice.source.OtticoServiceLocator;
import com.seda.payer.ottico.webservice.srv.FaultType;
/**
 * @author marco.montisano
 */
public class ReportHelper implements Serializable {

	private static final long serialVersionUID = 1L;
	/**
	 * @param tipoDocumento
	 * @param codiceSocieta
	 * @param tipologiaServizio
	 * @param codiceUtente
	 * @param codiceEnte
	 * @param tipoBollettino
	 * @param dataValidita
	 * @return
	 */
	public static RecuperaTemplateResponse recuperaTemplate(String tipoDocumento, String codiceSocieta, String tipologiaServizio, 
			String codiceUtente, String codiceEnte, String tipoBollettino, Calendar dataValidita, PropertiesTree propertiesTree) {
		OtticoInterface otticoPort = null;
		RecuperaTemplateResponse recuperaTemplateResponse = null;
		OtticoServiceLocator otticoServiceLocator = new OtticoServiceLocator();
		try {
			otticoPort = otticoServiceLocator.getOtticoPort(
					new URL(propertiesTree.getProperty(PropertiesPath.wsOtticoUrl.format(PropertiesPath.baseCatalogName.format()))));
			recuperaTemplateResponse = otticoPort.recuperaTemplate(
					new RecuperaTemplateRequest(tipoDocumento, codiceSocieta, tipologiaServizio, 
							codiceUtente, codiceEnte, tipoBollettino, dataValidita));
			//System.out.println("recuperaTemplate - " + recuperaTemplateResponse.getRiferimentoTemplate());
			
		} catch (MalformedURLException ex) {
			System.out.println("[ERROR] - l'url non è esatta: " + ex.getMessage());
		} catch (ServiceException ex) {
			System.out.println("[ERROR] - la creazione della chiamata è fallita: " + ex.getMessage());
		} catch (FaultType e) {
			System.out.println("[ERROR] - axis fault, generic error due to: [" + e.getCode() + "] " + e.getMessage());
		} catch (RemoteException ex) {
			System.out.println("[ERROR] - l'invocazione del WS è fallita: " + ex.getMessage());
		} catch (Exception ex) {
			System.out.println("[ERROR] - errore generico: " + ex.getMessage());
		}		
		return recuperaTemplateResponse;	
	}
}