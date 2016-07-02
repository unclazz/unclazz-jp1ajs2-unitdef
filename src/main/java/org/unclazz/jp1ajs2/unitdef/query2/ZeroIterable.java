package org.unclazz.jp1ajs2.unitdef.query2;

import java.util.Iterator;

final class ZeroIterable<T> implements Iterable<T> {
	private static final ZeroIterable<?> instance = new ZeroIterable<Object>();
	@SuppressWarnings("unchecked")
	public static<T> ZeroIterable<T> getInstance() {
		return (ZeroIterable<T>) instance;
	}
	
	private ZeroIterable() {}
	
	@Override
	public Iterator<T> iterator() {
		return ZeroIterator.getInstance();
	}
}
