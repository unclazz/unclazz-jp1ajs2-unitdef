package org.unclazz.jp1ajs2.unitdef;

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
	
	public static ExecutionTimedOutStatus forCode(final String code) {
		for (final ExecutionTimedOutStatus c : values()) {
			if (c.code.equals(code)) {
				return c;
			}
		}
		return null;
	}
	
	@Override
	public String toString() {
		return String.format("実行打ち切り時間が経過したあとのジョブの状態は`%s`", this.getDescription());
	}
}
