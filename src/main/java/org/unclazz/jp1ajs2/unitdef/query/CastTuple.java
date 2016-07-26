package org.unclazz.jp1ajs2.unitdef.query;

import org.unclazz.jp1ajs2.unitdef.ParameterValue;
import org.unclazz.jp1ajs2.unitdef.ParameterValueType;
import org.unclazz.jp1ajs2.unitdef.Tuple;

final class CastTuple implements Query<ParameterValue, Tuple> {
	@Override
	public Tuple queryFrom(ParameterValue t) {
		if (t.getType() == ParameterValueType.TUPLE) {
			return t.getTuple();
		}
		throw new IllegalArgumentException("parameter must be tuple.");
	}
}
