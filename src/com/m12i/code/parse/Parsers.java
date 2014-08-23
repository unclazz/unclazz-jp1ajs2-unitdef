package com.m12i.code.parse;

public final class Parsers {
	public static final class Options {
		private String lineCommentStart;
		private String blockCommentStart;
		private String blockCommentEnd;
		private char escapePrefixInDoubleQuotes;
		private char escapePrefixInSingleQuotes;
		private char escapePrefixInBackQuotes;
		private boolean skipCommentWithWhitespace;
		public String getLineCommentStart() {
			return lineCommentStart;
		}
		public void setLineCommentStart(String lineCommentStart) {
			this.lineCommentStart = lineCommentStart;
		}
		public String getBlockCommentStart() {
			return blockCommentStart;
		}
		public void setBlockCommentStart(String blockCommentStart) {
			this.blockCommentStart = blockCommentStart;
		}
		public String getBlockCommentEnd() {
			return blockCommentEnd;
		}
		public void setBlockCommentEnd(String blockCommentEnd) {
			this.blockCommentEnd = blockCommentEnd;
		}
		public char getEscapePrefixInDoubleQuotes() {
			return escapePrefixInDoubleQuotes;
		}
		public void setEscapePrefixInDoubleQuotes(char escapePrefixInDoubleQuotes) {
			this.escapePrefixInDoubleQuotes = escapePrefixInDoubleQuotes;
		}
		public char getEscapePrefixInSingleQuotes() {
			return escapePrefixInSingleQuotes;
		}
		public void setEscapePrefixInSingleQuotes(char escapePrefixInSingleQuotes) {
			this.escapePrefixInSingleQuotes = escapePrefixInSingleQuotes;
		}
		public char getEscapePrefixInBackQuotes() {
			return escapePrefixInBackQuotes;
		}
		public void setEscapePrefixInBackQuotes(char escapePrefixInBackQuotes) {
			this.escapePrefixInBackQuotes = escapePrefixInBackQuotes;
		}
		public boolean isSkipCommentWithWhitespace() {
			return skipCommentWithWhitespace;
		}
		public void setSkipCommentWithWhitespace(boolean skipCommentWithWhitespace) {
			this.skipCommentWithWhitespace = skipCommentWithWhitespace;
		}
	}
	
	
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
