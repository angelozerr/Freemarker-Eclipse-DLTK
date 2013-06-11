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

import freemarker.template.Configuration;

/**
 * Freemarker configuration providedr.
 */
public interface ConfigurationProvider {

	/**
	 * return the {@link Configuration}.
	 * 
	 * @return
	 * @throws InstanceProviderException
	 */
	Configuration getConfiguration();

}
