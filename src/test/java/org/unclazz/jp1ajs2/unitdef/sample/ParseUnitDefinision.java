package org.unclazz.jp1ajs2.unitdef.sample;

import java.io.File;
import java.nio.charset.Charset;
import java.util.List;

import org.unclazz.jp1ajs2.unitdef.Unit;
import org.unclazz.jp1ajs2.unitdef.Units;
import static org.unclazz.jp1ajs2.unitdef.query.Queries.*;

public final class ParseUnitDefinision {

	public static void main(String[] args) {
		// ユニット定義を文字シーケンスからパース
		final List<Unit> unitsFromString = Units.fromCharSequence(
				"unit=FOO,,,;{ty=n;cm=\"Sample Jobmet Unit\";sz=2×3;}");
		
		// パース結果のユニット数をチェック
		printfln("units.size() => %s", unitsFromString.size());
		
		// 1つめのルート・ユニットの名前
		printfln("units.get(0).getName() => %s", unitsFromString.get(0).getName());
		
		// 同ユニットのユニット種別（tyパラメータ）
		printfln("units.get(0).getType() => %s", unitsFromString.get(0).getType());
		
		// ユニット種別にアクセスするためのもう1つの方法
		printfln("units.get(0).getFullQualifiedName() => %s", unitsFromString.get(0).getFullQualifiedName());
		
		// ユニット定義をファイル・コンテンツからパース
		final Unit unitFromFile = Units
				.fromFile(new File("src/test/resources/0001.txt"),
						Charset.forName("Shift_JIS")).get(0);
		
		// ルートユニットおよびその子孫ユニットのうちコメント（cmパラメータ）を持つユニットの名前を問合せる
		printfln("unitFromFile.query(itSelfAndDescendants()"
				+ ".hasParameter(\"cm\").anyValue().theirFqn().list()) => \n\t%s",
				unitFromFile.query(itSelfAndDescendants()
				.hasParameter("cm").anyValue().theirFqn().list()));
	}

	private static void printfln(final String format, final Object... args) {
		System.out.printf(format + System.getProperty("line.separator"), args);
	}
}
