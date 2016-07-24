package org.unclazz.jp1ajs2.unitdef.query;

import org.unclazz.jp1ajs2.unitdef.Parameter;
import org.unclazz.jp1ajs2.unitdef.query.ParameterNormalizeQuerySupport.AppendWhenThenEntry;
import org.unclazz.jp1ajs2.unitdef.util.Predicate;

public final class ParameterNormalizeQueryWhenValueCountClause<T,U> {
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
	
	private final AppendWhenThenEntry<T, U> append;
	private final int count;
	
	ParameterNormalizeQueryWhenValueCountClause(final AppendWhenThenEntry<T, U> a, final int c) {
		this.append = a;
		this.count = c;
	}
	
	public ParameterNormalizeQueryThenClause<T,U> then() {
		return new ParameterNormalizeQueryThenClause<T,U>(append, new ValueCountPredicate(count));
	}
}
