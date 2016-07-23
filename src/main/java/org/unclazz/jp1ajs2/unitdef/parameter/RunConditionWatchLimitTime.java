package org.unclazz.jp1ajs2.unitdef.parameter;

/**
 * ユニット定義パラメータwt（起動条件監視の終了時刻）を表すインターフェース.
 */
public interface RunConditionWatchLimitTime {
	/**
	 * 起動条件監視およびその終了時刻の指定形式を表す列挙型.
	 */
	public static enum LimitationType {
		/**
		 * 起動条件監視を行わない.
		 */
		NO_WATCHING, 
		/**
		 * 絶対時刻で指定する.
		 */
		ABSOLUTE_TIME, 
		/**
		 * 相対時間で指定する.
		 */
		RELATIVE_TIME, 
		/**
		 * 無制限.
		 */
		UNLIMITTED
	}
	
	/**
	 * @return ルール番号
	 */
	RuleNumber getRuleNumber();
	/**
	 * @return 終了時間
	 */
	Time getTime();
	/**
	 * @return 指定方法
	 */
	LimitationType getType();
}
