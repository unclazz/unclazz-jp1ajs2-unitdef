package org.unclazz.jp1ajs2.unitdef.query;

import org.unclazz.jp1ajs2.unitdef.Tuple;

/**
 * ユニットが持つユニット定義パラメータ値のうちタプルのみを問合せるクエリ.
 */
public interface TupleIterableQuery<T> extends IterableQuery<T, Tuple> {
	/**
	 * エントリー・キーの条件を付与したクエリを返す.
	 * @param k キー
	 * @return クエリ
	 */
	TupleIterableQuery<T> hasKey(final String k);
	
	/**
	 * エントリー値の条件を付与したクエリを返す.
	 * @param v エントリー値
	 * @return クエリ
	 */
	TupleIterableQuery<T> hasValue(final CharSequence v);
	
	/**
	 * エントリーの条件を付与したクエリを返す.
	 * @param k エントリー・キー
	 * @param v エントリー値
	 * @return クエリ
	 */
	TupleIterableQuery<T> hasEntry(final String k, final CharSequence v);
	
	/**
	 * エントリー数の条件を付与したクエリを返す.
	 * @param c エントリー数
	 * @return クエリ
	 */
	TupleIterableQuery<T> entryCount(final int c);
}
