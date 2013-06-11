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
 * {@link AssistUtils} test case with FM Interpolation.
 * 
 */
public class AssistUtilsInterpolationTestCase extends TestCase {

	private static final int tagSyntax = Configuration.AUTO_DETECT_TAG_SYNTAX;

	private static final int maxLength = 100;

	public void testInterpolationOK() {

		String content = "public class ${className";
		int position = content.length();

		PositionCalculator calculator = AssistUtils.getPositionCalculator(
				content, position, tagSyntax, maxLength);
		assertNotNull(calculator);

		assertEquals(calculator.getCompletion(), "className");
		assertEquals(PositionTokenType.EXPRESSION, calculator.getTokenType());
		assertEquals(PositionTokenType.INTERPOLATION,
				calculator.getTokenTypeContainer());
	}

	public void testInterpolationPositionBefore() {

		String content = "public class ${className";
		int position = content.indexOf("$");

		PositionCalculator calculator = AssistUtils.getPositionCalculator(
				content, position, tagSyntax, maxLength);
		assertNull(calculator);
	}

	public void testInterpolationPositionAfter() {

		String content = "public class ${className";
		int position = content.indexOf("{") + 1;

		PositionCalculator calculator = AssistUtils.getPositionCalculator(
				content, position, tagSyntax, maxLength);
		assertNotNull(calculator);

		assertEquals(calculator.getCompletion(), "");
		assertEquals(PositionTokenType.EXPRESSION, calculator.getTokenType());
		assertEquals(PositionTokenType.INTERPOLATION,
				calculator.getTokenTypeContainer());
	}

	public void testInterpolationNotValid() {

		String content = "{className";
		int position = content.length();

		PositionCalculator calculator = AssistUtils.getPositionCalculator(
				content, position, tagSyntax, maxLength);
		assertNull(calculator);

	}
}
