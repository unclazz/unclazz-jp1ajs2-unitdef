package org.unclazz.jp1ajs2.unitdef.parameter;

public final class ExitCodeThreshold extends DefaultIntegral {
	
	public static final ExitCodeThreshold of(final int val) {
		if (val < 0) {
			throw new IllegalArgumentException("Threshold value must be between 0 and 2147483647");
		}
		return new ExitCodeThreshold(val);
	}
	
	private ExitCodeThreshold(int val) {
		super(val);
	}
	
	public boolean isExceededWith(int val) {
		return intValue() < val;
	}
	public boolean isExceededWith(Integral other) {
		return compareTo(other) < 0;
	}
}
