package com.tlcn.validator;

import java.util.Calendar;

import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.ValidationUtils;
import org.springframework.validation.Validator;
import org.springframework.web.multipart.MultipartFile;

import com.tlcn.dto.ModelUser;

@Component
public class EditProfileValidator implements Validator{

	@Override
	public boolean supports(Class<?> clazz) {
		// TODO Auto-generated method stub
		return ModelUser.class.equals(clazz);
	}

	@Override
	public void validate(Object target, Errors errors) {
		ModelUser user = (ModelUser) target;
		ValidationUtils.rejectIfEmptyOrWhitespace(errors, "name", "NotEmpty.User.name");
		ValidationUtils.rejectIfEmptyOrWhitespace(errors, "phone", "NotEmpty.User.phone");
		ValidationUtils.rejectIfEmptyOrWhitespace(errors, "birthday", "NotEmpty.User.birthday");
		ValidationUtils.rejectIfEmptyOrWhitespace(errors, "roleID", "NotEmpty.User.roleID");
		
		if(user.getBirthday() != null){
			if(Calendar.getInstance().getTime().getTime() < user.getBirthday().getTime())
				ValidationUtils.rejectIfEmptyOrWhitespace(errors, "birthday", "greaterThanToDay.Driver.birthday");
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

}
