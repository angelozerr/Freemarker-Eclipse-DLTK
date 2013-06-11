/*******************************************************************************
 * Copyright (c) 2010 xored software, Inc.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     xored software, Inc. - initial API and Implementation (Alex Panchenko)
 *******************************************************************************/
package org.eclipse.dltk.freemarker.internal.ui.text.scanners.comment;

import org.eclipse.dltk.compiler.task.ITodoTaskPreferences;
import org.eclipse.dltk.ui.text.IColorManager;
import org.eclipse.dltk.ui.text.ScriptCommentScanner;
import org.eclipse.jface.preference.IPreferenceStore;

/**
 * Freemarker Comment scanner.
 * 
 */
public class FreemarkerCommentScanner extends ScriptCommentScanner {

	public FreemarkerCommentScanner(IColorManager manager,
			IPreferenceStore store, String comment, String todoTag,
			ITodoTaskPreferences preferences) {
		super(manager, store, comment, todoTag, preferences);
	}

	@Override
	protected int skipCommentChars() {
		int count = 0;
		while (read() == '/') {
			++count;
		}
		unread();
		return count;
	}

}
