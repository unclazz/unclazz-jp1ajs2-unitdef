package org.unclazz.jp1ajs2.unitdef.query;

import java.util.Collections;

import org.unclazz.jp1ajs2.unitdef.ParameterValue;
import org.unclazz.jp1ajs2.unitdef.ParameterValueType;
import org.unclazz.jp1ajs2.unitdef.Tuple;
import org.unclazz.jp1ajs2.unitdef.Unit;
import org.unclazz.jp1ajs2.unitdef.util.CharSequenceUtils;
import org.unclazz.jp1ajs2.unitdef.util.UnitTreeNodesIterable;

final class InnerQueries {
	private InnerQueries() {}

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
