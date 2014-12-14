package com.m12i.jp1ajs2.unitdef.parser;

import com.m12i.jp1ajs2.unitdef.EnvironmentVariable;

class EnvironmentVariableImpl implements EnvironmentVariable {
	private final String name;
	private final String value;
	EnvironmentVariableImpl(final String name, final String value) {
		this.name = name;
		this.value = value;
	}
	@Override
	public String getName() {
		return name;
	}
	@Override
	public String getValue() {
		return value;
	}
}
