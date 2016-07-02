package org.unclazz.jp1ajs2.unitdef.query2;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

import org.unclazz.jp1ajs2.unitdef.Parameter;
import org.unclazz.jp1ajs2.unitdef.ParameterValue;
import org.unclazz.jp1ajs2.unitdef.Unit;
import org.unclazz.jp1ajs2.unitdef.query2.ChunkLazyIterable.ChunkYield;
import org.unclazz.jp1ajs2.unitdef.query2.ChunkLazyIterable.ChunkYieldCallable;
import org.unclazz.jp1ajs2.unitdef.query2.LazyIterable.Yield;
import org.unclazz.jp1ajs2.unitdef.query2.LazyIterable.YieldCallable;
import static org.unclazz.jp1ajs2.unitdef.query2.QueryUtils.*;

public final class ParameterValueListQuery extends AbstractParameterValueListQuery<ParameterValueListQuery> {
	ParameterValueListQuery(final ParameterListQuery baseQuery, final List<Predicate<ParameterValue>> preds) {
		super(baseQuery, preds);
	}
	ParameterValueListQuery(final ParameterListQuery baseQuery) {
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
	public ParameterValueListQuery and(final Predicate<ParameterValue> pred) {
		assertNotNull(pred, "argument must not be null.");
		
		final LinkedList<Predicate<ParameterValue>> newPreds = new LinkedList<Predicate<ParameterValue>>();
		newPreds.addAll(this.preds);
		newPreds.addLast(pred);
		return new ParameterValueListQuery(this.baseQuery, newPreds);
	}
	
	public SubscriptedParameterValueListQuery at(final int i) {
		assertNotNull(i < 0, "argument must not be greater than or equal 0.");
		
		return new SubscriptedParameterValueListQuery(baseQuery, preds, i);
	}
}
