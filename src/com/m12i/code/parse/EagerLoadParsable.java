package com.m12i.code.parse;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.Charset;

/**
 * {@link Parsable}の実装クラス.
 */
public class EagerLoadParsable implements Parsable {

	private static final int shrinkUnit = 1000;
	
	private final StringBuilder buff = new StringBuilder();
	private String content;
	private int position = 0;
	private String line = null;
	private char current = '\u0000';
	private boolean endOfFile = false;
	private int column = 1;
	private int lineNo = 1;
	
	@Override
	public char current() {
		return current;
	}
	
	@Override
	public int columnNo() {
		return column;
	}
	
	@Override
	public String line() {
		return line;
	}
	
	@Override
	public int lineNo() {
		return lineNo;
	}
	
	@Override
	public boolean hasReachedEof() {
		return endOfFile;
	}
	
	@Override
	public boolean hasReachedEol() {
		return current == '\r' || current == '\n' || current == '\u0000';
	}
	
	/**
	 * ラップ対象とともにキャラクタセット名も引数にとるコンストラクタ.
	 * @param stream ラップ対象の{@link InputStream}
	 * @param charset キャラクタセット名
	 * @throws IOException 読み取り処理中にエラーが発生した場合
	 */
	public EagerLoadParsable(final InputStream stream, final String charset)
			throws IOException {
		content = readAll(stream, Charset.forName(charset));
		init();
	}
	
	/**
	 * ラップ対象のみを引数にとるコンストラクタ.
	 * キャラクタセットは"utf-8"と仮定されます。
	 * @param s ラップ対象の{@link InputStream}
	 * @throws IOException 読み取り処理中にエラーが発生した場合
	 */
	public EagerLoadParsable(final InputStream s)
			throws IOException {
		this(s, Charset.defaultCharset().name());
	}
	
	/**
	 * Java文字列を引数にとるコンストラクタ.
	 * @param string 読み取り対象文字列
	 * @throws IOException 読み取り処理中にエラーが発生した場合
	 */
	public EagerLoadParsable(final String string) {
		this.content = string == null ? "" : string;
		init();
	}
	
	private String readAll(final InputStream stream, final Charset charset) throws IOException {
		buff.setLength(0);
		final BufferedReader br = new BufferedReader(new InputStreamReader(stream, charset));
		int i;
		while ((i = br.read()) != -1) {
			buff.append((char) i);
		}
		final String content = buff.toString();
		buff.setLength(0);
		return content;
	}
	
	private void init() {
		endOfFile = content.length() == 0;
		current = endOfFile ? '\u0000' : content.charAt(0);
		line = endOfFile ? null : clipLine();
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
			// EOFの判定を実施
			if (position >= content.length()) {
				// EOF到達済みの場合
				endOfFile = true;
				// 現在文字にはヌル文字を設定
				current = '\u0000';
				// 現在行をクリア
				line = null;
				// 現在位置には-1を設定
				column = 1;
				// 現在文字（ヌル文字）を返す
				return current;
			} else {
				// EOF到達前の場合

				// 新しい文字を取得
				final char ch = content.charAt(position);
				// 改行をチェック
				if (newLineStarts()) {
					// 新しい行に入ったと判断できたら行数をインクリメント
					lineNo += 1;
					// 現在行を設定
					line = clipLine();
					// 新しい行になったので現在位置をリセット
					column = 1;
				} else {
					// それ以外の場合は現在位置をインクリメント
					column += 1;
				}
				shrink();
				// 現在文字に新しく取得した文字を設定
				current = ch;
				// 現在文字を返す
				return current;
			}
		}
	}
	
	private void shrink() {
		if (position >= shrinkUnit) {
			content = content.substring(shrinkUnit);
			position -= shrinkUnit;
		}
	}

	private String clipLine() {
		if (endOfFile) {
			return null;
		} else {
			final String rest = content.substring(position);
			final int p0 = rest.indexOf("\r");
			final int p1 = rest.indexOf("\n");
			
			if (p0 == -1 && p1 == -1) {
				return rest;
			} else if (p0 == -1) {
				return rest.substring(0, p1);
			} else if (p1 == -1) {
				return rest.substring(0, p0);
			} else {
				return rest.substring(0, p0 < p1 ? p0 : p1);
			}
		}
	}
	
	private boolean newLineStarts() {
		if (position == 0) {
			return false;
		}
		
		final char prev = content.charAt(position - 1);
		final char now = content.charAt(position);
		
		if ((prev == '\r' && now != '\n') || prev == '\n') {
			return true;
		} else {
			return false;
		}
	}
	
	@Override
	public String toString() {
		final String center = escapeCrLf("" + current());
		final String left0 = position < 13 
				? content.substring(0, position) 
				: content.substring(position - 13, position);
		final String right0 = (position + 13) < content.length()
				? content.substring(position + 1, position + 13)
				: (endOfFile ? "" : content.substring(position + 1, content.length()));
		final String info = String.format("^ (%d,%d)", lineNo(), columnNo());
				
		final String left1 = (left0.length() < position ? repeat('.', 3) : "")
				+ escapeCrLf(left0);
		final String right1 = escapeCrLf(right0) 
				+ (right0.length() < (content.length() - (position + 1)) ? repeat('.', 3) : "");
		return left1 + center + right1 + System.lineSeparator() 
				+ repeat('-', (left1.length() + center.length() - 1)) + info;
	}
	
	private String escapeCrLf(String s) {
		return s.replaceAll("\r", "\\\\r").replaceAll("\n", "\\\\n");
	}
	
	private String repeat(char c, int n) {
		final StringBuilder sb = new StringBuilder();
		for (int i = 0; i < n; i++) {
			sb.append(c);
		}
		return sb.toString();
	}
	
}