package org.unclazz.jp1ajs2.unitdef.query2;

public final class CachedQuery<T, U> implements Query<T, U> {
	public static<T, U> Query<T, U> wrap(Query<T, U> q) {
		return new CachedQuery<T, U>(q);
	}
	
	private boolean done = false;
	private Query<T, U> baseQuery = null;
	private U cachedResult = null;
	
	private CachedQuery(final Query<T, U> q) {
		QueryUtils.assertNotNull(q, "argument must not be null.");
		
		this.baseQuery = q;
	}
	
	@Override
	public U queryFrom(T t) {
		QueryUtils.assertNotNull(t, "argument must not be null.");
		
		if (done) {
			return cachedResult;
		}
		
		cachedResult = baseQuery.queryFrom(t);
		done = true;
		baseQuery = null;
		return cachedResult;
	}
}
