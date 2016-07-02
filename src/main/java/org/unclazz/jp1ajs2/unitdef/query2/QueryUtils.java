package org.unclazz.jp1ajs2.unitdef.query2;

final class QueryUtils {
	private QueryUtils() {}
	
	public static void assertNotNull(final Object o, final String message) {
		if (o == null) throw new NullPointerException(message);
	}
	public static void assertTrue(final boolean result, final String message) {
		if (!result) throw new IllegalArgumentException(message);
	}
	public static void assertFalse(final boolean result, final String message) {
		if (result) throw new IllegalArgumentException(message);
	}
}
