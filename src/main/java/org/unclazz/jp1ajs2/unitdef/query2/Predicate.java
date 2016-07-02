package org.unclazz.jp1ajs2.unitdef.query2;

/**
 * 何かしらの条件が満たされているかどうかを判断する関数を表すインターフェース.
 * @param <T> 判断の対象となるオブジェクトの型
 */
public interface Predicate<T> {
	/**
	 * 条件が満たされているかどうか判断する.
	 * @param t 判断の対象となるオブジェクト
	 * @return 条件が満たされている場合{@code true}
	 */
	boolean test(T t);
}
