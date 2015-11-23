package org.unclazz.jp1ajs2.unitdef.parameter;

/**
 * プランニンググループ直下のルートジョブネットの実行順序制御方式.
 */
public enum ConnectorControllingSyncOption {
	/**
	 * ジョブネットコネクタと同期して実行.
	 */
	SYNC,
	/**
	 * ジョブネットコネクタと非同期で実行.
	 */
	ASYNC;
}
