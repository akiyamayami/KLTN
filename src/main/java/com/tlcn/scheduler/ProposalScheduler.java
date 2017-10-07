package com.tlcn.scheduler;

import java.util.Calendar;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import com.tlcn.dao.PasswordResetTokenRespository;
import com.tlcn.model.NotifyEvent;
import com.tlcn.model.Proposal;
import com.tlcn.service.NotifyEventService;
import com.tlcn.service.ProposalService;
import com.tlcn.service.SttProposalService;
import com.tlcn.service.TypeProposalService;
@Component
public class ProposalScheduler {
	@Autowired
	private ProposalService proposalService;
	@Autowired
	private NotifyEventService notifyEventService;
	@Autowired
	private SttProposalService sttProposalService;
	@Autowired
	private TypeProposalService typeProposalService;
	@Autowired
	private PasswordResetTokenRespository passwordResetTokenRespository;
	//Check proposal expired and not confirm everyday in 00:00:00 and cancel it and notify to user
	//format cron = (second, minute, hour, day of month, month, day(s) of week)
	@Scheduled(cron = "0 0 0 * * *", zone="Asia/Saigon")
	public void cancelProposalExpired(){
		System.out.println("Start check and cancel proposal expired");
		List<Proposal> listProposalExpired = proposalService.getListProposalExpired();
		System.out.println("Have " + listProposalExpired.size() + " proposal expired");
		for(Proposal proposal : listProposalExpired){
			proposal.setStt(sttProposalService.findOne(1));
			proposal.setType(typeProposalService.findOne(3));
			proposal.setExpired(true);
			notifyEventService.addNotifyforUser(proposal, proposal.getUserregister().getUser(), "CancelProposalExpired");
			proposalService.remove(proposal);
		}
		passwordResetTokenRespository.deleteAllExpiredSince(Calendar.getInstance().getTime());
	}
}
