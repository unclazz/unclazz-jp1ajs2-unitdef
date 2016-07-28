package org.unclazz.jp1ajs2.unitdef.query;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

import org.unclazz.jp1ajs2.unitdef.ParameterValue;
import org.unclazz.jp1ajs2.unitdef.ParameterValueType;
import org.unclazz.jp1ajs2.unitdef.Tuple;
import org.unclazz.jp1ajs2.unitdef.Unit;
import org.unclazz.jp1ajs2.unitdef.util.CharSequenceUtils;
import org.unclazz.jp1ajs2.unitdef.util.LazyIterable;
import org.unclazz.jp1ajs2.unitdef.util.Predicate;
import org.unclazz.jp1ajs2.unitdef.util.UnitTreeNodesIterable;
import org.unclazz.jp1ajs2.unitdef.util.LazyIterable.Yield;
import org.unclazz.jp1ajs2.unitdef.util.LazyIterable.YieldCallable;
import static org.unclazz.jp1ajs2.unitdef.query.InternalQueryUtils.*;

final class InternalQueries {
	private InternalQueries() {}

	static final class JointQuery<T,U,V> implements Query<T, V>, OneQuery<T, V> {
		static<T,U,V> JointQuery<T,U,V> join(Query<T, U> q0, Query<U, V> q1) {
			return new JointQuery<T, U, V>(q0, q1);
		}
		
		private final Query<T, U> q0;
		private final Query<U, V> q1;
		
		private JointQuery(Query<T, U> q0, Query<U, V> q1) {
			this.q0 = q0;
			this.q1 = q1;
		}
		
		@Override
		public V queryFrom(T t) {
			return q1.queryFrom(q0.queryFrom(t));
		}
		@Override
		public Query<T, V> cached() {
			return CachedQuery.wrap(this);
		}
	}
	
	/**
	 * ベースとなるクエリをラップし問合せに際して型変換と追加のフィルタリングを行うクエリ.
	 *
	 * @param <T> ベースとなるクエリ および このクエリ 双方の問合せ対象の型
	 * @param <U> ベースとなるクエリの問合せ結果の型
	 * @param <V> このクエリの問合せ結果の型
	 */
	static final class JointIterableQuery<T,U,V> extends IterableQuerySupport<T, V> {
		private final Query<T, Iterable<U>> baseQuery;
		private final List<Predicate<V>> preds;
		private final Query<U, V> transformer;
		
		JointIterableQuery(final Query<T, Iterable<U>> baseQuery, 
				final List<Predicate<V>> preds, final Query<U, V> transformer) {
			assertNotNull(baseQuery, "argument must not be null.");
			assertNotNull(preds, "argument must not be null.");
			
			this.baseQuery = baseQuery;
			this.preds = preds;
			this.transformer = transformer;
		}
		JointIterableQuery(final Query<T, Iterable<U>> baseQuery, final Query<U, V> transformer) {
			this(baseQuery, Collections.<Predicate<V>>emptyList(), transformer);
		}
		
		@Override
		public Iterable<V> queryFrom(T t) {
			assertNotNull(t, "argument must not be null.");
			
			return LazyIterable.forEach(baseQuery.queryFrom(t), new YieldCallable<U,V>(){
				@Override
				public Yield<V> yield(final U item, final int index) {
					final V typed = transformer.queryFrom(item);
					if (typed == null) {
						return Yield.yieldVoid();
					}
					for (final Predicate<V> pred : preds) {
						if (!pred.test(typed)) {
							return Yield.yieldVoid();
						}
					}
					return Yield.yieldReturn(typed);
				}
			});
		}
		
		public JointIterableQuery<T,U,V> and(final Predicate<V> pred) {
			assertNotNull(pred, "argument must not be null.");
			
			final LinkedList<Predicate<V>> newPreds = new LinkedList<Predicate<V>>();
			newPreds.addAll(this.preds);
			newPreds.addLast(pred);
			return new JointIterableQuery<T,U,V>(this.baseQuery, newPreds, transformer);
		}
	}
	
	static final class CastBoolean implements Query<ParameterValue, Boolean> {
		private final String[] trueValues;
		CastBoolean(final String[] trueValues) {
			this.trueValues = trueValues;
		}
		@Override
		public Boolean queryFrom(ParameterValue t) {
			final String v = t.getStringValue();
			for (final String trueValue : trueValues) {
				if (v.equals(trueValue)) {
					return true;
				}
			}
			return false;
		}
	}
	
	static final class CastEscapedString implements Query<ParameterValue, String> {
		@Override
		public String queryFrom(ParameterValue t) {
			return CharSequenceUtils.escape(t.getStringValue()).toString();
		}
	}

	static final class CastInteger implements Query<ParameterValue, Integer> {
		private final int defaultValue;
		private final boolean hasDefault;
		CastInteger(final int defaultValue) {
			this.defaultValue = defaultValue;
			this.hasDefault = true;
		}
		CastInteger() {
			this.defaultValue = -1;
			this.hasDefault = false;
		}
		@Override
		public Integer queryFrom(ParameterValue t) {
			try {
				return Integer.parseInt(t.getStringValue());
			} catch (final NumberFormatException e) {
				if (hasDefault) {
					return defaultValue;
				} else {
					throw e;
				}
			}
		}
	}

	static final class CastQuotedString implements Query<ParameterValue, String> {
		private final boolean force;
		CastQuotedString(final boolean force) {
			this.force = force;
		}
		@Override
		public String queryFrom(ParameterValue t) {
			if (force || t.getType() == ParameterValueType.QUOTED_STRING) {
				return CharSequenceUtils.quote(t.getStringValue()).toString();
			} else {
				return t.getStringValue();
			}
		}
	}

	static final class CastString implements Query<ParameterValue, String> {
		@Override
		public String queryFrom(ParameterValue t) {
			return t.getStringValue();
		}
	}

	static final class CastTuple implements Query<ParameterValue, Tuple> {
		@Override
		public Tuple queryFrom(ParameterValue t) {
			if (t.getType() == ParameterValueType.TUPLE) {
				return t.getTuple();
			}
			throw new IllegalArgumentException("parameter must be tuple.");
		}
	}

	static final class SourceChildren implements Query<Unit, Iterable<Unit>> {
		@Override
		public Iterable<Unit> queryFrom(Unit t) {
			return t.getSubUnits();
		}
	}

	static final class SourceDescendants implements Query<Unit, Iterable<Unit>> {
		private final boolean includesRoot;
		SourceDescendants(boolean includesRoot) {
			this.includesRoot = includesRoot;
		}
		@Override
		public Iterable<Unit> queryFrom(Unit t) {
			return UnitTreeNodesIterable.ofBreadthFirst(t, includesRoot);
		}
	}

	static final class SourceDescendantsDepthFirst implements Query<Unit, Iterable<Unit>> {
		private final boolean includesRoot;
		SourceDescendantsDepthFirst(boolean includesRoot) {
			this.includesRoot = includesRoot;
		}
		@Override
		public Iterable<Unit> queryFrom(Unit t) {
			return UnitTreeNodesIterable.ofDepthFirst(t, includesRoot);
		}
	}

	static final class SourceItSelf implements Query<Unit, Iterable<Unit>> {
		@Override
		public Iterable<Unit> queryFrom(Unit t) {
			return Collections.singleton(t);
		}
	}
}
