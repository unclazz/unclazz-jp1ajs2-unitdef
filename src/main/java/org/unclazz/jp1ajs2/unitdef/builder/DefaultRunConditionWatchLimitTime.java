package org.unclazz.jp1ajs2.unitdef.builder;

import org.unclazz.jp1ajs2.unitdef.parameter.RuleNumber;
import org.unclazz.jp1ajs2.unitdef.parameter.RunConditionWatchLimitTime;
import org.unclazz.jp1ajs2.unitdef.parameter.Time;

final class DefaultRunConditionWatchLimitTime implements RunConditionWatchLimitTime {
	private final RuleNumber rn;
	private final Time t;
	private final LimitationType lt;
	
	DefaultRunConditionWatchLimitTime(final RuleNumber rn, final Time t, final LimitationType lt) {
		this.rn = rn;
		this.t = t;
		this.lt = lt;
	}
	
	@Override
	public RuleNumber getRuleNumber() {
		return rn;
	}

	@Override
	public LimitationType getType() {
		return lt;
	}

	@Override
	public Time getTime() {
		return t;
	}
}
