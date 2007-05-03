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
import java.util.List;
import java.util.Properties;
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
		
		numbersVO = new OwnNumbersVO(aFileName,getGame());
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
	private String getUntil() {
		String until = "";
		
		until = properties.getProperty("until").trim();
		logger.debug("getUntil(): " + until);
	
		return until;
	}

	private List getGame() {

		List <String> games = new ArrayList <String> ();
		
		String game = properties.getProperty("game");
		StringTokenizer toke = new StringTokenizer(game, ",");
		
		while (toke.hasMoreTokens()) {
			games.add(toke.nextToken().trim());
		}
		
		logger.info("Own numbers for game: " + game + " - games: " + games);

		return games;
	}

	/**
	 * Get Own / group numbers & common info of the group
	 * @return OwnNumbersVO
	 * @throws VekapuException 
	 */
	public OwnNumbersVO getOwnNumbers() throws VekapuException {	

		// Fill common info
		
		getTo();
		getToSMS();
		
		numbersVO.setUntil(getUntil());
		
		List games = getGame();

		for (int i = 0; i < games.size(); i++) {
			String gam = (String) games.get(i);
			logger.info("game '" + i + "': " + gam);
			numbersVO.addOwnLines(gam, getOwnGameNumbers(gam));			
		}

		logger.debug(numbersVO.toString());	
		return numbersVO;
	}
	
	
	private List getOwnGameNumbers(String game) {
		
		// TODO Jokerin suunta pitäis vielä saada tähän mukaan.
		
		List <List> rivit = new ArrayList <List> ();
		List <String> lista = null;
		int lkm = Integer.parseInt(properties.getProperty(game));
		
		for (int i = 0; i < lkm; i++) {

			String rivi = properties.getProperty(game + "_" + (i + 1));
			StringTokenizer toke = new StringTokenizer(rivi, ",");
			
			lista = new ArrayList <String> ();

			int numero=0;
			while (toke.hasMoreTokens()) {
				numero = Integer.parseInt(toke.nextToken().trim());				
				lista.add(String.valueOf(numero));
				
			}			

			rivit.add(lista);
		}
		return rivit;
	}
}
