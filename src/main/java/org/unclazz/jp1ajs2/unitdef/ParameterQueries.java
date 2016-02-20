package org.unclazz.jp1ajs2.unitdef;

import java.util.Iterator;
import java.util.List;
import java.util.regex.MatchResult;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.unclazz.jp1ajs2.unitdef.builder.Builders;
import org.unclazz.jp1ajs2.unitdef.builder.ElementBuilder;
import org.unclazz.jp1ajs2.unitdef.builder.StartDateBuilder;
import org.unclazz.jp1ajs2.unitdef.parameter.AnteroposteriorRelationship;
import org.unclazz.jp1ajs2.unitdef.parameter.CommandLine;
import org.unclazz.jp1ajs2.unitdef.parameter.DayOfWeek;
import org.unclazz.jp1ajs2.unitdef.parameter.Element;
import org.unclazz.jp1ajs2.unitdef.parameter.EndDelayTime;
import org.unclazz.jp1ajs2.unitdef.parameter.EndStatusJudgementType;
import org.unclazz.jp1ajs2.unitdef.parameter.EnvironmentVariable;
import org.unclazz.jp1ajs2.unitdef.parameter.ExecutionCycle;
import org.unclazz.jp1ajs2.unitdef.parameter.ExecutionCycle.CycleUnit;
import org.unclazz.jp1ajs2.unitdef.parameter.MailAddress.MailAddressType;
import org.unclazz.jp1ajs2.unitdef.parameter.ExecutionTimedOutStatus;
import org.unclazz.jp1ajs2.unitdef.parameter.ExecutionUserType;
import org.unclazz.jp1ajs2.unitdef.parameter.ExitCodeThreshold;
import org.unclazz.jp1ajs2.unitdef.parameter.FileWatchingCondition;
import org.unclazz.jp1ajs2.unitdef.parameter.FileWatchingConditionSet;
import org.unclazz.jp1ajs2.unitdef.parameter.FixedDuration;
import org.unclazz.jp1ajs2.unitdef.parameter.LinkedRuleNumber;
import org.unclazz.jp1ajs2.unitdef.parameter.MailAddress;
import org.unclazz.jp1ajs2.unitdef.parameter.MapSize;
import org.unclazz.jp1ajs2.unitdef.parameter.ElapsedTime;
import org.unclazz.jp1ajs2.unitdef.parameter.ResultJudgmentType;
import org.unclazz.jp1ajs2.unitdef.parameter.RuleNumber;
import org.unclazz.jp1ajs2.unitdef.parameter.StartDate;
import org.unclazz.jp1ajs2.unitdef.parameter.StartDate.ByYearMonth.WithDayOfMonth;
import org.unclazz.jp1ajs2.unitdef.parameter.StartDate.CountingMethod;
import org.unclazz.jp1ajs2.unitdef.parameter.StartDate.DesignationMethod;
import org.unclazz.jp1ajs2.unitdef.parameter.StartDate.NumberOfWeek;
import org.unclazz.jp1ajs2.unitdef.parameter.DelayTime;
import org.unclazz.jp1ajs2.unitdef.parameter.DeleteOption;
import org.unclazz.jp1ajs2.unitdef.parameter.StartDelayTime;
import org.unclazz.jp1ajs2.unitdef.parameter.StartTime;
import org.unclazz.jp1ajs2.unitdef.parameter.Time;
import org.unclazz.jp1ajs2.unitdef.parameter.UnitConnectionType;
import org.unclazz.jp1ajs2.unitdef.parameter.UnitType;
import org.unclazz.jp1ajs2.unitdef.parameter.WriteOption;
import org.unclazz.jp1ajs2.unitdef.parser.EnvParamParser;
import org.unclazz.jp1ajs2.unitdef.util.CharSequenceUtils;
import org.unclazz.jp1ajs2.unitdef.util.UnsignedIntegral;

import static org.unclazz.jp1ajs2.unitdef.ParameterValueQueries.*;

/**
 * {@link ParameterQuery}のためのユーティリティ・クラス.
 */
public final class ParameterQueries {
	private ParameterQueries() {}
	
	/**
	 * ユニット定義パラメータelの第3値を解析するための正規表現パターン.
	 */
	private static final Pattern patternForParamElValue3 = Pattern.compile("^\\+(\\d+)\\s*\\+(\\d+)$");
	
	/**
	 * ユニット定義パラメータszを解析するための正規表現パターン.
	 */
	private final static Pattern patternForParamSzValue = Pattern.compile("^(\\d+)[^\\d]+(\\d+)$");

	/**
	 * ユニット定義パラメータscとteのためのクエリ.
	 */
	private static final ParameterQuery<CommandLine> queryForCommandLine =
			new ParameterQuery<CommandLine>() {
		@Override
		public CommandLine queryFrom(Parameter p) {
			return CommandLine.of(p.getValue(0).getRawCharSequence());
		}
	};
	
	/**
	 * 任意のユニット定義パラメータの第1値を読み取って{@code CharSequence}として返すクエリ.
	 */
	private static final ParameterQuery<CharSequence> queryForCharSequence =
			new ParameterQuery<CharSequence>() {
		@Override
		public CharSequence queryFrom(Parameter p) {
			return p.getValue(0).getRawCharSequence();
		}
	};
	
	/**
	 * ユニット定義パラメータtop1などのためのクエリ.
	 */
	private static final ParameterQuery<DeleteOption> queryForDeleteOption =
			new ParameterQuery<DeleteOption>() {
		@Override
		public DeleteOption queryFrom(Parameter p) {
			return DeleteOption.valueOfCode(p.getValue(0, string()));
		}
	};
	
	/**
	 * ユニット定義パラメータthoなどのためのクエリ.
	 */
	private static final ParameterQuery<ExitCodeThreshold> queryForExitCodeThreshold =
			new ParameterQuery<ExitCodeThreshold>() {
		@Override
		public ExitCodeThreshold queryFrom(Parameter p) {
			return ExitCodeThreshold.of(intValue(p));
		}
	};
	
	/**
	 * ユニット定義パラメータtmitvなどのためのクエリ.
	 */
	private static final ParameterQuery<ElapsedTime> queryForMinutesInterval =
	new ParameterQuery<ElapsedTime>() {
		@Override
		public ElapsedTime queryFrom(Parameter p) {
			return ElapsedTime.of(p.getValue(0, integer()));
		}
	};

	/**
	 * ユニット定義パラメータsoaなどのためのクエリ.
	 */
	private static final ParameterQuery<WriteOption> queryForWriteOption =
			new ParameterQuery<WriteOption>() {
		@Override
		public WriteOption queryFrom(Parameter p) {
			return WriteOption.valueOfCode(p.getValue(0, string()));
		}
	};
	
	/**
	 * 任意のユニット定義パラメータ第1値を整数として読み取る.
	 * @param p ユニット定義パラメータ
	 * @return 読み取り結果
	 */
	private static int intValue(Parameter p) {
		return p.getValue(0, ParameterValueQueries.integer());
	}
	
	/**
	 * 与えられたフォーマット文字列をメッセージとして持つ{@code IllegalArgumentException}インスタンスを生成する.
	 * @param format フォーマット
	 * @param args フォーマット文字列から参照されるオブジェクト
	 * @return 例外インスタンス
	 */
	private static IllegalArgumentException 
	illegalArgument(final String format, final Object... args) {
		throw new IllegalArgumentException(String.format(format, args));
	}
	
	/**
	 * ユニット定義パラメータarを読み取ってそのJavaオブジェクト表現を返すクエリ.
	 */
	public static final ParameterQuery<AnteroposteriorRelationship> AR =
			new ParameterQuery<AnteroposteriorRelationship>() {
		@Override
		public AnteroposteriorRelationship queryFrom(final Parameter p) {
			final Tuple t = p.getValue(0).getTuple();
			return Builders
					.parameterAR()
					.setFromUnitName(t.get("f"))
					.setToUnitName(t.get("t"))
					.setConnectionType(t.size() > 2 
							? UnitConnectionType.valueOfCode(t.get(2).toString())
							: UnitConnectionType.SEQUENTIAL)
					.build();
		}
	};
	
	/**
	 * ユニット定義パラメータcmを読み取ってそのJavaオブジェクト表現を返すクエリ.
	 */
	public static final ParameterQuery<CharSequence> CM = queryForCharSequence;
	
	/**
	 * ユニット定義パラメータcyを読み取ってそのJavaオブジェクト表現を返すクエリ.
	 */
	public static final ParameterQuery<ExecutionCycle> CY = 
			new ParameterQuery<ExecutionCycle>() {
		@Override
		public ExecutionCycle queryFrom(final Parameter p) {
			final int valueCount = p.getValueCount();
			final int ruleNumber = valueCount == 1 ? 1 : p.getValue(0, integer());
			final Tuple cycleNumberAndUnit = p.getValue(valueCount == 1 ? 0 : 1, tuple());
			final int cycleNumber = Integer.parseInt(cycleNumberAndUnit.get(0).toString());
			final CycleUnit cycleUnit = CycleUnit.valueOfCode(cycleNumberAndUnit.get(1));

			return ExecutionCycle.of(cycleNumber, cycleUnit).at(ruleNumber);
		}
	};
	
	/**
	 * ユニット定義パラメータelを読み取ってそのJavaオブジェクト表現を返すクエリ.
	 */
	public static final ParameterQuery<Element> EL = new ParameterQuery<Element>() {
		@Override
		public Element queryFrom(Parameter p) {
			final Iterator<ParameterValue> vals = p.iterator();
			final ElementBuilder builder = Builders
					.parameterEL()
					.setUnitName(vals.next().getRawCharSequence().toString())
					.setUnitType(UnitType.valueOfCode(vals.next().
							getRawCharSequence().toString()));
			
			final Matcher m = patternForParamElValue3.
					matcher(vals.next().getRawCharSequence());
			
			if (!m.matches()) {
				throw new IllegalArgumentException("Invalid el parameter");
			}
			
			return builder
					.setHPixel(Integer.parseInt(m.group(1)))
					.setVPixel(Integer.parseInt(m.group(2)))
					.build();
		}
	};
	
	/**
	 * ユニット定義パラメータetsを読み取ってそのJavaオブジェクト表現を返すクエリ.
	 */
	public static final ParameterQuery<ExecutionTimedOutStatus> ETS = 
			new ParameterQuery<ExecutionTimedOutStatus>() {
		@Override
		public ExecutionTimedOutStatus queryFrom(Parameter p) {
			return ExecutionTimedOutStatus.valueOfCode(p.getValue(0, string()));
		}
	};
	
	/**
	 * ユニット定義パラメータeuを読み取ってそのJavaオブジェクト表現を返すクエリ.
	 */
	public static final ParameterQuery<ExecutionUserType> EU = 
			new ParameterQuery<ExecutionUserType>() {
		@Override
		public ExecutionUserType queryFrom(Parameter p) {
			return ExecutionUserType.valueOfCode(p.getValue(0).
					getRawCharSequence().toString());
		}
	};
	
	/**
	 * ユニット定義パラメータfdを読み取ってそのJavaオブジェクト表現を返すクエリ.
	 */
	public static final ParameterQuery<FixedDuration> FD = 
			new ParameterQuery<FixedDuration>() {
		@Override
		public FixedDuration queryFrom(Parameter p) {
			return FixedDuration.of(intValue(p));
		}
	};
	
	/**
	 * ユニット定義パラメータflwcを読み取ってそのJavaオブジェクト表現を返すクエリ.
	 */
	public static final ParameterQuery<FileWatchingConditionSet> FLWC = 
			new ParameterQuery<FileWatchingConditionSet>() {
		@Override
		public FileWatchingConditionSet queryFrom(Parameter p) {
			return FileWatchingConditionSet.of(FileWatchingCondition
					.valueOfCodes(p.query(queryForCharSequence)));
		}
	};
	
	/**
	 * ユニット定義パラメータjdを読み取ってそのJavaオブジェクト表現を返すクエリ.
	 */
	public static final ParameterQuery<ResultJudgmentType> JD = 
			new ParameterQuery<ResultJudgmentType>() {
		@Override
		public ResultJudgmentType queryFrom(Parameter p) {
			return ResultJudgmentType.valueOfCode(p.getValue(0).
					getRawCharSequence().toString());
		}
	};
	
	/**
	 * ユニット定義パラメータlnを読み取ってそのJavaオブジェクト表現を返すクエリ.
	 */
	public static final ParameterQuery<LinkedRuleNumber> LN = 
			new ParameterQuery<LinkedRuleNumber>() {
		@Override
		public LinkedRuleNumber queryFrom(Parameter p) {
			final int valueCount = p.getValueCount();
			final int ruleNumber = valueCount == 1 ? 1 : p.getValue(0, integer());
			final int targetRuleNumber = p.getValue(valueCount == 1 ? 0 : 1, integer());
			return LinkedRuleNumber.ofTarget(targetRuleNumber).at(ruleNumber);
		}
	};
	
	/**
	 * ユニット定義パラメータscを読み取ってそのJavaオブジェクト表現を返すクエリ.
	 */
	public static final ParameterQuery<CommandLine> SC = queryForCommandLine;
	
	/**
	 * ユニット定義パラメータsdを読み取ってそのJavaオブジェクト表現を返すクエリ.
	 */
	public static final ParameterQuery<StartDate> SD = new ParameterQuery<StartDate>() {
		@Override
		public StartDate queryFrom(final Parameter p) {
			// sd=[N,]{
			// 		[[yyyy/]mm/]{
			// 			[+|*|@]dd
			// 			|[+|*|@]b[-DD]
			// 			|[+]{su|mo|tu|we|th|fr|sa} [:{n|b}]
			// 		}
			// 		|en
			// 		|ud
			// 	};
			
			final StartDateBuilder builder = Builders.parameterSD();
			final int valueCount = p.getValueCount();
			builder.setRuleNumber(valueCount == 1 
					? RuleNumber.DEFAULT 
					: RuleNumber.of(p.getValue(0, integer())));
			
			final String maybeYyyyMm = p.getValue(valueCount == 1 ? 0 : 1, string());
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
					throw new IllegalArgumentException("Invalid sd parameter");
				}
			}
			
			// sd=[N,]{
			// 		[[yyyy/]mm/]{
			// 			[+|*|@]dd
			// 			|[+|*|@]b[-DD]
			// 			|[+]{su|mo|tu|we|th|fr|sa} [:{n|b}]
			// 		}
			// 	};
			
			final String[] fragments = maybeYyyyMm.split("/");
			final String daysMaybePrefixed;
			if (fragments.length == 3) {
				builder.setYear(Integer.parseInt(fragments[0]));
				builder.setMonth(Integer.parseInt(fragments[1]));
				daysMaybePrefixed = fragments[2];
			} else if (fragments.length == 2) {
				builder.setMonth(Integer.parseInt(fragments[0]));
				daysMaybePrefixed = fragments[1];
			} else {
				daysMaybePrefixed = fragments[0];
			}
			
			final char daysPrefix = daysMaybePrefixed.charAt(0);
			final String days;
			final CountingMethod countingMethod;
			final boolean byDayOfWeek;
			if (daysPrefix == '+') {
				days = daysMaybePrefixed.substring(1);
				byDayOfWeek = 'f' <= days.charAt(0) || days.charAt(0) <= 'w';
			} else if (daysPrefix == '@') {
				days = daysMaybePrefixed.substring(1);
				byDayOfWeek = false;
			} else if (daysPrefix == '*') {
				days = daysMaybePrefixed.substring(1);
				byDayOfWeek = false;
			} else {
				days = daysMaybePrefixed;
				byDayOfWeek = 'f' <= days.charAt(0) || days.charAt(0) <= 'w';
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
					builder.setNumberOfWeek(NumberOfWeek.NONE_SPECIFIED);
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
	};
	
	/**
	 * ユニット定義パラメータsoaを読み取ってそのJavaオブジェクト表現を返すクエリ.
	 */
	public static final ParameterQuery<WriteOption> SOA = queryForWriteOption;

	/**
	 * ユニット定義パラメータseaを読み取ってそのJavaオブジェクト表現を返すクエリ.
	 */
	public static final ParameterQuery<WriteOption> SEA = queryForWriteOption;
	
	/**
	 * ユニット定義パラメータstを読み取ってそのJavaオブジェクト表現を返すクエリ.
	 */
	public static final ParameterQuery<StartTime> ST = new ParameterQuery<StartTime>() {
		@Override
		public StartTime queryFrom(Parameter p) {
			// st=[N,][+]hh:mm;
			
			// ルール番号の決定
			final int valueCount = p.getValueCount();
			final int ruleNumber;
			if (valueCount == 1) {
				// パラメータの値が1つしかない（＝ルール番号の表記がない）ならルール番号は1
				ruleNumber = 1;
			} else {
				// そうでない場合は先頭の値を整数値として読み取る
				ruleNumber = Integer.parseInt(p.getValue(0).toString());
			}
			
			// 相対時刻指定かどうかの決定
			final CharSequence timeMaybePrefixed = p.
					getValue(valueCount == 1 ? 0 : 1).getRawCharSequence();
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
	};
	
	/**
	 * ユニット定義パラメータsyを読み取ってそのJavaオブジェクト表現を返すクエリ.
	 */
	public static final ParameterQuery<StartDelayTime> SY = 
			new ParameterQuery<StartDelayTime>() {
		@Override
		public StartDelayTime queryFrom(Parameter p) {
			// sy=[N,]hh:mm|{M|U|C}mmmm;
			
			final int valueCount = p.getValueCount();
			final int ruleNumber;
			if (valueCount == 1) {
				ruleNumber = 1;
			} else {
				ruleNumber = Integer.parseInt(p.getValue(0).toString());
			}
			
			final CharSequence timeMaybeRelative = p
					.getValue(valueCount == 1 ? 1 : 0).getRawCharSequence();
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
	};
	
	/**
	 * ユニット定義パラメータeyを読み取ってそのJavaオブジェクト表現を返すクエリ.
	 */
	public static final ParameterQuery<EndDelayTime> EY = 
			new ParameterQuery<EndDelayTime>() {
		@Override
		public EndDelayTime queryFrom(Parameter p) {
			// ey=[N,]hh:mm|{M|U|C}mmmm;
			
			final int valueCount = p.getValueCount();
			final int ruleNumber;
			if (valueCount == 1) {
				ruleNumber = 1;
			} else {
				ruleNumber = Integer.parseInt(p.getValue(0).toString());
			}
			
			final CharSequence timeMaybeRelative = p
					.getValue(valueCount == 1 ? 1 : 0).getRawCharSequence();
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
	};
	
	/**
	 * ユニット定義パラメータszを読み取ってそのJavaオブジェクト表現を返すクエリ.
	 */
	public static final ParameterQuery<MapSize> SZ =
			new ParameterQuery<MapSize>() {
		@Override
		public MapSize queryFrom(Parameter p) {
			final Matcher m = patternForParamSzValue
					.matcher(p.getValue(0).getRawCharSequence());
			if (m.matches()) {
				final int w = Integer.parseInt(m.group(1));
				final int h = Integer.parseInt(m.group(2));
				return MapSize.of(w, h);
			} else {
				throw illegalArgument("Invalid sz parameter (%s)", p);
			}
		}
		
	};
	
	/**
	 * ユニット定義パラメータteを読み取ってそのJavaオブジェクト表現を返すクエリ.
	 */
	public static final ParameterQuery<CommandLine> TE = queryForCommandLine;
	
	/**
	 * ユニット定義パラメータthoを読み取ってそのJavaオブジェクト表現を返すクエリ.
	 */
	public static final ParameterQuery<ExitCodeThreshold> THO = queryForExitCodeThreshold;
	
	/**
	 * ユニット定義パラメータtmivを読み取ってそのJavaオブジェクト表現を返すクエリ.
	 */
	public static final ParameterQuery<ElapsedTime> TMITV = queryForMinutesInterval;
	
	/**
	 * ユニット定義パラメータtop1を読み取ってそのJavaオブジェクト表現を返すクエリ.
	 */
	public static final ParameterQuery<DeleteOption> TOP1 = queryForDeleteOption;
	
	/**
	 * ユニット定義パラメータtop2を読み取ってそのJavaオブジェクト表現を返すクエリ.
	 */
	public static final ParameterQuery<DeleteOption> TOP2 = queryForDeleteOption;
	
	/**
	 * ユニット定義パラメータtop3を読み取ってそのJavaオブジェクト表現を返すクエリ.
	 */
	public static final ParameterQuery<DeleteOption> TOP3 = queryForDeleteOption;
	
	/**
	 * ユニット定義パラメータtop4を読み取ってそのJavaオブジェクト表現を返すクエリ.
	 */
	public static final ParameterQuery<DeleteOption> TOP4 = queryForDeleteOption;
	
	/**
	 * ユニット定義パラメータtyを読み取ってそのJavaオブジェクト表現を返すクエリ.
	 */
	public static final ParameterQuery<UnitType> TY = 
			new ParameterQuery<UnitType>() {
		@Override
		public UnitType queryFrom(Parameter p) {
			return UnitType.valueOfCode(p.getValue(0)
					.getRawCharSequence().toString());
		}
	};
	
	/**
	 * ユニット定義パラメータwthを読み取ってそのJavaオブジェクト表現を返すクエリ.
	 */
	public static final ParameterQuery<ExitCodeThreshold> WTH = queryForExitCodeThreshold;
	
	/**
	 * 正規表現パターンマッチングを行うクエリを返す.
	 * クエリは任意のユニット定義パラメータに対して正規表現マッチングを行いその結果を返す。
	 * 
	 * @param pattern 正規表現パターン
	 * @return マッチング結果
	 */
	public static final ParameterQuery<MatchResult> withPattern(final Pattern pattern) {
		return new ParameterQuery<MatchResult>() {
			private final StringBuilder buff = CharSequenceUtils.builder();
			@Override
			public MatchResult queryFrom(final Parameter p) {
				return helper(p);
			}
			private MatchResult helper(final Parameter p) {
				buff.setLength(0);
				for (final ParameterValue val : p) {
					if (buff.length() > 0) {
						buff.append(',');
						buff.append(val.toString());
					}
				}
				final Matcher mat = pattern.matcher(buff);
				mat.matches();
				return mat;
			}
		};
	}
	
	/**
	 * 正規表現パターンマッチングを行うクエリを返す.
	 * クエリは任意のユニット定義パラメータに対して正規表現マッチングを行いその結果を返す。
	 * 
	 * @param pattern 正規表現パターン
	 * @return マッチング結果
	 */
	public static final ParameterQuery<MatchResult> withPattern(final String pattern) {
		return withPattern(Pattern.compile(pattern));
	}

	/**
	 * ユニット定義パラメータetmを読み取ってそのJavaオブジェクト表現を返すクエリ.
	 */
	public static final ParameterQuery<ElapsedTime> ETM = queryForMinutesInterval;
	
	/**
	 * ユニット定義パラメータejを読み取ってそのJavaオブジェクト表現を返すクエリ.
	 */
	public static final ParameterQuery<EndStatusJudgementType> EJ = 
		new ParameterQuery<EndStatusJudgementType>() {
			@Override
			public EndStatusJudgementType queryFrom(Parameter p) {
				return EndStatusJudgementType.valueOfCode(p.getValue(0)
						.getRawCharSequence().toString());
			}
	};
	
	/**
	 * ユニット定義パラメータejcを読み取ってそのJavaオブジェクト表現を返すクエリ.
	 */
	public static final ParameterQuery<UnsignedIntegral> EJC = 
			new ParameterQuery<UnsignedIntegral>() {
		@Override
		public UnsignedIntegral queryFrom(Parameter p) {
			return UnsignedIntegral.of(p.getValue(0)
					.query(ParameterValueQueries.longInteger()));
		}
	};
	
	/**
	 * ユニット定義パラメータmladrを読み取ってそのJavaオブジェクト表現を返すクエリ.
	 */
	public static final ParameterQuery<MailAddress> MLADR =
			new ParameterQuery<MailAddress>() {
		private final Pattern pat = Pattern.compile("^(TO|CC|BCC):\"(.+\"$)");
		@Override
		public MailAddress queryFrom(Parameter p) {
			final Matcher mat = pat.matcher(p.getValue(0).getRawCharSequence());
			if (mat.matches()) {
				final MailAddressType type = MailAddressType.valueOf(mat.group(1));
				final String address = mat.group(2)
						.replaceAll("#\"", "\"").replaceAll("##", "#");
				return new MailAddress(){
					@Override
					public MailAddressType getType() {
						return type;
					}
					@Override
					public String getAddress() {
						return address;
					}
				};
			}
			throw illegalArgument("Invalid mladr value (%s).", p.getValue(0));
		}
	};
	
	/**
	 * ユニット定義パラメータenvを読み取ってそのJavaオブジェクト表現を返すクエリ.
	 */
	public static final ParameterQuery<List<EnvironmentVariable>> EV =
			new ParameterQuery<List<EnvironmentVariable>>() {
		private final EnvParamParser parser = new EnvParamParser();
		@Override
		public List<EnvironmentVariable> queryFrom(Parameter p) {
			return parser.parse(p.getValue(0).getRawCharSequence()).get();
		}
	};
}
