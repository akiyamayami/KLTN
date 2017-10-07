package com.tlcn.dto;


public class ModelPassword {

	private String password;
	
	private String passwordconfirm;
	
	public ModelPassword(String password, String passwordconfirm) {
		super();
		this.password = password;
		this.passwordconfirm = passwordconfirm;
	}
	public ModelPassword() {
		super();
	}
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	public String getPasswordconfirm() {
		return passwordconfirm;
	}
	public void setPasswordconfirm(String passwordconfirm) {
		this.passwordconfirm = passwordconfirm;
	}
	
	
}
