package org.unclazz.jp1ajs2.unitdef.builder;

import org.unclazz.jp1ajs2.unitdef.parameter.RuleNumber;
import org.unclazz.jp1ajs2.unitdef.parameter.StartDateCompensation;

final class DefaultStartDateCompensation implements StartDateCompensation {
	private final RuleNumber ruleNumber;
	private final CompensationMethod type;
	
	DefaultStartDateCompensation(final RuleNumber ruleNumber, final CompensationMethod type) {
		this.ruleNumber = ruleNumber;
		this.type = type;
	}
	
	@Override
	public RuleNumber getRuleNumber() {
		return ruleNumber;
	}
	@Override
	public CompensationMethod getMethod() {
		return type;
	}
}
