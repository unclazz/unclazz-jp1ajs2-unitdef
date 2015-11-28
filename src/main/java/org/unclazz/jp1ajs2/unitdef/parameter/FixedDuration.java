package org.unclazz.jp1ajs2.unitdef.parameter;

public final class FixedDuration extends DefaultIntegral {
	public static FixedDuration ofMinutes(final int val) {
		return new FixedDuration(val * 60);
	}
	public static FixedDuration ofSeconds(final int val) {
		return new FixedDuration(val);
	}
	
	private FixedDuration(final int val) {
		super(val);
		if (val < 1 || 2879 < val) {
			throw new IllegalArgumentException("value of parameter 'fd' must be between 1 and 2879");
		}
	}
	public int toSeconds() {
		return intValue();
	}
	public int toMinutes() {
		return intValue() / 60;
	}
}
