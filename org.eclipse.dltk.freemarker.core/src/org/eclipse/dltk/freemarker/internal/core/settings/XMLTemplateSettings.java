package org.eclipse.dltk.freemarker.internal.core.settings;

import org.eclipse.core.runtime.CoreException;
import org.eclipse.dltk.freemarker.core.settings.IFreemarkerProjectSettings;
import org.eclipse.dltk.freemarker.core.settings.provider.IConfigurationInstanceProvider;
import org.eclipse.dltk.freemarker.core.settings.provider.IModelInstanceProvider;
import org.eclipse.dltk.freemarker.core.settings.provider.IObjectInstanceProvider;
import org.eclipse.dltk.freemarker.core.util.StringUtils;
import org.xml.sax.Attributes;

public class XMLTemplateSettings extends
		XMLSettingsLoader<FreemarkerTemplateSettings> {

	public XMLTemplateSettings(FreemarkerTemplateSettings settings) {
		super(settings);
	}

	@Override
	protected void loadProvider(Attributes atts) {
		String id = atts.getValue(REFERENCE_ID_ATTR);
		if (!StringUtils.isEmpty(id)) {
			try {
				IFreemarkerProjectSettings projectSettings = settings
						.getProjectSettings();
				IObjectInstanceProvider provider = projectSettings
						.getProvider(id);
				if (provider != null) {
					switch (provider.getType()) {
					case MODEL:
						settings
								.setModelProvider((IModelInstanceProvider) provider);
						break;
					case CONFIGURATION:
						settings
								.setConfigurationProvider((IConfigurationInstanceProvider) provider);
						break;
					}
				}

			} catch (CoreException e) {
			}
		} else {
			super.loadProvider(atts);
		}
	}

}
