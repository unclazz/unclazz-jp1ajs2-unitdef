package org.unclazz.jp1ajs2.unitdef.parameter;

/**
 * ジョブ実行開始時刻.
 */
public interface StartTime {
	
	/**
	 * ルール番号.
	 * @return ルール番号
	 */
	public RuleNumber getRuleNumber();
	/**
	 * 相対時刻かどうか.
	 * @return {@code true}：相対時刻、{@code false}：絶対時刻
	 */
	public boolean isRelative();
	/**
	 * 時刻を取得する.
	 * @return 時刻
	 */
	public Time getTime();
}
