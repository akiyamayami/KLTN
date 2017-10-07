package com.tlcn.model;

import java.util.List;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;

@Entity(name="typeproposal")
public class TypeProposal {
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private int typeID;
	private String name;
	
	@OneToMany(mappedBy="type")
	private List<Proposal> listproposal;

	
	public TypeProposal() {
		super();
	}

	public TypeProposal(int typeID, String name, List<Proposal> listproposal) {
		super();
		this.typeID = typeID;
		this.name = name;
		this.listproposal = listproposal;
	}

	public int getTypeID() {
		return typeID;
	}

	public void setTypeID(int typeID) {
		this.typeID = typeID;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public List<Proposal> getListproposal() {
		return listproposal;
	}

	public void setListproposal(List<Proposal> listproposal) {
		this.listproposal = listproposal;
	}
	
	
}
