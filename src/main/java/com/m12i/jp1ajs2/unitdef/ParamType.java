package com.m12i.jp1ajs2.unitdef;

public enum ParamType {
	// TODO
	CM("cm", ""),
	TY("ty", "");
	
	private final String code;
	private final String desc;
	private ParamType (final String code, final String desc) {
		this.code = code;
		this.desc = desc;
	}
	public String getCode() {
		return code;
	}
	public String getDesctiption() {
		return desc;
	}
	
	public static ParamType forCode(final String code) {
		final String lc = code.toLowerCase();
		for (final ParamType t : values()) {
			if (t.code.equals(lc)) {
				return t;
			}
		}
		throw new IllegalArgumentException();
	}
}
