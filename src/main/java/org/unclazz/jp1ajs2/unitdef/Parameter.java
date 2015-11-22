package org.unclazz.jp1ajs2.unitdef;

import java.util.List;

/**
 * ユニット定義パラメータ.
 */
public interface Parameter {
	/**
	 * ユニット定義パラメータ名を取得する.
	 * @return ユニット定義パラメータ名
	 */
	String getName();
	/**
	 * ユニット定義パラメータ値のリストを取得する.
	 * @return ユニット定義パラメータ値のリスト
	 */
	List<ParameterValue> getValues();
	/**
	 * ユニット定義パラメータ値に添字でアクセスする.
	 * @param i 添字
	 * @return ユニット定義パラメータ値
	 */
	ParameterValue getValue(int i);
	/**
	 * 単一の文字列値としてユニット定義パラメータ値を取得する.
	 * @return ユニット定義パラメータ値
	 */
	String getValue();
	/**
	 * このパラメータを持つユニットへの参照を返す.
	 * @return ユニット定義
	 */
	Unit getUnit();
}
