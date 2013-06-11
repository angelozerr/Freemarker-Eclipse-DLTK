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
package org.eclipse.dltk.freemarker.internal.debug.ui.launcher;

import org.eclipse.debug.core.ILaunchConfigurationType;
import org.eclipse.debug.ui.ILaunchShortcut;
import org.eclipse.dltk.freemarker.core.FreemarkerNature;
import org.eclipse.dltk.freemarker.launching.FreemarkerLaunchConfigurationConstants;
import org.eclipse.dltk.internal.debug.ui.launcher.AbstractScriptLaunchShortcut;

/**
 * 
 * Freemarker Debug {@link ILaunchShortcut} implementation.
 * 
 */
public class FreemarkerLaunchShortcut extends AbstractScriptLaunchShortcut {

	protected ILaunchConfigurationType getConfigurationType() {
		return getLaunchManager().getLaunchConfigurationType(
				FreemarkerLaunchConfigurationConstants.ID_FREEMARKER_SCRIPT);
	}

	protected String getNatureId() {
		return FreemarkerNature.NATURE_ID;
	}

}
