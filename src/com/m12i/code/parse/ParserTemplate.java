package com.m12i.code.parse;

import java.io.IOException;
import java.io.InputStream;

import com.m12i.code.parse.ParserHelpers.CheckForParsable;
import com.m12i.code.parse.ParserHelpers.FromParsable;
import com.m12i.code.parse.ParserHelpers.ParseOptions;
import com.m12i.code.parse.ParserHelpers.RequireOfParsable;

/**
 * {@link Parser}インターフェースに対しいくつかの補助メソッドを追加した抽象クラス.
 * このクラスはパーサを実装するにあたり頻繁に使用されると思われる各種処理──
 * 文字チェック、空白やコメントのスキップ、引用符で囲われた文字列の読み取りなど──を提供するクラスです。
 * 実装構造上このクラスとこのクラスの実装クラスのすべてのメソッドは同期化されません。
 * @param <T> パースした結果得られるオブジェクトの型
 */
public abstract class ParserTemplate<T> implements Parser<T>, Reader, ParseOptions {
	private Reader code = null;
	private FromParsable fromCode = null;
	private CheckForParsable checkForCode = null;
	private RequireOfParsable requireOfCode = null;
	protected void code(final Reader p) {
		code = p;
		fromCode = ParserHelpers.with(this).from(code());
		checkForCode = ParserHelpers.checkFor(p);
		requireOfCode = ParserHelpers.requireOf(p);
	}
	public final T parse(final Reader p) throws ParseException {
		code(p);
		try {
			return parseMain();
		} catch(UnexpectedException e) {
			throw ParseException.unexpectedError(p, e);
		}
	}
	public T parse(final String s) throws ParseException {
		return parse(new EagerLoadParsable(s));
	}
	public T parse(final InputStream s) throws IOException, ParseException {
		return parse(new LazyLoadParsable(s));
	}
	public T parse(final InputStream s, final String charset) throws IOException, ParseException {
		return parse(new LazyLoadParsable(s, charset));
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
	/**
	 * シングルクオテーション（一重引用符）で囲われた文字列で使用されるエスケープシーケンス・プレフィックスを返す.
	 * このメソッドは{@link ParserTemplate}のテンプレートメソッドから呼び出されます。
	 * 具象クラスを定義する場合このメソッドを実装する必要があります。
	 * パース対象コードの構文がシングルクオテーション文字列やその内部でのエスケープシーケンス使用をサポートしていない場合、
	 * このメソッドは{@literal '\u0000'}（ヌル文字）を返すようにします。
	 * @return エスケープシーケンス・プレフィックス
	 */
	public abstract char escapePrefixInSingleQuotes();
	/**
	 * ダブルクオテーション（二重引用符）で囲われた文字列で使用されるエスケープシーケンス・プレフィックスを返す.
	 * このメソッドは{@link ParserTemplate}のテンプレートメソッドから呼び出されます。
	 * 具象クラスを定義する場合このメソッドを実装する必要があります。
	 * パース対象コードの構文がダブルクオテーション文字列やその内部でのエスケープシーケンス使用をサポートしていない場合、
	 * このメソッドは{@literal '\u0000'}（ヌル文字）を返すようにします。
	 * @return エスケープシーケンス・プレフィックス
	 */
	public abstract char escapePrefixInDoubleQuotes();
	/**
	 * 行コメント開始文字列を返す.
	 * このメソッドは{@link ParserTemplate}のテンプレートメソッドから呼び出されます。
	 * 具象クラスを定義する場合このメソッドを実装する必要があります。
	 * パース対象コードの構文が行コメントをサポートしていない場合、空文字列もしくは{@literal null}を返すようにします。
	 * @return 行コメント開始文字列
	 */
	public abstract String lineCommentStart();
	/**
	 * ブロック・コメント開始文字列を返す.
	 * このメソッドは{@link ParserTemplate}のテンプレートメソッドから呼び出されます。
	 * 具象クラスを定義する場合このメソッドを実装する必要があります。
	 * パース対象コードの構文がブロック・コメントをサポートしていない場合、空文字列もしくは{@literal null}を返すようにします。
	 * @return ブロック・コメント開始文字列
	 */
	public abstract String blockCommentStart();
	/**
	 * ブロック・コメント終了文字列を返す.
	 * このメソッドは{@link ParserTemplate}のテンプレートメソッドから呼び出されます。
	 * 具象クラスを定義する場合このメソッドを実装する必要があります。
	 * @return ブロック・コメント終了文字列
	 */
	public abstract String blockCommentEnd();
	/**
	 * 空白文字列とともにコメントもスキップするかどうかを返す.
	 * このメソッドは{@link ParserTemplate}のテンプレートメソッドから呼び出されます。
	 * 具象クラスを定義する場合このメソッドを実装する必要があります。
	 * @return 空白文字列とともにコメントもスキップするかどうか（{@literal true}：する、{@literal false}：しない）
	 */
	public abstract boolean skipCommentWithSpace();
	/**
	 * 現在文字が空白文字であるかどうかを判定して返す.
	 * このメソッドは{@link ParserTemplate}のテンプレートメソッドから呼び出されます。
	 * デフォルトの実装では、半角スペース（コード32）と同じかそれより小さいコードの文字の場合、空白文字と見做します。
	 * 具象クラスでこの挙動を変えたい場合は、オーバーライドをしてください。
	 * @return 現在文字が空白文字であるかどうか（{@literal true}：である、{@literal false}：でない）
	 */
	protected boolean currentIsSpace() {
		return checkForCode.currentIsSpace();
	}
	/**
	 * 引数で指定された文字列をスキップする.
	 * 引数で指定された文字列を構成する文字を順に取り出して、パース対象の現在文字以降の文字列と比較します。
	 * 双方が一致している間は{@code #next()}でスキップを続けます。
	 * もし1文字でも一致しない文字があった場合、このメソッドは例外をスローします。
	 * @param s スキップ文字列
	 * @throws ParseException スキップ文字列がパース対象の現在文字以降と一致しない場合
	 */
	public void skipWord(String s) throws ParseException {
		fromCode.skipWord(s);
	}
	/**
	 * 引数で指定された文字が現れる前までの文字列を読み取って返す.
	 * 読み取りが完了したとき、読み取り位置は引数で指定された文字のある場所を指します。
	 * 対象文字として複数の文字が指定された場合、そのうちいずれかが現れた時点で読み取りが停止します。
	 * @param cs 対象文字
	 * @return 読み取り結果の文字列
	 * @throws ParseException 
	 */
	public String parseUntil(char... cs) throws ParseException {
		return fromCode.parseUntil(cs);
	}
	/**
	 * アルファベット（A-Za-z）のみで構成された文字列を読み取って返す.
	 * 読み取りが完了したとき、読み取り位置は非アルファベット文字のある場所を指します。
	 * @return 読み取り結果の文字列
	 * @throws ParseException 
	 */
	public String parseAlphabet() throws ParseException {
		return fromCode.parseAlphabet();
	}
	/**
	 * アルファベットと数字（A-Za-z0−9）のみで構成された文字列を読み取って返す.
	 * 読み取りが完了したとき、読み取り位置は非アルファベットかつ非数字の文字のある場所を指します。
	 * @return 読み取り結果の文字列
	 * @throws ParseException 
	 */
	public String parseAlphanum() throws ParseException {
		return fromCode.parseAlphanum();
	}
	/**
	 * ダブルクオテーション（二重引用符）もしくはシングルクオテーション（一重引用符）で囲われた文字列を読み取って返す.
	 * それぞれの引用符で囲われた文字列において使用可能なエスケープシーケンス・プレフィックスは、
	 * {@link #escapePrefixInSingleQuotes()}や{@link #escapePrefixInSingleQuotes()}で定義されます。
	 * これらのメソッドが{@literal '\u0000'}（ヌル文字）を返す場合、
	 * 引用符で囲われた文字列内でエスケープシーケンスの使用はサポートされない状態となります。
	 * @return 引用符で囲われていた文字列（前後の引用符は除去されたもの）
	 * @throws ParseException 構文エラーが発生した場合
	 */
	public String parseQuotedString() throws ParseException {
		return fromCode.parseQuotedString();
	}
	/**
	 * 読み取り位置を次の行の先頭に移動してその行（文字列）を返す.
	 * @return 読み取り位置移動後の行（文字列）
	 * @throws ParseException 
	 */
	public String nextLine() throws ParseException {
		return fromCode.nextLine();
	}
	/**
	 * 空白文字からなる文字列をスキップする.
	 * 現在位置の文字が空白文字と見做される文字でない場合、何もせず即座に処理を終了します。
	 * 現在位置の文字が空白文字と見做される文字である場合、現在位置が空白文字と見做されない文字となるまで、読み取り位置を前進させます。
	 * このメソッドが空白文字と見做すのは、半角スペース（コード32）と同じかそれより小さいコードの文字の場合です。
	 * このメソッドが処理を終えた時、現在文字は空白文字と見做されないいずれかの文字です。
	 * {@link #skipCommentWithSpace()}が{@literal true}を返す場合、このメソッドは空白文字列とともにコメントもスキップします。
	 * @throws ParseException 構文エラーが発生した場合
	 */
	public void skipSpace() throws ParseException {
		fromCode.skipSpace();
	}
	/**
	 * コメント文字列をスキップする.
	 * このメソッドは行コメントおよびブロック・コメントをスキップします。
	 * 行コメントの開始文字列は{@link #lineCommentStart()}で、
	 * ブロック・コメントの開始文字列は{@link #blockCommentStart()}、終了文字列は{@link #blockCommentEnd()}で定義します。
	 * @throws ParseException 構文エラーが発生した場合
	 */
	public void skipComment() throws ParseException {
		fromCode.skipComment();
	}
	/**
	 * 現在文字が引数で指定された文字と一致するか検証した上でその現在文字を返す.
	 * @param c 対象文字
	 * @return 現在文字
	 * @throws ParseException 指定された文字と現在文字が一致しない場合
	 */
	public char currentMustBe(char c) throws ParseException {
		requireOfCode.currentIs(c);
		return c;
	}
	/**
	 * 読み取り位置を1つ前進させたあと現在文字が引数で指定された文字と一致するか検証した上でその現在文字を返す.
	 * @param c 対象文字
	 * @return 現在文字
	 * @throws ParseException 指定された文字と現在文字が一致しない場合
	 */
	public char nextMustBe(char c) throws ParseException {
		requireOfCode.nextIs(c);
		return c;
	}
	/**
	 * {@link #currentMustBe(char)}の反対.
	 * @param c 対象文字
	 * @return 現在文字
	 * @throws ParseException 指定された文字と現在文字が一致した場合
	 */
	public char currentMustNotBe(char c) throws ParseException {
		requireOfCode.currentIsNot(c);
		return current();
	}
	/**
	 * {@link #nextMustBe(char)}の反対.
	 * @param c 対象文字
	 * @return 現在文字
	 * @throws ParseException 指定された文字と現在文字が一致した場合
	 */
	public char nextMustNotBe(char c) throws ParseException {
		requireOfCode.nextIsNot(c);
		return current();
	}
	/**
	 * 現在文字と引数で指定された文字が一致するかどうかを判定する.
	 * @param c 対象文字
	 * @return 判定結果（{@literal true}：一致する、{@literal false}：一致しない）
	 */
	public boolean currentIs(char c) {
		return checkForCode.currentIs(c);
	}
	/**
	 * 読み取り位置を1つ前進させたあと現在文字と引数で指定された文字が一致するかどうかを判定する.
	 * @param c 対象文字
	 * @return 判定結果（{@literal true}：一致する、{@literal false}：一致しない）
	 */
	public boolean nextIs(char c) {
		return checkForCode.nextIs(c);
	}
	/**
	 * {@link #currentIs(char)}の反対.
	 * @param c 対象文字
	 * @return 判定結果（{@literal true}：一致しない、{@literal false}：一致する）
	 */
	public boolean currentIsNot(char c) {
		return checkForCode.currentIsNot(c);
	}
	/**
	 * {@link #nextIs(char)}の反対.
	 * @param c 対象文字
	 * @return 判定結果（{@literal true}：一致しない、{@literal false}：一致する）
	 */
	public boolean nextIsNot(char c) {
		return checkForCode.nextIsNot(c);
	}
	/**
	 * 現在文字の後方に引数で指定された文字列が続くかどうかを判定する.
	 * このメソッドは「先読み」判定を行うものであり、処理中に読み取り位置を前進させません。
	 * 現在文字の後方に続く文字列を単にチェックして結果を返すだけです。
	 * @param s 対象文字列
	 * @return 判定結果（{@literal true}：指定された文字列が続く、{@literal false}：続かない）
	 */
	public boolean currentIsFollowedBy(String s) {
		return checkForCode.currentIsFollowedBy(s);
	}
	/**
	 * {@link #currentIsFollowedBy(String)}の反対.
	 * @param s 対象文字列
	 * @return 判定結果（{@literal true}：指定された文字列が続かない、{@literal false}：続く）
	 */
	public boolean currentIsNotFollowedBy(String s) {
		return checkForCode.currentIsNotFollowedBy(s);
	}
	/**
	 * 現在文字も含めパース対象コードの残りの部分が引数で指定された文字列で始まるかどうかを判定する.
	 * {@link #currentIsFollowedBy(String)}同様にこのメソッドも読み取り位置を前進させずに判定処理を行います。
	 * @param s 対象文字列
	 * @return 判定結果（{@literal true}：指定された文字列で始まる、{@literal false}：始まらない）
	 */
	public boolean remainingCodeStartsWith(String s) {
		return checkForCode.remainingCodeStartsWith(s);
	}
	/**
	 * 現在文字が引数で指定された文字のうちのいずれかと一致するかどうか判定する.
	 * @param cs 対象文字
	 * @return 判定結果（{@literal true}：いずれかに一致する、{@literal false}：いずれにも一致しない）
	 */
	public boolean currentIsAnyOf(char... cs) {
		return checkForCode.currentIsAnyOf(cs);
	}
	/**
	 * {@link #currentIsAnyOf(char...)}の反対.
	 * @param cs 対象文字
	 * @return 判定結果（{@literal true}：いずれにも一致しない、{@literal false}：いずれかに一致する）
	 */
	public boolean currentIsNotAnyOf(char... cs) {
		return checkForCode.currentIsNotAnyOf(cs);
	}
	/**
	 * 文字コード上（バイト表現上）、現在文字が引数で指定された2つの文字の間のいずれかの文字であるかどうか判定する.
	 * 判定ロジックは右のようになる： {@code (c0 <= current() && current() <= c1)}
	 * @param c0 対象文字
	 * @param c1 対象文字
	 * @return 判定結果（{@literal true}：いずれかに一致する、{@literal false}：いずれにも一致しない）
	 */
	public boolean currentIsBetween(char c0, char c1) {
		return checkForCode.currentIsBetween(c0, c1);
	}
	/**
	 * {@link #currentIsBetween(char, char)}の反対.
	 * @param c0 対象文字
	 * @param c1 対象文字
	 * @return 判定結果（{@literal true}：いずれにも一致しない、{@literal false}：いずれかに一致する）
	 */
	public boolean currentIsNotBetween(char c0, char c1) {
		return checkForCode.currentIsNotBetween(c0, c1);
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
	@Override
	public boolean hasReachedEol() {
		return code().hasReachedEol();
	}
	@Override
	public String toString() {
		return code().toString();
	}
}
