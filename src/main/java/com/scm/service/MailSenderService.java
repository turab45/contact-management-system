package com.scm.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
public class MailSenderService {
	
	@Autowired
	private JavaMailSender javaMailSender;
	
	
	public void sendSimpleEmail(String toEmail, String subject, String body) {
		
		SimpleMailMessage message = new SimpleMailMessage();
		message.setFrom("turabbajeer202@gmail.com");
		message.setTo(toEmail);
		message.setSubject(subject);
		message.setText(body);
		
		javaMailSender.send(message);
		
		System.out.println("mail sent ....");
		
	}

}
