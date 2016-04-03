package org.unclazz.jp1ajs2.unitdef.query;

import org.unclazz.jp1ajs2.unitdef.ParameterValue;

/**
 * ユニット定義パラメータを構成する個々の値が持つ情報を抽出・加工するためのクエリ.
 *
 * @param <R> 抽出・加工される値の型
 */
public interface ParameterValueQuery<R> {
	/**
	 * ユニット定義パラメータを構成する値が持つ情報を抽出・加工して返す.
	 * @param v ユニット定義パラメータの値
	 * @return 抽出・加工された値
	 */
	R queryFrom(ParameterValue v);
}
