package org.unclazz.jp1ajs2.unitdef.query;

final class InternalQueryUtils {
	private InternalQueryUtils() {}
	
	static void assertNotNull(final Object o, final String message) {
		if (o == null) throw new NullPointerException(message);
	}
	static void assertTrue(final boolean result, final String message) {
		if (!result) throw new IllegalArgumentException(message);
	}
	static void assertFalse(final boolean result, final String message) {
		if (result) throw new IllegalArgumentException(message);
	}
}
