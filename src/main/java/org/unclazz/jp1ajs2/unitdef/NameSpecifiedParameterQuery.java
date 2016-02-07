package org.unclazz.jp1ajs2.unitdef;

import java.util.LinkedList;
import java.util.List;

public final class NameSpecifiedParameterQuery implements UnitQuery<Parameter> {
	static NameSpecifiedParameterQuery parameter(final String name) {
		return new NameSpecifiedParameterQuery(name);
	}
	
	private final String name;
	private NameSpecifiedParameterQuery(final String name) {
		this.name = name;
	}
	
	@Override
	public List<Parameter> queryFrom(final Unit unit) {
		final List<Parameter> result = new LinkedList<Parameter>();
		for (final Parameter p : unit.getParameters()) {
			if (p.getName().equalsIgnoreCase(name)) {
				result.add(p);
			}
		}
		return result;
	}
	
	public SubscriptedQueryFactory item(final int i) {
		return new SubscriptedQueryFactory(name, i);
	}
}
