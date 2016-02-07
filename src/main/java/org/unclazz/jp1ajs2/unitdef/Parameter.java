package org.unclazz.jp1ajs2.unitdef;

import org.unclazz.jp1ajs2.unitdef.util.CharSequential;

public interface Parameter extends Iterable<ParameterValue>, CharSequential {
	String getName();
	ParameterValue getValue(int i);
	<R> R getValue(int i, ParameterValueQuery<R> q);
	int getValueCount();
}
