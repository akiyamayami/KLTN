package com.tlcn.validator;

import com.tlcn.dto.ModelCreateorChangeProposal;
import com.tlcn.model.Car;
import com.tlcn.model.Proposal;
import com.tlcn.service.CarService;
import com.tlcn.service.ProposalService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.ValidationUtils;
import org.springframework.validation.Validator;

import java.util.Calendar;

@Component
public class ProposalValidator implements Validator{
    @Autowired
	private ProposalService propsosalService;
    @Autowired
	private CarService carService;


    @Override
	public boolean supports(Class<?> arg0) {
		return ModelCreateorChangeProposal.class.equals(arg0);	
	}

	@Override
	public void validate(Object target, Errors errors) {
		System.out.println("check validator");
		ModelCreateorChangeProposal x = (ModelCreateorChangeProposal) target;
		Calendar now = Calendar.getInstance();
		long startDate = 0;
		long endDate = 0;
		Proposal proposal = propsosalService.findOne(x.getProposalID());
		
		if(x.getUsefromdate() != null && x.getUsetodate() != null && x.getUsefromtime() != null && x.getUsetotime() != null)
		{
			startDate = carService.getDate(x.getUsefromdate(),x.getUsefromtime());
			long today = System.currentTimeMillis();
			endDate = carService.getDate(x.getUsetodate(),x.getUsetotime());
			boolean startDateBiggerThanToday = startDate > today;
			boolean endDateBiggerThanStartDate = endDate > startDate;
			if(!startDateBiggerThanToday || !endDateBiggerThanStartDate)
				errors.rejectValue("usefromdate", "WrongDate.Proposal.usedate");
			if(x.getPickuptime() != null)
				if(x.getPickuptime().getTime() > x.getUsefromtime().getTime())
					errors.rejectValue("pickuptime", "greaterThanTimeUse.Proposal.pickuptime");
		}
		Car car = carService.findOne(x.getCarID());
		boolean isCarNotUsedYet = carService.findListCarAvailableInTime(startDate, endDate).parallelStream()
				.anyMatch(c -> c.getCarID() == car.getCarID());
		if(proposal != null){
			if (!isCarNotUsedYet && proposal.getCar().getCarID() != car.getCarID()) {
				errors.rejectValue("carID", "CarAlreadyUsed.Proposal.carID");
			}
		}
		else{
			if (!isCarNotUsedYet) {
				errors.rejectValue("carID", "CarAlreadyUsed.Proposal.carID");
			}
		}
		
		String name = x.getFile().getOriginalFilename();
		String ext;
		if(x.getFile().getSize() > 0)
		{
			ext = name.substring(name.lastIndexOf(".")+1,name.length()).toLowerCase();
			System.out.println(ext);
			System.out.println(name);
			System.out.println(x.getFile().getSize());
			if(ext.equals(name) || !ext.equals("pdf"))
				errors.rejectValue("file", "InvalidExt.Proposal.file");
			else if(x.getFile().getSize() > 10240000){
				errors.rejectValue("file", "BigSize.Proposal.file");
			}
		}
		System.out.println(errors.toString());
		ValidationUtils.rejectIfEmptyOrWhitespace(errors, "name", "NotEmpty.Proposal.name");
		ValidationUtils.rejectIfEmptyOrWhitespace(errors, "detail", "NotEmpty.Proposal.detail");
		ValidationUtils.rejectIfEmptyOrWhitespace(errors, "carID", "NotEmpty.Proposal.carID");
		ValidationUtils.rejectIfEmptyOrWhitespace(errors, "usefromtime", "NotEmpty.Proposal.usefromtime");
		ValidationUtils.rejectIfEmptyOrWhitespace(errors, "usetodate", "NotEmpty.Proposal.usetotime");
		ValidationUtils.rejectIfEmptyOrWhitespace(errors, "usefromdate", "NotEmpty.Proposal.usefromdate");
		ValidationUtils.rejectIfEmptyOrWhitespace(errors, "usetodate", "NotEmpty.Proposal.usefromdate");
		ValidationUtils.rejectIfEmptyOrWhitespace(errors, "destination", "NotEmpty.Proposal.destination");
		ValidationUtils.rejectIfEmptyOrWhitespace(errors, "pickuplocation", "NotEmpty.Proposal.pickuplocation");
		ValidationUtils.rejectIfEmptyOrWhitespace(errors, "pickuptime", "NotEmpty.Proposal.pickuptime");
	}

}
