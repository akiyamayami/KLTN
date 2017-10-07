package com.tlcn.runnable;

import java.util.List;
import java.util.Properties;

import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

import com.tlcn.model.User;

public class SendEmail implements Runnable{
	private List<User> listuser;
	private String message;
	private String header;
	
	public SendEmail() {
		super();
	}
	
	public SendEmail(List<User> listuser, String message, String header) {
		super();
		this.listuser = listuser;
		this.message = message;
		this.header = header;
	}

	@Override
	public void run() {
		for(User user : listuser) {
			sendmail(user.getEmail(),message,header);
		}
	}
	public void sendmail(String To,String Text, String Header)
	{
		try {
			Properties props = new Properties();
			props.put("mail.smtp.host", "smtp.gmail.com");
			props.put("mail.smtp.socketFactory.port", "465");
			props.put("mail.smtp.socketFactory.class",
					"javax.net.ssl.SSLSocketFactory");
			props.put("mail.smtp.auth", "true");
			props.put("mail.smtp.port", "465");
		
			Session session = Session.getDefaultInstance(props,
				new javax.mail.Authenticator() {
					protected PasswordAuthentication getPasswordAuthentication() {
						return new PasswordAuthentication("akiyamayami2@gmail.com","1900561558");
					}
				});
			Message message = new MimeMessage(session);
			message.setFrom(new InternetAddress("akiyamayami2@gmail.com"));
			message.setRecipients(Message.RecipientType.TO,
					InternetAddress.parse(To));
			message.setSubject(Header);
			message.setText(Text);
	
			Transport.send(message);
	
	
		} catch (MessagingException e) {
			throw new RuntimeException(e);
		}
	}

}
