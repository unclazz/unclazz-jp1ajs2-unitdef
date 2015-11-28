package org.unclazz.jp1ajs2.unitdef.builder;

import org.unclazz.jp1ajs2.unitdef.parameter.EndScheduledTime;
import org.unclazz.jp1ajs2.unitdef.parameter.RuleNumber;
import org.unclazz.jp1ajs2.unitdef.parameter.ScheduledTime;
import org.unclazz.jp1ajs2.unitdef.parameter.Time;
import org.unclazz.jp1ajs2.unitdef.parameter.ScheduledTime.TimingMethod;

public class EndScheduledTimeBuilder {
	EndScheduledTimeBuilder() {}
	
	private TimingMethod timingMethod = null;
	private int hours = ScheduledTime.NONE_SPECIFIED;
	private int minutes = ScheduledTime.NONE_SPECIFIED;
	private RuleNumber ruleNumber = RuleNumber.DEFAULT;
	
	public EndScheduledTimeBuilder setTimingMethod(TimingMethod timingMethod) {
		this.timingMethod = timingMethod;
		return this;
	}
	public EndScheduledTimeBuilder setRuleNumber(RuleNumber ruleNumber) {
		this.ruleNumber = ruleNumber;
		return this;
	}
	public EndScheduledTimeBuilder setRuleNumber(int ruleNumber) {
		this.ruleNumber = RuleNumber.of(ruleNumber);
		return this;
	}
	public EndScheduledTimeBuilder setTime(Time time) {
		this.hours = time.getHours();
		this.minutes = time.getMinutes();
		return this;
	}
	public EndScheduledTimeBuilder setHour(int hours) {
		this.hours = hours;
		return this;
	}
	public EndScheduledTimeBuilder setMinute(int minutes) {
		this.minutes = minutes;
		return this;
	}
	public EndScheduledTime build() {
		if (timingMethod == null) {
			throw new IllegalArgumentException("Timing method is not specified");
		}
		if (minutes <= ScheduledTime.NONE_SPECIFIED) {
			throw new IllegalArgumentException("Minutes is not specified");
		}
		if (timingMethod == TimingMethod.ABSOLUTE && hours <= ScheduledTime.NONE_SPECIFIED) {
			hours = 0;
		}
		return new DefaultEndScheduledTime(ruleNumber, Time.of(hours, minutes), timingMethod);
	}
}
