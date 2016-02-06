package org.unclazz.jp1ajs2.unitdef.parameter;


/**
 * ユニット定義パラメータeu（ジョブ実行時のJP1ユーザ）を表わすオブジェクト.
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
	
	/**
	 * コードに対応するインスタンスを返す.
	 * @param code コード
	 * @return インスタンス
	 * @throws IllegalArgumentException 指定されたコードに該当するインスタンスが存在しない場合
	 */
	public static ExecutionUserType valueOfCode(final String code) {
		for (final ExecutionUserType t : values()) {
			if (t.code.equals(code)) {
				return t;
			}
		}
		throw new IllegalArgumentException(String.format("Invalid code \"%s\".", code));
	}
	
	@Override
	public String toString() {
		return String.format("ジョブ実行時のJP1ユーザーは`%s`", desc);
	}
}
