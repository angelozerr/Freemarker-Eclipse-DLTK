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
package org.eclipse.dltk.freemarker.launching;

import org.eclipse.dltk.core.DLTKIdContributionSelector;
import org.eclipse.dltk.core.PreferencesLookupDelegate;
import org.eclipse.dltk.freemarker.debug.FreemarkerDebugConstants;
import org.eclipse.dltk.freemarker.debug.FreemarkerDebugPlugin;

/**
 * Freemarker debugging engine id based selector
 */
public class FreemarkerDebuggingEngineSelector extends
		DLTKIdContributionSelector {
	/*
	 * @see
	 * org.eclipse.dltk.core.DLTKIdContributionSelector#getSavedContributionId
	 * (org.eclipse.dltk.core.PreferencesLookupDelegate)
	 */
	protected String getSavedContributionId(PreferencesLookupDelegate delegate) {
		return delegate.getString(FreemarkerDebugPlugin.PLUGIN_ID,
				FreemarkerDebugConstants.DEBUGGING_ENGINE_ID_KEY);
	}

}
