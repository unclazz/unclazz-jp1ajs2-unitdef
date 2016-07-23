package org.unclazz.jp1ajs2.unitdef.parameter;

import java.util.Calendar;
import java.util.Date;

/**
 * ユニット定義パラメータed（スケジュールの有効期日）を表すオブジェクト.
 */
public final class EndDate {
	/**
	 * 年月日情報をもとに新しいインスタンスを生成する.
	 * @param year 年
	 * @param month 月（1～12）
	 * @param dayOfMonth 日
	 * @return インスタンス
	 */
	public static EndDate of(final int year, final int month, final int dayOfMonth) {
		return new EndDate(year, month, dayOfMonth);
	}
	
	private final int year;
	private final int month;
	private final int dayOfMonth;
	
	private EndDate(final int y, final int m, final int d) {
		this.year = y;
		this.month = m;
		this.dayOfMonth = d;
	}

	/**
	 * @return 年
	 */
	public int getYear() {
		return year;
	}
	/**
	 * @return 月（1～12）
	 */
	public int getMonth() {
		return month;
	}
	/**
	 * @return 日
	 */
	public int getDayOfMonth() {
		return dayOfMonth;
	}
	/**
	 * オブジェクトに格納された年月日情報をもとに{@link Date}インスタンスを生成して返す.
	 * @return {@link Date}インスタンス
	 */
	public Date toDate() {
		final Calendar cal = Calendar.getInstance();
		cal.clear();
		cal.set(Calendar.YEAR, year);
		cal.set(Calendar.MONTH, month - 1);
		cal.set(Calendar.DAY_OF_MONTH, dayOfMonth);
		return cal.getTime();
	}
}
