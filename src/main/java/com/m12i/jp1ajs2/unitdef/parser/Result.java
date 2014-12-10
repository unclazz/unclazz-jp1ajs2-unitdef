package com.m12i.jp1ajs2.unitdef.parser;

/**
 * パース結果をあらわすコンテナ・オブジェクト.
 * パースに成功した場合は{@link Success}、失敗した場合は{@link Failure}が使用されます。
 *
 * @param <T> パース結果として{@code Result}に格納されるオブジェクトの型
 */
abstract class Result<T> {
	private static final Success<Void> emptySuccess = new Success<Void>(null);
	private static final Success<String> emptyStringSuccess = new Success<String>("");
	private static final Failure<?> failure = new Failure<Object>("Failed to parse.");
	
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
	/**
	 * {@link Success}インスタンスを返す.
	 * @param val パースの結果得られた値
	 * @return パースの結果得られた値を格納した{@link Success}インスタンス
	 */
	public static<T1, T2 extends T1> Success<T1> success(final T2 val) {
		return new Success<T1>(val);
	}
	/**
	 * {@link Success}インスタンスを返す.
	 * 引数として{@code null}や{@code ""}（空文字列）が指定された場合はあらかじめ生成された専用インスタンスが返されます。
	 * @param val パースの結果得られた値
	 * @return パースの結果得られた値を格納した{@link Success}インスタンス
	 */
	public static Success<String> success(final String val) {
		if (val == null || val.length() == 0) {
			return emptyStringSuccess;
		} else {
			return new Success<String>(val);
		}
	}
	/**
	 * {@code Success<Void>}インスタンスを返す.
	 * {@code Success<Void>}はパース自体は成功しているが、パースの結果得られる値には用途がない場合に使用されます。
	 * @return {@code Success<Void>}インスタンス
	 */
	public static Success<Void> success() {
		// あらかじめ生成済みのSuccess<Void>インスタンスを返す
		return emptySuccess;
	}
	/**
	 * 引数で指定された{@link Failure}インスタンスを元に新しい{@link Failure}インスタンスを生成する.
	 * @param cause 元になる{@link Failure}インスタンス
	 * @return 新しい{@link Failure}インスタンス
	 */
	@SuppressWarnings("unchecked")
	public static<T> Failure<T> failure(final Result<?> cause) {
		// 元になるFailureインスタンスをキャストして返す
		// ＊Javaのジェネリックスの性質上Failureに関しては使い回しが可能
		return (Failure<T>) cause;
	}
	/**
	 * 引数で指定されたメッセージを使用して新しい{@link Failure}インスタンスを生成する.
	 * @param message メッセージ
	 * @return 新しい{@link Failure}インスタンス
	 */
	public static<T> Failure<T> failure(final String message) {
		return new Failure<T>(message);
	}
	public static<T> Failure<T> failure(final Input in, final String message) {
		return new Failure<T>(format(in) + message);
	}
	private static String format(final Input in) {
		return String.format("Failed to parse: error on line %s at column %s: ", in.lineNo(), in.columnNo());
	}
	/**
	 * 引数で指定された情報を元に新しい{@link Failure}インスタンスを生成する.
	 * @param expected 期待された文字
	 * @param actual 実際に検出された文字
	 * @return 新しい{@link Failure}インスタンス
	 */
	public static<T> Failure<T> failure(final char expected, final char actual) {
		return new Failure<T>(String.format("'%s' expected but '%s' found.", expected, actual));
	}
	public static<T> Failure<T> failure(final Input in, final char expected, final char actual) {
		return new Failure<T>(format(in) + String.format("'%s' expected but '%s' found.", expected, actual));
	}
	/**
	 * 新しい{@link Failure}インスタンスを生成する.
	 * @return 新しい{@link Failure}インスタンス
	 */
	@SuppressWarnings("unchecked")
	public static<T> Failure<T> failure() {
		// あらかじめ生成済みのFailureインスタンスをキャストして返す
		// ＊Javaのジェネリックスの性質上Failureに関しては使い回しが可能
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
	 * @param in パース処理対象の{@link Input}オブジェクト
	 * @param cause 処理中に発生した例外
	 * @return {@link ParseError}オブジェクト
	 */
	public final ParseError toError(final Input in, final Throwable cause) {
		if (!successful) {
			return new ParseError(cause.getMessage(), in, cause);
		} else {
			return null;
		}
	}
	/**
	 * {@code Result}のサブクラスから{@link ParseError}を生成する.
	 * {@link Success}に対してこのメソッドを呼び出した場合は{@code null}が返されます。
	 * @param in パース処理対象の{@link Input}オブジェクト
	 * @param message 例外メッセージとして使用される文字列
	 * @return {@link ParseError}オブジェクト
	 */
	public final ParseError toError(final Input in, final String message) {
		if (!successful) {
			return new ParseError(message, in);
		} else {
			return null;
		}
	}
	/**
	 * {@code Result}のサブクラスから{@link ParseError}を生成する.
	 * {@link Success}に対してこのメソッドを呼び出した場合は{@code null}が返されます。
	 * @param in パース処理対象の{@link Input}オブジェクト
	 * @return {@link ParseError}オブジェクト
	 */
	public final ParseError toError(final Input in) {
		if (!successful) {
			return new ParseError(message, in);
		} else {
			return null;
		}
	}
	/**
	 * {@code Result}のサブクラスから{@link ParseError}を生成してスローする.
	 * {@link Success}に対してこのメソッドを呼び出した場合、例外スローは発生しません。
	 * @param in パース処理対象の{@link Input}オブジェクト
	 * @param message 例外メッセージとして使用される文字列
	 * @throws ParseError 生成された例外オブジェクト
	 */
	public final void throwsError(final Input in, final String message) throws ParseError {
		if (!successful) throw toError(in, message);
	}
	/**
	 * {@code Result}のサブクラスから{@link ParseError}を生成してスローする.
	 * {@link Success}に対してこのメソッドを呼び出した場合、例外スローは発生しません。
	 * @param in パース処理対象の{@link Input}オブジェクト
	 * @throws ParseError 生成された例外オブジェクト
	 */
	public final void throwsError(final Input in) throws ParseError {
		if (!successful) throw toError(in);
	}
	/**
	 * {@code Result}のサブクラスから{@link ParseError}を生成してスローする.
	 * {@link Success}に対してこのメソッドを呼び出した場合、例外スローは発生しません。
	 * @param in パース処理対象の{@link Input}オブジェクト
	 * @param cause 処理中に発生した例外
	 * @throws ParseError 生成された例外オブジェクト
	 */
	public final void throwsError(final Input in, final Throwable cause) throws ParseError {
		if (!successful) throw toError(in, cause);
	}
}
