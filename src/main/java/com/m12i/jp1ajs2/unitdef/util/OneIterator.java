package com.m12i.jp1ajs2.unitdef.util;

import java.util.Iterator;

/**
 * ただ1つの要素を返すだけのイテレータ実装.
 * {@link Maybe}や{@link ParseResult}にて使用されるオブジェクトです。
 * 
 * @param <E> {@link #next()}が返すと仮定される要素の型
 */
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
