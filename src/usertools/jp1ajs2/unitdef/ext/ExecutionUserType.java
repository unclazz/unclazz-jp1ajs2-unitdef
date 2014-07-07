package usertools.jp1ajs2.unitdef.ext;

import usertools.jp1ajs2.unitdef.core.Unit;

public enum ExecutionUserType {
	ENTRY_USER("ent"),
	DEFINITION_USER("def");
	private final String abbr;
	public String getAbbr(Unit unit) {
		return abbr;
	}
	private ExecutionUserType(String abbr) {
		this.abbr = abbr;
	}
}
