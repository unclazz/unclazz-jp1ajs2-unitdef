package org.unclazz.jp1ajs2.unitdef.builder;

import org.unclazz.jp1ajs2.unitdef.parameter.Element;
import org.unclazz.jp1ajs2.unitdef.parameter.UnitType;

final class DefaultElement implements Element {
	private final String unitName;
	private final UnitType unitType;
	private final int hPixel;
	private final int vPixel;

	DefaultElement(final String unitName, final UnitType unitType, 
			final int hPixel, final int vPixel) {
		this.unitName = unitName;
		this.unitType = unitType;
		this.hPixel = hPixel;
		this.vPixel = vPixel;
	}

	public int getHPixel() {
		return hPixel;
	}
	public int getVPixel() {
		return vPixel;
	}
	public int getXCoord() {
		return (hPixel - 80) / 160;
	}
	public int getYCoord() {
		return (vPixel - 48) / 96;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + hPixel;
		result = prime * result + unitName.hashCode();
		result = prime * result + vPixel;
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
		final Element other = (Element) obj;
		if (hPixel != other.getHPixel())
			return false;
		if (!unitName.equals(other.getUnitName()))
			return false;
		if (vPixel != other.getVPixel())
			return false;
		return true;
	}
	
	@Override
	public String toString() {
		return String.format("下位ユニット`%1$s`（種別は`%2$s`）の位置は水平方向`%3$s`・垂直方向`%4$s`",
				unitName, unitType.getDescription(),
				hPixel, vPixel);
	}
	@Override
	public String getUnitName() {
		return unitName;
	}
	@Override
	public UnitType getUnitType() {
		return unitType;
	}
}
