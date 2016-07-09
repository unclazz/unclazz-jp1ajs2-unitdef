package org.unclazz.jp1ajs2.unitdef.query;

import org.unclazz.jp1ajs2.unitdef.parameter.AnteroposteriorRelationship;
import org.unclazz.jp1ajs2.unitdef.parameter.CommandLine;
import org.unclazz.jp1ajs2.unitdef.parameter.DeleteOption;
import org.unclazz.jp1ajs2.unitdef.parameter.Element;
import org.unclazz.jp1ajs2.unitdef.parameter.EndDelayTime;
import org.unclazz.jp1ajs2.unitdef.parameter.EndStatusJudgementType;
import org.unclazz.jp1ajs2.unitdef.parameter.ExecutionCycle;
import org.unclazz.jp1ajs2.unitdef.parameter.ExecutionTimedOutStatus;
import org.unclazz.jp1ajs2.unitdef.parameter.ExecutionUserType;
import org.unclazz.jp1ajs2.unitdef.parameter.ExitCodeThreshold;
import org.unclazz.jp1ajs2.unitdef.parameter.FileWatchingConditionSet;
import org.unclazz.jp1ajs2.unitdef.parameter.FixedDuration;
import org.unclazz.jp1ajs2.unitdef.parameter.LinkedRuleNumber;
import org.unclazz.jp1ajs2.unitdef.parameter.MailAddress;
import org.unclazz.jp1ajs2.unitdef.parameter.MapSize;
import org.unclazz.jp1ajs2.unitdef.parameter.ElapsedTime;
import org.unclazz.jp1ajs2.unitdef.parameter.ResultJudgmentType;
import org.unclazz.jp1ajs2.unitdef.parameter.StartDate;
import org.unclazz.jp1ajs2.unitdef.parameter.StartDelayTime;
import org.unclazz.jp1ajs2.unitdef.parameter.StartTime;
import org.unclazz.jp1ajs2.unitdef.parameter.UnitType;
import org.unclazz.jp1ajs2.unitdef.parameter.WriteOption;
import org.unclazz.jp1ajs2.unitdef.util.Function;
import org.unclazz.jp1ajs2.unitdef.util.UnitTreeNodesIterable;
import org.unclazz.jp1ajs2.unitdef.util.UnsignedIntegral;

import org.unclazz.jp1ajs2.unitdef.Parameter;
import org.unclazz.jp1ajs2.unitdef.Unit;

/**
 * ユニットを問合せ対象とするクエリのためのユーティリティ.
 */
public final class UnitQueries {
	private UnitQueries() {}
	
	private static final UnitIterableQuery children 
	= new UnitIterableQuery(new Function<Unit, Iterable<Unit>>() {
		@Override
		public Iterable<Unit> apply(final Unit t) {
			return t.getSubUnits();
		}
	});
	private static final UnitIterableQuery descendants 
	= new UnitIterableQuery(new Function<Unit, Iterable<Unit>>() {
		@Override
		public Iterable<Unit> apply(final Unit t) {
			return UnitTreeNodesIterable.ofBreadthFirst(t, false);
		}
	});
	private static final UnitIterableQuery descendantsDepthFirst 
	= new UnitIterableQuery(new Function<Unit, Iterable<Unit>>() {
		@Override
		public Iterable<Unit> apply(final Unit t) {
			return UnitTreeNodesIterable.ofDepthFirst(t, false);
		}
	});
	private static final UnitIterableQuery itSelfAndDescendants 
	= new UnitIterableQuery(new Function<Unit, Iterable<Unit>>() {
		@Override
		public Iterable<Unit> apply(final Unit t) {
			return UnitTreeNodesIterable.ofBreadthFirst(t, true);
		}
	});
	private static final UnitIterableQuery itSelfAndDescendantsDepthFirst 
	= new UnitIterableQuery(new Function<Unit, Iterable<Unit>>() {
		@Override
		public Iterable<Unit> apply(final Unit t) {
			return UnitTreeNodesIterable.ofDepthFirst(t, true);
		}
	});
	private static final ParameterIterableQuery parameters
	= new ParameterIterableQuery(new UnitIterableQuery(new Function<Unit, Iterable<Unit>>() {
		@Override
		public Iterable<Unit> apply(final Unit t) {
			return IdQuery.<Unit>getInstance().queryFrom(t);
		}
	}));
	
	/**
	 * 子ユニット（直接の下位ユニット）を問合せるクエリを返す.
	 * @return クエリ
	 */
	public static UnitIterableQuery children() {
		return children;
	}
	/**
	 * 子孫ユニット（直接・間接の下位ユニット）を問合せるクエリを返す.
	 * <p>ユニットの探索は幅優先に行われる。</p>
	 * @return クエリ
	 */
	public static UnitIterableQuery descendants() {
		return descendants;
	}
	/**
	 * 子孫ユニット（直接・間接の下位ユニット）を問合せるクエリを返す.
	 * <p>ユニットの探索を幅優先で行うか深さ優先で行うかはパラメータで制御できる。</p>
	 * @param depthFirst {@code true}の場合 深さ優先で探索する
	 * @return クエリ
	 */
	public static UnitIterableQuery descendants(final boolean depthFirst) {
		return depthFirst ? descendantsDepthFirst : descendants;
	}
	/**
	 * そのユニット自身と子孫ユニットを問合せるクエリを返す.
	 * <p>ユニットの探索は幅優先に行われる。</p>
	 * @return クエリ
	 */
	public static UnitIterableQuery itSelfAndDescendants() {
		return itSelfAndDescendants;
	}
	/**
	 * そのユニット自身と子孫ユニットを問合せるクエリを返す.
	 * <p>ユニットの探索を幅優先で行うか深さ優先で行うかはパラメータで制御できる。</p>
	 * @param depthFirst {@code true}の場合 深さ優先で探索する
	 * @return クエリ
	 */
	public static UnitIterableQuery itSelfAndDescendants(final boolean depthFirst) {
		return depthFirst ? itSelfAndDescendantsDepthFirst : itSelfAndDescendants;
	}
	/**
	 * そのユニットのユニット定義パラメータを問合せるクエリを返す.
	 * @return クエリ
	 */
	public static ParameterIterableQuery parameters() {
		return parameters;
	}
	
	/**
	 * ユニット定義パラメータarのJavaオブジェクト表現を取得するためのクエリを返す.
	 * @return クエリ・インスタンス
	 */
	public static final TypedValueIterableQuery<Unit, Parameter, AnteroposteriorRelationship> ar() {
		
		return parameters().nameEquals("ar").query(ParameterQueries.AR);
	}
	
	/**
	 * ユニット定義パラメータcmのJavaオブジェクト表現を取得するためのクエリを返す.
	 * @return クエリ・インスタンス
	 */
	public static final TypedValueIterableQuery<Unit, Parameter, CharSequence> cm() {
		return parameters().nameEquals("cm").query(ParameterQueries.CM);
	}
	
	/**
	 * ユニット定義パラメータcyのJavaオブジェクト表現を取得するためのクエリを返す.
	 * @return クエリ・インスタンス
	 */
	public static final TypedValueIterableQuery<Unit, Parameter, ExecutionCycle> cy() {
		return parameters().nameEquals("cy").query(ParameterQueries.CY);
	}
	
	/**
	 * ユニット定義パラメータelのJavaオブジェクト表現を取得するためのクエリを返す.
	 * @return クエリ・インスタンス
	 */
	public static final TypedValueIterableQuery<Unit, Parameter, Element> el() {
		return parameters().nameEquals("el").query(ParameterQueries.EL);
	}
	
	/**
	 * ユニット定義パラメータeuのJavaオブジェクト表現を取得するためのクエリを返す.
	 * @return クエリ・インスタンス
	 */
	public static final TypedValueIterableQuery<Unit, Parameter, ExecutionUserType> eu() {
		return parameters().nameEquals("eu").query(ParameterQueries.EU);
	}
	
	/**
	 * ユニット定義パラメータetsのJavaオブジェクト表現を取得するためのクエリを返す.
	 * @return クエリ・インスタンス
	 */
	public static final TypedValueIterableQuery<Unit, Parameter, ExecutionTimedOutStatus> ets() {
		return parameters().nameEquals("ets").query(ParameterQueries.ETS);
	}
	
	/**
	 * ユニット定義パラメータeyのJavaオブジェクト表現を取得するためのクエリを返す.
	 * @return クエリ・インスタンス
	 */
	public static final TypedValueIterableQuery<Unit, Parameter, EndDelayTime> ey() {
		return parameters().nameEquals("ey").query(ParameterQueries.EY);
	}
	
	/**
	 * ユニット定義パラメータfdのJavaオブジェクト表現を取得するためのクエリを返す.
	 * @return クエリ・インスタンス
	 */
	public static final TypedValueIterableQuery<Unit, Parameter, FixedDuration> fd() {
		return parameters().nameEquals("fd").query(ParameterQueries.FD);
	}
	
	/**
	 * ユニット定義パラメータflwcのJavaオブジェクト表現を取得するためのクエリを返す.
	 * @return クエリ・インスタンス
	 */
	public static final TypedValueIterableQuery<Unit, Parameter, FileWatchingConditionSet> flwc() {
		return parameters().nameEquals("flwc").query(ParameterQueries.FLWC);
	}
	
	/**
	 * ユニット定義パラメータjdのJavaオブジェクト表現を取得するためのクエリを返す.
	 * @return クエリ・インスタンス
	 */
	public static final TypedValueIterableQuery<Unit, Parameter, ResultJudgmentType> jd() {
		return parameters().nameEquals("jd").query(ParameterQueries.JD);
	}
	
	/**
	 * ユニット定義パラメータlnのJavaオブジェクト表現を取得するためのクエリを返す.
	 * @return クエリ・インスタンス
	 */
	public static final TypedValueIterableQuery<Unit, Parameter, LinkedRuleNumber> ln() {
		return parameters().nameEquals("ln").query(ParameterQueries.LN);
	}
	
	/**
	 * ユニット定義パラメータscのJavaオブジェクト表現を取得するためのクエリを返す.
	 * @return クエリ・インスタンス
	 */
	public static final TypedValueIterableQuery<Unit, Parameter, CommandLine> sc() {
		return parameters().nameEquals("sc").query(ParameterQueries.SC);
	}
	
	/**
	 * ユニット定義パラメータsdのJavaオブジェクト表現を取得するためのクエリを返す.
	 * @return クエリ・インスタンス
	 */
	public static final TypedValueIterableQuery<Unit, Parameter, StartDate> sd() {
		return parameters().nameEquals("sd").query(ParameterQueries.SD);
	}
	
	/**
	 * ユニット定義パラメータseaのJavaオブジェクト表現を取得するためのクエリを返す.
	 * @return クエリ・インスタンス
	 */
	public static final TypedValueIterableQuery<Unit, Parameter, WriteOption> sea() {
		return parameters().nameEquals("sea").query(ParameterQueries.SEA);
	}
	
	/**
	 * ユニット定義パラメータsoaのJavaオブジェクト表現を取得するためのクエリを返す.
	 * @return クエリ・インスタンス
	 */
	public static final TypedValueIterableQuery<Unit, Parameter, WriteOption> soa() {
		return parameters().nameEquals("soa").query(ParameterQueries.SOA);
	}
	
	/**
	 * ユニット定義パラメータstのJavaオブジェクト表現を取得するためのクエリを返す.
	 * @return クエリ・インスタンス
	 */
	public static final TypedValueIterableQuery<Unit, Parameter, StartTime> st() {
		return parameters().nameEquals("st").query(ParameterQueries.ST);
	}
	
	/**
	 * ユニット定義パラメータsyのJavaオブジェクト表現を取得するためのクエリを返す.
	 * @return クエリ・インスタンス
	 */
	public static final TypedValueIterableQuery<Unit, Parameter, StartDelayTime> sy() {
		return parameters().nameEquals("sy").query(ParameterQueries.SY);
	}
	
	/**
	 * ユニット定義パラメータszのJavaオブジェクト表現を取得するためのクエリを返す.
	 * @return クエリ・インスタンス
	 */
	public static final TypedValueIterableQuery<Unit, Parameter, MapSize> sz() {
		return parameters().nameEquals("sz").query(ParameterQueries.SZ);
	}
	
	/**
	 * ユニット定義パラメータthoのJavaオブジェクト表現を取得するためのクエリを返す.
	 * @return クエリ・インスタンス
	 */
	public static final TypedValueIterableQuery<Unit, Parameter, ExitCodeThreshold> tho() {
		return parameters().nameEquals("tho").query(ParameterQueries.THO);
	}
	
	/**
	 * ユニット定義パラメータtmitvのJavaオブジェクト表現を取得するためのクエリを返す.
	 * @return クエリ・インスタンス
	 */
	public static final TypedValueIterableQuery<Unit, Parameter, ElapsedTime> tmitv() {
		return parameters().nameEquals("tmitv").query(ParameterQueries.TMITV);
	}
	
	/**
	 * ユニット定義パラメータtop1のJavaオブジェクト表現を取得するためのクエリを返す.
	 * @return クエリ・インスタンス
	 */
	public static final TypedValueIterableQuery<Unit, Parameter, DeleteOption> top1() {
		return parameters().nameEquals("top1").query(ParameterQueries.TOP1);
	}
	
	/**
	 * ユニット定義パラメータtop2のJavaオブジェクト表現を取得するためのクエリを返す.
	 * @return クエリ・インスタンス
	 */
	public static final TypedValueIterableQuery<Unit, Parameter, DeleteOption> top2() {
		return parameters().nameEquals("top2").query(ParameterQueries.TOP2);
	}
	
	/**
	 * ユニット定義パラメータtop3のJavaオブジェクト表現を取得するためのクエリを返す.
	 * @return クエリ・インスタンス
	 */
	public static final TypedValueIterableQuery<Unit, Parameter, DeleteOption> top3() {
		return parameters().nameEquals("top3").query(ParameterQueries.TOP3);
	}
	
	/**
	 * ユニット定義パラメータtop4のJavaオブジェクト表現を取得するためのクエリを返す.
	 * @return クエリ・インスタンス
	 */
	public static final TypedValueIterableQuery<Unit, Parameter, DeleteOption> top4() {
		return parameters().nameEquals("top4").query(ParameterQueries.TOP4);
	}
	
	/**
	 * ユニット定義パラメータtyのJavaオブジェクト表現を取得するためのクエリを返す.
	 * @return クエリ・インスタンス
	 */
	public static final TypedValueIterableQuery<Unit, Parameter, UnitType> ty() {
		return parameters().nameEquals("ty").query(ParameterQueries.TY);
	}
	
	/**
	 * ユニット定義パラメータwthのJavaオブジェクト表現を取得するためのクエリを返す.
	 * @return クエリ・インスタンス
	 */
	public static final TypedValueIterableQuery<Unit, Parameter, ExitCodeThreshold> wth() {
		return parameters().nameEquals("wth").query(ParameterQueries.WTH);
	}

	/**
	 * ユニット定義パラメータejのJavaオブジェクト表現を取得するためのクエリを返す.
	 * @return クエリ・インスタンス
	 */
	public static final TypedValueIterableQuery<Unit, Parameter, EndStatusJudgementType> ej() {
		return parameters().nameEquals("ej").query(ParameterQueries.EJ);
	}
	
	/**
	 * ユニット定義パラメータejcのJavaオブジェクト表現を取得するためのクエリを返す.
	 * @return クエリ・インスタンス
	 */
	public static final TypedValueIterableQuery<Unit, Parameter, UnsignedIntegral> ejc() {
		return parameters().nameEquals("ejc").query(ParameterQueries.EJC);
	}
	
	/**
	 * ユニット定義パラメータelmのJavaオブジェクト表現を取得するためのクエリを返す.
	 * @return クエリ・インスタンス
	 */
	public static final TypedValueIterableQuery<Unit, Parameter, ElapsedTime> etm() {
		return parameters().nameEquals("etm").query(ParameterQueries.ETM);
	}
	
	/**
	 * ユニット定義パラメータmladrのJavaオブジェクト表現を取得するためのクエリを返す.
	 * @return クエリ・インスタンス
	 */
	public static final TypedValueIterableQuery<Unit, Parameter, MailAddress> mladr() {
		return parameters().nameEquals("mladr").query(ParameterQueries.MLADR);
	}
	
	/**
	 * ユニット定義パラメータteのJavaオブジェクト表現を取得するためのクエリを返す.
	 * @return クエリ・インスタンス
	 */
	public static final TypedValueIterableQuery<Unit, Parameter, CommandLine> te() {
		return parameters().nameEquals("te").query(ParameterQueries.TE);
	}
}
