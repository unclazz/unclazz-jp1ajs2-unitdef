package org.unclazz.jp1ajs2.unitdef;

import java.util.Iterator;
import java.util.regex.MatchResult;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.unclazz.jp1ajs2.unitdef.builder.Builders;
import org.unclazz.jp1ajs2.unitdef.builder.ElementBuilder;
import org.unclazz.jp1ajs2.unitdef.parameter.CommandLine;
import org.unclazz.jp1ajs2.unitdef.parameter.Element;
import org.unclazz.jp1ajs2.unitdef.parameter.EndDelayTime;
import org.unclazz.jp1ajs2.unitdef.parameter.ExecutionUserType;
import org.unclazz.jp1ajs2.unitdef.parameter.ExitCodeThreshold;
import org.unclazz.jp1ajs2.unitdef.parameter.FixedDuration;
import org.unclazz.jp1ajs2.unitdef.parameter.MapSize;
import org.unclazz.jp1ajs2.unitdef.parameter.ResultJudgmentType;
import org.unclazz.jp1ajs2.unitdef.parameter.StartDate;
import org.unclazz.jp1ajs2.unitdef.parameter.DelayTime;
import org.unclazz.jp1ajs2.unitdef.parameter.StartDelayTime;
import org.unclazz.jp1ajs2.unitdef.parameter.StartTime;
import org.unclazz.jp1ajs2.unitdef.parameter.Time;
import org.unclazz.jp1ajs2.unitdef.parameter.UnitType;

public final class ParameterQueries {
	private ParameterQueries() {}
	
	private static final ParameterQuery<CommandLine> queryForCommandLine =
			new ParameterQuery<CommandLine>() {
		@Override
		public CommandLine queryFrom(Parameter p) {
			return CommandLine.of(p.getValue(0).getRawCharSequence());
		}
	};
	
	private static final ParameterQuery<CharSequence> queryForCharSequence =
			new ParameterQuery<CharSequence>() {
		@Override
		public CharSequence queryFrom(Parameter p) {
			return p.getValue(0).getRawCharSequence();
		}
	};
	
	private static final ParameterQuery<ExitCodeThreshold> queryForExitCodeThreshold =
			new ParameterQuery<ExitCodeThreshold>() {
		@Override
		public ExitCodeThreshold queryFrom(Parameter p) {
			return ExitCodeThreshold.of(parseIntFrom(p));
		}
	}; 
	
	public static final ParameterQuery<CharSequence> CM = queryForCharSequence;
	
	private static final Pattern PARAM_EL_VALUE_3 = Pattern.compile("^\\+(\\d+)\\s*\\+(\\d+)$");
	public static final ParameterQuery<Element> EL = new ParameterQuery<Element>() {
		@Override
		public Element queryFrom(Parameter p) {
			final Iterator<ParameterValue> vals = p.iterator();
			final ElementBuilder builder = Builders
					.forParameterEL()
					.setUnitName(vals.next().getRawCharSequence().toString())
					.setUnitType(UnitType.valueOfCode(vals.next().getRawCharSequence().toString()));
			
			final Matcher m = PARAM_EL_VALUE_3.matcher(vals.next().getRawCharSequence());
			
			if (!m.matches()) {
				throw new IllegalArgumentException("Invalid el parameter");
			}
			
			return builder
					.setHPixel(Integer.parseInt(m.group(1)))
					.setVPixel(Integer.parseInt(m.group(2)))
					.build();
		}
	};
	
	public static final ParameterQuery<ExecutionUserType> EU = new ParameterQuery<ExecutionUserType>() {
		@Override
		public ExecutionUserType queryFrom(Parameter p) {
			return ExecutionUserType.valueOfCode(p.getValue(0).getRawCharSequence().toString());
		}
	};
	
	public static final ParameterQuery<FixedDuration> FD = new ParameterQuery<FixedDuration>() {
		@Override
		public FixedDuration queryFrom(Parameter p) {
			return FixedDuration.of(parseIntFrom(p));
		}
	};
	
	public static final ParameterQuery<ResultJudgmentType> JD = new ParameterQuery<ResultJudgmentType>() {
		@Override
		public ResultJudgmentType queryFrom(Parameter p) {
			return ResultJudgmentType.valueOfCode(p.getValue(0).getRawCharSequence().toString());
		}
	};
	
	public static final ParameterQuery<CharSequence> PRM = queryForCharSequence;
	
	public static final ParameterQuery<CommandLine> SC = queryForCommandLine;
	
	public static final ParameterQuery<StartDate> SD = new ParameterQuery<StartDate>() {
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
			return null;
		}
	};
	
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
			final CharSequence timeMaybePrefixed = p.getValue(valueCount == 1 ? 0 : 1).getRawCharSequence();
			final boolean relative = timeMaybePrefixed.charAt(0) == '+';
			
			// 時刻の決定
			final String[] hhmm = timeMaybePrefixed
					.subSequence(relative ? 1 : 0, timeMaybePrefixed.length())
					.toString()
					.split(":");
			final int hh = Integer.parseInt(hhmm[0]);
			final int mm = Integer.parseInt(hhmm[1]);
			
			// VOの組み立て
			return Builders.forParameterST()
					.setRuleNumber(ruleNumber)
					.setRelative(relative)
					.setHours(hh)
					.setMinutes(mm)
					.build();
		}
	};
	
	public static final ParameterQuery<StartDelayTime> SY = new ParameterQuery<StartDelayTime>() {
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
			
			final CharSequence timeMaybeRelative = p.getValue(valueCount == 1 ? 1 : 0).getRawCharSequence();
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
			return Builders.forParameterSY()
					.setRuleNumber(ruleNumber)
					.setTimingMethod(timingMethod)
					.setTime(time)
					.build();
		}
	};
	
	public static final ParameterQuery<EndDelayTime> EY = new ParameterQuery<EndDelayTime>() {
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
			
			final CharSequence timeMaybeRelative = p.getValue(valueCount == 1 ? 1 : 0).getRawCharSequence();
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
			return Builders.forParameterEY()
					.setRuleNumber(ruleNumber)
					.setTimingMethod(timingMethod)
					.setTime(time)
					.build();
		}
	};
	
	private final static Pattern szValuePattern = Pattern.compile("^(\\d+)[^\\d]+(\\d+)$");
	public static final ParameterQuery<MapSize> SZ = new ParameterQuery<MapSize>() {
		@Override
		public MapSize queryFrom(Parameter p) {
			final Matcher m = szValuePattern.matcher(p.getValue(0).getRawCharSequence());
			if (m.matches()) {
				final int w = Integer.parseInt(m.group(1));
				final int h = Integer.parseInt(m.group(2));
				return new MapSize(w, h);
			} else {
				throw illegalArgument("Invalid sz parameter (%s)", p);
			}
		}
		
	};
	
	public static final ParameterQuery<CommandLine> TE = queryForCommandLine;
	
	public static final ParameterQuery<UnitType> TY = new ParameterQuery<UnitType>() {
		@Override
		public UnitType queryFrom(Parameter p) {
			return UnitType.valueOfCode(p.getValue(0).getRawCharSequence().toString());
		}
	};
	
	public static final ParameterQuery<ExitCodeThreshold> THO = queryForExitCodeThreshold;
	
	public static final ParameterQuery<CharSequence> UN = queryForCharSequence;
	
	public static final ParameterQuery<CharSequence> WKP = queryForCharSequence;
	
	public static final ParameterQuery<ExitCodeThreshold> WTH = queryForExitCodeThreshold;
	
	public static final ParameterQuery<MatchResult> withPattern(final Pattern pattern) {
		return new ParameterQuery<MatchResult>() {
			private final StringBuilder buff = new StringBuilder();
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
	
	public static final ParameterQuery<MatchResult> withPattern(final String pattern) {
		return withPattern(Pattern.compile(pattern));
	}
	
	private static int parseIntFrom(Parameter p) {
		return Integer.parseInt(p.getValue(0).getRawCharSequence().toString());
	}
	private static IllegalArgumentException illegalArgument(final String format, final Object... args) {
		throw new IllegalArgumentException(String.format(format, args));
	}
}
