package usertools.jp1ajs2.unitdef.core;

import java.util.Collections;
import java.util.Iterator;
import java.util.List;

import usertools.jp1ajs2.unitdef.util.Option;

class TupleImpl implements Tuple {
	
	private final List<TupleEntry> values;

	public TupleImpl(final List<TupleEntry> list) {
		this.values = Collections.unmodifiableList(list);
	}

	@Override
	public Option<String> get(int index) {
		return Option.wrap(index < values.size() ? values.get(index).getValue() : null);
	}

	@Override
	public Option<String> get(String key) {
		for(TupleEntry e: values){
			if(e.getKey().equals(key)){
				return Option.some(e.getValue());
			}
		}
		return Option.none();
	}
	
	@Override
	public String toString(){
		final StringBuilder sb = new StringBuilder();
		sb.append('(');
		for(final TupleEntry e : values){
			if(sb.length() > 1){
				sb.append(',');
			}
			sb.append(e.toString());
		}
		sb.append(')');
		return sb.toString();
	}

	@Override
	public int size() {
		return values.size();
	}

	@Override
	public boolean isEmpty() {
		return values.isEmpty();
	}

	@Override
	public Iterator<TupleEntry> iterator() {
		return values.iterator();
	}
}
