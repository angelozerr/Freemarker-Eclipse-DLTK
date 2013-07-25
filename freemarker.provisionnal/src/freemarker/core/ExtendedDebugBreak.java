package freemarker.core;

import java.io.IOException;



import freemarker.provisionnal.debug.Breakpoint;
import freemarker.provisionnal.debug.impl.AbstractDebuggerService;
import freemarker.template.TemplateException;

public class ExtendedDebugBreak extends DebugBreak {

	private final AbstractDebuggerService debuggerService;
	private boolean suspendAfter = false;
	private final String templateName;
	private final String fileName;
	private TemplateElement nestedBlock;

	public ExtendedDebugBreak(TemplateElement nestedBlock,
			AbstractDebuggerService debuggerService, Breakpoint breakpoint) {
		super(nestedBlock);
		this.debuggerService = debuggerService;
		this.nestedBlock = nestedBlock;
		
		// Debug Break must be suspended before or after the TemplateElement te
		// founded.
		// After suspend, must be done when breakpoint line is not the same
		// than the TemplateElement
		// (ex : breakpoint added to </#if> must be executed before to execute
		// the whole ConditionnalBlock and suspend to the </#if>)
		boolean suspendAfter = (nestedBlock.getBeginLine() != breakpoint
				.getLine());
		this.suspendAfter = suspendAfter;
		this.templateName = breakpoint.getTemplateName();
		this.fileName = breakpoint.getFileName();
	}


	@Override
	public void accept(Environment env) throws TemplateException, IOException {
		int line = 0;
		if (suspendAfter) {
			// Suspend must be done after the execution of the nestedBlock
			line = nestedBlock.getEndLine();
			nestedBlock.accept(env);
		} else {
			// Suspend must be done before the execution of the nestedBlock
			line = nestedBlock.getBeginLine();
		}

		if (debuggerService.suspendEnvironment(env, line, templateName,
				fileName)) {
			if (!suspendAfter) {
				nestedBlock.accept(env);
			}
		} else {
			throw new StopException(env, "Stopped by debugger");
		}
	}

}
