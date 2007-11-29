///////////////////////////////////////////////////////////////////////////////
//
// Filename: VekapuView.java
//
// Author:   Janne Ilonen
// Project:  Vekapu
//
// Feedback: palaute@vekapu.net
//
// Purpose:  MDI-Gui
//
// Thanks:   Bernhard Stiftner - Java MDI Application Framework
//           http://jmdiframework.sourceforge.net/
//
//  (c) Copyright J.Ilonen, 2007
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

package net.vekapu.gui3;

import java.awt.BorderLayout;
import java.awt.Font;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.event.CaretEvent;
import javax.swing.event.CaretListener;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.text.BadLocationException;

import org.bs.mdi.Application;
import org.bs.mdi.swing.SwingRootView;

public class VekapuView extends SwingRootView {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	JTextArea textArea = new JTextArea();

	boolean listenerActive = true; // should text events ("actions") be
									// triggered?

	public VekapuView() {
		super();
		textArea.getDocument().addDocumentListener(new MyDocumentListener());
		textArea.addCaretListener(new MyCaretListener());
		textArea.setFont(new Font("Monospaced", Font.PLAIN, 12));
		textArea.addMouseListener(new MyMouseListener());
		setLayout(new BorderLayout());
		add(new JScrollPane(textArea), BorderLayout.CENTER);
		setPastePossible(true);
	}

	public void syncWithData() {
		listenerActive = false;
		int oldCaret = textArea.getCaretPosition();
		textArea.setText(((VekapuData) getDocument().getData()).getText());
		textArea.setCaretPosition(oldCaret);
		listenerActive = true;
	}

	public org.bs.mdi.Action copy() {
		String selectedText = textArea.getSelectedText();
		if (selectedText == null)
			return null;
		VekapuAction act = new VekapuAction(null, false, VekapuAction.COPY,
				selectedText, selectedText, textArea.getSelectionStart(),
				textArea.getSelectionEnd());
		return act;
	}

	public org.bs.mdi.Action cut() {
		String selectedText = textArea.getSelectedText();
		if (selectedText == null)
			return null;
		listenerActive = false;
		VekapuAction act = new VekapuAction(null, false, VekapuAction.CUT,
				selectedText, null, textArea.getSelectionStart(), textArea
						.getSelectionEnd());
		notifyObservers(act, false);
		listenerActive = true;
		return act;
	}

	public void paste(org.bs.mdi.Action action) {
		if (action == null)
			return;
		listenerActive = false;
		VekapuAction originalAction = (VekapuAction) action;
		String selectedText = textArea.getSelectedText();
		boolean selected = selectedText != null;
		int start = (selected) ? textArea.getSelectionStart() : textArea
				.getCaretPosition();
		int end = (selected) ? textArea.getSelectionEnd() : textArea
				.getCaretPosition();
		VekapuAction newAction = new VekapuAction(null, false,
				VekapuAction.PASTE, selectedText, originalAction.getNewText(),
				start, end);
		notifyObservers(newAction, false);
		listenerActive = true;
	}

	public void delete() {
		String selectedText = textArea.getSelectedText();
		if (selectedText == null)
			return;
		listenerActive = false;
		VekapuAction act = new VekapuAction(null, false, VekapuAction.DELETE,
				selectedText, null, textArea.getSelectionStart(), textArea
						.getSelectionEnd());
		notifyObservers(act, false);
		listenerActive = true;
	}

	public void insertText(int offset, String text) {
		listenerActive = false;
		textArea.insert(text, offset);
		listenerActive = true;
	}

	public void deleteText(int startOffset, int endOffset) {
		if (startOffset == endOffset)
			return;
		listenerActive = false;
		textArea.replaceRange(null, startOffset, endOffset);
		listenerActive = true;
	}

	private VekapuView getViewInstance() {
		return this;
	}

	class MyMouseListener extends MouseAdapter {
		public void mousePressed(MouseEvent e) {
			if (e.isPopupTrigger()) {
				VekapuMainWindow emw = (VekapuMainWindow) Application
						.getMainWindow();
				emw.getContextMenu().show(getViewInstance().textArea, e.getX(),
						e.getY());
			}
		}

		public void mouseReleased(MouseEvent e) {
			mousePressed(e);
		}
	}

	class MyCaretListener implements CaretListener {

		public void caretUpdate(CaretEvent e) {
			boolean selectionReady = (textArea.getSelectedText() != null);
			setCopyPossible(selectionReady);
			setCutPossible(selectionReady);
			setDeletePossible(selectionReady);
		}
	}

	class MyDocumentListener implements DocumentListener {

		public void changedUpdate(DocumentEvent e) {
		};

		public void insertUpdate(DocumentEvent e) {
			if (!listenerActive)
				return;
			try {
				// TODO
//				VekapuData data = (VekapuData) getDocument().getData();
				String newText = e.getDocument().getText(e.getOffset(),
						e.getLength());
				String oldText = textArea.getSelectedText();
				int oldTextLen = (oldText == null) ? 0 : oldText.length();
				VekapuAction act = new VekapuAction(getViewInstance(), true,
						VekapuAction.INSERT, oldText, newText, e.getOffset(), e
								.getOffset()
								+ oldTextLen);
				notifyObservers(act, false);
			} catch (BadLocationException ignored) {
			}
		}

		public void removeUpdate(DocumentEvent e) {
			if (!listenerActive)
				return;
			VekapuData data = (VekapuData) getDocument().getData();
			String newText = null;
			String oldText = data.getText().substring(e.getOffset(),
					e.getOffset() + e.getLength());
			VekapuAction act = new VekapuAction(getViewInstance(), true,
					VekapuAction.REMOVE, oldText, newText, e.getOffset(), e
							.getOffset()
							+ e.getLength());
			notifyObservers(act, false);
		}
	}

}
