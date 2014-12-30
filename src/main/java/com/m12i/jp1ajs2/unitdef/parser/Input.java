package com.m12i.jp1ajs2.unitdef.parser;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.Charset;

/**
 * 入力データをあらわすオブジェクト.
 * {@link InputStream}や{@link File}を使って初期化を行った場合は
 * EOFに到達した時点で{@link InputStream#close()}を呼び出してストリームをクローズする。
 * 実装の性質上、パース処理中に{@link IOException}が発生する可能性がある。
 * この例外が発生した場合、ストリームのクローズを試みた上で、
 * 例外を{@link ParseException}でラップしてスローする。
 */
public final class Input {
	/**
	 * 入力データ（文字列やリーダー）へのアクセスを汎化するためのインターフェース.
	 */
	private static interface WrappedSequence {
		/**
		 * 次の1文字を読み込む.
		 * 文字列の終わりあるいはEOFに到達すると{@code -1}を返す。
		 * @return 次の1文字
		 * @throws IOException 読み取り中にエラーが発生した場合
		 */
		int read() throws IOException;
		/**
		 * ストリームをクローズする.
		 * 入力データが文字列である場合は事実上なにもしない。
		 * @throws IOException クローズ中にエラーが発生した場合
		 */
		void close() throws IOException;
		/**
		 * 現在読み取り位置にマークをする.
		 * {@link #reset()}メソッドを呼び出すことで読み取り位置がこのマークした位置に戻る。
		 * ただし引数で指定された数値は{@link InputStream#mark(int)}に渡される。
		 * @param readAheadLimit 先読み上限
		 * @throws IOException マーク設定中に例外が発生した場合
		 */
		void mark(int readAheadLimit) throws IOException;
		/**
		 * 事前にマークした位置に読み取り位置を戻す.
		 * @throws IOException 読み取り位置の変更中にエラーが発生した場合
		 */
		void reset() throws IOException;
	}
	
	/**
	 * 文字列をラップする{@link WrappedSequence}の実装.
	 */
	private static final class WrappedString implements WrappedSequence {
		private int pos = 0;
		private int mem = 0;
		private String inner;
		public WrappedString(String s) {
			this.inner = s;
		}
		@Override
		public int read() {
			return (inner.length() > pos) ? inner.charAt(pos ++) : -1;
		}
		@Override
		public void close() {
			// Do nothing.
		}
		@Override
		public void mark(int i) {
			mem = pos;
		}
		@Override
		public void reset() {
			pos = mem;
		}
	}
	
	/**
	 * リーダーをラップする{@link WrappedSequence}の実装.
	 */
	private static final class WrappedBufferedReader implements WrappedSequence {
		private final BufferedReader inner;
		public WrappedBufferedReader(BufferedReader br) {
			this.inner = br;
		}
		@Override
		public int read() throws IOException {
			return inner.read();
		}
		@Override
		public void close() throws IOException {
			inner.close();
		}
		@Override
		public void mark(int readAheadLimit) throws IOException {
			inner.mark(readAheadLimit);
		}
		@Override
		public void reset() throws IOException {
			inner.reset();
		}
	}

	private static final int CR = (int) '\r';
	private static final int LF = (int) '\n';
	private static final char NULL = '\u0000';
	private static final String EMPTY = "";
	
	private final StringBuilder lineBuff = new StringBuilder();
	private final WrappedSequence reader;

	private int position = -1;
	private char current = NULL;
	private boolean eof = false;
	private boolean closed = false;
	private int lineNo = 0;
	
	/**
	 * 文字列を使って初期化を行う.
	 * @param s 文字列
	 * @return インスタンス
	 * @throws InputExeption 初期化中にエラーが発生した場合
	 */
	public static Input fromString(final String s) throws InputExeption {
		return new Input(s);
	}
	
	/**
	 * 入力ストリームを使って初期化を行う.
	 * キャラクターセットはランタイムのデフォルトを使用する。
	 * @param s 入力ストリーム
	 * @return インスタンス
	 * @throws InputExeption 初期化中にエラーが発生した場合
	 */
	public static Input fromStream(final InputStream s) throws InputExeption {
		return fromStream(s, Charset.defaultCharset());
	}
	
	/**
	 * 入力ストリームを使って初期化を行う.
	 * @param s 入力ストリーム
	 * @param charset キャラクターセット
	 * @return インスタンス
	 * @throws InputExeption 初期化中にエラーが発生した場合
	 */
	public static Input fromStream(final InputStream s, final Charset charset) throws InputExeption {
		try {
			return new Input(s, charset);
		} catch (final IOException e) {
			throw new InputExeption(e);
		}
	}
	
	/**
	 * コンストラクタ.
	 * @param stream 入力ストリーム
	 * @param charset キャラクターセット
	 * @throws IOException 初期化中にIOエラーが発生した場合
	 * @throws InputExeption 初期化中にエラーが発生した場合
	 */
	private Input(final InputStream stream, final Charset charset)
			throws IOException, InputExeption {
		reader = new WrappedBufferedReader(new BufferedReader(new InputStreamReader(stream, charset)));
		next();
	}
	
	/**
	 * ファイルを使って初期化を行う.
	 * キャラクターセットはランタイムのデフォルトを使用する。
	 * @param f ファイル
	 * @return インスタンス
	 * @throws InputExeption 初期化中にエラーが発生した場合
	 */
	public static Input fromFile(final File f) throws InputExeption {
		return fromFile(f, Charset.defaultCharset());
	}
	
	/**
	 * ファイルを使って初期化を行う.
	 * @param f ファイル
	 * @param charset キャラクターセット
	 * @return インスタンス
	 * @throws InputExeption 初期化中にエラーが発生した場合
	 */
	public static Input fromFile(final File f, final Charset charset) throws InputExeption {
		try {
			return new Input(new FileInputStream(f), charset);
		} catch (final FileNotFoundException e) {
			throw new InputExeption(e);
		} catch (final IOException e) {
			throw new InputExeption(e);
		}
	}
	
	/**
	 * コンストラクタ.
	 * @param s 文字列
	 * @throws InputExeption 初期化中にエラーが発生した場合
	 */
	private Input(final String s) throws InputExeption {
		reader = new WrappedString(s);
		next();
	}
	
	/**
	 * 現在読み取り位置の文字を返す.
	 * @return 現在読み取り位置の文字
	 */
	public char current() {
		return current;
	}
	
	/**
	 * 現在読み取り位置のカラム数を返す.
	 * カラム数は添字に{@code 1}を加算したもの。
	 * すなわち現在読み取り位置が行頭の文字の上にある場合このメソッドは{@code 1}を返す。
	 * @return カラム数
	 */
	public int columnNo() {
		return position + 1;
	}
	
	/**
	 * 現在の行を返す.
	 * このメソッドが返す文字列に行末の改行文字は含まれない。
	 * @return 行文字列
	 */
	public String line() {
		return eof ? null : lineBuff.toString().replaceAll("(\r\n|\r|\n)$", EMPTY);
	}
	
	/**
	 * 現在読み取り位置の行数を返す.
	 * @return 行数
	 */
	public int lineNo() {
		return lineNo;
	}
	
	/**
	 * 現在読み取り位置以降の行末までの文字列を返す.
	 * このメソッドが返す文字列には改行文字が含まれる。
	 * @return 現在読み取り位置以降の行末までの文字列
	 */
	public String rest() {
		return lineBuff.substring(position);
	}
	
	/**
	 * 現在読み取り位置に引数で指定された文字列があるかどうか判定する（前方一致判定する）.
	 * このメソッドの判定結果は{@code #rest().startsWith(String)}と同じ結果となる。
	 * @param prefix 前方一致判定に使用される文字列
	 * @return 判定結果
	 */
	public boolean restStartsWith(final String prefix) {
		return lineBuff.substring(position).startsWith(prefix);
	}
	
	/**
	 * EOFに到達済みかどうかを判定して返す.
	 * @return 判定結果
	 */
	public boolean reachedEof() {
		return eof;
	}
	
	/**
	 * {@link #reachedEof()}の反対.
	 * @return 判定結果
	 */
	public boolean unlessEof() {
		return ! eof;
	}
	
	/**
	 * 行末に到達済みかどうかを判定して返す.
	 * @return 判定結果
	 */
	public boolean reachedEol() {
		return eof || current == CR || current == LF;
	}
	
	/**
	 * {@link #reachedEol()}の反対.
	 * @return 判定結果
	 */
	public boolean unlessEol() {
		return ! reachedEol();
	}
	
	/**
	 * 読み取り位置を1つ前進させその位置にある文字を返す.
	 * EOFに到達した場合は{@code '\u0000'}を返す。
	 * @return 読み取り位置の文字
	 * @throws InputExeption 入力データ読み取り中にエラーが発生した場合
	 */
	public char next() throws InputExeption {
		if (eof) {
			// EOF到達後ならすぐに現在文字（ヌル文字）を返す
			return current;
		} else {
			// EOF到達前なら次の文字を取得する処理に入る
			// まず現在位置をインクリメント
			position += 1;
			// 現在位置が行バッファの末尾より後方にあるかチェック
			if (position >= lineBuff.length()) {
				// ストリームの状態をチェック
				if (closed) {
					// すでにストリームが閉じられているならEOF
					eof = true;
					current = NULL;
					position = 0;
				} else {
					// まだオープン状態なら次の行をロードする
					loadLine();
				}
			}
			// EOFの判定を実施
			if (eof) {
				// EOF到達済みの場合
				// 現在文字（ヌル文字）を返す
				return current;
			} else {
				// EOF到達前の場合
				// 現在文字に新しく取得した文字を設定
				current = lineBuff.charAt(position);
				// 現在文字を返す
				return current;
			}
		}
	}
	
	private void loadLine() throws InputExeption {
		try {
			// 現在位置を初期化
			position = 0;
			lineNo += 1;
			// 行バッファをクリアする
			lineBuff.setLength(0);
			// 繰り返し処理
			while (!closed) {
				// 次の文字を取得
				final int c0 = reader.read();
				// 文字のコード値が-1であるかどうか判定
				if (c0 == -1) {
					// 文字のコード値が−1ならストリームの終了
					// ストリームを即座にクローズする
					closed = true;
					reader.close();
					// バッファが空ならEOFでもある
					eof = lineBuff.length() == 0;
					if (eof) {
						current = NULL;
					}
					return;
				}
				
				// 読み取った文字をバッファに格納
				lineBuff.append((char) c0);
				// 文字がLF・CRであるかどうか判定
				if (c0 == LF) {
					// LFであればただちに読み取りを完了
					return;
				} else if (c0 == CR) {
					// CRである場合はまず現在位置にマークを設定
					reader.mark(1);
					// 次の文字を取得
					final int c1 = reader.read();
					// 文字のコード値を判定
					if (c1 == -1) {
						// 文字のコード値が−1ならストリームの終了
						// ストリームを即座にクローズする
						closed = true;
						reader.close();
					} else if (c1 == LF) {
						// LFであればそれもバッファに格納
						lineBuff.append((char) c1);
					} else {
						// そうでなければ読み取り位置を最前マークした位置に戻す
						reader.reset();
					}
					// 読み取りを完了
					return;
				}
			}
		} catch (IOException e0) {
			// 遅延読み込みのためI/Oエラーが発生した場合は実行時例外でラップする
			try {
				// I/Oエラーを実行時例外でラップする以上、リソースのクローズの責任も持つ
				reader.close();
			} catch (IOException e1) {
				// クローズ時のエラーも実行時例外でラップする
				// ＊Java6サポート対象としたいので addSuppressed(Throwable) メソッドは使用しない
				throw new InputExeption(this, e1);
			}
			throw new InputExeption(this, e0);
		}
	}
}
