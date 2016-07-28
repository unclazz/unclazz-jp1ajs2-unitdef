package org.unclazz.jp1ajs2.unitdef.query;

import java.util.regex.Pattern;

import org.unclazz.jp1ajs2.unitdef.ParameterValue;
import org.unclazz.jp1ajs2.unitdef.ParameterValueType;
import org.unclazz.jp1ajs2.unitdef.Tuple;
import org.unclazz.jp1ajs2.unitdef.Unit;

/**
 * ユニット定義パラメータ値を問合せるクエリ.
 */
public interface ParameterValueIterableQuery
extends IterableQuery<Unit, ParameterValue> {
	/**
	 * パラメータのタイプの条件を付与したクエリを返す.
	 * @param t タイプ
	 * @return クエリ
	 */
	ParameterValueIterableQuery typeIs(final ParameterValueType t);
	
	/**
	 * タプルを問合せるクエリを返す.
	 * <p>タプルでないパラメータ値はクエリの結果セットに含まれない。</p>
	 * @return クエリ
	 */
	TupleIterableQuery<Unit> typeIsTuple();
	
	/**
	 * パラメータ値を文字シーケンスに変換するクエリを返す.
	 * @return クエリ
	 */
	IterableQuery<Unit,String> asString();
	
	/**
	 * パラメータ値を整数に変換するクエリを返す.
	 * <p>クエリの結果セットには整数に変換できなかったものは含まれない。</p>
	 * @return クエリ
	 */
	IterableQuery<Unit,Integer> asInteger();
	
	/**
	 * パラメータ値を整数に変換するクエリを返す.
	 * <p>整数に変換できないパラメータ値はデフォルト値で置き換えられる。</p>
	 * @param defaultValue デフォルト値
	 * @return クエリ
	 */
	IterableQuery<Unit,Integer> asInteger(int defaultValue);
	
	IterableQuery<Unit, String> asEscapedString();
	
	IterableQuery<Unit, String> asQuotedString();
	
	IterableQuery<Unit, String> asQuotedString(boolean force);
	
	IterableQuery<Unit, Boolean> asBoolean(String... trueValues);
	
	IterableQuery<Unit, Tuple> asTuple();
	
	/**
	 * パラメータ値の条件を付与したクエリを返す.
	 * @param s 文字列
	 * @return クエリ
	 */
	ParameterValueIterableQuery contentEquals(final CharSequence s);
	
	/**
	 * パラメータ値の条件を付与したクエリを返す.
	 * @param s 部分文字列
	 * @return クエリ
	 */
	ParameterValueIterableQuery startsWith(final String s);
	
	/**
	 * パラメータ値の条件を付与したクエリを返す.
	 * @param s 部分文字列
	 * @return クエリ
	 */
	ParameterValueIterableQuery endsWith(final String s);
	
	/**
	 * パラメータ値の条件を付与したクエリを返す.
	 * @param s 部分文字列
	 * @return クエリ
	 */
	ParameterValueIterableQuery contains(final String s);
	
	/**
	 * パラメータ値の条件を付与したクエリを返す.
	 * @param regex 正規表現パターン
	 * @return クエリ
	 */
	ParameterValueIterableQuery matches(final String regex);
	
	/**
	 * パラメータ値の条件を付与したクエリを返す.
	 * @param regex 正規表現パターン
	 * @return クエリ
	 */
	ParameterValueIterableQuery matches(final Pattern regex);
	
	/**
	 * パラメータ値の位置の条件を追加したクエリを返す.
	 * @param i パラメータ値の位置（{@code 0}始まり）
	 * @return クエリ
	 * @throws IllegalStateException パラメータの位置を指定済みの場合
	 */
	ParameterValueIterableQuery at(final int i);
}
