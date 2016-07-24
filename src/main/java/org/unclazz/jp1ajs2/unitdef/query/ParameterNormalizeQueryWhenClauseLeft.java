package org.unclazz.jp1ajs2.unitdef.query;

import org.unclazz.jp1ajs2.unitdef.query.ParameterNormalizeQuerySupport.AppendWhenThenEntry;

public final class ParameterNormalizeQueryWhenClauseLeft<T,U> {
	private final AppendWhenThenEntry<T, U> append;
	
	ParameterNormalizeQueryWhenClauseLeft(final AppendWhenThenEntry<T, U> append) {
		this.append = append;
	}
	
	public ParameterNormalizeQueryWhenValueCountClause<T,U> valueCount(final int c) {
		return new ParameterNormalizeQueryWhenValueCountClause<T,U>(append, c);
	}
	public ParameterNormalizeQueryWhenClauseRight<T,U> valueAt(final int i) {
		return new ParameterNormalizeQueryWhenClauseRight<T,U>(append, i);
	}
}
