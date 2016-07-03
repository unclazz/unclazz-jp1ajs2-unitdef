package org.unclazz.jp1ajs2.unitdef.query2;

import java.util.LinkedList;
import java.util.List;

import org.unclazz.jp1ajs2.unitdef.Parameter;
import org.unclazz.jp1ajs2.unitdef.ParameterValue;
import org.unclazz.jp1ajs2.unitdef.Unit;
import org.unclazz.jp1ajs2.unitdef.util.LazyIterable;
import org.unclazz.jp1ajs2.unitdef.util.Predicate;
import org.unclazz.jp1ajs2.unitdef.util.LazyIterable.Yield;
import org.unclazz.jp1ajs2.unitdef.util.LazyIterable.YieldCallable;
import static org.unclazz.jp1ajs2.unitdef.query2.QueryUtils.*;

public final class SubscriptedParameterValueIterableQuery
extends AbstractParameterValueIterableQuery<SubscriptedParameterValueIterableQuery> {
	
	private final int at;

	SubscriptedParameterValueIterableQuery(ParameterIterableQuery baseQuery, List<Predicate<ParameterValue>> preds, int index) {
		super(baseQuery, preds);
		this.at = index;
	}
	
	@Override
	public Iterable<ParameterValue> queryFrom(Unit t) {
		assertNotNull(t, "argument must not be null.");
		
		final Iterable<Parameter> ps = baseQuery.queryFrom(t);
		final Iterable<ParameterValue> pvs = LazyIterable.forEach(ps,
				new YieldCallable<Parameter,ParameterValue>(){
			@Override
			public Yield<ParameterValue> yield(final Parameter item, final int index) {
				if (item.getValues().size() > at) {
					return Yield.yieldReturn(item.getValues().get(at));
				} else {
					return Yield.yieldVoid();
				}
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
	
	public SubscriptedParameterValueIterableQuery and(final Predicate<ParameterValue> pred) {
		assertNotNull(pred, "argument must not be null.");
		
		final LinkedList<Predicate<ParameterValue>> newPreds = new LinkedList<Predicate<ParameterValue>>();
		newPreds.addAll(this.preds);
		newPreds.addLast(pred);
		return new SubscriptedParameterValueIterableQuery(this.baseQuery, newPreds, at);
	}
}
