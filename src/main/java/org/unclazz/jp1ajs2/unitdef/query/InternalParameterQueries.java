package org.unclazz.jp1ajs2.unitdef.query;

import static org.unclazz.jp1ajs2.unitdef.query.Queries.*;

import java.util.Iterator;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.unclazz.jp1ajs2.unitdef.Parameter;
import org.unclazz.jp1ajs2.unitdef.ParameterValue;
import org.unclazz.jp1ajs2.unitdef.Tuple;
import org.unclazz.jp1ajs2.unitdef.builder.Builders;
import org.unclazz.jp1ajs2.unitdef.builder.ElementBuilder;
import org.unclazz.jp1ajs2.unitdef.builder.StartDateBuilder;
import org.unclazz.jp1ajs2.unitdef.parameter.AnteroposteriorRelationship;
import org.unclazz.jp1ajs2.unitdef.parameter.CommandLine;
import org.unclazz.jp1ajs2.unitdef.parameter.DayOfWeek;
import org.unclazz.jp1ajs2.unitdef.parameter.Element;
import org.unclazz.jp1ajs2.unitdef.parameter.EndDate;
import org.unclazz.jp1ajs2.unitdef.parameter.EndDelayTime;
import org.unclazz.jp1ajs2.unitdef.parameter.EndStatusJudgementType;
import org.unclazz.jp1ajs2.unitdef.parameter.ExecutionCycle;
import org.unclazz.jp1ajs2.unitdef.parameter.ExecutionCycle.CycleUnit;
import org.unclazz.jp1ajs2.unitdef.parameter.MailAddress.MailAddressType;
import org.unclazz.jp1ajs2.unitdef.parameter.ExecutionTimedOutStatus;
import org.unclazz.jp1ajs2.unitdef.parameter.ExecutionUserType;
import org.unclazz.jp1ajs2.unitdef.parameter.ExitCodeThreshold;
import org.unclazz.jp1ajs2.unitdef.parameter.FileWatchConditionFlag;
import org.unclazz.jp1ajs2.unitdef.parameter.FileWatchCondition;
import org.unclazz.jp1ajs2.unitdef.parameter.FixedDuration;
import org.unclazz.jp1ajs2.unitdef.parameter.LinkedRuleNumber;
import org.unclazz.jp1ajs2.unitdef.parameter.MailAddress;
import org.unclazz.jp1ajs2.unitdef.parameter.MapSize;
import org.unclazz.jp1ajs2.unitdef.parameter.ElapsedTime;
import org.unclazz.jp1ajs2.unitdef.parameter.ResultJudgmentType;
import org.unclazz.jp1ajs2.unitdef.parameter.RuleNumber;
import org.unclazz.jp1ajs2.unitdef.parameter.RunConditionWatchLimitCount;
import org.unclazz.jp1ajs2.unitdef.parameter.RunConditionWatchLimitTime;
import org.unclazz.jp1ajs2.unitdef.parameter.RunConditionWatchLimitTime.LimitationType;
import org.unclazz.jp1ajs2.unitdef.parameter.StartDate;
import org.unclazz.jp1ajs2.unitdef.parameter.StartDate.ByYearMonth.WithDayOfMonth;
import org.unclazz.jp1ajs2.unitdef.parameter.StartDate.CountingMethod;
import org.unclazz.jp1ajs2.unitdef.parameter.StartDate.DesignationMethod;
import org.unclazz.jp1ajs2.unitdef.parameter.StartDate.NumberOfWeek;
import org.unclazz.jp1ajs2.unitdef.parameter.StartDateAdjustment;
import org.unclazz.jp1ajs2.unitdef.parameter.StartDateAdjustment.AdjustmentType;
import org.unclazz.jp1ajs2.unitdef.parameter.StartDateCompensation;
import org.unclazz.jp1ajs2.unitdef.parameter.StartDateCompensation.CompensationMethod;
import org.unclazz.jp1ajs2.unitdef.parameter.StartDateCompensationDeadline;
import org.unclazz.jp1ajs2.unitdef.parameter.DelayTime;
import org.unclazz.jp1ajs2.unitdef.parameter.DeleteOption;
import org.unclazz.jp1ajs2.unitdef.parameter.StartDelayTime;
import org.unclazz.jp1ajs2.unitdef.parameter.StartTime;
import org.unclazz.jp1ajs2.unitdef.parameter.Time;
import org.unclazz.jp1ajs2.unitdef.parameter.UnitConnectionType;
import org.unclazz.jp1ajs2.unitdef.parameter.UnitType;
import org.unclazz.jp1ajs2.unitdef.parameter.WriteOption;
import org.unclazz.jp1ajs2.unitdef.util.StringUtils;
import org.unclazz.jp1ajs2.unitdef.util.UnsignedIntegral;

/**
 * ユニット定義パラメータを問合せ対象とするクエリのためのユーティリティ.
 */
final class InternalParameterQueries {
	private static final class ARQuery 
	implements Query<Parameter,AnteroposteriorRelationship> {
		@Override
		public AnteroposteriorRelationship queryFrom(final Parameter p) {
			final Tuple t = p.getValues().get(0).getTuple();
			return Builders
					.parameterAR()
					.setFromUnitName(t.get("f"))
					.setToUnitName(t.get("t"))
					.setConnectionType(t.size() > 2 
							? UnitConnectionType.valueOfCode(t.get(2).toString())
							: UnitConnectionType.SEQUENTIAL)
					.build();
		}
	}
	
	private static final class CFTDQuery 
	implements Query<Parameter,StartDateAdjustment> {
		@Override
		public StartDateAdjustment queryFrom(Parameter t) {
			final Iterator<ParameterValue> iter = t.getValues().iterator();
			final String val0 = iter.next().getStringValue();
			final String val1 = iter.hasNext() ? iter.next().getStringValue() : "";
			final String val2 = iter.hasNext() ? iter.next().getStringValue() : "";
			final String val3 = iter.hasNext() ? iter.next().getStringValue() : "";
			
			final RuleNumber rn;
			final AdjustmentType at;
			final int bd;
			final int dd;
			if (val0.charAt(0) == 'n') {
				return Builders.parameterCFTD()
						.setAdjustmentType(AdjustmentType.NOT_ADJUST)
						.build();
			} else if (val0.charAt(0) == 'b' || val0.charAt(0) == 'a' || val0.charAt(0) == 'n') {
				rn = RuleNumber.MIN;
				at = AdjustmentType.valueOfCode(val0);
				bd = val1.isEmpty() ? 1 : Integer.parseInt(val1);
				dd = val2.isEmpty() ? 10 : Integer.parseInt(val2);
			} else {
				rn = RuleNumber.of(Integer.parseInt(val0));
				at = AdjustmentType.valueOfCode(val1);
				bd = val2.isEmpty() ? 1 : Integer.parseInt(val2);
				dd = val3.isEmpty() ? 10 : Integer.parseInt(val3);
			}
			
			return Builders.parameterCFTD()
					.setRuleNumber(rn)
					.setAdjustmentType(at)
					.setBusinessDays(bd)
					.setDeadlineDays(dd)
					.build();
		}
	}
	private static final class CommandLineQuery 
	implements Query<Parameter,CommandLine> {
		@Override
		public CommandLine queryFrom(Parameter p) {
			return CommandLine.of(p.getValues().get(0).getStringValue());
		}
	}
	
	private static final class CYQuery 
	implements Query<Parameter,ExecutionCycle>{
		private final Query<Parameter, Parameter> q0 = parameter()
				.whenValueCount(1).thenPrepend("1");
		private final Query<Parameter, Integer> q1 = parameter()
				.valueAt(0).asInteger();
		private final Query<Parameter, Tuple> q2 = parameter()
				.valueAt(1).asTuple();
		@Override
		public ExecutionCycle queryFrom(Parameter p) {
			p = p.query(q0);
			final int ruleNumber = p.query(q1);
			final Tuple cycleNumberAndUnit = p.query(q2);
			final int cycleNumber = Integer.parseInt(cycleNumberAndUnit.get(0).toString());
			final CycleUnit cycleUnit = CycleUnit.valueOfCode(cycleNumberAndUnit.get(1));

			return ExecutionCycle.of(cycleNumber, cycleUnit).at(ruleNumber);
		}
	}
	
	private static final class DeleteOptionQuery 
	implements Query<Parameter,DeleteOption> {
		private static Query<Parameter, String> q =
				parameter().valueAt(0).asString();
		@Override
		public DeleteOption queryFrom(Parameter p) {
			return DeleteOption.valueOfCode(p.query(q));
		}
	}
	
	private static final class EDQuery implements Query<Parameter,EndDate> {
		private static final Pattern regex = Pattern.compile("(\\d+)/(\\d+)/(\\d+)");
		@Override
		public EndDate queryFrom(final Parameter t) {
			final Matcher m = regex.matcher(t.getValues().get(0).getStringValue());
			if (m.matches()) {
				return EndDate.of(Integer.parseInt(m.group(1)), 
						Integer.parseInt(m.group(2)), 
						Integer.parseInt(m.group(3)));
			}
			return null;
		}
	}
	
	private static final class EJCQuery implements Query<Parameter,UnsignedIntegral>{
		private static Query<Parameter, Long> q = 
				parameter().valueAt(0).asLong();
		@Override
		public UnsignedIntegral queryFrom(Parameter p) {
			return UnsignedIntegral.of(p.query(q));
		}
	}
	
	private static final class EJQuery implements Query<Parameter,EndStatusJudgementType>{
		@Override
		public EndStatusJudgementType queryFrom(Parameter p) {
			return EndStatusJudgementType.valueOfCode(p.getValues().get(0)
					.getStringValue().toString());
		}
	}
	
	private static final class ElapedTimeQuery 
	implements Query<Parameter,ElapsedTime> {
		private Query<Parameter, Integer> q = 
				parameter().valueAt(0).asInteger();
		@Override
		public ElapsedTime queryFrom(Parameter p) {
			return ElapsedTime.of(p.query(q));
		}
	}
	
	private static final class ElQuery 
	implements Query<Parameter,Element>{
		private static final Pattern patternForParamElValue3 = 
				Pattern.compile("^\\+(\\d+)\\s*\\+(\\d+)$");
		@Override
		public Element queryFrom(Parameter p) {
			final Iterator<ParameterValue> vals = p.getValues().iterator();
			final ElementBuilder builder = Builders
					.parameterEL()
					.setUnitName(vals.next().getStringValue().toString())
					.setUnitType(UnitType.valueOfCode(vals.next().
							getStringValue().toString()));
			
			final Matcher m = patternForParamElValue3.
					matcher(vals.next().getStringValue());
			
			if (!m.matches()) {
				throw new IllegalArgumentException("Invalid el parameter");
			}
			
			return builder
					.setHPixel(Integer.parseInt(m.group(1)))
					.setVPixel(Integer.parseInt(m.group(2)))
					.build();
		}
	}
	
	private static final class ETSQuery implements Query<Parameter,ExecutionTimedOutStatus>{
		private static final Query<Parameter,String> q = 
				parameter().valueAt(0).asString();
		@Override
		public ExecutionTimedOutStatus queryFrom(Parameter p) {
			return ExecutionTimedOutStatus.valueOfCode(p.query(q));
		}
	}
		
	private static final class EUQuery implements Query<Parameter,ExecutionUserType>{
		@Override
		public ExecutionUserType queryFrom(Parameter p) {
			return ExecutionUserType.valueOfCode(p.getValues().get(0).
					getStringValue().toString());
		}
	}
	
	private static final class ExitCodeThresholdQuery 
	implements Query<Parameter,ExitCodeThreshold>{
		private static Query<Parameter, Integer> q
		= parameter().valueAt(0).asInteger();
		@Override
		public ExitCodeThreshold queryFrom(Parameter p) {
			return ExitCodeThreshold.of(p.query(q));
		}
	}
	
	private static final class EYQuery implements Query<Parameter,EndDelayTime>{
		private static Query<Parameter, Parameter> q0 = parameter()
				.whenValueCount(1).thenPrepend("1");
		private static Query<Parameter, Integer> q1 = parameter()
				.valueAt(0).asInteger();
		private static Query<Parameter, String> q2 = parameter()
				.valueAt(1).asString();
		@Override
		public EndDelayTime queryFrom(Parameter p) {
			// ey=[N,]hh:mm|{M|U|C}mmmm;
			
			// パラメータ値の数をチェックし省略されたルール番号を補う
			p = p.query(q0);
			// 各パラメータ値の値を取得する
			final int ruleNumber = p.query(q1);
			final String timeMaybeRelative = p.query(q2);
			
			final char initial = timeMaybeRelative.charAt(0);
			final DelayTime.TimingMethod timingMethod;
			switch (initial) {
			case 'M':
				timingMethod = DelayTime.TimingMethod.RELATIVE_WITH_ROOT_START_TIME;
				break;
			case 'U':
				timingMethod = DelayTime.TimingMethod.RELATIVE_WITH_SUPER_START_TIME;
				break;
			case 'C':
				timingMethod = DelayTime.TimingMethod.RELATIVE_WITH_THEMSELF_START_TIME;
				break;
			default:
				timingMethod = DelayTime.TimingMethod.ABSOLUTE;
				break;
			}
			
			final Time time;
			if (timingMethod == DelayTime.TimingMethod.ABSOLUTE) {
				final String[] hhmm = timeMaybeRelative.toString().split(":");
				final int hh = Integer.parseInt(hhmm[0]);
				final int mm = Integer.parseInt(hhmm[1]);

				time = Time.of(hh, mm);
			} else {
				time = Time.ofMinutes(Integer.parseInt(
						timeMaybeRelative.subSequence(1, timeMaybeRelative.length())
						.toString()));
			}
			return Builders.parameterEY()
					.setRuleNumber(ruleNumber)
					.setTimingMethod(timingMethod)
					.setTime(time)
					.build();
		}
	}
	
	private static final class FDQuery implements Query<Parameter,FixedDuration>{
		private static final Query<Parameter, Integer> q = 
				parameter().valueAt(0).asInteger();
		@Override
		public FixedDuration queryFrom(Parameter p) {
			return FixedDuration.of(p.query(q));
		}
	}
	
	private static final class FLWCQuery implements Query<Parameter,FileWatchCondition>{
		@Override
		public FileWatchCondition queryFrom(Parameter p) {
			return FileWatchCondition.of(FileWatchConditionFlag
					.valueOfCodes(p.query(queryForCharSequence)));
		}
	}
	
	private static final class JDQuery implements Query<Parameter,ResultJudgmentType> {
		@Override
		public ResultJudgmentType queryFrom(Parameter p) {
			return ResultJudgmentType.valueOfCode(p.getValues().get(0).
					getStringValue().toString());
		}
	}
	
	private static final class LNQuery implements Query<Parameter,LinkedRuleNumber>{
		private static final Query<Parameter, Parameter> q0 = parameter()
				.whenValueCount(1).thenPrepend("1");
		private static final Query<Parameter, Integer> q1 = parameter()
				.valueAt(0).asInteger();
		private static final Query<Parameter, Integer> q2 = parameter()
				.valueAt(1).asInteger();
		@Override
		public LinkedRuleNumber queryFrom(Parameter p) {
			p = p.query(q0);
			final int ruleNumber = p.query(q1);
			final int targetRuleNumber = p.query(q2);
			return LinkedRuleNumber.ofTarget(targetRuleNumber).at(ruleNumber);
		}
	}
	
	private static final class MLADRQuery implements Query<Parameter,MailAddress>{
		private static final Pattern pat = Pattern.compile("^(to|cc|bcc):\"(.+)\"$");
		@Override
		public MailAddress queryFrom(Parameter p) {
			final Matcher mat = pat.matcher(p.getValues().get(0).getStringValue());
			if (mat.matches()) {
				final MailAddressType type = MailAddressType.valueOfCode(mat.group(1));
				final String address = StringUtils.unescape(mat.group(2)).toString();
				return new MailAddress(){
					@Override
					public String getAddress() {
						return address;
					}
					@Override
					public MailAddressType getType() {
						return type;
					}
				};
			}
			throw illegalArgument("Invalid mladr value (%s).", p.getValues().get(0));
		}
	}
	
	private static final class SDQuery implements Query<Parameter,StartDate> {
		private Query<Parameter, Parameter> q0 = parameter()
				.whenValueCount(1).thenPrepend("1");
		private Query<Parameter, Integer> q1 = parameter()
				.valueAt(0).asInteger();
		private Query<Parameter, String> q2 = parameter()
				.valueAt(1).asString();
		@Override
		public StartDate queryFrom(Parameter p) {
			// sd=[N,]{
			// 		[[yyyy/]mm/]{
			// 			[+|*|@]dd
			// 			|[+|*|@]b[-DD]
			// 			|[+]{su|mo|tu|we|th|fr|sa} [:{n|b}]
			// 		}
			// 		|en
			// 		|ud
			// 	};
			
			// パラメータ値の数をチェックして省略されているルール番号を補う
			p = p.query(q0);
			// 各パラメータ値を取得する
			final int ruleNumber = p.query(q1);
			final String maybeYyyyMm = p.query(q2).trim();
			
			final StartDateBuilder builder = Builders.parameterSD();
			builder.setRuleNumber(RuleNumber.of(ruleNumber));
			
			final char initial = maybeYyyyMm.charAt(0);
			if (initial == 'e' || initial == 'u') {
				final String enOrUd = maybeYyyyMm;
				if (enOrUd.equals("en")) {
					return builder
							.setDesignationMethod(DesignationMethod.ENTRY_DATE)
							.build();
				} else if (enOrUd.equals("ud")) {
					return builder
							.setDesignationMethod(DesignationMethod.UNDEFINED)
							.build();
				} else {
					throw new IllegalArgumentException(String.
							format("invalid sd parameter (%s).",
									p.serialize()));
				}
			}
			
			builder.setDesignationMethod(DesignationMethod.SCHEDULED_DATE);
			
			// sd=[N,]{
			// 		[[yyyy/]mm/]{
			// 			[+|*|@]dd
			// 			|[+|*|@]b[-DD]
			// 			|[+]{su|mo|tu|we|th|fr|sa} [:{n|b}]
			// 		}
			// 	};

			final Matcher yyyyMMddMatcher = Pattern.compile("^\\s*(?:(\\d{4})/)?(?:(\\d{1,2})/)\\s*").matcher(maybeYyyyMm);
			final String daysMaybePrefixed;
			if (yyyyMMddMatcher.find()){
				final String yyyy = yyyyMMddMatcher.group(1);
				final String mm = yyyyMMddMatcher.group(2);
				if (yyyy != null) {
					builder.setYear(Integer.parseInt(yyyy));
				}
				if (mm != null) {
					builder.setMonth(Integer.parseInt(mm));
				}
				daysMaybePrefixed = maybeYyyyMm.substring(yyyyMMddMatcher.end());
			} else {
				daysMaybePrefixed = maybeYyyyMm.trim();
			}
			
			final char daysPrefix = daysMaybePrefixed.charAt(0);
			final String days;
			final CountingMethod countingMethod;
			final boolean byDayOfWeek;
			if (daysPrefix == '+') {
				days = daysMaybePrefixed.substring(1);
				byDayOfWeek = 'f' <= days.charAt(0) && days.charAt(0) <= 'w';
			} else if (daysPrefix == '@') {
				days = daysMaybePrefixed.substring(1);
				byDayOfWeek = false;
			} else if (daysPrefix == '*') {
				days = daysMaybePrefixed.substring(1);
				byDayOfWeek = false;
			} else {
				days = daysMaybePrefixed;
				byDayOfWeek = 'f' <= days.charAt(0) && days.charAt(0) <= 'w';
			}
			if (byDayOfWeek) {
				countingMethod = null;
			} else {
				if (daysPrefix == '+') {
					countingMethod = CountingMethod.RELATIVE;
				} else if (daysPrefix == '@') {
					countingMethod = CountingMethod.NON_BUSINESS_DAY;
				} else if (daysPrefix == '*') {
					countingMethod = CountingMethod.BUSINESS_DAY;
				} else {
					countingMethod = CountingMethod.ABSOLUTE;
				}
			}
			
			if (byDayOfWeek) {
				// sd=[N,]{
				// 		[[yyyy/]mm/]{
				// 			[+]{su|mo|tu|we|th|fr|sa} [:{n|b}]
				// 		}
				// 	};
				final char last = days.charAt(days.length() - 1);
				final boolean hasNumberOfWeek = '0' <= last && last <= '9';
				final String dayOfWeekCode = days.split("[^a-z]")[0];
				if (hasNumberOfWeek) {
					builder.setNumberOfWeek(NumberOfWeek.of("0123456789".indexOf(last)));
				} else if (last == 'b') {
					builder.setNumberOfWeek(NumberOfWeek.LAST_WEEK);
				} else {
					builder.setNumberOfWeek(NumberOfWeek.NOT_SPECIFIED);
				}
				return builder
					.setBackward(last == 'b')
					.setDayOfWeek(DayOfWeek.valueOfCode(dayOfWeekCode))
					.setRelativeNumberOfWeek(daysPrefix == '+')
					.build();
			}
			// sd=[N,]{
			// 		[[yyyy/]mm/]{
			// 			[+|*|@]dd
			// 			|[+|*|@]b[-DD]
			// 		}
			// 	};
			builder.setCountingMethod(countingMethod);
			if (days.charAt(0) == 'b') {
				builder.setBackward(true);
				if (days.indexOf('-') == -1) {
					builder.setDay(WithDayOfMonth.LAST_DAY);
				} else {
					builder.setDay(Integer.parseInt(days.split("-")[1]));
				}
			} else {
				builder
				.setBackward(false)
				.setDay(Integer.parseInt(days));
			}
			return builder.build();
		}
	}
	
	private static final class SHDQuery implements 
	Query<Parameter,StartDateCompensationDeadline> {
		@Override
		public StartDateCompensationDeadline queryFrom(Parameter t) {
			final int valueCount = t.getValues().size();
			final int ruleNumber;
			final int days;
			
			if (valueCount == 1) {
				ruleNumber = 1;
				days = Integer.parseInt(t.getValues().get(0).getStringValue());
			} else {
				ruleNumber = Integer.parseInt(t.getValues().get(0).getStringValue());
				days = Integer.parseInt(t.getValues().get(1).getStringValue());
			}

			return Builders.parameterSHD()
					.setRuleNumber(RuleNumber.of(ruleNumber))
					.setDeadlineDays(days).build();
		}
	}
	
	private static final class SHQuery implements Query<Parameter,StartDateCompensation> {
		private static final Query<Parameter, Parameter> q0 = parameter()
				.whenValueCount(1).thenPrepend("1");
		private static final Query<Parameter, Integer> q1 = parameter()
				.valueAt(0).asInteger();
		private static final Query<Parameter, String> q2 = parameter()
				.valueAt(1).asString();
		@Override
		public StartDateCompensation queryFrom(Parameter t) {
			// パラメータ値の数をチェックして省略されたツール番号を補完
			t = t.query(q0);
			// 各パラメータ値を取得
			final int ruleNumber = t.query(q1);
			final String typeCode = t.query(q2);

			return Builders.parameterSH()
					.setRuleNumber(RuleNumber.of(ruleNumber))
					.setMethod(CompensationMethod.valueOfCode(typeCode))
					.build();
		}
	}
	private static final class STQuery implements Query<Parameter,StartTime>{
		@Override
		public StartTime queryFrom(Parameter p) {
			// st=[N,][+]hh:mm;
			
			// ルール番号の決定
			final int valueCount = p.getValues().size();
			final int ruleNumber;
			if (valueCount == 1) {
				// パラメータの値が1つしかない（＝ルール番号の表記がない）ならルール番号は1
				ruleNumber = 1;
			} else {
				// そうでない場合は先頭の値を整数値として読み取る
				ruleNumber = Integer.parseInt(p.getValues().get(0).toString());
			}
			
			// 相対時刻指定かどうかの決定
			final CharSequence timeMaybePrefixed = p.
					getValues().get(valueCount == 1 ? 0 : 1).getStringValue();
			final boolean relative = timeMaybePrefixed.charAt(0) == '+';
			
			// 時刻の決定
			final String[] hhmm = timeMaybePrefixed
					.subSequence(relative ? 1 : 0, timeMaybePrefixed.length())
					.toString()
					.split(":");
			final int hh = Integer.parseInt(hhmm[0]);
			final int mm = Integer.parseInt(hhmm[1]);
			
			// VOの組み立て
			return Builders.parameterST()
					.setRuleNumber(ruleNumber)
					.setRelative(relative)
					.setHours(hh)
					.setMinutes(mm)
					.build();
		}
	}
	private static final class SYQuery implements Query<Parameter,StartDelayTime>{
		@Override
		public StartDelayTime queryFrom(Parameter p) {
			// sy=[N,]hh:mm|{M|U|C}mmmm;
			
			final int valueCount = p.getValues().size();
			final int ruleNumber;
			if (valueCount == 1) {
				ruleNumber = 1;
			} else {
				ruleNumber = Integer.parseInt(p.getValues().get(0).toString());
			}
			
			final CharSequence timeMaybeRelative = p
					.getValues().get(valueCount == 1 ? 0 : 1).getStringValue();
			final char initial = timeMaybeRelative.charAt(0);
			
			final DelayTime.TimingMethod timingMethod;
			switch (initial) {
			case 'M':
				timingMethod = DelayTime.TimingMethod.RELATIVE_WITH_ROOT_START_TIME;
				break;
			case 'U':
				timingMethod = DelayTime.TimingMethod.RELATIVE_WITH_SUPER_START_TIME;
				break;
			case 'C':
				timingMethod = DelayTime.TimingMethod.RELATIVE_WITH_THEMSELF_START_TIME;
				break;
			default:
				timingMethod = DelayTime.TimingMethod.ABSOLUTE;
				break;
			}
			
			final Time time;
			if (timingMethod == DelayTime.TimingMethod.ABSOLUTE) {
				final String[] hhmm = timeMaybeRelative.toString().split(":");
				final int hh = Integer.parseInt(hhmm[0]);
				final int mm = Integer.parseInt(hhmm[1]);

				time = Time.of(hh, mm);
			} else {
				time = Time.ofMinutes(Integer.parseInt(
						timeMaybeRelative.subSequence(1, timeMaybeRelative.length())
						.toString()));
			}
			return Builders.parameterSY()
					.setRuleNumber(ruleNumber)
					.setTimingMethod(timingMethod)
					.setTime(time)
					.build();
		}
	}
	
	private static final class SZQuery implements Query<Parameter,MapSize> {
		private final static Pattern patternForParamSzValue = 
				Pattern.compile("^(\\d+)[^\\d]+(\\d+)$");
		@Override
		public MapSize queryFrom(Parameter p) {
			final Matcher m = patternForParamSzValue
					.matcher(p.getValues().get(0).getStringValue());
			if (m.matches()) {
				final int w = Integer.parseInt(m.group(1));
				final int h = Integer.parseInt(m.group(2));
				return MapSize.of(w, h);
			} else {
				throw illegalArgument("Invalid sz parameter (%s)", p);
			}
		}
	}
	
	private static final class TYQuery implements Query<Parameter,UnitType> {
		@Override
		public UnitType queryFrom(Parameter p) {
			return UnitType.valueOfCode(p.getValues().get(0)
					.getStringValue().toString());
		}
	}
	
	private static final class Value1AsCharSequenceQuery 
	implements Query<Parameter,CharSequence> {
		@Override
		public CharSequence queryFrom(Parameter p) {
			return p.getValues().get(0).getStringValue();
		}
	}
	private static final class WCQuery implements 
	Query<Parameter,RunConditionWatchLimitCount> {
		@Override
		public RunConditionWatchLimitCount queryFrom(Parameter t) {
			final int valueCount = t.getValues().size();
			final int ruleNumber;
			final String limit;
			
			if (valueCount == 1) {
				ruleNumber = 1;
				limit = t.getValues().get(0).getStringValue();
			} else {
				ruleNumber = Integer.parseInt(t.getValues().get(0).getStringValue());
				limit = t.getValues().get(1).getStringValue();
			}
			
			if (limit.equals("un")) {
				return Builders.parameterWC()
						.setRuleNumber(RuleNumber.of(ruleNumber))
						.setType(RunConditionWatchLimitCount
						.LimitationType.UNLIMITTED).build();
			} else if (limit.equals("no")) {
				return Builders.parameterWC()
						.setRuleNumber(RuleNumber.of(ruleNumber))
						.setType(RunConditionWatchLimitCount
						.LimitationType.NO_WATCHING).build();
			}

			return Builders.parameterWC()
					.setRuleNumber(RuleNumber.of(ruleNumber))
					.setCount(Integer.parseInt(limit))
					.setType(RunConditionWatchLimitCount
					.LimitationType.LIMITTED).build();
		}
	}
	private static final class WriteOptionQuery 
	implements Query<Parameter,WriteOption> {
		private Query<Parameter, String> q = 
				parameter().valueAt(0).asString();
		@Override
		public WriteOption queryFrom(Parameter p) {
			return WriteOption.valueOfCode(p.query(q));
		}
	}
	private static final class WTQuery implements Query<Parameter,RunConditionWatchLimitTime>{
		private Query<Parameter,Parameter> q0 = parameter()
				.whenValueCount(1).thenPrepend("1");
		@Override
		public RunConditionWatchLimitTime queryFrom(Parameter t) {
			t = t.query(q0);
			final int ruleNumber = Integer.parseInt(t.getValues().get(0).getStringValue());
			final String limit = t.getValues().get(1).getStringValue();
			
			if (limit.equals("un")) {
				return Builders.parameterWT()
						.setRuleNumber(RuleNumber.of(ruleNumber))
						.setType(RunConditionWatchLimitTime
						.LimitationType.UNLIMITTED).build();
			} else if (limit.equals("no")) {
				return Builders.parameterWT()
						.setRuleNumber(RuleNumber.of(ruleNumber))
						.setType(RunConditionWatchLimitTime
						.LimitationType.NO_WATCHING).build();
			}
			
			final int indexOfColon = limit.indexOf(':');
			final Time time = indexOfColon == -1 ? Time.ofMinutes(Integer.parseInt(limit))
					: Time.of(Integer.parseInt(limit.substring(0, indexOfColon)), 
							Integer.parseInt(limit.substring(indexOfColon + 1)));
			final LimitationType type = indexOfColon == -1 ? 
					LimitationType.RELATIVE_TIME : LimitationType.ABSOLUTE_TIME;
			
			return Builders.parameterWT()
					.setRuleNumber(RuleNumber.of(ruleNumber))
					.setType(type)
					.setTime(time).build();
		}
	}
	/**
	 * ユニット定義パラメータscとteのためのクエリ.
	 */
	private static final Query<Parameter,CommandLine> queryForCommandLine =
			new CommandLineQuery();
	
	/**
	 * 任意のユニット定義パラメータの第1値を読み取って{@code CharSequence}として返すクエリ.
	 */
	private static final Query<Parameter,CharSequence> queryForCharSequence =
			new Value1AsCharSequenceQuery();
	
	/**
	 * ユニット定義パラメータtop1などのためのクエリ.
	 */
	private static final Query<Parameter,DeleteOption> queryForDeleteOption =
			new DeleteOptionQuery();
	
	/**
	 * ユニット定義パラメータthoなどのためのクエリ.
	 */
	private static final Query<Parameter,ExitCodeThreshold> queryForExitCodeThreshold =
			new ExitCodeThresholdQuery();
	
	/**
	 * ユニット定義パラメータtmitvなどのためのクエリ.
	 */
	private static final Query<Parameter,ElapsedTime> queryForMinutesInterval =
			new ElapedTimeQuery();
	
	/**
	 * ユニット定義パラメータsoaなどのためのクエリ.
	 */
	private static final Query<Parameter,WriteOption> queryForWriteOption =
			new WriteOptionQuery();

	/**
	 * ユニット定義パラメータarを読み取ってそのJavaオブジェクト表現を返すクエリ.
	 */
	public static final Query<Parameter,AnteroposteriorRelationship> AR = new ARQuery();
	
	/**
	 * ユニット定義パラメータcmを読み取ってそのJavaオブジェクト表現を返すクエリ.
	 */
	public static final Query<Parameter,CharSequence> CM = queryForCharSequence;
	
	/**
	 * ユニット定義パラメータcyを読み取ってそのJavaオブジェクト表現を返すクエリ.
	 */
	public static final Query<Parameter,ExecutionCycle> CY = new CYQuery();
	
	/**
	 * ユニット定義パラメータelを読み取ってそのJavaオブジェクト表現を返すクエリ.
	 */
	public static final Query<Parameter,Element> EL = new ElQuery();
	
	/**
	 * ユニット定義パラメータetsを読み取ってそのJavaオブジェクト表現を返すクエリ.
	 */
	public static final Query<Parameter,ExecutionTimedOutStatus> ETS = new ETSQuery();
	
	/**
	 * ユニット定義パラメータeuを読み取ってそのJavaオブジェクト表現を返すクエリ.
	 */
	public static final Query<Parameter,ExecutionUserType> EU = new EUQuery();
	
	/**
	 * ユニット定義パラメータfdを読み取ってそのJavaオブジェクト表現を返すクエリ.
	 */
	public static final Query<Parameter,FixedDuration> FD = new FDQuery();
	
	/**
	 * ユニット定義パラメータflwcを読み取ってそのJavaオブジェクト表現を返すクエリ.
	 */
	public static final Query<Parameter,FileWatchCondition> FLWC = new FLWCQuery();
	
	/**
	 * ユニット定義パラメータjdを読み取ってそのJavaオブジェクト表現を返すクエリ.
	 */
	public static final Query<Parameter,ResultJudgmentType> JD = new JDQuery();
	
	/**
	 * ユニット定義パラメータlnを読み取ってそのJavaオブジェクト表現を返すクエリ.
	 */
	public static final Query<Parameter,LinkedRuleNumber> LN = new LNQuery();
	
	/**
	 * ユニット定義パラメータscを読み取ってそのJavaオブジェクト表現を返すクエリ.
	 */
	public static final Query<Parameter,CommandLine> SC = queryForCommandLine;
	
	/**
	 * ユニット定義パラメータsdを読み取ってそのJavaオブジェクト表現を返すクエリ.
	 */
	public static final Query<Parameter,StartDate> SD = new SDQuery();	
	/**
	 * ユニット定義パラメータsoaを読み取ってそのJavaオブジェクト表現を返すクエリ.
	 */
	public static final Query<Parameter,WriteOption> SOA = queryForWriteOption;
	
	/**
	 * ユニット定義パラメータseaを読み取ってそのJavaオブジェクト表現を返すクエリ.
	 */
	public static final Query<Parameter,WriteOption> SEA = queryForWriteOption;
	
	/**
	 * ユニット定義パラメータstを読み取ってそのJavaオブジェクト表現を返すクエリ.
	 */
	public static final Query<Parameter,StartTime> ST = new STQuery();

	/**
	 * ユニット定義パラメータshを読み取ってそのJavaオブジェクト表現を返すクエリ.
	 */
	public static final Query<Parameter,StartDateCompensation> SH = new SHQuery();
	
	/**
	 * ユニット定義パラメータshdを読み取ってそのJavaオブジェクト表現を返すクエリ.
	 */
	public static final Query<Parameter,StartDateCompensationDeadline> SHD = new SHDQuery();
	
	/**
	 * ユニット定義パラメータwtを読み取ってそのJavaオブジェクト表現を返すクエリ.
	 */
	public static final Query<Parameter,RunConditionWatchLimitTime> WT = new WTQuery();
	
	/**
	 * ユニット定義パラメータcftdを読み取ってそのJavaオブジェクト表現を返すクエリ.
	 */
	public static final Query<Parameter,StartDateAdjustment> CFTD = new CFTDQuery();
	
	public static final Query<Parameter,EndDate> ED = new EDQuery();
	
	public static final Query<Parameter,RunConditionWatchLimitCount> WC = new WCQuery();
	
	/**
	 * ユニット定義パラメータsyを読み取ってそのJavaオブジェクト表現を返すクエリ.
	 */
	public static final Query<Parameter,StartDelayTime> SY = new SYQuery();

	/**
	 * ユニット定義パラメータeyを読み取ってそのJavaオブジェクト表現を返すクエリ.
	 */
	public static final Query<Parameter,EndDelayTime> EY = new EYQuery();
	
	/**
	 * ユニット定義パラメータszを読み取ってそのJavaオブジェクト表現を返すクエリ.
	 */
	public static final Query<Parameter,MapSize> SZ = new SZQuery();
	
	/**
	 * ユニット定義パラメータteを読み取ってそのJavaオブジェクト表現を返すクエリ.
	 */
	public static final Query<Parameter,CommandLine> TE = queryForCommandLine;
	
	/**
	 * ユニット定義パラメータthoを読み取ってそのJavaオブジェクト表現を返すクエリ.
	 */
	public static final Query<Parameter,ExitCodeThreshold> THO = queryForExitCodeThreshold;
	
	/**
	 * ユニット定義パラメータtmivを読み取ってそのJavaオブジェクト表現を返すクエリ.
	 */
	public static final Query<Parameter,ElapsedTime> TMITV = queryForMinutesInterval;
	
	/**
	 * ユニット定義パラメータtop1を読み取ってそのJavaオブジェクト表現を返すクエリ.
	 */
	public static final Query<Parameter,DeleteOption> TOP1 = queryForDeleteOption;
	
	/**
	 * ユニット定義パラメータtop2を読み取ってそのJavaオブジェクト表現を返すクエリ.
	 */
	public static final Query<Parameter,DeleteOption> TOP2 = queryForDeleteOption;
	
	/**
	 * ユニット定義パラメータtop3を読み取ってそのJavaオブジェクト表現を返すクエリ.
	 */
	public static final Query<Parameter,DeleteOption> TOP3 = queryForDeleteOption;
	
	/**
	 * ユニット定義パラメータtop4を読み取ってそのJavaオブジェクト表現を返すクエリ.
	 */
	public static final Query<Parameter,DeleteOption> TOP4 = queryForDeleteOption;
	
	/**
	 * ユニット定義パラメータtyを読み取ってそのJavaオブジェクト表現を返すクエリ.
	 */
	public static final Query<Parameter,UnitType> TY = new TYQuery();
	
	/**
	 * ユニット定義パラメータwthを読み取ってそのJavaオブジェクト表現を返すクエリ.
	 */
	public static final Query<Parameter,ExitCodeThreshold> WTH = queryForExitCodeThreshold;
	
	/**
	 * ユニット定義パラメータetmを読み取ってそのJavaオブジェクト表現を返すクエリ.
	 */
	public static final Query<Parameter,ElapsedTime> ETM = queryForMinutesInterval;
	
	/**
	 * ユニット定義パラメータejを読み取ってそのJavaオブジェクト表現を返すクエリ.
	 */
	public static final Query<Parameter,EndStatusJudgementType> EJ =new EJQuery();

	/**
	 * ユニット定義パラメータejcを読み取ってそのJavaオブジェクト表現を返すクエリ.
	 */
	public static final Query<Parameter,UnsignedIntegral> EJC = new EJCQuery();
	
	/**
	 * ユニット定義パラメータmladrを読み取ってそのJavaオブジェクト表現を返すクエリ.
	 */
	public static final Query<Parameter,MailAddress> MLADR = new MLADRQuery();
	
	/**
	 * 与えられたフォーマット文字列をメッセージとして持つ{@code IllegalArgumentException}インスタンスを生成する.
	 * @param format フォーマット
	 * @param args フォーマット文字列から参照されるオブジェクト
	 * @return 例外インスタンス
	 */
	private static IllegalArgumentException illegalArgument
	(final String format, final Object... args) {
		throw new IllegalArgumentException(String.format(format, args));
	}
	
	private InternalParameterQueries() {}
}
