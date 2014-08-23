package com.m12i.code.parse;

public class ParseException extends Exception {

	private static final long serialVersionUID = -5628637752743091631L;
	private static final String messageHeader = "Failed to parse: error on line %d at column %d: ";
	private static final String s1ExpectedButFoundS2 = "'%s' expected but '%s' found.";
	private static final String newLine = System.getProperty("line.separator");
	
	public static ParseException unexpectedError(final Reader p, final Throwable cause) {
		return new ParseException("Unexpected error has occurred.", p, cause);
	}
	
	public static ParseException syntaxError(final Reader p) {
		return new ParseException("Syntax error has occurred.", p);
	}
	
	public static ParseException arg1ExpectedButFoundArg2(final Reader p, final char arg1, final char arg2) {
		return new ParseException(String.format(s1ExpectedButFoundS2, arg1, arg2), p);
	}
	
	private final Reader p;
	private final String message;
	private final Throwable cause;
	
	public ParseException(final String message, final Reader p, final Throwable cause) {
		this.p = p;
		this.message = message;
		this.cause = cause;
	}

	public ParseException(final String message, final Reader p) {
		this.p = p;
		this.message = message;
		this.cause = null;
	}
	
	@Override
	public String getMessage() {
		return String.format(messageHeader, p.lineNo(), p.columnNo()) + message + (cause == null ? "" : newLine + cause.getMessage());
	}
}
