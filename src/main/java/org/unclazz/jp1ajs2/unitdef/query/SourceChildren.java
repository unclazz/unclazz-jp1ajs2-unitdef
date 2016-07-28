package org.unclazz.jp1ajs2.unitdef.query;

import org.unclazz.jp1ajs2.unitdef.Unit;

final class SourceChildren implements Query<Unit, Iterable<Unit>> {
	@Override
	public Iterable<Unit> queryFrom(Unit t) {
		return t.getSubUnits();
	}
}
