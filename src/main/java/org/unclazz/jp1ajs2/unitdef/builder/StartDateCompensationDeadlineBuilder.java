package org.unclazz.jp1ajs2.unitdef.builder;

import org.unclazz.jp1ajs2.unitdef.parameter.RuleNumber;
import org.unclazz.jp1ajs2.unitdef.parameter.StartDateCompensationDeadline;

public final class StartDateCompensationDeadlineBuilder {
	private RuleNumber rn = RuleNumber.MIN;
	private int ds = 2;
	public StartDateCompensationDeadlineBuilder setRuleNumber(final RuleNumber rn) {
		this.rn = rn;
		return this;
	}
	public StartDateCompensationDeadlineBuilder setDeadlineDays(final int ds) {
		this.ds = ds;
		return this;
	}
	public StartDateCompensationDeadline build() {
		if (rn == null) {
			throw new NullPointerException("rule number must be not null.");
		}
		if (ds < 1 || 31 < ds) {
			throw new IllegalArgumentException("days must be between 1 and 31.");
		}
		return new DefaultStartDateCompensationDeadline(rn, ds);
	}
}
