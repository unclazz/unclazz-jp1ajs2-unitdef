package com.m12i.jp1ajs2.unitdef;

import java.util.ArrayList;
import java.util.List;

final class DescendentUnitsCollector extends UnitCollector {
	private final String unitName;
	DescendentUnitsCollector(final String unitName) {
		this.unitName = unitName;
	}
	@Override
	protected void handleUnitStart(Unit unit, int depth, List<Unit> context) {
		if (depth > 0 && unit.getName().equals(unitName)) context.add(unit);
	}
	@Override
	List<Unit> collect(final Unit root) {
		final ArrayList<Unit> list = new ArrayList<Unit>();
		walk(root, list);
		return list;
	}
}
