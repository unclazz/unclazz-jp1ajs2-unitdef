package org.unclazz.jp1ajs2.unitdef.query;

import java.util.List;
import java.util.NoSuchElementException;

import org.unclazz.jp1ajs2.unitdef.util.Predicate;

abstract class AbstractItrableQuery<T,U> implements Query<T, Iterable<U>> {
	/**
	 * {@link Predicate}で表される問合せ条件を追加した新しいクエリを返す.
	 * @param pred 問合せ条件
	 * @return クエリ
	 */
	public abstract Query<T,Iterable<U>> and(final Predicate<U> pred);
	/**
	 * 問合せの結果のうち最初の1件だけを取得するためのクエリを返す.
	 * <p>問合せ結果が0件のときはデフォルト値を返す。</p>
	 * @param defaultValue デフォルト値
	 * @return クエリ
	 */
	public final OneQuery<T, U> one(U defaultValue) {
		return new OneQuery<T, U>(this, defaultValue);
	}
	/**
	 * 問合せの結果のうち最初の1件だけを取得するためのクエリを返す.
	 * <p>引数が{@code true}の場合、問合せ結果が0件のときは{@code null}を返す。
	 * 引数が{@code false}の場合、問合せ結果が0件のときは{@link NoSuchElementException}をスローする。</p>
	 * @param nullable {@code true}の場合、問合せ結果が0件のときは{@code null}を返す
	 * @return クエリ
	 * @throws NoSuchElementException 問合せ結果が0件 かつ 引数に{@code false}が設定されていたとき
	 */
	public final OneQuery<T, U> one(boolean nullable) {
		return new OneQuery<T, U>(this, nullable);
	}
	/**
	 * 問合せの結果のうち最初の1件だけを取得するためのクエリを返す.
	 * <p>問合せ結果が0件のときは{@link NoSuchElementException}をスローする。</p>
	 * @return クエリ
	 * @throws NoSuchElementException 問合せ結果が0件のとき
	 */
	public final OneQuery<T, U> one() {
		return new OneQuery<T, U>(this, false);
	}
	/**
	 * 問合せ結果のコンテナを遅延評価{@link Iterable}から正格評価{@link List}に変換するクエリを返す.
	 * @return クエリ
	 */
	public final Query<T, List<U>> list() {
		return new StrictListQuery<T, U>(this);
	}
	/**
	 * キャッシュ機能付きクエリに変換して返す.
	 * @return クエリ
	 */
	public Query<T,Iterable<U>> cached() {
		return CachedQuery.wrap(this);
	}
	/**
	 * 問合せ結果に別のクエリを適用するクエリを返す.
	 * @param q レシーバのクエリの問合せ結果に対して適用されるクエリ 
	 * @return クエリ
	 */
	public final<V> TypedValueIterableQuery<T, U, V> query(final Query<U, V> q) {
		return new TypedValueIterableQuery<T, U, V>(this, q);
	}
}
