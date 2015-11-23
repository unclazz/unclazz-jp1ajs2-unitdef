package org.unclazz.jp1ajs2.unitdef.parameter;

/**
 * 環境変数.
 */
public interface EnvironmentVariable {
	/**
	 * 変数名を取得する.
	 * @return 変数名
	 */
	String getName();
	/**
	 * 変数値を取得する.
	 * @return 変数値
	 */
	String getValue();
}
