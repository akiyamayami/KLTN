package com.tlcn.validator;


import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.ValidationUtils;
import org.springframework.validation.Validator;

import com.tlcn.model.Car;

@Component
public class CarValidator implements Validator{

	@Override
	public boolean supports(Class<?> clazz) {
		// TODO Auto-generated method stub
		return Car.class.equals(clazz);
	}

	@Override
	public void validate(Object target, Errors errors) {
		ValidationUtils.rejectIfEmptyOrWhitespace(errors, "licenseplate", "NotEmpty.Car.licenseplate");
		ValidationUtils.rejectIfEmptyOrWhitespace(errors, "type", "NotEmpty.Car.type");
		ValidationUtils.rejectIfEmptyOrWhitespace(errors, "seats", "NotEmpty.Car.seats");
	}

}
