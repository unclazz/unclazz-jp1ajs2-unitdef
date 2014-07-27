# Query-parse

## プロジェクトの概要

Query-codeは比較演算子と論理演算子からなるクエリを処理するライブラリです。
クエリ文字列をパースするロジックは[Code-parse](https://github.com/mizukyf/code-parse)プロジェクトの成果物に依存しています。

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

    // MAP_QUERY_FACTORYはCollection<Map<String, Object>>から
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

## クエリの構文

使用できるクエリは論理演算子と比較演算子からなる比較的シンプルなものです。
式（expression）は比較演算子を使った単純な式から、そうした式同士を論理演算子で組み合わせた複雑な式までさまざまに指定できます。

演算子間に優先順位はとくにないため、クエリ内に複数の式がある場合の評価順序は基本的に「左から右へ」です。
丸括弧で囲うことで論理演算の結合方法を指定できます。
つまり `a == 1 and b == 2 or c == 3` は `(a == 1 and b == 2) or c == 3` と同義であるということです。

論理演算子には二項演算子（logical_binary_operator）と単項演算子（logical_unary_operator）が存在します。
`!`、`&&`、`||`、`and`、`or`といった演算子はいずれもご想像通りの動作をするはずです。

比較演算子にも二項演算子（comparative_binary_operator）と単項演算子（comparative_unary_operator）が存在します。
二項演算子の `==` と `!=` はJavaの `Object#equals(Object other)` による等価性比較を行います。
`^=` は左辺で指定されたプロパティが右辺で指定された値で始まることを（前方一致）、
`*=` は左辺で指定されたプロパティが右辺で指定された値を含むことを（中間一致）、
`$=` は左辺で指定されたプロパティが右辺で指定された値で終わることを（後方一致）それぞれ表します。
単項演算子の `is null` と `is not null` はこれもご想像通りの動作をするはずです。

```bnf
<query> ::= <expression>

<expression> ::= "(" expression ")" 
	| <expression> <logical_binary_operator> <expression>
	| <logical_unary_operator> <expression>
	| <property> <comparative_binary_operator> <value>
	| <property> <comparative_unary_operator>

<logical_unary_operator> ::= "!"

<logical_binary_operator> ::= "&&"
	| "||"
	| "and"
	| "or"

<comparative_unary_operator> ::= "is null"
	| "is not null"

<comparative_binary_operator> ::= "=="
	| "!="
	| "^="
	| "*="
	| "$="

<property> ::= '"' string '"'
	| "'" string "'"
	| string

<value> ::= '"' string '"'
	| "'" string "'"
	| string
```



