package com.m12i.query.parser;

import com.m12i.code.parse.ParseException;

public final class QueryFactory<E> {
	private static final ExpressionParser p = new ExpressionParser();
	private final Accessor<E> a;
	public QueryFactory(Accessor<E> accessor) {
		if (accessor == null) {
			throw new IllegalArgumentException();
		}
		this.a = accessor;
	}
	public Query<E> create(String expr) throws QueryParseException {
		try {
			return new QueryImpl<E>(p.parse(expr), a);
		} catch (ParseException e) {
			throw new QueryParseException(e);
		}
	}
}
