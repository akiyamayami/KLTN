package com.tlcn.controller;

import com.tlcn.dto.ModelCreateorChangeProposal;
import com.tlcn.dto.ModelFilterProposal;
import com.tlcn.dto.ModelShowNotify;
import com.tlcn.error.NotOwnerOfProposalException;
import com.tlcn.error.ProposalHasBeenRemoveException;
import com.tlcn.error.ProposalNotFoundException;
import com.tlcn.model.Car;
import com.tlcn.model.Driver;
import com.tlcn.model.Proposal;
import com.tlcn.model.User;
import com.tlcn.service.*;
import com.tlcn.validator.ProposalValidator;
import org.apache.commons.lang.math.NumberUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import javax.validation.Valid;
import java.util.ArrayList;
import java.util.List;


@Controller
public class ProposalController {
	private final UserService userService;
	private final ProposalService proposalService;
	private final TypeProposalService typeProposalService;
	private final CarService carService;
    private final NotifyEventService notifyEventService;
	private final SttProposalService sttProposalService;
    private final CalendarService calendarService;
	private final ProposalValidator proposaValidator;
	
	@Autowired
    public ProposalController(UserService userService, ProposalService proposalService, TypeProposalService typeProposalService, CarService carService, NotifyEventService notifyEventService, SttProposalService sttProposalService, CalendarService calendarService, ProposalValidator proposaValidator) {
		super();
        this.userService = userService;
        this.proposalService = proposalService;
        this.typeProposalService = typeProposalService;
        this.carService = carService;
        this.notifyEventService = notifyEventService;
        this.sttProposalService = sttProposalService;
        this.calendarService = calendarService;
        this.proposaValidator = proposaValidator;
    }
	private List<Proposal> getListProposalOfPage(int pageNumber, List<Proposal> listProposal, int numberOfPages){
		System.out.println("3");
		List<Proposal> list = listProposal;
		if(pageNumber == 1)
		{
			System.out.println("4");
			if(list.size() < 15){
				list = list.subList(0, list.size());
				System.out.println("5");
			}
			else
			{
				list = list.subList(0, 15);
				System.out.println("6");
			}
		}
		else{
			if(pageNumber > numberOfPages + 1){
				System.out.println("7");
				return null;
			}
			if(pageNumber * 15 > list.size()){
				list = list.subList((pageNumber - 1) * 15 , list.size());
				System.out.println("8");
			}
			else{
				list = list.subList(pageNumber * 15 , (pageNumber + 1) * 15);
				System.out.println("9");
			}
		}
		
		return list;
	}
	private void showListProposal(Model model, String month, String year, String pageNumber, ModelFilterProposal filterproposal){
		List<Proposal> listProposal;
		List<Proposal> listShow;
		int pageNum = 1;
		if(!StringUtils.isEmpty(pageNumber)){
			if(NumberUtils.isNumber(pageNumber))
				pageNum = Integer.parseInt(pageNumber);
		}
		System.out.println("get number page");
		System.out.println(pageNum);
		int numberOfPages = 0;
		if (userService.checkUserhasAuthority("CHANGE_PROPOSAL")) {
			// access to mode find-my-proposal for normal user
			model.addAttribute("MODE", "MODE_FIND_MY_PROPOSAL");
			listProposal = proposalService.listProposalFind(filterproposal, userService.GetUser());
			numberOfPages = listProposal.size() / 15 ;
			if(listProposal.size() % 15 != 0)
				numberOfPages++;
			listShow = getListProposalOfPage(pageNum, listProposal, numberOfPages);
			if(listShow == null)
			{
				if(listProposal.size() < 15)
					listProposal = listProposal.subList(0, listProposal.size());
				else
					listProposal = listProposal.subList(0, 15);
			}
			else{
				listProposal = listShow;
				model.addAttribute("pageNumber", pageNum);
			}
			model.addAttribute("listProposal",listProposal);
			model.addAttribute("numberOfPages", numberOfPages);
		} else {
			System.out.println("1");
			// mode find-all-proposal for P.TBVT and BGM
			model.addAttribute("MODE", "MODE_FIND_PROPOSAL");
			listProposal = proposalService.listProposalFind(filterproposal,null);
			numberOfPages = listProposal.size() / 15 ;
			if(listProposal.size() % 15 != 0)
				numberOfPages++;
			listShow = getListProposalOfPage(pageNum, listProposal, numberOfPages);
			System.out.println("2");
			if(listShow == null)
			{
				System.out.println("10");
				if(listProposal.size() < 15)
					listProposal = listProposal.subList(0, listProposal.size());
				else
					listProposal = listProposal.subList(0, 15);
			}
			else{
				System.out.println("11");
				listProposal = listShow;
				model.addAttribute("pageNumber", pageNum);
			}
			model.addAttribute("listProposal",listProposal);
			model.addAttribute("numberOfPages", numberOfPages);
		}
		showCalendarAndNotify(model,month,year);
		model.addAttribute("filter-model", new ModelFilterProposal());
	}
	// page find-proposal
	@RequestMapping(value = "/", method = RequestMethod.GET)
	public String findpropsal(Model model,@RequestParam(value = "page" , required=false) String pageNumber,
			@ModelAttribute("filter-model") ModelFilterProposal filterproposal) {
		System.out.println(pageNumber);
		showListProposal(model,null,null,pageNumber,filterproposal);
		return "Index";
	}

	@RequestMapping(value = "/", method = RequestMethod.POST)
	public String filterproposal(Model model, @ModelAttribute("filter-model") ModelFilterProposal filterproposal) {
		showListProposal(model,null,null,null,filterproposal);
		showCalendarAndNotify(model,null,null);
		System.out.println("date = " + filterproposal.getDatecreate() + "type = " + filterproposal.getType() + "stt = " + filterproposal.getStt());
		model.addAttribute("filter-model", filterproposal);
		
		return "Index";
	}
	// page create-proposal
	@RequestMapping(value = "/create-proposal", method = RequestMethod.GET)
	public String createProposal(Model model, @RequestParam(value = "m", required=false) String month, @RequestParam(value = "y", required=false) String year) {
		model.addAttribute("MODE", "MODE_CREATE_PROPOSAL");
		model.addAttribute("typeForm", "/create-proposal");
		model.addAttribute("Proposal", new ModelCreateorChangeProposal());
		model.addAttribute("carsAvailble", carService.getListCarAvailable());
		showCalendarAndNotify(model,month,year);
		return "Index";
	}

	@RequestMapping(value = "/create-proposal", method = RequestMethod.POST)
	public String createProposalPOST(Model model,
			@Valid @ModelAttribute("Proposal") ModelCreateorChangeProposal proposal,BindingResult result, 
			HttpServletRequest request) throws MultipartException {
		proposaValidator.validate(proposal, result);
		if(checkExceptionCarAndDriver(carService.findOne(proposal.getCarID()),model,"change") || result.hasErrors()){
			model.addAttribute("MODE", "MODE_CREATE_PROPOSAL");
			model.addAttribute("typeForm", "/create-proposal");
			model.addAttribute("Proposal", proposal);
			model.addAttribute("carsAvailble", carService.getListCarAvailable());
			showCalendarAndNotify(model,null,null);
			return "Index";
		}
		proposalService.saveProposal(proposal,userService.GetUser(),request);
		return "redirect:/";
	}

	// page change-proposal
	@RequestMapping(value = "/change-proposal-{proposalID}", method = RequestMethod.GET)
	public String changeProposal(Model model, @PathVariable int proposalID, HttpSession session, @RequestParam(value = "m", required=false) String month, @RequestParam(value = "y", required=false) String year) {
		model.addAttribute("typeForm", "/change-proposal");
		if(proposalID == -1)
			throw new ProposalHasBeenRemoveException();
		Proposal x = proposalService.findOne(proposalID);
		if(x == null)
			throw new ProposalNotFoundException();
		if (isProposalOfUser(proposalID,userService.GetUser())) {
			session.setAttribute("proposalID", proposalID);
			showCalendarAndNotify(model,month,year);
			ModelCreateorChangeProposal modelShow = proposalService.convertProposalToModelShow(x);
			model.addAttribute("Proposal", modelShow);
			// change position car use to top
			showCarWhenChangeProposal(modelShow,model,x);
			if(checkExceptionProposal(x,model))
				model.addAttribute("MODE", "MODE_PROPOSAL_EXPIRED");
			else{
				checkExceptionCarAndDriver(x.getCar(),model,"change");
				isCarAlreadyUsed(x,model);
				model.addAttribute("MODE", "MODE_CHANGE_PROPOSAL");
			}
			return "Index";
		}
		return "redirect:/accessDenied";
	}
	
	@RequestMapping(value = "/change-proposal", method = RequestMethod.POST)
	public String changeProposalPOST(Model model,@Valid @ModelAttribute("Proposal") ModelCreateorChangeProposal proposal,
			BindingResult result, HttpServletRequest request, HttpSession session) {
		model.addAttribute("MODE", "MODE_CHANGE_PROPOSAL");
		int proposalID = (int) session.getAttribute("proposalID");
		if (isProposalOfUser(proposalID, userService.GetUser())) {
			Proposal x = proposalService.findOne(proposalID);
			proposal.setProposalID(proposalID);
			proposaValidator.validate(proposal, result);
			if (checkExceptionProposal(x, model)) {
				return "redirect:/hackerDetected";
			}
			if (checkExceptionCarAndDriver(carService.findOne(proposal.getCarID()),model,"change") || result.hasErrors()) {
				model.addAttribute("typeForm", "/change-proposal");
				session.setAttribute("proposalID", proposalID);
				showCalendarAndNotify(model, null, null);
				model.addAttribute("Proposal", proposal);
				// change position car use to top
				showCarWhenChangeProposal(proposal, model, x);
				return "Index";
			}
			showCalendarAndNotify(model,null,null);
				
			if(proposalService.updateProposal(proposalID, proposal, request)){
				return "redirect:/";
			}else{
				return "Index";
			}
		}
		else
			throw new NotOwnerOfProposalException();
	}

	// page confirm(see)-proposal
	@RequestMapping(value = "/confirm-proposal-{proposalID}", method = RequestMethod.GET)
	public String confirmProposal(Model model, @PathVariable int proposalID, HttpSession session, @RequestParam(value = "m", required=false) String month, @RequestParam(value = "y", required=false) String year) {
		if(proposalID == -1)
			throw new ProposalHasBeenRemoveException();
		model.addAttribute("typeForm", "/confirm-proposal");
		session.setAttribute("proposalID", proposalID);
		Proposal proposal = proposalService.findOne(proposalID);
		ModelCreateorChangeProposal modelShow = proposalService.convertProposalToModelShow(proposal);
		model.addAttribute("Proposal", modelShow);
		showCalendarAndNotify(model,month,year);
		show1Car(proposal.getCar(),model);
		if(checkExceptionProposal(proposal,model) || checkExceptionCarAndDriver(proposal.getCar(),model,"confirm"))
		{
			model.addAttribute("MODE", "MODE_PROPOSAL_EXPIRED");
		}
		else if(!isCarAlreadyUsed(proposal, model)){
			model.addAttribute("MODE", "MODE_CONFIRM_PROPOSAL");
		}else model.addAttribute("MODE", "MODE_PROPOSAL_EXPIRED");
		
		return "Index";
	}
	@RequestMapping(value = "/confirm-proposal", method = RequestMethod.POST)
	public String confirmProposalPOST(Model model, @ModelAttribute("Proposal") ModelCreateorChangeProposal proposal,
                                      HttpSession session) {
		model.addAttribute("MODE", "MODE_CONFIRM_PROPOSAL");
		model.addAttribute("typeForm", "/confirm-proposal");
		int proposalID = (int) session.getAttribute("proposalID");
		Proposal x = proposalService.findOne(proposalID);
		System.out.println("check 1");
		if(x.getStt().getSttproposalID() != 1 || !checkExceptionProposal(x,model)){
			System.out.println("check 1");
			if(!isCarAlreadyUsed(x, model))
			{
				checkExceptionCarAndDriver(x.getCar(),model,"confirm");
				proposalService.approveProposal(x);
				for(Proposal p : proposalService.getListProposalHaveCarHasBeenUsed(x)){
					notifyEventService.addNotifyforUser(p, p.getUserregister().getUser(),"CarWasUsed");
				}
				return "redirect:/";
			}else{
				return "redirect:/confirm-proposal-" + proposalID;
			}
		}
		return "redirect:/hackerDetected";
	}
	@RequestMapping(value = "/cancel-proposal", method = RequestMethod.GET)
	public String cancelProposal(HttpSession session) {
		int proposalID = (int) session.getAttribute("proposalID");
		Proposal proposal = proposalService.findOne(proposalID);
		if(proposal == null)
			throw new ProposalNotFoundException();
		if(proposalService.isConfirmProposal(proposal) && proposal.getType().getTypeID() != 3){
			proposal.setType(typeProposalService.findOne(3));
			proposal.setStt(sttProposalService.findOne(1));
			proposalService.save(proposal);
			notifyEventService.addNotifyToBGMAndPTBVT(proposal);
		}else{
			proposalService.remove(proposal);
			/*proposal.setType(typeProposalService.findOne(3));
			proposal.setStt(sttProposalService.findOne(1));
			proposalService.save(proposal);*/
		}
		return "redirect:/";
	}
	@RequestMapping(value = "/cancel-proposal-{proposalID}", method = RequestMethod.GET)
	public String cancelProposalID(HttpSession session, @PathVariable("proposalID") int proposalID) {
		session.setAttribute("proposalID", proposalID);
		return "redirect:/cancel-proposal";
	}
	
	private boolean isProposalOfUser(int proposalID, User user){
		return proposalService.check_User_Owned_Proposal_Or_Not(proposalID,user);
	}
	
	
	
	
	private void showCalendarAndNotify(Model model, String month, String year){
		List<ModelShowNotify> listNotify = notifyEventService.getListNotifyNewest(userService.GetUser());
		if(listNotify != null && listNotify.size() < 5)
			model.addAttribute("listNotify",listNotify);
		else
			model.addAttribute("listNotify", notifyEventService.getListNotifyNewest(userService.GetUser()).subList(0, 5));
		model.addAttribute("calendar", calendarService.createCalendar(month,year));
	}
	
	
	private void showCarWhenChangeProposal(ModelCreateorChangeProposal modelShow, Model model, Proposal proposal){
		System.out.println("Show car");
		//List<Car> carShow = carService.findListCarAvailableInTime(proposalService.getDate(proposal.getUsefromdate(), proposal.getUsefromtime()),proposalService.getDate(proposal.getUsetodate(), proposal.getUsetotime()));
		List<Car> carShow = carService.getListCarAvailable();
		System.out.println("list car show "+ carShow.size());
		List<Car> listcars = new ArrayList<>();
		Car trash = carService.findOne(modelShow.getCarID());
		// if proposal has been confirmed or expired show only car proposal registered
		// else show car available and car proposal registered
		System.out.println(proposal.getStt().getSttproposalID());
		if(checkExceptionProposal(proposal,model) || proposalService.isConfirmProposal(proposal)){
			System.out.println("Exception");
			show1Car(trash,model);
		}
		else{
			checkExceptionCarAndDriver(carService.findOne(modelShow.getCarID()),model,"change");
			System.out.println("Normal");
			carShow.remove(trash);
			listcars.add(trash);
			listcars.addAll(carShow);			
			model.addAttribute("carsAvailble", listcars);
		}
		
		
	}
	
	private void show1Car(Car car, Model model){
		List<Car> listcars = new ArrayList<>();
		listcars.add(car);
		model.addAttribute("carsAvailble", listcars);
	}
	
	
	private boolean isCarAlreadyUsed(Proposal proposal, Model model){
		Proposal x = proposalService.isProposalHaveCarWasUsed(proposal.getCar(), proposal);
		if(proposal.getStt().getSttproposalID() == 1)
			return false;
		if(x != null){
			System.out.println("Đề nghị :" + proposal.getProposalID() + "sử dụng xe đã được đăng ký");
			model.addAttribute("message", "Xe đăng ký đã được xử dụng");
			return true;
		}
		return false;
	}

	private boolean checkExceptionProposal(Proposal proposal, Model model){
		if(proposalService.isProposalExpired(proposal)){
			System.out.println("Đề nghị :" + proposal.getProposalID() + "Hết hạn");
			model.addAttribute("message", "Đề nghị này đã hết hạn");
			return true;
		}
		if(proposalService.isInTimeUse(proposal)){
			model.addAttribute("message", "Đề nghị này đang trong thời gian thực hiện");
			System.out.println("Đề nghị :" + proposal.getProposalID() + "Trong thời gian thực hiện");
			return true;
		}
		if(proposalService.isProposalCancel(proposal)){
			model.addAttribute("message", "Đề nghị này bị đã hủy");
			return true;
		}
		return false;
	}
	
	
	private boolean checkExceptionCarAndDriver(Car car, Model model, String type){
		if(type.equals("confirm")){
			switch(car.getSttcar().getSttcarID()){
				case 2:
					model.addAttribute("message", "Xe đăng ký đang được bảo trì");
					return true;
				case 3:
					model.addAttribute("message", "Xe đăng ký đã bị xóa khỏi hệ thống");
					return true;
				case 4:
					model.addAttribute("message", "Xe đăng ký hiện không có tài xế");
					return true;
			}
			Driver driver = car.getDriver();
			if(driver == null){
				model.addAttribute("message", "Xe hiện không có tài xế");
				return true;
			}
			else{
				switch(driver.getSttdriver().getSttdriverID()){
					case 2:
						model.addAttribute("message", "Tài xế xe đăng ký đang nghỉ bệnh/phép");
						return true;
					case 3:
						model.addAttribute("message", "Tài xế xe đăng ký đã nghỉ việc");
						return true;
				}
			}
			return false;
		}
		else{
			switch(car.getSttcar().getSttcarID()){
				case 2:
					model.addAttribute("message", "Xe đăng ký đang được bảo trì, Vui lòng đổi xe khác.");
					return true;
				case 3:
					model.addAttribute("message", "Xe đăng ký đã bị xóa khỏi hệ thống, Vui lòng đổi xe khác.");
					return true;
				case 4:
					model.addAttribute("message", "Xe đăng ký hiện không có tài xế, Vui lòng đổi xe khác.");
					return true;
			}
			Driver driver = car.getDriver();
			if(driver == null){
				model.addAttribute("message", "Xe hiện không có tài xế, Vui lòng đổi xe khác.");
				return true;
			}
			else{
				switch(driver.getSttdriver().getSttdriverID()){
					case 2:
						model.addAttribute("message", "Tài xế xe đăng ký đang nghỉ bệnh/phép, Vui lòng đổi xe khác.");
						return true;
					case 3:
						model.addAttribute("message", "Tài xế xe đăng ký đã nghỉ việc, Vui lòng đổi xe khác.");
						return true;
				}
			}
			return false;
		}
	}
}
