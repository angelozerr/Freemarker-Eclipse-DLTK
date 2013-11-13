package org.eclipse.freemarker.internal.core.documentModel.parser;

import static org.junit.Assert.*;

import org.eclipse.wst.xml.core.internal.parser.XMLTokenizer;
import org.junit.Test;

public class JSPTokenizerTest {

	
	@Test
	public void testName() throws Exception {
		JSPTokenizer toker = new JSPTokenizer();
		//XMLTokenizer toker = new XMLTokenizer();
		toker.setCaseSensitiveBlocking(false);
		toker.reset(new java.io.StringReader(
				"<#assign >  <a></a></#assign>"));
		// toker.beginBlockMarkerScan("script", DOMRegionContext.BLOCK_TEXT);
		System.err.println(toker.getRegions());
	}
	
	@Test
	public void fmComment() throws Exception {
		JSPTokenizer toker = new JSPTokenizer();
		toker.setCaseSensitiveBlocking(false);
		toker.reset(new java.io.StringReader(
				"<#--<#assign >  <a></a></#assign>-->"));
		System.err.println(toker.getRegions());		
	}

	/*@Test
	public void fmCommentBracket() throws Exception {
		JSPTokenizer toker = new JSPTokenizer();
		toker.setCaseSensitiveBlocking(false);
		toker.reset(new java.io.StringReader(
				"[#--<#assign >  <a></a></#assign>--]"));
		System.err.println(toker.getRegions());		
	}*/
	
	@Test
	public void fmList() throws Exception {
		JSPTokenizer toker = new JSPTokenizer();
		toker.setCaseSensitiveBlocking(false);
		toker.reset(JSPTokenizerTest.class.getResourceAsStream("list.ftl"));
		System.err.println(toker.getRegions());		
	}
}
