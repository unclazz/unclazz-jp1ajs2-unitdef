package org.unclazz.jp1ajs2.unitdef.parameter;

/**
 * 削除オプション.
 */
public enum DeleteOption {
	/**
	 * ファイルを保存する.
	 */
	SAVE("sav"),
	/**
	 * ファイルを削除する.
	 */
	DELETE("del");

	private final String code;
	
	private DeleteOption(String code) {
		this.code = code;
	}
	
	public String getCode() {
		return code;
	}
	
	/**
	 * コードに対応するインスタンスを返す.
	 * @param code コード
	 * @return インスタンス
	 * @throws IllegalArgumentException 指定されたコードに該当するインスタンスが存在しない場合
	 */
	public static DeleteOption valueOfCode(final String code) {
		for (final DeleteOption o : values()) {
			if (o.code.equals(code)) {
				return o;
			}
		}
		throw new IllegalArgumentException();
	}
}
