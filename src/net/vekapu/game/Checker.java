///////////////////////////////////////////////////////////////////////////////
//
// Filename: Checker.java
//
// Author:   Janne Ilonen
// Project:  Vekapu
//
// Feedback: palaute@vekapu.net
//
// Purpose:  Checkin services.
//
// (c) Copyright J.Ilonen, 2003-2007
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

package net.vekapu.game;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import net.vekapu.CorrectNumberVO;
import net.vekapu.util.Constant;

import org.apache.log4j.Logger;

public class Checker {
	static Logger logger = Logger.getLogger(Checker.class);
	protected final String NEW_LINE = Constant.getLineSeparator();

	// Oikearivi
	private CorrectNumberVO correctNumberVO = null;
	
	protected String parasTulos;

	public Checker() {
	}

	public static int countJokeriA(List list) {
		int hit = 0;
		int lkm = list.size();
		boolean lbA = true;
		
		for (int j = 0; j < lkm; j++) {
			String apu = (String) list.get(j);
			if (apu.equals(Constant.getHit())) {
				if (lbA) {
					hit ++;
				}
			}
			else {
				lbA = false;
			}
		}
//		logger.debug(list + "Osumia: " + hit);
		return hit;
	}
	
	public static int countJokeriB(List list) {
		int hit = 0;
		int lkm = list.size();
		boolean lbB = true;
		
		for (int j = lkm - 1; j >= 0; j--) {
			String apu = (String) list.get(j);
			if (apu.equals(Constant.getHit())) {
				if (lbB) {
					hit ++;
				}
			}
			else {
				lbB = false;
			}
		}
//		logger.debug(list + "Osumia: " + hit);
		return hit;
	}
	protected String getBestResult() {
		return parasTulos;
	}

	protected List tarkistaRivit(List omaRivi) {
		
		logger.debug("Tarkistetaan osumat: " + omaRivi.size() + ":sta rivistä");
		
		List<List> tarkastetutRivit = new ArrayList<List>();
		List<String> rivinOsumat = null;
		
		int paras = 0;
		int paraslisa = 0;
		
		logger.debug(correctNumberVO.getCorrectNumbersString());
		// Omien rivien lukumäärä
		for (Iterator iter = omaRivi.iterator(); iter.hasNext();) {

			List rivi = (List) iter.next();

			logger.debug(rivi);
			int osumat = 0;
			int lisaNumero = 0;
			boolean lbOsuma = false;
			boolean lbLisa = false;

			rivinOsumat = new ArrayList<String>();

			// Oman rivin rastien lukumäärä
			for (Iterator iterator = rivi.iterator(); iterator.hasNext();) {
				String oma = (String) iterator.next();
				lbOsuma = false;
				lbLisa = false;
				
				// Oikean rivin numeroiden lukumaara
				for (Iterator iterator2 = correctNumberVO.getCorrectNumber().iterator(); iterator2.hasNext();) {
					// Kelataan numerot lapi, jos vaikka tulis osuma
					Integer oikea = (Integer) iterator2.next();
					
					// Osuuko ??
					if (Integer.valueOf(oma).equals(oikea)) {	
						// Osu = 1
						osumat++;
						lbOsuma = true;
					} 
				}

 				// Lisanumeroiden tarkistus
				for (Iterator iterator3 = correctNumberVO.getExtraNumber().iterator(); iterator3.hasNext();) {
					Integer lisa = (Integer) iterator3.next();
				
					// Osuuko ??
					if (Integer.valueOf(oma).equals(lisa)) {
						// Extra = 2
						lisaNumero++;
						lbLisa = true;
					} 
				}
				
				// Laitetaan tieto miten kävi
				if (lbOsuma) { 
					rivinOsumat.add(Constant.getHit());
				} else if (lbLisa) {
					rivinOsumat.add(Constant.getExtra());
				} else {
					rivinOsumat.add(Constant.getMiss());
				}
								
			}
			logger.debug(rivinOsumat);
			tarkastetutRivit.add(rivinOsumat);
			
			// Pidetään kirjaa parhaasta tuloksesta
			if (osumat > paras) {
				parasTulos = osumat + "+" + lisaNumero;
				paras = osumat;
				paraslisa = 0;
			} else if (osumat == paras && lisaNumero > paraslisa) {
				parasTulos = osumat + "+" + lisaNumero;
				paraslisa = lisaNumero;
			}			
		}
		return tarkastetutRivit;
	}


	/**
	 * @param correctNumberVO the correctNumberVO to set
	 */
	protected void setCorrectNumberVO(CorrectNumberVO correctNumberVO) {
		this.correctNumberVO = correctNumberVO;
	}
	
	protected List checkJokeri(List omaRivi) {
		logger.debug("Tarkistetaan osumat: " + omaRivi.size() + ":sta rivistä");
		
		List<List> tarkastetutRivit = new ArrayList<List> ();
		List<String> rivinOsumat = null;
		int i = 0;		

		for (Iterator iter = omaRivi.iterator(); iter.hasNext();) {
			List rivi = (List) iter.next();
			
			logger.debug(rivi);
			rivinOsumat = new ArrayList<String>();
			i = 0;

			// Oman rivin rastien lukumäärä
			for (Iterator iterator = rivi.iterator(); iterator.hasNext();) {
				String oma = (String) iterator.next();
				Integer oikea = (Integer) correctNumberVO.getCorrectNumber().get(i);
				i++;
				
				// Osuuko ??
				if (Integer.valueOf(oma).equals(oikea)) {
					// Laitetaan tieto miten kävi 
					rivinOsumat.add(Constant.getHit());
				} else {
					rivinOsumat.add(Constant.getMiss());
				}
			}
			logger.debug(rivinOsumat);
			tarkastetutRivit.add(rivinOsumat);
		}
		
		return tarkastetutRivit;
	}
}
