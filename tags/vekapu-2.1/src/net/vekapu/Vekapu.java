///////////////////////////////////////////////////////////////////////////////
//
// Filename: Vekapu.java
//
// Author:   Janne Ilonen
// Project:  Vekapu
//
// Feedback: palaute@vekapu.net
//
// Purpose:  Checkin lottery like games.
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

package net.vekapu;

import net.vekapu.game.GameMaster;
import net.vekapu.game.OwnNumbers;
import net.vekapu.mail.Messenger;
import net.vekapu.util.Constant;
import net.vekapu.util.DayHelper;
import net.vekapu.util.SettingsReader;
import net.vekapu.util.StoreFile;
import net.vekapu.util.VekapuInfo;

import org.apache.log4j.Logger;
import org.apache.log4j.PropertyConfigurator;

/**
 * Checkin lottery like games.
 *   
 * @author janne
 *
 */
public class Vekapu {
	private static Logger logger = Logger.getLogger(Vekapu.class);

	/**
	 * 
	 */
	private DayHelper dayhelper = null;

	/**
	 * 
	 */
	private static SettingsVO settingsVO = null;

	/**
	 * 
	 */
	private ResultVO resultVO = null;

	/**
	 * @deprecated
	 */
	private String tulos = "";

	/**
	 * 
	 */
	private boolean test = false;
		
	// Uusia instanssimuuttujia kun tietyn porukan tarkistus omaan metodiin
	private boolean ohi = true;
	private boolean checked = false;

	private int lkmLotto = 0;
	private int lkmJokeri = 0;
	private int lkmviking = 0;


	public Vekapu() {
		dayhelper = new DayHelper();
	}

	public Vekapu(String[] args) {
		// Tänne pitäis lisätä argumenttien käsittely.
		// Ainaskin versionumero pitäis tulostaa konsolille.
		this();

		boolean version = false;
		boolean exit = false;
		boolean usage = false;

		logger.info("Argumentien määrä: " + args.length);

		if (args.length > 0) {
			// Ohjaus argumentit
			for (int i = 0; i < args.length; i++) {
				logger.info("Argumentti: " + i + " = " + args[i]);
				if (args[i].equalsIgnoreCase("-version"))
					version = true;
				if (args[i].equalsIgnoreCase("-v"))
					version = true;
				if (args[i].equalsIgnoreCase("-test"))
					test = true;
				if (args[i].equalsIgnoreCase("-?"))
					usage = true;

			}
		}

		if (usage) {
			System.out.println(VekapuInfo.getUsage());
			exit = true;
		}

		if (version) {
			System.out.println(VekapuInfo.getVesionInfo());
			exit = true;
		}

		// Liinat kiinni
		if (exit)
			System.exit(0);
	}

	public static void main(String[] args) {
		PropertyConfigurator.configure(Constant.getLog4JConfigFileName());

		try {
			Vekapu vekapu = new Vekapu(args);
			logger.info(Constant.getName() + "-core " + Constant.getVersionNumber());
			vekapu.start();
		} catch (VekapuException v) {
			if (v.isBugMail()) {
				VekapuInfo.endInfo(v);
			} else {
				logger.info(v);
			}
		} catch (Throwable t) {
			VekapuInfo.endInfo(t);
		}
	}

	/**
	 * 
	 * @throws VekapuException
	 */
	public void start() throws VekapuException {

		SettingsReader pr = new SettingsReader();
		settingsVO = pr.getSettingsVO();
		if (test)
			settingsVO.setTest(Boolean.TRUE);


		if (settingsVO.getGroupInfo().equals("")) {
			logger.debug(settingsVO.toString());

			String kierros = settingsVO.getWeek();
			if (kierros.equals("?")) {
				// Otetaan oletuksena kuluva viikko.
				kierros = dayhelper.getWeek();
			}

			checkAll(kierros);
		} else {

			// Mikäli infotaan porukkaa niin mitään ei tarkisteta.
			OwnNumbers ownNbr = new OwnNumbers(settingsVO.getGroupInfo(),
					settingsVO);
			OwnNumbersVO numbersVO = ownNbr.getOwnNumbers();
			Messenger.sendInfo(settingsVO.getGroupInfo(), numbersVO.getTo(),
					numbersVO.getToSms(), settingsVO);
		}

	}

	/**
	 * Checkin every group lines.
	 * 
	 * @throws VekapuException
	 */
	public void checkAll(String kierros) throws VekapuException {

		tulos = "";

		// Kelataan läpi lottoporukat.
		for (int i = 1; i <= settingsVO.getGroupCount().intValue(); i++) {

			String group = settingsVO.getGroupName(i);
			if (group == null) {
				VekapuInfo.groupNull();
			}

			checkGroup(group, kierros);
			
			ResultFormater formater = new ResultFormater(resultVO,settingsVO.isServer().booleanValue());
			logger.debug(formater.toString());		
			tulos += formater.toString();
		}

		if (ohi) {
			String NEW_LINE = Constant.getLineSeparator();

			String messu = "Yhtään riviä ei tarkastettu !" + NEW_LINE
					+ "Aseta 'vekapu.properties' johonkin seuraavista tilosta:"
					+ NEW_LINE + "   manual = yes" + NEW_LINE
					+ "   cronjob = no";

			logger.warn(messu);
			System.out.println(messu);
		} else {
			logger.info("<===== TARKISTETTU " + lkmLotto  + " LOTTO RIVIA =====>");
			logger.info("<===== TARKISTETTU " + lkmJokeri + " JOKERI RIVIA =====>");
			logger.info("<===== TARKISTETTU " + lkmviking + " VIKING LOTTOPORUKAN RIVIT =====>");
		}
	}

	/**
	 * @deprecated
	 */
	public String getTulos() {
		return tulos;
	}
	
	/**
	 * 
	 * @param group
	 * @param kierros
	 * @return file locatin where is checked resutls 
	 * @throws VekapuException
	 */
	public String checkGroup(String group, String kierros) throws VekapuException { 
	
		try {
			
			// Taidetaan kutsua tätä luokkaa GUI:sta joten alustetaan arvot
			if (settingsVO == null) {	
				SettingsReader pr = new SettingsReader();
				settingsVO = pr.getSettingsVO();	
			}
	
			GameMaster gameMaster = new GameMaster(settingsVO);
	
			if (dayhelper == null) {
				dayhelper = new DayHelper();
			}	
	
			
			String resultFile = "";
			String parasTulos = "";
			String parasJokeri = "";
		
			// TODO Meniskähän koko tää luuppi GameMasteriin ??
			// Jos ei kokonaan niin ainakin eri pelien tarkastuksien kutsut
	
			logger.info("Tarkistettava porukka: '" + group + "' & kierros: '" + kierros + "'.");
	
			OwnNumbersVO numbersVO = new OwnNumbers(group, settingsVO).getOwnNumbers();
			
			resultVO = new ResultVO(numbersVO);
			checked = false;
	
			// Mikäli manual == true niin tarkistetaan kaikki
			// Jos ei ajastettu == tarkistetaan kaikki
			// Lauantai (ajastettuna) => tarkistetaan lotto ja jokeri
			boolean tarkista = false;
	
			if (settingsVO.isTest().booleanValue()) {
				logger.info("test = yes ==> tarkistetaan kaikki");
				tarkista = true;
			} else if (settingsVO.isManual().booleanValue()) {
				logger.info("manual = yes ==> tarkistetaan kaikki");
				tarkista = true;
			} else if (!settingsVO.isCronJob().booleanValue()) {
				logger.info("CronJob = no ==> tarkistetaan kaikki");
				tarkista = true;
			}
			if (dayhelper.isSaturday() && settingsVO.isCronJob().booleanValue()) {
				logger.info("Lauantai cronilla == true ==> tarkistetaan "
						+ "lauantain pelit (Lotto & Jokeri)");
				tarkista = true;
			}
	
			if (tarkista) {
	
				ohi = false;
				parasTulos = "";
	
				// Onko porukalla Lottorivejä
				if (numbersVO.isGame("lotto")) {
					lkmLotto++;
					checked = true;
	
					// Tarkistetaan onko rivi vielä voimassa
					if (dayhelper.isExpired(numbersVO.getUntil())) {
						Messenger.sendEndMail("Lotto", group,
								numbersVO.getTo(), settingsVO);
						logger.warn("<======== PORUKAN " + group
								+ " LOTTO ON VANHENTUNUT ========>");
						//						
					} else {
						// Tarkistetaan lotto
						logger.debug("resultVO: " + resultVO);
						
						resultVO = gameMaster.checkGame("lotto",resultVO, numbersVO);
						kierros = settingsVO.getWeek();
						logger.debug("kierros: " + kierros);
						
						parasTulos = numbersVO.getGameBest("lotto");
						
					}
				}
				// Jokerin tarkistua
				if (numbersVO.isGame("jokeri")) {
					resultVO = gameMaster.checkGame("jokeri",resultVO, numbersVO);
					parasJokeri = " Jokeri '"
							+ String.valueOf(numbersVO.getGameBest("jokeri"))
							+ "' kpl";
	
					lkmJokeri++;
					checked = true;
				}
			}
	
			// Keskiviikko tai muuten vaan => Vikinglotto
			if (dayhelper.isWednesday() && settingsVO.isCronJob().booleanValue()) {
				logger.info("Keskiviikko cronilla == true ==> tarkistetaan "
						+ "keskiviikon peli (VikingLotto)");
				tarkista = true;
			}
	
			if (tarkista) {
	
				if (numbersVO.isGame("viking")) {
					ohi = false;
					checked = true;
	
					resultVO = gameMaster.checkGame("viking",resultVO, numbersVO);
					parasTulos = numbersVO.getGameBest("viking");
	
					lkmviking++;
				}
			}
	
			if (checked) {
				logger.info("Porukan '" + group + "' rivejä on tarkistettu.");
				// Talletetaan parhaat tulokset
				logger.info("Talleteaan kierroksen parhaat tulokset.");
				String dir = "";
				if (settingsVO.isServer().booleanValue()) {
					dir = settingsVO.getGroupDir()
							+ Constant.getFileSeparator() + group;
				} else {
					dir = ".";
				}
				dir += Constant.getBestDir();
	
				StoreFile sf = new StoreFile(dir, group
						+ Constant.getBestFileExt());
				String jokeri = "";
				if (numbersVO.isGame("jokeri"))
					jokeri = " -" + parasJokeri;
	
				sf.store(kierros + " - " + parasTulos + jokeri);
	
				// Lähetetään lopuksi loppumisilmoitus mikäli aihetta.
				// TODO Onx järkee näin ??
	
				if (dayhelper.isToday(numbersVO.getUntil())
						|| settingsVO.isTest().booleanValue()) {
					Messenger.sendEndMail("Lotto", group, numbersVO.getTo(),
							settingsVO);
					sf.rename(dayhelper.getYearWeek());
				}
	
				// =========================================
				// Nyt talletetaan kaikki rivit.
				logger.info("Talleteaan tulokset kierrokselta: "
						+ settingsVO.getWeek());
				String weeknbr = settingsVO.getCheckedRound();
	
				// Talletetaan tarkistuksen tiedot
				String dir2 = "";
				if (settingsVO.isServer().booleanValue()) {
					dir2 = settingsVO.getGroupDir();
					dir2 += Constant.getFileSeparator() + group;
				} else {
					dir2 = ".";
				}
				dir2 += Constant.getResultDir();
				logger.debug("Group & week: " + group + "-" + weeknbr);
				
				resultFile = dir2 + group + "-" + weeknbr
				   + Constant.getResultFileExt();
				logger.debug("resultFile: " + resultFile);
				
				ResultFormater formater = new ResultFormater(resultVO,settingsVO.isServer().booleanValue());
				StoreFile sf2 = new StoreFile(dir2, group + "-" + weeknbr
						+ Constant.getResultFileExt(), formater.toString());
				
				logger.debug(sf2.toString());
	
				
				if (settingsVO.isConsole().booleanValue()
						|| settingsVO.isTest().booleanValue()) {
					System.out.println(resultVO.toString());
					System.out.println();
				}
	
				// Lähetetäänkö sähköpostia ???????
				if (settingsVO.isEmail().booleanValue()
						|| settingsVO.isTest().booleanValue()) {
					sendMail(kierros, numbersVO, parasTulos, parasJokeri);				
				}
	
				// Lähetetäänkö tekstivietejä ??????????????????
				if (settingsVO.isSms().booleanValue()
						|| settingsVO.isTest().booleanValue()) {
					sendSms(kierros, numbersVO, parasTulos, parasJokeri);
				}
			}
			return resultFile;
			
		} catch (VekapuException ve) {
			logger.error(ve);
			throw ve;
		}
	}
	
	private void sendMail(String kierros,OwnNumbersVO numbersVO, String parasTulos, String parasJokeri) throws VekapuException {
		
//		 TODO Porukka mukaan ??
		String otsikko = "Kierros " + kierros;

		if (numbersVO.isGame("lotto"))
			otsikko += " Paras lotto " + parasTulos;

		if (numbersVO.isGame("jokeri"))
			otsikko += " Paras" + parasJokeri;

		logger.debug("Otsikko: " + otsikko);

		ResultFormater formater = new ResultFormater(resultVO,settingsVO.isServer().booleanValue());
		Messenger.sendEMail(otsikko, numbersVO.getGroup(), numbersVO.getTo(),
				formater.toString(), settingsVO);
		
	}
	
	private void sendSms(String kierros,OwnNumbersVO numbersVO, String parasTulos, String parasJokeri) throws VekapuException {

		String otsikko = "Kierros " + kierros;

		if (numbersVO.isGame("lotto"))
			otsikko += " Paras lotto " + parasTulos;

		if (numbersVO.isGame("jokeri"))
			otsikko += " Paras jokeri " + parasJokeri;

		logger.debug("Otsikko: " + otsikko);

		Messenger.sendSMS(otsikko, numbersVO.getGroup(), numbersVO.getToSms(),
				settingsVO);
		
	} 
}