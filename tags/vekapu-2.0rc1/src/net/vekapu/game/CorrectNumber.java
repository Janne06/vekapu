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

import net.vekapu.CorrectNumberVO;
import net.vekapu.SettingsVO;
import net.vekapu.VekapuException;
import net.vekapu.util.Constant;
import net.vekapu.util.DayHelper;
import net.vekapu.util.StoreFile;
import net.vekapu.web.Html2txt;
import net.vekapu.web.PageLoader;

import org.apache.log4j.Logger;

/**
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

	protected Properties properties = new Properties();

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

	/**
	 * 
	 * @return
	 * @throws VekapuException
	 */
	protected String getManualFile() throws VekapuException {

		String fileName = Constant.getCorrectNumberFile();
		String checkWeek = "";

		logger.info("Haetaan tiedostoon '" + fileName
				+ "' määritelty oikea rivi.");
		try {
			properties.load(new FileInputStream(fileName));
			checkWeek = properties.getProperty("TarkistaKierros").trim();
			return checkWeek;
		} catch (IOException e) {
			logger.error("IOException", e);
			throw new VekapuException(e);
		}
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
