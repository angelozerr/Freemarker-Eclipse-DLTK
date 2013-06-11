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

import org.eclipse.dltk.launching.ScriptLaunchConfigurationConstants;

/**
 * Constants for Freemarker launch.
 * 
 */
public class FreemarkerLaunchConfigurationConstants extends
		ScriptLaunchConfigurationConstants {

	public static final String ID_FREEMARKER_SCRIPT = "org.eclipse.dltk.freemarker.launching.FreemarkerLaunchConfigurationType"; //$NON-NLS-1$

	public static final String ID_FREEMARKER_PROCESS_TYPE = "freemarkerInterpreter"; //$NON-NLS-1$

	protected FreemarkerLaunchConfigurationConstants() {

	}

}
