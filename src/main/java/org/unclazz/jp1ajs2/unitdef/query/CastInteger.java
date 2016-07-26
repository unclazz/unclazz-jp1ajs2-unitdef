package org.unclazz.jp1ajs2.unitdef.query;

import org.unclazz.jp1ajs2.unitdef.ParameterValue;

final class CastInteger implements Query<ParameterValue, Integer> {
	private final int defaultValue;
	private final boolean hasDefault;
	CastInteger(final int defaultValue) {
		this.defaultValue = defaultValue;
		this.hasDefault = true;
	}
	CastInteger() {
		this.defaultValue = -1;
		this.hasDefault = false;
	}
	@Override
	public Integer queryFrom(ParameterValue t) {
		try {
			return Integer.parseInt(t.getStringValue());
		} catch (final NumberFormatException e) {
			if (hasDefault) {
				return defaultValue;
			} else {
				throw e;
			}
		}
	}
}
