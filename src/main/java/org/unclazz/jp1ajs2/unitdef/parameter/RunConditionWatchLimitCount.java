package org.unclazz.jp1ajs2.unitdef.parameter;

/**
 * ユニット定義パラメータwc（起動条件監視の最大回数）を表すインターフェース.
 */
public interface RunConditionWatchLimitCount {
	/**
	 * 起動条件監視およびその最大回数の指定形式を表す列挙型.
	 */
	public static enum LimitationType {
		/**
		 * 起動条件監視を行わない.
		 */
		NO_WATCHING, 
		/**
		 * 回数を指定する.
		 */
		LIMITTED, 
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
	 * @return 最大回数
	 */
	int getCount();
	/**
	 * @return 指定方法
	 */
	LimitationType getType();
}
