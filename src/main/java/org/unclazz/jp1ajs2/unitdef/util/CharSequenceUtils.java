package org.unclazz.jp1ajs2.unitdef.util;

import java.io.Reader;
import java.io.StringReader;

public final class CharSequenceUtils {
	private CharSequenceUtils() {}
	
	public static Reader reader(final CharSequence s) {
		return new StringReader(s.toString());
	}
	
	public static boolean contentsAreEqual(final CharSequence s0, final CharSequence s1) {
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
	
	public static boolean startsWith(final CharSequence s, final CharSequence prefix) {
		final int prefixLength = prefix.length();
		if (prefixLength > s.length()) {
			return false;
		}
		for (int i = 0; i < prefixLength; i ++) {
			if (prefix.charAt(i) != s.charAt(i)) {
				return false;
			}
		}
		return true;
	}
}
