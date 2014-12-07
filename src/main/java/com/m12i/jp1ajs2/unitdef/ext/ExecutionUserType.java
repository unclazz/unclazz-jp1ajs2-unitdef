package com.m12i.jp1ajs2.unitdef.ext;

import com.m12i.jp1ajs2.unitdef.core.Unit;
import com.m12i.jp1ajs2.unitdef.util.ValueResolver;
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

	public static final ValueResolver<ExecutionUserType> VALUE_RESOLVER = new ValueResolver<ExecutionUserType>() {
		@Override
		public ExecutionUserType resolve(String rawValue) {
			return rawValue.equals("def") ? DEFINITION_USER : ENTRY_USER;
		}
	};
}
