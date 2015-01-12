package com.m12i.jp1ajs2.unitdef;

import java.util.List;

import com.m12i.jp1ajs2.unitdef.util.Maybe;

/**
 * ユニット定義.
 */
public interface Unit extends Iterable<Unit> {
	/**
	 * ユニット名を返す.
	 * @return ユニット名
	 */
	String getName();
	/**
	 * 許可モードを返す.
	 * @return 許可モード
	 */
	Maybe<String> getPermissionMode();
	/**
	 * ユニット所有者となるJP1ユーザー名を返す.
	 * @return JP1ユーザー名
	 */
	Maybe<String> getOwnerName();
	/**
	 * JP1資源グループ名を返す.
	 * @return JP1資源グループ名
	 */
	Maybe<String> getResourceGroupName();

	/**
	 * ユニット定義パラメータのリストを返す. 
	 * ユニット定義パラメータのなかには同名のエントリーの重複が許されるものがある。
	 * @return ユニット定義パラメータのリスト
	 */
	List<Param> getParams();
	/**
	 * 引数で指定された名称のユニット定義パラメータのリストを返す.
	 * @param paramName パラメータ名
	 * @return ユニット定義パラメータのリスト
	 */
	List<Param> getParams(String paramName);
	/**
	 * 下位ユニットのユニット定義のリストを返す.
	 * @return サブユニットのユニット定義リスト
	 */
	List<Unit> getSubUnits();

	// すべてのユニットに（ほぼ）共通するパラメータにアクセスするためのユーティリティ
	/**
	 * 引数で指定された名前の下位ユニットを返す.
	 * @param unitName ユニット名
	 * @return 下位ユニットを要素とする{@link Maybe}
	 */
	List<Unit> getSubUnits(final String unitName);
	
	/**
	 * 引数で指定された名前の子孫ユニットを返す.
	 * @param unitName ユニット名
	 * @return 子孫ユニットを要素とする{@link Maybe}
	 */
	List<Unit> getDescendentUnits(final String unitName);
	
	/**
	 * 属性定義情報{@code "ty"}で指定されたユニット種別を返す.
	 * ユニット種別は必須パラメータであるため、このメソッドが{@code null}を返すことはない。
	 * @return ユニット種別
	 */
	UnitType getType();

	/**
	 * 属性定義情報{@code "cm"}で指定されたユニットのコメントを返す.
	 * @return コメント
	 */
	Maybe<String> getComment();
	/**
	 * ユニットの完全名を返す.
	 * @return 完全名
	 */
	String getFullQualifiedName();
}
