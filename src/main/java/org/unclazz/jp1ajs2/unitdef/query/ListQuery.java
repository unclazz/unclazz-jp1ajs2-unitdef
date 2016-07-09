package org.unclazz.jp1ajs2.unitdef.query;

import java.util.List;
import java.util.NoSuchElementException;

/**
 * 問合せ結果としてリストを返すクエリ.
 *
 * @param <T> 問合せ対象の型
 * @param <U> 問合せ結果リストの要素型
 */
public interface ListQuery<T, U> extends Query<T, List<U>> {
	/**
	 * リストの最初の要素を問合せるクエリを返す.
	 * <p>クエリは要素が1つもないとき{@link NoSuchElementException}をスローする。</p>
	 * @return クエリ
	 * @throws NoSuchElementException リストの要素数が0の場合
	 */
	Query<T, U> first();
	/**
	 * リストの最初の要素を問合せるクエリを返す.
	 * <p>クエリは要素が1つもないとき{@link NoSuchElementException}をスローする。</p>
	 * @param nullale {@code true}の場合 クエリは要素が1つもないとき例外スローをせず{@code null}を返す
	 * @return クエリ
	 */
	Query<T, U> first(boolean nullale);
	/**
	 * リストの最初の要素を問合せるクエリを返す.
	 * <p>クエリは要素が1つもないときデフォルト値を返す。</p>
	 * @param defaultValue デフォルト値
	 * @return クエリ
	 */
	Query<T, U> first(U defaultValue);
	/**
	 * リストの最後の要素を問合せるクエリを返す.
	 * <p>クエリは要素が1つもないとき{@link NoSuchElementException}をスローする。</p>
	 * @return クエリ
	 */
	Query<T, U> last();
	/**
	 * リストの最後の要素を問合せるクエリを返す.
	 * <p>クエリは要素が1つもないとき{@link NoSuchElementException}をスローする。</p>
	 * @param nullale {@code true}の場合 クエリは要素が1つもないとき例外スローをせず{@code null}を返す
	 * @return クエリ
	 */
	Query<T, U> last(boolean nullale);
	/**
	 * リストの最後の要素を問合せるクエリを返す.
	 * <p>クエリは要素が1つもないときデフォルト値を返す。</p>
	 * @param defaultValue デフォルト値
	 * @return クエリ
	 */
	Query<T, U> last(U defaultValue);
}
