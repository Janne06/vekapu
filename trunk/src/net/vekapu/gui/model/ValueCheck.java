package net.vekapu.gui.model;

///////////////////////////////////////////////////////////////////////////////
//
// Filename: ValueCheck.java
//
// Author:   Jukka Koskelainen
// Project:  Vekapu
//
// Purpose:  check that lotto numbers are correct.
// @todo: supports now only 7-number rows, needs some rework for different
//        types of lotto
//
// (c) Copyright Jukka Koskelainen 2005
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

import javax.swing.JOptionPane;

import net.vekapu.util.GuiConstant;

public class ValueCheck {
	
	/**
	 * metodi tarkistaa, että syötetty numero on välillä 1-37
	 * - tarkistaa myös, että samalla rivillä ei ole ennestään
	 *   syötetty samaa numeroa
	 * @param newValue
	 * @param values
	 * @param row
	 * @param col
	 * @return
	 */
	public boolean valueOK(Object newValue, int[][] values, int row, int col) {
		int intValue = Integer.parseInt(newValue.toString());
		if (intValue < 1 || intValue > 37) {
			JOptionPane.showMessageDialog(null,
					GuiConstant.ERROR_LOSER_NUMBER,GuiConstant.ERROR_WINDOW_NAME, JOptionPane.ERROR_MESSAGE);

			return false;
		}
		//seuraavassa käydään läpi rivi ja tutkitaan että ei ole jo
		//syötetty samaa numeroa
		int nroidenLkm = values[row].length;
		for (int i = 0; i < nroidenLkm; i++){
			int oldIntValue = values [row][i];
			if(i == col){
				//ei tarvi vertailla 
			}
			else{
				if(intValue == oldIntValue){
					JOptionPane.showMessageDialog(null,
							GuiConstant.ERROR_SAMEOLD_NUMBER,GuiConstant.ERROR_WINDOW_NAME, JOptionPane.ERROR_MESSAGE);
					return false;	
				}
			}
		}
		return true;
	}
	
	/**
	 * metodi tarkistaa, ettei ruudukossa ole nollia
	 * @return false, jos ruudukossa on nollia
	 */
	public boolean checkRows(int[][] values) {
		int rivienLkm = values.length;
		for (int i = 0; i < rivienLkm; i++){
			
			int nroidenLkm = values[i].length;
			for (int j = 0; j < nroidenLkm; j++){
				int value = values [i][j];
				if(value == 0){
					return false;
				}
			}
		}
		return true;
	}

}
