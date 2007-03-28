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

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;

import net.vekapu.gui3.VekapuData;

import org.apache.log4j.Logger;
import org.bs.mdi.Application;
import org.bs.mdi.FileFormat;
import org.bs.mdi.FileIOException;
import org.bs.mdi.FileLoader;
import org.bs.mdi.FileSaver;
import org.bs.mdi.RootData;

public class VekapuFileIOModule implements FileLoader, FileSaver {

	private static Logger logger = Logger.getLogger(VekapuFileIOModule.class);

	static FileFormat[] formats = { new TextFileFormat() };

	public RootData load(String filename) throws FileIOException {

		logger.debug("filename: " + filename);

		char[] cbuf = new char[256];
		int readlen;
		StringBuffer sb = new StringBuffer();
		VekapuData data = new VekapuData();

		try {
			FileReader reader = new FileReader(filename);
			String encoding = reader.getEncoding();
			logger.info("reader.getEncoding() " + encoding);

			while (reader.ready()) {
				readlen = reader.read(cbuf, 0, 256);
				sb.append(cbuf, 0, readlen);
			}
			reader.close();

			byte[] bytes = sb.toString().getBytes(encoding);
			String utf = new String(bytes, "UTF-8");

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
			logger.info("Talletetaan file UTF-8 muodossa: " + filename);

			File f = new File(filename);
			FileOutputStream fos = new FileOutputStream(f);
			OutputStreamWriter osw = new OutputStreamWriter(fos, "UTF-8");
			BufferedWriter writer = new BufferedWriter(osw);

			PrintWriter out = new PrintWriter(writer);
			out.print(text);
			out.close();
		} catch (FileNotFoundException e) {
			// A FileNotFoundException while writing a file usually means
			// "access denied"
			throw new FileIOException(FileIOException.ERR_NOACCESS, filename);
		} catch (IOException e) {
			throw new FileIOException(FileIOException.ERR_UNKNOWN, filename);
		}
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
