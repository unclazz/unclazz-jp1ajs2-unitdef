package org.unclazz.jp1ajs2.unitdef.parameter;

/**
 * ユニット定義パラメータarを表わすオブジェクト.
 */
public interface AnteroposteriorRelationship {
	/** 先行するユニット名. */
	public String getFromUnitName();
	/** 後続のユニット名. */
	public String getToUnitName();
	/** 接続種別. */
	public UnitConnectionType getConnectionType();
}
