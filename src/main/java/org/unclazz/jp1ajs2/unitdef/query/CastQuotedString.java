package org.unclazz.jp1ajs2.unitdef.query;

import org.unclazz.jp1ajs2.unitdef.ParameterValue;
import org.unclazz.jp1ajs2.unitdef.ParameterValueType;
import org.unclazz.jp1ajs2.unitdef.util.CharSequenceUtils;

final class CastQuotedString implements Query<ParameterValue, String> {
	private final boolean force;
	CastQuotedString(final boolean force) {
		this.force = force;
	}
	@Override
	public String queryFrom(ParameterValue t) {
		if (force || t.getType() == ParameterValueType.QUOTED_STRING) {
			return CharSequenceUtils.quote(t.getStringValue()).toString();
		} else {
			return t.getStringValue();
		}
	}
}
