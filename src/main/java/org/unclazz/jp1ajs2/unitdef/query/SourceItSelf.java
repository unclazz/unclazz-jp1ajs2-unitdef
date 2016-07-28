package org.unclazz.jp1ajs2.unitdef.query;

import java.util.Collections;

import org.unclazz.jp1ajs2.unitdef.Unit;

final class SourceItSelf implements Query<Unit, Iterable<Unit>> {
	@Override
	public Iterable<Unit> queryFrom(Unit t) {
		return Collections.singleton(t);
	}
}
