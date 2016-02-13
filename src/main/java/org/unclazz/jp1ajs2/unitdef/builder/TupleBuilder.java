package org.unclazz.jp1ajs2.unitdef.builder;

import java.util.List;
import org.unclazz.jp1ajs2.unitdef.Tuple;
import static org.unclazz.jp1ajs2.unitdef.util.ListUtils.*;

public final class TupleBuilder {
	TupleBuilder() {}
	private final List<Tuple.Entry> entryList = linkedList();
	
	public TupleBuilder add(final String key, final String value) {
		entryList.add(new DefaultTuple.DefaultTupleEntry(key, value));
		return this;
	}
	public TupleBuilder add(final String value) {
		entryList.add(new DefaultTuple.DefaultTupleEntry(value));
		return this;
	}
	public Tuple build() {
		return entryList.isEmpty() ? Tuple.EMPTY_TUPLE : new DefaultTuple(entryList);
	}
}
