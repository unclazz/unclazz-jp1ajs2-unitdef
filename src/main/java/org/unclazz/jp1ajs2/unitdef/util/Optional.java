package org.unclazz.jp1ajs2.unitdef.util;

import java.util.Collection;
import java.util.NoSuchElementException;

/**
 * ある値が存在するか・しないかの2状態を表わすためのオブジェクト.
 * @param <T> 期待される値の型
 */
public final class Optional<T> {
	/**
	 * 値がない状態を表わすための{@link Optional}インスタンスを返す.
	 * @return インスタンス
	 */
	@SuppressWarnings("unchecked")
	public static<T> Optional<T> empty() {
		return (Optional<T>) EMPTY;
	}
	
	/**
	 * 引数で指定された値を内包する新しい{@link Optional}インスタンスを返す.
	 * 指定された値が{@code null}である場合、{@link NullPointerException}をスローする。
	 * @param value ラップする値
	 * @return インスタンス
	 */
	public static<T> Optional<T> of(final T value) {
		if (value == null) {
			throw new NullPointerException();
		} else {
			return new Optional<T>(value);
		}
	}
	
	/**
	 * 引数で指定された値を内包する新しい{@link Optional}インスタンスを返す.
	 * 指定された値が{@code null}である場合、値が存在しないことを表わすインスタンスを返す。
	 * @param value ラップする値
	 * @return インスタンス
	 */
	public static<T> Optional<T> ofNullable(final T value) {
		if (value == null) {
			return empty();
		} else {
			return new Optional<T>(value);
		}
	}
	
	/**
	 * 引数で指定されたコレクションの最初の要素を内包する新しい{@link Optional}インスタンスを返す.
	 * 指定されたコレクション自体が{@code null}である場合、あるいはコレクションの要素数が{@code 0}である場合、
	 * 値が存在しないことを表わすインスタンスを返す。
	 * @param values コレクション
	 * @return インスタンス
	 */
	public static<T> Optional<T> ofFirst(final Collection<T> values) {
		if (values == null || values.isEmpty()) {
			return empty();
		} else {
			return ofNullable(values.iterator().next());
		}
	}
	
	/**
	 * 引数で指定された配列の最初の要素を内包する新しい{@link Optional}インスタンスを返す.
	 * 指定された配列自体が{@code null}である場合、あるいは配列の要素数が{@code 0}である場合、
	 * 値が存在しないことを表わすインスタンスを返す。
	 * @param values 配列
	 * @return インスタンス
	 */
	public static<T> Optional<T> ofFirst(final T[] values) {
		if (values == null || values.length == 0) {
			return empty();
		} else {
			return ofNullable(values[0]);
		}
	}
	
	/**
	 * 値がない状態を表わすインスタンス.
	 */
	private static final Optional<?> EMPTY = new Optional<Object>(null);
	
	/**
	 * 内包する値.
	 */
	private final T value;
	
	/**
	 * コンストラクタ.
	 * @param value 内包する値
	 */
	private Optional(final T value) {
		this.value = value;
	}
	
	/**
	 * 内包する値を返す.
	 * 値がない状態を表わすインスタンスの場合、{@link NoSuchElementException}をスローする。
	 * @return 内包する値
	 */
	public T get() {
		if (value == null) {
			throw new NoSuchElementException();
		} else {
			return value;
		}
	}
	
	/**
	 * 値がある状態を示すインスタンスの場合は内包する値を返し、そうでない場合は引数で指定された値を返す.
	 * @param other 値がない状態を表わすインスタンスである場合に返す値
	 * @return 内包する値、もしくは引数で指定された値
	 */
	public T orElse(final T other) {
		return value == null ? other : value;
	}
	
	/**
	 * 値がある状態を示すインスタンスの場合は引数で指定された値を内包する新しいインスタンスを返す.
	 * そうでない場合は値がない状態を表わすインスタンスを返す。
	 * @param value 値がある状態を表わすインスタンスである場合にラップされる値
	 * @return 引数で指定された値を内包するインスタンス、もしくは値がない状態を表わすインスタンス
	 */
	public<U> Optional<U> ifPresent(final U value) {
		if (value == null) {
			return Optional.empty();
		} else {
			return Optional.ofNullable(value);
		}
	}
	
	/**
	 * 値がある状態であれば{@code true}を返す.
	 * @return {@code true}: 値がある状態、{@code false}: 値がない状態
	 */
	public boolean isPresent() {
		return value != null;
	}

	/**
	 * 値がない状態であれば{@code true}を返す.
	 * @return {@code true}: 値がない状態、{@code false}: 値がある状態
	 */
	public boolean isNotPresent() {
		return value == null;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((value == null) ? 0 : value.hashCode());
		return result;
	}

	@Override
	public boolean equals(final Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		final Optional<?> other = (Optional<?>) obj;
		if (value == null) {
			if (other.value != null)
				return false;
		} else if (!value.equals(other.value))
			return false;
		return true;
	}
	
	@Override
	public String toString() {
		if (value == null) {
			return "Optional()";
		} else {
			return String.format("Optional(%s)", value);
		}
	}
}
