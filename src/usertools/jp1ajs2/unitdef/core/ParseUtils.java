package usertools.jp1ajs2.unitdef.core;

import java.io.InputStream;
import usertools.jp1ajs2.unitdef.util.Either;
import com.m12i.code.parse.Reader;

/**
 * JP1のユニット定義コードをパースするためのAPIを提供するユーティリティ・クラス.
 */
public final class ParseUtils {
	private ParseUtils() {
	}

	/**
	 * {@link InputStream}を介してJP1ユニット定義コードをパースして結果を返す. 結果は{@link Either}
	 * オブジェクトとして返される。 パースが成功した場合は"Right(Unit)"。失敗した場合は"Left(IOException)"もしくは
	 * "Left(ParseException)"。 JP1ユニット定義コードはUTF-8で記述されているものとみなしてパースを行う。
	 * 
	 * @param stream
	 *            JP1ユニット定義コード
	 * @return {@link Either}オブジェクト
	 */
	public static Either<Throwable, Unit> parse(InputStream stream) {
		try {
			return Either.success(new Parser().parse(stream));
		} catch (Exception e) {
			return Either.failure(e);
		}
	}

	/**
	 * {@link InputStream}を介してJP1ユニット定義コードをパースして結果を返す. 結果は{@link Either}
	 * オブジェクトとして返される。 パースが成功した場合は"Right(Unit)"。失敗した場合は"Left(IOException)"もしくは
	 * "Left(ParseException)"。
	 * JP1ユニット定義コードは第2引数で指定されたキャラクターセットで記述されているものとみなしてパースを行う。
	 * 
	 * @param stream
	 *            JP1ユニット定義コード
	 * @param charset
	 *            キャラクターセット
	 * @return {@link Either}オブジェクト
	 */
	public static Either<Throwable, Unit> parse(InputStream stream,
			String charset) {
		try {
			return Either.success(new Parser().parse(stream, charset));
		} catch (Exception e) {
			return Either.failure(e);
		}
	}

	/**
	 * 文字列をJP1ユニット定義コードとしてパースして結果を返す. 結果は{@link Either}オブジェクトとして返される。
	 * パースが成功した場合は"Right(Unit)"。失敗した場合は"Left(ParseException)"。
	 * 
	 * @param string
	 *            JP1ユニット定義コード
	 * @return {@link Either}オブジェクト
	 */
	public static Either<Throwable, Unit> parse(String string) {
		try {
			return Either.success(new Parser().parse(string));
		} catch (Exception e) {
			return Either.failure(e);
		}
	}

	/**
	 * {@link Reader}を介してJP1ユニット定義コードをパースして結果を返す. 結果は{@link Either}
	 * オブジェクトとして返される。 パースが成功した場合は"Right(Unit)"。失敗した場合は"Left(IOException)"もしくは
	 * "Left(ParseException)"。 JP1ユニット定義コードはUTF-8で記述されているものとみなしてパースを行う。
	 * 
	 * @param in
	 *            JP1ユニット定義コード
	 * @return {@link Either}オブジェクト
	 */
	public static Either<Throwable, Unit> parse(Reader in) {
		try {
			return Either.success(new Parser().parse(in));
		} catch (Exception e) {
			return Either.failure(e);
		}
	}
}