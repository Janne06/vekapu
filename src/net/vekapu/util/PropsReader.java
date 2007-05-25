///////////////////////////////////////////////////////////////////////////////
//
// Filename: PropsReader.java
//
// Author:   Janne Ilonen
// Project:  Vekapu
//
// Purpose:  Reading all props files.
//
// (c) Copyright J.Ilonen, 2007
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

package net.vekapu.util;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

import net.vekapu.VekapuException;

import org.apache.log4j.Logger;

/**
 * Reading all props files.
 * 
 * @author janne
 *
 */
public class PropsReader {

	static Logger logger = Logger.getLogger(PropsReader.class);
	
	/**
	 * 
	 */
	public PropsReader() {
		// TODO Auto-generated constructor stub
	}

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub

	}

	/**
	 * @param name Path & name to the props file. 
	 * @throws VekapuException
	 */
	public static Properties read(String name) throws VekapuException {

		if (!name.endsWith(".properties"))
			name += ".properties";
		
		Properties props = new Properties();
		
		try {
			props.load(new FileInputStream(name));
		} catch (IOException e) {
			logger.error("Here we are: " + System.getProperty("user.dir"));
			System.out.println(System.getProperty("user.dir"));
			logger.error("File: '" + name + "'. IOException", e);
			throw new VekapuException("File: '" + name, e);
		}
		
		return props;
	}
	
	public static String getGameType(String game) {

		String gameSettings = Constant.getGamePropsDir()+ game + ".properties";
		String type = "-";
		try {
			type = PropsReader.read(gameSettings).getProperty("type");
		} catch (VekapuException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return type;
	}
}
