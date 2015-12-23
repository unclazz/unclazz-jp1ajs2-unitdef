package org.unclazz.jp1ajs2.unitdef.parameter;

/**
 * ユニット定義パラメータarを表わすオブジェクト.
 */
public interface AnteroposteriorRelationship {
	public String getFromUnitName();
	public String getToUnitName();
	public UnitConnectionType getConnectionType();
}
