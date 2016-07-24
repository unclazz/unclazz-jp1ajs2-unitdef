package org.unclazz.jp1ajs2.unitdef.sample;

import static org.unclazz.jp1ajs2.unitdef.query.UnitQueries.*;

import org.unclazz.jp1ajs2.unitdef.Unit;
import org.unclazz.jp1ajs2.unitdef.Units;

public final class AccessParameterValue {

	public static void main(String[] args) {
		// ユニット定義を文字シーケンスからパース
		final Unit unit = Units.fromCharSequence(
				"unit=FOO,,,;{ty=n;cm=\"Sample Jobmet Unit\";sz=2×3;"
				+ "el=BAR,pj,+80 +48;el=BAZ,n,+240 +144;"
				+ "unit=BAR,,,;{ty=pj;cm=\"Sub Unit 0\";tho=0;}"
				+ "unit=BAZ,,,;{ty=n;cm=\"Sub Unit 1\";tho=1;wth=0;}"
				+ "}").get(0);
		
		// ユニット種別にアクセスするためのもう1つの方法
		// UnitQueriesが提供するUnitQueryインスタンスを使用する
		printfln("unit.query(ty()) => %s", unit.query(ty().one()));

		// 同ユニットのコメントを取得
		printfln("unit.query(cm()) => %s", unit.query(cm().list()));
		
		// 同ユニットのマップサイズ（szパラメータ）からマップのタテxヨコを取得
		printfln("unit.query(sz()) => %s", unit.query(sz().list()));
		
		// ユニット定義パラメータには同一キーで複数設定されるものがある
		// また1エントリ内にカンマ区切りであらかじめ決められた順序で値が記述されるものも多い
		// こうした値にアクセスする場合 UnitQueries#parameter(...) が利用できる
		printfln("unit.query(parameters().nameEquals(\"el\").theirValues().at(1).contentEquals(\"j\").list()) => %s",
				unit.query(parameters().nameEquals("el").theirValues(1).endsWith("j").list()));
		
		printfln("%s", unit.query(parameters("ty").normalize().when().valueCount(1).then().appendValue("foo")));
	}

	private static void printfln(final String format, final Object... args) {
		System.out.printf(format + System.getProperty("line.separator"), args);
	}
}
