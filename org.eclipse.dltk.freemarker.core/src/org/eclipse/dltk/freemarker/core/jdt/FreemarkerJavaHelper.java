package org.eclipse.dltk.freemarker.core.jdt;

import java.util.ArrayList;

import org.eclipse.core.resources.IProject;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.Path;
import org.eclipse.dltk.freemarker.core.util.StringUtils;
import org.eclipse.jdt.core.IJavaProject;
import org.eclipse.jdt.core.IMethod;
import org.eclipse.jdt.core.IPackageFragmentRoot;
import org.eclipse.jdt.core.IType;
import org.eclipse.jdt.core.JavaCore;
import org.eclipse.jdt.core.JavaModelException;
import org.eclipse.jdt.core.search.IJavaSearchScope;
import org.eclipse.jdt.core.search.SearchEngine;
import org.eclipse.jdt.launching.JavaRuntime;

public class FreemarkerJavaHelper {

	public static IJavaSearchScope getSearchScope(IProject project) {
		return getSearchScope(JavaCore.create(project));
	}

	public static IJavaSearchScope getSearchScope(IJavaProject project) {
		return SearchEngine.createJavaSearchScope(getNonJRERoots(project));
	}

	public static IPackageFragmentRoot[] getNonJRERoots(IJavaProject project) {
		ArrayList result = new ArrayList();
		try {
			IPackageFragmentRoot[] roots = project.getAllPackageFragmentRoots();
			for (int i = 0; i < roots.length; i++) {
				if (!isJRELibrary(roots[i])) {
					result.add(roots[i]);
				}
			}
		} catch (JavaModelException e) {
		}
		return (IPackageFragmentRoot[]) result
				.toArray(new IPackageFragmentRoot[result.size()]);
	}

	public static boolean isJRELibrary(IPackageFragmentRoot root) {
		try {
			IPath path = root.getRawClasspathEntry().getPath();
			if (path.equals(new Path(JavaRuntime.JRE_CONTAINER))
					|| path.equals(new Path(JavaRuntime.JRELIB_VARIABLE))) {
				return true;
			}
		} catch (JavaModelException e) {
		}
		return false;
	}

	public static boolean isOnClasspath(String fullyQualifiedName,
			IJavaProject project) {
		if (fullyQualifiedName.indexOf('$') != -1)
			fullyQualifiedName = fullyQualifiedName.replace('$', '.');
		try {
			IType type = project.findType(fullyQualifiedName);
			return type != null && type.exists();
		} catch (JavaModelException e) {
		}
		return false;
	}

	public static IMethod getMethod(String className, String methodName,
			IJavaProject project) {
		if (className.indexOf('$') != -1)
			className = className.replace('$', '.');
		try {
			IType type = project.findType(className);
			if (type != null && type.exists()) {
				IMethod method = type.getMethod(methodName,
						StringUtils.EMPTY_STRING_ARRAY);
				if (method != null && method.exists()) {
					return method;
				}
			}
		} catch (JavaModelException e) {
		}
		return null;
	}
}
