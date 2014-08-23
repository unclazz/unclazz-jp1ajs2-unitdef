package com.m12i.code.parse;

public abstract class Result<T> {
	private static final Success<Void> emptySuccess = new Success<Void>(null);
	private static final Failure<?> failure = new Failure<Object>("Error has occured.");
	
	/**
	 * パースが成功したときに返されるコンテナ.
	 * @param <T> パース結果として期待される型
	 */
	public static final class Success<T> extends Result<T> {
		private Success(final T val) {
			super(val);
		}
		@Override
		public String toString() {
			return String.format("Success(%s)", value);
		}
	}
	/**
	 * パースが失敗したときに返されるコンテナ.
	 * @param <T> パース結果として期待される型
	 */
	public static final class Failure<T> extends Result<T> {
		private Failure(final String message) {
			super(message);
		}
		@Override
		public String toString() {
			return String.format("Failure(%s)", message);
		}
	}
	public static<T> Success<T> success(final T val) {
		return new Success<T>(val);
	}
	public static Success<Void> success() {
		return emptySuccess;
	}
	public static<T> Failure<T> failure(final String message) {
		return new Failure<T>(message);
	}
	@SuppressWarnings("unchecked")
	public static<T> Failure<T> failure() {
		return (Failure<T>) failure;
	}
	
	public final boolean successful;
	public final boolean failed;
	public final T value;
	public final String message;
	
	Result(final T value) {
		this.value = value;
		this.successful = true;
		this.failed = false;
		this.message = "";
	}
	
	Result(final String message) {
		this.value = null;
		this.successful = false;
		this.failed = true;
		this.message = message;
	}
}
