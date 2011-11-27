///////////////////////////////////////////////////////////////////////////////
//
// Filename: ResultVO.java
//
// Author:   Janne Ilonen
// Project:  Vekapu
//
// Feedback: palaute@vekapu.net
//
// Purpose:  Transfer/Value object for Vekapu results.
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

import java.util.HashMap;
import java.util.Map;

import net.vekapu.util.Constant;

/**
 * Transfer/Value object for Vekapu results.
 * 
 * @author janne
 *
 */
public class ResultVO {
	
	private OwnNumbersVO ownNumbersVO = null;
	
	private Map<String, CorrectNumberVO> correct = new HashMap<String, CorrectNumberVO>();
	
	
	/**
	 * 
	 * @param ownNumbersVO
	 */
	public ResultVO(OwnNumbersVO ownNumbersVO) {
		this.ownNumbersVO = ownNumbersVO;
	}
	
	
	/**
	 * @param game
	 * @param correctNumberVO
	 */
	public void addCorrectNumber(String game,CorrectNumberVO correctNumberVO) {
		correct.put(game, correctNumberVO);
	}
		
	/**
	 * 
	 * @param game
	 * @return Games correct numbers.
	 */
	public CorrectNumberVO getCorrect(String game) {
	 
		return correct.get(game);
	}

	/**
	 * @return the ownNumbersVO
	 */
	public OwnNumbersVO getOwnNumbersVO() {
		return ownNumbersVO;
	}

	/**
	 * @return Returns the group.
	 */
	public String getGroup() {
		return ownNumbersVO.getGroup();
	}
		

	@Override
	public String toString() {
		String NEW_LINE = Constant.getLineSeparator();
		
		String ret = NEW_LINE;		
		ret += ownNumbersVO.toString();
		ret += NEW_LINE;	
		ret += correct.toString();

		return ret;
	}
}
