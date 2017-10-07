package com.tlcn.model;

import java.util.Calendar;
import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

@Entity(name="notifyevent")
public class NotifyEvent {
	
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private int notifyID;
	
	private Calendar dateUpEvent;
	
	@ManyToOne(fetch=FetchType.EAGER)
	@JoinColumn(name="proposalID")
	private Proposal notifyOfProposal;
	
	@ManyToOne(fetch=FetchType.EAGER)
	@JoinColumn(name="emailUser")
	private User notifyOfUser;
	
	private String message;
	

	public NotifyEvent(Calendar dateUpEvent, Proposal notifyOfProposal, User notifyOfUser) {
		super();
		this.dateUpEvent = dateUpEvent;
		this.notifyOfProposal = notifyOfProposal;
		this.notifyOfUser = notifyOfUser;
	}



	public NotifyEvent(Calendar dateUpEvent, Proposal notifyOfProposal, User notifyOfUser, String message) {
		super();
		this.dateUpEvent = dateUpEvent;
		this.notifyOfProposal = notifyOfProposal;
		this.notifyOfUser = notifyOfUser;
		this.message = message;
	}



	public NotifyEvent() {
		super();
	}

	public int getNotifyID() {
		return notifyID;
	}

	public void setNotifyID(int notifyID) {
		this.notifyID = notifyID;
	}


	public Calendar getDateUpEvent() {
		return dateUpEvent;
	}

	public void setDateUpEvent(Calendar dateUpEvent) {
		this.dateUpEvent = dateUpEvent;
	}



	public Proposal getNotifyOfProposal() {
		return notifyOfProposal;
	}


	public void setNotifyOfProposal(Proposal notifyOfProposal) {
		this.notifyOfProposal = notifyOfProposal;
	}


	public User getNotifyOfUser() {
		return notifyOfUser;
	}

	public void setNotifyOfUser(User notifyOfUser) {
		this.notifyOfUser = notifyOfUser;
	}


	public String getMessage() {
		return message;
	}


	public void setMessage(String message) {
		this.message = message;
	}

	
	
}


