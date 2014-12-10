package com.m12i.jp1ajs2.unitdef;

import java.io.File;
import java.io.InputStream;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;

import com.m12i.jp1ajs2.unitdef.parser.UnitParser;
import com.m12i.jp1ajs2.unitdef.parser.Input;

/**
 * JP1のユニット定義コードをパースするためのAPIを提供するユーティリティ・クラス.
 */
public final class Units {
	private Units() {
	}

	public static ParseResult fromFile(final File f) {
		try {
			return fromInput(Input.fromFile(f));
		} catch (Exception e) {
			return ParseResult.failure(e);
		}
	}

	public static ParseResult fromFile(final File f, final Charset charset) {
		try {
			return fromInput(Input.fromFile(f, charset));
		} catch (Exception e) {
			return ParseResult.failure(e);
		}
	}

	/**
	 * {@link InputStream}を介してJP1ユニット定義コードをパースして結果を返す. 結果は{@link ParseResult}
	 * オブジェクトとして返される。 パースが成功した場合は"Right(Unit)"。失敗した場合は"Left(IOException)"もしくは
	 * "Left(ParseException)"。 JP1ユニット定義コードはUTF-8で記述されているものとみなしてパースを行う。
	 * 
	 * @param stream
	 *            JP1ユニット定義コード
	 * @return {@link ParseResult}オブジェクト
	 */
	public static ParseResult fromStream(final InputStream s) {
		try {
			return fromInput(Input.fromStream(s));
		} catch (Exception e) {
			return ParseResult.failure(e);
		}
	}

	/**
	 * {@link InputStream}を介してJP1ユニット定義コードをパースして結果を返す. 結果は{@link ParseResult}
	 * オブジェクトとして返される。 パースが成功した場合は"Right(Unit)"。失敗した場合は"Left(IOException)"もしくは
	 * "Left(ParseException)"。
	 * JP1ユニット定義コードは第2引数で指定されたキャラクターセットで記述されているものとみなしてパースを行う。
	 * 
	 * @param stream
	 *            JP1ユニット定義コード
	 * @param charset
	 *            キャラクターセット
	 * @return {@link ParseResult}オブジェクト
	 */
	public static ParseResult fromStream(final InputStream s, final Charset charset) {
		try {
			return fromInput(Input.fromStream(s, charset));
		} catch (Exception e) {
			return ParseResult.failure(e);
		}
	}

	/**
	 * 文字列をJP1ユニット定義コードとしてパースして結果を返す. 結果は{@link ParseResult}オブジェクトとして返される。
	 * パースが成功した場合は"Right(Unit)"。失敗した場合は"Left(ParseException)"。
	 * 
	 * @param string
	 *            JP1ユニット定義コード
	 * @return {@link ParseResult}オブジェクト
	 */
	public static ParseResult fromString(final String s) {
		return fromInput(Input.fromString(s));
	}
	
	private static ParseResult fromInput(final Input in) {
		try {
			return ParseResult.success(new UnitParser().parse(in));
		} catch (Exception e) {
			return ParseResult.failure(e);
		}
	}
	
	public static List<Unit> asList(Unit unit) {
		final ArrayList<Unit> list = new ArrayList<>();
		collectSubUnits(list, unit);
		return list;
	}
	
	private static void collectSubUnits(List<Unit> list, Unit unit) {
		list.add(unit);
		for (final Unit child : unit.getSubUnits()) {
			collectSubUnits(list, child);
		}
	}
}