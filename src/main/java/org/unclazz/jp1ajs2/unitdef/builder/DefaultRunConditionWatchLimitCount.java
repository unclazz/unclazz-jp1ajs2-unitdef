package org.unclazz.jp1ajs2.unitdef.builder;

import org.unclazz.jp1ajs2.unitdef.parameter.RuleNumber;
import org.unclazz.jp1ajs2.unitdef.parameter.RunConditionWatchLimitCount;

final class DefaultRunConditionWatchLimitCount implements RunConditionWatchLimitCount {
	private final RuleNumber rn;
	private final int c;
	private final LimitationType lt;
	
	DefaultRunConditionWatchLimitCount(final RuleNumber rn, final int c, final LimitationType lt) {
		this.rn = rn;
		this.c = c;
		this.lt = lt;
	}
	
	@Override
	public RuleNumber getRuleNumber() {
		return rn;
	}

	@Override
	public int getCount() {
		return c;
	}

	@Override
	public LimitationType getType() {
		return lt;
	}
}
