package org.unclazz.jp1ajs2.unitdef.query;

import org.unclazz.jp1ajs2.unitdef.query.InternalQueries.JointIterableQuery;
import org.unclazz.jp1ajs2.unitdef.util.Predicate;

abstract class IterableQuerySupport<T,U> implements IterableQuery<T,U> {
	public abstract Query<T,Iterable<U>> and(final Predicate<U> pred);
	@Override
	public final OneQuery<T, U> one(U defaultValue) {
		return new DefaultOneQuery<T, U>(this, defaultValue);
	}
	@Override
	public final OneQuery<T, U> one(boolean nullable) {
		return new DefaultOneQuery<T, U>(this, nullable);
	}
	@Override
	public final OneQuery<T, U> one() {
		return new DefaultOneQuery<T, U>(this, false);
	}
	@Override
	public final ListQuery<T, U> list() {
		return new DefaultListQuery<T, U>(this, false);
	}
	@Override
	public final ListQuery<T, U> list(boolean cached) {
		return new DefaultListQuery<T, U>(this, cached);
	}
	@Override
	public final Query<T,Iterable<U>> cached() {
		return CachedQuery.wrap(this);
	}
	@Override
	public final<V> JointIterableQuery<T, U, V> query(final Query<U, V> q) {
		return new JointIterableQuery<T, U, V>(this, q);
	}
}
