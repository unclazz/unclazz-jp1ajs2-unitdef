package org.unclazz.jp1ajs2.unitdef;

import java.util.ArrayList;
import java.util.List;

import org.unclazz.jp1ajs2.unitdef.util.UnitWalker;

class UnitCollector extends UnitWalker<List<Unit>> {
	@Override
	protected void handleStart(Unit root, List<Unit> context) {
		// Do nothing.
	}
	@Override
	protected void handleUnitStart(Unit unit, int depth, List<Unit> context) {
		context.add(unit);
	}
	@Override
	protected void handleUnitEnd(Unit unit, int depth, List<Unit> context) {
		// Do nothing.
	}
	@Override
	protected void handleParam(Param param, int depth, List<Unit> context) {
		// Do nothing.
	}
	@Override
	protected void handleEnd(Unit root, List<Unit> context) {
		// Do nothing.
	}
	List<Unit> collect(final Unit root) {
		final ArrayList<Unit> list = new ArrayList<Unit>();
		walk(root, list);
		return list;
	}
}
