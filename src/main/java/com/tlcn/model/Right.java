package com.tlcn.model;

import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;

@Entity(name="rights")
public class Right {
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private int rightID;
	private String name;
	private String detail;
	
	@ManyToMany(cascade = { CascadeType.ALL },mappedBy="listRight" ,targetEntity = Role.class)
	private List<Role> ListRight_role;

	
	@ManyToMany(cascade = { CascadeType.ALL }, targetEntity = User.class)
	@JoinTable
	(
			name = "user_right",
			joinColumns={ @JoinColumn(name="rightID", unique=true) },
			inverseJoinColumns={ @JoinColumn(name="email") }
	)
	private List<User> ListRight_user;
	
	
	
	public Right() {
		super();
	}
	public Right(int rightID, String name, String detail, List<Role> listRight_role, List<User> listRight_user) {
		super();
		this.rightID = rightID;
		this.name = name;
		this.detail = detail;
		this.ListRight_role = listRight_role;
		this.ListRight_user = listRight_user;
	}
	public int getRightID() {
		return rightID;
	}
	public void setRightID(int rightID) {
		this.rightID = rightID;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getDetail() {
		return detail;
	}
	public void setDetail(String detail) {
		this.detail = detail;
	}
	public List<Role> getListRight_role() {
		return ListRight_role;
	}
	public void setListRight_role(List<Role> listRight_role) {
		ListRight_role = listRight_role;
	}
	public List<User> getListRight_user() {
		return ListRight_user;
	}
	public void setListRight_user(List<User> listRight_user) {
		ListRight_user = listRight_user;
	}
	
	
}
