///////////////////////////////////////////////////////////////////////////////
//
// Filename: VekapuActionConverter.java
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

import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.Transferable;
import java.awt.datatransfer.UnsupportedFlavorException;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.util.ArrayList;

import org.apache.log4j.Logger;
import org.bs.mdi.Action;
import org.bs.mdi.ActionConverter;

// conversion between EditorActions <-> Transferables
public class VekapuActionConverter implements ActionConverter {

	private static Logger logger = Logger.getLogger(VekapuActionConverter.class);
	
	public boolean canHandle(Action action) {
		if (action instanceof VekapuAction)
			return true;
		return false;
	}

	public boolean canHandle(Transferable transferable) {
		// we can only handle text/plain data flavours
		if (transferable == null)
			return false;
		DataFlavor flavors[] = transferable.getTransferDataFlavors();
		for (int i = 0; i < flavors.length; i++) {
			DataFlavor f = flavors[i];
			if (f.getMimeType().startsWith("text/plain"))
				return true;
		}
		return false;
	}

	public Transferable toTransferable(Action action) {
		logger.debug("action.getName(): " + action.getName() );

		if (!(action instanceof VekapuAction))
			return null;
		return new EditorTransferable((VekapuAction) action);
	}

	public Action toAction(Transferable transferable) {
		try {
			Object o = transferable.getTransferData(DataFlavor.stringFlavor);
			String s = (String) o;
			return new VekapuAction(null, false, VekapuAction.UNDEFINED, null,
					s, 0, 0);
		} catch (Exception e) {
		}
		return null;
	}

	class EditorTransferable implements Transferable {
		String text;

		ArrayList<DataFlavor> flavors;

		public EditorTransferable(VekapuAction action) {
			logger.debug("VekapuAction " + action);
			
			text = action.getOldText();
			flavors = new ArrayList<DataFlavor> ();
			flavors.add(DataFlavor.stringFlavor);
			try {
				flavors.add(new DataFlavor("text/plain; charset=ISO-8859-1"));
				flavors.add(new DataFlavor("text/plain; charset=UTF-8"));
				flavors.add(new DataFlavor("text/plain; charset=US-ASCII"));
				flavors.add(new DataFlavor("text/plain; charset=ascii"));
			} catch (ClassNotFoundException e) {
			}
		}

		public Object getTransferData(DataFlavor flavor) throws IOException,
				UnsupportedFlavorException {
			if (flavor.equals(DataFlavor.stringFlavor)) {
				return text;
			} else if (flavor.isMimeTypeEqual("text/plain")) {
				ByteArrayOutputStream os = new ByteArrayOutputStream();
				OutputStreamWriter osw = new OutputStreamWriter(os, flavor
						.getParameter("charset"));
				PrintWriter wr = new PrintWriter(osw);
				wr.print(text);
				wr.flush();
				wr.close();
				return new ByteArrayInputStream(os.toByteArray());
			} else {
				throw new UnsupportedFlavorException(flavor);
			}
		}

		public DataFlavor[] getTransferDataFlavors() {
			return (DataFlavor[]) flavors.toArray(new DataFlavor[0]);
		}

		public boolean isDataFlavorSupported(DataFlavor flavor) {
			for (int i = 0; i < flavors.size(); i++) {
				if (flavor.equals(flavors.get(i)))
					return true;
			}
			return false;
		}
	}

}
