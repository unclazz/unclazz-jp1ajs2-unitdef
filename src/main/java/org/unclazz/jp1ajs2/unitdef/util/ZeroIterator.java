package org.unclazz.jp1ajs2.unitdef.util;

import java.util.Iterator;
import java.util.NoSuchElementException;

public final class ZeroIterator<T> implements Iterator<T> {
	private static final ZeroIterator<?> instance = new ZeroIterator<Object>();
	@SuppressWarnings("unchecked")
	public static<T> ZeroIterator<T> getInstance() {
		return (ZeroIterator<T>) instance;
	}
	
	private ZeroIterator() {}

	@Override
	public boolean hasNext() {
		return false;
	}
	@Override
	public T next() {
		throw new NoSuchElementException();
	}
	@Override
	public void remove() {
		throw new UnsupportedOperationException();
	}
}
