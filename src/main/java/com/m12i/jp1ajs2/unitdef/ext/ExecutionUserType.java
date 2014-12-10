package com.m12i.jp1ajs2.unitdef.ext;

import com.m12i.jp1ajs2.unitdef.Unit;
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
	private final String code;
	public String getCode(Unit unit) {
		return code;
	}
	private ExecutionUserType(String abbr) {
		this.code = abbr;
	}
}
