package com.tlcn.model;

import java.util.List;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;

@Entity(name = "sttdriver")
public class SttDriver {
	
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private int sttdriverID;
	
	private String name;
	
	@OneToMany(mappedBy="sttdriver")
	private List<Driver> listdriver;

	public SttDriver() {
		super();
	}

	public SttDriver(int sttdriverID, String name, List<Driver> listdriver) {
		super();
		this.sttdriverID = sttdriverID;
		this.name = name;
		this.listdriver = listdriver;
	}

	public int getSttdriverID() {
		return sttdriverID;
	}

	public void setSttdriverID(int sttdriverID) {
		this.sttdriverID = sttdriverID;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public List<Driver> getListdriver() {
		return listdriver;
	}

	public void setListdriver(List<Driver> listdriver) {
		this.listdriver = listdriver;
	}
	
	
}
