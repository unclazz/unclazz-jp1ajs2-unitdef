package org.unclazz.jp1ajs2.unitdef.query;

import org.unclazz.jp1ajs2.unitdef.ParameterValue;
import org.unclazz.jp1ajs2.unitdef.util.CharSequenceUtils;

final class CastEscapedString implements Query<ParameterValue, String> {
	@Override
	public String queryFrom(ParameterValue t) {
		return CharSequenceUtils.escape(t.getStringValue()).toString();
	}
}
