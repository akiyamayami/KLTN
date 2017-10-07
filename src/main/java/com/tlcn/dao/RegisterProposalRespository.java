package com.tlcn.dao;

import java.io.Serializable;

import org.springframework.data.repository.CrudRepository;

import com.tlcn.model.Proposal;
import com.tlcn.model.RegisterProposal;

public interface RegisterProposalRespository extends CrudRepository<RegisterProposal, Integer> {

}
