package com.seda.payer.ottico.facade.core;

import java.io.Serializable;

import com.seda.payer.commons.utility.StringUtility;
/**
 * @author marco.montisano
 */
public class FlowMessages implements Serializable {
	private static final long serialVersionUID = 1L;
	private StringBuffer messages;
	public FlowMessages() {
		this.messages = new StringBuffer();
	}
	public void addMessage(String message) {
		this.messages.append(message + "<br/>");
	}
	public String getMessages() {
		return this.messages.toString();
	}
	public boolean isEmpty() {
		return StringUtility.isEmpty(this.messages.toString());
	}
}