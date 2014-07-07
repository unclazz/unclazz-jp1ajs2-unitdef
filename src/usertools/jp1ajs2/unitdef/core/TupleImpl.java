package usertools.jp1ajs2.unitdef.core;

import java.util.List;

class TupleImpl implements Tuple {
	
	private final List<TupleEntry> values;

	public TupleImpl(final List<TupleEntry> list) {
		this.values = list;
	}

	@Override
	public String get(int index) {
		return index < values.size() ? values.get(index).value() : null;
	}

	@Override
	public String get(String key) {
		for(TupleEntry e: values){
			if(e.key().equals(key)){
				return e.value();
			}
		}
		return null;
	}
	
	public static class TupleEntry {
		private final String k;
		private final String v;
		TupleEntry(String key, String value){
			k = key;
			v = value;
		}
		TupleEntry(String value){
			k = "";
			v = value;
		}
		String key() {
			return k;
		}
		String value() {
			return v;
		}
	}
	
	@Override
	public String toString(){
		final StringBuilder sb = new StringBuilder();
		sb.append('(');
		for(final TupleEntry e : values){
			if(sb.length() > 1){
				sb.append(',');
			}
			if(e.key().length() > 0){
				sb.append(e.key());
				sb.append('=');
			}
			sb.append(e.value());
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
}
