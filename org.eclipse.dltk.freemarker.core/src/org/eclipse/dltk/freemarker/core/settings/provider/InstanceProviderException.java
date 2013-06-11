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
package org.eclipse.dltk.freemarker.core.settings.provider;

import org.eclipse.dltk.freemarker.core.settings.provider.IObjectInstanceProvider.ProviderType;

/**
 * Exception thrown when {@link IObjectInstanceProvider#getInstance()} has
 * error.
 * 
 */
public class InstanceProviderException extends RuntimeException {

	private ProviderType providerType;
	
	public InstanceProviderException(ProviderType providerType, Exception e) {
		super(e);
		this.providerType = providerType;
	}
	
	public InstanceProviderException(ProviderType providerType, String message) {
		super(message);
		this.providerType = providerType;
	}
	
	@Override
	public String getMessage() {		
		String message =  super.getMessage();
		message = providerType.name() + " provider problem." + "\n" + message;
		return message;
	}
}
