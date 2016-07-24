package org.unclazz.jp1ajs2.unitdef.query;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.regex.Pattern;

import org.unclazz.jp1ajs2.unitdef.Parameter;
import org.unclazz.jp1ajs2.unitdef.ParameterValue;
import org.unclazz.jp1ajs2.unitdef.ParameterValueType;
import org.unclazz.jp1ajs2.unitdef.util.CharSequenceUtils;
import org.unclazz.jp1ajs2.unitdef.util.Predicate;

public final class ParameterNormalizeQueryWhenClauseRight {
	private static final class ValueNPredicate implements Predicate<Parameter> {
		private final List<Predicate<ParameterValue>> preds;
		private final int i;
		private ValueNPredicate(final List<Predicate<ParameterValue>> preds, final int i) {
			this.preds = preds;
			this.i = i;
		}
		@Override
		public boolean test(final Parameter t) {
			if (t.getValues().size() <= i) {
				return false;
			}
			final ParameterValue v = t.getValues().get(i);
			for (final Predicate<ParameterValue> pred : preds) {
				if (!pred.test(v)) {
					return false;
				}
			}
			return true;
		}
		
	}
	
	private final ParameterNormalizeQuery baseQuery;
	private final int at;
	private final List<Predicate<ParameterValue>> preds;
	
	ParameterNormalizeQueryWhenClauseRight(final ParameterNormalizeQuery q, 
			final int at, final List<Predicate<ParameterValue>> preds) {
		this.baseQuery = q;
		this.at = at;
		this.preds = preds;
	}
	
	ParameterNormalizeQueryWhenClauseRight(final ParameterNormalizeQuery q, final int at) {
		this.baseQuery = q;
		this.at = at;
		this.preds = Collections.emptyList();
	}
	
	ParameterNormalizeQueryWhenClauseRight and(Predicate<ParameterValue> newPred) {
		final LinkedList<Predicate<ParameterValue>> l = new LinkedList<Predicate<ParameterValue>>();
		l.addAll(preds);
		l.addLast(newPred);
		return new ParameterNormalizeQueryWhenClauseRight(baseQuery, at, l);
	}
	
	public ParameterNormalizeQueryWhenClauseRight typeIs(final ParameterValueType type) {
		return and(new Predicate<ParameterValue>() {
			@Override
			public boolean test(ParameterValue t) {
				return t.getType() == type;
			}
		});
	}
	
	public ParameterNormalizeQueryWhenClauseRight matches(final Pattern regex) {
		return and(new Predicate<ParameterValue>() {
			@Override
			public boolean test(ParameterValue t) {
				return regex.matcher(t.getStringValue()).matches();
			}
		});
	}
	
	public ParameterNormalizeQueryWhenClauseRight contentEquals(final CharSequence s) {
		return and(new Predicate<ParameterValue>() {
			@Override
			public boolean test(ParameterValue t) {
				return CharSequenceUtils.contentsAreEqual(t.getStringValue(), s);
			}
		});
	}
	
	public ParameterNormalizeQueryWhenClauseRight startsWith(final CharSequence s) {
		return and(new Predicate<ParameterValue>() {
			@Override
			public boolean test(ParameterValue t) {
				return CharSequenceUtils.arg0StartsWithArg1(t.getStringValue(), s);
			}
		});
	}
	
	public ParameterNormalizeQueryWhenClauseRight endsWith(final CharSequence s) {
		return and(new Predicate<ParameterValue>() {
			@Override
			public boolean test(ParameterValue t) {
				return CharSequenceUtils.arg0EndsWithArg1(t.getStringValue(), s);
			}
		});
	}
	
	public ParameterNormalizeQueryWhenClauseRight contains(final CharSequence s) {
		return and(new Predicate<ParameterValue>() {
			@Override
			public boolean test(ParameterValue t) {
				return CharSequenceUtils.arg0ContainsArg1(t.getStringValue(), s);
			}
		});
	}
	
	public ParameterNormalizeQueryWhenClauseRight equalsAnyOf(final CharSequence... ss) {
		return and(new Predicate<ParameterValue>() {
			@Override
			public boolean test(ParameterValue t) {
				for (final CharSequence s : ss) {
					if (CharSequenceUtils.arg0ContainsArg1(t.getStringValue(), s)) {
						return true;
					}
				}
				return false;
			}
		});
	}
	
	public ParameterNormalizeQueryWhenClauseRight consistsOfDigits() {
		return matches(Pattern.compile("\\d+"));
	}
	public ParameterNormalizeQueryWhenClauseRight consistsOfAlphabets() {
		return matches(Pattern.compile("[a-z]+"));
	}
	
	public ParameterNormalizeQueryThenClause then() {
		return new ParameterNormalizeQueryThenClause(baseQuery, new ValueNPredicate(preds, at));
	}
}
