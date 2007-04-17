///////////////////////////////////////////////////////////////////////////////
//
// Filename: CorrectNumber.java
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

/**
 * Master class off the correct game info (numbers)
 * 
 * @author janne
 */
package net.vekapu.game;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;
import java.util.StringTokenizer;

import net.vekapu.CorrectNumberVO;
import net.vekapu.SettingsVO;
import net.vekapu.VekapuException;
import net.vekapu.util.Constant;
import net.vekapu.util.DayHelper;
import net.vekapu.util.SettingsReader;
import net.vekapu.util.StoreFile;
import net.vekapu.web.Html2txt;
import net.vekapu.web.PageLoader;

import org.apache.log4j.Logger;
import org.apache.log4j.PropertyConfigurator;

/**
 * Gettin correct numbers of the game.
 * 
 * @author janne
 * 
 */
public class CorrectNumber {
	static Logger logger = Logger.getLogger(CorrectNumber.class);

	private SettingsVO settingsVO = null;

	/*
	 * Kuluvan päivän hahmotus => mikä kierrokselle numeroksi ?
	 */
	protected DayHelper dayhelper = new DayHelper();

	/**
	 * Storing correct numbers
	 */
	protected CorrectNumberVO correctNumberVO = null;

	/**
	 * Game spesifig setting
	 */
	protected Properties gameProps = new Properties();
	

	/**
	 * 
	 * @param settingsVO
	 * @param game
	 */
	public CorrectNumber(SettingsVO settingsVO, String game) {
		logger.info("Haetaan oikeat '" + game + "' rivit. abManual = "
				+ settingsVO.isManual() + " Tarkistettava: "
				+ settingsVO.getCorrect());

		this.settingsVO = settingsVO;
		correctNumberVO = new CorrectNumberVO(game);
		
		if (isManual()) {
			setWeek("manual");
			setCheckedRound("manual");
		}
		
	}

	public static void main(String s[]) {
		PropertyConfigurator.configure(Constant.getLog4JConfigFileName());
		
		try {
			SettingsReader pr = new SettingsReader();
			SettingsVO settingsVO = pr.getSettingsVO();

			// lotto, jokeri, viking-lotto
			String game = "jokeri";
			
			
			CorrectNumber cn = new CorrectNumber(settingsVO,game);
			cn.getCorrectNumbers(game);
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}
	/**
	 * @return Returns the settingsVO.
	 */
	protected SettingsVO getSettingsVO() {
		return settingsVO;
	}

	/**
	 * 
	 * @return
	 */
	protected boolean isManual() {
		return getSettingsVO().isManual().booleanValue();
	}

	/**
	 * 
	 * @param week
	 */
	protected void setWeek(String week) {
		settingsVO.setWeek(week);
	}

	/**
	 * 
	 * @param checkedRound
	 */
	protected void setCheckedRound(String checkedRound) {
		settingsVO.setCheckedRound(checkedRound);
	}

	/**
	 * 
	 *
	 */
	protected void currentWeek() {
		setCheckedRound(dayhelper.getYearWeek());
		setWeek(dayhelper.getWeek());
	}

	/**
	 * 
	 *
	 */
	protected void lastWeek() {
		// Pitää tutkia viimeviikon kierrosta
		setCheckedRound(dayhelper.getYear() + "-" + dayhelper.getLastWeek());
		setWeek(dayhelper.getLastWeek());
	}

	public CorrectNumberVO getCorrectNumbers(String game) throws VekapuException {

		CorrectNumberVO l_correctNumberVO = new CorrectNumberVO(game);
		
		String name = game;
		String name_txt = "";
		String dir = game + Constant.getFileSeparator();

		String gameSettings = Constant.getGamePropsDir()+ game + ".properties";
		
		try {
			gameProps.load(new FileInputStream(gameSettings));
		} catch (IOException e) {
			logger.error("IOException", e);
			throw new VekapuException(e);
		}
		
		logger.info(gameSettings);
		logger.info(gameProps);
		
		// Tarkistettava kierrros 
		logger.debug("getSettingsVO().getCorrect() : " + getSettingsVO().getCorrect());
		if (getSettingsVO().getCorrect().equals("auto")) {	
			// ===============================================================
			// Päätellään tarkistettava kierros ja asetetaan se SettingsVO:hon
			// 
			int day = Integer.valueOf( gameProps.getProperty("day") ).intValue();
			
			logger.debug("Weekday: " + day);
			
			if ( day <= dayhelper.getWeekDayNumber()) {
				currentWeek();
			} else {
				// Pitää tutkia viimeviikon kierrosta
				lastWeek();
			}
		}
		
//		 Haetaan sivu missä oikeat numrot. 
		name_txt = getPage(name, gameProps.getProperty("url"));
		
		logger.debug("getSettingsVO().getCorrect() : " + getSettingsVO().getCorrect());
		logger.debug("getSettingsVO().getKierros() : " + getSettingsVO().getWeek());
		logger.debug("Name: " + name_txt);	
		String sivu = "";
		sivu = StoreFile.getFile(Constant.getWwwDir() + dir + name_txt);
		// Poistetaan html tägit varmuuden vuoksi.
		sivu = Html2txt.HtmlPage2txt(new StringBuffer(sivu));
		
		// logger.debug(sivu);
		
		if (sivu.indexOf(gameProps.getProperty("pass")) > 0) {
			// Oikeita numeroita ei vielä ole julkaistu
			String messu = "Oikeita " + game + "-numeroita ei vielä ole julkaistu.";
			logger.warn(messu);
			throw new VekapuException(messu);
		}
		
		// Asetetaan kierroksen numero mukaan rivien tietoihin.
		correctNumberVO.setGameweek(getSettingsVO().getWeek());
		l_correctNumberVO.setGameweek(getSettingsVO().getWeek());
		
		// Jos etsittävä merkkijono vaihtelee
		String round = gameProps.getProperty("round");
		logger.debug("round: " + round);
		


		int kierros_int = 0;
		kierros_int = sivu.indexOf(round);

		int start = Integer.valueOf( gameProps.getProperty("roundStartPosition") ).intValue();
		int end = Integer.valueOf( gameProps.getProperty("roundEndPosition") ).intValue();

		String viikko = sivu.substring(kierros_int + start , kierros_int + end).trim();
		viikko = viikko.replace('<', ' ').trim();
		logger.debug("viikko: " + viikko);

		start = Integer.valueOf( gameProps.getProperty("dateStartPosition") ).intValue();
		end = Integer.valueOf( gameProps.getProperty("dateEndPosition") ).intValue();
		
		String pvm = sivu.substring(kierros_int + start, kierros_int + end).trim();
		l_correctNumberVO.setDate(pvm);
		logger.debug("date: " + pvm);
		
		int alku = sivu.indexOf(gameProps.getProperty("startCorrect"));
		logger.debug("OIKEAT NUMEROT alkaakohdasta " + alku);

		start = Integer.valueOf( gameProps.getProperty("corrctStartPosition") ).intValue();
		end = Integer.valueOf( gameProps.getProperty("corrctEndPosition") ).intValue();
		
		String oikeat = sivu.substring(alku + start, alku + end).trim();
		logger.debug("oikeat: " + oikeat);
		
		start = Integer.valueOf( gameProps.getProperty("startExtraFirstPosition") );
		logger.debug("startExtra: " + gameProps.getProperty("startExtra"));
		int lisanro = sivu.indexOf(gameProps.getProperty("startExtra"),start);
		start = Integer.valueOf( gameProps.getProperty("extraStartPosition") ).intValue();
		end = Integer.valueOf( gameProps.getProperty("extraEndPosition") ).intValue();
		
		logger.debug("lisanro: " + lisanro);

		// TODO Kato tää loppu kuntoon
		String lisat = sivu.substring(lisanro + start, lisanro + end).trim();
		logger.debug("lisat: " + lisat);
		lisat = lisat.replace(':', ' ').trim();
		logger.debug("lisat: " + lisat);
						
		l_correctNumberVO.setDate(pvm);
		l_correctNumberVO.setGameweek(viikko);
		
		
		String delimeter = ",";
		if (oikeat.indexOf(delimeter) < 0) delimeter = " ";
		
		StringTokenizer toke = new StringTokenizer(oikeat, delimeter);
		Integer number = null;

		while (toke.hasMoreTokens()) {
			number = Integer.valueOf(toke.nextToken().trim());
			l_correctNumberVO.addCorrectNumber(number);
		}
		
		delimeter = ",";
		if (lisat.indexOf(delimeter) < 0) delimeter = " ";
		
		toke = new StringTokenizer(lisat, delimeter);
		number = null;

		while (toke.hasMoreTokens()) {
			number = Integer.valueOf(toke.nextToken().trim());
			l_correctNumberVO.addExtraNumber(number);
		}
		
		logger.debug(l_correctNumberVO.toString());
		
		return l_correctNumberVO;
	}
	
	
	/**
	 * Here we get local copy of web page whicts contains correct numbers.
	 * 
	 * @param name
	 *            Local copy name. YYYY-ROUND (year int- round int)
	 * @param url
	 *            Web page location.
	 * @return Name of text file where is correct numbers.
	 * @throws VekapuException
	 * 
	 */
	protected String getPage(String name, String url) throws VekapuException {

		logger.debug("name: " + name);
		logger.debug("getSettingsVO().getCorrect(): "
				+ getSettingsVO().getCorrect());
		logger.debug("getSettingsVO().getCheckedRound(): "
				+ getSettingsVO().getCheckedRound());

		name = name + "-" + getSettingsVO().getCheckedRound();
		logger.debug("name: " + name);

		String dir = correctNumberVO.getGame() + Constant.getFileSeparator();
		String name_txt = name + ".txt";
		name = name + Constant.getWwwFileExt();
		String fullname = Constant.getWwwDir() + dir + name;
		String userdir = System.getProperty("user.dir");
		
		logger.debug("fullname: " + fullname);
		logger.debug("Here we are: " + userdir);
		logger.info("Real fullname: " +  userdir + fullname);

		// Poistetaan vanha versio jos tarpeen
		if (getSettingsVO().isNewPage().booleanValue()) {
			StoreFile.deleteFile(userdir + fullname);
		}

		// Jos paikallinen kopio löytyy niin ei tarvii hake uutta
		if (StoreFile.isFileExist(userdir + fullname)) {
			logger.info("Sivu '" + fullname + "' on jo haettu");
			return name_txt;
		}

		// Jos tekstiteeveen sivusta ei vielä ole paikallista kopiota
		// TODO jos tarkistettavaksi on annetu joku mu kuin 'auto' niin ei haeta
		// netistä
		// ainakaan tekstiTV:n sivuilta. Joskus vois ehkä hakee kopion vekapun
		// omilta sivuilta !!
		// Vois olla aika hyvä.
		if (!StoreFile.isFileExist(fullname)) {
			logger.info("Haetaan oikea " + name + " rivi ja talleteaan se "
					+ Constant.getWwwDir() + dir + " hakemistoon.");
			logger.info(fullname);

			PageLoader hae = new PageLoader(getSettingsVO());
			String wwwpage = hae.readPage(url);
			StoreFile sf = new StoreFile(Constant.getWwwDir() + dir, name,
					wwwpage);
			logger.debug("StoreFile: " + sf);

			// Talleteaan lisäks tekstimuodossa.
			StoreFile sf2 = new StoreFile(Constant.getWwwDir() + dir, name_txt,
					Html2txt.HtmlPage2txt(new StringBuffer(wwwpage)));
			logger.debug("StoreFile txt: " + sf2);

		}
		return name_txt;
	}
}
