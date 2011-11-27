///////////////////////////////////////////////////////////////////////////////
//
// Filename: GameMaster.java
//
// Author:   Janne Ilonen
// Project:  Vekapu
//
// Purpose:  Giving correct lotto numbers.
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

package net.vekapu.game;

import java.util.List;

import net.vekapu.CorrectNumberVO;
import net.vekapu.ResultVO;
import net.vekapu.SettingsVO;
import net.vekapu.VekapuException;
import net.vekapu.mail.Messenger;
import net.vekapu.util.Constant;
import net.vekapu.util.DayHelper;
import net.vekapu.util.PropsReader;

import org.apache.log4j.Logger;

/**
 * Master class off the game rules.
 * 
 * Witch games are checked ? 
 * 
 * @author janne
 */

public class GameMaster {
	static Logger logger = Logger.getLogger(GameMaster.class);
	
	private SettingsVO settingsVO = null;
	
	public GameMaster(SettingsVO settingsVO) {
		logger.debug("Haetaan oikeat rivit. abManual = " + settingsVO.isManual());
		this.settingsVO = settingsVO;
	}


	public ResultVO checkGame(ResultVO resultVO) throws VekapuException {
		try {
			
			boolean tarkista = false;
			
			
			// Eix sitä lähtökohtaisesti pitäis tarkistaa kaikki ??
			// Ainoastaan jos on cronitettu ja lähettää meilii niin sillon pitäis tutkii et päivä täsmää ?!!!?!!
			if (settingsVO.isTest().booleanValue()) {
				logger.info("test = yes ==> tarkistetaan kaikki");
				tarkista = true;
			}

			if (settingsVO.isManual().booleanValue()) {
				logger.info("manual = yes ==> tarkistetaan kaikki");
				tarkista = true;
			}

			if (!settingsVO.isCronJob().booleanValue()) {
				logger.info("CronJob = no ==> tarkistetaan kaikki");
				tarkista = true;
			}

			/*
			if (dayhelper.isSaturday() && settingsVO.isCronJob().booleanValue()) {
				logger.info("Lauantai cronilla == true ==> tarkistetaan "
						+ "lauantain pelit (Lotto & Jokeri)");
				tarkista = true;
			}
*/
			
			String gametype = "";
			logger.debug(resultVO);

			CorrectNumberVO correctNumVO = null;
			Checker checker = null;
			DayHelper dayhelper = new DayHelper();
			
			List<String> games = resultVO.getOwnNumbersVO().getGames();
			logger.debug(games);
			
			for (int i = 0; i < games.size(); i++) {
				String game = (String) games.get(i);
				logger.info("game '" + i + "': " + game);
			
				gametype = PropsReader.getGameType(game);
				logger.info("game: " + game + " & gametype: " + gametype);
				
				correctNumVO = getGameCorrectNumbers(game);	
				checker = new Checker(correctNumVO);
				
				if (dayhelper.isExpired(resultVO.getOwnNumbersVO().getUntil())) {
					
					if (settingsVO.isEmail()) {
						Messenger.sendEndMail(game, resultVO.getOwnNumbersVO().getGroup(),
								resultVO.getOwnNumbersVO().getTo(), settingsVO);
					} 
					
					// Then we throw this to info
					String msg = "Veikkausporukan '" + resultVO.getOwnNumbersVO().getGroup() + "' peli '" +
							game + "' on vanhentunut." + Constant.getLineSeparator() +
							"Tarkista valikosta 'Vekapu->Kupongit->" + resultVO.getOwnNumbersVO().getGroup() + 
							"' kohta 'until = '.";
					
					logger.warn(msg);
					throw new VekapuException(msg);
					
					
				} else if (gametype.equals("lotto")) {
					resultVO.getOwnNumbersVO().addCheckedGame2(game,checker.checkLotto(resultVO.getOwnNumbersVO().getOwnLines(game)));
				} else if (gametype.equals("jokeri")) {
					resultVO.getOwnNumbersVO().addCheckedGame2(game, checker.checkJokeri(resultVO.getOwnNumbersVO().getOwnLines(game)));
				} else {
					// Unknown game
					logger.error("Unknown game type :" + gametype);
					throw new VekapuException("Unknown game type :" + gametype);
				}
				
				resultVO.getOwnNumbersVO().setGameBest(game, checker.getBestResult());
				resultVO.addCorrectNumber(game, correctNumVO);

			}
						
			return resultVO;
			
		} catch (VekapuException ve) {
			logger.error(ve);
			throw ve;
		} 
	}

	
	/**
	 * 
	 * @param game
	 * @return
	 * @throws VekapuException
	 */
	private CorrectNumberVO getGameCorrectNumbers(String game) throws VekapuException {

		try {
			CorrectNumber correctNumber = new CorrectNumber(settingsVO,game);
			CorrectNumberVO correctVO = correctNumber.getCorrectNumbers(game);
			
			return correctVO;
		} catch (VekapuException ve) {
			logger.error(ve);
			throw ve;
		}
	}
}
