package org.unclazz.jp1ajs2.unitdef.query;

import org.unclazz.jp1ajs2.unitdef.Unit;

/**
 * ユニット、ユニット定義パラメータ、パラメータ値に対して値を問合せるためのインターフェース.
 * <p>このインターフェースを実装し{@link Unit#query(Query)}メソッドなどとともに利用することで、
 * 任意のオブジェクトに対するタイプセーフな問合せを実現することができる。</p>
 * <p>定義済みのクエリは{@link Queries}ユーティリティ・クラスにより提供されている。</p>
 *
 * @param <T> 問合せ対象の型
 * @param <U> 問合せ結果の型
 */
public interface Query<T, U> {
	/**
	 * オブジェクトに対して問合せを行う.
	 * @param t 問合せ対象
	 * @return 問合せ結果
	 */
	U queryFrom(T t);
}
