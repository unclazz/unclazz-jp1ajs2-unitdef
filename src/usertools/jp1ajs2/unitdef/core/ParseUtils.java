package usertools.jp1ajs2.unitdef.core;

import java.io.IOException;
import java.io.InputStream;

import com.m12i.code.parse.Parsable;
import com.m12i.code.parse.ParseException;

/**
 * JP1のユニット定義コードをパースするためのAPIを提供するユーティリティ・クラス.
 */
public final class ParseUtils {
	private ParseUtils() {}
	
	/**
	 * {@link InputStream}を介してJP1ユニット定義コードをパースし{@link Unit}インターフェースのインスタンスを返す.
	 * JP1ユニット定義コードはUTF-8で記述されているものとみなしてパースを行う。
	 * @param stream JP1ユニット定義コード
	 * @return {@link Unit}インターフェースのインスタンス
	 * @throws IOException JP1ユニット定義コードをパース中に入力エラーが発生した場合
	 * @throws ParseException JP1ユニット定義コードをパース中に構文エラーが発生した場合
	 */
	public static Unit parse(InputStream stream) throws IOException, ParseException {
		return new Parser().parse(stream);
	}
	/**
	 * {@link InputStream}を介してJP1ユニット定義コードをパースし{@link Unit}インターフェースのインスタンスを返す.
	 * JP1ユニット定義コードは第2引数で指定されたキャラクターセットで記述されているものとみなしてパースを行う。
	 * @param stream JP1ユニット定義コード
	 * @param charset キャラクターセット
	 * @return {@link Unit}インターフェースのインスタンス
	 * @throws IOException JP1ユニット定義コードをパース中に入力エラーが発生した場合
	 * @throws ParseException JP1ユニット定義コードをパース中に構文エラーが発生した場合
	 */
	public static Unit parse(InputStream stream, String charset) throws IOException, ParseException {
		return new Parser().parse(stream, charset);
	}
	/**
	 * 文字列をJP1ユニット定義コードとみなしてパースし{@link Unit}インターフェースのインスタンスを返す.
	 * @param string JP1ユニット定義コード
	 * @return {@link Unit}インターフェースのインスタンス
	 * @throws ParseException JP1ユニット定義コードをパース中に構文エラーが発生した場合
	 */
	public static Unit parse(String string) throws ParseException {
		return new Parser().parse(string);
	}
	/**
	 * {@link Parsable}を介してJP1ユニット定義コードをパースし{@link Unit}インターフェースのインスタンスを返す.
	 * JP1ユニット定義コードはUTF-8で記述されているものとみなしてパースを行う。
	 * @param parsable JP1ユニット定義コード
	 * @return {@link Unit}インターフェースのインスタンス
	 * @throws IOException JP1ユニット定義コードをパース中に入力エラーが発生した場合
	 * @throws ParseException JP1ユニット定義コードをパース中に構文エラーが発生した場合
	 */
	public static Unit parse(Parsable parsable) throws IOException, ParseException {
		return new Parser().parse(parsable);
	}
}
