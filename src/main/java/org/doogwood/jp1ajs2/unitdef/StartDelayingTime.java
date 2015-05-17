package org.doogwood.jp1ajs2.unitdef;

/**
 * 開始遅延時刻.
 */
public final class StartDelayingTime {
	public static enum TimingMethod {
		ABSOLUTE,
		RELATIVE_WITH_ROOT_START_TIME,
		RELATIVE_WITH_SUPER_START_TIME,
		RELATIVE_WITH_THEMSELF_START_TIME;
	}
	
	private final int ruleNo;
	private final Integer hh;
	private final Integer mi;
	private final TimingMethod timingMethod;
	
	StartDelayingTime(
			final int ruleNo,
			final Integer hh,
			final Integer mi,
			final TimingMethod timingMethod) {
		this.ruleNo = ruleNo;
		this.hh = hh;
		this.mi = mi;
		this.timingMethod = timingMethod;
	}

	public int getRuleNo() {
		return ruleNo;
	}

	public int getHh() {
		return hh;
	}

	public int getMi() {
		return mi;
	}

	public TimingMethod getTimingMethod() {
		return timingMethod;
	}
	
	@Override
	public String toString() {
		final String timeExpl = 
				timingMethod == null 
					? (hh + "時" + mi + "分")
					: (timingMethod == TimingMethod.RELATIVE_WITH_ROOT_START_TIME
						? "ルートジョブネットの実行開始時刻からの相対値"
						: timingMethod == TimingMethod.RELATIVE_WITH_SUPER_START_TIME
							? "上位ジョブネットの実行開始時刻からの相対値"
							: "自ジョブネットの実行開始時刻からの相対値") + "で " + mi + "分"
					;
		return String.format("ルール番号`%s` ジョブネットの開始遅延時刻は`%s`",ruleNo, timeExpl);
	}
}
