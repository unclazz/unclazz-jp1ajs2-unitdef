package org.unclazz.jp1ajs2.unitdef.parser;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 各種トークンを読み取るためのヘルパー・メソッドを提供するオブジェクト.
 */
public final class ParseHelper {
	
	private static final Pattern numberPattern = Pattern.compile("^[+|\\-]?(\\d*\\.\\d+|\\d+\\.?)((e|E)[+|\\-]?\\d+)?");
	private static final char SP = ' ';
	private static final char CR = '\r';
	private static final char LF = '\n';
	
	private final StringBuilder buff = new StringBuilder();
	private final String lineCommentStart;
	private final String blockCommentStart;
	private final String blockCommentEnd;
	private final char escapePrefixInDoubleQuotes;
	private final char escapePrefixInSingleQuotes;
	private final char escapePrefixInBackQuotes;
	private final boolean skipCommentWithWhitespace;
	
	public ParseHelper() {
		this(new ParseOptions());
	}
	
	public ParseHelper(final ParseOptions options) {
		lineCommentStart = options.getLineCommentStart();
		blockCommentStart = options.getBlockCommentStart();
		blockCommentEnd = options.getBlockCommentEnd();
		escapePrefixInDoubleQuotes = options.getEscapePrefixInDoubleQuotes();
		escapePrefixInSingleQuotes = options.getEscapePrefixInSingleQuotes();
		escapePrefixInBackQuotes = options.getEscapePrefixInBackQuotes();
		skipCommentWithWhitespace = options.isSkipCommentWithWhitespace();
	}
	
	/**
	 * 空白文字をスキップする.
	 * スキップされた文字がない場合でもエラーとはしない。
	 * {@link ParseOptions#skipCommentWithWhitespace}が{@code true}の場合コメントもスキップする。
	 * @param in 入力データ
	 * @throws ParseException 空白文字のスキップ中にエラーが発生した場合
	 */
	public void skipWhitespace(final Input in) throws ParseException {
		try {
			if (skipCommentWithWhitespace) {
				this.skipComment(in);
				while (in.unlessEOF()) {
					if (in.current() <= SP) {
						in.next();
					} else {
						final String rest = in.restOfLine();
						if (rest.startsWith(lineCommentStart)) {
							next(in, lineCommentStart.length());
							while (in.unlessEOF()) {
								final char c0 = in.current();
								if (c0 == CR) {
									final char c1 = in.next();
									if (c1 == LF) {
										in.next();
									}
									break;
								}
								in.next();
								if (in.reachedEOF()) {
									break;
								}
							}
						} else if (rest.startsWith(blockCommentStart)) {
							next(in, blockCommentStart.length());
							while (in.unlessEOF()) {
								if (in.restStartsWith(blockCommentEnd)) {
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
				while (in.unlessEOF()) {
					if (in.current() <= SP) {
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
	 * @throws ParseException 空白文字のスキップ中にエラーが発生した場合
	 */
	public void skipWhitespaceWith(final Input in, final char ch) throws ParseException {
		try {
			while (in.unlessEOF()) {
				final char current = in.current();
				if (current <= SP || current == ch) {
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
	 * @throws ParseException 空白文字のスキップ中にエラーが発生した場合
	 */
	public void skipWhitespaceWith(final Input in, final char ch0, final char ch1) throws ParseException {
		try {
			while (in.unlessEOF()) {
				final char current = in.current();
				if (current <= SP || current == ch0 || current == ch1) {
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
	 * @throws ParseException 空白文字のスキップ中にエラーが発生した場合
	 */
	public void skipWhitespaceWith(final Input in, final char ch0, final char ch1, final char ch2) throws ParseException {
		try {
			while (in.unlessEOF()) {
				final char current = in.current();
				if (current <= SP || current == ch0 || current == ch1 || current == ch2) {
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
	 * @throws ParseException コメントのスキップ中にエラーが発生した場合
	 */
	public void skipComment(final Input in) throws ParseException {
		try {
			final String rest = in.restOfLine();
			if (rest.startsWith(lineCommentStart)) {
				next(in, lineCommentStart.length());
				while (in.unlessEOF()) {
					final char c0 = in.current();
					if (c0 == CR) {
						final char c1 = in.next();
						if (c1 == LF) {
							in.next();
						}
						break;
					}
					in.next();
					if (in.reachedEOF()) {
						break;
					}
				}
			} else if (rest.startsWith(blockCommentStart)) {
				next(in, blockCommentStart.length());
				while (in.unlessEOF()) {
					if (in.restStartsWith(blockCommentEnd)) {
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
	 * @throws ParseException 文字列のスキップ中にエラーが発生した場合
	 */
	public void skipWord(final Input in, final String word) throws ParseException {
		try {
			if (in.restStartsWith(word)) {
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
	 * @throws ParseException  文字列の読み取り中にエラーが発生した場合
	 */
	public String parseRawString(final Input in) throws ParseException {
		try {
			buff.setLength(0);
			while (in.unlessEOF()) {
				final char c1 = in.current();
				if (c1 <= SP) {
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
	
	/**
	 * 浮動小数点数を読み取って返す.
	 * @param in 入力データ
	 * @return 浮動小数点数
	 * @throws ParseException 浮動小数点数の読み取り中にエラーが発生した場合
	 */
	public Double parseNumber(final Input in) throws ParseException {
		final String rest = in.restOfLine();
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
	 * @throws ParseException 文字列の読み取り中にエラーが発生した場合
	 */
	public String parseUntil(final Input in, final char c0) throws ParseException {
		try {
			buff.setLength(0);
			while (in.unlessEOF()) {
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
	 * @throws ParseException 文字列の読み取り中にエラーが発生した場合
	 */
	public String parseUntil(final Input in, final char c0, final char c1) throws ParseException {
		try {
			buff.setLength(0);
			while (in.unlessEOF()) {
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
	 * @throws ParseException 文字列の読み取り中にエラーが発生した場合
	 */
	public String parseUntil(final Input in, final char c0, final char c1, final char c2) throws ParseException {
		try {
			buff.setLength(0);
			while (in.unlessEOF()) {
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
	 * @throws ParseException 文字列の読み取り中にエラーが発生した場合
	 */
	public String parseAbc(final Input in) throws ParseException {
		try {
			buff.setLength(0);
			while (in.unlessEOF()) {
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
	 * @throws ParseException  文字列の読み取り中にエラーが発生した場合
	 */
	public String parseAbc123(final Input in) throws ParseException {
		try {
			buff.setLength(0);
			while (in.unlessEOF()) {
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
	 * @throws ParseException 文字列の読み取り中にエラーが発生した場合
	 */
	public String parseAbc123_$(final Input in) throws ParseException {
		try {
			buff.setLength(0);
			while (in.unlessEOF()) {
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
	 * @throws ParseException 文字列の読み取り中にエラーが発生した場合
	 */
	public String parseQuotedString(final Input in) throws ParseException {
		try {
			final char c0 = in.current();
			if (c0 != '"' && c0 != '\'' && c0 != '`') {
				throw new ParseException("No quoted string found.", in);
			}
			
			final char escape = c0 == '"' ? escapePrefixInDoubleQuotes
					: c0 == '\'' ? escapePrefixInSingleQuotes : escapePrefixInBackQuotes;
			buff.setLength(0);
	
			if (c0 == escape) {
				while (in.unlessEOF()) {
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
				while (in.unlessEOF()) {
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
	
	/**
	 * 現在位置の文字が期待通りの文字かチェックする.
	 * @param in 入力データ
	 * @param expected 期待される文字
	 * @throws ParseException 期待通りでなかった場合
	 */
	public void check(final Input in, final char expected) throws ParseException {
		final char actual = in.current();
		if (actual != expected) {
			throw ParseException.arg1ExpectedButFoundArg2(in, expected, actual);
		}
	}
	
	/**
	 * 現在位置の次の文字が期待通りの文字かチェックする.
	 * チェックにともない現在位置が1前進する。
	 * @param in 入力データ
	 * @param expected 期待される文字
	 * @throws ParseException 期待通りでなかった場合
	 */
	public void checkNext(final Input in, final char expected) throws ParseException {
		try {
			final char actual = in.next();
			if (actual != expected) {
				throw ParseException.arg1ExpectedButFoundArg2(in, expected, actual);
			}
		} catch (InputExeption e) {
			throw new ParseException(e, in);
		}
	}
	
	/**
	 * 現在位置から始まる文字列が期待通りの文字列で始まるかどうかチェックする.
	 * チェックにともなう現在位置移動は発生しない。
	 * @param in 入力データ
	 * @param expected 期待される文字列
	 * @throws ParseException 期待通りでなかった場合
	 */
	public void checkWord(final Input in, final String expected) throws ParseException {
		final String rest = in.restOfLine();
		if (!rest.startsWith(expected)) {
			throw ParseException.arg1NotFound(in, expected);
		}
	}

	/**
	 * 指定された回数だけ現在位置を先に進める.
	 * @param in 入力データ
	 * @param times 回数
	 * @throws ParseException 現在位置の移動中にエラーが発生した場合
	 */
	private void next(final Input in, final int times) throws ParseException {
		for (int i = 0; i < times; i++) {
			try {
				in.next();
			} catch (InputExeption e) {
				throw new ParseException(e, in);
			}
		}
	}
}
