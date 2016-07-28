package org.unclazz.jp1ajs2.unitdef.query;

import org.unclazz.jp1ajs2.unitdef.parameter.AnteroposteriorRelationship;
import org.unclazz.jp1ajs2.unitdef.parameter.CommandLine;
import org.unclazz.jp1ajs2.unitdef.parameter.DeleteOption;
import org.unclazz.jp1ajs2.unitdef.parameter.Element;
import org.unclazz.jp1ajs2.unitdef.parameter.EndDate;
import org.unclazz.jp1ajs2.unitdef.parameter.EndDelayTime;
import org.unclazz.jp1ajs2.unitdef.parameter.EndStatusJudgementType;
import org.unclazz.jp1ajs2.unitdef.parameter.ExecutionCycle;
import org.unclazz.jp1ajs2.unitdef.parameter.ExecutionTimedOutStatus;
import org.unclazz.jp1ajs2.unitdef.parameter.ExecutionUserType;
import org.unclazz.jp1ajs2.unitdef.parameter.ExitCodeThreshold;
import org.unclazz.jp1ajs2.unitdef.parameter.FileWatchCondition;
import org.unclazz.jp1ajs2.unitdef.parameter.FixedDuration;
import org.unclazz.jp1ajs2.unitdef.parameter.LinkedRuleNumber;
import org.unclazz.jp1ajs2.unitdef.parameter.MailAddress;
import org.unclazz.jp1ajs2.unitdef.parameter.MapSize;
import org.unclazz.jp1ajs2.unitdef.parameter.ElapsedTime;
import org.unclazz.jp1ajs2.unitdef.parameter.ResultJudgmentType;
import org.unclazz.jp1ajs2.unitdef.parameter.RunConditionWatchLimitCount;
import org.unclazz.jp1ajs2.unitdef.parameter.RunConditionWatchLimitTime;
import org.unclazz.jp1ajs2.unitdef.parameter.StartDate;
import org.unclazz.jp1ajs2.unitdef.parameter.StartDateAdjustment;
import org.unclazz.jp1ajs2.unitdef.parameter.StartDateCompensation;
import org.unclazz.jp1ajs2.unitdef.parameter.StartDateCompensationDeadline;
import org.unclazz.jp1ajs2.unitdef.parameter.StartDelayTime;
import org.unclazz.jp1ajs2.unitdef.parameter.StartTime;
import org.unclazz.jp1ajs2.unitdef.parameter.UnitType;
import org.unclazz.jp1ajs2.unitdef.parameter.WriteOption;
import org.unclazz.jp1ajs2.unitdef.util.UnsignedIntegral;
import org.unclazz.jp1ajs2.unitdef.query.InternalQueries.*;
import static org.unclazz.jp1ajs2.unitdef.query.InternalParameterQueries.*;
import org.unclazz.jp1ajs2.unitdef.Unit;

/**
 * ユニットを問合せ対象とするクエリのためのユーティリティ.
 */
public final class Queries {
	private Queries() {}
	
	private static final UnitIterableQuery children = 
			new DefaultUnitIterableQuery(new SourceChildren());
	
	private static final UnitIterableQuery descendants =
			new DefaultUnitIterableQuery(new SourceDescendants(false));
	
	private static final UnitIterableQuery descendantsDepthFirst = 
			new DefaultUnitIterableQuery(new SourceDescendantsDepthFirst(false));
	
	private static final UnitIterableQuery itSelfAndDescendants =
			new DefaultUnitIterableQuery(new SourceDescendants(true));
	
	private static final UnitIterableQuery itSelfAndDescendantsDepthFirst =
			new DefaultUnitIterableQuery(new SourceDescendantsDepthFirst(true));
	
	private static final ParameterIterableQuery parameters =
			new DefaultParameterIterableQuery(new SourceItSelf());
	
	private static final ParameterQuery parameter = 
			new DefaultParameterQuery();
	
	/**
	 * ユニットに対してその子ユニット（直接の下位ユニット）を問合せるクエリを返す.
	 * @return クエリ
	 */
	public static UnitIterableQuery children() {
		return children;
	}
	/**
	 * ユニットに対してその子ユニット（直接の下位ユニット）を問合せるクエリを返す.
	 * <p>{@code children().nameEquals(String).one()}と同義。</p>
	 * @param name ユニット名
	 * @return クエリ
	 */
	public static OneQuery<Unit, Unit> children(final String name) {
		return children().nameEquals(name).one();
	}
	/**
	 * ユニットに対してその子孫ユニット（直接・間接の下位ユニット）を問合せるクエリを返す.
	 * <p>ユニットの探索は幅優先に行われる。</p>
	 * @return クエリ
	 */
	public static UnitIterableQuery descendants() {
		return descendants;
	}
	/**
	 * ユニットに対してその子孫ユニット（直接・間接の下位ユニット）を問合せるクエリを返す.
	 * <p>ユニットの探索は幅優先に行われる。{@code descendants().nameEquals(String)}と同義。</p>
	 * @param name ユニット名
	 * @return クエリ
	 */
	public static UnitIterableQuery descendants(final String name) {
		return descendants().nameEquals(name);
	}
	/**
	 * ユニットに対してその子孫ユニット（直接・間接の下位ユニット）を問合せるクエリを返す.
	 * <p>ユニットの探索を幅優先で行うか深さ優先で行うかはパラメータで制御できる。</p>
	 * @param depthFirst {@code true}の場合 深さ優先で探索する
	 * @return クエリ
	 */
	public static UnitIterableQuery descendants(final boolean depthFirst) {
		return depthFirst ? descendantsDepthFirst : descendants;
	}
	/**
	 * ユニットに対してそのそのユニット自身と子孫ユニットを問合せるクエリを返す.
	 * <p>ユニットの探索は幅優先に行われる。</p>
	 * @return クエリ
	 */
	public static UnitIterableQuery itSelfAndDescendants() {
		return itSelfAndDescendants;
	}
	/**
	 * ユニットに対してそのユニット自身と子孫ユニットを問合せるクエリを返す.
	 * <p>ユニットの探索を幅優先で行うか深さ優先で行うかはパラメータで制御できる。</p>
	 * @param depthFirst {@code true}の場合 深さ優先で探索する
	 * @return クエリ
	 */
	public static UnitIterableQuery itSelfAndDescendants(final boolean depthFirst) {
		return depthFirst ? itSelfAndDescendantsDepthFirst : itSelfAndDescendants;
	}
	/**
	 * ユニットに対してそのユニット定義パラメータを問合せるクエリを返す.
	 * @return クエリ
	 */
	public static ParameterIterableQuery parameters() {
		return parameters;
	}
	/**
	 * ユニット定義パラメータに対してその値を問合せるクエリを返す.
	 * @return クエリ
	 */
	public static ParameterQuery parameter() {
		return parameter;
	}
	/**
	 * ユニットに対してそのユニット定義パラメータを問合せるクエリを返す.
	 * <p>{@code parameters().nameEquals(String)}と同義。</p>
	 * @param name パラメータ名
	 * @return クエリ
	 */
	public static ParameterIterableQuery parameters(final String name) {
		return parameters().nameEquals(name);
	}
	/**
	 * ユニット定義パラメータarのJavaオブジェクト表現を取得するためのクエリを返す.
	 * @return クエリ
	 */
	public static IterableQuery<Unit, AnteroposteriorRelationship> ar() {
		
		return parameters().nameEquals("ar").query(AR);
	}
	
	/**
	 * ユニット定義パラメータcmのJavaオブジェクト表現を取得するためのクエリを返す.
	 * @return クエリ
	 */
	public static IterableQuery<Unit,CharSequence> cm() {
		return parameters().nameEquals("cm").query(CM);
	}
	
	/**
	 * ユニット定義パラメータcyのJavaオブジェクト表現を取得するためのクエリを返す.
	 * @return クエリ
	 */
	public static IterableQuery<Unit,ExecutionCycle> cy() {
		return parameters().nameEquals("cy").query(CY);
	}
	
	/**
	 * ユニット定義パラメータelのJavaオブジェクト表現を取得するためのクエリを返す.
	 * @return クエリ
	 */
	public static IterableQuery<Unit,Element> el() {
		return parameters().nameEquals("el").query(EL);
	}
	
	/**
	 * ユニット定義パラメータeuのJavaオブジェクト表現を取得するためのクエリを返す.
	 * @return クエリ
	 */
	public static IterableQuery<Unit,ExecutionUserType> eu() {
		return parameters().nameEquals("eu").query(EU);
	}
	
	/**
	 * ユニット定義パラメータetsのJavaオブジェクト表現を取得するためのクエリを返す.
	 * @return クエリ
	 */
	public static IterableQuery<Unit,ExecutionTimedOutStatus> ets() {
		return parameters().nameEquals("ets").query(ETS);
	}
	
	/**
	 * ユニット定義パラメータeyのJavaオブジェクト表現を取得するためのクエリを返す.
	 * @return クエリ
	 */
	public static IterableQuery<Unit,EndDelayTime> ey() {
		return parameters().nameEquals("ey").query(EY);
	}
	
	/**
	 * ユニット定義パラメータfdのJavaオブジェクト表現を取得するためのクエリを返す.
	 * @return クエリ
	 */
	public static IterableQuery<Unit,FixedDuration> fd() {
		return parameters().nameEquals("fd").query(FD);
	}
	
	/**
	 * ユニット定義パラメータflwcのJavaオブジェクト表現を取得するためのクエリを返す.
	 * @return クエリ
	 */
	public static IterableQuery<Unit,FileWatchCondition> flwc() {
		return parameters().nameEquals("flwc").query(FLWC);
	}
	
	/**
	 * ユニット定義パラメータjdのJavaオブジェクト表現を取得するためのクエリを返す.
	 * @return クエリ
	 */
	public static IterableQuery<Unit,ResultJudgmentType> jd() {
		return parameters().nameEquals("jd").query(JD);
	}
	
	/**
	 * ユニット定義パラメータlnのJavaオブジェクト表現を取得するためのクエリを返す.
	 * @return クエリ
	 */
	public static IterableQuery<Unit,LinkedRuleNumber> ln() {
		return parameters().nameEquals("ln").query(LN);
	}
	
	/**
	 * ユニット定義パラメータscのJavaオブジェクト表現を取得するためのクエリを返す.
	 * @return クエリ
	 */
	public static IterableQuery<Unit,CommandLine> sc() {
		return parameters().nameEquals("sc").query(SC);
	}
	
	/**
	 * ユニット定義パラメータsdのJavaオブジェクト表現を取得するためのクエリを返す.
	 * @return クエリ
	 */
	public static IterableQuery<Unit,StartDate> sd() {
		return parameters().nameEquals("sd").query(SD);
	}
	
	/**
	 * ユニット定義パラメータshのJavaオブジェクト表現を取得するためのクエリを返す.
	 * @return クエリ
	 */
	public static IterableQuery<Unit,StartDateCompensation> sh() {
		return parameters().nameEquals("sh").query(SH);
	}
	
	/**
	 * ユニット定義パラメータshdのJavaオブジェクト表現を取得するためのクエリを返す.
	 * @return クエリ
	 */
	public static IterableQuery<Unit,StartDateCompensationDeadline> shd() {
		return parameters().nameEquals("shd").query(SHD);
	}
	
	/**
	 * ユニット定義パラメータwcのJavaオブジェクト表現を取得するためのクエリを返す.
	 * @return クエリ
	 */
	public static IterableQuery<Unit,RunConditionWatchLimitCount> wc() {
		return parameters().nameEquals("wc").query(WC);
	}
	
	/**
	 * ユニット定義パラメータwtのJavaオブジェクト表現を取得するためのクエリを返す.
	 * @return クエリ
	 */
	public static IterableQuery<Unit,RunConditionWatchLimitTime> wt() {
		return parameters().nameEquals("wt").query(WT);
	}
	
	/**
	 * ユニット定義パラメータcftdのJavaオブジェクト表現を取得するためのクエリを返す.
	 * @return クエリ
	 */
	public static IterableQuery<Unit,StartDateAdjustment> cftd() {
		return parameters().nameEquals("cftd").query(CFTD);
	}
	
	/**
	 * ユニット定義パラメータedのJavaオブジェクト表現を取得するためのクエリを返す.
	 * @return クエリ
	 */
	public static IterableQuery<Unit,EndDate> ed() {
		return parameters().nameEquals("ed").query(ED);
	}
	
	/**
	 * ユニット定義パラメータseaのJavaオブジェクト表現を取得するためのクエリを返す.
	 * @return クエリ
	 */
	public static IterableQuery<Unit,WriteOption> sea() {
		return parameters().nameEquals("sea").query(SEA);
	}
	
	/**
	 * ユニット定義パラメータsoaのJavaオブジェクト表現を取得するためのクエリを返す.
	 * @return クエリ
	 */
	public static IterableQuery<Unit,WriteOption> soa() {
		return parameters().nameEquals("soa").query(SOA);
	}
	
	/**
	 * ユニット定義パラメータstのJavaオブジェクト表現を取得するためのクエリを返す.
	 * @return クエリ
	 */
	public static IterableQuery<Unit,StartTime> st() {
		return parameters().nameEquals("st").query(ST);
	}
	
	/**
	 * ユニット定義パラメータsyのJavaオブジェクト表現を取得するためのクエリを返す.
	 * @return クエリ
	 */
	public static IterableQuery<Unit,StartDelayTime> sy() {
		return parameters().nameEquals("sy").query(SY);
	}
	
	/**
	 * ユニット定義パラメータszのJavaオブジェクト表現を取得するためのクエリを返す.
	 * @return クエリ
	 */
	public static IterableQuery<Unit,MapSize> sz() {
		return parameters().nameEquals("sz").query(SZ);
	}
	
	/**
	 * ユニット定義パラメータthoのJavaオブジェクト表現を取得するためのクエリを返す.
	 * @return クエリ
	 */
	public static IterableQuery<Unit,ExitCodeThreshold> tho() {
		return parameters().nameEquals("tho").query(THO);
	}
	
	/**
	 * ユニット定義パラメータtmitvのJavaオブジェクト表現を取得するためのクエリを返す.
	 * @return クエリ
	 */
	public static IterableQuery<Unit,ElapsedTime> tmitv() {
		return parameters().nameEquals("tmitv").query(TMITV);
	}
	
	/**
	 * ユニット定義パラメータtop1のJavaオブジェクト表現を取得するためのクエリを返す.
	 * @return クエリ
	 */
	public static IterableQuery<Unit,DeleteOption> top1() {
		return parameters().nameEquals("top1").query(TOP1);
	}
	
	/**
	 * ユニット定義パラメータtop2のJavaオブジェクト表現を取得するためのクエリを返す.
	 * @return クエリ
	 */
	public static IterableQuery<Unit,DeleteOption> top2() {
		return parameters().nameEquals("top2").query(TOP2);
	}
	
	/**
	 * ユニット定義パラメータtop3のJavaオブジェクト表現を取得するためのクエリを返す.
	 * @return クエリ
	 */
	public static IterableQuery<Unit,DeleteOption> top3() {
		return parameters().nameEquals("top3").query(TOP3);
	}
	
	/**
	 * ユニット定義パラメータtop4のJavaオブジェクト表現を取得するためのクエリを返す.
	 * @return クエリ
	 */
	public static IterableQuery<Unit,DeleteOption> top4() {
		return parameters().nameEquals("top4").query(TOP4);
	}
	
	/**
	 * ユニット定義パラメータtyのJavaオブジェクト表現を取得するためのクエリを返す.
	 * @return クエリ
	 */
	public static IterableQuery<Unit,UnitType> ty() {
		return parameters().nameEquals("ty").query(TY);
	}
	
	/**
	 * ユニット定義パラメータwthのJavaオブジェクト表現を取得するためのクエリを返す.
	 * @return クエリ
	 */
	public static IterableQuery<Unit,ExitCodeThreshold> wth() {
		return parameters().nameEquals("wth").query(WTH);
	}

	/**
	 * ユニット定義パラメータejのJavaオブジェクト表現を取得するためのクエリを返す.
	 * @return クエリ
	 */
	public static IterableQuery<Unit,EndStatusJudgementType> ej() {
		return parameters().nameEquals("ej").query(EJ);
	}
	
	/**
	 * ユニット定義パラメータejcのJavaオブジェクト表現を取得するためのクエリを返す.
	 * @return クエリ
	 */
	public static IterableQuery<Unit,UnsignedIntegral> ejc() {
		return parameters().nameEquals("ejc").query(EJC);
	}
	
	/**
	 * ユニット定義パラメータelmのJavaオブジェクト表現を取得するためのクエリを返す.
	 * @return クエリ
	 */
	public static IterableQuery<Unit,ElapsedTime> etm() {
		return parameters().nameEquals("etm").query(ETM);
	}
	
	/**
	 * ユニット定義パラメータmladrのJavaオブジェクト表現を取得するためのクエリを返す.
	 * @return クエリ
	 */
	public static IterableQuery<Unit,MailAddress> mladr() {
		return parameters().nameEquals("mladr").query(MLADR);
	}
	
	/**
	 * ユニット定義パラメータteのJavaオブジェクト表現を取得するためのクエリを返す.
	 * @return クエリ
	 */
	public static IterableQuery<Unit,CommandLine> te() {
		return parameters().nameEquals("te").query(TE);
	}
}
