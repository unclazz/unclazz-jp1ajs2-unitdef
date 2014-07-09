package usertools.jp1ajs2.unitdef.ext;

import usertools.jp1ajs2.unitdef.core.Unit;
/**
 * ジョブ実行時のJP1ユーザ.
 */
public enum ExecutionUserType {
	/**
	 * "ent": ジョブネットを登録したJP1ユーザーをジョブ実行時のJP1ユーザーとする.
	 */
	ENTRY_USER("ent"),
	/**
	 * "def": ジョブを所有するJP1ユーザーをジョブ実行時のJP1ユーザーとする.
	 */
	DEFINITION_USER("def");
	private final String abbr;
	public String getAbbr(Unit unit) {
		return abbr;
	}
	private ExecutionUserType(String abbr) {
		this.abbr = abbr;
	}
}
