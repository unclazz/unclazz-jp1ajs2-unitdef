package org.unclazz.jp1ajs2.unitdef.util;

import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.ListIterator;
import java.util.NoSuchElementException;

/**
 * 初期化に際して指定されたデータソースから要素を遅延読み込みするリスト.
 * <p>このリストはイテレータや別のコレクションや配列をデータソースとして内包する。
 * リストの各種メソッドが呼び出されたとき、
 * そのメソッドのオペレーション上最低限必要と考えられる要素のみがデータソースから読み込まれる。</p>
 * <p>メソッド呼び出しとデータ読み込みの関係の例：
 * <ul>
 * <li>{@link #isEmpty()}が呼び出されたとき、データソースからの読み込みは一切発生しない。</li>
 * <li>{@link #get(int)}が呼び出されたとき、引数で指定されたインデックスまでの要素がデータソースから読み込まれるが、
 * それ以降の要素については読み込まれない。</li>
 * <li>{@link #size()}が呼び出されたとき、読み込み可能なすべての要素が読み込まれる。</li>
 * </ul>
 * <p>このリストは{@link List}で宣言されたメソッドのうち参照系のメソッドのみをサポートする。
 * 更新系のメソッドが呼びだされた場合は{@link UnsupportedOperationException}がスローされる。</p>
 * 
 * @param <T> リストの要素型
 */
public final class LazyList<T> implements List<T> {
	private static final LazyList<Object> empty = 
			new LazyList<Object>(Collections.emptyIterator()); 

	/**
	 * 要素を1つも持たないインスタンスを返す.
	 * @return インスタンス
	 */
	@SuppressWarnings("unchecked")
	public static<T> LazyList<T> empty() {
		return (LazyList<T>) empty;
	}
	
	/**
	 * 引数で指定されたコレクションをデータソースとするインスタンスを返す.
	 * @param c コレクション
	 * @return インスタンス
	 */
	public static<T> LazyList<T> of(final Collection<T> c) {
		if (c.isEmpty()) {
			return empty();
		}
		return new LazyList<T>(c.iterator());
	}
	
	/**
	 * 引数で指定された配列をデータソースとするインスタンスを返す.
	 * @param a 配列
	 * @return インスタンス
	 */
	public static<T> LazyList<T> of(final T... a) {
		if (a.length == 0) {
			return empty();
		}
		return new LazyList<T>(Arrays.asList(a).iterator());
	}
	
	/**
	 * 引数で指定されたイテレータをデータソースとするインスタンスを返す.
	 * @param iter イテレータ
	 * @return インスタンス
	 */
	public static<T> LazyList<T> wrap(final Iterator<T> iter) {
		if (iter.hasNext()) {
			return new LazyList<T>(iter);
		}
		return empty();
	}
	
	private final Iterator<T> source;
	private final List<T> fetched;

	private LazyList(final Iterator<T> source) {
		this.source = source;
		this.fetched = new LinkedList<T>();
	}
	
	private LazyList(final List<T> fetched, final Iterator<T> source) {
		this.source = source;
		this.fetched = fetched;
	}
	
	/**
	 * 指定されたインデックスまで要素を読み込む.
	 * すでに十分な数の要素が読み込まれている場合は何もしない。
	 * @param i インデックス
	 * @return 取込みが行われた場合{@code true}
	 */
	private boolean load(final int i) {
		final int fetchedSize = fetched.size();
		if (fetchedSize > i) {
			return false;
		}
		while (source.hasNext() && fetched.size() < i + 1) {
			fetched.add(source.next());
		}
		return fetchedSize < fetched.size();
	}
	
	/**
	 * 読み込みが可能なすべての要素を読み込む.
	 * すでに読み込み可能なすべての要素が読み込み済みである場合は何もしない。
	 * @return 取込みが行われた場合{@code true}
	 */
	private boolean loadAll() {
		final int fetchedSize = fetched.size();
		while (source.hasNext()) {
			fetched.add(source.next());
		}
		return fetchedSize < fetched.size();
	}
	
	/**
	 * すでに読み込みが終わっている要素の数.
	 * 読み込み可能なすべての要素が読み込み済み出ない場合、
	 * このメソッドが返す数値は{@link #size()}より小さい数値となる。
	 * @return すでに読み込みが終わっている要素の数
	 */
	public int minimumSize() {
		return fetched.size();
	}
	
	@Override
	public int size() {
		if (source.hasNext()) {
			loadAll();
		}
		return fetched.size();
	}

	@Override
	public boolean isEmpty() {
		if (fetched.isEmpty()) {
			if (source.hasNext()) {
				return false;
			} else {
				return true;
			}
		}
		return false;
	}

	@Override
	public Iterator<T> iterator() {
		return new Iterator<T>() {
			private int current = 0;
			@Override
			public boolean hasNext() {
				return current < fetched.size() || source.hasNext();
			}
			@Override
			public T next() {
				if (load(current)) {
					return fetched.get(current ++);
				}
				throw new NoSuchElementException();
			}
			@Override
			public void remove() {
				throw new UnsupportedOperationException();
			}
		};
	}

	@Override
	public Object[] toArray() {
		loadAll();
		return fetched.toArray();
	}

	@Override
	public <U> U[] toArray(U[] a) {
		loadAll();
		return fetched.toArray(a);
	}

	@Override
	public boolean add(T e) {
		throw new UnsupportedOperationException();
	}

	@Override
	public boolean remove(Object o) {
		throw new UnsupportedOperationException();
	}

	@Override
	public boolean containsAll(Collection<?> c) {
		for (final Object e : c) {
			if (!contains(e)) {
				return false;
			}
		}
		return true;
	}

	@Override
	public boolean addAll(Collection<? extends T> c) {
		throw new UnsupportedOperationException();
	}

	@Override
	public boolean addAll(int index, Collection<? extends T> c) {
		throw new UnsupportedOperationException();
	}

	@Override
	public boolean removeAll(Collection<?> c) {
		throw new UnsupportedOperationException();
	}

	@Override
	public boolean retainAll(Collection<?> c) {
		throw new UnsupportedOperationException();
	}

	@Override
	public void clear() {
		throw new UnsupportedOperationException();
	}

	@Override
	public T get(int index) {
		load(index);
		return fetched.get(index);
	}

	@Override
	public T set(int index, T element) {
		throw new UnsupportedOperationException();
	}

	@Override
	public void add(int index, T element) {
		throw new UnsupportedOperationException();
	}

	@Override
	public T remove(int index) {
		throw new UnsupportedOperationException();
	}

	@Override
	public int indexOf(Object o) {
		final int i = fetched.indexOf(o);
		if (i != -1) {
			return i;
		}
		while (source.hasNext()) {
			final T e = source.next();
			fetched.add(e);
			if (e != null && e.equals(o)) {
				return fetched.size() - 1;
			}
		}
		return -1;
	}

	@Override
	public int lastIndexOf(Object o) {
		loadAll();
		return fetched.lastIndexOf(o);
	}

	@Override
	public ListIterator<T> listIterator() {
		loadAll();
		return Collections.unmodifiableList(fetched).listIterator();
	}

	@Override
	public ListIterator<T> listIterator(int index) {
		loadAll();
		return Collections.unmodifiableList(fetched).listIterator(index);
	}

	@Override
	public List<T> subList(int fromIndex, int toIndex) {
		load(toIndex - 1);
		return Collections.unmodifiableList(fetched).subList(fromIndex, toIndex);
	}

	@Override
	public boolean contains(Object o) {
		if (fetched.contains(o)) {
			return true;
		}
		while (source.hasNext()) {
			final T e = source.next();
			fetched.add(e);
			if (e != null && e.equals(o)) {
				return true;
			}
		}
		return false;
	}
	
	/**
	 * リストの先頭要素を返す.
	 * @return 先頭要素
	 * @throws NoSuchElementException リストが空である場合
	 */
	public T head() {
		load(0);
		if (isEmpty()) {
			throw new NoSuchElementException();
		}
		return fetched.get(0);
	}
	
	/**
	 * リストの非先頭要素からなるサブリストを返す.
	 * <p>リストの要素数が0の場合このメソッドは例外をスローする。
	 * リストの要素数が1の場合このメソッドは空のリストを返す。</p>
	 * <p>元のリスト（このメソッドのレシーバ・オブジェクトであるリスト）と
	 * 新しいリスト（このメソッドが返したサブリスト）とは、
	 * 読み込み済みリストやデータソースを共有する。
	 * したがって新しいリストにおいてデータソースからの読み込みが必要なオペレーションが行われると、
	 * その{@link #minimumSize()}だけでなく、元のリストの{@link #minimumSize()}もまた変動する。</p>
	 * @return
	 */
	public LazyList<T> tail() {
		load(0);
		if (isEmpty()) {
			throw new NoSuchElementException();
		}
		return new LazyList<T>(fetched.subList(1, fetched.size()), source);
	}
}
