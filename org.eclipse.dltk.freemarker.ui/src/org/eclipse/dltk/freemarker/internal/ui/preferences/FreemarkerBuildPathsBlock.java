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
package org.eclipse.dltk.freemarker.internal.ui.preferences;

import org.eclipse.dltk.ui.util.IStatusChangeListener;
import org.eclipse.dltk.ui.wizards.BuildpathsBlock;
import org.eclipse.jface.operation.IRunnableContext;
import org.eclipse.ui.preferences.IWorkbenchPreferenceContainer;

/**
 * Freemarker {@link BuildpathsBlock}.
 * 
 */
public class FreemarkerBuildPathsBlock extends BuildpathsBlock {

	public FreemarkerBuildPathsBlock(IRunnableContext runnableContext,
			IStatusChangeListener context, int pageToShow, boolean useNewPage,
			IWorkbenchPreferenceContainer pageContainer) {
		super(runnableContext, context, pageToShow, useNewPage, pageContainer);
	}

	@Override
	protected boolean supportZips() {
		return true;
	}
}
