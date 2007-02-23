///////////////////////////////////////////////////////////////////////////////
//
// Filename: VekapuException.java
//
// Author:   Janne Ilonen
// Project:  Vekapu
//
// Purpose:  Vekapu exception handlin.
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

	/**
	 * 
	 */
	public VekapuException() {
		super();
		// TODO Auto-generated constructor stub
	}

	/**
	 * @param message
	 */
	public VekapuException(String message) {
		super(message);
		// TODO Auto-generated constructor stub
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
	 * @param cause
	 */
	public VekapuException(Throwable cause) {
		super(cause);
		// TODO Auto-generated constructor stub
	}

}
