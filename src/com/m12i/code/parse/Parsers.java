package com.m12i.code.parse;

public final class Parsers {
	
	private final StringBuilder buff = new StringBuilder();
	
	public Result<Void> skipWhitespace(final Reader in) {
		// TODO
		return null;
	}
	
	public Result<Void> skipLineComment(final Reader in, final String start) {
		// TODO
		return null;
	}
	
	public Result<Void> skipBlockComment(final Reader in, final String start, final String end) {
		// TODO
		return null;
	}
	
	public Result<String> parseRawString(final Reader in) {
		// TODO
		return null;
	}
	
	public Result<Long> parseLong(final Reader in) {
		// TODO
		return null;
	}
	
	public Result<Integer> parseInteger(final Reader in) {
		// TODO
		return null;
	}
	
	public Result<Double> parseDouble(final Reader in) {
		// TODO
		return null;
	}
	
	public Result<String> parseUntil(final Reader in, final char...cs) {
		// TODO
		return null;
	}
	
	public Result<String> parseAbc(final Reader in) {
		// TODO
		return null;
	}
	
	public Result<String> parse123(final Reader in) {
		// TODO
		return null;
	}
	
	public Result<String> parseAbc123(final Reader in) {
		// TODO
		return null;
	}
	
	public Result<String> parseAbc123_$(final Reader in) {
		// TODO
		return null;
	}
	
	public Result<String> parseQuotedString(final Reader in) {
		// TODO
		return null;
	}
	
	public Result<String> parseQuotedString(final Reader in, final char quote) {
		// TODO
		return null;
	}
	
}
