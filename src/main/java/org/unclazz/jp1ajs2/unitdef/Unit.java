package org.unclazz.jp1ajs2.unitdef;

import java.util.Iterator;
import java.util.List;

import org.unclazz.jp1ajs2.unitdef.parameter.UnitType;
import org.unclazz.jp1ajs2.unitdef.query2.Query;

/**
 * JP1/AJS2のユニットを表わすインターフェース.
 * 
 * <p>ユニット定義を構成する情報は非常に多岐にわたり
 * それらはHITACHIのリファレンスの記述においても必ずしも網羅されているわけではない。
 * あるユニット種別のユニットがどのユニット定義パラメータを保持することが許容されているか、
 * あるユニット定義パラメータはどのような組み合わせで、どのようなフォーマットで、
 * どのような制約のものとで設定可能であるか、これらは必ずしも明確にされていない。
 * したがってこのインターフェースはユニット定義ファイルをパースした結果に対する
 * 参照オペレーションのみを提供し、かつその参照される情報の適正性については保証しない。</p>
 * 
 * <p>このインターフェースの実装の{@link #iterator()}から得られる{@link Iterator}は
 * このインターフェースが表わすユニットおよびその子孫ユニットのすべてに対してシーケンシャルにアクセスする手段を提供する。
 * ただし{@link Iterator#next()}が返すユニットの順序は保証されない。</p>
 */
public interface Unit {
	/**
	 * ユニットの完全名を返す.
	 * <p>このメソッドが返す完全名は、あくまでもオブジェクトが生成される元になったユニット定義ファイルにおける
	 * ルート・ユニットを起点としたものであり、JP1/AJS2 Viewなどで確認できる完全名とは異なる可能性がある。</p>
	 * @return 完全名
	 */
	FullQualifiedName getFullQualifiedName();
	/**
	 * ユニット属性パラメータを返す.
	 * @return ユニット属性パラメータ
	 */
	Attributes getAttributes();
	/**
	 * ユニット名を返す.
	 * <p>ユニット名はユニット属性パラメータのうちでも必須の項目であるため、
	 * メソッドが返す値は{@code null}でも{@code ""}でもないことが保証されている。</p>
	 * @return ユニット名
	 */
	String getName();
	/**
	 * ユニット種別を返す.
	 * <p>ユニット定義パラメータtyがすべてのユニット種別における必須項目であるため、
	 * このメソッドが返す値が{@code null}ではないことが保証されている。</p>
	 * @return ユニット種別
	 */
	UnitType getType();
	/**
	 * コメントを返す.
	 * <p>ユニット定義パラメータcmが存在しない場合は{@code ""}を返す。</p>
	 * @return コメント
	 */
	CharSequence getComment();
	/**
	 * ユニット定義パラメータのリストを返す.
	 * <p>ユニット定義パラメータtyがすべてのユニット種別における必須項目であるため、
	 * このメソッドが返すリストの要素数は必ず1以上になる。</p>
	 * @return ユニット定義パラメータ・リスト
	 */
	List<Parameter> getParameters();
	/**
	 * クエリを使用してユニット定義から情報を取り出す.
	 * @param <R> クエリにより返される値の型
	 * @param q クエリ 
	 * @return クエリにより取得された値のリスト
	 */
	<R> R query(Query<Unit,R> q);
	/**
	 * 下位ユニットのリストを返す.
	 * <p>下位ユニットが1つも存在しない場合、このメソッドは空のリストを返す。</p>
	 * @return 下位ユニット・リスト
	 */
	List<Unit> getSubUnits();
	/**
	 * 下位ユニットを返す.
	 * <p>名前が一致するユニットが存在しない場合、このメソッドは{@code null}を返す。</p>
	 * <p>各ユニットは兄弟ユニット間で一意の名前を持つので、
	 * {@link #getParameter(String)}におけるような順序問題は存在しない。</p>
	 * @param name ユニット名
	 * @return 下位ユニット
	 */
	Unit getSubUnit(String name);
}
