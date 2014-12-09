package com.m12i.jp1ajs2.unitdef.parser;

/**
 * パース中に発生したエラーをあらわす例外オブジェクト.
 */
public class ParseError extends RuntimeException {
	private static final long serialVersionUID = -5628637752743091631L;
	private static final String messageHeader = "Failed to parse: error on line %d at column %d: ";
	private static final String s1NotFoundS2 = "\"%s\" not found.";
	private static final String s1ExpectedButFoundS2 = "'%s' expected but '%s' found.";
	private static final String newLine = System.getProperty("line.separator");

	public static void unexpectedError(final Input in,
			final Throwable cause) {
		throw new ParseError("Unexpected error has occurred.", in, cause);
	}

	public static void syntaxError(final Input in) {
		throw new ParseError("Syntax error has occurred.", in);
	}

	public static void arg1NotFound(final Input in,
			final String arg1) {
		throw new ParseError(String.format(s1NotFoundS2, arg1), in);
	}

	public static void arg1ExpectedButFoundArg2(final Input in,
			final char arg1, final char arg2) {
		throw new ParseError(String.format(s1ExpectedButFoundS2, arg1, arg2), in);
	}

	private final Input in;
	private final String message;
	private final Throwable cause;

	public ParseError(final String message, final Input in, final Throwable cause) {
		super(message, cause);
		this.in = in;
		this.message = message;
		this.cause = cause;
	}

	public ParseError(final String message, final Input in) {
		super(message);
		this.in = in;
		this.message = message;
		this.cause = null;
	}

	@Override
	public String getMessage() {
		return String.format(messageHeader, in.lineNo(), in.columnNo()) + message
				+ (cause == null ? "" : newLine + cause.getMessage());
	}
}
