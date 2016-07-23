package org.unclazz.jp1ajs2.unitdef.builder;

import org.unclazz.jp1ajs2.unitdef.parameter.RuleNumber;
import org.unclazz.jp1ajs2.unitdef.parameter.StartDateCompensation;
import org.unclazz.jp1ajs2.unitdef.parameter.StartDateCompensation.CompensationMethod;

public final class StartDateCompensationBuilder {
	StartDateCompensationBuilder() {}
	
	private RuleNumber rn = RuleNumber.MIN;
	private CompensationMethod ct = null;
	
	public StartDateCompensationBuilder setRuleNumber(final RuleNumber rn) {
		this.rn = rn;
		return this;
	}
	public StartDateCompensationBuilder setMethod(final CompensationMethod ct) {
		this.ct = ct;
		return this;
	}
	public StartDateCompensation build() {
		if (ct == null) {
			throw new NullPointerException("compensation method must not be null.");
		}
		if (rn == null) {
			throw new NullPointerException("rule number must not be null.");
		}
		return new DefaultStartDateCompensation(rn, ct);
	}
}
