package org.unclazz.jp1ajs2.unitdef.builder;

import org.unclazz.jp1ajs2.unitdef.parameter.DelayTime.TimingMethod;
import org.unclazz.jp1ajs2.unitdef.parameter.RuleNumber;
import org.unclazz.jp1ajs2.unitdef.parameter.StartDelayTime;
import org.unclazz.jp1ajs2.unitdef.parameter.Time;

public class StartScheduledTimeBuilder {
	StartScheduledTimeBuilder() {}
	
	private TimingMethod timingMethod = null;
	private Time time = null;
	private RuleNumber ruleNumber = RuleNumber.DEFAULT;
	
	public StartScheduledTimeBuilder setTimingMethod(TimingMethod timingMethod) {
		this.timingMethod = timingMethod;
		return this;
	}
	public StartScheduledTimeBuilder setRuleNumber(RuleNumber n) {
		this.ruleNumber = n;
		return this;
	}
	public StartScheduledTimeBuilder setRuleNumber(int ruleNumber) {
		this.ruleNumber = RuleNumber.of(ruleNumber);
		return this;
	}
	public StartScheduledTimeBuilder setTime(Time time) {
		this.time = time;
		return this;
	}
	public StartDelayTime build() {
		if (timingMethod == null) {
			throw new IllegalArgumentException("Timing method is not specified");
		}
		if (time == null) {
			throw new IllegalArgumentException("Time is not specified");
		}
		if (ruleNumber.intValue() < 1) {
			throw new IllegalArgumentException("Rule number must be greater than 0");
		}
		return new DefaultStartDelayTime(ruleNumber, time, timingMethod);
	}
}
