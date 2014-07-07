package usertools.jp1ajs2.unitdef.ext;

import usertools.jp1ajs2.unitdef.core.Unit;

/**
 * 関連線で結ばれたユニットのペアをあらわすオブジェクト.
 */
public class Arrow {

	private final Unit from;
	private final Unit to;
	private final UnitConnectionType type;

	/**
	 * @param from
	 *            始点
	 * @param to
	 *            終点
	 * @param type
	 *            接続種別
	 */
	public Arrow(final Unit from, final Unit to, final UnitConnectionType type) {
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
	public UnitConnectionType type() {
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
		Arrow other = (Arrow) obj;
		if (!from.equals(other.from))
			return false;
		if (!to.equals(other.to))
			return false;
		return true;
	}
}
