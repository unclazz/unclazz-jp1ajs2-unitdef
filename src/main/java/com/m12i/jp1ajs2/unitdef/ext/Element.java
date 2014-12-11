package com.m12i.jp1ajs2.unitdef.ext;

import com.m12i.jp1ajs2.unitdef.Unit;

/**
 * ジョブネット内のユニットをJP1/AJS2 - Viewのウィンドウに表示する際の位置情報.
 */
public class Element {

	private final Unit unit;
	private final int horizontalPixel;
	private final int verticalPixel;

	public Element(final Unit unit, final int horizontalPixel,
			final int verticalPixel) {
		if (unit == null) {
			throw new IllegalArgumentException();
		}
		this.unit = unit;
		this.horizontalPixel = horizontalPixel;
		this.verticalPixel = verticalPixel;
	}

	/**
	 * ユニット定義を取得する.
	 * @return ユニット定義
	 */
	public Unit getUnit() {
		return unit;
	}

	/**
	 * ユニットアイコンの水平位置のピクセル値を取得する.
	 * @return 0〜16,000
	 */
	public int getHorizontalPixel() {
		return horizontalPixel;
	}

	/**
	 * ユニットアイコンの垂直位置のピクセル値を取得する.
	 * @return 0〜10,000
	 */
	public int getVerticalPixel() {
		return verticalPixel;
	}

	/**
	 * ユニットアイコンの水平位置を取得する.
	 * {@link #getHorizontalPixel()}が返す数値はピクセル値であるのに対し、
	 * この{@code #getX()}が返す数値は、実際にユニットを配置可能なマス目の位置を示す抽象化された値。
	 * @return ユニットアイコンの水平位置（0から始まる）
	 */
	public int getX() {
		return (horizontalPixel - 80) / 160;
	}

	/**
	 * ユニットアイコンの垂直位置を取得する.
	 * {@link #getVerticalPixel()}が返す数値はピクセル値であるのに対し、
	 * この{@code #getY()}が返す数値は、実際にユニットを配置可能なマス目の位置を示す抽象化された値。
	 * @return ユニットアイコンの垂直位置（0から始まる）
	 */
	public int getY() {
		return (verticalPixel - 48) / 96;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + horizontalPixel;
		result = prime * result + unit.hashCode();
		result = prime * result + verticalPixel;
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
		if (horizontalPixel != other.horizontalPixel)
			return false;
		if (!unit.equals(other.unit))
			return false;
		if (verticalPixel != other.verticalPixel)
			return false;
		return true;
	}
}
