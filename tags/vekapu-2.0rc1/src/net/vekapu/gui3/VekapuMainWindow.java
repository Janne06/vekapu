///////////////////////////////////////////////////////////////////////////////
//
// Filename: VekapuMainWindow.java
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

package net.vekapu.gui3;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JMenu;
import javax.swing.JMenuItem;
import javax.swing.event.MenuEvent;
import javax.swing.event.MenuListener;

import net.vekapu.Vekapu;
import net.vekapu.VekapuException;
import net.vekapu.util.Constant;
import net.vekapu.util.DayHelper;

import org.apache.log4j.Logger;
import org.bs.mdi.Application;
import org.bs.mdi.swing.SwingCommandButton;
import org.bs.mdi.swing.SwingCommandMenu;
import org.bs.mdi.swing.SwingDefaultCommands;
import org.bs.mdi.swing.SwingMainWindow;


public class VekapuMainWindow extends SwingMainWindow {

	private static Logger logger = Logger.getLogger(VekapuMainWindow.class);
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	VekapuContextMenu contextMenu;
	
	protected JMenu couponsMenu;
	protected JMenu checkedMenu;
	
	
	public VekapuMainWindow() {
		super();
		constructMenu();
		contextMenu = new VekapuContextMenu((VekapuCommands) getCommands());
	}

	public VekapuContextMenu getContextMenu() {
		return contextMenu;
	}

	protected void constructMenu() {
		JMenu vekapuMenu = SwingCommandMenu
				.createMenu(((VekapuCommands) getCommands())
						.getShowVekapuMenuCommand()); 
		vekapuMenu.add(SwingCommandButton
				.createMenuItem(((VekapuCommands) getCommands())
						.getVekapuPreferencesCommand())); 
		vekapuMenu.addSeparator();
		
		couponsMenu = SwingCommandMenu
		        .createMenu(((VekapuCommands) getCommands())
				        .getShowCouponsMenuCommand());
		
		couponsMenu.addMenuListener(new CouponsMenuListener());
		vekapuMenu.add(couponsMenu);
		vekapuMenu.addSeparator();
		checkedMenu = SwingCommandMenu
        		.createMenu(((VekapuCommands) getCommands())
        				.getShowCheckedMenuCommand());

		checkedMenu.addMenuListener(new CheckedMenuListener());
		vekapuMenu.add(checkedMenu);
		
		
		getJMenuBar().add(vekapuMenu); 
		JMenu helpMenu = SwingCommandMenu
				.createMenu(((VekapuCommands) getCommands())
						.getShowHelpMenuCommand());
		helpMenu.add(SwingCommandButton
				.createMenuItem(((VekapuCommands) getCommands())
						.getHelpAboutCommand()));
		getJMenuBar().add(helpMenu);
	}

	protected SwingDefaultCommands createCommands() {
		return new VekapuCommands();
	}
	

	////// ========================================================================
	class CouponsMenuListener implements MenuListener {
		public void menuSelected(MenuEvent e) {			
			CouponsFileActionListener listener = new CouponsFileActionListener();

			int count = VekapuMdiGui.getSettingsVO().getGroupCount().intValue();
			couponsMenu.removeAll();
			for (int i=0; i<=count; i++) {
				JMenuItem item = new JMenuItem(VekapuMdiGui.getSettingsVO().getGroupName(i));
				item.addActionListener(listener);
				couponsMenu.add(item);
			}

		}
		public void menuDeselected(MenuEvent e) {};
		public void menuCanceled(MenuEvent e) {};
	}
	class CouponsFileActionListener implements ActionListener {
		public void actionPerformed(ActionEvent event) {
			String filename = event.getActionCommand();
			String fullName = Constant.getUserDir() + Constant.getFileSeparator();
			logger.debug("filename : " + filename);
			
			if (VekapuMdiGui.getSettingsVO().isServer().booleanValue()) {
				logger.debug("Server");
				fullName += VekapuMdiGui.getSettingsVO().getGroupDir();
				fullName += Constant.getFileSeparator() + filename ;
			}
			fullName += Constant.getCouponDir() + filename + ".properties";
				
			logger.debug("fullName : " + fullName);		
			Application.getInstance().openDocument(fullName);
		}
	}
	////// ========================================================================
	class CheckedMenuListener implements MenuListener {
		public void menuSelected(MenuEvent e) {			
			CheckedFileActionListener listener = new CheckedFileActionListener();

			int count = VekapuMdiGui.getSettingsVO().getGroupCount().intValue();
			checkedMenu.removeAll();
			for (int i=0; i<=count; i++) {
				JMenuItem item = new JMenuItem(VekapuMdiGui.getSettingsVO().getGroupName(i));
				item.addActionListener(listener);
				checkedMenu.add(item);
			}

		}
		public void menuDeselected(MenuEvent e) {};
		public void menuCanceled(MenuEvent e) {};
	}
	class CheckedFileActionListener implements ActionListener {
		public void actionPerformed(ActionEvent event) {
			String filename = event.getActionCommand();
			String fullName = Constant.getUserDir() + Constant.getFileSeparator();
			logger.debug("filename : " + filename);
			
			if (VekapuMdiGui.getSettingsVO().isServer().booleanValue()) {
				logger.debug("Server");
				fullName += VekapuMdiGui.getSettingsVO().getGroupDir();
				fullName += Constant.getFileSeparator() + filename ;
			}

			Vekapu vekapu = new Vekapu();
	
			try {
				DayHelper dayHelper = new DayHelper();
				String file = vekapu.checkGroup(event.getActionCommand(),dayHelper.getWeek());
			
				logger.debug("fullName : " + fullName);	
				logger.debug("file : " + file);	
				
				Application.getInstance().openDocument(file);
			
			} catch (VekapuException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}

}
