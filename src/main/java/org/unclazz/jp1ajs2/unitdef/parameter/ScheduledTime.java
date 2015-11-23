package org.unclazz.jp1ajs2.unitdef.parameter;

/**
 * 開始/終了予定時刻（開始/終了遅延時刻）.
 */
public interface ScheduledTime {
	public static final int NONE_SPECIFIED = -1;
	public static enum TimingMethod {
		ABSOLUTE,
		RELATIVE_WITH_ROOT_START_TIME,
		RELATIVE_WITH_SUPER_START_TIME,
		RELATIVE_WITH_THEMSELF_START_TIME;
	}
	public RuleNumber getRuleNumber();
	public Time getTime();
	public TimingMethod getTimingMethod();
}
