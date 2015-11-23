package org.unclazz.jp1ajs2.unitdef.builder;

import org.unclazz.jp1ajs2.unitdef.ParameterValueType;
import org.unclazz.jp1ajs2.unitdef.Tuple;

final class TupleParameterValue extends DefaultParameterValue {
	private final Tuple tuple;
	TupleParameterValue(final Tuple tuple) {
		this.tuple = tuple;
	}
	@Override
	public CharSequence getRawCharSequence() {
		return tuple.toString();
	}
	@Override
	public Tuple getTuple() {
		return tuple;
	}
	@Override
	public ParameterValueType getType() {
		return ParameterValueType.TUPLE;
	}
}
