package org.unclazz.jp1ajs2.unitdef.query;

import static org.unclazz.jp1ajs2.unitdef.query.QueryUtils.*;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

import org.unclazz.jp1ajs2.unitdef.Parameter;
import org.unclazz.jp1ajs2.unitdef.ParameterValue;
import org.unclazz.jp1ajs2.unitdef.Unit;
import org.unclazz.jp1ajs2.unitdef.util.ChunkLazyIterable;
import org.unclazz.jp1ajs2.unitdef.util.LazyIterable;
import org.unclazz.jp1ajs2.unitdef.util.Predicate;
import org.unclazz.jp1ajs2.unitdef.util.ChunkLazyIterable.ChunkYield;
import org.unclazz.jp1ajs2.unitdef.util.ChunkLazyIterable.ChunkYieldCallable;
import org.unclazz.jp1ajs2.unitdef.util.LazyIterable.Yield;
import org.unclazz.jp1ajs2.unitdef.util.LazyIterable.YieldCallable;

public final class ParameterValueIterableQuery extends AbstractParameterValueIterableQuery<ParameterValueIterableQuery> {
	ParameterValueIterableQuery(final ParameterIterableQuery baseQuery, final List<Predicate<ParameterValue>> preds) {
		super(baseQuery, preds);
	}
	ParameterValueIterableQuery(final ParameterIterableQuery baseQuery) {
		this(baseQuery, Collections.<Predicate<ParameterValue>>emptyList());
	}
	
	@Override
	public Iterable<ParameterValue> queryFrom(Unit t) {
		assertNotNull(t, "argument must not be null.");
		
		final Iterable<ParameterValue> pvs 
		= ChunkLazyIterable.forEach(baseQuery.queryFrom(t),
				new ChunkYieldCallable<Parameter, ParameterValue>(){
			@Override
			public ChunkYield<ParameterValue> yield(Parameter item, int index) {
				return ChunkYield.yieldReturn(item.getValues());
			}
		});
		return LazyIterable.forEach(pvs, new YieldCallable<ParameterValue,ParameterValue>(){
			@Override
			public Yield<ParameterValue> yield(final ParameterValue item, final int index) {
				for (final Predicate<ParameterValue> pred : preds) {
					if (!pred.test(item)) {
						return Yield.yieldVoid();
					}
				}
				return Yield.yieldReturn(item);
			}
		});
	}
	
	@Override
	public ParameterValueIterableQuery and(final Predicate<ParameterValue> pred) {
		assertNotNull(pred, "argument must not be null.");
		
		final LinkedList<Predicate<ParameterValue>> newPreds = new LinkedList<Predicate<ParameterValue>>();
		newPreds.addAll(this.preds);
		newPreds.addLast(pred);
		return new ParameterValueIterableQuery(this.baseQuery, newPreds);
	}
	
	public SubscriptedParameterValueIterableQuery at(final int i) {
		assertNotNull(i < 0, "argument must not be greater than or equal 0.");
		
		return new SubscriptedParameterValueIterableQuery(baseQuery, preds, i);
	}
}
