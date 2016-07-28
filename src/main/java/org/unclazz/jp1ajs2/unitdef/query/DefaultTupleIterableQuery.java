package org.unclazz.jp1ajs2.unitdef.query;

import static org.unclazz.jp1ajs2.unitdef.query.QueryUtils.assertFalse;
import static org.unclazz.jp1ajs2.unitdef.query.QueryUtils.assertNotNull;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

import org.unclazz.jp1ajs2.unitdef.ParameterValue;
import org.unclazz.jp1ajs2.unitdef.ParameterValueType;
import org.unclazz.jp1ajs2.unitdef.Tuple;
import org.unclazz.jp1ajs2.unitdef.util.CharSequenceUtils;
import org.unclazz.jp1ajs2.unitdef.util.LazyIterable;
import org.unclazz.jp1ajs2.unitdef.util.Predicate;
import org.unclazz.jp1ajs2.unitdef.util.LazyIterable.Yield;
import org.unclazz.jp1ajs2.unitdef.util.LazyIterable.YieldCallable;

final class DefaultTupleIterableQuery<T> 
extends IterableQuerySupport<T, Tuple>
implements TupleIterableQuery<T> {
	private final IterableQuery<T, ParameterValue> baseQuery;
	private final List<Predicate<Tuple>> preds;
	
	DefaultTupleIterableQuery(final IterableQuery<T, ParameterValue> baseQuery, final List<Predicate<Tuple>> preds) {
		assertNotNull(baseQuery, "argument must not be null.");
		assertNotNull(preds, "argument must not be null.");
		
		this.baseQuery = baseQuery;
		this.preds = preds;
	}
	DefaultTupleIterableQuery(final IterableQuery<T, ParameterValue> baseQuery) {
		this(baseQuery, Collections.<Predicate<Tuple>>emptyList());
	}
	
	@Override
	public Iterable<Tuple> queryFrom(T t) {
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
	
	@Override
	public TupleIterableQuery<T> and(final Predicate<Tuple> pred) {
		assertNotNull(pred, "argument must not be null.");
		
		final LinkedList<Predicate<Tuple>> newPreds = new LinkedList<Predicate<Tuple>>();
		newPreds.addAll(this.preds);
		newPreds.addLast(pred);
		return new DefaultTupleIterableQuery<T>(this.baseQuery, newPreds);
	}
	
	/**
	 * エントリー・キーの条件を付与したクエリを返す.
	 * @param k キー
	 * @return クエリ
	 */
	public TupleIterableQuery<T> hasKey(final String k) {
		return and(new Predicate<Tuple>() {
			@Override
			public boolean test(Tuple t) {
				return t.keySet().contains(k);
			}
		});
	}
	
	/**
	 * エントリー値の条件を付与したクエリを返す.
	 * @param v エントリー値
	 * @return クエリ
	 */
	public TupleIterableQuery<T> hasValue(final CharSequence v) {
		return and(new Predicate<Tuple>() {
			private final String ks = v.toString();
			@Override
			public boolean test(Tuple t) {
				for (final Tuple.Entry e : t) {
					if (CharSequenceUtils.contentsAreEqual(e.getValue(), ks)) {
						return true;
					}
				}
				return false;
			}
		});
	}
	
	/**
	 * エントリーの条件を付与したクエリを返す.
	 * @param k エントリー・キー
	 * @param v エントリー値
	 * @return クエリ
	 */
	public TupleIterableQuery<T> hasEntry(final String k, final CharSequence v) {
		return and(new Predicate<Tuple>() {
			private final String vs = v.toString();
			@Override
			public boolean test(Tuple t) {
				return t.keySet().contains(k) && CharSequenceUtils.contentsAreEqual(t.get(k), vs);
			}
		});
	}
	
	/**
	 * エントリー数の条件を付与したクエリを返す.
	 * @param c エントリー数
	 * @return クエリ
	 */
	public TupleIterableQuery<T> entryCount(final int c) {
		assertFalse(c < 0, "argument must be greater than or equal 0.");
		
		return and(new Predicate<Tuple>() {
			@Override
			public boolean test(Tuple t) {
				return t.size() == c;
			}
		});
	}
}
