package org.unclazz.jp1ajs2.unitdef.parameter;

/**
 * ユニット定義パラメータsh（実行日の振り替え方法）を表わすインターフェース.
 */
public interface StartDateCompensation {
	/**
	 * 実行日の振り替えの方法を表す列挙型.
	 */
	public static enum CompensationMethod {
		/**
		 * 実行予定日が休業日に当たる場合、実行予定日の前の日に振り替え.
		 */
		BEFORE("be"),
		/**
		 * 実行予定日が休業日に当たる場合、実行予定日のあとの日に振り替え.
		 */
		AFTER("af"),
		/**
		 * 実行予定日が休業日に当たる場合、ジョブネットを実行しない.
		 */
		CANCEL("ca"),
		/**
		 * 実行予定日が休業日であっても実行する.
		 */
		NOT_CONSIDER("no");
		
		private final String code;
		private CompensationMethod(final String code) {
			this.code = code;
		}
		/**
		 * @return コード値
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
		public static final CompensationMethod valueOfCode(final String code){
			for(final CompensationMethod t : values()){
				if(t.getCode().equals(code)){
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
	 * @return 振り替え方法
	 */
	CompensationMethod getMethod();
}
