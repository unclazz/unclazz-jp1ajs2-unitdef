package com.m12i.jp1ajs2.unitdef.parser;

import java.util.Collections;
import java.util.Iterator;
import java.util.List;

import com.m12i.jp1ajs2.unitdef.Tuple;
import com.m12i.jp1ajs2.unitdef.util.Maybe;

final class TupleImpl implements Tuple {
	
	private final List<Entry> values;

	public TupleImpl(final List<Entry> list) {
		this.values = Collections.unmodifiableList(list);
	}

	@Override
	public Maybe<String> get(int index) {
		return Maybe.wrap(index < values.size() ? values.get(index).getValue() : null);
	}

	@Override
	public Maybe<String> get(String key) {
		for(Entry e: values){
			if(e.getKey().equals(key)){
				return Maybe.wrap(e.getValue());
			}
		}
		return Maybe.nothing();
	}
	
	@Override
	public String toString(){
		final StringBuilder sb = new StringBuilder();
		sb.append('(');
		for(final Entry e : values){
			if(sb.length() > 1){
				sb.append(',');
			}
			sb.append(e.toString());
		}
		sb.append(')');
		return sb.toString();
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
		return values.iterator();
	}
	
	static final class EntryImpl implements Tuple.Entry {
		private final String k;
		private final String v;
		public EntryImpl(String key, String value){
			k = key;
			v = value;
		}
		public EntryImpl(String value){
			k = "";
			v = value;
		}
		@Override
		public String getKey() {
			return k;
		}
		@Override
		public String getValue() {
			return v;
		}
		@Override
		public String toString() {
			if (getKey().length() == 0) {
				return getValue();
			} else {
				return getKey() + "=" + getValue();
			}
		}
	}
}
