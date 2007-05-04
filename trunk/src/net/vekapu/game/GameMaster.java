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

import java.util.HashMap;
import java.util.Map;

import net.vekapu.CorrectNumberVO;
import net.vekapu.OwnNumbersVO;
import net.vekapu.ResultVO;
import net.vekapu.SettingsVO;
import net.vekapu.VekapuException;
import net.vekapu.util.Constant;
import net.vekapu.util.SettingsReader;

import org.apache.log4j.Logger;
import org.apache.log4j.PropertyConfigurator;

/**
 * Master class off the game rules.
 * 
 * Witch games are checked ? 
 * 
 * @author janne
 */
//TODO Muuta oikeiden numeroiden palautusmetodien paluu tyyppiksi: List
//TODO Tännepitäis vissiinkin saada päättely siitä mikä/mitkä pelit tarkistetaan.

public class GameMaster {
	static Logger logger = Logger.getLogger(GameMaster.class);
	
	private SettingsVO settingsVO = null;
	
	// Correct numbers of the game	(kind of cache)
	private Map<String, CorrectNumberVO> correct = new HashMap<String, CorrectNumberVO> ();

	
	public GameMaster(SettingsVO settingsVO) {
		logger.debug("Haetaan oikeat rivit. abManual = " + settingsVO.isManual());
		this.settingsVO = settingsVO;
	}

	public static void main(String s[]) {
		PropertyConfigurator.configure(Constant.getLog4JConfigFileName());
		logger.info("CorrectNumber main Start");

		try {
			SettingsReader pr = new SettingsReader();
			SettingsVO settingsVO = pr.getSettingsVO();

			GameMaster gm = new GameMaster(settingsVO);
			gm.test();
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}

	/**
	 * Eri pelien tarkistamin hoidetaan tän metodin kautta.
	 * (ehkä, siis kait se on niin, oiskohan ehkä järkevää ?? en tiä arvotaan)
	 * 
	 * @throws VekapuException
	 */
	public ResultVO checkGame(String game, ResultVO resultVO, OwnNumbersVO numbersVO) throws VekapuException {
		getGameCorrectNumbers(game);
		
		CorrectNumberVO correctNumVO = (CorrectNumberVO) correct.get(game);
		
		logger.debug(correctNumVO);
		
		Checker checker = new Checker();
		checker.setCorrectNumberVO(correctNumVO);
		
		// FIXME Pelityyppi kuntoon
		if (game.equalsIgnoreCase("jokeri")) {
			numbersVO.addCheckedGame2(game, checker.checkJokeri(numbersVO.getOwnLines(game)) );
		} else {
			numbersVO.addCheckedGame2(game,checker.tarkistaRivit(numbersVO.getOwnLines(game)));
		}
		numbersVO.setGameBest(game, checker.getBestResult());
		resultVO.addCorrectNumber(game, correctNumVO);
		
		return resultVO;
	}

	private void getGameCorrectNumbers(String game) throws VekapuException {
		CorrectNumberVO correctVO = null;
		
		// If empty for game
		if (!correct.containsKey(game)) {

			CorrectNumber correctNumber = new CorrectNumber(settingsVO,game);
			correctVO = correctNumber.getCorrectNumbers(game);			
			correct.put(game, correctVO);
		
		}		
					
		logger.info( correct.toString() );
	}


	private void test() {
		logger.info("CorrectNumber test Start");

		try {
//			tarkista("lotto");
//			getOikeaRiviLotto();
//			getLisaNumerot();
//			getLottoWeek();
//			getKierrosLotto();
			// getKierrosViking();
			// getOikeaRiviViking();
			// getOikeaRivi();

		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		logger.info("CorrectNumber test Stop");
	}

}
