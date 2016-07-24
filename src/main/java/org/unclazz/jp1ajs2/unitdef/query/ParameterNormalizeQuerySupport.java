package org.unclazz.jp1ajs2.unitdef.query;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

import org.unclazz.jp1ajs2.unitdef.Parameter;
import org.unclazz.jp1ajs2.unitdef.Unit;
import org.unclazz.jp1ajs2.unitdef.util.Function;
import org.unclazz.jp1ajs2.unitdef.util.LazyIterable;
import org.unclazz.jp1ajs2.unitdef.util.LazyIterable.Yield;
import org.unclazz.jp1ajs2.unitdef.util.LazyIterable.YieldCallable;

abstract class ParameterNormalizeQuerySupport<T, U, Q extends Query<T, U>> implements Query<T, U> {
	static abstract class AppendWhenThenEntry<T,U,Q extends Query<T, U>> implements 
		Function<ParameterNormalizeQueryWhenThenEntry,
		ParameterNormalizeQuerySupport<T,U,Q>>{}
	
	public static final class ParameterNormalizeQuery
	extends ParameterNormalizeQuerySupport<Parameter,Parameter, ParameterNormalizeQuery> {
		ParameterNormalizeQuery() {
			this(Collections.<ParameterNormalizeQueryWhenThenEntry>emptyList());
		}
		private  ParameterNormalizeQuery(final List<ParameterNormalizeQueryWhenThenEntry> es) {
			super(new AppendWhenThenEntry<Parameter, Parameter, ParameterNormalizeQuery>() {
				@Override
				public ParameterNormalizeQuerySupport<Parameter, Parameter> 
				apply(ParameterNormalizeQueryWhenThenEntry e) {
					final LinkedList<ParameterNormalizeQueryWhenThenEntry> l = 
					new LinkedList<ParameterNormalizeQueryWhenThenEntry>();
					l.addAll(es);
					l.addLast(e);
					return new ParameterNormalizeQuery(l);
				}
			}, es);
		}
		@Override
		public Parameter queryFrom(Parameter t) {
			for (final ParameterNormalizeQueryWhenThenEntry e : whenEntryList) {
				if (e.getCondition().test(t)) {
					return e.getOperation().apply(t);
				}
			}
			return t;
		}
	}
	
	private final AppendWhenThenEntry<T, U> append;
	protected final List<ParameterNormalizeQueryWhenThenEntry> whenEntryList;
	
	protected ParameterNormalizeQuerySupport(AppendWhenThenEntry<T, U> a, 
			final List<ParameterNormalizeQueryWhenThenEntry> whenEntryList) {
		this.append = a;
		this.whenEntryList = whenEntryList;
	}
	
	protected ParameterNormalizeQuerySupport(AppendWhenThenEntry<T, U> a) {
		this.append = a;
		this.whenEntryList = Collections.emptyList();
	}
	
	public ParameterNormalizeQueryWhenClauseLeft<T,U> when() {
		return new ParameterNormalizeQueryWhenClauseLeft<T,U>(append);
	}
}
