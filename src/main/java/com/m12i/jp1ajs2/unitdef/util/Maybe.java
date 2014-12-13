package com.m12i.jp1ajs2.unitdef.util;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

/**
 * 何らかの操作の結果として得られる値をラップするオブジェクト.<br>
 * <p>当該操作の結果、得られる値の有無や値の数が不明確であることを表わす。
 * オブジェクトの初期化には{@link #wrap(Object)}系の静的メソッドを使用する。</p>
 * <p>このオブジェクトはHaskellにおける{@code Maybe a}をある点で拡張したものある。
 * このオブジェクトには3種類のインスタンスが存在する：</p>
 * <ul>
 * <li>{@code Nothing}: 値が存在しないことを表わす</li>
 * <li>{@code One}: 値が1つだけ存在することを表わす</li>
 * <li>{@code Multiple}: 値が2つ以上の任意の個数存在することを表わす</li>
 * </ul>
 * @param <T> 要素型
 */
public final class Maybe<T> implements Iterable<T>{
	private final T value;
	private final List<T> valueList;
	
	private Maybe (T value, List<T> valueList) {
		this.value = value;
		this.valueList = valueList;
	}
	/**
	 * {@code Nothing}インスタンスを返す.
	 * @return インスタンス
	 */
	@SuppressWarnings("unchecked")
	public static<T> Maybe<T> nothing() {
		return (Maybe<T>) NOTHING;
	}
	/**
	 * {@code Nothing}インスタンス.
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public static final Maybe<?> NOTHING = new Maybe(null, null);
	/**
	 * 引数で指定したオブジェクトをラップして返す.<br>
	 * <p>引数に{@code null}が設定された場合、{@code Nothing}が返されます。
	 * それ以外の場合は{@code One}が返されます。</p>
	 * @param value ラップ対象のオブジェクト
	 * @return インスタンス
	 */
	public static<T> Maybe<T> wrap(final T value) {
		if (value != null) {
			return new Maybe<T>(value, null);
		} else {
			return nothing();
		}
	}
	/**
	 * 引数で指定したコレクションの要素をラップして返す.<br>
	 * <p>引数に{@code null}が設定された場合と引数に指定されたコレクションが空である場合、{@code Nothing}が返される。
	 * 引数に指定されたコレクションの要素数が{@code 1}の場合は{@code One}が返される。
	 * 引数に指定されたコレクションの要素数が{@code 2}以上の場合は{@code Multiple}が返される。</p>
	 * @param values ラップ対象のオブジェクトを格納したコレクション
	 * @return インスタンス
	 */
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
	/**
	 * 引数で指定したオブジェクトをラップして返す.
	 * @param value0 1つめのオブジェクト
	 * @param value1 2つめのオブジェクト
	 * @return インスタンス
	 */
	public static<T> Maybe<T> wrap(final T value0, final T value1) {
		final List<T> list = new ArrayList<T>();
		list.add(value0);
		list.add(value1);
		return new Maybe<T>(null, list);
	}
	/**
	 * 引数で指定したオブジェクトをラップして返す.
	 * @param value0 1つめのオブジェクト
	 * @param value1 2つめのオブジェクト
	 * @param value2 3つめのオブジェクト
	 * @return インスタンス
	 */
	public static<T> Maybe<T> wrap(final T value0, final T value1, final T value2) {
		final List<T> list = new ArrayList<T>();
		list.add(value0);
		list.add(value1);
		list.add(value2);
		return new Maybe<T>(null, list);
	}
	/**
	 * 引数で指定したオブジェクトをラップして返す.
	 * @param value0 1つめのオブジェクト
	 * @param value1 2つめのオブジェクト
	 * @param value2 3つめのオブジェクト
	 * @param values 残りのオブジェクト
	 * @return インスタンス
	 */
	public static<T> Maybe<T> wrap(final T value0, final T value1, final T value2, final T... values) {
		final List<T> list = new ArrayList<T>();
		list.add(value0);
		list.add(value1);
		list.add(value2);
		list.addAll(Arrays.asList(values));
		return new Maybe<T>(null, list);
	}
	/**
	 * このインスタンスが{@code One}であるかどうかを判定する.
	 * @return 判定結果
	 */
	public boolean isOne() {
		return value != null;
	}
	/**
	 * {@link #isOne()}メソッドと同義.
	 * @return 判定結果
	 */
	public boolean isJust() {
		return isOne();
	}
	/**
	 * このインスタンスが{@code Multiple}であるかどうかを判定する.
	 * @return 判定結果
	 */
	public boolean isMultiple() {
		return valueList != null;
	}
	/**
	 * このインスタンスが{@link Nothing}であるかどうかを判定する.
	 * @return 判定結果
	 */
	public boolean isNothing() {
		return value == null && valueList == null;
	}
	/**
	 * {@link #isNothing()}メソッドと同義.
	 * @return 判定結果
	 */
	public boolean isEmpty() {
		return isNothing();
	}
	/**
	 * このインスタンスが保持している値の数を返す.<br>
	 * <p>{@code Nothing}の場合は{@code 0}を返す。
	 * {@code One}の場合は{@code 1}を返す。
	 * {@code Multiple}の場合はコレクションの要素数を返す。</p>
	 * @return 値の数
	 */
	public int size() {
		return isNothing() ? 0 : (isOne() ? 1 : valueList.size());
	}
	/**
	 * ラップされた値を取り出す.<br>
	 * <p>{@code Nothing}の場合は{@code null}を返す。
	 * {@code Multiple}の場合はコレクションの1番目の要素を返す。</p>
	 * @return 取り出された値
	 */
	public T get() {
		return isMultiple() ? valueList.get(0) : value;
	}
	/**
	 * ラップされた値を取り出す.<br>
	 * <p>{@link #get()}メソッドとちがって、
	 * {@code Nothing}の場合に{@code null}ではなく引数で指定された代替値を返す。</p>
	 * @param alt 代替となる値
	 * @return 取り出された値もしくは代替の値
	 */
	public T getOrElse(T alt) {
		return isNothing() ? alt : get();
	}
	/**
	 * ラップされた値を取り出す.
	 * @param i 添字
	 * @return 取り出された値
	 */
	public T get(int i) {
		if (isNothing()) {
			return null;
		} else if (i == 0 && isOne()) {
			return value;
		} else {
			return valueList.get(i);
		}
	}
	/**
	 * ラップされた値を取り出す.<br>
	 * <p>{@code Nothing}の場合は空のリストが返される。
	 * また{@code One}の場合は要素を1つだけもつリストが返される。</p>
	 * @return 取り出された値のリスト
	 */
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
			return String.format("One(%s)", value);
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
