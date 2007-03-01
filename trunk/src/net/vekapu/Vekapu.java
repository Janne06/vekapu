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

/**

 Tarkistetaan lottorivit (& jokeri & vikinlotto).


 @author J.Ilonen
 @version $Revision$

 */
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

public class Vekapu {
	private static Logger logger = Logger.getLogger(Vekapu.class);

	private DayHelper dayhelper = null;

	private GameMaster gameMaster = null;

	private static SettingsVO settingsVO = null;

	private ResultVO resultVO = null;

	/**
	 * @deprecated
	 */
	private String tulos = "";

	private boolean test = false;
	
	/**
	 * @deprecated
	 */
	private String chec_kedFileName = "";

	public Vekapu() {
		logger.info(Constant.getName() + " " + Constant.getVersionNumber());
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

		gameMaster = new GameMaster(settingsVO);

		if (settingsVO.getGroupInfo().equals("")) {
			logger.debug(settingsVO.toString());

			String kierros = settingsVO.getWeek();
			if (kierros.equals("?")) {
				// Otetaan oletuksena kuluva viikko.
				kierros = dayhelper.getWeek();
			}

			tarkista(kierros);
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
	 * Suoritetaan rivien tarkistus.
	 * 
	 * @throws VekapuException
	 */
	public void tarkista(String kierros) throws VekapuException {

		boolean ohi = true;
		boolean checked = false;

		int lkmLotto = 0;
		int lkmJokeri = 0;
		int lkmviking = 0;

		String parasTulos = "";
		String parasJokeri = "";

		tulos = "";

		// Kelataan läpi lottoporukat.
		for (int i = 1; i <= settingsVO.getGroupCount().intValue(); i++) {

			String group = settingsVO.getGroupName(i);
			if (group == null) {
				VekapuInfo.groupNull();
			}

			ohi = checkGroup(group, kierros);
			
/*			
			// TODO Meniskähän koko tää luuppi GameMasteriin ??
			// Jos ei kokonaan niin ainakin eri pelien tarkastuksien kutsut

			// TODO Porukka kohtaiset kutsut !!!!

			logger.debug("Tarkastettava kierros: " + kierros);

			String otsikko = "";

			String group = settingsVO.getGroupName(i);
			if (group == null) {
				VekapuInfo.groupNull();
			}

			logger.info("Tarkistettava porukka: " + group);

			OwnNumbersVO numbersVO = new OwnNumbers(group, settingsVO)
					.getOwnNumbers();

			resultVO = new ResultVO(numbersVO, settingsVO.isServer());
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
				if (numbersVO.isLotto()) {
					lkmLotto++;
					checked = true;

					// Tarkistetaan onko rivi vielä voimassa
					if (dayhelper.isExpired(numbersVO.getMihinAstiLotto())) {
						Messenger.sendEndMail("Lotto", group,
								numbersVO.getTo(), settingsVO);
						logger.warn("<======== PORUKAN " + group
								+ " LOTTO ON VANHENTUNUT ========>");
						//						
					} else {
						// Tarkistetaan lotto
						resultVO = gameMaster.checkLotto(resultVO, numbersVO);
						kierros = settingsVO.getWeek();
						logger.debug("kierros: " + kierros);
						parasTulos = resultVO.getLottobest();

						logger.debug(resultVO.getLotto());
						logger.debug("LottoBest: " + resultVO.getLottobest());

					}
				}
				// Jokerin tarkistua
				if (numbersVO.isJokeri()) {
					resultVO = gameMaster.checkJokeri(resultVO, numbersVO);
					parasJokeri = " Jokeri '"
							+ String.valueOf(resultVO.getJokeribest())
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

				if (numbersVO.isViking()) {
					ohi = false;
					checked = true;

					resultVO = gameMaster.checkVikingLotto(resultVO, numbersVO);
					parasTulos = resultVO.getVikingbest();

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
				if (numbersVO.isJokeri())
					jokeri = " -" + parasJokeri;

				sf.store(kierros + " - " + parasTulos + jokeri);

				// Lähetetään lopuksi loppumisilmoitus mikäli aihetta.
				// TODO Onx järkee näin ??
				// FIXME Nyt tutkitaan vain Loton loppumisajankohta. Tosin
				// VO:ssa katotaan
				// viikkari jos lotto on tyhjä.
				if (dayhelper.isToday(numbersVO.getMihinAstiLotto())
						|| settingsVO.isTest().booleanValue()) {
					Messenger.sendEndMail("Lotto", group, numbersVO.getTo(),
							settingsVO);
					sf.rename(dayhelper.getYearWeek());
				}

				// =========================================

				if (numbersVO.isStoreResult()) {
					logger.info("Talleteaan tulokset kierrokselta: "
							+ settingsVO.getWeek());
					String weeknbr = settingsVO.getCheckedRound();

					// Talletetaan tarkistuksen tiedot
					String dir2 = "";
					if (settingsVO.isServer().booleanValue()) {
						dir2 = settingsVO.getGroupDir() + "/" + group;
					} else {
						dir2 = ".";
					}
					dir2 += Constant.getResultDir();
					logger.debug("Group & week: " + group + "-" + weeknbr);
/*
					checkedFileName = dir2 + group + "-" + weeknbr
					   + Constant.getResultFileExt();
					logger.debug("checkedFileName: " + checkedFileName);
* /					
					StoreFile sf2 = new StoreFile(dir2, group + "-" + weeknbr
							+ Constant.getResultFileExt(), resultVO.toString());
					logger.debug(sf2.toString());
				}

				if (settingsVO.isConsole().booleanValue()
						|| settingsVO.isTest().booleanValue()) {
					System.out.println(resultVO.toString());
					System.out.println();
				}

				// Lähetetäänkö sähköpostia ???????
				if (settingsVO.isEmail().booleanValue()
						|| settingsVO.isTest().booleanValue()) {

					// TODO Porukka mukaan ??
					otsikko = "Kierros " + kierros;

					if (numbersVO.isLotto())
						otsikko += " Paras lotto " + parasTulos;

					if (numbersVO.isJokeri())
						otsikko += " Paras" + parasJokeri;

					logger.debug("Otsikko: " + otsikko);

					Messenger.sendEMail(otsikko, group, numbersVO.getTo(),
							resultVO.toString(), settingsVO);
				}

				// Lähetetäänkö tekstivietejä ??????????????????
				if (settingsVO.isSms().booleanValue()
						|| settingsVO.isTest().booleanValue()) {

					otsikko = "Kierros " + kierros;

					if (numbersVO.isLotto())
						otsikko += " Paras lotto " + parasTulos;

					if (numbersVO.isJokeri())
						otsikko += " Paras jokeri " + parasJokeri;

					logger.debug("Otsikko: " + otsikko);

					Messenger.sendSMS(otsikko, group, numbersVO.getToSms(),
							settingsVO);
				}
			}
*/
			// Katotaan kuinka tarkistus menis tällätavalla
			logger.debug(resultVO.getHeader() + resultVO.printLotto()
					+ resultVO.printJokeri(true) + resultVO.printViking());

			tulos += resultVO.toString();
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
	
	public boolean checkGroup(String group, String kierros) throws VekapuException { 

		boolean ohi = true;
		boolean checked = false;
	
		int lkmLotto = 0;
		int lkmJokeri = 0;
		int lkmviking = 0;
	
		String parasTulos = "";
		String parasJokeri = "";
	
		// TODO Meniskähän koko tää luuppi GameMasteriin ??
		// Jos ei kokonaan niin ainakin eri pelien tarkastuksien kutsut

		// TODO Porukka kohtaiset kutsut !!!!

		String otsikko = "";

		logger.info("Tarkistettava porukka: '" + group + "' & kierros: '" + kierros + "'.");

		OwnNumbersVO numbersVO = new OwnNumbers(group, settingsVO).getOwnNumbers();

		resultVO = new ResultVO(numbersVO, settingsVO.isServer());
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
			if (numbersVO.isLotto()) {
				lkmLotto++;
				checked = true;

				// Tarkistetaan onko rivi vielä voimassa
				if (dayhelper.isExpired(numbersVO.getMihinAstiLotto())) {
					Messenger.sendEndMail("Lotto", group,
							numbersVO.getTo(), settingsVO);
					logger.warn("<======== PORUKAN " + group
							+ " LOTTO ON VANHENTUNUT ========>");
					//						
				} else {
					// Tarkistetaan lotto
					resultVO = gameMaster.checkLotto(resultVO, numbersVO);
					kierros = settingsVO.getWeek();
					logger.debug("kierros: " + kierros);
					parasTulos = resultVO.getLottobest();

					logger.debug(resultVO.getLotto());
					logger.debug("LottoBest: " + resultVO.getLottobest());

				}
			}
			// Jokerin tarkistua
			if (numbersVO.isJokeri()) {
				resultVO = gameMaster.checkJokeri(resultVO, numbersVO);
				parasJokeri = " Jokeri '"
						+ String.valueOf(resultVO.getJokeribest())
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

			if (numbersVO.isViking()) {
				ohi = false;
				checked = true;

				resultVO = gameMaster.checkVikingLotto(resultVO, numbersVO);
				parasTulos = resultVO.getVikingbest();

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
			if (numbersVO.isJokeri())
				jokeri = " -" + parasJokeri;

			sf.store(kierros + " - " + parasTulos + jokeri);

			// Lähetetään lopuksi loppumisilmoitus mikäli aihetta.
			// TODO Onx järkee näin ??
			// FIXME Nyt tutkitaan vain Loton loppumisajankohta. Tosin
			// VO:ssa katotaan
			// viikkari jos lotto on tyhjä.
			if (dayhelper.isToday(numbersVO.getMihinAstiLotto())
					|| settingsVO.isTest().booleanValue()) {
				Messenger.sendEndMail("Lotto", group, numbersVO.getTo(),
						settingsVO);
				sf.rename(dayhelper.getYearWeek());
			}

			// =========================================

			if (numbersVO.isStoreResult()) {
				logger.info("Talleteaan tulokset kierrokselta: "
						+ settingsVO.getWeek());
				String weeknbr = settingsVO.getCheckedRound();

				// Talletetaan tarkistuksen tiedot
				String dir2 = "";
				if (settingsVO.isServer().booleanValue()) {
					dir2 = settingsVO.getGroupDir() + "/" + group;
				} else {
					dir2 = ".";
				}
				dir2 += Constant.getResultDir();
				logger.debug("Group & week: " + group + "-" + weeknbr);
/*
				checkedFileName = dir2 + group + "-" + weeknbr
				   + Constant.getResultFileExt();
				logger.debug("checkedFileName: " + checkedFileName);
*/					
				StoreFile sf2 = new StoreFile(dir2, group + "-" + weeknbr
						+ Constant.getResultFileExt(), resultVO.toString());
				logger.debug(sf2.toString());
			}

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
		return ohi;
	}
	
	private void sendMail(String kierros,OwnNumbersVO numbersVO, String parasTulos, String parasJokeri) throws VekapuException {
		
//		 TODO Porukka mukaan ??
		String otsikko = "Kierros " + kierros;

		if (numbersVO.isLotto())
			otsikko += " Paras lotto " + parasTulos;

		if (numbersVO.isJokeri())
			otsikko += " Paras" + parasJokeri;

		logger.debug("Otsikko: " + otsikko);

		Messenger.sendEMail(otsikko, numbersVO.getGroup(), numbersVO.getTo(),
				resultVO.toString(), settingsVO);
		
	}
	
	private void sendSms(String kierros,OwnNumbersVO numbersVO, String parasTulos, String parasJokeri) throws VekapuException {

		String otsikko = "Kierros " + kierros;

		if (numbersVO.isLotto())
			otsikko += " Paras lotto " + parasTulos;

		if (numbersVO.isJokeri())
			otsikko += " Paras jokeri " + parasJokeri;

		logger.debug("Otsikko: " + otsikko);

		Messenger.sendSMS(otsikko, numbersVO.getGroup(), numbersVO.getToSms(),
				settingsVO);
		
	} 
}
