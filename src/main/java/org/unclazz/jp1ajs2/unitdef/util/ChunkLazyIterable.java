package org.unclazz.jp1ajs2.unitdef.util;

import java.util.Collections;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.NoSuchElementException;

public final class ChunkLazyIterable<T,U> implements Iterable<U> {
	public static final class ChunkYield<T> {
		private static final ChunkYield<?> nullInstance = new ChunkYield<Object>(null, 0);
		private static final ChunkYield<?> voidInstance = new ChunkYield<Object>(null, 1);
		private static final ChunkYield<?> breakInstance = new ChunkYield<Object>(null, 2);
		@SuppressWarnings("unchecked")
		public static<T> ChunkYield<T> yieldReturn(Iterable<T> value) {
			return (ChunkYield<T>) (value == null ? nullInstance : new ChunkYield<T>(value, 0));
		}
		@SuppressWarnings("unchecked")
		public static<T> ChunkYield<T> yieldVoid() {
			return (ChunkYield<T>) voidInstance;
		}
		@SuppressWarnings("unchecked")
		public static<T> ChunkYield<T> yieldBreak() {
			return (ChunkYield<T>) breakInstance;
		}
		
		private final Iterable<T> value;
		private final int mode; // 0: return, 1: void, 2: break
		private ChunkYield(Iterable<T> value, int mode){
			this.value = value;
			this.mode = mode;
		}
		public Iterable<T> get() {
			if (mode == 0) {
				return value;
			} else {
				throw new NoSuchElementException();
			}
		}
		public boolean isReturn() {
			return mode == 0;
		}
		public boolean isVoid() {
			return mode == 1;
		}
		public boolean isBreak() {
			return mode == 2;
		}
		@Override
		public boolean equals(Object other) {
			if (this == other) {
				return true;
			}
			if (mode == 0 && other instanceof ChunkYield) {
				final ChunkYield<?> that = (ChunkYield<?>)other;
				return this.get() == that.get();
			}
			return false;
		}
	}
	public static interface ChunkYieldCallable<T,U> {
		ChunkYield<U> yield(T item, int index);
	}
	private static class IteratorBasedLazyIterator<T,U> implements Iterator<U>{
		private final Iterator<T> source;
		private final ChunkYieldCallable<T, U> callable;
		
		private LinkedList<U> chunk = new LinkedList<U>();
		private int index = -1;
		
		private IteratorBasedLazyIterator(final Iterator<T> source, final ChunkYieldCallable<T, U> callable) {
			this.source = source;
			this.callable = callable;
		}
		
		private boolean loadChunk() {
			while (source.hasNext()) {
				final ChunkYield<U> y = callable.yield(source.next(), ++index);
				if (y.isBreak()) {
					break;
				}
				if (y.isVoid()) {
					continue;
				}
				if (y.isReturn()) {
					chunk.clear();
					for (final U u : y.get()) {
						chunk.addLast(u);
					}
					if (chunk.isEmpty()) {
						continue;
					} else {
						return true;
					}
				}
			}
			return false;
		}
		
		@Override
		public boolean hasNext() {
			if (!chunk.isEmpty()) {
				return true;
			} else if (source.hasNext()) {
				return loadChunk();
			} else {
				return false;
			}
		}

		@Override
		public U next() {
			if (hasNext()) {
				return chunk.removeFirst();
			} else {
				throw new NoSuchElementException();
			}
		}
		
		@Override
		public void remove() {
			notSupportedRemoveMethod();
		}
	}
	private static final class SupplierBasedIterator<T> implements Iterator<T>{
		private final Supplier<T> supplier;
		private boolean next = true;
		private SupplierBasedIterator(Supplier<T> s) {
			this.supplier = s;
		}
		@Override
		public boolean hasNext() {
			return next;
		}
		@Override
		public T next() {
			if (!next) {
				throw new NoSuchElementException();
			}
			try {
				return supplier.get();
			} catch (final NoSuchElementException e) {
				next = false;
				throw e;
			}
		}
		@Override
		public void remove() {
			notSupportedRemoveMethod();
		}
	}
	private static final class IteratorWrapper<T> implements Iterable<T> {
		private final Iterator<T> iter;
		private IteratorWrapper(Iterator<T> iter) {
			this.iter = iter;
		}
		@Override
		public Iterator<T> iterator() {
			return iter;
		}
	}
	/**
	 * データソースを表すインターフェース.
	 * <p>{@link Iterator}と同じく例外{@link NoSuchElementException}をスローすることで
	 * 取得可能な値がなくなったことを示す。{@link Iterator#hasNext()}に相当するメソッドは存在しないので、
	 * 例外がこの状況を示す唯一の手段となる。</p>
	 * @param <T> データソースから取得できる値の型
	 */
	public static interface Supplier<T> {
		/**
		 * データソースから値を1つ取得する.
		 * @return 値
		 * @throws NoSuchElementException データソースから取得可能な値がなくなった場合
		 */
		T get();
	}
	
	/**
	 * 単一値をデータソースとする{@link Iterable}を生成して返す.
	 * @param source データソースとなる単一値
	 * @param callable データソースから取得された値をもとに判断・加工を行ってその値と制御情報を反復子に提供するインターフェース
	 * @return {@link Iterable}のインスタンス
	 */
	public static<T,U> Iterable<U> forOnce(final T source, final ChunkYieldCallable<T, U> callable) {
		return new ChunkLazyIterable<T,U>(Collections.singleton(source), callable);
	}
	/**
	 * {@link Iterable}をデータソースとする{@link Iterable}を生成して返す.
	 * <p>データソースからの値の取得とそれに伴う判断・加工の処理は可能な限り遅らせられる。
	 * @param source データソースとなる{@link Iterable}
	 * @param callable データソースから取得された値をもとに判断・加工を行ってその値と制御情報を反復子に提供するインターフェース
	 * @return {@link Iterable}のインスタンス
	 */
	public static<T,U> Iterable<U> forEach(final Iterable<T> source, final ChunkYieldCallable<T, U> callable) {
		return new ChunkLazyIterable<T,U>(source, callable);
	}
	/**
	 * {@link Supplier}をデータソースとする{@link Iterable}を生成して返す.
	 * <p>データソースからの値の取得とそれに伴う判断・加工の処理は可能な限り遅らせられる。
	 * @param source データソースとなる{@link Supplier}
	 * @param callable データソースから取得された値をもとに判断・加工を行ってその値と制御情報を反復子に提供するインターフェース
	 * @return {@link Iterable}のインスタンス
	 */
	public static<T,U> Iterable<U> forEach(final Supplier<T> source, final ChunkYieldCallable<T, U> callable) {
		return new ChunkLazyIterable<T,U>(new IteratorWrapper<T>(new SupplierBasedIterator<T>(source)), callable);
	}
	private static void notSupportedRemoveMethod() {
		throw new UnsupportedOperationException(String.format(
				"%s and relative iterators does not support remove() method.",
				IteratorBasedLazyIterator.class.getSimpleName()));
	}
	
	private final Iterable<T> source;
	private final ChunkYieldCallable<T, U> callable;
	
	private ChunkLazyIterable(final Iterable<T> source, final ChunkYieldCallable<T, U> callable) {
		this.source = source;
		this.callable = callable;
	}
	
	@Override
	public Iterator<U> iterator() {
		return new IteratorBasedLazyIterator<T, U>(source.iterator(), callable);
	}
}
