///////////////////////////////////////////////////////////////////////////////
//
// Filename: CorrectNumber.java
//
// Author:   Janne Ilonen
// Project:  Vekapu
//
// Purpose:  Giving correct lotto numbers.
//
// (c) Copyright J.Ilonen, 2003-2009
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
 * Master class off the correct game info (numbers)
 * 
 * @author janne
 */
package net.vekapu.game;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

import net.vekapu.VekapuException;
import net.vekapu.util.Constant;

import org.apache.log4j.Logger;

/**
 * 
 * Actual this in normal / typical 'props reader' class.
 * 
 * @author janne
 * 
 */
public class CorrectNumberManual {
	static Logger logger = Logger.getLogger(CorrectNumberManual.class);

	protected Properties properties = new Properties();
		

	/**
	 * @throws VekapuException 
	 */	
	public CorrectNumberManual() throws VekapuException {
		
		logger.info("Gettin Correct numbers from " + 
				Constant.getCorrectNumberFile() + "-file");
		getManualFile();

	}


	/**
	 * @param arg
	 * @throws VekapuException 
	 */
	public String getArg(String arg) throws VekapuException {
		String ret = "";
		try {
			ret = properties.getProperty(arg).trim();
		} catch (Exception e) {
			
			String msg = "Haettua atribuuttia '" + arg + "' ei löydy !!" + 
			Constant.getLineSeparator() + "Tarkista tiedosto: " + 
			Constant.getUserDir() +  Constant.getCorrectNumberFile();
			
			logger.error(msg,e);
			throw new VekapuException(msg,e);
		}
		return ret;

	}
	

	/**
	 * 
	 * @return
	 * @throws VekapuException
	 */
	private void getManualFile() throws VekapuException {

		String fileName = Constant.getCorrectNumberFile();

		logger.info("Haetaan tiedostoon '" + fileName
				+ "' määritelty oikea rivi.");
		try {
			properties.load(new FileInputStream(fileName));
		} catch (IOException e) {
			logger.error("IOException", e);
			throw new VekapuException(e);
		}
	}

}
