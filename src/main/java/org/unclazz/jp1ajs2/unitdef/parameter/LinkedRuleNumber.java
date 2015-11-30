package org.unclazz.jp1ajs2.unitdef.parameter;

/**
 * ユニット定義パラメータln（対応する上位ジョブネットのスケジュールのルール番号）を表わすオブジェクト.
 * 
 */
public final class LinkedRuleNumber {
	/**
	 * 引数で指定されたルール番号をリンク先とするインスタンスを生成する.
	 * 自身のルール番号は1である。
	 * @param linkTarget リンク先
	 * @return インスタンス
	 */
	public static LinkedRuleNumber ofTarget(final RuleNumber linkTarget) {
		return new LinkedRuleNumber(RuleNumber.DEFAULT, linkTarget);
	}
	/**
	 * {@link #ofTarget(RuleNumber)}を参照のこと.
	 * @param linkTarget リンク先
	 * @return インスタンス
	 */
	public static LinkedRuleNumber ofTarget(final int linkTarget) {
		return new LinkedRuleNumber(RuleNumber.DEFAULT, RuleNumber.of(linkTarget));
	}
	
	private final RuleNumber ruleNumber;
	private final RuleNumber linkedRuleNumber;
	
	private LinkedRuleNumber(final RuleNumber selfRuleNumber, final RuleNumber linkTarget) {
		if (selfRuleNumber == null || linkTarget == null) {
			throw new IllegalArgumentException();
		}
		this.ruleNumber = selfRuleNumber;
		this.linkedRuleNumber = linkTarget;
	}
	
	/**
	 * 当該ジョブネット側のルール番号を取得する.
	 * @return 当該ジョブネット側のルール番号
	 */
	public RuleNumber getRuleNumber() {
		return ruleNumber;
	}
	/**
	 * 対応する上位ジョブネットのスケジュールのルール番号を取得する.
	 * @return 対応する上位ジョブネットのスケジュールのルール番号
	 */
	public RuleNumber getLinkedRuleNumber() {
		return linkedRuleNumber;
	}
	/**
	 * このインスタンス（レシーバ）をもとに引数で指定されたルール番号を持つ新しいインスタンスを生成する.
	 * @param self 新しいインスタンスが持つルール番号
	 * @return インスタンス
	 */
	public LinkedRuleNumber at(RuleNumber self) {
		return new LinkedRuleNumber(self, this.getLinkedRuleNumber());
	}
	/**
	 * {@link #at(RuleNumber)}を参照のこと.
	 * @param self 新しいインスタンスが持つルール番号
	 * @return インスタンス
	 */
	public LinkedRuleNumber at(int self) {
		return new LinkedRuleNumber(RuleNumber.of(self), this.getLinkedRuleNumber());
	}
	@Override
	public String toString() {
		return String.format("ルール番号`%s` 対応する上位ジョブネットのスケジュールのルール番号は`%s`", ruleNumber, linkedRuleNumber);
	}
}
