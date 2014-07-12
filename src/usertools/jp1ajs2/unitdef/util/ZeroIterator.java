package usertools.jp1ajs2.unitdef.util;

import java.util.Iterator;

class ZeroIterator<E> implements Iterator<E> {
	@Override
	public boolean hasNext() {
		return false;
	}
	@Override
	public E next() {
		return null;
	}
	@Override
	public void remove() {
		// Do nothing.
	}

}
