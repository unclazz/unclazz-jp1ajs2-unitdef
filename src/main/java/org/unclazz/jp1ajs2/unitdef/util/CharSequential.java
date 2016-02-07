package org.unclazz.jp1ajs2.unitdef.util;

public interface CharSequential {
	CharSequence toCharSequence();
	boolean contentEquals(CharSequence other);
	boolean contentEquals(CharSequential other);
}
