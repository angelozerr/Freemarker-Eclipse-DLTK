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
import java.util.Collection;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Properties;
import java.util.Set;
import java.util.TimeZone;


import freemarker.cache.CacheStorage;
import freemarker.cache.TemplateCache;
import freemarker.cache.TemplateLoader;
import freemarker.core.Configurable;
import freemarker.core.Environment;
import freemarker.core.Scope;
import freemarker.core.ast.ASTVisitor;
import freemarker.core.ast.ArithmeticEngine;
import freemarker.provisionnal.debug.impl.DebuggerService;
import freemarker.template.Configuration;
import freemarker.template.ObjectWrapper;
import freemarker.template.Template;
import freemarker.template.TemplateCollectionModel;
import freemarker.template.TemplateException;
import freemarker.template.TemplateExceptionHandler;
import freemarker.template.TemplateHashModelEx;
import freemarker.template.TemplateModel;
import freemarker.template.TemplateModelException;

/**
 * Debuggable configuration wrapper which register the template into a debugger
 * service when it is created.
 * 
 */
public class DebuggableConfigurationWrapper extends Configuration {

	private final Configuration configuration;
	private final DebuggerService debuggerService;

	public DebuggableConfigurationWrapper(Configuration configuration,
			DebuggerService debuggerService) {
		this.configuration = configuration;
		this.debuggerService = debuggerService;
	}

	public void addAutoImport(String namespace, String template) {
		configuration.addAutoImport(namespace, template);
	}

	public void addAutoInclude(String templateName) {
		configuration.addAutoInclude(templateName);
	}

	public void addAutoTemplateVisitors(ASTVisitor... visitors) {
		configuration.addAutoTemplateVisitors(visitors);
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

	public boolean definesVariable(String name) {
		return configuration.definesVariable(name);
	}

	public boolean equals(Object arg0) {
		return configuration.equals(arg0);
	}

	public TemplateModel get(String name) {
		return configuration.get(name);
	}

	public ArithmeticEngine getArithmeticEngine() {
		return configuration.getArithmeticEngine();
	}

	public String getBooleanFormat() {
		return configuration.getBooleanFormat();
	}

	public String getBooleanFormat(boolean value) {
		return configuration.getBooleanFormat(value);
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

	public Collection<String> getDirectVariableNames() {
		return configuration.getDirectVariableNames();
	}

	public Scope getEnclosingScope() {
		return configuration.getEnclosingScope();
	}

	public String getEncoding(Locale loc) {
		return configuration.getEncoding(loc);
	}

	public Environment getEnvironment() {
		return configuration.getEnvironment();
	}

	public Locale getLocale() {
		return configuration.getLocale();
	}

	public boolean getLocalizedLookup() {
		return configuration.getLocalizedLookup();
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

	public String getSetting(String key) {
		return configuration.getSetting(key);
	}

	public Map getSettings() {
		return configuration.getSettings();
	}

	public TemplateModel getSharedVariable(String name) {
		return configuration.getSharedVariable(name);
	}

	public Set<String> getSharedVariableNames() {
		return configuration.getSharedVariableNames();
	}

	public boolean getStrictVariableDefinition() {
		return configuration.getStrictVariableDefinition();
	}

	public int getTagSyntax() {
		return configuration.getTagSyntax();
	}

	public Template getTemplate() {
		return configuration.getTemplate();
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

	@Override
	public Template getTemplate(String name, Locale locale, String encoding,
			boolean parse) throws IOException {
		Template template = configuration.getTemplate(name, locale, encoding,
				parse);
		if (template != null) {
			// Template is created (ex : with <#import 'test.ftl' > directive),
			// register it to the debugger service.
			debuggerService.registerTemplate(template);
		}
		return template;
	}

	public TemplateCache getTemplateCache() {
		return configuration.getTemplateCache();
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

	public boolean isEmpty() {
		return configuration.isEmpty();
	}

	public boolean isSecure() {
		return configuration.isSecure();
	}

	public TemplateCollectionModel keys() {
		return configuration.keys();
	}

	public void loadBuiltInEncodingMap() {
		configuration.loadBuiltInEncodingMap();
	}

	public void put(String varname, TemplateModel value) {
		configuration.put(varname, value);
	}

	public TemplateModel remove(String varname) {
		return configuration.remove(varname);
	}

	public void removeAutoImport(String namespace) {
		configuration.removeAutoImport(namespace);
	}

	public void removeAutoInclude(String templateName) {
		configuration.removeAutoInclude(templateName);
	}

	public void removeAutoTemplateVisitors(ASTVisitor... visitors) {
		configuration.removeAutoTemplateVisitors(visitors);
	}

	public void removeCustomAttribute(String name) {
		configuration.removeCustomAttribute(name);
	}

	public TemplateModel resolveVariable(String name) {
		return configuration.resolveVariable(name);
	}

	public void setAllSharedVariables(TemplateHashModelEx hash)
			throws TemplateModelException {
		configuration.setAllSharedVariables(hash);
	}

	public void setArithmeticEngine(ArithmeticEngine arithmeticEngine) {
		configuration.setArithmeticEngine(arithmeticEngine);
	}

	public void setAutoImports(Map<String, String> map) {
		configuration.setAutoImports(map);
	}

	public void setAutoIncludes(List<String> templateNames) {
		configuration.setAutoIncludes(templateNames);
	}

	public void setAutoTemplateVisitors(ASTVisitor... visitors) {
		configuration.setAutoTemplateVisitors(visitors);
	}

	public void setBooleanFormat(String booleanFormat) {
		configuration.setBooleanFormat(booleanFormat);
	}

	public void setCacheStorage(CacheStorage storage) {
		configuration.setCacheStorage(storage);
	}

	public void setClassForTemplateLoading(Class clazz, String pathPrefix) {
		configuration.setClassForTemplateLoading(clazz, pathPrefix);
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

	public void setDirectoryForTemplateLoading(File dir) throws IOException {
		configuration.setDirectoryForTemplateLoading(dir);
	}

	public void setEncoding(Locale locale, String encoding) {
		configuration.setEncoding(locale, encoding);
	}

	public void setLocale(Locale locale) {
		configuration.setLocale(locale);
	}

	public void setLocalizedLookup(boolean localizedLookup) {
		configuration.setLocalizedLookup(localizedLookup);
	}

	public void setNumberFormat(String numberFormat) {
		configuration.setNumberFormat(numberFormat);
	}

	public void setNumbersForComputers(boolean b) {
		configuration.setNumbersForComputers(b);
	}

	public void setObjectWrapper(ObjectWrapper objectWrapper) {
		configuration.setObjectWrapper(objectWrapper);
	}

	public void setOutputEncoding(String outputEncoding) {
		configuration.setOutputEncoding(outputEncoding);
	}

	public void setParent(Configurable parent) {
		configuration.setParent(parent);
	}

	public void setSecure(boolean secure) {
		configuration.setSecure(secure);
	}

	public void setServletContextForTemplateLoading(Object sctxt, String path) {
		configuration.setServletContextForTemplateLoading(sctxt, path);
	}

	public void setSetting(String key, String value) throws TemplateException {
		configuration.setSetting(key, value);
	}

	public void setSettings(InputStream propsIn) throws TemplateException,
			IOException {
		configuration.setSettings(propsIn);
	}

	public void setSettings(Properties props) throws TemplateException {
		configuration.setSettings(props);
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

	public void setStrictVariableDefinition(boolean b) {
		configuration.setStrictVariableDefinition(b);
	}

	public void setTagSyntax(int tagSyntax) {
		configuration.setTagSyntax(tagSyntax);
	}

	public void setTemplateCache(TemplateCache cache) {
		configuration.setTemplateCache(cache);
	}

	public void setTemplateExceptionHandler(
			TemplateExceptionHandler templateExceptionHandler) {
		configuration.setTemplateExceptionHandler(templateExceptionHandler);
	}

	public void setTemplateLoader(TemplateLoader loader) {
		configuration.setTemplateLoader(loader);
	}

	public void setTemplateUpdateDelay(int delay) {
		configuration.setTemplateUpdateDelay(delay);
	}

	public void setTimeFormat(String timeFormat) {
		configuration.setTimeFormat(timeFormat);
	}

	public void setTimeZone(TimeZone timeZone) {
		configuration.setTimeZone(timeZone);
	}

	public void setTolerateParsingProblems(boolean tolerateParsingProblems) {
		configuration.setTolerateParsingProblems(tolerateParsingProblems);
	}

	public void setURLEscapingCharset(String urlEscapingCharset) {
		configuration.setURLEscapingCharset(urlEscapingCharset);
	}

	public void setWhitespaceStripping(boolean b) {
		configuration.setWhitespaceStripping(b);
	}

	public int size() {
		return configuration.size();
	}

	public String toString() {
		return configuration.toString();
	}

	public TemplateCollectionModel values() {
		return configuration.values();
	}
}
