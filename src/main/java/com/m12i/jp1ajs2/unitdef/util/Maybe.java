package com.m12i.jp1ajs2.unitdef.util;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

//TODO Javadoc, Test
public final class Maybe<T> implements Iterable<T>{
	private final T value;
	private final List<T> valueList;
	
	private Maybe (T value, List<T> valueList) {
		this.value = value;
		this.valueList = valueList;
	}
	/**
	 * {@link Nothing}オブジェクトを返す.
	 * @return {@link Nothing}オブジェクト
	 */
	@SuppressWarnings("unchecked")
	public static<T> Maybe<T> nothing() {
		return (Maybe<T>) NOTHING;
	}
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public static final Maybe<?> NOTHING = new Maybe(null, null);
	/**
	 * 引数で与えられたオブジェクトが{@code null}でれば{@link Nothing}を返しそうでなければ{@link Just}を返す.
	 * @param value ラップしたいオブジェクトもしくは{@code null}
	 * @return {@link Nothing}もしくは{@link Just}
	 */
	public static<T> Maybe<T> wrap(final T value) {
		if (value != null) {
			return new Maybe<T>(value, null);
		} else {
			return nothing();
		}
	}
	@SuppressWarnings("unchecked")
	public static<T> Maybe<T> wrap(final Collection<T> values) {
		if (values == null || values.isEmpty()) {
			return (Maybe<T>) NOTHING;
		} else if (values.size() == 1) {
			return new Maybe<T>(values.iterator().next(), null);
		} else {
			final List<T> list = new ArrayList<T>();
			list.addAll(values);
			return new Maybe<T>(null, list);
		}
	}
	public static<T> Maybe<T> wrap(final T value0, final T value1) {
		final List<T> list = new ArrayList<T>();
		list.add(value0);
		list.add(value1);
		return new Maybe<T>(null, list);
	}
	public static<T> Maybe<T> wrap(final T value0, final T value1, final T value2) {
		final List<T> list = new ArrayList<T>();
		list.add(value0);
		list.add(value1);
		list.add(value2);
		return new Maybe<T>(null, list);
	}
	@SuppressWarnings("unchecked")
	public static<T> Maybe<T> wrap(final T value0, final T value1, final T value2, final T... values) {
		final List<T> list = new ArrayList<T>();
		list.add(value0);
		list.add(value1);
		list.add(value2);
		list.addAll(Arrays.asList(values));
		return new Maybe<T>(null, list);
	}
	
	public boolean isOne() {
		return value != null;
	}
	public boolean isJust() {
		return isOne();
	}
	public boolean isMultiple() {
		return valueList != null;
	}
	/**
	 * レシーバ・オブジェクトが{@link Nothing}のインスタンスか判定する.
	 * @return 判定結果
	 */
	public boolean isNothing() {
		return value == null && valueList == null;
	}
	
	public boolean isEmpty() {
		return isNothing();
	}
	
	public int size() {
		return isNothing() ? 0 : (isOne() ? 1 : valueList.size());
	}
	
	/**
	 * ラップされた値を取り出す.
	 * @return 取り出された値
	 */
	public T get() {
		return isMultiple() ? valueList.get(0) : value;
	}
	/**
	 * ラップされた値を取り出す.
	 * @param alt 代替となる値
	 * @return 取り出された値もしくは代替の値
	 */
	public T getOrElse(T alt) {
		return isNothing() ? alt : get();
	}
	
	public List<T> getList() {
		if (isNothing()) {
			return Collections.emptyList();
		} else if (isOne()) {
			final List<T> list = new ArrayList<T>();
			list.add(value);
			return list;
		} else {
			return valueList;
		}
	}

	@Override
	public Iterator<T> iterator() {
		if (isNothing()) {
			return ZeroIterator.getInstance();
		} else if (isOne()) {
			return new OneIterator<T>(value);
		} else {
			return valueList.iterator();
		}
	}
	@Override
	public String toString() {
		if (isNothing()) {
			return "Nothing";
		} else if (isOne()) {
			return String.format("Just(%s)", value);
		} else {
			return String.format("Multiple(%s)", valueList);
		}
	}
	@Override
	public int hashCode() {
		if (isNothing()) return -1;
		
		final int prime = 31;
		if (isOne()) {
			return prime + value.hashCode();
		} else {
			return prime + valueList.hashCode();
		}
	}
	@SuppressWarnings("rawtypes")
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		if (isNothing() && this != obj)
			return false;
		final Maybe other = (Maybe) obj;
		if (isOne()) {
			if (!other.isOne() || !value.equals(other.value))
				return false;
		} else if (isMultiple()) {
			if (!other.isMultiple() || !valueList.equals(other.valueList))
				return false;
		}
		return true;
	}
}
