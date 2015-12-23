package org.unclazz.jp1ajs2.unitdef;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.regex.MatchResult;
import java.util.regex.Pattern;

import org.unclazz.jp1ajs2.unitdef.builder.Builders;
import org.unclazz.jp1ajs2.unitdef.parameter.AnteroposteriorRelationship;
import org.unclazz.jp1ajs2.unitdef.parameter.CommandLine;
import org.unclazz.jp1ajs2.unitdef.parameter.DeleteOption;
import org.unclazz.jp1ajs2.unitdef.parameter.Element;
import org.unclazz.jp1ajs2.unitdef.parameter.EndDelayTime;
import org.unclazz.jp1ajs2.unitdef.parameter.ExecutionCycle;
import org.unclazz.jp1ajs2.unitdef.parameter.ExecutionTimedOutStatus;
import org.unclazz.jp1ajs2.unitdef.parameter.ExecutionUserType;
import org.unclazz.jp1ajs2.unitdef.parameter.ExitCodeThreshold;
import org.unclazz.jp1ajs2.unitdef.parameter.FixedDuration;
import org.unclazz.jp1ajs2.unitdef.parameter.LinkedRuleNumber;
import org.unclazz.jp1ajs2.unitdef.parameter.MapSize;
import org.unclazz.jp1ajs2.unitdef.parameter.MinutesInterval;
import org.unclazz.jp1ajs2.unitdef.parameter.ResultJudgmentType;
import org.unclazz.jp1ajs2.unitdef.parameter.StartDate;
import org.unclazz.jp1ajs2.unitdef.parameter.StartDelayTime;
import org.unclazz.jp1ajs2.unitdef.parameter.StartTime;
import org.unclazz.jp1ajs2.unitdef.parameter.UnitConnectionType;
import org.unclazz.jp1ajs2.unitdef.parameter.UnitType;
import org.unclazz.jp1ajs2.unitdef.parameter.WriteOption;

public final class UnitQueries {
	private UnitQueries() {}
	
	private static final<T> List<T> list() {
		return new LinkedList<T>();
	}
	
	public static UnitQuery<Parameter> parameterNamed(final String paramName) {
		return new UnitQuery<Parameter>() {
			@Override
			public List<Parameter> queryFrom(final Unit unit) {
				final List<Parameter> result = UnitQueries.list();
				for (final Parameter p : unit.getParameters()) {
					if (p.getName().equalsIgnoreCase(paramName)) {
						result.add(p);
					}
				}
				return result;
			}
		};
	}
	public static UnitQuery<Unit> subUnitNamed(final String name) {
		return new UnitQuery<Unit>() {
			@Override
			public List<Unit> queryFrom(Unit unit) {
				for (final Unit subUnit : unit.getSubUnits()) {
					if (subUnit.getAttributes().getUnitName().equals(name)) {
						return Collections.singletonList(subUnit);
					}
				}
				return Collections.emptyList();
			}
		};
	}
	public static UnitQuery<MatchResult> parameterNamed(final String paramName, final String pattern) {
		return parameterNamed(paramName, Pattern.compile(pattern));
	}
	public static UnitQuery<MatchResult> parameterNamed(final String paramName, final Pattern pattern) {
		return parameterNamed(paramName, ParameterQueries.withPattern(pattern));
	}
	public static<T> UnitQuery<T> parameterNamed(final String paramName, final ParameterQuery<T> paramQuery) {
		return new UnitQuery<T>() {
			@Override
			public List<T> queryFrom(final Unit unit) {
				final List<T> result = list();
				for (final Parameter p : unit.getParameters()) {
					if (p.getName().equals(paramName)) {
						final T t = paramQuery.queryFrom(p);
						if (t != null) {
							result.add(t);
						}
					}
				}
				return result;
			}
		};
	}
	
	public static final UnitQuery<AnteroposteriorRelationship> ar() {
		return parameterNamed("ar", ParameterQueries.AR);
	}
	
	public static final UnitQuery<CharSequence> cm() {
		return parameterNamed("cm", ParameterQueries.CM);
	}
	
	public static final UnitQuery<ExecutionCycle> cy() {
		return parameterNamed("cy", ParameterQueries.CY);
	}
	
	public static final UnitQuery<Element> el() {
		return parameterNamed("el", ParameterQueries.EL);
	}
	
	public static final UnitQuery<ExecutionUserType> eu() {
		return parameterNamed("eu", ParameterQueries.EU);
	}
	
	public static final UnitQuery<ExecutionTimedOutStatus> ets() {
		return parameterNamed("ets", ParameterQueries.ETS);
	}
	
	public static final UnitQuery<EndDelayTime> ey() {
		return parameterNamed("ey", ParameterQueries.EY);
	}
	
	public static final UnitQuery<FixedDuration> fd() {
		return parameterNamed("fd", ParameterQueries.FD);
	}
	
	public static final UnitQuery<ResultJudgmentType> jd() {
		return parameterNamed("jd", ParameterQueries.JD);
	}
	
	public static final UnitQuery<LinkedRuleNumber> ln() {
		return parameterNamed("ln", ParameterQueries.LN);
	}
	
	public static final UnitQuery<CharSequence> prm() {
		return parameterNamed("prm", ParameterQueries.PRM);
	}
	
	public static final UnitQuery<CommandLine> sc() {
		return parameterNamed("sc", ParameterQueries.SC);
	}
	
	public static final UnitQuery<StartDate> sd() {
		return parameterNamed("sd", ParameterQueries.SD);
	}
	
	public static final UnitQuery<WriteOption> sea() {
		return parameterNamed("sea", ParameterQueries.SEA);
	}
	
	public static final UnitQuery<WriteOption> soa() {
		return parameterNamed("soa", ParameterQueries.SOA);
	}
	
	public static final UnitQuery<StartTime> st() {
		return parameterNamed("st", ParameterQueries.ST);
	}
	
	public static final UnitQuery<StartDelayTime> sy() {
		return parameterNamed("sy", ParameterQueries.SY);
	}
	
	public static final UnitQuery<MapSize> sz() {
		return parameterNamed("sz", ParameterQueries.SZ);
	}
	
	public static final UnitQuery<ExitCodeThreshold> tho() {
		return parameterNamed("tho", ParameterQueries.THO);
	}
	
	public static final UnitQuery<MinutesInterval> tmitv() {
		return parameterNamed("tmitv", ParameterQueries.TMITV);
	}
	
	public static final UnitQuery<DeleteOption> top1() {
		return parameterNamed("top1", ParameterQueries.TOP1);
	}
	
	public static final UnitQuery<DeleteOption> top2() {
		return parameterNamed("top2", ParameterQueries.TOP2);
	}
	
	public static final UnitQuery<DeleteOption> top3() {
		return parameterNamed("top3", ParameterQueries.TOP3);
	}
	
	public static final UnitQuery<DeleteOption> top4() {
		return parameterNamed("top4", ParameterQueries.TOP4);
	}
	
	public static final UnitQuery<UnitType> ty() {
		return parameterNamed("ty", ParameterQueries.TY);
	}
	
	public static final UnitQuery<CharSequence> un() {
		return parameterNamed("un", ParameterQueries.UN);
	}
	
	public static final UnitQuery<CharSequence> wkp() {
		return parameterNamed("wkp", ParameterQueries.WKP);
	}
	
	public static final UnitQuery<ExitCodeThreshold> wth() {
		return parameterNamed("wth", ParameterQueries.WTH);
	}
}
