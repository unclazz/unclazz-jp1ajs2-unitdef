package org.unclazz.jp1ajs2.unitdef.parameter;

/**
 * ユニット定義パラメータsd（ジョブ実行開始日）を表わすインターフェース.
 * udやenなどの特殊なキーワード、末日からの逆算日付などに関して、
 * 公式リファレンスの記載には不明瞭な点が見られるため、
 * 実装コードではあまり厳密もしくは決定論的な記述すべきではない。
 */
public interface StartDate {
	/**
	 * 実行開始日指定方式.
	 */
	public static enum DesignationMethod {
		/**
		 * ud：ジョブネットのスケジュールはすべて未定義.
		 */
		UNDEFINED,
		/**
		 * en：ジョブネットを実行登録した日が実行開始日.
		 */
		ENTRY_DATE,
		/**
		 * 月初や月末からの日数や曜日により指定された日が実行開始日.
		 */
		SCHEDULED_DATE
	}
	
	public static enum CountingMethod {
		/**
		 * 絶対日数.
		 */
		ABSOLUTE,
		/**
		 * 相対日数.
		 */
		RELATIVE,
		/**
		 * 運用日数.
		 */
		BUSINESS_DAY,
		/**
		 * 休業日数.
		 */
		NON_BUSINESS_DAY
	}

	RuleNumber getRuleNumber();
	DesignationMethod getDesignationMethod();
	
	/**
	 * ユニット定義パラメータsdのうち年月が指定された値に対応するインターフェース.
	 * <p>sdのパラメータ値の構文
	 * <code>[N,]{[[yyyy/]mm/]{[+|*|@]dd|[+|*|@]b[-DD]|[+]{su|mo|tu|we|th|fr|sa} [:{n|b}]}|en|ud}</code>のうち、
	 * <code>[N,][[yyyy/]mm/]{[+|*|@]dd|[+|*|@]b[-DD]|[+]{su|mo|tu|we|th|fr|sa} [:{n|b}]}</code>に該当する。</p>
	 */
	public static interface ByYearMonth extends StartDate {
		YearMonth getYearMonth();
		
		/**
		 * ユニット定義パラメータsdのうち年月と日番号が指定された値に対応するインターフェース.
		 * <p>上位のインターフェース{@link ByYearMonth}が表わすパラメータ値の構文
		 * <code>[N,][[yyyy/]mm/]{[+|*|@]dd|[+|*|@]b[-DD]|[+]{su|mo|tu|we|th|fr|sa} [:{n|b}]}</code>のうち、
		 * <code>[N,][[yyyy/]mm/][+|*|@]dd|[+|*|@]b[-DD]</code>に該当する。</p>
		 */
		public static interface WithDayOfMonth extends ByYearMonth {
			public static final int LAST_DAY = Integer.MAX_VALUE;
			CountingMethod getCountingMethod();
			int getDay();
			boolean isBackward();
		}
		/**
		 * ユニット定義パラメータsdのうち年月と週番号が指定された値に対応するインターフェース.
		 * <p>上位のインターフェース{@link ByYearMonth}が表わすパラメータ値の構文
		 * <code>[N,][[yyyy/]mm/]{[+|*|@]dd|[+|*|@]b[-DD]|[+]{su|mo|tu|we|th|fr|sa} [:{n|b}]}</code>のうち、
		 * <code>[N,][+]{su|mo|tu|we|th|fr|sa} [:{n|b}]</code>に該当する。</p>
		 */
		public static interface WithDayOfWeek extends ByYearMonth {
			DayOfWeek getDayOfWeek();
			NumberOfWeek getNumberOfWeek();
			boolean isRelative();
		}
	}
	/**
	 * ユニット定義パラメータsdのうち特殊キーワードenが指定された値に対応するインターフェース.
	 * <p>sdのパラメータ値の構文
	 * <code>[N,]{[[yyyy/]mm/]{[+|*|@]dd|[+|*|@]b[-DD]|[+]{su|mo|tu|we|th|fr|sa} [:{n|b}]}|en|ud}</code>のうち、
	 * <code>[N,] en</code>に該当する。</p>
	 * <p>ジョブネットを実行登録した日を実行開始日とするもので、それ自体としては年月情報を持たない。</p>
	 */
	public static interface ByEntryDate extends StartDate {}
	/**
	 * ユニット定義パラメータsdのうち特殊キーワードudが指定された値に対応するインターフェース.
	 * <p>sdのパラメータ値の構文
	 * <code>[N,]{[[yyyy/]mm/]{[+|*|@]dd|[+|*|@]b[-DD]|[+]{su|mo|tu|we|th|fr|sa} [:{n|b}]}|en|ud}</code>のうち、
	 * <code>[N,] ud</code>に該当する。</p>
	 * <p>ジョブネットのスケジュールをすべて未定義にするもので、ルール番号（<code>N</code>）は必ず0となる。</p>
	 */
	public static interface Undefined extends StartDate {}
	/**
	 * ユニット定義パラメータsdの年月情報パートを保持するインターフェース.
	 * 年月のいずれも指定するケース、月のみ指定するケース、そして年月のいずれも指定しないケースの3つがある。
	 * 年月のいずれか・いずれも省略された場合にはジョブネットを実行登録した日付の年月情報が代用される。
	 */
	public static final class YearMonth {
		/**
		 * 年月いずれも指定してインスタンスを取得する.
		 * 指定可能な年は1994～2036。
		 * 指定可能な月は1〜12。
		 * @param year 年
		 * @param month 月
		 * @return インスタンス
		 */
		public static YearMonth of(final int year, final int month) {
			return new YearMonth(year, month);
		}
		/**
		 * 月のみ指定してインスタンスを取得する.
		 * 指定可能な月は1〜12。
		 * 年には実行登録した日付の年が代用される。
		 * @param month 月
		 * @return インスタンス
		 */
		public static YearMonth ofMonth(final int month) {
			return new YearMonth(month);
		}
		/**
		 * 年月のいずれも指定せず（省略して）インスタンスを取得する.
		 * @return インスタンス
		 */
		public static YearMonth ofEntryDate() {
			return ENTRY_DATE;
		}
		
		public static final YearMonth ENTRY_DATE = new YearMonth();
		public static final int NONE_SPECIFIED = -1;
		private final int year;
		private final int month;
		
		private YearMonth(final int y, final int m) {
			if (y != NONE_SPECIFIED && (y < 1994 && 2036 < y)) {
				throw new IllegalArgumentException("Invalid year");
			}
			if (m != NONE_SPECIFIED && (m < 1 && 12 < m)) {
				throw new IllegalArgumentException("Invalid month");
			}
			if (m == NONE_SPECIFIED && y != NONE_SPECIFIED) {
				throw new IllegalArgumentException("Month must be specified");
			}
			this.year = y;
			this.month = m;
		}
		private YearMonth(final int m) {
			this.year = NONE_SPECIFIED;
			this.month = m;
		}
		private YearMonth() {
			this.year = NONE_SPECIFIED;
			this.month = NONE_SPECIFIED;
		}
		public int getYear() {
			return year;
		}
		public int getMonth() {
			return month;
		}
		public boolean isYearSpecified() {
			return year != NONE_SPECIFIED;
		}
		public boolean isMonthSpecified() {
			return month != NONE_SPECIFIED;
		}
		public boolean isEntryDate() {
			return isMonthSpecified();
		}
		@Override
		public int hashCode() {
			final int prime = 31;
			int result = 1;
			result = prime * result + month;
			result = prime * result + year;
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
			YearMonth other = (YearMonth) obj;
			if (month != other.month)
				return false;
			if (year != other.year)
				return false;
			return true;
		}
	}
	/**
	 * ユニット定義パラメータsdの曜日指定パートで使用される週番号を表わすインターフェース.
	 * 省略した場合や数値の代わりにbを指定した場合など特殊なケースが存在する。
	 * 詳細については公式リファレンスを参照のこと。
	 */
	public static final class NumberOfWeek extends DefaultIntegral {
		/**
		 * 指定なしを表わすインスタンス.
		 */
		public static final NumberOfWeek NONE_SPECIFIED = new NumberOfWeek(-1);
		/**
		 * 最終週を表わすインスタンス.
		 */
		public static final NumberOfWeek LAST_WEEK = new NumberOfWeek(Integer.MAX_VALUE);
		/**
		 * 週番号を指定してインスタンスを取得する.
		 * 指定可能な週番号は1〜5。
		 * @param n 週番号
		 * @return インスタンス
		 */
		public static NumberOfWeek of(int n) {
			return new NumberOfWeek(n);
		}
		
		private NumberOfWeek(final int n) {
			super(n);
			if (-1 != n && (n < 1 || 5 < n) && n != Integer.MAX_VALUE) {
				throw new IllegalArgumentException("Invalid number");
			}
		}
		
		public boolean isLast() {
			return intValue() == Integer.MAX_VALUE;
		}
		public boolean isNoneSpecified() {
			return intValue() == -1;
		}
		@Override
		public String toString() {
			if (intValue() == -1) {
				return "NONE_SPECIFIED";
			} else if (intValue() == Integer.MAX_VALUE) {
				return "LAST_WEEK";
			} else {
				return Integer.toString(intValue());
			}
		}
		@Override
		public int hashCode() {
			final int prime = 31;
			int result = 1;
			result = prime * result + intValue();
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
			NumberOfWeek other = (NumberOfWeek) obj;
			if (intValue() != other.intValue())
				return false;
			return true;
		}
	}
}
