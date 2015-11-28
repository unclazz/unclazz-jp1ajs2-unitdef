package org.unclazz.jp1ajs2.unitdef.builder;

import org.unclazz.jp1ajs2.unitdef.parameter.Element;
import org.unclazz.jp1ajs2.unitdef.parameter.UnitType;

public final class ElementBuilder {
	ElementBuilder() {}
	
	private String unitName;
	private UnitType unitType;
	private int horizontalPixel;
	private int verticalPixel;
	
	public ElementBuilder setHPixel(int px) {
		if (px != 0 && (px < 80 || 16000 < px)) {
			throw new IllegalArgumentException("Horizontal pixel must be between 80 and 16000");
		}
		final int mod = (px - 80) % 160;
		if (mod != 0) {
			throw new IllegalArgumentException("Horizontal pixel(px) "
					+ "must satisfy the equation: '(px - 80) % 160 = 0'");
		}
		this.horizontalPixel = px;
		return this;
	}
	public ElementBuilder setVPixel(int px) {
		if (px != 0 && (px < 48 || 10000 < px)) {
			throw new IllegalArgumentException("Vertical pixel must be between 48 and 10000");
		}
		final int mod = (px - 48) % 96;
		if (mod != 0) {
			throw new IllegalArgumentException("Vertical pixel(px) "
					+ "must satisfy the equation: '(px - 48) % 96 = 0'");
		}
		this.verticalPixel = px;
		return this;
	}
	public ElementBuilder setXCoord(int x) {
		return setHPixel(convertXCoordIntoHPixel(x));
	}
	public ElementBuilder setYCoord(int y) {
		return setVPixel(convertYCoordIntoHPixel(y));
	}
	public ElementBuilder setUnitName(String name) {
		if (name == null || name.isEmpty()) {
			throw new IllegalArgumentException("Unit name must be not null and not empty");
		}
		this.unitName = name;
		return this;
	}
	public ElementBuilder setUnitType(UnitType unitType) {
		if (unitType == null) {
			throw new IllegalArgumentException("Unit type must be not null");
		}
		this.unitType = unitType;
		return this;
	}
	public Element build() {
		if (unitName == null) {
			throw new IllegalArgumentException("Unit name must be not null and not empty");
		}
		if (unitType == null) {
			throw new IllegalArgumentException("Unit type must be not null");
		}
		return new DefaultElement(unitName, unitType, horizontalPixel, verticalPixel);
	}
	
	private static int convertXCoordIntoHPixel(int x) {
		return 80 + 160 * x;
	}
	private static int convertYCoordIntoHPixel(int y) {
		return 48 + 96 * y;
	}
}
