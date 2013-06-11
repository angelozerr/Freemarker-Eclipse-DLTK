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
package org.eclipse.dltk.launching;

import java.text.DateFormat;
import java.util.Date;

import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IPath;
import org.eclipse.debug.core.DebugEvent;
import org.eclipse.debug.core.DebugException;
import org.eclipse.debug.core.DebugPlugin;
import org.eclipse.debug.core.IDebugEventSetListener;
import org.eclipse.debug.core.ILaunch;
import org.eclipse.debug.core.ILaunchConfiguration;
import org.eclipse.debug.core.ILaunchConfigurationType;
import org.eclipse.debug.core.model.IProcess;
import org.eclipse.debug.core.model.RuntimeProcess;
import org.eclipse.dltk.compiler.util.Util;
import org.eclipse.dltk.core.DLTKCore;
import org.eclipse.dltk.core.environment.IExecutionEnvironment;
import org.eclipse.dltk.core.environment.IExecutionLogger;
import org.eclipse.dltk.internal.launching.DLTKLaunchingPlugin;
import org.eclipse.osgi.util.NLS;

/**
 * Abstract implementation of a DLTK interpreter runner which works with
 * {@link RunnableProcess} instead of classic Java Lang {@link Process}.
 * 
 * This class could interest DLTK project?
 */
public abstract class AbstractRunnableInterpreterRunner extends
		AbstractInterpreterRunner {

	public AbstractRunnableInterpreterRunner(IInterpreterInstall install) {
		super(install);
	}

	@Override
	protected IProcess rawRun(final ILaunch launch, InterpreterConfig config)
			throws CoreException {

		checkConfig(config, getInstall().getEnvironment());

		String[] cmdLine = renderCommandLine(config);
		IPath workingDirectory = config.getWorkingDirectoryPath();
		String[] environment = getEnvironmentVariablesAsStrings(config);

		final String cmdLineLabel = renderCommandLineLabel(cmdLine);
		final String processLabel = renderProcessLabel(cmdLine);

		if (DLTKLaunchingPlugin.TRACE_EXECUTION) {
			traceExecution(processLabel, cmdLineLabel, workingDirectory,
					environment);
		}

		IExecutionEnvironment exeEnv = getInstall().getExecEnvironment();
		IExecutionLogger logger = DLTKLaunchingPlugin.LOGGING_CATCH_OUTPUT
				.isEnabled() ? new LaunchLogger() : null;
		// Process p = exeEnv.exec(cmdLine, workingDirectory, environment,
		// logger);
		// if (p == null) {
		// abort(LaunchingMessages.AbstractInterpreterRunner_executionWasCancelled,
		// null);
		// }

		RunnableProcess p = createRunnableProcess(launch, config);

		launch.setAttribute(DLTKLaunchingPlugin.LAUNCH_COMMAND_LINE,
				cmdLineLabel);
		final IProcess process[] = new IProcess[] { null };
		DebugPlugin.getDefault().addDebugEventListener(
				new IDebugEventSetListener() {
					public void handleDebugEvents(DebugEvent[] events) {
						for (int i = 0; i < events.length; i++) {
							DebugEvent event = events[i];
							if (event.getSource().equals(process[0])) {
								if (event.getKind() == DebugEvent.CHANGE
										|| event.getKind() == DebugEvent.TERMINATE) {
									updateProcessLabel(launch, cmdLineLabel,
											process[0]);
									if (event.getKind() == DebugEvent.TERMINATE) {
										DebugPlugin.getDefault()
												.removeDebugEventListener(this);
									}
								}
							}
						}
					}
				});
		// process[0] = newProcess(launch, p, processLabel,
		// getDefaultProcessMap());
		process[0] = new RuntimeProcess(launch, p, processLabel, null);

		process[0].setAttribute(IProcess.ATTR_CMDLINE, cmdLineLabel);
		updateProcessLabel(launch, cmdLineLabel, process[0]);
		return process[0];
	}

	/**
	 * String representation of the command line
	 * 
	 * @param commandLine
	 * @return
	 */
	private static String renderCommandLineLabel(String[] commandLine) {
		if (commandLine.length == 0)
			return Util.EMPTY_STRING;
		StringBuffer buf = new StringBuffer();
		for (int i = 0; i < commandLine.length; i++) {
			if (i != 0) {
				buf.append(' ');
			}
			char[] characters = commandLine[i].toCharArray();
			StringBuffer command = new StringBuffer();
			boolean containsSpace = false;
			for (int j = 0; j < characters.length; j++) {
				char character = characters[j];
				if (character == '\"') {
					command.append('\\');
				} else if (character == ' ') {
					containsSpace = true;
				}
				command.append(character);
			}
			if (containsSpace) {
				buf.append('\"');
				buf.append(command.toString());
				buf.append('\"');
			} else {
				buf.append(command.toString());
			}
		}
		return buf.toString();
	}

	private static String renderProcessLabel(String[] commandLine) {
		String format = LaunchingMessages.StandardInterpreterRunner;
		String timestamp = DateFormat.getDateTimeInstance(DateFormat.MEDIUM,
				DateFormat.MEDIUM).format(new Date(System.currentTimeMillis()));
		return NLS.bind(format, commandLine[0], timestamp);
	}

	private void traceExecution(String processLabel, String cmdLineLabel,
			IPath workingDirectory, String[] environment) {
		StringBuffer sb = new StringBuffer();
		sb.append("-----------------------------------------------\n"); //$NON-NLS-1$
		sb.append("Running ").append(processLabel).append('\n'); //$NON-NLS-1$
		sb.append("Command line: ").append(cmdLineLabel).append('\n'); //$NON-NLS-1$
		sb.append("Working directory: ").append(workingDirectory).append('\n'); //$NON-NLS-1$
		sb.append("Environment:\n"); //$NON-NLS-1$
		for (int i = 0; i < environment.length; i++) {
			sb.append('\t').append(environment[i]).append('\n');
		}
		sb.append("-----------------------------------------------\n"); //$NON-NLS-1$
		System.out.println(sb);
	}

	private void updateProcessLabel(final ILaunch launch,
			final String cmdLineLabel, final IProcess process) {
		StringBuffer buffer = new StringBuffer();
		int exitValue = 0;
		try {
			exitValue = process.getExitValue();
		} catch (DebugException e1) {
			// DLTKCore.error(e1);
			exitValue = 0;// Seems not available yet
		}
		if (exitValue != 0) {
			buffer.append("<abnormal exit code:" + exitValue + "> ");
		}
		String type = null;
		ILaunchConfiguration launchConfiguration = launch
				.getLaunchConfiguration();
		if (launchConfiguration != null) {
			try {
				ILaunchConfigurationType launchConfigType = launchConfiguration
						.getType();
				if (launchConfigType != null) {
					type = launchConfigType.getName();
				}
			} catch (CoreException e) {
				DLTKCore.error(e);
			}
			buffer.append(launchConfiguration.getName());
		}
		if (type != null) {
			buffer.append(" ["); //$NON-NLS-1$
			buffer.append(type);
			buffer.append("] "); //$NON-NLS-1$
		}
		buffer.append(process.getLabel());
		process.setAttribute(IProcess.ATTR_PROCESS_LABEL, buffer.toString());
	}

	/**
	 * Create {@link RunnableProcess}.
	 * 
	 * @param launch
	 * @param config
	 * @return
	 */
	protected abstract RunnableProcess createRunnableProcess(ILaunch launch,
			InterpreterConfig config);

}
