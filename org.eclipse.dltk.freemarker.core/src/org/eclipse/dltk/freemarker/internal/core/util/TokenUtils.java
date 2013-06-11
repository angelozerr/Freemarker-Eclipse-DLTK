/*******************************************************************************
 * Copyright (c) 2010 Freemarker Team.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:      
 *     Angelo Zerr <angelo.zerr@gmail.com> - initial API and implementation
 *******************************************************************************/
package org.eclipse.dltk.freemarker.internal.core.util;

/**
 * This class must be removed as soon as Freemarker Token implement start/end
 * Offset.
 *
 * 
 */
public class TokenUtils {

	public static int[] getOffsets(char[] text, int startLine, int endLine,
			int startColumn, int endColumn) {
		int s;

		int startOffset = -1;
		int endOffset = -1;

		boolean newLine = true;
		int currentLine = 0;
		int currentColumn = 0;

		boolean lineFounded = false;
		boolean searchEndOffset = false;

		for (int i = 0; i < text.length; i++) {
			if (newLine) {
				newLine = false;
				currentColumn = 0;
				currentLine++;

				if (!lineFounded) {
					if (!searchEndOffset) {
						lineFounded = startLine == currentLine;
					} else {
						lineFounded = endLine == currentLine;
					}
				}
			}
			s = text[i];
			if (s == '\r') {
				newLine = (i != text.length - 1 && text[i + 1] != '\n');
			} else if (s == '\n') {
				newLine = true;
			} else {
				currentColumn++;

				if (lineFounded) {
					if (!searchEndOffset) {
						if (currentColumn == startColumn) {
							startOffset = i + 1;
							searchEndOffset = true;
							lineFounded = endLine == currentLine;
						}
					} else {
						if (currentColumn == endColumn) {
							endOffset = i + 1;
							break;
						}
					}
				}
			}
		}
		return new int[] { startOffset, endOffset };
	}

}
