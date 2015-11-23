package org.unclazz.jp1ajs2.unitdef;


public interface Parameter extends Iterable<ParameterValue> {
	String getName();
	ParameterValue getValue(int i);
	<R> R getValue(int i, ParameterValueQuery<R> q);
	int getValueCount();
}
