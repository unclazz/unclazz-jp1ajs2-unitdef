package org.unclazz.jp1ajs2.unitdef.parser;

/**
 * 入力データ読み取り中に発生したエラーをあらわす例外オブジェクト.
 */
public final class InputExeption extends Exception {
	private static final long serialVersionUID = 5679215065575767929L;
	private final Input in;
	InputExeption(final Input in) {
		this.in = in;
	}
	InputExeption(final Input in, final Throwable th) {
		super(th);
		this.in = in;
	}
	InputExeption(final Throwable th) {
		super(th);
		this.in = null;
	}
	@Override
	public String getMessage() {
		return String.format("Error has occured while reading input string or stream."
				+ (in == null ? "" : " (line: %s, column: %s)"), in.lineNumber(), in.columnNumber());
	}
}
