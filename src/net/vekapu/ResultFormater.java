///////////////////////////////////////////////////////////////////////////////
//
// Filename: ResultVO.java
//
// Author:   Janne Ilonen
// Project:  Vekapu
//
// Feedback: palaute@vekapu.net
//
// Purpose:  Vekapu results layout formater.
//
// (c) Copyright J.Ilonen, 2006-2007
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
 * Vekapu results layout formater.
 */

package net.vekapu;

import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import net.vekapu.game.Checker;
import net.vekapu.util.Constant;
import net.vekapu.util.DayHelper;

import org.apache.log4j.Logger;

/**
 * @author janne
 *
 */
public class ResultFormater {

	static Logger logger = Logger.getLogger(ResultFormater.class);
	
	private OwnNumbersVO ownNumbersVO = null;
	
	private Map<String, CorrectNumberVO> correct = new HashMap<String, CorrectNumberVO>();
	
	private Boolean server = Boolean.FALSE;
	private String NEW_LINE = Constant.getLineSeparator();
	
	/**
	 * 
	 */
	public ResultFormater(OwnNumbersVO ownNumbersVO, Boolean abServer) {
		this.ownNumbersVO = ownNumbersVO;
		this.server = abServer;
	}
	
	
	public void addCorrectNumber(String game,CorrectNumberVO correctNumberVO) {
		correct.put(game, correctNumberVO);
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
	
	public String printGame(String game) {
		// FIXME Tää metodi EI kuulu tähän luokkaan..
		if (correct.get(game) == null) {
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
		ret.append(correct.get(game).getGameRound());
		ret.append(" Arvontapäivä: " );
		ret.append(correct.get(game).getDate());
		ret.append(NEW_LINE);
		ret.append(NEW_LINE);
					
		// Oikea rivi & lisänumerot
		ret.append("Oikea " + game + " rivi: ");
		ret.append(correct.get(game).getCorrectNumbersString());
		
		ret.append(NEW_LINE);
		ret.append("Lisänumerot: ");
		ret.append(correct.get(game).getExtraNumbersString());
		ret.append(NEW_LINE);
		
		// Tarkistetut rivit
		//TODO tarkista loopissa.
		
		int size = 0;
		int i = 0;
		for (Iterator iter = ownNumbersVO.getOwnLines(game).iterator(); iter.hasNext();) {
			List element = (List) iter.next();
			int lkm = element.size();
			
			if (size != lkm) {
				ret.append(NEW_LINE);
				ret.append("Tarkistetaan " + lkm + " rastin rivit.");
				ret.append(NEW_LINE);
				size = lkm;
			}
			
			// Lotto
			int hit = 0;
			int extra = 0;
			// Jokeri
			int hitA = 0;
			int hitB = 0;
			ret.append("Rivi " + (i < 9 ? " " : "") + (i + 1) + ". | ");
							
			for (int j = 0; j < lkm; j++) {
				// Tähän pitää laittaa tiedot osumista

				List tulos = (List) ownNumbersVO.getCheckedGame(game).get(i);
				String apu = (String) tulos.get(j);
				
				List numbers =  (List) ownNumbersVO.getOwnLines(game).get(i);
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
				// FIXME Pelityyppin huomiointi järkeväx
				if (game.equalsIgnoreCase("jokeri")) {
					
					hitA = Checker.countJokeriA(tulos);
					hitB = Checker.countJokeriB(tulos);
					ret.append((j < lkm - 1) ? ", " : " | Osumia " + hitA + " + " + hitB);
					
				} else {
					ret.append((j < lkm - 1) ? ", " : " | Osumia " + hit + " + " + extra);
				}
				
			}
			
			// FIXME Voittoilmoitukset pitäis saada määriteltyjen filujen kautta.
			if (game.equalsIgnoreCase("jokeri")) {
				if (hitA > 1 || hitB > 1) {
					ret.append("  <==== VOITTO ====>");
				}
			} else {
				if (hit > 3) {
					ret.append(NEW_LINE + "  <==== VOITTO ====>");
				}
			}
			
			ret.append( NEW_LINE);
			i++ ;
		}
		return ret.toString();
	}
	
	// FIXME Tän voi kohta  poistaa turhana.
	public String __printJokeri(boolean header) {
		
		if (correct.get("jokeri") == null) {
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
		ret.append(correct.get("jokeri").getCorrectNumbersString());			
		ret.append(NEW_LINE + NEW_LINE);
		
		// Tarkistetut rivit
		int i = 0;
		for (Iterator iter = ownNumbersVO.getOwnLines("jokeri").iterator(); iter.hasNext();) {
			List element = (List) iter.next();

			int lkm = element.size();

			int hitA = 0;
			int hitB = 0;
			ret.append("Rivi " + (i < 9 ? " " : "") + (i + 1) + ". | ");
			
			for (int j = 0; j < lkm; j++) {
				// Tähän pitää laittaa tiedot osumista
				
				
				List tulos = (List) ownNumbersVO.getCheckedGame("jokeri").get(i);
				String apu = (String) tulos.get(j);
				
				List numbers =  (List) ownNumbersVO.getOwnLines("jokeri").get(i);
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
	
	@Override
	public String toString() {
		
		String ret = NEW_LINE + getHeader();
		ret += printGame("lotto") +printGame("jokeri") + printGame("viking");		
		
		return ret;
	}
}
