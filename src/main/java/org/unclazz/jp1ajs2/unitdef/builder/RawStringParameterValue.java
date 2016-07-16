package org.unclazz.jp1ajs2.unitdef.builder;

import org.unclazz.jp1ajs2.unitdef.ParameterValueType;
import org.unclazz.jp1ajs2.unitdef.Tuple;

final class RawStringParameterValue extends DefaultParameterValue {
	private final CharSequence seq;
	private String cachedString = null;
	RawStringParameterValue(final CharSequence seq) {
		this.seq = seq;
	}
	@Override
	public String getStringValue() {
		if (cachedString == null) {
			cachedString = seq.toString();
		}
		return cachedString;
	}
	@Override
	public Tuple getTuple() {
		return Tuple.EMPTY_TUPLE;
	}
	@Override
	public ParameterValueType getType() {
		return ParameterValueType.RAW_STRING;
	}
}
