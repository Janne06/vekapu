///////////////////////////////////////////////////////////////////////////////
//
// Filename: OwnJokeri.java
//
// Author:   Janne Ilonen
// Project:  Vekapu
//
// Purpose:  Giving groups Jokeri numbers & group info.
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

package net.vekapu.game.jokeri;

import java.util.ArrayList;
import java.util.List;
import java.util.Properties;
import java.util.StringTokenizer;

import org.apache.log4j.Logger;

public class OwnJokeri {
	static Logger logger = Logger.getLogger(OwnJokeri.class);
	private Properties properties = new Properties();
	private List rivit = new ArrayList();

	public OwnJokeri(Properties properties) {
		this.properties = properties;
	}

	/**
	 * @return the rivit
	 */
	public List getRivit() {

		// Alustetaan paluuarvo jos on tarvis.
		if (rivit.size() < 1) {				
			getJokeriRivit();			
		}
		return rivit;
	}

	public String getMihinAstiJokeri() {
		return properties.getProperty("jokeri_asti").trim();
	}

	public boolean isStoreResult() {
		boolean rc = false;

		String cron = properties.getProperty("storeresult", "no");
		if (cron.equalsIgnoreCase("yes"))
			rc = true;

		return rc;
	}

	/**
	 * 
	 * @return
	 */
	private void getJokeriRivit() {
		String jokeri = null;

		jokeri = properties.getProperty("jokeri");

		int lkm = Integer.valueOf(jokeri).intValue();
		String haku = "jokeri_";

		// Lista mihin numerot ängetään.
		List lista = null;
		for (int i = 0; i < lkm; i++) {

			lista = new ArrayList();
			
			String rivi = properties.getProperty(haku + (i + 1));
			StringTokenizer toke = new StringTokenizer(rivi, ",");
			int j = 0;
			int numero = 0;
			while (toke.hasMoreTokens()) {
								
				numero = Integer.parseInt(toke.nextToken().trim());
				lista.add(String.valueOf(numero));
			
				j++;
			}
			rivit.add(lista);
		}
	}
}
