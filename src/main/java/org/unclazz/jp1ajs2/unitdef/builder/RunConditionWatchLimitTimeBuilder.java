package org.unclazz.jp1ajs2.unitdef.builder;

import org.unclazz.jp1ajs2.unitdef.parameter.RuleNumber;
import org.unclazz.jp1ajs2.unitdef.parameter.RunConditionWatchLimitTime;
import org.unclazz.jp1ajs2.unitdef.parameter.RunConditionWatchLimitTime.LimitationType;
import org.unclazz.jp1ajs2.unitdef.parameter.Time;

public final class RunConditionWatchLimitTimeBuilder {
	RunConditionWatchLimitTimeBuilder() {}
	
	private RuleNumber rn = RuleNumber.MIN;
	private LimitationType lt = LimitationType.NO_WATCHING;
	private Time t = null;
	
	public RunConditionWatchLimitTimeBuilder setRuleNumber(final RuleNumber rn) {
		this.rn = rn;
		return this;
	}
	public RunConditionWatchLimitTimeBuilder setType(final LimitationType lt) {
		this.lt = lt;
		return this;
	}
	public RunConditionWatchLimitTimeBuilder setTime(final Time t) {
		this.t = t;
		return this;
	}
	public RunConditionWatchLimitTime build() {
		if (lt == null) {
			throw new NullPointerException("compensation method must not be null.");
		}
		if (rn == null) {
			throw new NullPointerException("rule number must not be null.");
		}
		if ((lt == LimitationType.ABSOLUTE_TIME || lt == LimitationType.RELATIVE_TIME) && t == null) {
			throw new IllegalArgumentException("time must not be null when limitation is enable.");
		}
		return new DefaultRunConditionWatchLimitTime(rn, t, lt);
	}
}
