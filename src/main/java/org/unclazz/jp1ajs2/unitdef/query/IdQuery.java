package org.unclazz.jp1ajs2.unitdef.query;

import org.unclazz.jp1ajs2.unitdef.util.LazyIterable;
import org.unclazz.jp1ajs2.unitdef.util.LazyIterable.Yield;
import org.unclazz.jp1ajs2.unitdef.util.LazyIterable.YieldCallable;

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
	public Iterable<T> queryFrom(final T t) {
		return LazyIterable.forOnce(t, new YieldCallable<T, T>(){
			@Override
			public Yield<T> yield(T item, int index) {
				return Yield.yieldReturn(item);
			}
		});
	}
}
