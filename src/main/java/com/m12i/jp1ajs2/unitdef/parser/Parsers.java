package com.m12i.jp1ajs2.unitdef.parser;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public final class Parsers {
	public static final class Options {
		private String lineCommentStart = "//";
		private String blockCommentStart = "/*";
		private String blockCommentEnd = "*/";
		private char escapePrefixInDoubleQuotes = '\\';
		private char escapePrefixInSingleQuotes = '\\';
		private char escapePrefixInBackQuotes = '\\';
		private boolean skipCommentWithWhitespace = true;
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
	
	private static final Pattern numberPattern = Pattern.compile("^[+|\\-]?(\\d*\\.\\d+|\\d+\\.?)((e|E)[+|\\-]?\\d+)?");
	private static final Result<Void> skipWhitespaceFailure = Result.failure("Whitespace sequence not found.");
	private static final Result<Void> skipCommentFailure = Result.failure("Comment sequence not found.");
	

	private static void next(final Input in, final int times) {
		for (int i = 0; i < times; i++) {
			in.next();
		}
	}
	
	private final StringBuilder buff = new StringBuilder();
	private final String lineCommentStart;
	private final String blockCommentStart;
	private final String blockCommentEnd;
	private final char escapePrefixInDoubleQuotes;
	private final char escapePrefixInSingleQuotes;
	private final char escapePrefixInBackQuotes;
	private final boolean skipCommentWithWhitespace;
	
	public Parsers() {
		this(new Options());
	}
	
	public Parsers(final Options options) {
		lineCommentStart = options.lineCommentStart;
		blockCommentStart = options.blockCommentStart;
		blockCommentEnd = options.blockCommentEnd;
		escapePrefixInDoubleQuotes = options.escapePrefixInDoubleQuotes;
		escapePrefixInSingleQuotes = options.escapePrefixInSingleQuotes;
		escapePrefixInBackQuotes = options.escapePrefixInBackQuotes;
		skipCommentWithWhitespace = options.skipCommentWithWhitespace;
	}
	
	public Result<Void> skipWhitespace(final Input in) {
		if (skipCommentWithWhitespace) {
			while (!in.hasReachedEof()) {
				if (in.current() <= ' ') {
					in.next();
				} else {
					if (skipComment(in).successful) {
						continue;
					} else {
						return Result.success();
					}
				}
			}
		} else {
			while (!in.hasReachedEof()) {
				if (in.current() <= ' ') {
					in.next();
				} else {
					return Result.success();
				}
			}
		}
		return skipWhitespaceFailure;
	}
	
	public Result<Void> skipWhitespaceWith(final Input in, final char ch) {
		while (!in.hasReachedEof()) {
			final char c0 = in.current();
			if (c0 <= ' ' || c0 == ch) {
				in.next();
			} else {
				return Result.success();
			}
		}
		return skipWhitespaceFailure;
	}
	
	public Result<Void> skipWhitespaceWith(final Input in, final char... cs) {
		outer:
		while (!in.hasReachedEof()) {
			final char c0 = in.current();
			if (c0 <= ' ') {
				in.next();
			} else {
				for (final char c1 : cs) {
					if (c0 == c1) {
						in.next();
						continue outer;
					}
				}
				return Result.success();
			}
		}
		return skipWhitespaceFailure;
	}
	
	public Result<Void> skipComment(final Input in) {
		if (skipWord(in, lineCommentStart).successful) {
			while (!in.hasReachedEof()) {
				final char c0 = in.current();
				if (c0 == '\r') {
					final char c1 = in.next();
					if (c1 == '\n') {
						in.next();
					}
					return Result.success();
				}
				in.next();
				if (in.hasReachedEof()) {
					return Result.success();
				}
			}
		} else if (skipWord(in, blockCommentStart).successful) {
			while (!in.hasReachedEof()) {
				if (skipWord(in, blockCommentEnd).successful) {
					return Result.success();
				}
				in.next();
			}
		}
		return skipCommentFailure;
	}
	
	public Result<Void> skipWord(final Input in, final String word) {
		final String l = in.rest();
		if (l.startsWith(word)) {
			for (int i = 0; i < word.length(); i ++) {
				in.next();
			}
			return Result.success();
		} else {
			return Result.failure(String.format("\"%s\" not found.", word));
		}
	}
	
	public Result<String> parseRawString(final Input in) {
		buff.setLength(0);
		while (!in.hasReachedEof()) {
			final char c1 = in.current();
			if (c1 <= ' ') {
				break;
			}
			buff.append(c1);
			in.next();
		}
		return Result.success(buff.toString());
	}
	
	public Result<Double> parseNumber(final Input in) {
		final String rest = in.rest();
		final Matcher m = numberPattern.matcher(rest);
		if (m.lookingAt()) {
			final String n = m.group();
			next(in, n.length());
			return Result.success(Double.parseDouble(n));
		} else {
			return Result.failure(String.format("Number literal expected but \"%s...\" found",
					rest.length() > 5 ? rest.substring(0, 5) : rest));
		}
	}
	
	public Result<String> parseUntil(final Input in, final char...cs) {
		buff.setLength(0);
		while (!in.hasReachedEof()) {
			final char current = in.current();
			for (final char c : cs) {
				if (c == current) {
					return Result.success(buff.toString());
				}
			}
			buff.append(current);
			in.next();
		}
		return Result.success(buff.toString());
	}
	
	public Result<String> parseAbc(final Input in) {
		buff.setLength(0);
		while (!in.hasReachedEof()) {
			final char c = in.current();
			if (('A' <= c && c <= 'Z') || ('a' <= c && c <= 'z')) {
				buff.append(c);
				in.next();
			} else {
				break;
			}
		}
		return Result.success(buff.toString());
	}
	
	public Result<String> parseAbc123(final Input in) {
		buff.setLength(0);
		while (!in.hasReachedEof()) {
			final char c = in.current();
			if (('A' <= c && c <= 'Z') || ('a' <= c && c <= 'z') || ('0' <= c && c <= '9')) {
				buff.append(c);
				in.next();
			} else {
				break;
			}
		}
		return Result.success(buff.toString());
	}
	
	public Result<String> parseAbc123_$(final Input in) {
		buff.setLength(0);
		while (!in.hasReachedEof()) {
			final char c = in.current();
			if (('A' <= c && c <= 'Z') || ('a' <= c && c <= 'z') || ('0' <= c && c <= '9') 
					|| c == '_' || c == '$') {
				buff.append(c);
				in.next();
			} else {
				break;
			}
		}
		return Result.success(buff.toString());
	}
	
	public Result<String> parseQuotedString(final Input in) {
		final char c0 = in.current();
		if (c0 != '"' && c0 != '\'' && c0 != '`') {
			return Result.failure();
		}
		
		final char escape = c0 == '"' ? escapePrefixInDoubleQuotes
				: c0 == '\'' ? escapePrefixInSingleQuotes : escapePrefixInBackQuotes;
		buff.setLength(0);

		if (c0 == escape) {
			while (!in.hasReachedEof()) {
				final char c1 = in.next();
				if (c1 == c0) {
					final char c2 = in.next();
					if (c1 == c2) {
						buff.append(c2);
						continue;
					} else {
						return Result.success(buff.toString());
					}
				}
				buff.append(c1);
			}
		} else {
			while (!in.hasReachedEof()) {
				final char c1 = in.next();
				if (c1 == c0) {
					in.next();
					return Result.success(buff.toString());
				}
				buff.append(c1 != escape ? c1 : in.next());
			}
		}
		return Result.failure("Unclosed quoted string.");
	}
	
	public void check(final Input in, final char expected) {
		final char actual = in.current();
		if (actual != expected) {
			ParseError.arg1ExpectedButFoundArg2(in, expected, actual);
		}
	}
	
	public void checkNext(final Input in, final char expected) {
		final char actual = in.next();
		if (actual != expected) {
			ParseError.arg1ExpectedButFoundArg2(in, expected, actual);
		}
	}
	
	public void checkWord(final Input in, final String expected) {
		if (skipWord(in, expected).failed) {
			ParseError.arg1NotFound(in, expected);
		}
	}
}
