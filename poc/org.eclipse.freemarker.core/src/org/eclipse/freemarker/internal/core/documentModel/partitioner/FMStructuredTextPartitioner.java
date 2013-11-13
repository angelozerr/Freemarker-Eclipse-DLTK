package org.eclipse.freemarker.internal.core.documentModel.partitioner;

import org.eclipse.freemarker.internal.core.documentModel.parser.FMRegionContext;
import org.eclipse.jface.text.IDocumentPartitioner;
import org.eclipse.jface.text.ITypedRegion;
import org.eclipse.wst.html.core.internal.text.StructuredTextPartitionerForHTML;
import org.eclipse.wst.sse.core.internal.provisional.text.IStructuredDocumentRegion;
import org.eclipse.wst.sse.core.internal.provisional.text.ITextRegion;

public class FMStructuredTextPartitioner extends
		StructuredTextPartitionerForHTML {

	public String getContentType(final int offset,
			final boolean preferOpenPartitions) {
		final ITypedRegion partition = getPartition(offset);
		return partition == null ? null : partition.getType();
	}

	public String getPartitionType(final ITextRegion region, final int offset) {
		// if php region
		if (isPhpRegion(region.getType())) {
			return FMPartitionTypes.FM_DEFAULT;
		}

		// else do super
		return super.getPartitionType(region, offset);
	}

	/**
	 * to be abstract eventually
	 */
	protected void initLegalContentTypes() {
		super.initLegalContentTypes();

		final int length = fSupportedTypes.length;
		final String[] types = new String[fSupportedTypes.length + 1];

		System.arraycopy(fSupportedTypes, 0, types, 0, length);
		types[length] = FMPartitionTypes.FM_DEFAULT;

		fSupportedTypes = types;
	}

	/**
	 * @param regionType
	 * @return
	 */
	private static final boolean isPhpRegion(final String regionType) {
		return regionType == FMRegionContext.FM_DIRECTIVE_START;
				
				/*|| regionType == FMRegionContext.FM_CLOSE
				|| regionType == FMRegionContext.FM_CONTENT;*/
	}

	private final static String[] configuredContentTypes = new String[] {
			FMPartitionTypes.FM_DEFAULT
			/*,
			FMPartitionTypes.FM_SINGLE_LINE_COMMENT,
			FMPartitionTypes.FM_MULTI_LINE_COMMENT,
			FMPartitionTypes.FM_DOC, FMPartitionTypes.FM_QUOTED_STRING*/ };

	public static String[] getConfiguredContentTypes() {
		return configuredContentTypes;
	}

	public static boolean isFMPartitionType(final String type) {
		for (int i = 0; i < configuredContentTypes.length; i++)
			if (configuredContentTypes[i].equals(type))
				return true;
		return false;
	}

	public IDocumentPartitioner newInstance() {
		return new FMStructuredTextPartitioner();
	}

	public ITypedRegion getPartition(int offset) {

		// in case we are in the end of document
		// we return the partition of last region
		int docLength = fStructuredDocument.getLength();
		if (offset == docLength && offset > 0) {
			return super.getPartition(offset - 1);
		}
		ITypedRegion result = super.getPartition(offset);
		if (result.getType().equals(FMPartitionTypes.FM_DEFAULT)) {
			IStructuredDocumentRegion structuredDocumentRegion = fStructuredDocument
					.getRegionAtCharacterOffset(offset);
			if (structuredDocumentRegion.getStartOffset() == offset
					|| ((offset > 0 && structuredDocumentRegion
							.getStartOffset() == offset - 1))) {
				return super.getPartition(offset - 1);
			}
		}

		return result;
	}

	@Override
	public ITypedRegion[] computePartitioning(int offset, int length) {
		// workaround for https://bugs.eclipse.org/bugs/show_bug.cgi?id=268930
		ITypedRegion[] result = new ITypedRegion[0];
		try {
			result = super.computePartitioning(offset, length);
		} catch (NullPointerException e) {
		}
		return result;
	}
}
