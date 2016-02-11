package org.unclazz.jp1ajs2.unitdef;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

import org.unclazz.jp1ajs2.unitdef.parameter.AnteroposteriorRelationship;
import org.unclazz.jp1ajs2.unitdef.parameter.CommandLine;
import org.unclazz.jp1ajs2.unitdef.parameter.DeleteOption;
import org.unclazz.jp1ajs2.unitdef.parameter.Element;
import org.unclazz.jp1ajs2.unitdef.parameter.EndDelayTime;
import org.unclazz.jp1ajs2.unitdef.parameter.EndStatusJudgementType;
import org.unclazz.jp1ajs2.unitdef.parameter.EnvironmentVariable;
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
import org.unclazz.jp1ajs2.unitdef.parameter.UnsignedIntegral;
import org.unclazz.jp1ajs2.unitdef.parameter.WriteOption;

/**
 * {@link UnitQuery}オブジェクトのためのユーティリティ.
 * 各種の定義済みクエリを提供する。
 */
public final class UnitQueries {
	private UnitQueries() {}
	
	private static final<T> List<T> list() {
		return new LinkedList<T>();
	}
	
	/**
	 * ユニット定義パラメータのリストを返すクエリを返す.
	 * <p>このクエリは{@link NameSpecifiedParameterQuery}のインスタンスであり、
	 * そのメソッド{@link NameSpecifiedParameterQuery#item(int)}を呼び出すことで、
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
	public static UnitQuery<Unit> subUnit(final String name) {
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
	
	/**
	 * ユニット定義パラメータに対し{@link ParameterQuery#queryFrom(Parameter)}を適用した結果を返すクエリを返す.
	 * {@link ParameterQuery#queryFrom(Parameter)}の結果が{@code null}だった場合、
	 * その値は結果リストには加えられない。
	 * @param <T> クエリにより返される値の型
	 * @param paramName パラメータ名
	 * @param paramQuery ユニット定義パラメータ・クエリ
	 * @return ユニット定義パラメータ適用結果のリスト
	 */
	public static<T> UnitQuery<T> parameter(final String paramName, final ParameterQuery<T> paramQuery) {
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
	
	/**
	 * ユニット定義パラメータarのJavaオブジェクト表現を取得するためのクエリを返す.
	 * @return クエリ・インスタンス
	 */
	public static final UnitQuery<AnteroposteriorRelationship> ar() {
		return parameter("ar", ParameterQueries.AR);
	}
	
	/**
	 * ユニット定義パラメータcmのJavaオブジェクト表現を取得するためのクエリを返す.
	 * @return クエリ・インスタンス
	 */
	public static final UnitQuery<CharSequence> cm() {
		return parameter("cm", ParameterQueries.CM);
	}
	
	/**
	 * ユニット定義パラメータcyのJavaオブジェクト表現を取得するためのクエリを返す.
	 * @return クエリ・インスタンス
	 */
	public static final UnitQuery<ExecutionCycle> cy() {
		return parameter("cy", ParameterQueries.CY);
	}
	
	/**
	 * ユニット定義パラメータelのJavaオブジェクト表現を取得するためのクエリを返す.
	 * @return クエリ・インスタンス
	 */
	public static final UnitQuery<Element> el() {
		return parameter("el", ParameterQueries.EL);
	}
	
	/**
	 * ユニット定義パラメータeuのJavaオブジェクト表現を取得するためのクエリを返す.
	 * @return クエリ・インスタンス
	 */
	public static final UnitQuery<ExecutionUserType> eu() {
		return parameter("eu", ParameterQueries.EU);
	}
	
	/**
	 * ユニット定義パラメータetsのJavaオブジェクト表現を取得するためのクエリを返す.
	 * @return クエリ・インスタンス
	 */
	public static final UnitQuery<ExecutionTimedOutStatus> ets() {
		return parameter("ets", ParameterQueries.ETS);
	}
	
	/**
	 * ユニット定義パラメータeyのJavaオブジェクト表現を取得するためのクエリを返す.
	 * @return クエリ・インスタンス
	 */
	public static final UnitQuery<EndDelayTime> ey() {
		return parameter("ey", ParameterQueries.EY);
	}
	
	/**
	 * ユニット定義パラメータfdのJavaオブジェクト表現を取得するためのクエリを返す.
	 * @return クエリ・インスタンス
	 */
	public static final UnitQuery<FixedDuration> fd() {
		return parameter("fd", ParameterQueries.FD);
	}
	
	/**
	 * ユニット定義パラメータflwcのJavaオブジェクト表現を取得するためのクエリを返す.
	 * @return クエリ・インスタンス
	 */
	public static final UnitQuery<FileWatchingConditionSet> flwc() {
		return parameter("flwc", ParameterQueries.FLWC);
	}
	
	/**
	 * ユニット定義パラメータjdのJavaオブジェクト表現を取得するためのクエリを返す.
	 * @return クエリ・インスタンス
	 */
	public static final UnitQuery<ResultJudgmentType> jd() {
		return parameter("jd", ParameterQueries.JD);
	}
	
	/**
	 * ユニット定義パラメータlnのJavaオブジェクト表現を取得するためのクエリを返す.
	 * @return クエリ・インスタンス
	 */
	public static final UnitQuery<LinkedRuleNumber> ln() {
		return parameter("ln", ParameterQueries.LN);
	}
	
	/**
	 * ユニット定義パラメータscのJavaオブジェクト表現を取得するためのクエリを返す.
	 * @return クエリ・インスタンス
	 */
	public static final UnitQuery<CommandLine> sc() {
		return parameter("sc", ParameterQueries.SC);
	}
	
	/**
	 * ユニット定義パラメータsdのJavaオブジェクト表現を取得するためのクエリを返す.
	 * @return クエリ・インスタンス
	 */
	public static final UnitQuery<StartDate> sd() {
		return parameter("sd", ParameterQueries.SD);
	}
	
	/**
	 * ユニット定義パラメータseaのJavaオブジェクト表現を取得するためのクエリを返す.
	 * @return クエリ・インスタンス
	 */
	public static final UnitQuery<WriteOption> sea() {
		return parameter("sea", ParameterQueries.SEA);
	}
	
	/**
	 * ユニット定義パラメータsoaのJavaオブジェクト表現を取得するためのクエリを返す.
	 * @return クエリ・インスタンス
	 */
	public static final UnitQuery<WriteOption> soa() {
		return parameter("soa", ParameterQueries.SOA);
	}
	
	/**
	 * ユニット定義パラメータstのJavaオブジェクト表現を取得するためのクエリを返す.
	 * @return クエリ・インスタンス
	 */
	public static final UnitQuery<StartTime> st() {
		return parameter("st", ParameterQueries.ST);
	}
	
	/**
	 * ユニット定義パラメータsyのJavaオブジェクト表現を取得するためのクエリを返す.
	 * @return クエリ・インスタンス
	 */
	public static final UnitQuery<StartDelayTime> sy() {
		return parameter("sy", ParameterQueries.SY);
	}
	
	/**
	 * ユニット定義パラメータszのJavaオブジェクト表現を取得するためのクエリを返す.
	 * @return クエリ・インスタンス
	 */
	public static final UnitQuery<MapSize> sz() {
		return parameter("sz", ParameterQueries.SZ);
	}
	
	/**
	 * ユニット定義パラメータthoのJavaオブジェクト表現を取得するためのクエリを返す.
	 * @return クエリ・インスタンス
	 */
	public static final UnitQuery<ExitCodeThreshold> tho() {
		return parameter("tho", ParameterQueries.THO);
	}
	
	/**
	 * ユニット定義パラメータtmitvのJavaオブジェクト表現を取得するためのクエリを返す.
	 * @return クエリ・インスタンス
	 */
	public static final UnitQuery<ElapsedTime> tmitv() {
		return parameter("tmitv", ParameterQueries.TMITV);
	}
	
	/**
	 * ユニット定義パラメータtop1のJavaオブジェクト表現を取得するためのクエリを返す.
	 * @return クエリ・インスタンス
	 */
	public static final UnitQuery<DeleteOption> top1() {
		return parameter("top1", ParameterQueries.TOP1);
	}
	
	/**
	 * ユニット定義パラメータtop2のJavaオブジェクト表現を取得するためのクエリを返す.
	 * @return クエリ・インスタンス
	 */
	public static final UnitQuery<DeleteOption> top2() {
		return parameter("top2", ParameterQueries.TOP2);
	}
	
	/**
	 * ユニット定義パラメータtop3のJavaオブジェクト表現を取得するためのクエリを返す.
	 * @return クエリ・インスタンス
	 */
	public static final UnitQuery<DeleteOption> top3() {
		return parameter("top3", ParameterQueries.TOP3);
	}
	
	/**
	 * ユニット定義パラメータtop4のJavaオブジェクト表現を取得するためのクエリを返す.
	 * @return クエリ・インスタンス
	 */
	public static final UnitQuery<DeleteOption> top4() {
		return parameter("top4", ParameterQueries.TOP4);
	}
	
	/**
	 * ユニット定義パラメータtyのJavaオブジェクト表現を取得するためのクエリを返す.
	 * @return クエリ・インスタンス
	 */
	public static final UnitQuery<UnitType> ty() {
		return parameter("ty", ParameterQueries.TY);
	}
	
	/**
	 * ユニット定義パラメータwthのJavaオブジェクト表現を取得するためのクエリを返す.
	 * @return クエリ・インスタンス
	 */
	public static final UnitQuery<ExitCodeThreshold> wth() {
		return parameter("wth", ParameterQueries.WTH);
	}

	/**
	 * ユニット定義パラメータejのJavaオブジェクト表現を取得するためのクエリを返す.
	 * @return クエリ・インスタンス
	 */
	public static final UnitQuery<EndStatusJudgementType> ej() {
		return parameter("ej", ParameterQueries.EJ);
	}
	
	/**
	 * ユニット定義パラメータejcのJavaオブジェクト表現を取得するためのクエリを返す.
	 * @return クエリ・インスタンス
	 */
	public static final UnitQuery<UnsignedIntegral> ejc() {
		return parameter("ejc", ParameterQueries.EJC);
	}
	
	/**
	 * ユニット定義パラメータelmのJavaオブジェクト表現を取得するためのクエリを返す.
	 * @return クエリ・インスタンス
	 */
	public static final UnitQuery<ElapsedTime> etm() {
		return parameter("elm", ParameterQueries.ETM);
	}
	
	/**
	 * ユニット定義パラメータevのJavaオブジェクト表現を取得するためのクエリを返す.
	 * @return クエリ・インスタンス
	 */
	public static final UnitQuery<List<EnvironmentVariable>> ev() {
		return parameter("ev", ParameterQueries.EV);
	}
	
	/**
	 * ユニット定義パラメータmladrのJavaオブジェクト表現を取得するためのクエリを返す.
	 * @return クエリ・インスタンス
	 */
	public static final UnitQuery<MailAddress> mladr() {
		return parameter("mladr", ParameterQueries.MLADR);
	}
	
	/**
	 * ユニット定義パラメータteのJavaオブジェクト表現を取得するためのクエリを返す.
	 * @return クエリ・インスタンス
	 */
	public static final UnitQuery<CommandLine> te() {
		return parameter("te", ParameterQueries.TE);
	}
}
