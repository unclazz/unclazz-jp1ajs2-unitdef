package org.unclazz.jp1ajs2.unitdef;

import java.util.List;
import static org.unclazz.jp1ajs2.unitdef.util.ListUtils.*;

/**
 * 名前指定されたユニット定義パラメータを返すクエリ.
 * <p>このクラスのインスタンスは{@link UnitQueries#parameter(String)}を呼び出すことで取得できる。
 * したがって、例えば{@code unit.query(parameter("el"))}といったかたちで利用することができる。
 * この場合、メソッド呼び出し式の結果として{@link Parameter}のリストが得られる。</p>
 * <p>一方、{@link #valueAt(int)}を呼び出すことで{@link SubscriptedQueryFactory}のインスタンスを取得できる。
 * したがって、例えば{@code unit.query(parameter("el").valurAt(0).asString())}といったかたちで利用することもできる。
 * この場合、メソッド呼び出し式の結果として{@link String}のリストが得られる。</p>
 *
 */
public final class NameSpecifiedParameterQuery implements ListUnitQuery<Parameter> {
	static NameSpecifiedParameterQuery parameter(final String name) {
		return new NameSpecifiedParameterQuery(name);
	}
	
	private final String name;
	private NameSpecifiedParameterQuery(final String name) {
		this.name = name;
	}
	
	@Override
	public List<Parameter> queryFrom(final Unit unit) {
		final List<Parameter> result = linkedList();
		for (final Parameter p : unit.getParameters()) {
			if (p.getName().equalsIgnoreCase(name)) {
				result.add(p);
			}
		}
		return result;
	}
	
	/**
	 * {@link SubscriptedQueryFactory}のインスタンスを返す.
	 * @param i 読み取り対象のユニット定義パラメータ値の位置
	 * @return {@link SubscriptedQueryFactory}のインスタンス
	 */
	public SubscriptedQueryFactory valueAt(final int i) {
		return new SubscriptedQueryFactory(name, i);
	}
}
