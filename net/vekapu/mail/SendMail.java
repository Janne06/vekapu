///////////////////////////////////////////////////////////////////////////////
//
// Filename: SendMail.java
//
// Author:   Janne Ilonen
// Project:  Vekapu
//
// Purpose:  Sending email.
//
// (c) Copyright J.Ilonen, 2003-2005
//
// Used this site as modell:
// http://www.javacommerce.com/articles/sendingmail.htm
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

import java.util.Properties;

import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

/**
 * Sendin EMail. This class needs: activation.jar && mail.jar
 * 
 */
public class SendMail {
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
		boolean debug = false;
		if (recipients.length < 1)
			return;

		// Set the host smtp address
		Properties props = new Properties();
		props.put("mail.smtp.host", mailHost);

		// create some properties and get the default Session
		Session session = Session.getDefaultInstance(props, null);
		session.setDebug(debug);

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
		// Want
		// Like this:
		// Avoiding autorepley.
		msg.addHeader("Precedence", "bulk");

		// Setting the Subject and Content Type
		msg.setSubject(subject);
		msg.setContent(message, "text/plain");
		Transport.send(msg);
	}
}
