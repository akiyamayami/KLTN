package com.tlcn.dto;

public class ModelCreateorChangeCar {
	private int carID;
	
	private String licenseplate;
	private String type;
	private int seats;
	private int sttcarID;
	private String emailDriver;
	
	public ModelCreateorChangeCar() {
		super();
	}

	public ModelCreateorChangeCar(int carID, String licenseplate, String type, int seats, int sttcarID,
			String emailDriver) {
		super();
		this.carID = carID;
		this.licenseplate = licenseplate;
		this.type = type;
		this.seats = seats;
		this.sttcarID = sttcarID;
		this.emailDriver = emailDriver;
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

	public int getSttcarID() {
		return sttcarID;
	}

	public void setSttcarID(int sttcarID) {
		this.sttcarID = sttcarID;
	}

	public String getEmailDriver() {
		return emailDriver;
	}

	public void setEmailDriver(String emailDriver) {
		this.emailDriver = emailDriver;
	}
	
	
}
