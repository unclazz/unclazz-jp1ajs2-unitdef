package org.doogwood.jp1ajs2.unitdef.parser;

/**
 * パース中に発生したエラーをあらわす例外オブジェクト.
 */
public final class ParseException extends RuntimeException {
	private static final long serialVersionUID = -5628637752743091631L;
	private static final String MESSAGE_HEADER = "Error has occured while parsing.";
	private static final String LINE_A1_COLUMN_A2 = " (line %s, column %s)";
	private static final String A1_NOT_FOUND = "\"%s\" not found.";
	private static final String A1_EXPECTED_BUT_A2_FOUND = "'%s' expected but '%s' found.";
	private static final String NEW_LINE = System.getProperty("line.separator");

	static ParseException unexpectedError(final Input in,
			final Throwable cause) {
		return new ParseException("Unexpected error has occurred.", in, cause);
	}

	static ParseException syntaxError(final Input in) {
		return new ParseException("Syntax error has occurred.", in);
	}

	static ParseException arg1NotFound(final Input in,
			final String arg1) {
		return new ParseException(String.format(A1_NOT_FOUND, arg1), in);
	}

	static ParseException arg1ExpectedButFoundArg2(final Input in,
			final char arg1, final char arg2) {
		return new ParseException(String.format(A1_EXPECTED_BUT_A2_FOUND, arg1, arg2), in);
	}

	private final Input in;
	private final String message;
	private final Throwable cause;

	ParseException(final String message, final Input in, final Throwable cause) {
		super(message, cause);
		this.in = in;
		this.message = message;
		this.cause = cause;
	}

	ParseException(final String message, final Input in) {
		super(message);
		this.in = in;
		this.message = message;
		this.cause = null;
	}

	ParseException(final Throwable cause, final Input in) {
		super(cause);
		this.in = in;
		this.message = null;
		this.cause = cause;
	}

	ParseException(final Throwable cause) {
		super(cause);
		this.in = null;
		this.message = null;
		this.cause = cause;
	}

	@Override
	public String getMessage() {
		return MESSAGE_HEADER + 
				(in == null ? "" : String.format(LINE_A1_COLUMN_A2, in.lineNo(), in.columnNo())) +
				(message == null ? "" : ' ' + message) +
				(cause == null ? "" : NEW_LINE + cause.getMessage());
	}
}
