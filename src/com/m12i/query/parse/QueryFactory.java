package com.m12i.query.parse;

import java.util.Map;

import com.m12i.code.parse.ParseException;

/**
 * 文字列として表現されたクエリをパースして解析済みクエリを生成するファクトリ・オブジェクト.
 * @param <E> 解析済みクエリによる検索対象となるコレクションの要素型
 */
public final class QueryFactory<E> {
	/**
	 * {@link Map}に対する検索を行うクエリを生成するファクトリ・オブジェクト.
	 */
	public static final QueryFactory<Map<String, Object>> MAP_QUERY_FACTORY = 
			new QueryFactory<Map<String,Object>>(new Accessor<Map<String, Object>>() {
				@Override
				public String accsess(Map<String, Object> elem, String prop) {
					return elem.containsKey(prop) ? elem.get(prop).toString() : null;
				}
			});
	private static final ExpressionParser p = new ExpressionParser();
	private final Accessor<E> a;
	/**
	 * ファクトリ・オブジェクトのコンストラクタ.
	 * アクセサ・オブジェクト──クエリの条件式で指定されたプロパティを要素から取得するためのオブジェクト──をパラメータとして受け取り、
	 * ファクトリ・オブジェクトを初期化します。
	 * @param accessor アクセサ・オブジェクト
	 */
	public QueryFactory(Accessor<E> accessor) {
		if (accessor == null) {
			throw new IllegalArgumentException();
		}
		this.a = accessor;
	}
	/**
	 * 文字列として表現されたクエリをパースして解析済みクエリを生成する.
	 * @param query クエリ文字列
	 * @return 解析済みクエリ
	 * @throws QueryParseException クエリ内容に問題があり解析に失敗した場合
	 */
	public Query<E> create(String query) throws QueryParseException {
		try {
			return new QueryImpl<E>(p.parse(query), a);
		} catch (ParseException e) {
			throw new QueryParseException(e);
		}
	}
}
