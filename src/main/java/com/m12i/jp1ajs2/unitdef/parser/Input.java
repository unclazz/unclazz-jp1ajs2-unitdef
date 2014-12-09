package com.m12i.jp1ajs2.unitdef.parser;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.Charset;

/**
 * {@link Input}の実装クラス.
 * 初期化の際に引数として渡された{@link InputStream}を内部で保持して遅延読み込みを行い、
 * EOFに到達した時点で{@link InputStream#close()}を呼び出してストリームをクローズします。
 * 実装の性質上、パース処理中に{@link IOException}が発生する可能性があります。
 * この例外が発生した場合、{@link LazyReader}は対象のストリームのクローズを試みた上で、
 * {@link IOException}を{@link ParseError}でラップしてスローします。
 */
public final class Input {
	
	private static interface WrapperSequence {
		int read() throws IOException;
		void close() throws IOException;
		void mark(int i) throws IOException;
		void reset() throws IOException;
	}
	
	private static final class WrappedString implements WrapperSequence {
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
	
	private static final class WrappedBufferedReader implements WrapperSequence {
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

	private static final int cr = (int) '\r';
	private static final int lf = (int) '\n';
	
	private final StringBuilder lineBuff = new StringBuilder();
	private final WrapperSequence reader;

	private int position = -1;
	private char current = '\u0000';
	private boolean endOfFile = false;
	private boolean closed = false;
	private int lineNo = 0;
	
	public static Input fromString(final String s) {
		return new Input(s);
	}
	
	public static Input fromStream(final InputStream s) throws IOException {
		return new Input(s, Charset.forName("utf-8"));
	}
	
	public static Input fromStream(final InputStream s, final String charset) throws IOException {
		return new Input(s, Charset.forName(charset));
	}
	
	public static Input fromStream(final InputStream s, final Charset charset) throws IOException {
		return new Input(s, charset);
	}
		
	private Input(final InputStream stream, final Charset charset)
			throws IOException {
		reader = new WrappedBufferedReader(new BufferedReader(new InputStreamReader(stream, charset)));
		next();
	}
	
	private Input(final String s) {
		reader = new WrappedString(s);
		next();
	}
	
	public char current() {
		return current;
	}
	
	public int columnNo() {
		return position + 1;
	}
	
	public String line() {
		return endOfFile ? null : lineBuff.toString().replaceAll("(\r\n|\r|\n)$", "");
	}
	
	public int lineNo() {
		return lineNo;
	}
	
	public String rest() {
		return hasReachedEol() ? "" : line().substring(columnNo() - 1);
	}
	
	public boolean hasReachedEof() {
		return endOfFile;
	}
	
	public boolean hasReachedEol() {
		return endOfFile || current == '\r' || current == '\n';
	}
	
	public char next() {
		if (endOfFile) {
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
					endOfFile = true;
					current = '\u0000';
					position = 0;
				} else {
					// まだオープン状態なら次の行をロードする
					loadLine();
				}
			}
			// EOFの判定を実施
			if (endOfFile) {
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
	
	private void loadLine() {
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
					endOfFile = lineBuff.length() == 0;
					if (endOfFile) {
						current = '\u0000';
					}
					return;
				}
				
				// 読み取った文字をバッファに格納
				lineBuff.append((char) c0);
				// 文字がLF・CRであるかどうか判定
				if (c0 == lf) {
					// LFであればただちに読み取りを完了
					return;
				} else if (c0 == cr) {
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
					} else if (c1 == lf) {
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
				throw new ParseError(e1.getMessage(), this, e1);
			}
			throw new ParseError(e0.getMessage(), this, e0);
		}
	}
}
