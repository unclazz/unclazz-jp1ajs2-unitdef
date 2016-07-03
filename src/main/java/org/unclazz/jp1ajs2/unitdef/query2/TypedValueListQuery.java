package org.unclazz.jp1ajs2.unitdef.query2;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

import org.unclazz.jp1ajs2.unitdef.Unit;
import org.unclazz.jp1ajs2.unitdef.util.LazyIterable;
import org.unclazz.jp1ajs2.unitdef.util.Predicate;
import org.unclazz.jp1ajs2.unitdef.util.LazyIterable.Yield;
import org.unclazz.jp1ajs2.unitdef.util.LazyIterable.YieldCallable;
import static org.unclazz.jp1ajs2.unitdef.query2.QueryUtils.*;

public final class TypedValueListQuery<T,U> implements Query<Unit, Iterable<U>> {
	private final Query<Unit, Iterable<T>> baseQuery;
	private final List<Predicate<U>> preds;
	private final Query<T, U> transformer;
	
	TypedValueListQuery(final Query<Unit, Iterable<T>> baseQuery, 
			final List<Predicate<U>> preds, final Query<T, U> transformer) {
		assertNotNull(baseQuery, "argument must not be null.");
		assertNotNull(preds, "argument must not be null.");
		
		this.baseQuery = baseQuery;
		this.preds = preds;
		this.transformer = transformer;
	}
	TypedValueListQuery(final Query<Unit, Iterable<T>> baseQuery, final Query<T, U> transformer) {
		this(baseQuery, Collections.<Predicate<U>>emptyList(), transformer);
	}
	
	@Override
	public Iterable<U> queryFrom(Unit t) {
		assertNotNull(t, "argument must not be null.");
		
		return LazyIterable.forEach(baseQuery.queryFrom(t), new YieldCallable<T,U>(){
			@Override
			public Yield<U> yield(final T item, final int index) {
				final U typed = transformer.queryFrom(item);
				if (typed == null) {
					return Yield.yieldVoid();
				}
				for (final Predicate<U> pred : preds) {
					if (!pred.test(typed)) {
						return Yield.yieldVoid();
					}
				}
				return Yield.yieldReturn(typed);
			}
		});
	}
	
	public TypedValueListQuery<T,U> and(final Predicate<U> pred) {
		assertNotNull(pred, "argument must not be null.");
		
		final LinkedList<Predicate<U>> newPreds = new LinkedList<Predicate<U>>();
		newPreds.addAll(this.preds);
		newPreds.addLast(pred);
		return new TypedValueListQuery<T,U>(this.baseQuery, newPreds, transformer);
	}
	
	public Query<Unit,U> one(final boolean nullable) {
		return new OneQuery<Unit, U>(this, nullable);
	}
	public Query<Unit,U> one(final U defaultValue) {
		return new OneQuery<Unit, U>(this, defaultValue);
	}
	public Query<Unit, List<U>> list() {
		return new ListQuery<Unit, U>(this);
	}
	
	public Query<Unit,U> one() {
		return new OneQuery<Unit, U>(this, false);
	}
}
