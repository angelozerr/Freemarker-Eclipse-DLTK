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

import org.eclipse.core.resources.IStorage;
import org.eclipse.dltk.core.IBuildpathEntry;
import org.eclipse.dltk.core.IMethod;
import org.eclipse.dltk.core.IModelElement;
import org.eclipse.dltk.core.IParameter;
import org.eclipse.dltk.core.IProjectFragment;
import org.eclipse.dltk.core.IScriptFolder;
import org.eclipse.dltk.core.ISourceModule;
import org.eclipse.dltk.core.IType;
import org.eclipse.dltk.core.ModelException;
import org.eclipse.dltk.core.environment.EnvironmentPathUtils;
import org.eclipse.dltk.ui.ScriptElementLabels;
import org.eclipse.dltk.ui.viewsupport.StorageLabelProvider;
import org.eclipse.jface.viewers.LabelProvider;
import org.eclipse.swt.graphics.Image;

public class FreemarkerLabelProvider extends LabelProvider {

	public static final String PACKAGE_SEPARATOR = "::"; //$NON-NLS-1$
	public static final String SEPARATOR = " - "; //$NON-NLS-1$
	public static final String FOLDER_SEPARATOR = "/"; //$NON-NLS-1$
	public static final String METHOD_SEPARATOR = "#"; //$NON-NLS-1$

	private final StorageLabelProvider fStorageLabelProvider;

	public FreemarkerLabelProvider() {
		fStorageLabelProvider = new StorageLabelProvider();
	}

	public Image getImage(Object element) {
		if (element instanceof IStorage) {
			return fStorageLabelProvider.getImage(element);
		}
		return null;
	}

	public String getText(Object element) {
		if (element instanceof ISourceModule) {
			final StringBuffer sb = new StringBuffer();
			appendSourceModule((ISourceModule) element, sb);
			return sb.toString();
		} else if (element instanceof IType) {
			final StringBuffer sb = new StringBuffer();
			appendQualifiedType((IType) element, sb);
			return sb.toString();
		} else if (element instanceof IMethod) {
			final StringBuffer sb = new StringBuffer();
			appendQualifiedMethod((IMethod) element, sb);
			return sb.toString();
		}
		return super.getText(element);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.jface.viewers.BaseLabelProvider#dispose()
	 */
	public void dispose() {
		fStorageLabelProvider.dispose();
	}

	public static void appendQualifiedMethod(IMethod method, StringBuffer sb) {
		final IModelElement parent = method.getParent();
		final IType type;
		if (parent instanceof IType) {
			type = (IType) parent;
		} else {
			type = null;
		}
		if (type != null) {
			ISourceModule module = appendType(type, sb);
			sb.append(METHOD_SEPARATOR);
			sb.append(method.getElementName());
			IParameter[] parameters;
			try {
				parameters = method.getParameters();
			} catch (ModelException e) {
				parameters = null;
			}
			if (parameters != null) {
				sb.append('(');
				for (int i = 0; i < parameters.length; ++i) {
					if (i != 0) {
						sb.append(", "); //$NON-NLS-1$
					}
					sb.append(parameters[i]);
				}
				sb.append(')');
			}
			if (module != null) {
				sb.append(SEPARATOR);
				appendSourceModule(module, sb);
			}
		}
	}

	public static void appendQualifiedType(IType type, StringBuffer sb) {
		sb.append(type.getElementName());
		final IModelElement parent = type.getParent();
		final ISourceModule parentModule;
		if (parent instanceof IType) {
			sb.append(SEPARATOR);
			parentModule = appendType((IType) parent, sb);
		} else if (parent instanceof ISourceModule) {
			parentModule = (ISourceModule) parent;
		} else {
			parentModule = null;
		}
		if (parentModule != null) {
			sb.append(SEPARATOR);
			appendSourceModule(parentModule, sb);
		}
	}

	private static ISourceModule appendType(IType type, StringBuffer sb) {
		final IModelElement parent = type.getParent();
		if (parent instanceof IType) {
			final ISourceModule module = appendType((IType) parent, sb);
			sb.append(PACKAGE_SEPARATOR);
			sb.append(type.getElementName());
			return module;
		} else {
			sb.append(type.getElementName());
			if (parent instanceof ISourceModule) {
				return (ISourceModule) parent;
			} else {
				return null;
			}
		}
	}

	public static void appendSourceModule(ISourceModule module, StringBuffer sb) {
		final IModelElement parent = module.getParent();
		final IProjectFragment fragment;
		if (parent instanceof IScriptFolder) {
			fragment = appendScriptFolder((IScriptFolder) parent, sb);
		} else {
			fragment = null;
		}
		sb.append(module.getElementName());
		if (fragment != null) {
			if (!fragment.isArchive()) {
				if (fragment.getPath().toString().startsWith(
						IBuildpathEntry.BUILTIN_EXTERNAL_ENTRY_STR)) {
					sb.append(' ');
					sb.append(ScriptElementLabels.BUILTINS_FRAGMENT);
				} else if (fragment.isExternal()) {
					sb.append(SEPARATOR);
					sb.append(EnvironmentPathUtils.getLocalPath(
							fragment.getPath()).toPortableString());
				}
			}
		}
	}

	private static IProjectFragment appendScriptFolder(IScriptFolder folder,
			StringBuffer sb) {
		final IModelElement parent = folder.getParent();
		final IProjectFragment fragment;
		if (parent instanceof IScriptFolder) {
			fragment = appendScriptFolder((IScriptFolder) parent, sb);
		} else if (parent instanceof IProjectFragment) {
			fragment = (IProjectFragment) parent;
		} else {
			fragment = null;
		}
		if (!folder.isRootFolder()) {
			sb.append(folder.getElementName());
			sb.append(FOLDER_SEPARATOR);
		}
		return fragment;
	}

}
