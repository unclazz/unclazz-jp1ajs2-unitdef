package org.unclazz.jp1ajs2.unitdef.parser;

import org.unclazz.jp1ajs2.unitdef.parameter.EnvironmentVariable;

final class DefaultEnvironmentVariable implements EnvironmentVariable {
	private final String name;
	private final String value;
	DefaultEnvironmentVariable(final String name, final String value) {
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
