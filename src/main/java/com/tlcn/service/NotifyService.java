package com.tlcn.service;

import java.util.List;
import java.util.Locale;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.core.env.Environment;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import com.tlcn.model.User;
import com.tlcn.runnable.SendEmail;

@Service
public class NotifyService {
	
	@Autowired
    private JavaMailSender mailSender;
	
	private SendEmail sendEmail;
	
	@Autowired
    private Environment env;
	
	@Autowired
	private MessageSource messages;
	
	
	public NotifyService(){
		super();
	}
	
	public void SendMailSTMP(List<User> listuser , String message, String header){
		Thread nThread = new Thread(new SendEmail(listuser,message,header));
		nThread.start();
	}
	
	public SimpleMailMessage constructResetTokenEmail(HttpServletRequest request, Locale locale, String token, User user) {
        String url = getAppUrl(request) + "/change-password?email=" + user.getEmail() + "&token=" + token;
        String message = messages.getMessage("message.resetPassword", null, locale);
        return constructEmail("Reset Password", message + " \r\n" + url, user);
    }
	public String  genarateMessageResetTokenEmail(HttpServletRequest request, Locale locale, String token, User user) {
        String url = getAppUrl(request) + "/change-password?email=" + user.getEmail() + "&token=" + token;
        String message = messages.getMessage("message.resetPassword", null, locale);
        return  message + " \r\n" + url;
    }
	public String  genaratepassword(HttpServletRequest request, Locale locale, String password, User user) {
		String url = getAppUrl(request) + "/login";
        String message = "This is your password : " + password +", please login and change it.";
        return  message + " \r\n" + url;
    }
	public SimpleMailMessage constructNewpassword(HttpServletRequest request, Locale locale,User user, String password) {
        String url = getAppUrl(request) + "/login";
        String message = "This is your password : " + password +", please login and change it.";
        return constructEmail("Reset Password", message + " \r\n" + url, user);
    }
	
	public SimpleMailMessage constructEmail(String subject, String body, User user) {
        final SimpleMailMessage email = new SimpleMailMessage();
        email.setSubject(subject);
        email.setText(body);
        email.setTo(user.getEmail());
        email.setFrom(env.getProperty("support.email"));
        return email;
    }
	
	public String getAppUrl(HttpServletRequest request) {
        return "http://" + request.getServerName() + ":" + request.getServerPort() + request.getContextPath();
    }
}
