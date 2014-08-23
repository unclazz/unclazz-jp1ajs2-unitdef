package com.m12i.code.parse;

import java.io.IOException;
import java.io.InputStream;

/**
 * {@link Parser}インターフェースに対しいくつかの補助メソッドを追加した抽象クラス.
 * このクラスはパーサを実装するにあたり最低限必要と思われる機能──読み取り対象の{@link Reader}オブジェクトに簡易にアクセスするための委譲メソッドと
 * {@link Parser#parse(Reader)}の利便性を向上させるための各種多重定義メソッド──を提供するクラスです。
 * @param <T> パースした結果得られるオブジェクトの型
 */
public abstract class MinimalParserTemplate<T> implements Parser<T>, Reader {
	private Reader code = null;
	protected void code(final Reader p) {
		code = p;
	}
	public final T parse(final Reader p) throws ParseException {
		code(p);
		return parseMain();
	}
	public T parse(final String s) throws ParseException {
		return parse(new EagerReader(s));
	}
	public T parse(final InputStream s) throws IOException, ParseException {
		return parse(new LazyReader(s));
	}
	public T parse(final InputStream s, final String charset) throws IOException, ParseException {
		return parse(new LazyReader(s, charset));
	}
	/**
	 * 対象コードをパースして返す.
	 * このメソッドは{@link #parse(Reader)}から呼び出されます。
	 * {@link ParserTemplate<T>}を具象クラスとして実装する場合、このメソッドがパース処理のエントリー・ポイントとなります。
	 * {@link #parse(Reader)}に引数として渡された{@link Reader}インスタンスには、
	 * {@link #code()}メソッドを通じてアクセスできます。
	 * @return 読み取り結果
	 * @throws ParseException 構文エラーが発生した場合、もしくは、読み取り中に予期せぬエラーが発生した場合
	 */
	protected abstract T parseMain() throws ParseException;
	/**
	 * パース対象を返す.
	 * このメソッドは{@link ParserTemplate<T>}のテンプレートメソッドから呼び出されます。
	 * @return パース対象
	 */
	protected Reader code() {
		return code;
	}
	@Override
	public char current() {
		return code().current();
	}
	@Override
	public char next() {
		return code().next();
	}
	@Override
	public int lineNo() {
		return code().lineNo();
	}
	@Override
	public int columnNo() {
		return code().columnNo();
	}
	@Override
	public String line() {
		return code().line();
	}
	@Override
	public boolean hasReachedEof() {
		return code().hasReachedEof();
	}
}
