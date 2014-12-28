package com.m12i.jp1ajs2.unitdef.parser;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

final class Parsers {
	static final class Options {
		private String lineCommentStart = "//";
		private String blockCommentStart = "/*";
		private String blockCommentEnd = "*/";
		private char escapePrefixInDoubleQuotes = '\\';
		private char escapePrefixInSingleQuotes = '\\';
		private char escapePrefixInBackQuotes = '\\';
		private boolean skipCommentWithWhitespace = true;
		String getLineCommentStart() {
			return lineCommentStart;
		}
		void setLineCommentStart(String lineCommentStart) {
			this.lineCommentStart = lineCommentStart;
		}
		String getBlockCommentStart() {
			return blockCommentStart;
		}
		void setBlockCommentStart(String blockCommentStart) {
			this.blockCommentStart = blockCommentStart;
		}
		String getBlockCommentEnd() {
			return blockCommentEnd;
		}
		void setBlockCommentEnd(String blockCommentEnd) {
			this.blockCommentEnd = blockCommentEnd;
		}
		char getEscapePrefixInDoubleQuotes() {
			return escapePrefixInDoubleQuotes;
		}
		void setEscapePrefixInDoubleQuotes(char escapePrefixInDoubleQuotes) {
			this.escapePrefixInDoubleQuotes = escapePrefixInDoubleQuotes;
		}
		char getEscapePrefixInSingleQuotes() {
			return escapePrefixInSingleQuotes;
		}
		void setEscapePrefixInSingleQuotes(char escapePrefixInSingleQuotes) {
			this.escapePrefixInSingleQuotes = escapePrefixInSingleQuotes;
		}
		char getEscapePrefixInBackQuotes() {
			return escapePrefixInBackQuotes;
		}
		void setEscapePrefixInBackQuotes(char escapePrefixInBackQuotes) {
			this.escapePrefixInBackQuotes = escapePrefixInBackQuotes;
		}
		boolean isSkipCommentWithWhitespace() {
			return skipCommentWithWhitespace;
		}
		void setSkipCommentWithWhitespace(boolean skipCommentWithWhitespace) {
			this.skipCommentWithWhitespace = skipCommentWithWhitespace;
		}
	}
	
	private static final Pattern numberPattern = Pattern.compile("^[+|\\-]?(\\d*\\.\\d+|\\d+\\.?)((e|E)[+|\\-]?\\d+)?");
	

	private static void next(final Input in, final int times) {
		for (int i = 0; i < times; i++) {
			try {
				in.next();
			} catch (InputExeption e) {
				throw new ParseException(e, in);
			}
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
	
	Parsers() {
		this(new Options());
	}
	
	Parsers(final Options options) {
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
	void skipWhitespace(final Input in) {
		try {
			if (skipCommentWithWhitespace) {
				this.skipComment(in);
				while (in.unlessEof()) {
					if (in.current() <= ' ') {
						in.next();
					} else {
						final String rest = in.rest();
						if (rest.startsWith(lineCommentStart)) {
							next(in, lineCommentStart.length());
							while (in.unlessEof()) {
								final char c0 = in.current();
								if (c0 == '\r') {
									final char c1 = in.next();
									if (c1 == '\n') {
										in.next();
									}
									break;
								}
								in.next();
								if (in.reachedEof()) {
									break;
								}
							}
						} else if (rest.startsWith(blockCommentStart)) {
							next(in, blockCommentStart.length());
							while (in.unlessEof()) {
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
				while (in.unlessEof()) {
					if (in.current() <= ' ') {
						in.next();
					} else {
						break;
					}
				}
			}
			return;
		} catch (InputExeption e) {
			throw new ParseException(e, in);
		}
	}
	
	/**
	 * 引数で指定された文字とともに空白文字をスキップする.
	 * スキップされた文字がない場合（現在読み取り位置が引数で指定された文字や空白文字でない場合）でもエラーとはしない。
	 * @param in 入力データ
	 * @param ch スキップ対象文字
	 */
	void skipWhitespaceWith(final Input in, final char ch) {
		try {
			while (in.unlessEof()) {
				final char current = in.current();
				if (current <= ' ' || current == ch) {
					in.next();
				} else {
					break;
				}
			}
			return;
		} catch (InputExeption e) {
			throw new ParseException(e, in);
		}
	}
	
	/**
	 * 引数で指定された文字とともに空白文字をスキップする.
	 * スキップされた文字がない場合（現在読み取り位置が引数で指定された文字や空白文字でない場合）でもエラーとはしない。
	 * @param in 入力データ
	 * @param ch0 スキップ対象文字
	 * @param ch1 スキップ対象文字
	 */
	void skipWhitespaceWith(final Input in, final char ch0, final char ch1) {
		try {
			while (in.unlessEof()) {
				final char current = in.current();
				if (current <= ' ' || current == ch0 || current == ch1) {
					in.next();
				} else {
					break;
				}
			}
			return;
		} catch (InputExeption e) {
			throw new ParseException(e, in);
		}
	}
	
	/**
	 * 引数で指定された文字とともに空白文字をスキップする.
	 * スキップされた文字がない場合（現在読み取り位置が引数で指定された文字や空白文字でない場合）でもエラーとはしない。
	 * @param in 入力データ
	 * @param ch0 スキップ対象文字
	 * @param ch1 スキップ対象文字
	 * @param ch2 スキップ対象文字
	 */
	void skipWhitespaceWith(final Input in, final char ch0, final char ch1, final char ch2) {
		try {
			while (in.unlessEof()) {
				final char current = in.current();
				if (current <= ' ' || current == ch0 || current == ch1 || current == ch2) {
					in.next();
				} else {
					break;
				}
			}
			return;
		} catch (InputExeption e) {
			throw new ParseException(e, in);
		}
	}
	
	/**
	 * コメントをスキップする.
	 * スキップされた文字がない場合でもエラーとはしない。
	 * @param in 入力データ
	 */
	void skipComment(final Input in) {
		try {
			final String rest = in.rest();
			if (rest.startsWith(lineCommentStart)) {
				next(in, lineCommentStart.length());
				while (in.unlessEof()) {
					final char c0 = in.current();
					if (c0 == '\r') {
						final char c1 = in.next();
						if (c1 == '\n') {
							in.next();
						}
						break;
					}
					in.next();
					if (in.reachedEof()) {
						break;
					}
				}
			} else if (rest.startsWith(blockCommentStart)) {
				next(in, blockCommentStart.length());
				while (in.unlessEof()) {
					if (in.startsWith(blockCommentEnd)) {
						next(in, blockCommentEnd.length());
						break;
					}
					in.next();
				}
			}
			return;
		} catch (InputExeption e) {
			throw new ParseException(e, in);
		}
	}
	
	/**
	 * 引数で指定された文字列をスキップする.
	 * 読み取り位置に指定された文字列が見つからなかった場合エラーとする。
	 * @param in 入力データ
	 * @param word スキップする文字列
	 */
	void skipWord(final Input in, final String word) {
		try {
			if (in.startsWith(word)) {
				for (int i = 0; i < word.length(); i ++) {
					in.next();
				}
				return;
			} else {
				throw new ParseException(String.format("\"%s\" not found.", word), in);
			}
		} catch (InputExeption e) {
			throw new ParseException(e, in);
		}
	}
	
	/**
	 * 空白文字以外から構成される文字列を読み取って返す.
	 * @param in 入力データ
	 * @return 読み取り結果
	 */
	String parseRawString(final Input in) {
		try {
			buff.setLength(0);
			while (in.unlessEof()) {
				final char c1 = in.current();
				if (c1 <= ' ') {
					break;
				}
				buff.append(c1);
				in.next();
			}
			return buff.toString();
		} catch (InputExeption e) {
			throw new ParseException(e, in);
		}
	}
	
	Double parseNumber(final Input in) {
		final String rest = in.rest();
		final Matcher m = numberPattern.matcher(rest);
		if (m.lookingAt()) {
			final String n = m.group();
			next(in, n.length());
			return Double.parseDouble(n);
		} else {
			throw new ParseException(String.format("Number literal expected but \"%s...\" found",
					rest.length() > 5 ? rest.substring(0, 5) : rest), in);
		}
	}
	
	/**
	 * 引数で指定された文字が現れる前の文字列を読み取ってを返す.
	 * @param in 入力データ
	 * @param c0 読み取りを終える文字
	 * @return 読み取り結果
	 */
	String parseUntil(final Input in, final char c0) {
		try {
			buff.setLength(0);
			while (in.unlessEof()) {
				final char current = in.current();
				if (c0 == current) {
					return buff.toString();
				}
				buff.append(current);
				in.next();
			}
			return buff.toString();
		} catch (InputExeption e) {
			throw new ParseException(e, in);
		}
	}
	
	/**
	 * 引数で指定された文字が現れる前の文字列を読み取ってを返す.
	 * @param in 入力データ
	 * @param c0 読み取りを終える文字
	 * @param c1 読み取りを終える文字
	 * @return 読み取り結果
	 */
	String parseUntil(final Input in, final char c0, final char c1) {
		try {
			buff.setLength(0);
			while (in.unlessEof()) {
				final char current = in.current();
				if (c0 == current || c1 == current) {
					return buff.toString();
				}
				buff.append(current);
				in.next();
			}
			return buff.toString();
		} catch (InputExeption e) {
			throw new ParseException(e, in);
		}
	}
	
	/**
	 * 引数で指定された文字が現れる前の文字列を読み取ってを返す.
	 * @param in 入力データ
	 * @param c0 読み取りを終える文字
	 * @param c1 読み取りを終える文字
	 * @param c2 読み取りを終える文字
	 * @return 読み取り結果
	 */
	String parseUntil(final Input in, final char c0, final char c1, final char c2) {
		try {
			buff.setLength(0);
			while (in.unlessEof()) {
				final char current = in.current();
				if (c0 == current || c1 == current || c2 == current) {
					return buff.toString();
				}
				buff.append(current);
				in.next();
			}
			return buff.toString();
		} catch (InputExeption e) {
			throw new ParseException(e, in);
		}
	}
	
	/**
	 * {@code 'A'}から{@code 'Z'}もしくは{@code 'a'}から{@code 'z'}の
	 * いずれかのみで構成される文字列を読み取って返す.
	 * @param in 入力データ
	 * @return 読み取り結果
	 */
	String parseAbc(final Input in) {
		try {
			buff.setLength(0);
			while (in.unlessEof()) {
				final char c = in.current();
				if (('A' <= c && c <= 'Z') || ('a' <= c && c <= 'z')) {
					buff.append(c);
					in.next();
				} else {
					break;
				}
			}
			return buff.toString();
		} catch (InputExeption e) {
			throw new ParseException(e, in);
		}
	}
	
	/**
	 * {@code 'A'}から{@code 'Z'}、{@code 'a'}から{@code 'z'}
	 * もしくは{@code '0'}から{@code '9'}の
	 * いずれかのみで構成される文字列を読み取って返す.
	 * @param in 入力データ
	 * @return 読み取り結果
	 */
	String parseAbc123(final Input in) {
		try {
			buff.setLength(0);
			while (in.unlessEof()) {
				final char c = in.current();
				if (('A' <= c && c <= 'Z') || ('a' <= c && c <= 'z') || ('0' <= c && c <= '9')) {
					buff.append(c);
					in.next();
				} else {
					break;
				}
			}
			return buff.toString();
		} catch (InputExeption e) {
			throw new ParseException(e, in);
		}
	}
	
	/**
	 * {@code 'A'}から{@code 'Z'}、{@code 'a'}から{@code 'z'}、
	 * {@code '0'}から{@code '9'}もしくは{@code '_'}と{@code '$'}の
	 * いずれかのみで構成される文字列を読み取って返す.
	 * @param in 入力データ
	 * @return 読み取り結果
	 */
	String parseAbc123_$(final Input in) {
		try {
			buff.setLength(0);
			while (in.unlessEof()) {
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
		} catch (InputExeption e) {
			throw new ParseException(e, in);
		}
	}
	
	/**
	 * 引用符で囲われた文字列を読み取って返す.
	 * @param in 入力データ
	 * @return 読み取り結果
	 */
	String parseQuotedString(final Input in) {
		try {
			final char c0 = in.current();
			if (c0 != '"' && c0 != '\'' && c0 != '`') {
				throw new ParseException("No quoted string found.", in);
			}
			
			final char escape = c0 == '"' ? escapePrefixInDoubleQuotes
					: c0 == '\'' ? escapePrefixInSingleQuotes : escapePrefixInBackQuotes;
			buff.setLength(0);
	
			if (c0 == escape) {
				while (in.unlessEof()) {
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
				while (in.unlessEof()) {
					final char c1 = in.next();
					if (c1 == c0) {
						in.next();
						return buff.toString();
					}
					buff.append(c1 != escape ? c1 : in.next());
				}
			}
			throw new ParseException("Unclosed quoted string.", in);
		} catch (InputExeption e) {
			throw new ParseException(e, in);
		}
	}
	
	void check(final Input in, final char expected) {
		final char actual = in.current();
		if (actual != expected) {
			throw ParseException.arg1ExpectedButFoundArg2(in, expected, actual);
		}
	}
	
	void checkNext(final Input in, final char expected) {
		try {
			final char actual = in.next();
			if (actual != expected) {
				throw ParseException.arg1ExpectedButFoundArg2(in, expected, actual);
			}
		} catch (InputExeption e) {
			throw new ParseException(e, in);
		}
	}
	
	void checkWord(final Input in, final String expected) {
		final String rest = in.rest();
		if (!rest.startsWith(expected)) {
			throw ParseException.arg1NotFound(in, expected);
		}
	}
}
