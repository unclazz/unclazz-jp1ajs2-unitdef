package org.unclazz.jp1ajs2.unitdef.builder;

import org.unclazz.jp1ajs2.unitdef.parameter.RuleNumber;
import org.unclazz.jp1ajs2.unitdef.parameter.StartDateAdjustment;

final class DefaultStartDateAdjustment implements StartDateAdjustment {
	private final RuleNumber rn;
	private final AdjustmentType t;
	private final int bd;
	private final int dd;
	
	DefaultStartDateAdjustment(final RuleNumber rn, final AdjustmentType t, final int bd, final int dd) {
		this.rn = rn;
		this.t= t;
		this.dd = dd;
		this.bd = bd;
	}

	@Override
	public RuleNumber getRuleNumber() {
		return rn;
	}
	@Override
	public AdjustmentType getType() {
		return t;
	}
	@Override
	public int getBusinessDays() {
		return bd;
	}
	@Override
	public int getDeadlineDays() {
		return dd;
	}
}
