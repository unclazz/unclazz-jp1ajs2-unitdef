package org.unclazz.jp1ajs2.unitdef.builder;

import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

import org.unclazz.jp1ajs2.unitdef.Attributes;
import org.unclazz.jp1ajs2.unitdef.FullQualifiedName;
import org.unclazz.jp1ajs2.unitdef.Parameter;
import org.unclazz.jp1ajs2.unitdef.Unit;

public final class UnitBuilder {
	UnitBuilder() {}
	private FullQualifiedName fqn;
	private Attributes attributes;
	private final List<Parameter> parameterList = new LinkedList<Parameter>();
	private final List<Unit> subUnitList = new LinkedList<Unit>();
	private final Set<String> subUnitNameSet = new HashSet<String>();
	
	public UnitBuilder setFullQualifiedName(final FullQualifiedName fqn) {
		this.fqn = fqn;
		return this;
	}
	public UnitBuilder setFullQualifiedName(final CharSequence... fragments) {
		this.fqn = Builders.forFullQualifiedName().addFragments(fragments).build();
		return this;
	}
	public UnitBuilder setFullQualifiedName(final List<CharSequence> fragments) {
		this.fqn = Builders.forFullQualifiedName().addFragments(fragments).build();
		return this;
	}
	public UnitBuilder setAttributes(final Attributes Attributes) {
		this.attributes = Attributes;
		return this;
	}
	public UnitBuilder addParameter(final Parameter Parameter) {
		if (Parameter == null) {
			throw new NullPointerException();
		}
		parameterList.add(Parameter);
		return this;
	}
	public UnitBuilder addParameters(final Parameter... Parameters) {
		for (final Parameter Parameter : Parameters) {
			if (Parameter == null) {
				throw new NullPointerException();
			}
			parameterList.add(Parameter);
		}
		return this;
	}
	public UnitBuilder addParameters(final List<Parameter> Parameters) {
		for (final Parameter Parameter : Parameters) {
			if (Parameter == null) {
				throw new NullPointerException();
			}
			parameterList.add(Parameter);
		}
		return this;
	}
	public UnitBuilder addUnit(final Unit unit) {
		if (unit == null) {
			throw new NullPointerException();
		}
		if (subUnitNameSet.add(unit.getAttributes().getUnitName())) {
			subUnitList.add(unit);
			return this;
		}
		throw new IllegalArgumentException("Duplicated unit name");
	}
	public UnitBuilder addSubUnits(final Unit... units) {
		for (final Unit unit : units) {
			addUnit(unit);
		}
		return this;
	}
	public UnitBuilder addSubUnits(final List<Unit> units) {
		for (final Unit unit : units) {
			if (unit == null) {
				throw new NullPointerException();
			}
			subUnitList.add(unit);
		}
		return this;
	}
	public Unit build() {
		if (fqn == null || attributes == null || parameterList.isEmpty()) {
			throw new IllegalArgumentException();
		}
		return new DefaultUnit(fqn, attributes, parameterList, subUnitList);
	}

}
