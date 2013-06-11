package org.eclipse.dltk.dbgp.debugger.packet.sender.response;

import org.eclipse.dltk.dbgp.DbgpRequest;
import org.eclipse.dltk.dbgp.debugger.IDbgpFeaturesProvider;

/**
 * DBGp XML Get Feature Packet. Example :
 * 
 * <pre>
 * <response command="feature_get"
 *           feature_name="feature_name"
 *           supported="0|1"
 *           transaction_id="transaction_id">
 *     feature setting or available options, such as a list of
 *     supported encodings
 * </response>
 * </pre>
 * 
 * @see specification at http://xdebug.org/docs-dbgp.php#feature-get
 */
public class FeatureGetPacket extends BaseFeaturePacket {

	private static final String SUPPORTED_ATTR = "supported";
	private static final String FEATURE_NAME_ATTR = "feature_name";

	public FeatureGetPacket(DbgpRequest command, IDbgpFeaturesProvider provider) {
		super(command);
		String featureName = super.getFeatureName();
		String value = provider.getDbgpFeature(featureName);
		addAttribute(FEATURE_NAME_ATTR, featureName);
		addAttribute(SUPPORTED_ATTR, (value != null ? ONE : ZERO));
		if (value != null) {
			setData(value);
		}
	}

}
