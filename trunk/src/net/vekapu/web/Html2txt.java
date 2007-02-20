///////////////////////////////////////////////////////////////////////////////
//
// Filename: Html2txt.java
//
// Author:   Janne Ilonen
// Project:  Vekapu
//
// Purpose:  Converting html-page to raw text.
//
// (c) Copyright J.Ilonen, 2006
//
// Used this sites as modell:
// http://www.rgagnon.com/howto.html
// http://www.rgagnon.com/javadetails/java-0307.html
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

package net.vekapu.web;

import org.apache.log4j.Logger;

/**
 * Converting html-page to raw text.
 * 
 * @author janne
 *
 */

public class Html2txt {

	static Logger logger = Logger.getLogger(Html2txt.class);

	public Html2txt() {

	}

	public static String HtmlPage2txt(StringBuffer html) {
		String rc = getText(html,0);
		rc = unescapeHTML(rc,0);
		return rc;
	}
	
	private static String getText(StringBuffer html,int f) {
		
		int i = 0, j = 0;

		i = html.indexOf("<", f);
		if (i > -1) {
			j = html.indexOf(">", i);
			j ++;


			if (j > i) {
				html.delete(i,j);
				
				if (i < html.length()) {
					return getText(html, i ); // recursive call
				}
			}
		}
		return html.toString();
	}
	
	/**
	 * http://64.18.163.122/rgagnon/howto.html
	 * Unescape HTML special characters from a String
	 * 
	 * http://64.18.163.122/rgagnon/javadetails/java-0307.html
	 * 
	 * TODO UTF-8 may afect some problems.
	 * 
	 * @param s
	 * @param f
	 * @return
	 */
	private static final String unescapeHTML(String s, int f) {
		String[][] escape = { { "&lt;", "<" }, { "&gt;", ">" },
				{ "&amp;", "&" }, { "&quot;", "\"" }, { "&agrave;", "�" },
				{ "&Agrave;", "�" }, { "&acirc;", "�" }, { "&auml;", "�" },
				{ "&Auml;", "�" }, { "&Acirc;", "�" }, { "&aring;", "�" },
				{ "&Aring;", "�" }, { "&aelig;", "�" }, { "&AElig;", "�" },
				{ "&ccedil;", "�" }, { "&Ccedil;", "�" }, { "&eacute;", "�" },
				{ "&Eacute;", "�" }, { "&egrave;", "�" }, { "&Egrave;", "�" },
				{ "&ecirc;", "�" }, { "&Ecirc;", "�" }, { "&euml;", "�" },
				{ "&Euml;", "�" }, { "&iuml;", "�" }, { "&Iuml;", "�" },
				{ "&ocirc;", "�" }, { "&Ocirc;", "�" }, { "&ouml;", "�" },
				{ "&Ouml;", "�" }, { "&oslash;", "�" }, { "&Oslash;", "�" },
				{ "&szlig;", "�" }, { "&ugrave;", "�" }, { "&Ugrave;", "�" },
				{ "&ucirc;", "�" }, { "&Ucirc;", "�" }, { "&uuml;", "�" },
				{ "&Uuml;", "�" }, { "&nbsp;", " " }, { "&reg;", "\u00a9" },
				{ "&copy;", "\u00ae" }, { "&euro;", "\u20a0" } };
		int i, j, k ;

		i = s.indexOf("&", f);
		if (i > -1) {
			j = s.indexOf(";", i);
			// --------
			// we don't start from the beginning
			// the next time, to handle the case of
			// the &amp;
			// thanks to Pieter Hertogh for the bug fix!
			f = i + 1;
			// --------
			if (j > i) {
				// ok this is not most optimized way to
				// do it, a StringBuffer would be better,
				// this is left as an exercise to the reader!
				String temp = s.substring(i, j + 1);
				// search in escape[][] if temp is there
				k = 0;
				while (k < escape.length) {
					if (escape[k][0].equals(temp))
						break;
					else
						k++;
				}
				if (k < escape.length) {
					s = s.substring(0, i) + escape[k][1] + s.substring(j + 1);
					return unescapeHTML(s, f); // recursive call
				}
			}
		}
		return s;
	}
}
