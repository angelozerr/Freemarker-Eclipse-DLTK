package freemarker.core;

import java.io.IOException;

import freemarker.core.Environment;
import freemarker.core.StopException;
import freemarker.core.TemplateElement;
import freemarker.provisionnal.debug.Breakpoint;
import freemarker.provisionnal.debug.impl.AbstractDebuggerService;
import freemarker.template.TemplateException;

/**
 * @author Attila Szegedi
 * @version $Id: DebugBreak.java,v 1.2 2004/09/11 13:11:14 stephanmueller Exp $
 */
public class DebugBreakToContribute {/*extends TemplateElement {
	private final AbstractDebuggerService debuggerService;
	private boolean suspendAfter = false;
	private final String templateName;
	private final String fileName;

	public DebugBreakToContribute(TemplateElement nestedBlock,
			AbstractDebuggerService debuggerService, Breakpoint breakpoint) {
		this.nestedBlock = nestedBlock;
		nestedBlock.setParent(this);
		copyLocationFrom(nestedBlock);
		this.debuggerService = debuggerService;

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

	public void execute(Environment env) throws TemplateException, IOException {
		int line = 0;
		if (suspendAfter) {
			// Suspend must be done after the execution of the nestedBlock
			line = nestedBlock.getEndLine();
			nestedBlock.execute(env);
		} else {
			// Suspend must be done before the execution of the nestedBlock
			line = nestedBlock.getBeginLine();
		}

		if (debuggerService.suspendEnvironment(env, line, templateName,
				fileName)) {
			if (!suspendAfter) {
				nestedBlock.execute(env);
			}
		} else {
			throw new StopException(env, "Stopped by debugger");
		}
	}

	public String getDescription() {
		return nestedBlock.getDescription();
	}*/
}
