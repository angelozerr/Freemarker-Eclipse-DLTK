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
package org.eclipse.dltk.freemarker.internal.ui;

import org.eclipse.osgi.util.NLS;

/**
 * Messages used into Freemarker UI Plug-in bounded with fmresources.properties
 * file.
 * 
 */
public class FreemarkerUIPluginMessages extends NLS {

	private static final String BUNDLE_NAME = "org.eclipse.dltk.freemarker.internal.ui.FreemarkerUIPluginMessages";//$NON-NLS-1$

	// ------------------  Template Page  ----------------------------- 
	public static String FreemarkerEditor_TemplatePage_title;
	
	// ------------------  Overview Page  ----------------------------- 
	public static String FreemarkerEditor_OverviewPage_title;
	public static String FreemarkerEditor_OverviewPage_tab;
	public static String FTLGeneralInfoSection_title;
	public static String FTLGeneralInfoSection_desc;
	public static String ProviderSection_title;
	public static String ProviderSection_desc;
	
	public static String TestingSection_title;
	public static String TestingSection_launchRun;
	public static String TestingSection_launchDebug;
	public static String OverviewPage_testing;
	
	// ------------------  Configuration Page  ----------------------------- 
	public static String FreemarkerEditor_ConfigurationPage_title;
	public static String FreemarkerEditor_ConfigurationPage_tab;
	public static String ConfigurationProjectLevelConfigurationSection_title;
	public static String ConfigurationProjectLevelConfigurationSection_desc;
	public static String ConfigurationTemplateSpecificConfigurationSection_title;
	public static String ConfigurationTemplateSpecificConfigurationSection_desc;
	public static String ConfigurationProviderSection_title;
	public static String ConfigurationProviderSection_desc;
	public static String ConfigurationDetailSection_title;
	public static String ConfigurationDetailSection_desc;
	
	// ------------------  Model Page  ----------------------------- 
	public static String FreemarkerEditor_ModelPage_title;
	public static String FreemarkerEditor_ModelPage_tab;
	public static String ModelProjectLevelConfigurationSection_title;
	public static String ModelProjectLevelConfigurationSection_desc;
	public static String ModelTemplateSpecificConfigurationSection_title;
	public static String ModelTemplateSpecificConfigurationSection_desc;
	public static String ModelProviderSection_title;
	public static String ModelProviderSection_desc;
	public static String ModelDetailSection_title;
	public static String ModelDetailSection_desc;
	
	// ------------------  Preview Page  ----------------------------- 
	public static String FreemarkerEditor_PreviewPage_tab;
	public static String FreemarkerEditor_PreviewPage_title;

	public static String FreemarkerFormPage_help;
	
	// ------------------  Common Provider section  -----------------------------
	
	public static String ClassSelectionSection_useTheTemplateSpecficConfiguration;
	public static String ClassSelectionSection_useProjectLevelConfiguration;

	public static String TemplateSpecificConfigurationSection_class;
	public static String TemplateSpecificConfigurationSection_method;
	public static String TemplateSpecificConfigurationSection_browse;
	public static String TemplateSpecificConfigurationSection_setAsProjectLevelConfig;
	
	public static String ProjectLevelConfigurationSection_addButton;
	public static String ProjectLevelConfigurationSection_editButton;
	public static String ProjectLevelConfigurationSection_removeButton;
	public static String ProjectLevelConfigurationSection_duplicateButton;
	public static String ProjectLevelConfigurationSection_openClassButton;
	

	// ------------------  Java Class/Method dialogs -----------------------------
	
	public static String ClassAttributeRow_dialogTitle;
	public static String MethodSelectionDialog_title;
	public static String MethodSelectionDialog_label;
	
	
	public static String FreemarkerJavaHelper_msgContentAssistAvailable;

	public static String ControlValidationUtility_errorMsgNotOnClasspath;
	public static String ControlValidationUtility_errorMsgClassRequired;
	public static String ControlValidationUtility_errorMsgMethodRequired;
	public static String ControlValidationUtility_errorMsgMethodNotExist;
	public static String ControlValidationUtility_nameConfigRequired ;
	public static String ControlValidationUtility_nameConfigAlreadyExist;
	
	public static String EditableTablePart_renameTitle;
	public static String EditableTablePart_renameAction;

	public static String TableSection_itemCount;

	// ------------------  Freemarker Preview View -----------------------------
	
	public static String FreemarkerPreviewView_notAvailable;

	static {
		// load message values from bundle file
		NLS.initializeMessages(BUNDLE_NAME, FreemarkerUIPluginMessages.class);
	}
}

