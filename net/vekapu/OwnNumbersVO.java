///////////////////////////////////////////////////////////////////////////////
//
// Filename: OwnNumbersVO.java
//
// Author:   Janne Ilonen
// Project:  Vekapu
//
// Purpose:  Transfer/Value Object for numbers & group info.
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

package net.vekapu;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import net.vekapu.util.Constant;

import org.apache.log4j.Logger;

public class OwnNumbersVO {

	static Logger logger = Logger.getLogger(OwnNumbersVO.class);
	private String group = "";
	
	// Eri peleille yhteinen ??
	private boolean lotto = false;
	private boolean jokeri = false;
	private boolean viking = false;
	
	// TODO Tarvisko tarkastuksesta samanlaisen ??
	private boolean lottoChecked = false;
	private boolean jokeriChecked = false;
	private boolean vikingChecked = false;
	
	private boolean storeResult = false;
		
	private String mihinAstiLotto = "";
	private String mihinAstiJokeri = "";
	private String mihinAstiViking = "";
	
	private String bestLotto = "?";
	
	private List to = new ArrayList();
	private List toSms = new ArrayList();
	
	// Asetetaan pariksi (tyyppi = List (rivi)) eli lotto7=List(omat lottorivit)).

	private Map jokeririvit = Collections.synchronizedMap(new HashMap());
	private Map vikingrivit = Collections.synchronizedMap(new HashMap());
	
	// Tää vaikuttaa hyvältä !!!
	private List ownLotto = new ArrayList();
	private List checkedLotto = new ArrayList();

	private List ownJokeri = new ArrayList();
	private List checkedJokeri = new ArrayList();

	private List ownViking = new ArrayList();
	private List checkedViking = new ArrayList();
	/**
	 * 
	 */
	public OwnNumbersVO(String aGroup) {
		this.group = aGroup;
	}

	public String getGroup() {
		return group;
	}

	public boolean isJokeri() {
		return jokeri;
	}

	public void setJokeri(boolean jokeri) {
		this.jokeri = jokeri;
	}

	public boolean isLotto() {
		return lotto;
	}

	public void setLotto(boolean lotto) {
		this.lotto = lotto;
	}

	public String getMihinAstiJokeri() {
		return mihinAstiJokeri;
	}

	public void setMihinAstiJokeri(String mihinAstiJokeri) {
		this.mihinAstiJokeri = mihinAstiJokeri;
	}

	public String getMihinAstiLotto() {
		// TODO Onx järkee ??
		String pk = "";
		if (mihinAstiLotto.equals("")) {
			pk = getMihinAstiViking();
		} else {
			pk = mihinAstiLotto;
		}
		return pk;
	}

	public void setMihinAstiLotto(String mihinAstiLotto) {
		this.mihinAstiLotto = mihinAstiLotto;
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

	public String getMihinAstiViking() {
		return mihinAstiViking;
	}

	public void setMihinAstiViking(String mihinAstiViking) {
		this.mihinAstiViking = mihinAstiViking;
	}
	
	public boolean isViking() {
		return viking;
	}

	public void setViking(boolean viking) {
		this.viking = viking;
	}

	/**
	 * @return the jokeririvit
	 */
	public Map getJokeririvit() {
		return jokeririvit;
	}

	/**
	 * @param jokeririvit the jokeririvit to set
	 */
	public void setJokeririvit(Map jokeririvit) {
		this.jokeririvit = jokeririvit;
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
	 * @return the vikingrivit
	 */
	public Map getVikingrivit() {
		return vikingrivit;
	}

	/**
	 * @param vikingrivit the vikingrivit to set
	 */
	public void setVikingrivit(Map vikingrivit) {
		this.vikingrivit = vikingrivit;
	}

	public void addLottoRivi(List rivi) {
		ownLotto.addAll(rivi);
	}
		
	/**
	 * @return the ownLotto
	 */
	public List getOwnLotto() {
		return ownLotto;
	}

	public void addCheckedLotto(List rivi) {
//		logger.debug("##rivi : " + rivi);
		checkedLotto.addAll(rivi);
		setLottoChecked(true);
	}

	public List getCheckedLotto() {
		return checkedLotto;
	}
	
	public void addJokeriRivi(List rivi) {
		ownJokeri.addAll(rivi);
	}
		
	public List getOwnJokeri() {
		return ownJokeri;
	}
	
	public void addCheckedJokeri(List rivi) {
//		logger.debug("##rivi : " + rivi);
		checkedJokeri.addAll(rivi);
		setJokeriChecked(true);
	}

	public List getCheckedJokeri() {
		return checkedJokeri;
	}
	
	// Viikkari
	public void addVikingRivi(List rivi) {
		ownViking.addAll(rivi);
	}
		
	/**
	 * @return the ownLotto
	 */
	public List getOwnViking() {
		return ownViking;
	}

	public void addCheckedViking(List rivi) {
//		logger.debug("##rivi : " + rivi);
		checkedViking.addAll(rivi);
		setVikingChecked(true);
	}

	public List getCheckedViking() {
		return checkedViking;
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
		ret += " isStoreResult " + isStoreResult() + NEW_LINE;
		
		ret += NEW_LINE;
		ret += " Game numbers:" + NEW_LINE;
		ret += " =============" + NEW_LINE;
		ret += " isLotto :" + lotto + NEW_LINE;
		
		if (isLotto()) {

			ret += " mihinAstiLotto : " + mihinAstiLotto + NEW_LINE;
			ret += " isLottoChecked :" + isLottoChecked() + NEW_LINE;

			ret += "Rivit: " + getOwnLotto().size() + " kpl" + NEW_LINE;	
			if (isLottoChecked()) ret += " Parastulos : " + getBestLotto() + NEW_LINE;
			
			ret += NEW_LINE;
			
			int i = 0;
			for (Iterator iter = getOwnLotto().iterator(); iter.hasNext();) {
				List element = (List) iter.next();
				ret += "Rivi "+ (i +1) +":" +  element + NEW_LINE;
				
				if (isLottoChecked()) {
					ret += "Osumat:" +  getCheckedLotto().get(i) + NEW_LINE + NEW_LINE;
				}
				i++ ;
			}					
		}
		
		ret += NEW_LINE + " =============" + NEW_LINE;
		ret += " isJokeri :" + jokeri + NEW_LINE;
		if (isJokeri()) {
			
			ret += " mihinAstiJokeri : " + mihinAstiJokeri + NEW_LINE;
			ret += " isJokeriChecked :" + isJokeriChecked() + NEW_LINE;
			
			ret += "Rivit: " + getOwnJokeri().size() + " kpl" + NEW_LINE;	
//			if (isLottoChecked()) ret += " Parastulos : " + getBestJokeri() + NEW_LINE;
			
			ret += NEW_LINE;
			
			int i = 0;
			for (Iterator iter = getOwnJokeri().iterator(); iter.hasNext();) {
				List element = (List) iter.next();
				ret += "Rivi "+ (i +1) +":" +  element + NEW_LINE;
				
				if (isJokeriChecked()) {
					ret += "Osumat:" +  getCheckedJokeri().get(i) + NEW_LINE + NEW_LINE;
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
		ret += " isViking :" + viking + NEW_LINE;
		
		
		if (isViking()) {
			
			ret += " mihinAstiViking : " + mihinAstiViking + NEW_LINE;
			ret += " isVikingChecked :" + isVikingChecked() + NEW_LINE;
			
			ret += "Rivit: " + getOwnViking().size() + " kpl" + NEW_LINE;	
//			if (isVikingChecked()) ret += " Parastulos : " + getBestViking() + NEW_LINE;
			
			ret += NEW_LINE;
			
			int i = 0;
			for (Iterator iter = getOwnViking().iterator(); iter.hasNext();) {
				List element = (List) iter.next();
				ret += "Rivi "+ (i +1) +":" +  element + NEW_LINE;
				
				if (isVikingChecked()) {
					ret += "Osumat:" +  getCheckedViking().get(i) + NEW_LINE + NEW_LINE;
				}
				i++ ;
			}					
		}
		return ret;		
	}

	/**
	 * @return the storeResult
	 */
	public boolean isStoreResult() {
		return storeResult;
	}

	/**
	 * @param storeResult the storeResult to set
	 */
	public void setStoreResult(boolean storeResult) {
		this.storeResult = storeResult;
	}

	/**
	 * @return the lottoChecked
	 */
	public boolean isLottoChecked() {
		return lottoChecked;
	}

	/**
	 * 
	 * @return
	 */
	public boolean isJokeriChecked() {
		return jokeriChecked;
	}

	/**
	 * @param lottoChecked the lottoChecked to set
	 */
	private void setLottoChecked(boolean lottoChecked) {
		this.lottoChecked = lottoChecked;
	}

	/**
	 * @param lottoChecked the lottoChecked to set
	 */
	private void setJokeriChecked(boolean jokeriChecked) {
		this.jokeriChecked = jokeriChecked;
	}

	public boolean isVikingChecked() {
		return vikingChecked;
	}
	
	private void setVikingChecked(boolean vikingChecked) {
		this.vikingChecked = vikingChecked;
	}
}
