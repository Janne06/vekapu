// /////////////////////////////////////////////////////////////////////////////
//
// Filename: VekapuGui.java
//
// Author: Jukka Koskelainen
// Project: Vekapu
//
// Feedback: palaute@vekapu.net
//
// Purpose: the "view" part of MVC-pattern
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
package net.vekapu.gui.view;

import java.awt.CardLayout;
import java.awt.Dimension;
import java.util.Observable;
import java.util.Observer;

import javax.swing.JComponent;
import javax.swing.JDesktopPane;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;

import net.vekapu.VekapuException;
import net.vekapu.gui.model.VekapuModel;
import net.vekapu.gui.GuiConstant;

import org.apache.log4j.Logger;

public class VekapuGui implements Observer {
	static Logger logger = Logger.getLogger(VekapuGui.class);

	private JScrollPane scrollPane;

	private JTextArea tulos = new JTextArea("");
	

	private VekapuModel vekapuModel;

	private final JComponent cardHolder = new JPanel(new CardLayout(10, 10));;

	private RowsTablePanel rowsTablePanel;

	/**
	 * Create the GUI and show it. Uses MVC -pattern.
	 * @throws VekapuException 
	 */
	public VekapuGui(VekapuModel vekapuModel) throws VekapuException {
		//we don't let the user write any "fun" stuff on the jtextarea
		tulos.setEditable(false);
		this.vekapuModel = vekapuModel;
		vekapuModel.addObserver(this);

		// Make sure we have nice window decorations.
		// Don't work at JDK 1.3
		// JFrame.setDefaultLookAndFeelDecorated(true);

		// Create and set up the window.

		JFrame frame = new JFrame(GuiConstant.APPLICATION_NAME);

		JDesktopPane desktop = new JDesktopPane();

		frame.setJMenuBar(new Menu(vekapuModel));
		frame.setSize(GuiConstant.SIZE_MAIN_WINDOW_WIDTH,
				GuiConstant.SIZE_MAIN_WINDOW_WIDTH);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

		scrollPane = new JScrollPane(tulos);

		// frame.getContentPane().add(scrollPane);
		cardHolder.add(scrollPane, GuiConstant.MENU_LOTTERY_CHECK_ROWS);

		this.rowsTablePanel = new RowsTablePanel(vekapuModel);

		cardHolder.add(rowsTablePanel, GuiConstant.MENU_LOTTERY_OWN_ROWS_7);
		// frame.getContentPane().add(m);

		rowsTablePanel.setSize(new Dimension(600, 300));
		frame.getContentPane().add(cardHolder);
		frame.setVisible(true);
		try{
// @TODO 
//			new PageLoader().readPage(Constant.getUrlLotto());
			logger.info("Tarttis tehrä jotain.");
		}
		catch (Exception e){
			JOptionPane.showMessageDialog(null,
					GuiConstant.ERROR_WWW_NOT_AVAILABLE,GuiConstant.ERROR_WINDOW_WWW_NOT_AVAILABLE, JOptionPane.ERROR_MESSAGE);

		}
		

	}

	public void update(Observable observable, Object o) {

		this.tulos.setText(this.vekapuModel.getTulos().toString());

		((CardLayout) cardHolder.getLayout()).show(cardHolder, o.toString());
		this.rowsTablePanel.repaint();

	}

}
