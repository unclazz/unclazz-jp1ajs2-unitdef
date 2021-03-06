package org.unclazz.jp1ajs2.unitdef.parameter;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.regex.Pattern;

import org.unclazz.jp1ajs2.unitdef.util.StringUtils;

/**
 * ファイル監視条件フラグ.
 */
public enum FileWatchConditionFlag {
	CREATE("c", "c：ファイルの作成を監視"),
	DELETE("d", "d：ファイルの削除を監視"),
	SIZE("s", "s：ファイルのサイズ変更を監視"),
	MODIFY("m", "m：ファイルの最終書き込み時刻変更を監視");
	
	private static final Pattern colon = Pattern.compile(":");
	
	private final String code;
	private final String description;
	
	private FileWatchConditionFlag(final String code, final String desc) {
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
	public static FileWatchConditionFlag valueOfCode(final CharSequence code) {
		for (final FileWatchConditionFlag cond : values()) {
			if (StringUtils.contentsAreEqual(cond.code, code)) {
				return cond;
			}
		}
		throw new IllegalArgumentException(String.format("Unknown value \"%s\".", code));
	}

	/**
	 * 指定されたコード値もしくはコード値の組み合わせに対応するインスタンスを返す.
	 * @param codes コード値もしくはコード値の組み合わせ
	 * @return インスタンスのリスト
	 * @throws IllegalArgumentException 指定されたコード値に対応するインスタンスが存在しない場合
	 */
	public static List<FileWatchConditionFlag> valueOfCodes(final CharSequence codes) {
		if (codes.length() == 0) {
			return Collections.emptyList();
		}
		final List<FileWatchConditionFlag> list = new LinkedList<FileWatchConditionFlag>();
		for (final String code : colon.split(codes)) {
			list.add(valueOfCode(code));
		}
		return list;
	}
}
