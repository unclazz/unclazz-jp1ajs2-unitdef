package org.unclazz.jp1ajs2.unitdef.parser;

import java.util.Iterator;
import java.util.NoSuchElementException;

/**
 * パース処理の結果を格納するオブジェクト.
 * @param <T> パース結果の型
 */
public class ParseResult<T> implements Iterable<T> {
	static final class OneIterator<E> implements Iterator<E> {
		OneIterator(final E next) {
			this.next = next;
		}
		private E next;
		@Override
		public boolean hasNext() {
			return next != null;
		}
		@Override
		public E next() {
			if (next != null) {
				final E ret = next;
				next = null;
				return ret;
			} else {
				throw new NoSuchElementException();
			}
		}
		@Override
		public void remove() {}
	}
	
	static final class ZeroIterator<E> implements Iterator<E> {
		@Override
		public boolean hasNext() {
			return false;
		}
		@Override
		public E next() {
			throw new NoSuchElementException();
		}
		@Override
		public void remove() {}
	}
	
	public static<T> ParseResult<T> failure(final Throwable error) {
		return new ParseResult<T>(null, error);
	}
	public static<T> ParseResult<T> successful(final T result) {
		return new ParseResult<T>(result, null);
	}
	
	private T result;
	private Throwable error;
	
	private ParseResult(final T result, final Throwable error) {
		this.result = result;
		this.error = error;
	}

	@Override
	public Iterator<T> iterator() {
		return error == null ? new ZeroIterator<T>() : new OneIterator<T>(result);
	}
	public T get() {
		return result;
	}
	public T getOrElse(final T alternative) {
		return result == null ? alternative : result;
	}
	public boolean isSuccessful() {
		return error == null;
	}
	public Throwable getError() {
		return error;
	}
}
