package org.unclazz.jp1ajs2.unitdef.parameter;

/**
 * 実行打ち切り時間が経過したあとのジョブの状態.
 */
public enum ExecutionTimedOutStatus {
	KILLED("kl", "kl：強制終了"),
	NORMAL_ENDED("nr", "nr：正常終了"),
	WANING_ENDED("wr", "wr：警告検出終了"),
	ABNORMAL_ENDED("an", "an：異常検出終了");
	
	private final String code;
	private final String desc;
	
	private ExecutionTimedOutStatus(final String code, final String desc) {
		this.code = code;
		this.desc = desc;
	}

	public String getCode() {
		return code;
	}
	public String getDescription() {
		return desc;
	}
	
	/**
	 * コードに対応するインスタンスを返す.
	 * @param code コード
	 * @return インスタンス
	 * @throws IllegalArgumentException 指定されたコードに該当するインスタンスが存在しない場合
	 */
	public static ExecutionTimedOutStatus valueOfCode(final String code) {
		for (final ExecutionTimedOutStatus c : values()) {
			if (c.code.equals(code)) {
				return c;
			}
		}
		throw new IllegalArgumentException(String.format("Invalid code \"%s\".", code));
	}
	
	@Override
	public String toString() {
		return String.format("実行打ち切り時間が経過したあとのジョブの状態は`%s`", this.getDescription());
	}
}
