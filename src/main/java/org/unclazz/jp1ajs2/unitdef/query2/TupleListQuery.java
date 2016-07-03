package org.unclazz.jp1ajs2.unitdef.query2;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

import org.unclazz.jp1ajs2.unitdef.ParameterValue;
import org.unclazz.jp1ajs2.unitdef.ParameterValueType;
import org.unclazz.jp1ajs2.unitdef.Tuple;
import org.unclazz.jp1ajs2.unitdef.Unit;
import org.unclazz.jp1ajs2.unitdef.util.LazyIterable;
import org.unclazz.jp1ajs2.unitdef.util.Predicate;
import org.unclazz.jp1ajs2.unitdef.util.LazyIterable.Yield;
import org.unclazz.jp1ajs2.unitdef.util.LazyIterable.YieldCallable;
import static org.unclazz.jp1ajs2.unitdef.query2.QueryUtils.*;

public final class TupleListQuery implements Query<Unit, Iterable<Tuple>> {
	private final AbstractParameterValueListQuery<?> baseQuery;
	private final List<Predicate<Tuple>> preds;
	
	TupleListQuery(final AbstractParameterValueListQuery<?> baseQuery, final List<Predicate<Tuple>> preds) {
		assertNotNull(baseQuery, "argument must not be null.");
		assertNotNull(preds, "argument must not be null.");
		
		this.baseQuery = baseQuery;
		this.preds = preds;
	}
	TupleListQuery(final AbstractParameterValueListQuery<?> baseQuery) {
		this(baseQuery, Collections.<Predicate<Tuple>>emptyList());
	}
	
	@Override
	public Iterable<Tuple> queryFrom(Unit t) {
		assertNotNull(t, "argument must not be null.");
		
		return LazyIterable.forEach(baseQuery.queryFrom(t), new YieldCallable<ParameterValue,Tuple>(){
			@Override
			public Yield<Tuple> yield(final ParameterValue item, final int index) {
				if (ParameterValueType.TUPLE != item.getType()) {
					return Yield.yieldVoid();
				}
				final Tuple itemTuple = item.getTuple();
				for (final Predicate<Tuple> pred : preds) {
					if (!pred.test(itemTuple)) {
						return Yield.yieldVoid();
					}
				}
				return Yield.yieldReturn(itemTuple);
			}
		});
	}
	
	public TupleListQuery and(final Predicate<Tuple> pred) {
		assertNotNull(pred, "argument must not be null.");
		
		final LinkedList<Predicate<Tuple>> newPreds = new LinkedList<Predicate<Tuple>>();
		newPreds.addAll(this.preds);
		newPreds.addLast(pred);
		return new TupleListQuery(this.baseQuery, newPreds);
	}
	
	public Query<Unit,Tuple> one(final boolean nullable) {
		return new OneQuery<Unit, Tuple>(this, nullable);
	}
	public Query<Unit,Tuple> one() {
		return new OneQuery<Unit, Tuple>(this, false);
	}
	public Query<Unit, List<Tuple>> list() {
		return new ListQuery<Unit, Tuple>(this);
	}
	
	public TupleListQuery hasKey(final String k) {
		return and(new Predicate<Tuple>() {
			@Override
			public boolean test(Tuple t) {
				return t.keySet().contains(k);
			}
		});
	}
	
	public TupleListQuery hasValue(final CharSequence k) {
		return and(new Predicate<Tuple>() {
			private final String ks = k.toString();
			@Override
			public boolean test(Tuple t) {
				for (final Tuple.Entry e : t) {
					if (e.getValue().toString().equals(ks)) {
						return true;
					}
				}
				return false;
			}
		});
	}
	
	public TupleListQuery hasEntry(final String k, final CharSequence v) {
		return and(new Predicate<Tuple>() {
			private final String vs = v.toString();
			@Override
			public boolean test(Tuple t) {
				return t.keySet().contains(k) && t.get(k).equals(vs);
			}
		});
	}
	
	public TupleListQuery entryCount(final int c) {
		assertFalse(c < 0, "argument must be greater than or equal 0.");
		
		return and(new Predicate<Tuple>() {
			@Override
			public boolean test(Tuple t) {
				return t.size() == c;
			}
		});
	}
}
