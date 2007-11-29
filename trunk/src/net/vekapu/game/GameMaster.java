///////////////////////////////////////////////////////////////////////////////
//
// Filename: GameMaster.java
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

package net.vekapu.game;

import net.vekapu.CorrectNumberVO;
import net.vekapu.OwnNumbersVO;
import net.vekapu.ResultVO;
import net.vekapu.SettingsVO;
import net.vekapu.VekapuException;
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

	/**
	 * Eri pelien tarkistamin hoidetaan tän metodin kautta.
	 * 
	 * @param game
	 * @param resultVO
	 * @param numbersVO
	 * @return
	 * @throws VekapuException
	 */
	public ResultVO checkGame(String game, ResultVO resultVO, OwnNumbersVO numbersVO) throws VekapuException {
		
		try {
			CorrectNumberVO correctNumVO = getGameCorrectNumbers(game);	
			
			Checker checker = new Checker(correctNumVO);
	
			String gametype = PropsReader.getGameType(game);
			logger.info("game: " + game + " & gametype: " + gametype);
	
			if (gametype.equals("jokeri")) {
				numbersVO.addCheckedGame2(game, checker.checkJokeri(numbersVO.getOwnLines(game)));
			} else {
				numbersVO.addCheckedGame2(game,checker.checkLotto(numbersVO.getOwnLines(game)));
			}
			numbersVO.setGameBest(game, checker.getBestResult());
			resultVO.addCorrectNumber(game, correctNumVO);
			
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
