package com.m12i.jp1ajs2.unitdef.parser;

/**
 * {@link Parsers}や{@link AbstractParser}がパース処理を行うのに必要なメソッドを提供するインターフェース.
 */
public interface Reader {
	/**
	 * 現在文字（読み取り位置の文字）を返す.
	 * 読み取り位置がEOFに到達している場合このメソッドは{@literal '\u0000'}（ヌル文字）を返します。
	 * @return 現在文字
	 */
	char current();
	/**
	 * 読み取り位置を1つ前進させた上でその時点の現在文字（現在読み取り位置の文字）を返す.
	 * 読み取り位置がEOFに到達している場合このメソッドは{@literal '\u0000'}（ヌル文字）を返します。
	 * @return 現在文字
	 */
	char next();
	/**
	 * 読み取り位置のある行数を返す.
	 * @return 読み取り位置の行数
	 */
	int lineNo();
	/**
	 * 読み取り位置のある列数（行内における左端からの文字数）を返す.
	 * @return 読み取り位置の列数
	 */
	int columnNo();
	/**
	 * 読み取り位置のある行（文字列）を返す.
	 * 返される文字列に行末の改行文字は含まれない。
	 * 読み取り位置がEOFに到達している場合このメソッドは{@literal null}を返します。
	 * @return 読み取り位置の行
	 */
	String line();
	/**
	 * 読み取り位置から行末までの文字列を返す.
	 * 返される文字列に行末の改行文字は含まれない。
	 * @return 読み取り位置から行末までの文字列
	 */
	String rest();
	/**
	 * 読み取り位置がEOF（ファイル末尾）に到達しているかどうかを返す.
	 * @return {@literal true}：到達している、{@literal false}：到達していない
	 */
	boolean hasReachedEof();
	/**
	 * 読み取り位置がEOL（行末）に到達しているかどうかを返す.
	 * @return {@literal true}：到達している、{@literal false}：到達していない
	 */
	boolean hasReachedEol();
}
