package org.unclazz.jp1ajs2.unitdef.query;

import java.util.LinkedList;
import java.util.List;
import java.util.NoSuchElementException;

import org.unclazz.jp1ajs2.unitdef.Unit;

public abstract class FunctionalListUnitQuery<A> implements ListUnitQuery<A> {
	protected abstract Iterable<Unit> source(Unit u);
	protected abstract Function<Unit,A> function();
	public UnitQuery<A> one() {
		final ListUnitQuery<A> base = this;
		return new UnitQuery<A>() {
			@Override
			public A queryFrom(final Unit u) {
				final List<A> one = base.queryFrom(u);
				if (one.isEmpty()) {
					throw new NoSuchElementException("No item exsists in query result.");
				}
				return one.get(0);
			}
		};
	}
	
	@Override
	public List<A> queryFrom(final Unit u) {
		final LinkedList<A> result = new LinkedList<A>();
		final Function<Unit,A> func = function();
		for (final Unit a : source(u)) {
			final A b = func.apply(a);
			if (b != null) {
				result.addLast(b);
			}
		}
		return result;
	}
	public<B> FunctionalListUnitQuery<B> and(final Function<A,B> g) {
		final FunctionalListUnitQuery<A> base = this;
		return new FunctionalListUnitQuery<B>() {
			@Override
			public Iterable<Unit> source(final Unit n) {
				return base.source(n);
			}
			@Override
			public Function<Unit, B> function() {
				return SyntheticFunction.synthesize(base.function(), g);
			}
		};
	}
}
