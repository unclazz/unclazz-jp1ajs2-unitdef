package org.unclazz.jp1ajs2.unitdef.query;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

import org.unclazz.jp1ajs2.unitdef.Parameter;
import org.unclazz.jp1ajs2.unitdef.util.Function;
import org.unclazz.jp1ajs2.unitdef.util.Predicate;

public final class ParameterNormalizeQuery implements Query<Parameter, Parameter> {
	private final List<ParameterNormalizeQueryWhenThenEntry> whenEntryList;
	
	ParameterNormalizeQuery(final List<ParameterNormalizeQueryWhenThenEntry> whenEntryList) {
		this.whenEntryList = whenEntryList;
	}
	
	ParameterNormalizeQuery() {
		this.whenEntryList = Collections.emptyList();
	}
	
	ParameterNormalizeQuery and(final Predicate<Parameter> newPred, 
			final Function<Parameter, Parameter> newFunc) {
		final LinkedList<ParameterNormalizeQueryWhenThenEntry> l = 
				new LinkedList<ParameterNormalizeQueryWhenThenEntry>();
		l.addAll(whenEntryList);
		l.addLast(new ParameterNormalizeQueryWhenThenEntry(newPred, newFunc));
		return new ParameterNormalizeQuery(l);
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
	
	public ParameterNormalizeQueryWhenClauseLeft when() {
		return new ParameterNormalizeQueryWhenClauseLeft(this);
	}
}
