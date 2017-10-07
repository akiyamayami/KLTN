package com.tlcn.model;

import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.OneToMany;

@Entity(name="role")
public class Role {
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private int roleID;
	private String name;
	
	@ManyToMany(cascade = CascadeType.ALL ,fetch = FetchType.EAGER)
	@JoinTable
	(
			name = "role_right",
			joinColumns={ @JoinColumn(name="roleID") },
			inverseJoinColumns={ @JoinColumn(name="rightID")}
	)
	private List<Right> listRight;
	
	@OneToMany(mappedBy="roleUser")
	private List<User> listUser;

	
	
	public Role() {
		super();
	}

	public Role(int roleID, String name, List<Right> listRight, List<User> listUser) {
		super();
		this.roleID = roleID;
		this.name = name;
		this.listRight = listRight;
		this.listUser = listUser;
	}

	public int getRoleID() {
		return roleID;
	}

	public void setRoleID(int roleID) {
		this.roleID = roleID;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public List<Right> getListRight() {
		return listRight;
	}

	public void setListRight(List<Right> listRight) {
		this.listRight = listRight;
	}

	public List<User> getListUser() {
		return listUser;
	}

	public void setListUser(List<User> listUser) {
		this.listUser = listUser;
	} 
	
	
}
