package org.unclazz.jp1ajs2.unitdef.util;

/**
 * 何らかの整数値を表わすオブジェクトのインターフェース.
 */
public interface Integral extends Comparable<Integral>, CharSequential {
	/**
	 * このオブジェクトが表わす整数値を返す.
	 * @return 整数値
	 */
	int intValue();
	/**
	 * このオブジェクトが表わす整数値を返す.
	 * @return 整数値
	 */
	long longValue();
	/**
	 * このオブジェクトの文字列表現を返す.
	 * @param radix 基数
	 * @return 文字列表現
	 */
	String toString(int radix);
	/**
	 * このオブジェクトの文字列表現を返す.
	 * @return 文字列表現
	 */
	String toString();
}
