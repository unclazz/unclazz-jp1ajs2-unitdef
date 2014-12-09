package com.m12i.jp1ajs2.unitdef;

import java.util.Iterator;

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
public class ParseResult implements Iterable<Unit> {
	/**
	 * 引数で指定されたオブジェクトをRightに設定して返す.
	 * @param r Rightに設定されるオブジェクト
	 * @return Right
	 */
	public static ParseResult success(Unit r) {
		return new ParseResult(null, r);
	}
	/**
	 * 引数で指定されたメッセージを持つ例外をLeftに設定して返す.
	 * @param message メッセージ
	 * @return Left
	 */
	public static<R> ParseResult failure(String message) {
		return new ParseResult(new RuntimeException(message), null);
	}
	/**
	 * 引数で指定された例外をLeftに設定して返す.
	 * @param error 例外
	 * @return Left
	 */
	public static<R> ParseResult failure(Throwable error) {
		return new ParseResult(error, null);
	}
	
	private final Throwable l;
	private final Unit r;
	public ParseResult (Throwable error, Unit unit) {
		this.l = error;
		this.r = unit;
	}
	/**
	 * レシーバ・オブジェクトがLeftかどうか判定する.
	 * @return 判定結果
	 */
	public boolean isFailure() {
		return l != null;
	}
	/**
	 * レシーバ・オブジェクトがRightかどうか判定する.
	 * @return 判定結果
	 */
	public boolean isSuccess() {
		return l == null;
	}
	/**
	 * Leftから値を取り出す.
	 * @return Leftに設定されていた値
	 */
	public Throwable error() {
		if (isFailure()) {
			return l;
		}
		throw new RuntimeException();
	}
	/**
	 * Rightから値を取り出す.
	 * @return Rightに設定されていた値
	 */
	public Unit unit() {
		if (isSuccess()) {
			return r;
		}
		throw new RuntimeException();
	}
	@Override
	public Iterator<Unit> iterator() {
		if (isFailure()) {
			return ZeroIterator.getInstance();
		} else {
			return new OneIterator<Unit>(r);
		}
	}
	@Override
	public String toString() {
		return (isFailure() ? "Failure(" : "Success(") + error() + ")";
	}
	@Override
	public boolean equals(Object other) {
		if (other == null || other.getClass() != ParseResult.class) {
			return false;
		}
		final ParseResult that = (ParseResult) other;
		if (this.isFailure() != that.isSuccess()) {
			return false;
		}
		return this.isFailure() ? (this.error().equals(that.error())) : (this.unit().equals(that.unit()));
	}
	@Override
	public int hashCode() {
		return 37 * (this.isFailure() ? this.error() : this.unit()).hashCode();
	}
}
