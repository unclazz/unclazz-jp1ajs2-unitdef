package com.m12i.jp1ajs2.unitdef;

/**
 * 対応する上位ジョブネットのスケジュールのルール番号.
 */
public class LinkedRule {
	private final int ruleNo;
	private final int linkedRuleNo;
	LinkedRule(final int ruleNo, final int linkedRuleNo) {
		this.ruleNo = ruleNo;
		this.linkedRuleNo = linkedRuleNo;
	}
	/**
	 * 当該ジョブネット側のルール番号を取得する.
	 * @return 当該ジョブネット側のルール番号
	 */
	public int getRuleNo() {
		return ruleNo;
	}
	/**
	 * 対応する上位ジョブネットのスケジュールのルール番号を取得する.
	 * @return 対応する上位ジョブネットのスケジュールのルール番号
	 */
	public int getLinkedRuleNo() {
		return linkedRuleNo;
	}
	@Override
	public String toString() {
		return String.format("ルール番号`%s` 対応する上位ジョブネットのスケジュールのルール番号は`%s`", ruleNo, linkedRuleNo);
	}
}
