///////////////////////////////////////////////////////////////////////////////
//
// Filename: OwnLotto.java
//
// Author:   Janne Ilonen
// Project:  Vekapu
//
// Purpose:  Giving groups lottery numbers & group info.
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

package net.vekapu.game.lotto;

import java.util.ArrayList;
import java.util.List;
import java.util.Properties;
import java.util.StringTokenizer;

import org.apache.log4j.Logger;

public class OwnLotto {
	static Logger logger = Logger.getLogger(OwnLotto.class);
	private Properties properties = new Properties();	
	private List rivit = new ArrayList();

	/**
	 * 
	 * @param properties
	 */
	public OwnLotto(Properties properties) {
		this.properties = properties;
	}
	
	/**
	 * @return the rivit
	 */
	public List getRivit() {

		// Alustetaan paluuarvo jos on tarvis.
		if (rivit.size() < 1) {
			for (int i = 7; i < 12; i++) {
				if (isJarjestelma(i)) {
					logger.debug("Haetaan järjestelmä : " + i);
			
					getRivit(i);
				}
			}
		}
		return rivit;
	}


	/**
	 * 
	 * @param rastit
	 */
	private void getRivit(int rastit) {
				
		int lkm = 0;
		String haku = null;
		switch (rastit) {
		case 8:
			lkm = Integer.parseInt(properties.getProperty("jarjestelma8"));
			haku = "jarjestelma8_";
			break;
		case 9:
			lkm = Integer.parseInt(properties.getProperty("jarjestelma9"));
			haku = "jarjestelma9_";
			break;
		case 10:
			lkm = Integer.parseInt(properties.getProperty("jarjestelma10"));
			haku = "jarjestelma10_";
			break;
		case 11:
			lkm = Integer.parseInt(properties.getProperty("jarjestelma11"));
			haku = "jarjestelma11_";
			break;
		default:
			lkm = Integer.parseInt(properties.getProperty("perusrivit"));
			haku = "rivi_";
			break;
		}

		logger.debug("Rivin typpi: " + haku + ", Lkm: " + lkm);
		
		// Lista mihin numerot ängetään.
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
		}
	}

	/**
	 * 
	 * @param rastit
	 * @return
	 */
	private boolean isJarjestelma(int rastit) {
		boolean loytyi = false;
		switch (rastit) {
		case 7:
			if (properties.getProperty("perusrivit") == null)
				loytyi = false;
			else if (Integer.parseInt(properties.getProperty("perusrivit")) > 0)
				loytyi = true;
			break;
		case 8:
			if (properties.getProperty("jarjestelma8") == null)
				loytyi = false;
			else if (Integer.parseInt(properties.getProperty("jarjestelma8")) > 0)
				loytyi = true;
			break;
		case 9:
			if (properties.getProperty("jarjestelma9") == null)
				loytyi = false;
			else if (Integer.parseInt(properties.getProperty("jarjestelma9")) > 0)
				loytyi = true;
			break;
		case 10:
			if (properties.getProperty("jarjestelma10") == null)
				loytyi = false;
			else if (Integer.parseInt(properties.getProperty("jarjestelma10")) > 0)
				loytyi = true;
			break;
		case 11:
			if (properties.getProperty("jarjestelma11") == null)
				loytyi = false;
			else if (Integer.parseInt(properties.getProperty("jarjestelma11")) > 0)
				loytyi = true;
			break;
		default:

			break;
		}
		return loytyi;
	}
}
