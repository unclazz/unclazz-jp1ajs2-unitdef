package com.m12i.jp1ajs2.unitdef;

/**
 * 環境変数.
 */
public class EnvironmentVariable {
	private final String name;
	private final String value;
	public EnvironmentVariable(final String name, final String value) {
		this.name = name;
		this.value = value;
	}
	/**
	 * 変数名を取得する.
	 * @return 変数名
	 */
	public String getName() {
		return name;
	}
	/**
	 * 変数値を取得する.
	 * @return 変数値
	 */
	public String getValue() {
		return value;
	}
}
