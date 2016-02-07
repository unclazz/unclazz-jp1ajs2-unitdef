package org.unclazz.jp1ajs2.unitdef;

/**
 * ユニット属性パラメータを表わすインターフェース.
 */
public interface Attributes {
	/**
	 * ユニット名を返す.
	 * このメソッドが返す値は{@code null}でも{@code ""}でもないことが保証されている。
	 * @return ユニット名
	 */
	String getUnitName();
	/**
	 * 許可モードを返す.
	 * このメソッドが返す値は{@code null}でないことが保証されている。
	 * ユニット定義ファイル上指定がない場合は空文字列が返される。
	 * @return 許可モード
	 */
	String getPermissionMode();
	/**
	 * ユニットの所有者となるJP1ユーザの名前を返す.
	 * このメソッドが返す値は{@code null}でないことが保証されている。
	 * ユニット定義ファイル上指定がない場合は空文字列が返される。
	 * @return JP1ユーザ名
	 */
	String getJP1UserName();
	/**
	 * JP1資源グループ名を返す.
	 * このメソッドが返す値は{@code null}でないことが保証されている。
	 * ユニット定義ファイル上指定がない場合は空文字列が返される。
	 * @return JP1資源グループ名
	 */
	String getResourceGroupName();
}
