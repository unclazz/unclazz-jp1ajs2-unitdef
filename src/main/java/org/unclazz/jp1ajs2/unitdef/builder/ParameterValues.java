package org.unclazz.jp1ajs2.unitdef.builder;

import org.unclazz.jp1ajs2.unitdef.ParameterValue;
import org.unclazz.jp1ajs2.unitdef.Tuple;

public final class ParameterValues {
	private ParameterValues() {}
	
	public static ParameterValue charSequence(final CharSequence value) {
		return new CharSequenceParameterValue(value);
	}
	
	public static ParameterValue quoted(final CharSequence value) {
		return new QuotedParameterValue(value);
	}
	
	public static ParameterValue tuple(final Tuple value) {
		return new TupleParameterValue(value);
	}
}
