///////////////////////////////////////////////////////////////////////////////
//
// Filename: OwnNumbersVO.java
//
// Author:   Janne Ilonen
// Project:  Vekapu
//
// Purpose:  Transfer/Value Object for numbers & group info.
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

package net.vekapu;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import net.vekapu.util.Constant;

import org.apache.log4j.Logger;

/**
 * Transfer/Value Object for numbers & group info.
 * 
 * @author janne
 *
 */
public class OwnNumbersVO {

	static Logger logger = Logger.getLogger(OwnNumbersVO.class);
	private String group = "";
	private String until = "";

	private List games = new ArrayList();
		
	private List<String> checkedGame = new ArrayList<String>();
		
	private List to = new ArrayList();
	private List toSms = new ArrayList();
		
	private Map<String, List> own = new HashMap<String, List>();
	private Map<String, List> checkedGame2 = new HashMap<String, List>();
	private Map<String, String> gameBest = new HashMap <String, String>();

	
	/**
	 * 
	 * @param aGroup
	 * @param games
	 */
	public OwnNumbersVO(String aGroup,List games) {
		this.group = aGroup;
		this.games = games;
	}

	/**
	 * @return
	 */
	public String getGroup() {
		return group;
	}
	
	/**
	 * @param game
	 * @return
	 */
	public boolean isGame(String game) {
		
		return games.contains(game);
	}
	
	/**
	 * @return
	 */
	public String getUntil() {
		return until;
	}

	/**
	 * @param mihinAstiLotto
	 */
	public void setUntil(String mihinAstiLotto) {
		this.until = mihinAstiLotto;
	}	
	
	/**
	 * @param game
	 * @param best
	 */
	public void setGameBest(String game,String best) {
		gameBest.put(game,best);
	}
	
	/**
	 * @param game
	 * @return
	 */
	public String getGameBest(String game) {
		String best = (String) gameBest.get(game);
		return best;
	}
	
	/**
	 * @param game
	 */
	public void addCheckegGame(String game) {
		checkedGame.add(game);
	}

	/**
	 * @return the to
	 */
	public List getTo() {
		return to;
	}

	/**
	 * @param to the to to set
	 */
	public void setTo(List to) {
		this.to = to;
	}

	/**
	 * @return the toSms
	 */
	public List getToSms() {
		return toSms;
	}

	/**
	 * @param toSms the toSms to set
	 */
	public void setToSms(List toSms) {
		this.toSms = toSms;
	}
	
	/**
	 * @param game
	 * @param lines
	 */
	public void addOwnLines(String game,List lines) {
		own.put(game, lines);
	}

	/**
	 * @param game
	 * @return
	 */
	public List getOwnLines(String game) {
		
		List list = (List) own.get(game);
		return list;
	}
		
	/**
	 * @param game
	 * @param lines
	 */
	public void addCheckedGame2(String game,List lines) {
		checkedGame2.put(game, lines);
	}
	
	/**
	 * @param game
	 * @return
	 */
	public List getCheckedGame(String game) {
		
		List list = (List) checkedGame2.get(game);
		return list;
	}
	

	/**
	 * Returns groups numbers & setteings at readable format.
	 */
	@Override
	public String toString() {

		String NEW_LINE = Constant.getLineSeparator();
		String ret = " OwnNumbersVO.toString():" + NEW_LINE;
		ret += " Common group settings:" + NEW_LINE;
		ret += "========================" + NEW_LINE;
		ret += " group : " + group + NEW_LINE;
		ret += " until : " + until + NEW_LINE;
		ret += " getTo() : " + getTo().toString() + NEW_LINE; 
		ret += " getToSms() : " + getToSms().toString() + NEW_LINE;		
		ret += NEW_LINE;
		ret += " Game numbers:" + NEW_LINE;
		ret += "========================" + NEW_LINE;
	
		for (int i = 0; i < games.size(); i++) {
			String game = (String) games.get(i);
			List list = getOwnLines(game);
			ret += "Game '" + game + "' has '" + list.size()+ "' lines." + NEW_LINE;
			
			if (isGameChecked(game)) ret += " Best results : " + getGameBest(game) + NEW_LINE;			
			ret += NEW_LINE;
			
			int j = 0;
			for (Iterator iter = list.iterator(); iter.hasNext();) {
				List element = (List) iter.next();
				ret += "Line "+ (j + 1) +":" +  element + NEW_LINE;
				
				if (isGameChecked(game)) {
					ret += "Hit:" +  getCheckedGame(game).get(j) + NEW_LINE + NEW_LINE;
				}
				j++ ;
			}			
			ret += NEW_LINE + "========================" + NEW_LINE;
		}
		return ret;		
	}

	/**
	 * @param game
	 * @return
	 */
	private boolean isGameChecked(String game) {
		return checkedGame.contains(game);
	}
	
}
