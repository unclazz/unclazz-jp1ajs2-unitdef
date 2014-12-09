package com.m12i.jp1ajs2.unitdef.ext;


public enum WriteOption {
	NEW, ADD;

	public static final ValueResolver<WriteOption> VALUE_RESOLVER = new ValueResolver<WriteOption>() {
		@Override
		public WriteOption resolve(String rawValue) {
			return valueOf(rawValue.toUpperCase());
		}
	};
}
