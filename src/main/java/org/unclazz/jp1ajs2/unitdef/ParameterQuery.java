package org.unclazz.jp1ajs2.unitdef;

/**
 * ユニット定義パラメータから値を取得するためのクエリ.
 *
 * @param <T> クエリにより取得される値の型
 */
public interface ParameterQuery<T> {
	/**
	 * ユニット定義パラメータから値を取得して返す.
	 * @param p ユニット定義パラメータ
	 * @return 取得された値
	 */
	T queryFrom(Parameter p);
}
