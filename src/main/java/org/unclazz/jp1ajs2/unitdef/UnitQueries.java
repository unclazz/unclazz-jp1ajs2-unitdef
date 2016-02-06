package org.unclazz.jp1ajs2.unitdef;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.regex.MatchResult;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

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
import org.unclazz.jp1ajs2.unitdef.parameter.FixedDuration;
import org.unclazz.jp1ajs2.unitdef.parameter.LinkedRuleNumber;
import org.unclazz.jp1ajs2.unitdef.parameter.MailAddress;
import org.unclazz.jp1ajs2.unitdef.parameter.MapSize;
import org.unclazz.jp1ajs2.unitdef.parameter.MinutesInterval;
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
	 * 引数で指定された名前のパラメータが存在しなかった場合、クエリは空のリストを返す。
	 * @param paramName ユニット定義パラメータ名
	 * @return ユニット定義パラメータのリスト
	 */
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
	
	/**
	 * サブユニットのリストを返すクエリを返す.
	 * 引数で指定された名前のサブユニットが存在しなかった場合、クエリは空のリストを返す。
	 * 存在した場合は単一要素からなるリストを返す。
	 * @param name サブユニット名
	 * @return サブユニットのリスト
	 */
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
	
	/**
	 * ユニット定義パラメータの値に対する正規表現マッチの結果を返すクエリを返す.
	 * 引数で指定されたパラメータが存在しなかった場合、クエリは空のリストを返す。
	 * マッチングは{@link Matcher#matches()}による完全一致型。
	 * @param paramName パラメータ名
	 * @param pattern 正規表現パターン
	 * @return マッチング結果のリスト
	 */
	public static UnitQuery<MatchResult> parameterNamed(final String paramName, final String pattern) {
		return parameterNamed(paramName, Pattern.compile(pattern));
	}
	
	/**
	 * ユニット定義パラメータの値に対する正規表現マッチの結果を返すクエリを返す.
	 * 引数で指定されたパラメータが存在しなかった場合、クエリは空のリストを返す。
	 * マッチングは{@link Matcher#matches()}による完全一致型。
	 * @param paramName パラメータ名
	 * @param pattern 正規表現パターン
	 * @return マッチング結果のリスト
	 */
	public static UnitQuery<MatchResult> parameterNamed(final String paramName, final Pattern pattern) {
		return parameterNamed(paramName, ParameterQueries.withPattern(pattern));
	}
	
	/**
	 * ユニット定義パラメータに対し{@link ParameterQuery#queryFrom(Parameter)}を適用した結果を返すクエリを返す.
	 * {@link ParameterQuery#queryFrom(Parameter)}の結果が{@code null}だった場合、
	 * その値は結果リストには加えられない。
	 * @param paramName パラメータ名
	 * @param paramQuery ユニット定義パラメータ・クエリ
	 * @return ユニット定義パラメータ適用結果のリスト
	 */
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
	
	/**
	 * ユニット定義パラメータarのJavaオブジェクト表現を取得するためのクエリを返す.
	 * @return クエリ・インスタンス
	 */
	public static final UnitQuery<AnteroposteriorRelationship> ar() {
		return parameterNamed("ar", ParameterQueries.AR);
	}
	
	/**
	 * ユニット定義パラメータcmのJavaオブジェクト表現を取得するためのクエリを返す.
	 * @return クエリ・インスタンス
	 */
	public static final UnitQuery<CharSequence> cm() {
		return parameterNamed("cm", ParameterQueries.CM);
	}
	
	/**
	 * ユニット定義パラメータcyのJavaオブジェクト表現を取得するためのクエリを返す.
	 * @return クエリ・インスタンス
	 */
	public static final UnitQuery<ExecutionCycle> cy() {
		return parameterNamed("cy", ParameterQueries.CY);
	}
	
	/**
	 * ユニット定義パラメータelのJavaオブジェクト表現を取得するためのクエリを返す.
	 * @return クエリ・インスタンス
	 */
	public static final UnitQuery<Element> el() {
		return parameterNamed("el", ParameterQueries.EL);
	}
	
	/**
	 * ユニット定義パラメータeuのJavaオブジェクト表現を取得するためのクエリを返す.
	 * @return クエリ・インスタンス
	 */
	public static final UnitQuery<ExecutionUserType> eu() {
		return parameterNamed("eu", ParameterQueries.EU);
	}
	
	/**
	 * ユニット定義パラメータetsのJavaオブジェクト表現を取得するためのクエリを返す.
	 * @return クエリ・インスタンス
	 */
	public static final UnitQuery<ExecutionTimedOutStatus> ets() {
		return parameterNamed("ets", ParameterQueries.ETS);
	}
	
	/**
	 * ユニット定義パラメータeyのJavaオブジェクト表現を取得するためのクエリを返す.
	 * @return クエリ・インスタンス
	 */
	public static final UnitQuery<EndDelayTime> ey() {
		return parameterNamed("ey", ParameterQueries.EY);
	}
	
	/**
	 * ユニット定義パラメータfdのJavaオブジェクト表現を取得するためのクエリを返す.
	 * @return クエリ・インスタンス
	 */
	public static final UnitQuery<FixedDuration> fd() {
		return parameterNamed("fd", ParameterQueries.FD);
	}
	
	/**
	 * ユニット定義パラメータjdのJavaオブジェクト表現を取得するためのクエリを返す.
	 * @return クエリ・インスタンス
	 */
	public static final UnitQuery<ResultJudgmentType> jd() {
		return parameterNamed("jd", ParameterQueries.JD);
	}
	
	/**
	 * ユニット定義パラメータlnのJavaオブジェクト表現を取得するためのクエリを返す.
	 * @return クエリ・インスタンス
	 */
	public static final UnitQuery<LinkedRuleNumber> ln() {
		return parameterNamed("ln", ParameterQueries.LN);
	}
	
	/**
	 * ユニット定義パラメータprmのJavaオブジェクト表現を取得するためのクエリを返す.
	 * @return クエリ・インスタンス
	 */
	public static final UnitQuery<CharSequence> prm() {
		return parameterNamed("prm", ParameterQueries.PRM);
	}
	
	/**
	 * ユニット定義パラメータscのJavaオブジェクト表現を取得するためのクエリを返す.
	 * @return クエリ・インスタンス
	 */
	public static final UnitQuery<CommandLine> sc() {
		return parameterNamed("sc", ParameterQueries.SC);
	}
	
	/**
	 * ユニット定義パラメータsdのJavaオブジェクト表現を取得するためのクエリを返す.
	 * @return クエリ・インスタンス
	 */
	public static final UnitQuery<StartDate> sd() {
		return parameterNamed("sd", ParameterQueries.SD);
	}
	
	/**
	 * ユニット定義パラメータseaのJavaオブジェクト表現を取得するためのクエリを返す.
	 * @return クエリ・インスタンス
	 */
	public static final UnitQuery<WriteOption> sea() {
		return parameterNamed("sea", ParameterQueries.SEA);
	}
	
	/**
	 * ユニット定義パラメータsoaのJavaオブジェクト表現を取得するためのクエリを返す.
	 * @return クエリ・インスタンス
	 */
	public static final UnitQuery<WriteOption> soa() {
		return parameterNamed("soa", ParameterQueries.SOA);
	}
	
	/**
	 * ユニット定義パラメータstのJavaオブジェクト表現を取得するためのクエリを返す.
	 * @return クエリ・インスタンス
	 */
	public static final UnitQuery<StartTime> st() {
		return parameterNamed("st", ParameterQueries.ST);
	}
	
	/**
	 * ユニット定義パラメータsyのJavaオブジェクト表現を取得するためのクエリを返す.
	 * @return クエリ・インスタンス
	 */
	public static final UnitQuery<StartDelayTime> sy() {
		return parameterNamed("sy", ParameterQueries.SY);
	}
	
	/**
	 * ユニット定義パラメータszのJavaオブジェクト表現を取得するためのクエリを返す.
	 * @return クエリ・インスタンス
	 */
	public static final UnitQuery<MapSize> sz() {
		return parameterNamed("sz", ParameterQueries.SZ);
	}
	
	/**
	 * ユニット定義パラメータthoのJavaオブジェクト表現を取得するためのクエリを返す.
	 * @return クエリ・インスタンス
	 */
	public static final UnitQuery<ExitCodeThreshold> tho() {
		return parameterNamed("tho", ParameterQueries.THO);
	}
	
	/**
	 * ユニット定義パラメータtmitvのJavaオブジェクト表現を取得するためのクエリを返す.
	 * @return クエリ・インスタンス
	 */
	public static final UnitQuery<MinutesInterval> tmitv() {
		return parameterNamed("tmitv", ParameterQueries.TMITV);
	}
	
	/**
	 * ユニット定義パラメータtop1のJavaオブジェクト表現を取得するためのクエリを返す.
	 * @return クエリ・インスタンス
	 */
	public static final UnitQuery<DeleteOption> top1() {
		return parameterNamed("top1", ParameterQueries.TOP1);
	}
	
	/**
	 * ユニット定義パラメータtop2のJavaオブジェクト表現を取得するためのクエリを返す.
	 * @return クエリ・インスタンス
	 */
	public static final UnitQuery<DeleteOption> top2() {
		return parameterNamed("top2", ParameterQueries.TOP2);
	}
	
	/**
	 * ユニット定義パラメータtop3のJavaオブジェクト表現を取得するためのクエリを返す.
	 * @return クエリ・インスタンス
	 */
	public static final UnitQuery<DeleteOption> top3() {
		return parameterNamed("top3", ParameterQueries.TOP3);
	}
	
	/**
	 * ユニット定義パラメータtop4のJavaオブジェクト表現を取得するためのクエリを返す.
	 * @return クエリ・インスタンス
	 */
	public static final UnitQuery<DeleteOption> top4() {
		return parameterNamed("top4", ParameterQueries.TOP4);
	}
	
	/**
	 * ユニット定義パラメータtyのJavaオブジェクト表現を取得するためのクエリを返す.
	 * @return クエリ・インスタンス
	 */
	public static final UnitQuery<UnitType> ty() {
		return parameterNamed("ty", ParameterQueries.TY);
	}
	
	/**
	 * ユニット定義パラメータunのJavaオブジェクト表現を取得するためのクエリを返す.
	 * @return クエリ・インスタンス
	 */
	public static final UnitQuery<CharSequence> un() {
		return parameterNamed("un", ParameterQueries.UN);
	}
	
	/**
	 * ユニット定義パラメータwkpのJavaオブジェクト表現を取得するためのクエリを返す.
	 * @return クエリ・インスタンス
	 */
	public static final UnitQuery<CharSequence> wkp() {
		return parameterNamed("wkp", ParameterQueries.WKP);
	}
	
	/**
	 * ユニット定義パラメータwthのJavaオブジェクト表現を取得するためのクエリを返す.
	 * @return クエリ・インスタンス
	 */
	public static final UnitQuery<ExitCodeThreshold> wth() {
		return parameterNamed("wth", ParameterQueries.WTH);
	}

	/**
	 * ユニット定義パラメータejのJavaオブジェクト表現を取得するためのクエリを返す.
	 * @return クエリ・インスタンス
	 */
	public static final UnitQuery<EndStatusJudgementType> ej() {
		return parameterNamed("ej", ParameterQueries.EJ);
	}
	
	/**
	 * ユニット定義パラメータejcのJavaオブジェクト表現を取得するためのクエリを返す.
	 * @return クエリ・インスタンス
	 */
	public static final UnitQuery<UnsignedIntegral> ejc() {
		return parameterNamed("ejc", ParameterQueries.EJC);
	}
	
	/**
	 * ユニット定義パラメータejfのJavaオブジェクト表現を取得するためのクエリを返す.
	 * @return クエリ・インスタンス
	 */
	public static final UnitQuery<CharSequence> ejf() {
		return parameterNamed("ejf", ParameterQueries.EJF);
	}
	
	/**
	 * ユニット定義パラメータejiのJavaオブジェクト表現を取得するためのクエリを返す.
	 * @return クエリ・インスタンス
	 */
	public static final UnitQuery<Integer> eji() {
		return parameterNamed("eji", ParameterQueries.EJI);
	}
	
	/**
	 * ユニット定義パラメータejtのJavaオブジェクト表現を取得するためのクエリを返す.
	 * @return クエリ・インスタンス
	 */
	public static final UnitQuery<CharSequence> ejt() {
		return parameterNamed("ejt", ParameterQueries.EJT);
	}
	
	/**
	 * ユニット定義パラメータejvのJavaオブジェクト表現を取得するためのクエリを返す.
	 * @return クエリ・インスタンス
	 */
	public static final UnitQuery<CharSequence> ejv() {
		return parameterNamed("ejv", ParameterQueries.EJV);
	}
	
	/**
	 * ユニット定義パラメータelmのJavaオブジェクト表現を取得するためのクエリを返す.
	 * @return クエリ・インスタンス
	 */
	public static final UnitQuery<MinutesInterval> etm() {
		return parameterNamed("elm", ParameterQueries.ETM);
	}
	
	/**
	 * ユニット定義パラメータevのJavaオブジェクト表現を取得するためのクエリを返す.
	 * @return クエリ・インスタンス
	 */
	public static final UnitQuery<List<EnvironmentVariable>> ev() {
		return parameterNamed("ev", ParameterQueries.EV);
	}
	
	/**
	 * ユニット定義パラメータjdfのJavaオブジェクト表現を取得するためのクエリを返す.
	 * @return クエリ・インスタンス
	 */
	public static final UnitQuery<CharSequence> jdf() {
		return parameterNamed("jdf", ParameterQueries.JDF);
	}
	
	/**
	 * ユニット定義パラメータmladrのJavaオブジェクト表現を取得するためのクエリを返す.
	 * @return クエリ・インスタンス
	 */
	public static final UnitQuery<MailAddress> mladr() {
		return parameterNamed("mladr", ParameterQueries.MLADR);
	}
	
	/**
	 * ユニット定義パラメータmlaflのJavaオブジェクト表現を取得するためのクエリを返す.
	 * @return クエリ・インスタンス
	 */
	public static final UnitQuery<CharSequence> mlafl() {
		return parameterNamed("mlafl", ParameterQueries.MLAFL);
	}
	
	/**
	 * ユニット定義パラメータmlatfのJavaオブジェクト表現を取得するためのクエリを返す.
	 * @return クエリ・インスタンス
	 */
	public static final UnitQuery<CharSequence> mlatf() {
		return parameterNamed("mlatf", ParameterQueries.MLATF);
	}
	
	/**
	 * ユニット定義パラメータmlftxのJavaオブジェクト表現を取得するためのクエリを返す.
	 * @return クエリ・インスタンス
	 */
	public static final UnitQuery<CharSequence> mlftx() {
		return parameterNamed("mlftx", ParameterQueries.MLFTX);
	}
	
	/**
	 * ユニット定義パラメータmlprfのJavaオブジェクト表現を取得するためのクエリを返す.
	 * @return クエリ・インスタンス
	 */
	public static final UnitQuery<CharSequence> mlprf() {
		return parameterNamed("mlprf", ParameterQueries.MLPRF);
	}
	
	/**
	 * ユニット定義パラメータmlsbjのJavaオブジェクト表現を取得するためのクエリを返す.
	 * @return クエリ・インスタンス
	 */
	public static final UnitQuery<CharSequence> mlsbj() {
		return parameterNamed("mlsbj", ParameterQueries.MLSBJ);
	}
	
	/**
	 * ユニット定義パラメータmltxtのJavaオブジェクト表現を取得するためのクエリを返す.
	 * @return クエリ・インスタンス
	 */
	public static final UnitQuery<CharSequence> mltxt() {
		return parameterNamed("mltxt", ParameterQueries.MLTXT);
	}
	
	/**
	 * ユニット定義パラメータncexのJavaオブジェクト表現を取得するためのクエリを返す.
	 * @return クエリ・インスタンス
	 */
	public static final UnitQuery<Boolean> ncex() {
		return parameterNamed("ncex", ParameterQueries.NCEX);
	}
	
	/**
	 * ユニット定義パラメータnchnのJavaオブジェクト表現を取得するためのクエリを返す.
	 * @return クエリ・インスタンス
	 */
	public static final UnitQuery<CharSequence> nchn() {
		return parameterNamed("nchn", ParameterQueries.NCHN);
	}
	
	/**
	 * ユニット定義パラメータnclのJavaオブジェクト表現を取得するためのクエリを返す.
	 * @return クエリ・インスタンス
	 */
	public static final UnitQuery<Boolean> ncl() {
		return parameterNamed("ncl", ParameterQueries.NCL);
	}
	
	/**
	 * ユニット定義パラメータncnのJavaオブジェクト表現を取得するためのクエリを返す.
	 * @return クエリ・インスタンス
	 */
	public static final UnitQuery<CharSequence> ncn() {
		return parameterNamed("ncn", ParameterQueries.NCN);
	}
	
	/**
	 * ユニット定義パラメータncsのJavaオブジェクト表現を取得するためのクエリを返す.
	 * @return クエリ・インスタンス
	 */
	public static final UnitQuery<Boolean> ncs() {
		return parameterNamed("ncs", ParameterQueries.NCS);
	}
	
	/**
	 * ユニット定義パラメータncsvのJavaオブジェクト表現を取得するためのクエリを返す.
	 * @return クエリ・インスタンス
	 */
	public static final UnitQuery<CharSequence> ncsv() {
		return parameterNamed("ncsv", ParameterQueries.NCSV);
	}
	
	/**
	 * ユニット定義パラメータseのJavaオブジェクト表現を取得するためのクエリを返す.
	 * @return クエリ・インスタンス
	 */
	public static final UnitQuery<CharSequence> se() {
		return parameterNamed("se", ParameterQueries.SE);
	}
	
	/**
	 * ユニット定義パラメータsiのJavaオブジェクト表現を取得するためのクエリを返す.
	 * @return クエリ・インスタンス
	 */
	public static final UnitQuery<CharSequence> si() {
		return parameterNamed("si", ParameterQueries.SI);
	}
	
	/**
	 * ユニット定義パラメータsoのJavaオブジェクト表現を取得するためのクエリを返す.
	 * @return クエリ・インスタンス
	 */
	public static final UnitQuery<CharSequence> so() {
		return parameterNamed("so", ParameterQueries.SO);
	}
	
	/**
	 * ユニット定義パラメータtd1のJavaオブジェクト表現を取得するためのクエリを返す.
	 * @return クエリ・インスタンス
	 */
	public static final UnitQuery<CharSequence> td1() {
		return parameterNamed("td1", ParameterQueries.TD1);
	}
	
	/**
	 * ユニット定義パラメータtd2のJavaオブジェクト表現を取得するためのクエリを返す.
	 * @return クエリ・インスタンス
	 */
	public static final UnitQuery<CharSequence> td2() {
		return parameterNamed("td2", ParameterQueries.TD2);
	}
	
	/**
	 * ユニット定義パラメータtd3のJavaオブジェクト表現を取得するためのクエリを返す.
	 * @return クエリ・インスタンス
	 */
	public static final UnitQuery<CharSequence> td3() {
		return parameterNamed("td3", ParameterQueries.TD3);
	}
	
	/**
	 * ユニット定義パラメータtd4のJavaオブジェクト表現を取得するためのクエリを返す.
	 * @return クエリ・インスタンス
	 */
	public static final UnitQuery<CharSequence> td4() {
		return parameterNamed("td4", ParameterQueries.TD4);
	}
	
	/**
	 * ユニット定義パラメータteのJavaオブジェクト表現を取得するためのクエリを返す.
	 * @return クエリ・インスタンス
	 */
	public static final UnitQuery<CommandLine> te() {
		return parameterNamed("te", ParameterQueries.TE);
	}
	
	/**
	 * ユニット定義パラメータts1のJavaオブジェクト表現を取得するためのクエリを返す.
	 * @return クエリ・インスタンス
	 */
	public static final UnitQuery<CharSequence> ts1() {
		return parameterNamed("ts1", ParameterQueries.TS1);
	}
	
	/**
	 * ユニット定義パラメータts2のJavaオブジェクト表現を取得するためのクエリを返す.
	 * @return クエリ・インスタンス
	 */
	public static final UnitQuery<CharSequence> ts2() {
		return parameterNamed("ts2", ParameterQueries.TS2);
	}
	
	/**
	 * ユニット定義パラメータts3のJavaオブジェクト表現を取得するためのクエリを返す.
	 * @return クエリ・インスタンス
	 */
	public static final UnitQuery<CharSequence> ts3() {
		return parameterNamed("ts3", ParameterQueries.TS3);
	}
	
	/**
	 * ユニット定義パラメータts4のJavaオブジェクト表現を取得するためのクエリを返す.
	 * @return クエリ・インスタンス
	 */
	public static final UnitQuery<CharSequence> ts4() {
		return parameterNamed("ts4", ParameterQueries.TS4);
	}
}
