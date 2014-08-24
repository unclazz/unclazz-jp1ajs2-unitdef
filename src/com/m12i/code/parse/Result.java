package com.m12i.code.parse;

/**
 * パース結果をあらわすコンテナ・オブジェクト.
 * パースに成功した場合は{@link Success}、失敗した場合は{@link Failure}が使用されます。
 *
 * @param <T> パース結果として{@code Result}に格納されるオブジェクトの型
 */
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
	public static<T> Failure<T> failure(final Result<?> cause) {
		return new Failure<T>(cause.message);
	}
	public static<T> Failure<T> failure(final String message) {
		return new Failure<T>(message);
	}
	public static<T> Failure<T> failure(final char expected, final char actual) {
		return new Failure<T>(String.format("'%s' expected but '%s' found.", expected, actual));
	}
	@SuppressWarnings("unchecked")
	public static<T> Failure<T> failure() {
		return (Failure<T>) failure;
	}
	/**
	 * {@link Success}の場合は{@code true}、{@link Failure}の場合は{@code false}で初期化される.
	 */
	public final boolean successful;
	/**
	 * {@link Success}の場合は{@code false}、{@link Failure}の場合は{@code true}で初期化される.
	 */
	public final boolean failed;
	/**
	 * {@link Success}の場合はパース結果として得られたオブジェクト、{@link Failure}の場合は{@code null}で初期化される.
	 */
	public final T value;
	/**
	 * {@link Success}の場合は{@code ""}（空文字列）、{@link Failure}の場合はパース失敗を示すメッセージで初期化される.
	 */
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
	/**
	 * {@code Result}のサブクラスから{@link ParseError}を生成する.
	 * {@link Success}に対してこのメソッドを呼び出した場合は{@code null}が返されます。
	 * @param in パース処理対象の{@link Reader}オブジェクト
	 * @param cause 処理中に発生した例外
	 * @return {@link ParseError}オブジェクト
	 */
	public final ParseError toError(final Reader in, final Throwable cause) {
		if (!successful) {
			return new ParseError(cause.getMessage(), in, cause);
		} else {
			return null;
		}
	}
	/**
	 * {@code Result}のサブクラスから{@link ParseError}を生成する.
	 * {@link Success}に対してこのメソッドを呼び出した場合は{@code null}が返されます。
	 * @param in パース処理対象の{@link Reader}オブジェクト
	 * @param message 例外メッセージとして使用される文字列
	 * @return {@link ParseError}オブジェクト
	 */
	public final ParseError toError(final Reader in, final String message) {
		if (!successful) {
			return new ParseError(message, in);
		} else {
			return null;
		}
	}
	/**
	 * {@code Result}のサブクラスから{@link ParseError}を生成する.
	 * {@link Success}に対してこのメソッドを呼び出した場合は{@code null}が返されます。
	 * @param in パース処理対象の{@link Reader}オブジェクト
	 * @return {@link ParseError}オブジェクト
	 */
	public final ParseError toError(final Reader in) {
		if (!successful) {
			return new ParseError(message, in);
		} else {
			return null;
		}
	}
	/**
	 * {@code Result}のサブクラスから{@link ParseError}を生成してスローする.
	 * {@link Success}に対してこのメソッドを呼び出した場合、例外スローは発生しません。
	 * @param in パース処理対象の{@link Reader}オブジェクト
	 * @param message 例外メッセージとして使用される文字列
	 * @throws ParseError 生成された例外オブジェクト
	 */
	public final void throwsError(final Reader in, final String message) throws ParseError {
		if (!successful) throw toError(in, message);
	}
	/**
	 * {@code Result}のサブクラスから{@link ParseError}を生成してスローする.
	 * {@link Success}に対してこのメソッドを呼び出した場合、例外スローは発生しません。
	 * @param in パース処理対象の{@link Reader}オブジェクト
	 * @throws ParseError 生成された例外オブジェクト
	 */
	public final void throwsError(final Reader in) throws ParseError {
		if (!successful) throw toError(in);
	}
	/**
	 * {@code Result}のサブクラスから{@link ParseError}を生成してスローする.
	 * {@link Success}に対してこのメソッドを呼び出した場合、例外スローは発生しません。
	 * @param in パース処理対象の{@link Reader}オブジェクト
	 * @param cause 処理中に発生した例外
	 * @throws ParseError 生成された例外オブジェクト
	 */
	public final void throwsError(final Reader in, final Throwable cause) throws ParseError {
		if (!successful) throw toError(in, cause);
	}
}
