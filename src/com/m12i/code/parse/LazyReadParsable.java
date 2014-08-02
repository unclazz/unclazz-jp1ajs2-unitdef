package com.m12i.code.parse;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.Charset;

/**
 * {@link Parsable}の実装クラス.
 */
public class LazyReadParsable implements Parsable {

	private static final int cr = (int) '\r';
	private static final int lf = (int) '\n';
	
	private final StringBuilder lineBuff = new StringBuilder();
	private final BufferedReader reader;

	private int position = 0;
	private char current = '\u0000';
	private boolean endOfFile = false;
	private boolean closed = false;
	private int lineNo = 0;
	
	@Override
	public char current() {
		return current;
	}
	
	@Override
	public int columnNo() {
		return position + 1;
	}
	
	@Override
	public String line() {
		return endOfFile ? null : lineBuff.toString().replaceAll("(\r\n|\r|\n)$", "");
	}
	
	@Override
	public int lineNo() {
		return lineNo;
	}
	
	@Override
	public boolean hasReachedEof() {
		return endOfFile;
	}
	
	public boolean hasReachedEol() {
		return endOfFile || current == '\r' || current == '\n';
	}
	
	/**
	 * ラップ対象とともにキャラクタセット名も引数にとるコンストラクタ.
	 * @param stream ラップ対象の{@link InputStream}
	 * @param charset キャラクタセット名
	 * @throws IOException 読み取り処理中にエラーが発生した場合
	 */
	public LazyReadParsable(final InputStream stream, final String charset)
			throws IOException {
		this(stream, Charset.forName(charset));
	}
	
	public LazyReadParsable(final InputStream stream, final Charset charset)
			throws IOException {
		reader = new BufferedReader(new InputStreamReader(stream, charset));
		loadLine();
		lineNo = 1;
	}
	
	/**
	 * ラップ対象のみを引数にとるコンストラクタ.
	 * キャラクタセットは"utf-8"と仮定されます。
	 * @param s ラップ対象の{@link InputStream}
	 * @throws IOException 読み取り処理中にエラーが発生した場合
	 */
	public LazyReadParsable(final InputStream s)
			throws IOException {
		this(s, Charset.defaultCharset().name());
	}
	
	@Override
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
		} catch (IOException e) {
			e.printStackTrace();
			// 遅延読み込みのためI/Oエラーが発生した場合は実行時例外でラップする
			try {
				// I/Oエラーを実行時例外でラップする以上、リソースのクローズの責任も持つ
				reader.close();
			} catch (IOException e1) {
				// クローズ時のエラーも実行時例外でラップする
				// ＊Java6サポート対象としたいので addSuppressed(Throwable) メソッドは使用しない
				e1.printStackTrace();
			}
			throw new RuntimeException(e);
		}
	}
}
