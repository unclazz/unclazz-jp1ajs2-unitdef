package org.unclazz.jp1ajs2.unitdef.builder;

import java.util.Iterator;
import java.util.List;
import java.util.NoSuchElementException;

import org.unclazz.jp1ajs2.unitdef.Tuple;
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
		throw new NoSuchElementException(String.format("key is \"%s\""));
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
	
	static final class DefaultTupleEntry implements Tuple.Entry, CharSequential {
		private static final String empty = ""; 
		private final String k;
		private final CharSequence v;
		public DefaultTupleEntry(CharSequence key, CharSequence value){
			k = key.toString();
			v = value;
		}
		public DefaultTupleEntry(CharSequence value){
			k = empty;
			v = value;
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
				return UnitdefUtils.buff().append(getKey()).append('=').append(getValue());
			}
		}
	}

	@Override
	public CharSequence toCharSequence() {
		final StringBuilder sb = UnitdefUtils.buff().append('(');
		for(final Entry e : values){
			if(sb.length() > 1){
				sb.append(',');
			}
			if (e instanceof CharSequential) {
				sb.append(((CharSequential) e).toCharSequence());
			} else {
				sb.append(e);
			}
		}
		return sb.append(')');
	}
}
