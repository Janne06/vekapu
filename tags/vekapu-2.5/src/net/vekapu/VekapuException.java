///////////////////////////////////////////////////////////////////////////////
//
// Filename: VekapuException.java
//
// Author:   Janne Ilonen
// Project:  Vekapu
//
// Purpose:  Vekapu exception handlin.
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

import net.vekapu.util.Constant;

/**
 * Vekapu exception handlin.
 * 
 * @author janne
 * 
 */
public class VekapuException extends Exception {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private boolean bugMail = true;


	/**
	 * @param message
	 */
	public VekapuException(String message) {
		this(message, false);
	}

	/**
	 * @param message
	 * @param cause
	 */
	public VekapuException(String message, Throwable cause) {
		super(message, cause);
		// TODO Auto-generated constructor stub
	}
	
	/**
	 * If bugMail = false not sendin bug report.
	 * It's normal action.
	 * 
	 * @param message   What's up.
	 * @param bugMail   If 'true' sendin email bug report. Defaut 'true'.
	 * @param cause     Original exception.
	 */
	public VekapuException(String message, boolean bugMail, Throwable cause) {
		this(message, cause);
		this.bugMail = bugMail;
		// TODO Auto-generated constructor stub
	}

	/**
	 * @param cause
	 */
	public VekapuException(Throwable cause) {
		super(cause);
		// TODO Auto-generated constructor stub
	}

	/**
	 * If bugMail = false not sendin bug report.
	 * It's normal action.
	 * 
	 * @param message What's up
	 * @param bugMail If 'true' sendin email bug report. Defaut 'true'.
	 */
	public VekapuException(String message, boolean bugMail) {
		super(message);
		this.bugMail = bugMail;

	}

	/**
	 * @return the bugMail
	 */
	public boolean isBugMail() {
		return bugMail;
	}

	/**
	 * 
	 */
	public String getInfo() {
		StringBuffer ret = new StringBuffer();
		String NEW_LINE = Constant.getLineSeparator();

		ret.append(super.toString());
		ret.append(NEW_LINE);
		ret.append(" bugMail : " + bugMail);

		return ret.toString();
	}
}
