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
package org.eclipse.dltk.freemarker.internal.ui.util;

import org.eclipse.jface.dialogs.Dialog;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.PlatformUI;

/**
 * 
 * Static utility methods for manipulating SWT Shell...
 * 
 * 
 */
public class SWTUtils {

	/**
	 * Get active SWT {@link Shell}.
	 * 
	 * @return
	 */
	public static Shell getActiveShell() {
		IWorkbenchWindow window = getActiveWorkbenchWindow();
		if (window != null) {
			Shell shell = window.getShell();
			if (shell != null) {
				return shell;
			} // else {
			// return JavaPlugin.getActiveWorkbenchShell();
			// }
		}
		return null;
	}

	/**
	 * Get active {@link IWorkbenchWindow}.
	 * 
	 * @return
	 */
	public static IWorkbenchWindow getActiveWorkbenchWindow() {
		class SwtRunner implements Runnable {

			public IWorkbenchWindow getWindow() {
				return window;
			}

			public void computeActiveShell() {
				SWTUtils.invokeOnDisplayThread(this);
			}

			public void run() {
				window = PlatformUI.getWorkbench().getActiveWorkbenchWindow();
			}

			private IWorkbenchWindow window;

			SwtRunner() {
			}
		}

		SwtRunner runner = new SwtRunner();
		runner.computeActiveShell();
		return runner.getWindow();
	}

	/**
	 * Invoke <code>runnable</code> by using {@link Display} of the first
	 * window.
	 * 
	 * @param runnable
	 */
	public static void invokeOnDisplayThread(Runnable runnable) {
		IWorkbench workbench = PlatformUI.getWorkbench();
		IWorkbenchWindow windows[] = workbench.getWorkbenchWindows();
		if (windows != null && windows.length > 0) {
			Display display = windows[0].getShell().getDisplay();
			display.syncExec(runnable);
		} else {
			runnable.run();
		}
	}

	public static void setDialogSize(Dialog dialog, int width, int height) {
		Point computedSize = dialog.getShell().computeSize(SWT.DEFAULT,
				SWT.DEFAULT);
		width = Math.max(computedSize.x, width);
		height = Math.max(computedSize.y, height);
		dialog.getShell().setSize(width, height);
	}
}
