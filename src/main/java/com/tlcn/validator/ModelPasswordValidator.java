package com.tlcn.validator;

import java.util.Arrays;

import org.passay.DigitCharacterRule;
import org.passay.LengthRule;
import org.passay.PasswordData;
import org.passay.PasswordValidator;
import org.passay.RuleResult;
import org.passay.UppercaseCharacterRule;
import org.passay.WhitespaceRule;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.ValidationUtils;
import org.springframework.validation.Validator;

import com.tlcn.dto.ModelPassword;

@Component
public class ModelPasswordValidator implements Validator{

	@Override
	public boolean supports(Class<?> clazz) {
		// TODO Auto-generated method stub
		return ModelPassword.class.equals(clazz);
	}

	@Override
	public void validate(Object target, Errors errors) {
		ModelPassword password = (ModelPassword) target;
		ValidationUtils.rejectIfEmptyOrWhitespace(errors, "password", "NotEmpty.Password.password");
		ValidationUtils.rejectIfEmptyOrWhitespace(errors, "passwordconfirm", "NotEmpty.Password.passwordconfirm");
		if(!validatePasswod(password.getPassword())){
			errors.rejectValue("password", "Invalid.password");
		}
		if(!validatePasswod(password.getPasswordconfirm())){
			errors.rejectValue("passwordconfirm", "Invalid.Password");
		}
		if(!password.getPassword().equals(password.getPasswordconfirm())){
			errors.rejectValue("passwordconfirm", "NotMatch.Password");
		}
		
		
	}
	
	public boolean validatePasswod(String password){
		PasswordValidator validator = new PasswordValidator(Arrays.asList(
        		new LengthRule(8, 30), 
        		new UppercaseCharacterRule(1), 
        		new DigitCharacterRule(1), 
        		new WhitespaceRule()));
		RuleResult result = validator.validate(new PasswordData(password));
		if (result.isValid()) {
            return true;
        }
		return false;
	}

}
