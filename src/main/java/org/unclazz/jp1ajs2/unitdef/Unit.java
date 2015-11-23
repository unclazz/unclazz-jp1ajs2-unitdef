package org.unclazz.jp1ajs2.unitdef;

import java.util.List;

public interface Unit extends Iterable<Unit> {
	FullQualifiedName getFullQualifiedName();
	Attributes getAttributes();
	String getName();
	UnitType getType();
	List<Parameter> getParameters();
	List<Parameter> getParameters(String name);
	Parameter getParameter(String name);
	<T> List<T> query(UnitQuery<T> r);
	List<Unit> getSubUnits();
	Unit getSubUnit(String name);
}
