package org.unclazz.jp1ajs2.unitdef.query;

final class JointQuery<T,U,V> implements Query<T, V>, OneQuery<T, V> {
	static<T,U,V> JointQuery<T,U,V> join(Query<T, U> q0, Query<U, V> q1) {
		return new JointQuery<T, U, V>(q0, q1);
	}
	
	private final Query<T, U> q0;
	private final Query<U, V> q1;
	
	private JointQuery(Query<T, U> q0, Query<U, V> q1) {
		this.q0 = q0;
		this.q1 = q1;
	}
	
	@Override
	public V queryFrom(T t) {
		return q1.queryFrom(q0.queryFrom(t));
	}
	@Override
	public Query<T, V> cached() {
		return CachedQuery.wrap(this);
	}
}
