package com.tlcn.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.tlcn.dao.SttProposalRespository;
import com.tlcn.model.SttProposal;

@Service
public class SttProposalService {

	@Autowired
	private SttProposalRespository sttProposalRespository;

	public SttProposalService() {
		super();
	}
	
	public SttProposal findOne(int sttproposalID){
		return sttProposalRespository.findOne(sttproposalID);
	}
}
