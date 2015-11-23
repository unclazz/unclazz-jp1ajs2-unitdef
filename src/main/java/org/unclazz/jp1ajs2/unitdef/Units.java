package org.unclazz.jp1ajs2.unitdef;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.charset.Charset;
import java.util.List;

import org.unclazz.jp1ajs2.unitdef.parser.ParseResult;
import org.unclazz.jp1ajs2.unitdef.parser.UnitParser;
import org.unclazz.jp1ajs2.unitdef.util.Formatter;

/**
 * {@link Unit}のためのユーティリティを提供するオブジェクト.
 */
public final class Units {
	private static final UnitParser parser = new UnitParser();
	private static final Formatter formatter = new Formatter();
	private static final UnitCollector collector = new UnitCollector();
	
	private Units() {}

	/**
	 * ファイルからユニット定義情報を読み取る.
	 * システム・デフォルトのキャラクターセットを使用する。
	 * @param f ファイル
	 * @return ユニット定義
	 * @throws IllegalArgumentException 構文エラーが検出された場合
	 */
	public static List<Unit> fromFile(final File f) {
		final ParseResult<List<Unit>> res = parser.parse(f);
		if (res.isSuccessful()) {
			return res.get();
		} else {
			throw new IllegalArgumentException(res.getError());
		}
	}

	/**
	 * ファイルからユニット定義情報を読み取る.
	 * @param f ファイル
	 * @param charset キャラクターセット
	 * @return ユニット定義
	 * @throws IllegalArgumentException 構文エラーが検出された場合
	 */
	public static List<Unit> fromFile(final File f, final Charset charset) {
		final ParseResult<List<Unit>> res = parser.parse(f, charset);
		if (res.isSuccessful()) {
			return res.get();
		} else {
			throw new IllegalArgumentException(res.getError());
		}
	}

	/**
	 * 入力ストリームからユニット定義情報を読み取る.
	 * システム・デフォルトのキャラクターセットを使用する。
	 * @param s ストリーム
	 * @return ユニット定義
	 * @throws IOException I/Oエラーが発生した場合
	 * @throws IllegalArgumentException 構文エラーが検出された場合
	 */
	public static List<Unit> fromStream(final InputStream s) throws IOException {
		final ParseResult<List<Unit>> res = parser.parse(s);
		if (res.isSuccessful()) {
			return res.get();
		} else {
			throw new IllegalArgumentException(res.getError());
		}
	}

	/**
	 * 入力ストリームからユニット定義情報を読み取る.
	 * @param s ストリーム
	 * @param charset キャラクターセット
	 * @return ユニット定義
	 * @throws IOException I/Oエラーが発生した場合
	 * @throws IllegalArgumentException 構文エラーが検出された場合
	 */
	public static List<Unit> fromStream(final InputStream s, final Charset charset) throws IOException {
		final ParseResult<List<Unit>> res = parser.parse(s, charset);
		if (res.isSuccessful()) {
			return res.get();
		} else {
			throw new IllegalArgumentException(res.getError());
		}
	}

	/**
	 * 文字列からユニット定義情報を読み取る.
	 * @param s 文字列
	 * @return ユニット定義
	 * @throws IllegalArgumentException 構文エラーが検出された場合
	 */
	public static List<Unit> fromString(final String s) {
		final ParseResult<List<Unit>> res = parser.parse(s);
		if (res.isSuccessful()) {
			return res.get();
		} else {
			throw new IllegalArgumentException(res.getError());
		}
	}
	
	/**
	 * 引数に指定されたユニット定義とその子孫のユニット定義を要素とするリストを返す.
	 * リスト要素は深さ優先探索により発見された順序で並ぶ。
	 * @param unit ユニット
	 * @return ユニット定義リスト
	 */
	public static List<Unit> asList(Unit unit) {
		return collector.collect(unit);
	}
	
	/**
	 * ユニット定義を文字列化する.
	 * @param unit ユニット定義
	 * @return 文字列化されたユニット定義
	 */
	public static String toString(final Unit unit) {
		return formatter.format(unit);
	}
	
	/**
	 * ユニット定義を文字列化して出力ストリームに書き出す.
	 * @param unit ユニット定義
	 * @param out 出力ストリーム
	 * @param charset キャラクターセット
	 * @throws IOException 処理中にI/Oエラーが発生した場合
	 */
	public static void writeToStream(final Unit unit, final OutputStream out, final Charset charset) throws IOException {
		formatter.format(unit, out, charset);
	}
	
	/**
	 * ユニット定義を文字列化して出力ストリームに書き出す.
	 * @param unit ユニット定義
	 * @param out 出力ストリーム
	 * @throws IOException 処理中にI/Oエラーが発生した場合
	 */
	public static void writeToStream(final Unit unit, final OutputStream out) throws IOException {
		writeToStream(unit, out, Charset.defaultCharset());
	}
}