package org.unclazz.jp1ajs2.unitdef;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.MatchResult;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.unclazz.jp1ajs2.unitdef.builder.Builders;
import org.unclazz.jp1ajs2.unitdef.builder.EndScheduledTimeBuilder;
import org.unclazz.jp1ajs2.unitdef.builder.StartDateBuilder;
import org.unclazz.jp1ajs2.unitdef.builder.StartScheduledTimeBuilder;
import org.unclazz.jp1ajs2.unitdef.parameter.ConnectorControllingSyncOption;
import org.unclazz.jp1ajs2.unitdef.parameter.DayOfWeek;
import org.unclazz.jp1ajs2.unitdef.parameter.DeleteOption;
import org.unclazz.jp1ajs2.unitdef.parameter.EndScheduledTime;
import org.unclazz.jp1ajs2.unitdef.parameter.EnvironmentVariable;
import org.unclazz.jp1ajs2.unitdef.parameter.EvaluateConditionType;
import org.unclazz.jp1ajs2.unitdef.parameter.ExecutionCycle;
import org.unclazz.jp1ajs2.unitdef.parameter.ExecutionTimedOutStatus;
import org.unclazz.jp1ajs2.unitdef.parameter.ExecutionUserType;
import org.unclazz.jp1ajs2.unitdef.parameter.HoldAttrType;
import org.unclazz.jp1ajs2.unitdef.parameter.LinkedRule;
import org.unclazz.jp1ajs2.unitdef.parameter.MailAddress;
import org.unclazz.jp1ajs2.unitdef.parameter.ResultJudgmentType;
import org.unclazz.jp1ajs2.unitdef.parameter.RuleNumber;
import org.unclazz.jp1ajs2.unitdef.parameter.StartDate;
import org.unclazz.jp1ajs2.unitdef.parameter.StartScheduledTime;
import org.unclazz.jp1ajs2.unitdef.parameter.StartTime;
import org.unclazz.jp1ajs2.unitdef.parameter.WriteOption;
import org.unclazz.jp1ajs2.unitdef.parameter.ExecutionCycle.CycleUnit;
import org.unclazz.jp1ajs2.unitdef.parameter.StartDate.CountingMethod;
import org.unclazz.jp1ajs2.unitdef.parameter.StartDate.DesignationMethod;
import org.unclazz.jp1ajs2.unitdef.parameter.StartDate.NumberOfWeek;
import org.unclazz.jp1ajs2.unitdef.parser.EnvParamParser;
import org.unclazz.jp1ajs2.unitdef.util.Optional;

/**
 * ユニット定義パラメータへのアクセスを提供するユーティリティ.<br>
 * <p>パラメータを検索し値を抽出するための一般的メソッドに加えて、
 * 特定パラメータに特化したアクセサ・メソッドも提供する。</p>
 * <p>メソッド名はいずれも{@code "fd"}・{@code "eu"}・{@code "tmitv"}といった
 * ユニット定義ファイルにおける縮約名から推論されたパラメータ名。</p>
 */
public final class Params {
	private Params() {}
	
	/**
	 * {@code "env"}パラメータのためのパーサー.
	 */
	private static final EnvParamParser envParamParser = new EnvParamParser();
	/**
	 * {@code "sy"}パラメータの値を解析するための正規表現パターン.
	 */
	private static final Pattern SY = Pattern.compile("((\\d+),\\s*)?((\\d+):(\\d+)|(M|U|C)(\\d+))");
	/**
	 * {@code "sd"}パラメータの値を解析するための正規表現パターン.
	 */
	private static final Pattern SD_OUTER = Pattern.compile("((\\d+),\\s*)?(en|ud|.+)");
	private static final Pattern SD_INNER = 
			Pattern.compile("(((\\d\\d\\d\\d)/)?(\\d{1,2})/)?((\\+|\\*|@)?(\\d+)"
					+ "|(\\+|\\*|@)?b(-(\\d+))?"
					+ "|(\\+)?(su|mo|tu|we|th|fr|sa)(\\s*:(\\d|b))?)");
	/**
	 * {@code "st"}パラメータの値を解析するための正規表現パターン.
	 */
	private static final Pattern ST = Pattern.compile("((\\d+),\\s*)?(\\+)?((\\d+):(\\d+))");
	/**
	 * {@code "cy"}パラメータの値を解析するための正規表現パターン.
	 */
	private static final Pattern CY = Pattern.compile("((\\d+),\\s*)?\\((\\d+),\\s*(y|m|w|d)\\)");
	

	/**
	 * 引数で指定された名称のユニット定義パラメータを検索して値を抽出する.
	 * @param unit ユニット定義
	 * @param paramName ユニット定義パラメータ名
	 * @return ユニット定義パラメータ値
	 */
	public static List<String> getStringValues(final Unit unit, final String paramName) {
		final List<String> list = new ArrayList<String>();
		for (final Parameter p : unit.getParameters(paramName)) {
			list.add(p.getValue(0).getRawCharSequence().toString());
		}
		return list;
	}

	/**
	 * 引数で指定された名称のユニット定義パラメータを検索して値を抽出する.
	 * 値は整数としてパースされた上で返される。
	 * @param unit ユニット定義
	 * @param paramName ユニット定義パラメータ名
	 * @return ユニット定義パラメータ値
	 */
	public static List<Integer> getIntValues(final Unit unit, final String paramName) {
		final List<Integer> list = new ArrayList<Integer>();
		for (final Parameter p : unit.getParameters(paramName)) {
			try {
				list.add(Integer.parseInt(p.getValue(0).getRawCharSequence().toString()));
			} catch (final NumberFormatException e) {
				// Do nothing.
			}
		}
		return list;
	}

	/**
	 * 引数で指定された名称のユニット定義パラメータを検索して値を抽出する.
	 * 値は長整数としてパースされた上で返される。
	 * @param unit ユニット定義
	 * @param paramName ユニット定義パラメータ名
	 * @return ユニット定義パラメータ値
	 */
	public static List<Long> getLongValues(final Unit unit, final String paramName) {
		final List<Long> list = new ArrayList<Long>();
		for (final Parameter p : unit.getParameters(paramName)) {
			try {
				list.add(Long.parseLong(p.getValue(0).getRawCharSequence().toString()));
			} catch (final NumberFormatException e) {
				// Do nothing.
			}
		}
		return list;
	}

	/**
	 * 引数で指定された名称のユニット定義パラメータを検索して値を抽出する.
	 * 値は倍精度浮動小数点数としてパースされた上で返される。
	 * @param unit ユニット定義
	 * @param paramName ユニット定義パラメータ名
	 * @return ユニット定義パラメータ値
	 */
	public static List<Double> getDoubleValues(final Unit unit, final String paramName) {
		final List<Double> list = new ArrayList<Double>();
		for (final Parameter p : unit.getParameters(paramName)) {
			try {
				list.add(Double.parseDouble(p.getValue(0).getRawCharSequence().toString()));
			} catch (final NumberFormatException e) {
				// Do nothing.
			}
		}
		return list;
	}

	/**
	 * 引数で指定された名称のユニット定義パラメータを検索して値を抽出する.
	 * 値はタプルもどきとしてパースされた上で返される。
	 * @param unit ユニット定義
	 * @param paramName ユニット定義パラメータ名
	 * @return ユニット定義パラメータ値
	 */
	public static List<Tuple> getTupleValues(final Unit unit, final String paramName) {
		final List<Tuple> list = new ArrayList<Tuple>();
		for (final Parameter p : unit.getParameters(paramName)) {
			final ParameterValue v = p.getValue(0);
			if (v.getType() == ParameterValueType.TUPLE) {
				list.add(v.getTuple());
			}
		}
		return list;
	}

	/**
	 * 引数で指定された名称のユニット定義パラメータを検索して値を抽出する.
	 * 値は真偽値としてパースされた上で返される。
	 * @param unit ユニット定義
	 * @param paramName ユニット定義パラメータ名
	 * @return ユニット定義パラメータ値
	 */
	public static List<Boolean> getBoolValues(final Unit unit, final String paramName) {
		final List<Boolean> list = new ArrayList<Boolean>();
		for (final Parameter p : unit.getParameters(paramName)) {
			final String v = p.getValue(0).getRawCharSequence().toString().toLowerCase();
			if (v.equals("y") || v.equals("yes") || v.equals("on") || v.equals("t") || v.equals("true") || v.equals("1")) {
				list.add(true);
			} else if(v.equals("n") || v.equals("no") || v.equals("off") || v.equals("f") || v.equals("false") || v.equals("0")) {
				list.add(false);
			}
		}
		return list;
	}
	
	/**
	 * ユニット定義パラメータ{@code "sd"}で指定された実行開始日のリストを返す.
	 * @param u ユニット定義
	 * @return 実行開始日のリスト
	 */
	public static List<StartDate> getStartDates(final Unit u) {
		final List<StartDate> sds = new ArrayList<StartDate>();
		for (final Parameter p : u.getParameters("sd")) {
			sds.add(getStartDate(p));
		}
		return sds;
	}
	/**
	 * ユニット定義パラメータ{@code "sd"}で指定された実行開始日を返す.
	 * @param p ユニット定義パラメータ
	 * @return 実行開始日
	 */
	public static StartDate getStartDate(final Parameter p) {
		final Matcher m0 = SD_OUTER.matcher(p.getValue(0).getRawCharSequence().toString());
		if (m0.matches()) {
			final StartDateBuilder builder = Builders.forParameterSD();
			if (m0.group(3).equals("ud")) {
				return builder
						.setRuleNumber(RuleNumber.UNDEFINED)
						.setDesignationMethod(DesignationMethod.UNDEFINED)
						.build();
			} else if (m0.group(3).equals("en")) {
				final int ruleNo = m0.group(2) == null ? 1 : Integer.parseInt(m0.group(2));
				return builder
						.setRuleNumber(RuleNumber.of(ruleNo))
						.setDesignationMethod(DesignationMethod.ENTRY_DATE)
						.build();
			}
			
			builder.setRuleNumber(RuleNumber.of(m0.group(2) == null ? 1 : Integer.parseInt(m0.group(2))));
			
			//  123                4          56       7      8        9 10       11  12                    13   14
			// "(((\\d\\d\\d\\d)/)?(\\d\\d)/)?((+|*|@)?(\\d+)|(+|*|@)?b(-(\\d+))?|(+)?(su|mo|tu|we|th|fr|sa)(\\s*:(\\d|b))?)"
			final Matcher m1 = SD_INNER.matcher(m0.group(3));
			
			if (!m1.matches()) {
				throw new IllegalArgumentException();
			}
			
			final String yyyy = m1.group(3);
			final String mm = m1.group(4);
			final String ddPrefix = m1.group(6);
			final String bddPrefix = m1.group(8);
			final String dd = m1.group(7);
			final String bdd = m1.group(10);
			final DayOfWeek dayOfWeek = m1.group(12) == null ? null : DayOfWeek.forCode(m1.group(12));
			final String dayNB = m1.group(14);
			
			if (yyyy != null) {
				builder.setYear(Integer.parseInt(yyyy));
			}
			if (mm != null) {
				builder.setMonth(Integer.parseInt(mm));
			}
			if (dd != null) {
				builder.setDay(Integer.parseInt(dd));
			} else if (bdd != null) {
				builder.setDay(Integer.parseInt(bdd));
			}
			final Integer dayN;
			if (dayNB == null || dayNB.equals("b")) {
				dayN = null;
			} else {
				dayN = Integer.parseInt(dayNB);
			}
			builder.setDesignationMethod(DesignationMethod.SCHEDULED_DATE);
			final CountingMethod countingMethod;
			if (dayOfWeek == null) {
				if (dd == null) {
					countingMethod = bddPrefix == null 
							? CountingMethod.ABSOLUTE
							: (bddPrefix.equals("+")
									? CountingMethod.RELATIVE
									: (bddPrefix.equals("*")
											? CountingMethod.BUSINESS_DAY
											: CountingMethod.NON_BUSINESS_DAY));
				} else {
					countingMethod = ddPrefix == null 
							? CountingMethod.ABSOLUTE
									: (ddPrefix.equals("+")
											? CountingMethod.RELATIVE
											: (ddPrefix.equals("*")
													? CountingMethod.BUSINESS_DAY
													: CountingMethod.NON_BUSINESS_DAY));
				}
				builder.setCountingMethod(countingMethod);
			} else {
				builder
				.setNumberOfWeek(dayNB != null && dayNB.equals("b") 
					? NumberOfWeek.LAST_WEEK 
					: NumberOfWeek.of(dayN))
				.setRelativeNumberOfWeek(m1.group(11) != null);
			}
			
			return builder.build();
		}
		throw new IllegalArgumentException();
	}
	
	/**
	 * ユニット定義パラメータ{@code "st"}で指定されたジョブ実行開始時刻のリストを返す.
	 * @param u ユニット定義
	 * @return ジョブ実行開始時刻のリスト
	 */
	public static List<StartTime> getStartTimes(final Unit u) {
		final List<StartTime> sts = new ArrayList<StartTime>();
		for (final Parameter p : u.getParameters("st")) {
			sts.add(getStartTime(p));
		}
		return sts;
	}
	
	/**
	 * ユニット定義パラメータ{@code "st"}で指定されたジョブ実行開始時刻を返す.
	 * @param p ユニット定義パラメータ
	 * @return ジョブ実行開始時刻
	 */
	public static StartTime getStartTime(final Parameter p) {
		final String value = p.getValue(0).getRawCharSequence().toString();
		final Matcher m = ST.matcher(value);
		if (!m.matches()) {
			return null;
		}
		
		//  12            3     4
		// "((\\d+),\\s*)?(\\+)?(\\d+:\\d+)"
		final int ruleNo = m.group(2) == null ? 1 : Integer.parseInt(m.group(2));
		final boolean relative = m.group(3) != null;
		final int hh = Integer.parseInt(m.group(5));
		final int mi = Integer.parseInt(m.group(6));
		
		return Builders.forParameterST()
				.setRuleNumber(ruleNo)
				.setRelative(relative)
				.setHours(hh)
				.setMinutes(mi)
				.build();
	}
	
	/**
	 * ユニット定義パラメータ{@code "sy"}で指定されたジョブ開始遅延時刻のリストを返す.
	 * @param u ユニット定義
	 * @return 開始遅延時刻のリスト
	 */
	public static List<StartScheduledTime> getStartDelayingTimes(final Unit u) {
		final List<StartScheduledTime> result = new ArrayList<StartScheduledTime>();
		u.query(UnitQueries.parameterNamed("sy", SY));
		for (final MatchResult mr : u.query(UnitQueries.parameterNamed("sy", SY))) {
			result.add(getStartDelayingTime(mr));
		}
		return result;
	}
	
	/**
	 * ユニット定義パラメータ{@code "sy"}で指定されたジョブ開始遅延時刻を返す.
	 * @param p ユニット定義パラメータ
	 * @return 開始遅延時刻
	 */
	private static StartScheduledTime getStartDelayingTime(final MatchResult m) {
		final StartScheduledTimeBuilder builder = Builders.forParameterSY();
		
		//  12            34      5      6      7
		// "((\\d+),\\s*)?((\\d+):(\\d+)|(M|U|C)(\\d+))"
		builder.setRuleNumber(m.group(2) == null ? 1 : Integer.parseInt(m.group(2)));
		
		final String relCode = m.group(6);
		final boolean relative = relCode != null;
		builder
		.setHour(relative ? -1 : Integer.parseInt(m.group(4)))
		.setMinute(Integer.parseInt(relative ? m.group(7) : m.group(5)));
		
		final EndScheduledTime.TimingMethod timingMethod;
		if (!relative) {
			timingMethod = EndScheduledTime.TimingMethod.ABSOLUTE;
		} else if (relCode.equals("M")) {
			timingMethod = EndScheduledTime.TimingMethod.RELATIVE_WITH_ROOT_START_TIME;
		} else if (relCode.equals("U")) {
			timingMethod = EndScheduledTime.TimingMethod.RELATIVE_WITH_SUPER_START_TIME;
		} else {
			timingMethod = EndScheduledTime.TimingMethod.RELATIVE_WITH_THEMSELF_START_TIME;
		}
		return builder.setTimingMethod(timingMethod).build();
	}
	
	/**
	 * ユニット定義パラメータ{@code "ey"}で指定されたジョブ終了遅延時刻のリストを返す.
	 * @param u ユニット定義
	 * @return 終了遅延時刻のリスト
	 */
	public static List<EndScheduledTime> getEndDelayingTimes(final Unit u) {
		final List<EndScheduledTime> result = new ArrayList<EndScheduledTime>();
		for (final MatchResult mr : u.query(UnitQueries.parameterNamed("ey", SY))) {
			result.add(getEndDelayingTime(mr));
		}
		return result;
	}
	
	/**
	 * ユニット定義パラメータ{@code "ey"}で指定されたジョブ終了遅延時刻を返す.
	 * @param p ユニット定義パラメータ
	 * @return 終了遅延時刻
	 */
	public static EndScheduledTime getEndDelayingTime(final MatchResult m) {
		final EndScheduledTimeBuilder builder = Builders.forParameterEY();
		
		//  12            34      5      6      7
		// "((\\d+),\\s*)?((\\d+):(\\d+)|(M|U|C)(\\d+))"
		builder.setRuleNumber(m.group(2) == null ? 1 : Integer.parseInt(m.group(2)));
		
		final String relCode = m.group(6);
		final boolean relative = relCode != null;
		builder
		.setHour(relative ? -1 : Integer.parseInt(m.group(4)))
		.setMinute(Integer.parseInt(relative ? m.group(7) : m.group(5)));
		
		final EndScheduledTime.TimingMethod timingMethod;
		if (!relative) {
			timingMethod = EndScheduledTime.TimingMethod.ABSOLUTE;
		} else if (relCode.equals("M")) {
			timingMethod = EndScheduledTime.TimingMethod.RELATIVE_WITH_ROOT_START_TIME;
		} else if (relCode.equals("U")) {
			timingMethod = EndScheduledTime.TimingMethod.RELATIVE_WITH_SUPER_START_TIME;
		} else {
			timingMethod = EndScheduledTime.TimingMethod.RELATIVE_WITH_THEMSELF_START_TIME;
		}
		return builder.setTimingMethod(timingMethod).build();
	}

	/**
	 * ユニット定義パラメータ{@code "cy"}で指定されたジョブネットの処理サイクルのリストを返す.
	 * @param u ユニット定義
	 * @return ジョブネットの処理サイクルのリスト
	 */
	public static List<ExecutionCycle> getExecutionCycles(final Unit u) {
		final List<ExecutionCycle> result = new ArrayList<ExecutionCycle>();
		for (final Parameter p : u.getParameters("cy")) {
			result.add(getExecutionCycle(p));
		}
		return result;
	}
	
	/**
	 * ユニット定義パラメータ{@code "cy"}で指定されたジョブネットの処理サイクルを返す.
	 * @param p ユニット定義パラメータ
	 * @return ジョブネットの処理サイクル
	 */
	public static ExecutionCycle getExecutionCycle(final Parameter p) {
		final Matcher m = CY.matcher(p.getValue(0).getRawCharSequence().toString());
		if (!m.matches()) {
			return null;
		}
		
		final CycleUnit u = CycleUnit.forCode(m.group(4));
		final int d = Integer.parseInt(m.group(3));
		final int n = m.group(2) == null ? 1 : Integer.parseInt(m.group(2));
		
		return new ExecutionCycle(n, d, u);
	}
	
	/**
	 * ユニット定義パラメータ{@code "ln"}で対応する上位ジョブネットのスケジュールのルール番号のリストを返す.
	 * @param u ユニット定義
	 * @return 対応する上位ジョブネットのスケジュールのルール番号のリスト
	 */
	public static List<LinkedRule> getLinkedRules(final Unit u) {
		final List<LinkedRule> result = new ArrayList<LinkedRule>();
		for (final Parameter p : u.getParameters("ln")) {
			result.add(getLinkedRule(p));
		}
		return result;
	}
	
	/**
	 * 対応する上位ジョブネットのスケジュールのルール番号を取得する.
	 * @param p ユニット定義パラメータ
	 * @return 対応する上位ジョブネットのスケジュールのルール番号
	 */
	public static LinkedRule getLinkedRule(final Parameter p) {
		final int n = p.getValueCount() == 1 ? 1 : Integer.parseInt(p.getValue(0).toString());
		final int d = Integer.parseInt((p.getValueCount() == 1 ? p.getValue(0) : p.getValue(1)).toString());
		if (d < 0) {
			return null;
		}
		return new LinkedRule(n, d);
	}
	
	/**
	 * ユニット定義パラメータ{@code "ncl"}の値を返す.
	 * @param unit ユニット定義
	 * @return 定義情報値
	 */
	public static Optional<Boolean> getJobnetConnectorControlling(final Unit unit) {
		return Optional.ofFirst(getBoolValues(unit, "ncl"));
	}
	/**
	 * ユニット定義パラメータ{@code "ncn"}の値を返す.
	 * 定義情報の詳細はJP1/AJS2の公式リファレンスを参照してください。
	 * @param unit ユニット定義
	 * @return 定義情報値
	 */
	public static Optional<String> getJobnetConnectorName(final Unit unit) {
		return Optional.ofFirst(getStringValues(unit, "ncn"));
	}
	/**
	 * ユニット定義パラメータ{@code "ncs"}の値を返す.
	 * 定義情報の詳細はJP1/AJS2の公式リファレンスを参照してください。
	 * @param unit ユニット定義
	 * @return 定義情報値
	 */
	public static Optional<ConnectorControllingSyncOption> getJobnetConnectorControllingSyncOption(final Unit unit) {
		if (getJobnetConnectorControlling(unit).orElse(false)) {
			if (getBoolValues(unit, "ncs").isEmpty()) {
				return Optional.of(ConnectorControllingSyncOption.ASYNC);
			} else {
				return Optional.ofNullable(getBoolValues(unit, "ncs").get(0)
						? ConnectorControllingSyncOption.SYNC 
						: ConnectorControllingSyncOption.ASYNC);
			}
		} else {
			return Optional.empty();
		}
	}
	
	// For Connectables
	/**
	 * ユニット定義パラメータ{@code "ncex"}の値を返す.
	 * 定義情報の詳細はJP1/AJS2の公式リファレンスを参照してください。
	 * @param unit ユニット定義
	 * @return 定義情報値
	 */
	public static Optional<Boolean> getJobnetConnectorOrderingExchangeOption(final Unit unit) {
		return Optional.ofFirst(getBoolValues(unit, "ncex"));
	}
	/**
	 * ユニット定義パラメータ{@code "nchn"}の値を返す.
	 * 定義情報の詳細はJP1/AJS2の公式リファレンスを参照してください。
	 * @param unit ユニット定義
	 * @return 定義情報値
	 */
	public static Optional<String> getJobnetConnectorHostName(final Unit unit) {
		return Optional.ofFirst(getStringValues(unit, "nchn"));
	}
	/**
	 * ユニット定義パラメータ{@code "ncsv"}の値を返す.
	 * 定義情報の詳細はJP1/AJS2の公式リファレンスを参照してください。
	 * @param unit ユニット定義
	 * @return 定義情報値
	 */
	public static Optional<String> getJobnetConnectorServiceName(final Unit unit) {
		return Optional.ofFirst(getStringValues(unit, "ncsv"));
	}

	// For Executables
	/**
	 * ユニット定義パラメータ{@code "ha"}で指定された保留属性設定タイプを返す.
	 * @param unit ユニット定義
	 * @return 保留属性設定タイプ
	 */
	public static Optional<HoldAttrType> getHoldAttrType(final Unit unit) {
		final List<String> p = getStringValues(unit, "ha");
		if (!p.isEmpty()) {
			return Optional.ofNullable(HoldAttrType.forCode(p.get(0)));
		} else {
			return Optional.empty();
		}
	}
	/**
	 * ユニット定義パラメータ{@code "fd"}で指定された実行所要時間の値を返す.
	 * 設定可能な値は1～1440。単位は分です。未設定の場合−1を返します。
	 * @param unit ユニット定義
	 * @return 実行所要時間
	 */
	public static Optional<Integer> getFixedDuration(final Unit unit) {
		return Optional.ofFirst(getIntValues(unit, "fd"));
	}
	/**
	 * ユニット定義パラメータ{@code "ex"}で指定された実行ホスト名を返す.
	 * @param unit ユニット定義
	 * @return 実行ホスト名
	 */
	public static Optional<String> getExecutionHostName(final Unit unit) {
		return Optional.ofFirst(getStringValues(unit, "ex"));
	}
	/**
	 * ユニット定義パラメータ{@code "eu"}で指定された
	 * ジョブ実行時のJP1ユーザの定義を返す.
	 * @param unit ユニット定義
	 * @return ジョブ実行時のJP1ユーザ
	 */
	public static Optional<ExecutionUserType> getExecutionUserType(final Unit unit) {
		final List<String> v = getStringValues(unit, "eu");
		if (v.size() > 0) {
			return Optional.ofNullable(v.get(0).equals("def") 
					? ExecutionUserType.DEFINITION_USER 
					: ExecutionUserType.ENTRY_USER);
		} else {
			return Optional.empty();
		}
	}
	/**
	 * ユニット定義パラメータ{@code "etm"}で指定された
	 * 実行開始時刻からの相対分数で指定された実行打ち切り時間を返す.
	 * 設定可能な値は1～1440。単位は分です。未設定の場合−1を返します。
	 * @param unit ユニット定義
	 * @return 実行打ち切り時間
	 */
	public static Optional<Integer> getExecutionTimeOut(final Unit unit) {
		return Optional.ofFirst(getIntValues(unit, "etm"));
	}
		
	// For Judgments
	/**
	 * ユニット定義パラメータ{@code "ej"}で指定された判定条件タイプを返す.
	 * @param unit ユニット定義
	 * @return 判定条件タイプ
	 */
	public static Optional<EvaluateConditionType> getEvaluateConditionType(final Unit unit) {
		final List<String> v = getStringValues(unit, "ej");
		if (v.size() > 0) {
			return Optional.ofNullable(EvaluateConditionType.forCode(v.get(0)));
		} else {
			return Optional.empty();
		}
	}
	
	/**
	 * ユニット定義パラメータ{@code "ej"}で指定された判定条件タイプを返す.
	 * @param p ユニット定義パラメータ
	 * @return 判定条件タイプ
	 */
	public static EvaluateConditionType getEvaluateConditionType(final Parameter p) {
		return EvaluateConditionType.forCode(p.getValue(0).getRawCharSequence().toString());
	}
	
	/**
	 * ユニット定義パラメータ{@code "ejc"}で指定された判定終了コードを返す.
	 * 指定可能な値は、0～4294967295です。指定されていない場合は0を返します。
	 * @param unit ユニット定義
	 * @return 判定終了コード
	 */
	public static Optional<Integer> getEvaluableExitCode(final Unit unit) {
		return Optional.ofFirst(getIntValues(unit, "ejc"));
	}
	/**
	 * ユニット定義パラメータ{@code "ejf"}で指定された
	 * 終了判定ファイル名を返す.
	 * @param unit ユニット定義
	 * @return 終了判定ファイル名
	 */
	public static Optional<String> getEvaluableFileName(final Unit unit) {
		return Optional.ofFirst(getStringValues(unit, "ejf"));
	}
	
	/**
	 * ユニット定義パラメータ{@code "ejv"}で指定された
	 * 判定対象変数名を返す.
	 * @param unit ユニット定義
	 * @return 判定対象変数名
	 */
	public static Optional<String> getEvaluableVariableName(final Unit unit) {
		return Optional.ofFirst(getStringValues(unit, "ejv"));
	}
	
	/**
	 * ユニット定義パラメータ{@code "ejt"}で指定された
	 * 判定対象変数（文字列）の判定値を返す.
	 * @param unit ユニット定義
	 * @return 判定対象変数（文字列）の判定値
	 */
	public static Optional<String> getEvaluableVariableStringValue(final Unit unit) {
		return Optional.ofFirst(getStringValues(unit, "ejt"));
	}
	
	/**
	 * ユニット定義パラメータ{@code "eji"}で指定された
	 * 判定対象変数（数値）の判定値を返す.
	 * @param unit ユニット定義
	 * @return 判定対象変数（数値）の判定値
	 */
	public static Optional<Integer> getEvaluableVariableIntegerValue(final Unit unit) {
		return Optional.ofFirst(getIntValues(unit, "eji"));
	}
	
	// For Mail Agents
	/**
	 * ユニット定義パラメータ{@code "mlprf"}で指定された
	 * メールプロファイル名を返す.
	 * @param unit ユニット定義
	 * @return メールプロファイル名
	 */
	public static Optional<String> getMailProfileName(final Unit unit) {
		return Optional.ofFirst(getStringValues(unit, "mlprf"));
	}
	/**
	 * ユニット定義パラメータ{@code "mladr"}で指定された
	 * 送信先メールアドレスのリストを返す.
	 * 設定されていない場合は空のリストを返します。
	 * @param unit ユニット定義
	 * @return 送信先メールアドレスのリスト
	 */
	public static List<MailAddress> getMailAddresses(final Unit unit) {
		final ArrayList<MailAddress> l = new ArrayList<MailAddress>();
		final List<Parameter> ps = unit.getParameters("mladr");
		final Pattern pat = Pattern.compile("^(TO|CC|BCC):\"(.+\"$)");
		
		for (final Parameter p : ps) {
			final Matcher mat = pat.matcher(p.getValue(0).getRawCharSequence().toString());
			if (mat.matches()) {
				final String t = mat.group(1);
				final String a = mat.group(2).replaceAll("#\"", "\"").replaceAll("##", "#");
				l.add(new MailAddress(){
					@Override
					public MailAddressType getType() {
						return t.equals("TO") ? MailAddressType.TO : (t.equals("CC") ? MailAddressType.CC : MailAddressType.BCC);
					}
					@Override
					public String getAddress() {
						return a;
					}
				});
			}
		}
		return l;
	}
	/**
	 * ユニット定義パラメータ{@code "mlsbj"}で指定された
	 * メール件名を返す.
	 * @param unit ユニット定義
	 * @return メール件名
	 */
	public static Optional<String> getMailSubject(final Unit unit) {
		return Optional.ofFirst(getStringValues(unit, "mlsbj"));
	}
	/**
	 * ユニット定義パラメータ{@code "mltxt"}で指定された
	 * メール本文を返す.
	 * @param unit ユニット定義
	 * @return メール本文
	 */
	public static Optional<String> getMailBody(final Unit unit) {
		return Optional.ofFirst(getStringValues(unit, "mltxt"));
	}
	/**
	 * ユニット定義パラメータ{@code "mlafl"}で指定された
	 * メール添付ファイルリスト名を返す.
	 * @param unit ユニット定義
	 * @return メール添付ファイルリスト名
	 */
	public static Optional<String> getAttachmentFileListPath(final Unit unit) {
		return Optional.ofFirst(getStringValues(unit, "mlafl"));
	}
	
	// For Mail Sends
	/**
	 * ユニット定義パラメータ{@code "mlftx"}で指定された
	 * メール本文ファイル名を返す.
	 * @param unit ユニット定義
	 * @return メール本文ファイル名
	 */
	public static Optional<String> getMailBodyFilePath(final Unit unit) {
		return Optional.ofFirst(getStringValues(unit, "mlftx"));
	}
	/**
	 * ユニット定義パラメータ{@code "mlatf"}で指定された
	 * メール添付ファイル名を返す.
	 * @param unit ユニット定義
	 * @return メール添付ファイル名
	 */
	public static Optional<String> getMailAttachmentFilePath(final Unit unit) {
		return Optional.ofFirst(getStringValues(unit, "mlatf"));
	}

	// For Unix/Pc Job
	/**
	 * ユニット定義パラメータ{@code "wth"}で指定された
	 * 対象ユニットの警告終了閾値を返す.
	 * @return 警告終了閾値（0〜2,147,483,647）
	 */
	public static Optional<Integer> getWarningThreshold(final Unit unit) {
		return Optional.ofFirst(getIntValues(unit, "wth"));
	}
	/**
	 * ユニット定義パラメータ{@code tho"}で指定された
	 * 対象ユニットの異常終了閾値を返す.
	 * @return 異常終了閾値（0〜2,147,483,647）
	 */
	public static Optional<Integer> getErrorThreshold(final Unit unit) {
		return Optional.ofFirst(getIntValues(unit, "tho"));
	}
	/**
	 * ユニット定義パラメータ{@code "jd"}で指定された
	 * 終了判定種別を返す.
	 * @param unit ユニット定義
	 * @return 終了判定種別
	 */
	public static Optional<ResultJudgmentType> getResultJudgmentType(final Unit unit) {
		final List<String> v = getStringValues(unit, "jd");
		if (v.size() > 0) {
			return Optional.ofNullable(ResultJudgmentType.forCode(v.get(0)));
		} else {
			return Optional.empty();
		}
	}
	/**
	 * ユニット定義パラメータ{@code "un"}で指定された実行ユーザ名を返す.
	 * @param unit ユニット定義
	 * @return 実行ユーザ名
	 */
	public static Optional<String> getExecutionUserName(final Unit unit) {
		return Optional.ofFirst(getStringValues(unit, "un"));
	}
	/**
	 * ユニット定義パラメータ{@code "sc"}で指定された
	 * スクリプトファイル名（UNIXジョブ）もしくは実行ファイル名（PCジョブ）を返す.
	 * @param unit ユニット定義
	 * @return スクリプトファイル名（UNIXジョブ）もしくは実行ファイル名（PCジョブ）
	 */
	public static Optional<String> getScriptFilePath(final Unit unit) {
		return Optional.ofFirst(getStringValues(unit, "sc"));
	}
	/**
	 * ユニット定義パラメータ{@code "prm"}で指定された
	 * 実行ファイルに対するパラメータの設定値を返す.
	 * @param unit ユニット定義
	 * @return 実行ファイルに対するパラメータ
	 */
	public static Optional<String> getParameter(final Unit unit) {
		return Optional.ofFirst(getStringValues(unit, "prm"));
	}
	/**
	 * ユニット定義パラメータ{@code "ts1"}で指定された
	 * 転送元ファイル名1（絶対パス）を返す.
	 * @param unit ユニット定義
	 * @return 転送元ファイル名1
	 */
	public static Optional<String> getTransportSourceFilePath1(final Unit unit) {
		return Optional.ofFirst(getStringValues(unit, "ts1"));
	}
	/**
	 * ユニット定義パラメータ{@code "td1"}で指定された
	 * 転送先ファイル名1を返す.
	 * @param unit ユニット定義
	 * @return 転送先ファイル名1
	 */
	public static Optional<String> getTransportDestinationFilePath1(final Unit unit) {
		return Optional.ofFirst(getStringValues(unit, "td1"));
	}
	/**
	 * ユニット定義パラメータ{@code "ts2"}で指定された
	 * 転送元ファイル名2（絶対パス）を返す.
	 * @param unit ユニット定義
	 * @return 転送元ファイル名2
	 */
	public static Optional<String> getTransportSourceFilePath2(final Unit unit) {
		return Optional.ofFirst(getStringValues(unit, "ts2"));
	}
	/**
	 * ユニット定義パラメータ{@code "td2"}で指定された
	 * 転送先ファイル名2を返す.
	 * @param unit ユニット定義
	 * @return 転送先ファイル名2
	 */
	public static Optional<String> getTransportDestinationFilePath2(final Unit unit) {
		return Optional.ofFirst(getStringValues(unit, "td2"));
	}
	/**
	 * ユニット定義パラメータ{@code "ts3"}で指定された
	 * 転送元ファイル名3（絶対パス）を返す.
	 * @param unit ユニット定義
	 * @return 転送元ファイル名3
	 */
	public static Optional<String> getTransportSourceFilePath3(final Unit unit) {
		return Optional.ofFirst(getStringValues(unit, "ts3"));
	}
	/**
	 * ユニット定義パラメータ{@code "td3"}で指定された
	 * 転送先ファイル名3を返す.
	 * @param unit ユニット定義
	 * @return 転送先ファイル名3
	 */
	public static Optional<String> getTransportDestinationFilePath3(final Unit unit) {
		return Optional.ofFirst(getStringValues(unit, "td3"));
	}
	/**
	 * ユニット定義パラメータ{@code "ts4"}で指定された
	 * 転送元ファイル名4（ファイルパス）を返す.
	 * @param unit ユニット定義
	 * @return 転送元ファイル名4
	 */
	public static Optional<String> getTransportSourceFilePath4(final Unit unit) {
		return Optional.ofFirst(getStringValues(unit, "ts4"));
	}
	/**
	 * ユニット定義パラメータ{@code "td4"}で指定された
	 * 転送先ファイル名4を返す.
	 * @param unit ユニット定義
	 * @return 転送先ファイル名4
	 */
	public static Optional<String> getTransportDestinationFilePath4(final Unit unit) {
		return Optional.ofFirst(getStringValues(unit, "td4"));
	}
	/**
	 * ユニット定義パラメータ{@code "te"}で指定された
	 * コマンドテキストを返す.
	 * @param unit ユニット定義
	 * @return コマンドテキスト
	 */
	public static Optional<String> getCommandText(final Unit unit) {
		return Optional.ofFirst(getStringValues(unit, "te"));
	}
	/**
	 * ユニット定義パラメータ{@code "wkp"}で指定された
	 * 作業用パス名（絶対パス）を返す.
	 * @param unit ユニット定義
	 * @return 作業用パス名
	 */
	public static Optional<String> getWorkPath(final Unit unit) {
		return Optional.ofFirst(getStringValues(unit, "wkp"));
	}
	/**
	 * ユニット定義パラメータ{@code "ev"}で指定された
	 * エージェントホスト上の環境変数ファイル名（絶対パスもしくは相対パス）を返す.
	 * @param unit ユニット定義
	 * @return 環境変数ファイル名
	 */
	public static Optional<String> getEnvironmentVariableFilePath(final Unit unit) {
		return Optional.ofFirst(getStringValues(unit, "ev"));
	}
	/**
	 * ユニット定義パラメータ{@code "env"}で指定された
	 * 環境変数定義リストを返す.
	 * @param unit ユニット定義
	 * @return 環境変数定義リスト
	 */
	public static List<EnvironmentVariable> getEnvironmentVariable(final Unit unit) {
		final List<EnvironmentVariable> l = new ArrayList<EnvironmentVariable>();
		final List<Parameter> p = unit.getParameters("env");
		if (!p.isEmpty()) {
			l.addAll(envParamParser.parse(p.get(0).getValue(0).getRawCharSequence().toString()).get());
		}
		return l;
	}
	/**
	 * ユニット定義パラメータ{@code "si"}で指定された
	 * ジョブ実行ホストの標準入力ファイル名（絶対パスもしくは相対パス）を返す.
	 * @param unit ユニット定義
	 * @return 標準入力ファイル名
	 */
	public static Optional<String> getStandardInputFilePath(final Unit unit) {
		return Optional.ofFirst(getStringValues(unit, "si"));
	}
	/**
	 * ユニット定義パラメータ{@code "so"}で指定された
	 * 標準出力ファイル名（絶対パス）を返す.
	 * @param unit ユニット定義
	 * @return 標準出力ファイル名
	 */
	public static Optional<String> getStandardOutputFilePath(final Unit unit) {
		return Optional.ofFirst(getStringValues(unit, "so"));
	}
	/**
	 * ユニット定義パラメータ{@code "se"}で指定された
	 * 標準エラー出力ファイル名（絶対パス）を返す.
	 * @param unit ユニット定義
	 * @return 標準エラー出力ファイル名
	 */
	public static Optional<String> getStandardErrorFilePath(final Unit unit) {
		return Optional.ofFirst(getStringValues(unit, "se"));
	}
	/**
	 * ユニット定義パラメータ{@code "soa"}で指定された
	 * 標準出力ファイルの追加書きオプションを返す.
	 * @param unit ユニット定義
	 * @return 追加書きオプション
	 */
	public static Optional<WriteOption> getStandardOutputWriteOption(final Unit unit) {
		final List<String> v = getStringValues(unit, "soa");
		if (v.size() > 0) {
			return Optional.ofNullable(WriteOption.valueOf(v.get(0).toUpperCase()));
		} else {
			return Optional.empty();
		}
	}
	/**
	 * ユニット定義パラメータ{@code "sea"}で指定された
	 * 標準エラー出力ファイルの追加書きオプションを返す.
	 * @param unit ユニット定義
	 * @return 追加書きオプション
	 */
	public static Optional<WriteOption> getStandardErrorWriteOption(final Unit unit) {
		final List<String> v = getStringValues(unit, "sea");
		if (v.size() > 0) {
			return Optional.ofNullable(WriteOption.valueOf(v.get(0).toUpperCase()));
		} else {
			return Optional.empty();
		}
	}
	/**
	 * ユニット定義パラメータ{@code "jdf"}で指定された
	 * 終了判定ファイル名（絶対パス）を返す.
	 * @param unit ユニット定義
	 * @return 終了判定ファイル名
	 */
	public static Optional<String> getResultJudgementFilePath(final Unit unit) {
		return Optional.ofFirst(getStringValues(unit, "jdf"));
	}
	/**
	 * ユニット定義パラメータ{@code "top1"}で指定された
	 * 転送先ファイル1の自動削除オプションを返す.
	 * @param unit ユニット定義
	 * @return 転送先ファイル1の自動削除オプション
	 */
	public static Optional<DeleteOption> getTransportDestinationFileDeleteOption1(final Unit unit) {
		final Parameter top1 = unit.getParameter("top1");
		if (top1 != null) {
			final String s = top1.getValue(0).getRawCharSequence().toString();
			if (s.equals("sav")) {
				return Optional.of(DeleteOption.SAVE);
			} else if (s.equals("del")) {
				return Optional.of(DeleteOption.DELETE);
			}
		}
		final Optional<String> ts = getTransportSourceFilePath1(unit);
		final Optional<String> td = getTransportDestinationFilePath1(unit);
		if (ts.isPresent() && td.isPresent()) {
			return Optional.of(DeleteOption.SAVE);
		} else if (ts.isPresent() && td.isNotPresent()) {
			return Optional.of(DeleteOption.DELETE);
		} else {
			return Optional.empty();
		}
	}
	/**
	 * ユニット定義パラメータ{@code "top2"}で指定された
	 * 転送先ファイル2の自動削除オプションを返す.
	 * @param unit ユニット定義
	 * @return 転送先ファイル2の自動削除オプション
	 */
	public static Optional<DeleteOption> getTransportDestinationFileDeleteOption2(final Unit unit) {
		final Parameter top2 = unit.getParameter("top2");
		if (top2 != null) {
			final String s = top2.getValue(0).getRawCharSequence().toString();
			if (s.equals("sav")) {
				return Optional.of(DeleteOption.SAVE);
			} else if (s.equals("del")) {
				return Optional.of(DeleteOption.DELETE);
			}
		}
		final Optional<String> ts = getTransportSourceFilePath2(unit);
		final Optional<String> td = getTransportDestinationFilePath2(unit);
		if (ts.isPresent() && td.isPresent()) {
			return Optional.of(DeleteOption.SAVE);
		} else if (ts.isPresent() && td.isNotPresent()) {
			return Optional.of(DeleteOption.DELETE);
		} else {
			return Optional.empty();
		}
	}
	/**
	 * ユニット定義パラメータ{@code "top3"}で指定された
	 * 転送先ファイル3の自動削除オプションを返す.
	 * @param unit ユニット定義
	 * @return 転送先ファイル3の自動削除オプション
	 */
	public static Optional<DeleteOption> getTransportDestinationFileDeleteOption3(final Unit unit) {
		final Parameter top3 = unit.getParameter("top3");
		if (top3 != null) {
			final String s = top3.getValue(0).getRawCharSequence().toString();
			if (s.equals("sav")) {
				return Optional.of(DeleteOption.SAVE);
			} else if (s.equals("del")) {
				return Optional.of(DeleteOption.DELETE);
			}
		}
		final Optional<String> ts = getTransportSourceFilePath3(unit);
		final Optional<String> td = getTransportDestinationFilePath3(unit);
		if (ts.isPresent() && td.isPresent()) {
			return Optional.of(DeleteOption.SAVE);
		} else if (ts.isPresent() && td.isNotPresent()) {
			return Optional.of(DeleteOption.DELETE);
		} else {
			return Optional.empty();
		}
	}
	/**
	 * ユニット定義パラメータ{@code "top4"}で指定された
	 * 転送先ファイル4の自動削除オプションを返す.
	 * @param unit ユニット定義
	 * @return 転送先ファイル4の自動削除オプション
	 */
	public static Optional<DeleteOption> getTransportDestinationFileDeleteOption4(final Unit unit) {
		final Parameter top4 = unit.getParameter("top4");
		if (top4 != null) {
			final String s = top4.getValue(0).getRawCharSequence().toString();
			if (s.equals("sav")) {
				return Optional.of(DeleteOption.SAVE);
			} else if (s.equals("del")) {
				return Optional.of(DeleteOption.DELETE);
			}
		}
		final Optional<String> ts = getTransportSourceFilePath4(unit);
		final Optional<String> td = getTransportDestinationFilePath4(unit);
		if (ts.isPresent() && td.isPresent()) {
			return Optional.of(DeleteOption.SAVE);
		} else if (ts.isPresent() && td.isNotPresent()) {
			return Optional.of(DeleteOption.DELETE);
		} else {
			return Optional.empty();
		}
	}
	/**
	 * ユニット定義パラメータ{@code "tmitv"}で指定された
	 * 実行間隔制御の待ち時間を返す.
	 * 指定できる値は1～1440。単位は分です。
	 * @param unit ユニット定義
	 * @return 待ち時間
	 */
	public static Optional<Integer> getTimeInterval(final Unit unit) {
		return Optional.ofFirst(getIntValues(unit, "tmitv"));
	}
	
	/**
	 * ユニット定義パラメータ{@code "ets"}で指定された
	 * 実行打ち切り時間が経過したあとのジョブの状態を返す.
	 * @param u ユニット定義
	 * @return 実行打ち切り時間が経過したあとのジョブの状態
	 */
	public static Optional<ExecutionTimedOutStatus> getExecutionTimedOutStatus(final Unit u) {
		final List<Parameter> p = u.getParameters("ets");
		if (p.isEmpty()) {
			return Optional.empty();
		} else {
			return Optional.ofNullable(getExecutionTimedOutStatus(p.get(0)));
		}
	}
	
	/**
	 * ユニット定義パラメータ{@code "ets"}で指定された
	 * 実行打ち切り時間が経過したあとのジョブの状態を返す.
	 * @param p ユニット定義パラメータ
	 * @return 実行打ち切り時間が経過したあとのジョブの状態
	 */
	public static ExecutionTimedOutStatus getExecutionTimedOutStatus(final Parameter p) {
		return ExecutionTimedOutStatus.forCode(p.getValue(0).getRawCharSequence().toString());
	}
}
