package org.unclazz.jp1ajs2.unitdef.builder;

import org.unclazz.jp1ajs2.unitdef.parameter.AnteroposteriorRelationship;
import org.unclazz.jp1ajs2.unitdef.parameter.UnitConnectionType;

/**
 * 関連線で結ばれたユニットのペアをあらわすオブジェクト.
 */
class DefaultAnteroposteriorRelationship implements AnteroposteriorRelationship {

	private final CharSequence from;
	private final CharSequence to;
	private final UnitConnectionType type;

	/**
	 * @param from 始点
	 * @param to 終点
	 * @param type 接続種別
	 */
	DefaultAnteroposteriorRelationship(final CharSequence from, final CharSequence to, final UnitConnectionType type) {
		this.from = from;
		this.to = to;
		this.type = type;
	}

	/**
	 * @return 始点のユニット
	 */
	@Override
	public String getFromUnitName() {
		return from.toString();
	}

	/**
	 * @return 終点のユニット
	 */
	@Override
	public String getToUnitName() {
		return to.toString();
	}
	
	/**
	 * @return 接続種別
	 */
	@Override
	public UnitConnectionType getConnectionType() {
		return type;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + from.hashCode();
		result = prime * result + to.hashCode();
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		AnteroposteriorRelationship other = (AnteroposteriorRelationship) obj;
		if (!from.equals(other.getFromUnitName()))
			return false;
		if (!to.equals(other.getToUnitName()))
			return false;
		return true;
	}
	
	@Override
	public String toString() {
		return String.format("下位ユニットの前後関係 `%1$s`から`%2$s`へ（接続種別は`%3$s`）",
				from, to,
				type == UnitConnectionType.SEQUENTIAL ?
						"seq: 順接続" : "cond: 条件接続");
	}
}
