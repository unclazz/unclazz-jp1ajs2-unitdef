package com.m12i.query.parse;

import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

import com.m12i.code.parse.ParseException;

/**
 * 文字列として表現されたクエリをパースして解析済みクエリを生成するファクトリ・オブジェクト.
 * @param <E> 解析済みクエリによる検索対象となるコレクションの要素型
 */
public final class QueryFactory<E> {
	/**
	 * {@link Map}のコレクションのためのクエリ・ファクトリを生成する.
	 * @return ファクトリ・オブジェクト
	 */
	public static QueryFactory<Map<String, Object>> createMapQueryFactory() { 
		return new QueryFactory<Map<String,Object>>(new Accessor<Map<String, Object>>() {
				@Override
				public String accsess(Map<String, Object> elem, String prop) {
					return elem.containsKey(prop) ? elem.get(prop).toString() : null;
				}
			});
	}
	/**
	 * JavaBeansのコレクションのためのクエリ・ファクトリを生成する.
	 * @param elemType 検索対象コレクションの要素型
	 * @return ファクトリ・オブジェクト
	 */
	public static<T> QueryFactory<T> createBeanQueryFactory(final Class<T> elemType) { 
		return new QueryFactory<T>(new Accessor<T>() {
			private final Object[] args = new Object[0];
			private final HashMap<String, Method> getters = new HashMap<String, Method>();
			private Method getGetter(String prop) {
				if (getters.containsKey(prop)) {
					return getters.get(prop);
				} else {
					try {
						final Method getter = elemType.getMethod("get" +
								prop.substring(0, 1).toUpperCase() + prop.substring(1));
						getters.put(prop, getter);
						return getter;
					} catch (Exception e) {
						throw new RuntimeException(e);
					}
				}
			}
			@Override
			public String accsess(T elem, String prop) {
				try {
					return getGetter(prop).invoke(elem, args).toString();
				} catch (Exception e) {
					return null;
				}
			}
		});
	}
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
