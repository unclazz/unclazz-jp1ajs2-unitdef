package org.unclazz.jp1ajs2.unitdef.parameter;

/**
 * ジョブ実行開始日.
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
	
	public static interface ByYearMonth extends StartDate {
		YearMonth getYearMonth();
		
		public static interface WithDayOfMonth extends ByYearMonth {
			public static final int LAST_DAY = Integer.MAX_VALUE;
			CountingMethod getCountingMethod();
			int getDay();
			boolean isBackward();
		}
		public static interface WithDayOfWeek extends ByYearMonth {
			DayOfWeek getDayOfWeek();
			NumberOfWeek getNumberOfWeek();
			boolean isRelative();
		}
	}
	public static interface ByEntryDate extends StartDate {}
	public static interface Undefined extends StartDate {}
	public static final class YearMonth {
		public static YearMonth of(final int year, final int month) {
			return new YearMonth(year, month);
		}
		public static YearMonth ofMonth(final int month) {
			return new YearMonth(month);
		}
		public static YearMonth ofEntryDate() {
			return ENTRY_DATE;
		}
		
		public static final YearMonth ENTRY_DATE = new YearMonth();
		public static final int NONE_SPECIFIED = -1;
		private final int year;
		private final int month;
		
		YearMonth(final int y, final int m) {
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
		YearMonth(final int m) {
			this.year = NONE_SPECIFIED;
			this.month = m;
		}
		YearMonth() {
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
	public static final class NumberOfWeek {
		public static final NumberOfWeek NONE_SPECIFIED = new NumberOfWeek(-1);
		public static final NumberOfWeek LAST_WEEK = new NumberOfWeek(Integer.MAX_VALUE);
		
		public static NumberOfWeek of(int n) {
			return new NumberOfWeek(n);
		}
		
		private final int n;
		
		private NumberOfWeek(final int n) {
			if (-1 != n && (n < 1 || 5 < n) && n != Integer.MAX_VALUE) {
				throw new IllegalArgumentException("Invalid number");
			}
			this.n = n;
		}
		
		public int intValue() {
			return n;
		}
		public boolean isLast() {
			return n == Integer.MAX_VALUE;
		}
		public boolean isNoneSpecified() {
			return n == -1;
		}
		@Override
		public String toString() {
			if (n == -1) {
				return "NONE_SPECIFIED";
			} else if (n == Integer.MAX_VALUE) {
				return "LAST_WEEK";
			} else {
				return Integer.toString(n);
			}
		}
		@Override
		public int hashCode() {
			final int prime = 31;
			int result = 1;
			result = prime * result + n;
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
			if (n != other.n)
				return false;
			return true;
		}
	}
}
