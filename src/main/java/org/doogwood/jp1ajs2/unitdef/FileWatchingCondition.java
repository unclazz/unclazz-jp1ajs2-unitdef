package org.doogwood.jp1ajs2.unitdef;

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

	public static List<FileWatchingCondition> forCode(final String code) {
		final List<FileWatchingCondition> list = new ArrayList<FileWatchingCondition>();
		for (final FileWatchingCondition c : values()) {
			if (code.contains(c.code)) {
				list.add(c);
			}
		}
		return list;
	}
}
