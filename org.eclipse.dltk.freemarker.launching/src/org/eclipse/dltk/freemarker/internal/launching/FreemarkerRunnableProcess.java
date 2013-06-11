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
package org.eclipse.dltk.freemarker.internal.launching;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.Reader;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.debug.core.ILaunch;
import org.eclipse.dltk.freemarker.core.manager.FreemarkerSettingsManager;
import org.eclipse.dltk.freemarker.core.manager.FreemarkerTemplateManager;
import org.eclipse.dltk.freemarker.core.settings.IFreemarkerTemplateSettings;
import org.eclipse.dltk.freemarker.core.settings.provider.InstanceProviderException;
import org.eclipse.dltk.freemarker.core.util.SettingsUtils;
import org.eclipse.dltk.launching.DLTKRunnableProcess;
import org.eclipse.dltk.launching.IInterpreterInstall;
import org.eclipse.dltk.launching.InterpreterConfig;

import freemarker.template.Configuration;
import freemarker.template.TemplateException;

/**
 * 
 * Freemarker runnable process implementation. The
 * {@link DLTKRunnableProcess#run()} method, execute merge Model with Template
 * (written into Template page from the Freemarker Editor) and display result of
 * the merge into Eclipse console.
 * 
 */
public class FreemarkerRunnableProcess extends DLTKRunnableProcess {

	public FreemarkerRunnableProcess(IInterpreterInstall install,
			ILaunch launch, InterpreterConfig config) {
		super(install, launch, config);
	}

	public void run() {

		// Get template reader from the file
		IFile resourcesFile = super.getResourcesFile();
		File templateFile = super.getFile();
		Reader templateReader;
		try {
			templateReader = new FileReader(templateFile);

			String templateName = templateFile.getAbsolutePath();

			// Get Freemarker configuration
			Configuration cfg = getConfiguration(resourcesFile);

			// Prepare Model
			Object model = getModel(resourcesFile);

			// Merge data-model with template
			String result = FreemarkerTemplateManager.getManager().process(
					templateName, templateReader, cfg, model);
			super.out(result);

		} catch (FileNotFoundException e) {
			super.err(e);
		} catch (IOException e) {
			super.err(e);
		} catch (TemplateException e) {
			super.err(e);
		} catch (InstanceProviderException e) {
			super.err(e);
		}

	};

	private Configuration getConfiguration(IFile templateFile)
			throws InstanceProviderException {
		return SettingsUtils.getConfiguration(templateFile);
	}

	public Object getModel(IFile templateFile) throws InstanceProviderException {
		return SettingsUtils.getModel(templateFile);
	}

	public IFreemarkerTemplateSettings getFreemarkerTemplate(IFile templateFile)
			throws CoreException {
		return FreemarkerSettingsManager.getManager().getTemplateSettings(templateFile);
	}
}
