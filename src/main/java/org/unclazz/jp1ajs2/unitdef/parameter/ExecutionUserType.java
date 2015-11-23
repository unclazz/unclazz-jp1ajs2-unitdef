package org.unclazz.jp1ajs2.unitdef.parameter;


/**
 * ジョブ実行時のJP1ユーザ.
 */
public enum ExecutionUserType {
	/**
	 * "ent": ジョブネットを登録したJP1ユーザーをジョブ実行時のJP1ユーザーとする.
	 */
	ENTRY_USER("ent", "ent: ジョブネットを登録したJP1ユーザーをジョブ実行時のJP1ユーザーとする"),
	/**
	 * "def": ジョブを所有するJP1ユーザーをジョブ実行時のJP1ユーザーとする.
	 */
	DEFINITION_USER("def", "def: ジョブを所有するJP1ユーザーをジョブ実行時のJP1ユーザーとする.");
	
	private final String code;
	private final String desc;
	public String getCode() {
		return code;
	}
	public String getDescription() {
		return desc;
	}
	private ExecutionUserType(String code, String desc) {
		this.code = code;
		this.desc = desc;
	}
	
	public static ExecutionUserType forCode(final String code) {
		for (final ExecutionUserType t : values()) {
			if (t.code.equals(code)) {
				return t;
			}
		}
		return null;
	}
	
	@Override
	public String toString() {
		return String.format("ジョブ実行時のJP1ユーザーは`%s`", desc);
	}
}
