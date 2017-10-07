package com.tlcn.service;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.function.Predicate;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.tlcn.dao.CarRepository;
import com.tlcn.dao.ProposalRepository;
import com.tlcn.dto.ModelCarReady;
import com.tlcn.dto.ModelCarRegistered;
import com.tlcn.dto.ModelCreateorChangeCar;
import com.tlcn.error.CarNotFoundException;
import com.tlcn.error.HaveProposalInTimeUseException;
import com.tlcn.model.Car;
import com.tlcn.model.Driver;
import com.tlcn.model.Proposal;

@Service
public class CarService {
	@Autowired
	private CarRepository carRepository;

	@Autowired
	private ProposalService proposalService;
	@Autowired
	private NotifyEventService notifyEventService;
	@Autowired
	private DriverService driverService;
	@Autowired
	private TypeProposalService typeProposalService;
	@Autowired 
	private SttCarService sttCarService;
	
	
	public CarService() {
		super();
	}
	
	public void save(Car car){
		carRepository.save(car);
	}
	public List<Car> getListCarFree(){
		return carRepository.getListCarFree();
	}
	public Car findOne(int carID){
		return carRepository.findOne(carID);
	}
	public List<Car> findAll(){
		List<Car> cars = new ArrayList<>();
		for(Car car :carRepository.findAll()){
			cars.add(car);
		}
		return cars;
	}
	public List<Car> getListCarAvailable(){
		return carRepository.getListCarAvaliable();
	}
	public List<Car> findListCarAvailableInTime(long timeFrom, long timeTo){
		List<Car> listCarAvailable = carRepository.getListCarAvaliable();
		System.out.println("start find car availble in time");
		Set<Car> y = new HashSet<>(listCarAvailable.parallelStream()
				.filter(c -> c.getListproposal() == null || (c.getListproposal() != null
					&& !c.getListproposal().parallelStream()
						.filter(p -> p.getStt().getSttproposalID() == 1
							&& p.getType().getTypeID() != 3
							&& (isBetween(timeFrom,timeTo,getDate(p.getUsefromdate(), p.getUsefromtime()),getDate(p.getUsetodate(), p.getUsetotime()))
							|| isBetween(getDate(p.getUsefromdate(), p.getUsefromtime()),getDate(p.getUsetodate(), p.getUsetotime()),timeFrom,timeTo)))
						.findFirst().isPresent()))
				.collect(Collectors.toList()));
		System.out.println("list car avalible in time");
		for(Car c : y ){
			System.out.println(c.getCarID());
		}
				
				
		/*	
		Set<Car> x = new HashSet<>(listProposal.parallelStream()
				.filter(p -> p.getStt().getSttproposalID() == 1 
					&& p.getType().getTypeID() != 3 
					&& !isBetween(TimeFrom,timeTo,getDate(p.getUsefromdate(), p.getUsefromtime()),getDate(p.getUsetodate(), p.getUsetotime()))
					&& p.getCar().getSttcar().getSttcarID() == 1)
				.map(Proposal::getCar)
				.collect(Collectors.toList()));*/
		List<Car> cars = new ArrayList<>(y);
		return cars;
 	}
	
	public List<Car> findListFilter_Type(String type){
		return carRepository.getListFilter_Type(type);
	}
	public List<Car> findListFilter_Seat(int seats){
		return carRepository.getListFilter_Seat(seats);
	}
	public List<Car> findListFilter_Type_Seat(String type, int seats){
		return carRepository.getListFilter_Type_Seat(type, seats);
	}
	
	public void remove(Car car){
		carRepository.delete(car);
	}

	public List<ModelCarReady> getListCarReady() {
		
		long timeNow = Calendar.getInstance().getTime().getTime();
		List<ModelCarReady> listcarready = new ArrayList<>();
		List<Car> listcaravailable = findAll();
		listcaravailable.parallelStream()
				.filter(c -> c.getListproposal() != null && c.getListproposal().parallelStream()
						.filter(p -> p.getStt().getSttproposalID() == 1 && p.getType().getTypeID() != 3
								&& (getDate(p.getUsefromdate(), p.getUsefromtime()) >= timeNow 
										|| proposalService.isInTimeUse(p)))
						.findFirst().isPresent())
				.forEach(c -> listcarready.add(new ModelCarReady(c.getLicenseplate(),
						c.getListproposal().parallelStream()
								.filter(p -> p.getStt().getSttproposalID() == 1 && p.getType().getTypeID() != 3 
								&& (getDate(p.getUsefromdate(), p.getUsefromtime()) >= timeNow || proposalService.isInTimeUse(p)))
								.collect(Collectors.toList()))));
		/*
		 * List<Proposal> listProposal = proposalService.getListProposalReady();
		 * if (listProposal.isEmpty()) return null; List<ModelCarReady>
		 * listCarReady = new ArrayList<>(); for (Proposal p : listProposal) {
		 * System.out.println(p.getCar().getLicenseplate());
		 * listCarReady.add(new ModelCarReady(p.getCar().getLicenseplate(), p));
		 * }
		 */
		return listcarready;
	}
	public void converAndSave(ModelCreateorChangeCar car){
		Car x = new Car(car.getLicenseplate(),car.getType(),car.getSeats(),
				sttCarService.findOne(1),driverService.findOne(car.getEmailDriver()));
		carRepository.save(x);
	}
	public ModelCreateorChangeCar convertCarToShow(Car car){
		System.out.println(car);
		ModelCreateorChangeCar carShow;
		if(car.getDriver() != null)
			carShow = new ModelCreateorChangeCar(car.getCarID(),car.getLicenseplate(),car.getType(),car.getSeats(),car.getSttcar().getSttcarID(),car.getDriver().getEmail());
		else
			carShow = new ModelCreateorChangeCar(car.getCarID(),car.getLicenseplate(),car.getType(),car.getSeats(),car.getSttcar().getSttcarID(),null);
		return carShow ;
	}
	public void converAndChange(ModelCreateorChangeCar car, Car caradd){
		System.out.println("change-car-6");
		long timeNow = Calendar.getInstance().getTime().getTime();
		if(car.getSttcarID() != 1){
			System.out.println("change-car-7");
			boolean isCarinTimeUse = caradd.getListproposal().parallelStream()
					.filter(p -> p.getStt().getSttproposalID() == 1 && proposalService.isInTimeUse(p))
					.findFirst().isPresent();
			if(isCarinTimeUse){
				throw new HaveProposalInTimeUseException();
			}
		}
		System.out.println("change-car-8");
		caradd.setLicenseplate(car.getLicenseplate());
		caradd.setSeats(car.getSeats());
		caradd.setType(car.getType());
		Driver driver = driverService.findOne(car.getEmailDriver());
		if(caradd.getSttcar().getSttcarID() != car.getSttcarID() && caradd.getListproposal() != null){
			if(caradd.getListproposal().parallelStream().filter(p -> proposalService.isInTimeUse(p)).findFirst().isPresent())
				throw new HaveProposalInTimeUseException();
			if(caradd.getDriver() != null && driver.getEmail().equals(caradd.getDriver().getEmail())){
				switch(car.getSttcarID()){
					case 1:
						caradd.setDriver(driver);
						switch(caradd.getSttcar().getSttcarID()){
							case 2:
								System.out.println("Car is RepairDone");
								notifyEventService.SendNotifyChange(caradd, "RepairDone", timeNow);
								break;
							case 3:
								System.out.println("Car is BackToWork");
								notifyEventService.SendNotifyChange(caradd, "BackToWork", timeNow);
								break;
							case 4:
								System.out.println("Car is DriverBack");
								notifyEventService.SendNotifyChange(caradd, "DriverBack", timeNow);
								break;
						}
						break;
					case 2:
						System.out.println("Car is CarIsRepair");
						notifyEventService.SendNotifyChange(caradd, "CarIsRepair", timeNow);
						caradd.setDriver(driver);
						break;
					case 3:
						System.out.println("Car is RemoveCar");
						notifyEventService.SendNotifyChange(caradd, "RemoveCar", timeNow);
						caradd.setDriver(null);
						// thông báo khi xe không còn hoạt động.
						break;
					default:
						// stt car == 4 không tài xế
						System.out.println("Car is NoDriver");
						notifyEventService.SendNotifyChange(caradd, "NoDriver", timeNow);
						caradd.setDriver(null);
						break;
				}
			}
			else{
				notifyEventService.SendNotifyChange(caradd, "DriverBack", timeNow);
				caradd.setDriver(driver);
			}
			
		}
		else{
			if(caradd.getDriver() != null && driver.getEmail().equals(caradd.getDriver().getEmail()))
			{
				notifyEventService.SendNotifyChange(caradd, "DriverBack", timeNow);
				caradd.setDriver(driver);
			}
		}
		caradd.setSttcar(sttCarService.findOne(car.getSttcarID()));
		System.out.println("change-car-9");
		carRepository.save(caradd);
	}
	public void updateListCarOfDriver(Car caradd, String type){
		Driver driver = caradd.getDriver();
		List<Car> listcar = driver.getListcar();
		switch (type) {
		case "add":
			listcar.add(caradd);
			break;
		default:
			// remove
			listcar.remove(caradd);
			break;
		}
		
		driver.setListcar(listcar);
		driverService.save(driver);
	}
	public List<ModelCarRegistered> getListCarRegistered(){
		long timeNow = Calendar.getInstance().getTime().getTime();
		List<Car> allCar = carRepository.getListCarAvaliable();
		List<ModelCarRegistered> listcarRegister = new ArrayList<>();
		allCar.parallelStream()
			  .filter(c -> c.getListproposal() != null
			  				&& c.getListproposal().parallelStream()
												.filter(p -> p.getStt().getSttproposalID() == 0 
													&& p.getType().getTypeID() != 3
													&& getDate(p.getUsefromdate(),p.getUsefromtime()) > timeNow)
												.findFirst().isPresent())
			  .forEach(c -> listcarRegister.add(new ModelCarRegistered(c.getLicenseplate(), c.getListproposal()
					  .parallelStream().filter(p -> getDate(p.getUsefromdate(), p.getUsefromtime()) >= timeNow)
					  .collect(Collectors.toList()))));
		return listcarRegister;
	}

	public List<Car> getListCarNotRegistered(){
		long timeNow = Calendar.getInstance().getTime().getTime();
		List<Car> listCarNotRegistered = findAll();
		listCarNotRegistered = listCarNotRegistered.parallelStream().filter(c -> c.getListproposal() == null 
				|| !c.getListproposal().parallelStream().filter(p -> p.getType().getTypeID() != 3 
					&& (getDate(p.getUsefromdate(), p.getUsefromtime()) >= timeNow 
							|| proposalService.isInTimeUse(p))).findFirst().isPresent())
				.collect(Collectors.toList());
		return listCarNotRegistered;
	}
	
	public Long getDate(Date date, Date time){
		Calendar Cdate = Calendar.getInstance(),Ctime = Calendar.getInstance(),dateTime = Calendar.getInstance();
		Cdate.setTime(date);
		Ctime.setTime(time);
		dateTime.set(Cdate.get(Calendar.YEAR), Cdate.get(Calendar.MONTH), Cdate.get(Calendar.DATE), 
				Ctime.get(Calendar.HOUR_OF_DAY), Ctime.get(Calendar.MINUTE));
		return dateTime.getTime().getTime();
		
	}
	
	public boolean isBetween(long timeCheckFrom,long timeCheckTo, long timeFrom, long timeTo){
		System.out.println("time check from" + timeCheckFrom);
		System.out.println("time check to " + timeCheckTo);
		System.out.println("time timeFrom" + timeFrom);
		System.out.println("time timeTo" + timeTo);
		System.out.println(timeCheckFrom >= timeFrom && timeCheckFrom <= timeTo);
		System.out.println(timeCheckTo >= timeFrom && timeCheckTo <= timeTo);
		if((timeCheckFrom >= timeFrom && timeCheckFrom <= timeTo) || (timeCheckTo >= timeFrom && timeCheckTo <= timeTo))
			return true;
		return false;
	}
}
