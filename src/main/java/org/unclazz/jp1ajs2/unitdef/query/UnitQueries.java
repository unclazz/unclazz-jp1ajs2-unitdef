package org.unclazz.jp1ajs2.unitdef.query;

import java.util.Collections;
import java.util.List;

import org.unclazz.jp1ajs2.unitdef.Parameter;
import org.unclazz.jp1ajs2.unitdef.Unit;
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
import org.unclazz.jp1ajs2.unitdef.util.UnsignedIntegral;

/**
 * {@link UnitQuery}オブジェクトのためのユーティリティ.
 * 各種の定義済みクエリを提供する。
 */
public final class UnitQueries {
	private UnitQueries() {}
	
	public static RelativeUnitsQuery children = new ChildUnitsQuery();
	public static RelativeUnitsQuery descendants = new DescendantUnitsQuery();
	
	private static final SubUnitQueryFactory subUnitQueryFactory = new SubUnitQueryFactory();
	
	/**
	 * ユニット定義パラメータのリストを返すクエリを返す.
	 * <p>このクエリは{@link NameSpecifiedParameterQuery}のインスタンスであり、
	 * そのメソッド{@link NameSpecifiedParameterQuery#valueAt(int)}を呼び出すことで、
	 * {@link SubscriptedQueryFactory}のインスタンスが得られる。
	 * {@link SubscriptedQueryFactory}は整数値や真偽値、正規表現パターンマッチ結果など
	 * 種々の形式でユニット定義パラメータの値にアクセスを提供するクエリを生成するファクトリである。</p>
	 * <p>以上からこのメソッドの戻り値には2通りの利用方法がある：</p>
	 * <ol>
	 * <li>特定の名前を持つユニット定義パラメータのリストを取得する方法
	 * （{@link NameSpecifiedParameterQuery}をそのまま{@link Unit#query(UnitQuery)}に渡す）</li>
	 * <li>特定の名前を持つユニット定義パラメータの特定位置の値を、
	 * 整数値や真偽値、正規表現パターンマッチ結果などの形式で取得する方法
	 * （{@link SubscriptedQueryFactory}を{@link Unit#query(UnitQuery)}に渡す）</li>
	 * </ol>
	 * 
	 * 引数で指定された名前のパラメータが存在しなかった場合、クエリは空のリストを返す。
	 * @param paramName ユニット定義パラメータ名
	 * @return ユニット定義パラメータのリスト
	 */
	public static NameSpecifiedParameterQuery parameter(final String paramName) {
		return NameSpecifiedParameterQuery.parameter(paramName);
	}
	
	/**
	 * サブユニットのリストを返すクエリを返す.
	 * 引数で指定された名前のサブユニットが存在しなかった場合、クエリは空のリストを返す。
	 * 存在した場合は単一要素からなるリストを返す。
	 * @param name サブユニット名
	 * @return サブユニットのリスト
	 */
	public static ListUnitQuery<Unit> subUnit(final String name) {
		return new ListUnitQuery<Unit>() {
			@Override
			public List<Unit> queryFrom(Unit unit) {
				for (final Unit subUnit : unit.getSubUnits()) {
					if (subUnit.getName().equals(name)) {
						return Collections.singletonList(subUnit);
					}
				}
				return Collections.emptyList();
			}
		};
	}
	/**
	 * サブユニットにアクセスするためのクエリのファクトリを返す.
	 * @return サブユニットのリスト
	 */
	public static SubUnitQueryFactory subUnit() {
		return subUnitQueryFactory;
	}
	/**
	 * ユニット定義パラメータに対し{@link ParameterQuery#queryFrom(Parameter)}を適用した結果を返すクエリを返す.
	 * {@link ParameterQuery#queryFrom(Parameter)}の結果が{@code null}だった場合、
	 * その値は結果リストには加えられない。
	 * @param <R> クエリにより返される値の型
	 * @param paramName パラメータ名
	 * @param paramQuery ユニット定義パラメータ・クエリ
	 * @return ユニット定義パラメータ適用結果のリスト
	 */
	public static<R> ListUnitQuery<R> parameter(final String paramName, final ParameterQuery<R> paramQuery) {
		return new UnitParameterQuery<R>(paramName, paramQuery);
	}
	
	private static<R> ListUnitQuery<R> singleParameter(
			final String paramName, final ParameterQuery<R> paramQuery) {
		return new UnitSingleParameterQuery<R>(paramName, paramQuery);
	}
	
	/**
	 * ユニット定義パラメータarのJavaオブジェクト表現を取得するためのクエリを返す.
	 * @return クエリ・インスタンス
	 */
	public static final ListUnitQuery<AnteroposteriorRelationship> ar() {
		return parameter("ar", ParameterQueries.AR);
	}
	
	/**
	 * ユニット定義パラメータcmのJavaオブジェクト表現を取得するためのクエリを返す.
	 * @return クエリ・インスタンス
	 */
	public static final ListUnitQuery<CharSequence> cm() {
		return singleParameter("cm", ParameterQueries.CM);
	}
	
	/**
	 * ユニット定義パラメータcyのJavaオブジェクト表現を取得するためのクエリを返す.
	 * @return クエリ・インスタンス
	 */
	public static final ListUnitQuery<ExecutionCycle> cy() {
		return parameter("cy", ParameterQueries.CY);
	}
	
	/**
	 * ユニット定義パラメータelのJavaオブジェクト表現を取得するためのクエリを返す.
	 * @return クエリ・インスタンス
	 */
	public static final ListUnitQuery<Element> el() {
		return parameter("el", ParameterQueries.EL);
	}
	
	/**
	 * ユニット定義パラメータeuのJavaオブジェクト表現を取得するためのクエリを返す.
	 * @return クエリ・インスタンス
	 */
	public static final ListUnitQuery<ExecutionUserType> eu() {
		return singleParameter("eu", ParameterQueries.EU);
	}
	
	/**
	 * ユニット定義パラメータetsのJavaオブジェクト表現を取得するためのクエリを返す.
	 * @return クエリ・インスタンス
	 */
	public static final ListUnitQuery<ExecutionTimedOutStatus> ets() {
		return singleParameter("ets", ParameterQueries.ETS);
	}
	
	/**
	 * ユニット定義パラメータeyのJavaオブジェクト表現を取得するためのクエリを返す.
	 * @return クエリ・インスタンス
	 */
	public static final ListUnitQuery<EndDelayTime> ey() {
		return parameter("ey", ParameterQueries.EY);
	}
	
	/**
	 * ユニット定義パラメータfdのJavaオブジェクト表現を取得するためのクエリを返す.
	 * @return クエリ・インスタンス
	 */
	public static final ListUnitQuery<FixedDuration> fd() {
		return singleParameter("fd", ParameterQueries.FD);
	}
	
	/**
	 * ユニット定義パラメータflwcのJavaオブジェクト表現を取得するためのクエリを返す.
	 * @return クエリ・インスタンス
	 */
	public static final ListUnitQuery<FileWatchingConditionSet> flwc() {
		return singleParameter("flwc", ParameterQueries.FLWC);
	}
	
	/**
	 * ユニット定義パラメータjdのJavaオブジェクト表現を取得するためのクエリを返す.
	 * @return クエリ・インスタンス
	 */
	public static final ListUnitQuery<ResultJudgmentType> jd() {
		return singleParameter("jd", ParameterQueries.JD);
	}
	
	/**
	 * ユニット定義パラメータlnのJavaオブジェクト表現を取得するためのクエリを返す.
	 * @return クエリ・インスタンス
	 */
	public static final ListUnitQuery<LinkedRuleNumber> ln() {
		return parameter("ln", ParameterQueries.LN);
	}
	
	/**
	 * ユニット定義パラメータscのJavaオブジェクト表現を取得するためのクエリを返す.
	 * @return クエリ・インスタンス
	 */
	public static final ListUnitQuery<CommandLine> sc() {
		return singleParameter("sc", ParameterQueries.SC);
	}
	
	/**
	 * ユニット定義パラメータsdのJavaオブジェクト表現を取得するためのクエリを返す.
	 * @return クエリ・インスタンス
	 */
	public static final ListUnitQuery<StartDate> sd() {
		return parameter("sd", ParameterQueries.SD);
	}
	
	/**
	 * ユニット定義パラメータseaのJavaオブジェクト表現を取得するためのクエリを返す.
	 * @return クエリ・インスタンス
	 */
	public static final ListUnitQuery<WriteOption> sea() {
		return singleParameter("sea", ParameterQueries.SEA);
	}
	
	/**
	 * ユニット定義パラメータsoaのJavaオブジェクト表現を取得するためのクエリを返す.
	 * @return クエリ・インスタンス
	 */
	public static final ListUnitQuery<WriteOption> soa() {
		return singleParameter("soa", ParameterQueries.SOA);
	}
	
	/**
	 * ユニット定義パラメータstのJavaオブジェクト表現を取得するためのクエリを返す.
	 * @return クエリ・インスタンス
	 */
	public static final ListUnitQuery<StartTime> st() {
		return parameter("st", ParameterQueries.ST);
	}
	
	/**
	 * ユニット定義パラメータsyのJavaオブジェクト表現を取得するためのクエリを返す.
	 * @return クエリ・インスタンス
	 */
	public static final ListUnitQuery<StartDelayTime> sy() {
		return parameter("sy", ParameterQueries.SY);
	}
	
	/**
	 * ユニット定義パラメータszのJavaオブジェクト表現を取得するためのクエリを返す.
	 * @return クエリ・インスタンス
	 */
	public static final ListUnitQuery<MapSize> sz() {
		return singleParameter("sz", ParameterQueries.SZ);
	}
	
	/**
	 * ユニット定義パラメータthoのJavaオブジェクト表現を取得するためのクエリを返す.
	 * @return クエリ・インスタンス
	 */
	public static final ListUnitQuery<ExitCodeThreshold> tho() {
		return singleParameter("tho", ParameterQueries.THO);
	}
	
	/**
	 * ユニット定義パラメータtmitvのJavaオブジェクト表現を取得するためのクエリを返す.
	 * @return クエリ・インスタンス
	 */
	public static final ListUnitQuery<ElapsedTime> tmitv() {
		return singleParameter("tmitv", ParameterQueries.TMITV);
	}
	
	/**
	 * ユニット定義パラメータtop1のJavaオブジェクト表現を取得するためのクエリを返す.
	 * @return クエリ・インスタンス
	 */
	public static final ListUnitQuery<DeleteOption> top1() {
		return singleParameter("top1", ParameterQueries.TOP1);
	}
	
	/**
	 * ユニット定義パラメータtop2のJavaオブジェクト表現を取得するためのクエリを返す.
	 * @return クエリ・インスタンス
	 */
	public static final ListUnitQuery<DeleteOption> top2() {
		return singleParameter("top2", ParameterQueries.TOP2);
	}
	
	/**
	 * ユニット定義パラメータtop3のJavaオブジェクト表現を取得するためのクエリを返す.
	 * @return クエリ・インスタンス
	 */
	public static final ListUnitQuery<DeleteOption> top3() {
		return singleParameter("top3", ParameterQueries.TOP3);
	}
	
	/**
	 * ユニット定義パラメータtop4のJavaオブジェクト表現を取得するためのクエリを返す.
	 * @return クエリ・インスタンス
	 */
	public static final ListUnitQuery<DeleteOption> top4() {
		return singleParameter("top4", ParameterQueries.TOP4);
	}
	
	/**
	 * ユニット定義パラメータtyのJavaオブジェクト表現を取得するためのクエリを返す.
	 * @return クエリ・インスタンス
	 */
	public static final ListUnitQuery<UnitType> ty() {
		return singleParameter("ty", ParameterQueries.TY);
	}
	
	/**
	 * ユニット定義パラメータwthのJavaオブジェクト表現を取得するためのクエリを返す.
	 * @return クエリ・インスタンス
	 */
	public static final ListUnitQuery<ExitCodeThreshold> wth() {
		return singleParameter("wth", ParameterQueries.WTH);
	}

	/**
	 * ユニット定義パラメータejのJavaオブジェクト表現を取得するためのクエリを返す.
	 * @return クエリ・インスタンス
	 */
	public static final ListUnitQuery<EndStatusJudgementType> ej() {
		return singleParameter("ej", ParameterQueries.EJ);
	}
	
	/**
	 * ユニット定義パラメータejcのJavaオブジェクト表現を取得するためのクエリを返す.
	 * @return クエリ・インスタンス
	 */
	public static final ListUnitQuery<UnsignedIntegral> ejc() {
		return singleParameter("ejc", ParameterQueries.EJC);
	}
	
	/**
	 * ユニット定義パラメータelmのJavaオブジェクト表現を取得するためのクエリを返す.
	 * @return クエリ・インスタンス
	 */
	public static final ListUnitQuery<ElapsedTime> etm() {
		return singleParameter("etm", ParameterQueries.ETM);
	}
	
	/**
	 * ユニット定義パラメータmladrのJavaオブジェクト表現を取得するためのクエリを返す.
	 * @return クエリ・インスタンス
	 */
	public static final ListUnitQuery<MailAddress> mladr() {
		return parameter("mladr", ParameterQueries.MLADR);
	}
	
	/**
	 * ユニット定義パラメータteのJavaオブジェクト表現を取得するためのクエリを返す.
	 * @return クエリ・インスタンス
	 */
	public static final ListUnitQuery<CommandLine> te() {
		return singleParameter("te", ParameterQueries.TE);
	}
}
