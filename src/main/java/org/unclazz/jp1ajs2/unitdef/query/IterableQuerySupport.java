package org.unclazz.jp1ajs2.unitdef.query;

import org.unclazz.jp1ajs2.unitdef.util.Predicate;

abstract class IterableQuerySupport<T,U> implements IterableQuery<T,U> {
	public abstract Query<T,Iterable<U>> and(final Predicate<U> pred);
	public final OneQuery<T, U> one(U defaultValue) {
		return new OneQuery<T, U>(this, defaultValue);
	}
	public final OneQuery<T, U> one(boolean nullable) {
		return new OneQuery<T, U>(this, nullable);
	}
	public final OneQuery<T, U> one() {
		return new OneQuery<T, U>(this, false);
	}
	public final ListQuery<T, U> list() {
		return new StrictListQuery<T, U>(this, false);
	}
	public final ListQuery<T, U> list(boolean cached) {
		return new StrictListQuery<T, U>(this, cached);
	}
	public final Query<T,Iterable<U>> cached() {
		return CachedQuery.wrap(this);
	}
	public final<V> TypedValueIterableQuery<T, U, V> query(final Query<U, V> q) {
		return new TypedValueIterableQuery<T, U, V>(this, q);
	}
}
