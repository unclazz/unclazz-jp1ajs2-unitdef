package org.unclazz.jp1ajs2.unitdef;

import java.util.Collections;
import java.util.List;

/**
 * オブジェクト生成時に指定されたユニット定義パラメータ名と
 * そのパラメータを処理するためのクエリを用いて、ユニット定義から任意の情報を抽出するクエリ.
 * <p>このクエリは指定されたユニット定義パラメータが0個もしくは1個しか存在しないことを前提に振る舞う。
 * したがって{@link #queryFrom(Unit)}の戻り値は常に空のリストか単一要素のリストのいずれかとなる。</p>
 * 
 * @param <R> クエリが返すリストの要素型
 */
class UnitSingleParameterQuery<R> implements ListUnitQuery<R> {
	private final String name;
	private final ParameterQuery<R> query;
	UnitSingleParameterQuery(final String name, final ParameterQuery<R> query) {
		this.name = name;
		this.query = query;
	}
	public List<R> queryFrom(final Unit u) {
		final Parameter p = u.getParameter(name);
		if (p == null) {
			return Collections.emptyList();
		}
		final R r = query.queryFrom(p);
		if (r == null) {
			return Collections.emptyList();
		}
		return Collections.singletonList(r);
	}
}
