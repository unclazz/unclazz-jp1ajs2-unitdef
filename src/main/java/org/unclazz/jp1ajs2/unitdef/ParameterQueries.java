package org.unclazz.jp1ajs2.unitdef;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.unclazz.jp1ajs2.unitdef.parameter.CommandLine;
import org.unclazz.jp1ajs2.unitdef.parameter.ExitCodeThreshold;
import org.unclazz.jp1ajs2.unitdef.parameter.FixedDuration;
import org.unclazz.jp1ajs2.unitdef.parameter.MapSize;
import org.unclazz.jp1ajs2.unitdef.parameter.ResultJudgmentType;
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
	
	public static final ParameterQuery<CharSequence> EU = queryForCharSequence;
	
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
	
	private static int parseIntFrom(Parameter p) {
		return Integer.parseInt(p.getValue(0).getRawCharSequence().toString());
	}
	private static IllegalArgumentException illegalArgument(final String format, final Object... args) {
		throw new IllegalArgumentException(String.format(format, args));
	}
}
