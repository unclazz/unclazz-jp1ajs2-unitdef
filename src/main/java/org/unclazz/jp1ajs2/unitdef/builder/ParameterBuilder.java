package org.unclazz.jp1ajs2.unitdef.builder;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

import org.unclazz.jp1ajs2.unitdef.Parameter;
import org.unclazz.jp1ajs2.unitdef.ParameterValue;
import org.unclazz.jp1ajs2.unitdef.Tuple;

public final class ParameterBuilder {
	ParameterBuilder() {}
	
	private String name;
	private final List<ParameterValue> valueList = new LinkedList<ParameterValue>();
	
	public ParameterBuilder setName(String name) {
		this.name = name;
		return this;
	}
	public ParameterBuilder addRawCharSequence(CharSequence cs) {
		valueList.add(new CharSequenceParamValue(cs));
		return this;
	}
	public ParameterBuilder addQuoted(CharSequence cs) {
		valueList.add(new QuotedParamValue(cs));
		return this;
	}
	public ParameterBuilder addTuple(Tuple t) {
		valueList.add(new TupleParamValue(t));
		return this;
	}
	public ParameterBuilder addValue(ParameterValue v) {
		valueList.add(v);
		return this;
	}
	public ParameterBuilder addValues(ParameterValue... vs) {
		for (final ParameterValue v : vs) {
			addValue(v);
		}
		return this;
	}
	public ParameterBuilder addValues(List<ParameterValue> vs) {
		for (final ParameterValue v : vs) {
			addValue(v);
		}
		return this;
	}
	public Parameter build() {
		if (name == null) {
			throw new IllegalArgumentException("name of param is not specified");
		}
		return new DefauleParam(name, (valueList.isEmpty() ? Collections.<ParameterValue>emptyList() : valueList));
	}
}
