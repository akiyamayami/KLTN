package com.tlcn.model;


import java.util.List;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;

@Entity(name="sttproposal")
public class SttProposal {
	
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private int sttproposalID;

	private String name;
	
	
	@OneToMany(mappedBy = "stt")
	private List<Proposal> listProposal;
	
	public SttProposal() {
		super();
	}


	public SttProposal(int sttproposalID, String name, List<Proposal> listProposal) {
		super();
		this.sttproposalID = sttproposalID;
		this.name = name;
		this.listProposal = listProposal;
	}


	public List<Proposal> getListProposal() {
		return listProposal;
	}


	public void setListProposal(List<Proposal> listProposal) {
		this.listProposal = listProposal;
	}



	public int getSttproposalID() {
		return sttproposalID;
	}



	public void setSttproposalID(int sttproposalID) {
		this.sttproposalID = sttproposalID;
	}



	public String getName() {
		return name;
	}



	public void setName(String name) {
		this.name = name;
	}

	
}
