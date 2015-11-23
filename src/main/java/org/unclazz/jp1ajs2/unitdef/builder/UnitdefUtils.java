package org.unclazz.jp1ajs2.unitdef.builder;

public final class UnitdefUtils {
	private UnitdefUtils() {}
	
	static void mustBeNotNull(final Object... args) {
		for (final Object arg : args) {
			if (arg == null) {
				throw new IllegalArgumentException("Null founds in arguments");
			}
		}
	}
	
	static StringBuilder buff() {
		return new StringBuilder();
	}
	
	public static CharSequence escape(final CharSequence original) {
		final StringBuilder buff = buff();
		final int len = original.length();
		for (int i = 0; i < len; i ++) {
			final char c = original.charAt(i);
			if (c == '"' || c == '#') {
				buff.append('#');
			}
			buff.append(c);
		}
		return buff;
	}
	
	public static CharSequence quote(final CharSequence original) {
		final StringBuilder buff = buff().append('"');
		final int len = original.length();
		for (int i = 0; i < len; i ++) {
			final char c = original.charAt(i);
			if (c == '"' || c == '#') {
				buff.append('#');
			}
			buff.append(c);
		}
		return buff.append('"');
	}
	
	public static boolean equal(final CharSequence left, final CharSequence right) {
		if (left == right) {
			return true;
		} else if (left instanceof String && right instanceof String) {
			return left.equals(right);
		} else {
			final int len = left.length();
			if (len != right.length()) {
				return false;
			}
			for (int i = 0; i < len; i ++) {
				if (left.charAt(i) != right.charAt(i)) {
					return false;
				}
			}
			return true;
		}
	}

}
