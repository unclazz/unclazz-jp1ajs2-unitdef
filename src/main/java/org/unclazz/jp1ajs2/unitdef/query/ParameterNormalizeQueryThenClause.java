package org.unclazz.jp1ajs2.unitdef.query;

import org.unclazz.jp1ajs2.unitdef.Parameter;
import org.unclazz.jp1ajs2.unitdef.ParameterValue;
import org.unclazz.jp1ajs2.unitdef.builder.Builders;
import org.unclazz.jp1ajs2.unitdef.builder.ParameterBuilder;
import org.unclazz.jp1ajs2.unitdef.util.Function;
import org.unclazz.jp1ajs2.unitdef.util.Predicate;

public final class ParameterNormalizeQueryThenClause {
	private final ParameterNormalizeQuery baseQuery;
	private final Predicate<Parameter> pred;
	
	ParameterNormalizeQueryThenClause(final ParameterNormalizeQuery q,
			final Predicate<Parameter> pred) {
		this.baseQuery = q;
		this.pred = pred;
	}
	
	private ParameterBuilder builder(final String name) {
		return Builders.parameter().setName(name);
	}
	
	public ParameterNormalizeQuery prependValue(final CharSequence v) {
		return baseQuery.and(pred, new Function<Parameter, Parameter>() {
			@Override
			public Parameter apply(final Parameter t) {
				if (pred.test(t)) {
					final ParameterBuilder b = builder(t.getName());
					b.addRawCharSequence(v);
					for (final ParameterValue v2 : t.getValues()) {
						b.addValue(v2);
					}
					return b.build();
				}
				return t;
			}
		});
	}
	
	public ParameterNormalizeQuery appendValue(final CharSequence v) {
		return baseQuery.and(pred, new Function<Parameter, Parameter>() {
			@Override
			public Parameter apply(final Parameter t) {
				if (pred.test(t)) {
					final ParameterBuilder b = builder(t.getName());
					for (final ParameterValue v2 : t.getValues()) {
						b.addValue(v2);
					}
					b.addRawCharSequence(v);
					return b.build();
				}
				return t;
			}
		});
	}
	
	public ParameterNormalizeQuery insertValue(final int i, final CharSequence v) {
		return baseQuery.and(pred, new Function<Parameter, Parameter>() {
			@Override
			public Parameter apply(final Parameter t) {
				if (pred.test(t)) {
					final ParameterBuilder b = builder(t.getName());
					int i2 = 0;
					for (final ParameterValue v2 : t.getValues()) {
						if (i == i2 ++) {
							b.addRawCharSequence(v);
						}
						b.addValue(v2);
					}
					return b.build();
				}
				return t;
			}
		});
	}
	
	public ParameterNormalizeQuery replaceValue(final int i, final CharSequence v) {
		return baseQuery.and(pred, new Function<Parameter, Parameter>() {
			@Override
			public Parameter apply(final Parameter t) {
				if (pred.test(t)) {
					final ParameterBuilder b = builder(t.getName());
					int i2 = 0;
					for (final ParameterValue v2 : t.getValues()) {
						if (i == i2 ++) {
							b.addRawCharSequence(v);
						} else {
							b.addValue(v2);
						}
					}
					return b.build();
				}
				return t;
			}
		});
	}
	
	public ParameterNormalizeQuery replaceAll(final CharSequence... vs) {
		return baseQuery.and(pred, new Function<Parameter, Parameter>() {
			@Override
			public Parameter apply(final Parameter t) {
				if (pred.test(t)) {
					final ParameterBuilder b = builder(t.getName());
					for (final CharSequence v : vs) {
						b.addRawCharSequence(v);
					}
					return b.build();
				}
				return t;
			}
		});
	}
	
	public ParameterNormalizeQuery replaceAll(final Parameter p) {
		return baseQuery.and(pred, new Function<Parameter, Parameter>() {
			@Override
			public Parameter apply(final Parameter t) {
				if (pred.test(t)) {
					return p;
				}
				return t;
			}
		});
	}
}
