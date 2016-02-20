package org.unclazz.jp1ajs2.unitdef.parameter;

/**
 * ユニット定義パラメータcy（ジョブネットの処理サイクル）を表わすオブジェクト.
 */
public final class ExecutionCycle {
	/**
	 * 処理サイクルの単位.
	 */
	public static enum CycleUnit {
		YEAR, MONTH, WEEK, DAY;
		public String toJapaneseString() {
			switch (this) {
			case YEAR:
				return "年";
			case MONTH:
				return "月";
			case WEEK:
				return "週";
			case DAY:
				return "日";
			default:
				return null;
			}
		}
		public static CycleUnit valueOfCode(final CharSequence code) {
			final char initial = Character.toUpperCase(code.charAt(0));
			for (final CycleUnit u : values()) {
				if (u.name().charAt(0) == initial) {
					return u;
				}
			}
			throw new IllegalArgumentException();
		}
	}
	
	/**
	 * 指定された間隔と単位をもとに新しいインスタンスを生成する.
	 * @param interval 間隔
	 * @param unit 単位
	 * @return インスタンス
	 */
	public static ExecutionCycle of(final int interval, final CycleUnit unit) {
		return new ExecutionCycle(RuleNumber.DEFAULT, interval, unit);
	}
	
	private final RuleNumber ruleNumber;
	private final int interval;
	private final CycleUnit cycleUnit;
	
	private ExecutionCycle (RuleNumber ruleNumber, int interval, CycleUnit cycleUnit) {
		this.ruleNumber = ruleNumber;
		this.interval = interval;
		this.cycleUnit = cycleUnit;
	}
	
	/**
	 * ルール番号を取得する.
	 * @return ルール番号
	 */
	public RuleNumber getRuleNumber() {
		return ruleNumber;
	}
	/**
	 * 間隔を取得する.
	 * @return 間隔
	 */
	public int getInterval() {
		return interval;
	}
	/**
	 * 単位を習得する.
	 * @return 単位
	 */
	public CycleUnit getCycleUnit() {
		return cycleUnit;
	}
	/**
	 * このインスタンス（レシーバ）をもとに新しいインスタンスを生成する.
	 * @param ruleNumber 新しいインスタンスのルール番号
	 * @return インスタンス
	 */
	public ExecutionCycle at(final RuleNumber ruleNumber) {
		return new ExecutionCycle(ruleNumber, interval, cycleUnit);
	}
	/**
	 * {@link #at(RuleNumber)}を参照のこと.
	 * @param ruleNumber 新しいインスタンスのルール番号
	 * @return インスタンス
	 */
	public ExecutionCycle at(final int ruleNumber) {
		return at(RuleNumber.of(ruleNumber));
	}
	
	@Override
	public String toString() {
		return String.format("ルール番号`%s` ジョブネットの処理サイクルは`%s%s毎`",
				ruleNumber, interval, cycleUnit.toJapaneseString());
	}
}
