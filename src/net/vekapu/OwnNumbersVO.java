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
import java.util.Collections;
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
	
	// Eri peleille yhteinen ??
	// TODO Tää luokka pitää saada joustavammax
	private List games = new ArrayList();
		
	// TODO Tarvisko tarkastuksesta samanlaisen ??
	private List checkedGame = new ArrayList();
	
	
	private String until = "";
	
	private String bestLotto = "?";
	
	private List to = new ArrayList();
	private List toSms = new ArrayList();
	
	
	// TODO Pitäiskö nää laittaa Map:piin josta sitten vois hakee pelikohtaset
	// tiedot jouheesti.
	private Map own = Collections.synchronizedMap(new HashMap());
	private Map correct = Collections.synchronizedMap(new HashMap());
	private Map checkedGame2 = Collections.synchronizedMap(new HashMap());

	
	/**
	 * 
	 */
	public OwnNumbersVO(String aGroup) {
		this.group = aGroup;
	}

	public String getGroup() {
		return group;
	}

	public void setGames(List games) {
		this.games = games;
	}
	
	public boolean isGame(String game) {
		
		return games.contains(game);
	}
	
	public String getUntil() {
		return until;
	}

	public void setUntil(String mihinAstiLotto) {
		this.until = mihinAstiLotto;
	}

	/**
	 * @return the bestLotto
	 */
	public String getBestLotto() {
		return bestLotto;
	}

	/**
	 * @param bestLotto the bestLotto to set
	 */
	public void setBestLotto(String bestLotto) {
		this.bestLotto = bestLotto;
	}
	
	
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
	
	public void addOwnLines(String game,List lines) {
		own.put(game, lines);
	}

	public List getOwnLines(String game) {
		
		List list = (List) own.get(game);
		return list;
	}
		
	public void addCheckedGame2(String game,List lines) {
		checkedGame2.put(game, lines);
	}
	
	public List getCheckedGame(String game) {
		
		List list = (List) checkedGame2.get(game);
		return list;
	}
	

	/**
	 * 
	 */
	@Override
	public String toString() {

		String NEW_LINE = Constant.getLineSeparator();
		String ret = " OwnNumbersVO.toString():" + NEW_LINE;
		ret += " Common group settings:" + NEW_LINE;
		ret += " ======================" + NEW_LINE;
		ret += " group : " + group + NEW_LINE;
		ret += " getTo() : " + getTo().toString() + NEW_LINE; 
		ret += " getToSms() : " + getToSms().toString() + NEW_LINE;
		
		ret += NEW_LINE;
		ret += " Game numbers:" + NEW_LINE;
		ret += " =============" + NEW_LINE;
		ret += " until : " + until + NEW_LINE;
		ret += " isLotto :" + isGame("lotto") + NEW_LINE;
	
		// TODO Tääkin pitää paketoida. Joustoa juttun.
		if (isGame("lotto")) {

			ret += " isLottoChecked :" + isGameChecked("lotto") + NEW_LINE;

			ret += "Rivit: " + getOwnLines("lotto").size() + " kpl" + NEW_LINE;	
			if (isGameChecked("lotto")) ret += " Parastulos : " + getBestLotto() + NEW_LINE;
			
			ret += NEW_LINE;
			
			int i = 0;
			for (Iterator iter = getOwnLines("lotto").iterator(); iter.hasNext();) {
				List element = (List) iter.next();
				ret += "Rivi "+ (i +1) +":" +  element + NEW_LINE;
				
				if (isGameChecked("lotto")) {
					ret += "Osumat:" +  getCheckedGame("lotto").get(i) + NEW_LINE + NEW_LINE;
				}
				i++ ;
			}					
		}
		
		ret += NEW_LINE + " =============" + NEW_LINE;
		ret += " isJokeri :" + isGame("jokeri") + NEW_LINE;
		if (isGame("jokeri")) {
			
			ret += " isJokeriChecked :" + isGameChecked("jokeri") + NEW_LINE;
			
			ret += "Rivit: " + getOwnLines("jokeri").size() + " kpl" + NEW_LINE;	
			
			ret += NEW_LINE;
			
			int i = 0;
			for (Iterator iter = getOwnLines("jokeri").iterator(); iter.hasNext();) {
				List element = (List) iter.next();
				ret += "Rivi "+ (i +1) +":" +  element + NEW_LINE;
				
				if (isGameChecked("jokeri")) {
					ret += "Osumat:" +  getCheckedGame("jokeri").get(i) + NEW_LINE + NEW_LINE;
				}
				i++ ;
			}		
		}
/*
		ret += " isViking :" + viking + NEW_LINE;
		if (isViking()) {
			ret += getVikingrivit().toString();
		}
	*/
		ret += NEW_LINE + " =============" + NEW_LINE;
		ret += " isViking :" + isGame("viking") + NEW_LINE;
		
		if (isGame("viking")) {
			
			ret += " isVikingChecked :" + isGameChecked("viking") + NEW_LINE;
			
			ret += "Rivit: " + getOwnLines("viking").size() + " kpl" + NEW_LINE;	
//			if (isVikingChecked()) ret += " Parastulos : " + getBestViking() + NEW_LINE;
			
			ret += NEW_LINE;
			
			int i = 0;
			for (Iterator iter = getOwnLines("viking").iterator(); iter.hasNext();) {
				List element = (List) iter.next();
				ret += "Rivi "+ (i +1) +":" +  element + NEW_LINE;
				
				if (isGameChecked("viking")) {
					ret += "Osumat:" +  getCheckedGame("viking").get(i) + NEW_LINE + NEW_LINE;
				}
				i++ ;
			}					
		}

		return ret;		
	}


	public boolean isGameChecked(String game) {
		return checkedGame.contains(game);
	}
	
}
