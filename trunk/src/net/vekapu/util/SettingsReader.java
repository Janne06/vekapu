///////////////////////////////////////////////////////////////////////////////
//
// Filename: PropertyReader.java
//
// Author:   Janne Ilonen
// Project:  Vekapu
//
// Purpose:  Reading settings from vekapu.properties-file to SettingsVO.
//
// (c) Copyright J.Ilonen, 2006 =>
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

package net.vekapu.util;

import java.util.Properties;

import net.vekapu.SettingsVO;
import net.vekapu.VekapuException;

import org.apache.log4j.Logger;
import org.apache.log4j.PropertyConfigurator;

/**
 * Reading settings from vekapu.properties -file to SettingsVO.
 * 
 * @author janne
 *
 */
public class SettingsReader  {
	static Logger logger = Logger.getLogger(SettingsReader.class);
	private Properties properties = new Properties();
	private String fileName = "vekapu.properties";
	private SettingsVO settingsVO = null; 

	/**
	 * 
	 * @throws VekapuException
	 */
	public SettingsReader() throws VekapuException {
		readProperties();
	}

	/**
	 * 
	 * @return Returns the settingsVO.
	 */
	public SettingsVO getSettingsVO() {
		return settingsVO;
	}

	public static void main(String s[]) {
		PropertyConfigurator.configure(Constant.getLog4JConfigFileName());
		try {
			new SettingsReader().test();
		} catch (VekapuException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}


	/**
	 * 
	 * @return
	 * @throws VekapuException 
	 */
	private void readProperties() throws VekapuException {
		
		properties = PropsReader.read(fileName);
		fillVO();

	}

	/*
	 * 
	 */
	private void test() {
		logger.info("Testing...");
		logger.debug(properties.toString().replace(',', '\n'));
		logger.debug( settingsVO.toString() );
		logger.info("... end test.");
	}

	/**
	 * 
	 * @throws VekapuException
	 */
	private void fillVO() throws VekapuException {
		boolean rc = false;

		String correct = properties.getProperty("correct", "auto").trim();
		if (correct.trim().length() == 0) {
			correct = "auto";
		}
		
		settingsVO = new SettingsVO(correct);

		String ok = properties.getProperty("test", "");
		if (ok.equalsIgnoreCase("yes")) 
			rc = true;
		settingsVO.setTest(Boolean.valueOf(rc));
		
		rc = false;
		String cron = properties.getProperty("cronjob", "");
		if (cron.equalsIgnoreCase("yes"))
			rc = true;

		settingsVO.setCronJob(Boolean.valueOf(rc));
		
		rc = false;
		String console = properties.getProperty("konsolille", "yes");
		if (console.equalsIgnoreCase("yes"))
			rc = true;

		settingsVO.setConsole(Boolean.valueOf(rc));

		rc = false;
		String email = properties.getProperty("email", "");
		if (email.equalsIgnoreCase("yes"))
			rc = true;

		settingsVO.setEmail(Boolean.valueOf(rc));
		
		rc = false;
		String manual = properties.getProperty("manual", "");
		if (manual.equalsIgnoreCase("yes"))
			rc = true;

		settingsVO.setManual(Boolean.valueOf(rc));
		
		rc = false;
		ok = properties.getProperty("newpage", "");
		if (ok.equalsIgnoreCase("yes"))
			rc = true;

		settingsVO.setNewPage(Boolean.valueOf(rc));
		
		rc = false;
		String proxy = properties.getProperty("proxySet", "");
		if (proxy.equalsIgnoreCase("true"))
			rc = true;
		
		settingsVO.setProxySet(Boolean.valueOf(rc));
		
		rc = false;
		ok = properties.getProperty("server", "");
		if (ok.equalsIgnoreCase("yes"))
			rc = true;

		settingsVO.setServer(Boolean.valueOf(rc));
		
		rc = false;
		String sms = properties.getProperty("sms", "");
		if (sms.equalsIgnoreCase("yes"))
			rc = true;

		settingsVO.setSms(Boolean.valueOf(rc));
		
		
		settingsVO.setAdmin(properties.getProperty("admin", Constant.getDefaultEMail()));
		settingsVO.setFrom(properties.getProperty("from", Constant.getDefaultEMail()));
		settingsVO.setGroupDir(properties.getProperty("groupDir", "."));
		settingsVO.setGroupInfo(properties.getProperty("info_group", ""));
		settingsVO.setMailServer(properties.getProperty("mailserver", ""));

		// Jos postipalvelimena oletus 'localhost' niin nollataan asetus
		if (settingsVO.getMailServer().equals("localhost"))
			settingsVO.setMailServer("");

		settingsVO.setPassWord(properties.getProperty("password", ""));
		settingsVO.setPort(properties.getProperty("port", ""));

		settingsVO.setProxyHost(properties.getProperty("proxyHost", ""));
		settingsVO.setProxyPort(properties.getProperty("proxyPort", ""));
		settingsVO.setReplyAddress(properties.getProperty("replyto", Constant.getDefaultEMail()));	
		
		Integer count = Integer.valueOf(properties.getProperty(
				"group_count", "0"));
		
		settingsVO.setGroupCount(count);
		
		String[] groupName = new String[count.intValue() + 1];
		for (int i = 1; i <= count.intValue(); i++) {
			groupName[i] = properties.getProperty("group_" + i);
		}
		settingsVO.setGroupName(groupName);
		
		// Lisaa asetuksia
		// ======================================================
		// Asetetaan tarkistettava kierros 
		if (settingsVO.getCorrect().equals("auto"))
		{
			// Tarkistetaan viimeisin kierros
			// Oikean viikon päättely riippuu pelistäja tarkistushetkestä
			// joten toi päättely tehdään ko pelin oikeiden numeroiden haussa
		}
		else
		{
			// Pitäis laittaa tarkistettava kierros kohalleen			
			settingsVO.setWeek( settingsVO.getCorrect().substring(5, 7) );
		}	
		
		settingsVO.setCheckedRound( settingsVO.getCorrect());
	}
}
