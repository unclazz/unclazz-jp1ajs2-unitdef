package org.unclazz.jp1ajs2.unitdef.util;

import java.util.Collections;
import java.util.Iterator;
import java.util.NoSuchElementException;

/**
 * 遅延評価による反復子{@link Iterator}を提供する{@link Iterable}の実装クラス.
 * <p>このクラスの{@link #iterator()}メソッドが返す反復子はデータソースからの値を取得を可能な限り遅らせる。
 * なお反復子のメソッド{@link Iterator#remove()}はサポートされておらず、
 * 呼びだされた場合はかならず例外{@link UnsupportedOperationException}をスローする。</p>
 * @param <T> データソースが提供する値の型
 * @param <U> 反復子が返す値の型
 */
public final class LazyIterable<T,U> implements Iterable<U> {
	/**
	 * {@link YieldCallable#yield(Object, int)}が値を返すのに用いるコンテナ.
	 * <p>インスタンスは以下の3種のstaticファクトリ・メソッドを通じて取得する：</p>
	 * <dl>
	 * <dt>{@link #yieldReturn(Object)}</dt>
	 * <dd>値を返す。データソースから取得された値は{@link LazyIterable}による反復処理に反映される。</dd>
	 * <dt>{@link #yieldVoid()}</dt>
	 * <dd>値を返さない。データソースから取得された値は{@link LazyIterable}による反復処理に反映されない。</dd>
	 * <dt>{@link #yieldBreak()}</dt>
	 * <dd>値を返さない。データソースからの値の取得を停止する。取得された値は{@link LazyIterable}による反復処理に反映されない。</dd>
	 * </dl>
	 * @param <T> コンテナが内包する値の型
	 */
	public static final class Yield<T> {
		private static final Yield<?> nullInstance = new Yield<Object>(null, 0);
		private static final Yield<?> voidInstance = new Yield<Object>(null, 1);
		private static final Yield<?> breakInstance = new Yield<Object>(null, 2);
		@SuppressWarnings("unchecked")
		public static<T> Yield<T> yieldReturn(T value) {
			return (Yield<T>) (value == null ? nullInstance : new Yield<T>(value, 0));
		}
		@SuppressWarnings("unchecked")
		public static<T> Yield<T> yieldVoid() {
			return (Yield<T>) voidInstance;
		}
		@SuppressWarnings("unchecked")
		public static<T> Yield<T> yieldBreak() {
			return (Yield<T>) breakInstance;
		}
		
		private final T value;
		private final int mode; // 0: return, 1: void, 2: break
		private Yield(T value, int mode){
			this.value = value;
			this.mode = mode;
		}
		public T get() {
			if (mode == 0) {
				return value;
			} else {
				throw new NoSuchElementException();
			}
		}
		public T get(T defaultValue) {
			return mode == 0 ? value : defaultValue;
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
			if (mode == 0 && other instanceof Yield) {
				final Yield<?> that = (Yield<?>)other;
				return this.get().equals(that.get(null));
			}
			return false;
		}
	}
	/**
	 * 遅延評価による反復子のためユーザが実装するインターフェース.
	 * <p>データソースから取得された値を引数にとり、その内容に基づき何かしらの判断・加工などを行った上で、
	 * その結果を制御情報とともに返す。結果値と制御情報は反復子により利用される。
	 * 値と制御情報の返し方については{@link Yield}のドキュメントを参照のこと。</p>
	 *
	 * @param <T> データソースが提供する値の型
	 * @param <U> 反復子が返す値の型
	 * @throws NoSuchElementException データソースが同例外をスローした場合。
	 * 			{@link Yield#yieldBreak()}と同じ意味に解釈される。
	 */
	public static interface YieldCallable<T,U> {
		Yield<U> yield(T item, int index);
	}
	private static class IteratorBasedLazyIterator<T,U> implements Iterator<U>{
		private final Iterator<T> source;
		private final YieldCallable<T, U> callable;
		
		private U cachedItem = null;
		private boolean cacheIsEmpty = true;
		private int index = -1;
		
		private IteratorBasedLazyIterator(final Iterator<T> source, final YieldCallable<T, U> callable) {
			this.source = source;
			this.callable = callable;
		}
		
		public boolean loadItem() {
			while (source.hasNext()) {
				try {
					final Yield<U> y = callable.yield(source.next(), ++index);
					if (y.isBreak()) {
						break;
					}
					if (y.isVoid()) {
						continue;
					}
					if (y.isReturn()) {
						cachedItem = y.get();
						cacheIsEmpty = false;
						return true;
					}
				} catch (final NoSuchElementException e) {
					break;
				}
			}
			cachedItem = null;
			cacheIsEmpty = true;
			return false;
		}
		@Override
		public boolean hasNext() {
			return cacheIsEmpty ? loadItem() : true;
		}
		@Override
		public U next() {
			if (hasNext()) {
				final U next = cachedItem;
				cachedItem = null;
				cacheIsEmpty = true;
				return next;
			}
			throw new NoSuchElementException();
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
	public static<T,U> Iterable<U> forOnce(final T source, final YieldCallable<T, U> callable) {
		return new LazyIterable<T,U>(Collections.singleton(source), callable);
	}
	/**
	 * {@link Iterable}をデータソースとする{@link Iterable}を生成して返す.
	 * <p>データソースからの値の取得とそれに伴う判断・加工の処理は可能な限り遅らせられる。
	 * @param source データソースとなる{@link Iterable}
	 * @param callable データソースから取得された値をもとに判断・加工を行ってその値と制御情報を反復子に提供するインターフェース
	 * @return {@link Iterable}のインスタンス
	 */
	public static<T,U> Iterable<U> forEach(final Iterable<T> source, final YieldCallable<T, U> callable) {
		return new LazyIterable<T,U>(source, callable);
	}
	/**
	 * {@link Supplier}をデータソースとする{@link Iterable}を生成して返す.
	 * <p>データソースからの値の取得とそれに伴う判断・加工の処理は可能な限り遅らせられる。
	 * @param source データソースとなる{@link Supplier}
	 * @param callable データソースから取得された値をもとに判断・加工を行ってその値と制御情報を反復子に提供するインターフェース
	 * @return {@link Iterable}のインスタンス
	 */
	public static<T,U> Iterable<U> forEach(final Supplier<T> source, final YieldCallable<T, U> callable) {
		return new LazyIterable<T,U>(new IteratorWrapper<T>(new SupplierBasedIterator<T>(source)), callable);
	}
	private static void notSupportedRemoveMethod() {
		throw new UnsupportedOperationException(String.format(
				"%s and relative iterators does not support remove() method.",
				IteratorBasedLazyIterator.class.getSimpleName()));
	}
	
	private final Iterable<T> source;
	private final YieldCallable<T, U> callable;
	
	private LazyIterable(final Iterable<T> source, final YieldCallable<T, U> callable) {
		this.source = source;
		this.callable = callable;
	}
	
	@Override
	public Iterator<U> iterator() {
		return new IteratorBasedLazyIterator<T, U>(source.iterator(), callable);
	}
}
