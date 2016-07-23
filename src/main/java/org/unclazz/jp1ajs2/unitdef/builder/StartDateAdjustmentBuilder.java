package org.unclazz.jp1ajs2.unitdef.builder;

import org.unclazz.jp1ajs2.unitdef.parameter.RuleNumber;
import org.unclazz.jp1ajs2.unitdef.parameter.StartDateAdjustment;
import org.unclazz.jp1ajs2.unitdef.parameter.StartDateAdjustment.AdjustmentType;

public final class StartDateAdjustmentBuilder {
	StartDateAdjustmentBuilder() {}
	
	private RuleNumber rn = RuleNumber.MIN;
	private AdjustmentType t = null;
	private int bd = 1;
	private int dd = 10;
	
	public StartDateAdjustmentBuilder setRuleNumber(final RuleNumber rn) {
		this.rn = rn;
		return this;
	}
	public StartDateAdjustmentBuilder setAdjustmentType(final AdjustmentType t) {
		this.t= t;
		return this;
	}
	public StartDateAdjustmentBuilder setBusinessDays(final int bd) {
		this.bd = bd;
		return this;
	}
	public StartDateAdjustmentBuilder setDeadlineDays(final int dd) {
		this.dd = dd;
		return this;
	}
	public StartDateAdjustment build() {
		if (rn == null) {
			throw new NullPointerException("rule number must not be null.");
		}
		if (t == null) {
			throw new NullPointerException("adjustment type must not be null.");
		}
		if (bd < 1 || 31 < bd) {
			throw new IllegalArgumentException("business days must be between 1 and 31.");
		}
		if (dd < 1 || 31 < dd) {
			throw new IllegalArgumentException("deadline days must be between 1 and 31.");
		}
		return new DefaultStartDateAdjustment(rn, t, bd, dd);
	}
}
