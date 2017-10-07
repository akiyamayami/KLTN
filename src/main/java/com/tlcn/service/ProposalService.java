package com.tlcn.service;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartException;
import org.springframework.web.multipart.MultipartFile;

import com.tlcn.dao.ProposalRepository;
import com.tlcn.dto.ModelCreateorChangeProposal;
import com.tlcn.dto.ModelFilterProposal;
import com.tlcn.dto.ModelFormProposal;
import com.tlcn.model.Car;
import com.tlcn.model.ConfirmProposal;
import com.tlcn.model.Proposal;
import com.tlcn.model.RegisterProposal;
import com.tlcn.model.SttProposal;
import com.tlcn.model.TypeProposal;
import com.tlcn.model.User;
import com.tlcn.validator.ProposalValidator;


@Service
public class ProposalService {
	@Autowired
	private ProposalRepository proposalRepository;
	@Autowired
	private TypeProposalService typeProposalService;
	@Autowired
	private SttProposalService sttProposalService;
	@Autowired
	private NotifyEventService notifyEventService;
	@Autowired
	private UserService userService;
	@Autowired
	private CarService carService;
	@Autowired
	private RegisterProposalService registerProposalService;
	@Autowired
	private ConfirmProposalService confirmProposalService;
	
	
	public void saveProposal(ModelCreateorChangeProposal model, User user, HttpServletRequest request)
			throws MultipartException, IOException {
		System.out.println("Save Proposal");
		Car carRegistered = carService.findOne(model.getCarID());
		long count = carService
				.findListCarAvailableInTime(getDate(model.getUsefromdate(), model.getUsefromtime()),
						getDate(model.getUsetodate(), model.getUsetotime()))
				.parallelStream().filter(x -> x.equals(carRegistered)).count();
		Proposal proposalnew = null;
		System.out.println("Save Proposal 1");
		proposalnew = new Proposal(typeProposalService.findOne(1), model.getName(), model.getDetail(),
				model.getDestination(), model.getPickuplocation(), model.getPickuptime(), model.getUsefromdate(),
				model.getUsefromtime(), model.getUsetodate(), model.getUsetotime(), sttProposalService.findOne(0),
				carService.findOne(model.getCarID()));
		System.out.println("Save Proposal 2");
		System.out.println("Save Proposal 4");
		proposalRepository.save(proposalnew);
		MultipartFile file = model.getFile();
		if (!file.isEmpty()) {
			String location = request.getServletContext().getRealPath("static") + "/file/";
			String name = file.getOriginalFilename();
			String namefile = proposalnew.getProposalID() + name.substring(name.lastIndexOf("."), name.length());
			uploadfile(file, location, namefile);
			proposalnew.setFile(namefile);
			System.out.println("Save Proposal 5");
		}
		System.out.println("Save Proposal 6");
		RegisterProposal register = new RegisterProposal(user, proposalnew, new Date());
		registerProposalService.save(register);
		proposalnew.setUserregister(register);
		proposalRepository.save(proposalnew);
		System.out.println("Save Proposal 7");
		notifyEventService.addNotifyToBGMAndPTBVT(proposalnew);

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
				if(uploadfile(file,location,namefile))
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
	public List<Proposal> getListProposalReady(){
		long today = Calendar.getInstance().getTime().getTime();
		List<Proposal> listproposaready = findAll().parallelStream()
				.filter(p -> p.getStt().getSttproposalID() == 1 && p.getType().getTypeID() != 3 
						&& (getDate(p.getUsefromdate(), p.getUsefromtime()) >= today ||
						isInTimeUse(p)))
				.collect(Collectors.toList());
		return listproposaready;
	}
	
	public void approveProposal(Proposal proposal) {
		proposal.setStt(sttProposalService.findOne(1));
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
	public List<Proposal> findProposalofuser(User user){
		List<Proposal> proposals = new ArrayList<>();
		for(Proposal proposal : proposalRepository.listProposal_User(user)){
			proposals.add(proposal);
		}
		Collections.reverse(proposals);
		return proposals;
	}
	public List<Proposal> listProposalFind(ModelFilterProposal filter, User user){
		List<Proposal> proposals = new ArrayList<>();
		for(Proposal proposal : getListFilter(filter,user)){
			proposals.add(proposal);
		}
		Collections.reverse(proposals);
		return proposals;
	}
	public void confirmProposal(int proprosalID){
		Proposal x = proposalRepository.findOne(proprosalID);
		x.setStt(sttProposalService.findOne(1));
		proposalRepository.save(x);
	}
	public void deleteProposal(int proposalID){
		// code here
	}
	
	public ModelCreateorChangeProposal convertProposalToModelShow( Proposal proposal){
		ModelCreateorChangeProposal modelShow;
		Calendar x = Calendar.getInstance();
		Calendar y = Calendar.getInstance();
		x.setTime(proposal.getUsefromdate());
		y.setTime(proposal.getUsetodate());
		boolean exitstfile = (proposal.getFile() != null) ? true : false;
		modelShow = new ModelCreateorChangeProposal(proposal.getProposalID(),
				proposal.getName(),proposal.getDetail(),proposal.getDestination(),proposal.getPickuplocation(),
				proposal.getPickuptime(),x.getTime(),proposal.getUsefromtime(),
				y.getTime(),proposal.getUsetotime(),proposal.getCar().getCarID(),exitstfile,proposal.getStt());
		return modelShow;
	}
	
	public List<Proposal> getListProposalExpired(){
		return proposalRepository.listProposalExpired();
	}
	
	private List<Proposal> getListFilter(ModelFilterProposal filter, User user){
		System.out.println("stt" + filter.getStt() + ", " + filter.getType() + "," + filter.getDatecreate()  );
		Date datecreate = filter.getDatecreate();
		int typeNumber = 0;
		int sttID = -1;
		if(filter.getStt() != null){
			sttID = Integer.parseInt(filter.getStt());
		}
		if(filter.getType() != null && filter.getType() != "")
			typeNumber = Integer.parseInt(filter.getType());
		TypeProposal type = null;
		if(typeNumber != 0){
			type = typeProposalService.findOne(typeNumber);
		}
		SttProposal stt = sttProposalService.findOne(sttID);
		System.out.println("stt = "+ stt);
		System.out.println("test " + datecreate == null && stt == null && type == null);
		if(user == null){
			if(datecreate == null && stt == null && type == null)
				return findAll();
			if(datecreate != null && stt != null && type != null)
				return proposalRepository.LPF_all(datecreate, type, stt);
			if(datecreate != null && stt == null && type != null)
				return proposalRepository.LPF_datecreate_and_type(datecreate, type);
			if(datecreate != null && stt != null && type == null)
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
			if(datecreate != null && stt != null && type == null)
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
		long timeFrom = getDate(proposal.getUsefromdate(), proposal.getUsefromtime());
		long timeTo = getDate(proposal.getUsetodate(), proposal.getUsetotime());
		List<Proposal> x = listProposal.parallelStream()
				.filter(p -> isBetween(getDate(p.getUsefromdate(), p.getUsefromtime()),getDate(p.getUsetodate(), p.getUsetotime()),timeFrom,timeTo))
				.collect(Collectors.toList());
		return x;
	}
	
	public Proposal isProposalHaveCarWasUsed(Car car, Proposal proposal){
		List<Proposal> listProposal = proposalRepository.getListProposalConfirmOfCar(car);
		long timeFrom =  getDate(proposal.getUsefromdate(), proposal.getUsefromtime());
		long timeTo = getDate(proposal.getUsetodate(), proposal.getUsetotime());
		Proposal x = null;
		// time check is X
		// time Already used is Y 
		// first check X is Between Yfrom and YTo
		// second check Y is Between Xfrom and XTo
		x = listProposal.parallelStream()
				.filter(p -> isBetween(timeFrom,timeTo,getDate(p.getUsefromdate(), p.getUsefromtime()),getDate(p.getUsetodate(), p.getUsetotime()))
						|| isBetween(getDate(p.getUsefromdate(), p.getUsefromtime()),getDate(p.getUsetodate(), p.getUsetotime()),timeFrom,timeTo))
				.findFirst().orElse(null);
		if(x != null){
			System.out.println(" Proposal register car is : " + x.getProposalID());
		}
		return x;
	}
	public boolean check_User_Owned_Proposal_Or_Not(int proposalID, User user){
		if(proposalRepository.checkProposalOwnedByUserOrNot(proposalID, user) == 0){
			return false;
		}
		return true;
	}
	public boolean isBetween(long timeCheckFrom,long timeCheckTo, long timeFrom, long timeTo){
		if((timeCheckFrom >= timeFrom && timeCheckFrom <= timeTo) || (timeCheckTo >= timeFrom && timeCheckTo <= timeTo))
			return true;
		return false;
	}
	public Long getDate(Date date, Date time){
		Calendar Cdate = Calendar.getInstance(),Ctime = Calendar.getInstance(),dateTime = Calendar.getInstance();
		Cdate.setTime(date);
		Ctime.setTime(time);
		dateTime.set(Cdate.get(Calendar.YEAR), Cdate.get(Calendar.MONTH), Cdate.get(Calendar.DATE), 
				Ctime.get(Calendar.HOUR_OF_DAY), Ctime.get(Calendar.MINUTE));
		return dateTime.getTime().getTime();
		
	}
	
	public ModelFormProposal genarateFormProposal(Proposal proposal){
		ModelFormProposal x = new ModelFormProposal(1,"1231231","12312321","!23123213","123123123","12312321");
		return x;
	}
	
	public boolean isInTimeUse(Proposal proposal){
		Calendar now = Calendar.getInstance();
		long timeStart = getDate(proposal.getUsefromdate(),proposal.getUsefromtime());
		long today = now.getTime().getTime();
		long timeEnd = getDate(proposal.getUsetodate(),proposal.getUsetotime());
		if( proposal.getStt().getSttproposalID() == 1 && proposal.getType().getTypeID() != 3
				&& today > timeStart && timeEnd > today)
			return true;
		return false;
	}
	
	public boolean uploadfile(MultipartFile file, String localtion, String namefile)
			throws IOException, MultipartException {
		byte[] bytes;
		bytes = file.getBytes();
		File serverFile = new File(localtion + namefile);
		BufferedOutputStream stream = new BufferedOutputStream(new FileOutputStream(serverFile));
		stream.write(bytes);
		stream.close();
		return true;
	}
	
	public boolean isConfirmProposal(Proposal proposal){
		if(proposal.getStt().getSttproposalID() == 1)
			return true;
		return false;
	}
	public boolean isProposalCancel(Proposal proposal){
		if(proposal.getStt().getSttproposalID() == 1 && proposal.getType().getTypeID() == 3)
			return true;
		return false;
	}
	public boolean isProposalExpired(Proposal proposal){
		Calendar now = Calendar.getInstance();
		System.out.println(proposal.getUsetodate() +  "+ now = " + now.getTime());
		
		long timeFrom = getDate(proposal.getUsefromdate(),proposal.getUsefromtime());
		long timeTo = getDate(proposal.getUsetodate(),proposal.getUsetotime());
		long timeNow = now.getTime().getTime();
		System.out.println(timeTo < timeNow);
		System.out.println(timeFrom < timeNow);
		if(proposal.getStt().getSttproposalID() == 1){
			if(timeTo < timeNow)
				return true;
			return false;
		}
		else{
			if(timeFrom < timeNow)
				return true;
			return false;
		}
	}
}
