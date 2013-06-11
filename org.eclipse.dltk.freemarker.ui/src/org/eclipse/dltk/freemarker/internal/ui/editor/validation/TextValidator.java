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

import org.eclipse.core.resources.IProject;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.widgets.Text;
import org.eclipse.ui.forms.IManagedForm;

/**
 * TextControlValidator
 *
 */
public abstract class TextValidator extends AbstractControlValidator {

	private ModifyListener fModifyListener;

	private boolean fAutoValidate;

	private String fCurrentText;

	/**
	 * @param managedForm
	 * @param control
	 * @param project
	 * @param autoValidate
	 */
	public TextValidator(IManagedForm managedForm, Text control, IProject project, boolean autoValidate) {
		super(managedForm, control, project);
		// Turn on / off auto-validation
		// If auto-validation is on, validation is triggered by modify text 
		// events.  Otherwise, manual calls to validate must be made.
		fAutoValidate = autoValidate;
		// Initialize the text validator
		intialize();
	}

	/**
	 * 
	 */
	private void intialize() {
		// Save the current contents of the Text field
		fCurrentText = getText().getText();
		// No listeners required if auto-validation is off
		if (fAutoValidate == false) {
			return;
		}
		// Create the listeners for auto-validation
		createListeners();
		// Add the listeners if the validator is enabled
		if (getEnabled()) {
			addListeners();
		}
	}

	/* (non-Javadoc)
	 * @see org.eclipse.pde.internal.ui.editor.validation.AbstractControlValidator#setEnabled(boolean)
	 */
	public void setEnabled(boolean enabled) {
		// Nothing to do here if enablement is not being changed
		if (getEnabled() == enabled) {
			return;
		}
		// Update validator enablement
		super.setEnabled(enabled);
		// No listeners required if auto-validation is off
		if (fAutoValidate == false) {
			return;
		}
		// Add listeners if enabled; otherwise, remove them
		if (getEnabled()) {
			addListeners();
		} else {
			removeListeners();
		}
	}

	/**
	 * 
	 */
	protected void createListeners() {
		fModifyListener = new ModifyListener() {
			public void modifyText(ModifyEvent e) {
				handleModifyTextEvent(e);
			}
		};
	}

	/**
	 * @param e
	 */
	protected void handleModifyTextEvent(ModifyEvent e) {
		// Validation is not required if the current text contents is the 
		// same as the new text contents
		String newText = getText().getText();
		if (newText.equals(fCurrentText)) {
			return;
		}
		// Save the current contents of the Text field
		fCurrentText = newText;
		// Perform auto-validation
		validate();
	}

	/**
	 * 
	 */
	protected void addListeners() {
		getText().addModifyListener(fModifyListener);
	}

	/**
	 * 
	 */
	protected void removeListeners() {
		getText().removeModifyListener(fModifyListener);
	}

	/**
	 * @return
	 */
	protected Text getText() {
		return (Text) getControl();
	}

	/* (non-Javadoc)
	 * @see org.eclipse.pde.internal.ui.editor.validation.AbstractControlValidator#autoEnable()
	 */
	protected boolean autoEnable() {
		// Enable validator if the text field is editable
		if (getText().getEditable() == false) {
			return false;
		}
		return super.autoEnable();
	}
}
