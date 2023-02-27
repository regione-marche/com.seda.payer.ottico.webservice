package com.seda.payer.ottico.facade.core;

import java.io.Serializable;
/**
 * @author marco.montisano
 */
public class FlowState implements Serializable {
	private static final long serialVersionUID = 1L;
	public static final int SUCCESS = 0;
	public static final int DISCARDED = 1;
	private int code;
	private String message;
	public FlowState(int code, String message) {
		this.code = code;
		this.message = message;
	}
	public int getCode() {
		return code;
	}
	public FlowState setCode(int code) {
		this.code = code;
		return this;
	}
	public String getMessage() {
		return message;
	}
	public void setMessage(String message) {
		this.message = message;
	}
	public void addMessage(String message) {
		if (this.message == null)
			this.message = "";
		
		this.message = this.message + message + "<br/>";
	}
}