package com.m12i.code.parse;

/**
 * {@link Parser}が読み取りを行うのに必要なメソッドを提供するインターフェース.
 * 
 */
public interface Parsable {
	/**
	 * 現在文字（読み取り位置の文字）を返す.
	 * 読み取り位置がEOFに到達している場合このメソッドは{@literal '\u0000'}（ヌル文字）を返します。
	 * @return 現在文字
	 * @throws UnexpectedException 予期せぬエラーが発生した場合
	 */
	char current() throws UnexpectedException;
	/**
	 * 読み取り位置を1つ前進させた上でその時点の現在文字（現在読み取り位置の文字）を返す.
	 * 読み取り位置がEOFに到達している場合このメソッドは{@literal '\u0000'}（ヌル文字）を返します。
	 * @return 現在文字
	 * @throws UnexpectedException 予期せぬエラーが発生した場合
	 */
	char next() throws UnexpectedException;
	/**
	 * 読み取り位置のある行数を返す.
	 * @return 読み取り位置の行数
	 * @throws UnexpectedException 予期せぬエラーが発生した場合
	 */
	int lineNo() throws UnexpectedException;
	/**
	 * 読み取り位置のある列数（行内における左端からの文字数）を返す.
	 * @return 読み取り位置の列数
	 * @throws UnexpectedException 予期せぬエラーが発生した場合
	 */
	int columnNo() throws UnexpectedException;
	/**
	 * 読み取り位置のある行（文字列）を返す.
	 * 読み取り位置がEOFに到達している場合このメソッドは{@literal null}を返します。
	 * @return 読み取り位置の行
	 * @throws UnexpectedException 予期せぬエラーが発生した場合
	 */
	String line() throws UnexpectedException;
	/**
	 * 読み取り位置がEOF（ファイル末尾）に到達しているかどうかを返す.
	 * @return {@literal true}：到達している、{@literal false}：到達していない
	 * @throws UnexpectedException 予期せぬエラーが発生した場合
	 */
	boolean hasReachedEof() throws UnexpectedException;
	/**
	 * 読み取り位置がEOL（行末）に到達しているかどうかを返す.
	 * @return {@literal true}：到達している、{@literal false}：到達していない
	 * @throws UnexpectedException 予期せぬエラーが発生した場合
	 */
	boolean hasReachedEol() throws UnexpectedException;
}
