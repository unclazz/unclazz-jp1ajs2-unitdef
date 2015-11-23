package org.unclazz.jp1ajs2.unitdef;

import java.util.List;

public interface UnitQuery<T> {
	List<T> queryFrom(Unit unit);
}
