package org.unclazz.jp1ajs2.unitdef.query;

import static org.unclazz.jp1ajs2.unitdef.query.QueryUtils.*;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

import org.unclazz.jp1ajs2.unitdef.Parameter;
import org.unclazz.jp1ajs2.unitdef.Unit;
import org.unclazz.jp1ajs2.unitdef.parameter.UnitType;
import org.unclazz.jp1ajs2.unitdef.util.Function;
import org.unclazz.jp1ajs2.unitdef.util.LazyIterable;
import org.unclazz.jp1ajs2.unitdef.util.Predicate;
import org.unclazz.jp1ajs2.unitdef.util.LazyIterable.Yield;
import org.unclazz.jp1ajs2.unitdef.util.LazyIterable.YieldCallable;

public class UnitIterableQuery extends AbstractItrableQuery<Unit,Unit> implements Query<Unit, Iterable<Unit>> {
	private final Function<Unit, Iterable<Unit>> func;
	private final List<Predicate<Unit>> preds;
	
	UnitIterableQuery(final Function<Unit, Iterable<Unit>> func, final List<Predicate<Unit>> preds) {
		assertNotNull(func, "argument must not be null.");
		assertNotNull(preds, "argument must not be null.");
		
		this.func = func;
		this.preds = preds;
	}
	UnitIterableQuery(final Function<Unit, Iterable<Unit>> func) {
		this(func, Collections.<Predicate<Unit>>emptyList());
	}

	@Override
	public Iterable<Unit> queryFrom(Unit t) {
		assertNotNull(t, "argument must not be null.");
		
		return LazyIterable.forEach(func.apply(t), new YieldCallable<Unit,Unit>() {
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
	
	public ParameterIterableQuery theirParameters() {
		return new ParameterIterableQuery(this);
	}
	
	@Override
	public UnitIterableQuery and(final Predicate<Unit> pred) {
		assertNotNull(pred, "argument must not be null.");
		
		final LinkedList<Predicate<Unit>> newPreds = new LinkedList<Predicate<Unit>>();
		newPreds.addAll(this.preds);
		newPreds.addLast(pred);
		return new UnitIterableQuery(this.func, newPreds);
	}
	
	public UnitIterableQuery typeIs(final UnitType t) {
		assertNotNull(t, "argument must not be null.");
		
		return and(new Predicate<Unit>() {
			private final UnitType t1 = t;
			@Override
			public boolean test(final Unit u) {
				return u.getType().equals(t1);
			}
		});
	}
	
	public UnitIterableQuery fqnEquals(final String n) {
		assertNotNull(n, "argument must not be null.");
		assertFalse(n.isEmpty(), "argument must not be empty.");
		
		return and(new Predicate<Unit>() {
			private final String n1 = n;
			@Override
			public boolean test(final Unit u) {
				return u.getFullQualifiedName().toString().equals(n1);
			}
		});
	}
	
	public UnitIterableQuery nameEquals(final String n) {
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
	
	public UnitIterableQuery nameStartsWith(final String n) {
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
	
	public UnitIterableQuery nameEndsWith(final String n) {
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
	
	public UnitIterableQuery nameContains(final String n) {
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
	
	public UnitIterableQuery name(final Predicate<String> test) {
		assertNotNull(test, "argument must not be null.");

		return and(new Predicate<Unit>() {
			private final Predicate<String> n1 = test;
			@Override
			public boolean test(final Unit u) {
				return n1.test(u.getName());
			}
		});
	}
	
	public UnitIterableQuery hasChildren() {
		return and(new Predicate<Unit>() {
			@Override
			public boolean test(final Unit u) {
				return !u.getSubUnits().isEmpty();
			}
		});
	}
	
	public UnitIterableQuery hasChildren(final Predicate<Unit> test) {
		assertNotNull(test, "argument must not be null.");

		return and(new Predicate<Unit>() {
			private final UnitIterableQuery q = Queries.children().and(test);
			@Override
			public boolean test(final Unit u) {
				return q.queryFrom(u).iterator().hasNext();
			}
		});
	}
	
	public UnitIterableQuery hasChildren(final Query<Unit,Boolean> query) {
		assertNotNull(query, "argument must not be null.");

		return and(new Predicate<Unit>() {
			@Override
			public boolean test(final Unit u) {
				return query.queryFrom(u);
			}
		});
	}
	
	public UnitIterableQuery hasParameter(final String name) {
		assertNotNull(name, "argument must not be null.");
		assertFalse(name.isEmpty(), "argument must not be empty.");
		
		return and(new Predicate<Unit>() {
			private Query<Unit, Parameter> q 
			= Queries.parameters().nameEquals(name).one(true);
			@Override
			public boolean test(final Unit u) {
				return u.query(q) != null;
			}
		});
	}
}
