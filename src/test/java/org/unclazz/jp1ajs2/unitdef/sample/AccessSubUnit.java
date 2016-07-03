package org.unclazz.jp1ajs2.unitdef.sample;

import org.unclazz.jp1ajs2.unitdef.Unit;
import org.unclazz.jp1ajs2.unitdef.Units;
import org.unclazz.jp1ajs2.unitdef.query.Queries;

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
		
		// Unit#query(UnitQuery) を使うとより柔軟なアクセスも可能になる
		printfln("unit.query(subUnit().nameStartsWith(\"BA\")).get(0).getFullQualifiedName() => %s",
				unit.query(Queries.children().nameStartsWith("BA").list()).get(0).getFullQualifiedName());
		
		printfln("unit.query(subUnit().hasParameter(\"de\")).get(0).getFullQualifiedName() => %s",
				unit.query(Queries.children().hasParameter("de").list()).get(0).getFullQualifiedName());
	}

	private static void printfln(final String format, final Object... args) {
		System.out.printf(format + System.getProperty("line.separator"), args);
	}
}
