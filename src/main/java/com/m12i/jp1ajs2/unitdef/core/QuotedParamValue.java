package com.m12i.jp1ajs2.unitdef.core;


final class QuotedParamValue implements ParamValue {
	private final String s;
	public QuotedParamValue(final String s) {
		this.s = s;
	}
	@Override
	public String getUnclassifiedValue() {
		return s;
	}
	@Override
	public Tuple getTuploidValue() {
		return null;
	}
	@Override
	public boolean is(ParamValueType type) {
		return ParamValueType.STRING == type;
	}
	@Override
	public String getStringValue() {
		return s;
	}
	@Override
	public String toString() {
		return "\"" + s.replaceAll("(#|\")", "#$1") + "\"";
	}
}
