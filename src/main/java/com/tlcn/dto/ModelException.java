package com.tlcn.dto;

public class ModelException {
	private String error;
	private String message;
	private int code;
	private String mescode;
	
	public String getMescode() {
		return mescode;
	}
	public void setMescode(String mescode) {
		this.mescode = mescode;
	}
	public ModelException(String error, String message, int code, String mescode) {
		super();
		this.error = error;
		this.message = message;
		this.code = code;
		this.mescode = mescode;
	}
	public ModelException() {
		super();
	}
	public String getError() {
		return error;
	}
	public void setError(String error) {
		this.error = error;
	}
	public String getMessage() {
		return message;
	}
	public void setMessage(String message) {
		this.message = message;
	}
	public int getCode() {
		return code;
	}
	public void setCode(int code) {
		this.code = code;
	}

	
	
}
