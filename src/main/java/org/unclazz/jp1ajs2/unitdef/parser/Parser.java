package org.unclazz.jp1ajs2.unitdef.parser;

/**
 * パーサーのインターフェース.
 * @param <T> パース結果
 */
public interface Parser<T> {
	/**
	 * パース処理を行う.
	 * @param in 入力データ
	 * @return パース結果
	 */
	ParseResult<T> parse(Input in);
}
