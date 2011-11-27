///////////////////////////////////////////////////////////////////////////////
//
// Filename: VekapuAction.java
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

import org.bs.mdi.Action;
import org.bs.mdi.ActionObservable;
import org.bs.mdi.Application;
import org.bs.mdi.Data;
import org.bs.mdi.View;

public class VekapuAction extends Action {
	
	// It's quite ugly to distinguish between the different types
	// of actions by using these constants. In a bigger project
	// you would probably introduce separate Action subclasses
	// for each type of action.
	public static final int UNDEFINED = 0;

	public static final int INSERT = 1;

	public static final int REMOVE = 2;

	public static final int COPY = 3; // clipboard

	public static final int CUT = 4; // clipboard

	public static final int PASTE = 5; // clipboard

	public static final int DELETE = 6; // clipboard

	public static final String descriptions[] = { "",
			Application.tr("Insert Text"), Application.tr("Remove Text"),
			Application.tr("Copy"), Application.tr("Cut"),
			Application.tr("Paste"), Application.tr("Delete") };

	String newText;

	String oldText;

	int startoffset;

	int endoffset;

	int type;

	public VekapuAction(ActionObservable observable, boolean retard, int type,
			String oldText, String newText, int start, int end) {
		super(observable, retard);
		
		this.type = type;
		this.newText = newText;
		this.oldText = oldText;
		this.startoffset = start;
		this.endoffset = end;
	}

	public boolean clustersWith(Action action) {
		VekapuAction newAction = (VekapuAction) action;

		boolean containsNewline = (newAction.newText != null && newAction.newText
				.indexOf('\n') != -1);
		boolean nextOffset = (startoffset + 1 == newAction.getStartOffset());
		boolean prevOffset = (startoffset - 1 == newAction.getStartOffset());
		boolean sameType = (getType() == newAction.getType());

		switch (newAction.getType()) {
		case VekapuAction.INSERT:
			if (sameType && nextOffset && !containsNewline) {
				return true;
			} else {
				return false;
			}
		case VekapuAction.REMOVE:
			if (sameType && prevOffset && !containsNewline) {
				return true;
			} else {
				return false;
			}
		default:
			return false;
		}
	}

	public boolean isUndoable() {
		return true;
	}

	public String getName() {
		return descriptions[type];
	}

	public String toString() {
		return getName() + ": \"" + newText + "\" <- \"" + oldText + "\"";
	}

	public int getType() {
		return type;
	}

	public String getNewText() {
		return newText;
	}

	public String getOldText() {
		return oldText;
	}

	public int getStartOffset() {
		return startoffset;
	}

	public int getEndOffset() {
		return endoffset;
	}

	public void applyTo(Data data) {
		VekapuData ed = (VekapuData) data;
		switch (getType()) {
		case VekapuAction.INSERT:
		case VekapuAction.PASTE:
			ed.deleteText(getStartOffset(), getEndOffset());
			if (getNewText() != null)
				ed.insertText(getStartOffset(), getNewText());
			break;
		case VekapuAction.REMOVE:
		case VekapuAction.CUT:
		case VekapuAction.DELETE:
			ed.deleteText(getStartOffset(), getEndOffset());
			break;
		}
	}

	public void applyTo(View view) {
		VekapuView ev = (VekapuView) view;
		switch (getType()) {
		case VekapuAction.INSERT:
		case VekapuAction.PASTE:
			ev.deleteText(getStartOffset(), getEndOffset());
			if (getNewText() != null)
				ev.insertText(getStartOffset(), getNewText());
			break;
		case VekapuAction.REMOVE:
		case VekapuAction.CUT:
		case VekapuAction.DELETE:
			ev.deleteText(getStartOffset(), getEndOffset());
			break;
		}
	}

	public void undoFrom(Data data) {
		VekapuData ed = (VekapuData) data;
		switch (getType()) {
		case VekapuAction.INSERT:
		case VekapuAction.PASTE:
			ed.deleteText(getStartOffset(), getStartOffset()
					+ getNewText().length());
			if (getOldText() != null)
				ed.insertText(getStartOffset(), getOldText());
			break;
		case VekapuAction.REMOVE:
		case VekapuAction.CUT:
		case VekapuAction.DELETE:
			ed.insertText(getStartOffset(), getOldText());
			break;
		}
	}

	public void undoFrom(View view) {
		VekapuView ev = (VekapuView) view;
		switch (getType()) {
		case VekapuAction.INSERT:
		case VekapuAction.PASTE:
			ev.deleteText(getStartOffset(), getStartOffset()
					+ getNewText().length());
			if (getOldText() != null)
				ev.insertText(getStartOffset(), getOldText());
			break;
		case VekapuAction.REMOVE:
		case VekapuAction.CUT:
		case VekapuAction.DELETE:
			ev.insertText(getStartOffset(), getOldText());
			break;
		}
	}

}
