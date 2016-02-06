package org.unclazz.jp1ajs2.unitdef.builder;

import org.unclazz.jp1ajs2.unitdef.ParameterValueQuery;
import org.unclazz.jp1ajs2.unitdef.ParameterValueType;
import org.unclazz.jp1ajs2.unitdef.ParameterValue;
import org.unclazz.jp1ajs2.unitdef.Tuple;
import org.unclazz.jp1ajs2.unitdef.util.CharSequential;

abstract class DefaultParameterValue implements ParameterValue, CharSequential {
	@Override
	public <R> R query(ParameterValueQuery<R> r) {
		return r.queryFrom(this);
	}
	@Override
	public String toString() {
		return toCharSequence().toString();
	}
	@Override
	public CharSequence toCharSequence() {
		final StringBuilder buff = new StringBuilder();
		if (getType() == ParameterValueType.TUPLE) {
			final Tuple t = getTuple();
			if (t instanceof CharSequential) {
				buff.append(((CharSequential) t).toCharSequence());
			} else {
				buff.append(t);
			}
		} else if (getType() == ParameterValueType.QUOTED) {
			buff.append(UnitdefUtils.quote(getRawCharSequence()));
		} else {
			buff.append(getRawCharSequence());
		}
		return buff;
	}
	@Override
	public boolean contentEquals(final CharSequence chars) {
		final CharSequence myChars = toCharSequence();
		final int myCharsLen = myChars.length();
		if (myCharsLen != chars.length()) {
			return false;
		}
		for (int i = 0; i < myCharsLen; i ++) {
			if (myChars.charAt(i) != chars.charAt(i)) {
				return false;
			}
		}
		return true;
	}
	@Override
	public boolean contentEquals(final ParameterValue value) {
		return contentEquals(value.getRawCharSequence());
	}
}
