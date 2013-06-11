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

/**
 * Position calculator.
 * 
 */
public class PositionCalculator {

	private String completion;
	private PositionTokenType tokenType = PositionTokenType.UNKNOWN;
	private PositionTokenType tokenTypeContainer = PositionTokenType.UNKNOWN;

	public PositionCalculator(String completion, PositionTokenType tokenType,
			PositionTokenType tokenTypeContainer) {
		this.completion = completion;
		this.tokenType = tokenType;
		this.tokenTypeContainer = tokenTypeContainer;
	}

	/**
	 * Return the completion content used by IDE autocompletion search .
	 * 
	 * <pre>
	 * The FTL template : 
	 *  'public class ${className'
	 *  
	 *  returns className.
	 * </pre>
	 * 
	 * @return
	 */
	public String getCompletion() {
		return completion;
	}

	/**
	 * Return the FM token type.
	 * 
	 * <pre>
	 * The FTL template : 
	 *  'public class ${className'
	 *  
	 *  returns {@link PositionTokenType#EXPRESSION} because className belong to a FM Expression.
	 * </pre>
	 * 
	 * @return
	 */
	public PositionTokenType getTokenType() {
		return tokenType;
	}

	/**
	 * <pre>
	 * The FTL template : 
	 *  'public class ${className'
	 *  
	 *  returns {@link PositionTokenType#INTERPOLATION} because className belong to a FM Expression coming from Interpolation.
	 * </pre>
	 * 
	 * @return
	 */
	public PositionTokenType getTokenTypeContainer() {
		return tokenTypeContainer;
	}
}
