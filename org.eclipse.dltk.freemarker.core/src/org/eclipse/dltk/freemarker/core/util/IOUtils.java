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
import java.net.URI;

import org.eclipse.core.filesystem.EFS;
import org.eclipse.core.filesystem.IFileStore;
import org.eclipse.core.resources.IFolder;
import org.eclipse.core.resources.IWorkspaceRoot;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.IProgressMonitor;

/**
 * I/O Utilities.
 * 
 */
public class IOUtils {

	/**
	 * Converts the given URI to a local file. Use the existing file if the uri
	 * is on the local file system. Otherwise fetch it. Returns null if unable
	 * to fetch it.
	 * 
	 * @param uri
	 * @param monitor
	 * @return
	 * @throws CoreException
	 */
	public static File toLocalFile(URI uri, IProgressMonitor monitor)
			throws CoreException {
		IFileStore fileStore = EFS.getStore(uri);
		File localFile = fileStore.toLocalFile(EFS.NONE, monitor);
		if (localFile == null)
			// non local file system
			localFile = fileStore.toLocalFile(EFS.CACHE, monitor);
		return localFile;
	}

	/**
	 * Create Eclipse {@link IFolder}.
	 * 
	 * @param folderHandle
	 * @param monitor
	 * @throws CoreException
	 */
	public static void createFolder(IFolder folderHandle,
			IProgressMonitor monitor) throws CoreException {
		if (folderHandle.exists())
			return;
		
		IPath path = folderHandle.getFullPath();
		IWorkspaceRoot root = ResourcesPlugin.getWorkspace().getRoot();
		int numSegments = path.segmentCount();
		if (numSegments > 2
				&& !root.getFolder(path.removeLastSegments(1)).exists()) {
			// If the direct parent of the path doesn't exist, try
			// to create the
			// necessary directories.
			for (int i = numSegments - 2; i > 0; i--) {
				IFolder folder = root.getFolder(path.removeLastSegments(i));
				if (!folder.exists()) {
					folder.create(false, true, monitor);
				}
			}
		}
		folderHandle.create(false, true, monitor);
	}

}
