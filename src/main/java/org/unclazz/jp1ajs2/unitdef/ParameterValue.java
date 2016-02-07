package org.unclazz.jp1ajs2.unitdef;

import org.unclazz.jp1ajs2.unitdef.util.CharSequential;

public interface ParameterValue extends CharSequential {
	CharSequence getRawCharSequence();
	Tuple getTuple();
	ParameterValueType getType();
	<T> T query(ParameterValueQuery<T> r);
}
