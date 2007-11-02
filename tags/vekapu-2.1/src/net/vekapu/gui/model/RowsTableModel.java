///////////////////////////////////////////////////////////////////////////////
//
// Author:   Jukka Koskelainen
// Project:  Vekapu
//
// Feedback: palaute@vekapu.net
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

/*
 * Created on 30.3.2005
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package net.vekapu.gui.model;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Vector;

import javax.swing.table.AbstractTableModel;

import net.vekapu.VekapuException;
import net.vekapu.gui.GuiConstant;
import net.vekapu.util.SettingsReader;

/**
 * @author Jukka
 * Jukka Koskelainen
 * 
 * TODO To change the template for this generated type comment go to Window -
 * Preferences - Java - Code Style - Code Templates
 */
public class RowsTableModel extends AbstractTableModel {

	// Names of the columns
	public String[] m_colNames = { GuiConstant.ROW_COLUMN_NAME_1,
			GuiConstant.ROW_COLUMN_NAME_2,
			GuiConstant.ROW_COLUMN_NAME_3,
			GuiConstant.ROW_COLUMN_NAME_4,
			GuiConstant.ROW_COLUMN_NAME_5,
			GuiConstant.ROW_COLUMN_NAME_6,
			GuiConstant.ROW_COLUMN_NAME_7,
			GuiConstant.ROW_COLUMN_NAME_DELETEROW};
								

	// Types of the columns.
	public Class[] m_colTypes = { Integer.class, Integer.class, Integer.class,
			Integer.class, Integer.class, Integer.class, Integer.class,
			Boolean.class };

	// store the data
	private Vector rowDataVector;

	private SettingsReader pr = null;

	private int[][] omaRivi;

	private boolean[] deleteRow = new boolean[1000];

	private int rastienLkm = 7;

	private int rivienLkm = 0;

	private boolean rowsChanged = false;

	private LinkedList rows;

	private ArrayList row;
	
	private ValueCheck valueCheck = new ValueCheck();

	/**
	 * Constructor
	 * @throws VekapuException 
	 */
	public RowsTableModel() throws VekapuException {
		super();

		pr = new SettingsReader();
		
		for (int i = 0; i == 1000; i++) {
			this.deleteRow[i] = false;
		}

		// store the data
		this.getOwnRowsData();
	}

	/**
	 * getColumnCount Number columns same as the column array length
	 */
	public int getColumnCount() {
		return m_colNames.length;
	}

	/**
	 * getRowCount Row count same as the size of data vector
	 */
	public int getRowCount() {
		return this.rivienLkm;
	}

	/**
	 * setValueAt This function updates the data in the TableModel depending
	 * upon the change in the JTable
	 */
	@Override
	public void setValueAt(Object value, int row, int col) {
		if (col < 7) {
			if (valueCheck.valueOK(value, omaRivi, row, col)) {
				this.omaRivi[row][col] = Integer.parseInt(value.toString());
			}
		}
		if ("true".equalsIgnoreCase(value.toString())) {
			this.deleteRow[row] = true;
		} else {
			this.deleteRow[row] = false;
		}

		this.rowsChanged = true;
	}

	@Override
	public String getColumnName(int col) {
		return m_colNames[col];
	}

	@Override
	public Class getColumnClass(int col) {
		return m_colTypes[col];
	}

	/**
	 * getValueAt This function updates the JTable depending upon the data in
	 * the TableModel
	 */
	public Object getValueAt(int row, int col) {
		if (col < 7) {
			if (omaRivi[row][col] == 0) {
				return new Integer(omaRivi[row][col]);
			} else {
				return new Integer(omaRivi[row][col]);
			}
		}
		if (deleteRow[row] == false) {
			return new Boolean(false);
		} else {
			return new Boolean(true);
		}
	}

	/*
	 * Don't need to implement this method unless your table's editable.
	 */
	@Override
	public boolean isCellEditable(int row, int col) {
		return true;
		// Note that the data/cell address is constant,
		// no matter where the cell appears onscreen.
		// if (col < 2) {
		// return false;
		// } else {
		// return true;
		// }
	}

	private void getOwnRowsData() {
/* @TODO
		OwnNumbers ownNumbers = new OwnNumbers(GuiConstant.PROPERTY_FILE);
		omaRivi = ownNumbers.getRivit(7);

		this.rivienLkm = omaRivi.length;
*/
	}

	public void addRow() {
		int[][] newRows = new int[rivienLkm + 1][7];
		for (int k = 0; k < rivienLkm; k++) {
			for (int j = 0; j < 7; j++) {
				newRows[k][j] = omaRivi[k][j];
			}
		}
		omaRivi = newRows;
		this.rivienLkm++;
		this.fireTableDataChanged();
		this.rowsChanged = true;

	}

	public void deleteRows() {

		int rowsLeft = 0;
		// count how many rows there are left
		for (int i = 0; i < this.rivienLkm; i++) {
			if (this.deleteRow[i] == false) {
				rowsLeft++;
			}

		}
		// make table that is size of rowsLeft
		int[][] rowsToSave = new int[rowsLeft][7];
		int rowToAdd = -1;
		for (int k = 0; k < rivienLkm; k++) {
			if (this.deleteRow[k] == false) {
				rowToAdd++;
				for (int j = 0; j < 7; j++) {
					rowsToSave[rowToAdd][j] = omaRivi[k][j];
				}
			}
		}
		for (int l = 0; l < this.rivienLkm + 100; l++) {
			this.deleteRow[l] = false;
		}

		this.omaRivi = rowsToSave;
		this.rivienLkm = rowsLeft;

		this.rowsChanged = true;
		this.fireTableDataChanged();

	}

	public int[][] getOmaRivi() {
		return omaRivi;
	}

	public boolean isRowsChanged() {
		return rowsChanged;
	}

}
