//////////////////////////////////////////////////////////////////////////////
//
// Filename: Constant.java
//
// Author:   Janne Ilonen
// Project:  Vekapu
//
// Purpose:  Project constants.
//
// (c) Copyright J.Ilonen, 2003-2011
//
// $Id$
//
//////////////////////////////////////////////////////////////////////////////
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
//////////////////////////////////////////////////////////////////////////////

package net.vekapu.util;

/**
 * Project constants.
 * 
 * Onkohan tää nyt kuitenkaan ihan järkevää ängetä kaikki vakiot samaan luokkaan ??
 */
public class Constant {
	private final static String NAME = "Vekapu";

	/*
	 Päivitä molemmat (versio & VERSION) numerot AINA SAMOIKSI. 
	 versio = 2.6
	 */
	private final static String VERSION = "2.6";

	private final static String URL = "http://www.vekapu.net/";

	private final static String EMAIL = "vekapu@vekapu.net";
	private final static String EMAIL_BUGS = "bugs@vekapu.net";

	private final static String HEADER = "[Vekapu]";

	private final static String FILE_MANUAL = "correctnumbers.properties";

	private final static String LOG4J_CONFIGURE = "log4j.properties";

	//
	private final static String NEW_LINE = System.getProperty("line.separator");
	private final static String FILE_SEPARATOR = System.getProperty("file.separator");
	private final static String USER_DIR = System.getProperty("user.dir");

	//
	private final static String HIT = "X";

	private final static String EXTRA = "O";

	private final static String MISS = "-";

	//
	private final static String HIT_OPEN = "(";

	private final static String HIT_CLOSE = ")";

	private final static String EXTRA_OPEN = "[";

	private final static String EXTRA_CLOSE = "]";

	// Default file append
	private final static String BEST_DIR = FILE_SEPARATOR + "best" + FILE_SEPARATOR;

	private final static String BEST_APP = ".txt";

	// Tää eteen niin pelittää näillä etuliite virityksillä
	private final static String WWW_DIR = "." + FILE_SEPARATOR + "correctnumbers" + FILE_SEPARATOR;

	private final static String WWW_APP = ".html";

	private final static String RESULT_DIR = FILE_SEPARATOR + "checked" + FILE_SEPARATOR;

	private final static String RESULT_APP = ".txt";

	// 
	private final static String COUPON_DIR = "." + FILE_SEPARATOR + "coupon" + FILE_SEPARATOR;

	private final static String GAME_DIR = "." + FILE_SEPARATOR + "game" + FILE_SEPARATOR;

	
	public static String getName() {
		return NAME;
	}

	public static String getVersionNumber() {
		return VERSION;
	}

	public static String getUrlHomePage() {
		return URL;
	}

	public static String getDefaultEMail() {
		return EMAIL;
	}

	public static String getBugMail() {
		return EMAIL_BUGS;
	}
	
	public static String getEmailHeaderPrefix() {
		return HEADER;
	}

	public static String getCorrectNumberFile() {
		return WWW_DIR + FILE_MANUAL;
	}

	public static String getLog4JConfigFileName() {
		return LOG4J_CONFIGURE;
	}

	public static String getLineSeparator() {
		return NEW_LINE;
	}
	
	public static String getFileSeparator() {
		return FILE_SEPARATOR;
	}
	
	public static String getUserDir() {
		return USER_DIR;
	}

	public static String getHitOpen() {
		return HIT_OPEN;
	}

	public static String getHitClose() {
		return HIT_CLOSE;
	}

	public static String getExtraOpen() {
		return EXTRA_OPEN;
	}

	public static String getExtraClose() {
		return EXTRA_CLOSE;
	}

	public static String getBestDir() {
		return BEST_DIR;
	}

	public static String getBestFileExt() {
		return BEST_APP;
	}

	public static String getWwwDir() {
		return WWW_DIR;
	}

	public static String getWwwFileExt() {
		return WWW_APP;
	}

	public static String getResultDir() {
		return RESULT_DIR;
	}

	public static String getResultFileExt() {
		return RESULT_APP;
	}

	public static String getCouponDir() {
		return COUPON_DIR;
	}

	public static String getGamePropsDir() {
		return GAME_DIR;
	}

	public static String getEmptyAddress() {
		return "-";
	}

	/**
	 * @return the eXTRA
	 */
	public static String getExtra() {
		return EXTRA;
	}

	/**
	 * @return the hIT
	 */
	public static String getHit() {
		return HIT;
	}

	/**
	 * @return the mISS
	 */
	public static String getMiss() {
		return MISS;
	}
	
	
}
