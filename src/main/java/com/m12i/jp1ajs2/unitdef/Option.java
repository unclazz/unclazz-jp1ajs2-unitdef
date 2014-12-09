package com.m12i.jp1ajs2.unitdef;

import java.util.Iterator;

/**
 * ある値が「1つある」状態か「1つもない」状態のいずれかを表現するオブジェクト.
 * 「1つある」場合は{@link Some}オブジェクトとなり、「1つもない」場合は{@link None}オブジェクトとなります。
 * 単一値を返す（しかし該当する値が取得できない可能性もある）メソッドの戻り値の型として使用されます。
 * シグネチャ上戻り値としてOption型を持つメソッドは返すべき値がないとき{@code null}を返す代わりに{@link None}を返します。
 * 
 * @param <T> ラップされるオブジェクトの型
 */
public abstract class Option<T> implements Iterable<T>{
	/**
	 * {@link None}オブジェクトに対して{@link None#get()}メソッドを呼び出した時にスローされる例外.
	 */
	public static final class NoneHasNoValueException extends RuntimeException {
		private static final long serialVersionUID = -2964252894387685610L;
	}
	public static final class Some<T> extends Option<T> {
		private final T v;
		public Some(T value) {
			this.v = value;
		}
		@Override
		public Iterator<T> iterator() {
			return new OneIterator<T>(v);
		}
		@Override
		public boolean isSome() {
			return true;
		}
		@Override
		public boolean isNone() {
			return false;
		}
		@Override
		public T get() {
			return v;
		}
		@Override
		public String toString() {
			return "Some(" + v + ")";
		}
		@Override
		public boolean equals(Object other) {
			if (other == null || other.getClass() != Some.class) {
				return false;
			}
			Some<?> that = (Some<?>) other;
			Object thatValue = that.get();
			return v.equals(thatValue);
		}
		@Override
		public int hashCode() {
			return 37 * v.hashCode();
		}
	}
	
	public static final class None<T> extends Option<T> {
		@Override
		public Iterator<T> iterator() {
			return new ZeroIterator<T>();
		}
		@Override
		public boolean isSome() {
			return false;
		}
		@Override
		public boolean isNone() {
			return true;
		}
		@Override
		public T get() {
			throw new NoneHasNoValueException();
		}
		@Override
		public String toString() {
			return "None";
		}
		@Override
		public boolean equals(Object other) {
			return !(other == null || other.getClass() != None.class);
		}
		@Override
		public int hashCode() {
			return -1;
		}
	}
	/**
	 * 引数で与えられたオブジェクトを{@link Some}でラップして返す.
	 * @param value ラップしたいオブジェクト
	 * @return {@link Some}オブジェクト
	 */
	public static<T> Option<T> some(final T value) {
		if (value == null) {
			throw new IllegalArgumentException();
		}
		return new Some<T>(value);
	}
	/**
	 * {@link None}オブジェクトを返す.
	 * @return {@link None}オブジェクト
	 */
	@SuppressWarnings("unchecked")
	public static<T> Option<T> none() {
		return (Option<T>) NONE;
	}
	@SuppressWarnings("rawtypes")
	public static final Option<?> NONE = new None();
	/**
	 * 引数で与えられたオブジェクトが{@code null}でれば{@link None}を返しそうでなければ{@link Some}を返す.
	 * @param value ラップしたいオブジェクトもしくは{@code null}
	 * @return {@link None}もしくは{@link Some}
	 */
	public static<T> Option<T> wrap(final T value) {
		if (value != null) {
			return some(value);
		} else {
			return none();
		}
	}
	/**
	 * レシーバ・オブジェクトが{@link Some}のインスタンスか判定する.
	 * @return 判定結果
	 */
	public abstract boolean isSome();
	/**
	 * レシーバ・オブジェクトが{@link None}のインスタンスか判定する.
	 * @return 判定結果
	 */
	public abstract boolean isNone();
	/**
	 * ラップされた値を取り出す.
	 * レシーバ・オブジェクトが{@link None}のインスタンスであった場合は{@link NoneHasNoValueException}がスローされます。
	 * @return 取り出された値
	 */
	public abstract T get();
	/**
	 * ラップされた値を取り出す.
	 * レシーバ・オブジェクトが{@link None}のインスタンスであった場合は引数で指定された代替の値を返します。
	 * @param alt 代替となる値
	 * @return 取り出された値もしくは代替の値
	 */
	public T getOrElse(T alt) {
		return isSome() ? get() : alt;
	}
}
