///////////////////////////////////////////////////////////////////////////////
//
// Filename: PageLoader.java
//
// Author:   Janne Ilonen
// Project:  Vekapu
//
// Purpose:  Getting html-page from net.
//
// (c) Copyright J.Ilonen, 2003-2006
//
// Used this sites as modell:
// http://www.rgagnon.com/howto.html
// http://www.rgagnon.com/javadetails/java-0059.html
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

package net.vekapu.web;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Properties;

import net.vekapu.SettingsVO;
import net.vekapu.VekapuException;
import net.vekapu.util.Constant;
import net.vekapu.util.SettingsReader;

import org.apache.log4j.Logger;
import org.apache.log4j.PropertyConfigurator;

public class PageLoader {
	static Logger logger = Logger.getLogger(PageLoader.class);
	private SettingsVO settingsVO = null;

	
	public PageLoader(SettingsVO settingsVO) {
		this.settingsVO = settingsVO;
	}

	public static void main(String s[]) {
		PropertyConfigurator.configure(Constant.getLog4JConfigFileName());
		
		SettingsReader pr;
		try {
			pr = new SettingsReader();
			SettingsVO settingsVO = pr.getSettingsVO();
			
			new PageLoader(settingsVO).test();		
			
		} catch (VekapuException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	private void test() {
		try {
/*
			String sivu = readPage(Constant.getUrlLotto());
			logger.debug(sivu);
			sivu = readPage(Constant.getUrlJokeri());
			logger.debug(sivu);
			sivu = readPage(Constant.getUrlVikingLotto());
			logger.debug(sivu);
*/
		} catch (Exception ex) {
			logger.error(ex, ex);
			ex.printStackTrace();
		}

	}

	public String readPage(String URLName) throws VekapuException {
		String page = "";
		logger.info("Haetaan netistä sivu: " + URLName);

		try {

			Properties systemSettings = System.getProperties();
			systemSettings.put("proxySet", String.valueOf(settingsVO.isProxySet()));
			systemSettings.put("http.proxyHost", settingsVO.getProxyHost());
			systemSettings.put("http.proxyPort", settingsVO.getProxyPort());

			URL u = new URL(URLName);
			HttpURLConnection con = (HttpURLConnection) u.openConnection();

			if (settingsVO.isProxySet().booleanValue()) {
				sun.misc.BASE64Encoder encoder = new sun.misc.BASE64Encoder();
				String encodedUserPwd = encoder
						.encode("domain\\username:password".getBytes());
				con.setRequestProperty("Proxy-Authorization", "Basic "
						+ encodedUserPwd);
			}

			con.setRequestMethod("HEAD");

			try {
				BufferedReader in = new BufferedReader(new InputStreamReader(u
						.openStream()));
				String str;

				while ((str = in.readLine()) != null) {
					page += str + Constant.getLineSeparator();
				}
				in.close();
			} catch (MalformedURLException e) {
				logger.error("MalformedURLException", e);
				throw e;
			} catch (IOException e) {
				// @ToDo Lisää infoa kun ei saada sivua haettua.
				String messu = "Tarkista proxy-asetukset '"
						+ "vekapu.properties' - tiedostosta kohdasta :"
						+ Constant.getLineSeparator() + " proxySet = true"
						+ Constant.getLineSeparator() + " proxyHost = ???"
						+ Constant.getLineSeparator() + " proxyPort = ???";
				logger.error("IOException", e);
				logger.error(messu);
				System.out.println(messu);
				throw e;
			}
		} catch (Exception e) {
			logger.error(e, e);
			throw new VekapuException(e);
		}
		return page;
	}
}
