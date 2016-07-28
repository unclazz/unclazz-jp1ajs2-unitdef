package org.unclazz.jp1ajs2.unitdef.query;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.regex.Pattern;

import org.unclazz.jp1ajs2.unitdef.Parameter;
import org.unclazz.jp1ajs2.unitdef.Unit;
import org.unclazz.jp1ajs2.unitdef.util.ChunkLazyIterable;
import org.unclazz.jp1ajs2.unitdef.util.LazyIterable;
import org.unclazz.jp1ajs2.unitdef.util.Predicate;
import org.unclazz.jp1ajs2.unitdef.util.ChunkLazyIterable.ChunkYield;
import org.unclazz.jp1ajs2.unitdef.util.ChunkLazyIterable.ChunkYieldCallable;
import org.unclazz.jp1ajs2.unitdef.util.LazyIterable.Yield;
import org.unclazz.jp1ajs2.unitdef.util.LazyIterable.YieldCallable;

final class DefaultParameterIterableQuery 
extends IterableQuerySupport<Unit, Parameter>
implements ParameterIterableQuery {
	private static final class QueryFactory
	implements ModifierFactory<ParameterIterableQuery> {
		private final Query<Unit,Iterable<Unit>> baseQuery;
		private final List<Predicate<Parameter>> preds;
		private final WhenThenList whenThenList;
		public QueryFactory(Query<Unit,Iterable<Unit>> baseQuery,
				List<Predicate<Parameter>> preds, WhenThenList whenThenList) {
			this.baseQuery = baseQuery;
			this.preds = preds;
			this.whenThenList = whenThenList;
		}
		@Override
		public ParameterIterableQuery apply(WhenThenList normalize) {
			final WhenThenList n = whenThenList == null ? normalize : whenThenList.concat(normalize);
			return new DefaultParameterIterableQuery(baseQuery, preds, n);
		}
	}

	private final Query<Unit,Iterable<Unit>> baseQuery;
	private final List<Predicate<Parameter>> preds;
	private final WhenThenList whenThenList;
	
	DefaultParameterIterableQuery(final Query<Unit,Iterable<Unit>> q,
			final List<Predicate<Parameter>> preds,
			final WhenThenList whenThenList) {
		this.baseQuery = q;
		this.preds = preds;
		this.whenThenList = whenThenList;
	}
	DefaultParameterIterableQuery(final Query<Unit,Iterable<Unit>> q) {
		this(q, Collections.<Predicate<Parameter>>emptyList(), null);
	}

	@Override
	public Iterable<Parameter> queryFrom(final Unit t) {
		// ChunkLazyIterableを用いて問合せ対象ユニットからパラメータを取得する
		final Iterable<Parameter> ps = ChunkLazyIterable
				.forEach(baseQuery.queryFrom(t),
				new ChunkYieldCallable<Unit, Parameter>(){
			@Override
			public ChunkYield<Parameter> yield(Unit item, int index) {
				return ChunkYield.yieldReturn(item.getParameters());
			}
		});
		// LazyIterableを用いてパラメータのそれぞれにPredicateで判断を加えつつ
		// その問合せの結果を呼び出し元に返す
		return LazyIterable.forEach
		(ps, new YieldCallable<Parameter,Parameter>() {
			@Override
			public Yield<Parameter> yield(Parameter item, int index) {
				if (whenThenList != null) {
					item = whenThenList.apply(item);
				}
				for (final Predicate<Parameter> pred : preds) {
					if (!pred.test(item)) {
						return Yield.yieldVoid();
					}
				}
				return Yield.yieldReturn(item);
			}
		});
	}
	@Override
	public ParameterValueIterableQuery theirValues() {
		return new DefaultParameterValueIterableQuery(this);
	}
	@Override
	public ParameterValueIterableQuery theirValues(final int at) {
		return new DefaultParameterValueIterableQuery(this).at(at);
	}
	@Override
	public ParameterIterableQuery and(final Predicate<Parameter> pred) {
		final LinkedList<Predicate<Parameter>> newPreds = new LinkedList<Predicate<Parameter>>();
		newPreds.addAll(this.preds);
		newPreds.addLast(pred);
		return new DefaultParameterIterableQuery(this.baseQuery, newPreds, whenThenList);
	}
	@Override
	public ParameterIterableQuery nameEquals(final String n) {
		return and(new Predicate<Parameter>() {
			private final String n1 = n;
			@Override
			public boolean test(final Parameter t) {
				return t.getName().equals(n1);
			}
		});
	}
	@Override
	public ParameterIterableQuery nameStartsWith(final String n) {
		return and(new Predicate<Parameter>() {
			private final String n1 = n;
			@Override
			public boolean test(final Parameter t) {
				return t.getName().startsWith(n1);
			}
		});
	}
	@Override
	public ParameterIterableQuery nameEndsWith(final String n) {
		return and(new Predicate<Parameter>() {
			private final String n1 = n;
			@Override
			public boolean test(final Parameter t) {
				return t.getName().endsWith(n1);
			}
		});
	}
	@Override
	public ParameterIterableQuery nameContains(final String n) {
		return and(new Predicate<Parameter>() {
			private final String n1 = n;
			@Override
			public boolean test(final Parameter t) {
				return t.getName().contains(n1);
			}
		});
	}
	@Override
	public ParameterIterableQuery nameMatches(final Pattern regex) {
		return and(new Predicate<Parameter>() {
			@Override
			public boolean test(final Parameter t) {
				return regex.matcher(t.getName()).matches();
			}
		});
	}
	@Override
	public ParameterIterableQuery nameMatches(final String regex) {
		return nameMatches(Pattern.compile(regex));
	}
	@Override
	public ParameterIterableQuery valueCountEquals(final int c) {
		return and(new Predicate<Parameter>() {
			private final int i1 = c;
			@Override
			public boolean test(final Parameter t) {
				return t.getValues().size() == i1;
			}
		});
	}
	@Override
	public WhenValueAtNClause<ParameterIterableQuery> whenValueAt(int i) {
		final QueryFactory factory =
				new QueryFactory(baseQuery, preds, whenThenList);
		return new DefaultWhenValueAtNClause<ParameterIterableQuery>(factory, i);
	}
	@Override
	public WhenValueCountNClause<ParameterIterableQuery> whenValueCount(int c) {
		final QueryFactory factory =
				new QueryFactory(baseQuery, preds, whenThenList);
		return new DefaultWhenValueCountNClause<ParameterIterableQuery>(factory, c);
	}
}
