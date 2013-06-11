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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.eclipse.dltk.ast.ASTNode;
import org.eclipse.dltk.ast.Modifiers;
import org.eclipse.dltk.ast.expressions.Expression;
import org.eclipse.dltk.ast.references.SimpleReference;
import org.eclipse.dltk.ast.references.VariableReference;
import org.eclipse.dltk.ast.statements.Statement;
import org.eclipse.dltk.compiler.ISourceElementRequestor;
import org.eclipse.dltk.compiler.SourceElementRequestVisitor;
import org.eclipse.dltk.freemarker.internal.core.parser.ast.expressions.Assignment;

/**
 * 
 * Freemarker DLTK {@link SourceElementRequestVisitor} implementation.
 * 
 */
public class FreemarkerSourceElementRequestorVisitor extends
		SourceElementRequestVisitor {

	/**
	 * Used to depermine duplicate names.
	 */
	private Map<ASTNode, List<String>> fTypeVariables = new HashMap<ASTNode, List<String>>();

	public FreemarkerSourceElementRequestorVisitor(
			ISourceElementRequestor requesor) {
		super(requesor);
	}

	@Override
	public boolean visit(Expression expression) throws Exception {

		if (expression instanceof Assignment) {
			// this is static variable assignment.
			Assignment assignment = (Assignment) expression;
			Statement left = assignment.getLeft();
			Statement right = assignment.getRight();
			if (left == null) {
				return true;
			}
			processAssignment(left, right);
			return false;
		}
		return true;
	}

	@Override
	public boolean endvisit(Expression expression) throws Exception {
		return true;
	}

	private void processAssignment(Statement left, Statement right) {
		if (left instanceof Assignment) {
			Assignment assignment = (Assignment) left;
			processAssignment(assignment.getLeft(), right);
			processAssignment(assignment.getRight(), right);
		} else if (left instanceof VariableReference && !this.fInMethod) // Handle
			// static
			// variables
			onVisitStaticVariableAssignment((VariableReference) left, right);
		// else if (left instanceof SimpleReference && right instanceof
		// PythonLambdaExpression)
		// onVisitLambdaAssignnment(((SimpleReference)left).getName(),
		// (PythonLambdaExpression)right);
		// else if (left instanceof VariableReference && !this.fInMethod) //
		// Handle static variables
		// onVisitStaticVariableAssignment((VariableReference)left, right);
		// else if (left instanceof ExtendedVariableReference && this.fInClass
		// && this.fInMethod) // This is for in class and in method.
		// onVisitInstanceVariableAssignment((ExtendedVariableReference)left,
		// right);
		// else if (left instanceof ExpressionList)
		// onVisitTestListAssignment((ExpressionList)left, right);
		// else {// TODO: dynamic variable handling not yet
		// // supported.
		// }

	}

	private void onVisitStaticVariableAssignment(SimpleReference var,
			Statement val) {
		// for module static of class static variables.

		if (canAddVariables((ASTNode) this.fNodes.peek(), var.getName())) {
			ISourceElementRequestor.FieldInfo info = new ISourceElementRequestor.FieldInfo();
			info.modifiers = Modifiers.AccStatic;
			info.name = var.getName();
			info.nameSourceEnd = var.sourceEnd() - 1;
			info.nameSourceStart = var.sourceStart();
			info.declarationStart = var.sourceStart();
			this.fRequestor.enterField(info);
			if (val != null) {
				this.fRequestor.exitField(val.sourceEnd() - 1);
			} else {
				this.fRequestor.exitField(var.sourceEnd() - 1);
			}
		}
	}

	private boolean canAddVariables(ASTNode type, String name) {

		if (this.fTypeVariables.containsKey(type)) {
			List<String> variables = this.fTypeVariables.get(type);
			if (variables.contains(name)) {
				return false;
			}
			variables.add(name);
			return true;
		} else {
			List<String> variables = new ArrayList<String>();
			variables.add(name);
			this.fTypeVariables.put(type, variables);
			return true;
		}
	}

}
