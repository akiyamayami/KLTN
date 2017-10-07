package com.tlcn.model;

import java.util.List;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;

@Entity(name="sttcar")
public class SttCar {
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private int sttcarID;
	private String name;
	
	@OneToMany(mappedBy="sttcar")
	private List<Car> listcar;

	
	public SttCar(int sttcarID, String name, List<Car> listcar) {
		super();
		this.sttcarID = sttcarID;
		this.name = name;
		this.listcar = listcar;
	}

	public SttCar() {
		super();
	}

	public int getSttcarID() {
		return sttcarID;
	}

	public void setSttcarID(int sttcarID) {
		this.sttcarID = sttcarID;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public List<Car> getListcar() {
		return listcar;
	}

	public void setListcar(List<Car> listcar) {
		this.listcar = listcar;
	}
	
	
}
