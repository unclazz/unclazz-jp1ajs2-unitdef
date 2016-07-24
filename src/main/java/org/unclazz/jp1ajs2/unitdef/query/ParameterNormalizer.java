package org.unclazz.jp1ajs2.unitdef.query;

import java.util.regex.Pattern;

import org.unclazz.jp1ajs2.unitdef.Parameter;
import org.unclazz.jp1ajs2.unitdef.ParameterValueType;

public interface ParameterNormalizer<Q extends ParameterNormalizer<Q>> {
	public static interface WhenValueAtNClause<Q> extends ThenClause<Q> {
		WhenValueAtNClause<Q> consistsOfAlphabets();
		WhenValueAtNClause<Q> consistsOfDigits();
		WhenValueAtNClause<Q> contains(CharSequence cs);
		WhenValueAtNClause<Q> contentEquals(CharSequence cs);
		WhenValueAtNClause<Q> endsWith(CharSequence cs);
		WhenValueAtNClause<Q> equalsAnyOf(CharSequence...cs);
		WhenValueAtNClause<Q> matches(Pattern re);
		WhenValueAtNClause<Q> startsWith(CharSequence cs);
		WhenValueAtNClause<Q> typeIs(ParameterValueType t);
	}
	public static interface WhenValueCountNClause<Q> extends ThenClause<Q> {
		WhenValueAtNClause<Q> valueAt(int i);
	}
	public static interface ThenClause<Q> {
		Q thenAppend(CharSequence cs);
		Q thenInsert(int i, CharSequence cs);
		Q thenPrepend(CharSequence cs);
		Q thenReplace(int i, CharSequence cs);
		Q thenReplaceAll(CharSequence... css);
		Q thenReplaceAll(Parameter cs);
	}
	
	WhenValueAtNClause<Q> whenValueAt(int i);
	WhenValueCountNClause<Q> whenValueCount(int c);
}
