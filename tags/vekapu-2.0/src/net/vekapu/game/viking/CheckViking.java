///////////////////////////////////////////////////////////////////////////////
//
// Filename: CheckViking.java
//
// Author:   Janne Ilonen
// Project:  Vekapu
//
// Feedback: palaute@vekapu.net
//
// Purpose:  Checkin Vikinglotto-lines.
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

package net.vekapu.game.viking;

import net.vekapu.CorrectNumberVO;
import net.vekapu.OwnNumbersVO;
import net.vekapu.VekapuException;
import net.vekapu.game.Checker;

import org.apache.log4j.Logger;

public class CheckViking extends Checker {
	static Logger logger = Logger.getLogger(CheckViking.class);

	public CheckViking(CorrectNumberVO correctNumberVO) {
		setCorrectNumberVO(correctNumberVO);		
	}
	
	public void tarkista(OwnNumbersVO numbersVO) throws VekapuException {
		
		logger.debug("Tarkistetaan ryhmän '" + numbersVO.getGroup() +"' Vikinglotto.");
		numbersVO.addCheckedViking( tarkistaRivit(numbersVO.getOwnViking()) );
	}

	public String getParasTulos() {
		return getBestResult();
	}

}