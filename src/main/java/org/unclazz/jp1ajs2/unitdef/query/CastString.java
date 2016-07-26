package org.unclazz.jp1ajs2.unitdef.query;

import org.unclazz.jp1ajs2.unitdef.ParameterValue;

final class CastString implements Query<ParameterValue, String> {
	@Override
	public String queryFrom(ParameterValue t) {
		return t.getStringValue();
	}
}
