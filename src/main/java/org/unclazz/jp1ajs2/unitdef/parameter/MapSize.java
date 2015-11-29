package org.unclazz.jp1ajs2.unitdef.parameter;

/**
 * ユニット定義パラメータsz（マップサイズ）を表わすオブジェクト.
 * ヨコに並ぶユニット数とタテに並ぶユニット数で表される。
 * ヨコとタテいずれもユニット数として指定できる範囲は1〜100。
 * このパラメータの指定を省略した場合のデフォルト値はヨコが10、タテが8である。
 */
public final class MapSize {
	/**
	 * デフォルト値を表わすインスタンス.
	 * ヨコが10、タテが8。
	 */
	public static final MapSize DEFAULT = new MapSize(10, 8);
	/**
	 * ヨコに並ぶデフォルトのユニット数.
	 */
	public static final int DEFAULT_WIDTH = 10;
	/**
	 * タテに並ぶデフォルトのユニット数.
	 */
	public static final int DEFAULT_HEIGHT = 8;
	/**
	 * ヨコとタテの値を指定してインスタンスを取得する.
	 * @param w ヨコ
	 * @param h タテ
	 * @return インスタンス
	 */
	public static MapSize of(final int w, final int h) {
		if (w == DEFAULT_WIDTH && h == DEFAULT_HEIGHT) {
			return DEFAULT;
		}
		return new MapSize(w, h);
	}
	/**
	 * ヨコだけを指定してインスタンスを取得する.
	 * @param w ヨコ
	 * @return インスタンス
	 */
	public static MapSize ofWidth(final int w) {
		return of(w, DEFAULT_HEIGHT);
	}
	/**
	 * タテだけを指定してインスタンスを取得する.
	 * @param h タテ
	 * @return インスタンス
	 */
	public static MapSize ofHeight(final int h) {
		return of(DEFAULT_WIDTH, h);
	}
	
	private final int width;
	private final int height;
	private MapSize(final int width, final int height) {
		if (width < 1 || 100 < width) {
			throw new IllegalArgumentException("Width must be between 1 and 100");
		}
		if (height < 1 || 100 < height) {
			throw new IllegalArgumentException("Height must be between 1 and 100");
		}
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
