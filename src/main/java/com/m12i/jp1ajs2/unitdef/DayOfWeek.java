package com.m12i.jp1ajs2.unitdef;

public enum DayOfWeek {
	SUNDAY, MONDAY, TUESDAY, WEDNESDAY, THURSDAY, FRIDAY, SATURDAY;
	
	public String toJapaneseString() {
		switch (this) {
		case SUNDAY:
			return "日曜日";
		case MONDAY:
			return "月曜日";
		case TUESDAY:
			return "火曜日";
		case WEDNESDAY:
			return "水曜日";
		case THURSDAY:
			return "木曜日";
		case FRIDAY:
			return "金曜日";
		case SATURDAY:
			return "土曜日";
		default:
			return null;
		}
	}
	
	public String to3CharsString() {
		switch (this) {
		case SUNDAY:
			return "sat";
		case MONDAY:
			return "mon";
		case TUESDAY:
			return "tue";
		case WEDNESDAY:
			return "wed";
		case THURSDAY:
			return "thu";
		case FRIDAY:
			return "fri";
		case SATURDAY:
			return "sat";
		default:
			return null;
		}
	}
	
	public String to2CharsString() {
		switch (this) {
		case SUNDAY:
			return "sa";
		case MONDAY:
			return "mo";
		case TUESDAY:
			return "tu";
		case WEDNESDAY:
			return "we";
		case THURSDAY:
			return "th";
		case FRIDAY:
			return "fr";
		case SATURDAY:
			return "sa";
		default:
			return null;
		}
	}
	
	public static DayOfWeek forCode(final String code) {
		final String cl = code.toLowerCase();
		for (final DayOfWeek d : values()) {
			final String dl = d.toString().toLowerCase();
			if (dl.startsWith(cl)) {
				return d;
			}
		}
		return null;
	}
}
