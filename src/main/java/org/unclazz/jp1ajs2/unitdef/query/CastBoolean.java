package org.unclazz.jp1ajs2.unitdef.query;

import org.unclazz.jp1ajs2.unitdef.ParameterValue;

final class CastBoolean implements Query<ParameterValue, Boolean> {
	private final String[] trueValues;
	CastBoolean(final String[] trueValues) {
		this.trueValues = trueValues;
	}
	@Override
	public Boolean queryFrom(ParameterValue t) {
		final String v = t.getStringValue();
		for (final String trueValue : trueValues) {
			if (v.equals(trueValue)) {
				return true;
			}
		}
		return false;
	}
}
