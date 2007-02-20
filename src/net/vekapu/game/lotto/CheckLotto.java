///////////////////////////////////////////////////////////////////////////////
//
// Filename: CheckLotto.java
//
// Author:   Janne Ilonen
// Project:  Vekapu
//
// Feedback: palaute@vekapu.net
//
// Purpose:  Checkin lotto-lines.
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

package net.vekapu.game.lotto;

import net.vekapu.CorrectNumberVO;
import net.vekapu.OwnNumbersVO;
import net.vekapu.VekapuException;
import net.vekapu.game.Checker;

import org.apache.log4j.Logger;

/**
 * TODO Tarvitaanx tätä luokkaa ollenskaan ???
 * @author janne
 *
 */
public class CheckLotto extends Checker {
	private static Logger logger = Logger.getLogger(CheckLotto.class);

	
	public CheckLotto(CorrectNumberVO correctNumberVO) {
		setCorrectNumberVO(correctNumberVO);
	}

	public void tarkista(OwnNumbersVO numbersVO) throws VekapuException {

		logger.debug("Tarkistetaan ryhmän '" + numbersVO.getGroup() +"' lotto.");
		
		// Vekslata VO:n kautta.
		// Täähän toimii. Homma paketoituu aika paljo pienemmäks.
		
		numbersVO.addCheckedLotto( tarkistaRivit(numbersVO.getOwnLotto()) );
		numbersVO.setBestLotto(parasTulos);
	}

	public String getParasTulos() {
		return getBestResult();
	}
}
