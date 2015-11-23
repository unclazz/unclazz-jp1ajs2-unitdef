package org.unclazz.jp1ajs2.unitdef.builder;

import java.util.Iterator;

final class UnremovableIterator<E> implements Iterator<E> {
	private final Iterator<E> inner;
	UnremovableIterator(final Iterator<E> inner) {
		this.inner = inner;
	}
	@Override
	public boolean hasNext() {
		return inner.hasNext();
	}
	@Override
	public E next() {
		return inner.next();
	}
	@Override
	public void remove() {
		throw new UnsupportedOperationException();
	}
}
