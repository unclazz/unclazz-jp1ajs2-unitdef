package org.unclazz.jp1ajs2.unitdef.query;

import static org.unclazz.jp1ajs2.unitdef.query.InternalQueryUtils.*;
import org.unclazz.jp1ajs2.unitdef.query.InternalQueries.*;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.regex.Pattern;

import org.unclazz.jp1ajs2.unitdef.FullQualifiedName;
import org.unclazz.jp1ajs2.unitdef.Parameter;
import org.unclazz.jp1ajs2.unitdef.ParameterValue;
import org.unclazz.jp1ajs2.unitdef.ParameterValueType;
import org.unclazz.jp1ajs2.unitdef.Tuple;
import org.unclazz.jp1ajs2.unitdef.Unit;
import org.unclazz.jp1ajs2.unitdef.Tuple.Entry;
import org.unclazz.jp1ajs2.unitdef.parameter.UnitType;
import org.unclazz.jp1ajs2.unitdef.util.StringUtils;
import org.unclazz.jp1ajs2.unitdef.util.LazyIterable;
import org.unclazz.jp1ajs2.unitdef.util.Predicate;
import org.unclazz.jp1ajs2.unitdef.util.LazyIterable.Yield;
import org.unclazz.jp1ajs2.unitdef.util.LazyIterable.YieldCallable;

final class DefaultUnitIterableQuery extends IterableQuerySupport<Unit,Unit>
implements UnitIterableQuery {
	private final Query<Unit, Iterable<Unit>> srcQuery;
	private final List<Predicate<Unit>> preds;
	
	DefaultUnitIterableQuery(final Query<Unit, Iterable<Unit>> srcQuery, final List<Predicate<Unit>> preds) {
		assertNotNull(srcQuery, "argument must not be null.");
		assertNotNull(preds, "argument must not be null.");
		
		this.srcQuery = srcQuery;
		this.preds = preds;
	}
	DefaultUnitIterableQuery(final Query<Unit, Iterable<Unit>> srcQuery) {
		this(srcQuery, Collections.<Predicate<Unit>>emptyList());
	}

	@Override
	public Iterable<Unit> queryFrom(Unit t) {
		assertNotNull(t, "argument must not be null.");
		
		return LazyIterable.forEach(srcQuery.queryFrom(t), new YieldCallable<Unit,Unit>() {
			@Override
			public Yield<Unit> yield(final Unit item, int index) {
				for (final Predicate<Unit> pred : preds) {
					if (!pred.test(item)) {
						return Yield.yieldVoid();
					}
				}
				return Yield.yieldReturn(item);
			}
		});
	}
	@Override
	public ParameterIterableQuery theirParameters() {
		return new DefaultParameterIterableQuery(this);
	}
	@Override
	public ParameterIterableQuery theirParameters(final String name) {
		return new DefaultParameterIterableQuery(this).nameEquals(name);
	}
	@Override
	public IterableQuery<Unit, FullQualifiedName> theirFqn() {
		return new JointIterableQuery<Unit, Unit, FullQualifiedName>(this,
		new Query<Unit, FullQualifiedName>() {
			@Override
			public FullQualifiedName queryFrom(Unit t) {
				return t.getFullQualifiedName();
			}
		});
	}
	@Override
	public IterableQuery<Unit, String> theirName() {
		return new JointIterableQuery<Unit, Unit, String>(this,
		new Query<Unit, String>() {
			@Override
			public String queryFrom(Unit t) {
				return t.getName();
			}
		});
	}
	@Override
	public DefaultUnitIterableQuery and(final Predicate<Unit> pred) {
		assertNotNull(pred, "argument must not be null.");
		
		final LinkedList<Predicate<Unit>> newPreds = new LinkedList<Predicate<Unit>>();
		newPreds.addAll(this.preds);
		newPreds.addLast(pred);
		return new DefaultUnitIterableQuery(this.srcQuery, newPreds);
	}
	@Override
	public DefaultUnitIterableQuery typeIs(final UnitType t) {
		assertNotNull(t, "argument must not be null.");
		
		return and(new Predicate<Unit>() {
			private final UnitType t1 = t;
			@Override
			public boolean test(final Unit u) {
				return u.getType().equals(t1);
			}
		});
	}
	@Override
	public DefaultUnitIterableQuery fqnEquals(final String n) {
		assertNotNull(n, "argument must not be null.");
		assertFalse(n.isEmpty(), "argument must not be empty.");
		
		return and(new Predicate<Unit>() {
			@Override
			public boolean test(final Unit u) {
				return u.getFullQualifiedName().toString().equals(n);
			}
		});
	}
	@Override
	public DefaultUnitIterableQuery fqnStartsWith(final String n) {
		assertNotNull(n, "argument must not be null.");
		assertFalse(n.isEmpty(), "argument must not be empty.");
		
		return and(new Predicate<Unit>() {
			@Override
			public boolean test(final Unit u) {
				return u.getFullQualifiedName().toString().startsWith(n);
			}
		});
	}
	@Override
	public DefaultUnitIterableQuery fqnEndsWith(final String n) {
		assertNotNull(n, "argument must not be null.");
		assertFalse(n.isEmpty(), "argument must not be empty.");
		
		return and(new Predicate<Unit>() {
			@Override
			public boolean test(final Unit u) {
				return u.getFullQualifiedName().toString().endsWith(n);
			}
		});
	}
	@Override
	public DefaultUnitIterableQuery fqnContains(final String n) {
		assertNotNull(n, "argument must not be null.");
		assertFalse(n.isEmpty(), "argument must not be empty.");
		
		return and(new Predicate<Unit>() {
			@Override
			public boolean test(final Unit u) {
				return u.getFullQualifiedName().toString().contains(n);
			}
		});
	}
	@Override
	public DefaultUnitIterableQuery nameEquals(final String n) {
		assertNotNull(n, "argument must not be null.");
		assertFalse(n.isEmpty(), "argument must not be empty.");
		
		return and(new Predicate<Unit>() {
			private final String n1 = n;
			@Override
			public boolean test(final Unit u) {
				return u.getName().equals(n1);
			}
		});
	}
	@Override
	public DefaultUnitIterableQuery nameStartsWith(final String n) {
		assertNotNull(n, "argument must not be null.");
		assertFalse(n.isEmpty(), "argument must not be empty.");
		
		return and(new Predicate<Unit>() {
			private final String n1 = n;
			@Override
			public boolean test(final Unit u) {
				return u.getName().startsWith(n1);
			}
		});
	}
	@Override
	public DefaultUnitIterableQuery nameEndsWith(final String n) {
		assertNotNull(n, "argument must not be null.");
		assertFalse(n.isEmpty(), "argument must not be empty.");
		
		return and(new Predicate<Unit>() {
			private final String n1 = n;
			@Override
			public boolean test(final Unit u) {
				return u.getName().endsWith(n1);
			}
		});
	}
	@Override
	public DefaultUnitIterableQuery nameContains(final String n) {
		assertNotNull(n, "argument must not be null.");
		assertFalse(n.isEmpty(), "argument must not be empty.");
		
		return and(new Predicate<Unit>() {
			private final String n1 = n;
			@Override
			public boolean test(final Unit u) {
				return u.getName().contains(n1);
			}
		});
	}
	@Override
	public DefaultUnitIterableQuery nameMatches(final Pattern regex) {
		assertNotNull(regex, "argument must not be null.");

		return and(new Predicate<Unit>() {
			@Override
			public boolean test(final Unit u) {
				return regex.matcher(u.getName()).matches();
			}
		});
	}
	@Override
	public DefaultUnitIterableQuery nameMatches(final String regex) {
		return nameMatches(Pattern.compile(regex));
	}
	@Override
	public DefaultUnitIterableQuery hasChildren() {
		return and(new Predicate<Unit>() {
			@Override
			public boolean test(final Unit u) {
				return !u.getSubUnits().isEmpty();
			}
		});
	}
	@Override
	public HasParameterValueAtN hasParameter(final String name) {
		assertNotNull(name, "argument must not be null.");
		assertFalse(name.isEmpty(), "argument must not be empty.");
		
		return new DefaultHasParameterValueAtN(srcQuery, preds, name);
	}
}

final class DefaultHasParameterValueAtN
implements UnitIterableQuery.HasParameterValueAtN {
	private final String parameterName;
	private final int valueIndex;
	private final Query<Unit, Iterable<Unit>> srcQuery;
	private final List<Predicate<Unit>> preds;
	
	DefaultHasParameterValueAtN(
			final Query<Unit, Iterable<Unit>> srcQuery,
			final List<Predicate<Unit>> preds,
			final String parameterName) {
		this(srcQuery, preds, parameterName, -1);
	}
	
	DefaultHasParameterValueAtN(
			final Query<Unit, Iterable<Unit>> srcQuery,
			final List<Predicate<Unit>> preds,
			final String parameterName,
			final int valueIndex) {
		this.srcQuery = srcQuery;
		this.preds = preds;
		this.parameterName = parameterName;
		this.valueIndex = valueIndex;
	}
	
	private UnitIterableQuery createQueryWithNewPredicate(final Predicate<Unit> newPred) {
		final LinkedList<Predicate<Unit>> newPreds = new LinkedList<Predicate<Unit>>();
		newPreds.addAll(preds);
		newPreds.add(newPred);
		return new DefaultUnitIterableQuery(srcQuery, newPreds);
	}
	private Iterable<String> fetchParameterValues(final Unit u) {
		return LazyIterable.forEach(u.getParameters(), 
				new YieldCallable<Parameter,String>(){
			@Override
			public Yield<String> yield(final Parameter item, final int index) {
				if (!item.getName().equals(parameterName)) {
					return Yield.yieldVoid();
				}
				if (!(item.getValues().size() > valueIndex)) {
					return Yield.yieldVoid();
				}
				return Yield.yieldReturn(item.getValues().
						get(valueIndex == -1 ? 0 : valueIndex).getStringValue());
			}
		});
	}
	@Override
	public UnitIterableQuery.HasParameterValueAtN valueAt(final int i) {
		assertTrue(i >= 0, "argument must be greater than or equal 0.");
		if (valueIndex != -1) {
			throw new IllegalStateException("value index has been specified.");
		}
		return new DefaultHasParameterValueAtN(srcQuery, preds, parameterName, i);
	}
	@Override
	public UnitIterableQuery anyValue() {
		return createQueryWithNewPredicate(new Predicate<Unit>() {
			@Override
			public boolean test(Unit t) {
				for (final Parameter p : t.getParameters()) {
					if (p.getName().equals(parameterName) && p.getValues().size() > valueIndex) {
						return true;
					}
				}
				return false;
			}
		});
	}
	@Override
	public UnitIterableQuery contentEquals(final CharSequence s) {
		return createQueryWithNewPredicate(new Predicate<Unit>() {
			@Override
			public boolean test(final Unit t) {
				for (final String v : fetchParameterValues(t)) {
					if (StringUtils.contentsAreEqual(v, s)) {
						return true;
					}
				}
				return false;
			}
		});
	}
	@Override
	public UnitIterableQuery startsWith(final CharSequence s) {
		return createQueryWithNewPredicate(new Predicate<Unit>() {
			@Override
			public boolean test(final Unit t) {
				for (final String v : fetchParameterValues(t)) {
					if (StringUtils.startsWith(v, s)) {
						return true;
					}
				}
				return false;
			}
		});
	}
	@Override
	public UnitIterableQuery endsWith(final CharSequence s) {
		return createQueryWithNewPredicate(new Predicate<Unit>() {
			@Override
			public boolean test(final Unit t) {
				for (final String v : fetchParameterValues(t)) {
					if (StringUtils.endsWith(v, s)) {
						return true;
					}
				}
				return false;
			}
		});
	}
	@Override
	public UnitIterableQuery contains(final CharSequence s) {
		return createQueryWithNewPredicate(new Predicate<Unit>() {
			@Override
			public boolean test(final Unit t) {
				for (final String v : fetchParameterValues(t)) {
					if (StringUtils.contains(v, s)) {
						return true;
					}
				}
				return false;
			}
		});
	}
	@Override
	public UnitIterableQuery matches(final Pattern regex) {
		return createQueryWithNewPredicate(new Predicate<Unit>() {
			@Override
			public boolean test(final Unit t) {
				for (final String v : fetchParameterValues(t)) {
					if (regex.matcher(v).matches()) {
						return true;
					}
				}
				return false;
			}
		});
	}
	@Override
	public UnitIterableQuery matches(final CharSequence regex) {
		return matches(Pattern.compile(regex.toString()));
	}
	@Override
	public UnitIterableQuery.HasParameterValueAtNIsTuple typeIsTuple() {
		return new DefaultHasParameterValueAtNIsTuple(srcQuery, preds, 
				parameterName, valueIndex);
	}
}

final class DefaultHasParameterValueAtNIsTuple
implements UnitIterableQuery.HasParameterValueAtNIsTuple {
	private final String parameterName;
	private final int valueIndex;
	private final Query<Unit, Iterable<Unit>> func;
	private final List<Predicate<Unit>> preds;
	
	DefaultHasParameterValueAtNIsTuple(
			final Query<Unit, Iterable<Unit>> func,
			final List<Predicate<Unit>> preds,
			final String parameterName) {
		this(func, preds, parameterName, -1);
	}
	
	DefaultHasParameterValueAtNIsTuple(
			final Query<Unit, Iterable<Unit>> func,
			final List<Predicate<Unit>> preds,
			final String parameterName,
			final int valueIndex) {
		this.func = func;
		this.preds = preds;
		this.parameterName = parameterName;
		this.valueIndex = valueIndex;
	}
	
	private UnitIterableQuery createQueryWithNewPredicate(final Predicate<Unit> newPred) {
		final LinkedList<Predicate<Unit>> newPreds = new LinkedList<Predicate<Unit>>();
		newPreds.addAll(preds);
		newPreds.add(newPred);
		return new DefaultUnitIterableQuery(func, newPreds);
	}
	
	private Iterable<Tuple> fetchTuples(final Unit u) {
		return LazyIterable.forEach(u.getParameters(), 
				new YieldCallable<Parameter,Tuple>(){
			@Override
			public Yield<Tuple> yield(final Parameter item, final int index) {
				if (!item.getName().equals(parameterName)) {
					return Yield.yieldVoid();
				}
				if (!(item.getValues().size() > valueIndex)) {
					return Yield.yieldVoid();
				}
				final ParameterValue v = item.getValues().get(valueIndex == -1 ? 0 : valueIndex);
				if (v.getType() != ParameterValueType.TUPLE) {
					return Yield.yieldVoid();
				}
				return Yield.yieldReturn(v.getTuple());
			}
		});
	}
	
	@Override
	public UnitIterableQuery.HasParameterValueAtNIsTuple valueAt(final int i) {
		assertTrue(i >= 0, "argument must be greater than or equal 0.");
		if (valueIndex != -1) {
			throw new IllegalStateException("value index has been specified.");
		}
		return new DefaultHasParameterValueAtNIsTuple(func, preds, parameterName, i);
	}
	
	@Override
	public UnitIterableQuery entryCount(final int i) {
		return createQueryWithNewPredicate(new Predicate<Unit>() {
			@Override
			public boolean test(final Unit t) {
				for (final Tuple tuple : fetchTuples(t)) {
					if (tuple.size() == i) {
						return true;
					}
				}
				return false;
			}
		});
	}
	
	@Override
	public UnitIterableQuery hasEntry(final String k, final CharSequence v) {
		return createQueryWithNewPredicate(new Predicate<Unit>() {
			@Override
			public boolean test(final Unit t) {
				for (final Tuple tuple : fetchTuples(t)){
					if (tuple.keySet().contains(k) && tuple.get(k).equals(v.toString())) {
						return true;
					}
				}
				return false;
			}
		});
	}
	
	@Override
	public UnitIterableQuery hasKey(final String k) {
		return createQueryWithNewPredicate(new Predicate<Unit>() {
			@Override
			public boolean test(final Unit t) {
				for (final Tuple tuple : fetchTuples(t)){
					if (tuple.keySet().contains(k)) {
						return true;
					}
				}
				return false;
			}
		});
	}
	
	@Override
	public UnitIterableQuery hasValue(final CharSequence v) {
		return createQueryWithNewPredicate(new Predicate<Unit>() {
			@Override
			public boolean test(final Unit t) {
				for (final Tuple tuple : fetchTuples(t)){
					for (final Entry e : tuple) {
						if (StringUtils.contentsAreEqual(e.getValue(), v)) {
							return true;
						}
					}
				}
				return false;
			}
		});
	}
}
