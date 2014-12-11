package com.m12i.jp1ajs2.unitdef.ext;

/**
 * プランニンググループ直下のルートジョブネットの実行順序制御方式.
 */
public enum ConnectorOrderingSyncOption {
	/**
	 * ジョブネットコネクタと同期して実行.
	 */
	SYNC,
	/**
	 * ジョブネットコネクタと非同期で実行.
	 */
	ASYNC;
}
