package com.gkp.onlinebookstore.service.impl;

import com.gkp.onlinebookstore.service.EmailService;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class EmailServiceImpl implements EmailService {

	@Value("${spring.mail.username}")
	private String fromEmailId;

	private final JavaMailSender javaMailSender;

	@Override
	public void sendRegistrationEmail(String to, String username, String password) {
		String subject = "Your Account has been created on GKP";

		String body = "Thanks for creating an account on GKP.\r\n" + "\r\n" + "Your username is: " + username + "\r\n"
				+ "Your password is: " + password + "\r\n";

		sendEmail(to, subject, body);

	}

	@Override
	public void sendEmail(String to, String subject, String body) {
		try {
			// Create a MimeMessage for email
			MimeMessage message = javaMailSender.createMimeMessage();
			MimeMessageHelper helper = new MimeMessageHelper(message, true);

			// Set email details
			helper.setTo(to.trim().replaceAll("\\s", ""));
			helper.setSubject(subject.trim());
			helper.setText(body.trim());
			helper.setFrom(fromEmailId.trim());

			// Send the email
			javaMailSender.send(message);
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

}
