///////////////////////////////////////////////////////////////////////////////
//
// Filename: CorrectNumberVO.java
//
// Author:   Janne Ilonen
// Project:  Vekapu
//
// Feedback: palaute@vekapu.net
//
// Purpose:  Transfer/Value object for correct numbers.
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
 * Transfer/Value object for correct numbers.
 */
package net.vekapu;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import net.vekapu.util.Constant;

public class CorrectNumberVO {

	private String game = "";
	private String gameweek = "";
	private String date = "";
	private List correctNumber = new ArrayList();
	private List extraNumber = new ArrayList();
	
	public CorrectNumberVO(String game) {
		this.game = game;
	}

	/**
	 * @return Returns the correctNumber.
	 */
	public List getCorrectNumber() {
		return correctNumber;
	}

	/**
	 * @return Returns the extraNumber.
	 */
	public List getExtraNumber() {
		return extraNumber;
	}

	/**
	 * @return Returns the game.
	 */
	public String getGame() {
		return game;
	}

	/**
	 * @return Returns the gameweek.
	 */
	public String getGameweek() {
		return gameweek;
	}

	/**
	 * @param correctNumber The correctNumber to set.
	 */
	public void addCorrectNumber(Integer number) {
		correctNumber.add(number);
	}

	/**
	 * @param extraNumber The extraNumber to set.
	 */
	public void addExtraNumber(Integer extra) {
		extraNumber.add(extra);
	}

	/**
	 * @param gameweek The gameweek to set.
	 */
	public void setGameweek(String gameweek) {
		this.gameweek = gameweek;
	}
	
	/**
	 * @return Returns the date.
	 */
	public String getDate() {
		return date;
	}

	/**
	 * @param date The date to set.
	 */
	public void setDate(String date) {
		this.date = date;
	}

	public String getCorrectNumbersString() {
		String rs ="";
		int i = 0;
		int count = correctNumber.size();
		for (Iterator iter = correctNumber.iterator(); iter.hasNext();) {
			Integer element = (Integer) iter.next();
			rs = rs + element.toString() + (i < (count - 1) ? ", " : "");
			i++;
		}
		
		return rs;
	}
	
	public String getExtraNumbersString() {
		String rs ="";
		int i = 0;
		int count = extraNumber.size();
		for (Iterator iter = extraNumber.iterator(); iter.hasNext();) {
			Integer element = (Integer) iter.next();
			rs = rs + element.toString() + (i < (count - 1) ? ", " : "");
			i++;
		}
		
		return rs;
	}
	
    /**
     * 
     */
	@Override
	public String toString() {
		String NEW_LINE = Constant.getLineSeparator();
		String rc = NEW_LINE + "CorrectNumberVO()" + NEW_LINE +
		 "game : " + game + NEW_LINE +
		 "gameweek : " + gameweek + NEW_LINE +
		 "date : " + date + NEW_LINE +
		 "Correct numbers : " + getCorrectNumbersString() + NEW_LINE +
		 "Extra numbers : " + getExtraNumbersString() + NEW_LINE; 	    

		return rc;
	}
}
