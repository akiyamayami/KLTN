package com.tlcn.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.tlcn.dao.RegisterProposalRespository;
import com.tlcn.model.RegisterProposal;

@Service
public class RegisterProposalService {
	@Autowired
	private RegisterProposalRespository registerProposalRepository;

	public RegisterProposalService() {
		super();
	}
	
	public void save(RegisterProposal register){
		registerProposalRepository.save(register);
	}
}
