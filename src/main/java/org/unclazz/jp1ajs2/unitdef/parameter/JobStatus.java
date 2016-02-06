package org.unclazz.jp1ajs2.unitdef.parameter;

public enum JobStatus {
	NO_PLAN("no plan", "未計画"), TIME_WAIT("time-wait", "開始時刻待ち"),
	TERM_WAIT("term-wait", "先行終了待ち"), HOLDING("holding", "保留中"),
	EXEC_WAIT("exec-wait", "実行待ち"), UNEXEC("unexec", "未実行終了"),
	UNEXEC_W("unexec-W", "未実行終了-W"), BYPASS("bypass", "計画未実行"),
	RUNNING("running", "実行中"), QUEUING("queuing", "キューイング"),
	AB_CONT("AB-cont", "異常検出実行中"), WA_CONT("WA-cont", "警告検出実行中"),
	NORMAL("normal", "正常終了"), NORMAL_FALSE("normal-false", "正常終了-偽"),
	WARNNING("warnning", "警告検出終了"), ABNORMAL("abnormal", "異常検出終了"),
	ABNORMAL_WR("abnormal-WR", "異常検出終了-WR"), EXEC_DEFFER("exec-deffer", "繰り越し未実行"),
	INVALID_SEQ("invalid-seq", "順序不正"), INTERRUPT("interrupt", "中断"),
	KILL("kill", "強制終了"), KILL_WR("kill-WR", "強制終了-WR"),
	FAIL("fail", "起動失敗"), FAIL_WR("fail-WR", "起動失敗-WR"),
	UNKNOWN("unknown", "終了状態不明"), UNKNOWN_WR("unknown-WR", "終了状態不明-WR"),
	SHUTDOWN("shutdown", "閉塞"), CONDITION_WAIT("condition-wait", "起動条件待ち"),
	MONITORING("monitoring", "監視中"), UNEXEC_MONITOR("unexec-monitor", "監視未起動終了"),
	MONITOR_CLOSE("monitor-close", "監視打ち切り終了"), MONITOR_INTRPT("monitor-intrpt", "監視中断"),
	MONITOR_NORMAL("monitor-normal", "監視正常終了"), END_DELAY("end-delay", "終了遅延"),
	START_DELAY("start-delay","開始遅延"), NEST_END_DELAY("nest-end-delay", "ネスト終了遅延"),
	NEST_START_DELAY("nest-start-delay", "ネスト開始遅延");
	
	private final String code;
	private final String label;
	private JobStatus(final String code, final String label) {
		this.code = code;
		this.label = label;
	}
	
	public String getCode() {
		return code;
	}
	public String getLable() {
		return label;
	}
	
	/**
	 * コードに対応するインスタンスを返す.
	 * @param code コード
	 * @return インスタンス
	 * @throws IllegalArgumentException 指定されたコードに該当するインスタンスが存在しない場合
	 */
	public static JobStatus valueOfCode(String code) {
		for (final JobStatus status : values()) {
			if (status.code.equals(code)) {
				return status;
			}
		}
		throw new IllegalArgumentException("Invalid code");
	}
}
