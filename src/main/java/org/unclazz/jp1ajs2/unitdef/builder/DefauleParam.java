package org.unclazz.jp1ajs2.unitdef.builder;

import java.util.Iterator;
import java.util.List;

import org.unclazz.jp1ajs2.unitdef.ParameterValueQuery;
import org.unclazz.jp1ajs2.unitdef.Parameter;
import org.unclazz.jp1ajs2.unitdef.ParameterValue;

class DefauleParam implements Parameter, BeAbleToCharSequence {
	private final int size;
	private final String name;
	private final List<ParameterValue> values;
	
	DefauleParam(final CharSequence name, final List<ParameterValue> values) {
		this.size = values.size();
		this.values = values;
		this.name = name.toString();
	}
	
	@Override
	public Iterator<ParameterValue> iterator() {
		return new Iterator<ParameterValue>() {
			private final Iterator<ParameterValue> inner = values.iterator();
			@Override
			public boolean hasNext() {
				return inner.hasNext();
			}

			@Override
			public ParameterValue next() {
				return inner.next();
			}

			@Override
			public void remove() {
				throw new UnsupportedOperationException();
			}
		};
	}

	@Override
	public String getName() {
		return name;
	}

	@Override
	public ParameterValue getValue(int i) {
		return values.get(i);
	}

	@Override
	public <R> R getValue(int i, ParameterValueQuery<R> q) {
		return values.get(i).query(q);
	}

	@Override
	public int getValueCount() {
		return size;
	}

	@Override
	public String toString() {
		return toCharSequence().toString();
	}

	@Override
	public CharSequence toCharSequence() {
		final StringBuilder buff = new StringBuilder();
		buff.append(name).append('=');
		final int initLen = buff.length();
		for (final ParameterValue value : values) {
			if (buff.length() > initLen) {
				buff.append(',');
			}
			if (value instanceof BeAbleToCharSequence) {
				buff.append(((BeAbleToCharSequence) value).toCharSequence());
			} else {
				buff.append(value);
			}
		}
		return buff;
	}
}
