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
package org.eclipse.dltk.freemarker.internal.ui.editor;

import org.eclipse.core.resources.IProject;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.dltk.freemarker.core.settings.SettingsScope;
import org.eclipse.dltk.freemarker.core.settings.provider.IObjectInstanceProvider;
import org.eclipse.dltk.freemarker.core.settings.provider.IProjectObjectInstanceProvider;
import org.eclipse.dltk.freemarker.core.util.StringUtils;
import org.eclipse.dltk.freemarker.internal.ui.FreemarkerUIPluginImages;
import org.eclipse.dltk.freemarker.internal.ui.FreemarkerUIPluginMessages;
import org.eclipse.dltk.freemarker.internal.ui.dialogs.OpenProjectLevelConfigurationDialog;
import org.eclipse.dltk.freemarker.internal.ui.editor.validation.ControlValidationUtility;
import org.eclipse.dltk.freemarker.internal.ui.editor.validation.TextValidator;
import org.eclipse.dltk.freemarker.internal.ui.jdt.FreemarkerJavaHelperUI;
import org.eclipse.dltk.freemarker.internal.ui.jdt.contentassist.TypeFieldAssistDisposer;
import org.eclipse.dltk.freemarker.internal.ui.parts.FormEntry;
import org.eclipse.dltk.freemarker.internal.ui.parts.FormEntryAdapter;
import org.eclipse.dltk.freemarker.ui.FreemarkerUIPlugin;
import org.eclipse.jdt.core.IMethod;
import org.eclipse.jdt.core.IType;
import org.eclipse.jdt.core.search.IJavaSearchConstants;
import org.eclipse.jdt.ui.IJavaElementSearchConstants;
import org.eclipse.jface.dialogs.ErrorDialog;
import org.eclipse.jface.window.Window;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.BusyIndicator;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.ui.IActionBars;
import org.eclipse.ui.forms.events.HyperlinkAdapter;
import org.eclipse.ui.forms.events.HyperlinkEvent;
import org.eclipse.ui.forms.widgets.FormToolkit;
import org.eclipse.ui.forms.widgets.ImageHyperlink;
import org.eclipse.ui.forms.widgets.Section;
import org.eclipse.ui.forms.widgets.TableWrapData;

/**
 * Template specific configuration section used to select a configuration/model
 * provider only used by the template.
 * 
 */
public abstract class TemplateSpecificConfigurationSection<T extends ClassProviderFormPage>
		extends FreemarkerSection<T> {

	private FormEntry classField;
	private TypeFieldAssistDisposer classFieldAssist;
	private TextValidator classFieldValidator;

	private FormEntry methodField;
	private TextValidator methodFieldValidator;

	public TemplateSpecificConfigurationSection(T page, Composite parent) {
		super(page, parent, Section.DESCRIPTION | Section.TWISTIE
				| Section.EXPANDED);
		createClient(getSection(), page.getEditor().getToolkit());
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.dltk.freemarker.internal.ui.editor.FreemarkerSection#createClient
	 * (org.eclipse.ui.forms.widgets.Section,
	 * org.eclipse.ui.forms.widgets.FormToolkit)
	 */
	protected void createClient(Section section, FormToolkit toolkit) {
		// Title + Description Section
		section.setText(getTitle());
		section.setDescription(getDescription());
		// Layout section
		section.setLayout(FormLayoutFactory
				.createClearTableWrapLayout(false, 1));
		TableWrapData data = new TableWrapData(TableWrapData.FILL_GRAB);
		section.setLayoutData(data);

		// Body section
		Composite client = toolkit.createComposite(section);
		client.setLayout(FormLayoutFactory.createSectionClientTableWrapLayout(
				false, 3));
		section.setClient(client);

		// Actions bars section
		IActionBars actionBars = getPage().getFreemarkerEditor()
				.getEditorSite().getActionBars();

		// Create class field
		createClassEntry(client, toolkit, actionBars);
		// Create method field
		createMethodField(client, toolkit, actionBars);
		// Link
		createSetAsProjectLevelLink(client, toolkit);
		toolkit.paintBordersFor(client);

	}

	protected final void createSetAsProjectLevelLink(Composite parent,
			FormToolkit toolkit) {
		final ImageHyperlink link = toolkit.createImageHyperlink(parent,
				SWT.WRAP);
		link.addHyperlinkListener(new HyperlinkAdapter() {
			public void linkActivated(HyperlinkEvent e) {
				// Open the dialog which save the provider project settings.
				BusyIndicator.showWhile(link.getDisplay(), new Runnable() {
					public void run() {
						OpenProjectLevelConfigurationDialog dialog;
						try {
							String className = classField.getValue();
							String methodName = methodField.getValue();
							dialog = new OpenProjectLevelConfigurationDialog(
									getSection().getShell(),
									getTemplateSettings().getProjectSettings(),
									className, methodName, getPage()
											.getProviderType());
							dialog.create();
							if (dialog.open() == Window.OK) {
								IProjectObjectInstanceProvider projectProvider = dialog
										.getProjectInstanceProvider();
								if (projectProvider != null) {
									getPage().selectNewInstanceProvider(dialog);
								}
							}
						} catch (CoreException e) {
							IStatus status = new Status(IStatus.ERROR,
									FreemarkerUIPlugin.PLUGIN_ID, 0,
									"Error while loading project settings", e);
							ErrorDialog.openError(link.getShell(),
									"Settings error",
									"Error while loading project settings",
									status);

						}
					}
				});
			}
		});
		link
				.setText(FreemarkerUIPluginMessages.TemplateSpecificConfigurationSection_setAsProjectLevelConfig);
		link
				.setImage(FreemarkerUIPluginImages
						.get(FreemarkerUIPluginImages.OBJ_DESC_SET_AS_PROJECT_LEVEL_FM_OBJ));
		link.setLayoutData(new TableWrapData(TableWrapData.FILL,
				TableWrapData.TOP, 1, 3));
	}

	// ------------- Class entry

	/**
	 * Create Text field which is used to select Model provider class.
	 * 
	 * @param client
	 * @param toolkit
	 * @param actionBars
	 */
	private void createClassEntry(Composite client, FormToolkit toolkit,
			IActionBars actionBars) {

		boolean isEditable = true;

		// Create FormEntry for the Class Model provider.
		classField = new FormEntry(
				client,
				toolkit,
				FreemarkerUIPluginMessages.TemplateSpecificConfigurationSection_class,
				FreemarkerUIPluginMessages.TemplateSpecificConfigurationSection_browse,
				isEditable);

		// Add listener to react to the button Browse click, select Java Class
		// Model....
		classField.setFormEntryListener(new FormEntryAdapter(this, actionBars) {

			public void textValueChanged(FormEntry entry) {

			}

			public void linkActivated(HyperlinkEvent e) {
				// Class link is clicked : open the model class provider with
				// Java Editor.
				openClassIntoJavaEditor();
			}

			public void browseButtonSelected(FormEntry entry) {
				// Browse... button is clicked : open dialog to select Java
				// Class.
				openClassSelectionDialog(entry.getValue());
			}
		});
		classField.setEditable(isEditable);

		if (isEditable) {
			// Add Autocompletion to search Class Model provider.
			classFieldAssist = FreemarkerJavaHelperUI.addTypeFieldAssistToText(
					classField.getText(), getProject(),
					IJavaSearchConstants.CLASS);
		}
		// Create validator
		classFieldValidator = new TextValidator(getManagedForm(), classField
				.getText(), getProject(), true) {
			protected boolean validateControl() {
				return validateClassField();
			}
		};

	}

	/**
	 * Validate class field.
	 * 
	 * @return
	 */
	private boolean validateClassField() {
		String className = classField.getText().getText();
		boolean result = ControlValidationUtility.validateClassField(className,
				classFieldValidator, getProject(), false);
		if (result) {
			boolean methodFieldEnabled = !StringUtils.isEmpty(className);
			methodField.setEditable(methodFieldEnabled);
			methodFieldValidator.setEnabled(methodFieldEnabled);
		} else {
			methodField.setValue("");
			methodField.setEditable(false);
		}
		return result;
	}

	/**
	 * Open Java Class Dialog to select a Java class.
	 * 
	 * @param className
	 */
	private void openClassSelectionDialog(String className) {
		IProject resource = super.getProject();
		IType type = FreemarkerJavaHelperUI.selectType(resource,
				IJavaElementSearchConstants.CONSIDER_CLASSES, className, null);
		if (type != null)
			classField.setValue(type.getFullyQualifiedName('$'));
	}

	/**
	 * Open Java class selected into a Java Editor.
	 */
	private void openClassIntoJavaEditor() {
		// Java Class is selected
		String value = classField.getValue();
		IProject project = getProject();
		if (project == null)
			return;
		value = FreemarkerJavaHelperUI.createClass(value, null, project, null,
				false);
		if (value != null)
			classField.setValue(value);
	}

	// ------------- Method entry

	private void createMethodField(Composite client, FormToolkit toolkit,
			IActionBars actionBars) {
		boolean isEditable = classField.isFilled();

		// Create FormEntry for the Class Model provider.
		methodField = new FormEntry(
				client,
				toolkit,
				FreemarkerUIPluginMessages.TemplateSpecificConfigurationSection_method,
				FreemarkerUIPluginMessages.TemplateSpecificConfigurationSection_browse, // 
				true);

		// Add listener to react to the button Browse click, select Java Class
		// Model....
		methodField
				.setFormEntryListener(new FormEntryAdapter(this, actionBars) {

					public void textValueChanged(FormEntry entry) {

					}

					public void linkActivated(HyperlinkEvent e) {
						// Class link is clicked : open the model class provider
						// with
						// Java Editor.
						doOpenModelProviderMethodWithJavaEditor();
					}

					public void browseButtonSelected(FormEntry entry) {
						// Browse... button is clicked : open dialog to select
						// Java
						// Class.
						openMethodSelectionDialog(classField.getValue(), entry
								.getValue());
					}
				});
		methodField.setEditable(isEditable);

		// Create validator
		methodFieldValidator = new TextValidator(getManagedForm(), methodField
				.getText(), getProject(), true) {
			protected boolean validateControl() {
				return validateMethodField();
			}
		};

	}

	/**
	 * Validate method field.
	 * 
	 * @return
	 */
	private boolean validateMethodField() {
		String className = classField.getText().getText();
		String methodName = methodField.getText().getText();
		boolean result = ControlValidationUtility.validateMethodProviderField(
				getPage().getProviderType(), className, methodName,
				methodFieldValidator, getProject(), false);
		return result;
	}

	/**
	 * Open Java Method Dialog to select a Java Method.
	 * 
	 * @param className
	 */
	private void openMethodSelectionDialog(String className, String methodName) {
		IProject resource = super.getProject();
		IMethod method = FreemarkerJavaHelperUI.handleAdd(resource, className,
				methodName);
		if (method != null)
			methodField.setValue(method.getElementName());
	}

	/**
	 * Open Java model class selected into a Java Editor.
	 */
	private void doOpenModelProviderMethodWithJavaEditor() {
		// Java Class Model is selected
		String className = classField.getValue();
		String methodName = methodField.getValue();
		IProject project = getProject();
		if (project == null)
			return;
		String value = FreemarkerJavaHelperUI.createClass(className,
				methodName, project, null, false);
		if (value != null)
			methodField.setValue(value);
	}

	@Override
	public void refresh() {
		if (classField.isDirty() == false) {
			String initialClassName = getInitialClassName();
			classField.setValue(initialClassName, true);
		}
		if (methodField.isDirty() == false) {
			String initialMethodName = getInitialMethodName();
			methodField.setValue(initialMethodName, true);
		}
		super.refresh();
	}

	@Override
	public void cancelEdit() {
		classField.cancelEdit();
		methodField.cancelEdit();
		super.cancelEdit();
	}

	@Override
	public void commit(boolean onSave) {
		classField.commit();
		methodField.commit();
		if (onSave) {
			if (getSection().isExpanded()) {
				// Section is enabled (user a selected the Template specific
				// section, save it)
				IObjectInstanceProvider instanceProvider = getObjectInstanceProviderTemplateScope(true);
				if (instanceProvider != null) {
					instanceProvider.setClassName(classField.getValue());
					instanceProvider.setMethodName(methodField.getValue());
				}
			}
		}
		super.commit(onSave);
	}

	public void dispose() {
		super.dispose();
		if (classFieldAssist != null) {
			classFieldAssist.dispose();
		}
	}

	protected String getInitialClassName() {
		IObjectInstanceProvider instanceProvider = getObjectInstanceProviderTemplateScope(false);
		if (instanceProvider != null) {
			return instanceProvider.getClassName();
		}
		return null;
	}

	protected String getInitialMethodName() {
		IObjectInstanceProvider instanceProvider = getObjectInstanceProviderTemplateScope(false);
		if (instanceProvider != null) {
			return instanceProvider.getMethodName();
		}
		return null;
	}

	protected IObjectInstanceProvider getObjectInstanceProviderTemplateScope(
			boolean force) {
		if (force) {
			IObjectInstanceProvider instanceProvider = getPage()
					.getObjectInstanceProvider();
			if (instanceProvider == null
					|| instanceProvider.getScope() != SettingsScope.TEMPLATE) {
				getPage().setInstanceProviderScope(SettingsScope.TEMPLATE);
			}
			return getPage().getObjectInstanceProvider();

		} else {
			IObjectInstanceProvider instanceProvider = getPage()
					.getObjectInstanceProvider();
			if (instanceProvider != null
					&& instanceProvider.getScope() == SettingsScope.TEMPLATE) {
				return instanceProvider;
			}
		}
		return null;
	}

	public void setFocus() {
		classField.getText().setFocus();
	}

	protected abstract String getTitle();

	protected abstract String getDescription();

}
