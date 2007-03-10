///////////////////////////////////////////////////////////////////////////////
//
// Filename: OwnViking.java
//
// Author:   Janne Ilonen
// Project:  Vekapu
//
// Purpose:  Giving groups Viking numbers & group info.
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

package net.vekapu.game.viking;

import java.util.ArrayList;
import java.util.List;
import java.util.Properties;
import java.util.StringTokenizer;

import org.apache.log4j.Logger;

public class OwnViking {
	static Logger logger = Logger.getLogger(OwnViking.class);
	private Properties properties = new Properties();
	private List rivit = new ArrayList();

	/**
	 * 
	 * @param properties
	 */
	public OwnViking(Properties properties) {
		this.properties = properties;
	}

	/**
	 * @return the rivit
	 */
	public List getRivit() {

		// Alustetaan paluuarvo jos on tarvis.
		if (rivit.size() < 1) {
			for (int i = 7; i < 12; i++) {
				if (isJarjestelmaViking(i)) {
					logger.debug("Haetaan järjestelmä : " + i);
			
					getRivitViking(i);
				}
			}
		}
		return rivit;
	}

	public boolean isStoreResult() {
		boolean rc = false;

		String cron = properties.getProperty("storeresult", "no");
		if (cron.equalsIgnoreCase("yes"))
			rc = true;

		return rc;
	}

	public String getMihinAstiViking() {
		return properties.getProperty("viking_asti").trim();
	}

	private void getRivitViking(int rastit) {
		int lkm = 0;
		String haku = null;
		switch (rastit) {
		case 7:
			lkm = Integer.parseInt(properties
					.getProperty("viking_jarjestelma7"));
			haku = "viking_jarjestelma7_";
			break;
		case 8:
			lkm = Integer.parseInt(properties
					.getProperty("viking_jarjestelma8"));
			haku = "viking_jarjestelma8_";
			break;
		case 9:
			lkm = Integer.parseInt(properties
					.getProperty("viking_jarjestelma9"));
			haku = "viking_jarjestelma9_";
			break;
		case 10:
			lkm = Integer.parseInt(properties
					.getProperty("viking_jarjestelma10"));
			haku = "viking_jarjestelma10_";
			break;
		case 11:
			lkm = Integer.parseInt(properties
					.getProperty("viking_jarjestelma11"));
			haku = "viking_jarjestelma11_";
			break;
		default:
			lkm = Integer.parseInt(properties.getProperty("viking_rivit"));
			haku = "viking_rivi_";
			break;
		}

		logger.debug("Rivin typpi: " + haku + ", Lkm: " + lkm);
		
//		 Lista mihin numerot ängetään.
		List lista = null;

		for (int i = 0; i < lkm; i++) {

			String rivi = properties.getProperty(haku + (i + 1));
			StringTokenizer toke = new StringTokenizer(rivi, ",");
			
			lista = new ArrayList();

			int numero=0;
			while (toke.hasMoreTokens()) {
				numero = Integer.parseInt(toke.nextToken().trim());				
				lista.add(String.valueOf(numero));
				
			}			
			// Laitetaankin suoraa OwnNumbersVO:hon. (kaiki samaan)
			rivit.add(lista);
//			logger.debug(lista);
		}
	}

	public boolean isJarjestelmaViking(int rastit) {
		boolean loytyi = false;
		switch (rastit) {
		case 6:
			if (properties.getProperty("viking_rivit") == null)
				loytyi = false;
			else if (Integer.parseInt(properties.getProperty("viking_rivit")) > 0)
				loytyi = true;
			break;
		case 7:
			if (properties.getProperty("viking_jarjestelma7") == null)
				loytyi = false;
			else if (Integer.parseInt(properties
					.getProperty("viking_jarjestelma7")) > 0)
				loytyi = true;
			break;
		case 8:
			if (properties.getProperty("viking_jarjestelma8") == null)
				loytyi = false;
			else if (Integer.parseInt(properties
					.getProperty("viking_jarjestelma8")) > 0)
				loytyi = true;
			break;
		case 9:
			if (properties.getProperty("viking_jarjestelma9") == null)
				loytyi = false;
			else if (Integer.parseInt(properties
					.getProperty("viking_jarjestelma9")) > 0)
				loytyi = true;
			break;
		case 10:
			if (properties.getProperty("viking_jarjestelma10") == null)
				loytyi = false;
			else if (Integer.parseInt(properties
					.getProperty("viking_jarjestelma10")) > 0)
				loytyi = true;
			break;
		case 11:
			if (properties.getProperty("viking_jarjestelma11") == null)
				loytyi = false;
			else if (Integer.parseInt(properties
					.getProperty("viking_jarjestelma11")) > 0)
				loytyi = true;
			break;
		default:

			break;
		}
		return loytyi;
	}
}
