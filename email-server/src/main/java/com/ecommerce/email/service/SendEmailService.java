package com.ecommerce.email.service;

public interface SendEmailService
{
	void sendSimpleMessage(String toEmail, String subject, String content);

	void sendMessageWithAttachment(String toEmail, String subject, String content, String pathToAttachment);
}
