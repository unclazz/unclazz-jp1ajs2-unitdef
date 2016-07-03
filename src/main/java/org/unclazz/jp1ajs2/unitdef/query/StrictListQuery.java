package org.unclazz.jp1ajs2.unitdef.query;

import java.util.LinkedList;
import java.util.List;

final class StrictListQuery<T,U> implements Query<T, List<U>> {
	private final Query<T, Iterable<U>> baseQuery;
	
	StrictListQuery(final Query<T, Iterable<U>> baseQuery) {
		QueryUtils.assertNotNull(baseQuery, "argument must not be null.");
		this.baseQuery = baseQuery;
	}

	@Override
	public List<U> queryFrom(T t) {
		final LinkedList<U> r = new LinkedList<U>();
		for (final U u : baseQuery.queryFrom(t)) {
			r.addLast(u);
		}
		return r;
	}
}
