package org.unclazz.jp1ajs2.unitdef.builder;

import org.unclazz.jp1ajs2.unitdef.parameter.EndScheduledTime;
import org.unclazz.jp1ajs2.unitdef.parameter.RuleNumber;
import org.unclazz.jp1ajs2.unitdef.parameter.Time;

/**
 * 終了遅延時刻.
 */
final class DefaultEndScheduledTime implements EndScheduledTime {
	private final RuleNumber ruleNo;
	private final Time time;
	private final TimingMethod timingMethod;
	
	DefaultEndScheduledTime(
			final RuleNumber ruleNo,
			final Time time,
			final TimingMethod timingMethod) {
		this.ruleNo = ruleNo;
		this.time = time;
		this.timingMethod = timingMethod;
	}

	public RuleNumber getRuleNumber() {
		return ruleNo;
	}
	public Time getTime() {
		return time;
	}
	public TimingMethod getTimingMethod() {
		return timingMethod;
	}
	
	@Override
	public String toString() {
		final String timeExpl = 
				timingMethod == null 
					? (time.getHour() + "時" + time.getMinute() + "分")
					: (timingMethod == TimingMethod.RELATIVE_WITH_ROOT_START_TIME
						? "ルートジョブネットの実行開始時刻からの相対値"
						: timingMethod == TimingMethod.RELATIVE_WITH_SUPER_START_TIME
							? "上位ジョブネットの実行開始時刻からの相対値"
							: "自ジョブネットの実行開始時刻からの相対値") + "で " + time.getMinute() + "分"
					;
		return String.format("ルール番号`%s` ジョブネットの終了遅延時刻は`%s`",ruleNo, timeExpl);
	}
}
