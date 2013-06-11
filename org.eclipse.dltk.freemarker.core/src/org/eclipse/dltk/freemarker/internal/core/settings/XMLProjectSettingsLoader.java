package org.eclipse.dltk.freemarker.internal.core.settings;

import org.eclipse.dltk.freemarker.core.settings.provider.IObjectInstanceProvider;
import org.eclipse.dltk.freemarker.core.settings.provider.IProjectObjectInstanceProvider;
import org.xml.sax.Attributes;

/**
 * {@link FreemarkerProjectSettings} XML loader. Example :
 * 
 * <pre>
 * <project>
 *   <provider id="1277830101750" name="My Model Config 1" class="com.mycompany.myapp.fm.MyFreemarkerSettings" method="getModel" type="MODEL" /> 
 *   <provider id="1277830675171" name="My Model Config 2" class="com.mycompany.myapp.fm.MyFreemarkerSettings" method="getModel2" type="MODEL" /> 
 *   <provider id="1277829256203" name="My Configuration Config" default="false" class="com.mycompany.myapp.fm.MyFreemarkerSettings" method="getConfiguration" type="CONFIGURATION" /> 
 *   <provider id="1277829333468" name="My Configuration Config 2" default="false" class="com.mycompany.myapp.fm.MyFreemarkerSettings" method="getConfiguration2" type="CONFIGURATION" /> 
 * </project>
 * </pre>
 * 
 */
public class XMLProjectSettingsLoader extends
		XMLSettingsLoader<FreemarkerProjectSettings> {

	public XMLProjectSettingsLoader(FreemarkerProjectSettings settings) {
		super(settings);
	}

	@Override
	protected void loadProvider(IObjectInstanceProvider provider,
			Attributes atts) {
		super.loadProvider(provider, atts);
		IProjectObjectInstanceProvider projectProvider = (IProjectObjectInstanceProvider) provider;
		String id = atts.getValue(ID_ATTR);
		projectProvider.setId(id);
		projectProvider.setName(atts.getValue(NAME_ATTR));
	}
}
