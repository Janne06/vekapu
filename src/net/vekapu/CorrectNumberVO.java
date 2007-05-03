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
 * Transfer/Value object for correct numbers.
 */
package net.vekapu;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import net.vekapu.util.Constant;

/** 
 * Transfer/Value object for correct numbers. One instance for one game round correct numbers.
 * 
 * @author janne
 *
 */
public class CorrectNumberVO {

	private String game = "";
	private String gameRound = "";
	private String date = "";
	private List<Integer> correctNumber = new ArrayList<Integer> ();
	private List<Integer> extraNumber = new ArrayList<Integer>();
	
	// TODO Pitäiskö myös kierros laittaa mukaan muodostimeen ????? Eipävän muuttuis turhaan.
	/**
	 * 
	 * @param game
	 */
	public CorrectNumberVO(String game,String gameRound) {
		this.game = game;
		this.gameRound = gameRound;
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
	 * @return Returns the gameRound.
	 */
	public String getGameRound() {
		return gameRound;
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
	 * @return Returns the date when correct line of game numbers is publiched.
	 */
	public String getDate() {
		return date;
	}

	/**
	 * @param date Set the date when correct line of game numbers is publiched.
	 */
	public void setDate(String date) {
		this.date = date;
	}

	/**
	 * Returns correct numbers at ','-separeted String. For printing.
	 * @return
	 */
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
	
	/**
	 * Returns extra numbers at ','-separeted String. For printing.
	 * @return
	 */
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
		 "game     : " + game + NEW_LINE +
		 "gameRound : " + gameRound + NEW_LINE +
		 "date     : " + date + NEW_LINE +
		 "Correct numbers : " + getCorrectNumbersString() + NEW_LINE +
		 "Extra numbers   : " + getExtraNumbersString() + NEW_LINE; 	    

		return rc;
	}
}
