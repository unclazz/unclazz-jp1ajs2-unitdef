package org.unclazz.jp1ajs2.unitdef.query;

public final class ParameterNormalizeQueryWhenClauseLeft {
	private final ParameterNormalizeQuery baseQuery;
	
	ParameterNormalizeQueryWhenClauseLeft(final ParameterNormalizeQuery baseQuery) {
		this.baseQuery = baseQuery;
	}
	
	public ParameterNormalizeQueryWhenValueCountClause valueCount(final int c) {
		return new ParameterNormalizeQueryWhenValueCountClause(baseQuery, c);
	}
	public ParameterNormalizeQueryWhenClauseRight valueAt(final int i) {
		return new ParameterNormalizeQueryWhenClauseRight(baseQuery, i);
	}
}
