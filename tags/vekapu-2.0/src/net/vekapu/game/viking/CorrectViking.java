///////////////////////////////////////////////////////////////////////////////
//
// Filename: CorrectViking.java
//
// Author:   Janne Ilonen
// Project:  Vekapu
//
// Purpose:  Giving correct Viking numbers.
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

import java.util.StringTokenizer;

import net.vekapu.CorrectNumberVO;
import net.vekapu.SettingsVO;
import net.vekapu.VekapuException;
import net.vekapu.game.CorrectNumber;
import net.vekapu.util.Constant;
import net.vekapu.util.SettingsReader;
import net.vekapu.util.StoreFile;
import net.vekapu.web.Html2txt;

import org.apache.log4j.Logger;
import org.apache.log4j.PropertyConfigurator;

/**
 * @author janne
 *
 */
public class CorrectViking extends CorrectNumber {
	static Logger logger = Logger.getLogger(CorrectViking.class);

	public final static String GAME = "viking-lotto";
	private final static String URL_VIKING = "http://www.yle.fi/cgi-bin/tekstitv/ttv.cgi/47103/txt";	
	private String checkWeek = "";
	private String kierrosViking = "";
	private String vikingNumerot = "";
	private String vikingLisat = "";

	/**
	 * 
	 * @param abManual
	 * @param aDayhelper
	 * @throws VekapuException
	 */
	public CorrectViking(SettingsVO settingsVO)
			throws VekapuException {
		super(settingsVO,GAME);

		logger.debug("Haetaan oikea VIKINGLOTTO rivi. abManual = " + isManual());
		if (isManual()) {
			checkWeek = getManualFile();
		} else {
			haeOikeaRiviVikin();
		}
	}

	public static void main(String s[]) {
		PropertyConfigurator.configure(Constant.getLog4JConfigFileName());
				
		try {
			SettingsReader pr = new SettingsReader();
			SettingsVO settingsVO = pr.getSettingsVO();

			CorrectViking cn = new CorrectViking(settingsVO);
			cn.test();
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}

	/**
	 * 
	 * @return
	 */
	private void getOikeaRiviViking() {
		if (isManual()) {
			vikingNumerot = properties.getProperty(checkWeek
					+ "_VikingOikeaRivi");
		}
		StringTokenizer toke = new StringTokenizer(vikingNumerot, ",");
		int i = 0;
		int[] oikeaRivi = new int[6];

		while (toke.hasMoreTokens() || i < 6) {
			oikeaRivi[i] = Integer.parseInt(toke.nextToken().trim());
			correctNumberVO.addCorrectNumber(new Integer(oikeaRivi[i]));
			i++;
		}
		logger.debug("Oikea Vikinglotto: " + vikingNumerot);
	}

	/**
	 * 
	 * @return
	 */
	private void getLisaNumerotViking() {
		if (isManual()) {
			vikingLisat = properties.getProperty(checkWeek
					+ "_VikingLisanumerot");
		}
		StringTokenizer toke = new StringTokenizer(vikingLisat, ",");
		int i = 0;
		int[] lisaNumero = new int[2];

		while (toke.hasMoreTokens()) {
			lisaNumero[i] = Integer.parseInt(toke.nextToken().trim());
			correctNumberVO.addExtraNumber(new Integer(lisaNumero[i]));
			i++;
		}
		logger.debug("Lisänumerot: " + vikingLisat);
	}

	/**
	 * 
	 * @return 
	 */
	private String getKierrosViking() {
		if (isManual()) {
			checkWeek = properties.getProperty("VikingTarkistaKierros").trim();
			kierrosViking = properties
					.getProperty(checkWeek + "_VikingKierros");
		}
		logger.debug("Kierros Viking: " + kierrosViking);
		return kierrosViking;
	}

	/**
	 * @return Returns the correctNumberVO.
	 */
	public CorrectNumberVO getCorrectNumberVO() {
		getOikeaRiviViking();
		getLisaNumerotViking();
		getKierrosViking();
//		getVikingLottoWeek();
		
		logger.debug(correctNumberVO);
		
		return correctNumberVO;
	}

	
	private void haeOikeaRiviVikin() throws VekapuException {
		String name = GAME;
		String name_txt = "";
		String dir = GAME + "/";

		// Tarkistettava kierrros 
		logger.debug("getSettingsVO().getCorrect() : " + getSettingsVO().getCorrect());
		if (getSettingsVO().getCorrect().equals("auto".trim())) {
			
			// ===============================================================
			// Päätellään tarkistettava kierros ja asetetaan se SettingsVO:hon
			//
			if (dayhelper.isMonday() || dayhelper.isTuesday()) {
				// Pitää tutkia viimeviikon kierrosta
				setCheckedRound(dayhelper.getYear() + "-"
						+ dayhelper.getLastWeek());	
				setWeek(dayhelper.getLastWeek());
				
			} else {
				setCheckedRound(dayhelper.getYearWeek());
				setWeek(dayhelper.getWeek());
			}
		}
		
		// Haetaan sivu missä oikeat numrot. 
		name_txt = getPage(name, URL_VIKING);
		
		logger.debug("getSettingsVO().getCorrect() : " + getSettingsVO().getCorrect());
		logger.debug("getSettingsVO().getKierros() : " + getSettingsVO().getWeek());
		logger.debug("Name: " + name_txt);	
		String sivu = "";
		sivu = StoreFile.getFile(Constant.getWwwDir() + dir + name_txt);
		// Poistetaan html tägit varmuuden vuoksi.
		sivu = Html2txt.HtmlPage2txt(new StringBuffer(sivu));
				
		if (sivu.indexOf("Seuraava arvonta") > 0) {
			// Oikeita numeroita ei vilä ole julkaistu
			String messu = "Oikeita VikinLotto-numeroita ei vielä ole julkaistu.";
			logger.warn(messu);
			throw new VekapuException(messu);
		}

		int viikko_int = sivu.indexOf("Kierros");
		String viikko = sivu.substring(viikko_int + 7, viikko_int + 10).trim();
		logger.debug("viikko: " + viikko);
		
		int pvm_int = sivu.indexOf("Kierros");
		String pvm = sivu.substring(pvm_int - 50, pvm_int - 30).trim();
		logger.debug("pvm: " + pvm);
		
		int alku = sivu.indexOf("OIKEAT NUMEROT");
		String oikeat = sivu.substring(alku + 15, alku + 40).trim();
		logger.debug("oikeat: " + oikeat);

		int lisanro = sivu.indexOf("LIS&Auml;NUMEROT");
		// Jostain syyst� kirjoitusasu TXTTV sivuilla on muuttunu.
		if (lisanro < 0)
			lisanro = sivu.indexOf("LIS�NUMEROT");
		if (lisanro < 0)
			lisanro = sivu.indexOf("LISÄNUMEROT");
		if (lisanro < 0)
			lisanro = sivu.indexOf("LISÃ„NUMEROT");
		// Jos merkistö sekoilee niin ei kai tässä muukaan auta.
		if (lisanro < 0)
			lisanro = 531;
		
		logger.debug("lisanro: " + lisanro);
		
		String lisat = sivu.substring(lisanro + 12, lisanro + 30).trim();
		logger.debug("lisat: " + lisat);
		lisat = lisat.replace(':', ' ').trim();
		logger.debug("lisat: " + lisat);
		
		checkWeek = viikko;
		kierrosViking = "VikingKierros " + viikko + " - " + pvm;
		vikingNumerot = oikeat;
		vikingLisat = lisat;
		
		correctNumberVO.setDate(pvm);
		correctNumberVO.setGameweek(viikko);
		
	}

	/**
	 * 
	 *
	 */
	private void test() {
		logger.info("CorrectViking test Start");
		logger.info(getCorrectNumberVO());
		logger.info("CorrectViking test Stop");
	}

}
