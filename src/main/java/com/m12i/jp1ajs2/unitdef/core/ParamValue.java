package com.m12i.jp1ajs2.unitdef.core;

/**
 * JP1定義コードのユニット定義パラメータの値をあらわすオブジェクト.
 * 
 * @author mizuki.fujitain
 *
 */
public interface ParamValue {
	/**
	 * @return パラメータ値（未分類値）
	 */
	String getUnclassifiedValue();
	/**
	 * @return パラメータ値（タプルもどき）
	 */
	Tuple getTuploidValue();
	/**
	 * @return パラメータ値（文字列）
	 */
	String getStringValue();
	/**
	 * ユニット定義パラメータのタイプが引数で指定されたタイプと一致するかどうか判定して返す.
	 * @param type パラメータ・タイプ
	 * @return 判定結果（{@code true}:一致する、{@code flase}:一致しない）
	 */
	boolean is(ParamValueType type);
	/**
	 * ユニット定義パラメータ・タイプ
	 * 
	 * @author mizuki.fujitani
	 *
	 */
	public enum ParamValueType {
		UNCLASSIFIED, TUPLOID, STRING, LABELED_VALUE;
	}
}
