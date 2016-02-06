package org.unclazz.jp1ajs2.unitdef.parameter;

import java.util.Calendar;
import java.util.Date;

/**
 * ユニット定義パラメータsy・eyなどで指定された時間を表わすオブジェクト.
 * 表現可能な時間の範囲は0時間00分以上47時間59分以下。
 */
public final class Time implements Comparable<Time> {
	/**
	 * 指定された時間・分の値に対応するインスタンスを返す.
	 * @param hours 時間
	 * @param minutes 分
	 * @return インスタンス
	 * @throws IllegalArgumentException このオブジェクトが表現可能な範囲を超える値が指定された場合
	 */
	public static Time of(final int hours, final int minutes) {
		if (hours == 0 && minutes == 0) {
			return MIDNIGHT;
		} else if (hours == 12 && minutes == 0) {
			return NOON;
		} else if (hours == 47 && minutes == 59) {
			return MAX;
		} else {
			return new Time(hours, minutes);
		}
	}
	/**
	 * 指定された分の値に対応するインスタンスを返す.
	 * @param minutes 分
	 * @return インスタンス
	 * @throws IllegalArgumentException このオブジェクトが表現可能な範囲を超える値が指定された場合
	 */
	public static Time ofMinutes(final int minutes) {
		return of(minutes / 60, minutes % 60);
	}
	
	/**
	 * 0:00を表わすインスタンス.
	 */
	public static final Time MIDNIGHT = new Time(0, 0);
	
	/**
	 * 12:00を表わすインスタンス.
	 */
	public static final Time NOON = new Time(12, 0);
	
	/**
	 * 0:00を表わすインスタンス.
	 */
	public static final Time MIN = MIDNIGHT;
	
	/**
	 * 47:59を表わすインスタンス.
	 */
	public static final Time MAX = new Time(47, 59);
	
	/**
	 * 時間.
	 */
	private final int hours;
	
	/**
	 * 分.
	 */
	private final int minutes;
	
	/**
	 * コンストラクタ.
	 * @param h 時間
	 * @param m 分
	 */
	private Time(int h, int m) {
		if (h < 0 || 47 < h) {
			throw new IllegalArgumentException("Hours must be between 0 and 47");
		}
		if (m < 0 || 59 < m) {
			throw new IllegalArgumentException("Minutes must be between 0 and 59");
		}
		this.hours = h;
		this.minutes = m;
	}
	
	/**
	 * 時間を返す.
	 * @return 時間
	 */
	public int getHours() {
		return hours;
	}
	
	/**
	 * 分を返す.
	 * @return 分
	 */
	public int getMinutes() {
		return minutes;
	}
	
	/**
	 * このオブジェクトが表わす時間・分を{@link Date}型に変換する.
	 * @return {@link Date}オブジェクト
	 */
	public Date toDate() {
		final Calendar cal = Calendar.getInstance();
		cal.clear();
		cal.set(Calendar.HOUR_OF_DAY, hours);
		cal.set(Calendar.MINUTE, minutes);
		return cal.getTime();
	}
	
	@Override
	public String toString() {
		return String.format("%02d:%02d", hours, minutes);
	}
	
	@Override
	public int compareTo(Time o) {
		final int hoursDelta = this.hours - o.hours;
		return hoursDelta == 0 ? this.minutes - o.minutes : hoursDelta;
	}
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + hours;
		result = prime * result + minutes;
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
		Time other = (Time) obj;
		if (hours != other.hours)
			return false;
		if (minutes != other.minutes)
			return false;
		return true;
	}
}
