///////////////////////////////////////////////////////////////////////////////
//
// Filename: DayHelper.java
//
// Author:   Janne Ilonen
// Project:  Vekapu
//
// Purpose:  Determinating what is day of the week.
//
// (c) Copyright J.Ilonen, 2003-2007
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

package net.vekapu.util;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

import net.vekapu.VekapuException;

import org.apache.log4j.Logger;
import org.apache.log4j.PropertyConfigurator;

/**
 * Determinating what is day of the week.
 * 
 * @author janne
 * 
 */
public class DayHelper {
	static Logger logger = Logger.getLogger(DayHelper.class);

	private int week = 0;

	private int year = 0;

	private int day = 0;

	/**
	 * 
	 */
	public DayHelper() {
		setYearInt();
		setWeekInt();
		setDayInt();
	}

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		PropertyConfigurator.configure(Constant.getLog4JConfigFileName());
		DayHelper dh = new DayHelper();
		dh.test();
	}

	/**
	 */
	public boolean isSaturday() {
		return (day == Calendar.SATURDAY);
	}

	/**
	 */
	public boolean isSunday() {
		return (day == Calendar.SUNDAY);
	}

	/**
	 */
	public boolean isMonday() {
		return (day == Calendar.MONDAY);
	}

	/**
	 */
	public boolean isTuesday() {
		return (day == Calendar.TUESDAY);
	}

	/**
	 */
	public boolean isWednesday() {
		return (day == Calendar.WEDNESDAY);
	}

	/**
	 * @param aDate
	 * @throws VekapuException
	 */
	public boolean isToday(String aDate) throws VekapuException {
		boolean isToday = false;
		try {

			String DATE_FORMAT = "dd.MM.yyyy";
			SimpleDateFormat sdf = new SimpleDateFormat(DATE_FORMAT);

			Date date = sdf.parse(aDate);

			Calendar cal = Calendar.getInstance();
			cal.setTime(date);

			Calendar calToday = Calendar.getInstance();

			if (sdf.format(cal.getTime())
					.equals(sdf.format(calToday.getTime()))) {
				isToday = true;
			}
		} catch (ParseException pe) {
			// Error with date
			logger.error("Virhe päiväyksen kanssa !! ", pe);
			// TODO Poikkeus kuntoon
			throw new VekapuException(pe);
		}
		return isToday;
	}

	/**
	 * @param aDate
	 * @throws VekapuException
	 */
	public boolean isExpired(String aDate) throws VekapuException {
		boolean expired = false;
		try {

			String DATE_FORMAT = "dd.MM.yyyy";
			SimpleDateFormat sdf = new SimpleDateFormat(DATE_FORMAT);

			Date date = sdf.parse(aDate);

			Calendar cal = Calendar.getInstance();
			cal.setTime(date);
			cal.add(Calendar.DATE, 1);

			Calendar calToday = Calendar.getInstance();

			if (cal.before(calToday)) {
				expired = true;
			}
		} catch (ParseException pe) {
			// Error with date
			logger.error("Virhe päiväyksen kanssa !!", pe);
			// TODO Poikkeus kuntoon
			throw new VekapuException(pe);
		}
		return expired;
	}

	/**
	 */
	public String getYearWeek() {
		String ret = getYear() + "-" + week;

		return ret;
	}

	/**
	 */
	public String getWeek() {
		return String.valueOf(week);
	}

	/**
	 */
	public String getLastWeek() {
		int week2 = week - 1;
		// Laitetaan vuodenvaihteessa viikoksi 53.
		if (week2 < 1)
			week2 = 53;

		String ret = String.valueOf(week2);
		return ret;
	}

	/**
	 */
	public String getYear() {
		return String.valueOf(year);
	}

	/**
	 */
	public static String now() {
		String DATE_FORMAT = "dd.MM.yyyy HH:mm:ss";
		SimpleDateFormat sdf = new SimpleDateFormat(DATE_FORMAT);

		Date date = new Date();

		return sdf.format(date);
	}

	/**
	 * Getting current weekdays 'normal' number. Week start at monday and ends at sunday.
	 * Monday = 1,..., Sunday = 7
	 * @return current weekdays 'normal' number.
	 */
	public int getWeekDayNumber() {
		int rc = day - 1;
		if (rc == 0) rc = 7; 		
		return rc;
	}
	
	/**
	 * 
	 */
	private void setDayInt() {
		GregorianCalendar newCal = new GregorianCalendar();
		day = newCal.get(Calendar.DAY_OF_WEEK);
	}

	/**
	 * 
	 */
	private void setWeekInt() {
		GregorianCalendar newCal = new GregorianCalendar();
		week = newCal.get(Calendar.WEEK_OF_YEAR);
	}

	/**
	 * 
	 */
	private void setYearInt() {
		GregorianCalendar newCal = new GregorianCalendar();
		year = newCal.get(Calendar.YEAR);
	}

	/**
	 * 
	 */
	private void test() {
		// Testataan luokan toimintaa
		try {
			logger.debug("Mikä päivä nyt on ? " + DayHelper.now());
			logger.debug("Onko lauantai ? " + this.isSaturday());
			logger.debug("Onko sunnuntai ? " + this.isSunday());
			logger.debug("Onko maanantai ? " + this.isMonday());
			logger.debug("Onko tiistai ? " + this.isTuesday());
			logger.debug("Onko keskiviikko ? " + this.isWednesday());

			String pvm = "24.4.2003";
			logger.debug("Onko tänään " + pvm + " ? " + this.isToday(pvm));
			logger.debug("Onko vanhentunut " + pvm + " ? "
					+ this.isExpired(pvm));

			String pvm2 = "24.4.2300";
			logger.debug("Onko vanhentunut " + pvm2 + " ? "
					+ this.isExpired(pvm2));

			logger.debug("Mikä vuosi ja viikko ? " + this.getYearWeek());
			logger.debug("Mikä vuosi ? " + this.getYear());
			logger.debug("Mikä oli viimeviikko ? " + this.getLastWeek());
		} catch (VekapuException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
