///////////////////////////////////////////////////////////////////////////////
//
// Filename: VekapuFileIOModule.java
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

package net.vekapu.gui3.util;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.UnsupportedEncodingException;

import net.vekapu.gui3.VekapuData;

import org.apache.log4j.Logger;
import org.bs.mdi.Application;
import org.bs.mdi.FileExporter;
import org.bs.mdi.FileFormat;
import org.bs.mdi.FileIOException;
import org.bs.mdi.FileLoader;
import org.bs.mdi.FileSaver;
import org.bs.mdi.MainWindow;
import org.bs.mdi.RootData;
import org.bs.mdi.Task;

public class VekapuFileIOModule implements FileLoader, FileSaver, FileExporter {

	private static Logger logger = Logger.getLogger(VekapuFileIOModule.class);
	
	static FileFormat[] formats = {new TextFileFormat() };

	public RootData load(String filename) throws FileIOException {
		
		logger.debug("filename: " + filename);
		
		char[] cbuf = new char[256];
		int readlen;
		StringBuffer sb = new StringBuffer();
		VekapuData data = new VekapuData();

		try {
			FileReader reader = new FileReader(filename);
			String encoding = reader.getEncoding();
			logger.info("reader.getEncoding() " + encoding );
			
			while (reader.ready()) {
				readlen = reader.read(cbuf, 0, 256);
				sb.append(cbuf, 0, readlen);
			}
			reader.close();

			byte[] bytes = sb.toString().getBytes(encoding);
			String utf = new String(bytes,"UTF-8");			
			
//			data.setText(sb.toString());
			data.setText(utf);
			
		} catch (FileNotFoundException e) {
			throw new FileIOException(FileIOException.ERR_NOSUCHFILE, filename);
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			throw new FileIOException(FileIOException.ERR_UNKNOWN, filename);	
		}
		
		return data;
	}

	public void save(RootData data, String filename) throws FileIOException {
		
		logger.debug("filename: " + filename);
		String text = ((VekapuData) data).getText();
		
		try {
			FileWriter writer = new FileWriter(filename);
			writer.write(text);
			writer.close();
		} catch (FileNotFoundException e) {
			// A FileNotFoundException while writing a file usually means
			// "access denied"
			throw new FileIOException(FileIOException.ERR_NOACCESS, filename);
		} catch (IOException e) {
			throw new FileIOException(FileIOException.ERR_UNKNOWN, filename);
		}
	}

	public void export(RootData data) {
		Task exporter = new Task() {
			public String getName() {
				return Application.tr("exporting...");
			}

			public boolean isActive() {
				return true;
			}

			public int getMinimumProgress() {
				return 0;
			}

			public int getMaximumProgress() {
				return 100;
			}

			public int getProgress() {
				return Task.PROGRESS_UNAVAILABLE;
			}
		};
		Application.getMainWindow().getProgressMonitor().add(exporter);
		Application.getMainWindow().showMessage(MainWindow.INFO,
				Application.getMainWindow(),
				Application.tr("This is just a dummy exporting function."));
		Application.getMainWindow().getProgressMonitor().remove(exporter);
	}

	public FileFormat[] getSupportedFormats() {
		return formats;
	}

	public boolean canHandle(String filename) {
		logger.debug(filename);
		return formats[0].accept(filename);
	}

	public String getDescription() {
		return Application.tr("Text File");
	}

}
