package org.eclipse.freemarker.internal.core.documentModel.parser;

public interface FMRegionContext {

	public static final String FM_TAG_OPEN = "FM_TAG_OPEN";
	public static final String FM_TAG_CLOSE = "FM_TAG_CLOSE";
	public static final String FM_TAG_NAME  ="FM_TAG_NAME";
	
	public static final String FM_DIRECTIVE_TEXT = "FM_DIRECTIVE_TEXT";
	
	public static final String FM_DIRECTIVE_START = "FM_DIRECTIVE_START";
	
	
	//public static final String PHP_OPEN = "PHP_OPEN"; //$NON-NLS-1$
	public static final String PHP_CLOSE = "PHP_CLOSE"; //$NON-NLS-1$
	public static final String PHP_CONTENT = "PHP_CONTENT"; //$NON-NLS-1$
	public static final String PHP_ASP_CONTENT = "PHP_ASP_CONTENT"; //$NON-NLS-1$

	public static final String PHP_SCRIPTLET_OPEN = "PHP_SCRIPTLET_OPEN"; //$NON-NLS-1$
	public static final String XML_TAG_ATTRIBUTE_VALUE_DQUOTE = "XML_TAG_ATTRIBUTE_VALUE_DQUOTE"; //$NON-NLS-1$
	public static final String XML_TAG_ATTRIBUTE_VALUE_SQUOTE = "XML_TAG_ATTRIBUTE_VALUE_SQUOTE"; //$NON-NLS-1$

	
}
