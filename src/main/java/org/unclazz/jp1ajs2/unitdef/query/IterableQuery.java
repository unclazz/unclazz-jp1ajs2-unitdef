package org.unclazz.jp1ajs2.unitdef.query;

import java.util.List;
import java.util.NoSuchElementException;

import org.unclazz.jp1ajs2.unitdef.util.Predicate;

/**
 * 問合せ結果として{@link Iterable}を返すクエリの共通インターフェース.
 * <p>フィルタ条件機能やキャッシュ機能など複数の拡張が行われた{@link Query}インターフェース。</p>
 *
 * @param <T> 問合せ対象の型
 * @param <U> 問合せ結果の型
 */
public interface IterableQuery<T,U> extends Query<T, Iterable<U>> {
	/**
	 * {@link Predicate}で表される問合せ条件を追加した新しいクエリを返す.
	 * @param pred 問合せ条件
	 * @return クエリ
	 */
	Query<T,Iterable<U>> and(final Predicate<U> pred);
	/**
	 * 問合せの結果のうち最初の1件だけを取得するためのクエリを返す.
	 * <p>問合せ結果が0件のときはデフォルト値を返す。</p>
	 * @param defaultValue デフォルト値
	 * @return クエリ
	 */
	OneQuery<T, U> one(U defaultValue);
	/**
	 * 問合せの結果のうち最初の1件だけを取得するためのクエリを返す.
	 * <p>引数が{@code true}の場合、問合せ結果が0件のときは{@code null}を返す。
	 * 引数が{@code false}の場合、問合せ結果が0件のときは{@link NoSuchElementException}をスローする。</p>
	 * @param nullable {@code true}の場合、問合せ結果が0件のときは{@code null}を返す
	 * @return クエリ
	 * @throws NoSuchElementException 問合せ結果が0件 かつ 引数に{@code false}が設定されていたとき
	 */
	OneQuery<T, U> one(boolean nullable);
	/**
	 * 問合せの結果のうち最初の1件だけを取得するためのクエリを返す.
	 * <p>問合せ結果が0件のときは{@link NoSuchElementException}をスローする。</p>
	 * @return クエリ
	 * @throws NoSuchElementException 問合せ結果が0件のとき
	 */
	OneQuery<T, U> one();
	/**
	 * 問合せ結果のコンテナを遅延評価{@link Iterable}から正格評価{@link List}に変換するクエリを返す.
	 * @return クエリ
	 */
	ListQuery<T, U> list();
	/**
	 * 問合せ結果のコンテナを遅延評価{@link Iterable}から正格評価{@link List}に変換するクエリを返す.
	 * @param cached {@code true}の場合 キャッシュ機能が付与される
	 * @return クエリ
	 */
	ListQuery<T, U> list(boolean cached);
	/**
	 * キャッシュ機能付きクエリに変換して返す.
	 * @return クエリ
	 */
	Query<T,Iterable<U>> cached();
	/**
	 * 問合せ結果に別のクエリを適用するクエリを返す.
	 * @param q レシーバのクエリの問合せ結果に対して適用されるクエリ 
	 * @return クエリ
	 */
	<V> IterableQuery<T,V> query(final Query<U, V> q);
}
