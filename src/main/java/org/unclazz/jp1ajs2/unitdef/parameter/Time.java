package org.unclazz.jp1ajs2.unitdef.parameter;

import java.util.Calendar;
import java.util.Date;

public final class Time implements Comparable<Time> {
	public static Time of(final int hours, final int minutes) {
		return new Time(hours, minutes);
	}
	public static final Time MIDNIGHT = new Time(0, 0);
	public static final Time NOON = new Time(12, 0);
	public static final Time MIN = MIDNIGHT;
	public static final Time MAX = new Time(47, 59);
	
	private final int hours;
	private final int minutes;
	
	private Time(int h, int m) {
		if (h < 0 || 47 < h) {
			throw new IllegalArgumentException("Hours must be between 0 and 47");
		}
		if (m < 0 || 59 < m) {
			throw new IllegalArgumentException("Minutes must be between 0 and 59");
		}
		this.hours = h;
		this.minutes = m;
	}
	
	public int getHours() {
		return hours;
	}
	public int getMinutes() {
		return minutes;
	}
	public Date toDate() {
		final Calendar cal = Calendar.getInstance();
		cal.clear();
		cal.set(Calendar.HOUR_OF_DAY, hours);
		cal.set(Calendar.MINUTE, minutes);
		return cal.getTime();
	}
	@Override
	public String toString() {
		return String.format("%02d:%02d", hours, minutes);
	}
	@Override
	public int compareTo(Time o) {
		final int hoursDelta = this.hours - o.hours;
		return hoursDelta == 0 ? this.minutes - o.minutes : hoursDelta;
	}
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + hours;
		result = prime * result + minutes;
		return result;
	}
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Time other = (Time) obj;
		if (hours != other.hours)
			return false;
		if (minutes != other.minutes)
			return false;
		return true;
	}
}
