package org.unclazz.jp1ajs2.unitdef;

import java.util.List;

import org.unclazz.jp1ajs2.unitdef.query.Query;

/**
 * ユニット定義パラメータを表わすインターフェース.
 */
public interface Parameter extends Component {
	/**
	 * ユニット定義パラメータ名を返す.
	 * このメソッドが返す値は{@code null}でも{@code ""}でもないことが保証されている。
	 * @return ユニット定義パラメータ名
	 */
	String getName();
	/**
	 * ユニット定義パラメータ値のリストを返す.
	 * @return ユニット定義パラメータ値のリスト
	 */
	List<ParameterValue> getValues();
	/**
	 * クエリを使用してユニット定義パラメータから情報を取得する.
	 * @param <R> クエリにより返される値の型
	 * @param q クエリ
	 * @return クエリにより取得された値
	 */
	<R> R query(Query<Parameter,R> q);
}
