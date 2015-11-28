package org.unclazz.jp1ajs2.unitdef.parameter;

/**
 * 対応する上位ジョブネットのスケジュールのルール番号.
 */
public final class LinkedRuleNumber {
	private final int ruleNumber;
	private final int linkedRuleNumber;
	
	public LinkedRuleNumber(final int ruleNo, final int linkedRuleNo) {
		this.ruleNumber = ruleNo;
		this.linkedRuleNumber = linkedRuleNo;
	}
	
	/**
	 * 当該ジョブネット側のルール番号を取得する.
	 * @return 当該ジョブネット側のルール番号
	 */
	public int getRuleNumber() {
		return ruleNumber;
	}
	/**
	 * 対応する上位ジョブネットのスケジュールのルール番号を取得する.
	 * @return 対応する上位ジョブネットのスケジュールのルール番号
	 */
	public int getLinkedRuleNumber() {
		return linkedRuleNumber;
	}
	@Override
	public String toString() {
		return String.format("ルール番号`%s` 対応する上位ジョブネットのスケジュールのルール番号は`%s`", ruleNumber, linkedRuleNumber);
	}
}
