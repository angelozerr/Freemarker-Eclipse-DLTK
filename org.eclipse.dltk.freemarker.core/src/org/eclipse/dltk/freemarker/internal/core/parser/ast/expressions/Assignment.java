package org.eclipse.dltk.freemarker.internal.core.parser.ast.expressions;

import org.eclipse.dltk.ast.statements.Statement;
import org.eclipse.dltk.utils.CorePrinter;

/**
 * Assignment expression used to hold a = b expressions.
 */
public class Assignment extends BinaryExpression
{
	
	public static final Assignment[] EMPTY_ARRAY = new Assignment[0];

	/**
	 * Construct from left, right and type expression. Used to construct NotStrictAssignment class.
	 * 
	 * @param left
	 * @param type
	 * @param right
	 */
	protected Assignment( Statement left, int type, Statement right ) {

		super( left, type, right );
	}

	/**
	 * Construct default strict assignment.
	 * 
	 * @param left
	 * @param right
	 */
	public Assignment( Statement left, Statement right ) {

		super( left, E_ASSIGN, right );
	}

	/**
	 * Convert to string in pettern: "left = right"
	 */
	public String toString( ) {
		return getLeft().toString( ) + '=' + getRight().toString( );
	}

	/**
	 * Testing purposes only. Used to print expression.
	 */
	public void printNode( CorePrinter output ) {

		if( getLeft() != null ) {
			getLeft().printNode( output );
		}
		output.formatPrintLn( " = " );
		if( getRight() != null ) {
			getRight().printNode( output );
		}
	}
}
