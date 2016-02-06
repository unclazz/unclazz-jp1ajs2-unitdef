package org.unclazz.jp1ajs2.unitdef;


public interface ParameterValue {
	CharSequence getRawCharSequence();
	Tuple getTuple();
	ParameterValueType getType();
	<T> T query(ParameterValueQuery<T> r);
	boolean contentEquals(CharSequence chars);
	boolean contentEquals(ParameterValue value);
}
