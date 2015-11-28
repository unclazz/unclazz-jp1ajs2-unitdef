package org.unclazz.jp1ajs2.unitdef.builder;

import org.unclazz.jp1ajs2.unitdef.parameter.RuleNumber;
import org.unclazz.jp1ajs2.unitdef.parameter.StartTime;
import org.unclazz.jp1ajs2.unitdef.parameter.Time;

public final class StartTimeBuilder {
	StartTimeBuilder() {}
	
	private RuleNumber ruleNumber = RuleNumber.DEFAULT;
	private int hours = 0;
	private int minutes = 0;
	private boolean relative = false;
	
	public StartTimeBuilder setHours(RuleNumber n) {
		this.ruleNumber = n;
		return this;
	}
	public StartTimeBuilder setHours(int h) {
		this.hours = h;
		return this;
	}
	public StartTimeBuilder setMinutes(int m) {
		this.minutes = m;
		return this;
	}
	public StartTimeBuilder setTime(Time t) {
		this.hours = t.getHours();
		this.minutes = t.getMinutes();
		return this;
	}
	public StartTimeBuilder setRelative(boolean r) {
		this.relative = r;
		return this;
	}
	public StartTimeBuilder setRuleNumber(int n) {
		this.ruleNumber = RuleNumber.of(n);
		return this;
	}
	public StartTime build() {
		if (ruleNumber == null) {
			throw new NullPointerException("Rule number must be not null");
		}
		return new DefaultStartTime(ruleNumber, relative, Time.of(hours, minutes));
	}
}
