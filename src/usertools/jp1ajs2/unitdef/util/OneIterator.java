package usertools.jp1ajs2.unitdef.util;

import java.util.Iterator;

public class OneIterator<E> implements Iterator<E> {
	private final E e;
	private boolean first = true;
	public OneIterator(E e) {
		this.e = e;
	}
	@Override
	public boolean hasNext() {
		return first;
	}
	@Override
	public E next() {
		if (first) {
			first = false;
			return e;
		} else {
			return null;
		}
	}

	@Override
	public void remove() {
		// Do nothing.
	}

}
