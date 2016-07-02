package org.unclazz.jp1ajs2.unitdef.builder;

import java.util.Collections;
import java.util.List;

import org.unclazz.jp1ajs2.unitdef.util.CharSequenceUtils;
import org.unclazz.jp1ajs2.unitdef.util.CharSequential;
import org.unclazz.jp1ajs2.unitdef.Parameter;
import org.unclazz.jp1ajs2.unitdef.ParameterValue;
import org.unclazz.jp1ajs2.unitdef.query.ParameterQuery;

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
		return toCharSequence().toString();
	}

	@Override
	public CharSequence toCharSequence() {
		final StringBuilder buff = CharSequenceUtils.builder();
		buff.append(name).append('=');
		final int initLen = buff.length();
		for (final ParameterValue value : values) {
			if (buff.length() > initLen) {
				buff.append(',');
			}
			buff.append(value.toCharSequence());
		}
		return buff;
	}

	@Override
	public boolean contentEquals(CharSequence other) {
		return CharSequenceUtils.contentsAreEqual(toCharSequence(), other);
	}

	@Override
	public boolean contentEquals(CharSequential other) {
		return contentEquals(other.toCharSequence());
	}

	@Override
	public <R> R query(ParameterQuery<R> q) {
		return q.queryFrom(this);
	}
}
