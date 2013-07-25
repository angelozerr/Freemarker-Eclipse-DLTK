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
package freemarker.provisionnal.template;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Properties;
import java.util.Set;
import java.util.TimeZone;

import freemarker.cache.CacheStorage;
import freemarker.cache.TemplateLoader;
import freemarker.core.ArithmeticEngine;
import freemarker.core.TemplateClassResolver;
import freemarker.provisionnal.debug.impl.DebuggerService;
import freemarker.template.Configuration;
import freemarker.template.ObjectWrapper;
import freemarker.template.Template;
import freemarker.template.TemplateException;
import freemarker.template.TemplateExceptionHandler;
import freemarker.template.TemplateHashModelEx;
import freemarker.template.TemplateModel;
import freemarker.template.TemplateModelException;
import freemarker.template.Version;

/**
 * Debuggable configuration wrapper which register the template into a debugger
 * service when it is created.
 * 
 */
public class DebuggableConfigurationWrapper extends Configuration {

	private final Configuration configuration;
	private final DebuggerService debuggerService;
	private String booleanFormat;

	public DebuggableConfigurationWrapper(Configuration configuration,
			DebuggerService debuggerService) {
		this.configuration = configuration;
		this.debuggerService = debuggerService;
		if (booleanFormat != null) {
			configuration.setBooleanFormat(booleanFormat);
		}
	}

	@Override
	public Template getTemplate(String name, Locale locale, String encoding,
			boolean parseAsFTL) throws IOException {
		Template template = configuration.getTemplate(name, locale, encoding,
				parseAsFTL);
		if (template != null) {
			// Template is created (ex : with <#import 'test.ftl' > directive),
			// register it to the debugger service.
			debuggerService.registerTemplate(template);
		}
		return template;
	}

	public void addAutoImport(String namespaceVarName, String templateName) {
		configuration.addAutoImport(namespaceVarName, templateName);
	}

	public void addAutoInclude(String templateName) {
		configuration.addAutoInclude(templateName);
	}

	public void clearEncodingMap() {
		configuration.clearEncodingMap();
	}

	public void clearSharedVariables() {
		configuration.clearSharedVariables();
	}

	public void clearTemplateCache() {
		configuration.clearTemplateCache();
	}

	public Object clone() {
		return configuration.clone();
	}

	public boolean equals(Object arg0) {
		return configuration.equals(arg0);
	}

	public ArithmeticEngine getArithmeticEngine() {
		return configuration.getArithmeticEngine();
	}

	public boolean getAutoFlush() {
		return configuration.getAutoFlush();
	}

	public String getBooleanFormat() {
		return configuration.getBooleanFormat();
	}

	public CacheStorage getCacheStorage() {
		return configuration.getCacheStorage();
	}

	public int getClassicCompatibleAsInt() {
		return configuration.getClassicCompatibleAsInt();
	}

	public Object getCustomAttribute(String name) {
		return configuration.getCustomAttribute(name);
	}

	public String[] getCustomAttributeNames() {
		return configuration.getCustomAttributeNames();
	}

	public String getDateFormat() {
		return configuration.getDateFormat();
	}

	public String getDateTimeFormat() {
		return configuration.getDateTimeFormat();
	}

	public String getDefaultEncoding() {
		return configuration.getDefaultEncoding();
	}

	public String getEncoding(Locale arg0) {
		return configuration.getEncoding(arg0);
	}

	public String getIncompatibleEnhancements() {
		return configuration.getIncompatibleEnhancements();
	}

	public Version getIncompatibleImprovements() {
		return configuration.getIncompatibleImprovements();
	}

	public Locale getLocale() {
		return configuration.getLocale();
	}

	public boolean getLocalizedLookup() {
		return configuration.getLocalizedLookup();
	}

	public TemplateClassResolver getNewBuiltinClassResolver() {
		return configuration.getNewBuiltinClassResolver();
	}

	public String getNumberFormat() {
		return configuration.getNumberFormat();
	}

	public ObjectWrapper getObjectWrapper() {
		return configuration.getObjectWrapper();
	}

	public String getOutputEncoding() {
		return configuration.getOutputEncoding();
	}

	public int getParsedIncompatibleEnhancements() {
		return configuration.getParsedIncompatibleEnhancements();
	}

	public String getSetting(String key) {
		return configuration.getSetting(key);
	}

	public Map getSettings() {
		return configuration.getSettings();
	}

	public TemplateModel getSharedVariable(String name) {
		return configuration.getSharedVariable(name);
	}

	public Set getSharedVariableNames() {
		return configuration.getSharedVariableNames();
	}

	public boolean getStrictSyntaxMode() {
		return configuration.getStrictSyntaxMode();
	}

	public Set getSupportedBuiltInNames() {
		return configuration.getSupportedBuiltInNames();
	}

	public int getTagSyntax() {
		return configuration.getTagSyntax();
	}

	public Template getTemplate(String name, Locale locale, String encoding)
			throws IOException {
		return configuration.getTemplate(name, locale, encoding);
	}

	public Template getTemplate(String name, Locale locale) throws IOException {
		return configuration.getTemplate(name, locale);
	}

	public Template getTemplate(String name, String encoding)
			throws IOException {
		return configuration.getTemplate(name, encoding);
	}

	public Template getTemplate(String name) throws IOException {
		return configuration.getTemplate(name);
	}

	public TemplateExceptionHandler getTemplateExceptionHandler() {
		return configuration.getTemplateExceptionHandler();
	}

	public TemplateLoader getTemplateLoader() {
		return configuration.getTemplateLoader();
	}

	public String getTimeFormat() {
		return configuration.getTimeFormat();
	}

	public TimeZone getTimeZone() {
		return configuration.getTimeZone();
	}

	public String getURLEscapingCharset() {
		return configuration.getURLEscapingCharset();
	}

	public boolean getWhitespaceStripping() {
		return configuration.getWhitespaceStripping();
	}

	public int hashCode() {
		return configuration.hashCode();
	}

	public boolean isClassicCompatible() {
		return configuration.isClassicCompatible();
	}

	public void loadBuiltInEncodingMap() {
		configuration.loadBuiltInEncodingMap();
	}

	public void removeAutoImport(String namespaceVarName) {
		configuration.removeAutoImport(namespaceVarName);
	}

	public void removeAutoInclude(String templateName) {
		configuration.removeAutoInclude(templateName);
	}

	public void removeCustomAttribute(String name) {
		configuration.removeCustomAttribute(name);
	}

	public void removeTemplateFromCache(String name, Locale locale,
			String encoding, boolean parse) throws IOException {
		configuration.removeTemplateFromCache(name, locale, encoding, parse);
	}

	public void removeTemplateFromCache(String name, Locale locale,
			String encoding) throws IOException {
		configuration.removeTemplateFromCache(name, locale, encoding);
	}

	public void removeTemplateFromCache(String name, Locale locale)
			throws IOException {
		configuration.removeTemplateFromCache(name, locale);
	}

	public void removeTemplateFromCache(String name, String encoding)
			throws IOException {
		configuration.removeTemplateFromCache(name, encoding);
	}

	public void removeTemplateFromCache(String name) throws IOException {
		configuration.removeTemplateFromCache(name);
	}

	public void setAllSharedVariables(TemplateHashModelEx hash)
			throws TemplateModelException {
		configuration.setAllSharedVariables(hash);
	}

	public void setArithmeticEngine(ArithmeticEngine arithmeticEngine) {
		configuration.setArithmeticEngine(arithmeticEngine);
	}

	public void setAutoFlush(boolean autoFlush) {
		configuration.setAutoFlush(autoFlush);
	}

	public void setAutoImports(Map map) {
		configuration.setAutoImports(map);
	}

	public void setAutoIncludes(List arg0) {
		configuration.setAutoIncludes(arg0);
	}

	public void setBooleanFormat(String booleanFormat) {
		if (configuration == null) {
			this.booleanFormat = booleanFormat;
		} else {
			configuration.setBooleanFormat(booleanFormat);
		}
	}

	public void setCacheStorage(CacheStorage storage) {
		configuration.setCacheStorage(storage);
	}

	public void setClassForTemplateLoading(Class clazz, String pathPrefix) {
		configuration.setClassForTemplateLoading(clazz, pathPrefix);
	}

	public void setClassicCompatible(boolean classicCompatibility) {
		configuration.setClassicCompatible(classicCompatibility);
	}

	public void setClassicCompatibleAsInt(int classicCompatibility) {
		configuration.setClassicCompatibleAsInt(classicCompatibility);
	}

	public void setCustomAttribute(String name, Object value) {
		configuration.setCustomAttribute(name, value);
	}

	public void setDateFormat(String dateFormat) {
		configuration.setDateFormat(dateFormat);
	}

	public void setDateTimeFormat(String dateTimeFormat) {
		configuration.setDateTimeFormat(dateTimeFormat);
	}

	public void setDefaultEncoding(String encoding) {
		configuration.setDefaultEncoding(encoding);
	}

	public void setDirectoryForTemplateLoading(File arg0) throws IOException {
		configuration.setDirectoryForTemplateLoading(arg0);
	}

	public void setEncoding(Locale locale, String encoding) {
		configuration.setEncoding(locale, encoding);
	}

	public void setIncompatibleEnhancements(String version) {
		configuration.setIncompatibleEnhancements(version);
	}

	public void setIncompatibleImprovements(Version version) {
		configuration.setIncompatibleImprovements(version);
	}

	public void setLocale(Locale locale) {
		configuration.setLocale(locale);
	}

	public void setLocalizedLookup(boolean localizedLookup) {
		configuration.setLocalizedLookup(localizedLookup);
	}

	public void setNewBuiltinClassResolver(
			TemplateClassResolver newBuiltinClassResolver) {
		configuration.setNewBuiltinClassResolver(newBuiltinClassResolver);
	}

	public void setNumberFormat(String numberFormat) {
		configuration.setNumberFormat(numberFormat);
	}

	public void setObjectWrapper(ObjectWrapper objectWrapper) {
		configuration.setObjectWrapper(objectWrapper);
	}

	public void setOutputEncoding(String outputEncoding) {
		configuration.setOutputEncoding(outputEncoding);
	}

	public void setServletContextForTemplateLoading(Object arg0, String arg1) {
		configuration.setServletContextForTemplateLoading(arg0, arg1);
	}

	public void setSetting(String arg0, String arg1) throws TemplateException {
		configuration.setSetting(arg0, arg1);
	}

	public void setSettings(InputStream propsIn) throws TemplateException,
			IOException {
		configuration.setSettings(propsIn);
	}

	public void setSettings(Properties arg0) throws TemplateException {
		configuration.setSettings(arg0);
	}

	public void setSharedVariable(String name, Object obj)
			throws TemplateModelException {
		configuration.setSharedVariable(name, obj);
	}

	public void setSharedVariable(String name, TemplateModel tm) {
		configuration.setSharedVariable(name, tm);
	}

	public void setStrictBeanModels(boolean strict) {
		configuration.setStrictBeanModels(strict);
	}

	public void setStrictSyntaxMode(boolean b) {
		configuration.setStrictSyntaxMode(b);
	}

	public void setTagSyntax(int tagSyntax) {
		configuration.setTagSyntax(tagSyntax);
	}

	public void setTemplateExceptionHandler(
			TemplateExceptionHandler templateExceptionHandler) {
		configuration.setTemplateExceptionHandler(templateExceptionHandler);
	}

	public void setTemplateLoader(TemplateLoader loader) {
		configuration.setTemplateLoader(loader);
	}

	public void setTemplateUpdateDelay(int seconds) {
		configuration.setTemplateUpdateDelay(seconds);
	}

	public void setTimeFormat(String timeFormat) {
		configuration.setTimeFormat(timeFormat);
	}

	public void setTimeZone(TimeZone timeZone) {
		configuration.setTimeZone(timeZone);
	}

	public void setURLEscapingCharset(String urlEscapingCharset) {
		configuration.setURLEscapingCharset(urlEscapingCharset);
	}

	public void setWhitespaceStripping(boolean b) {
		configuration.setWhitespaceStripping(b);
	}

	public String toString() {
		return configuration.toString();
	}

}
