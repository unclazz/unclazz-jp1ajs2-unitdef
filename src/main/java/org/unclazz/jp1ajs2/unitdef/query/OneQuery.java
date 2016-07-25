package org.unclazz.jp1ajs2.unitdef.query;

import java.util.Iterator;
import java.util.NoSuchElementException;

/**
 * ベースとなるクエリが返す{@link Iterable}から最初の要素だけを取得するクエリ.
 * <p>要素が1件もない場合の挙動は初期化の際に与えられたパラメータにより決まる。</p>
 *
 * @param <T> 問合せ対象オブジェクトの型
 * @param <U> 問合せ結果として得られる{@link Iterable}の要素型
 */
public interface OneQuery<T,U> extends Query<T, U> {
	/**
	 * キャッシュ機能付きクエリに変換して返す.
	 * @return クエリ
	 */
	Query<T,U> cached();
}

class DefaultOneQuery<T,U> implements OneQuery<T, U> {
	private final Query<T,Iterable<U>> baseQuery;
	private final boolean nullable;
	private final U defaultValue;
	
	/**
	 * コンストラクタ.
	 * @param baseQuery ベースとなるクエリ
	 * @param nullable {@code true}の場合、クエリは要素が1件もない場合に{@code null}を返す
	 */
	DefaultOneQuery(final Query<T,Iterable<U>> baseQuery, final boolean nullable) {
		this.baseQuery = baseQuery;
		this.nullable = nullable;
		this.defaultValue = null;
	}
	DefaultOneQuery(final Query<T,Iterable<U>> baseQuery, final U defaultValue) {
		this.baseQuery = baseQuery;
		this.nullable = true;
		this.defaultValue = defaultValue;
	}
	@Override
	public U queryFrom(final T t) {
		final Iterator<U> iter = baseQuery.queryFrom(t).iterator();
		if (iter.hasNext()) {
			return iter.next();
		} else if (nullable) {
			return defaultValue;
		} else {
			throw new NoSuchElementException();
		}
	}
	@Override
	public Query<T,U> cached() {
		return CachedQuery.wrap(this);
	}
}
