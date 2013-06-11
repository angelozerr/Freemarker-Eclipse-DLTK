package org.eclipse.dltk.freemarker.internal.core.settings.events;

import org.eclipse.dltk.freemarker.core.settings.IFreemarkerElementSettings;
import org.eclipse.dltk.freemarker.core.settings.IFreemarkerSettingsChangedEvent;

public class FreemarkerSettingsSavedEvent implements
		IFreemarkerSettingsChangedEvent {

	private final IFreemarkerElementSettings settings;
	private final EventType type;

	public FreemarkerSettingsSavedEvent(IFreemarkerElementSettings settings,
			EventType type) {
		this.settings = settings;
		this.type = type;
	}

	public IFreemarkerElementSettings getSettings() {
		return settings;
	}

	public EventType getType() {
		return type;
	}

}
