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
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;

@Entity(name = "car")
public class Car {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private int carID;
	private String licenseplate;
	private String type;
	private int seats;

	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "sttcarID")
	private SttCar sttcar;

	@ManyToOne(fetch = FetchType.EAGER)
	@JoinTable
	(
			name = "driver_car", 
			joinColumns = { @JoinColumn(name = "carID", unique = true) }, 
			inverseJoinColumns = {@JoinColumn(name = "email") }
	)
	private Driver driver;

	@OneToMany(fetch = FetchType.EAGER, mappedBy = "car")
	private List<Proposal> listproposal;

	public Car() {
		super();
	}

	public Car(String licenseplate, String type, int seats, SttCar sttcar) {
		super();
		this.licenseplate = licenseplate;
		this.type = type;
		this.seats = seats;
		this.sttcar = sttcar;
	}

	public Car(String licenseplate, String type, int seats, SttCar sttcar, Driver driver) {
		super();
		this.licenseplate = licenseplate;
		this.type = type;
		this.seats = seats;
		this.sttcar = sttcar;
		this.driver = driver;
	}

	public Car(int carID, String licenseplate, String type, int seats, SttCar sttcar, Driver driver,
			List<Proposal> listproposal) {
		super();
		this.carID = carID;
		this.licenseplate = licenseplate;
		this.type = type;
		this.seats = seats;
		this.sttcar = sttcar;
		this.driver = driver;
		this.listproposal = listproposal;
	}

	public Car(String licenseplate, String type, int seats) {
		super();
		this.licenseplate = licenseplate;
		this.type = type;
		this.seats = seats;
	}

	public int getCarID() {
		return carID;
	}

	public void setCarID(int carID) {
		this.carID = carID;
	}

	public String getLicenseplate() {
		return licenseplate;
	}

	public void setLicenseplate(String licenseplate) {
		this.licenseplate = licenseplate;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public int getSeats() {
		return seats;
	}

	public void setSeats(int seats) {
		this.seats = seats;
	}

	public SttCar getSttcar() {
		return sttcar;
	}

	public void setSttcar(SttCar sttcar) {
		this.sttcar = sttcar;
	}

	public Driver getDriver() {
		return driver;
	}

	public void setDriver(Driver driver) {
		this.driver = driver;
	}

	public List<Proposal> getListproposal() {
		return listproposal;
	}

	public void setListproposal(List<Proposal> listproposal) {
		this.listproposal = listproposal;
	}

	@Override
	public String toString() {
		return "Car [carID=" + carID + ", licenseplate=" + licenseplate + ", type=" + type + ", seats=" + seats
				+ ", sttcar=" + sttcar + ", driver=" + driver + ", listproposal=" + listproposal + "]";
	}

}
