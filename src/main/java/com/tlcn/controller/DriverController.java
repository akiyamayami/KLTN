package com.tlcn.controller;

import com.tlcn.dto.ModelCreateorChangeDriver;
import com.tlcn.dto.ModelShowNotify;
import com.tlcn.error.DriverNotFoundException;
import com.tlcn.model.Car;
import com.tlcn.model.Driver;
import com.tlcn.model.SttDriver;
import com.tlcn.model.User;
import com.tlcn.service.*;
import com.tlcn.validator.DriverValidator;
import com.tlcn.validator.SttDriverValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartException;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import javax.validation.Valid;
import java.util.Calendar;
import java.util.List;

@Controller
public class DriverController {
	private final CarService carService;
	private final DriverValidator driverValidator;
	private final DriverService driverService;
	private final SttDriverService sttDriverService;
	private final ProposalService proposalService;
	private final NotifyEventService notifyEventService;
	private final SttDriverValidator sttDriverValidator;
	private final SttCarService sttCarService;

	private final CalendarService calendarService;

	private final UserService userService;
	
	
	@Autowired
    public DriverController(CarService carService, DriverValidator driverValidator, DriverService driverService, SttDriverService sttDriverService, ProposalService proposalService, NotifyEventService notifyEventService, SttDriverValidator sttDriverValidator, SttCarService sttCarService, CalendarService calendarService, UserService userService) {
		super();
        this.carService = carService;
        this.driverValidator = driverValidator;
        this.driverService = driverService;
        this.sttDriverService = sttDriverService;
        this.proposalService = proposalService;
        this.notifyEventService = notifyEventService;
        this.sttDriverValidator = sttDriverValidator;
        this.sttCarService = sttCarService;
        this.calendarService = calendarService;
        this.userService = userService;
    }
	@RequestMapping(value="/list-driver", method = RequestMethod.GET)
	public String listDriver(Model model) {
		model.addAttribute("MODE", "MODE_FIND_DRIVER");
		model.addAttribute("listdrivers", driverService.findAll());
		showCalendarAndNotify(model);
		return "driverManager";
	}
	//
	@RequestMapping(value="/create-driver", method = RequestMethod.GET)
	public String createDriver(Model model) {
		model.addAttribute("MODE", "MODE_CREATE_DRIVER");
		showCalendarAndNotify(model);
		showInfoDriver(model,new ModelCreateorChangeDriver(),"create-driver");
		return "driverManager";
	}
	
	@RequestMapping(value="/create-driver", method = RequestMethod.POST)
	public String createDriverPOST(Model model,@Valid @ModelAttribute("Driver") ModelCreateorChangeDriver driver, 
			BindingResult result, HttpServletRequest request) throws MultipartException {
		driverValidator.validate(driver, result);
		if(result.hasErrors()){
			model.addAttribute("MODE", "MODE_CREATE_DRIVER");
			model.addAttribute("Driver", driver);
			model.addAttribute("Car", new Car());
			model.addAttribute("typeForm", "/create-driver");
			showCalendarAndNotify(model);
			List<Car> listCarFree = carService.getListCarFree();
			System.out.println(listCarFree.size());
			if(driver.getListcar() != null){
				for(Car c : driver.getListcar()){
					if(c.getCarID() != 0  && listCarFree.parallelStream().anyMatch(cf -> cf.equals(c))){
						listCarFree.remove(c);
					}
				}
			}
			model.addAttribute("ListCarFree", listCarFree);
			return "driverManager";
		}
		MultipartFile file = driver.getFile();
		System.out.println(file.getOriginalFilename());
		System.out.println(file.isEmpty());
		System.out.println(file.getSize());
		if(!file.isEmpty()){
			String location = request.getServletContext().getRealPath("static") + "/img/user/";
			String name = file.getOriginalFilename();
			String namefile = driver.getEmail() + name.substring(name.lastIndexOf("."),name.length()-1);
			System.out.println(namefile);
            userService.uploadfile(file,location,namefile);
		}
		driverService.convertAndSave(driver);
		
		return "redirect:/list-driver";
	}
	@RequestMapping(value="/change-driver/{email}/", method = RequestMethod.GET)
	public String changeDriverForward(@PathVariable String email, HttpSession session) {
		session.setAttribute("emaildriver", email);
		return "redirect:/change-driver";
	}
	
	
	@RequestMapping(value="/change-driver", method = RequestMethod.GET)
	public String changeDriver(Model model, HttpSession session) {
		model.addAttribute("MODE", "MODE_CHANGE_DRIVER");
		String emaildriver = session.getAttribute("emaildriver").toString();
		System.out.println(emaildriver);
		showCalendarAndNotify(model);
		Driver driver = driverService.findOne(emaildriver);
		if(driver == null){
			throw new DriverNotFoundException();
		}
		ModelCreateorChangeDriver driverShow = driverService.converDriverToDisplay(driver);
		showInfoDriver(model,driverShow,"change-driver");
		return "driverManager";
	}
	
	@RequestMapping(value="/change-driver", method = RequestMethod.POST)
	public String changeDriverPOST(Model model, HttpSession session,
			@Valid @ModelAttribute("Driver") ModelCreateorChangeDriver modelDriver, BindingResult result
			, HttpServletRequest request) {
		driverValidator.validate(modelDriver, result);
		List<Car> listcarofdriverchange = modelDriver.getListcar();
		String emaildriver = session.getAttribute("emaildriver").toString();
		Driver driver = driverService.findOne(emaildriver);
		boolean isCarInTimeUse = false;
		if(driver == null){
			throw new DriverNotFoundException();
		}
		if(listcarofdriverchange != null)
			isCarInTimeUse = (listcarofdriverchange.parallelStream()
					.anyMatch(this::isCarInTimeUse));
		if((driver.getSttdriver().getSttdriverID() == 1 && driver.getSttdriver().getSttdriverID() != modelDriver.getSttdriverID()
				&& isCarInTimeUse)  
				|| result.hasErrors()){
			if(isCarInTimeUse){
				model.addAttribute("message", "Have some car in time use cant change stt of driver");
				modelDriver.setSttdriverID(driver.getSttdriver().getSttdriverID());
			}
			model.addAttribute("MODE", "MODE_CHANGE_DRIVER");
			model.addAttribute("Driver", modelDriver);
			model.addAttribute("Car", new Car());
			model.addAttribute("typeForm", "/change-driver");
			model.addAttribute("listSttDriver", sttDriverService.findAll());
			List<Car> listCarFree = carService.getListCarFree();
			System.out.println(listCarFree.size());
			if(modelDriver.getListcar() != null){
				for(Car c : modelDriver.getListcar()){
					if(listCarFree.parallelStream().anyMatch(cf -> cf.equals(c))){
						listCarFree.remove(c);
					}
				}
			}
			model.addAttribute("ListCarFree", listCarFree);
			showCalendarAndNotify(model);
			return "driverManager";
		}
		
		
		MultipartFile file = modelDriver.getFile();
		if(!file.isEmpty()){
			String location = request.getServletContext().getRealPath("static") + "/img/user/";
			String name = file.getOriginalFilename();
			String namefile = driver.getEmail() + name.substring(name.lastIndexOf("."),name.length());
			userService.uploadfile(file,location,namefile);
		}
		driverService.convertAndChange(modelDriver, driver);
		return "redirect:/list-driver";
	}
	
	@RequestMapping(value="/remove-driver/{email}/", method = RequestMethod.GET)
	public String removeDriverRedirect(HttpSession session, @PathVariable String email){
		session.setAttribute("emailDriver", email);
		return "redirect:/remove-driver";
	}
	@RequestMapping(value="/remove-driver", method = RequestMethod.GET)
	public String removeDriver(Model model, HttpSession session) {
		String email = (String) session.getAttribute("emailDriver");
		Driver driver = driverService.findOne(email);
		if(driver == null){
			throw new DriverNotFoundException();
		}
		if(driver.getListcar() == null || driver.getListcar().size() == 0){
			driverService.remove(driver);
			return "redirect:/list-driver";
		}
		boolean isCarinTimeUse = driver.getListcar().parallelStream()
						   .anyMatch(this::isCarInTimeUse);
		ModelCreateorChangeDriver driverShow = driverService.converDriverToDisplay(driver);
		showInfoDriver(model,driverShow,"change-dirver");
		if(isCarinTimeUse){
			model.addAttribute("messagesEro", "Tài xế đang thực hiện công việc không thể xóa");
			model.addAttribute("MODE", "MODE_FIND_DRIVER");
			model.addAttribute("listdrivers", driverService.findAll());
			showCalendarAndNotify(model);
			return "driverManager";
		}
		
		long timeNow = Calendar.getInstance().getTime().getTime();
		List<Car> listCarOfDriver = driver.getListcar();
		
		listCarOfDriver.parallelStream().filter(c -> c.getListproposal() != null)
				.forEach(c -> notifyEventService.SendNotifyChange(c, "DriverQuitJob", timeNow));
		driver.setSttdriver(sttDriverService.findOne(3));
		driverService.save(driver);
		for(Car c : listCarOfDriver){
			c.setDriver(null);
			c.setSttcar(sttCarService.findOne(4));
			carService.save(c);
		}
		return "redirect:/list-driver";
	}
	@RequestMapping(value="/add-new-stt-driver", method = RequestMethod.POST)
	@ResponseBody
	public String addNewStt(@ModelAttribute("SttDriver") SttDriver stt, BindingResult result) {
		System.out.println(stt.getName());
		sttDriverValidator.validate(stt, result);
		if(result.hasErrors()){
			return "errors";
		}
		sttDriverService.save(stt);
        return "<option value='"+ stt.getSttdriverID() + "'>"+ stt.getName() +"</option>";
	}
	private void showCalendarAndNotify(Model model){
		List<ModelShowNotify> listNotify = notifyEventService.getListNotifyNewest(GetUser());
		if(listNotify != null && listNotify.size() < 5)
			model.addAttribute("listNotify",listNotify);
		else
			model.addAttribute("listNotify", notifyEventService.getListNotifyNewest(GetUser()).subList(0, 5));
		model.addAttribute("calendar", calendarService.createCalendar(null, null));
	}

	private UserDetails getUserLogin() {
		return (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
	}
	
	private User GetUser(){
		return userService.findOne(getUserLogin().getUsername());
	}
	private void showInfoDriver(Model model, ModelCreateorChangeDriver driver, String type){
		List<Car> listCarFree = carService.getListCarFree();
		model.addAttribute("Driver", driver);
		model.addAttribute("Car", new Car());
		model.addAttribute("ListCarFree", listCarFree);
		model.addAttribute("typeForm", "/"+type);
		model.addAttribute("listSttDriver", sttDriverService.findAll());
	}
	private boolean isCarInTimeUse(Car c){
		if(c.getListproposal() != null)
			return c.getListproposal().parallelStream()
			.anyMatch(p -> p.getStt().getSttproposalID() == 1 && proposalService.isInTimeUse(p));
		return false;
	}
}
