package com.tlcn.dto;


public class ModelFormProposal {
	private int proposalID;
	private String nameUser;
	private String destination;
	private String pickupTimeAndLocation;
	private String timeUse;
	private String nameProposal;
	
	public ModelFormProposal(int proposalID, String nameUser, String destination, String pickupTimeAndLocation,
			String timeUse, String nameProposal) {
		super();
		this.proposalID = proposalID;
		this.nameUser = nameUser;
		this.destination = destination;
		this.pickupTimeAndLocation = pickupTimeAndLocation;
		this.timeUse = timeUse;
		this.nameProposal = nameProposal;
	}
	public ModelFormProposal() {
		super();
	}
	public int getProposalID() {
		return proposalID;
	}
	public void setProposalID(int proposalID) {
		this.proposalID = proposalID;
	}
	public String getNameUser() {
		return nameUser;
	}
	public void setNameUser(String nameUser) {
		this.nameUser = nameUser;
	}
	public String getDestination() {
		return destination;
	}
	public void setDestination(String destination) {
		this.destination = destination;
	}
	public String getPickupTimeAndLocation() {
		return pickupTimeAndLocation;
	}
	public void setPickupTimeAndLocation(String pickupTimeAndLocation) {
		this.pickupTimeAndLocation = pickupTimeAndLocation;
	}
	public String getTimeUse() {
		return timeUse;
	}
	public void setTimeUse(String timeUse) {
		this.timeUse = timeUse;
	}
	public String getNameProposal() {
		return nameProposal;
	}
	public void setNameProposal(String nameProposal) {
		this.nameProposal = nameProposal;
	}
	
	
	
	
}
