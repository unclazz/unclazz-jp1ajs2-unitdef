package org.unclazz.jp1ajs2.unitdef.query;

import org.unclazz.jp1ajs2.unitdef.Parameter;
import org.unclazz.jp1ajs2.unitdef.query2.Query;

/**
 * ユニット定義パラメータから値を取得するためのクエリ.
 *
 * @param <R> クエリにより取得される値の型
 */
public interface ParameterQuery<R> extends Query<Parameter, R> {
	/**
	 * ユニット定義パラメータから値を取得して返す.
	 * @param p ユニット定義パラメータ
	 * @return 取得された値
	 */
	R queryFrom(Parameter p);
}
