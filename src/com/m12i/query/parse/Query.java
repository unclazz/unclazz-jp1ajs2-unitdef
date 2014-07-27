package com.m12i.query.parse;

import java.util.Collection;
import java.util.List;

/**
 * 解析済みクエリを表わすオブジェクト.
 * コレクション要素を検索するためのAPIを提供します。
 * @param <E> 検索対象となるコレクションの要素型
 */
public interface Query<E> {
	/**
	 * クエリにマッチしたすべての要素を返す.
	 * 返却されるリストに含まれる要素の順序は、検索対象のコレクションとそのイテレータの実装次第となります。
	 * @param source 検索対象のコレクション
	 * @return クエリ内容にマッチしたすべての要素
	 */
	List<E> selectAllFrom(Collection<E> source);
	/**
	 * クエリにマッチした最初の要素を返す.
	 * クエリにマッチする要素が複数あった場合にいずれの要素が「最初の」要素とみなされるかは、
	 * 検索対象のコレクションとそのイテレータの実装次第となります。
	 * マッチする要素がなかった場合は{@code null}を返します。
	 * @param source 検索対象のコレクション
	 * @return クエリ内容にマッチした要素
	 */
	E selectOneFrom(Collection<E> source);
}
