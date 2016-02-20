package org.unclazz.jp1ajs2.unitdef.builder;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.unclazz.jp1ajs2.unitdef.Attributes;
import org.unclazz.jp1ajs2.unitdef.FullQualifiedName;
import org.unclazz.jp1ajs2.unitdef.Parameter;
import org.unclazz.jp1ajs2.unitdef.Unit;
import org.unclazz.jp1ajs2.unitdef.util.CharSequenceUtils;

import static org.unclazz.jp1ajs2.unitdef.util.ListUtils.*;

public final class UnitBuilder {
	UnitBuilder() {}
	private FullQualifiedName fqn;
	private Attributes attributes;
	private final List<Parameter> parameterList = linkedList();
	private final List<Unit> subUnitList = linkedList();
	private final Set<String> subUnitNameSet = new HashSet<String>();
	
	public UnitBuilder setFullQualifiedName(final FullQualifiedName fqn) {
		this.fqn = fqn;
		return this;
	}
	public UnitBuilder setFullQualifiedName(final CharSequence... fragments) {
		this.fqn = Builders.fullQualifiedName().addFragments(fragments).build();
		return this;
	}
	public UnitBuilder setFullQualifiedName(final List<CharSequence> fragments) {
		this.fqn = Builders.fullQualifiedName().addFragments(fragments).build();
		return this;
	}
	public UnitBuilder setAttributes(final Attributes attrs) {
		if (attrs == null) {
			throw new NullPointerException();
		}
		this.attributes = attrs;
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
	public UnitBuilder addSubUnit(final Unit unit) {
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
			addSubUnit(unit);
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
		if (fqn == null || attributes == null) {
			throw new NullPointerException();
		}
		if (parameterList.isEmpty()) {
			throw new IllegalArgumentException();
		}
		if (!hasParameterTY()) {
			throw new IllegalArgumentException("parameter \"ty\" must be specified.");
		}
		if (!hasConsistencyBetweenFqnAndAttributes()) {
			throw new IllegalArgumentException("unit must have consistency between "
					+ "unit-name of full-qualified-name and unit-name of attributes.");
		}
		return new DefaultUnit(fqn, attributes, parameterList, subUnitList);
	}
	
	private boolean hasParameterTY() {
		for (final Parameter p : parameterList) {
			if (p.getName().equals("ty")) {
				return true;
			}
		}
		return false;
	}
	
	private boolean hasConsistencyBetweenFqnAndAttributes() {
		final CharSequence fqnUnitName = fqn.getUnitName();
		final CharSequence attesUnitName = attributes.getUnitName();
		return CharSequenceUtils.contentsAreEqual(fqnUnitName, attesUnitName);
	}
}
