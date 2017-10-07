package com.tlcn.controller;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Calendar;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartException;
import org.springframework.web.multipart.MultipartFile;

import com.tlcn.dto.ModelCalendar;
import com.tlcn.dto.ModelCreateorChangeDriver;
import com.tlcn.dto.ModelShowNotify;
import com.tlcn.error.DriverNotFoundException;
import com.tlcn.model.Car;
import com.tlcn.model.Driver;
import com.tlcn.model.SttDriver;
import com.tlcn.model.User;
import com.tlcn.service.CarService;
import com.tlcn.service.DriverService;
import com.tlcn.service.NotifyEventService;
import com.tlcn.service.ProposalService;
import com.tlcn.service.SttCarService;
import com.tlcn.service.SttDriverService;
import com.tlcn.service.UserService;
import com.tlcn.validator.DriverValidator;
import com.tlcn.validator.SttDriverValidator;

@Controller
public class DriverController {
	@Autowired
	private CarService carService;
	@Autowired
	private DriverValidator driverValidator;
	@Autowired
	private DriverService driverService;
	@Autowired
	private SttDriverService sttDriverService;
	@Autowired
	private ProposalService proposalService;
	@Autowired
	private NotifyEventService notifyEventService;
	@Autowired
	private SttDriverValidator sttDriverValidator; 
	@Autowired 
	private SttCarService sttCarService;
	
	@Autowired
	private UserService userService;
	
	
	public DriverController() {
		super();
	}
	@RequestMapping(value="/list-driver", method = RequestMethod.GET)
	public String listDriver(Model model)throws Exception{
		model.addAttribute("MODE", "MODE_FIND_DRIVER");
		model.addAttribute("listdrivers", driverService.findAll());
		showCalendarAndNotify(model,null,null);
		return "driverManager";
	}
	//
	@RequestMapping(value="/create-driver", method = RequestMethod.GET)
	public String createDriver(Model model)throws Exception{
		model.addAttribute("MODE", "MODE_CREATE_DRIVER");
		showCalendarAndNotify(model,null,null);
		showInfoDriver(model,new ModelCreateorChangeDriver(),"create-driver");
		return "driverManager";
	}
	
	@RequestMapping(value="/create-driver", method = RequestMethod.POST)
	public String createDriverPOST(Model model,@Valid @ModelAttribute("Driver") ModelCreateorChangeDriver driver, 
			BindingResult result, HttpServletRequest request) throws MultipartException, IOException{
		driverValidator.validate(driver, result);
		if(result.hasErrors()){
			model.addAttribute("MODE", "MODE_CREATE_DRIVER");
			model.addAttribute("Driver", driver);
			model.addAttribute("Car", new Car());
			model.addAttribute("typeForm", "/create-driver");
			showCalendarAndNotify(model,null,null);
			List<Car> listCarFree = carService.getListCarFree();
			System.out.println(listCarFree.size());
			if(driver.getListcar() != null){
				for(Car c : driver.getListcar()){
					if(c.getCarID() != 0  && listCarFree.parallelStream().filter(cf -> cf.equals(c)).findFirst().isPresent()){
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
		if(file != null){
			String location = request.getServletContext().getRealPath("static") + "/img/user/";
			String name = file.getOriginalFilename();
			String namefile = driver.getEmail() + name.substring(name.lastIndexOf("."),name.length());
			System.out.println(namefile);
			uploadfile(file,location,namefile);
		}
		driverService.convertAndSave(driver);
		
		return "redirect:/list-driver";
	}
	@RequestMapping(value="/change-driver/{email}/", method = RequestMethod.GET)
	public String changeDriverForward(Model model,@PathVariable String email, HttpSession session)throws Exception{
		session.setAttribute("emaildriver", email);
		return "redirect:/change-driver";
	}
	
	
	@RequestMapping(value="/change-driver", method = RequestMethod.GET)
	public String changeDriver(Model model, HttpSession session)throws Exception{
		model.addAttribute("MODE", "MODE_CHANGE_DRIVER");
		String emaildriver = session.getAttribute("emaildriver").toString();
		System.out.println(emaildriver);
		showCalendarAndNotify(model,null,null);
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
			, HttpServletRequest request) throws MultipartException, IOException, Exception{
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
					.filter(c -> isCarInTimeUse(c))
					.findFirst().isPresent());
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
					if(listCarFree.parallelStream().filter(cf -> cf.equals(c)).findFirst().isPresent()){
						listCarFree.remove(c);
					}
				}
			}
			model.addAttribute("ListCarFree", listCarFree);
			showCalendarAndNotify(model,null,null);
			return "driverManager";
		}
		
		
		MultipartFile file = modelDriver.getFile();
		if(file != null && file.getSize() > 0){
			String location = request.getServletContext().getRealPath("static") + "/img/user/";
			String name = file.getOriginalFilename();
			String namefile = driver.getEmail() + name.substring(name.lastIndexOf("."),name.length());
			uploadfile(file,location,namefile);
		}
		driverService.convertAndChange(modelDriver, driver);
		return "redirect:/list-driver";
	}
	
	@RequestMapping(value="/remove-driver/{email}/", method = RequestMethod.GET)
	public String removeDriverRedirect(Model model, HttpSession session, @PathVariable String email){
		session.setAttribute("emailDriver", email);
		return "redirect:/remove-driver";
	}
	@RequestMapping(value="/remove-driver", method = RequestMethod.GET)
	public String removeDriver(Model model, HttpSession session) throws Exception{
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
						   .filter(c -> isCarInTimeUse(c))
						   .findFirst().isPresent();
		ModelCreateorChangeDriver driverShow = driverService.converDriverToDisplay(driver);
		showInfoDriver(model,driverShow,"change-dirver");
		if(isCarinTimeUse){
			model.addAttribute("messagesEro", "Tài xế đang thực hiện công việc không thể xóa");
			model.addAttribute("MODE", "MODE_FIND_DRIVER");
			model.addAttribute("listdrivers", driverService.findAll());
			showCalendarAndNotify(model,null,null);
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
	public String addNewStt(@ModelAttribute("SttDriver") SttDriver stt, BindingResult result) throws Exception{
		System.out.println(stt.getName());
		sttDriverValidator.validate(stt, result);
		if(result.hasErrors()){
			return "errors";
		}
		sttDriverService.save(stt);
		String html = "<option value='"+ stt.getSttdriverID() + "'>"+ stt.getName() +"</option>";
		return html;
	}
	public void showCalendarAndNotify(Model model, String month, String year){
		List<ModelShowNotify> listNotify = notifyEventService.getListNotifyNewest(GetUser());
		if(listNotify != null && listNotify.size() < 5)
			model.addAttribute("listNotify",listNotify);
		else
			model.addAttribute("listNotify", notifyEventService.getListNotifyNewest(GetUser()).subList(0, 5));
		model.addAttribute("calendar", createCalendar(month,year));
	}
	public String createCalendar(String month, String year){
		System.out.println(month+"xx" + year);
		Calendar now = Calendar.getInstance();
		int y,m;
		if(month == null || year == null)
		{
			m = now.get(Calendar.MONTH);
			y = now.get(Calendar.YEAR);
			now.set(y, m, 1);
		}else{
			now.clear();
			y = Integer.parseInt(year);
			m = Integer.parseInt(month);
			now.set(y, m, 1);
		}
		String html = new ModelCalendar().createCalendar(y, m, now);
		return html;
	}
	public UserDetails getUserLogin() {
		return (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
	}
	
	public User GetUser(){
		return userService.findOne(getUserLogin().getUsername());
	}
	public void showInfoDriver(Model model, ModelCreateorChangeDriver driver, String type){
		List<Car> listCarFree = carService.getListCarFree();
		model.addAttribute("Driver", driver);
		model.addAttribute("Car", new Car());
		model.addAttribute("ListCarFree", listCarFree);
		model.addAttribute("typeForm", "/"+type);
		model.addAttribute("listSttDriver", sttDriverService.findAll());
	}
	public boolean uploadfile(MultipartFile file, String localtion, String namefile){
		try {
			byte[] bytes;
			bytes = file.getBytes();
			File serverFile = new File(localtion + namefile);
			BufferedOutputStream stream = new BufferedOutputStream(new FileOutputStream(serverFile));
			stream.write(bytes);
			stream.close();
			return true;
		} catch (IOException e) {
			e.getMessage();
			return false;
		}
	}
	public boolean isCarInTimeUse(Car c){
		if(c.getListproposal() != null)
			return c.getListproposal().parallelStream()
			.filter(p -> p.getStt().getSttproposalID() == 1 && proposalService.isInTimeUse(p))
			.findFirst().isPresent();
		return false;
	}
}
