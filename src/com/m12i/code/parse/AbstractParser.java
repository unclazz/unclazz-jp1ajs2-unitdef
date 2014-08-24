package com.m12i.code.parse;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.Charset;

import com.m12i.code.parse.Parsers.Options;

/**
 * パーサを実装する際に使用する抽象クラス.
 * @param <T> パースした結果得られるオブジェクトの型
 */
public abstract class AbstractParser<T> {
	protected final Parsers.Options options;
	protected final Parsers parsers;
	public AbstractParser(final Parsers.Options options) {
		this.options = options;
		this.parsers = new Parsers(options);
	}
	public AbstractParser() {
		this.options = new Options();
		this.parsers = new Parsers(options);
	}
	/**
	 * 文字列を対象にしてパース処理を行う.
	 * @param string パース対象の文字列
	 * @return パース結果
	 */
	public final Result<T> parse(final String string) {
		return parse(Readers.createReader(string));
	}
	/**
	 * ストリームを対象にしてパース処理を行う.
	 * ストリームから文字列をロードする際、システムのデフォルト・キャラクタセット（{@code Charset#defaultCharset()}）が使用されます。
	 * @param stream パース対象のストリーム
	 * @return パース結果
	 * @throws IOException パース中に発生したIOエラー
	 */
	public final Result<T> parse(final InputStream stream) throws IOException {
		try {
			return parse(Readers.parse(stream));
		} catch (final ParseError e) {
			if (e.getCause() instanceof IOException) {
				throw (IOException) e.getCause();
			} else {
				throw e;
			}
		}
	}
	/**
	 * ストリームを対象にしてパース処理を行う.
	 * @param stream パース対象のストリーム
	 * @param charset ストリームから文字列をロードする際に使用するキャラクタセット
	 * @return パース結果
	 * @throws IOException パース中に発生したIOエラー
	 */
	public final Result<T> parse(final InputStream stream, final Charset charset) throws IOException {
		return parse(Readers.parse(stream, charset));
	}
	/**
	 * ストリームを対象にしてパース処理を行う.
	 * @param stream パース対象のストリーム
	 * @param charset ストリームから文字列をロードする際に使用するキャラクタセット
	 * @return パース結果
	 * @throws IOException パース中に発生したIOエラー
	 */
	public final Result<T> parse(final InputStream stream, final String charset) throws IOException {
		return parse(Readers.parse(stream, charset));
	}
	/**
	 * {@link Reader}オブジェクトを使用してパース処理を行う.
	 * この抽象クラスを継承・拡張する具象クラスはこのメソッドを実装する必要がある。
	 * パース処理中に発生した例外は{@link ParseError}でラップして再スローすること。
	 * @param reader {@link Reader}オブジェクト
	 * @return パース結果
	 */
	public abstract Result<T> parse(final Reader reader);
}
