package org.unclazz.jp1ajs2.unitdef.query2;

import java.util.Collections;

/**
 * 問合せ対象オブジェクトそのものを返すクエリ.
 * @param <T> 問合せ対象の型
 */
final class IdQuery<T> implements Query<T, Iterable<T>> {
	private static final IdQuery<?> instance = new IdQuery<Object>();
	
	/**
	 * クエリのインスタンスを返す.
	 * @return クエリ
	 */
	@SuppressWarnings("unchecked")
	public static<T> IdQuery<T> getInstance() {
		return (IdQuery<T>) instance;
	}
	
	@Override
	public Iterable<T> queryFrom(T t) {
		return Collections.singleton(t);
	}
}
