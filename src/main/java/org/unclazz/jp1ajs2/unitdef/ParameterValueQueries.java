package org.unclazz.jp1ajs2.unitdef;

import org.unclazz.jp1ajs2.unitdef.builder.UnitdefUtils;

public final class ParameterValueQueries {
	private ParameterValueQueries() {}
	
	private static final ParameterValueQuery<String> STRING =
			new ParameterValueQuery<String>() {
		@Override
		public String queryFrom(ParameterValue value) {
			return value.getRawCharSequence().toString();
		}
	};
	private static final ParameterValueQuery<CharSequence> CHAR_SEQUENCE =
			new ParameterValueQuery<CharSequence>() {
		@Override
		public CharSequence queryFrom(ParameterValue value) {
			return value.getRawCharSequence();
		}
	};
	private static final ParameterValueQuery<CharSequence> ESCAPED = 
			new ParameterValueQuery<CharSequence>() {
		@Override
		public CharSequence queryFrom(ParameterValue value) {
			return UnitdefUtils.escape(value.getRawCharSequence());
		}
	};
	private static final ParameterValueQuery<CharSequence> QUOTED = 
			new ParameterValueQuery<CharSequence>() {
		@Override
		public CharSequence queryFrom(ParameterValue value) {
			return UnitdefUtils.quote(value.getRawCharSequence());
		}
	};
	private static final ParameterValueQuery<Integer> INTEGER =
			new ParameterValueQuery<Integer>() {
		@Override
		public Integer queryFrom(ParameterValue value) {
			return Integer.parseInt(STRING.queryFrom(value));
		}
	};
	private static final ParameterValueQuery<Long> LONG_INTEGER =
			new ParameterValueQuery<Long>() {
		@Override
		public Long queryFrom(ParameterValue value) {
			return Long.parseLong(STRING.queryFrom(value));
		}
	};
	private static final ParameterValueQuery<Tuple> TUPLE =
			new ParameterValueQuery<Tuple>() {
		@Override
		public Tuple queryFrom(ParameterValue value) {
			return value.getTuple();
		}
	};
	
	public static final ParameterValueQuery<CharSequence> charSequence() {
		return CHAR_SEQUENCE;
	}
	public static final ParameterValueQuery<CharSequence> escaped() {
		return ESCAPED;
	}
	public static final ParameterValueQuery<Integer> integer() {
		return INTEGER;
	}
	public static final ParameterValueQuery<Long> longInteger() {
		return LONG_INTEGER;
	}
	public static final ParameterValueQuery<CharSequence> quoted() {
		return QUOTED;
	}
	public static final ParameterValueQuery<String> string() {
		return STRING;
	}
	public static final ParameterValueQuery<Tuple> tuple() {
		return TUPLE;
	}
	
}
