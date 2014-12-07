package com.m12i.jp1ajs2.unitdef.core;


final class TupleParamValue implements ParamValue {
	private final Tuple t;
	private String s;
	public TupleParamValue(final Tuple t) {
		this.t = t;
	}
	@Override
	public String getUnclassifiedValue() {
		return toStringHelper();
	}
	@Override
	public Tuple getTuploidValue() {
		return t;
	}
	@Override
	public boolean is(ParamValueType type) {
		return ParamValueType.TUPLOID == type;
	}
	@Override
	public String getStringValue() {
		return toStringHelper();
	}
	@Override
	public String toString() {
		return toStringHelper();
	}
	private String toStringHelper() {
		return s == null ? (s = t.toString()) : s;
	}
}
