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

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;

import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.dltk.freemarker.core.FreemarkerCorePlugin;
import org.eclipse.dltk.freemarker.core.settings.IFreemarkerSettingsConstants;
import org.eclipse.dltk.freemarker.core.settings.provider.IObjectInstanceProvider;
import org.eclipse.dltk.freemarker.core.settings.provider.IObjectInstanceProvider.ProviderType;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

/**
 * Base XML settings loader.
 * 
 * @param <T>
 */
@SuppressWarnings("unchecked")
public abstract class XMLSettingsLoader<T extends FreemarkerElementSettings>
		extends DefaultHandler implements IFreemarkerSettingsConstants {

	protected final T settings;

	public XMLSettingsLoader(T settings) {
		this.settings = settings;
	}

	@Override
	public void startElement(String uri, String localName, String qName,
			Attributes atts) throws SAXException {
		if (PROVIDER_ELT.equals(qName)) {
			loadProvider(atts);
		}
	}

	/**
	 * Load provider.
	 * 
	 * @param atts
	 */
	protected void loadProvider(Attributes atts) {
		String type = atts.getValue(TYPE_ATTR);
		ProviderType providerType = ProviderType.getProviderType(type);
		IObjectInstanceProvider provider = settings
				.createProvider(providerType, atts);
		if (provider != null) {
			loadProvider(provider, atts);
		}
	}

	/**
	 * Load provider.
	 * 
	 * @param provider
	 * @param atts
	 */
	protected void loadProvider(IObjectInstanceProvider provider,
			Attributes atts) {
		provider.setClassName(atts.getValue(CLASS_ATTR));
		provider.setMethodName(atts.getValue(METHOD_ATTR));
	}

	/**
	 * Load XML config from file.
	 * 
	 * @param metadataFile
	 * @throws CoreException
	 */
	public void load(File metadataFile) throws CoreException {
		InputStream stream = null;
		try {
			stream = new FileInputStream(metadataFile);
			load(stream);
		} catch (FileNotFoundException e) {
			IStatus status = new Status(IStatus.ERROR,
					FreemarkerCorePlugin.PLUGIN_ID, e.getMessage(), e);
			throw new CoreException(status);
		} finally {
			if (stream != null) {
				try {
					stream.close();
				} catch (IOException e) {

				}
			}
		}
	}

	/**
	 * Load XML config from stream.
	 * 
	 * @param stream
	 */
	public void load(InputStream stream) {
		SAXParserFactory factory = SAXParserFactory.newInstance();
		try {
			SAXParser saxParser = factory.newSAXParser();
			saxParser.parse(stream, this);
		} catch (Throwable t) {
			FreemarkerCorePlugin.log(t);
		}
	}

}
