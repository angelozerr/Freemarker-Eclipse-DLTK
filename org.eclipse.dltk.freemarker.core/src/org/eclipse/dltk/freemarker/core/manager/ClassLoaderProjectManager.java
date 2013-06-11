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
package org.eclipse.dltk.freemarker.core.manager;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.dltk.freemarker.core.settings.IClassLoaderProject;
import org.eclipse.jdt.core.ElementChangedEvent;
import org.eclipse.jdt.core.IElementChangedListener;
import org.eclipse.jdt.core.IJavaElementDelta;
import org.eclipse.jdt.core.JavaCore;

/**
 * {@link IClassLoaderProject} manager.
 * 
 * @param <T>
 */
public class ClassLoaderProjectManager<T extends IClassLoaderProject> {

	private List<T> projects = new ArrayList<T>();
	private boolean initialized = false;

	private IElementChangedListener javaListener = new IElementChangedListener() {

		public void elementChanged(ElementChangedEvent event) {

			IJavaElementDelta[] deltas = event.getDelta().getChangedChildren();
			if (isJavaSourceChanged(deltas)) {
				// Java source file is modified, Class loader cache must be
				// cleared.
				clearAllClassLoderCache();
			}
		}

		/**
		 * Return true if Java source changed and false otherwise.
		 * 
		 * @param deltas
		 * @return
		 */
		private boolean isJavaSourceChanged(IJavaElementDelta[] deltas) {
			if (deltas != null && deltas.length > 0) {
				for (int i = 0; i < deltas.length; i++) {
					IJavaElementDelta delta = deltas[i];
					if ((delta.getFlags() & IJavaElementDelta.F_PRIMARY_RESOURCE) != 0) {
						return true;
					}
					if (isJavaSourceChanged(delta.getChangedChildren())) {
						return true;
					}
				}
			}
			return false;
		}
	};

	protected ClassLoaderProjectManager() {

	}

	/**
	 * Initialize manager by adding the JDT Java listener to observe changes of
	 * Java code (Java source, classpath...).
	 */
	public void initialize() {
		if (initialized) {
			return;
		}
		JavaCore.addElementChangedListener(javaListener,
				ElementChangedEvent.POST_CHANGE);
		initialized = true;
	}

	/**
	 * Dispose manager by removing the JDT Java listener to observe changes of
	 * Java code (Java source, classpath...).
	 */
	public void dispose() {
		JavaCore.removeElementChangedListener(javaListener);
	}

	/**
	 * Add Project wich must manage Java ClassLoader.
	 * 
	 * @param project
	 */
	protected void addProject(T project) {
		if (project != null && !projects.contains(project)) {
			initialize();
			projects.add(project);
		}
	}

	/**
	 * Clear cache of the whole project.
	 */
	public void clearAllClassLoderCache() {
		for (T project : projects) {
			project.clearClassLoderCache();
		}
	}
}
