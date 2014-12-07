package com.m12i.jp1ajs2.unitdef.ext;

public enum DeleteOption {
	SAVE("sav"),
	DELETE("del");
	private final String abbr;
	public String getAbbr() {
		return abbr;
	}
	private DeleteOption(String abbr) {
		this.abbr = abbr;
	}
}
