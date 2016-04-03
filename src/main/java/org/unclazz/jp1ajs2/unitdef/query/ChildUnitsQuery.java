package org.unclazz.jp1ajs2.unitdef.query;

import org.unclazz.jp1ajs2.unitdef.Unit;

final class ChildUnitsQuery extends RelativeUnitsQuery {
	@Override
	protected Iterable<Unit> source(Unit u) {
		return u.getSubUnits();
	}
}
