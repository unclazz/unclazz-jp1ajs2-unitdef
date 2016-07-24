package org.unclazz.jp1ajs2.unitdef.query;

import org.unclazz.jp1ajs2.unitdef.Parameter;
import org.unclazz.jp1ajs2.unitdef.util.Predicate;

public final class ParameterNormalizeQueryWhenValueCountClause {
	private static final class ValueCountPredicate implements Predicate<Parameter> {
		private final int count;
		private ValueCountPredicate(final int c) {
			this.count = c;
		}
		@Override
		public boolean test(final Parameter t) {
			return count == t.getValues().size();
		}
	}
	
	private final ParameterNormalizeQuery baseQuery;
	private final int count;
	
	ParameterNormalizeQueryWhenValueCountClause(final ParameterNormalizeQuery q, final int c) {
		this.baseQuery = q;
		this.count = c;
	}
	
	public ParameterNormalizeQueryThenClause then() {
		return new ParameterNormalizeQueryThenClause(baseQuery, new ValueCountPredicate(count));
	}
}
