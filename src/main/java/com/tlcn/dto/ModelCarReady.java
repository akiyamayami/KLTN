package com.tlcn.dto;

import java.util.Date;
import java.util.List;

import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import org.springframework.format.annotation.DateTimeFormat;

import com.tlcn.model.Proposal;

public class ModelCarReady {
	
	private String licenseplate;
	
	private List<Proposal> proposal;
	
	
	

	public ModelCarReady(String licenseplate,List<Proposal> proposal) {
		super();
		this.licenseplate = licenseplate;
		this.proposal = proposal;
	}

	public ModelCarReady() {
		super();
	}

	public String getLicenseplate() {
		return licenseplate;
	}

	public void setLicenseplate(String licenseplate) {
		this.licenseplate = licenseplate;
	}

	public List<Proposal> getProposal() {
		return proposal;
	}

	public void setProposal(List<Proposal> proposal) {
		this.proposal = proposal;
	}



	

	
	
}
