package org.unclazz.jp1ajs2.unitdef.parameter;

public final class FixedDuration extends DefaultIntegral {
	public static FixedDuration of(final int minutes) {
		return new FixedDuration(minutes);
	}
	
	private FixedDuration(final int val) {
		super(val);
		if (val < 1) {
			throw new IllegalArgumentException("value of parameter 'fd' must be greater than 0");
		}
	}
	public int toSeconds() {
		return intValue() * 60;
	}
	public int toMinutes() {
		return intValue();
	}
}
