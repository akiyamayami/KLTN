package com.tlcn.dao;

import java.util.Date;
import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import com.tlcn.model.Car;
import com.tlcn.model.Proposal;
import com.tlcn.model.SttProposal;
import com.tlcn.model.TypeProposal;
import com.tlcn.model.User;

public interface ProposalRepository extends CrudRepository<Proposal, Integer>{
	@Query("select count(p) from proposal p where p.proposalID = ?1 and p.proposalID in (select p.proposalID from proposal p, com.tlcn.model.RegisterProposal r where r.user = ?2 and p.proposalID = r.proposal)")
	public int checkProposalOwnedByUserOrNot(int proposalID,User user);
	
	@Query("select p from proposal p, com.tlcn.model.RegisterProposal r where r.user = ?1 and p.proposalID = r.proposal")
	public List<Proposal> listProposal_User(User user);
	// 1 sort 
	@Query("select p from com.tlcn.model.RegisterProposal r , proposal p where r.dateregister = ?1 and p.proposalID = r.proposal")
	public List<Proposal> LPF_datecreate(Date datecreate);
	
	@Query("select p from com.tlcn.model.RegisterProposal r , proposal p where r.dateregister = ?1 and p.proposalID = r.proposal and p.proposalID in (select p.proposalID from proposal p, com.tlcn.model.RegisterProposal r where r.user = ?2 and p.proposalID = r.proposal)")
	public List<Proposal> LPF_datecreate_of_user(Date datecreate, User user);
	
	@Query("select p from proposal p where p.type = ?1")
	public List<Proposal> LPF_type(TypeProposal type);
	
	@Query("select p from proposal p where p.type = ?1 and p.proposalID in (select p from proposal p, com.tlcn.model.RegisterProposal r where r.user = ?2 and p.proposalID = r.proposal)")
	public List<Proposal> LPF_type_of_user(TypeProposal type, User user);
	
	@Query("select p from proposal p where p.stt = ?1")
	public List<Proposal> LPF_stt(SttProposal stt);
		
	@Query("select p from proposal p where p.stt = ?1 and p.proposalID in (select p from proposal p, com.tlcn.model.RegisterProposal r where r.user = ?2 and p.proposalID = r.proposal)")
	public List<Proposal> LPF_stt_of_user(SttProposal stt, User user);
	// 2 sort
	@Query("select p from com.tlcn.model.RegisterProposal r , proposal p where r.dateregister = ?1 and p.type = ?2 and p.proposalID = r.proposal")
	public List<Proposal> LPF_datecreate_and_type(Date datecreate,TypeProposal type);
	
	@Query("select p from com.tlcn.model.RegisterProposal r , proposal p where r.dateregister = ?1 and p.proposalID = r.proposal and p.type = ?2 and p.proposalID in (select p.proposalID from proposal p, com.tlcn.model.RegisterProposal r where r.user = ?3 and p.proposalID = r.proposal)")
	public List<Proposal> LPF_datecreate_and_type_of_user(Date datecreate,TypeProposal type, User user);
	
	@Query("select p from com.tlcn.model.RegisterProposal r , proposal p where r.dateregister = ?1 and p.stt = ?2 and p.proposalID = r.proposal")
	public List<Proposal> LPF_datecreate_and_stt(Date datecreate,SttProposal stt);
	
	@Query("select p from com.tlcn.model.RegisterProposal r , proposal p where r.dateregister = ?1 and p.proposalID = r.proposal and p.stt = ?2 and p.proposalID in (select p.proposalID from proposal p, com.tlcn.model.RegisterProposal r where r.user = ?3 and p.proposalID = r.proposal)")
	public List<Proposal> LPF_datecreate_and_stt_of_user(Date datecreate,SttProposal stt, User user);
	
	@Query("select p from proposal p where p.type = ?1 and p.stt = ?2")
	public List<Proposal> LPF_type_stt(TypeProposal type, SttProposal stt);
	
	@Query("select p from proposal p where p.type = ?1 and p.stt = ?2 and p.proposalID in (select p.proposalID from proposal p, com.tlcn.model.RegisterProposal r where r.user = ?3 and p.proposalID = r.proposal)")
	public List<Proposal> LPF_type_stt_of_user(TypeProposal type, SttProposal stt, User user);
	
	// 3 sort
	@Query("select p from com.tlcn.model.RegisterProposal r , proposal p where r.dateregister = ?1 and p.type = ?2 and p.proposalID = r.proposal and p.stt = ?3")
	public List<Proposal> LPF_all(Date datecreate,TypeProposal type, SttProposal stt);
	
	@Query("select p from com.tlcn.model.RegisterProposal r , proposal p where r.dateregister = ?1 and p.proposalID = r.proposal and p.type = ?2 and p.stt = ?3 and p.proposalID in (select p.proposalID from proposal p, com.tlcn.model.RegisterProposal r where r.user = ?4 and p.proposalID = r.proposal)")
	public List<Proposal> LPF_all_of_user(Date datecreate,TypeProposal type, SttProposal stt, User user);
	///

	
	
	
	
	@Query("select p from proposal p where (p.usetodate < CURDATE() or CURDATE() between p.usefromdate and p.usetodate) and p.stt = 0")
	public List<Proposal> listProposalExpired();
	
	
	@Query("select p from proposal p where p.stt = 0 and p.car = ?1 and p.type != 3")
	public List<Proposal> getListProposalNotCofirmOfCar(Car car);
	
	@Query("select p from proposal p where p.stt = 1 and p.car = ?1 and p.type != 3")
	public List<Proposal> getListProposalConfirmOfCar(Car car);
	
	
}
