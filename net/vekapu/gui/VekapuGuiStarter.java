///////////////////////////////////////////////////////////////////////////////
//
// Filename: VekapuGuiStarter.java
//
// Author:   Jukka Koskelainen
// Project:  Vekapu
//
// Feedback: palaute@vekapu.net
//
// Purpose:  Starts Vekapu GUI
//
// Thanks:  http://java.sun.com/docs/books/tutorial/uiswing/learn/example1.html
//          http://csis.pace.edu/~bergin/mvc/mvcgui.html
//
// (c) Copyright J.Koskelainen, 2005
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
package net.vekapu.gui;

import net.vekapu.VekapuException;
import net.vekapu.gui.model.VekapuModel;
import net.vekapu.gui.view.VekapuGui;
import net.vekapu.util.Constant;

import org.apache.log4j.Logger;
import org.apache.log4j.PropertyConfigurator;

/**
 * @author Jukka
 * 
 * TODO To change the template for this generated type comment go to Window -
 * Preferences - Java - Code Style - Code Templates
 */
public class VekapuGuiStarter {
	static Logger logger = Logger.getLogger(VekapuGuiStarter.class);

	public static void main(String[] args) {
		PropertyConfigurator.configure(Constant.getLog4JConfigFileName());

		// Schedule a job for the event-dispatching thread:
		// creating and showing this application's GUI.
		javax.swing.SwingUtilities.invokeLater(new Runnable() {
			public void run() {
				VekapuModel vekapuModel = new VekapuModel();
				try {
					new VekapuGui(vekapuModel);
				} catch (VekapuException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}

			}
		});
	}
}
