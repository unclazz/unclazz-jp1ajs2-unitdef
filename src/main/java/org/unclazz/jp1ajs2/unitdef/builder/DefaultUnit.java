package org.unclazz.jp1ajs2.unitdef.builder;

import java.util.Collections;
import java.util.List;

import org.unclazz.jp1ajs2.unitdef.Attributes;
import org.unclazz.jp1ajs2.unitdef.Component;
import org.unclazz.jp1ajs2.unitdef.FullQualifiedName;
import org.unclazz.jp1ajs2.unitdef.Parameter;
import org.unclazz.jp1ajs2.unitdef.parameter.UnitType;
import org.unclazz.jp1ajs2.unitdef.query.CachedQuery;
import org.unclazz.jp1ajs2.unitdef.query.Query;
import org.unclazz.jp1ajs2.unitdef.query.Q;
import org.unclazz.jp1ajs2.unitdef.util.CharSequenceUtils;
import org.unclazz.jp1ajs2.unitdef.util.Formatter;
import org.unclazz.jp1ajs2.unitdef.Unit;

final class DefaultUnit implements Unit {
	private static final Query<Unit, UnitType> tyQueryStatic = 
			Q.ty().one();
	private static final Query<Unit, CharSequence> cmQueryStatic = 
			Q.cm().one("");

	private final FullQualifiedName fqn;
	private final Attributes attributes;
	private final List<Parameter> parameterList;
	private final List<Unit> subUnitList;
	private final Query<Unit, UnitType> tyQuery = CachedQuery.wrap(tyQueryStatic);
	private final Query<Unit, CharSequence> cmQuery = CachedQuery.wrap(cmQueryStatic);
	private CharSequence serialized = null;
	
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
		return query(tyQuery);
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
		return query(cmQuery);
	}

	@Override
	public CharSequence serialize() {
		if (serialized == null) {
			serialized = Formatter.DEFAULT.format(this);
		}
		return serialized;
	}

	@Override
	public boolean contentEquals(CharSequence other) {
		return CharSequenceUtils.contentsAreEqual(serialize(), other);
	}

	@Override
	public boolean contentEquals(Component other) {
		return CharSequenceUtils.contentsAreEqual(serialize(), other.serialize());
	}
}
