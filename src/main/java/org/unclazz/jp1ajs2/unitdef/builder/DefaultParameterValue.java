package org.unclazz.jp1ajs2.unitdef.builder;

import org.unclazz.jp1ajs2.unitdef.ParameterValueQuery;
import org.unclazz.jp1ajs2.unitdef.ParameterValueType;
import org.unclazz.jp1ajs2.unitdef.ParameterValue;
import org.unclazz.jp1ajs2.unitdef.Tuple;
import org.unclazz.jp1ajs2.unitdef.util.CharSequenceUtils;
import org.unclazz.jp1ajs2.unitdef.util.CharSequential;
import org.unclazz.jp1ajs2.unitdef.util.UnitdefUtils;

abstract class DefaultParameterValue implements ParameterValue {
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
	public boolean contentEquals(final CharSequence other) {
		return CharSequenceUtils.contentsAreEqual(toCharSequence(), other);
	}
	@Override
	public boolean contentEquals(final CharSequential value) {
		return contentEquals(value.toCharSequence());
	}
}
