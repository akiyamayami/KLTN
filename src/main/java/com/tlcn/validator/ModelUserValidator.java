package com.tlcn.validator;

import java.util.Calendar;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.ValidationUtils;
import org.springframework.validation.Validator;
import org.springframework.web.multipart.MultipartFile;

import com.tlcn.dto.ModelUser;
import com.tlcn.service.UserService;

@Component
public class ModelUserValidator implements Validator{
	@Autowired
	private UserService userService;
	
	
	private Pattern pattern;
	private Matcher matcher;
	private String EMAIL_PATTERN = "^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@" + "[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$";
	@Override
	public boolean supports(Class<?> clazz) {
		return ModelUser.class.equals(clazz);
	}

	@Override
	public void validate(Object target, Errors errors) {
		ModelUser user = (ModelUser) target;
		
		ValidationUtils.rejectIfEmptyOrWhitespace(errors, "email", "NotEmpty.User.email");
		ValidationUtils.rejectIfEmptyOrWhitespace(errors, "name", "NotEmpty.User.name");
		ValidationUtils.rejectIfEmptyOrWhitespace(errors, "phone", "NotEmpty.User.phone");
		ValidationUtils.rejectIfEmptyOrWhitespace(errors, "birthday", "NotEmpty.User.birthday");
		ValidationUtils.rejectIfEmptyOrWhitespace(errors, "roleID", "NotEmpty.User.roleID");
		if(user.getEmail() != null && user.getEmail() != "" && !validateEmail(user.getEmail())){
			errors.rejectValue("email", "WrongFormat.User.email");
		}
		if(user.getBirthday() != null){
			if(Calendar.getInstance().getTime().getTime() < user.getBirthday().getTime())
				ValidationUtils.rejectIfEmptyOrWhitespace(errors, "birthday", "greaterThanToDay.Driver.birthday");
		}
		boolean userExist = userService.findAll().parallelStream().filter(u -> u.getEmail().equals(user.getEmail())).findFirst().isPresent();
		if(userExist){
			errors.rejectValue("email", "EmailExist.User.email");
		}
		MultipartFile file = user.getFile();
		if(file != null)
		{
			String name = file.getOriginalFilename();
			String ext = null;
			ext = name.substring(name.lastIndexOf(".")+1,name.length()).toLowerCase();
			System.out.println(ext);
			if(!ext.equals("") && (ext.equals(name) || !ext.equals("jpg")))
				errors.rejectValue("file", "InvalidExt.Driver.file");
			else if(file.getSize() > 1024000){
				errors.rejectValue("file", "BigSize.Driver.file");
			}
		}
		
	}
	
	private boolean validateEmail(String email) {
        pattern = Pattern.compile(EMAIL_PATTERN);
        matcher = pattern.matcher(email);
        return matcher.matches();
    }

}
