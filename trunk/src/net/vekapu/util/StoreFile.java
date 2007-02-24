///////////////////////////////////////////////////////////////////////////////
//
// Filename: StoreFile.java
//
// Author:   Janne Ilonen
// Project:  Vekapu
//
// Feedback: palaute@vekapu.net
//
// Purpose:  Writing & reading files.
//
// (c) Copyright J.Ilonen, 2003-2007
//
// $Id$
//
// Used this sites as modell:
// http://www.rgagnon.com/howto.html
// http://www.rgagnon.com/javadetails/java-0054.html
// http://www.rgagnon.com/javadetails/java-0057.html
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

package net.vekapu.util;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintStream;

import net.vekapu.VekapuException;

import org.apache.log4j.Logger;

/**
 * @author janne
 * 
 */
public class StoreFile {
	static Logger logger = Logger.getLogger(StoreFile.class);

	private String isFileName = null;

	private static SettingsReader pr = null;

	public StoreFile(String subdir, String file)  throws VekapuException {
		this(subdir, file, "");
	}

	/**
	 * 
	 * @param subdir
	 * @param filename
	 * @param file
	 */
	public StoreFile(String subdir, String filename, String file) throws VekapuException {
		logger.debug("subdir: " + subdir + " filename: " + filename
				+ " file's length: " + file.length());
		try {
			pr = new SettingsReader();
			
			// If subdir dosn't exitis it will be created
			if (!isFileExist(subdir)) {
				logger.info("Tehdään hekemisto: " + subdir);
				File dir = new File(subdir);
				dir.mkdir();

			}

			isFileName = "." + Constant.getFileSeparator() +  subdir + filename;

			File f = new File(isFileName);
			// If file dosn't exitis it will be created
			if (!isFileExist(isFileName)) {

				f.createNewFile();

				PrintStream ps = new PrintStream(new FileOutputStream(
						isFileName, true));

				// Kun tehdään ekan kerran luettelo parhaista tuloksista niin
				// laitetaan fileeseen otsikko.
				if (subdir.indexOf(Constant.getBestDir()) > 0) {
					ps.println("Porukan " + filename
							+ " parhaat lottotulokset kierroksittain.");
					ps.println();
				} else {
					ps.print(file);
				}

				logger.debug("Tehtiin tiedosto: "
						+ System.getProperty("user.dir") + isFileName);
			}
			
			logger.debug(System.getProperty("user.dir") + f
					+ (f.exists() ? " is found " : " is missing "));
			
		} catch (FileNotFoundException fnfe) {
			//
			logger.error("Ongelmia tiedoston: "
					+ System.getProperty("user.dir") + isFileName
					+ " tallennuksessa. ", fnfe);
			throw new VekapuException(fnfe);
		} catch (Exception e) {
			// "user.dir"
			logger.error("Ongelmia tiedoston: "
					+ System.getProperty("user.dir") + isFileName
					+ " luomisessa. ", e);
			throw new VekapuException(e);
		}

		try {
			File dir1 = new File(".");
			logger.debug("Current dir : " + dir1.getCanonicalPath());
		} catch (Exception e) {
			logger.error(e, e);
			throw new VekapuException(e);
		}
	}

	/**
	 * 
	 * @param args
	 */
	public static void main(String[] args) {
		StoreFile a1;
		try {
			a1 = new StoreFile(Constant.getWwwDir(), "test", "kokeilu");
			a1.test();
		} catch (VekapuException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}		
	}

	/**
	 * 
	 * @param results
	 * @throws FileNotFoundException
	 */
	public void store(String results) throws VekapuException {
		// Tarkistetaan onko rivi jo kirjoitettu
		String old = getFile(isFileName);

		if (old.lastIndexOf(results) > 0) {
			logger.info("Rivi on jo talletettu");
			return;
		}

		try {
			PrintStream ps = new PrintStream(new FileOutputStream(isFileName,
					true));
			ps.println(results);
		} catch (FileNotFoundException fnfe) {
			//
			logger.error("Ongelmia tiedoston " + isFileName
					+ " tallennuksessa.", fnfe);
			throw new VekapuException(fnfe);
		}

	}

	/**
	 * 
	 * @param fileName
	 * @return
	 * @throws Exception 
	 */
	public static String getFile(String fileName) throws VekapuException {
		logger.debug("fileName: " + fileName);
//		String userdir = System.getProperty("user.dir");
		String userdir = Constant.getUserDir();
		try {
			BufferedReader br = new BufferedReader(new FileReader(userdir + fileName));
			String nextLine = "";
			StringBuffer sb = new StringBuffer();
			while ((nextLine = br.readLine()) != null) {
				sb.append(nextLine + Constant.getLineSeparator());
			}

			return sb.toString();

		} catch (FileNotFoundException fnfe) {
			String msg = "Filettä: '" + fileName + "' ei löydy hakemistosta: " + Constant.getUserDir();
			
			logger.error(msg + " FileNotFoundException", fnfe);
			
			throw new VekapuException(msg,fnfe);
		} catch (IOException e) {
			logger.error("IOException", e);
			throw new VekapuException(e);
		}
	}

	/**
	 * Renaming file.
	 * 
	 * @param yearWeek
	 * @throws VekapuException 
	 */
	public void rename(String yearWeek) throws VekapuException {

		File f = new File(isFileName);
		// If file exitis then rename it.
		logger.debug("If file (" + isFileName
				+ ") exitis then rename it.");

		if (f.exists()) {
			try {
				String newFile = isFileName + "." + yearWeek;
				if (pr.getSettingsVO().isTest().booleanValue()) {
					logger.debug("Uudelleen nimetty tiedosto: " + newFile);
				} else {
					f.renameTo(new File(newFile));
				}
				logger.debug("Tehtiin tiedosto: " + newFile);
			} catch (Exception e) {
				//
				logger.error("Ongelmia tiedoston " + "uudelleennimeämisessä.",
						e);
				throw new VekapuException(e);
			}
		}
	}

	/**
	 * @param fileName
	 * @return
	 */
	public static boolean isFileExist(String fileName) {
		File f = new File(fileName);
		return f.exists();
	}

	/**
	 * @param fileName
	 * @return
	 */
	public static boolean deleteFile(String fileName) {
		logger.debug("Poistetaan file: " + fileName);
		File f = new File(fileName);
		return f.delete();
	}

	@Override
	public String toString() {
		String NEW_LINE = Constant.getLineSeparator();
		String rc = "StoreFile()" + NEW_LINE + "isFileName : " + isFileName;

		return rc;
	}

	private void test() {
		try {

			StringBuffer sb = new StringBuffer();

			logger.info("Test StoreFile");
			sb.append("Kokeilu versio:" + Constant.getVersionNumber()
					+ Constant.getLineSeparator());
			sb.append("123");

			store(sb.toString());
			logger.info("Test StoreFile Ok");
		} catch (VekapuException e) {
			//
			logger.error("Ongelmia tiedoston " + "uudelleennimeämisessä.", e);
		} catch (Exception e) {
			//
			logger.error("Ongelmia tiedoston " + "uudelleennimeämisessä.", e);
		}

	}
}
