package org.unclazz.jp1ajs2.unitdef.parameter;

/**
 * 追加書きオプション.
 */
public enum WriteOption {
	/**
	 * ファイルを新規に作成.
	 */
	NEW,
	/**
	 * 既存のファイルに情報を追加.
	 */
	ADD;
	public String getCode() {
		return name().toLowerCase();
	}
	public static WriteOption valueOfCode(final String code) {
		return valueOf(code.toUpperCase());
	}
}
