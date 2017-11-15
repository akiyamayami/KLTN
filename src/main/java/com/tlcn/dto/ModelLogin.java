package com.tlcn.dto;

public class ModelLogin {
	private String email;
	private String password;
	public ModelLogin(String email, String password) {
		super();
		this.email = email;
		this.password = password;
	}
	public ModelLogin() {
		super();
	}

	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	
	
}
