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
package org.eclipse.dltk.freemarker.internal.ui.text.scanners;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.dltk.freemarker.internal.ui.text.FreemarkerWhitespaceDetector;
import org.eclipse.dltk.freemarker.internal.ui.text.IFreemarkerColorConstants;
import org.eclipse.dltk.ui.text.AbstractScriptScanner;
import org.eclipse.dltk.ui.text.IColorManager;
import org.eclipse.jface.preference.IPreferenceStore;
import org.eclipse.jface.text.rules.IRule;
import org.eclipse.jface.text.rules.WhitespaceRule;

/**
 * Freemarker String scanner.
 * 
 */
public class FreemarkerStringScanner extends AbstractScriptScanner {

	private static final String[] fgTokenProperties = new String[] {
			IFreemarkerColorConstants.FREEMARKER_STRING,
			IFreemarkerColorConstants.FREEMARKER_DEFAULT,
	/*
	 * IFreemarkerColorConstants.FREEMARKER_CLASS_VARIABLE,
	 * IFreemarkerColorConstants.FREEMARKER_GLOBAL_VARIABLE,
	 * IFreemarkerColorConstants.FREEMARKER_INSTANCE_VARIABLE
	 */};

	public FreemarkerStringScanner(IColorManager manager, IPreferenceStore store) {
		super(manager, store);

		initialize();
	}

	protected String[] getTokenProperties() {
		return fgTokenProperties;
	}

	protected List<IRule> createRules() {
		List<IRule> rules = new ArrayList<IRule>();

		// Add generic whitespace rule.
		rules.add(new WhitespaceRule(new FreemarkerWhitespaceDetector()));
		setDefaultReturnToken(getToken(IFreemarkerColorConstants.FREEMARKER_STRING));

		return rules;
	}
}
