package org.unclazz.jp1ajs2.unitdef.query;

import org.unclazz.jp1ajs2.unitdef.Parameter;
import org.unclazz.jp1ajs2.unitdef.ParameterValue;
import org.unclazz.jp1ajs2.unitdef.ParameterValueType;
import org.unclazz.jp1ajs2.unitdef.Tuple;
import org.unclazz.jp1ajs2.unitdef.parameter.AnteroposteriorRelationship;
import org.unclazz.jp1ajs2.unitdef.parameter.CommandLine;
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
import org.unclazz.jp1ajs2.unitdef.parameter.FixedDuration;
import org.unclazz.jp1ajs2.unitdef.parameter.LinkedRuleNumber;
import org.unclazz.jp1ajs2.unitdef.parameter.MailAddress;
import org.unclazz.jp1ajs2.unitdef.parameter.MapSize;
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

/**
 * ユニット定義パラメータに対してその値を問合せるためのクエリ.
 */
public interface ParameterQuery extends Query<Parameter, Parameter>,
ParameterConditionalModifier<ParameterQuery> {
	/**
	 * すべての値を問合せるクエリを返す.
	 * @return クエリ
	 */
	ValueIterableQuery values();
	/**
	 * 位置指定された値を問合せるクエリを返す.
	 * @param i パラメータ値の位置
	 * @return クエリ
	 */
	ValueOneQuery valueAt(int i);
	
	/**
	 * ユニット定義パラメータarを表すオブジェクトに変換するクエリを返す.
	 * @return クエリ
	 */
	Query<Parameter, AnteroposteriorRelationship> ar();
	/**
	 * ユニット定義パラメータcftdを表すオブジェクトに変換するクエリを返す.
	 * @return クエリ
	 */
	Query<Parameter, StartDateAdjustment> cftd();
	/**
	 * ユニット定義パラメータcmを表すオブジェクトに変換するクエリを返す.
	 * @return クエリ
	 */
	Query<Parameter, CharSequence> cm();
	/**
	 * ユニット定義パラメータcyを表すオブジェクトに変換するクエリを返す.
	 * @return クエリ
	 */
	Query<Parameter, ExecutionCycle> cy();
	/**
	 * ユニット定義パラメータedを表すオブジェクトに変換するクエリを返す.
	 * @return クエリ
	 */
	Query<Parameter, EndDate> ed();
	/**
	 * ユニット定義パラメータejを表すオブジェクトに変換するクエリを返す.
	 * @return クエリ
	 */
	Query<Parameter, EndStatusJudgementType> ej();
	/**
	 * ユニット定義パラメータejcを表すオブジェクトに変換するクエリを返す.
	 * @return クエリ
	 */
	Query<Parameter, UnsignedIntegral> ejc();
	/**
	 * ユニット定義パラメータelを表すオブジェクトに変換するクエリを返す.
	 * @return クエリ
	 */
	Query<Parameter, Element> el();
	/**
	 * ユニット定義パラメータetmを表すオブジェクトに変換するクエリを返す.
	 * @return クエリ
	 */
	Query<Parameter, ElapsedTime> etm();
	/**
	 * ユニット定義パラメータetsを表すオブジェクトに変換するクエリを返す.
	 * @return クエリ
	 */
	Query<Parameter, ExecutionTimedOutStatus> ets();
	/**
	 * ユニット定義パラメータeuを表すオブジェクトに変換するクエリを返す.
	 * @return クエリ
	 */
	Query<Parameter, ExecutionUserType> eu();
	/**
	 * ユニット定義パラメータeyを表すオブジェクトに変換するクエリを返す.
	 * @return クエリ
	 */
	Query<Parameter, EndDelayTime> ey();
	/**
	 * ユニット定義パラメータfdを表すオブジェクトに変換するクエリを返す.
	 * @return クエリ
	 */
	Query<Parameter, FixedDuration> fd();
	/**
	 * ユニット定義パラメータflwcを表すオブジェクトに変換するクエリを返す.
	 * @return クエリ
	 */
	Query<Parameter, FileWatchCondition> flwc();
	/**
	 * ユニット定義パラメータjdを表すオブジェクトに変換するクエリを返す.
	 * @return クエリ
	 */
	Query<Parameter, ResultJudgmentType> jd();
	/**
	 * ユニット定義パラメータlnを表すオブジェクトに変換するクエリを返す.
	 * @return クエリ
	 */
	Query<Parameter, LinkedRuleNumber> ln();
	/**
	 * ユニット定義パラメータmladrを表すオブジェクトに変換するクエリを返す.
	 * @return クエリ
	 */
	Query<Parameter, MailAddress> mladr();
	/**
	 * ユニット定義パラメータscを表すオブジェクトに変換するクエリを返す.
	 * @return クエリ
	 */
	Query<Parameter, CommandLine> sc();
	/**
	 * ユニット定義パラメータsdを表すオブジェクトに変換するクエリを返す.
	 * @return クエリ
	 */
	Query<Parameter, StartDate> sd();
	/**
	 * ユニット定義パラメータseaを表すオブジェクトに変換するクエリを返す.
	 * @return クエリ
	 */
	Query<Parameter, WriteOption> sea();
	/**
	 * ユニット定義パラメータshを表すオブジェクトに変換するクエリを返す.
	 * @return クエリ
	 */
	Query<Parameter, StartDateCompensation> sh();
	/**
	 * ユニット定義パラメータshdを表すオブジェクトに変換するクエリを返す.
	 * @return クエリ
	 */
	Query<Parameter, StartDateCompensationDeadline> shd();
	/**
	 * ユニット定義パラメータsoaを表すオブジェクトに変換するクエリを返す.
	 * @return クエリ
	 */
	Query<Parameter, WriteOption> soa();
	/**
	 * ユニット定義パラメータstを表すオブジェクトに変換するクエリを返す.
	 * @return クエリ
	 */
	Query<Parameter, StartTime> st();
	/**
	 * ユニット定義パラメータsyを表すオブジェクトに変換するクエリを返す.
	 * @return クエリ
	 */
	Query<Parameter, StartDelayTime> sy();
	/**
	 * ユニット定義パラメータszを表すオブジェクトに変換するクエリを返す.
	 * @return クエリ
	 */
	Query<Parameter, MapSize> sz();
	/**
	 * ユニット定義パラメータteを表すオブジェクトに変換するクエリを返す.
	 * @return クエリ
	 */
	Query<Parameter, CommandLine> te();
	/**
	 * ユニット定義パラメータthoを表すオブジェクトに変換するクエリを返す.
	 * @return クエリ
	 */
	Query<Parameter, ExitCodeThreshold> tho();
	/**
	 * ユニット定義パラメータtmitvを表すオブジェクトに変換するクエリを返す.
	 * @return クエリ
	 */
	Query<Parameter, ElapsedTime> tmitv();
	/**
	 * ユニット定義パラメータtop1を表すオブジェクトに変換するクエリを返す.
	 * @return クエリ
	 */
	Query<Parameter, DeleteOption> top1();
	/**
	 * ユニット定義パラメータtop2を表すオブジェクトに変換するクエリを返す.
	 * @return クエリ
	 */
	Query<Parameter, DeleteOption> top2();
	/**
	 * ユニット定義パラメータtop3を表すオブジェクトに変換するクエリを返す.
	 * @return クエリ
	 */
	Query<Parameter, DeleteOption> top3();
	/**
	 * ユニット定義パラメータtop4を表すオブジェクトに変換するクエリを返す.
	 * @return クエリ
	 */
	Query<Parameter, DeleteOption> top4();
	/**
	 * ユニット定義パラメータtyを表すオブジェクトに変換するクエリを返す.
	 * @return クエリ
	 */
	Query<Parameter, UnitType> ty();
	/**
	 * ユニット定義パラメータwcを表すオブジェクトに変換するクエリを返す.
	 * @return クエリ
	 */
	Query<Parameter, RunConditionWatchLimitCount> wc();
	/**
	 * ユニット定義パラメータwtを表すオブジェクトに変換するクエリを返す.
	 * @return クエリ
	 */
	Query<Parameter, RunConditionWatchLimitTime> wt();
	/**
	 * ユニット定義パラメータwthを表すオブジェクトに変換するクエリを返す.
	 * @return クエリ
	 */
	Query<Parameter, ExitCodeThreshold> wth();
	
	/**
	 * パラメータ値を問合せるためのクエリ.
	 */
	public static interface ValueIterableQuery 
	extends IterableQuery<Parameter, ParameterValue> {
		/**
		 * パラメータ値を整数値に変換するクエリを返す.
		 * @return クエリ
		 */
		IterableQuery<Parameter, Integer> asInteger();
		/**
		 * パラメータ値を整数値に変換するクエリを返す.
		 * @param defaultValue 変換できなかった場合に使用されるデフォルト値
		 * @return クエリ
		 */
		IterableQuery<Parameter, Integer> asInteger(int defaultValue);
		/**
		 * パラメータ値を文字列に変換するクエリを返す.
		 * @return クエリ
		 */
		IterableQuery<Parameter, String> asString();
		/**
		 * パラメータ値をエスケープ済みの文字列に変換するクエリを返す.
		 * @return クエリ
		 */
		IterableQuery<Parameter, String> asEscapedString();
		/**
		 * パラメータ値を二重引用符で囲われた文字列に変換するクエリを返す.
		 * <p>パラメータのタイプが{@link ParameterValueType#QUOTED_STRING}でない場合はただの文字列表現となる。</p>
		 * @return クエリ
		 */
		IterableQuery<Parameter, String> asQuotedString();
		/**
		 * パラメータ値を文字列に変換するクエリを返す.
		 * @param forceQuote {@code true}の場合 パラメータ値のタイプが
		 * {@link ParameterValueType#QUOTED_STRING}でない場合も二重引用符で囲われた文字列に変換する
		 * @return クエリ
		 */
		IterableQuery<Parameter, String> asQuotedString(boolean forceQuote);
		/**
		 * パラメータ値を真偽値に変換するクエリを返す.
		 * @param trueValues {@code true}と見做す文字列の値のセット
		 * @return クエリ
		 */
		IterableQuery<Parameter, Boolean> asBoolean(String... trueValues);
		/**
		 * パラメータ値をタプルに変換するクエリを返す.
		 * @return クエリ
		 */
		IterableQuery<Parameter, Tuple> asTuple();
		/**
		 * パラメータ値を整数値に変換するクエリを返す.
		 * @return クエリ
		 */
		IterableQuery<Parameter, Long> asLong();
		/**
		 * パラメータ値を整数値に変換するクエリを返す.
		 * @return クエリ
		 */
		IterableQuery<Parameter, Long> asLong(long defaultValue);
	}
	
	/**
	 * 位置指定された単一のパラメータ値を問合せるためのクエリ.
	 */
	public static interface ValueOneQuery
	extends OneQuery<Parameter, ParameterValue>{
		/**
		 * パラメータ値を整数値に変換するクエリを返す.
		 * @return クエリ
		 */
		OneQuery<Parameter, Integer> asInteger();
		/**
		 * パラメータ値を整数値に変換するクエリを返す.
		 * @param defaultValue 変換できなかった場合に使用されるデフォルト値
		 * @return クエリ
		 */
		OneQuery<Parameter, Integer> asInteger(int defaultValue);
		/**
		 * パラメータ値を文字列に変換するクエリを返す.
		 * @return クエリ
		 */
		OneQuery<Parameter, String> asString();
		/**
		 * パラメータ値をエスケープ済みの文字列に変換するクエリを返す.
		 * @return クエリ
		 */
		OneQuery<Parameter, String> asEscapedString();
		/**
		 * パラメータ値を二重引用符で囲われた文字列に変換するクエリを返す.
		 * <p>パラメータのタイプが{@link ParameterValueType#QUOTED_STRING}でない場合はただの文字列表現となる。</p>
		 * @return クエリ
		 */
		OneQuery<Parameter, String> asQuotedString();
		/**
		 * パラメータ値を文字列に変換するクエリを返す.
		 * @param forceQuote {@code true}の場合 パラメータ値のタイプが
		 * {@link ParameterValueType#QUOTED_STRING}でない場合も二重引用符で囲われた文字列に変換する
		 * @return クエリ
		 */
		OneQuery<Parameter, String> asQuotedString(boolean forceQuote);
		/**
		 * パラメータ値を真偽値に変換するクエリを返す.
		 * @param trueValues {@code true}と見做す文字列の値のセット
		 * @return クエリ
		 */
		OneQuery<Parameter, Boolean> asBoolean(String... trueValues);
		/**
		 * パラメータ値をタプルに変換するクエリを返す.
		 * @return クエリ
		 */
		OneQuery<Parameter, Tuple> asTuple();
		/**
		 * パラメータ値を整数値に変換するクエリを返す.
		 * @return クエリ
		 */
		Query<Parameter, Long> asLong();
		/**
		 * パラメータ値を整数値に変換するクエリを返す.
		 * @return クエリ
		 */
		Query<Parameter, Long> asLong(long defaultValue);
	}
}
