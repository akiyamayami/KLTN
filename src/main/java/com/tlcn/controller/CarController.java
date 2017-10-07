package com.tlcn.controller;

import java.security.Principal;
import java.util.Calendar;
import java.util.HashSet;
import java.util.List;
import java.util.Random;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.tlcn.dto.ModelCalendar;
import com.tlcn.dto.ModelCarReady;
import com.tlcn.dto.ModelCreateorChangeCar;
import com.tlcn.dto.ModelCreateorChangeDriver;
import com.tlcn.dto.ModelFilterCar;
import com.tlcn.dto.ModelShowNotify;
import com.tlcn.error.CarNotFoundException;
import com.tlcn.model.Car;
import com.tlcn.service.CarService;
import com.tlcn.service.DriverService;
import com.tlcn.service.NotifyEventService;
import com.tlcn.service.ProposalService;
import com.tlcn.service.SttCarService;
import com.tlcn.service.UserService;
import com.tlcn.validator.CarValidator;
import com.tlcn.validator.DriverValidator;
import com.tlcn.validator.ModelCarValidator;
import com.tlcn.model.Driver;
import com.tlcn.model.SttCar;
import com.tlcn.model.User;

@Controller
@Component
public class CarController {
	@Autowired
	private CarService carService;

	@Autowired
	private CarValidator carValidator;
	
	@Autowired
	private DriverService driverService;
	
	@Autowired
	private ModelCarValidator modelCarValidator;
	
	@Autowired
	private SttCarService sttCarService;
	
	@Autowired
	private ProposalService proposalService;
	
	@Autowired 
	private NotifyEventService notifyEventService;
	
	@Autowired
	private UserService userService;
	public CarController() {
		super();
	}
	public void addListTypeAndSeat(Model model){
		Set<String>  listtype = new HashSet<>();
		Set<Integer>  listseats = new HashSet<>();
		for(Car c : carService.findAll()){
			listtype.add(c.getType());
			listseats.add(c.getSeats());
		
		}
		model.addAttribute("listtype", listtype);
		model.addAttribute("listseats", listseats);
	}
	
	// page find-car
	@RequestMapping(value="/find-cars", method = RequestMethod.GET)
	public String findCar(Model model) throws Exception{
		showCalendarAndNotify(model, null, null);
		model.addAttribute("MODE", "MODE_FIND_CARS");
		addListTypeAndSeat(model);
		model.addAttribute("listcars", carService.findAll());
		model.addAttribute("filter-car", new ModelFilterCar());
		return "carManager";
	}
	
	// filter car
	@RequestMapping(value="/find-cars", method = RequestMethod.POST)
	public String filterCar(Model model, @ModelAttribute("filter-car") ModelFilterCar filtercar,
			BindingResult result) throws Exception{
		showCalendarAndNotify(model, null, null);
		model.addAttribute("MODE", "MODE_FIND_CARS");
		addListTypeAndSeat(model);
		model.addAttribute("listcars", getListCarFilter(filtercar));
		model.addAttribute("filter-car", filtercar);
		
		return "carManager";
	}
	// page check-stt-car
	@RequestMapping(value="/check-stt-cars", method = RequestMethod.GET)
	public String checkSttCar(Model model) throws Exception{
		showCalendarAndNotify(model, null, null);
		model.addAttribute("MODE", "MODE_CHECK_STT_CARS");
		model.addAttribute("listCarReady", carService.getListCarReady()); 
		model.addAttribute("listCarRegistered", carService.getListCarRegistered()); 
		model.addAttribute("listCarNotRegistered", carService.getListCarNotRegistered()); 
		return "carManager";
	}
	
	// page create car
	@RequestMapping(value="/create-car", method = RequestMethod.GET)
	public String createCar(Model model) throws Exception{
		showCalendarAndNotify(model, null, null);
		model.addAttribute("MODE", "MODE_CREATE_CAR");
		model.addAttribute("typeForm", "/create-car");
		model.addAttribute("listDriver", driverService.findAll());
		model.addAttribute("liststtcar", sttCarService.findAll());
		model.addAttribute("Car", new ModelCreateorChangeCar()); 
		return "carManager";
	}
	
	
	
	
	@RequestMapping(value="/create-car", method = RequestMethod.POST)
	public String createCar(Model model, @ModelAttribute("Car") ModelCreateorChangeCar car, BindingResult result) throws Exception{
		modelCarValidator.validate(car, result);
		if(result.hasErrors()){
			showCalendarAndNotify(model, null, null);
			model.addAttribute("MODE", "MODE_CREATE_CAR");
			model.addAttribute("SttCar", new SttCar());
			model.addAttribute("typeForm", "/create-car");
			model.addAttribute("listDriver", driverService.findAll());
			model.addAttribute("liststtcar", sttCarService.findAll());
			model.addAttribute("Car", car);
			return "carManager";
		}
		carService.converAndSave(car);
		return "redirect:/find-cars";
	}
	
	@RequestMapping(value="/change-car-{carID}", method = RequestMethod.GET)
	public String changeCar(Model model, @PathVariable int carID, HttpSession session) throws Exception{
		Car car = carService.findOne(carID);
		if(car == null){
			throw new CarNotFoundException();
		}
		showCalendarAndNotify(model, null, null);
		session.setAttribute("carID", carID);
		model.addAttribute("MODE", "MODE_CHANGE_CAR");
		model.addAttribute("typeForm", "/change-car");
		model.addAttribute("listDriver", driverService.getListDriverAvailable());
		model.addAttribute("liststtcar", sttCarService.findAll());
		model.addAttribute("Car", carService.convertCarToShow(car)); 
		return "carManager";
	}
	
	@RequestMapping(value="/remove-car-{carID}", method = RequestMethod.GET)
	public String removeCar(Model model,@PathVariable int carID) throws Exception{		
		Car car = carService.findOne(carID);
		if(car == null){
			throw new CarNotFoundException();
		}
		long timeNow = Calendar.getInstance().getTime().getTime();
		System.out.println(car.getListproposal().size());
		if(car.getListproposal() == null || car.getListproposal().size() == 0){
			carService.remove(car);
		}else {
			boolean isCarinTimeUse = car.getListproposal().parallelStream()
					.filter(p -> proposalService.isInTimeUse(p))
					.findFirst().isPresent();
			if(!isCarinTimeUse){
				car.setSttcar(sttCarService.findOne(3));
				car.setDriver(null);
				notifyEventService.SendNotifyChange(car,"RemoveCar",timeNow);
				carService.save(car);
			}else{
				model.addAttribute("message", "Xe đang được sử dụng không thể xóa");
				showCalendarAndNotify(model, null, null);
				model.addAttribute("MODE", "MODE_FIND_CARS");
				addListTypeAndSeat(model);
				model.addAttribute("listcars", carService.findAll());
				model.addAttribute("filter-car", new ModelFilterCar());
				return "carManager";
			}
		}
		return "redirect:/find-cars";
	}
	
	@RequestMapping(value = "/change-car", method = RequestMethod.POST)
	public String changeCarPOST(Model model, @ModelAttribute("Car") ModelCreateorChangeCar car, BindingResult result,
			HttpSession session) {
		modelCarValidator.validate(car, result);
		System.out.println("change-car-1");
		if (result.hasErrors()) {
			System.out.println("change-car-2");
			model.addAttribute("MODE", "MODE_CHANGE_CAR");
			model.addAttribute("typeForm", "/change-car");
			model.addAttribute("listDriver", driverService.getListDriverAvailable());
			model.addAttribute("liststtcar", sttCarService.findAll());
			model.addAttribute("Car", car);
			showCalendarAndNotify(model, null, null);
			return "carManager";
		}
		System.out.println("change-car-3");
		int carID = (int) session.getAttribute("carID");
		Car caradd = carService.findOne(carID);
		if (caradd == null) {
			System.out.println("change-car-4");
			throw new CarNotFoundException();
		}
		System.out.println("change-car-5");
		carService.converAndChange(car, caradd);
		return "redirect:/find-cars";
	}
	
	@RequestMapping(value="/create-car-x", method = RequestMethod.POST)
	@ResponseBody
    public String createCarxPOST(@ModelAttribute("Car") Car car, BindingResult result)  throws Exception{
		carValidator.validate(car, result);
		if(result.hasErrors()){
			return "errors";
		}
		Car c = new Car(car.getLicenseplate(),car.getType(),car.getSeats(),sttCarService.findOne(1));
		carService.save(c);
		Random r = new Random();
		int count = r.nextInt(20 - 10 + 1) + 10;
		String html = "<tr>";
		html += "<td><input id='listcar"+ count +".carID' name='listcar["+ count +"].carID' type='text' readonly='true' value='"
				+ c.getCarID() + "' style='border:none;' size='1'/></td>";
		html += "<td><input id='listcar"+ count +".licenseplate' name='listcar["+ count +"].licenseplate' type='text' readonly='true' value='"
				+ c.getLicenseplate() + "' style='border:none;' size='6'/></td>";
		html += "<td><input id='listcar"+ count +".type' name='listcar["+ count +"].type' type='text' readonly='true' value='"
				+ c.getType() + "' style='border:none;' size='2'/></td>";
		html += "<td><input id='listcar"+ count +".seats' name='listcar["+ count +"].seats' type='text' readonly='true' value='"
				+ c.getSeats() + "' style='border:none;' size='1'/>Chỗ</td>"
				+ "<td><a href='#' class='remove-car'><i class='fa fa-trash fa-lg' aria-hidden='true'></i></a></td></tr>";
        return html;
    }
	public List<Car> getListCarFilter(ModelFilterCar filter){
		int seats = filter.getSeat();
		String type = filter.getType();
		if(seats == -1 && type.equals("all"))
			return carService.findAll();
		if(seats != -1 && !type.equals("all"))
			return carService.findListFilter_Type_Seat(type, seats);
		if(seats != -1)
			return carService.findListFilter_Seat(seats);
		else
			return carService.findListFilter_Type(type);
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
	public void showCalendarAndNotify(Model model, String month, String year){
		List<ModelShowNotify> listNotify = notifyEventService.getListNotifyNewest(GetUser());
		if(listNotify != null && listNotify.size() < 5)
			model.addAttribute("listNotify",listNotify);
		else
			model.addAttribute("listNotify", notifyEventService.getListNotifyNewest(GetUser()).subList(0, 5));
		model.addAttribute("calendar", createCalendar(month,year));
	}
	public UserDetails getUserLogin() {
		return (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
	}
	
	public User GetUser(){
		return userService.findOne(getUserLogin().getUsername());
	}
}
