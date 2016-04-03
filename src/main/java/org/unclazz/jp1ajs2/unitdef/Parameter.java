package org.unclazz.jp1ajs2.unitdef;

import org.unclazz.jp1ajs2.unitdef.query.ParameterQuery;
import org.unclazz.jp1ajs2.unitdef.util.CharSequential;

/**
 * ユニット定義パラメータを表わすインターフェース.
 */
public interface Parameter extends Iterable<ParameterValue>, CharSequential {
	/**
	 * ユニット定義パラメータ名を返す.
	 * このメソッドが返す値は{@code null}でも{@code ""}でもないことが保証されている。
	 * @return ユニット定義パラメータ名
	 */
	String getName();
	/**
	 * ユニット定義パラメータを構成するパラメータ値を返す.
	 * @param i 添字
	 * @return ユニット定義パラメータ
	 * @throws IndexOutOfBoundsException 指定された添字に対応する値が存在しない場合
	 */
	ParameterValue getValue(int i);
	/**
	 * ユニット定義パラメータの数を返す.
	 * @return ユニット定義パラメータ数
	 */
	int getValueCount();
	/**
	 * クエリを使用してユニット定義パラメータから情報を取得する.
	 * @param <R> クエリにより返される値の型
	 * @param q クエリ
	 * @return クエリにより取得された値
	 */
	<R> R query(ParameterQuery<R> q);
}
