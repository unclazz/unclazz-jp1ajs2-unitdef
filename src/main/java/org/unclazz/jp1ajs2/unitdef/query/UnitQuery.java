package org.unclazz.jp1ajs2.unitdef.query;

import org.unclazz.jp1ajs2.unitdef.Unit;

/**
 * ユニット定義情報から値を取得するためのクエリ.
 *
 * @param <R> クエリにより取得される値の型
 */
public interface UnitQuery<R> {
	/**
	 * ユニット定義情報から値を取得して返す.
	 * @param unit ユニット定義
	 * @return 取得された値
	 */
	R queryFrom(Unit unit);
}
