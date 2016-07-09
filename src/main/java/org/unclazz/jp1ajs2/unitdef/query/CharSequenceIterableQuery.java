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

public final class CharSequenceIterableQuery 
extends IterableQuerySupport<Unit, CharSequence>
implements Query<Unit, Iterable<CharSequence>> {
	private final ParameterValueIterableQuerySupport<?> baseQuery;
	private final List<Predicate<CharSequence>> preds;
	
	CharSequenceIterableQuery(final ParameterValueIterableQuerySupport<?> baseQuery, final List<Predicate<CharSequence>> preds) {
		assertNotNull(baseQuery, "argument must not be null.");
		assertNotNull(preds, "argument must not be null.");
		
		this.baseQuery = baseQuery;
		this.preds = preds;
	}
	CharSequenceIterableQuery(final ParameterValueIterableQuerySupport<?> baseQuery) {
		this(baseQuery, Collections.<Predicate<CharSequence>>emptyList());
	}
	
	@Override
	public Iterable<CharSequence> queryFrom(Unit t) {
		assertNotNull(t, "argument must not be null.");
		
		return LazyIterable.forEach(baseQuery.queryFrom(t), new YieldCallable<ParameterValue,CharSequence>(){
			@Override
			public Yield<CharSequence> yield(final ParameterValue item, final int index) {
				final CharSequence itemCharSequence = item.serialize();
				for (final Predicate<CharSequence> pred : preds) {
					if (!pred.test(itemCharSequence)) {
						return Yield.yieldVoid();
					}
				}
				return Yield.yieldReturn(itemCharSequence);
			}
		});
	}
	
	@Override
	public CharSequenceIterableQuery and(final Predicate<CharSequence> pred) {
		assertNotNull(pred, "argument must not be null.");
		
		final LinkedList<Predicate<CharSequence>> newPreds = new LinkedList<Predicate<CharSequence>>();
		newPreds.addAll(this.preds);
		newPreds.addLast(pred);
		return new CharSequenceIterableQuery(this.baseQuery, newPreds);
	}
}
