package org.unclazz.jp1ajs2.unitdef.builder;

import org.unclazz.jp1ajs2.unitdef.parameter.DayOfWeek;
import org.unclazz.jp1ajs2.unitdef.parameter.RuleNumber;
import org.unclazz.jp1ajs2.unitdef.parameter.StartDate;
import org.unclazz.jp1ajs2.unitdef.parameter.StartDate.ByEntryDate;
import org.unclazz.jp1ajs2.unitdef.parameter.StartDate.ByYearMonth.WithDayOfMonth;
import org.unclazz.jp1ajs2.unitdef.parameter.StartDate.ByYearMonth.WithDayOfWeek;
import org.unclazz.jp1ajs2.unitdef.parameter.StartDate.CountingMethod;
import org.unclazz.jp1ajs2.unitdef.parameter.StartDate.DesignationMethod;
import org.unclazz.jp1ajs2.unitdef.parameter.StartDate.NumberOfWeek;
import org.unclazz.jp1ajs2.unitdef.parameter.StartDate.Undefined;
import org.unclazz.jp1ajs2.unitdef.parameter.StartDate.YearMonth;
import org.unclazz.jp1ajs2.unitdef.util.CharSequenceUtils;

/**
 * {@link StartDate}のためのビルダー.
 * <p>このビルダーが生成するオブジェクトは{@link StartDate}のサブクラスのいずれかである。
 * {@link StartDate}はそれが表現するユニット定義パラメータsdの構文の複雑さに起因して
 * 複数の拡張されたインターフェースを持っている。
 * 詳細については同インターフェースのJavadocを参照のこと。</p>
 */
public final class StartDateBuilder {
	StartDateBuilder() {}
	
	private DesignationMethod designationMethod = DesignationMethod.UNDEFINED;
	private RuleNumber ruleNumber = RuleNumber.UNDEFINED;
	private Integer year = null;
	private Integer month = null;
	private CountingMethod countingMethod = CountingMethod.ABSOLUTE;
	private Integer day = null;
	private boolean backward = false;
	private boolean relativeNumberOfWeek = false;
	private DayOfWeek dayOfWeek = null;
	private NumberOfWeek numberOfWeek = null;
	
	/**
	 * パラメータ値の指定方法を指定する.
	 * <p>{@link DesignationMethod}のインスタンスのいずれを指定するかによって、
	 * このビルダーが生成するオブジェクトが{@link StartDate}の拡張されたインターフェースのうちのいずれになるかが決まる。</p>
	 * <dl>
	 * <dt>{@link DesignationMethod#ENTRY_DATE}を指定した場合</dt>
	 * <dd>{@link StartDate.ByEntryDate}のインスタンスが生成される</dd>
	 * <dt>{@link DesignationMethod#SCHEDULED_DATE}を指定した場合</dt>
	 * <dd>{@link StartDate.ByYearMonth}の拡張されたインターフェースのうちいずれかのインスタンスが生成される</dd>
	 * <dt>{@link DesignationMethod#UNDEFINED}を指定した場合</dt>
	 * <dd>{@link StartDate.Undefined}のインスタンスが生成される</dd>
	 * </dl>
	 * @param dm 指定方法
	 * @return ビルダー
	 */
	public StartDateBuilder setDesignationMethod(DesignationMethod dm) {
		if (dm == null) throw new IllegalAccessError("Designation method must be not null");
		this.designationMethod = dm;
		return this;
	}
	/**
	 * スケジュール・ルール番号を指定する.
	 * @param rn ルール番号
	 * @return ビルダー
	 */
	public StartDateBuilder setRuleNumber(RuleNumber rn) {
		if (rn == null) throw new IllegalAccessError("Rule number must be not null");
		this.ruleNumber = rn;
		return this;
	}
	/**
	 * 日番号や週番号のカウント方法を指定する.
	 * @param cm 日番号や週番号のカウント方法
	 * @return ビルダー
	 */
	public StartDateBuilder setCountingMethod(CountingMethod cm) {
		if (cm == null) throw new IllegalAccessError("Counting method must be not null");
		this.countingMethod = cm;
		return this;
	}
	/**
	 * 年を指定する.
	 * @param y 年
	 * @return ビルダー
	 */
	public StartDateBuilder setYear(Integer y) {
		this.year = y;
		return this;
	}
	/**
	 * 月を指定する.
	 * @param m 月
	 * @return ビルダー
	 */
	public StartDateBuilder setMonth(Integer m) {
		this.month = m;
		return this;
	}
	/**
	 * 日番号を指定する.
	 * カウント方法は{@link CountingMethod}と
	 * {@link #setBackward(boolean)}で指定された値により変化する。
	 * @param d 日番号
	 * @return ビルダー
	 */
	public StartDateBuilder setDay(Integer d) {
		this.day = d;
		return this;
	}
	/**
	 * 日番号や週番号を末日起算の値とするよう指定する.
	 * @param b {@code true}の場合 末尾起算する
	 * @return ビルダー
	 */
	public StartDateBuilder setBackward(boolean b) {
		this.backward = b;
		return this;
	}
	/**
	 * 週番号を指定する.
	 * カウント方法は{@link #setRelativeNumberOfWeek(boolean)}と
	 * {@link #setBackward(boolean)}で指定された値により変化する。
	 * @param nw 週番号
	 * @return ビルダー
	 */
	public StartDateBuilder setNumberOfWeek(NumberOfWeek nw) {
		this.numberOfWeek = nw;
		return this;
	}
	/**
	 * 曜日を指定する.
	 * @param dw 曜日
	 * @return ビルダー
	 */
	public StartDateBuilder setDayOfWeek(DayOfWeek dw) {
		this.dayOfWeek = dw;
		return this;
	}
	/**
	 * 週番号を相対値で指定するよう指定する.
	 * @param relative {@code true}の場合 相対値とする
	 * @return ビルダー
	 */
	public StartDateBuilder setRelativeNumberOfWeek(boolean relative) {
		this.relativeNumberOfWeek = relative;
		return this;
	}
	/**
	 * 新しい{@link StartDate}インスタンスを生成して返す.
	 * @return 新しい{@link StartDate}インスタンス
	 */
	public StartDate build() {
		if (designationMethod == DesignationMethod.UNDEFINED) {
			if (ruleNumber.intValue() != 0) {
				throw new IllegalArgumentException("Rule number must be 0");
			}
			final Undefined result = new Undefined() {
				@Override
				public RuleNumber getRuleNumber() {
					return RuleNumber.UNDEFINED;
				}
				@Override
				public DesignationMethod getDesignationMethod() {
					return DesignationMethod.UNDEFINED;
				}
				@Override
				public String toString() {
					return StartDateBuilder.startDateToString(this);
				}
				@Override
				public int hashCode() {
					return 31 * 1;
				}
				@Override
				public boolean equals(final Object other) {
					return other != null && other instanceof Undefined;
				}
			};
			return result;
		} else if (designationMethod == DesignationMethod.ENTRY_DATE) {
			if (ruleNumber.intValue() == 0) {
				throw new IllegalArgumentException("Rule number must be greater than 0");
			}
			final ByEntryDate bed = new ByEntryDate() {
				private final RuleNumber ruleNumber = StartDateBuilder.this.ruleNumber;
				@Override
				public RuleNumber getRuleNumber() {
					return ruleNumber;
				}
				@Override
				public DesignationMethod getDesignationMethod() {
					return DesignationMethod.ENTRY_DATE;
				}
				@Override
				public String toString() {
					return StartDateBuilder.startDateToString(this);
				}
				@Override
				public int hashCode() {
					return ruleNumber.hashCode();
				}
				@Override
				public boolean equals(final Object other) {
					return other != null 
							&& other instanceof ByEntryDate 
							&& this.getRuleNumber() == ((ByEntryDate) other).getRuleNumber();
				}
			};
			return bed;
		} else if (designationMethod == DesignationMethod.SCHEDULED_DATE) {
			if (ruleNumber.intValue() == 0) {
				throw new IllegalArgumentException("Rule number must be greater than 0");
			}
			final YearMonth yearMonth;
			if (year == null) {
				yearMonth = YearMonth.ofMonth(month);
			} else if (month == null) {
				yearMonth = YearMonth.ENTRY_DATE;
			} else {
				yearMonth = YearMonth.of(year, month);
			}
			if (dayOfWeek == null) {
				if (countingMethod == null) {
					throw new IllegalArgumentException("Counting method must be not null");
				}
				final int primitiveDay = StartDateBuilder.this.day == null ? -1 : StartDateBuilder.this.day;
				final WithDayOfMonth result = new WithDayOfMonth() {
					private final RuleNumber ruleNumber = StartDateBuilder.this.ruleNumber;
					private final boolean backward =  StartDateBuilder.this.backward;
					private final int day = primitiveDay;
					private final CountingMethod countingMethod = StartDateBuilder.this.countingMethod;
					@Override
					public RuleNumber getRuleNumber() {
						return ruleNumber;
					}
					@Override
					public DesignationMethod getDesignationMethod() {
						return DesignationMethod.SCHEDULED_DATE;
					}
					@Override
					public YearMonth getYearMonth() {
						return yearMonth;
					}
					@Override
					public boolean isBackward() {
						return backward;
					}
					@Override
					public int getDay() {
						return day;
					}
					@Override
					public CountingMethod getCountingMethod() {
						return countingMethod;
					}
					@Override
					public String toString() {
						return StartDateBuilder.startDateToString(this);
					}
					@Override
					public int hashCode() {
						return ruleNumber.hashCode() + yearMonth.hashCode() +
								(backward ? 1 : 0) + day + countingMethod.hashCode();
					}
					@Override
					public boolean equals(final Object other) {
						if (other == null) return false;
						if (!(other instanceof WithDayOfMonth)) return false;
						final WithDayOfMonth other_ = (WithDayOfMonth) other;
						return this.ruleNumber.equals(other_.getRuleNumber())
								&& this.day == other_.getDay()
								&& this.countingMethod == other_.getCountingMethod()
								&& backward == other_.isBackward()
								&& yearMonth.equals(other_.getYearMonth());
					}
				};
				return result;
			} else {
				final NumberOfWeek numberOfWeek;
				if (this.backward) {
					numberOfWeek = NumberOfWeek.LAST_WEEK;
				} else {
					numberOfWeek = this.numberOfWeek;
				}
				final WithDayOfWeek result = new WithDayOfWeek() {
					private final RuleNumber ruleNumber = StartDateBuilder.this.ruleNumber;
					private final DayOfWeek dayOfWeek = StartDateBuilder.this.dayOfWeek;
					private final boolean relativeNumberOfWeek = StartDateBuilder.this.relativeNumberOfWeek;
					@Override
					public RuleNumber getRuleNumber() {
						return ruleNumber;
					}
					@Override
					public DesignationMethod getDesignationMethod() {
						return DesignationMethod.SCHEDULED_DATE;
					}
					@Override
					public YearMonth getYearMonth() {
						return yearMonth;
					}
					@Override
					public NumberOfWeek getNumberOfWeek() {
						return numberOfWeek;
					}
					@Override
					public DayOfWeek getDayOfWeek() {
						return dayOfWeek;
					}
					@Override
					public String toString() {
						return StartDateBuilder.startDateToString(this);
					}
					@Override
					public int hashCode() {
						return ruleNumber.hashCode() + yearMonth.hashCode() + numberOfWeek.hashCode() + dayOfWeek.hashCode();
					}
					@Override
					public boolean equals(final Object other) {
						if (other == null) return false;
						if (!(other instanceof WithDayOfWeek)) return false;
						final WithDayOfWeek other_ = (WithDayOfWeek) other;
						return this.ruleNumber.equals(other_.getRuleNumber())
								&& this.dayOfWeek == other_.getDayOfWeek()
								&& numberOfWeek.equals(other_.getNumberOfWeek())
								&& yearMonth.equals(other_.getYearMonth());
					}
					@Override
					public boolean isRelative() {
						return relativeNumberOfWeek;
					}
				};
				return result;
			}
			
		} else {
			throw new IllegalArgumentException("Invalid designation method");
		}
	}
	
	private static String startDateToString(final StartDate sd) {
		// 定形もしくはほぼ定形のものは先に処理
		if (sd.getDesignationMethod() == DesignationMethod.UNDEFINED) {
			return "ルール番号`0` ud: ジョブネットのスケジュールは未定義";
		} else if (sd.getDesignationMethod() == DesignationMethod.ENTRY_DATE) {
			return String.format("ルール番号`%d` ジョブネットの実行開始日は`en: ジョブネットを実行登録した日が実行開始日`", sd.getRuleNumber());
		}

		// 複雑系はStringBuilderで文字列を組み立てる
		final StringBuilder sb = CharSequenceUtils.builder();
		// 複雑系に共通の部分を組み立てる
		sb.append("ルール番号`").append(sd.getRuleNumber()).append("` ジョブネットの実行開始日は`");
		
		// タイミング指定方式ごとに処理
		if (sd instanceof WithDayOfMonth) {
			final WithDayOfMonth sdWithDayOfMonth = (WithDayOfMonth) sd;
			final YearMonth yearMonth = sdWithDayOfMonth.getYearMonth();
			final CountingMethod countingMethod = sdWithDayOfMonth.getCountingMethod();
			// 月初・月末からカウントする方式の場合
			sb.append(yearMonth.isYearSpecified() ? "実行登録した" : yearMonth.getYear()).append("年 ");
			sb.append(yearMonth.isMonthSpecified() ? "実行登録した" : yearMonth.getMonth()).append("月 ");
			
			if (countingMethod == CountingMethod.RELATIVE) {
				sb.append("相対日");
			} else if (countingMethod == CountingMethod.BUSINESS_DAY) {
				sb.append("運用日");
			} else if (countingMethod == CountingMethod.NON_BUSINESS_DAY) {
				sb.append("休業日");
			}
			
			if (!sdWithDayOfMonth.isBackward()) {
				// 月初からカウントする方式の場合
				sb.append(" 第").append(sdWithDayOfMonth.getDay()).append("日");
			} else {
				// 月末からカウントする方式の場合
				if (sdWithDayOfMonth.getDay() == WithDayOfMonth.LAST_DAY) {
					sb.append("の月末または最終運用日");
				} else {
					sb.append("の月末または最終運用日から逆算で 第").append(sdWithDayOfMonth.getDay()).append("日");
				}
			}
		} else if (sd instanceof WithDayOfWeek) {
			final WithDayOfWeek sdWithDayOfWeek = (WithDayOfWeek) sd;
			final NumberOfWeek numberOfWeek = sdWithDayOfWeek.getNumberOfWeek();
			// 曜日指定する方式の場合
			sb.append(numberOfWeek.isLast() 
					? "最終 " : (numberOfWeek.isNoneSpecified() 
					? "直近 " : "第" + numberOfWeek + " "));
			sb.append(sdWithDayOfWeek.getDayOfWeek().toJapaneseString());
		}
		return sb.append("`").toString();
	}
}
