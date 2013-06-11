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


/**
 * Freemarker model provider.
 */
public interface ModelProvider {

	/**
	 * Return the model.
	 * 
	 * @return
	 * @throws InstanceProviderException
	 */
	Object getModel();
}
