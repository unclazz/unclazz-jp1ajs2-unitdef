package org.unclazz.jp1ajs2.unitdef.util;

import java.io.Reader;
import java.io.StringReader;

/**
 * {@link String}および{@link CharSequence}のためのユーティリティ・クラス.
 */
public final class StringUtils {
	private StringUtils() {}
	
	/**
	 * 文字シーケンスから{@link Reader}インスタンスを生成して返す.
	 * @param s 文字シーケンス
	 * @return {@link Reader}インスタンス
	 */
	public static Reader reader(final CharSequence s) {
		return new StringReader(s.toString());
	}
	
	/**
	 * {@link StringBuilder}インスタンスを生成して返す.
	 * @return {@link StringBuilder}インスタンス
	 */
	public static StringBuilder builder() {
		return new StringBuilder();
	}
	
	/**
	 * 2つの文字シーケンスがその内容の文字の並びにおいて等しい場合{@code true}を返す.
	 * いずれか片方もしくは両方が{@code null}である場合は{@code false}を返す。
	 * @param s0 文字シーケンス
	 * @param s1 文字シーケンス
	 * @return 判定結果
	 */
	public static boolean contentsAreEqual(final CharSequence s0, final CharSequence s1) {
		if (s0 == null || s1 == null) {
			return false;
		}
		if (s0 == s1) {
			return true;
		}
		final int s0Length = s0.length();
		if (s0Length != s1.length()) {
			return false;
		}
		for (int i = 0; i < s0Length; i ++) {
			if (s0.charAt(i) != s1.charAt(i)) {
				return false;
			}
		}
		return true;
	}
	
	/**
	 * 1つめの文字シーケンスが2つめの文字シーケンスの文字の並びで始まる場合{@code true}を返す.
	 * いずれか片方もしくは両方が{@code null}である場合は{@code false}を返す。
	 * @param target 判定対象の文字シーケンス
	 * @param prefix 接頭辞となる文字シーケンス
	 * @return 判定結果
	 */
	public static boolean startsWith(final CharSequence target, final CharSequence prefix) {
		if (target == null || prefix == null) {
			return false;
		}
		if (target == prefix) {
			return true;
		}
		final int prefixLength = prefix.length();
		if (prefixLength > target.length()) {
			return false;
		}
		for (int i = 0; i < prefixLength; i ++) {
			if (prefix.charAt(i) != target.charAt(i)) {
				return false;
			}
		}
		return true;
	}
	/**
	 * 1つめの文字シーケンスが2つめの文字シーケンスの文字の並びで終わる場合{@code true}を返す.
	 * いずれか片方もしくは両方が{@code null}である場合は{@code false}を返す。
	 * @param target 判定対象の文字シーケンス
	 * @param suffix 接頭辞となる文字シーケンス
	 * @return 判定結果
	 */
	public static boolean endsWith(final CharSequence target, final CharSequence suffix) {
		if (target == null || suffix == null) {
			return false;
		}
		if (target == suffix) {
			return true;
		}
		final int suffixLength = suffix.length();
		final int targetLength = target.length();
		if (suffixLength > target.length()) {
			return false;
		}
		for (int i = 0; i < suffixLength; i ++) {
			if (suffix.charAt(suffixLength - i - 1) != target.charAt(targetLength - i - 1)) {
				return false;
			}
		}
		return true;
	}
	
	/**
	 * 1つめの文字シーケンスが2つめの文字シーケンスの文字を含んでいる場合{@code true}を返す.
	 * いずれか片方もしくは両方が{@code null}である場合は{@code false}を返す。
	 * @param target 判定対象の文字シーケンス
	 * @param part 部分文字列となる文字シーケンス
	 * @return 判定結果
	 */
	public static boolean contains(CharSequence target, final CharSequence part) {
		if (target == null || part == null) {
			return false;
		}
		if (target == part) {
			return true;
		}
		outer:
		while (true) {
			final int partLength = part.length();
			final int targetLength = target.length();
			if (partLength > target.length()) {
				return false;
			}
			for (int i = 0; i < partLength; i ++) {
				if (part.charAt(i) != target.charAt(i)) {
					target = target.subSequence(1, targetLength);
					continue outer;
				}
			}
			return true;
		}
	}
	
	/**
	 * 文字シーケンスにエスケープ処理を施す.
	 * これにより{@code "\""}は{@code "#\""}へ、
	 * {@code "#"}は{@code "##"}へとそれぞれ変換される。
	 * @param original 元の文字シーケンス
	 * @return 変換後の文字シーケンス
	 */
	public static CharSequence escape(final CharSequence original) {
		final StringBuilder buff = builder();
		final int len = original.length();
		for (int i = 0; i < len; i ++) {
			final char c = original.charAt(i);
			if (c == '"' || c == '#') {
				buff.append('#');
			}
			buff.append(c);
		}
		buff.trimToSize();
		return buff;
	}
	
	/**
	 * 文字シーケンスを二重引用符で囲われた文字シーケンスへと変換する.
	 * {@link #escape(CharSequence)}と同様の文字列置換が行われ、かつシーケンスの先頭と末尾に{@code '"'}が付与される。
	 * @param original 元の文字シーケンス
	 * @return 変換後の文字シーケンス
	 */
	public static CharSequence quote(final CharSequence original) {
		final StringBuilder buff = builder().append('"');
		final int len = original.length();
		for (int i = 0; i < len; i ++) {
			final char c = original.charAt(i);
			if (c == '"' || c == '#') {
				buff.append('#');
			}
			buff.append(c);
		}
		buff.append('"');
		buff.trimToSize();
		return buff;
	}
	
	/**
	 * {@code '#'}でエスケープされた文字シーケンスを元の文字シーケンスに逆変換する.
	 * 
	 * @param target 対象文字シーケンス
	 * @return 逆変換後の文字シーケンス
	 */
	public static CharSequence unescape(final CharSequence target) {
		final StringBuilder buff = builder();
		final int len = target.length();
		for (int i = 0; i < len; i ++) {
			final char ch = target.charAt(i);
			if (ch == '#') {
				i ++;
				buff.append(target.charAt(i));
			} else {
				buff.append(ch);
			}
		}
		return buff;
	}
	
	/**
	 * {@code '#'}でエスケープされ二重引用符で囲われた文字シーケンスを元の文字シーケンスに逆変換する.
	 * 
	 * @param taget 対象文字シーケンス
	 * @return 逆変換後の文字シーケンス
	 * @throws IllegalArgumentException 対象文字シーケンスが二重引用符で囲われていない場合
	 */
	public static CharSequence disquote(final CharSequence taget) {
		if (taget.charAt(0) != '"') {
			throw new IllegalArgumentException("target sequence does not start with '\"'.");
		}
		if (taget.charAt(taget.length() - 1) != '"') {
			throw new IllegalArgumentException("target sequence does not end with '\"'.");
		}
		return unescape(taget.subSequence(1, taget.length() - 1));
	}
	
	/**
	 * 第1引数の文字シーケンスの中から第2引数の文字を検索してその位置を返す.
	 * 位置は{@code 0}始まりのインデックスで表される。
	 * 文字が見つからなかった場合は{@code -1}が返される。
	 * @param cs 文字シーケンス
	 * @param ch 文字
	 * @return 文字が見つかった位置
	 */
	public static int indexOf(final CharSequence cs, final char ch) {
		final int csLen = cs.length();
		for (int i = 0; i < csLen; i ++) {
			if (cs.charAt(i) == ch) {
				return i;
			}
		}
		return -1;
	}
}
