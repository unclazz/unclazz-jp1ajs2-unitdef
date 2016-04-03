package org.unclazz.jp1ajs2.unitdef.query;

import java.util.LinkedList;
import java.util.List;

import org.unclazz.jp1ajs2.unitdef.Unit;

final class DescendantUnitsQuery extends RelativeUnitsQuery {
	@Override
	public List<Unit> source(final Unit u) {
		final LinkedList<Unit> l = new LinkedList<Unit>();
		for (final Unit s : u.getSubUnits()) {
			collect(l, s);
		}
		return l;
	}
	
	private void collect(final LinkedList<Unit> l, final Unit u) {
		l.add(u);
		for (final Unit s : u.getSubUnits()) {
			collect(l, s);
		}
	}
}
