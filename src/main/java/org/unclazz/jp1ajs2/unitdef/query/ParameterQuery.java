package org.unclazz.jp1ajs2.unitdef.query;

import org.unclazz.jp1ajs2.unitdef.Parameter;
import org.unclazz.jp1ajs2.unitdef.ParameterValue;
import org.unclazz.jp1ajs2.unitdef.Tuple;
import org.unclazz.jp1ajs2.unitdef.parameter.AnteroposteriorRelationship;
import org.unclazz.jp1ajs2.unitdef.parameter.CommandLine;
import org.unclazz.jp1ajs2.unitdef.parameter.DeleteOption;
import org.unclazz.jp1ajs2.unitdef.parameter.ElapsedTime;
import org.unclazz.jp1ajs2.unitdef.parameter.Element;
import org.unclazz.jp1ajs2.unitdef.parameter.EndDate;
import org.unclazz.jp1ajs2.unitdef.parameter.EndDelayTime;
import org.unclazz.jp1ajs2.unitdef.parameter.EndStatusJudgementType;
import org.unclazz.jp1ajs2.unitdef.parameter.ExecutionCycle;
import org.unclazz.jp1ajs2.unitdef.parameter.ExecutionTimedOutStatus;
import org.unclazz.jp1ajs2.unitdef.parameter.ExecutionUserType;
import org.unclazz.jp1ajs2.unitdef.parameter.ExitCodeThreshold;
import org.unclazz.jp1ajs2.unitdef.parameter.FileWatchCondition;
import org.unclazz.jp1ajs2.unitdef.parameter.FixedDuration;
import org.unclazz.jp1ajs2.unitdef.parameter.LinkedRuleNumber;
import org.unclazz.jp1ajs2.unitdef.parameter.MailAddress;
import org.unclazz.jp1ajs2.unitdef.parameter.MapSize;
import org.unclazz.jp1ajs2.unitdef.parameter.ResultJudgmentType;
import org.unclazz.jp1ajs2.unitdef.parameter.RunConditionWatchLimitCount;
import org.unclazz.jp1ajs2.unitdef.parameter.RunConditionWatchLimitTime;
import org.unclazz.jp1ajs2.unitdef.parameter.StartDate;
import org.unclazz.jp1ajs2.unitdef.parameter.StartDateAdjustment;
import org.unclazz.jp1ajs2.unitdef.parameter.StartDateCompensation;
import org.unclazz.jp1ajs2.unitdef.parameter.StartDateCompensationDeadline;
import org.unclazz.jp1ajs2.unitdef.parameter.StartDelayTime;
import org.unclazz.jp1ajs2.unitdef.parameter.StartTime;
import org.unclazz.jp1ajs2.unitdef.parameter.UnitType;
import org.unclazz.jp1ajs2.unitdef.parameter.WriteOption;
import org.unclazz.jp1ajs2.unitdef.util.UnsignedIntegral;

public interface ParameterQuery extends Query<Parameter, Parameter>,
ParameterConditionalModifier<ParameterQuery> {
	ValueIterableQuery values();
	ValueOneQuery valueAt(int i);
	
	Query<Parameter, AnteroposteriorRelationship> ar();
	Query<Parameter, StartDateAdjustment> cftd();
	Query<Parameter, CharSequence> cm();
	Query<Parameter, ExecutionCycle> cy();
	Query<Parameter, EndDate> ed();
	Query<Parameter, EndStatusJudgementType> ej();
	Query<Parameter, UnsignedIntegral> ejc();
	Query<Parameter, Element> el();
	Query<Parameter, ElapsedTime> etm();
	Query<Parameter, ExecutionTimedOutStatus> ets();
	Query<Parameter, ExecutionUserType> eu();
	Query<Parameter, EndDelayTime> ey();
	Query<Parameter, FixedDuration> fd();
	Query<Parameter, FileWatchCondition> flwc();
	Query<Parameter, ResultJudgmentType> jd();
	Query<Parameter, LinkedRuleNumber> ln();
	Query<Parameter, MailAddress> mladr();
	Query<Parameter, CommandLine> sc();
	Query<Parameter, StartDate> sd();
	Query<Parameter, WriteOption> sea();
	Query<Parameter, StartDateCompensation> sh();
	Query<Parameter, StartDateCompensationDeadline> shd();
	Query<Parameter, WriteOption> soa();
	Query<Parameter, StartTime> st();
	Query<Parameter, StartDelayTime> sy();
	Query<Parameter, MapSize> sz();
	Query<Parameter, CommandLine> te();
	Query<Parameter, ExitCodeThreshold> tho();
	Query<Parameter, ElapsedTime> tmitv();
	Query<Parameter, DeleteOption> top1();
	Query<Parameter, DeleteOption> top2();
	Query<Parameter, DeleteOption> top3();
	Query<Parameter, DeleteOption> top4();
	Query<Parameter, UnitType> ty();
	Query<Parameter, RunConditionWatchLimitCount> wc();
	Query<Parameter, RunConditionWatchLimitTime> wt();
	Query<Parameter, ExitCodeThreshold> wth();
	
	public static interface ValueIterableQuery 
	extends IterableQuery<Parameter, ParameterValue> {
		IterableQuery<Parameter, Integer> asInteger();
		IterableQuery<Parameter, Integer> asInteger(int defaultValue);
		IterableQuery<Parameter, String> asString();
		IterableQuery<Parameter, String> asQuotedString();
		IterableQuery<Parameter, String> asQuotedString(boolean forceQuote);
		IterableQuery<Parameter, Boolean> asBoolean(String... trueValues);
		IterableQuery<Parameter, Tuple> asTuple();
	}
	public static interface ValueOneQuery
	extends OneQuery<Parameter, ParameterValue>{
		OneQuery<Parameter, Integer> asInteger();
		OneQuery<Parameter, Integer> asInteger(int defaultValue);
		OneQuery<Parameter, String> asString();
		OneQuery<Parameter, String> asEscapedString();
		OneQuery<Parameter, String> asQuotedString();
		OneQuery<Parameter, String> asQuotedString(boolean forceQuote);
		OneQuery<Parameter, Boolean> asBoolean(String... trueValues);
		OneQuery<Parameter, Tuple> asTuple();
	}
}
