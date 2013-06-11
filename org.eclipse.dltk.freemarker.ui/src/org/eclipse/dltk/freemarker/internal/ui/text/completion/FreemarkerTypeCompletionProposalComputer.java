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
package org.eclipse.dltk.freemarker.internal.ui.text.completion;

import org.eclipse.dltk.freemarker.internal.ui.templates.FreemarkerTemplateCompletionProcessor;
import org.eclipse.dltk.ui.text.completion.ScriptCompletionProposalCollector;
import org.eclipse.dltk.ui.text.completion.ScriptCompletionProposalComputer;
import org.eclipse.dltk.ui.text.completion.ScriptContentAssistInvocationContext;
import org.eclipse.jface.text.templates.TemplateCompletionProcessor;

public class FreemarkerTypeCompletionProposalComputer extends
		ScriptCompletionProposalComputer {

	public FreemarkerTypeCompletionProposalComputer() {
	}

	protected ScriptCompletionProposalCollector createCollector(
			ScriptContentAssistInvocationContext context) {
		return new FreemarkerCompletionProposalCollector(context
				.getSourceModule());
	}

	protected TemplateCompletionProcessor createTemplateProposalComputer(
			ScriptContentAssistInvocationContext context) {
		return new FreemarkerTemplateCompletionProcessor(context);
	}
}
