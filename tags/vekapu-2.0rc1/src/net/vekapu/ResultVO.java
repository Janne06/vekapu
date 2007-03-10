///////////////////////////////////////////////////////////////////////////////
//
// Filename: ResultVO.java
//
// Author:   Janne Ilonen
// Project:  Vekapu
//
// Feedback: palaute@vekapu.net
//
// Purpose:  Transfer/Value object for Vekapu results.
//
// (c) Copyright J.Ilonen, 2006
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
 * Transfer/Value object for Vekapu results.
 */

package net.vekapu;

import java.util.Iterator;
import java.util.List;

import org.apache.log4j.Logger;

import net.vekapu.game.Checker;
import net.vekapu.util.Constant;
import net.vekapu.util.DayHelper;

/**
 * @author janne
 *
 */
public class ResultVO {

	static Logger logger = Logger.getLogger(ResultVO.class);
	
	private String lotto = "";
	private String lottobest = "";
	private String jokeri = "";
	private String jokeribest = "";
	private String viking = "";
	private String vikingbest = "";
	
	private OwnNumbersVO ownNumbersVO = null;
	// Pelien oikeat numerot
	private CorrectNumberVO correctLottoVO = null;
	private CorrectNumberVO correctJokeriVO = null;
	private CorrectNumberVO correctVikingLottoVO = null;
	
	private Boolean server = Boolean.FALSE;
	private String NEW_LINE = Constant.getLineSeparator();
	
	/**
	 * 
	 */
	public ResultVO(OwnNumbersVO ownNumbersVO, Boolean abServer) {
		this.ownNumbersVO = ownNumbersVO;
		this.server = abServer;
	}
	
	/**
	 * @param correctLottoVO the correctLottoVO to set
	 */
	public void setCorrectLottoVO(CorrectNumberVO correctLottoVO) {
		this.correctLottoVO = correctLottoVO;
	}

	public void setCorrectJokeriVO(CorrectNumberVO correctJokeriVO) {
		this.correctJokeriVO = correctJokeriVO;
	}
	
	public void setCorrectVikingLottoVO(CorrectNumberVO correctVikingLottoVO) {
		this.correctVikingLottoVO = correctVikingLottoVO;
	}
	/**
	 * @return the ownNumbersVO
	 */
	public OwnNumbersVO getOwnNumbersVO() {
		return ownNumbersVO;
	}


	/**
	 * @param ownNumbersVO the ownNumbersVO to set
	 */
	public void setOwnNumbersVO(OwnNumbersVO ownNumbersVO) {
		this.ownNumbersVO = ownNumbersVO;
	}

	/**
	 * @return Returns the group.
	 */
	public String getGroup() {
		return ownNumbersVO.getGroup();
	}

	/**
	 * @return Returns the jokeri.
	 */
	public String getJokeri() {
		return jokeri;
	}

	/**
	 * @return Returns the jokeribest.
	 */
	public String getJokeribest() {
		return jokeribest;
	}

	/**
	 * @return Returns the lotto.
	 */
	public String getLotto() {
		return lotto;
	}

	/**
	 * @return Returns the lottobest.
	 */
	public String getLottobest() {
		return lottobest;
	}

	/**
	 * @return Returns the viking.
	 */
	public String getViking() {
		return viking;
	}

	/**
	 * @return Returns the vikingbest.
	 */
	public String getVikingbest() {
		return vikingbest;
	}

	/**
	 * @param jokeri The jokeri to set.
	 */
	public void setJokeri(String jokeri) {
		this.jokeri = jokeri;
	}

	/**
	 * @param jokeribest The jokeribest to set.
	 */
	public void setJokeribest(String jokeribest) {
		this.jokeribest = jokeribest;
	}

	/**
	 * @param lotto The lotto to set.
	 */
	public void setLotto(String lotto) {
		this.lotto = lotto;
	}

	/**
	 * @param lottobest The lottobest to set.
	 */
	public void setLottobest(String lottobest) {
		this.lottobest = lottobest;
	}

	/**
	 * @param viking The viking to set.
	 */
	public void setViking(String viking) {
		this.viking = viking;
	}

	/**
	 * @param vikingbest The vikingbest to set.
	 */
	public void setVikingbest(String vikingbest) {
		this.vikingbest = vikingbest;
	}

	public String getHeader() {
		return getHeader(server);
	}
	
	public String getHeader(Boolean server) {
		StringBuffer tulos = new StringBuffer();

		tulos.append(Constant.getName() +
				" - Veikkaus apuri - versio " +
				Constant.getVersionNumber());
		tulos.append(NEW_LINE);
		tulos.append(Constant.getUrlHomePage());
		tulos.append(NEW_LINE + NEW_LINE);

		if (server.booleanValue()) {
			tulos.append("Tarkistettu rivi nyt myös netissä:");
			tulos.append(NEW_LINE);
			tulos.append(Constant.getUrlHomePage() + "group/" + ownNumbersVO.getGroup());
			tulos.append(NEW_LINE + NEW_LINE);
		}

		return tulos.toString();
	}
	
	/**
	 * String kierros
	 * Printing checked lottery.
	 * @return
	 */
	public String printLotto() {
		
		if (correctLottoVO == null) {
			// Liinat kiini
			return "";
		}
		StringBuffer ret = new StringBuffer();

		ret.append(NEW_LINE);
		
		// Lottoporuken tiedot
		ret.append("Lottoporukka: ");
		ret.append(ownNumbersVO.getGroup());
		ret.append(" ");
		ret.append("Voimassa: ");
		ret.append(ownNumbersVO.getUntil());
		ret.append(NEW_LINE);
		ret.append("Tarkastettu: ");
		ret.append(DayHelper.now());
		ret.append(NEW_LINE);
		ret.append(NEW_LINE);

		// Tarkistuksen tiedot
		ret.append("Kierros: ");
		ret.append(correctLottoVO.getGameweek());
		ret.append(" Arvontapäivä: " );
		ret.append(correctLottoVO.getDate());
		ret.append(NEW_LINE);
		ret.append(NEW_LINE);
					
		// Oikea rivi & lisänumerot
		ret.append("Oikea lotto rivi: ");
		ret.append(correctLottoVO.getCorrectNumbersString());
		
		ret.append(NEW_LINE);
		ret.append("Lisänumerot: ");
		ret.append(correctLottoVO.getExtraNumbersString());
		ret.append(NEW_LINE);
		
		// Tarkistetut rivit
		//TODO tarkista loopissa.
		
		int size = 0;
		int i = 0;
		for (Iterator iter = ownNumbersVO.getOwnLotto().iterator(); iter.hasNext();) {
			List element = (List) iter.next();
			int lkm = element.size();
			
			if (size != lkm) {
				ret.append(NEW_LINE);
				ret.append("Tarkistetaan " + lkm + " rastin rivit.");
				ret.append(NEW_LINE);
				size = lkm;
			}
			
			int hit = 0;
			int extra = 0;
			ret.append("Rivi " + (i < 9 ? " " : "") + (i + 1) + ". | ");
							
			for (int j = 0; j < lkm; j++) {
				// Tähän pitää laittaa tiedot osumista

				List tulos = (List) ownNumbersVO.getCheckedLotto().get(i);
				String apu = (String) tulos.get(j);
				
				List numbers =  (List) ownNumbersVO.getOwnLotto().get(i);
				String number = (String) numbers.get(j);
				
				ret.append((Integer.valueOf(number).intValue() < 10 ? " ":""));					
				
				if (apu.equals(Constant.getHit())) {
					
					ret.append(Constant.getHitOpen());
					ret.append(number);
					ret.append(Constant.getHitClose());
					hit ++;
				}
				else if (apu.equals(Constant.getExtra())) {
					
					ret.append(Constant.getExtraOpen());
					ret.append(number);
					ret.append(Constant.getExtraClose());
					extra ++;
				}
				else {
					ret.append(" ");
					ret.append(number);
					ret.append(" ");
				}
				ret.append((j < lkm - 1) ? ", " : " | Osumia " + hit + " + " + extra);
				
			}
			if (hit > 3) {
				ret.append(NEW_LINE + "  <==== VOITTO ====>");
			}
			
			ret.append( NEW_LINE);
			i++ ;
		}
		
		return ret.toString();
	}
	
	public String printJokeri(boolean header) {
		
		if (correctJokeriVO == null) {
			// Ei tehdä mitään
			return "";
		}
		StringBuffer ret = new StringBuffer();

		ret.append(NEW_LINE);
		
		if (header) {
			// Porukan tiedot
			ret.append("Jokeriporukka: ");
			ret.append(ownNumbersVO.getGroup());
			ret.append(" ");
			ret.append("Voimassa: ");
			ret.append(ownNumbersVO.getUntil());
			ret.append(NEW_LINE);
			ret.append("Tarkastettu: ");
			ret.append(DayHelper.now());
			ret.append(NEW_LINE);
			ret.append(NEW_LINE);
	
			// Tarkistuksen tiedot
			ret.append("Kierros: ");
	//		ret.append(correctJokeriVO.getGameweek());
	//		ret.append(" Arvontapäivä: " );
	//		ret.append(correctJokeriVO.getDate());
			ret.append(NEW_LINE);
			ret.append(NEW_LINE);
		}
			
		// Oikea rivi & lisänumerot
		ret.append("Oikea Jokeri rivi: ");
		ret.append(correctJokeriVO.getCorrectNumbersString());			
		ret.append(NEW_LINE + NEW_LINE);
		
		// Tarkistetut rivit
		int i = 0;
		for (Iterator iter = ownNumbersVO.getOwnJokeri().iterator(); iter.hasNext();) {
			List element = (List) iter.next();

			int lkm = element.size();

			int hitA = 0;
			int hitB = 0;
			ret.append("Rivi " + (i < 9 ? " " : "") + (i + 1) + ". | ");
			
			for (int j = 0; j < lkm; j++) {
				// Tähän pitää laittaa tiedot osumista
				
				
				List tulos = (List) ownNumbersVO.getCheckedJokeri().get(i);
				String apu = (String) tulos.get(j);
				
				List numbers =  (List) ownNumbersVO.getOwnJokeri().get(i);
				String number = (String) numbers.get(j);
				
				ret.append((Integer.valueOf(number).intValue() < 10 ? " ":""));					
				
				if (apu.equals(Constant.getHit())) {
					
					ret.append(Constant.getHitOpen());
					ret.append(number);
					ret.append(Constant.getHitClose());
				}					
				else {
					ret.append(" ");
					ret.append(number);
					ret.append(" ");
				}
				hitA = Checker.countJokeriA(tulos);
				hitB = Checker.countJokeriB(tulos);
				ret.append((j < lkm - 1) ? ", " : " | Osumia " + hitA + " + " + hitB);
				
			}
			if (hitA > 1 || hitB > 1) {
				ret.append("  <==== VOITTO ====>");
			}
			
			ret.append( NEW_LINE);
			i++ ;
		}
		
		return ret.toString();
	}
	
public String printViking() {
		
		if (correctVikingLottoVO == null) {
			// Liinat kiini
			return "";
		}
		StringBuffer ret = new StringBuffer();

		ret.append(NEW_LINE);
		
		// Lottoporuken tiedot
		ret.append("Vikinglottoporukka: ");
		ret.append(ownNumbersVO.getGroup());
		ret.append(" ");
		ret.append("Voimassa: ");
		ret.append(ownNumbersVO.getUntil());
		ret.append(NEW_LINE);
		ret.append("Tarkastettu: ");
		ret.append(DayHelper.now());
		ret.append(NEW_LINE);
		ret.append(NEW_LINE);

		// Tarkistuksen tiedot
		ret.append("Kierros: ");
		ret.append(correctVikingLottoVO.getGameweek());
		ret.append(" Arvontapäivä: " );
		ret.append(correctVikingLottoVO.getDate());
		ret.append(NEW_LINE);
		ret.append(NEW_LINE);
					
		// Oikea rivi & lisänumerot
		ret.append("Oikea Vikinglotto rivi: ");
		ret.append(correctVikingLottoVO.getCorrectNumbersString());
		
		ret.append(NEW_LINE);
		ret.append("Lisänumerot: ");
		ret.append(correctVikingLottoVO.getExtraNumbersString());
		ret.append(NEW_LINE);
		
		// Tarkistetut rivit
		//TODO tarkista loopissa.
		
		int size = 0;
		int i = 0;
		for (Iterator iter = ownNumbersVO.getOwnViking().iterator(); iter.hasNext();) {
			List element = (List) iter.next();
			int lkm = element.size();
			
			if (size != lkm) {
				ret.append(NEW_LINE);
				ret.append("Tarkistetaan " + lkm + " rastin rivit.");
				ret.append(NEW_LINE);
				size = lkm;
			}
			
			int hit = 0;
			int extra = 0;
			ret.append("Rivi " + (i < 9 ? " " : "") + (i + 1) + ". | ");
							
			for (int j = 0; j < lkm; j++) {
				// Tähän pitää laittaa tiedot osumista

				List tulos = (List) ownNumbersVO.getCheckedViking().get(i);
				String apu = (String) tulos.get(j);
				
				List numbers =  (List) ownNumbersVO.getOwnViking().get(i);
				String number = (String) numbers.get(j);
				
				ret.append((Integer.valueOf(number).intValue() < 10 ? " ":""));					
				
				if (apu.equals(Constant.getHit())) {
					
					ret.append(Constant.getHitOpen());
					ret.append(number);
					ret.append(Constant.getHitClose());
					hit ++;
				}
				else if (apu.equals(Constant.getExtra())) {
					
					ret.append(Constant.getExtraOpen());
					ret.append(number);
					ret.append(Constant.getExtraClose());
					extra ++;
				}
				else {
					ret.append(" ");
					ret.append(number);
					ret.append(" ");
				}
				ret.append((j < lkm - 1) ? ", " : " | Osumia " + hit + " + " + extra);
				
			}
			if (hit > 2) {
				ret.append(NEW_LINE + "  <==== VOITTO ====>");
			}
			
			ret.append( NEW_LINE);
			i++ ;
		}
		
		return ret.toString();
	}
	@Override
	public String toString() {

		String ret = NEW_LINE + getHeader() + printLotto() + printJokeri(false) + printViking();
		return ret;
	}
}
