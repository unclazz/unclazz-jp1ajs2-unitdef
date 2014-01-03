package com.m12i.code.parse;

public final class ParserHelpers {
	private ParserHelpers() {}
	
	/**
	 * 読み取り対象コードの内容をチェックするための各種メソッドを提供するインターフェース.
	 * このインターフェースのインスタンスは{@link ParserHelpers#checkFor(Parsable)}メソッドから取得できます。
	 */
	public static interface CheckForParsable {
		/**
		 * 現在文字と引数で指定された文字が一致するかどうかを判定する.
		 * @param c 対象文字
		 * @return 判定結果（{@literal true}：一致する、{@literal false}：一致しない）
		 */
		boolean currentIs(char c);
		/**
		 * {@link #currentIs(char)}の反対.
		 * @param c 対象文字
		 * @return 判定結果（{@literal true}：一致しない、{@literal false}：一致する）
		 */
		boolean currentIsNot(char c);
		/**
		 * 読み取り位置を1つ前進させたあと現在文字と引数で指定された文字が一致するかどうかを判定する.
		 * @param c 対象文字
		 * @return 判定結果（{@literal true}：一致する、{@literal false}：一致しない）
		 */
		boolean nextIs(char c);
		/**
		 * {@link #nextIs(char)}の反対.
		 * @param c 対象文字
		 * @return 判定結果（{@literal true}：一致しない、{@literal false}：一致する）
		 */
		boolean nextIsNot(char c);
		/**
		 * 現在文字の後方に引数で指定された文字列が続くかどうかを判定する.
		 * このメソッドは「先読み」判定を行うものであり、処理中に読み取り位置を前進させません。
		 * 現在文字の後方に続く文字列を単にチェックして結果を返すだけです。
		 * @param s 対象文字列
		 * @return 判定結果（{@literal true}：指定された文字列が続く、{@literal false}：続かない）
		 */
		boolean currentIsFollowedBy(String s);
		/**
		 * {@link #currentIsFollowedBy(String)}の反対.
		 * @param s 対象文字列
		 * @return 判定結果（{@literal true}：指定された文字列が続かない、{@literal false}：続く）
		 */
		boolean currentIsNotFollowedBy(String s);
		/**
		 * 現在文字も含めパース対象コードの残りの部分が引数で指定された文字列で始まるかどうかを判定する.
		 * {@link #currentIsFollowedBy(String)}同様にこのメソッドも読み取り位置を前進させずに判定処理を行います。
		 * @param s 対象文字列
		 * @return 判定結果（{@literal true}：指定された文字列で始まる、{@literal false}：始まらない）
		 */
		boolean remainingCodeStartsWith(String s);
		/**
		 * 現在文字が引数で指定された文字のうちのいずれかと一致するかどうか判定する.
		 * @param cs 対象文字
		 * @return 判定結果（{@literal true}：いずれかに一致する、{@literal false}：いずれにも一致しない）
		 */
		boolean currentIsAnyOf(char... cs);
		/**
		 * {@link #currentIsAnyOf(char...)}の反対.
		 * @param cs 対象文字
		 * @return 判定結果（{@literal true}：いずれにも一致しない、{@literal false}：いずれかに一致する）
		 */
		boolean currentIsNotAnyOf(char... cs);
		/**
		 * 文字コード上（バイト表現上）、現在文字が引数で指定された2つの文字の間のいずれかの文字であるかどうか判定する.
		 * 判定ロジックは右のようになる： {@code (c0 <= current() && current() <= c1)}
		 * @param c0 対象文字
		 * @param c1 対象文字
		 * @return 判定結果（{@literal true}：いずれかに一致する、{@literal false}：いずれにも一致しない）
		 */
		boolean currentIsBetween(char c0, char c1);
		/**
		 * {@link #currentIsBetween(char, char)}の反対.
		 * @param c0 対象文字
		 * @param c1 対象文字
		 * @return 判定結果（{@literal true}：いずれにも一致しない、{@literal false}：いずれかに一致する）
		 */
		boolean currentIsNotBetween(char c0, char c1);
		/**
		 * 現在読み取り位置がEOF（ファイル終端）に到達しているかどうか判定する.
		 * @return 判定結果（{@literal true}：到達している、{@literal false}：到達していない）
		 */
		boolean hasReachedEof();
		/**
		 * {@link #hasReachedEof()}の反対.
		 * @return 判定結果（{@literal true}：到達していない、{@literal false}：到達している）
		 */
		boolean hasNotReachedEof();
		/**
		 * 現在読み取り位置が行頭にあるかどうかを判定する.
		 * @return 判定結果（{@literal true}：行頭にある、{@literal false}：行頭にない）
		 */
		boolean currentIsLineHead();
		/**
		 * {@link #currentIsLineHead()}の反対.
		 * @return 判定結果（{@literal true}：行頭にない、{@literal false}：行頭にある）
		 */
		boolean currentIsNotLineHead();
		/**
		 * 現在読み取り位置が行末にあるかどうかを判定する.
		 * @return 判定結果（{@literal true}：行末にある、{@literal false}：行末にない）
		 */
		boolean currentIsLineTail();
		/**
		 * {@link #currentIsLineTail()}の反対.
		 * @return 判定結果（{@literal true}：行末にある、{@literal false}：行末にない）
		 */
		boolean currentIsNotLineTail();
		/**
		 * 現在文字が空白文字であるかどうかを判定して返す.
		 * デフォルトの実装では、半角スペース（コード32）と同じかそれより小さいコードの文字の場合、空白文字と見做します。
		 * @return 現在文字が空白文字であるかどうか（{@literal true}：である、{@literal false}：でない）
		 */
		boolean currentIsSpace();
	}

	/**
	 * 読み取り対象コードの内容をチェックし、結果がNGの場合例外をスローするための各種メソッドを提供するインターフェース.
	 * このインターフェースのインスタンスは{@link ParserHelpers#requireOf(Parsable)}メソッドから取得できます。
	 * このインターフェースのメソッドは、{@link CheckForParsable}インターフェースと同じロジックで判定を行い、
	 * 結果がNG（{@literal false}）の場合は{@link ParseException}例外をスローします。
	 */
	public static interface RequireOfParsable {
		boolean currentIs(char c) throws ParseException;
		boolean currentIsNot(char c) throws ParseException;
		boolean nextIs(char c) throws ParseException;
		boolean nextIsNot(char c) throws ParseException;
		boolean currentIsFollowedBy(String s) throws ParseException;
		boolean currentIsNotFollowedBy(String s) throws ParseException;
		boolean remainingCodeStartsWith(String s) throws ParseException;
		boolean currentIsAnyOf(char... cs) throws ParseException;
		boolean currentIsNotAnyOf(char... cs) throws ParseException;
		boolean currentIsBetween(char c0, char c1) throws ParseException;
		boolean currentIsNotBetween(char c0, char c1) throws ParseException;
		boolean hasReachedEof() throws ParseException;
		boolean hasNotReachedEof() throws ParseException;
		boolean currentIsLineHead() throws ParseException;
		boolean currentIsNotLineHead() throws ParseException;
		boolean currentIsLineTail() throws ParseException;
		boolean currentIsNotLineTail() throws ParseException;
		boolean currentIsSpace() throws ParseException;
	}
	
	/**
	 * 読み取りロジックを制御する各種オプションを定義するインターフェース.
	 */
	public static interface ParseOptions {
		/**
		 * シングルクオテーション（一重引用符）で囲われた文字列で使用されるエスケープシーケンス・プレフィックスを返す.
		 * このメソッドは{@link ParserTemplate<T>}のテンプレートメソッドから呼び出されます。
		 * 具象クラスを定義する場合このメソッドを実装する必要があります。
		 * パース対象コードの構文がシングルクオテーション文字列やその内部でのエスケープシーケンス使用をサポートしていない場合、
		 * このメソッドは{@literal '\u0000'}（ヌル文字）を返すようにします。
		 * @return エスケープシーケンス・プレフィックス
		 */
		char escapePrefixInSingleQuotes();
		/**
		 * ダブルクオテーション（二重引用符）で囲われた文字列で使用されるエスケープシーケンス・プレフィックスを返す.
		 * このメソッドは{@link ParserTemplate<T>}のテンプレートメソッドから呼び出されます。
		 * 具象クラスを定義する場合このメソッドを実装する必要があります。
		 * パース対象コードの構文がダブルクオテーション文字列やその内部でのエスケープシーケンス使用をサポートしていない場合、
		 * このメソッドは{@literal '\u0000'}（ヌル文字）を返すようにします。
		 * @return エスケープシーケンス・プレフィックス
		 */
		char escapePrefixInDoubleQuotes();
		/**
		 * 行コメント開始文字列を返す.
		 * このメソッドは{@link ParserTemplate<T>}のテンプレートメソッドから呼び出されます。
		 * 具象クラスを定義する場合このメソッドを実装する必要があります。
		 * パース対象コードの構文が行コメントをサポートしていない場合、空文字列もしくは{@link null}を返すようにします。
		 * @return 行コメント開始文字列
		 */
		String lineCommentStart();
		/**
		 * ブロック・コメント開始文字列を返す.
		 * このメソッドは{@link ParserTemplate<T>}のテンプレートメソッドから呼び出されます。
		 * 具象クラスを定義する場合このメソッドを実装する必要があります。
		 * パース対象コードの構文がブロック・コメントをサポートしていない場合、空文字列もしくは{@link null}を返すようにします。
		 * @return ブロック・コメント開始文字列
		 */
		String blockCommentStart();
		/**
		 * ブロック・コメント終了文字列を返す.
		 * このメソッドは{@link ParserTemplate<T>}のテンプレートメソッドから呼び出されます。
		 * 具象クラスを定義する場合このメソッドを実装する必要があります。
		 * @return ブロック・コメント終了文字列
		 */
		String blockCommentEnd();
		/**
		 * 空白文字列とともにコメントもスキップするかどうかを返す.
		 * このメソッドは{@link ParserTemplate<T>}のテンプレートメソッドから呼び出されます。
		 * 具象クラスを定義する場合このメソッドを実装する必要があります。
		 * @return 空白文字列とともにコメントもスキップするかどうか（{@literal true}：する、{@literal false}：しない）
		 */
		boolean skipCommentWithSpace();
	}
	
	public static interface FromParsableWithOptions {
		FromParsable from(Parsable p);
	}
	
	/**
	 * パース・オプション（読み取りロジックを制御するオプション）を引数にとり、同オプションを使用してパース処理を行うためのオブジェクトを返す.
	 * デフォルトのパース・オプションを使用してパースを行う場合は、{@link ParserHelpers#from(Parsable)}を使用してください。
	 * @param o パース・オプション
	 * @return 指定されたオプションを使用してパース処理を行うためのオブジェクト
	 */
	public static FromParsableWithOptions with(final ParseOptions o) {
		return new FromParsableWithOptions() {
			@Override
			public FromParsable from(final Parsable p) {
				return new DefaultFromParsable(p, o);
			}
		};
	}

	/**
	 * 各種パース処理メソッドを提供するインターフェース.
	 * デフォルトのパース・オプションを使用してパースを行う場合は、{@link ParserHelpers#from(Parsable)}メソッドでこのインターフェースのインスタンスを取得できます。
	 * 独自のオプションを使用してパースを行う場合は、{@link ParserHelpers#with(ParseOptions)}メソッドで{@link FromParsableWithOptions}オブジェクトを取得し、
	 * 同オブジェクトの{@link FromParsableWithOptions#from(Parsable)}メソッドを呼び出すことで、このインターフェースのインスタンスを取得できます。
	 */
	public static interface FromParsable {
		void skipWord(String s) throws ParseException;
		String parseUntil(char... cs) throws ParseException;
		String parseAlphabet() throws ParseException;
		String parseAlphanum() throws ParseException;
		String parseQuotedString() throws ParseException;
		void skipSpace() throws ParseException;
		void skipComment() throws ParseException;
		String nextLine() throws ParseException;
	}
	
	/**
	 * パース・オプションのデフォルト値を提供する{@link ParseOptions}インターフェースの実装クラス.
	 */
	public static final ParseOptions DEFAULT_OPTIONS = new ParseOptions() {
		@Override
		public boolean skipCommentWithSpace() {
			return true;
		}
		@Override
		public String lineCommentStart() {
			return "//";
		}
		@Override
		public char escapePrefixInSingleQuotes() {
			return '\\';
		}
		@Override
		public char escapePrefixInDoubleQuotes() {
			return '\\';
		}
		@Override
		public String blockCommentStart() {
			return "/*";
		}
		@Override
		public String blockCommentEnd() {
			return "*/";
		}
	};
	
	/**
	 * {@link FromParsable}インターフェースのデフォルト実装クラス.
	 */
	public static class DefaultFromParsable implements FromParsable {
		private Parsable p;
		private ParseOptions o = DEFAULT_OPTIONS;
		private DefaultFromParsable(Parsable p, ParseOptions o) {
			parsable(p);
			options(o);
		}
		/**
		 * 読み取り処理対象を設定する.
		 * @param p 読み取り処理対象
		 */
		protected void parsable(Parsable p) {
			this.p = p;
		}
		/**
		 * 読み取り処理で使用するオプションを設定する.
		 * @param o パース・オプション
		 */
		protected void options(ParseOptions o) {
			this.o = o == null ? DEFAULT_OPTIONS : o;
		}
		/**
		 * @return 読み取り処理対象
		 */
		protected Parsable parsable() {
			return p;
		}
		/**
		 * @return パース・オプション
		 */
		protected ParseOptions options() {
			return o;
		}
		@Override
		public void skipWord(String s) throws ParseException {
			for(int i = 0; i < s.length(); i ++) {
				requireOf(p).currentIs(s.charAt(i));
				p.next();
			}
		}
		@Override
		public String parseUntil(char... cs) {
			final StringBuilder sb = new StringBuilder();
			while(! p.hasReachedEof() && checkFor(p).currentIsNotAnyOf(cs)) {
				sb.append(p.current());
				p.next();
			}
			return sb.toString();
		}
		@Override
		public String parseAlphabet() {
			final StringBuilder sb = new StringBuilder();
			while(! p.hasReachedEof() 
					&& (checkFor(p).currentIsBetween('a', 'z')
							|| checkFor(p).currentIsBetween('A', 'Z'))) {
				sb.append(p.current());
				p.next();
			}
			return sb.toString();
		}

		@Override
		public String parseAlphanum() {
			final StringBuilder sb = new StringBuilder();
			while(! p.hasReachedEof() 
					&& (checkFor(p).currentIsBetween('a', 'z')
							|| checkFor(p).currentIsBetween('A', 'Z')
							|| checkFor(p).currentIsBetween('0', '9'))) {
				sb.append(p.current());
				p.next();
			}
			return sb.toString();
		}

		@Override
		public String parseQuotedString() throws ParseException {
			// 現在文字が二重引用符もしくは一重引用符でない場合は構文エラー
			if(checkFor(p).currentIsNotAnyOf('"', '\'')){
				throw ParseException.syntaxError(p);
			}
			
			final StringBuilder sb = new StringBuilder();
			final char quote = p.current();
			final char escapePrefix = quote == '"' ? o.escapePrefixInDoubleQuotes() : o.escapePrefixInSingleQuotes();
			final boolean escapeEnabled = quote != '\u0000';
			p.next();
			
			// EOFに到達するまで繰り返す
			while (! p.hasReachedEof()) {
				// 現在文字が引用符である場合は読み取りは終了
				if(checkFor(p).currentIs(quote)) {
					// 引用符の次の位置に読み取り位置を進める
					p.next();
					// ここまで読み取ってきた結果を返す
					return sb.toString();
				}
				// エスケープシーケンス使用がサポートされており、現在文字がエスケープシーケンス・プレフィックスである場合
				if (escapeEnabled && checkFor(p).currentIs(escapePrefix)) {
					// 読み取り位置を進めた上で現在文字を取得
					sb.append(p.next());
				}
				// それ以外の場合
				else {
					// 現在文字を取得
					sb.append(p.current());
				}
				// 次に進む
				p.next();
			}
			
			// 終了側の引用符が登場しなかった場合は構文エラー
			throw ParseException.syntaxError(p);
		}
		@Override
		public void skipSpace() throws ParseException {
			final CheckForParsable check = checkFor(p);
			// EOFに到達するまで繰り返す
			while (!p.hasReachedEof()) {

				// 半角スペース（コード32）と同じかそれより小さいコードの文字の場合
				if (check.currentIsSpace()) {
					// ホワイトスペースとみなしてスキップ
					p.next();
					
				} 
				// 空白文字とともにコメントスキップする設定が有効であり、かつ現在文字および続く文字列がコメント開始文字列である場合
				else if (o.skipCommentWithSpace() 
						&& (check.remainingCodeStartsWith(o.lineCommentStart()) 
								|| check.remainingCodeStartsWith(o.blockCommentStart()))) {

					// コメント開始とみなして行コメントのスキップ
					skipComment();
				} 
				// それ以外の文字の場合
				else {
					break;
				}
			}
		}
		@Override
		public void skipComment() throws ParseException {
			final CheckForParsable check = checkFor(p);
			// 現在文字および続く文字列が行コメント開始文字列である場合
			if(check.remainingCodeStartsWith(o.lineCommentStart())) {
				nextLine();
			}
			// 現在文字および続く文字列がブロックコメント開始文字列である場合
			else if(check.remainingCodeStartsWith(o.blockCommentStart())) {
				// コメント開始文字列をスキップ
				skipWord(o.blockCommentStart());
				
				// EOFに到達するまで繰り返す。
				while (!p.hasReachedEof()) {

					// 現在文字および続く文字列がコメント終了文字列である場合
					if (check.remainingCodeStartsWith(o.blockCommentEnd())) {
						// コメント終了文字列をスキップ。
						skipWord(o.blockCommentEnd());
						// コメントは終了、内側ループを抜ける。
						return;
					}
					// それ以外の場合、
					else {
						// 現在位置を前進させる。
						p.next();
					}
				}
				throw ParseException.syntaxError(p);
			}
		}
		@Override
		public String nextLine() {
			final int l = p.lineNo();
			while(! p.hasReachedEof() && l == p.lineNo()) {
				p.next();
			}
			return p.line();
		}
	}
	
	/**
	 * {@link CheckForParsable}インターフェースのデフォルト実装クラス.
	 */
	public static class DefaultCheckForParsable implements CheckForParsable {
		private Parsable p;
		private DefaultCheckForParsable(Parsable p) {
			parsable(p);
		}
		/**
		 * 読み取り処理対象を設定する.
		 * @param p 読み取り処理対象
		 */
		protected void parsable(Parsable p) {
			this.p = p;
		}
		/**
		 * @return 読み取り処理対象
		 */
		protected Parsable parsable() {
			return p;
		}
		@Override
		public boolean currentIs(char c) {
			return p.current() == c;
		}
		@Override
		public boolean currentIsNot(char c) {
			return !currentIs(c);
		}
		@Override
		public boolean nextIs(char c) {
			p.next();
			return p.current() == c;
		}
		@Override
		public boolean nextIsNot(char c) {
			p.next();
			return ! currentIs(c);
		}
		@Override
		public boolean currentIsFollowedBy(String s) {
			return (s == null || s.length() == 0 || p.line() == null) ? false : p.line().substring(p.columnNo()).startsWith(s);
		}
		@Override
		public boolean currentIsNotFollowedBy(String s) {
			return (s == null || s.length() == 0 || p.line() == null) ? true : !(p.line().substring(p.columnNo()).startsWith(s));
		}
		@Override
		public boolean remainingCodeStartsWith(String s) {
			return (s == null || s.length() == 0) ? false : (currentIs(s.charAt(0)) && (s.length() == 1 || currentIsFollowedBy(s.substring(1))));
		}
		@Override
		public boolean currentIsAnyOf(char... cs) {
			for(final char c : cs) {
				if(currentIs(c)) {
					return true;
				}
			}
			return false;
		}
		@Override
		public boolean currentIsNotAnyOf(char... cs) {
			return ! currentIsAnyOf(cs);
		}
		@Override
		public boolean currentIsBetween(char c0, char c1) {
			return (c0 <= c1) ? (c0 <= p.current() && p.current() <= c1) : (c1 <= p.current() && p.current() <= c0);
		}
		@Override
		public boolean currentIsNotBetween(char c0, char c1) {
			return ! currentIsBetween(c0, c1);
		}
		@Override
		public boolean hasReachedEof() {
			return p.hasReachedEof();
		}
		@Override
		public boolean hasNotReachedEof() {
			return !p.hasReachedEof();
		}
		@Override
		public boolean currentIsLineHead() {
			return hasNotReachedEof() && p.columnNo() == 1;
		}
		@Override
		public boolean currentIsNotLineHead() {
			return !currentIsLineHead();
		}
		@Override
		public boolean currentIsLineTail() {
			return hasNotReachedEof() && p.line().length() == p.columnNo();
		}
		@Override
		public boolean currentIsNotLineTail() {
			return !currentIsLineTail();
		}
		@Override
		public boolean currentIsSpace() {
			return p.current() <= ' ';
		}
	}
	
	/**
	 * 読み取り対象を引数にとり、同コードの内容をチェックするためのオブジェクトを返す.
	 * @param p 読み取り対象
	 * @return 読み取り対象コード内容チェックのための各種メソッドを提供するオブジェクト
	 */
	public static CheckForParsable checkFor(Parsable p) {
		return new DefaultCheckForParsable(p);
	}
	
	/**
	 * 読み取り対象を引数にとり、同コードの必須条件チェックをするためのオブジェクトを返す.
	 * @param p 読み取り対象
	 * @return 読み取り対象コードの必須条件チェックのための各種メソッドを提供するオブジェクト
	 */
	public static RequireOfParsable requireOf(final Parsable p) {
		return new RequireOfParsable() {
			private final CheckForParsable check = new DefaultCheckForParsable(p);
			private boolean returnIfTrue(boolean r) throws ParseException {
				if(r) {
					return r;
				} else {
					throw ParseException.syntaxError(p);
				}
			}
			@Override
			public boolean remainingCodeStartsWith(String s) throws ParseException {
				return returnIfTrue(check.remainingCodeStartsWith(s));
			}
			@Override
			public boolean nextIsNot(char c) throws ParseException {
				return returnIfTrue(check.nextIsNot(c));
			}
			@Override
			public boolean nextIs(char c) throws ParseException {
				return returnIfTrue(check.nextIs(c));
			}
			@Override
			public boolean hasReachedEof() throws ParseException {
				return returnIfTrue(check.hasReachedEof());
			}
			@Override
			public boolean hasNotReachedEof() throws ParseException {
				return returnIfTrue(check.hasNotReachedEof());
			}
			@Override
			public boolean currentIsSpace() throws ParseException {
				return returnIfTrue(check.currentIsSpace());
			}
			@Override
			public boolean currentIsNotLineTail() throws ParseException {
				return returnIfTrue(check.currentIsNotLineTail());
			}
			@Override
			public boolean currentIsNotLineHead() throws ParseException {
				return returnIfTrue(check.currentIsNotLineHead());
			}
			@Override
			public boolean currentIsNotFollowedBy(String s) throws ParseException {
				return returnIfTrue(check.currentIsNotFollowedBy(s));
			}
			@Override
			public boolean currentIsNotBetween(char c0, char c1) throws ParseException {
				return returnIfTrue(check.currentIsNotBetween(c0, c1));
			}
			@Override
			public boolean currentIsNotAnyOf(char... cs) throws ParseException {
				return returnIfTrue(check.currentIsNotAnyOf(cs));
			}
			@Override
			public boolean currentIsNot(char c) throws ParseException {
				return returnIfTrue(check.currentIsNot(c));
			}
			@Override
			public boolean currentIsLineTail() throws ParseException {
				return returnIfTrue(check.currentIsLineTail());
			}
			@Override
			public boolean currentIsLineHead() throws ParseException {
				return returnIfTrue(check.currentIsLineHead());
			}
			@Override
			public boolean currentIsFollowedBy(String s) throws ParseException {
				return returnIfTrue(check.currentIsFollowedBy(s));
			}
			@Override
			public boolean currentIsBetween(char c0, char c1) throws ParseException {
				return returnIfTrue(check.currentIsBetween(c0, c1));
			}
			@Override
			public boolean currentIsAnyOf(char... cs) throws ParseException {
				return returnIfTrue(check.currentIsAnyOf(cs));
			}
			@Override
			public boolean currentIs(char c) throws ParseException {
				return returnIfTrue(check.currentIs(c));
			}
		};
	}
	
	/**
	 * 読み取り対象を引数にとり、各種パース処理メソッドを提供するオブジェクトを返す.
	 * @param p 読み取り対象
	 * @return 各種パース処理メソッドを提供するオブジェクト
	 */
	public static FromParsable from(Parsable p) {
		return with(DEFAULT_OPTIONS).from(p);
	}
	
}
