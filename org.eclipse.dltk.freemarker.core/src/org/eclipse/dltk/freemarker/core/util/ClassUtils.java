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
package org.eclipse.dltk.freemarker.core.util;

import java.io.File;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.ArrayList;
import java.util.List;

import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IWorkspaceRoot;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IPath;
import org.eclipse.dltk.freemarker.core.settings.IClassLoaderProject;
import org.eclipse.jdt.core.IClasspathEntry;
import org.eclipse.jdt.core.IJavaProject;
import org.eclipse.jdt.core.JavaCore;
import org.eclipse.jdt.core.JavaModelException;

/**
 * Class Utilities.
 */
public class ClassUtils {

	private static final Class<?>[] EMPTY_CLASS = new Class[0];

	private static final Object[] EMPTY_OBJECT = new Object[0];

	public static Object execute(IClassLoaderProject project, String className,
			String methodName) throws CoreException, ClassNotFoundException,
			InstantiationException, IllegalAccessException, SecurityException,
			NoSuchMethodException, IllegalArgumentException,
			InvocationTargetException {
		Class<?> clazz = getClass(project, className);
		if (clazz != null) {

			Method method = clazz.getMethod(methodName, EMPTY_CLASS);
			boolean staticMethod = Modifier.isStatic(method.getModifiers());
			if (staticMethod) {
				return method.invoke(clazz, EMPTY_OBJECT);
			} else {

				return method.invoke(clazz.newInstance(), EMPTY_OBJECT);
			}
		}
		return null;
	}

	public static Class<?> getClass(IClassLoaderProject project,
			String className) throws CoreException, ClassNotFoundException {
		IProject p = project.getProject();
		if (!p.hasNature(JavaCore.NATURE_ID)) {
			return null;
		}
		ClassLoader classLoader = project.getClassLoader();
		if (classLoader == null) {
			classLoader = createClassLoader(JavaCore.create(p));
			project.setClassLoader(classLoader);
		}
		return Class.forName(className, true, classLoader);

	}

	/***
	 * Assembles a Classpath for an URLClassloader from a Project-Configuration
	 * 
	 * @param project
	 *            the eclipse project to get the config info from
	 * @return a configured Classloader with the classpath of the project
	 */
	public static ClassLoader createClassLoader(IJavaProject javaProject)
			throws CoreException {

		// List of URL ClassLoader.
		List<URL> urls = new ArrayList<URL>();
		// List of Java Project already done to avoid cycle.
		List<IJavaProject> javaProjectsAlreadyDone = new ArrayList<IJavaProject>();
		IWorkspaceRoot root = ResourcesPlugin.getWorkspace().getRoot();
		IPath rootLocation = root.getLocation();
		// Add URL from JavaProject
		addPath(javaProject, urls, root, rootLocation, javaProjectsAlreadyDone);
		return new URLClassLoader((URL[]) urls.toArray(new URL[urls.size()]),
				Thread.currentThread().getContextClassLoader()) {

			@Override
			public Class<?> loadClass(String arg0)
					throws ClassNotFoundException {
				return super.loadClass(arg0);
			}
		};
	}

	/**
	 * Add URL form the JavaProject.
	 * 
	 * @param javaProject
	 * @param urls
	 * @param root
	 * @param rootLocation
	 * @param javaProjectsAlreadyDone
	 * @throws JavaModelException
	 * @throws CoreException
	 */
	private static void addPath(IJavaProject javaProject, List<URL> urls,
			IWorkspaceRoot root, IPath rootLocation,
			List<IJavaProject> javaProjectsAlreadyDone)
			throws JavaModelException, CoreException {
		if (javaProjectsAlreadyDone.contains(javaProject))
			return;

		javaProjectsAlreadyDone.add(javaProject);
		String projectName = javaProject.getElementName();
		IClasspathEntry javacp[] = javaProject.getResolvedClasspath(true);

		// Add bin folder
		IPath outputLocation = javaProject.getOutputLocation();
		addPath(urls, rootLocation.append(outputLocation));

		// Loop for .classpath
		IClasspathEntry entry = null;
		IPath entryPath = null;
		for (int i = 0; i < javacp.length; i++) {
			// load bin folder of referenced projects into classpath
			entry = javacp[i];
			entryPath = entry.getPath();
			switch (entry.getEntryKind()) {
			case IClasspathEntry.CPE_LIBRARY:
				// add jars
				if (projectName.equals(entryPath.segment(0))) {
					// Jar belongs to the Java Project, add base dir
					addPath(urls, rootLocation.append(entryPath));
				} else {
					// External Jar (ex : C:/Program Files/xxx.jar)
					addPath(urls, entryPath);
				}
				break;
			case IClasspathEntry.CPE_SOURCE:
				// add the source folders of the project
				addPath(urls, rootLocation.append(entryPath));
				break;
			case IClasspathEntry.CPE_PROJECT:
				// add bin folder from referenced project
				IProject referencedProject = root.getProject(entryPath
						.segment(0));
				if (referencedProject != null && referencedProject.exists()
						&& referencedProject.hasNature(JavaCore.NATURE_ID)) {
					IJavaProject referencedJavaProject = JavaCore
							.create(referencedProject);
					addPath(referencedJavaProject, urls, root, rootLocation,
							javaProjectsAlreadyDone);
				}
				break;
			default:
				addPath(urls, entryPath);
				break;
			}
		}
	}

	private static void addPath(List<URL> urls, IPath path) {
		File f = path.toFile();
		try {
			URL url = f.toURI().toURL();
			urls.add(url);
		} catch (MalformedURLException e) {
			// This error should be never thrown.
		}
	}

	/**
	 * Format the class/method name.
	 * 
	 * @param className
	 * @param methodName
	 * @param fullClassName
	 * @return
	 */
	public static String formatClassMethodName(String className,
			String methodName, boolean fullClassName) {
		StringBuffer result = new StringBuffer();
		if (fullClassName) {
			result.append(className);
		} else {
			if (className != null) {
				int lastIndex = className.lastIndexOf('.');
				if (lastIndex != -1) {
					result.append(className.substring(lastIndex,
							className.length()));
				} else {
					result.append(className);
				}

			}
		}
		result.append("#");
		result.append(methodName);
		result.append("()");
		return result.toString();
	}
}
