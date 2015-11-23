package org.unclazz.jp1ajs2.unitdef;

import org.unclazz.jp1ajs2.unitdef.builder.UnitdefUtils;

public final class ParameterValueQueries {
	private ParameterValueQueries() {}
	
	public static final ParameterValueQuery<String> STRING =
			new ParameterValueQuery<String>() {
		@Override
		public String queryFrom(ParameterValue value) {
			return value.getRawCharSequence().toString();
		}
	};
	public static final ParameterValueQuery<CharSequence> CHAR_SEQUENCE =
			new ParameterValueQuery<CharSequence>() {
		@Override
		public CharSequence queryFrom(ParameterValue value) {
			return value.getRawCharSequence();
		}
	};
	public static final ParameterValueQuery<CharSequence> ESCAPED = 
			new ParameterValueQuery<CharSequence>() {
		@Override
		public CharSequence queryFrom(ParameterValue value) {
			return UnitdefUtils.escape(value.getRawCharSequence());
		}
	};
	public static final ParameterValueQuery<CharSequence> QUOTED = 
			new ParameterValueQuery<CharSequence>() {
		@Override
		public CharSequence queryFrom(ParameterValue value) {
			return UnitdefUtils.quote(value.getRawCharSequence());
		}
	};
	public static final ParameterValueQuery<Integer> INTEGER =
			new ParameterValueQuery<Integer>() {
		@Override
		public Integer queryFrom(ParameterValue value) {
			return Integer.parseInt(STRING.queryFrom(value));
		}
	};
	public static final ParameterValueQuery<Tuple> TUPLE =
			new ParameterValueQuery<Tuple>() {
		@Override
		public Tuple queryFrom(ParameterValue value) {
			return value.getTuple();
		}
	};
}
