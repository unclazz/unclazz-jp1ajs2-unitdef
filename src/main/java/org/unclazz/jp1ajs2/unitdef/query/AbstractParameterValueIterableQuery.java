package org.unclazz.jp1ajs2.unitdef.query;

import static org.unclazz.jp1ajs2.unitdef.query.QueryUtils.*;

import java.util.Collections;
import java.util.List;
import java.util.regex.Pattern;

import org.unclazz.jp1ajs2.unitdef.ParameterValue;
import org.unclazz.jp1ajs2.unitdef.ParameterValueType;
import org.unclazz.jp1ajs2.unitdef.Unit;
import org.unclazz.jp1ajs2.unitdef.util.CharSequenceUtils;
import org.unclazz.jp1ajs2.unitdef.util.Predicate;

abstract class AbstractParameterValueIterableQuery<T extends AbstractParameterValueIterableQuery<T>>
extends AbstractItrableQuery<Unit, ParameterValue>
implements Query<Unit, Iterable<ParameterValue>> {
	
	final ParameterIterableQuery baseQuery;
	final List<Predicate<ParameterValue>> preds;
	
	AbstractParameterValueIterableQuery(final ParameterIterableQuery baseQuery, final List<Predicate<ParameterValue>> preds) {
		assertNotNull(baseQuery, "argument must not be null.");
		assertNotNull(preds, "argument must not be null.");
		
		this.baseQuery = baseQuery;
		this.preds = preds;
	}
	AbstractParameterValueIterableQuery(final ParameterIterableQuery baseQuery) {
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
	
	public final TupleIterableQuery typeIsTuple() {
		return new TupleIterableQuery(this);
	}
	
	public final CharSequenceIterableQuery asString() {
		return new CharSequenceIterableQuery(this);
	}
	
	public final IntegerIterableQuery asInteger() {
		return new IntegerIterableQuery(this, null);
	}
	public final IntegerIterableQuery asInteger(int defaultValue) {
		return new IntegerIterableQuery(this, defaultValue);
	}
	
	public final T contentEquals(final CharSequence s) {
		assertNotNull(s, "argument must not be null.");
		assertFalse(s.length() == 0, "argument must not be empty.");
		
		return and(new Predicate<ParameterValue>() {
			@Override
			public boolean test(ParameterValue t) {
				return CharSequenceUtils.contentsAreEqual(t.getString(),s);
			}
		});
	}
	
	public final T startsWith(final String s) {
		assertNotNull(s, "argument must not be null.");
		assertFalse(s.isEmpty(), "argument must not be empty.");
		
		return and(new Predicate<ParameterValue>() {
			@Override
			public boolean test(ParameterValue t) {
				return CharSequenceUtils.arg0StartsWithArg1(t.getString(),s);
			}
		});
	}
	
	public final T endsWith(final String s) {
		assertNotNull(s, "argument must not be null.");
		assertFalse(s.isEmpty(), "argument must not be empty.");
		
		return and(new Predicate<ParameterValue>() {
			@Override
			public boolean test(ParameterValue t) {
				return CharSequenceUtils.arg0EndsWithArg1(t.getString(), s);
			}
		});
	}
	
	public final T contains(final String s) {
		assertNotNull(s, "argument must not be null.");
		assertFalse(s.isEmpty(), "argument must not be empty.");
		
		return and(new Predicate<ParameterValue>() {
			@Override
			public boolean test(ParameterValue t) {
				return CharSequenceUtils.arg0ContainsArg1(t.getString(), s);
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
