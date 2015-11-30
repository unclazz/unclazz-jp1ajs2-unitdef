package org.unclazz.jp1ajs2.unitdef.parameter;

import java.util.Calendar;
import java.util.Date;

public final class MinutesInterval extends DefaultIntegral {
	public static MinutesInterval ofMinutes(final int minutes) {
		return new MinutesInterval(minutes);
	}
	public static MinutesInterval of(final int hours, final int minutes) {
		if (59 < minutes) {
			throw new IllegalArgumentException("Minutes must not be greater than 59");
		}
		return new MinutesInterval(hours * 60 + minutes);
	}
	
	private MinutesInterval(final int val) {
		super(val);
		if (val < 1 || 1440 < val) {
			throw new IllegalArgumentException("Interval must be between 1 and 1440");
		}
	}
	
	public int getHours() {
		return intValue() / 60;
	}
	
	public int getMinutes() {
		return intValue() % 60;
	}
	
	public Date toDate() {
		final Calendar cal = Calendar.getInstance();
		cal.clear();
		cal.set(Calendar.HOUR_OF_DAY, intValue() / 60);
		cal.set(Calendar.MINUTE, intValue() % 60);
		return cal.getTime();
	}
}
