package com.tlcn.validator;

import java.util.Calendar;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.ValidationUtils;
import org.springframework.validation.Validator;
import org.springframework.web.multipart.MultipartFile;

import com.tlcn.dto.ModelCreateorChangeDriver;
import com.tlcn.model.Driver;
import com.tlcn.service.UserService;

@Component
public class DriverValidator implements Validator{

	@Autowired
	private UserService userService;
	@Override
	public boolean supports(Class<?> clazz) {
		// TODO Auto-generated method stub
		return Driver.class.equals(clazz);
	}

	@Override
	public void validate(Object target, Errors errors) {
		ModelCreateorChangeDriver driver = (ModelCreateorChangeDriver) target;
		System.out.println("validator");
		ValidationUtils.rejectIfEmptyOrWhitespace(errors, "name", "NotEmpty.Driver.name");
		ValidationUtils.rejectIfEmptyOrWhitespace(errors, "email", "NotEmpty.Driver.email");
		ValidationUtils.rejectIfEmptyOrWhitespace(errors, "birthday", "NotEmpty.Driver.birthday");
		ValidationUtils.rejectIfEmptyOrWhitespace(errors, "phone", "NotEmpty.Driver.phone");
		ValidationUtils.rejectIfEmptyOrWhitespace(errors, "experience", "NotEmpty.Driver.experience");
		ValidationUtils.rejectIfEmptyOrWhitespace(errors, "license", "NotEmpty.Driver.license");
		ValidationUtils.rejectIfEmptyOrWhitespace(errors, "address", "NotEmpty.Driver.address");
		if(driver.getBirthday() != null){
			if(Calendar.getInstance().getTime().getTime() < driver.getBirthday().getTime())
				ValidationUtils.rejectIfEmptyOrWhitespace(errors, "birthday", "greaterThanToDay.Driver.birthday");
		}
		boolean userExist = userService.findAll().parallelStream().filter(u -> u.getEmail().equals(driver.getEmail())).findFirst().isPresent();
		if(userExist){
			errors.rejectValue("email", "EmailExist.User.email");
		}
		MultipartFile file = driver.getFile();
		if(file != null)
		{
			String name = driver.getFile().getOriginalFilename();
			String ext = null;
			ext = name.substring(name.lastIndexOf(".")+1,name.length()).toLowerCase();
			System.out.println(ext);
			if(!ext.equals("") && (ext.equals(name) || !ext.equals("jpg")))
				errors.rejectValue("file", "InvalidExt.Driver.file");
			else if(driver.getFile().getSize() > 1024000){
				errors.rejectValue("file", "BigSize.Driver.file");
			}
		}
	}
	
}
