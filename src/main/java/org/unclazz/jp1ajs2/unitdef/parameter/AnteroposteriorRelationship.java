package org.unclazz.jp1ajs2.unitdef.parameter;

/**
 * ユニット定義パラメータarを表わすオブジェクト.
 */
public interface AnteroposteriorRelationship {
	/**
	 * @return 先行するユニット名
	 */
	public String getFromUnitName();
	/**
	 * @return 後続のユニット名
	 */
	public String getToUnitName();
	/**
	 * @return 接続種別
	 */
	public UnitConnectionType getConnectionType();
}
