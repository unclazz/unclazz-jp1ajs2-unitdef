package com.m12i.jp1ajs2.unitdef.util;

import java.util.Iterator;

import com.m12i.jp1ajs2.unitdef.util.Option.None;
import com.m12i.jp1ajs2.unitdef.util.Option.Some;

/**
 * 2種類の異なる型のオブジェクトのうち「いずれか」を表現するオブジェクト.
 * 一般なケースでは、Left（“左側”）には処理途中のエラーや例外事象を表すオブジェクトが設定され、
 * Right（“右側”）には処理が正常に完了した結果得られたオブジェクトが設定されます。
 * Leftに設定されるのは予め予期されているエラーや例外事象であり、Javaでいうところのチェック例外におおよそ該当します。
 * LeftとRightの「いずれも」に値が設定されることはありません。
 * 
 * @param <L> Leftに設定されるオブジェクトの型
 * @param <R> Rightに設定されるオブジェクトの型
 */
public class Either<L, R> implements Iterable<R> {
	/**
	 * 引数で指定されたオブジェクトをLeftに設定して返す.
	 * @param l Leftに設定されるオブジェクト
	 * @return Left
	 */
	public static<L, R> Either<L, R> left(L l) {
		return new Either<L, R>(l, null);
	}
	/**
	 * 引数で指定されたオブジェクトをRightに設定して返す.
	 * @param r Rightに設定されるオブジェクト
	 * @return Right
	 */
	public static<L, R> Either<L, R> right(R r) {
		return new Either<L, R>(null, r);
	}
	/**
	 * 引数で指定されたメッセージを持つ例外をLeftに設定して返す.
	 * @param message メッセージ
	 * @return Left
	 */
	public static<R> Either<Throwable, R> failure(String message) {
		return new Either<Throwable, R>(new RuntimeException(message), null);
	}
	/**
	 * 引数で指定された例外をLeftに設定して返す.
	 * @param error 例外
	 * @return Left
	 */
	public static<R> Either<Throwable, R> failure(Throwable error) {
		return new Either<Throwable, R>(error, null);
	}
	/**
	 * 引数で指定されたオブジェクトをRightに設定して返す.
	 * @param r オブジェクト
	 * @return Right
	 */
	public static<R> Either<Throwable, R> success(R r) {
		return new Either<Throwable, R>(null, r);
	}
	
	private final L l;
	private final R r;
	public Either (L left, R right) {
		this.l = left;
		this.r = right;
	}
	/**
	 * レシーバ・オブジェクトがLeftかどうか判定する.
	 * @return 判定結果
	 */
	public boolean isLeft() {
		return l != null;
	}
	/**
	 * レシーバ・オブジェクトがRightかどうか判定する.
	 * @return 判定結果
	 */
	public boolean isRight() {
		return l == null;
	}
	/**
	 * Leftから値を取り出す.
	 * @return Leftに設定されていた値
	 */
	public L left() {
		if (isLeft()) {
			return l;
		}
		throw new RuntimeException();
	}
	/**
	 * Rightから値を取り出す.
	 * @return Rightに設定されていた値
	 */
	public R right() {
		if (isRight()) {
			return r;
		}
		throw new RuntimeException();
	}
	@Override
	public Iterator<R> iterator() {
		return isLeft() ? new ZeroIterator<R>() : new OneIterator<R>(r);
	}
	/**
	 * レシーバ・オブジェクトから{@link Option}オブジェクトを生成する.
	 * Leftからは{@link None}が、Rightからは{@link Some}がそれぞれ生成されます。
	 * @return {@link None}もしくは{@link Some}
	 */
	public Option<R> toOption() {
		if (isLeft()) {
			return Option.none();
		} else {
			return Option.some(r);
		}
	}
	@Override
	public String toString() {
		return (isLeft() ? "Left(" : "Right(") + left() + ")";
	}
	@Override
	public boolean equals(Object other) {
		if (other == null || other.getClass() != Either.class) {
			return false;
		}
		Either<?, ?> that = (Either<?, ?>) other;
		if (this.isLeft() != that.isRight()) {
			return false;
		}
		return this.isLeft() ? (this.left().equals(that.left())) : (this.right().equals(that.right()));
	}
	@Override
	public int hashCode() {
		return 37 * (this.isLeft() ? this.left() : this.right()).hashCode();
	}
}
