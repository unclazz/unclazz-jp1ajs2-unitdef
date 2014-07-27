# Query-parse

## プロジェクトの概要

Query-codeは比較演算子と論理演算子からなるクエリを処理するライブラリです。
定義情報をパースするロジックは[Code-parse](https://github.com/mizukyf/code-parse)プロジェクトの成果物に依存しています。

### QueryFactory

QueryFactoryは文字列として表現されたクエリをパースして解析済みクエリを生成するファクトリ・オブジェクトです。
このオブジェクトの初期化にはAccessor（後述）が必要になります。

### Query

解析済みクエリを表わすオブジェクトです。コレクション要素を検索するためのAPIを提供します。
ファクトリのAPIによって解析されたコードは、Query内部にJavaオブジェクト・グラフとして格納され、コレクションの要素検索時に使用されます。

### Accessor

クエリの条件式で指定されたプロパティを要素から取得するためのアクセサのインターフェースです。
このインターフェースの実装オブジェクトをパラメータとしてQueryFactoryは初期化されます。

### 検索対象コレクションとAccessorオブジェクト

Accessorインターフェースは、Query-parseのAPIが提供する機能の抽象化のかなめです。
このインターフェースを適切に実装することで、ライブラリのユーザはさまざまなコレクションを検索対象とするクエリを作成できます。
例えばQueryFactory.MAP_QUERY_FACTORYは、Map<String, Object>を処理するAccessorで初期化されたファクトリです。

## 使用方法

Code-parseのjarのjarをプロジェクトのビルドパスに設定します。あとはQueryFactoryを初期化して、それを使ってQueryを作成、任意のコレクションに対して問合せを行います：

```java
package code.parse.usage;

import com.m12i.query.parse.Query;
import com.m12i.query.parse.QueryFactory;

...

public class Main {
  
	public static void main(String... args) throws QueryParseException {
    
    // Map<String, Object>を要素とするコレクションがあると仮定します
    final List<Map<String, Object> list0 = ...;

    // MAP_QUERY_FACTORYはCollection<Map<String, Object>＞から
    // 条件にマッチする要素を検索するクエリのためのファクトリ実装です
		final QueryFactory<Map<String, Object>> factory = QueryFactory
				.MAP_QUERY_FACTORY;
		
		// 文字列で表現されたクエリをパースして解析済みクエリを作成します
		final Query<Map<String, Object>> query = factory
		    .create("prop0 == foo and prop1 == bar");

    // コレクションに対する検索を実施します
		q.selectAllFrom(list0); // => List<Map<String, Object>>
		
  }

}
```
