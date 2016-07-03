package org.unclazz.jp1ajs2.unitdef.builder;

import org.unclazz.jp1ajs2.unitdef.ParameterValueType;
import org.unclazz.jp1ajs2.unitdef.Component;
import org.unclazz.jp1ajs2.unitdef.ParameterValue;
import org.unclazz.jp1ajs2.unitdef.Tuple;
import org.unclazz.jp1ajs2.unitdef.query.ParameterValueQuery;
import org.unclazz.jp1ajs2.unitdef.util.CharSequenceUtils;

abstract class DefaultParameterValue implements ParameterValue {
	@Override
	public <R> R query(ParameterValueQuery<R> r) {
		return r.queryFrom(this);
	}
	@Override
	public String toString() {
		return serialize().toString();
	}
	@Override
	public CharSequence serialize() {
		final StringBuilder buff = CharSequenceUtils.builder();
		if (getType() == ParameterValueType.TUPLE) {
			final Tuple t = getTuple();
			if (t instanceof Component) {
				buff.append(((Component) t).serialize());
			} else {
				buff.append(t);
			}
		} else if (getType() == ParameterValueType.QUOTED_STRING) {
			buff.append(CharSequenceUtils.quote(getString()));
		} else {
			buff.append(getString());
		}
		return buff;
	}
	@Override
	public boolean contentEquals(final CharSequence other) {
		return CharSequenceUtils.contentsAreEqual(serialize(), other);
	}
	@Override
	public boolean contentEquals(final Component value) {
		return contentEquals(value.serialize());
	}
}
