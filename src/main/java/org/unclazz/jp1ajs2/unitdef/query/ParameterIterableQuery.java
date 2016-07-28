package org.unclazz.jp1ajs2.unitdef.query;

import java.util.regex.Pattern;

import org.unclazz.jp1ajs2.unitdef.Parameter;
import org.unclazz.jp1ajs2.unitdef.Unit;

/**
 * ユニットに問合せを行いユニット定義パラメータを返すクエリ.
 * 
 * <p>このクエリのインスタンスを得るには{@link Q}の提供する静的メソッドを利用する。
 * {@link Unit#query(Query)}メソッドをクエリに対して適用すると問合せが行われる：</p>
 * 
 * <pre> import static org.unclazz.jp1ajs2.unitdef.query.Queries.*;
 * Unit u = ...;
 * Iterable&lt;Parameter&gt; ui = u.query(parameters());
 * Iterable&lt;Parameter&gt; ui2 = u.query(children().theirParameters());</pre>
 * 
 * <p>クエリへの種々の条件追加、クエリのイミュータブルな特性、クエリが返す{@link Iterable}と
 * メソッド{@link #one()}・{@link #list()}については{@link UnitIterableQuery}と同様である。
 * 詳しくは{@link UnitIterableQuery}のドキュメントを参照のこと。</p>
 */
public interface ParameterIterableQuery 
extends IterableQuery<Unit, Parameter>, 
ParameterConditionalModifier<ParameterIterableQuery>{
	/**
	 * 問合せ結果のパラメータが持つパラメータ値を問合せるクエリを返す.
	 * @return クエリ
	 */
	ParameterValueIterableQuery theirValues();
	/**
	 * 問合せ結果のパラメータが持つパラメータ値を問合せるクエリを返す.
	 * @param i パラメータ値の位置
	 * @return クエリ
	 */
	ParameterValueIterableQuery theirValues(int i);
	/**
	 * パラメータ名の条件を追加したクエリを返す.
	 * @param n パラメータ名
	 * @return クエリ
	 */
	ParameterIterableQuery nameEquals(String n);
	/**
	 * パラメータ名の条件を追加したクエリを返す.
	 * @param n パラメータ名の部分文字列
	 * @return クエリ
	 */
	ParameterIterableQuery nameStartsWith(String n);
	/**
	 * パラメータ名の条件を追加したクエリを返す.
	 * @param n パラメータ名の部分文字列
	 * @return クエリ
	 */
	ParameterIterableQuery nameEndsWith(String n);
	/**
	 * パラメータ名の条件を追加したクエリを返す.
	 * @param n パラメータ名の部分文字列
	 * @return クエリ
	 */
	ParameterIterableQuery nameContains(String n);
	/**
	 * パラメータ名の条件を追加したクエリを返す.
	 * @param regex パラメータ名の正規表現パターン
	 * @return クエリ
	 */
	ParameterIterableQuery nameMatches(Pattern regex);
	/**
	 * パラメータ名の条件を追加したクエリを返す.
	 * @param regex パラメータ名の正規表現パターン
	 * @return クエリ
	 */
	ParameterIterableQuery nameMatches(String regex);
	/**
	 * パラメータ値の個数の条件を追加したクエリを返す.
	 * @param c パラメータ値の個数
	 * @return クエリ
	 */
	ParameterIterableQuery valueCountEquals(int i);
}
