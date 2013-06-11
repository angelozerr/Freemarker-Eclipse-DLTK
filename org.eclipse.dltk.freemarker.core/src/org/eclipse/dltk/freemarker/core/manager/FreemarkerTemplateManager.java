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
package org.eclipse.dltk.freemarker.core.manager;

import java.io.File;
import java.io.IOException;
import java.io.Reader;
import java.io.StringWriter;
import java.io.Writer;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IResource;

import freemarker.core.Configurable;
import freemarker.template.Configuration;
import freemarker.template.ObjectWrapper;
import freemarker.template.SimpleHash;
import freemarker.template.Template;
import freemarker.template.TemplateException;
import freemarker.template.TemplateExceptionHandler;
import freemarker.template.TemplateHashModel;
import freemarker.template.TemplateHashModelEx;
import freemarker.template.TemplateModelException;

/**
 * Freemarker Template Manager used to merge data model with Freemarker
 * template.
 * 
 */
public class FreemarkerTemplateManager {

	private static final FreemarkerTemplateManager INSTANCE = new FreemarkerTemplateManager();

	public static FreemarkerTemplateManager getManager() {
		return INSTANCE;
	}

	/**
	 * Merge data-model with template and returns result as String.
	 * 
	 * @param name
	 * @param reader
	 * @param cfg
	 * @param model
	 * @return
	 * @throws IOException
	 * @throws TemplateException
	 */
	public String process(String name, Reader reader, Configuration cfg,
			Object model) throws IOException, TemplateException {
		StringWriter writer = new StringWriter();
		process(name, reader, cfg, model, writer, null);
		return writer.getBuffer().toString();
	}

	/**
	 * Merge data-model with template and write the result into the writer.
	 * 
	 * @param name
	 * @param reader
	 * @param cfg
	 * @param model
	 * @param writer
	 * @param templateExceptionHandler
	 * @throws IOException
	 * @throws TemplateException
	 */
	public Template process(String name, Reader reader, Configuration cfg,
			Object model, Writer writer,
			TemplateExceptionHandler templateExceptionHandler)
			throws IOException, TemplateException {
		Template template = null;
		try {
			
			/* Create Freemarker template */
			template = new Template(name, reader, cfg);
			if (templateExceptionHandler != null) {
				// Set template handler to Configuration to avoid display errors
				// into the writer.
				cfg.setTemplateExceptionHandler(templateExceptionHandler);
			}

			/* Merge data-model with template */
			if (model != null) {
				// Model is defined, merge the template with data model.
				template.process(model, writer == null ? NullWriter.NULL_WRITER
						: writer);
			}
			if (writer != null) {
				writer.flush();
				writer.close();
			}

		} finally {
			reader.close();
		}
		return template;
	}

	/**
	 * Return Freemarker configuration with setDirectoryForTemplateLoading
	 * intialized with the base dir of the {@link IFile} templateFile.
	 * 
	 * @param templateFile
	 * @return
	 * @throws IOException
	 */
	public Configuration getConfiguration(IFile templateFile)
			throws IOException {
		return getConfiguration(templateFile.getLocation().toFile());
	}

	/**
	 * Return Freemarker configuration with
	 * {@link Configuration#setDirectoryForTemplateLoading(File)} initialized
	 * with the base dir of the {@link File} templateFile.
	 * 
	 * @param templateFile
	 * @return
	 * @throws IOException
	 */
	public Configuration getConfiguration(File templateFile) throws IOException {

		Configuration configuration = new Configuration();
		prepareConfiguration(configuration, templateFile);
		return configuration;
	}

	/**
	 * Initialize {@link Configuration#setDirectoryForTemplateLoading(File)}
	 * with the base dir of the {@link File} templateFile.
	 * 
	 * @param configuration
	 * @param templateFile
	 * @throws IOException
	 */
	public void prepareConfiguration(Configuration configuration,
			IResource templateFile) throws IOException {
		File baseDir = templateFile.getLocation().toFile();
		prepareConfiguration(configuration, baseDir);
	}

	/**
	 * Initialize {@link Configuration#setDirectoryForTemplateLoading(File)}
	 * with the base dir of the {@link File} templateFile.
	 * 
	 * @param configuration
	 * @param templateFile
	 * @throws IOException
	 */
	public void prepareConfiguration(Configuration configuration,
			File templateFile) throws IOException {
		File baseDir = templateFile.getParentFile();
		// Set the directory base dir for Freemarker template loading with
		// the directory which contains this file to manage <#import '...'>.
		configuration.setDirectoryForTemplateLoading(baseDir);
	}

	/**
	 * Get {@link TemplateHashModelEx} from the rootMap.
	 * 
	 * @param rootMap
	 * @param configurable
	 * @param wrapper
	 * @return
	 * @throws TemplateModelException
	 */
	public TemplateHashModelEx getTemplateHashModelEx(Object rootMap,
			Configurable configurable) throws TemplateModelException {
		return getTemplateHashModelEx(rootMap, configurable, null);
	}

	/**
	 * Get {@link TemplateHashModelEx} from the rootMap.
	 * 
	 * @param rootMap
	 * @param configurable
	 * @param wrapper
	 * @return
	 * @throws TemplateModelException
	 */
	public TemplateHashModelEx getTemplateHashModelEx(Object rootMap,
			Configurable configurable, ObjectWrapper wrapper)
			throws TemplateModelException {
		TemplateHashModel templateHashModel = getTemplateHashModel(rootMap,
				configurable, wrapper);
		if (templateHashModel instanceof TemplateHashModelEx) {
			return (TemplateHashModelEx) templateHashModel;
		}

		throw new IllegalArgumentException(configurable.getClass().getName()
				+ " could not convert "
				+ (rootMap == null ? "null" : rootMap.getClass().getName())
				+ " to a TemplateHashModelEx.");

	}

	/**
	 * Get {@link TemplateHashModel} from the rootMap.
	 * 
	 * @param rootMap
	 * @param configurable
	 * @param wrapper
	 * @return
	 * @throws TemplateModelException
	 */
	public TemplateHashModel getTemplateHashModel(Object rootMap,
			Configurable configurable) throws TemplateModelException {
		return getTemplateHashModel(rootMap, configurable, null);
	}

	/**
	 * Get {@link TemplateHashModel} from the rootMap.
	 * 
	 * @param rootMap
	 * @param configurable
	 * @param wrapper
	 * @return
	 * @throws TemplateModelException
	 */
	public TemplateHashModel getTemplateHashModel(Object rootMap,
			Configurable configurable, ObjectWrapper wrapper)
			throws TemplateModelException {
		// FIXME : The code of this method is the same than {@link
		// Template#createProcessingEnvironment}.
		// It should be better Freemarker API will have Class utility to manage
		// that.
		TemplateHashModel root = null;
		if (rootMap instanceof TemplateHashModel) {
			root = (TemplateHashModel) rootMap;
		} else {
			if (wrapper == null) {
				wrapper = configurable.getObjectWrapper();
			}

			try {
				root = rootMap != null ? (TemplateHashModel) wrapper
						.wrap(rootMap) : new SimpleHash(wrapper);
				if (root == null) {
					throw new IllegalArgumentException(wrapper.getClass()
							.getName()
							+ " converted "
							+ (rootMap == null ? "null" : rootMap.getClass()
									.getName()) + " to null.");
				}
			} catch (ClassCastException e) {
				throw new IllegalArgumentException(wrapper.getClass().getName()
						+ " could not convert "
						+ (rootMap == null ? "null" : rootMap.getClass()
								.getName()) + " to a TemplateHashModel.");
			}
		}
		return root;
	}
}
