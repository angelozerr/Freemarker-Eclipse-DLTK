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
package org.eclipse.dltk.freemarker.internal.core.parser.visitors;

import java.util.LinkedList;
import java.util.List;

import org.eclipse.dltk.ast.ASTNode;
import org.eclipse.dltk.ast.declarations.ModuleDeclaration;
import org.eclipse.dltk.ast.references.VariableKind;
import org.eclipse.dltk.ast.references.VariableReference;
import org.eclipse.dltk.ast.statements.Statement;
import org.eclipse.dltk.freemarker.internal.core.parser.FreemarkerModuleDeclaration;
import org.eclipse.dltk.freemarker.internal.core.parser.ast.expressions.Assignment;

/*import freemarker.core.ast.ASTVisitor;
import freemarker.core.ast.AssignmentInstruction;
import freemarker.core.ast.Expression;
import freemarker.core.ast.IfBlock;
import freemarker.core.ast.Include;
import freemarker.core.ast.VarDirective;
*/
/**
 * Visitor to transform Freemarker AST 2 DLTK AST.
 * 
 */
public class DLTKFreemarkerASTVisitor {/*extends ASTVisitor {

	private FreemarkerModuleDeclaration moduleDeclaration = null;

	private StateManager states = new StateManager();

	public DLTKFreemarkerASTVisitor(char[] content) {
		moduleDeclaration = new FreemarkerModuleDeclaration(content.length);
		states.push(new TopLevelState(this.moduleDeclaration));
	}

	@Override
	public void visit(AssignmentInstruction node) {

		IState root = states.peek();
		List<String> varNames = node.getVarNames();
		List<Expression> values = node.getValues();
		for (int i = 0; i < varNames.size(); i++) {
			String varname = varNames.get(i);
			// Expression valueExp = values.get(i);
			// TemplateModel value = valueExp.getAsTemplateModel(null);
			// valueExp.literalValue();

			VariableKind variableKind = VariableKind.UNKNOWN;
			int type = node.getType();
			switch (type) {
			case AssignmentInstruction.GLOBAL:
				variableKind = VariableKind.GLOBAL;
				break;
			case AssignmentInstruction.LOCAL:
				variableKind = VariableKind.LOCAL;
				break;
			case AssignmentInstruction.NAMESPACE:
				variableKind = VariableKind.GLOBAL;
				break;
			case AssignmentInstruction.SET:
				variableKind = VariableKind.LOCAL;
				break;
			}

			int start = node.getBeginColumnTabAdjusted();
			int end = node.getEndColumnTabAdjusted();

			// FIXME : compute start/end corretly
			Statement left = new VariableReference(start, end, varname,
					variableKind);
			// FIXME : compute start/end corretly + var value
			Statement right = new VariableReference(6, 7, "TODO",
					VariableKind.ARGUMENT);
			Assignment assignment = new Assignment(left, right);
			root.add(assignment);
		}

	}

	@Override
	public void visit(Include node) {
		// TODO Auto-generated method stub
		super.visit(node);
	}

	@Override
	public void visit(VarDirective node) {
		// TODO Auto-generated method stub
		super.visit(node);
	}

	public void visit(IfBlock node) {
		super.visit(node);
		ASTNode condition = null;// collectSingleNodeSafe(node.getCondition());
		ASTNode thenPart = null;// collectSingleNodeSafe(iVisited.getThenBody());
		ASTNode elsePart = null;// collectSingleNodeSafe(iVisited.getElseBody());
		DLTKIfBlock res = new DLTKIfBlock(condition, thenPart, elsePart);
		// res.setStart(iVisited.getPosition().getStartOffset());
		// res.setEnd(iVisited.getPosition().getEndOffset() + 1);
		states.peek().add(res);
	}

	public ModuleDeclaration getModuleDeclaration() {
		return moduleDeclaration;
	}

	protected static interface IState {
		public void add(ASTNode node);
	}

	protected static String getShortClassName(Object instance) {
		final String className = instance.getClass().getName();
		int dotIndex = className.lastIndexOf('.');
		int dollarIndex = className.lastIndexOf('$');
		if (dotIndex >= 0 || dollarIndex >= 0) {
			return className.substring(Math.max(dotIndex, dollarIndex) + 1);
		} else {
			return className;
		}
	}

	protected static class TopLevelState implements IState {
		private final ModuleDeclaration module;

		public TopLevelState(ModuleDeclaration module) {
			this.module = module;
		}

		public void add(ASTNode statement) {
			module.getStatements().add(statement);
		}

		public String toString() {
			return getShortClassName(this);
		}
	}

	private static class StateManager {
		private LinkedList states = new LinkedList();

		public IState peek() {
			return (IState) states.getLast();
		}

		public void pop() {
			states.removeLast();
		}

		public void push(IState state) {
			states.add(state);
		}
	}*/
}
