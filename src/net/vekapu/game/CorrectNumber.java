///////////////////////////////////////////////////////////////////////////////
//
// Filename: CorrectNumber.java
//
// Author:   Janne Ilonen
// Project:  Vekapu
//
// Purpose:  Giving correct lottery numbers.
//
// (c) Copyright J.Ilonen, 2003 =>
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
 * Lottery game info. Getting web-page containing correct numbers.
 * 
 * @author janne
 */
package net.vekapu.game;

import java.util.Properties;
import java.util.StringTokenizer;

import net.vekapu.CorrectNumberVO;
import net.vekapu.SettingsVO;
import net.vekapu.VekapuException;
import net.vekapu.util.Constant;
import net.vekapu.util.DayHelper;
import net.vekapu.util.PropsReader;
import net.vekapu.util.SettingsReader;
import net.vekapu.util.StoreFile;
import net.vekapu.web.Html2txt;
import net.vekapu.web.PageLoader;

import org.apache.log4j.Logger;
import org.apache.log4j.PropertyConfigurator;

/**
 * Getting correct numbers of the game.
 * 
 * @author janne
 * 
 */
public class CorrectNumber {
	static Logger logger = Logger.getLogger(CorrectNumber.class);

	private SettingsVO settingsVO = null;

	/*
	 * What day is it ? => What week ??
	 */
	protected DayHelper dayhelper = new DayHelper();
	

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
			
			String allgames = Constant.getGamePropsDir()+ "games.properties";
			Properties games = PropsReader.read(allgames);
			
			int max = Integer.parseInt(games.getProperty("count"));
			
			for (int i = 0; i < max; i++) {
				String game = games.getProperty("game_" + (i + 1));
				
				CorrectNumber cn = new CorrectNumber(settingsVO,game);
				cn.getCorrectNumbers(game);	
			}
			
			
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
	 * What is current week number ??
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
		setCheckedRound(dayhelper.getYear() + "-" + dayhelper.getLastWeek());
		setWeek(dayhelper.getLastWeek());
	}

	public CorrectNumberVO getCorrectNumbers(String game) throws VekapuException {

		String name = game;
		String name_txt = "";
		String dir = game + Constant.getFileSeparator();
		String sivu = "";
		
		Properties gameProps = new Properties();
		String gameSettings = Constant.getGamePropsDir()+ game + ".properties";
		gameProps = PropsReader.read(gameSettings);
		
		// -------------------------------------------------------------------------------------
		CorrectNumberVO l_correctNumberVO = new CorrectNumberVO(game,getSettingsVO().getWeek());
		
		String oikeat = "";
		String lisat = "";

		///////////////////////////////////////////////////////////////////////////////////////
		if (!settingsVO.isManual()) {
			
			// Checked week 
			logger.debug("getSettingsVO().getCorrect() : " + getSettingsVO().getCorrect());
			if (getSettingsVO().getCorrect().equals("auto")) {
				
				// ===============================================================
				// Deduce checked round
				// 
				int day = Integer.valueOf( gameProps.getProperty("day") ).intValue();
				
				logger.info("Lottery game day: " + day + " today : " + dayhelper.getWeekDayNumber() + 
						" so next log line is telling what week is corect (current/last):");
				
				if ( day <= dayhelper.getWeekDayNumber()) {
					logger.info("Current week");
					currentWeek();
				} else {
					logger.info("Last week");
					lastWeek();
				}
			}
			
			// ---------------------------------------------------------------------------------
			l_correctNumberVO = new CorrectNumberVO(game,getSettingsVO().getWeek());
			l_correctNumberVO.setGameProps(gameProps);
			
			// Haetaan sivu missä oikeat numrot. 
			name_txt = getPage(name, gameProps.getProperty("url"));
			
			logger.debug("getSettingsVO().getCorrect() : " + getSettingsVO().getCorrect());
			logger.debug("getSettingsVO().getKierros() : " + getSettingsVO().getWeek());
			logger.debug("Name: " + name_txt);	
			
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
		} else {
			l_correctNumberVO.setGameProps(gameProps);
		}
		
		if (!settingsVO.isManual()) {
			try {
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
				
				oikeat = sivu.substring(alku + start, alku + end).trim();
				
				start = Integer.valueOf( gameProps.getProperty("startExtraFirstPosition") );
				logger.debug("startExtra: " + gameProps.getProperty("startExtra"));
				int lisanro = sivu.indexOf(gameProps.getProperty("startExtra"),start);
				start = Integer.valueOf( gameProps.getProperty("extraStartPosition") ).intValue();
				end = Integer.valueOf( gameProps.getProperty("extraEndPosition") ).intValue();
				
				logger.debug("lisanro: " + lisanro);
				lisat = sivu.substring(lisanro + start, lisanro + end).trim();
				logger.debug("lisat: " + lisat);
				lisat = lisat.replace(':', ' ').trim();
				
		
				l_correctNumberVO.setDate(pvm);
				
			} catch (RuntimeException re) {
				
				logger.error(re);
				String messu = "TextiTV:n sivut on varmaankin muuttuneet ?? Ongelmia pelissä: " + game;
				throw new VekapuException(messu,re);
			}
		} else {
			// Manual mode
			CorrectNumberManual manual = new CorrectNumberManual();
			oikeat = manual.getArg("correct_" + game);
			if (game != "jokeri") lisat = manual.getArg("extra_" + game);
			l_correctNumberVO.setDate(manual.getArg("info_" + game));
		}
	
		logger.info("oikeat: " + oikeat);
		logger.info("lisat: " + lisat);
		
		String delimeter = ",";
		if (oikeat.indexOf(delimeter) < 0) delimeter = " ";
				
		StringTokenizer toke;
		try {
			toke = new StringTokenizer(oikeat, delimeter);
		} catch (Exception e) {
			logger.error(e);
			String msg = "TextiTV:n sivut on varmaankin muuttuneet ?? Ongelmia pelissä: " + game;
			logger.error(msg);
			throw new VekapuException(msg,e);
		}
				
		Integer number = null;
		try {
			while (toke.hasMoreTokens()) {
				number = Integer.valueOf(toke.nextToken().trim());
				l_correctNumberVO.addCorrectNumber(number);
			}
		} catch (Exception e) {
			logger.error(e);
			String msg = "TextiTV:n sivut on varmaankin muuttuneet ?? Ongelmia pelissä: " + game;
			throw new VekapuException(msg,e);
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
	 * Here we get local copy of web page which contains correct numbers.
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

		String dir = name + Constant.getFileSeparator();
		
		logger.debug("name: " + name);
		logger.debug("getSettingsVO().getCorrect(): "
				+ getSettingsVO().getCorrect());
		logger.debug("getSettingsVO().getCheckedRound(): "
				+ getSettingsVO().getCheckedRound());

		name = name + "-" + getSettingsVO().getCheckedRound();
		logger.debug("name: " + name);

		String name_txt = name + ".txt";
		name = name + Constant.getWwwFileExt();
		String fullname = Constant.getWwwDir() + dir + name;
		String userdir = System.getProperty("user.dir");
		
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
		// netistä ainakaan tekstiTV:n sivuilta.
		//  
		// FIXME Joskus vois ehkä hakee kopion vekapun omilta sivuilta !! 
		// Vois olla aika hyvä. Tai sitten tulis vaan yks turha ylläpidettävä osa lisää :(
		
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
