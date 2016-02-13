package org.unclazz.jp1ajs2.unitdef.parser;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.io.Reader;
import java.nio.charset.Charset;

/**
 * パーサーを実装する際に使用する抽象クラス.
 * このクラスは{@link Parser}インターフェースを拡張して
 * {@code String}や{@code InputStream}、そして{@code File}を入力として受け付けるようにしたもの。
 * @param <T> パースした結果得られるオブジェクトの型
 */
public abstract class ParserSupport<T> implements Parser<T> {
	protected final ParseOptions options;
	protected final ParseHelper helper;
	
	public ParserSupport(final ParseOptions options) {
		this.options = options;
		this.helper = new ParseHelper(options);
	}
	public ParserSupport() {
		this.options = new ParseOptions();
		this.helper = new ParseHelper(options);
	}
	
	/**
	 * 文字列を対象にしてパース処理を行う.
	 * @param s パース対象の文字列
	 * @return パース結果
	 */
	public final ParseResult<T> parse(final CharSequence s) {
		try {
			return parse(Input.fromCharSequence(s));
		} catch (final InputExeption e) {
			return ParseResult.failure(e);
		}
	}
	/**
	 * ストリームを対象にしてパース処理を行う.
	 * システムのデフォルト・キャラクタセット（{@code Charset#defaultCharset()}）が使用される。
	 * @param stream パース対象のストリーム
	 * @return パース結果
	 */
	public final ParseResult<T> parse(final InputStream stream) {
		try {
			return parse(Input.fromStream(stream));
		} catch (final Exception e) {
			return ParseResult.failure(e);
		}
	}
	/**
	 * ストリームを対象にしてパース処理を行う.
	 * @param stream パース対象のストリーム
	 * @param charset キャラクタセット
	 * @return パース結果
	 */
	public final ParseResult<T> parse(final InputStream stream, final Charset charset) {
		try {
			return parse(Input.fromStream(stream, charset));
		} catch (InputExeption e) {
			return ParseResult.failure(e);
		}
	}
	/**
	 * リーダを対象にしてパース処理を行う.
	 * @param reader パース対象のリーダ
	 * @return パース結果
	 */
	public final ParseResult<T> parse(final Reader reader) {
		try {
			return parse(Input.fromReader(reader));
		} catch (InputExeption e) {
			return ParseResult.failure(e);
		}
	}
	/**
	 * ファイルを対象にしてパース処理を行う.
	 * システムのデフォルト・キャラクタセット（{@code Charset#defaultCharset()}）が使用される。
	 * @param file パース対象のファイル
	 * @return パース結果
	 */
	public final ParseResult<T> parse(final File file) {
		return parse(file, Charset.defaultCharset());
	}
	/**
	 * ファイルを対象にしてパース処理を行う.
	 * @param file パース対象のファイル
	 * @param charset キャラクタセット
	 * @return パース結果
	 */
	public final ParseResult<T> parse(final File file, final Charset charset) {
		try {
			return parse(new FileInputStream(file), charset);
		} catch (FileNotFoundException e) {
			return ParseResult.failure(e);
		}
	}
	
	/**
	 * {@link Input}オブジェクトを使用してパース処理を行う.
	 * この抽象クラスを継承・拡張する具象クラスはこのメソッドを実装する必要がある。
	 * パース処理中に発生した例外は{@link ParseResult#failure(Throwable)}でラップすること。
	 * @param in {@link Input}オブジェクト
	 * @return パース結果
	 */
	public abstract ParseResult<T> parse(final Input in);
}
