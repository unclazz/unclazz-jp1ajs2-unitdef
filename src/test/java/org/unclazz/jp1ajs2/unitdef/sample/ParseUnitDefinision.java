package org.unclazz.jp1ajs2.unitdef.sample;

import java.util.List;

import org.unclazz.jp1ajs2.unitdef.Unit;
import org.unclazz.jp1ajs2.unitdef.Units;

public final class ParseUnitDefinision {

	public static void main(String[] args) {
		// ユニット定義を文字シーケンスからパース
		final List<Unit> units = Units.fromCharSequence(
				"unit=FOO,,,;{ty=n;cm=\"Sample Jobmet Unit\";sz=2×3;}");
		
		// パース結果のユニット数をチェック
		printfln("units.size() => %s", units.size());
		
		// 1つめのルート・ユニットの名前
		printfln("units.get(0).getName() => %s", units.get(0).getName());
		
		// 同ユニットのユニット種別（tyパラメータ）
		printfln("units.get(0).getType() => %s", units.get(0).getType());
		
		// ユニット種別にアクセスするためのもう1つの方法
		printfln("units.get(0).getFullQualifiedName() => %s", units.get(0).getFullQualifiedName());
	}

	private static void printfln(final String format, final Object... args) {
		System.out.printf(format + System.getProperty("line.separator"), args);
	}
}
