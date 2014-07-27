package com.m12i.query.parse;

/**
 * クエリの条件式で指定されたプロパティを要素から取得するためのアクセサ.
 * @param <E> 対象の要素の型
 */
public interface Accessor<E> {
	/**
	 * 第1引数で指定された要素から第2引数で指定されたプロパティを取得して返す.
	 * 対象の要素に指定されたプロパティが存在しない場合は{@code null}を返します。
	 * 対象の要素に同名の複数のプロパティが存在した場合の挙動をどのようにするかの決定は実装者に委ねられています。
	 * @param elem 対象の要素
	 * @param prop 対象のプロパティ
	 * @return 取得結果
	 */
	String accsess(E elem, String prop);
}
