package org.unclazz.jp1ajs2.unitdef.builder;

import org.unclazz.jp1ajs2.unitdef.parameter.RuleNumber;
import org.unclazz.jp1ajs2.unitdef.parameter.StartTime;
import org.unclazz.jp1ajs2.unitdef.parameter.Time;

final class DefaultStartTime implements StartTime {
	private final RuleNumber ruleNumber;
	private final boolean relative;
	private final Time time;
	
	DefaultStartTime(final RuleNumber ruleNo, final boolean relative, final Time time) {
		this.ruleNumber = ruleNo;
		this.relative = relative;
		this.time = time;
	}
	/**
	 * ルール番号.
	 * @return ルール番号
	 */
	public RuleNumber getRuleNumber() {
		return ruleNumber;
	}
	/**
	 * 相対時刻かどうか.
	 * @return {@code true}：相対時刻、{@code false}：絶対時刻
	 */
	public boolean isRelative() {
		return relative;
	}
	/**
	 * 時刻を取得する.
	 * @return 時刻
	 */
	public Time getTime() {
		return time;
	}
	@Override
	public String toString() {
		return String.format("ルール番号`%d` ジョブネットの実行開始時刻は`%s %d時%d分`",
				ruleNumber, relative ? "相対時刻" : "絶対時刻",
				time.getHour(), time.getMinute());
	}
}
