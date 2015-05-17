package org.doogwood.jp1ajs2.unitdef;

/**
 * 曜日を表現する列挙型.
 * <p>ユニット定義パラメータ{@code "sd"}などの内容をJavaオブジェクト表現に変換するにあたり使用される。</p>
 */
public enum DayOfWeek {
	SUNDAY, MONDAY, TUESDAY, WEDNESDAY, THURSDAY, FRIDAY, SATURDAY;
	
	/**
	 * 曜日情報を日本語漢字表現（例：{@code "日曜日"}）で返す.
	 * @return 日本語漢字表現の曜日
	 */
	public String toJapaneseString() {
		switch (this) {
		case SUNDAY:
			return "日曜日";
		case MONDAY:
			return "月曜日";
		case TUESDAY:
			return "火曜日";
		case WEDNESDAY:
			return "水曜日";
		case THURSDAY:
			return "木曜日";
		case FRIDAY:
			return "金曜日";
		case SATURDAY:
			return "土曜日";
		default:
			return null;
		}
	}
	/**
	 * 曜日情報をアルファベット3文字の表現で返す.
	 * @return アルファベット3文字表現の曜日
	 */
	public String to3CharsString() {
		switch (this) {
		case SUNDAY:
			return "sun";
		case MONDAY:
			return "mon";
		case TUESDAY:
			return "tue";
		case WEDNESDAY:
			return "wed";
		case THURSDAY:
			return "thu";
		case FRIDAY:
			return "fri";
		case SATURDAY:
			return "sat";
		default:
			return null;
		}
	}
	/**
	 * 曜日情報をアルファベット2文字の表現で返す.
	 * @return アルファベット2文字表現の曜日
	 */
	public String to2CharsString() {
		switch (this) {
		case SUNDAY:
			return "sa";
		case MONDAY:
			return "mo";
		case TUESDAY:
			return "tu";
		case WEDNESDAY:
			return "we";
		case THURSDAY:
			return "th";
		case FRIDAY:
			return "fr";
		case SATURDAY:
			return "sa";
		default:
			return null;
		}
	}
	/**
	 * 先頭2・3文字のみの曜日表現文字列を受け取って対応する列挙型インスタンスを返す.
	 * @param code 曜日表現文字列
	 * @return 列挙型インスタンス
	 */
	public static DayOfWeek forCode(final String code) {
		final String cl = code.toLowerCase();
		for (final DayOfWeek d : values()) {
			final String dl = d.toString().toLowerCase();
			if (dl.startsWith(cl)) {
				return d;
			}
		}
		return null;
	}
}
