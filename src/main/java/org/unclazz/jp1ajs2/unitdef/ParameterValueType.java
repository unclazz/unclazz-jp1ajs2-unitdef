package org.unclazz.jp1ajs2.unitdef;

/**
 * ユニット定義パラメータを構成する個々の値の種別.
 */
public enum ParameterValueType {
	/**
	 * 文字シーケンス.
	 */
	RAW_STRING,
	/**
	 * 全体が二重引用符で囲われた文字シーケンス.
	 */
	QUOTED_STRING,
	/**
	 * タプル.
	 */
	TUPLE;
}
