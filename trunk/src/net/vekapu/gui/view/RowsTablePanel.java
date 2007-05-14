/*
 * Created on 31.3.2005
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package net.vekapu.gui.view;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Observable;
import java.util.Observer;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;

import net.vekapu.VekapuException;
import net.vekapu.gui.model.RowsTableModel;
import net.vekapu.gui.model.VekapuModel;
import net.vekapu.gui.GuiConstant;

/**
 * @author Jukka
 * 
 * TODO To change the template for this generated type comment go to Window -
 * Preferences - Java - Code Style - Code Templates
 */
public class RowsTablePanel extends JPanel implements Observer {
	// JTable table
	private JTable rowsTable;

	// JTable model
	private RowsTableModel m_RowsTableModel;

	private JScrollPane scrollPane;

	private JLabel label;

	private JPanel buttonPanel;

	private JButton lisaysButton;

	private JButton poistoButton;

	/**
	 * Constructor
	 * 
	 * @return void
	 * @throws VekapuException 
	 * @exception
	 */
	public RowsTablePanel(VekapuModel vekapuModel) throws VekapuException {
		this.setLayout(new BorderLayout());
		// super("Omat lottorivit");
		vekapuModel.addObserver(this);

		m_RowsTableModel = new RowsTableModel();
		vekapuModel.setRowsTableModel(m_RowsTableModel);
		rowsTable = new JTable(m_RowsTableModel);

		scrollPane = new JScrollPane(rowsTable);
		label = new JLabel(GuiConstant.ROW_7_LABEL);
		buttonPanel = new JPanel();
		poistoButton = new JButton(GuiConstant.ROW_DELETE_ROWS);
		poistoButton.addActionListener(new PoistoButtonListener());
		lisaysButton = new JButton(GuiConstant.ROW_ADD_ROW);
		lisaysButton.addActionListener(new AddButtonListener());

		this.paivita();
	}

	public void paivita() {
		this.add(label, BorderLayout.NORTH);
		this.add(scrollPane);
		buttonPanel.add(lisaysButton, BorderLayout.CENTER);
		buttonPanel.add(poistoButton, BorderLayout.SOUTH);
		this.add(buttonPanel, BorderLayout.EAST);
		rowsTable.setToolTipText(GuiConstant.ROW_TOOLTIP_TABLE_EDIT);
	}

	public void update(Observable observable, Object o) {
		this.paivita();
	}

	private class PoistoButtonListener implements ActionListener {
		public void actionPerformed(ActionEvent e) {
			m_RowsTableModel.deleteRows();
		}
	}

	private class AddButtonListener implements ActionListener {
		public void actionPerformed(ActionEvent e) {
			m_RowsTableModel.addRow();
		}
	}

}
