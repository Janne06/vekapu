// /////////////////////////////////////////////////////////////////////////////
//
// Filename: Menu.java
//
// Author: Jukka Koskelainen
// Project: Vekapu
//
// Feedback: palaute@vekapu.net
//
// Purpose: Menu bar
//
// Thanks: http://www.onjava.com/pub/a/onjava/excerpt/swing_14/index1.html
//
// (c) Copyright J.Koskelainen, 2005
//
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

package net.vekapu.gui.view;

import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Observable;
import java.util.Observer;

import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.KeyStroke;

import net.vekapu.gui.model.VekapuModel;
import net.vekapu.gui.GuiConstant;

public class Menu extends JMenuBar implements Observer {
	private VekapuModel model;

	public Menu(VekapuModel model) {
		this.model = model;

		model.addObserver(this);

		JMenu fileMenu = new JMenu(GuiConstant.MENU_FILE);
		JMenu lotteryMenu = new JMenu(GuiConstant.MENU_LOTTERY);
		JMenu groupMenu = new JMenu(GuiConstant.MENU_GROUP);
		groupMenu.setEnabled(false);
		JMenu helpMenu = new JMenu(GuiConstant.MENU_HELP);

		// Assemble the menus with shortcut keys

		JMenuItem item = new JMenuItem(GuiConstant.MENU_FILE_SAVE);
		item.setAccelerator(KeyStroke.getKeyStroke(
				GuiConstant.MENU_FILE_SAVE_SHORTCUT, Toolkit
						.getDefaultToolkit().getMenuShortcutKeyMask(), false));
		item.addActionListener(new SaveListener());
		fileMenu.add(item);

		item = new JMenuItem(GuiConstant.MENU_FILE_EXIT);
		item.setAccelerator(KeyStroke.getKeyStroke(
				GuiConstant.MENU_FILE_EXIT_SHORTCUT, Toolkit
						.getDefaultToolkit().getMenuShortcutKeyMask(), false));
		item.addActionListener(new QuitListener());
		fileMenu.add(item);

		item = new JMenuItem(GuiConstant.MENU_LOTTERY_CHECK_ROWS);
		item.addActionListener(new CheckRowsListener());
		lotteryMenu.add(item);

		item = new JMenuItem(GuiConstant.MENU_LOTTERY_OWN_ROWS_7);
		item.addActionListener(new OwnRowsListener());
		lotteryMenu.add(item);

		item = new JMenuItem(GuiConstant.MENU_HELP_OHJEITA);
		item.addActionListener(new OhjeitaListener());
		helpMenu.add(item);

		add(fileMenu);
		add(lotteryMenu);
		add(groupMenu);
		add(helpMenu);
	}

	public void update(Observable observable, Object o) {
		// no need to do much here
	}

	protected VekapuModel getModel() {
		return this.model;
	}

	private class SaveListener implements ActionListener {
		public void actionPerformed(ActionEvent e) {
			model.saveAll(); // tallenna kaikki
		}
	}

	private class QuitListener implements ActionListener {
		public void actionPerformed(ActionEvent e) {
			if(model.getRowsTableModel().isRowsChanged()){
				//rivejä on käpälöity, kysytään halutaanko tallettaa
				int answer = JOptionPane.showConfirmDialog(null,
						GuiConstant.QUESTION_ROWS_CHANGED_SAVE_ROWS,GuiConstant.COMMENT_CHANGED_WINDOW_NAME, JOptionPane.NO_OPTION);
				if(answer == 0)
				{
					model.saveAll();
				}
			}
			System.exit(0);
		}
	}

	private class CheckRowsListener implements ActionListener {
		public void actionPerformed(ActionEvent e) {
			model.checkRows(e.getActionCommand());
		}
	}

	private class OwnRowsListener implements ActionListener {
		public void actionPerformed(ActionEvent e) {
			model.activateOwnRows(e.getActionCommand());
		}
	}

	private class OhjeitaListener implements ActionListener {
		public void actionPerformed(ActionEvent e) {
//			model.showHelp();
		}
	}
	
}
