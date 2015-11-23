package org.unclazz.jp1ajs2.unitdef.builder;

import org.unclazz.jp1ajs2.unitdef.parameter.ScheduledTime.TimingMethod;
import org.unclazz.jp1ajs2.unitdef.parameter.RuleNumber;
import org.unclazz.jp1ajs2.unitdef.parameter.ScheduledTime;
import org.unclazz.jp1ajs2.unitdef.parameter.StartScheduledTime;
import org.unclazz.jp1ajs2.unitdef.parameter.Time;

public class StartScheduledTimeBuilder {
	StartScheduledTimeBuilder() {}
	
	private TimingMethod timingMethod = null;
	private int hours = ScheduledTime.NONE_SPECIFIED;
	private int minutes = ScheduledTime.NONE_SPECIFIED;
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
	public StartScheduledTimeBuilder setHour(int hours) {
		this.hours = hours;
		return this;
	}
	public StartScheduledTimeBuilder setMinute(int minutes) {
		this.minutes = minutes;
		return this;
	}
	public StartScheduledTime build() {
		if (timingMethod == null) {
			throw new IllegalArgumentException("Timing method is not specified");
		}
		if (minutes <= ScheduledTime.NONE_SPECIFIED) {
			throw new IllegalArgumentException("Minutes is not specified");
		}
		if (timingMethod == TimingMethod.ABSOLUTE && hours <= ScheduledTime.NONE_SPECIFIED) {
			hours = 0;
		}
		if (hours < 1) {
			throw new IllegalArgumentException("Rule number must be greater than 0");
		}
		return new DefaultStartScheduledTime(ruleNumber, Time.of(hours, minutes), timingMethod);
	}
}
