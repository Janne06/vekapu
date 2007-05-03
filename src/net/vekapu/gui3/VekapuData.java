///////////////////////////////////////////////////////////////////////////////
//
// Filename: VekapuData.java
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

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.RenderingHints;
import java.awt.print.PageFormat;
import java.io.IOException;
import java.io.LineNumberReader;
import java.io.StringReader;

import org.bs.mdi.Printer;
import org.bs.mdi.RootData;

public class VekapuData extends RootData {
	
	StringBuffer text = new StringBuffer();

	Printer printer = new EditorDocumentPrinter();

	public void setText(String string) {
		text.replace(0, text.length(), string);
	}

	public String getText() {
		return text.toString();
	}

	public void insertText(int offset, String string) {
		text.insert(offset, string);
	}

	public void deleteText(int startOffset, int endOffset) {
		text.delete(startOffset, endOffset);
	}

	public Printer getPrinter() {
		return printer;
	}

	class EditorDocumentPrinter implements Printer {

		Font printerFont = new Font("Monospaced", Font.PLAIN, 12);

		int lineHeight = 14;

		public int getNumPages(PageFormat format) {
			int lines = 1;
			int h = (int) format.getImageableHeight();
			for (int i = 0; i < text.length(); i++) {
				if (text.charAt(i) == '\n')
					lines++;
			}
			return ((lines * 14) / h) + 1;
		}

		public boolean print(java.awt.Graphics g, PageFormat format,
				int pageindex) {
			String s = null;
			int originX = (int) format.getImageableX();
			int originY = (int) format.getImageableY();
			int height = (int) format.getImageableHeight();
			int width = (int) format.getImageableWidth();
			int linesPerPage = height / 14;
			Graphics2D g2d = (Graphics2D) g;

			Rectangle oldClip = g2d.getClipBounds();
			g2d.setClip(originX, originY, width, oldClip.y + oldClip.height
					- originY);
			g2d.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING,
					RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
			g2d.setRenderingHint(RenderingHints.KEY_FRACTIONALMETRICS,
					RenderingHints.VALUE_FRACTIONALMETRICS_ON);
			g2d.setColor(Color.BLACK);
			g2d.setFont(printerFont);
			int lineY = originY + lineHeight;
			int printedLines = 0;
			LineNumberReader reader = new LineNumberReader(new StringReader(
					getText()));
			try {
				for (int i = 0; i < linesPerPage * pageindex; i++) {
					reader.readLine();
				}
			} catch (IOException e) {
			}
			do {
				try {
					s = reader.readLine();
					if (s != null)
						g2d.drawString(s, originX, lineY);
				} catch (IOException e) {
				}
				lineY += lineHeight;
				printedLines++;
			} while (s != null && printedLines < linesPerPage);
			g2d.setClip(oldClip);
			return true;
		}

	}

}
