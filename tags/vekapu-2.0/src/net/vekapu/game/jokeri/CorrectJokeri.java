///////////////////////////////////////////////////////////////////////////////
//
// Filename: CorrectJokeri.java
//
// Author:   Janne Ilonen
// Project:  Vekapu
//
// Purpose:  Giving correct Jokeri numbers.
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
 * 
 * @author janne
 *
 */
public class CorrectJokeri extends CorrectNumber {
	static Logger logger = Logger.getLogger(CorrectJokeri.class);

	public final static String GAME = "jokeri";
	private final static String URL_JOKERI = "http://www.yle.fi/cgi-bin/tekstitv/ttv.cgi/47102/txt";	
	private String checkWeek = "";
	private String jokeriRivi = "";
	private String kierrosJokeri = "";
	
	
	/**
	 * @param aDayhelper
	 * @throws VekapuException
	 */
	public CorrectJokeri(SettingsVO settingsVO)
			throws VekapuException {
		super(settingsVO,GAME);

		if (isManual()) {
			checkWeek = getManualFile();
		} else {
			haeOikeaJokeriRivi();
		}
	}

	public static void main(String s[]) {
		PropertyConfigurator.configure(Constant.getLog4JConfigFileName());
		logger.info("CorrectJokeri main() Start");
		try {
			SettingsReader pr = new SettingsReader();
			SettingsVO settingsVO = pr.getSettingsVO();

			CorrectJokeri cn = new CorrectJokeri(settingsVO);
			cn.test();
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}

	
	/**
	 * 
	 * @return
	 */
	private String getKierrosJokeri() {
		if (isManual()) {
			checkWeek = properties.getProperty("JokeriTarkistaKierros").trim();
			kierrosJokeri = properties
					.getProperty(checkWeek + "_JokeriKierros");
		}
		correctNumberVO.setGameweek(kierrosJokeri);
		
		logger.debug("Kierros Jokeri: " + kierrosJokeri);
		return kierrosJokeri;
	}

	/**
	 * 
	 * @return
	 */
	private String getJokeriWeek() {
		String week = "";
		if (isManual()) {
			checkWeek = properties.getProperty("JokeriTarkistaKierros").trim();
		}
		logger.debug("JokeriWeek: " + week);
		return week;
	}

	/**
	 * 
	 * @return
	 */
	private void getOikeaRiviJokeri() {
		if (isManual()) {
			checkWeek = properties.getProperty("JokeriTarkistaKierros").trim();
			jokeriRivi = properties.getProperty(checkWeek + "_JokeriOikeaRivi");
		}
		StringTokenizer toke = new StringTokenizer(jokeriRivi, " ");
		int i = 0;
		int[] oikeaRivi = new int[7];

		while (toke.hasMoreTokens()) {
			oikeaRivi[i] = Integer.parseInt(toke.nextToken().trim());
			if (correctNumberVO.getCorrectNumber().size() < 7 ) {
				correctNumberVO.addCorrectNumber(new Integer(oikeaRivi[i]));
			}
			i++;
		}
		logger.debug("Oikea Jokeri: " + jokeriRivi);
	}

	/**
	 * @return Returns the correctNumberVO.
	 */
	public CorrectNumberVO getCorrectNumberVO() {
		getOikeaRiviJokeri();
		getKierrosJokeri();
		getJokeriWeek();
		
		logger.debug(correctNumberVO);
		
		return correctNumberVO;
	}

	/**
	 * @throws VekapuException
	 */
	private void haeOikeaJokeriRivi() throws VekapuException {
		String name = GAME;
		String name_txt = "";
		String dir = GAME + "/";
		
		// Tarkistettava kierros
		logger.debug("getSettingsVO().getCorrect() : " + getSettingsVO().getCorrect());
		if (getSettingsVO().getCorrect().equals("auto")) {	
			
			// ===============================================================
			// Päätellään tarkistettava kierros ja asetetaan se SettingsVO:hon
			//
			if (dayhelper.isSaturday() || dayhelper.isSunday()) {
				setCheckedRound(dayhelper.getYearWeek());
				setWeek(dayhelper.getWeek());
			} else {
				// Pitää tutkia viimeviikon kierrosta
				setCheckedRound(dayhelper.getYear() + "-"
						+ dayhelper.getLastWeek());	
				setWeek(dayhelper.getLastWeek());
			}
		}
					
		// Haetaan sivu missä oikeat numrot. 
		name_txt = getPage(name, URL_JOKERI);
		
		logger.debug("getSettingsVO().getCorrect() : " + getSettingsVO().getCorrect());
		logger.debug("getSettingsVO().getKierros() : " + getSettingsVO().getWeek());
		logger.debug("Name: " + name_txt);	
		String sivu = "";
		sivu = StoreFile.getFile(Constant.getWwwDir() + dir + name_txt);

		// Poistetaan html tägit varmuuden vuoksi.
		sivu = Html2txt.HtmlPage2txt(new StringBuffer(sivu));
		
		if (sivu.indexOf("Seuraava arvonta") > 0) {
			// Oikeita numeroita ei vilä ole julkaistu
			String messu = "Oikeita Jokeri-numeroita ei vielä ole julkaistu.";
			logger.warn(messu);
			
			// TODO: Vois hakee edellisen kierroksen sivun kopion vekapun omilta sivuilta
			
			throw new VekapuException(messu);
		}

		int viikko_int = sivu.indexOf("Kierros");
		String viikko = sivu.substring(viikko_int + 7, viikko_int + 10).trim();
		logger.debug("viikko: " + viikko);
		
		int pvm_int = sivu.indexOf("Kierros");
		// 136 - 119
		String pvm = sivu.substring(pvm_int - 60, pvm_int - 20).trim();
		logger.debug("pvm: " + pvm);
		
		int alku = sivu.indexOf("Voittonumero:");
		logger.debug("alku:" + alku);
		// 43 	
		String oikeat = sivu.substring(alku + 13, alku + 29).trim();
		logger.debug("Oikea Jokeri: " + oikeat);

		checkWeek = viikko;
		kierrosJokeri = "JokeriKierros " + viikko + " - " + pvm;
		jokeriRivi = oikeat;
		
		correctNumberVO.setDate(pvm);
		correctNumberVO.setGameweek(viikko);
	}

	/**
	 * 
	 *
	 */
	private void test() {
		logger.info("CorrectJokeri test Start");
		logger.info(getCorrectNumberVO());
		logger.info("CorrectJokeri test Stop");
	}

}
