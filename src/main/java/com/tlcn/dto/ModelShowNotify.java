package com.tlcn.dto;

public class ModelShowNotify {
	private int proposalID;
	private String message;
	private String time;
	public ModelShowNotify(int proposalID, String message, String time) {
		super();
		this.proposalID = proposalID;
		this.message = message;
		this.time = time;
	}
	public ModelShowNotify() {
		super();
	}
	public String getMessage() {
		return message;
	}
	public void setMessage(String message) {
		this.message = message;
	}
	public String getTime() {
		return time;
	}
	public void setTime(String time) {
		this.time = time;
	}
	public int getProposalID() {
		return proposalID;
	}
	public void setProposalID(int proposalID) {
		this.proposalID = proposalID;
	}
	

	
}
