///////////////////////////////////////////////////////////////////////////////
//
// Filename: ResultFormater.java
//
// Author:   Janne Ilonen
// Project:  Vekapu
//
// Feedback: palaute@vekapu.net
//
// Purpose:  Vekapu results layout formater.
//
// (c) Copyright J.Ilonen, 2007-2011
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

package net.vekapu;

import java.util.Iterator;
import java.util.List;

import net.vekapu.game.Checker;
import net.vekapu.util.Constant;
import net.vekapu.util.DayHelper;

import org.apache.log4j.Logger;

/**
 * Vekapu results layout formater.
 * 
 * @author janne
 *
 */
public class ResultFormater {

	static Logger logger = Logger.getLogger(ResultFormater.class);
		
	private ResultVO resultVO = null;
	private Boolean server = Boolean.FALSE;
	private String NEW_LINE = Constant.getLineSeparator();
	
	/**
	 * 
	 */
	public ResultFormater(ResultVO resultVO, Boolean abServer) {
		this.resultVO = resultVO;
		this.server = abServer;
	}
		

	public String getHeader() {
		return getHeader(server);
	}
	
	private String getHeader(Boolean server) {
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
			tulos.append(Constant.getUrlHomePage() + "group/" + resultVO.getOwnNumbersVO().getGroup());
			tulos.append(NEW_LINE + NEW_LINE);
		}

		return tulos.toString();
	}
	
	public String printGame() {
		StringBuffer ret = new StringBuffer();
		List games = resultVO.getOwnNumbersVO().getGames();
		
		logger.debug(resultVO.toString());
		
		for (int count = 0; count < games.size(); count++) {
			String game = (String) games.get(count);
			String gametype = resultVO.getCorrect(game).getGameProps().getProperty("type");
			int win = Integer.parseInt( resultVO.getCorrect(game).getGameProps().getProperty("win") );
			int win_extra = 0;
			
			if (gametype.equals("lotto"))
				{
				win_extra = Integer.parseInt( resultVO.getCorrect(game).getGameProps().getProperty("win_extra") );
				}
			
			
			ret.append(NEW_LINE);
			
			// Lottoporuken tiedot
			ret.append("Lottoporukka: ");
			ret.append(resultVO.getOwnNumbersVO().getGroup());
			ret.append(" Peli: " + game);
			ret.append(" Voimassa: ");
			ret.append(resultVO.getOwnNumbersVO().getUntil());
			ret.append(NEW_LINE);
			ret.append("Tarkastettu: ");
			ret.append(DayHelper.now());
			ret.append(NEW_LINE);
			ret.append(NEW_LINE);
	
			// Tarkistuksen tiedot
			ret.append("Kierros: ");
			ret.append(resultVO.getCorrect(game).getGameRound());
			ret.append(" Arvontapäivä: " );
			ret.append(resultVO.getCorrect(game).getDate());
			ret.append(NEW_LINE);
			ret.append(NEW_LINE);
						
			// Oikea rivi & lisänumerot
			ret.append("Oikea " + game + " rivi: ");
			ret.append(resultVO.getCorrect(game).getCorrectNumbersString());
			ret.append(NEW_LINE);
			
			if (!resultVO.getCorrect(game).getGameProps().getProperty("extra").equals("")) {
				ret.append("Lisänumerot: ");
				ret.append(resultVO.getCorrect(game).getExtraNumbersString());
				ret.append(NEW_LINE);
			}
			
			int size = 0;
			int i = 0;
			for (Iterator iter = resultVO.getOwnNumbersVO().getOwnLines(game).iterator(); iter.hasNext();) {
				List element = (List) iter.next();
				int lkm = element.size();
				
				if (size != lkm) {
					ret.append(NEW_LINE);
					if (gametype.equals("jokeri")) {
						ret.append("Tarkistetaan " + game + " rivit.");
					} else {
						ret.append("Tarkistetaan " + lkm + " rastin " + game + " rivit.");
					}
					ret.append(NEW_LINE);
					size = lkm;
				}

				boolean winmsg = false;
				
				// Lotto
				int hit = 0;
				int extra = 0;
				
				// Jokeri
				boolean dirA = false;
				boolean dirB = false;
				boolean any = true; // Hit any number correct position
				String direction = "";
				String hitA = "-";
				String hitB = "-";
				
				if (gametype.equals("jokeri")) {
					List directions = (List) resultVO.getOwnNumbersVO().getOwnLines(game + "_direction").get(i);
					direction = directions.toString();
					if (direction.indexOf("a") > 0) dirA = true;
					if (direction.indexOf("b") > 0) dirB = true;
					
					if (dirA || dirB) any = false;
				}
				
				ret.append("Rivi " + (i < 9 ? " " : "") + (i + 1) + ". | ");

				for (int j = 0; j < lkm; j++) {
					// Tähän pitää laittaa tiedot osumista

					List tulos = (List) resultVO.getOwnNumbersVO().getCheckedGame(game).get(i);
					String apu = (String) tulos.get(j);
					
					List numbers =  (List) resultVO.getOwnNumbersVO().getOwnLines(game).get(i);
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

					if (gametype.equals("jokeri")) {
						if (any)
						{
							ret.append((j < lkm - 1) ? ", " : " | Osumia " + hit);
						}
						else
						{
							hitA = "-";
							hitB = "-";
							if (dirA) hitA = String.valueOf( Checker.countJokeriA(tulos) );
							if (dirB) hitB = String.valueOf( Checker.countJokeriB(tulos) );
							ret.append((j < lkm - 1) ? ", " : " | Osumia " + hitA + " / " + hitB);
						}
					} else {
						ret.append((j < lkm - 1) ? ", " : " | Osumia " + hit + " + " + extra);
					}
					
				}

				if (gametype.equals("jokeri")) {
					if (any)
					{
						if (hit >= win) winmsg = true;
					}
					else
					{
						ret.append(" suunta " + direction);
						if (dirA) {
							if (Integer.parseInt(hitA) >= win) winmsg = true;
						}
						if (dirB) {
							if (Integer.parseInt(hitB) >= win) winmsg = true;
						}
					}
				} else {
					if (hit > win) winmsg = true;
					else 
						if ((hit == win) && (extra >= win_extra)) winmsg = true;
				}
				
				if (winmsg) ret.append("  <==== VOITTO ====>");
				
				ret.append( NEW_LINE);
				i++ ;
			}
		}

		return ret.toString();
	}
	
	@Override
	public String toString() {
		
		String ret = NEW_LINE + getHeader();
		ret += printGame();
		
		return ret;
	}
}
