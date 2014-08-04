package usertools.jp1ajs2.unitdef.core;

import java.io.ByteArrayInputStream;
import java.io.InputStream;

import unklazz.parsec.LazyReader;
import unklazz.parsec.Reader;
import usertools.jp1ajs2.unitdef.util.Either;

import com.m12i.code.parse.Parsable;

/**
 * JP1のユニット定義コードをパースするためのAPIを提供するユーティリティ・クラス.
 */
public final class ParseUtils2 {
	private ParseUtils2() {}
	private static final UnitP unitP = new UnitP();
	
	/**
	 * {@link InputStream}を介してJP1ユニット定義コードをパースして結果を返す.
	 * 結果は{@link Either}オブジェクトとして返される。
	 * パースが成功した場合は"Right(Unit)"。失敗した場合は"Left(IOException)"もしくは"Left(ParseException)"。
	 * JP1ユニット定義コードはUTF-8で記述されているものとみなしてパースを行う。
	 * @param stream JP1ユニット定義コード
	 * @return {@link Either}オブジェクト
	 */
	public static Either<Throwable, Unit> parse(InputStream stream) {
		try {
			return Either.success(unitP.parse(new LazyReader(stream)).get());
		} catch (Exception e) {
			return Either.failure(e);
		}
	}
	/**
	 * {@link InputStream}を介してJP1ユニット定義コードをパースして結果を返す.
	 * 結果は{@link Either}オブジェクトとして返される。
	 * パースが成功した場合は"Right(Unit)"。失敗した場合は"Left(IOException)"もしくは"Left(ParseException)"。
	 * JP1ユニット定義コードは第2引数で指定されたキャラクターセットで記述されているものとみなしてパースを行う。
	 * @param stream JP1ユニット定義コード
	 * @param charset キャラクターセット
	 * @return {@link Either}オブジェクト
	 */
	public static Either<Throwable, Unit> parse(InputStream stream, String charset) {
		try {
			return Either.success(unitP.parse(new LazyReader(stream, charset)).get());
		} catch (Exception e) {
			return Either.failure(e);
		}
	}
	/**
	 * 文字列をJP1ユニット定義コードとしてパースして結果を返す.
	 * 結果は{@link Either}オブジェクトとして返される。
	 * パースが成功した場合は"Right(Unit)"。失敗した場合は"Left(ParseException)"。
	 * @param string JP1ユニット定義コード
	 * @return {@link Either}オブジェクト
	 */
	public static Either<Throwable, Unit> parse(String string) {
		try {
			return Either.success(unitP.parse(new LazyReader(new ByteArrayInputStream(string.getBytes()))).get());
		} catch (Exception e) {
			return Either.failure(e);
		}
	}
	/**
	 * {@link Parsable}を介してJP1ユニット定義コードをパースして結果を返す.
	 * 結果は{@link Either}オブジェクトとして返される。
	 * パースが成功した場合は"Right(Unit)"。失敗した場合は"Left(IOException)"もしくは"Left(ParseException)"。
	 * JP1ユニット定義コードはUTF-8で記述されているものとみなしてパースを行う。
	 * 
	 * @param parsable JP1ユニット定義コード
	 * @return {@link Either}オブジェクト
	 */
	public static Either<Throwable, Unit> parse(Reader reader) {
		try {
			return Either.success(unitP.parse(reader).get());
		} catch (Exception e) {
			return Either.failure(e);
		}
	}
}
