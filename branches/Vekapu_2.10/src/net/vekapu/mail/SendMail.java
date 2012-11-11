///////////////////////////////////////////////////////////////////////////////
//
// Filename: SendMail.java
//
// Author:   Janne Ilonen
// Project:  Vekapu
//
// Purpose:  Sending email.
//
// (c) Copyright J.Ilonen, 2003 =>
//
// Used this site as model:
// http://www.javacommerce.com/articles/sendingmail.htm
// http://stackoverflow.com/questions/46663/how-do-you-send-email-from-a-java-app-using-gmail
// http://www.javaworld.com/javatips/jw-javatip115.html
//
// Needs: activation.jar && mail.jar
//
// $Id$
//
///////////////////////////////////////////////////////////////////////////////
//
// This program is free software; you can redistribute it and/or modify
// it under the terms of the GNU General Public License as published by
// the Free Software Foundation; either version 2 of the License, or
// (at your option) any later version.
//
// This program is distributed in the hope that it will be useful,
// but WITHOUT ANY WARRANTY; without even the implied warranty of
// MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
// GNU General Public License for more details at gnu.org.
//
///////////////////////////////////////////////////////////////////////////////

package net.vekapu.mail;

import java.security.Security;
import java.util.Properties;

import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

import org.apache.log4j.Logger;

import net.vekapu.SettingsVO;

/**
 * Sending EMail. This class needs: activation.jar && mail.jar
 * 
 */
public class SendMail {
	private static Logger logger = Logger.getLogger(Messenger.class);
	private String mailHost;

	/**
	 * 
	 */
	public SendMail() {
		this("Mail");
	}

	/**
	 * 
	 */
	public SendMail(String mailHostName) {
		mailHost = mailHostName;
	}

	/**
	 * 
	 */
	public void postMail(String recipients, String subject, String message,
			String from) throws MessagingException {
		postMail(recipients, subject, message, from, from);
	}

	/**
	 * 
	 */
	public void postMail(String recipients, String subject, String message,
			String from, String replyTo) throws MessagingException {
		String[] apu = { recipients };
		postMail(apu, subject, message, from, replyTo);
	}

	/**
	 * 
	 */
	public void postMail(String recipients[], String subject, String message,
			String from) throws MessagingException {
		postMail(recipients, subject, message, from, from);
	}

	/**
	 * Here we do all the work for mailing.
	 */
	public void postMail(String recipients[], String subject, String message,
			String from, String replyTo) throws MessagingException {
				
		// Set the host smtp address
		Properties props = new Properties();
		props.put("mail.smtp.host", mailHost);

		// create some properties and get the default Session
		Session session = Session.getDefaultInstance(props, null);

		// create a message
		Message msg = new MimeMessage(session);

		// set the from and to address
		InternetAddress addressFrom = new InternetAddress(from);
		msg.setFrom(addressFrom);

		InternetAddress replyToList[] = { new InternetAddress(replyTo) };
		msg.setReplyTo(replyToList);

		InternetAddress[] addressTo = new InternetAddress[recipients.length];
		for (int i = 0; i < recipients.length; i++) {
			addressTo[i] = new InternetAddress(recipients[i]);
		}
		msg.setRecipients(Message.RecipientType.TO, addressTo);

		// Optional: You can also set your custom headers in the Email if you
		// Avoiding autorepley.
		msg.addHeader("Precedence", "bulk");

		// Setting the Subject and Content Type
		msg.setSubject(subject);
		msg.setContent(message, "text/plain");
		
		Transport.send(msg);
	}
	
	public void postAuthMail(String recipients[], String subject, String text,
			final SettingsVO settingsVO) throws MessagingException {
		
		Security.addProvider(new com.sun.net.ssl.internal.ssl.Provider());
		 
		// Set the host smtp address
		Properties props = new Properties();
		props.put("mail.transport.protocol", "smtp");        
	    props.put("mail.smtp.starttls.enable", "true"); // added this line
	    props.put("mail.smtp.host", settingsVO.getMailServer());
	    props.put("mail.smtp.user", settingsVO.getFrom());
	    props.put("mail.smtp.password", settingsVO.getMailPassWord());
	    props.put("mail.smtp.port", settingsVO.getMailPort());
	    props.put("mail.smtp.socketFactory.port", settingsVO.getMailPort());
	    props.put("mail.smtp.auth", "true");
	    props.put("mail.smtp.starttls.enable","true");
	    props.put("mail.smtp.starttls.required", "true");
	    props.put("mail.smtp.socketFactory.port", settingsVO.getMailPort());
	    props.put("mail.smtp.socketFactory.class", "javax.net.ssl.SSLSocketFactory");   
        props.put("mail.smtp.socketFactory.fallback", "false"); 
        props.setProperty("mail.smtp.quitwait", "false");
	    
        logger.debug("props : " + props.toString());
        logger.debug("System.getProperties() : " + System.getProperties().toString());
        
		Session session = Session.getDefaultInstance(props, new javax.mail.Authenticator() {
 
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(settingsVO.getFrom(), settingsVO.getMailPassWord());
            }
        });
		

		// create a message
		Message msg = new MimeMessage(session);

		// set the from and to address
		InternetAddress addressFrom = new InternetAddress(settingsVO.getFrom());
		msg.setFrom(addressFrom);

		InternetAddress replyToList[] = { new InternetAddress(settingsVO.getFrom()) };
		msg.setReplyTo(replyToList);

		InternetAddress[] addressTo = new InternetAddress[recipients.length];
		for (int i = 0; i < recipients.length; i++) {
			addressTo[i] = new InternetAddress(recipients[i]);
		}
		msg.setRecipients(Message.RecipientType.TO, addressTo);

		// Optional: You can also set your custom headers in the Email if you
		// Avoiding auto reply.
		msg.addHeader("Precedence", "bulk");

		// Setting the Subject and Content Type
		msg.setSubject(subject);
		msg.setContent(text, "text/plain");
		
		
		Transport.send(msg);
		/*
		Transport transport = session.getTransport("smtps");
	    transport.connect(settingsVO.getMailServer(), settingsVO.getFrom(), settingsVO.getPassWord());
	    transport.sendMessage(msg, msg.getAllRecipients());
	    transport.close();
	*/
		
	}	
}
