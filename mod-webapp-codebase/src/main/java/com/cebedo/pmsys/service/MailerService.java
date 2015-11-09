package com.cebedo.pmsys.service;

import org.springframework.mail.MailSender;
import org.springframework.mail.SimpleMailMessage;

/**
 * TODO Make an interface for this, then just implement.
 */
public class MailerService {
    public static final String BEAN_NAME = "mailer";
    private MailSender mailSender;

    public void setMailSender(MailSender mailSender) {
	this.mailSender = mailSender;
    }

    public void sendMail(String from, String to, String subject, String msg) {
	SimpleMailMessage message = new SimpleMailMessage();
	message.setFrom(from);
	message.setTo(to);
	message.setSubject(subject);
	message.setText(msg);
	mailSender.send(message);
    }
}