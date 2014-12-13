package com.m12i.jp1ajs2.unitdef;

/**
 * ジョブ実行開始時刻.
 */
public class StartTime {
	private final int ruleNo;
	private final boolean relative;
	private final int hh;
	private final int mi;
	
	StartTime(final int ruleNo, final boolean relative, final int hh, final int mi) {
		this.ruleNo = ruleNo;
		this.relative = relative;
		this.hh = hh;
		this.mi = mi;
	}
	/**
	 * ルール番号.
	 * @return ルール番号
	 */
	public int getRuleNo() {
		return ruleNo;
	}
	/**
	 * 相対時刻かどうか.
	 * @return {@code true}：相対時刻、{@code false}：絶対時刻
	 */
	public boolean isRelative() {
		return relative;
	}
	/**
	 * 時を取得する.
	 * @return 時
	 */
	public int getHh() {
		return hh;
	}
	/**
	 * 分を取得する.
	 * @return 分
	 */
	public int getMi() {
		return mi;
	}
	@Override
	public String toString() {
		return String.format("ルール番号`%d` ジョブネットの実行開始時刻は`%s %d:%d`",
				ruleNo, relative ? "相対時刻" : "絶対時刻",
				hh, mi);
	}
}
