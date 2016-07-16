package org.unclazz.jp1ajs2.unitdef.sample;

import org.unclazz.jp1ajs2.unitdef.Unit;
import org.unclazz.jp1ajs2.unitdef.Units;
import static org.unclazz.jp1ajs2.unitdef.query.UnitQueries.*;

public final class AccessSubUnit {

	public static void main(String[] args) {
		// ユニット定義を文字シーケンスからパース
		final Unit unit = Units.fromCharSequence(
				"unit=FOO,,,;{ty=n;cm=\"Sample Jobmet Unit\";sz=2×3;"
				+ "el=BAR,pj,+80 +48;el=BAZ,n,+240 +144;"
				+ "unit=BAR,,,;{ty=pj;cm=\"Sub Unit 0\";}"
				+ "unit=BAZ,,,;{ty=n;cm=\"Sub Unit 1\";de=n;}"
				+ "}").get(0);
		
		// ユニットはツリー構造を持つ
		// サブユニットにアクセスするには Unit#getSubUnit(String) や Unit#getSubUnits() を用いる
		printfln("unit.getSubUnit(\"BAR\").getFullQualifiedName() => %s",
				unit.getSubUnit("BAR").getFullQualifiedName());
		
		// Unit#query(Query) メソッドをクエリに適用することで柔軟なアクセスも可能になる
		printfln("unit.query(children().nameStartsWith(\"BA\").one()).getFullQualifiedName() => %s",
				unit.query(children().nameStartsWith("BA").one()).getFullQualifiedName());
		printfln("unit.query(children().hasParameter(\"de\").anyValue().one()).getFullQualifiedName() => %s",
				unit.query(children().hasParameter("de").anyValue().one()).getFullQualifiedName());
	}

	private static void printfln(final String format, final Object... args) {
		System.out.printf(format + System.getProperty("line.separator"), args);
	}
}
