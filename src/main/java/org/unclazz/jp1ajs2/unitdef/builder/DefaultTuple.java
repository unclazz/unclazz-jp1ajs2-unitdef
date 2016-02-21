package org.unclazz.jp1ajs2.unitdef.builder;

import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Set;

import org.unclazz.jp1ajs2.unitdef.Tuple;
import org.unclazz.jp1ajs2.unitdef.util.CharSequenceUtils;
import org.unclazz.jp1ajs2.unitdef.util.CharSequential;

final class DefaultTuple implements Tuple, CharSequential {
	
	private final List<Entry> values;

	DefaultTuple(final List<Entry> list) {
		this.values = list;
	}

	@Override
	public CharSequence get(int index) {
		return values.get(index).getValue();
	}

	@Override
	public CharSequence get(CharSequence key) {
		for(Entry e: values){
			if(key.toString().equals(e.getKey())){
				return e.getValue();
			}
		}
		throw new NoSuchElementException(String.format("key is \"%s\"", key));
	}
	
	@Override
	public String toString(){
		return toCharSequence().toString();
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
			return toCharSequence().toString();
		}
		@Override
		public CharSequence toCharSequence() {
			if (getKey() == empty) {
				return getValue();
			} else {
				return CharSequenceUtils.builder().append(getKey()).append('=').append(getValue());
			}
		}
		@Override
		public boolean contentEquals(CharSequence other) {
			return CharSequenceUtils.contentsAreEqual(toCharSequence(), other);
		}
		@Override
		public boolean contentEquals(CharSequential other) {
			return contentEquals(other.toCharSequence());
		}
	}

	@Override
	public CharSequence toCharSequence() {
		final StringBuilder sb = CharSequenceUtils.builder().append('(');
		for(final Entry e : values){
			if(sb.length() > 1){
				sb.append(',');
			}
			sb.append(e.toCharSequence());
		}
		return sb.append(')');
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
