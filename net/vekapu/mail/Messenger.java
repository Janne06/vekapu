///////////////////////////////////////////////////////////////////////////////
//
// Filename: Messenger.java
//
// Author:   Janne Ilonen
// Project:  Vekapu
//
// Purpose:  Handlin eMail & sms.
//
// (c) Copyright J.Ilonen, 2003-2006
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

import java.util.Iterator;
import java.util.List;

import javax.mail.MessagingException;

import net.vekapu.SettingsVO;
import net.vekapu.VekapuException;
import net.vekapu.util.Constant;
import net.vekapu.util.StoreFile;

import org.apache.log4j.Logger;

public class Messenger {
	private static Logger logger = Logger.getLogger(Messenger.class);

	public Messenger() {
	}

	public static void sendEMail(String otsikko, String group, List to,
			String tulos,  SettingsVO settingsVO) throws VekapuException {
		
		String[] to2 = new String[to.size()];
		
		Iterator it = to.iterator();
		for (int i = 0; i < to.size(); i++) {
			to2[i] = (String) it.next();
			logger.debug(to2[i]);
		}

		sendEMail(otsikko,group, to2,tulos,settingsVO);
	}

	public static void sendEMail(String otsikko, String group, String[] to,
			String tulos,  SettingsVO settingsVO) throws VekapuException {
		String lsOtsikko = Constant.getEmailHeaderPrefix() + " " + group + " "
				+ otsikko;

		sendMail(lsOtsikko, group, to, tulos, settingsVO);

		logger.info("Sähköposti - Porukan '" + group + "' lottotarkistus "
				+ to.length + " henkilölle.");
	}

	public static void sendSMS(String otsikko, String group, List to, SettingsVO settingsVO) throws VekapuException {
		String[] to2 = new String[to.size()];
		
		Iterator it = to.iterator();
		for (int i = 0; i < to.size(); i++) {
			to2[i] = (String) it.next();
			logger.debug(to2[i]);
		}

		sendSMS(otsikko,group,to2, settingsVO);
	}
	
	public static void sendSMS(String otsikko, String group, String[] to, SettingsVO settingsVO) throws VekapuException {
		String viesti = "Lottoporukka '" + group + "' - " + otsikko;

		sendMail(Constant.getEmailHeaderPrefix(), group, to, viesti, settingsVO);

		logger.info("Tekstiviesti - Porukan '" + group + "' lottotarkistus "
				+ to.length + " henkilölle.");
	}

	public static void sendEndMail(String game, String group, List to, SettingsVO settingsVO) throws VekapuException {
	
		String[] to2 = new String[to.size()];
		
		Iterator it = to.iterator();
		for (int i = 0; i < to.size(); i++) {
			to2[i] = (String) it.next();
			logger.debug(to2[i]);
		}

		sendEndMail(game,group,to2, settingsVO);
	}

		
	public static void sendEndMail(String game, String group, String[] to, SettingsVO settingsVO) throws VekapuException {

		String lsOtsikko = Constant.getEmailHeaderPrefix() + game
				+ " kierrosten parhaat tulokset";

		String lsViesti = "Lottoporukan '" + group + "' " + game
				+ " on vanhentunut. Ota yhteyttä porukan vetäjään (" + to[0]
				+ ") varmistaaksesi osaanottosi uusille kierroksille.";

		// Lisätään mukaan tiedot parhaista tuloksista
		lsViesti += Constant.getLineSeparator();
		lsViesti += Constant.getLineSeparator();
		lsViesti += StoreFile.getFile( settingsVO.getGroupDir() 
				+ "/"
				+ group 
				+ Constant.getBestDir() + group
				+ Constant.getBestFileExt());

		String lsViestiVetaja = "Veikkausporukkasi '" + group + "' " + game
				+ " on vanhentunut. Ota yhteyttä Vekapu:n ylläpitäjään ("
				+ settingsVO.getAdmin()
				+ ") uusiaksesi porukkasi rivien tarkistaminen.";

		String lsViestiAdmin = "Porukan '" + group + "' " + game
				+ " on vanhentunut.";

		if (settingsVO.isEmail().booleanValue() && !settingsVO.isTest().booleanValue()) {
			sendMail(lsOtsikko, group, to, lsViesti, settingsVO);

			// Porukan vetäjä
			sendMail(lsOtsikko + " - lottopomo", group, to[0], lsViestiVetaja, settingsVO);

			// Admin
			sendMail(lsOtsikko + " - admin", group, settingsVO.getAdmin(),
					lsViestiAdmin, settingsVO);

			logger.info("Sähköposti - Porukan '" + group + "' " + game
					+ " loppumisesta lähetetty.");

		} else if (settingsVO.isTest().booleanValue()) {
			logger.debug("Loppumisilmoitukset: " + game);
			logger.debug("Otsikko - " + lsOtsikko);
			logger.debug("Viesti  - " + lsViesti);
			logger.debug("Vetäjä  - " + lsViestiVetaja);
			logger.debug("Admin   - " + lsViestiAdmin);
		}

		for (int i = 0; i < to.length; i++) {
			if (i == 0) {
				logger.debug(to[i].toString() + " - Porukan vetäjä");
			} else {
				logger.debug(to[i]);
			}
		}

	}

	public static void sendInfo(String group, List to, List toSms, SettingsVO settingsVO) throws VekapuException {
		
		String[] to2 = new String[to.size()];
		String[] toSms2 = new String[toSms.size()];
		
		Iterator it = to.iterator();
		for (int i = 0; i < to.size(); i++) {
			to2[i] = (String) it.next();
			logger.debug(to2[i]);
		}

		it = toSms.iterator();
		for (int i = 0; i < toSms.size(); i++) {
			toSms2[i] = (String) it.next();
			logger.debug(toSms2[i]);
		}
		
		sendInfo( group,  to2,  toSms2,  settingsVO);
	}
	public static void sendInfo(String group, String[] to, String[] toSms, SettingsVO settingsVO) throws VekapuException {
		StringBuffer lsInfo = new StringBuffer(
				"Tervetuloa mukaan '"
						+ group
						+ "' veikkaus porukkaan. Porukkaan kuuluvat seuraavat henkilöt:");
		lsInfo.append(Constant.getLineSeparator());
		lsInfo.append(Constant.getLineSeparator());

		for (int i = 0; i < to.length; i++) {
			lsInfo.append(to[i]);
			if (i == 0)
				lsInfo.append(" - Porukan vetäjä");
			lsInfo.append(Constant.getLineSeparator());
		}
		lsInfo.append(Constant.getLineSeparator());
		lsInfo.append(Constant.getName() + " - " + Constant.getVersionNumber());
		lsInfo.append(Constant.getLineSeparator());
		lsInfo.append(Constant.getUrlHomePage());

		logger.debug(lsInfo.toString());

		// Varsinainen ilmoituksen lähetys
		sendMail(Constant.getEmailHeaderPrefix() + " Info", group, to, lsInfo
				.toString(), settingsVO);
		logger.info("Sähköposti Info - Porukan '" + group + "' tarkistuksista "
				+ to.length + " henkilölle.");

		// sms lähetys
		String viesti = "Tervetuloa mukaan '" + group + "' veikkaus porukkaan.";

		sendMail(Constant.getEmailHeaderPrefix(), group, toSms, viesti, settingsVO);

		logger.info("Tekstiviesti Info - Porukan '" + group
				+ "' tarkistuksista " + to.length + " henkilölle.");

	}

	private static void sendMail(String header, String group, String to,
			String text,  SettingsVO settingsVO) throws VekapuException {
		String[] apu = { to };
		sendMail(header, group, apu, text, settingsVO);
	}

	/**
	 * Caling the real meil senging class.
	 * @throws VekapuException 
	 */
	private static void sendMail(String header, String group, String[] to,
			String text, SettingsVO settingsVO) throws VekapuException {
		// if there is no email
		if (to[0].equalsIgnoreCase(Constant.getEmptyAddress()))
			return;
		if (settingsVO.isTest().booleanValue()) {
			// No mail when testing
			logger.debug("Testing...");
			logger.debug("Mail info:");
			logger.debug("Header : " + header);
			logger.debug("Group  : " + group);
			for (int i = 0; i < to.length; i++) {
				logger.debug("To     : " + to[i]);
			}
			logger.debug("Mail text : " + text);
		} else {
			try {
				SendMail sm = new SendMail(settingsVO.getMailServer());
				sm.postMail(to, header, text, settingsVO.getFrom(), 
						settingsVO.getReplyAddress());

				logger.debug("Viestit lähetetty osoitteisiin:");
				for (int i = 0; i < to.length; i++) {
					logger.debug(to[i]);
				}
			} catch (MessagingException me) {
				// TODO: handle exception
				String msg = "Meilin lähetys ei onnaa. Oiskohan säädöissä parantamista ??";
				logger.warn(msg);
				throw new VekapuException(msg,me);
				
			} catch (Exception e) {
				logger.error("Virhe postituksessa", e);
				throw new VekapuException(e);
			}
		}
	}
}
