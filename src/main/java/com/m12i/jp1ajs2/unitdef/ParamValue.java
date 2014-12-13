package com.m12i.jp1ajs2.unitdef;

/**
 * ユニット定義パラメータ値.
 */
public interface ParamValue {
	/**
	 * 文字列を取得する.
	 * @return 文字列
	 */
	String getStringValue();
	/**
	 * タプルもどきを取得する.
	 * @return タプルもどき
	 */
	Tuple getTupleValue();
	/**
	 * ユニット定義パラメータ値のフォーマットを取得する.
	 * @return ユニット定義パラメータ値のフォーマット
	 */
	ParamValueFormat getFormat();
}
