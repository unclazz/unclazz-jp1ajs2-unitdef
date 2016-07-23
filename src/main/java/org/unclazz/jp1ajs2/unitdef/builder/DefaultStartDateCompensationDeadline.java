package org.unclazz.jp1ajs2.unitdef.builder;

import org.unclazz.jp1ajs2.unitdef.parameter.RuleNumber;
import org.unclazz.jp1ajs2.unitdef.parameter.StartDateCompensationDeadline;

final class DefaultStartDateCompensationDeadline implements StartDateCompensationDeadline {
	private final RuleNumber rn;
	private final int ds;
	
	DefaultStartDateCompensationDeadline(final RuleNumber rn, final int ds) {
		this.rn = rn;
		this.ds = ds;
	}
	
	@Override
	public RuleNumber getRuleNumber() {
		return rn;
	}

	@Override
	public int getDeadlineDays() {
		return ds;
	}
}
