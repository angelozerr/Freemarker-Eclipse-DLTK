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
package org.eclipse.dltk.freemarker.internal.ui.dialogs;

import org.eclipse.core.resources.IProject;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.dltk.freemarker.core.settings.IFreemarkerProjectSettings;
import org.eclipse.dltk.freemarker.core.settings.provider.IObjectInstanceProvider;
import org.eclipse.dltk.freemarker.core.settings.provider.IProjectObjectInstanceProvider;
import org.eclipse.dltk.freemarker.core.settings.provider.IObjectInstanceProvider.ProviderType;
import org.eclipse.dltk.freemarker.core.util.StringUtils;
import org.eclipse.dltk.freemarker.internal.ui.editor.validation.ControlValidationUtility;
import org.eclipse.dltk.freemarker.internal.ui.editor.validation.IValidatorMessageHandler;
import org.eclipse.dltk.freemarker.internal.ui.jdt.FreemarkerJavaHelperUI;
import org.eclipse.dltk.freemarker.ui.FreemarkerUIPlugin;
import org.eclipse.jdt.core.IMethod;
import org.eclipse.jdt.core.IType;
import org.eclipse.jdt.core.search.IJavaSearchConstants;
import org.eclipse.jdt.ui.IJavaElementSearchConstants;
import org.eclipse.jface.dialogs.ErrorDialog;
import org.eclipse.jface.dialogs.IDialogConstants;
import org.eclipse.jface.dialogs.IMessageProvider;
import org.eclipse.jface.dialogs.TitleAreaDialog;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;
import org.eclipse.ui.forms.IManagedForm;
import org.eclipse.ui.forms.IMessageManager;

/**
 * Dialog which manage the {@link IObjectInstanceProvider} settings project.
 * 
 */
public class OpenProjectLevelConfigurationDialog extends TitleAreaDialog
		implements IValidatorMessageHandler {

	private enum JavaFieldType {
		CLASS, METHOD
	}

	private final IFreemarkerProjectSettings projectSettings;
	private final ProviderType providerType;
	private final String initialClassName;
	private final String initialMethodName;

	private Text nameConfigField;
	private Text classField;
	private Text methodField;
	private Button defaultConfigField;

	private boolean fieldsInitializing = false;
	private IProjectObjectInstanceProvider projectInstanceProvider;
	private boolean validating;
	private boolean editMode;

	/**
	 * Set as project level.
	 * 
	 * @param parent
	 * @param projectSettings
	 * @param instanceProvider
	 */
	public OpenProjectLevelConfigurationDialog(Shell parent,
			IFreemarkerProjectSettings projectSettings,
			IProjectObjectInstanceProvider projectInstanceProvider) {
		this(parent, projectSettings, projectInstanceProvider.getClassName(),
				projectInstanceProvider.getMethodName(),
				projectInstanceProvider.getType());
		this.projectInstanceProvider = projectInstanceProvider;
		editMode = true;
	}

	public OpenProjectLevelConfigurationDialog(Shell parent,
			IFreemarkerProjectSettings projectSettings, String className,
			String methodName, ProviderType providerType) {
		super(parent);
		this.providerType = providerType;
		this.projectSettings = projectSettings;
		this.initialClassName = className;
		this.initialMethodName = methodName;
		editMode = false;
	}

	@Override
	protected Control createDialogArea(Composite parent) {
		// top level composite
		Composite parentComposite = (Composite) super.createDialogArea(parent);

		initializeDialogUnits(parentComposite);

		// Set title Shell + Dialog
		getShell().setText("Project settings");
		String titleDialog = (providerType == ProviderType.CONFIGURATION) ? "Freemarker Configuration provider"
				: "Data-Model provider";
		setTitle(titleDialog);

		// creates dialog area composite
		Composite contents = createComposite(parentComposite);
		return contents;
	}

	@Override
	protected Control createContents(Composite parent) {
		Control control = super.createContents(parent);
		initializeFields();
		return control;
	}

	/**
	 * Create fields of the dialog.
	 * 
	 * @param parent
	 * @return
	 */
	private Composite createComposite(Composite parent) {
		Composite panel = new Composite(parent, SWT.NONE);
		panel.setLayoutData(new GridData(GridData.FILL_BOTH));

		GridLayout layout = new GridLayout(3, false);
		panel.setLayout(layout);

		createNameConfigField(panel);
		createClassField(panel);
		createMethodField(panel);
		createDefaultConfigField(panel);
		return panel;
	}

	// ------------- Name configuration field

	/**
	 * 
	 * @param parent
	 */
	private void createNameConfigField(Composite parent) {
		nameConfigField = createLabelTextField(parent, "Name:", false);
		// Validate name config field.
		nameConfigField.addModifyListener(new ModifyListener() {

			public void modifyText(ModifyEvent e) {
				validate();
			}
		});

	}

	/**
	 * Validate name config.
	 * 
	 * @return
	 */
	private boolean validateNameConfigField() {

		String name = nameConfigField.getText();
		return ControlValidationUtility.validateNameConfigField(name,
				projectSettings, providerType, !editMode,
				this);
	}

	// ------------- Class field

	/**
	 * Create Class field.
	 * 
	 * @param parent
	 */
	private void createClassField(Composite parent) {
		// Create Label+Text+Browse button Class field
		classField = createJavaField(parent, JavaFieldType.CLASS);

		// Add completion to search classes
		FreemarkerJavaHelperUI.addTypeFieldAssistToText(classField,
				projectSettings.getProject(), IJavaSearchConstants.CLASS);

		// Validate class fields.
		classField.addModifyListener(new ModifyListener() {

			public void modifyText(ModifyEvent e) {
				validate();
			}
		});
	}

	/**
	 * Open Java Class Dialog to select a Java class Model.
	 * 
	 * @param className
	 */
	private void openClassSelectionDialog(String className) {
		IProject resource = getProject();
		IType type = FreemarkerJavaHelperUI.selectType(resource,
				IJavaElementSearchConstants.CONSIDER_CLASSES, className, null);
		if (type != null)
			classField.setText(type.getFullyQualifiedName('$'));
	}

	/**
	 * Validate class field.
	 * 
	 * @return
	 */
	private boolean validateClassField() {
		String className = classField.getText();
		boolean classFieldOK = ControlValidationUtility.validateClassField(
				className, this, getProject(), true);
		if (classFieldOK) {
			methodField.setEditable(!StringUtils.isEmpty(className));
		} else {
			methodField.setText("");
			methodField.setEditable(false);
		}
		return classFieldOK;
	}

	// ------------- Method field

	/**
	 * Create Method field.
	 * 
	 * @param parent
	 */
	private void createMethodField(Composite parent) {
		// Create Label+Text+Browse button Class field
		methodField = createJavaField(parent, JavaFieldType.METHOD);

		// Validate method fields.
		methodField.addModifyListener(new ModifyListener() {

			public void modifyText(ModifyEvent e) {
				validate();
			}
		});

	}

	/**
	 * Validate method field.
	 * 
	 */
	private boolean validateMethodField() {
		String className = classField.getText();
		String methodName = methodField.getText();
		return ControlValidationUtility.validateMethodProviderField(
				providerType, className, methodName, this, getProject(), true);
	}

	/**
	 * Open Java Method Dialog to select a Java Method.
	 * 
	 * @param className
	 */
	private void openMethodSelectionDialog(String className, String methodName) {
		IProject resource = getProject();
		IMethod method = FreemarkerJavaHelperUI.handleAdd(resource, className,
				methodName);
		if (method != null)
			methodField.setText(method.getElementName());
	}

	// ------------- Default config checkbox field

	/**
	 * Create default config checkbox field.
	 * 
	 * @param panel
	 */
	private void createDefaultConfigField(Composite parent) {
		defaultConfigField = createLabelChecboxField(parent, "Default:",
				"Is default project configuration?", false);
	}

	/**
	 * Create Java field, with Label+text+Browse button.
	 * 
	 * @param parent
	 * @param type
	 * @return
	 */
	private Text createJavaField(Composite parent, final JavaFieldType type) {

		String labelText = (type == JavaFieldType.CLASS) ? "Class:" : "Method:";
		Text textField = createLabelTextField(parent, labelText, true);

		// Browse Button
		Button browseButton = new Button(parent, SWT.NONE);
		browseButton.setText("Browse...");
		browseButton.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				browseButtonClicked(type);
			}

		});
		return textField;
	}

	/**
	 * Create field, with Label+text.
	 * 
	 * @param parent
	 * @param type
	 * @return
	 */
	private Text createLabelTextField(Composite parent, String labelText,
			boolean hasButton) {
		// Label
		Label label = new Label(parent, SWT.NONE);
		label.setText(labelText);

		// Text
		Text textField = new Text(parent, SWT.BORDER);
		if (!hasButton) {
			GridData gridData = new GridData(GridData.FILL_HORIZONTAL);
			gridData.horizontalSpan = 2;
			textField.setLayoutData(gridData);
		} else {
			textField.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
		}
		return textField;
	}

	/**
	 * Create field, with Label+text.
	 * 
	 * @param parent
	 * @param type
	 * @return
	 */
	private Button createLabelChecboxField(Composite parent, String labelText,
			String checkboxLabelText, boolean hasButton) {
		// Label
		Label label = new Label(parent, SWT.NONE);
		label.setText(labelText);

		// Checkbox
		Button checkboxField = new Button(parent, SWT.CHECK);
		checkboxField.setText(checkboxLabelText);
		if (!hasButton) {
			GridData gridData = new GridData(GridData.FILL_HORIZONTAL);
			gridData.horizontalSpan = 2;
			checkboxField.setLayoutData(gridData);
		} else {
			checkboxField.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
		}
		return checkboxField;
	}

	private void initializeFields() {
		try {
			fieldsInitializing = true;
			if (projectInstanceProvider != null) {
				String nameConfig = projectInstanceProvider.getName();
				if (!StringUtils.isEmpty(nameConfig)) {
					nameConfigField.setText(nameConfig);
				}
				defaultConfigField.setSelection(projectInstanceProvider
						.isDefaultConfig());
			}
			classField
					.setText(initialClassName != null ? initialClassName : "");
			methodField.setText(initialMethodName != null ? initialMethodName
					: "");

		} finally {
			fieldsInitializing = false;
			validate();
			if (editMode) {
				// No change, OK button is disabled.
				getButton(IDialogConstants.OK_ID).setEnabled(false);		
			}
		}

	}

	private IProject getProject() {
		return projectSettings.getProject();
	}

	/**
	 * Nrowse button clicked, open a dialog.
	 * 
	 * @param type
	 */
	private void browseButtonClicked(JavaFieldType type) {
		String className = classField.getText();
		switch (type) {
		case CLASS:
			openClassSelectionDialog(className);
			break;
		case METHOD:
			String methodName = methodField.getText();
			openMethodSelectionDialog(className, methodName);
			break;
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @seeorg.eclipse.dltk.freemarker.internal.ui.editor.validation.
	 * IValidatorMessageHandler#addMessage(java.lang.Object, java.lang.String,
	 * int)
	 */
	public void addMessage(Object key, String messageText, int messageType) {

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @seeorg.eclipse.dltk.freemarker.internal.ui.editor.validation.
	 * IValidatorMessageHandler#addMessage(java.lang.String, int)
	 */
	public void addMessage(String messageText, int messageType) {
		if (messageType == IMessageProvider.ERROR) {
			setErrorMessage(messageText);
		} else {
			setMessage(messageText, messageType);
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @seeorg.eclipse.dltk.freemarker.internal.ui.editor.validation.
	 * IValidatorMessageHandler#getManagedForm()
	 */
	public IManagedForm getManagedForm() {
		return null;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @seeorg.eclipse.dltk.freemarker.internal.ui.editor.validation.
	 * IValidatorMessageHandler#getMessageManager()
	 */
	public IMessageManager getMessageManager() {
		return null;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @seeorg.eclipse.dltk.freemarker.internal.ui.editor.validation.
	 * IValidatorMessageHandler#getMessagePrefix()
	 */
	public String getMessagePrefix() {
		return null;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @seeorg.eclipse.dltk.freemarker.internal.ui.editor.validation.
	 * IValidatorMessageHandler#removeMessage(java.lang.Object)
	 */
	public void removeMessage(Object key) {
		setMessage("", IMessageProvider.NONE);

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @seeorg.eclipse.dltk.freemarker.internal.ui.editor.validation.
	 * IValidatorMessageHandler#setMessagePrefix(java.lang.String)
	 */
	public void setMessagePrefix(String prefix) {

	}

	private void validate() {
		if (fieldsInitializing)
			return;
		if (validating)
			return;
		try {
			validating = true;
			boolean ok = validateFields();
			if (validateFields()) {
				super.setErrorMessage(null);
			}
			getButton(IDialogConstants.OK_ID).setEnabled(ok);
		} finally {
			validating = false;
		}
	}

	private boolean validateFields() {
		if (!validateNameConfigField())
			return false;
		if (!validateClassField())
			return false;
		if (!validateMethodField())
			return false;
		return true;
	}

	@Override
	protected void okPressed() {
		try {
			// Update the provider instance with fields of the dialog
			if (this.projectInstanceProvider == null) {
				projectInstanceProvider = projectSettings
						.createProvider(providerType);
			}
			projectInstanceProvider.setName(nameConfigField.getText());
			projectInstanceProvider.setClassName(classField.getText());
			projectInstanceProvider.setMethodName(methodField.getText());
			projectInstanceProvider.setDefaultConfig(defaultConfigField
					.getSelection());
			
			// Save setting
			projectSettings.save();
			
		} catch (Throwable e) {
			IStatus status = new Status(IStatus.ERROR,
					FreemarkerUIPlugin.PLUGIN_ID, 0,
					"Error while saving project settings", e);
			ErrorDialog.openError(getShell(), "Project Settings error",
					"Error while saving project settings", status);

		}
		super.okPressed();
	}

	@Override
	protected void cancelPressed() {
		this.projectInstanceProvider = null;
		super.cancelPressed();
	}

	public IProjectObjectInstanceProvider getProjectInstanceProvider() {
		return projectInstanceProvider;
	}
}
