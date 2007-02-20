///////////////////////////////////////////////////////////////////////////////
//
// Filename: CheckJokeri.java
//
// Author:   Janne Ilonen
// Project:  Vekapu
//
// Feedback: palaute@vekapu.net
//
// Purpose:  Checkin jokeri-lines.
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

package net.vekapu.game.jokeri;

import net.vekapu.CorrectNumberVO;
import net.vekapu.OwnNumbersVO;
import net.vekapu.VekapuException;
import net.vekapu.game.Checker;

import org.apache.log4j.Logger;

public class CheckJokeri extends Checker {
	static Logger logger = Logger.getLogger(CheckJokeri.class);

	// Parastulos
	private int iiParasJokeri = 0;


	public CheckJokeri(CorrectNumberVO correctNumberVO) {
		setCorrectNumberVO(correctNumberVO);
	}
	
	
	public void tarkista(OwnNumbersVO numbersVO) throws VekapuException {

		logger.debug("Tarkistetaan ryhmän '" + numbersVO.getGroup() +"' jokeri.");
		
		// TODO Taitaa nää olla aivan turhia väli luokkia. Pitänee hankkiutua eroon.
		// Vekslata VO:n kautta.
		// Täähän toimii. Homma paketoituu aika paljo pienemmäks.

		numbersVO.addCheckedJokeri( checkJokeri(numbersVO.getOwnJokeri()) );
//		numbersVO.setBestLotto(parasTulos);
	}

	public int getBest() {
		return iiParasJokeri;
	}
}
