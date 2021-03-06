///////////////////////////////////////////////////////////////////////////////
//
// Filename: VekapuCommands.java
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
//  (c) Copyright J.Ilonen, 2007-2011
//
// $Id:VekapuCommands.java 30 2007-02-25 20:21:49Z janne.ilonen $
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

import javax.swing.KeyStroke;

import net.vekapu.util.Constant;

import org.apache.log4j.Logger;
import org.bs.mdi.Application;
import org.bs.mdi.MainWindow;
import org.bs.mdi.MessageDispatcher;
import org.bs.mdi.swing.SwingCommand;
import org.bs.mdi.swing.SwingCommandAdapter;
import org.bs.mdi.swing.SwingDefaultCommands;

import edu.stanford.ejalbert.BrowserLauncher;
import edu.stanford.ejalbert.exception.BrowserLaunchingInitializingException;
import edu.stanford.ejalbert.exception.UnsupportedOperatingSystemException;

public class VekapuCommands extends SwingDefaultCommands {

	private static Logger logger = Logger.getLogger(VekapuCommands.class);
	
	SwingCommand showVekapuMenuCommand = new ShowVekapuMenuCommand();

	SwingCommand vekapuPreferencesCommand = new VekapuPreferencesCommand();
	SwingCommand showCouponsMenuCommand = new ShowCouponsMenuCommand();
	SwingCommand showCheckedMenuCommand = new ShowCheckedMenuCommand();
	
	SwingCommand showHelpMenuCommand = new ShowHelpMenuCommand();

	SwingCommand helpAboutCommand = new HelpAboutCommand();
	SwingCommand homepageCommand = new HelpHomePageCommand();
	
	public SwingCommand getShowVekapuMenuCommand() {
		return showVekapuMenuCommand;
	}

	public SwingCommand getVekapuPreferencesCommand() {
		return vekapuPreferencesCommand;
	}
	
	public SwingCommand getShowCouponsMenuCommand() {
		return showCouponsMenuCommand;
	}
	
	public SwingCommand getShowCheckedMenuCommand() {
		return showCheckedMenuCommand;
	}
	
	public SwingCommand getShowHelpMenuCommand() {
		return showHelpMenuCommand;
	}

	public SwingCommand getHelpAboutCommand() {
		return helpAboutCommand;
	}
	
	public SwingCommand getHelpHomePageCommand() {
		return homepageCommand;
	}
	

	class ShowVekapuMenuCommand extends SwingCommandAdapter {
		public ShowVekapuMenuCommand() {
			super("Vekapu", null);
			setAvailable(true);
			
		}
	}

	/*
	 * Tahan vois vissiin laittaa Vekapun omia valikkoja ??!!
	 */
	class VekapuPreferencesCommand extends SwingCommandAdapter {
		public VekapuPreferencesCommand() {
			super("Preferences", "Vekapu preferences");
			setAvailable(true);	
			
		}

		public void processMessage(Object source, int type, Object argument) {
			switch (type) {
			case MessageDispatcher.APP_INIT:
				// Pitää vissiin olla aina valittavissa
				setAvailable(true);
				break;
			}
		}

		public KeyStroke getAccelerator() {
			return KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_R,
					java.awt.event.InputEvent.CTRL_MASK);
		}
	}
	//// ===========================================================================
	class ShowCouponsMenuCommand extends SwingCommandAdapter {
		public ShowCouponsMenuCommand() {
			super("Coupons", null);
			setAvailable(true);
		}
		
		public void processMessage(Object source, int type, Object argument) {
			switch (type) {
			case MessageDispatcher.APP_INIT:
				setAvailable(true);
				break;
			}
		}
		
	}
	
	//// ===========================================================================
	class ShowCheckedMenuCommand extends SwingCommandAdapter {
		public ShowCheckedMenuCommand() {
			super("Checked", null);
			setAvailable(true);
		}
		
		public void processMessage(Object source, int type, Object argument) {
			switch (type) {
			case MessageDispatcher.APP_INIT:
				// Pitää vissiin olla aina valittavissa
				setAvailable(true);
				break;
			}
		}
	}
	////////////////////////////////////////////////////////////////////////////
	class ShowHelpMenuCommand extends SwingCommandAdapter {
		public ShowHelpMenuCommand() {
			super("Help", null);
			setAvailable(true);
		}
	}

	class HelpAboutCommand extends SwingCommandAdapter {
		public HelpAboutCommand() {
			super("About", "Shows information about this application");
			setAvailable(true);
		}

		protected void doExecute() {
			Application.getMainWindow().showMessage(MainWindow.INFO,
					Application.getMainWindow(),
					Application.tr("About Vekapu"));
		}
	}

	class HelpHomePageCommand extends SwingCommandAdapter {
		public HelpHomePageCommand() {
			super("Homepage", "Vekapu homepage");
			setAvailable(true);
		}

		protected void doExecute() {
//          Thanks:   BrowserLauncher2
//          http://browserlaunch2.sourceforge.net/index.shtml

			try {
				BrowserLauncher launcher = new BrowserLauncher();
				launcher.openURLinBrowser(Constant.getUrlHomePage());
			} catch (BrowserLaunchingInitializingException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (UnsupportedOperatingSystemException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
		}

	}
}
