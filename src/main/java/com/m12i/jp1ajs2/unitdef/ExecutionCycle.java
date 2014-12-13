package com.m12i.jp1ajs2.unitdef;

public class ExecutionCycle {
	/**
	 * 処理サイクルの単位.
	 */
	public static enum CycleUnit {
		YEAR, MONTH, WEEK, DAY;
		public static CycleUnit forCode(final String code) {
			final String cl = code.toLowerCase();
			for (final CycleUnit u : values()) {
				final String ul = u.toString().toLowerCase();
				if (ul.startsWith(cl)) {
					return u;
				}
			}
			return null;
		}
	}
	
	private final int ruleNo;
	private final int interval;
	private final CycleUnit cycleUnit;
	
	ExecutionCycle (int ruleNo, int interval, CycleUnit cycleUnit) {
		this.ruleNo = ruleNo;
		this.interval = interval;
		this.cycleUnit = cycleUnit;
	}

	public int getRuleNo() {
		return ruleNo;
	}

	public int getInterval() {
		return interval;
	}

	public CycleUnit getCycleUnit() {
		return cycleUnit;
	}
}
