package org.unclazz.jp1ajs2.unitdef;

/**
 * ユニット定義パラメータを構成する個々の値の種別.
 */
public enum ParameterValueType {
	/**
	 * 文字シーケンス.
	 */
	CHAR_SEQUENCE,
	/**
	 * 全体が二重引用符で囲われた文字シーケンス.
	 */
	QUOTED,
	/**
	 * タプル.
	 */
	TUPLE;
}
