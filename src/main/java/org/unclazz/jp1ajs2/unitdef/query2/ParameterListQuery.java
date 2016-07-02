package org.unclazz.jp1ajs2.unitdef.query2;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

import org.unclazz.jp1ajs2.unitdef.Parameter;
import org.unclazz.jp1ajs2.unitdef.Unit;
import org.unclazz.jp1ajs2.unitdef.query2.ChunkLazyIterable.ChunkYield;
import org.unclazz.jp1ajs2.unitdef.query2.ChunkLazyIterable.ChunkYieldCallable;
import org.unclazz.jp1ajs2.unitdef.query2.LazyIterable.Yield;
import org.unclazz.jp1ajs2.unitdef.query2.LazyIterable.YieldCallable;

public class ParameterListQuery implements Query<Unit, Iterable<Parameter>> {
	private final UnitListQuery baseQuery;
	private final List<Predicate<Parameter>> preds;
	
	ParameterListQuery(final UnitListQuery q, final List<Predicate<Parameter>> preds) {
		this.baseQuery = q;
		this.preds = preds;
	}
	ParameterListQuery(final UnitListQuery q) {
		this(q, Collections.<Predicate<Parameter>>emptyList());
	}

	@Override
	public Iterable<Parameter> queryFrom(final Unit t) {
		final Iterable<Parameter> ps = ChunkLazyIterable
				.forEach(baseQuery.queryFrom(t),
				new ChunkYieldCallable<Unit, Parameter>(){
			@Override
			public ChunkYield<Parameter> yield(Unit item, int index) {
				return ChunkYield.yieldReturn(item.getParameters());
			}
		});
		
		return LazyIterable.forEach
		(ps, new YieldCallable<Parameter,Parameter>() {
			@Override
			public Yield<Parameter> yield(Parameter item, int index) {
				for (final Predicate<Parameter> pred : preds) {
					if (!pred.test(item)) {
						return Yield.yieldVoid();
					}
				}
				return Yield.yieldReturn(item);
			}
		});
	}
	
	public ParameterValueListQuery theirValues() {
		return new ParameterValueListQuery(this);
	}
	
	public Query<Unit,Parameter> one(final boolean nullable) {
		return new OneQuery<Unit, Parameter>(this, nullable);
	}
	
	public Query<Unit,Parameter> one() {
		return new OneQuery<Unit, Parameter>(this, false);
	}
	
	public ParameterListQuery and(final Predicate<Parameter> pred) {
		final LinkedList<Predicate<Parameter>> newPreds = new LinkedList<Predicate<Parameter>>();
		newPreds.addAll(this.preds);
		newPreds.addLast(pred);
		return new ParameterListQuery(this.baseQuery, newPreds);
	}
	
	public ParameterListQuery nameEquals(final String n) {
		return and(new Predicate<Parameter>() {
			private final String n1 = n;
			@Override
			public boolean test(final Parameter t) {
				return t.getName().equals(n1);
			}
		});
	}
	
	public ParameterListQuery nameStartsWith(final String n) {
		return and(new Predicate<Parameter>() {
			private final String n1 = n;
			@Override
			public boolean test(final Parameter t) {
				return t.getName().startsWith(n1);
			}
		});
	}
	
	public ParameterListQuery nameEndsWith(final String n) {
		return and(new Predicate<Parameter>() {
			private final String n1 = n;
			@Override
			public boolean test(final Parameter t) {
				return t.getName().endsWith(n1);
			}
		});
	}
	
	public ParameterListQuery nameContains(final String n) {
		return and(new Predicate<Parameter>() {
			private final String n1 = n;
			@Override
			public boolean test(final Parameter t) {
				return t.getName().contains(n1);
			}
		});
	}
	
	public ParameterListQuery name(final Predicate<String> test) {
		return and(new Predicate<Parameter>() {
			private final Predicate<String> p1 = test;
			@Override
			public boolean test(final Parameter t) {
				return p1.test(t.getName());
			}
		});
	}
	
	public ParameterListQuery valueCountEquals(final int i) {
		return and(new Predicate<Parameter>() {
			private final int i1 = i;
			@Override
			public boolean test(final Parameter t) {
				return t.getValues().size() == i1;
			}
		});
	}
	
	public ParameterListQuery valueCount(final Predicate<Integer> test) {
		return and(new Predicate<Parameter>() {
			private final Predicate<Integer> p1 = test;
			@Override
			public boolean test(final Parameter t) {
				return p1.test(t.getValues().size());
			}
		});
	}
}
