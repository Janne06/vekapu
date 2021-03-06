///////////////////////////////////////////////////////////////////////////////
//
// Filename: VekapuInfo.java
//
// Author:   Janne Ilonen
// Project:  Vekapu
//
// Feedback: palaute@vekapu.net
//
// Purpose:  Informatin about Vekapu..
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

import net.vekapu.SettingsVO;
import net.vekapu.VekapuException;
import net.vekapu.mail.Messenger;

import org.apache.log4j.Logger;

/**
 * Information about Vekapu project.
 * 
 * @author janne
 * 
 */
public class VekapuInfo {

	private static Logger logger = Logger.getLogger(VekapuInfo.class);

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub
        System.out.println(getVesionInfo());
        System.out.println(getUsage());

	}

	public static String getUsage() {
		String NEW_LINE = Constant.getLineSeparator();
		String rc = NEW_LINE;

		rc += "usage: vekapu.bat/vekapu.sh [-v] [-test] " + NEW_LINE;
		rc += "  [-v/version] displays Vekapu version" + NEW_LINE;
		rc += "  [-test]      run Vekapu in test mode. Sends no email." + NEW_LINE;

		return rc;
	}
	
	/**
	 * Version information
	 */
	public static String getVesionInfo() {
		String NEW_LINE = Constant.getLineSeparator();
		String rc = NEW_LINE;

		rc += Constant.getName() + " " + Version.getVersionNumber();
		rc += NEW_LINE + NEW_LINE;
		rc += "Veikkausapuri lotto- ja jokeririven tarkistamiseen.";
		rc += NEW_LINE + Constant.getUrlHomePage();
		rc += NEW_LINE + NEW_LINE;
		rc += "(C) Janne Ilonen 2002 =>";

		return rc;
	}

	public static void endInfo(Throwable t) {

		SettingsVO settingsVO = new SettingsVO("info");

		String mail = Constant.getBugMail();
		String messu = "Ilmoittaisitko ongelmista osoitteeseen: " + mail;

		System.out.println(messu);
		t.printStackTrace();
		logger.error(t, t);
		if (!settingsVO.getMailServer().equals(""))
			try {
				Messenger.sendEMail("Bug", "", new String[] { mail },
						t.toString() + String.valueOf(t.getStackTrace()), settingsVO);
			} catch (VekapuException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

	}

	public static void groupNull() throws VekapuException {
		String msg = "Tarkistetavan lottoporukan määritely pielessä.";
		msg += "Tarkista asetuksen 'vekapu.properties' tiedostosta.";
		logger.error(msg);
		System.exit(0);
		// throw new VekapuException(msg);
	}
}
