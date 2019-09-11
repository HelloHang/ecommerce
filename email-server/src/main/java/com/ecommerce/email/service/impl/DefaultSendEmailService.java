package com.ecommerce.email.service.impl;

import com.ecommerce.email.service.SendEmailService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.FileSystemResource;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import java.io.File;


@Service
public class DefaultSendEmailService implements SendEmailService
{
	private final static Logger LOG = LoggerFactory.getLogger(DefaultSendEmailService.class);
	@Autowired
	private JavaMailSender emailSender;

	@Override
	public void sendSimpleMessage(String toEmail, String subject, String content)
	{
		SimpleMailMessage simpleMailMessage = new SimpleMailMessage();
		simpleMailMessage.setTo(toEmail);
		simpleMailMessage.setSubject(subject);
		simpleMailMessage.setText(content);
		emailSender.send(simpleMailMessage);
	}

	@Override
	public void sendMessageWithAttachment(String toEmail, String subject, String content, String pathToAttachment)
	{
		MimeMessage mimeMessage = emailSender.createMimeMessage();
		try
		{
			MimeMessageHelper mimeMessageHelper = new MimeMessageHelper(mimeMessage, true);
			mimeMessageHelper.setTo(toEmail);
			mimeMessageHelper.setSubject(subject);
			mimeMessageHelper.setText(content);
			FileSystemResource file = new FileSystemResource(new File(pathToAttachment));
			mimeMessageHelper.addAttachment("Invoice", file);
		}
		catch (MessagingException e)
		{
			LOG.error(e.getMessage(), e);
		}
		emailSender.send(mimeMessage);
	}
}
