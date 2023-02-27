package com.seda.payer.ottico.facade.core;

import java.io.Serializable;
/**
 * @author aniello.labua
 */
public class TracciatiOtticoInfo implements Serializable{

	private static final long serialVersionUID = 1L;
	private String flowType;
	private String flowCreationData;
	private String codiceEnte;
	private String docType;
	
	public TracciatiOtticoInfo() {
		super();
		// TODO Auto-generated constructor stub
	}

	public TracciatiOtticoInfo(String flowType, String flowCreationData,
			String codiceEnte, String docType) {
		super();
		this.flowType = flowType;
		this.flowCreationData = flowCreationData;
		this.codiceEnte = codiceEnte;
		this.docType = docType;
	}

	public String getFlowType() {
		return flowType;
	}

	public TracciatiOtticoInfo setFlowType(String flowType) {
		this.flowType = flowType;
		return this;
	}

	public String getFlowCreationData() {
		return flowCreationData;
	}

	public TracciatiOtticoInfo setFlowCreationData(String flowCreationData) {
		this.flowCreationData = flowCreationData;
		return this;
	}

	public String getCodiceEnte() {
		return codiceEnte;
	}

	public TracciatiOtticoInfo setCodiceEnte(String codiceEnte) {
		this.codiceEnte = codiceEnte;
		return this;
	}

	public String getDocType() {
		return docType;
	}

	public TracciatiOtticoInfo setDocType(String docType) {
		this.docType = docType;
		return this;
	}

	public String toString() {
		return "TracciatiOtticoInfo [codiceEnte=" + codiceEnte + ", docType="
				+ docType + ", flowCreationData=" + flowCreationData
				+ ", flowType=" + flowType + "]";
	}
	
	
	
}
