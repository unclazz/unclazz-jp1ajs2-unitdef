package org.unclazz.jp1ajs2.unitdef.util;

import java.io.IOException;
import java.io.OutputStream;
import java.nio.charset.Charset;

import org.unclazz.jp1ajs2.unitdef.Unit;

/**
 * JP1/AJS2のユニット定義を文字列化するオブジェクト.
 */
public interface Formatter {
	/**
	 * JP1/AJS2のユニット定義を文字列化する際のオプション.
	 */
	public static interface Options {
		/**
		 * @return 行区切り文字列
		 */
		public String getLineSeparator();
		/**
		 * @return {@code true}の場合 タブの代わりに半角空白文字を使用する
		 */
		public boolean getUseSpacesForTabs();
		/**
		 * @return タブ幅
		 */
		public int getTabWidth();
		/**
		 * インデントと改行を行うかどうかの.
		 * @return {@code true}の場合 インデントと改行を行う
		 */
		public boolean getUseIndentAndLineBreak();
	}
	
	/**
	 * ユニット定義情報オブジェクトをフォーマットする.
	 * @param unit ユニット
	 * @return フォーマットしたユニット定義
	 */
	public CharSequence format(final Unit unit);
	/**
	 * ユニット定義情報オブジェクトをフォーマットし出力ストリームに書き出す.<br>
	 * <p>キャラクターセットにはシステムのデフォルトが使用される。</p>
	 * @param unit ユニット定義
	 * @param out 出力ストリーム
	 * @throws IOException I/Oエラーが発生した場合
	 */
	public void format(final Unit unit, final OutputStream out) throws IOException;
	/**
	 * ユニット定義情報オブジェクトをフォーマットし出力ストリームに書き出す.
	 * @param unit ユニット定義
	 * @param out 出力ストリーム
	 * @param charset キャラクターセット
	 * @throws IOException I/Oエラーが発生した場合
	 */
	public void format(final Unit unit, final OutputStream out, final Charset charset) throws IOException;
}
