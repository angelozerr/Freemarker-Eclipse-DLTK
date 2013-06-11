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
package org.eclipse.dltk.freemarker.internal.core.settings;

import static org.eclipse.dltk.freemarker.core.util.XMLUtils.endElement;
import static org.eclipse.dltk.freemarker.core.util.XMLUtils.startElement;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.StringWriter;
import java.io.UnsupportedEncodingException;
import java.io.Writer;
import java.net.URI;

import org.eclipse.core.resources.IContainer;
import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IFolder;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.ListenerList;
import org.eclipse.core.runtime.Status;
import org.eclipse.dltk.freemarker.core.FreemarkerCorePlugin;
import org.eclipse.dltk.freemarker.core.manager.FreemarkerSettingsManager;
import org.eclipse.dltk.freemarker.core.settings.IFreemarkerElementSettings;
import org.eclipse.dltk.freemarker.core.settings.IFreemarkerProjectSettings;
import org.eclipse.dltk.freemarker.core.settings.IFreemarkerSettingsChangedEvent;
import org.eclipse.dltk.freemarker.core.settings.IFreemarkerSettingsChangedEvent.EventType;
import org.eclipse.dltk.freemarker.core.settings.IFreemarkerSettingsChangedListener;
import org.eclipse.dltk.freemarker.core.settings.provider.IObjectInstanceProvider;
import org.eclipse.dltk.freemarker.core.settings.provider.IObjectInstanceProvider.ProviderType;
import org.eclipse.dltk.freemarker.core.util.IOUtils;
import org.eclipse.dltk.freemarker.internal.core.settings.events.FreemarkerSettingsSavedEvent;
import org.xml.sax.Attributes;

public abstract class FreemarkerElementSettings<T extends IResource> implements
		IFreemarkerElementSettings {

	public final static String UTF_8 = "UTF-8";

	public static String PROJECT_METADATA_FILENAME = "___fmproject.xml";

	protected final T resource;

	private ListenerList settingsListeners = new ListenerList();

	private boolean loaded;

	public FreemarkerElementSettings(T resource) {
		this.resource = resource;
	}

	public T getResource() {
		return resource;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.dltk.freemarker.core.settings.IFreemarkerElementSettings#
	 * save()
	 */
	public void save() throws CoreException {
		// get .metatdata/fm' folder
		IFolder baseDir = getProjectSettings().getMetadataBaseDir();

		// Save settings into writer
		StringWriter writer = new StringWriter();
		try {
			save(writer);
			writer.flush();
			writer.close();
		} catch (IOException e) {

		}

		// Get the metdata file (ex : test.ftl, metdata file is
		// .metatdata/fm/test.ftl.xml
		IFile metadataFile = getMetadataFile(baseDir, getResource());
		// Save writer settings into file.
		String content = writer.getBuffer().toString();
		save(metadataFile, content);

		// Reload config
		load(true);

		// Fire saved event
		if (!settingsListeners.isEmpty()) {
			fireSettingsChanged(new FreemarkerSettingsSavedEvent(this,
					getSavedEventType()));
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.dltk.freemarker.core.settings.IFreemarkerElementSettings#
	 * load()
	 */
	public void load() throws CoreException {
		this.load(false);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.dltk.freemarker.core.settings.IFreemarkerElementSettings#
	 * load(boolean)
	 */
	public void load(boolean force) throws CoreException {
		if (!force && loaded) {
			return;
		}

		// Get the metdata file (ex : test.ftl, metdata file is
		// .metatdata/fm/test.ftl.xml
		IFile metadataFile = getMetadataFile();
		if (metadataFile == null)
			return;
		if (!metadataFile.exists()) {
			return;
		}

		URI location = metadataFile.getLocationURI();
		if (location == null) {
			IStatus status = new Status(IStatus.ERROR,
					FreemarkerCorePlugin.PLUGIN_ID,
					"Cannot obtain a location URI for " + metadataFile);
			throw new CoreException(status);
		}

		File file = IOUtils.toLocalFile(location, null);
		if (!file.exists())
			return;

		XMLSettingsLoader<?> loader = createXMLSettingsLoader();
		loader.load(file);
		loaded = true;
	}

	protected IFile getMetadataFile() throws CoreException {
		return getMetadataFile(getProjectSettings().getMetadataBaseDir(),
				resource);
	}

	private IFile getMetadataFile(IFolder baseDir, IResource resource) {
		IPath relativePath = resource.getFullPath().makeRelativeTo(
				getProject().getFullPath());
		if (relativePath.isEmpty()) {
			// It's project resource
			return baseDir.getFile(PROJECT_METADATA_FILENAME);
		}
		IPath metadataFilePath = relativePath.addFileExtension("xml");
		return baseDir.getFile(metadataFilePath);
	}

	private void save(Writer writer) throws IOException {
		String rootElementName = getRootElementName();
		startElement(rootElementName, true, writer);
		saveCustomXMLContent(writer);
		endElement(rootElementName, writer);
	}

	protected void saveCustomXMLContent(Writer writer) throws IOException {

	}

	protected void saveProvider(IObjectInstanceProvider instanceProvider,
			Writer writer) throws IOException {
		if (instanceProvider == null)
			return;
		startElement(PROVIDER_ELT, writer);
		saveProviderBody(instanceProvider, writer);
		endElement(writer);
	}

	protected abstract void saveProviderBody(
			IObjectInstanceProvider instanceProvider, Writer writer)
			throws IOException;

	protected void saveCustomXMLAttrProvider(
			IObjectInstanceProvider instanceProvider, Writer writer)
			throws IOException {

	}

	public IProject getProject() {
		return resource.getProject();
	}

	public IFreemarkerProjectSettings getProjectSettings() throws CoreException {
		return FreemarkerSettingsManager.getManager().getProjectSettings(
				getProject());
	}

	private void save(IFile metadataFile, String content) throws CoreException {
		byte[] bytes = null;
		try {
			bytes = content.getBytes(UTF_8); // .classpath
			// always
			// encoded
			// with
			// UTF-8
		} catch (UnsupportedEncodingException e) {
			//Util.log(e, "Could not write .classpath with UTF-8 encoding "); //$NON-NLS-1$
			// fallback to default
			bytes = content.getBytes();
		}
		InputStream inputStream = new ByteArrayInputStream(bytes);
		// update the resource content
		if (metadataFile.exists()) {
			if (metadataFile.isReadOnly()) {
				// provide opportunity to checkout read-only .classpath file
				ResourcesPlugin.getWorkspace().validateEdit(
						new IFile[] { metadataFile }, null);
			}
			metadataFile.setContents(inputStream, IResource.FORCE, null);
		} else {
			// Create base dir folder of the template settings if needed.
			IContainer container = metadataFile.getParent();
			if (container.getType() == IContainer.FOLDER) {
				IFolder folder = (IFolder) container;
				IOUtils.createFolder(folder, null);
			}

			metadataFile.create(inputStream, IResource.FORCE, null);
		}
	}

	public void addFreemarkerSettingsChangedListener(
			IFreemarkerSettingsChangedListener listener) {
		settingsListeners.add(listener);
	}

	public void removeFreemarkerSettingsChangedListener(
			IFreemarkerSettingsChangedListener listener) {
		settingsListeners.remove(listener);
	}

	/**
	 * Fire the settings event.
	 * 
	 * @param event
	 */
	protected void fireSettingsChanged(IFreemarkerSettingsChangedEvent event) {

		// fire the event
		Object[] listeners = settingsListeners.getListeners();
		for (int i = 0; i < listeners.length; ++i) {
			((IFreemarkerSettingsChangedListener) listeners[i])
					.settingsChanged(event);
		}
	}

	protected abstract String getRootElementName();

	public abstract IObjectInstanceProvider createProvider(
			ProviderType providerType, Attributes att);

	protected abstract XMLSettingsLoader<?> createXMLSettingsLoader();

	protected EventType getSavedEventType() {
		switch (getScope()) {
		case PROJECT:
			return EventType.PROJECT_SETTINGS_SAVED;
		case TEMPLATE:
			return EventType.TEMPLATE_SETTINGS_SAVED;
		}
		return null;
	}
}
