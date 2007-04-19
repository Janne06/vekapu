///////////////////////////////////////////////////////////////////////////////
//
// Filename: OwnNumbers.java
//
// Author:   Janne Ilonen
// Project:  Vekapu
//
// Purpose:  Giving groups lottery numbers & group info.
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

import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Properties;
import java.util.Set;
import java.util.StringTokenizer;

import net.vekapu.OwnNumbersVO;
import net.vekapu.SettingsVO;
import net.vekapu.VekapuException;
import net.vekapu.util.Constant;

import org.apache.log4j.Logger;

/**
 * Handling groups lottery numbers & group info.
 * 
 * @author janne
 *
 */
public class OwnNumbers {
	static Logger logger = Logger.getLogger(OwnNumbers.class);
	private Properties properties = new Properties();
	private String fileName = "";
	protected OwnNumbersVO numbersVO = null;

	public OwnNumbers(String aFileName,SettingsVO settingsVO) throws VekapuException {
		
		fileName = Constant.getUserDir() +  Constant.getFileSeparator();
		numbersVO = new OwnNumbersVO(aFileName);

		// MODE SERVER
		if (settingsVO.isServer().booleanValue()) {
			logger.debug("isServer = true");
			fileName = settingsVO.getGroupDir() + Constant.getFileSeparator() + aFileName;
			logger.debug("fileName : " + fileName);
		} 
		
		//fileName = System.getProperty("user.dir") + 
		//		   fileName +
		//           Constant.getFileSeparator() + 
		fileName += Constant.getCouponDir() + 
			        aFileName + ".properties";

		logger.debug("fileName : " + fileName);
		
		// Read properties file.
		try {
			properties.load(new FileInputStream(fileName));
			logger.debug("File for own numbers & setting: " + fileName);
		} catch (IOException e) {
			// Mikäli filettä ei ole
			String msg = "Tiedostoa '" + fileName + "' ei löydy";
			logger.error(msg, e);
			throw new VekapuException(msg, false, e);
		}
	}

	/**
	 *  
	 * @return
	 */
	private void getTo() {
		int max = 0;
		List <String> to = new ArrayList <String> ();
		try {
			max = Integer.parseInt(properties.getProperty("to_count"));
		} catch (NumberFormatException nfe) {
			// if property isn'n number
			max = -1;
		}
		if (max < 1) {
			to.add(Constant.getEmptyAddress());
		}
		else
		{
			for (int i = 0; i < max; i++) {
				to.add(properties.getProperty("to_" + (i + 1)));
			}
		}		
		numbersVO.setTo(to);		
	}

	/**
	 * 
	 * @return
	 */
	private void getToSMS() {
		int max = 0;
		List <String> toSms = new ArrayList <String> ();
		try {
			max = Integer.parseInt(properties.getProperty("sms_count"));
		} catch (NumberFormatException nfe) {
			// if property isn'n number
			max = -1;
		}
		if (max < 1) {
			toSms.add(Constant.getEmptyAddress());
		}
		else
		{
			for (int i = 0; i < max; i++) {
				toSms.add(properties.getProperty("sms_" + (i + 1)));
			}
		}

		numbersVO.setToSms(toSms);
	}

	/**
	 * 
	 * @return
	 *
	 */
	private String getMihinAsti() {
		String asti = "";
		
		asti = properties.getProperty("until").trim();
		logger.debug("getMihinAsti(): " + asti);
	
		return asti;
	}

	private Set getGame() {

		String game = properties.getProperty("game");
		StringTokenizer toke = new StringTokenizer(game, ",");

		Set <String> games = new HashSet <String> ();
		
		while (toke.hasMoreTokens()) {
			games.add(toke.nextToken().trim());
		}
		
		logger.info("Own numbers for game: " + game + " - games: " + games);

		return games;
	}

	private boolean isJokeri() {
		boolean jokeri = false;

		if(getGame().contains("jokeri"))
			jokeri = true;

		return jokeri;
	}


	private boolean isViking() {
		boolean viking = false;

		if(getGame().contains("viking"))
			viking = true;

		return viking;
	}

	/**
	 * @return
	 */
	private boolean isLotto() {
		// TODO täähän pitää muuttaa erilaisex
		// TODO Lisä kuponkeihin arvo 'game='
		boolean lotto = false;

		if(getGame().contains("lotto"))
			lotto = true;

		return lotto;
	} 

	/**
	 * Get Own / group numbers & common info of the group
	 * @return
	 * @throws VekapuException 
	 */
	public OwnNumbersVO getOwnNumbers() throws VekapuException {	

		// Fill common info
		getTo();
		getToSMS();
		
		numbersVO.setUntil(getMihinAsti());
			
		// Fill Lotto
		numbersVO.setLotto(isLotto());
		if (isLotto()) {
			logger.info("Haetaan lotto-rivit & muut tiedot");			
			numbersVO.addLottoRivi(getOwnNumbers("lotto"));
			
		}
		
		// Fill Jokeri
		numbersVO.setJokeri(isJokeri());
		if (isJokeri()) {
			logger.info("Haetaan jokeri-rivit & muut tiedot");
			numbersVO.addJokeriRivi(getOwnNumbers("jokeri"));
			
		}
		
		// Fill Viking
		numbersVO.setViking(isViking());
		if (isViking()) {
			logger.info("Haetaan viikkari-rivit & muut tiedot");
			numbersVO.addVikingRivi(getOwnNumbers("viking"));
			
		}
				
		logger.debug(numbersVO.toString());
		
		return numbersVO;
	}
	
	
	private List getOwnNumbers(String game) {
		
		// TODO Jokerin suunta pitäis vielä saada tähän mukaan.
		
		// Haetaan yleishakuna
		// Lista mihin numerot ängetään.
		
		List rivit = new ArrayList();
		List lista = null;
		int lkm = Integer.parseInt(properties.getProperty(game));
		
		for (int i = 0; i < lkm; i++) {

			String rivi = properties.getProperty(game + "_" + (i + 1));
			StringTokenizer toke = new StringTokenizer(rivi, ",");
			
			lista = new ArrayList ();

			int numero=0;
			while (toke.hasMoreTokens()) {
				numero = Integer.parseInt(toke.nextToken().trim());				
				lista.add(String.valueOf(numero));
				
			}			
			// Laitetaankin suoraa OwnNumbersVO:hon. (kaiki samaan)
			rivit.add(lista);
		}
		return rivit;
	}
}
