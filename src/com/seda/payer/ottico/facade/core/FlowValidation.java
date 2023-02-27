package com.seda.payer.ottico.facade.core;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class FlowValidation implements Serializable{

	private static final long serialVersionUID = 1L;
	private List<String> validationMessages;

	public FlowValidation() {
		validationMessages = new ArrayList<String>();
	}
	
	public void add(String message){
		this.validationMessages.add(message);
	}
	
	public List<String> getMessages(){
		return this.validationMessages;
	}
	
	public boolean recordNumber(int flow, int record){
		return flow==record ? true : false;
	}
}
