///////////////////////////////////////////////////////////////////////////////
//
// Filename: OwnNumbers.java
//
// Author:   Janne Ilonen
// Project:  Vekapu
//
// Purpose:  Giving groups lottery numbers & group info.
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

package net.vekapu.game;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import net.vekapu.OwnNumbersVO;
import net.vekapu.SettingsVO;
import net.vekapu.VekapuException;
import net.vekapu.game.jokeri.OwnJokeri;
import net.vekapu.game.lotto.OwnLotto;
import net.vekapu.game.viking.OwnViking;
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
		
		numbersVO = new OwnNumbersVO(aFileName);
		
		// MODE SERVER
		if (settingsVO.isServer().booleanValue()) {
			fileName = settingsVO.getGroupDir() + "/" + aFileName;
		} else {
			fileName = ".";
		}
		fileName += Constant.getCouponDir() + aFileName + ".properties";		

		// Read properties file.
		try {
			properties.load(new FileInputStream(fileName));
			logger.debug("File for own numbers & setting: " + fileName);
		} catch (IOException e) {
			// Mikäli filettä ei ole
			String msg = "Tiedostoa '" + fileName + "' ei löydy";
			logger.error(msg, e);
			throw new VekapuException(msg,e);
		}
	}

	/**
	 *  
	 * @return
	 */
	private void getTo() {
		int max = 0;
		List to = new ArrayList();
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
		List toSms = new ArrayList();
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
		logger.debug("getMihinAsti()");
		String asti = "";
		try {
			asti = properties.getProperty("asti").trim();
			logger.debug("asti: " + asti);
		} catch (NullPointerException ne) {
			// Rivinä on joko viikkari tai jokeri
			// Ainaski versioon 1.5.2 asti.
			// Taidan luottaa siihen että kyseessä on viikkari
			logger.warn("Oiskohan viikkari ...");
			try {
				asti = getMihinAstiViking();
				logger.warn("asti viking: " + asti);
			} catch (NullPointerException ne2) {
				asti = getMihinAstiJokeri();
				logger.warn("asti jokeri: " + asti);
			}
		}
		return asti;
	}

	private String getMihinAstiJokeri() {
		return properties.getProperty("jokeri_asti").trim();
	}

	private void isStoreResult() {
		boolean rc = false;

		String cron = properties.getProperty("storeresult", "no");
		if (cron.equalsIgnoreCase("yes"))
			rc = true;

		numbersVO.setStoreResult(rc);
	}

	private boolean isJokeri() {
		boolean jokeri = true;

		String ok = properties.getProperty("jokeri");
		if (ok == null)
			jokeri = false;

		return jokeri;
	}

	private String getMihinAstiViking() {
		return properties.getProperty("viking_asti").trim();
	}

	private boolean isViking() {
		boolean viking = false;

		String ok = properties.getProperty("viking_asti");
		if (ok != null)
			viking = true;

		return viking;
	}

	/**
	 * @return
	 */
	private boolean isLotto() {
		boolean lotto = false;

		String ok = properties.getProperty("asti");
		if (ok != null)
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
		isStoreResult();
			
		// Fill Lotto
		numbersVO.setLotto(isLotto());
		if (isLotto()) {
			logger.info("Haetaan lotto-rivit & muut tiedot");
			numbersVO.setMihinAstiLotto(getMihinAsti());	
			
			// TODO Nyt pitäis kutsua OwnLotto luokkaa mikä sitten palauttaa 
			// Set:issä (?) omat lottorivit.
			OwnLotto lotto = new OwnLotto(properties);
			
			numbersVO.addLottoRivi(lotto.getRivit());
		}
			
		
		// Fill Jokeri
		numbersVO.setJokeri(isJokeri());
		if (isJokeri()) {
			logger.info("Haetaan jokeri-rivit & muut tiedot");
			numbersVO.setMihinAstiJokeri(getMihinAstiJokeri());
			
			OwnJokeri jokeri = new OwnJokeri(properties);
			numbersVO.addJokeriRivi(jokeri.getRivit());			
		}

		
		// Fill Viking
		numbersVO.setViking(isViking());
		if (isViking()) {
			logger.info("Haetaan viikkari-rivit & muut tiedot");
			numbersVO.setMihinAstiViking(getMihinAstiViking());
			
			OwnViking viking = new OwnViking(properties);
			numbersVO.addVikingRivi(viking.getRivit());	
		}

		logger.debug(numbersVO.toString());
		
		return numbersVO;
	}
}
