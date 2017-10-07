package com.tlcn.model;

import java.util.Date;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import org.springframework.format.annotation.DateTimeFormat;

@Entity(name="registerproposal")
public class RegisterProposal{
	
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private int registerproposalID;
	
	@ManyToOne
    @JoinColumn(name = "emailuser")
	private User user;

	@OneToOne(cascade = CascadeType.REMOVE, orphanRemoval = true)
	@JoinColumn(name="proposalID",unique=true)
	private Proposal proposal;
	
	@Temporal(TemporalType.DATE)
	@DateTimeFormat(pattern = "dd-MM-yyyy")
	private Date dateregister;

	public RegisterProposal() {
		super();
	}
	
	public RegisterProposal(User user, Proposal proposal, Date dateregister) {
		super();
		this.user = user;
		this.proposal = proposal;
		this.dateregister = dateregister;
	}

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

	public Proposal getProposal() {
		return proposal;
	}

	public void setProposal(Proposal proposal) {
		this.proposal = proposal;
	}

	public int getRegisterproposalID() {
		return registerproposalID;
	}

	public void setRegisterproposalID(int registerproposalID) {
		this.registerproposalID = registerproposalID;
	}

	public Date getDateregister() {
		return dateregister;
	}

	public void setDateregister(Date dateregister) {
		this.dateregister = dateregister;
	}

	
	
}
