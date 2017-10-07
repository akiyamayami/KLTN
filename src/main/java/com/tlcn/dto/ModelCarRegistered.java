package com.tlcn.dto;

import java.util.List;

import com.tlcn.model.Proposal;


public class ModelCarRegistered {
	private String licenseplate;
	private List<Proposal> listproposal;
	
	public ModelCarRegistered(String licenseplate, List<Proposal> listproposal) {
		super();
		this.licenseplate = licenseplate;
		this.listproposal = listproposal;
	}
	public ModelCarRegistered() {
		super();
	}
	public String getLicenseplate() {
		return licenseplate;
	}
	public void setLicenseplate(String licenseplate) {
		this.licenseplate = licenseplate;
	}
	public List<Proposal> getListproposal() {
		return listproposal;
	}
	public void setListproposal(List<Proposal> listproposal) {
		this.listproposal = listproposal;
	}
	
	
	
}

