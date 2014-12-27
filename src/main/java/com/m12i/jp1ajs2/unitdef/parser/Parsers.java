package com.m12i.jp1ajs2.unitdef.parser;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

final class Parsers {
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
	
	/**
	 * 空白文字をスキップする.
	 * スキップされた文字がない場合でもエラーとはしない。
	 * {@link Options#skipCommentWithWhitespace}が{@code true}の場合コメントもスキップする。
	 * @param in 入力データ
	 */
	public void skipWhitespace(final Input in) {
		if (skipCommentWithWhitespace) {
			this.skipComment(in);
			while (!in.hasReachedEof()) {
				if (in.current() <= ' ') {
					in.next();
				} else {
					final String rest = in.rest();
					if (rest.startsWith(lineCommentStart)) {
						next(in, lineCommentStart.length());
						while (!in.hasReachedEof()) {
							final char c0 = in.current();
							if (c0 == '\r') {
								final char c1 = in.next();
								if (c1 == '\n') {
									in.next();
								}
								break;
							}
							in.next();
							if (in.hasReachedEof()) {
								break;
							}
						}
					} else if (rest.startsWith(blockCommentStart)) {
						next(in, blockCommentStart.length());
						while (!in.hasReachedEof()) {
							if (in.startsWith(blockCommentEnd)) {
								next(in, blockCommentEnd.length());
								break;
							}
							in.next();
						}
					} else {
						break;
					}
				}
			}
		} else {
			while (!in.hasReachedEof()) {
				if (in.current() <= ' ') {
					in.next();
				} else {
					break;
				}
			}
		}
		return;
	}
	
	/**
	 * 引数で指定された文字とともに空白文字をスキップする.
	 * スキップされた文字がない場合（現在読み取り位置が引数で指定された文字や空白文字でない場合）でもエラーとはしない。
	 * @param in 入力データ
	 * @param ch スキップ対象文字
	 */
	public void skipWhitespaceWith(final Input in, final char ch) {
		while (!in.hasReachedEof()) {
			final char current = in.current();
			if (current <= ' ' || current == ch) {
				in.next();
			} else {
				break;
			}
		}
		return;
	}
	
	/**
	 * 引数で指定された文字とともに空白文字をスキップする.
	 * スキップされた文字がない場合（現在読み取り位置が引数で指定された文字や空白文字でない場合）でもエラーとはしない。
	 * @param in 入力データ
	 * @param ch0 スキップ対象文字
	 * @param ch1 スキップ対象文字
	 */
	public void skipWhitespaceWith(final Input in, final char ch0, final char ch1) {
		while (!in.hasReachedEof()) {
			final char current = in.current();
			if (current <= ' ' || current == ch0 || current == ch1) {
				in.next();
			} else {
				break;
			}
		}
		return;
	}
	
	/**
	 * 引数で指定された文字とともに空白文字をスキップする.
	 * スキップされた文字がない場合（現在読み取り位置が引数で指定された文字や空白文字でない場合）でもエラーとはしない。
	 * @param in 入力データ
	 * @param ch0 スキップ対象文字
	 * @param ch1 スキップ対象文字
	 * @param ch2 スキップ対象文字
	 */
	public void skipWhitespaceWith(final Input in, final char ch0, final char ch1, final char ch2) {
		while (!in.hasReachedEof()) {
			final char current = in.current();
			if (current <= ' ' || current == ch0 || current == ch1 || current == ch2) {
				in.next();
			} else {
				break;
			}
		}
		return;
	}
	
	/**
	 * コメントをスキップする.
	 * スキップされた文字がない場合でもエラーとはしない。
	 * @param in 入力データ
	 */
	public void skipComment(final Input in) {
		final String rest = in.rest();
		if (rest.startsWith(lineCommentStart)) {
			next(in, lineCommentStart.length());
			while (!in.hasReachedEof()) {
				final char c0 = in.current();
				if (c0 == '\r') {
					final char c1 = in.next();
					if (c1 == '\n') {
						in.next();
					}
					break;
				}
				in.next();
				if (in.hasReachedEof()) {
					break;
				}
			}
		} else if (rest.startsWith(blockCommentStart)) {
			next(in, blockCommentStart.length());
			while (!in.hasReachedEof()) {
				if (in.startsWith(blockCommentEnd)) {
					next(in, blockCommentEnd.length());
					break;
				}
				in.next();
			}
		}
		return;
	}
	
	/**
	 * 引数で指定された文字列をスキップする.
	 * 読み取り位置に指定された文字列が見つからなかった場合エラーとする。
	 * @param in 入力データ
	 * @param word スキップする文字列
	 */
	public void skipWord(final Input in, final String word) {
		if (in.startsWith(word)) {
			for (int i = 0; i < word.length(); i ++) {
				in.next();
			}
			return;
		} else {
			throw new ParseError(String.format("\"%s\" not found.", word), in);
		}
	}
	
	/**
	 * 空白文字以外から構成される文字列を読み取って返す.
	 * @param in 入力データ
	 * @return 読み取り結果
	 */
	public String parseRawString(final Input in) {
		buff.setLength(0);
		while (!in.hasReachedEof()) {
			final char c1 = in.current();
			if (c1 <= ' ') {
				break;
			}
			buff.append(c1);
			in.next();
		}
		return buff.toString();
	}
	
	public Double parseNumber(final Input in) {
		final String rest = in.rest();
		final Matcher m = numberPattern.matcher(rest);
		if (m.lookingAt()) {
			final String n = m.group();
			next(in, n.length());
			return Double.parseDouble(n);
		} else {
			throw new ParseError(String.format("Number literal expected but \"%s...\" found",
					rest.length() > 5 ? rest.substring(0, 5) : rest), in);
		}
	}
	
	/**
	 * 引数で指定された文字が現れる前の文字列を読み取ってを返す.
	 * @param in 入力データ
	 * @param c0 読み取りを終える文字
	 * @return 読み取り結果
	 */
	public String parseUntil(final Input in, final char c0) {
		buff.setLength(0);
		while (!in.hasReachedEof()) {
			final char current = in.current();
			if (c0 == current) {
				return buff.toString();
			}
			buff.append(current);
			in.next();
		}
		return buff.toString();
	}
	
	/**
	 * 引数で指定された文字が現れる前の文字列を読み取ってを返す.
	 * @param in 入力データ
	 * @param c0 読み取りを終える文字
	 * @param c1 読み取りを終える文字
	 * @return 読み取り結果
	 */
	public String parseUntil(final Input in, final char c0, final char c1) {
		buff.setLength(0);
		while (!in.hasReachedEof()) {
			final char current = in.current();
			if (c0 == current || c1 == current) {
				return buff.toString();
			}
			buff.append(current);
			in.next();
		}
		return buff.toString();
	}
	
	/**
	 * 引数で指定された文字が現れる前の文字列を読み取ってを返す.
	 * @param in 入力データ
	 * @param c0 読み取りを終える文字
	 * @param c1 読み取りを終える文字
	 * @param c2 読み取りを終える文字
	 * @return 読み取り結果
	 */
	public String parseUntil(final Input in, final char c0, final char c1, final char c2) {
		buff.setLength(0);
		while (!in.hasReachedEof()) {
			final char current = in.current();
			if (c0 == current || c1 == current || c2 == current) {
				return buff.toString();
			}
			buff.append(current);
			in.next();
		}
		return buff.toString();
	}
	
	/**
	 * {@code 'A'}から{@code 'Z'}もしくは{@code 'a'}から{@code 'z'}の
	 * いずれかのみで構成される文字列を読み取って返す.
	 * @param in 入力データ
	 * @return 読み取り結果
	 */
	public String parseAbc(final Input in) {
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
		return buff.toString();
	}
	
	/**
	 * {@code 'A'}から{@code 'Z'}、{@code 'a'}から{@code 'z'}
	 * もしくは{@code '0'}から{@code '9'}の
	 * いずれかのみで構成される文字列を読み取って返す.
	 * @param in 入力データ
	 * @return 読み取り結果
	 */
	public String parseAbc123(final Input in) {
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
		return buff.toString();
	}
	
	/**
	 * {@code 'A'}から{@code 'Z'}、{@code 'a'}から{@code 'z'}、
	 * {@code '0'}から{@code '9'}もしくは{@code '_'}と{@code '$'}の
	 * いずれかのみで構成される文字列を読み取って返す.
	 * @param in 入力データ
	 * @return 読み取り結果
	 */
	public String parseAbc123_$(final Input in) {
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
		return buff.toString();
	}
	
	/**
	 * 引用符で囲われた文字列を読み取って返す.
	 * @param in 入力データ
	 * @return 読み取り結果
	 */
	public String parseQuotedString(final Input in) {
		final char c0 = in.current();
		if (c0 != '"' && c0 != '\'' && c0 != '`') {
			throw new ParseError("No quoted string found.", in);
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
						return buff.toString();
					}
				}
				buff.append(c1);
			}
		} else {
			while (!in.hasReachedEof()) {
				final char c1 = in.next();
				if (c1 == c0) {
					in.next();
					return buff.toString();
				}
				buff.append(c1 != escape ? c1 : in.next());
			}
		}
		throw new ParseError("Unclosed quoted string.", in);
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
		final String rest = in.rest();
		if (!rest.startsWith(expected)) {
			ParseError.arg1NotFound(in, expected);
		}
	}
}
