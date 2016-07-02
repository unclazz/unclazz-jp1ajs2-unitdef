package org.unclazz.jp1ajs2.unitdef.builder;

import java.util.List;

import org.unclazz.jp1ajs2.unitdef.Parameter;
import org.unclazz.jp1ajs2.unitdef.ParameterValue;
import org.unclazz.jp1ajs2.unitdef.Tuple;
import static org.unclazz.jp1ajs2.unitdef.util.ListUtils.*;

public final class ParameterBuilder {
	ParameterBuilder() {}
	
	private String name;
	private final List<ParameterValue> valueList = linkedList();
	
	public ParameterBuilder setName(String name) {
		this.name = name;
		return this;
	}
	public ParameterBuilder addRawCharSequence(CharSequence cs) {
		valueList.add(new RawStringParameterValue(cs));
		return this;
	}
	public ParameterBuilder addQuoted(CharSequence cs) {
		valueList.add(new QuotedStringParameterValue(cs));
		return this;
	}
	public ParameterBuilder addTuple(Tuple t) {
		valueList.add(new TupleParameterValue(t));
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
			throw new NullPointerException("name of parameter is not specified.");
		}
		if (name.isEmpty()) {
			throw new IllegalArgumentException("name of parameter is empty.");
		}
		if (valueList.isEmpty()) {
			throw new IllegalArgumentException("values of parameter is empty.");
		}
		return new DefauleParameter(name, valueList);
	}
}
