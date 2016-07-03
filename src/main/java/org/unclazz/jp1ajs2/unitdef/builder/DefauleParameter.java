package org.unclazz.jp1ajs2.unitdef.builder;

import java.util.Collections;
import java.util.List;

import org.unclazz.jp1ajs2.unitdef.util.CharSequenceUtils;
import org.unclazz.jp1ajs2.unitdef.Component;
import org.unclazz.jp1ajs2.unitdef.Parameter;
import org.unclazz.jp1ajs2.unitdef.ParameterValue;
import org.unclazz.jp1ajs2.unitdef.query.Query;

class DefauleParameter implements Parameter {
	private final String name;
	private final List<ParameterValue> values;
	
	DefauleParameter(final CharSequence name, final List<ParameterValue> values) {
		this.values = Collections.unmodifiableList(values);
		this.name = name.toString();
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
	public String toString() {
		return serialize().toString();
	}

	@Override
	public CharSequence serialize() {
		final StringBuilder buff = CharSequenceUtils.builder();
		buff.append(name).append('=');
		final int initLen = buff.length();
		for (final ParameterValue value : values) {
			if (buff.length() > initLen) {
				buff.append(',');
			}
			buff.append(value.serialize());
		}
		return buff;
	}

	@Override
	public boolean contentEquals(CharSequence other) {
		return CharSequenceUtils.contentsAreEqual(serialize(), other);
	}

	@Override
	public boolean contentEquals(Component other) {
		return contentEquals(other.serialize());
	}

	@Override
	public <R> R query(Query<Parameter,R> q) {
		return q.queryFrom(this);
	}
}
