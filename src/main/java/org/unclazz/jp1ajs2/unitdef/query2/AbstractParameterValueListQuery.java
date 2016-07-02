package org.unclazz.jp1ajs2.unitdef.query2;

import java.util.Collections;
import java.util.List;
import java.util.regex.Pattern;

import org.unclazz.jp1ajs2.unitdef.ParameterValue;
import org.unclazz.jp1ajs2.unitdef.ParameterValueType;
import org.unclazz.jp1ajs2.unitdef.Unit;
import org.unclazz.jp1ajs2.unitdef.util.Predicate;

import static org.unclazz.jp1ajs2.unitdef.query2.QueryUtils.*;

abstract class AbstractParameterValueListQuery<T extends AbstractParameterValueListQuery<T>>
implements Query<Unit, Iterable<ParameterValue>> {
	
	final ParameterListQuery baseQuery;
	final List<Predicate<ParameterValue>> preds;
	
	AbstractParameterValueListQuery(final ParameterListQuery baseQuery, final List<Predicate<ParameterValue>> preds) {
		assertNotNull(baseQuery, "argument must not be null.");
		assertNotNull(preds, "argument must not be null.");
		
		this.baseQuery = baseQuery;
		this.preds = preds;
	}
	AbstractParameterValueListQuery(final ParameterListQuery baseQuery) {
		this(baseQuery, Collections.<Predicate<ParameterValue>>emptyList());
	}
	
	public abstract T and(final Predicate<ParameterValue> pred);
	
	public final T typeIs(final ParameterValueType t) {
		assertNotNull(t, "argument must not be null.");
		return and(new Predicate<ParameterValue>() {
			private final ParameterValueType t1 = t;
			@Override
			public boolean test(ParameterValue t) {
				return t1 == t.getType();
			}
		});
	}
	
	public Query<Unit,ParameterValue> one(final boolean nullable) {
		return new OneQuery<Unit, ParameterValue>(this, nullable);
	}
	
	public Query<Unit,ParameterValue> one() {
		return new OneQuery<Unit, ParameterValue>(this, false);
	}
	
	public final TupleListQuery typeIsTuple() {
		return new TupleListQuery(this);
	}
	
	public final T startsWith(final String s) {
		assertNotNull(s, "argument must not be null.");
		assertFalse(s.isEmpty(), "argument must not be empty.");
		
		return and(new Predicate<ParameterValue>() {
			@Override
			public boolean test(ParameterValue t) {
				return t.getString().toString().startsWith(s);
			}
		});
	}
	
	public final T endsWith(final String s) {
		assertNotNull(s, "argument must not be null.");
		assertFalse(s.isEmpty(), "argument must not be empty.");
		
		return and(new Predicate<ParameterValue>() {
			@Override
			public boolean test(ParameterValue t) {
				return t.getString().toString().endsWith(s);
			}
		});
	}
	
	public final T contains(final String s) {
		assertNotNull(s, "argument must not be null.");
		assertFalse(s.isEmpty(), "argument must not be empty.");
		
		return and(new Predicate<ParameterValue>() {
			@Override
			public boolean test(ParameterValue t) {
				return t.getString().toString().contains(s);
			}
		});
	}
	
	public final T matches(final String regex) {
		assertNotNull(regex, "argument must not be null.");
		assertFalse(regex.isEmpty(), "argument must not be empty.");
		
		return and(new Predicate<ParameterValue>() {
			private final Pattern pat = Pattern.compile(regex);
			@Override
			public boolean test(ParameterValue t) {
				return pat.matcher(t.getString()).matches();
			}
		});
	}
	
	public final T matches(final Pattern regex) {
		assertNotNull(regex, "argument must not be null.");
		
		return and(new Predicate<ParameterValue>() {
			private final Pattern pat = regex;
			@Override
			public boolean test(ParameterValue t) {
				return pat.matcher(t.getString()).matches();
			}
		});
	}
}
