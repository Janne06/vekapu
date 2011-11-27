///////////////////////////////////////////////////////////////////////////////
//
// Filename: VekapuContextMenu.java
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
// $Id: VekapuContextMenu.java 423 2007-02-06 19:40:09Z janne $
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

package net.vekapu.gui3;

import javax.swing.JPopupMenu;

import org.bs.mdi.swing.SwingCommandButton;

public class VekapuContextMenu extends JPopupMenu {

	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public VekapuContextMenu(VekapuCommands commands) {
		add(SwingCommandButton.createMenuItem(commands.getEditUndoCommand()));
		add(SwingCommandButton.createMenuItem(commands.getEditRedoCommand()));
		addSeparator();
		add(SwingCommandButton.createMenuItem(commands.getEditCutCommand()));
		add(SwingCommandButton.createMenuItem(commands.getEditCopyCommand()));
		add(SwingCommandButton.createMenuItem(commands.getEditPasteCommand()));
		addSeparator();
		add(SwingCommandButton.createMenuItem(commands.getEditDeleteCommand()));
	}

}
