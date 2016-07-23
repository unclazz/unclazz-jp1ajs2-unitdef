package org.unclazz.jp1ajs2.unitdef.parameter;

/**
 * ユニット定義パラメータcftd（起算スケジュール定義）を表すインターフェース.
 * <p>起算スケジュールはスケジュールルールなどから導き出された実行開始予定日に対して、
 * 「何日前」「何日後」というかたちで実行開始予定日をずらすためのパラメータである。
 * パラメータには何日ずらすかを運用日換算で指定する数値（{@link #getBusinessDays()}}）と、
 * 実際にずらすことが可能な上限を通常日付換算で指定する数値（{@link #getDeadlineDays()}）が含まれる。</p>
 */
public interface StartDateAdjustment {
	/**
	 * 実行開始予定日の調整の形式を表す列挙型.
	 */
	public static enum AdjustmentType {
		/**
		 * 実行開始予定日の調整を行わない.
		 */
		NOT_ADJUST("no"),
		/**
		 * 実行開始予定日の「何日前」という形式で調整する.
		 */
		BEFORE("be"),
		/**
		 * 実行開始予定日の「何日後」という形式で調整する.
		 */
		AFTER("af");

		private final String code;
		private AdjustmentType(final String code) {
			this.code = code;
		}
		
		/**
		 * @param code コード
		 */
		public String getCode() {
			return code;
		}
		/**
		 * コード値に該当する列挙型のインスタンスを返す.
		 * @param code コード値
		 * @return インスタンス
		 * @throws IllegalArgumentException 該当するインスタンスが見つからない場合
		 */
		public static AdjustmentType valueOfCode(final String code) {
			for (final AdjustmentType t : values()) {
				if (t.code.equals(code)) {
					return t;
				}
			}
			throw new IllegalArgumentException(String.format("Invalid code \"%s\".", code));
		}
	}
	/**
	 * @return ルール番号
	 */
	RuleNumber getRuleNumber();
	/**
	 * @return 実行開始予定日の調整形式
	 */
	AdjustmentType getType();
	/**
	 * @return 実行開始予定日をずらす運用日数
	 */
	int getBusinessDays();
	/**
	 * @return 実行開始予定日をずらすことが可能な上限の通常日数
	 */
	int getDeadlineDays();
}
