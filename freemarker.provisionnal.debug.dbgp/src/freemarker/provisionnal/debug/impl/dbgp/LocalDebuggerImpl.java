package freemarker.provisionnal.debug.impl.dbgp;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.Reader;
import java.io.Writer;
import java.rmi.RemoteException;
import java.util.Collection;
import java.util.Collections;

import org.eclipse.dltk.dbgp.debugger.DbgpContext;
import org.eclipse.dltk.dbgp.debugger.IDbgpDebuggerEngine;
import org.eclipse.dltk.dbgp.debugger.IVariableAdder;
import org.eclipse.dltk.dbgp.debugger.debugger.AbstractDebugger;

import freemarker.core.StopException;
import freemarker.provisionnal.debug.Breakpoint;
import freemarker.provisionnal.debug.DebugModel;
import freemarker.provisionnal.debug.DebuggedEnvironment;
import freemarker.provisionnal.debug.DebuggerListener;
import freemarker.provisionnal.debug.EnvironmentSuspendedEvent;
import freemarker.provisionnal.debug.impl.DebuggerService;
import freemarker.provisionnal.template.ConfigurationProvider;
import freemarker.provisionnal.template.DebuggableConfiguration;
import freemarker.provisionnal.template.DebuggableConfigurationWrapper;
import freemarker.provisionnal.template.ModelProvider;
import freemarker.template.Configuration;
import freemarker.template.Template;
import freemarker.template.TemplateException;
import freemarker.template.TemplateModelException;

public class LocalDebuggerImpl extends AbstractDebugger<Breakpoint> implements
		DebuggerListener {

	private DebuggerService debuggerService;
	private EnvironmentSuspendedEvent currentEnvironmentSuspendedEvent;
	private ConfigurationProvider configurationProvider;
	private ModelProvider modelProvider;
	private Object debuggerId;

	public LocalDebuggerImpl(IDbgpDebuggerEngine debuggerEngine,
			DebuggerService debuggerService,
			ConfigurationProvider configurationProvider,
			ModelProvider modelProvider) {
		super(debuggerEngine);
		this.configurationProvider = configurationProvider;
		this.modelProvider = modelProvider;
		this.debuggerService = debuggerService;
		this.debuggerId = this.debuggerService.addDebuggerListener(this);
	}

	public void doRun() {
		// Get template reader from the file
		File templateFile = new File(super.getFileURI());
		Reader reader;
		try {
			reader = new FileReader(templateFile);

			String templateName = templateFile.getName();

			// Prepare Model
			Object model = getModel();

			Configuration configuration = getConfiguration(templateFile);

			Writer writer = getWriter();
			// Merge data-model with template
			try {
				/* Create Freemarker template */
				Template template = new Template(templateName, reader,
						configuration);
				debuggerService.registerTemplate(template);

				/* Merge data-model with template */
				template.process(model, writer);
				writer.flush();
				writer.close();

			} finally {
				reader.close();
			}

		} catch (FileNotFoundException e) {
			super.err(e);
		} catch (IOException e) {
			super.err(e);
		} catch (StopException e) {
			// Ignore this error. It is thrown by DebugBreak when Debug is
			// stooped
		} catch (TemplateException e) {
			super.err(e);
		} catch (Throwable e) {
			super.err(e);
		}

	}

	private Configuration getConfiguration(File templateFile) {
		if (configurationProvider != null) {
			Configuration configuration = configurationProvider
					.getConfiguration();
			if (configuration != null) {
				return new DebuggableConfigurationWrapper(configuration,
						debuggerService);
			}
		}
		DebuggableConfiguration configuration = new DebuggableConfiguration(
				debuggerService);
		initializeConfiguration(templateFile, configuration);
		return configuration;
	}

	private void initializeConfiguration(File templateFile,
			Configuration configuration) {
		File baseDir = templateFile.getParentFile();
		// Set the directory base dir for Freemarker template loading with
		// the directory which contains this file to manage <#import '...'>.
		try {
			configuration.setDirectoryForTemplateLoading(baseDir);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	private Object getModel() {
		if (modelProvider != null) {
			Object model = modelProvider.getModel();
			if (model != null) {
				return model;
			}
		}
		return Collections.EMPTY_MAP;
	}

	public void resume() {
		if (currentEnvironmentSuspendedEvent != null) {
			try {
				currentEnvironmentSuspendedEvent.getEnvironment().stop();
			} catch (RemoteException e) {
				e.printStackTrace();
			}
		}
	}

	public void suspend() {
		System.out.println("SUSPEND");
	}

	@Override
	protected Breakpoint createBreakpoint(String filename, int lineno) {
		String templateName = super.getRelativePath(filename);
		Breakpoint breakPoint = new Breakpoint(templateName, lineno, filename);
		debuggerService.addBreakpoint(breakPoint);
		return breakPoint;
	}

	/**
	 * Remove the FM BreakPoint retrieved by BreakPoint ID.
	 */
	@Override
	protected void removeBreakpoint(Breakpoint breakPoint) {
		debuggerService.removeBreakpoint(breakPoint);
	}

	public void doStop() {
		if (currentEnvironmentSuspendedEvent != null) {
			try {
				currentEnvironmentSuspendedEvent.getEnvironment().resume();
			} catch (RemoteException e) {
				// Do Nothing
			}
		}
		debuggerService.removeDebuggerListener(debuggerId);
	}

	public void environmentSuspended(EnvironmentSuspendedEvent e)
			throws RemoteException {

		this.currentEnvironmentSuspendedEvent = e;
		String fileName = e.getFileName();
		// String fileName = "file:/" +
		// e.getEnvironment().getTemplate().getName();

		int lineBegin = e.getLine();
		int columnBegin = 0;
		int lineEnd = e.getLine();
		int columnEnd = 0;

		super.suspendByBreakPoint(fileName, lineBegin, columnBegin, lineEnd,
				columnEnd);

	}

	public void evaluate(String expression, int transactionId) {

		// TODO : implement evaluate
		if (currentEnvironmentSuspendedEvent != null) {
			String varName = expression;

			try {
				DebugModel var = currentEnvironmentSuspendedEvent
						.getEnvironment().getVariable(varName);
				if (var != null) {
					int types = var.getModelTypes();
					if ((types & DebugModel.TYPE_BOOLEAN) == DebugModel.TYPE_BOOLEAN) {
						boolean result = var.getAsBoolean();

					}
				}

			} catch (Throwable e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

		}
	}

	// private void intializeConfiguration(File templateFile,
	// Configuration configuration) {
	// File baseDir = templateFile.getParentFile();
	// // Set the directory base dir for Freemarker template loading with
	// // the directory which contains this file to manage <#import '...'>.
	// try {
	// configuration.setDirectoryForTemplateLoading(baseDir);
	// } catch (IOException e) {
	// e.printStackTrace();
	// }
	// }

	public void collectVariables(int contextId, IVariableAdder variableAdder) {
		if (currentEnvironmentSuspendedEvent != null) {
			DebuggedEnvironment environment = currentEnvironmentSuspendedEvent
					.getEnvironment();

			try {
				switch (contextId) {
				case DbgpContext.DBGP_CONTEXT_GLOBAL_ID:
					// Global
					Collection<String> globalNames = environment
							.getGlobalVariableNames();
					populateVariables(globalNames, environment, variableAdder);
					break;
				case DbgpContext.DBGP_CONTEXT_LOCAL_ID:
					// Global
					Collection<String> localNames = environment
							.getLocalVariableNames();
					populateVariables(localNames, environment, variableAdder);
					break;

				}

			} catch (RemoteException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}

	private void populateVariables(Collection<String> varNames,
			DebuggedEnvironment environment, IVariableAdder variableAdder) {
		Object value = null;
		for (String varName : varNames) {

			DebugModel varValue;
			try {
				varValue = environment.getVariable(varName);

				if (varValue != null) {

					int types = varValue.getModelTypes();
					String type = "undefined";
					if ((types & DebugModel.TYPE_BOOLEAN) == DebugModel.TYPE_BOOLEAN) {
						value = varValue.getAsBoolean();
						type = "bool";
					}

					variableAdder.addVariable(varName, type,
							value != null ? value.toString() : "");
				}
			} catch (RemoteException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (TemplateModelException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}

}
