package com.m12i.jp1ajs2.unitdef.util;

import java.util.Iterator;

/**
 * いかなる要素も返すことがないイテレータ実装.
 * {@link Option}や{@link ParseResult}にて使用されるオブジェクトです。
 * 
 * @param <E> {@link #next()}が返すと仮定される要素の型
 */
public class ZeroIterator<E> implements Iterator<E> {
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
