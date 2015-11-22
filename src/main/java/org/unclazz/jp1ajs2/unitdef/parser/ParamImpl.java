package org.unclazz.jp1ajs2.unitdef.parser;

import java.util.List;

import org.unclazz.jp1ajs2.unitdef.Parameter;
import org.unclazz.jp1ajs2.unitdef.ParameterValue;
import org.unclazz.jp1ajs2.unitdef.Unit;
import org.unclazz.jp1ajs2.unitdef.util.ListUtils;

final class ParamImpl implements Parameter {
	private final String name;
	private final List<ParameterValue> values;
	private Unit unit;

	ParamImpl(final String name,
			final List<ParameterValue> values) {
		this.name = name;
		this.values = ListUtils.immutableList(values);
	}

	@Override
	public String getName() {
		return name;
	}

	@Override
	public List<ParameterValue> getValues() {
		return values;
	}
	
	@Override
	public ParameterValue getValue(int i) {
		return values.get(i);
	}
	
	@Override
	public String getValue() {
		final StringBuilder sb = new StringBuilder();
		for (final ParameterValue v : getValues()) {
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
