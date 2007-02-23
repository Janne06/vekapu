///////////////////////////////////////////////////////////////////////////////
//
// Filename: GuiConstant.java
//
// Author:   Jukka Koskelainen
// Project:  Vekapu
//
// Feedback: palaute@vekapu.net
//
// Purpose:  Project Gui constants.
//
// (c) Copyright J.Koskelainen, 2005
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


public class GuiConstant {

//---------------------------------------------------------
	
	// property-file used by GUI
	public final static String PROPERTY_FILE = "lotto";

//	---------------------------------------------------------
	// application constants
	public final static String APPLICATION_NAME = "VEKAPU - VEIKKAUS APURI " + Constant.getVersionNumber();
//---------------------------------------------------------
	// menu constants
	public final static String MENU_FILE = "Tiedosto";
	public final static String MENU_LOTTERY = "Rivit";
	public final static String MENU_GROUP = "Porukka";
	public final static String MENU_HELP = "Ohjeet";
	public final static String MENU_HELP_OHJEITA = "Näytä ohjeet";
	public final static String MENU_FILE_SAVE = "Tallenna";
	public final static String MENU_FILE_EXIT = "Lopeta";
	public final static char MENU_FILE_SAVE_SHORTCUT = 'T';
	public final static char MENU_FILE_EXIT_SHORTCUT = 'L';
	public final static String MENU_LOTTERY_CHECK_ROWS = "Tarkista kaikki rivisi";
	public final static String MENU_LOTTERY_OWN_ROWS_7 = "Näytä omat 7 rastin rivisi";

//	---------------------------------------------------------
	// row constants

	public final static String ROW_OWN_LOTTO_ROWS = "omat rivit";
	public final static String ROW_7_LABEL = "7 RASTIN RIVIT:";
	public final static String ROW_DELETE_ROWS = "poista valitut rivit";
	public final static String ROW_ADD_ROW = "lisää uusi rivi";	
	public final static String ROW_TOOLTIP_TABLE_EDIT = "selaa ja muuta lottonumeroita";
	public final static String ROW_COLUMN_NAME_1 = "1. nro";
	public final static String ROW_COLUMN_NAME_2 = "2. nro";
	public final static String ROW_COLUMN_NAME_3 = "3. nro";
	public final static String ROW_COLUMN_NAME_4 = "4. nro";
	public final static String ROW_COLUMN_NAME_5 = "5. nro";
	public final static String ROW_COLUMN_NAME_6 = "6. nro";
	public final static String ROW_COLUMN_NAME_7 = "7. nro";
	public final static String ROW_COLUMN_NAME_DELETEROW = "poista rivi";
	
//	---------------------------------------------------------
	// error constants
	public final static String ERROR_WINDOW_NAME ="VIRHE";
	public final static String ERROR_LOSER_NUMBER = "Tällaisilla numeroilla ei voita... Syötä numero 1-37.";
	public final static String ERROR_SAMEOLD_NUMBER = "Sama numero on jo kertaalleen tässä rivissä.";
	public final static String ERROR_ZERO_VALUES = "Osa riveistäsi on keskeneräisiä. Talletetaanko silti rivit?";
	public final static String ERROR_WINDOW_WWW_NOT_AVAILABLE = "VIRHE TIETOVERKOSSA";
	public final static String ERROR_WWW_NOT_AVAILABLE = "Verkkoyhteyksissäsi on ongelmia tai Veikkauksen kotisivuilla on ongelmia. Voit muokata ja tallentaa rivejäsi, mutta et voi tarkistaa niitä.";
//	---------------------------------------------------------
	// exception constants
	public final static String EXCEPTION_WINDOW_NAME ="OHJELMAVIRHE";
	public final static String EXCEPTION_CHECK_ERROR = "Tarkistus epäonnistui.";

//	---------------------------------------------------------
	// question constants
	public final static String QUESTION_ROWS_CHANGED_SAVE_ROWS ="Olet muuttanut rivejäsi, talletetaanko muutokset?";
//	---------------------------------------------------------
	// comment constants
	public final static String COMMENT_CHECK =" (huom.tarkistus tehdään talletettuja tietoja vastaan.)";
	public final static String COMMENT_CHANGED_WINDOW_NAME ="TIETOJA MUUTTUNUT";
	
//	---------------------------------------------------------
	// size constants
	public final static int SIZE_MAIN_WINDOW_HEIGHT = 800;
	public final static int SIZE_MAIN_WINDOW_WIDTH = 700;
}