///////////////////////////////////////////////////////////////////////////////
//
// Filename: VekapuResources.java
//
// Author:   Janne Ilonen
// Project:  Vekapu
//
// Feedback: palaute@vekapu.net
//
// Purpose:  MDI-Gui
//
// Thanks:   Bernhard Stiftner - Java MDI Application Framework
//           http://jmdiframework.sourceforge.net/
//
//  (c) Copyright J.Ilonen, 2007
//
// $Id: VekapuResources.java 427 2007-02-06 20:52:28Z janne $
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

package net.vekapu.gui3.util;

import java.util.ResourceBundle;

import org.bs.mdi.Resources;

/**
 * @author janne
 *
 */
public class VekapuResources extends Resources {

	ResourceBundle strings;

	/* (non-Javadoc)
	 * @see org.bs.mdi.Resources#loadResources()
	 */
	public void loadResources() {
		strings = ResourceBundle.getBundle("translations.vekapu_text",
				getLocale());
	}

	/* (non-Javadoc)
	 * @see org.bs.mdi.Resources#getString(java.lang.String)
	 */
	public String getString(String key) {
		try {
			return strings.getString(key);
		} catch (RuntimeException e) {
			return null;
		}
	}

	/* (non-Javadoc)
	 * @see org.bs.mdi.Resources#getIcon(java.lang.String, int)
	 */
	public javax.swing.Icon getIcon(String key, int size) {
		return null; // does not provide additional icons
	}

}
