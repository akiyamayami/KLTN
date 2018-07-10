package com.tlcn.service;

import com.tlcn.Const.Const;
import com.tlcn.dao.ProposalRepository;
import com.tlcn.dto.ModelCreateorChangeProposal;
import com.tlcn.dto.ModelFilterProposal;
import com.tlcn.model.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartException;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.util.*;
import java.util.stream.Collectors;


@Service
public class ProposalService {

    @Autowired
	private  ProposalRepository proposalRepository;
    @Autowired
	private  TypeProposalService typeProposalService;
    @Autowired
	private  SttProposalService sttProposalService;
    @Autowired
	private  NotifyEventService notifyEventService;
    @Autowired
	private  UserService userService;
    @Autowired
	private  CarService carService;
    @Autowired
	private  RegisterProposalService registerProposalService;
    @Autowired
	private  ConfirmProposalService confirmProposalService;



    public void saveProposal(ModelCreateorChangeProposal model, User user, HttpServletRequest request)
			throws MultipartException {
		System.out.println("Save Proposal");
//		Car carRegistered = carService.findOne(model.getCarID());
//		long count = carService
//				.findListCarAvailableInTime(carService.getDate(model.getUsefromdate(), model.getUsefromtime()),
//						carService.getDate(model.getUsetodate(), model.getUsetotime()))
//				.parallelStream().filter(x -> x.equals(carRegistered)).count();
		Proposal proposalnew;
		proposalnew = new Proposal(typeProposalService.findOne(1), model.getName(), model.getDetail(),
				model.getDestination(), model.getPickuplocation(), model.getPickuptime(), model.getUsefromdate(),
				model.getUsefromtime(), model.getUsetodate(), model.getUsetotime(), sttProposalService.findOne(0),
				carService.findOne(model.getCarID()));

		proposalRepository.save(proposalnew);
		MultipartFile file = model.getFile();
		if (!file.isEmpty()) {
			String location = request.getServletContext().getRealPath("static") + "/file/";
			String name = file.getOriginalFilename();
			String namefile = proposalnew.getProposalID() + name.substring(name.lastIndexOf("."), name.length());
            userService.uploadfile(file, location, namefile);
			proposalnew.setFile(namefile);
			System.out.println("Save Proposal 5");
		}
		System.out.println("Save Proposal 6");
		RegisterProposal register = new RegisterProposal(user, proposalnew, new Date());
		registerProposalService.save(register);
		proposalnew.setUserregister(register);
		if(user.getRoleUser().getRoleID() == 2 || user.getRoleUser().getRoleID() == 3)
		{
			proposalnew.setStt(sttProposalService.findOne(1));
			proposalRepository.save(proposalnew);
		}else {
			proposalRepository.save(proposalnew);
			System.out.println("Save Proposal 7");
			notifyEventService.addNotifyToBGMAndPTBVT(proposalnew);
		}
	}
	public boolean updateProposal(int proposalID,ModelCreateorChangeProposal model, HttpServletRequest request){
		try{
			Proposal proposal = proposalRepository.findOne(proposalID);
			MultipartFile file = model.getFile();
			if(!file.isEmpty())
			{
				String location = request.getServletContext().getRealPath("static") + "/file/";
				String name = file.getOriginalFilename();
				String namefile = proposal.getProposalID() + name.substring(name.lastIndexOf("."),name.length());
				if(userService.uploadfile(file,location,namefile))
					return false;
				if(proposal.getFile() != null){
					proposal.setFile(namefile);
				}
			}
			System.out.println("fiel is :" + file.isEmpty());
			System.out.println("check is " + proposal.checkEqual(model));
			// if proposal change is equal to old return
			if(proposal.checkEqual(model) && file.isEmpty()){
				System.out.println("Proposal không có chỉnh sữa gì");
				return true;
			}
			proposal.setName(model.getName());
			proposal.setDetail(model.getDetail());
			proposal.setType(typeProposalService.findOne(2));
			proposal.setDestination(model.getDestination());
			proposal.setPickuplocation(model.getPickuplocation());
			proposal.setPickuptime(model.getPickuptime());
			proposal.setUsefromdate(model.getUsefromdate());
			proposal.setUsefromtime(model.getUsefromtime());
			proposal.setUsetodate(model.getUsetodate());
			proposal.setUsetotime(model.getUsetotime());
			// allow changing car when proposal not confirm yet
			if(!isConfirmProposal(proposal))
				proposal.setCar(carService.findOne(model.getCarID()));
			if(isConfirmProposal(proposal)){
				// notify old proposal have been canceled to user,P.TBVT, driver
				// and set new proposal to not confirm
				//proposal.setStt(sttProposalService.findOne(0));// set not comfirm
				confirmProposalService.delete(proposal.getInfoconfirm().getConfrimproposalID());
				notifyEventService.addNotifyToBGMAndPTBVT(proposal);
				// set notify
			}
			proposalRepository.save(proposal);
			return true;
		}catch (Exception e) {
			e.getMessage();
			return false;
		}
	}
	
	public void remove(Proposal proposal){
		proposalRepository.delete(proposal);
	}
	
	public void approveProposal(Proposal proposal) {
		proposal.setStt(sttProposalService.findOne(Const.Proposal.CONFIRM));
		ConfirmProposal confirmproposal = new ConfirmProposal(userService.GetUser(), proposal, Calendar.getInstance().getTime());
		confirmProposalService.save(confirmproposal);
		proposal.setInfoconfirm(confirmproposal);
		proposalRepository.save(proposal);
		notifyEventService.addNotifyforUser(proposal, proposal.getUserregister().getUser(), "");
	}
	
	public Proposal findOne(int proposalID){
		return proposalRepository.findOne(proposalID);
	}
	public void save(Proposal proposal){
		proposalRepository.save(proposal);
	}
	public List<Proposal> findAll(){
		List<Proposal> proposals = new ArrayList<>();
		for(Proposal proposal : proposalRepository.findAll()){
			proposals.add(proposal);
		}
		Collections.reverse(proposals);
		return proposals;
	}
	private List<Proposal> findProposalofuser(User user){
        List<Proposal> proposals = new ArrayList<>(proposalRepository.listProposal_User(user));
		Collections.reverse(proposals);
		return proposals;
	}
	public List<Proposal> listProposalFind(ModelFilterProposal filter, User user){
        List<Proposal> proposals = new ArrayList<>(getListFilter(filter, user));
		Collections.reverse(proposals);
		return proposals;
	}
	
	public ModelCreateorChangeProposal convertProposalToModelShow( Proposal proposal){
		ModelCreateorChangeProposal modelShow;
		Calendar x = Calendar.getInstance();
		Calendar y = Calendar.getInstance();
		x.setTime(proposal.getUsefromdate());
		y.setTime(proposal.getUsetodate());
		boolean exitstfile = proposal.getFile() != null;
		modelShow = new ModelCreateorChangeProposal(proposal.getProposalID(),
				proposal.getName(),proposal.getDetail(),proposal.getDestination(),proposal.getPickuplocation(),
				proposal.getPickuptime(),x.getTime(),proposal.getUsefromtime(),
				y.getTime(),proposal.getUsetotime(),proposal.getCar().getCarID(),exitstfile,proposal.getStt());
		return modelShow;
	}
	
	public List<Proposal> getListProposalExpired(){
		return proposalRepository.listProposalExpired();
	}
	public List<Proposal> getListProposalAreProcessing(){
        return findAll().parallelStream()
                .filter(p -> p.getStt().getSttproposalID() == 1 && p.getType().getTypeID() != 3 && isInTimeUse(p))
                .collect(Collectors.toList());
	}
	private List<Proposal> getListFilter(ModelFilterProposal filter, User user){
		System.out.println("stt" + filter.getStt() + ", " + filter.getType() + "," + filter.getDatecreate()  );
		Date datecreate = filter.getDatecreate();
		int typeNumber = 0;
		int sttID = -1;
		if(filter.getStt() != null){
			sttID = Integer.parseInt(filter.getStt());
		}
		if(!StringUtils.isEmpty(filter.getType()))
			typeNumber = Integer.parseInt(filter.getType());
		TypeProposal type = null;
		if(typeNumber != 0){
			type = typeProposalService.findOne(typeNumber);
		}
		SttProposal stt = sttProposalService.findOne(sttID);
		if(user == null){
			if(datecreate == null && stt == null && type == null)
				return findAll();
			if(datecreate != null && stt != null && type != null)
				return proposalRepository.LPF_all(datecreate, type, stt);
			if(datecreate != null && stt == null && type != null)
				return proposalRepository.LPF_datecreate_and_type(datecreate, type);
			if(datecreate != null && stt != null)
				return proposalRepository.LPF_datecreate_and_stt(datecreate, stt);
			if(datecreate == null && stt != null && type != null)
				return proposalRepository.LPF_type_stt(type, stt);
			if(datecreate != null)
				return proposalRepository.LPF_datecreate(datecreate);
			if(type != null)
				return proposalRepository.LPF_type(type);
			else
				return proposalRepository.LPF_stt(stt);
		}
		else{
			if(datecreate == null && stt == null && type == null)
			{
				System.out.println("find list proposal of user");
				return findProposalofuser(user);
			}
			if(datecreate != null && stt != null && type != null)
				return proposalRepository.LPF_all_of_user(datecreate, type, stt, user);
			if(datecreate != null && stt == null && type != null)
				return proposalRepository.LPF_datecreate_and_type_of_user(datecreate, type, user);
			if(datecreate != null && stt != null)
				return proposalRepository.LPF_datecreate_and_stt_of_user(datecreate, stt, user);
			if(datecreate == null && stt != null && type != null)
				return proposalRepository.LPF_type_stt_of_user(type, stt, user);
			if(datecreate != null)
				return proposalRepository.LPF_datecreate_of_user(datecreate, user);
			if(type != null)
				return proposalRepository.LPF_type_of_user(type, user);
			else
				return proposalRepository.LPF_stt_of_user(stt, user);
		}
		
	}
	public List<Proposal> getListProposalHaveCarHasBeenUsed(Proposal proposal){
		List<Proposal> listProposal = proposalRepository.getListProposalNotCofirmOfCar(proposal.getCar());
		long timeFrom = carService.getDate(proposal.getUsefromdate(), proposal.getUsefromtime());
		long timeTo = carService.getDate(proposal.getUsetodate(), proposal.getUsetotime());
        return listProposal.parallelStream()
                .filter(p -> isBetween(carService.getDate(p.getUsefromdate(), p.getUsefromtime()),carService.getDate(p.getUsetodate(), p.getUsetotime()),timeFrom,timeTo))
                .collect(Collectors.toList());
	}
	
	public Proposal isProposalHaveCarWasUsed(Car car, Proposal proposal){
		List<Proposal> listProposal = proposalRepository.getListProposalConfirmOfCar(car);
		long timeFrom =  carService.getDate(proposal.getUsefromdate(), proposal.getUsefromtime());
		long timeTo = carService.getDate(proposal.getUsetodate(), proposal.getUsetotime());
		Proposal x;
		// time check is X
		// time Already used is Y 
		// first check X is Between Yfrom and YTo
		// second check Y is Between Xfrom and XTo
		x = listProposal.parallelStream()
				.filter(p -> isBetween(timeFrom,timeTo,carService.getDate(p.getUsefromdate(), p.getUsefromtime()),carService.getDate(p.getUsetodate(), p.getUsetotime()))
						|| isBetween(carService.getDate(p.getUsefromdate(), p.getUsefromtime()),carService.getDate(p.getUsetodate(), p.getUsetotime()),timeFrom,timeTo))
				.findFirst().orElse(null);
		if(x != null){
			System.out.println(" Proposal register car is : " + x.getProposalID());
		}
		return x;
	}
	public boolean check_User_Owned_Proposal_Or_Not(int proposalID, User user){
        return proposalRepository.checkProposalOwnedByUserOrNot(proposalID, user) != 0;
    }
	private boolean isBetween(long timeCheckFrom, long timeCheckTo, long timeFrom, long timeTo){
        return (timeCheckFrom >= timeFrom && timeCheckFrom <= timeTo) || (timeCheckTo >= timeFrom && timeCheckTo <= timeTo);
    }

	
	public boolean isInTimeUse(Proposal proposal){
        System.out.println("proposal " + proposal.getProposalID() + "checking time in use");
		long timeStart = carService.getDate(proposal.getUsefromdate(),proposal.getUsefromtime());
		long today = System.currentTimeMillis();
		long timeEnd = carService.getDate(proposal.getUsetodate(),proposal.getUsetotime());
        return proposal.getStt().getSttproposalID() == 1 && proposal.getType().getTypeID() != 3
                && today > timeStart && timeEnd > today;
    }

	public boolean isConfirmProposal(Proposal proposal){
        return proposal.getStt().getSttproposalID() == 1;
    }
	public boolean isProposalCancel(Proposal proposal){
        return proposal.getStt().getSttproposalID() == 1 && proposal.getType().getTypeID() == 3;
    }
	public boolean isProposalExpired(Proposal proposal){
		Calendar now = Calendar.getInstance();
		System.out.println(proposal.getUsetodate() +  "+ now = " + now.getTime());
		
		long timeFrom = carService.getDate(proposal.getUsefromdate(),proposal.getUsefromtime());
		long timeTo = carService.getDate(proposal.getUsetodate(),proposal.getUsetotime());
		long timeNow = now.getTime().getTime();
		System.out.println(timeTo < timeNow);
		System.out.println(timeFrom < timeNow);
		if(proposal.getStt().getSttproposalID() == 1){
            return timeTo < timeNow;
        }
		else{
            return timeFrom < timeNow;
        }
	}
}
