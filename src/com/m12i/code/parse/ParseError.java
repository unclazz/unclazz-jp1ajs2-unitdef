package com.m12i.code.parse;

public class ParseError extends RuntimeException {
	private static final long serialVersionUID = -5628637752743091631L;
	private static final String messageHeader = "Failed to parse: error on line %d at column %d: ";
	private static final String s1ExpectedButFoundS2 = "'%s' expected but '%s' found.";
	private static final String newLine = System.getProperty("line.separator");

	public static void unexpectedError(final Reader p,
			final Throwable cause) {
		throw new ParseError("Unexpected error has occurred.", p, cause);
	}

	public static void syntaxError(final Reader p) {
		throw new ParseError("Syntax error has occurred.", p);
	}

	public static void arg1ExpectedButFoundArg2(final Reader p,
			final char arg1, final char arg2) {
		throw new ParseError(String.format(s1ExpectedButFoundS2, arg1,
				arg2), p);
	}

	private final Reader p;
	private final String message;
	private final Throwable cause;

	public ParseError(final String message, final Reader p,
			final Throwable cause) {
		super(message, cause);
		this.p = p;
		this.message = message;
		this.cause = cause;
	}

	public ParseError(final String message, final Reader p) {
		super(message);
		this.p = p;
		this.message = message;
		this.cause = null;
	}

	@Override
	public String getMessage() {
		return String.format(messageHeader, p.lineNo(), p.columnNo()) + message
				+ (cause == null ? "" : newLine + cause.getMessage());
	}
}
