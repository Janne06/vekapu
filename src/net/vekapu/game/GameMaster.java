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

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import net.vekapu.CorrectNumberVO;
import net.vekapu.OwnNumbersVO;
import net.vekapu.ResultVO;
import net.vekapu.SettingsVO;
import net.vekapu.VekapuException;
import net.vekapu.game.jokeri.CheckJokeri;
import net.vekapu.game.lotto.CheckLotto;
import net.vekapu.game.viking.CheckViking;
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
	private Map correct = Collections.synchronizedMap(new HashMap());

	
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
	public void tarkista(String game) throws VekapuException {
		getGameCorrectNumbers(game);
		
	}
	
		
	/**
	 * 
	 * @param resultVO
	 * @return
	 * @throws VekapuException
	 */
	public ResultVO checkLotto(ResultVO resultVO, OwnNumbersVO numbersVO) throws VekapuException {
		
//		alustaLotto();
		
		getGameCorrectNumbers("lotto");
		
		CorrectNumberVO cLottoVO = (CorrectNumberVO) correct.get("lotto");
		
		logger.debug(cLottoVO);
		
		CheckLotto lotto = new CheckLotto(cLottoVO);
//		CheckLotto lotto = new CheckLotto(clotto.correctNumberVO);

		lotto.tarkista(numbersVO);
		resultVO.setLottobest(lotto.getParasTulos());
		resultVO.setCorrectLottoVO(cLottoVO );
//		resultVO.setCorrectLottoVO((CorrectNumberVO) correct.get(CorrectLotto.GAME));
		
		return resultVO;
		
	}
	
	/**
	 * 
	 * @param resultVO
	 * @return
	 * @throws VekapuException
	 */
	public ResultVO checkJokeri(ResultVO resultVO, OwnNumbersVO numbersVO) throws VekapuException {
		
//		alustaJokeri();
		getGameCorrectNumbers("jokeri");
		
		CorrectNumberVO cJokeriVO = (CorrectNumberVO) correct.get("jokeri");
		
		logger.debug(cJokeriVO);
		
		CheckJokeri jokeri = new CheckJokeri(cJokeriVO);
		
		jokeri.tarkista(numbersVO);
		resultVO.setCorrectJokeriVO(cJokeriVO );
//		resultVO.setJokeri( jokeri.tarkista(resultVO.getGroup()).toString()) ;
//		resultVO.setJokeribest(jokeri.getBestResult());
		
		return resultVO;
		
	}
	
	/**
	 * 
	 * @param resultVO
	 * @return
	 * @throws VekapuException
	 */
	public ResultVO checkVikingLotto(ResultVO resultVO, OwnNumbersVO numbersVO) throws VekapuException {
		
//		alustaViking();
		getGameCorrectNumbers("viking-lotto");
		
		CorrectNumberVO cVikingVO = (CorrectNumberVO) correct.get("viking-lotto");
		logger.debug(cVikingVO);
		logger.debug(numbersVO);
		
		CheckViking vlotto = new CheckViking(cVikingVO);
//		CheckViking vlotto = new CheckViking(cviking);
		
		vlotto.tarkista(numbersVO);
		
		resultVO.setVikingbest(vlotto.getParasTulos());
		resultVO.setCorrectVikingLottoVO(cVikingVO );
		
//		resultVO.setViking( vlotto.tarkista(resultVO.getGroup()).toString()) ;
//		resultVO.setVikingbest(vlotto.getParasTulos());
		
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
			tarkista("lotto");
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
