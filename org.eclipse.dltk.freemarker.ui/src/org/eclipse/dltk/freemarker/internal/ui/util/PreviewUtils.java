package /*******************************************************************************
 * Copyright (c) 2010 Freemarker Team.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:      
 *     Angelo Zerr <angelo.zerr@gmail.com> - initial API and implementation
 *******************************************************************************/
org.eclipse.dltk.freemarker.internal.ui.util;

import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;

import org.eclipse.dltk.freemarker.core.util.StringUtils;
import org.eclipse.dltk.freemarker.internal.ui.editor.template.TemplateEditor;
import org.eclipse.dltk.freemarker.ui.FreemarkerUIPlugin;
import org.eclipse.swt.custom.LineBackgroundEvent;
import org.eclipse.swt.custom.LineBackgroundListener;
import org.eclipse.swt.custom.StyledText;
import org.eclipse.swt.graphics.Color;

/**
 * 
 * Utilities to display result of Freemarker preview.
 */
public class PreviewUtils {

	private static final String ERROR_BACKGROUND_COLOR = "fmEerrorBackgroundColor";
	private static final String ERROR_LINE_NUMBER = "fmErrorLineNumber";
	/**
	 * Listener to set to red color the exception which can occur while preview.
	 */
	private static final LineBackgroundListener lineBackgroundListener = new LineBackgroundListener() {
		public void lineGetBackground(LineBackgroundEvent event) {
			StyledText text = ((StyledText) event.widget);

			Integer errorLineNumber = (Integer) text.getData(ERROR_LINE_NUMBER);
			if (errorLineNumber != null) {
				// The StyledText contains an error, set the background color
				// with a red color for line >= error line number.
				int line = text.getLineAtOffset(event.lineOffset);
				if (line >= errorLineNumber) {
					Color errorBackgroundColor = (Color) text
							.getData(ERROR_BACKGROUND_COLOR);
					event.lineBackground = errorBackgroundColor;
				}
			}
		};
	};

	/**
	 * Display the result of the preview into teh {@link StyledText}.
	 * 
	 * @param text
	 * @param templateEditor
	 */
	public static void displayPreview(StyledText text,
			TemplateEditor templateEditor) {

		StringWriter writer = new StringWriter();
		try {
			text.setData(ERROR_LINE_NUMBER, null);
			Throwable e = templateEditor.preview(writer);
			text.setText(writer.getBuffer().toString());

			if (e != null) {
				if (StringUtils.isEmpty(text.getText())) {
					text.setData(ERROR_LINE_NUMBER, 0);
				} else {
					text.setData(ERROR_LINE_NUMBER, text.getLineCount());
				}
				writer = new StringWriter();
				PrintWriter print = new PrintWriter(writer);
				e.printStackTrace(print);

				text.append(writer.getBuffer().toString());
				// No error while process model+template, set default background
				// color.
				// setBackground(null);
			} else {
				// Error while process model+template, set error background
				// color.
				// setBackground(errorBackgroundColor);
			}

		} catch (IOException e) {
			FreemarkerUIPlugin.log(e);
		}
	}

	/**
	 * Set message to text widget.
	 * 
	 * @param text
	 * @param message
	 */
	public static void setMessage(StyledText text, String message) {
		if (text == null || text.isDisposed()) {
			return;
		}
		text.setData(ERROR_LINE_NUMBER, null);
		text.setText(message);
	}

	/**
	 * Initialize {@link StyledText} to display result of Freemarker preview.
	 * 
	 * @param text
	 * @param errorBackgroundColor
	 */
	public static void initialize(StyledText text, Color errorBackgroundColor) {
		if (text == null || text.isDisposed()) {
			return;
		}
		text.addLineBackgroundListener(lineBackgroundListener);
		text.setData(ERROR_BACKGROUND_COLOR, errorBackgroundColor);
	}

	/**
	 * Dispose {@link StyledText} to display result of Freemarker preview.
	 * 
	 * @param text
	 */
	public static void dispose(StyledText text) {
		if (text == null || text.isDisposed()) {
			return;
		}
		text.removeLineBackgroundListener(lineBackgroundListener);
	}
}
