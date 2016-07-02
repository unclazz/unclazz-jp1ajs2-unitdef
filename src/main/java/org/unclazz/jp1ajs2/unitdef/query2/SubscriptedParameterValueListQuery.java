package org.unclazz.jp1ajs2.unitdef.query2;

import java.util.LinkedList;
import java.util.List;

import org.unclazz.jp1ajs2.unitdef.Parameter;
import org.unclazz.jp1ajs2.unitdef.ParameterValue;
import org.unclazz.jp1ajs2.unitdef.Unit;
import org.unclazz.jp1ajs2.unitdef.query2.LazyIterable.Yield;
import org.unclazz.jp1ajs2.unitdef.query2.LazyIterable.YieldCallable;

public final class SubscriptedParameterValueListQuery
extends AbstractParameterValueListQuery<SubscriptedParameterValueListQuery> {
	private static void assertNotNull(final Object o, final String message) {
		if (o == null) throw new NullPointerException(message);
	}
	
	private final int index;

	SubscriptedParameterValueListQuery(ParameterListQuery baseQuery, List<Predicate<ParameterValue>> preds, int index) {
		super(baseQuery, preds);
		this.index = index;
	}
	
	@Override
	public Iterable<ParameterValue> queryFrom(Unit t) {
		assertNotNull(t, "argument must not be null.");
		
		final Iterable<Parameter> ps = baseQuery.queryFrom(t);
		final Iterable<ParameterValue> pvs = LazyIterable.forEach(ps,
				new YieldCallable<Parameter,ParameterValue>(){
			@Override
			public Yield<ParameterValue> yield(final Parameter item, final int index) {
				if (item.getValues().size() > index) {
					return Yield.yieldReturn(item.getValues().get(index));
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
	
	public SubscriptedParameterValueListQuery and(final Predicate<ParameterValue> pred) {
		assertNotNull(pred, "argument must not be null.");
		
		final LinkedList<Predicate<ParameterValue>> newPreds = new LinkedList<Predicate<ParameterValue>>();
		newPreds.addAll(this.preds);
		newPreds.addLast(pred);
		return new SubscriptedParameterValueListQuery(this.baseQuery, newPreds, index);
	}
}
