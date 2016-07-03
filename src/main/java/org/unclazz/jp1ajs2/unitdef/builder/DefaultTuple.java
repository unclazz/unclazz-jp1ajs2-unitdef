package org.unclazz.jp1ajs2.unitdef.builder;

import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Set;

import org.unclazz.jp1ajs2.unitdef.Component;
import org.unclazz.jp1ajs2.unitdef.Tuple;
import org.unclazz.jp1ajs2.unitdef.util.CharSequenceUtils;

final class DefaultTuple implements Tuple, Component {
	
	private final List<Entry> values;

	DefaultTuple(final List<Entry> list) {
		this.values = list;
	}

	@Override
	public CharSequence get(int index) {
		return values.get(index).getValue();
	}

	@Override
	public CharSequence get(String key) {
		for(Entry e: values){
			if(key.toString().equals(e.getKey())){
				return e.getValue();
			}
		}
		throw new NoSuchElementException(String.format("key is \"%s\"", key));
	}
	
	@Override
	public String toString(){
		return serialize().toString();
	}

	@Override
	public int size() {
		return values.size();
	}

	@Override
	public boolean isEmpty() {
		return values.isEmpty();
	}

	@Override
	public Iterator<Entry> iterator() {
		return new UnremovableIterator<Tuple.Entry>(values.iterator());
	}
	
	static final class DefaultTupleEntry implements Tuple.Entry {
		private static final String empty = ""; 
		private final String k;
		private final CharSequence v;
		public DefaultTupleEntry(CharSequence key, CharSequence value){
			if (value == null) {
				throw new NullPointerException();
			}
			k = key.toString();
			v = value;
		}
		public DefaultTupleEntry(CharSequence value){
			if (value == null) {
				throw new NullPointerException();
			}
			k = empty;
			v = value;
		}
		@Override
		public boolean hasKey() {
			return ! k.isEmpty();
		}
		@Override
		public String getKey() {
			return k;
		}
		@Override
		public CharSequence getValue() {
			return v;
		}
		@Override
		public String toString() {
			return serialize().toString();
		}
		@Override
		public CharSequence serialize() {
			if (getKey() == empty) {
				return getValue();
			} else {
				return CharSequenceUtils.builder().append(getKey()).append('=').append(getValue());
			}
		}
		@Override
		public boolean contentEquals(CharSequence other) {
			return CharSequenceUtils.contentsAreEqual(serialize(), other);
		}
		@Override
		public boolean contentEquals(Component other) {
			return contentEquals(other.serialize());
		}
	}

	@Override
	public CharSequence serialize() {
		final StringBuilder sb = CharSequenceUtils.builder().append('(');
		for(final Entry e : values){
			if(sb.length() > 1){
				sb.append(',');
			}
			sb.append(e.serialize());
		}
		return sb.append(')');
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
	public Set<String> keySet() {
		final HashSet<String> r = new HashSet<String>(values.size());
		for (final Entry e : values) {
			final String k = e.getKey();
			if (!k.isEmpty()) {
				r.add(k);
			}
		}
		return r;
	}
}
