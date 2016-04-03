# unclazz-jp1ajs2-unitdef

## プロジェクトの概要

このプロジェクトは、日立ソリューションズの製造・販売する[JP1/AJS2](http://www.hitachi-solutions.co.jp/jp1/sp/?cid=aws0004461)の定義情報をパースし、JavaオブジェクトとしてアクセスするためのAPIを開発するものです。

APIを構成するモジュールや、それらのモジュールが提供する機能を使用するサンプルコードは
`src/test/java`ディレクトリ配下の`org.unclazz.jp1ajs2.sample`パッケージに含まれています。

## 使用例

まず[リリース一覧](https://github.com/unclazz/unclazz-jp1ajs2-unitdef/releases)からjarファイルを取得してプロジェクトのビルドパスに含めてください。もしあなたのプロジェクトがMavenを使用しているのであれば、`unclazz-jp1ajs2-unitdef`のアーティファクトはGithub上の[Mavenリポジトリ](https://github.com/unclazz/mvn-repo)から取得できます。そのための設定は`pom.xml`に以下のコード断片を追加するだけです：
```xml
<repositories>
	...
	<repository>
		<id>unclazz-mvn-repo</id>
		<url>https://raw.github.com/unclazz/mvn-repo/master/</url>
		<snapshots>
			<enabled>true</enabled>
			<updatePolicy>always</updatePolicy>
		</snapshots>
	</repository>
</repositories>
<dependencies>
	...
	<dependency>
		<groupId>org.unclazz.jp1ajs2</groupId>
		<artifactId>unclazz-jp1ajs2-unitdef</artifactId>
		<version>2.4.0-RELEASE</version>
	</dependency>
<dependencies>
```

そしてビジネスロジックをコーディングします：
```java
package ...;

import org.unclazz.jp1ajs2.unitdef.parameter.AnteroposteriorRelationship;
import org.unclazz.jp1ajs2.unitdef.parameter.FixedDuation;
import org.unclazz.jp1ajs2.unitdef.Unit;
import org.unclazz.jp1ajs2.unitdef.Units;
import static org.unclazz.jp1ajs2.unitdef.UnitQueries.*;
import static java.lang.System.*;

public final class Usage {
	
	private static final String sampleDef = ""
			+ "unit=XXXX0000,,,;\r\n"
			+ "{\r\n"
			+ "	ty=n;\r\n"
			+ "	el=XXXX0001,g,+80 +48;\r\n" 
			+ "	el=XXXX0002,g,+240 +144;\r\n"
			+ "	ar=(f=XXXX0001,t=XXXX0002);\r\n" 
			+ "	cm=\"これはコメントです。\";\r\n"
			+ "	fd=30;\r\n"
			+ "	unit=XXXX0001,,,;\r\n"
			+ "	{\r\n"
			+ "		ty=pj;\r\n"
			+ "		sc=\"hello.exe\";\r\n"
			+ "	}\r\n"
			+ "	unit=XXXX0002,,,;\r\n"
			+ "	{\r\n"
			+ "		ty=pj;\r\n" 
			+ "		sc=\"bonjour.exe\";\r\n"
			+ "	}\r\n"
			+ "}\r\n";
	
	public static void main(String[] args) {
		// ユニット定義を文字列から読み取ります
		// Units.from...系メソッドは文字列・ストリーム・ファイルに対応しています
		final Unit u = Units.fromCharSequence(sampleDef).get(0);

		// Unitオブジェクトはユニット定義情報にアクセスするローレベルのAPIを提供します
		out.println(u.getName()); // => "XXXX0000"
		out.println(u.getType()); // => "JOBNET"
		out.println(u.getSubUnits().size()); // => 2

		// UnitQuery<T>とそのユーティリティを使ってユニットから各種の情報を取得します
		// 実行所要時間の取得
		final List<FixedDuration> p0 = u.query(fd());
		// 下位ユニット間の実行順序関係を取得
		final List<AnteroposteriorRelationship> p1 = u.query(ar());
	}
}

```

## Unit/Unitsオブジェクト

`Unit`はJP1/AJS2のユニット定義をあらわすインターフェースです。
クライアント・コードはこのインターフェースを通じてユニット定義に含まれる情報
──ユニット名やその他のユニット属性パラメータや、sz・cm・fdといったユニット定義パラメータ、
そして下位ユニットの情報──にアクセスすることができます。

```java
final Unit u = ...;
u.getName(); // => ユニット名
u.getFullQualifiedName(); // => ユニット完全名
u.getAttributes(); // => ユニット属性パラメータ
u.getSubUnits(); // => 下位ユニットのリスト
```

`Units`は`Unit`オブジェクトのファクトリ/ユーティリティ・クラスです。
`Units.fromStream(InputStream)`とその同系統のメソッドは、各種の入力ソースから
ユニット定義情報を読み取って`Unit`オブジェクトを返します。

```java
final List<Unit> us = Units.fromString("unit=FOO,,,;{" +
    "ty=g;" + 
    "cm=\"This is a jobnet-group unit named #\"FOO#\".\";" + 
    "}");
final Unit u = us.get(0);
u.getName(); // => "FOO"
```

一方`toString()`や`writeToStream(Unit,Stream)`メソッドはその名前の通り`Unit`オブジェクトの文字列化を行います。

## Parameter/ParameterValue

JP1/AJS2のユニット定義は、ユニットをノードとする木構造と
そのノードに紐づく1つ以上のユニット定義パラメータ（名前の重複が許容されるものとそうでないものがある）、
そしてそのパラメータに紐づく1つ以上の値という部品を中心として成り立っています。

このうちユニット、つまり`Unit`についてはすでに紹介しました。
その`Unit#getParameters()`メソッドを呼び出すとユニット定義パラメータをあらわす`Parameter`インターフェースのリストが得られます。
そして`Parameter#getValue(int)`メソッドを呼び出すとパラメータ値をあらわす`ParameterValue`インターフェースが得られます。

`ParameterValue`には3つの値のかたち──文字シーケンス、
二重引用符で囲われた文字シーケンス、そしてタプル──のいずれかをとります。
`ParameterValue`が提供するメソッドを通じてこれらのデータにアクセスすることができます。

## *Query

`Unit`・`Parameter`・`ParameterValue`という3つのインターフェースを通じた木構造のトラバースは
もっとも基本的でローレベルの操作となります。
これに対して`UnitQuery`・`ParameterQuery`・`ParameterValueQuery`という3つのインターフェースを中核とした
より生産性の高いAPIが用意されています。

例えばユニット定義パラメータszの情報を読み取るには次のようにします：

```java
import static org.unclazz.jp1ajs2.unitdef.UnitQueries.*;
...
final Unit u = ...;
final List<MapSize> szs = u.query(sz()); // sz() = UnitQueries.sz()
```

またユニット定義パラメータelの0番めの文字シーケンス（下位ユニット名）を取得するには次のようにします：

```java
import static org.unclazz.jp1ajs2.unitdef.UnitQueries.*;
...
final Unit u = ...;
final List<CharSequence> el0s = u.query(parameter("el").valueAt(0)
    .asCharSeqence()); // parameter(String) = UnitQueries.parameter(String)
```

定義済みのクエリは`UnitQueries`・`ParameterQueries`・`ParameterValueQueries`という3つのユーティリティ・クラスを通じて得られます。
上のサンプルコードにもあるように、`*Query`系APIを利用する場合はこれらのクラスの静的メンバを適宜インポートしておきましょう。

## JP1/AJS2製造・販売元との関係

JP1/AJS2製造・販売元に対する本プロジェクト開発者の立場は単なる「ユーザー」です。したがって、本プロジェクトで開発・配布するコードは製造・販売元とは一切関わりがありません。

本プロジェクトで開発・配布するコードは、製造・販売元で公開している定義情報[リファレンス](http://www.hitachi.co.jp/Prod/comp/soft1/manual/pc/d3K2543/AJSO0001.HTM)に基づき、これを参考にして、定義ファイルのパースや各種パラメータのアクセサのロジックを実装していますが、同リファレンスの誤読もしくはリファレンスとJP1/AJS2間の実装齟齬などにより、実際の動作に差異が存在する可能性があります。
