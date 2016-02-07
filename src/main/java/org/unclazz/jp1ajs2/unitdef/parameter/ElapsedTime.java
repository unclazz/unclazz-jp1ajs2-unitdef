package org.unclazz.jp1ajs2.unitdef.parameter;

import java.util.Calendar;
import java.util.Date;

/**
 * ユニット定義パラメータetmやtmitvで指定される実行開始時刻からの相対時間を表わすオブジェクト.
 */
public final class ElapsedTime extends DefaultIntegral {
	/**
	 * 指定された分に対応するインスタンスを返す.
	 * @param minutes 分
	 * @return インスタンス
	 * @throws IllegalArgumentException 時間と分をあわせて24時間を超える時間が指定された場合
	 */
	public static ElapsedTime of(final int minutes) {
		return new ElapsedTime(minutes);
	}
	/**
	 * 指定された時間と分に対応するインスタンスを返す.
	 * @param hours 時間
	 * @param minutes 分
	 * @return インスタンス
	 * @throws IllegalArgumentException 分として59より大きな数値が指定された場合か、
	 * 			時間と分をあわせて24時間を超える時間が指定された場合
	 */
	public static ElapsedTime of(final int hours, final int minutes) {
		if (59 < minutes) {
			throw new IllegalArgumentException("Minutes must not be greater than 59");
		}
		return new ElapsedTime(hours * 60 + minutes);
	}
	
	private ElapsedTime(final int val) {
		super(val);
		if (val < 1 || 1440 < val) {
			throw new IllegalArgumentException("Interval must be between 1 and 1440");
		}
	}
	
	/**
	 * このインスタンスが表わす経過時間の時間部分だけを返す.
	 * @return 時間
	 */
	public int getHours() {
		return intValue() / 60;
	}
	/**
	 * このインスタンスが表わす経過時間の分の部分だけを返す.
	 * 例えばインスタンスが1時間1分を表わす場合であれば{@code 1}を返す。
	 * @return 分
	 */
	public int getMinutes() {
		return intValue() % 60;
	}
	/**
	 * このインスタンスが表わす経過時間を分換算した値を返す.
	 * 例えばインスタンスが1時間1分を表わす場合であれば{@code 61}を返す。
	 * @return 分換算の値
	 */
	public int toMinutes() {
		return intValue();
	}
	/**
	 * このインスタンスが表わす経過時間を時間換算した値を返す.
	 * 例えばインスタンスが1時間1分を表わす場合であれば{@code 1}を返す。
	 * @return 時間換算の値
	 */
	public int toHours() {
		return intValue() / 60;
	}
	/**
	 * このインスタンスが表わす経過時間をもとに{@link Date}インスタンスを生成して返す.
	 * @return {@link Date}インスタンス
	 */
	public Date toDate() {
		final Calendar cal = Calendar.getInstance();
		cal.clear();
		cal.set(Calendar.HOUR_OF_DAY, intValue() / 60);
		cal.set(Calendar.MINUTE, intValue() % 60);
		return cal.getTime();
	}
}
