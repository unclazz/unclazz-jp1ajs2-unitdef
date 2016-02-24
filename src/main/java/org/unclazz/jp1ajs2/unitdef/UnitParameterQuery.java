package org.unclazz.jp1ajs2.unitdef;

import static org.unclazz.jp1ajs2.unitdef.util.ListUtils.linkedList;

import java.util.List;

/**
 * オブジェクト生成時に指定されたユニット定義パラメータ名と
 * そのパラメータを処理するためのクエリを用いて、ユニット定義から任意の情報を抽出するクエリ.
 * 
 * @param <R> クエリが返すリストの要素型
 */
class UnitParameterQuery<R> implements ListUnitQuery<R> {
	private final String name;
	private final ParameterQuery<R> query;
	UnitParameterQuery(final String name, final ParameterQuery<R> query) {
		this.name = name;
		this.query = query;
	}
	public List<R> queryFrom(final Unit u) {
		final List<R> result = linkedList();
		for (final Parameter p : u.getParameters()) {
			if (p.getName().equals(name)) {
				final R t = query.queryFrom(p);
				if (t != null) {
					result.add(t);
				}
			}
		}
		return result;
	}
}
