package com.tlcn.service;

import com.tlcn.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.MessageSource;
import org.springframework.core.env.Environment;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import java.util.Locale;

@Service
public class NotifyService {

	
	@Autowired
    private Environment env;

    @Autowired
	private MessageSource messages;
	
	
	public NotifyService(){
		super();
	}


	public String  genarateMessageResetTokenEmail(HttpServletRequest request, Locale locale, String token, User user) {
        String url = getAppUrl(request) + "/change-password?email=" + user.getEmail() + "&token=" + token;
        String message = messages.getMessage("message.resetPassword", null, locale);
        return  message + " \r\n" + url;
    }
	String  genaratepassword(HttpServletRequest request, Locale locale, String password, User user) {
		String url = getAppUrl(request) + "/login";
        String message = "This is your password : " + password +", please login and change it.";
        return  message + " \r\n" + url;
    }
	
	private SimpleMailMessage constructEmail(String subject, String body, User user) {
        final SimpleMailMessage email = new SimpleMailMessage();
        email.setSubject(subject);
        email.setText(body);
        email.setTo(user.getEmail());
        email.setFrom(env.getProperty("support.email"));
        return email;
    }
	
	private String getAppUrl(HttpServletRequest request) {
        return "http://" + request.getServerName() + ":" + request.getServerPort() + request.getContextPath();
    }
}
