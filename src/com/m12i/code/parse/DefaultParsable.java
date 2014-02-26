package com.m12i.code.parse;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.Charset;

/**
 * {@link Parsable}の実装クラス.
 */
public class DefaultParsable implements Parsable {
	
	private final String content;
	private int position = 0;
	private String line = null;
	private char current = '\u0000';
	private int column = 1;
	private int lineNo = 1;
	private String lineSeparator = System.lineSeparator();
	
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
		return position >= content.length();
	}
	
	public boolean hasReachedEol() {
		return current == '\r' || current == '\n' || current == '\u0000';
	}
	
	public void lineSeparator(String sep) {
		lineSeparator = sep;
	}
	
	public String lineSeparator() {
		return lineSeparator;
	}
	
	/**
	 * ラップ対象とともにキャラクタセット名も引数にとるコンストラクタ.
	 * @param stream ラップ対象の{@link InputStream}
	 * @param charset キャラクタセット名
	 * @throws IOException 読み取り処理中にエラーが発生した場合
	 */
	public DefaultParsable(final InputStream stream, final String charset)
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
	public DefaultParsable(final InputStream s)
			throws IOException {
		this(s, Charset.defaultCharset().name());
	}
	
	/**
	 * Java文字列を引数にとるコンストラクタ.
	 * @param string 読み取り対象文字列
	 * @throws IOException 読み取り処理中にエラーが発生した場合
	 */
	public DefaultParsable(final String string) {
		this.content = string == null ? "" : string;
		init();
	}
	
	@Override
	public char next() {
		if (hasReachedEof()) {
			// EOF到達後ならすぐに現在文字（ヌル文字）を返す
			return current;
		} else {
			// EOF到達前なら次の文字を取得する処理に入る
			// まず現在位置をインクリメント
			position += 1;
			// EOFの判定を実施
			if (hasReachedEof()) {
				// EOF到達済みの場合
				
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
				// 現在文字に新しく取得した文字を設定
				current = ch;
				// 現在文字を返す
				return current;
			}
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
				: (hasReachedEof() ? "" : content.substring(position + 1, content.length()));
		final String info = String.format("^ (%d,%d)", lineNo(), columnNo());
				
		final String left1 = (left0.length() < position ? repeat('.', 3) : "")
				+ escapeCrLf(left0);
		final String right1 = escapeCrLf(right0) 
				+ (right0.length() < (content.length() - (position + 1)) ? repeat('.', 3) : "");
		return left1 + center + right1 + System.lineSeparator() 
				+ repeat('-', (left1.length() + center.length() - 1)) + info;
	}
	
	private String readAll(final InputStream stream, final Charset charset) {
		final StringBuilder sb = new StringBuilder();
		final InputStreamReader isr = new InputStreamReader(stream, charset);
		try {
			int i;
			while ((i = isr.read()) != -1) {
				sb.append((char) i);
			}
		} catch (IOException e) {
			throw new UnexpectedException(e);
		} finally {
			try {
				isr.close();
			} catch (IOException e) {
				throw new UnexpectedException(e);
			}
		}
		return sb.toString();
	}
	
	private String clipLine() {
		if (hasReachedEof()) {
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
	
	private void init() {
		current = content.length() == 0 ? '\u0000' : content.charAt(0);
		line = clipLine();
	}
	
	private boolean newLineStarts() {
		if (position == 0) {
			return false;
		}
		
		final String sep = System.lineSeparator();
		final char prev = content.charAt(position - 1);
		final char now = content.charAt(position);
		
		if (sep.equals("\r\n") && prev == '\r' && now == '\n') {
			return false;
		}
		
		if (prev == '\r' || prev == '\n') {
			return true;
		} else {
			return false;
		}
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