package com.m12i.jp1ajs2.unitdef;

/**
 * 実行開始日.
 */
public class StartDate {
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
	/**
	 * 日付指定方式.
	 */
	public static enum TimingMethod {
		/**
		 * 月初から第何日目かで指定.
		 */
		DAY_OF_MONTH,
		/**
		 * 月末から何日目かで指定.
		 */
		DAY_OF_MONTH_INVERSELY,
		/**
		 * 曜日で指定.
		 */
		DAY_OF_WEEK,
		/**
		 * 曜日で指定（最終週）.
		 */
		DAY_OF_LAST_WEEK;
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
	
	public static final StartDate UNDEFINED = new StartDate(0, DesignationMethod.UNDEFINED,
			null, null, null, null, null, null, null);
	
	private final int ruleNo;
	private final DesignationMethod designationMethod;
	private final TimingMethod timingMethod;
	private final CountingMethod countingMethod;
	private final Integer yyyy;
	private final Integer mm;
	private final Integer dd;
	private final DayOfWeek dayOfWeek;
	private final Integer weekNo;
	
	StartDate(
			final int ruleNo,
			final DesignationMethod designationMethod,
			final Integer yyyy,
			final Integer mm,
			final Integer dd,
			final DayOfWeek dayOfWeek,
			final Integer weekNo,
			final TimingMethod timingMethod,
			final CountingMethod countingMethod
			) {
		this.ruleNo = ruleNo;
		this.designationMethod = designationMethod;
		this.yyyy = yyyy;
		this.mm = mm;
		this.dd = dd;
		this.dayOfWeek = dayOfWeek;
		this.weekNo = weekNo;
		this.timingMethod = timingMethod;
		this.countingMethod = countingMethod;
	}

	public int getRuleNo() {
		return ruleNo;
	}
	public DesignationMethod getDesignationMethod() {
		return designationMethod;
	}
	public TimingMethod getTimingMethod() {
		return timingMethod;
	}
	public CountingMethod getCountingMethod() {
		return countingMethod;
	}
	public Integer getYyyy() {
		return yyyy;
	}
	public Integer getMm() {
		return mm;
	}
	public Integer getDd() {
		return dd;
	}
	public DayOfWeek getDayOfWeek() {
		return dayOfWeek;
	}
	public Integer getWeekNo() {
		return weekNo;
	}
	@Override
	public String toString() {
		// 定形もしくはほぼ定形のものは先に処理
		if (designationMethod == DesignationMethod.UNDEFINED) {
			return "ルール番号`0` ud: ジョブネットのスケジュールは未定義";
		} else if (designationMethod == DesignationMethod.ENTRY_DATE) {
			return String.format("ルール番号`%d` ジョブネットの実行開始日は`en: ジョブネットを実行登録した日が実行開始日`", ruleNo);
		}

		// 複雑系はStringBuilderで文字列を組み立てる
		final StringBuilder sb = new StringBuilder();
		// 複雑系に共通の部分を組み立てる
		sb.append("ルール番号`").append(ruleNo).append("` ジョブネットの実行開始日は`");
		
		// タイミング指定方式ごとに処理
		if (timingMethod == TimingMethod.DAY_OF_MONTH || timingMethod == TimingMethod.DAY_OF_MONTH_INVERSELY) {
			// 月初・月末からカウントする方式の場合
			sb.append(yyyy == null ? "実行登録した" : yyyy.toString()).append("年 ");
			sb.append(mm == null ? "実行登録した" : mm.toString()).append("月 ");
			
			if (countingMethod == CountingMethod.RELATIVE) {
				sb.append("相対日");
			} else if (countingMethod == CountingMethod.BUSINESS_DAY) {
				sb.append("運用日");
			} else if (countingMethod == CountingMethod.NON_BUSINESS_DAY) {
				sb.append("休業日");
			}
			
			if (timingMethod == TimingMethod.DAY_OF_MONTH) {
				// 月初からカウントする方式の場合
				sb.append(" 第").append(dd).append("日");
			} else {
				// 月末からカウントする方式の場合
				if (dd == null) {
					sb.append("月末または最終運用日");
				} else {
					sb.append("から逆算で 第").append(dd).append("日");
				}
			}
		} else if (timingMethod == TimingMethod.DAY_OF_WEEK || timingMethod == TimingMethod.DAY_OF_LAST_WEEK) {
			// 曜日指定する方式の場合
			sb.append(timingMethod == TimingMethod.DAY_OF_LAST_WEEK ? "最終 " : (weekNo == null ? "直近 " : "第" + weekNo + " "));
			sb.append(dayOfWeek.toJapaneseString());
		}
		return sb.append("`").toString();
	}
}
