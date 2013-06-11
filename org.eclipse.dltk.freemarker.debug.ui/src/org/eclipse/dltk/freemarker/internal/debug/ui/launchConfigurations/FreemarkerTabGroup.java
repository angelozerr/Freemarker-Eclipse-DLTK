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
package org.eclipse.dltk.freemarker.internal.debug.ui.launchConfigurations;

import org.eclipse.debug.ui.AbstractLaunchConfigurationTabGroup;
import org.eclipse.debug.ui.CommonTab;
import org.eclipse.debug.ui.EnvironmentTab;
import org.eclipse.debug.ui.ILaunchConfigurationDialog;
import org.eclipse.debug.ui.ILaunchConfigurationTab;
import org.eclipse.dltk.debug.ui.launchConfigurations.InterpreterTab;
import org.eclipse.dltk.debug.ui.launchConfigurations.ScriptArgumentsTab;

/**
 * 
 * Call FreemarkerMainLaunchConfigurationTab.
 */
public class FreemarkerTabGroup extends AbstractLaunchConfigurationTabGroup {
	public void createTabs(ILaunchConfigurationDialog dialog, String mode) {

		FreemarkerMainLaunchConfigurationTab main = new FreemarkerMainLaunchConfigurationTab(
				mode);
		ILaunchConfigurationTab[] tabs = new ILaunchConfigurationTab[] { main,
				new ScriptArgumentsTab(), new InterpreterTab(main),
				new EnvironmentTab(), new CommonTab() };
		setTabs(tabs);
	}
}
