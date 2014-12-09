package com.m12i.jp1ajs2.unitdef;

import java.util.Iterator;

/**
 * いかなる要素も返すことがないイテレータ実装.
 * {@link Maybe}や{@link ParseResult}にて使用されるオブジェクトです。
 * 
 * @param <E> {@link #next()}が返すと仮定される要素の型
 */
public class ZeroIterator<E> implements Iterator<E> {
	@SuppressWarnings({ "rawtypes" })
	private static final ZeroIterator<?> INSTANCE = new ZeroIterator();
	
	@SuppressWarnings("unchecked")
	public static<E> ZeroIterator<E> getInstance() {
		return (ZeroIterator<E>) INSTANCE;
	}
	
	private ZeroIterator() {}
	
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
