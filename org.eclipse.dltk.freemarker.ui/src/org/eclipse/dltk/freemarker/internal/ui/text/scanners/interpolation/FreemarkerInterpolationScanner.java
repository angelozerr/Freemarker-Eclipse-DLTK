package org.eclipse.dltk.freemarker.internal.ui.text.scanners.interpolation;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.dltk.freemarker.internal.ui.text.FreemarkerWhitespaceDetector;
import org.eclipse.dltk.freemarker.internal.ui.text.IFreemarkerColorConstants;
import org.eclipse.dltk.freemarker.internal.ui.text.rules.interpolation.FTLEndInterpolationRule;
import org.eclipse.dltk.freemarker.internal.ui.text.rules.interpolation.FTLStartInterpolationRule;
import org.eclipse.dltk.freemarker.internal.ui.text.scanners.AbstractFreemarkerScanner;
import org.eclipse.dltk.ui.text.IColorManager;
import org.eclipse.jface.preference.IPreferenceStore;
import org.eclipse.jface.text.rules.IRule;
import org.eclipse.jface.text.rules.IToken;
import org.eclipse.jface.text.rules.WhitespaceRule;

public class FreemarkerInterpolationScanner extends AbstractFreemarkerScanner {

	private static final String[] fgTokenProperties = new String[] {
			IFreemarkerColorConstants.FREEMARKER_EXPRESSION,
			IFreemarkerColorConstants.FREEMARKER_INTERPOLATION };

	public FreemarkerInterpolationScanner(IColorManager manager,
			IPreferenceStore store) {
		super(null, manager, store);
		initialize();
	}

	@Override
	protected String[] getTokenProperties() {
		return fgTokenProperties;
	}

	@Override
	protected List<IRule> createRules() {
		List<IRule> rules = new ArrayList<IRule>();

		IToken expression = getToken(IFreemarkerColorConstants.FREEMARKER_EXPRESSION);
		IToken interpolation = getToken(IFreemarkerColorConstants.FREEMARKER_INTERPOLATION);

		// Add generic whitespace rule.
		rules.add(new WhitespaceRule(new FreemarkerWhitespaceDetector()));

		rules.add(new FTLStartInterpolationRule(interpolation));
		rules.add(new FTLEndInterpolationRule(interpolation));

		setDefaultReturnToken(expression);
		return rules;
	}
}
