package com.tlcn.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.tlcn.dao.ConfirmProposalRespository;
import com.tlcn.model.ConfirmProposal;

@Service
public class ConfirmProposalService {
	
	@Autowired
	private ConfirmProposalRespository confirmProposalRespository;

	public ConfirmProposalService() {
		super();
	}
	
	public void save(ConfirmProposal confirmproposal){
		confirmProposalRespository.save(confirmproposal);
	}
	
	public void delete(int confirmproposalID){
		confirmProposalRespository.delete(confirmproposalID);
	}
	
}
