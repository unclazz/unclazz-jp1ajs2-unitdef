# jp1ajs2-unitdef

## プロジェクトの概要

このプロジェクトは、日立ソリューションズの製造・販売する[JP1/AJS2](http://www.hitachi-solutions.co.jp/jp1/sp/?cid=aws0004461)の定義情報をパースし、JavaオブジェクトとしてアクセスするためのAPIを開発するものです。

APIを構成するモジュールや、それらのモジュールが提供する機能の紹介は[プロジェクトのWikiページ](https://github.com/mizukyf/jp1ajs2.unitdef/wiki)に掲載されています。

## 使用例

まず[リリース一覧](https://github.com/mizukyf/jp1ajs2.unitdef/releases)からjarファイルを取得してプロジェクトのビルドパスに含めてください。もしあなたのプロジェクトがMavenを使用しているのであれば、`jp1ajs2-unitdef`のアーティファクトはGithub上の[Mavenリポジトリ](https://github.com/mizukyf/doogwood-mvnrepo)から取得できます。そのための設定は`pom.xml`に以下のコード断片を追加するだけです：
```xml
<repositories>
	...
	<repository>
		<id>doogwood-mvnrepo</id>
		<url>https://raw.github.com/mizukyf/doogwood-mvnrepo/master/</url>
		<snapshots>
			<enabled>true</enabled>
			<updatePolicy>always</updatePolicy>
		</snapshots>
	</repository>
</repositories>
<dependencies>
	...
	<dependency>
		<groupId>org.doogwood.jp1ajs2</groupId>
		<artifactId>jp1ajs2-unitdef</artifactId>
		<version>1.6.1-RELEASE</version>
	</dependency>
<dependencies>
```

そしてビジネスロジックをコーディングします：
```java
package ...;

import org.doogwood.jp1ajs2.unitdef.AnteroposteriorRelationship;
import org.doogwood.jp1ajs2.unitdef.Params;
import org.doogwood.jp1ajs2.unitdef.Unit;
import org.doogwood.jp1ajs2.unitdef.Units;
import org.doogwood.jp1ajs2.unitdef.util.Optional;
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
		// ユニット定義を文字列から読み取ります
		// Units.from...系メソッドは文字列・ストリーム・ファイルに対応しています
		final Unit u = Units.fromString(sampleDef).get(0);

		// Unitオブジェクトはユニット定義情報にアクセスするローレベルのAPIを提供します
		out.println(u.getName()); // => "XXXX0000"
		out.println(u.getType()); // => "JOBNET"
		out.println(u.getSubUnits().size()); // => 2

		// Paramsユーティリティはユニット種別ごとに定義された各種パラメータへのアクセスを提供します
		// 実行所要時間の取得
		final Optional<Integer> p0 = Params.getFixedDuration(u);
		// 下位ユニット間の実行順序関係を取得
		final List<AnteroposteriorRelationship> p1 = Params.getAnteroposteriorRelationships(u);

		// 必須でないパラメータはOptionalもしくはListオブジェクトに包まれて返されます
		out.println(p0.get()); // => 30
		out.println(p1.get(0).getFrom().getFullQualifiedName()); // => "/XXXX0000/XXXX0001"
	}
}

```

## JP1/AJS2製造・販売元との関係

JP1/AJS2製造・販売元に対する本プロジェクト開発者の立場は単なる「ユーザー」です。したがって、本プロジェクトで開発・配布するコードは製造・販売元とは一切関わりがありません。

本プロジェクトで開発・配布するコードは、製造・販売元で公開している定義情報[リファレンス](http://www.hitachi.co.jp/Prod/comp/soft1/manual/pc/d3K2543/AJSO0001.HTM)に基づき、これを参考にして、定義ファイルのパースや各種パラメータのアクセサのロジックを実装していますが、同リファレンスの誤読もしくはリファレンスとJP1/AJS2間の実装齟齬などにより、実際の動作に差異が存在する可能性があります。
