package org.unclazz.jp1ajs2.unitdef.query;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

import org.unclazz.jp1ajs2.unitdef.Parameter;
import org.unclazz.jp1ajs2.unitdef.Unit;
import org.unclazz.jp1ajs2.unitdef.util.LazyIterable;
import org.unclazz.jp1ajs2.unitdef.util.LazyIterable.Yield;
import org.unclazz.jp1ajs2.unitdef.util.LazyIterable.YieldCallable;
import org.unclazz.jp1ajs2.unitdef.util.Predicate;

public final class UnitParameterNormalizeQuery  
extends ParameterNormalizeQuerySupport<Unit,Iterable<Parameter>>
implements IterableQuery<Unit, Parameter> {
	private final ParameterIterableQuery baseQuery;
	UnitParameterNormalizeQuery(final ParameterIterableQuery baseQuery) {
		this(baseQuery, Collections.<ParameterNormalizeQueryWhenThenEntry>emptyList());
	}
	private  UnitParameterNormalizeQuery(final ParameterIterableQuery baseQuery,
			final List<ParameterNormalizeQueryWhenThenEntry> es) {
		super(new AppendWhenThenEntry<Unit, Iterable<Parameter>>() {
			@Override
			public ParameterNormalizeQuerySupport<Unit, Iterable<Parameter>> 
			apply(ParameterNormalizeQueryWhenThenEntry e) {
				final LinkedList<ParameterNormalizeQueryWhenThenEntry> l = 
				new LinkedList<ParameterNormalizeQueryWhenThenEntry>();
				l.addAll(es);
				l.addLast(e);
				return new UnitParameterNormalizeQuery(baseQuery,l);
			}
		}, es);
		this.baseQuery = baseQuery;
	}
	@Override
	public Iterable<Parameter> queryFrom(Unit t) {
		return LazyIterable.forEach(baseQuery.queryFrom(t),
				new YieldCallable<Parameter,Parameter>(){
			@Override
			public Yield<Parameter> yield(Parameter item, int index) {
				for (final ParameterNormalizeQueryWhenThenEntry e : whenEntryList) {
					if (e.getCondition().test(item)) {
						return Yield.yieldReturn(e.getOperation().apply(item));
					}
				}
				return Yield.yieldReturn(item);
			}
		});
	}
	@Override
	public ParameterIterableQuery and(Predicate<Parameter> pred) {
		return baseQuery.and(pred);
	}
	@Override
	public OneQuery<Unit, Parameter> one(Parameter defaultValue) {
		return baseQuery.one(defaultValue);
	}
	@Override
	public OneQuery<Unit, Parameter> one(boolean nullable) {
		return baseQuery.one(nullable);
	}
	@Override
	public OneQuery<Unit, Parameter> one() {
		return baseQuery.one();
	}
	@Override
	public ListQuery<Unit, Parameter> list() {
		return baseQuery.list();
	}
	@Override
	public ListQuery<Unit, Parameter> list(boolean cached) {
		return baseQuery.list(cached);
	}
	@Override
	public Query<Unit, Iterable<Parameter>> cached() {
		return baseQuery.cached();
	}
	@Override
	public <V> IterableQuery<Unit, V> query(Query<Parameter, V> q) {
		return baseQuery.query(q);
	}

}
