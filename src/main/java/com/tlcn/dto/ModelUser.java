package com.tlcn.dto;

import java.util.Date;

import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.multipart.MultipartFile;


public class ModelUser {
	private String email;
	private String name;
	private String phone;
	
	@Temporal(TemporalType.DATE)
	@DateTimeFormat(pattern = "dd-MM-yyyy")
	private Date birthday;
	
	private int roleID;
	
	private MultipartFile file;

	
	public ModelUser() {
		super();
	}

	public ModelUser(String email, String name, String phone, Date birthday, int roleID,
			MultipartFile file) {
		super();
		this.email = email;
		this.name = name;
		this.phone = phone;
		this.birthday = birthday;
		this.roleID = roleID;
		this.file = file;
	}

	public ModelUser(String email, String name, String phone, Date birthday, int roleID) {
		super();
		this.email = email;
		this.name = name;
		this.phone = phone;
		this.birthday = birthday;
		this.roleID = roleID;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}


	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	public Date getBirthday() {
		return birthday;
	}

	public void setBirthday(Date birthday) {
		this.birthday = birthday;
	}

	public int getRoleID() {
		return roleID;
	}

	public void setRoleID(int roleID) {
		this.roleID = roleID;
	}

	public MultipartFile getFile() {
		return file;
	}

	public void setFile(MultipartFile file) {
		this.file = file;
	}
	
	
	
}
