package org.unclazz.jp1ajs2.unitdef.query;

import java.util.List;

import org.unclazz.jp1ajs2.unitdef.util.Predicate;

abstract class AbstractItrableQuery<T,U> implements Query<T, Iterable<U>> {
	public abstract Query<T,Iterable<U>> and(final Predicate<U> pred);
	
	public final Query<T, U> one(U defaultValue) {
		return new OneQuery<T, U>(this, defaultValue);
	}
	public final Query<T, U> one(boolean nullable) {
		return new OneQuery<T, U>(this, nullable);
	}
	public final Query<T, U> one() {
		return new OneQuery<T, U>(this, false);
	}
	public final Query<T, List<U>> list() {
		return new StrictListQuery<T, U>(this);
	}
	public final<V> TypedValueIterableQuery<T, U, V> query(final Query<U, V> f) {
		return new TypedValueIterableQuery<T, U, V>(this, f);
	}
}
