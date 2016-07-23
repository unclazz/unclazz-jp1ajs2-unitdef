package org.unclazz.jp1ajs2.unitdef.parameter;

/**
 * ユニット定義パラメータshd（実行日の振り替えの猶予日数）を表わすインターフェース.
 */
public interface StartDateCompensationDeadline {
	/**
	 * @return ルール番号
	 */
	RuleNumber getRuleNumber();
	/**
	 * @return 猶予日数
	 */
	int getDeadlineDays();
}
