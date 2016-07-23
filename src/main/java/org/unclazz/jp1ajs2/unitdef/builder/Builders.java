package org.unclazz.jp1ajs2.unitdef.builder;

import org.unclazz.jp1ajs2.unitdef.Attributes;
import org.unclazz.jp1ajs2.unitdef.FullQualifiedName;
import org.unclazz.jp1ajs2.unitdef.Parameter;
import org.unclazz.jp1ajs2.unitdef.ParameterValue;
import org.unclazz.jp1ajs2.unitdef.PermissionMode;
import org.unclazz.jp1ajs2.unitdef.Tuple;
import org.unclazz.jp1ajs2.unitdef.Unit;
import org.unclazz.jp1ajs2.unitdef.parameter.AnteroposteriorRelationship;
import org.unclazz.jp1ajs2.unitdef.parameter.Element;
import org.unclazz.jp1ajs2.unitdef.parameter.EndDelayTime;
import org.unclazz.jp1ajs2.unitdef.parameter.RunConditionWatchLimitCount;
import org.unclazz.jp1ajs2.unitdef.parameter.RunConditionWatchLimitTime;
import org.unclazz.jp1ajs2.unitdef.parameter.StartDate;
import org.unclazz.jp1ajs2.unitdef.parameter.StartDateAdjustment;
import org.unclazz.jp1ajs2.unitdef.parameter.StartDateCompensation;
import org.unclazz.jp1ajs2.unitdef.parameter.StartDateCompensationDeadline;
import org.unclazz.jp1ajs2.unitdef.parameter.StartDelayTime;
import org.unclazz.jp1ajs2.unitdef.parameter.StartTime;

/**
 * ユニット定義情報を構成するコンポーネントのためのビルダーを提供するユーティリティ.
 */
public final class Builders {
	private Builders() {}
	
	/**
	 * {@link Tuple}のためのビルダーを返す.
	 * @return ビルダー
	 */
	public static TupleBuilder tuple() {
		return new TupleBuilder();
	}
	/**
	 * {@link Parameter}のためのビルダーを返す.
	 * @return ビルダー
	 */
	public static ParameterBuilder parameter() {
		return new ParameterBuilder();
	}
	/**
	 * {@link Unit}のためのビルダーを返す.
	 * @return ビルダー
	 */
	public static UnitBuilder unit() {
		return new UnitBuilder();
	}
	/**
	 * {@link Attributes}のためのビルダーを返す.
	 * @return ビルダー
	 */
	public static AttributesBuilder attributes() {
		return new AttributesBuilder();
	}
	/**
	 * {@link FullQualifiedName}のためのビルダーを返す.
	 * @return ビルダー
	 */
	public static FullQualifiedNameBuilder fullQualifiedName() {
		return new FullQualifiedNameBuilder();
	}
	/**
	 * {@link AnteroposteriorRelationship}のためのビルダーを返す.
	 * @return ビルダー
	 */
	public static AnteroposteriorRelationshipBuilder parameterAR() {
		return new AnteroposteriorRelationshipBuilder();
	}
	/**
	 * {@link EndDelayTime}のためのビルダーを返す.
	 * @return ビルダー
	 */
	public static EndDelayTimeBuilder parameterEY() {
		return new EndDelayTimeBuilder();
	}
	/**
	 * {@link StartDelayTime}のためのビルダーを返す.
	 * @return ビルダー
	 */
	public static StartDelayTimeBuilder parameterSY() {
		return new StartDelayTimeBuilder();
	}
	/**
	 * {@link StartTime}のためのビルダーを返す.
	 * @return ビルダー
	 */
	public static StartTimeBuilder parameterST() {
		return new StartTimeBuilder();
	}
	/**
	 * {@link StartDate}のためのビルダーを返す.
	 * @return ビルダー
	 */
	public static StartDateBuilder parameterSD() {
		return new StartDateBuilder();
	}
	/**
	 * {@link StartDateCompensation}のためのビルダーを返す.
	 * @return ビルダー
	 */
	public static StartDateCompensationBuilder parameterSH() {
		return new StartDateCompensationBuilder();
	}
	/**
	 * {@link StartDateCompensationDeadline}のためのビルダーを返す.
	 * @return ビルダー
	 */
	public static StartDateCompensationDeadlineBuilder parameterSHD() {
		return new StartDateCompensationDeadlineBuilder();
	}
	/**
	 * {@link RunConditionWatchLimitTime}のためのビルダーを返す.
	 * @return ビルダー
	 */
	public static RunConditionWatchLimitTimeBuilder parameterWT() {
		return new RunConditionWatchLimitTimeBuilder();
	}
	/**
	 * {@link RunConditionWatchLimitCount}のためのビルダーを返す.
	 * @return ビルダー
	 */
	public static RunConditionWatchLimitCountBuilder parameterWC() {
		return new RunConditionWatchLimitCountBuilder();
	}
	/**
	 * {@link StartDateAdjustment}のためのビルダーを返す.
	 * @return ビルダー
	 */
	public static StartDateAdjustmentBuilder parameterCFTD() {
		return new StartDateAdjustmentBuilder();
	}
	/**
	 * {@link Element}のためのビルダーを返す.
	 * @return ビルダー
	 */
	public static ElementBuilder parameterEL() {
		return new ElementBuilder();
	}
	/**
	 * 文字シーケンスを内容とする{@link ParameterValue}のインスタンスを返す.
	 * @param value ユニット定義パラメータ値の内容
	 * @return ユニット定義パラメータ値
	 */
	public static ParameterValue rawStringParameterValue(final CharSequence value) {
		return new RawStringParameterValue(value);
	}
	/**
	 * 引用符で囲われた文字シーケンスを内容とする{@link ParameterValue}のインスタンスを返す.
	 * @param value ユニット定義パラメータ値の内容
	 * @return ユニット定義パラメータ値
	 */
	public static ParameterValue quotedStringParameterValue(final CharSequence value) {
		return new QuotedStringParameterValue(value);
	}
	/**
	 * タプルもどきを内容とする{@link ParameterValue}のインスタンスを返す.
	 * @param value ユニット定義パラメータ値の内容
	 * @return ユニット定義パラメータ値
	 */
	public static ParameterValue tupleParameterValue(final Tuple value) {
		return new TupleParameterValue(value);
	}
	/**
	 * {@link PermissionMode}のインスタンスを返す.
	 * <p>空文字列もしくは{@code null}が引数に指定された場合、パラメータの指定が省略されていることを示すインスタンスが返される。
	 * 4桁の8進数文字列が指定された場合、その値を内容とするインスタンスが返される。
	 * それ以外の値が指定された場合、このメソッドは実行時例外をスローする。</p>
	 * @param hex4 4桁の8進数
	 * @return 許可モード
	 */
	public static PermissionMode permissionMode(final CharSequence hex4) {
		return hex4 == null || hex4.length() == 0 
				? DefaultPermissionMode.NOT_SPECIFIED 
				: new DefaultPermissionMode(hex4.toString());
	}
}
