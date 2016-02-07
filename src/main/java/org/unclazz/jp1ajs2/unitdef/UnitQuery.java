package org.unclazz.jp1ajs2.unitdef;

import java.util.List;

/**
 * ユニット定義情報から値を取得するためのクエリ.
 *
 * @param <T> クエリにより取得される値の型
 */
public interface UnitQuery<T> {
	/**
	 * ユニット定義情報から値を取得して返す.
	 * @param unit ユニット定義
	 * @return 取得された値
	 */
	List<T> queryFrom(Unit unit);
}
