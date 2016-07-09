package org.unclazz.jp1ajs2.unitdef.query;

import static org.unclazz.jp1ajs2.unitdef.query.QueryUtils.*;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

import org.unclazz.jp1ajs2.unitdef.ParameterValue;
import org.unclazz.jp1ajs2.unitdef.Unit;
import org.unclazz.jp1ajs2.unitdef.util.LazyIterable;
import org.unclazz.jp1ajs2.unitdef.util.Predicate;
import org.unclazz.jp1ajs2.unitdef.util.LazyIterable.Yield;
import org.unclazz.jp1ajs2.unitdef.util.LazyIterable.YieldCallable;

public final class IntegerIterableQuery 
extends ItrableQuerySupport<Unit, Integer>
implements Query<Unit, Iterable<Integer>> {
	private final AbstractParameterValueIterableQuery<?> baseQuery;
	private final List<Predicate<Integer>> preds;
	private final Integer defaultValue;
	
	IntegerIterableQuery(final AbstractParameterValueIterableQuery<?> baseQuery,
			final List<Predicate<Integer>> preds, final Integer defaultValue) {
		assertNotNull(baseQuery, "argument must not be null.");
		assertNotNull(preds, "argument must not be null.");
		
		this.baseQuery = baseQuery;
		this.preds = preds;
		this.defaultValue = defaultValue;
	}
	IntegerIterableQuery(final AbstractParameterValueIterableQuery<?> baseQuery, final Integer defaultValue) {
		this(baseQuery, Collections.<Predicate<Integer>>emptyList(), defaultValue);
	}
	
	@Override
	public Iterable<Integer> queryFrom(Unit t) {
		assertNotNull(t, "argument must not be null.");
		
		return LazyIterable.forEach(baseQuery.queryFrom(t), new YieldCallable<ParameterValue,Integer>(){
			@Override
			public Yield<Integer> yield(final ParameterValue item, final int index) {
				final Integer i = tryParse(item.serialize());
				if (i == null) {
					if (defaultValue == null) {
						return Yield.yieldVoid();
					} else {
						return Yield.yieldReturn(defaultValue);
					}
				}
				for (final Predicate<Integer> pred : preds) {
					if (!pred.test(i)) {
						return Yield.yieldVoid();
					}
				}
				return Yield.yieldReturn(i);
			}
		});
	}
	
	public IntegerIterableQuery and(final Predicate<Integer> pred) {
		assertNotNull(pred, "argument must not be null.");
		
		final LinkedList<Predicate<Integer>> newPreds = new LinkedList<Predicate<Integer>>();
		newPreds.addAll(this.preds);
		newPreds.addLast(pred);
		return new IntegerIterableQuery(this.baseQuery, newPreds, defaultValue);
	}
	
	private Integer tryParse(final CharSequence cs) {
		try {
			return Integer.parseInt(cs.toString());
		} catch (final RuntimeException e) {
			return null;
		}
	}
}
