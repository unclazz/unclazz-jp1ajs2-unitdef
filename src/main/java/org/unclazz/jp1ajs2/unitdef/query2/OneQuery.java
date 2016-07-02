package org.unclazz.jp1ajs2.unitdef.query2;

import java.util.Iterator;
import java.util.NoSuchElementException;

/**
 * ベースとなるクエリが返す{@link Iterable}から最初の要素だけを取得するクエリ.
 * <p>要素が1件もない場合の挙動は初期化の際に与えられたパラメータにより決まる。</p>
 *
 * @param <T> 問合せ対象オブジェクトの型
 * @param <U> 問合せ結果として得られる{@link Iterable}の要素型
 */
final class OneQuery<T,U> implements Query<T, U> {
	private final Query<T,Iterable<U>> baseQuery;
	private final boolean nullable;
	
	/**
	 * コンストラクタ.
	 * @param baseQuery ベースとなるクエリ
	 * @param nullable {@code true}の場合、クエリは要素が1件もない場合に{@code null}を返す
	 */
	OneQuery(final Query<T,Iterable<U>> baseQuery, final boolean nullable) {
		this.baseQuery = baseQuery;
		this.nullable = nullable;
	}
	@Override
	public U queryFrom(final T t) {
		final Iterator<U> iter = baseQuery.queryFrom(t).iterator();
		if (iter.hasNext()) {
			return iter.next();
		} else if (nullable) {
			return null;
		} else {
			throw new NoSuchElementException();
		}
	}
}
