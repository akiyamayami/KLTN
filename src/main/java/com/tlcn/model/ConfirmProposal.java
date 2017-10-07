package com.tlcn.model;

import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import org.springframework.format.annotation.DateTimeFormat;

@Entity(name="confirmproposal")
public class ConfirmProposal {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private int confrimproposalID;
	
	@ManyToOne(fetch=FetchType.EAGER)
	@JoinColumn(name="emailuser")
	private User userconfirm;
	
	@OneToOne
	@JoinColumn(name="proposalID",unique=true)
	private Proposal proposalapproved;
	
	@Temporal(TemporalType.DATE)
	@DateTimeFormat(pattern = "dd-MM-yyyy")
	private Date dateconfirm;

	public ConfirmProposal(User userconfirm, Proposal proposalapproved, Date dateconfirm) {
		super();
		this.userconfirm = userconfirm;
		this.proposalapproved = proposalapproved;
		this.dateconfirm = dateconfirm;
	}

	public ConfirmProposal(int confrimproposalID, User userconfirm, Proposal proposalapproved, Date dateconfirm) {
		super();
		this.confrimproposalID = confrimproposalID;
		this.userconfirm = userconfirm;
		this.proposalapproved = proposalapproved;
		this.dateconfirm = dateconfirm;
	}

	public ConfirmProposal() {
		super();
	}

	public int getConfrimproposalID() {
		return confrimproposalID;
	}

	public void setConfrimproposalID(int confrimproposalID) {
		this.confrimproposalID = confrimproposalID;
	}

	public User getUserconfirm() {
		return userconfirm;
	}

	public void setUserconfirm(User userconfirm) {
		this.userconfirm = userconfirm;
	}

	public Proposal getProposalapproved() {
		return proposalapproved;
	}

	public void setProposalapproved(Proposal proposalapproved) {
		this.proposalapproved = proposalapproved;
	}

	public Date getDateconfirm() {
		return dateconfirm;
	}

	public void setDateconfirm(Date dateconfirm) {
		this.dateconfirm = dateconfirm;
	}
	
	
}
