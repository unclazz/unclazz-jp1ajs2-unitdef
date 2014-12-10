package com.m12i.jp1ajs2.unitdef.ext;

public enum DeleteOption {
	SAVE("sav"),
	DELETE("del");
	private final String code;
	public String getCode() {
		return code;
	}
	private DeleteOption(String code) {
		this.code = code;
	}
}
