package org.unclazz.jp1ajs2.unitdef.parameter;

/**
 * ユニット定義パラメータsy（開始遅延時刻）とey（終了遅延時刻）の共通インターフェース.
 */
public interface DelayTime {
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
