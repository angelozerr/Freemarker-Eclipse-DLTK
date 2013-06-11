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
package org.eclipse.dltk.freemarker.internal.ui.editor.validation;

import org.eclipse.swt.widgets.Control;

/**
 * IControlValdiator
 *
 */
public interface IControlValidator {

	/**
	 * Enable / disable the validator.
	 * @param enabled
	 */
	public void setEnabled(boolean enabled);

	/**
	 * Determine whether the validator is enabled / disabled 
	 * @return
	 */
	public boolean getEnabled();

	/**
	 * Validate the control (manual validation).
	 * @return
	 */
	public boolean validate();

	/**
	 * Get the control that this validator validates.
	 * @return
	 */
	public Control getControl();

	/**
	 * Determine whether the control contents are valid.  No validation is
	 * done.  Validity is determined by the last time the control was validated
	 * @return
	 */
	public boolean isValid();

	/**
	 * Reset the validator.  Clear error messages and reset state.
	 */
	public void reset();

	/**
	 * Controls whether the message handler automatically updates messages in 
	 * the form. Setting the refresh to true, triggers an immediate update 
	 * @param refresh
	 */
	public void setRefresh(boolean refresh);

}
