package org.unclazz.jp1ajs2.unitdef;

/**
 * ユニット定義パラメータを構成する個々の値が持つ情報を抽出・加工するためのクエリ.
 *
 * @param <T> 抽出・加工される値の型
 */
public interface ParameterValueQuery<T> {
	/**
	 * ユニット定義パラメータを構成する値が持つ情報を抽出・加工して返す.
	 * @param v ユニット定義パラメータの値
	 * @return 抽出・加工された値
	 */
	T queryFrom(ParameterValue v);
}
