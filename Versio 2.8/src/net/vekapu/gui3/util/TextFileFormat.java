///////////////////////////////////////////////////////////////////////////////
//
// Filename: TextFileFormat.java
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
// $Id: TextFileFormat.java 426 2007-02-06 20:40:53Z janne $
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

import org.bs.mdi.Application;
import org.bs.mdi.FileFormat;

public class TextFileFormat extends FileFormat  {
	
	static String extensions[] = {"txt","properties","log"};
	static String description = Application.tr("Text Files");
	
	public TextFileFormat() {
		super(extensions, description);
	}

}
