Usertools.jp1ajs2.unitdef
=========================

## プロジェクトの概要

このプロジェクトは、日立ソリューションズの製造・販売する[JP1/AJS2](http://www.hitachi-solutions.co.jp/jp1/sp/?cid=aws0004461)の定義情報をパースし、Javaオブジェクトとしてアクセスするためのツールを開発するものです。
定義情報をパースロジックは[Code-parse](https://github.com/mizukyf/code-parse)プロジェクトの成果物に依存しています。

### Coreパッケージ

ParserオブジェクトはJP1/AJS2定義ファイルをパースして、同パッケージに含まれるJP1/AJS2の定義情報を表現するインターフェースの実装を返します。
これらのインターフェースは、JP1/AJS2定義ファイルに記載されるユニットとその構成要素をある程度抽象化させつつもなるべくそのままJavaオブジェクトとして表現するために用意されたものです。

### Utilパッケージ

このパッケージはより具象性の高いかたちでJP1/AJS2の定義情報にアクセスするための各種ユーティリティを提供します。これらの定義情報はおもにExtパッケージに含まれるオブジェクトとして取得できます。

### Extパッケージ

このパッケージはより具象性の高いかたちでJP1/AJS2の定義情報を表現するオブジェクトを提供します。
これらのオブジェクトはおもにUtilパッケージのAccessorsユーティリティを使用して取得できます。

## 使用方法

[Code-parse](https://github.com/mizukyf/code-parse)のjarと[Usertools.jp1.ajs2.unitdefのjar](https://github.com/mizukyf/usertools.jp1ajs2.unitdef/releases)をプロジェクトのビルドパスに設定します。あとはParseUtilsやAccessorsといったユーティリティを使って定義情報にアクセスするだけです：

```java
package unitdef.usage;

import java.util.List;

import com.m12i.code.parse.ParseException;

import usertools.jp1ajs2.unitdef.core.ParseUtils;
import usertools.jp1ajs2.unitdef.core.Unit;
import usertools.jp1ajs2.unitdef.ext.Arrow;
import usertools.jp1ajs2.unitdef.util.Either;
import usertools.jp1ajs2.unitdef.util.Option;
import static usertools.jp1ajs2.unitdef.util.Accessors.*;

public class Main {

	private static final String sampleDef = ""
			+ "unit=XXXX0000,AAAAA,BBBBB,CCCCC;\r\n"
			+ "{\r\n"
			+ "    ty=n;\r\n"
			+ "    el=XXXX0001,g,+80 +48;\r\n" 
			+ "    el=XXXX0002,g,+240 +144;\r\n"
			+ "    ar=(f=XXXX0001,t=XXXX0002);\r\n" 
			+ "    cm=\"これはコメントです。\";\r\n"
			+ "    fd=30;\r\n"
			+ "    unit=XXXX0001,AAAAA,BBBBB,CCCCC;\r\n"
			+ "    {\r\n"
			+ "        ty=pj;\r\n"
			+ "        sc=\"hello.exe\";\r\n"
			+ "    }\r\n"
			+ "    unit=XXXX0002,AAAAA,BBBBB,CCCCC;\r\n"
			+ "    {\r\n"
			+ "        ty=pj;\r\n" 
			+ "        sc=\"bonjour.exe\";\r\n"
			+ "    }\r\n"
			+ "}\r\n";
	
	public static void main(String[] args) throws ParseException {
		
		// ParseUtilsユーティリティは文字列やストリームから定義情報をパースします
		final Either<Throwable, Unit> e = ParseUtils.parse(sampleDef);
		// 返されるのはEitherオブジェクトなので柔軟な例外制御が可能です
		if (e.isRight()) {
			println(e.left().getMessage());
			System.exit(1);
		}
		
		// Unitオブジェクトはユニット定義情報にアクセスするローレベルのAPIを提供します
		final Unit u = e.right();
		println(u.getName()); // => "XXXX0000"
		println(u.getType()); // => "JOBNET"
		println(u.getSubUnits().size()); // => 2
		
		// Accessorsユーティリティはユニット種別ごとに定義された各種パラメータへのアクセスを提供します
		final Option<Integer> p0 = fixedDuration(u);
		final List<Arrow> p1 = arrows(u);
		
		// 必須でないパラメータで単一値をとるものはOptionオブジェクトに包まれて返されます
		println(p0.get()); // => 30
		
		// 複数値をとるものはListオブジェクトとして返されます
		println(p1.get(0).getFrom().getFullQualifiedName()); // => "/XXXX0000/XXXX0001"
	}
	
	private static void println(Object o) {
		System.out.println(o);
	}
}
```

## JP1/AJS2製造・販売元との関係

JP1/AJS2製造・販売元に対する本プロジェクト開発者の関係は単なる「ユーザー」です。したがって、本プロジェクトで開発・配布するコードは製造・販売元とは一切関わりがありません。

本プロジェクトで開発・配布するコードは、製造・販売元で公開している定義情報[リファレンス](http://www.hitachi.co.jp/Prod/comp/soft1/manual/pc/d3K2543/AJSO0001.HTM)に基づき、これを参考にして、定義ファイルのパースや各種パラメータのアクセサのロジックを実装していますが、同リファレンスの誤読もしくはリファレンスとJP1/AJS2間の実装齟齬などにより、実際の動作に差異が存在する可能性があります。
