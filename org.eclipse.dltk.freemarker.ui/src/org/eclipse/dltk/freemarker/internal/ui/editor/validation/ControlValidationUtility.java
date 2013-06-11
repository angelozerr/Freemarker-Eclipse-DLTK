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
import org.eclipse.core.runtime.CoreException;
import org.eclipse.dltk.freemarker.core.jdt.FreemarkerJavaHelper;
import org.eclipse.dltk.freemarker.core.settings.IFreemarkerProjectSettings;
import org.eclipse.dltk.freemarker.core.settings.provider.IObjectInstanceProvider.ProviderType;
import org.eclipse.dltk.freemarker.core.util.StringUtils;
import org.eclipse.dltk.freemarker.internal.ui.FreemarkerUIPluginMessages;
import org.eclipse.jdt.core.IJavaProject;
import org.eclipse.jdt.core.IMethod;
import org.eclipse.jdt.core.JavaCore;
import org.eclipse.jface.dialogs.IMessageProvider;

public class ControlValidationUtility {

	/**
	 * Validate class field.
	 * 
	 * @param text
	 * @param validator
	 * @param project
	 * @return
	 */
	public static boolean validateClassField(String className,
			IValidatorMessageHandler validator, IProject project,
			boolean required) {

		if (StringUtils.isEmpty(className)) {
			if (required) {
				validator
						.addMessage(
								FreemarkerUIPluginMessages.ControlValidationUtility_errorMsgClassRequired,
								IMessageProvider.ERROR);
				return false;
			}
			return true;
		}

		// Check to see if the class is on the plug-in classpath
		try {
			if (project.hasNature(JavaCore.NATURE_ID)) {
				IJavaProject javaProject = JavaCore.create(project);
				// Look for this activator in the project's classpath
				if (!FreemarkerJavaHelper.isOnClasspath(className, javaProject)) {
					validator
							.addMessage(
									FreemarkerUIPluginMessages.ControlValidationUtility_errorMsgNotOnClasspath,
									IMessageProvider.ERROR);
					return false;
				}
			}
		} catch (CoreException ce) {
			// Ignore
		}

		return true;
	}

	/***
	 * Validate method field provider.
	 * 
	 * @param providerType
	 * @param className
	 * @param methodName
	 * @param validator
	 * @param project
	 * @param required
	 * @return
	 */
	public static boolean validateMethodProviderField(
			ProviderType providerType, String className, String methodName,
			IValidatorMessageHandler validator, IProject project,
			boolean required) {
		
		// Validate Required method
		if (!validateMethodField(className, methodName, validator, project,
				required)) {
			return false;
		}

		// Validate existing method
		try {
			if (project.hasNature(JavaCore.NATURE_ID)) {
				IJavaProject javaProject = JavaCore.create(project);
				IMethod method = FreemarkerJavaHelper.getMethod(className,
						methodName, javaProject);
				if (method == null) {
					validator
							.addMessage(
									FreemarkerUIPluginMessages.ControlValidationUtility_errorMsgMethodNotExist,
									IMessageProvider.ERROR);
					return false;
				} else {
					// Method exists
					// TODO : test return type switch the provider type.					
					return true;
				}
			} else {
				validator
						.addMessage(
								FreemarkerUIPluginMessages.ControlValidationUtility_errorMsgNotOnClasspath,
								IMessageProvider.ERROR);
				return false;
			}
		} catch (CoreException ce) {
			// Ignore
		}

		return true;
	}

	private static boolean validateMethodField(String className,
			String methodName, IValidatorMessageHandler validator,
			IProject project, boolean required) {
		if (StringUtils.isEmpty(methodName)) {
			if (required) {
				validator
						.addMessage(
								FreemarkerUIPluginMessages.ControlValidationUtility_errorMsgMethodRequired,
								IMessageProvider.ERROR);
				return false;
			}
			return true;
		}
		return true;
	}

	/**
	 * Validate name config.
	 * 
	 * @param name
	 * @param projectSettings
	 * @param providerType
	 * @param checkAlreadyExist
	 * @param validator
	 * @return
	 */
	public static boolean validateNameConfigField(String name,
			IFreemarkerProjectSettings projectSettings,
			ProviderType providerType, boolean checkAlreadyExist,
			IValidatorMessageHandler validator) {
		if (StringUtils.isEmpty(name)) {
			validator
					.addMessage(
							FreemarkerUIPluginMessages.ControlValidationUtility_nameConfigRequired,
							IMessageProvider.ERROR);
			return false;

		}
		if (!checkAlreadyExist)
			return true;
		if (projectSettings.isNameConfigExist(name, providerType)) {
			validator
					.addMessage(
							FreemarkerUIPluginMessages.ControlValidationUtility_nameConfigAlreadyExist,
							IMessageProvider.ERROR);
			return false;
		}
		return true;
	}
}
