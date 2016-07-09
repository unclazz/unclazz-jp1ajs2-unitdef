package org.unclazz.jp1ajs2.unitdef.query;

import java.util.LinkedList;
import java.util.List;

final class StrictListQuery<T,U> implements ListQuery<T, U> {
	private final boolean cached;
	private final Query<T, Iterable<U>> baseQuery;
	private T prevTarget = null;
	private LinkedList<U> prevResult = null;
	
	StrictListQuery(final Query<T, Iterable<U>> baseQuery, final boolean cached) {
		QueryUtils.assertNotNull(baseQuery, "argument must not be null.");
		this.baseQuery = baseQuery;
		this.cached = cached;
	}

	@Override
	public List<U> queryFrom(final T t) {
		if (cached) {
			if (prevTarget == t) {
				return prevResult;
			}
			final LinkedList<U> r = new LinkedList<U>();
			for (final U u : baseQuery.queryFrom(t)) {
				r.addLast(u);
			}
			prevTarget = t;
			prevResult = r;
			return r;
		} else {
			final LinkedList<U> r = new LinkedList<U>();
			for (final U u : baseQuery.queryFrom(t)) {
				r.addLast(u);
			}
			return r;
		}
	}

	@Override
	public Query<T, U> first() {
		return new Query<T, U>(){
			private final Query<T, List<U>> baseQuery = StrictListQuery.this;
			@Override
			public U queryFrom(T t) {
				return baseQuery.queryFrom(t).get(0);
			}
		};
	}

	@Override
	public Query<T, U> first(final boolean nullale) {
		return nullale ? first(null) : first();
	}

	@Override
	public Query<T, U> first(final U defaultValue) {
		return new Query<T, U>(){
			private final Query<T, List<U>> baseQuery = StrictListQuery.this;
			@Override
			public U queryFrom(T t) {
				final List<U> list = baseQuery.queryFrom(t);
				return list.isEmpty() ? defaultValue : list.get(0);
			}
		};
	}

	@Override
	public Query<T, U> last() {
		return new Query<T, U>(){
			private final Query<T, List<U>> baseQuery = StrictListQuery.this;
			@Override
			public U queryFrom(T t) {
				final List<U> list = baseQuery.queryFrom(t);
				return list.get(list.size() - 1);
			}
		};
	}

	@Override
	public Query<T, U> last(final boolean nullale) {
		return nullale ? last(null) : last();
	}

	@Override
	public Query<T, U> last(final U defaultValue) {
		return new Query<T, U>(){
			private final Query<T, List<U>> baseQuery = StrictListQuery.this;
			@Override
			public U queryFrom(T t) {
				final List<U> list = baseQuery.queryFrom(t);
				return list.isEmpty() ? defaultValue : list.get(list.size() - 1);
			}
		};
	}
}
