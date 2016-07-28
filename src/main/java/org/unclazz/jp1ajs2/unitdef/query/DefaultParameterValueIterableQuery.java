package org.unclazz.jp1ajs2.unitdef.query;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.regex.Pattern;

import org.unclazz.jp1ajs2.unitdef.Parameter;
import org.unclazz.jp1ajs2.unitdef.ParameterValue;
import org.unclazz.jp1ajs2.unitdef.ParameterValueType;
import org.unclazz.jp1ajs2.unitdef.Tuple;
import org.unclazz.jp1ajs2.unitdef.Unit;
import org.unclazz.jp1ajs2.unitdef.util.StringUtils;
import org.unclazz.jp1ajs2.unitdef.util.ChunkLazyIterable;
import org.unclazz.jp1ajs2.unitdef.util.LazyIterable;
import org.unclazz.jp1ajs2.unitdef.util.Predicate;
import org.unclazz.jp1ajs2.unitdef.util.ChunkLazyIterable.ChunkYield;
import org.unclazz.jp1ajs2.unitdef.util.ChunkLazyIterable.ChunkYieldCallable;
import org.unclazz.jp1ajs2.unitdef.util.LazyIterable.Yield;
import org.unclazz.jp1ajs2.unitdef.util.LazyIterable.YieldCallable;
import static org.unclazz.jp1ajs2.unitdef.query.InternalQueries.*;
import static org.unclazz.jp1ajs2.unitdef.query.InternalQueryUtils.*;

final class DefaultParameterValueIterableQuery
extends IterableQuerySupport<Unit, ParameterValue>
implements ParameterValueIterableQuery {
	
	private final ParameterIterableQuery baseQuery;
	private final List<Predicate<ParameterValue>> preds;
	private final int at;

	DefaultParameterValueIterableQuery(ParameterIterableQuery baseQuery,
			List<Predicate<ParameterValue>> preds, int index) {
		assertNotNull(baseQuery, "argument must not be null.");
		assertNotNull(preds, "argument must not be null.");
		
		this.baseQuery = baseQuery;
		this.preds = preds;
		this.at = index;
	}

	DefaultParameterValueIterableQuery(ParameterIterableQuery baseQuery,
			List<Predicate<ParameterValue>> preds) {
		this(baseQuery, preds, -1);
	}

	DefaultParameterValueIterableQuery(ParameterIterableQuery baseQuery) {
		this(baseQuery, Collections.<Predicate<ParameterValue>>emptyList(), -1);
	}
	
	private Iterable<ParameterValue> fetchParameterValues(final Unit t) {
		final Iterable<Parameter> ps = baseQuery.queryFrom(t);
		// パラメータ値の位置の指定状態をチェック
		if (at == -1) {
			// パラメータ値の位置が指定されていない場合「すべて」を対象とする
			return ChunkLazyIterable.forEach(ps,
					new ChunkYieldCallable<Parameter,ParameterValue>(){
				@Override
				public ChunkYield<ParameterValue> yield(final Parameter item, final int index) {
					return ChunkYield.yieldReturn(item.getValues());
				}
			}); 
		}
		// パラメータ値の位置が指定されている場合はその位置のパラメータのみを対象とする
		return LazyIterable.forEach(ps,
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
	}
	@Override
	public Iterable<ParameterValue> queryFrom(final Unit t) {
		assertNotNull(t, "argument must not be null.");
		
		final Iterable<ParameterValue> pvs = fetchParameterValues(t);
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
	public final ParameterValueIterableQuery typeIs(final ParameterValueType t) {
		assertNotNull(t, "argument must not be null.");
		return and(new Predicate<ParameterValue>() {
			private final ParameterValueType t1 = t;
			@Override
			public boolean test(ParameterValue t) {
				return t1 == t.getType();
			}
		});
	}
	@Override
	public final TupleIterableQuery<Unit> typeIsTuple() {
		return new DefaultTupleIterableQuery<Unit>(this);
	}
	@Override
	public final IterableQuery<Unit,String> asString() {
		return query(new CastString());
	}
	@Override
	public final IterableQuery<Unit,Integer> asInteger() {
		return query(new CastInteger());
	}
	@Override
	public final IterableQuery<Unit,Integer> asInteger(int defaultValue) {
		return query(new CastInteger(defaultValue));
	}
	@Override
	public final IterableQuery<Unit, String> asEscapedString() {
		return query(new CastEscapedString());
	}
	@Override
	public final IterableQuery<Unit, String> asQuotedString() {
		return asQuotedString(false);
	}
	@Override
	public final IterableQuery<Unit, String> asQuotedString(boolean force) {
		return query(new CastQuotedString(force));
	}
	@Override
	public final IterableQuery<Unit, Boolean> asBoolean(String... trueValues) {
		return query(new CastBoolean(trueValues));
	}
	@Override
	public final IterableQuery<Unit, Tuple> asTuple() {
		return query(new CastTuple());
	}
	@Override
	public final ParameterValueIterableQuery contentEquals(final CharSequence s) {
		assertNotNull(s, "argument must not be null.");
		assertFalse(s.length() == 0, "argument must not be empty.");
		
		return and(new Predicate<ParameterValue>() {
			@Override
			public boolean test(ParameterValue t) {
				return StringUtils.contentsAreEqual(t.getStringValue(),s);
			}
		});
	}
	@Override
	public final ParameterValueIterableQuery startsWith(final String s) {
		assertNotNull(s, "argument must not be null.");
		assertFalse(s.isEmpty(), "argument must not be empty.");
		
		return and(new Predicate<ParameterValue>() {
			@Override
			public boolean test(ParameterValue t) {
				return StringUtils.startsWith(t.getStringValue(),s);
			}
		});
	}
	@Override
	public final ParameterValueIterableQuery endsWith(final String s) {
		assertNotNull(s, "argument must not be null.");
		assertFalse(s.isEmpty(), "argument must not be empty.");
		
		return and(new Predicate<ParameterValue>() {
			@Override
			public boolean test(ParameterValue t) {
				return StringUtils.endsWith(t.getStringValue(), s);
			}
		});
	}
	@Override
	public final ParameterValueIterableQuery contains(final String s) {
		assertNotNull(s, "argument must not be null.");
		assertFalse(s.isEmpty(), "argument must not be empty.");
		
		return and(new Predicate<ParameterValue>() {
			@Override
			public boolean test(ParameterValue t) {
				return StringUtils.contains(t.getStringValue(), s);
			}
		});
	}
	@Override
	public final ParameterValueIterableQuery matches(final String regex) {
		assertNotNull(regex, "argument must not be null.");
		assertFalse(regex.isEmpty(), "argument must not be empty.");
		
		return and(new Predicate<ParameterValue>() {
			private final Pattern pat = Pattern.compile(regex);
			@Override
			public boolean test(ParameterValue t) {
				return pat.matcher(t.getStringValue()).matches();
			}
		});
	}
	@Override
	public final ParameterValueIterableQuery matches(final Pattern regex) {
		assertNotNull(regex, "argument must not be null.");
		
		return and(new Predicate<ParameterValue>() {
			private final Pattern pat = regex;
			@Override
			public boolean test(ParameterValue t) {
				return pat.matcher(t.getStringValue()).matches();
			}
		});
	}
	@Override
	public ParameterValueIterableQuery at(final int i) {
		assertTrue(0 <= i, "argument must not be greater than or equal 0.");
		if (at != -1) {
			throw new IllegalStateException("value index has been specified.");
		}
		
		return new DefaultParameterValueIterableQuery(baseQuery, preds, i);
	}
	@Override
	public ParameterValueIterableQuery and(final Predicate<ParameterValue> pred) {
		assertNotNull(pred, "argument must not be null.");
		
		final LinkedList<Predicate<ParameterValue>> newPreds = new LinkedList<Predicate<ParameterValue>>();
		newPreds.addAll(this.preds);
		newPreds.addLast(pred);
		return new DefaultParameterValueIterableQuery(this.baseQuery, newPreds);
	}
}
