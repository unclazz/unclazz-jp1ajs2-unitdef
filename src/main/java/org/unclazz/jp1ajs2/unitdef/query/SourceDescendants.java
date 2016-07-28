package org.unclazz.jp1ajs2.unitdef.query;

import org.unclazz.jp1ajs2.unitdef.Unit;
import org.unclazz.jp1ajs2.unitdef.util.UnitTreeNodesIterable;

final class SourceDescendants implements Query<Unit, Iterable<Unit>> {
	private final boolean includesRoot;
	SourceDescendants(boolean includesRoot) {
		this.includesRoot = includesRoot;
	}
	@Override
	public Iterable<Unit> queryFrom(Unit t) {
		return UnitTreeNodesIterable.ofBreadthFirst(t, includesRoot);
	}
}
