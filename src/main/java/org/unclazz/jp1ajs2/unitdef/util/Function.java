package org.unclazz.jp1ajs2.unitdef.util;

/**
 * 引数を1つとって何らかの変換を行う関数を表すインターフェース.
 *
 * @param <T> 引数の型
 * @param <U> 戻り値の型
 */
public interface Function<T, U> {
	/**
	 * 関数を適用する.
	 * @param t 適用対象
	 * @return 適用結果
	 */
	U apply(T t);
}
