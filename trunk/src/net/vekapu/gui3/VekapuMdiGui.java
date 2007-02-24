///////////////////////////////////////////////////////////////////////////////
//
// Filename: VekapuMdiGui.java
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
// $Id: VekapuMdiGui.java 426 2007-02-06 20:40:53Z janne $
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

import net.vekapu.SettingsVO;
import net.vekapu.VekapuException;
import net.vekapu.gui3.util.VekapuFileIOModule;
import net.vekapu.gui3.util.VekapuResources;
import net.vekapu.util.Constant;
import net.vekapu.util.SettingsReader;
import net.vekapu.util.VekapuInfo;

import org.apache.log4j.Logger;
import org.apache.log4j.PropertyConfigurator;
import org.bs.mdi.ActionConverter;
import org.bs.mdi.Application;
import org.bs.mdi.FileIOModule;
import org.bs.mdi.MainWindow;
import org.bs.mdi.Resources;
import org.bs.mdi.RootData;
import org.bs.mdi.RootView;

/**
 * @author janne
 * 
 */
public class VekapuMdiGui extends Application {

	private static Logger logger = Logger.getLogger(VekapuMdiGui.class);
	
//	private static SettingsVO settingsVO = null;

	/**
	 * 
	 */
	public VekapuMdiGui() {
		// TODO Auto-generated constructor stub
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.bs.mdi.Application#getName()
	 */
	public String getName() {
		// TODO Auto-generated method stub
		return Constant.getName() + " " + Constant.getVersionNumber();
	} 
	
	
	/**
	 * @return the settingsVO
	 */
	public static SettingsVO getSettingsVO() {
		SettingsVO settingsVO = null;

		try {
			SettingsReader pr = new SettingsReader();
			settingsVO = pr.getSettingsVO();
		} catch (VekapuException ve) {
			logger.warn(ve,ve);
		} catch (Throwable t) {
			logger.warn(t);
		}
		return settingsVO;
	}

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		PropertyConfigurator.configure(Constant.getLog4JConfigFileName());
		logger.info(Constant.getName() + " " + Constant.getVersionNumber());
		logger.info("Argumentien määrä: " + args.length);
/*				
		String ENCODING = "encoding";
		String encoding = "UTF-8";
		
//		System.setProperty(ENCODING, encoding);
//		System.setProperty("file.encoding",encoding);
*/			
		VekapuMdiGui gui = new VekapuMdiGui();
		gui.run(args);
			
	}

	protected FileIOModule[] createFileIOModules() {
		FileIOModule[] modules = { new VekapuFileIOModule() };
		return modules;
	}

	protected ActionConverter[] createActionConverters() {
		ActionConverter[] converters = { new VekapuActionConverter() };
		return converters;
	}

	protected MainWindow createMainWindow() {
		return new VekapuMainWindow();
	}

	protected Resources createResources() {
		try {
			return new VekapuResources();
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	public RootData createRootData() {
		return new VekapuData();
	}

	public RootView createRootView() {
		return new VekapuView();
	}

}
