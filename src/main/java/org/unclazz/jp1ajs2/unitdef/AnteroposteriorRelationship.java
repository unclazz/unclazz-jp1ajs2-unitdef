package org.unclazz.jp1ajs2.unitdef;


/**
 * 関連線で結ばれたユニットのペアをあらわすオブジェクト.
 */
public class AnteroposteriorRelationship {

	private final Unit from;
	private final Unit to;
	private final UnitConnectionType type;

	/**
	 * @param from 始点
	 * @param to 終点
	 * @param type 接続種別
	 */
	public AnteroposteriorRelationship(final Unit from, final Unit to, final UnitConnectionType type) {
		if (from == null || to == null) {
			throw new IllegalArgumentException();
		}
		this.from = from;
		this.to = to;
		this.type = type;
	}

	/**
	 * @return 始点のユニット
	 */
	public Unit getFrom() {
		return from;
	}

	/**
	 * @return 終点のユニット
	 */
	public Unit getTo() {
		return to;
	}
	
	/**
	 * @return 接続種別
	 */
	public UnitConnectionType getType() {
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
		if (!from.equals(other.from))
			return false;
		if (!to.equals(other.to))
			return false;
		return true;
	}
	
	@Override
	public String toString() {
		return String.format("下位ユニットの前後関係 `%1$s`から`%2$s`へ（接続種別は`%3$s`）",
				from.getAttributes().getUnitName(), to.getAttributes().getUnitName(),
				type == UnitConnectionType.SEQUENTIAL ?
						"seq: 順接続" : "cond: 条件接続");
	}
}
