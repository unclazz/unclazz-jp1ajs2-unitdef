package org.unclazz.jp1ajs2.unitdef.builder;

import org.unclazz.jp1ajs2.unitdef.parameter.RuleNumber;
import org.unclazz.jp1ajs2.unitdef.parameter.RunConditionWatchLimitCount;
import org.unclazz.jp1ajs2.unitdef.parameter.RunConditionWatchLimitCount.LimitationType;

public final class RunConditionWatchLimitCountBuilder {
	RunConditionWatchLimitCountBuilder() {}
	
	private RuleNumber rn = RuleNumber.MIN;
	private LimitationType lt = LimitationType.NO_WATCHING;
	private int c = 1;
	
	public RunConditionWatchLimitCountBuilder setRuleNumber(final RuleNumber rn) {
		this.rn = rn;
		return this;
	}
	public RunConditionWatchLimitCountBuilder setType(final LimitationType lt) {
		this.lt = lt;
		return this;
	}
	public RunConditionWatchLimitCountBuilder setCount(final int c) {
		this.c = c;
		return this;
	}
	public RunConditionWatchLimitCount build() {
		if (lt == null) {
			throw new NullPointerException("compensation method must not be null.");
		}
		if (rn == null) {
			throw new NullPointerException("rule number must not be null.");
		}
		if (c < 1 || 999 < c) {
			throw new IllegalArgumentException("count must be between 1 and 999.");
		}
		return new DefaultRunConditionWatchLimitCount(rn, c, lt);
	}
}
