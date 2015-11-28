package org.unclazz.jp1ajs2.unitdef.parameter;

/**
 * ユニット定義パラメータsz（マップサイズ）を表わすオブジェクト.
 * ヨコに並ぶユニット数とタテに並ぶユニット数で表される。
 */
public final class MapSize {
	private final int width;
	private final int height;
	public MapSize(final int width, final int height) {
		this.height = height;
		this.width = width;
	}
	/**
	 * ヨコに並べられるユニット数.
	 * @return ユニット数
	 */
	public int getWidth() {
		return width;
	}
	/**
	 * タテに並べられるユニット数.
	 * @return ユニット数
	 */
	public int getHeight() {
		return height;
	}
}
