package org.doogwood.jp1ajs2.unitdef.parser;

import java.util.Collections;
import java.util.List;

import org.doogwood.jp1ajs2.unitdef.Param;
import org.doogwood.jp1ajs2.unitdef.ParamValue;
import org.doogwood.jp1ajs2.unitdef.Unit;

final class ParamImpl implements Param {
	private final String name;
	private final List<ParamValue> values;
	private Unit unit;

	public ParamImpl(final String name,
			final List<ParamValue> values) {
		this.name = name;
		this.values = Collections.unmodifiableList(values);
	}

	@Override
	public String getName() {
		return name;
	}

	@Override
	public List<ParamValue> getValues() {
		return values;
	}
	
	@Override
	public ParamValue getValue(int i) {
		return values.get(i);
	}
	
	@Override
	public String getValue() {
		final StringBuilder sb = new StringBuilder();
		for (final ParamValue v : getValues()) {
			if (sb.length() > 0) {
				sb.append(",");
			}
			sb.append(v.toString());
		}
		return sb.toString();
	}
	
	void setUnit(Unit unit) {
		this.unit = unit;
	}
	
	@Override
	public Unit getUnit() {
		return unit;
	}
	
	@Override
	public String toString() {
		return name + "=" + getValue();
	}
}
