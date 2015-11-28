package org.unclazz.jp1ajs2.unitdef.parameter;

/**
 * ユニット定義パラメータfd（実行所要時間）を表わすオブジェクト.
 * fdはそれが指定されたユニットタイプにより上限値が変動する。
 * たとえばUNIXジョブやPCジョブの場合1440分であるが、ジョブネットの場合2879分である。
 * このオブジェクトではこれらの文脈依存の判断は行わない。
 */
public final class FixedDuration extends DefaultIntegral {
	/**
	 * インスタンスを生成して返す.
	 * @param minutes 分
	 * @return インスタンス
	 */
	public static FixedDuration of(final int minutes) {
		return new FixedDuration(minutes);
	}
	
	private FixedDuration(final int val) {
		super(val);
		if (val < 1) {
			throw new IllegalArgumentException("value of parameter 'fd' must be greater than 0");
		}
	}
	/**
	 * 秒数換算の実行所要時間を返す.
	 * @return 実行所要時間
	 */
	public int toSeconds() {
		return intValue() * 60;
	}
	/**
	 * 分数換算の実行所要時間を返す.
	 * @return 実行所要時間
	 */
	public int toMinutes() {
		return intValue();
	}
}
