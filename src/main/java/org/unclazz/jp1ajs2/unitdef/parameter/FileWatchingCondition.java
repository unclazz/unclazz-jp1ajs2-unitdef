package org.unclazz.jp1ajs2.unitdef.parameter;

import java.util.ArrayList;
import java.util.List;

/**
 * ファイル監視条件.
 */
public enum FileWatchingCondition {
	CREATE("c", "c：ファイルの作成を監視"),
	DELETE("d", "d：ファイルの削除を監視"),
	SIZE("s", "s：ファイルのサイズ変更を監視"),
	MODIFY("m", "m：ファイルの最終書き込み時刻変更を監視");
	
	private final String code;
	private final String description;
	
	private FileWatchingCondition(final String code, final String desc) {
		this.code = code;
		this.description = desc;
	}
	
	public String getCode() {
		return code;
	}
	
	public String getDescription() {
		return description;
	}

	/**
	 * 指定されたコード値に対応するインスタンスを返す.
	 * @param code コード値
	 * @return インスタンス
	 * @throws IllegalArgumentException 指定されたコード値に対応するインスタンスが存在しない場合
	 */
	public static FileWatchingCondition valueOfCode(final String code) {
		for (final FileWatchingCondition cond : values()) {
			if (cond.code.equals(code)) {
				return cond;
			}
		}
		throw new IllegalArgumentException(String.format("Unknown value \"%s\".", code));
	}

	/**
	 * 指定されたコード値もしくはコード値の組み合わせに対応するインスタンスを返す.
	 * @param code コード値もしくはコード値の組み合わせ
	 * @return インスタンスのリスト
	 * @throws IllegalArgumentException 指定されたコード値に対応するインスタンスが存在しない場合
	 */
	public static List<FileWatchingCondition> valueOfCodes(final String codes) {
		final List<FileWatchingCondition> list = new ArrayList<FileWatchingCondition>();
		for (final String code : codes.split(":")) {
			list.add(valueOfCode(code));
		}
		return list;
	}
}
