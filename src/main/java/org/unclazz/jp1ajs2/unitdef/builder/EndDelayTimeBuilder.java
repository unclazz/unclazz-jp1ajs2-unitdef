package org.unclazz.jp1ajs2.unitdef.builder;

import org.unclazz.jp1ajs2.unitdef.parameter.EndDelayTime;
import org.unclazz.jp1ajs2.unitdef.parameter.RuleNumber;
import org.unclazz.jp1ajs2.unitdef.parameter.Time;
import org.unclazz.jp1ajs2.unitdef.parameter.DelayTime.TimingMethod;

public class EndDelayTimeBuilder {
	EndDelayTimeBuilder() {}
	
	private TimingMethod timingMethod = null;
	private Time time = null;
	private RuleNumber ruleNumber = RuleNumber.DEFAULT;
	
	public EndDelayTimeBuilder setTimingMethod(TimingMethod timingMethod) {
		this.timingMethod = timingMethod;
		return this;
	}
	public EndDelayTimeBuilder setRuleNumber(RuleNumber ruleNumber) {
		this.ruleNumber = ruleNumber;
		return this;
	}
	public EndDelayTimeBuilder setRuleNumber(int ruleNumber) {
		this.ruleNumber = RuleNumber.of(ruleNumber);
		return this;
	}
	public EndDelayTimeBuilder setTime(Time time) {
		this.time = time;
		return this;
	}
	public EndDelayTime build() {
		if (timingMethod == null) {
			throw new IllegalArgumentException("Timing method is not specified");
		}
		if (time == null) {
			throw new IllegalArgumentException("Time is not specified");
		}
		if (ruleNumber.intValue() < 1) {
			throw new IllegalArgumentException("Rule number must be greater than 0");
		}
		return new DefaultEndDelayTime(ruleNumber, time, timingMethod);
	}
}
