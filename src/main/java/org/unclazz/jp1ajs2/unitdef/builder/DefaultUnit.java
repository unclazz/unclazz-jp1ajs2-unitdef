package org.unclazz.jp1ajs2.unitdef.builder;

import java.util.Collections;
import java.util.List;

import org.unclazz.jp1ajs2.unitdef.Attributes;
import org.unclazz.jp1ajs2.unitdef.FullQualifiedName;
import org.unclazz.jp1ajs2.unitdef.Parameter;
import org.unclazz.jp1ajs2.unitdef.parameter.UnitType;
import org.unclazz.jp1ajs2.unitdef.query.UnitQueries;
import org.unclazz.jp1ajs2.unitdef.query2.Query;
import org.unclazz.jp1ajs2.unitdef.Unit;

final class DefaultUnit implements Unit {

	private final FullQualifiedName fqn;
	private final Attributes attributes;
	private final List<Parameter> parameterList;
	private final List<Unit> subUnitList;
	
	DefaultUnit(FullQualifiedName fqn, Attributes attributes,
			List<Parameter> parameterList, List<Unit> subUnitList) {
		this.fqn = fqn;
		this.attributes = attributes;
		this.parameterList = parameterList;
		this.subUnitList = Collections.unmodifiableList(subUnitList);
	}
	
	@Override
	public FullQualifiedName getFullQualifiedName() {
		return fqn;
	}
	@Override
	public Attributes getAttributes() {
		return attributes;
	}
	@Override
	public String getName() {
		return attributes.getUnitName();
	}
	@Override
	public UnitType getType() {
		return query(UnitQueries.ty()).get(0);
	}
	@Override
	public List<Parameter> getParameters() {
		return Collections.unmodifiableList(parameterList);
	}

	@Override
	public <R> R query(Query<Unit,R> r) {
		return r.queryFrom(this);
	}

	@Override
	public List<Unit> getSubUnits() {
		return subUnitList;
	}

	@Override
	public Unit getSubUnit(String name) {
		for (final Unit s : subUnitList) {
			if (s.getName().equals(name)) {
				return s;
			}
		}
		return null;
	}

	@Override
	public CharSequence getComment() {
		// TODO Auto-generated method stub
		return null;
	}
}
