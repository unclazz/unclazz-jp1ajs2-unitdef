package com.m12i.jp1ajs2.unitdef.parser;

import com.m12i.jp1ajs2.unitdef.TupleEntry;

final class TupleEntryImpl implements TupleEntry {
	private final String k;
	private final String v;
	public TupleEntryImpl(String key, String value){
		k = key;
		v = value;
	}
	public TupleEntryImpl(String value){
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
