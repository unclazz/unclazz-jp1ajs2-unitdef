package org.unclazz.jp1ajs2.unitdef.parser;

import org.unclazz.jp1ajs2.unitdef.Tuple;
import org.unclazz.jp1ajs2.unitdef.builder.Builders;
import org.unclazz.jp1ajs2.unitdef.builder.TupleBuilder;
import org.unclazz.jp1ajs2.unitdef.util.StringUtils;

public final class TupleParser implements Parser<Tuple> {
	@Override
	public ParseResult<Tuple> parse(Input in) {
		try {
			final Tuple t = parseTuple(in);
			return ParseResult.successful(t);
		} catch (ParseException e) {
			return ParseResult.failure(e);
		}
	}
	
	public ParseResult<Tuple> parse(CharSequence in) {
		try {
			return parse(Input.fromCharSequence(in));
		} catch (InputExeption e) {
			throw new IllegalArgumentException("invalid sequence as tuple literal.", e);
		}
	}
	
	private Tuple parseTuple(final Input in) throws ParseException {
		try {
			check(in, '(');
			final TupleBuilder builder = Builders.tuple();
			in.next();
			while (in.unlessEOF() && in.current() != ')') {
				final StringBuilder sb0 = StringUtils.builder();
				final StringBuilder sb1 = StringUtils.builder();
				boolean hasKey = false;
				while (in.unlessEOF() && (in.current() != ')' && in.current() != ',')) {
					if (in.current() == '=') {
						hasKey = true;
						in.next();
					}
					(hasKey ? sb1 : sb0).append(in.current());
					in.next();
				}
				if (hasKey) {
					builder.add(sb0.toString(), sb1.toString());
				} else {
					builder.add(sb0.toString());
				}
				if (in.current() == ')') {
					break;
				}
				in.next();
			}
			check(in, ')');
			in.next();
			return builder.build();
		} catch (InputExeption e) {
			throw new ParseException(e, in);
		}
	}
	
	private void check(final Input in, final char expected) throws ParseException {
		final char actual = in.current();
		if (actual != expected) {
			throw ParseException.arg1ExpectedButFoundArg2(in, expected, actual);
		}
	}
}
