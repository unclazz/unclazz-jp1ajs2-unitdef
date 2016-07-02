package org.unclazz.jp1ajs2.unitdef.query;

import java.util.List;

import org.unclazz.jp1ajs2.unitdef.Unit;
import org.unclazz.jp1ajs2.unitdef.query2.Query;

/**
 * {@link UnitQuery}インターフェースのヴァリアント.
 * <p>このインターフェースは簡便のため{@link UnitQuery}の型パラメータ記述方式のみを変更したものであり、
 * 機能的には何ら異なるところがない。
 * {@code UnitQuery<List<R>>}はすなわち{@code ListUnitQuery<R>}である。
 * だから例えば{@code UnitQuery<List<Element>>}はすなわち{@code ListUnitQuery<Element>}である。</p>
 *
 * @param <R> クエリが返すリストの要素型
 */
public interface ListUnitQuery<R> extends UnitQuery<List<R>>, Query<Unit,List<R>> {
	List<R> queryFrom(Unit u);
}
