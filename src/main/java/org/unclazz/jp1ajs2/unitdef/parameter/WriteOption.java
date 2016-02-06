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
	
	/**
	 * ユニット定義ファイルで利用されるコード値を返す.
	 * @return コード値
	 */
	public String getCode() {
		return name().toLowerCase();
	}
	
	/**
	 * ユニット定義ファイルで利用されるコード値に対応するインスタンスを返す.
	 * @param code コード値
	 * @return インスタンス
	 * @throws IllegalArgumentException 指定されたコード値に対応するインスタンスが存在しない場合
	 */
	public static WriteOption valueOfCode(final String code) {
		return valueOf(code.toUpperCase());
	}
}
