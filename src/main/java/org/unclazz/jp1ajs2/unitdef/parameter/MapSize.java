package org.unclazz.jp1ajs2.unitdef.parameter;

/**
 * マップサイズ.
 * JP1/AJS2 Viewのウィンドウに表示するユニットアイコン数の最大値。
 */
public final class MapSize {
	private final int width;
	private final int height;
	public MapSize(final int width, final int height) {
		this.height = height;
		this.width = width;
	}
	public int getWidth() {
		return width;
	}
	public int getHeight() {
		return height;
	}
}
