package org.unclazz.jp1ajs2.unitdef.query;

import static org.unclazz.jp1ajs2.unitdef.query.ParameterValueQueries.integer;
import static org.unclazz.jp1ajs2.unitdef.query.ParameterValueQueries.string;
import static org.unclazz.jp1ajs2.unitdef.query.ParameterValueQueries.tuple;

import java.util.Collections;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.unclazz.jp1ajs2.unitdef.Parameter;
import org.unclazz.jp1ajs2.unitdef.ParameterValue;
import org.unclazz.jp1ajs2.unitdef.ParameterValueType;
import org.unclazz.jp1ajs2.unitdef.Tuple;
import org.unclazz.jp1ajs2.unitdef.builder.Builders;
import org.unclazz.jp1ajs2.unitdef.builder.ElementBuilder;
import org.unclazz.jp1ajs2.unitdef.builder.StartDateBuilder;
import org.unclazz.jp1ajs2.unitdef.parameter.AnteroposteriorRelationship;
import org.unclazz.jp1ajs2.unitdef.parameter.CommandLine;
import org.unclazz.jp1ajs2.unitdef.parameter.DayOfWeek;
import org.unclazz.jp1ajs2.unitdef.parameter.DelayTime;
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
import org.unclazz.jp1ajs2.unitdef.parameter.FileWatchConditionFlag;
import org.unclazz.jp1ajs2.unitdef.parameter.FixedDuration;
import org.unclazz.jp1ajs2.unitdef.parameter.LinkedRuleNumber;
import org.unclazz.jp1ajs2.unitdef.parameter.MailAddress;
import org.unclazz.jp1ajs2.unitdef.parameter.MapSize;
import org.unclazz.jp1ajs2.unitdef.parameter.ResultJudgmentType;
import org.unclazz.jp1ajs2.unitdef.parameter.RuleNumber;
import org.unclazz.jp1ajs2.unitdef.parameter.RunConditionWatchLimitCount;
import org.unclazz.jp1ajs2.unitdef.parameter.RunConditionWatchLimitTime;
import org.unclazz.jp1ajs2.unitdef.parameter.StartDate;
import org.unclazz.jp1ajs2.unitdef.parameter.StartDateAdjustment;
import org.unclazz.jp1ajs2.unitdef.parameter.StartDateCompensation;
import org.unclazz.jp1ajs2.unitdef.parameter.StartDateCompensationDeadline;
import org.unclazz.jp1ajs2.unitdef.parameter.StartDelayTime;
import org.unclazz.jp1ajs2.unitdef.parameter.StartTime;
import org.unclazz.jp1ajs2.unitdef.parameter.Time;
import org.unclazz.jp1ajs2.unitdef.parameter.UnitConnectionType;
import org.unclazz.jp1ajs2.unitdef.parameter.UnitType;
import org.unclazz.jp1ajs2.unitdef.parameter.WriteOption;
import org.unclazz.jp1ajs2.unitdef.query.SingleParameterQuery.ValueIterableQuery;
import org.unclazz.jp1ajs2.unitdef.query.SingleParameterQuery.ValueOneQuery;
import org.unclazz.jp1ajs2.unitdef.parameter.ExecutionCycle.CycleUnit;
import org.unclazz.jp1ajs2.unitdef.parameter.MailAddress.MailAddressType;
import org.unclazz.jp1ajs2.unitdef.parameter.RunConditionWatchLimitTime.LimitationType;
import org.unclazz.jp1ajs2.unitdef.parameter.StartDate.CountingMethod;
import org.unclazz.jp1ajs2.unitdef.parameter.StartDate.DesignationMethod;
import org.unclazz.jp1ajs2.unitdef.parameter.StartDate.NumberOfWeek;
import org.unclazz.jp1ajs2.unitdef.parameter.StartDate.ByYearMonth.WithDayOfMonth;
import org.unclazz.jp1ajs2.unitdef.parameter.StartDateAdjustment.AdjustmentType;
import org.unclazz.jp1ajs2.unitdef.parameter.StartDateCompensation.CompensationMethod;
import org.unclazz.jp1ajs2.unitdef.util.CharSequenceUtils;
import org.unclazz.jp1ajs2.unitdef.util.LazyIterable;
import org.unclazz.jp1ajs2.unitdef.util.LazyIterable.Yield;
import org.unclazz.jp1ajs2.unitdef.util.LazyIterable.YieldCallable;
import org.unclazz.jp1ajs2.unitdef.util.Predicate;
import org.unclazz.jp1ajs2.unitdef.util.UnsignedIntegral;

public interface SingleParameterQuery extends Query<Parameter, Parameter>,
ParameterConditionalModifier<SingleParameterQuery> {
	@Override
	WhenValueAtNClause<SingleParameterQuery> whenValueAt(int i);
	@Override
	WhenValueCountNClause<SingleParameterQuery> whenValueCount(int c);
	
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
	}
}

final class CastInteger implements Query<ParameterValue, Integer> {
	private final int defaultValue;
	private final boolean hasDefault;
	CastInteger(final int defaultValue) {
		this.defaultValue = defaultValue;
		this.hasDefault = true;
	}
	CastInteger() {
		this.defaultValue = -1;
		this.hasDefault = false;
	}
	@Override
	public Integer queryFrom(ParameterValue t) {
		try {
			return Integer.parseInt(t.getStringValue());
		} catch (final NumberFormatException e) {
			if (hasDefault) {
				return defaultValue;
			} else {
				throw e;
			}
		}
	}
}
final class CastBoolean implements Query<ParameterValue, Boolean> {
	private final String[] trueValues;
	CastBoolean(final String[] trueValues) {
		this.trueValues = trueValues;
	}
	@Override
	public Boolean queryFrom(ParameterValue t) {
		final String v = t.getStringValue();
		for (final String trueValue : trueValues) {
			if (v.equals(trueValue)) {
				return true;
			}
		}
		return false;
	}
}
final class CastQuotedString implements Query<ParameterValue, String> {
	private final boolean force;
	CastQuotedString(final boolean force) {
		this.force = force;
	}
	@Override
	public String queryFrom(ParameterValue t) {
		if (force || t.getType() == ParameterValueType.QUOTED_STRING) {
			return CharSequenceUtils.quote(t.getStringValue()).toString();
		} else {
			return t.getStringValue();
		}
	}
}
final class CastEscapedString implements Query<ParameterValue, String> {
	@Override
	public String queryFrom(ParameterValue t) {
		return CharSequenceUtils.escape(t.getStringValue()).toString();
	}
}
final class CastString implements Query<ParameterValue, String> {
	@Override
	public String queryFrom(ParameterValue t) {
		return t.getStringValue();
	}
}

final class DefaultValueOneQuery implements ValueOneQuery {
	private static final class TypedOneQuery<T> implements OneQuery<Parameter, T>{
		private final ValueOneQuery baseQuery;
		private final Query<ParameterValue, T> castQuery;
		private TypedOneQuery(final ValueOneQuery baseQuery,
				Query<ParameterValue, T> castQuery) {
			this.baseQuery = baseQuery;
			this.castQuery = castQuery;
		}
		@Override
		public T queryFrom(Parameter t) {
			return castQuery.queryFrom(baseQuery.queryFrom(t));
		}

		@Override
		public Query<Parameter, T> cached() {
			return CachedQuery.wrap(this);
		}
	}
	
	private final SingleParameterQuery baseQuery;
	private final int i;
	DefaultValueOneQuery(final SingleParameterQuery baseQuery, final int i) {
		this.baseQuery = baseQuery;
		this.i = i;
	}
	@Override
	public Query<Parameter, ParameterValue> cached() {
		return CachedQuery.wrap(this);
	}
	@Override
	public ParameterValue queryFrom(Parameter t) {
		return baseQuery.queryFrom(t).getValues().get(i);
	}
	@Override
	public OneQuery<Parameter, Integer> asInteger() {
		return new TypedOneQuery<Integer>(this, new CastInteger());
	}
	@Override
	public OneQuery<Parameter, Integer> asInteger(int defaultValue) {
		return new TypedOneQuery<Integer>(this, new CastInteger(defaultValue));
	}
	@Override
	public OneQuery<Parameter, String> asString() {
		return new TypedOneQuery<String>(this, new CastString());
	}
	@Override
	public OneQuery<Parameter, String> asEscapedString() {
		return new TypedOneQuery<String>(this, new CastEscapedString());
	}
	@Override
	public OneQuery<Parameter, String> asQuotedString() {
		return new TypedOneQuery<String>(this, new CastQuotedString(false));
	}
	@Override
	public OneQuery<Parameter, String> asQuotedString(boolean forceQuote) {
		return new TypedOneQuery<String>(this, new CastQuotedString(forceQuote));
	}
	@Override
	public OneQuery<Parameter, Boolean> asBoolean(String... trueValues) {
		return new TypedOneQuery<Boolean>(this, new CastBoolean(trueValues));
	}
}

final class DefaultValueIterableQuery 
extends IterableQuerySupport<Parameter, ParameterValue>
implements ValueIterableQuery {
	
	private final List<Predicate<ParameterValue>> preds;
	private final SingleParameterQuery baseQuery;
	
	DefaultValueIterableQuery(final SingleParameterQuery baseQuery) {
		this.baseQuery = baseQuery;
		this.preds = Collections.emptyList();
	}
	DefaultValueIterableQuery(final SingleParameterQuery baseQuery,
			final List<Predicate<ParameterValue>> preds) {
		this.baseQuery = baseQuery;
		this.preds = preds;
	}

	@Override
	public Query<Parameter, Iterable<ParameterValue>> and(Predicate<ParameterValue> pred) {
		final LinkedList<Predicate<ParameterValue>> newList = new LinkedList<Predicate<ParameterValue>>();
		newList.addAll(preds);
		newList.addLast(pred);
		return new DefaultValueIterableQuery(baseQuery, newList);
	}

	@Override
	public Iterable<ParameterValue> queryFrom(Parameter t) {
		return LazyIterable.forEach(baseQuery.queryFrom(t).getValues(),
			new YieldCallable<ParameterValue,ParameterValue>(){
			@Override
			public Yield<ParameterValue> yield(ParameterValue item, int index) {
				for (final Predicate<ParameterValue> pred : preds) {
					if (!pred.test(item)) {
						return Yield.yieldVoid();
					}
				}
				return Yield.yieldReturn(item);
			}
		});
	}

	@Override
	public IterableQuery<Parameter, Integer> asInteger() {
		return query(new CastInteger());
	}

	@Override
	public IterableQuery<Parameter, Integer> asInteger(int defaultValue) {
		return query(new CastInteger(defaultValue));
	}

	@Override
	public IterableQuery<Parameter, String> asString() {
		return query(new CastString());
	}

	@Override
	public IterableQuery<Parameter, String> asQuotedString() {
		return query(new CastQuotedString(false));
	}

	@Override
	public IterableQuery<Parameter, String> asQuotedString(boolean forceQuote) {
		return query(new CastQuotedString(forceQuote));
	}

	@Override
	public IterableQuery<Parameter, Boolean> asBoolean(String... trueValues) {
		return query(new CastBoolean(trueValues));
	}
}

final class DefaultSingleParameterQuery implements SingleParameterQuery{
	private final class QueryFactory implements ModifierFactory<SingleParameterQuery> {
		private final WhenThenList whenThenList;
		QueryFactory(WhenThenList n) {
			this.whenThenList = n;
		}
		@Override
		public SingleParameterQuery apply(WhenThenList whenThenList) {
			return new DefaultSingleParameterQuery(this.whenThenList == null ?
					whenThenList : this.whenThenList.concat(whenThenList));
		}
	}
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
	private static final Query<Parameter,CommandLine> queryForCommandLine =
			new Query<Parameter,CommandLine>() {
		@Override
		public CommandLine queryFrom(Parameter p) {
			return CommandLine.of(p.getValues().get(0).getStringValue());
		}
	};
	
	/**
	 * 任意のユニット定義パラメータの第1値を読み取って{@code CharSequence}として返すクエリ.
	 */
	private static final Query<Parameter,CharSequence> queryForCharSequence =
			new Query<Parameter,CharSequence>() {
		@Override
		public CharSequence queryFrom(Parameter p) {
			return p.getValues().get(0).getStringValue();
		}
	};
	
	/**
	 * ユニット定義パラメータtop1などのためのクエリ.
	 */
	private static final Query<Parameter,DeleteOption> queryForDeleteOption =
			new Query<Parameter,DeleteOption>() {
		@Override
		public DeleteOption queryFrom(Parameter p) {
			return DeleteOption.valueOfCode(p.getValues().get(0).query(string()));
		}
	};
	
	/**
	 * ユニット定義パラメータthoなどのためのクエリ.
	 */
	private static final Query<Parameter,ExitCodeThreshold> queryForExitCodeThreshold =
			new Query<Parameter,ExitCodeThreshold>() {
		@Override
		public ExitCodeThreshold queryFrom(Parameter p) {
			return ExitCodeThreshold.of(Integer.parseInt(p.getValues().get(0).getStringValue()));
		}
	};
	
	/**
	 * ユニット定義パラメータtmitvなどのためのクエリ.
	 */
	private static final Query<Parameter,ElapsedTime> queryForMinutesInterval =
	new Query<Parameter,ElapsedTime>() {
		@Override
		public ElapsedTime queryFrom(Parameter p) {
			return ElapsedTime.of(p.getValues().get(0).query(integer()));
		}
	};

	/**
	 * ユニット定義パラメータsoaなどのためのクエリ.
	 */
	private static final Query<Parameter,WriteOption> queryForWriteOption =
			new Query<Parameter,WriteOption>() {
		@Override
		public WriteOption queryFrom(Parameter p) {
			return WriteOption.valueOfCode(p.getValues().get(0).query(string()));
		}
	};
	
	private final WhenThenList whenThenList;
	
	DefaultSingleParameterQuery(WhenThenList whenThenList) {
		this.whenThenList = whenThenList;
	}
	DefaultSingleParameterQuery() {
		this.whenThenList = null;
	}

	@Override
	public Parameter queryFrom(Parameter t) {
		return whenThenList == null ? t : whenThenList.apply(t);
	}

	@Override
	public WhenValueAtNClause<SingleParameterQuery> whenValueAt(int i) {
		return new DefaultWhenValueAtNClause<SingleParameterQuery>
			(new QueryFactory(whenThenList), i);
	}

	@Override
	public WhenValueCountNClause<SingleParameterQuery> whenValueCount(int c) {
		return new DefaultWhenValueCountNClause<SingleParameterQuery>
				(new QueryFactory(whenThenList), c);
	}

	@Override
	public ValueIterableQuery values() {
		return new DefaultValueIterableQuery(this);
	}

	@Override
	public ValueOneQuery valueAt(int i) {
		return new DefaultValueOneQuery(this, i);
	}

	@Override
	public Query<Parameter, AnteroposteriorRelationship> ar() {
		return AR;
	}

	@Override
	public Query<Parameter, StartDateAdjustment> cftd() {
		return CFTD;
	}

	@Override
	public Query<Parameter, CharSequence> cm() {
		return queryForCharSequence;
	}

	@Override
	public Query<Parameter, ExecutionCycle> cy() {
		return CY;
	}

	@Override
	public Query<Parameter, EndDate> ed() {
		return ED;
	}

	@Override
	public Query<Parameter, EndStatusJudgementType> ej() {
		return EJ;
	}

	@Override
	public Query<Parameter, UnsignedIntegral> ejc() {
		return EJC;
	}

	@Override
	public Query<Parameter, Element> el() {
		return EL;
	}

	@Override
	public Query<Parameter, ElapsedTime> etm() {
		return queryForMinutesInterval;
	}

	@Override
	public Query<Parameter, ExecutionTimedOutStatus> ets() {
		return ETS;
	}

	@Override
	public Query<Parameter, ExecutionUserType> eu() {
		return EU;
	}

	@Override
	public Query<Parameter, EndDelayTime> ey() {
		return EY;
	}

	@Override
	public Query<Parameter, FixedDuration> fd() {
		return FD;
	}

	@Override
	public Query<Parameter, FileWatchCondition> flwc() {
		return FLWC;
	}

	@Override
	public Query<Parameter, ResultJudgmentType> jd() {
		return JD;
	}

	@Override
	public Query<Parameter, LinkedRuleNumber> ln() {
		return LN;
	}

	@Override
	public Query<Parameter, MailAddress> mladr() {
		return MLADR;
	}

	@Override
	public Query<Parameter, CommandLine> sc() {
		return queryForCommandLine;
	}

	@Override
	public Query<Parameter, StartDate> sd() {
		return SD;
	}

	@Override
	public Query<Parameter, WriteOption> sea() {
		return queryForWriteOption;
	}

	@Override
	public Query<Parameter, StartDateCompensation> sh() {
		return SH;
	}

	@Override
	public Query<Parameter, StartDateCompensationDeadline> shd() {
		return SHD;
	}

	@Override
	public Query<Parameter, WriteOption> soa() {
		return queryForWriteOption;
	}

	@Override
	public Query<Parameter, StartTime> st() {
		return ST;
	}

	@Override
	public Query<Parameter, StartDelayTime> sy() {
		return SY;
	}

	@Override
	public Query<Parameter, MapSize> sz() {
		return SZ;
	}

	@Override
	public Query<Parameter, CommandLine> te() {
		return queryForCommandLine;
	}

	@Override
	public Query<Parameter, ExitCodeThreshold> tho() {
		return queryForExitCodeThreshold;
	}

	@Override
	public Query<Parameter, ElapsedTime> tmitv() {
		return queryForMinutesInterval;
	}

	@Override
	public Query<Parameter, DeleteOption> top1() {
		return queryForDeleteOption;
	}

	@Override
	public Query<Parameter, DeleteOption> top2() {
		return queryForDeleteOption;
	}

	@Override
	public Query<Parameter, DeleteOption> top3() {
		return queryForDeleteOption;
	}

	@Override
	public Query<Parameter, DeleteOption> top4() {
		return queryForDeleteOption;
	}

	@Override
	public Query<Parameter, UnitType> ty() {
		return TY;
	}

	@Override
	public Query<Parameter, RunConditionWatchLimitCount> wc() {
		return WC;
	}

	@Override
	public Query<Parameter, RunConditionWatchLimitTime> wt() {
		return WT;
	}

	@Override
	public Query<Parameter, ExitCodeThreshold> wth() {
		return queryForExitCodeThreshold;
	}
	
	/**
	 * ユニット定義パラメータarを読み取ってそのJavaオブジェクト表現を返すクエリ.
	 */
	private static final Query<Parameter,AnteroposteriorRelationship> AR =
			new Query<Parameter,AnteroposteriorRelationship>() {
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
	};
	
	/**
	 * ユニット定義パラメータcyを読み取ってそのJavaオブジェクト表現を返すクエリ.
	 */
	private static final Query<Parameter,ExecutionCycle> CY = 
			new Query<Parameter,ExecutionCycle>() {
		@Override
		public ExecutionCycle queryFrom(final Parameter p) {
			final int valueCount = p.getValues().size();
			final int ruleNumber = valueCount == 1 ? 1 : p.getValues().get(0).query(integer());
			final Tuple cycleNumberAndUnit = p.getValues().get(valueCount == 1 ? 0 : 1).query(tuple());
			final int cycleNumber = Integer.parseInt(cycleNumberAndUnit.get(0).toString());
			final CycleUnit cycleUnit = CycleUnit.valueOfCode(cycleNumberAndUnit.get(1));

			return ExecutionCycle.of(cycleNumber, cycleUnit).at(ruleNumber);
		}
	};
	
	/**
	 * ユニット定義パラメータelを読み取ってそのJavaオブジェクト表現を返すクエリ.
	 */
	private static final Query<Parameter,Element> EL = new Query<Parameter,Element>() {
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
	};
	
	/**
	 * ユニット定義パラメータetsを読み取ってそのJavaオブジェクト表現を返すクエリ.
	 */
	private static final Query<Parameter,ExecutionTimedOutStatus> ETS = 
			new Query<Parameter,ExecutionTimedOutStatus>() {
		@Override
		public ExecutionTimedOutStatus queryFrom(Parameter p) {
			return ExecutionTimedOutStatus.valueOfCode(p.getValues().get(0).query(string()));
		}
	};
	
	/**
	 * ユニット定義パラメータeuを読み取ってそのJavaオブジェクト表現を返すクエリ.
	 */
	private static final Query<Parameter,ExecutionUserType> EU = 
			new Query<Parameter,ExecutionUserType>() {
		@Override
		public ExecutionUserType queryFrom(Parameter p) {
			return ExecutionUserType.valueOfCode(p.getValues().get(0).
					getStringValue().toString());
		}
	};
	
	/**
	 * ユニット定義パラメータfdを読み取ってそのJavaオブジェクト表現を返すクエリ.
	 */
	private static final Query<Parameter,FixedDuration> FD = 
			new Query<Parameter,FixedDuration>() {
		@Override
		public FixedDuration queryFrom(Parameter p) {
			return FixedDuration.of(Integer.parseInt(p.getValues().get(0).getStringValue()));
		}
	};
	
	/**
	 * ユニット定義パラメータflwcを読み取ってそのJavaオブジェクト表現を返すクエリ.
	 */
	private static final Query<Parameter,FileWatchCondition> FLWC = 
			new Query<Parameter,FileWatchCondition>() {
		@Override
		public FileWatchCondition queryFrom(Parameter p) {
			return FileWatchCondition.of(FileWatchConditionFlag
					.valueOfCodes(p.query(queryForCharSequence)));
		}
	};
	
	/**
	 * ユニット定義パラメータjdを読み取ってそのJavaオブジェクト表現を返すクエリ.
	 */
	private static final Query<Parameter,ResultJudgmentType> JD = 
			new Query<Parameter,ResultJudgmentType>() {
		@Override
		public ResultJudgmentType queryFrom(Parameter p) {
			return ResultJudgmentType.valueOfCode(p.getValues().get(0).
					getStringValue().toString());
		}
	};
	
	/**
	 * ユニット定義パラメータlnを読み取ってそのJavaオブジェクト表現を返すクエリ.
	 */
	private static final Query<Parameter,LinkedRuleNumber> LN = 
			new Query<Parameter,LinkedRuleNumber>() {
		@Override
		public LinkedRuleNumber queryFrom(Parameter p) {
			final int valueCount = p.getValues().size();
			final int ruleNumber = valueCount == 1 ? 1 : p.getValues().get(0).query(integer());
			final int targetRuleNumber = p.getValues().get(valueCount == 1 ? 0 : 1).query(integer());
			return LinkedRuleNumber.ofTarget(targetRuleNumber).at(ruleNumber);
		}
	};
	
	/**
	 * ユニット定義パラメータsdを読み取ってそのJavaオブジェクト表現を返すクエリ.
	 */
	private static final Query<Parameter,StartDate> SD = new Query<Parameter,StartDate>() {
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
			final int valueCount = p.getValues().size();
			builder.setRuleNumber(valueCount == 1 
					? RuleNumber.DEFAULT 
					: RuleNumber.of(p.getValues().get(0).query(integer())));
			
			final String maybeYyyyMm = p.getValues().get(valueCount == 1 ? 0 : 1).query(string()).trim();
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
	};
	
	/**
	 * ユニット定義パラメータstを読み取ってそのJavaオブジェクト表現を返すクエリ.
	 */
	private static final Query<Parameter,StartTime> ST = new Query<Parameter,StartTime>() {
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
	};
	
	/**
	 * ユニット定義パラメータshを読み取ってそのJavaオブジェクト表現を返すクエリ.
	 */
	private static final Query<Parameter,StartDateCompensation> SH = 
			new Query<Parameter, StartDateCompensation>() {
		@Override
		public StartDateCompensation queryFrom(Parameter t) {
			System.out.println(t.getValues().size());
			final int valueCount = t.getValues().size();
			final int ruleNumber;
			final String typeCode;
			
			if (valueCount == 1) {
				ruleNumber = 1;
				typeCode = t.getValues().get(0).getStringValue();
			} else {
				ruleNumber = Integer.parseInt(t.getValues().get(0).getStringValue());
				typeCode = t.getValues().get(1).getStringValue();
			}

			return Builders.parameterSH()
					.setRuleNumber(RuleNumber.of(ruleNumber))
					.setMethod(CompensationMethod.valueOfCode(typeCode))
					.build();
		}
	};
	
	/**
	 * ユニット定義パラメータshdを読み取ってそのJavaオブジェクト表現を返すクエリ.
	 */
	private static final Query<Parameter,StartDateCompensationDeadline> SHD = 
			new Query<Parameter, StartDateCompensationDeadline>() {
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
	};
	
	/**
	 * ユニット定義パラメータwtを読み取ってそのJavaオブジェクト表現を返すクエリ.
	 */
	private static final Query<Parameter,RunConditionWatchLimitTime> WT = 
			new Query<Parameter, RunConditionWatchLimitTime>() {
		@Override
		public RunConditionWatchLimitTime queryFrom(Parameter t) {
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
	};
	
	/**
	 * ユニット定義パラメータcftdを読み取ってそのJavaオブジェクト表現を返すクエリ.
	 */
	private static final Query<Parameter,StartDateAdjustment> CFTD = 
			new Query<Parameter, StartDateAdjustment>() {
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
	};
	
	private static final Query<Parameter,EndDate> ED = new Query<Parameter, EndDate>() {
		private final Pattern regex = Pattern.compile("(\\d+)/(\\d+)/(\\d+)");
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
	};

	private static final Query<Parameter,RunConditionWatchLimitCount> WC = 
			new Query<Parameter, RunConditionWatchLimitCount>() {
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
	};
	
	/**
	 * ユニット定義パラメータsyを読み取ってそのJavaオブジェクト表現を返すクエリ.
	 */
	private static final Query<Parameter,StartDelayTime> SY = 
			new Query<Parameter,StartDelayTime>() {
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
	};
	
	/**
	 * ユニット定義パラメータeyを読み取ってそのJavaオブジェクト表現を返すクエリ.
	 */
	private static final Query<Parameter,EndDelayTime> EY = 
			new Query<Parameter,EndDelayTime>() {
		@Override
		public EndDelayTime queryFrom(Parameter p) {
			// ey=[N,]hh:mm|{M|U|C}mmmm;
			
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
	private static final Query<Parameter,MapSize> SZ =
			new Query<Parameter,MapSize>() {
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
		
	};
	
	/**
	 * ユニット定義パラメータtyを読み取ってそのJavaオブジェクト表現を返すクエリ.
	 */
	private static final Query<Parameter,UnitType> TY = 
			new Query<Parameter,UnitType>() {
		@Override
		public UnitType queryFrom(Parameter p) {
			return UnitType.valueOfCode(p.getValues().get(0)
					.getStringValue().toString());
		}
	};
	
	/**
	 * ユニット定義パラメータejを読み取ってそのJavaオブジェクト表現を返すクエリ.
	 */
	private static final Query<Parameter,EndStatusJudgementType> EJ = 
		new Query<Parameter,EndStatusJudgementType>() {
			@Override
			public EndStatusJudgementType queryFrom(Parameter p) {
				return EndStatusJudgementType.valueOfCode(p.getValues().get(0)
						.getStringValue().toString());
			}
	};
	
	/**
	 * ユニット定義パラメータejcを読み取ってそのJavaオブジェクト表現を返すクエリ.
	 */
	private static final Query<Parameter,UnsignedIntegral> EJC = 
			new Query<Parameter,UnsignedIntegral>() {
		@Override
		public UnsignedIntegral queryFrom(Parameter p) {
			return UnsignedIntegral.of(p.getValues().get(0)
					.query(ParameterValueQueries.longInteger()));
		}
	};
	
	/**
	 * ユニット定義パラメータmladrを読み取ってそのJavaオブジェクト表現を返すクエリ.
	 */
	private static final Query<Parameter,MailAddress> MLADR =
			new Query<Parameter,MailAddress>() {
		private final Pattern pat = Pattern.compile("^(to|cc|bcc):\"(.+)\"$");
		@Override
		public MailAddress queryFrom(Parameter p) {
			final Matcher mat = pat.matcher(p.getValues().get(0).getStringValue());
			if (mat.matches()) {
				final MailAddressType type = MailAddressType.valueOfCode(mat.group(1));
				final String address = CharSequenceUtils.unescape(mat.group(2)).toString();
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
			throw illegalArgument("Invalid mladr value (%s).", p.getValues().get(0));
		}
	};
	
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
}

