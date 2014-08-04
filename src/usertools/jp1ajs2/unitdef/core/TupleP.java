package usertools.jp1ajs2.unitdef.core;

import java.util.ArrayList;
import java.util.List;

import unklazz.parsec.Parser;
import unklazz.parsec.Reader;
import unklazz.parsec.Result;
import static usertools.jp1ajs2.unitdef.core.UnitP.*;

final class TupleP implements Parser<Tuple> {

	private final StringBuilder buff0 = new StringBuilder();
	private final StringBuilder buff1 = new StringBuilder();
	
	@Override
	public final Result<Tuple> parse(Reader in) {
		final char c = in.current();
		if (c != '(') {
			return Result.failure();
		}
		final List<TupleEntry> values = new ArrayList<TupleEntry>();
		in.next();
		
		while (!in.hasReachedEof() && in.current() != ')') {
			buff0.setLength(0);
			buff1.setLength(0);
			
			boolean hasKey = false;
			while (!in.hasReachedEof() && in.current() != ')' && in.current() != ',') {
				if (in.current() == '=') {
					hasKey = true;
					in.next();
				}
				(hasKey ? buff1 : buff0).append(in.current());
				in.next();
			}
			values.add(hasKey ? new TupleEntryImpl(buff0.toString(), buff1.toString())
					: new TupleEntryImpl(buff0.toString()));
			if (in.current() == ')') {
				break;
			}
			in.next();
		}
		if (in.current() !=')') error("タプルの末尾には')'が必要.", in);
		
		in.next();
		return Result.success(values.size() == 0 ? Tuple.EMPTY_TUPLE : new TupleImpl(values));
	}

	@Override
	public final String tokenName() {
		return "jp1/ajs2-unit-definition-parameter-tuple";
	}

}
