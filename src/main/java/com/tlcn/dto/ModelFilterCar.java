package com.tlcn.dto;


public class ModelFilterCar {
	private String type;
	private int seat;
	public ModelFilterCar(String type, int seat) {
		super();
		this.type = type;
		this.seat = seat;
	}
	public ModelFilterCar() {
		super();
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public int getSeat() {
		return seat;
	}
	public void setSeat(int seat) {
		this.seat = seat;
	}
	
	
}
