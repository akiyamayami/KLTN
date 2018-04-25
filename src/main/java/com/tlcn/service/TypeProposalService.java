package com.tlcn.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.tlcn.dao.TypeProposalRepository;
import com.tlcn.model.TypeProposal;

@Service
public class TypeProposalService {
    @Autowired
	private  TypeProposalRepository typeProposalRepository;

	
	public TypeProposal findOne(int typeID){
		return typeProposalRepository.findOne(typeID);
	}
	
}
