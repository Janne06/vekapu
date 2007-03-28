///////////////////////////////////////////////////////////////////////////////
//
// Filename: CorrectLotto.java
//
// Author:   Janne Ilonen
// Project:  Vekapu
//
// Purpose:  Giving correct lotto numbers.
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

package net.vekapu.game.lotto;

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
 
public class CorrectLotto extends CorrectNumber {
	static Logger logger = Logger.getLogger(CorrectLotto.class);

	public final static String GAME = "lotto";
	private final static String URL_LOTTO = "http://www.yle.fi/cgi-bin/tekstitv/ttv.cgi/47101/txt";
	private String checkWeek = "";
	private String lottoNumerot = "";
	private String lottoLisat = "";

	public CorrectLotto(SettingsVO settingsVO)
			throws VekapuException {
		super(settingsVO,GAME);
				
		if (isManual()) {
			checkWeek = getManualFile();
		} else {
			haeOikeaRiviLotto();
		}
	}

	public static void main(String s[]) {
		PropertyConfigurator.configure(Constant.getLog4JConfigFileName());
		
		try {
			SettingsReader pr = new SettingsReader();
			SettingsVO settingsVO = pr.getSettingsVO();

			CorrectLotto cn = new CorrectLotto(settingsVO);
			cn.test();
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}

	/**
	 * 
	 *
	 */
	private void getOikeaRiviLotto() {
		if (isManual()) {
			lottoNumerot = properties.getProperty(checkWeek + "_OikeaRivi");
		}

		StringTokenizer toke = new StringTokenizer(lottoNumerot, ",");
		int i = 0;
		int[] oikeaRivi = new int[7];

		while (toke.hasMoreTokens()) {
			oikeaRivi[i] = Integer.parseInt(toke.nextToken().trim());
			correctNumberVO.addCorrectNumber(new Integer(oikeaRivi[i]));
			i++;
		}
		logger.debug("Lotto numerot: " + lottoNumerot);
		logger.debug(correctNumberVO.toString());
	}

	/**
	 * 
	 *
	 */
	private void getLisaNumerot() {
		if (isManual()) {
			lottoLisat = properties.getProperty(checkWeek + "_Lisanumerot");
		}
		StringTokenizer toke = new StringTokenizer(lottoLisat, ",");
		int i = 0;
		int[] lisaNumero = new int[3];

		while (toke.hasMoreTokens()) {
			lisaNumero[i] = Integer.parseInt(toke.nextToken().trim());
			correctNumberVO.addExtraNumber(new Integer(lisaNumero[i]));
			i++;
		}
		logger.debug("Lisänumerot: " + lottoLisat);
		logger.debug(correctNumberVO.toString());
	}


	private void haeOikeaRiviLotto() throws VekapuException {
		String name = GAME;
		String name_txt = "";
		String dir = GAME + "/";

		// Tarkistettava kierrros 
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
		name_txt = getPage(name, URL_LOTTO);
		
		logger.debug("getSettingsVO().getCorrect() : " + getSettingsVO().getCorrect());
		logger.debug("getSettingsVO().getKierros() : " + getSettingsVO().getWeek());
		logger.debug("Name: " + name_txt);	
		String sivu = "";
		sivu = StoreFile.getFile(Constant.getWwwDir() + dir + name_txt);
		// Poistetaan html tägit varmuuden vuoksi.
		sivu = Html2txt.HtmlPage2txt(new StringBuffer(sivu));
			
//		logger.debug(sivu);
		
		if (sivu.indexOf("Seuraava arvonta") > 0) {
			// Oikeita numeroita ei vielä ole julkaistu
			String messu = "Oikeita Lotto-numeroita ei vielä ole julkaistu.";
			logger.warn(messu);
			throw new VekapuException(messu);
		}
		
		// Asetetaan kierroksen numero mukaan rivien tietoihin.
		correctNumberVO.setGameweek(getSettingsVO().getWeek());
		
		int kierros_int = 0;
		if (sivu.indexOf("KIERROS") > 0) {
			// o:lla
			kierros_int = sivu.indexOf("KIERROS");
		} else if (sivu.indexOf("KIERR0S") > 0) {
			// nollalla
			kierros_int = sivu.indexOf("KIERR0S");
		} else {
			logger.warn("Ongelmia oikean rivin haussa !!!.");
			logger.warn("Muuta 'newpage = yes' tai poista tiedosto: "
					+ Constant.getWwwDir() + name);
			return;
		}

		String viikko = sivu.substring(kierros_int + 7 , kierros_int + 11)
				.trim();
		viikko = viikko.replace('<', ' ').trim();
		logger.debug("viikko: " + viikko);
		// 99 80
		String pvm = sivu.substring(kierros_int - 50, kierros_int - 30).trim();
		correctNumberVO.setDate(pvm);

		int alku = sivu.indexOf("OIKEAT NUMEROT");
		logger.debug("OIKEAT NUMEROT alkaakohdasta " + alku);
		// Bug [ #15 ] Kooditakomo > Lisäarvonasta ongelmia
		// int alku2 = sivu.indexOf("</B>",alku);
		// (alku + 45, alku + 46 + 21)
		String oikeat = sivu.substring(alku + 16, alku + 42).trim();
		logger.debug("oikeat: " + oikeat);
		
		int lisanro = sivu.indexOf("LIS&Auml;NUMEROT");
		// Jostain syystä kirjoitusasu TXTTV sivuilla on muuttunu.
		if (lisanro < 0)
			lisanro = sivu.indexOf("LISÄNUMEROT");
		// Taas pitäis lisätä uus vaihtoehto
		if (lisanro < 0)
			lisanro = sivu.indexOf("LIS?NUMEROT");
		if (lisanro < 0)
			lisanro = sivu.indexOf("LIS�NUMEROT");
		
		if (lisanro < 0)
			lisanro = sivu.indexOf("LISÃ¤NUMEROT");
//		 LISÄNUMEROT
		if (lisanro < 0)
			lisanro = sivu.indexOf("LISÃ„NUMEROT");
		//		LISÃ„NUMEROT
		// Jos merkistö sekoilee niin ei kai tässä muukaan auta.
		if (lisanro < 0)
			lisanro = 490;
		
		logger.debug("lisanro: " + lisanro);
		
		//(lisanro + 42, lisanro + 60)
		String lisat = sivu.substring(lisanro + 12, lisanro + 25).trim();
		logger.debug("lisat: " + lisat);
		lisat = lisat.replace(':', ' ').trim();
		logger.debug("lisat: " + lisat);
		
		checkWeek = viikko;
		lottoNumerot = oikeat;
		lottoLisat = lisat;
		
		correctNumberVO.setDate(pvm);
		correctNumberVO.setGameweek(viikko);
		
		logger.debug(correctNumberVO.toString());
	}

	/**
	 * 
	 *
	 */
	private void test() {
		logger.info("CorrectNumber test Start");
		logger.info(getCorrectNumberVO());
		logger.info("CorrectNumber test Stop");
	}
	
	/**
	 * 
	 * @return
	 */
	public static String getUrlLotto() {
		return URL_LOTTO;
	}

	/**
	 * @return Returns the correctNumberVO.
	 */
	public CorrectNumberVO getCorrectNumberVO() {
		logger.debug("getCorrectNumberVO()");
		getOikeaRiviLotto();
		getLisaNumerot();
		
		logger.debug(correctNumberVO);
		
		return correctNumberVO;
	}

}
