# jp1ajs2.unitdef

## プロジェクトの概要

このプロジェクトは、日立ソリューションズの製造・販売する[JP1/AJS2](http://www.hitachi-solutions.co.jp/jp1/sp/?cid=aws0004461)の定義情報をパースし、JavaオブジェクトとしてアクセスするためのAPIを開発するものです。

## 使用例

```java
package jp1ajs2.unitdef.usage;

import com.m12i.jp1ajs2.unitdef.AnteroposteriorRelationship;
import com.m12i.jp1ajs2.unitdef.Params;
import com.m12i.jp1ajs2.unitdef.Unit;
import com.m12i.jp1ajs2.unitdef.Units;
import com.m12i.jp1ajs2.unitdef.util.Maybe;
import static java.lang.System.*;

public final class Usage {
	
	private static final String sampleDef = ""
			+ "unit=XXXX0000,AAAAA,BBBBB,CCCCC;\r\n"
			+ "{\r\n"
			+ "	ty=n;\r\n"
			+ "	el=XXXX0001,g,+80 +48;\r\n" 
			+ "	el=XXXX0002,g,+240 +144;\r\n"
			+ "	ar=(f=XXXX0001,t=XXXX0002);\r\n" 
			+ "	cm=\"これはコメントです。\";\r\n"
			+ "	fd=30;\r\n"
			+ "	unit=XXXX0001,AAAAA,BBBBB,CCCCC;\r\n"
			+ "	{\r\n"
			+ "		ty=pj;\r\n"
			+ "		sc=\"hello.exe\";\r\n"
			+ "	}\r\n"
			+ "	unit=XXXX0002,AAAAA,BBBBB,CCCCC;\r\n"
			+ "	{\r\n"
			+ "		ty=pj;\r\n" 
			+ "		sc=\"bonjour.exe\";\r\n"
			+ "	}\r\n"
			+ "}\r\n";
	
	public static void main(String[] args) {
		// ユニット定義を文字列から読み取る
		// Units.from...系メソッドは文字列・ストリーム・ファイルに対応しています
		final Unit u = Units.fromString(sampleDef);
		
		// Unitオブジェクトはユニット定義情報にアクセスするローレベルのAPIを提供します
		out.println(u.getName()); // => "XXXX0000"
		out.println(u.getType()); // => "JOBNET"
		out.println(u.getSubUnits().size()); // => 2

		// Paramsユーティリティはユニット種別ごとに定義された各種パラメータへのアクセスを提供します
		final Maybe<Integer> p0 = Params.getFixedDuration(u);
		final Maybe<AnteroposteriorRelationship> p1 = Params.getAnteroposteriorRelationships(u);

		// 必須でないパラメータでMaybeオブジェクトに包まれて返されます
		out.println(p0.get()); // => 30
		out.println(p1.get(0).getFrom().getFullQualifiedName()); // => "/XXXX0000/XXXX0001"
	}
}

```

## Unit/Unitsオブジェクト

`Unit`はJP1/AJS2のユニット定義をあらわすオブジェクトです。
クライアント・コードはこのインターフェースを通じてユニット定義に含まれる情報
──ユニット名やその他のユニット属性パラメータや、`sz`・`cm`・`fd`といったユニット定義パラメータ、
そして下位ユニットの情報──にアクセスすることができます。

`Units`はユニット定義情報の操作に関連するユーティリティ・クラスです。
`Units.fromStream(InputStream)`とその同系統のメソッドは、各種の入力ソースから
ユニット定義情報を読み取って`Unit`オブジェクトを返します。
`Units`にはその他、下位ユニットや子孫ユニットを検索するためのメソッドや、
メソッド定義パラメータを検索するためのメソッドが用意されています。

## Param/Paramsオブジェクト

`Param`はユニット定義パラメータをあらわすオブジェクトです。
クライアント・コードはこのインターフェースを通じてユニット定義パラメータ情報にアクセスできます。

ユニット定義パラメータの種類により、同一ユニットが同名のパラメータを複数個持つことがあります。
ユニット定義パラメータは、1つもしくはそれ以上の値（カンマ区切り）を持ちます。
値の個数はパラメータの種別により決まります。
個々の値は文字列、数値、日付、時間、タプル（タプルもどきのなにか）のかたちを取りますが、
これもパラメータの種別により決まります。

このAPIでは文字列とタプルとのちがいしか判断しません。
ある文字列値が数値として見なすべきものなかどうか、はたまた日付として見なすべきかものなのかどうか
──といった事項については判断をしていません。
ユニット定義パラメータの値はタプルかもしくは（さもなくば）文字列かのいずれかです。

`Params`はユニット定義パラメータの値にアクセスするためのヘルパー関数と、
ユニット種別固有のいくつかのパラメータについて専用のアクセサ・メソッドを提供しています。

## JP1/AJS2製造・販売元との関係

JP1/AJS2製造・販売元に対する本プロジェクト開発者の関係は単なる「ユーザー」です。したがって、本プロジェクトで開発・配布するコードは製造・販売元とは一切関わりがありません。

本プロジェクトで開発・配布するコードは、製造・販売元で公開している定義情報[リファレンス](http://www.hitachi.co.jp/Prod/comp/soft1/manual/pc/d3K2543/AJSO0001.HTM)に基づき、これを参考にして、定義ファイルのパースや各種パラメータのアクセサのロジックを実装していますが、同リファレンスの誤読もしくはリファレンスとJP1/AJS2間の実装齟齬などにより、実際の動作に差異が存在する可能性があります。
