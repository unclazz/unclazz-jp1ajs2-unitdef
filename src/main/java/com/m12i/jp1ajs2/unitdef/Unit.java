package com.m12i.jp1ajs2.unitdef;

import java.util.List;

public interface Unit extends Iterable<Unit> {
	String getName();
	Maybe<String> getPermissionMode();
	Maybe<String> getOwnerName();
	Maybe<String> getResourceGroupName();

	/**
	 * ユニット定義パラメータのリストを返す. 
	 * ユニット定義パラメータのなかには同名のエントリーの重複が許されるものがあります。
	 * @return ユニット定義パラメータのリスト
	 */
	List<Param> getParams();
	/**
	 * 引数で指定された名称のユニット定義パラメータのリストを返す.
	 * 指定されたパラメータが存在しない場合は空のリストを返します。
	 * @param paramName パラメータ名
	 * @return ユニット定義パラメータのリスト
	 */
	List<Param> getParams(String paramName);
	/**
	 * サブユニットのユニット定義リストを返す.
	 * 
	 * @return サブユニットのユニット定義リスト
	 */
	List<Unit> getSubUnits();

	// すべてのユニットに（ほぼ）共通するパラメータにアクセスするためのユーティリティ
	/**
	 * 引数で指定された名前のサブユニットを返す.
	 * 指定された名前のサブユニットが存在しない場合、{@code null}を返します。
	 * 
	 * @param targetUnitName 対象ユニット名
	 * @return サブユニット
	 */
	Maybe<Unit> getSubUnit(final String targetUnitName);
	
	/**
	 * 属性定義情報"ty"で指定されたユニット種別を返す.
	 * ユニット種別は必須パラメータであるため、このメソッドが{@code null}を返すことはありません。
	 * 
	 * @return ユニット種別
	 */
	UnitType getType();

	/**
	 * 属性定義情報"cm"で指定されたユニットのコメントを返す.
	 * コメントは必須パラメータではありません。ユニットに"cm"が設定されていない場合、このメソッドは空文字列を返します。
	 * 
	 * @return コメント
	 */
	Maybe<String> getComment();
	/**
	 * ユニットの完全名を返す.
	 * 
	 * @return 完全名
	 */
	String getFullQualifiedName();
}
