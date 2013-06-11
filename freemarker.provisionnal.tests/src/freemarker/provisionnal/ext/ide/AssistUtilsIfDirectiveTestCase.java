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
package freemarker.provisionnal.ext.ide;

import junit.framework.TestCase;
import freemarker.template.Configuration;

/**
 * {@link AssistUtils} test case with If Directive <#if.
 * 
 */
public class AssistUtilsIfDirectiveTestCase extends TestCase {

	private static final int tagSyntax = Configuration.AUTO_DETECT_TAG_SYNTAX;
	private static final int maxLength = 100;

	public void testIfDirectiveOK() {

		String content = "<#if className";
		int position = content.length();

		PositionCalculator calculator = AssistUtils.getPositionCalculator(
				content, position, tagSyntax, maxLength);
		assertNotNull(calculator);

		assertEquals(calculator.getCompletion(), "className");
		assertEquals(PositionTokenType.EXPRESSION, calculator.getTokenType());
		assertEquals(PositionTokenType.IF_DIRECTIVE,
				calculator.getTokenTypeContainer());
	}
	
	public void testIfDirectivePositionBefore() {

		String content = "<#if className";
		int position = content.indexOf("<#i");

		PositionCalculator calculator = AssistUtils.getPositionCalculator(
				content, position, tagSyntax, maxLength);
		assertNull(calculator);
	}

	public void testIfDirectivePositionJustAfter() {

		String content = "<#if className";
		int position = content.indexOf("f") + 1;

		PositionCalculator calculator = AssistUtils.getPositionCalculator(
				content, position, tagSyntax, maxLength);
		assertNull(calculator);
	}

	public void testIfDirectivePositionAfter() {

		String content = "<#if className";
		int position = content.indexOf("f") + 2; // position after '<#if ' (with space).

		PositionCalculator calculator = AssistUtils.getPositionCalculator(
				content, position, tagSyntax, maxLength);
		assertNotNull(calculator);

		assertEquals(calculator.getCompletion(), "");
		assertEquals(PositionTokenType.EXPRESSION, calculator.getTokenType());
		assertEquals(PositionTokenType.IF_DIRECTIVE,
				calculator.getTokenTypeContainer());
	}

	public void testIfDirectiveNotValid() {

		String content = "#if className";
		int position = content.length();

		PositionCalculator calculator = AssistUtils.getPositionCalculator(
				content, position, tagSyntax, maxLength);
		assertNull(calculator);

	}
	
	public void testIfDirectiveAngleBracketNOK() {

		String content = "<#if className";
		int position = content.length();

		int tagSyntax = Configuration.ANGLE_BRACKET_TAG_SYNTAX;
		
		PositionCalculator calculator = AssistUtils.getPositionCalculator(
				content, position, tagSyntax, maxLength);
		assertNull(calculator);

	}	

	public void testIfDirectiveAngleBracketOK() {

		String content = "[#if className";
		int position = content.length();

		int tagSyntax = Configuration.ANGLE_BRACKET_TAG_SYNTAX;
		
		PositionCalculator calculator = AssistUtils.getPositionCalculator(
				content, position, tagSyntax, maxLength);
		assertNotNull(calculator);

		assertEquals(calculator.getCompletion(), "className");
		assertEquals(PositionTokenType.EXPRESSION, calculator.getTokenType());
		assertEquals(PositionTokenType.IF_DIRECTIVE,
				calculator.getTokenTypeContainer());

	}	
}
