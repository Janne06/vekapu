///////////////////////////////////////////////////////////////////////////////
//
// Filename: VekapuModel.java
//
// Author: Jukka Koskelainen
// Project: Vekapu
//
// Feedback: palaute@vekapu.net
//
// Purpose: the "model" part of MVC-pattern
//          interacts with classes of the core-package
//
// Thanks: http://java.sun.com/docs/books/tutorial/uiswing/learn/example1.html
//          http://csis.pace.edu/~bergin/mvc/mvcgui.html
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
// MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
// GNU General Public License for more details at gnu.org.
//
///////////////////////////////////////////////////////////////////////////////

package net.vekapu.gui.model;

import java.util.Observable;

import javax.swing.JOptionPane;

import net.vekapu.Vekapu;
import net.vekapu.gui.GuiConstant;

import org.apache.log4j.Logger;

public class VekapuModel extends Observable {

	private static Logger logger = Logger.getLogger(VekapuModel.class);
	private ValueCheck valueCheck = new ValueCheck();
	private Vekapu vekapu = new Vekapu();
	private RowsTableModel rowsTableModel;

	private String tulos = "";
	private String ohje = "";

	public void checkRows(String actionCommand) {
		if(this.rowsTableModel.isRowsChanged()){
			//rivejä on käpälöity, täytyy tallentaa ennen kuin
			//rivien tarkistus voidaan tehdä, koska tarkistus
			//tehdään talletettuja tietoja vastaan, ei
			//taulukossa olevia tietoja vastaan
			int answer = JOptionPane.showConfirmDialog(null,
					GuiConstant.QUESTION_ROWS_CHANGED_SAVE_ROWS + GuiConstant.COMMENT_CHECK,GuiConstant.ERROR_WINDOW_NAME, JOptionPane.OK_CANCEL_OPTION);
			if(answer == 0)
			{
				this.saveAll();
			}
			
		}
		try{
			logger.info("Yritetään tarkistaa rivejä.");
			vekapu.start();
			this.tulos = this.vekapu.getTulos();
		}
		catch (Exception e)
		{
			JOptionPane.showMessageDialog(null,
					GuiConstant.EXCEPTION_CHECK_ERROR,GuiConstant.EXCEPTION_WINDOW_NAME, JOptionPane.WARNING_MESSAGE);
		}
		setChanged();
		notifyObservers(actionCommand);

	}

	public void activateOwnRows(String actionCommand) {
		setChanged();
		notifyObservers(actionCommand);

	}

	public String getTulos() {
		return this.tulos;
	}
	
	public void saveAll() {
		int [][] omaRivi = this.rowsTableModel.getOmaRivi();
		if(!valueCheck.checkRows(omaRivi)){
			int answer = JOptionPane.showConfirmDialog(null,
					GuiConstant.ERROR_ZERO_VALUES,GuiConstant.ERROR_WINDOW_NAME, JOptionPane.OK_CANCEL_OPTION);
			if(answer == 0)
			{
//				this.vekapu.tallennaRivi(omaRivi);
			}

		}
	}

	public void showHelp(String actionCommand) {
		//setChanged();
		//notifyObservers();

	}

	public void setRowsTableModel(RowsTableModel rowsTableModel) {
		this.rowsTableModel = rowsTableModel;
	}

	public RowsTableModel getRowsTableModel() {
		return rowsTableModel;
	}

	public String getOhje() {
		return ohje;
	}


}
