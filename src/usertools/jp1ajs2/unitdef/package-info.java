/**
 * <p>このパッケージのサブパッケージはJP1/AJS2のユニット定義情報にJavaオブジェクトとしてのインターフェースを提供します。</p>
 * <dl>
 * <dt>{@link usertools.jp1ajs2.unitdef.core}</dt>
 * <dd>{@link usertools.jp1ajs2.unitdef.core.Parser}オブジェクトはJP1/AJS2定義ファイルをパースして、
 * 同パッケージに含まれるJP1/AJS2の定義情報を表現するインターフェースの実装を返します。
 * これらのインターフェースは、JP1/AJS2定義ファイルに記載されるユニットとその構成要素をある程度抽象化させつつも
 * なるべくそのままJavaオブジェクトとして表現するために用意されたものです。</dd>
 * <dt>{@link usertools.jp1ajs2.unitdef.util}</dt>
 * <dd>このパッケージはより具象性の高いかたちでJP1/AJS2の定義情報にアクセスするための各種ユーティリティを提供します。</dd>
 * <dt>{@link usertools.jp1ajs2.unitdef.ext}</dt>
 * <dd>このパッケージはより具象性の高いかたちでJP1/AJS2の定義情報を表現するオブジェクトを提供します。
 * これらのオブジェクトはおもに{@link usertools.jp1ajs2.unitdef.util.Accessors}ユーティリティを使用して取得できます。</dd>
 * </dl>
 * <p>このパッケージで提供されるインターフェースは、JP1定義ファイルに記載されるユニットとその構成要素を
 * ある程度抽象化させつつもなるべくそのままJavaオブジェクトとして表現するために用意されたものです。</p>
 * <p>JP1定義ファイルをパースするためのAPIはパッケージに含まれています。</p>
 * <p>ユニット定義が持つ各種パラメータにアクセスするために{@link usertools.jp1ajs2.unitdef.util}パッケージのユーティリティが利用できます。
 * このユーティリティは{@link usertools.jp1ajs2.unitdef.ext}パッケージに含まれるより具体的な（パラメータごとに特化した）オブジェクトを返します。</p>
 * 
 */
package usertools.jp1ajs2.unitdef;