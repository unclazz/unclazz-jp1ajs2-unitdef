package com.m12i.jp1ajs2.unitdef;

import static com.m12i.jp1ajs2.unitdef.util.Maybe.*;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.m12i.jp1ajs2.unitdef.ExecutionCycle.CycleUnit;
import com.m12i.jp1ajs2.unitdef.StartDate.CountingMethod;
import com.m12i.jp1ajs2.unitdef.StartDate.DesignationMethod;
import com.m12i.jp1ajs2.unitdef.StartDate.TimingMethod;
import com.m12i.jp1ajs2.unitdef.parser.EnvParamParser;
import com.m12i.jp1ajs2.unitdef.parser.ParseException;
import com.m12i.jp1ajs2.unitdef.util.Maybe;

/**
 * ユニット定義パラメータへのアクセスを提供するユーティリティ.<br>
 * <p>パラメータを検索し値を抽出するための一般的メソッドに加えて、
 * 特定パラメータに特化したアクセサ・メソッドも提供する。</p>
 * <p>メソッド名はいずれも{@code "fd"}・{@code "eu"}・{@code "tmitv"}といった
 * ユニット定義ファイルにおける縮約名から推論されたパラメータ名。</p>
 */
public final class Params {
	private static final EnvParamParser envParamParser = new EnvParamParser();
	
	private Params() {}
	
	/**
	 * {@code "el"}パラメータの値を解析するための正規表現パターン.
	 */
	private static final Pattern PARAM_EL_VALUE_3 = Pattern.compile("^\\+(\\d+)\\s*\\+(\\d+)$");
	/**
	 * {@code "size"}パラメータの値を解析するための正規表現パターン.
	 */
	private static final Pattern PARAM_SZ_VALUE_1 = Pattern.compile("^(\\d+)[^\\d]+(\\d+)$");

	/**
	 * 引数で指定された名称のユニット定義パラメータを検索して値を抽出する.
	 * @param unit ユニット定義
	 * @param paramName ユニット定義パラメータ名
	 * @return ユニット定義パラメータ値
	 */
	public static Maybe<String> getStringValues(final Unit unit, final String paramName) {
		final List<String> list = new ArrayList<String>();
		for (final Param p : Units.getParams(unit, paramName)) {
			list.add(p.getValue(0).getStringValue());
		}
		return Maybe.wrap(list);
	}

	/**
	 * 引数で指定された名称のユニット定義パラメータを検索して値を抽出する.
	 * 値は整数としてパースされた上で返される。
	 * @param unit ユニット定義
	 * @param paramName ユニット定義パラメータ名
	 * @return ユニット定義パラメータ値
	 */
	public static Maybe<Integer> getIntValues(final Unit unit, final String paramName) {
		final List<Integer> list = new ArrayList<Integer>();
		for (final Param p : Units.getParams(unit, paramName)) {
			try {
				list.add(Integer.parseInt(p.getValue(0).getStringValue()));
			} catch (final NumberFormatException e) {
				// Do nothing.
			}
		}
		return Maybe.wrap(list);
	}

	/**
	 * 引数で指定された名称のユニット定義パラメータを検索して値を抽出する.
	 * 値は長整数としてパースされた上で返される。
	 * @param unit ユニット定義
	 * @param paramName ユニット定義パラメータ名
	 * @return ユニット定義パラメータ値
	 */
	public static Maybe<Long> getLongValues(final Unit unit, final String paramName) {
		final List<Long> list = new ArrayList<Long>();
		for (final Param p : Units.getParams(unit, paramName)) {
			try {
				list.add(Long.parseLong(p.getValue(0).getStringValue()));
			} catch (final NumberFormatException e) {
				// Do nothing.
			}
		}
		return Maybe.wrap(list);
	}

	/**
	 * 引数で指定された名称のユニット定義パラメータを検索して値を抽出する.
	 * 値は倍精度浮動小数点数としてパースされた上で返される。
	 * @param unit ユニット定義
	 * @param paramName ユニット定義パラメータ名
	 * @return ユニット定義パラメータ値
	 */
	public static Maybe<Double> getDoubleValues(final Unit unit, final String paramName) {
		final List<Double> list = new ArrayList<Double>();
		for (final Param p : Units.getParams(unit, paramName)) {
			try {
				list.add(Double.parseDouble(p.getValue(0).getStringValue()));
			} catch (final NumberFormatException e) {
				// Do nothing.
			}
		}
		return Maybe.wrap(list);
	}

	/**
	 * 引数で指定された名称のユニット定義パラメータを検索して値を抽出する.
	 * 値はタプルもどきとしてパースされた上で返される。
	 * @param unit ユニット定義
	 * @param paramName ユニット定義パラメータ名
	 * @return ユニット定義パラメータ値
	 */
	public static Maybe<Tuple> getTupleValues(final Unit unit, final String paramName) {
		final List<Tuple> list = new ArrayList<Tuple>();
		for (final Param p : Units.getParams(unit, paramName)) {
			final ParamValue v = p.getValue(0);
			if (v.getFormat() == ParamValueFormat.TUPLE) {
				list.add(v.getTupleValue());
			}
		}
		return Maybe.wrap(list);
	}

	/**
	 * 引数で指定された名称のユニット定義パラメータを検索して値を抽出する.
	 * 値は真偽値としてパースされた上で返される。
	 * @param unit ユニット定義
	 * @param paramName ユニット定義パラメータ名
	 * @return ユニット定義パラメータ値
	 */
	public static Maybe<Boolean> getBoolValues(final Unit unit, final String paramName) {
		final List<Boolean> list = new ArrayList<Boolean>();
		for (final Param p : Units.getParams(unit, paramName)) {
			final String v = p.getValue(0).getStringValue().toLowerCase();
			if (v.equals("y") || v.equals("yes") || v.equals("on") || v.equals("t") || v.equals("true") || v.equals("1")) {
				list.add(true);
			} else if(v.equals("n") || v.equals("no") || v.equals("off") || v.equals("f") || v.equals("false") || v.equals("0")) {
				list.add(false);
			}
		}
		return Maybe.wrap(list);
	}
	
	/**
	 * ユニット定義パラメータ{@code "el"}で指定された下位ユニットの位置情報のリストを返す.
	 * サブユニットが存在しない場合は空のリストを返す。
	 * JP1ユニット定義では、サブユニットの位置情報や関連線の情報はサブユニット自身では保持しておらず、
	 * 親ユニット側のユニット定義パラメータとして保持されている点に注意。
	 * @param unit ユニット定義
	 * @return 下位ユニットの位置情報のリスト
	 */
	public static List<Element> getElements(final Unit unit) {
		final List<Element> result = new ArrayList<Element>();
		for (final Param el : Units.getParams(unit, "el")) {
			result.add(getElement(el));
		}
		return result;
	}
	/**
	 * ユニット定義パラメータ{@code "el"}で指定された下位ユニットの位置情報を返す.
	 * @param p ユニット定義パラメータ
	 * @return 下位ユニットの位置情報のリスト
	 */
	public static Element getElement(final Param p) {
		final String pos = p.getValue(2).getStringValue();
		final String unitName = p.getValue(0).getStringValue();
		final Matcher m = PARAM_EL_VALUE_3.matcher(pos);
		if (!m.matches()) {
			return null;
		}
		final Unit subunit = p.getUnit().getSubUnits(unitName).get(0);
		final int horizontalPixel = Integer.parseInt(m.group(1));
		final int verticalPixel = Integer.parseInt(m.group(2));
		return new Element(subunit, horizontalPixel, verticalPixel);
	}
	/**
	 * ユニット定義パラメータ{@code "sz"}で指定されたマップサイズを返す.
	 * ジョブネットにおいてのみ有効なパラメータ。
	 * @param unit ユニット定義
	 * @return マップサイズ
	 */
	public static Maybe<MapSize> getMapSize(Unit unit) {
		final List<Param> sz = Units.getParams(unit, "sz");
		if (sz.isEmpty()) {
			return nothing();
		}
		return wrap(getMapSize(sz.get(0)));
	}
	/**
	 * ユニット定義パラメータ{@code "sz"}で指定されたマップサイズを返す.
	 * ジョブネットにおいてのみ有効なパラメータ。
	 * @param p ユニット定義パラメータ
	 * @return マップサイズ
	 */
	public static MapSize getMapSize(final Param p) {
		final Matcher m = PARAM_SZ_VALUE_1.matcher(p.getValue());
		if (!m.matches()) {
			return null;
		}
		return new MapSize() {
			@Override
			public int getWidth() {
				return Integer.parseInt(m.group(1));
			}
			@Override
			public int getHeight() {
				return Integer.parseInt(m.group(2));
			}
		};
	}
	
	private static final Pattern SD_OUTER = Pattern.compile("((\\d+),\\s*)?(en|ud|.+)");
	private static final Pattern SD_INNER = 
			Pattern.compile("(((\\d\\d\\d\\d)/)?(\\d{1,2})/)?((\\+|\\*|@)?(\\d+)"
					+ "|(\\+|\\*|@)?b(-(\\d+))?"
					+ "|(\\+)?(su|mo|tu|we|th|fr|sa)(\\s*:(\\d|b))?)");
	/**
	 * ユニット定義パラメータ{@code "sd"}で指定された実行開始日のリストを返す.
	 * @param u ユニット定義
	 * @return 実行開始日のリスト
	 */
	public static List<StartDate> getStartDates(final Unit u) {
		final List<StartDate> sds = new ArrayList<StartDate>();
		for (final Param p : u.getParams("sd")) {
			sds.add(getStartDate(p));
		}
		return sds;
	}
	/**
	 * ユニット定義パラメータ{@code "sd"}で指定された実行開始日を返す.
	 * @param p ユニット定義パラメータ
	 * @return 実行開始日
	 */
	public static StartDate getStartDate(final Param p) {
		final Matcher m0 = SD_OUTER.matcher(p.getValue());
		if (m0.matches()) {
			if (m0.group(3).equals("ud")) {
				return StartDate.UNDEFINED;
			} else if (m0.group(3).equals("en")) {
				final int ruleNo = m0.group(2) == null ? 1 : Integer.parseInt(m0.group(2));
				return new StartDate(ruleNo, DesignationMethod.ENTRY_DATE, null, null, null, null, null, null, null);
			}
			
			//  123                4          56       7      8        9 10       11  12                    13   14
			// "(((\\d\\d\\d\\d)/)?(\\d\\d)/)?((+|*|@)?(\\d+)|(+|*|@)?b(-(\\d+))?|(+)?(su|mo|tu|we|th|fr|sa)(\\s*:(\\d|b))?)"
			final Matcher m1 = SD_INNER.matcher(m0.group(3));
			
			if (!m1.matches()) {
				throw new IllegalArgumentException();
			}
			
			final int ruleNo = m0.group(2) == null ? 1 : Integer.parseInt(m0.group(2));
			
			final String yyyy = m1.group(3);
			final String mm = m1.group(4);
			
			final String ddPrefix = m1.group(6);

			final String bddPrefix = m1.group(8);
			
			final String dd = m1.group(7);
			final String bdd = m1.group(10);
			final DayOfWeek day = m1.group(12) == null ? null : DayOfWeek.forCode(m1.group(12));
			final String dayNB = m1.group(14);
			
			final Integer yyyyi;
			if (yyyy == null) {
				yyyyi = null;
			} else {
				yyyyi = Integer.parseInt(yyyy);
			}
			final Integer mmi;
			if (mm == null) {
				mmi = null;
			} else {
				mmi = Integer.parseInt(mm);
			}
			final Integer ddi;
			if (dd == null) {
				if (bdd == null) {
					ddi = null;
				} else {
					ddi =  Integer.parseInt(bdd);
				}
			} else {
				ddi = Integer.parseInt(dd);
			}
			final Integer dayN;
			if (dayNB == null || dayNB.equals("b")) {
				dayN = null;
			} else {
				dayN = Integer.parseInt(dayNB);
			}
			final TimingMethod timingMethod;
			final CountingMethod countingMethod;
			if (day == null) {
				if (dd == null) {
					timingMethod = TimingMethod.DAY_OF_MONTH_INVERSELY;
					countingMethod = bddPrefix == null 
							? CountingMethod.ABSOLUTE
							: (bddPrefix.equals("+")
									? CountingMethod.RELATIVE
									: (bddPrefix.equals("*")
											? CountingMethod.BUSINESS_DAY
											: CountingMethod.NON_BUSINESS_DAY));
				} else {
					timingMethod = TimingMethod.DAY_OF_MONTH;
					countingMethod = ddPrefix == null 
							? CountingMethod.ABSOLUTE
									: (ddPrefix.equals("+")
											? CountingMethod.RELATIVE
											: (ddPrefix.equals("*")
													? CountingMethod.BUSINESS_DAY
													: CountingMethod.NON_BUSINESS_DAY));
				}
			} else {
				if (dayNB != null && dayNB.equals("b")) {
					timingMethod = TimingMethod.DAY_OF_LAST_WEEK;
				} else {
					timingMethod = TimingMethod.DAY_OF_WEEK;
				}
				countingMethod = m1.group(11) == null
						? CountingMethod.ABSOLUTE : CountingMethod.RELATIVE;
			}
			
			return new StartDate(ruleNo, DesignationMethod.SCHEDULED_DATE,
					yyyyi,
					mmi,
					ddi,
					day,
					dayN,
					timingMethod,
					countingMethod);
		}
		throw new IllegalArgumentException();
	}
	
	private static final Pattern ST = Pattern.compile("((\\d+),\\s*)?(\\+)?((\\d+):(\\d+))");

	/**
	 * ユニット定義パラメータ{@code "st"}で指定されたジョブ実行開始時刻のリストを返す.
	 * @param u ユニット定義
	 * @return ジョブ実行開始時刻のリスト
	 */
	public static List<StartTime> getStartTimes(final Unit u) {
		final List<StartTime> sts = new ArrayList<StartTime>();
		for (final Param p : u.getParams("st")) {
			sts.add(getStartTime(p));
		}
		return sts;
	}
	
	/**
	 * ユニット定義パラメータ{@code "st"}で指定されたジョブ実行開始時刻を返す.
	 * @param p ユニット定義パラメータ
	 * @return ジョブ実行開始時刻
	 */
	public static StartTime getStartTime(final Param p) {
		final String value = p.getValue();
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
		
		return new StartTime(ruleNo, relative, hh, mi);
	}
	
	private static final Pattern SY = Pattern.compile("((\\d+),\\s*)?((\\d+):(\\d+)|(M|U|C)(\\d+))");
	
	/**
	 * ユニット定義パラメータ{@code "sy"}で指定されたジョブ開始遅延時刻のリストを返す.
	 * @param u ユニット定義
	 * @return 開始遅延時刻のリスト
	 */
	public static List<StartDelayingTime> getStartDelayingTimes(final Unit u) {
		final List<StartDelayingTime> result = new ArrayList<StartDelayingTime>();
		for (final Param p : u.getParams("sy")) {
			result.add(getStartDelayingTime(p));
		}
		return result;
	}
	
	/**
	 * ユニット定義パラメータ{@code "sy"}で指定されたジョブ開始遅延時刻を返す.
	 * @param p ユニット定義パラメータ
	 * @return 開始遅延時刻
	 */
	public static StartDelayingTime getStartDelayingTime(final Param p) {
		final String value = p.getValue();
		final Matcher m = SY.matcher(value);
		if (!m.matches()) {
			return null;
		}
		
		//  12            34      5      6      7
		// "((\\d+),\\s*)?((\\d+):(\\d+)|(M|U|C)(\\d+))"
		final int ruleNo = m.group(2) == null ? 1 : Integer.parseInt(m.group(2));
		final String relCode = m.group(6);
		final boolean relative = relCode != null;
		final Integer hh = relative ? null : Integer.parseInt(m.group(4));
		final Integer mi = Integer.parseInt(relative ? m.group(7) : m.group(5));
		final StartDelayingTime.TimingMethod timingMethod;
		if (!relative) {
			timingMethod = StartDelayingTime.TimingMethod.ABSOLUTE;
		} else if (relCode.equals("M")) {
			timingMethod = StartDelayingTime.TimingMethod.RELATIVE_WITH_ROOT_START_TIME;
		} else if (relCode.equals("U")) {
			timingMethod = StartDelayingTime.TimingMethod.RELATIVE_WITH_SUPER_START_TIME;
		} else {
			timingMethod = StartDelayingTime.TimingMethod.RELATIVE_WITH_THEMSELF_START_TIME;
		}
		
		return new StartDelayingTime(ruleNo, hh, mi, timingMethod);
	}
	
	/**
	 * ユニット定義パラメータ{@code "ey"}で指定されたジョブ終了遅延時刻のリストを返す.
	 * @param u ユニット定義
	 * @return 終了遅延時刻のリスト
	 */
	public static List<EndDelayingTime> getEndDelayingTimes(final Unit u) {
		final List<EndDelayingTime> result = new ArrayList<EndDelayingTime>();
		for (final Param p : u.getParams("ey")) {
			result.add(getEndDelayingTime(p));
		}
		return result;
	}
	
	/**
	 * ユニット定義パラメータ{@code "ey"}で指定されたジョブ終了遅延時刻を返す.
	 * @param p ユニット定義パラメータ
	 * @return 終了遅延時刻
	 */
	public static EndDelayingTime getEndDelayingTime(final Param p) {
		final String value = p.getValue();
		final Matcher m = SY.matcher(value);
		if (!m.matches()) {
			return null;
		}
		
		//  12            34      5      6      7
		// "((\\d+),\\s*)?((\\d+):(\\d+)|(M|U|C)(\\d+))"
		final int ruleNo = m.group(2) == null ? 1 : Integer.parseInt(m.group(2));
		final String relCode = m.group(6);
		final boolean relative = relCode != null;
		final Integer hh = relative ? null : Integer.parseInt(m.group(4));
		final Integer mi = Integer.parseInt(relative ? m.group(7) : m.group(5));
		final EndDelayingTime.TimingMethod timingMethod;
		if (!relative) {
			timingMethod = EndDelayingTime.TimingMethod.ABSOLUTE;
		} else if (relCode.equals("M")) {
			timingMethod = EndDelayingTime.TimingMethod.RELATIVE_WITH_ROOT_START_TIME;
		} else if (relCode.equals("U")) {
			timingMethod = EndDelayingTime.TimingMethod.RELATIVE_WITH_SUPER_START_TIME;
		} else {
			timingMethod = EndDelayingTime.TimingMethod.RELATIVE_WITH_THEMSELF_START_TIME;
		}
		
		return new EndDelayingTime(ruleNo, hh, mi, timingMethod);
	}

	private static final Pattern CY = Pattern.compile("((\\d+),\\s*)?\\((\\d+),\\s*(y|m|w|d)\\)");
	
	/**
	 * ユニット定義パラメータ{@code "cy"}で指定されたジョブネットの処理サイクルのリストを返す.
	 * @param u ユニット定義
	 * @return ジョブネットの処理サイクルのリスト
	 */
	public static List<ExecutionCycle> getExecutionCycles(final Unit u) {
		final List<ExecutionCycle> result = new ArrayList<ExecutionCycle>();
		for (final Param p : u.getParams("cy")) {
			result.add(getExecutionCycle(p));
		}
		return result;
	}
	
	/**
	 * ユニット定義パラメータ{@code "cy"}で指定されたジョブネットの処理サイクルを返す.
	 * @param p ユニット定義パラメータ
	 * @return ジョブネットの処理サイクル
	 */
	public static ExecutionCycle getExecutionCycle(final Param p) {
		final Matcher m = CY.matcher(p.getValue());
		if (!m.matches()) {
			return null;
		}
		
		final CycleUnit u = CycleUnit.forCode(m.group(4));
		final int d = Integer.parseInt(m.group(3));
		final int n = m.group(2) == null ? 1 : Integer.parseInt(m.group(2));
		
		return new ExecutionCycle(n, d, u);
	}
	
	private static final Pattern LN = Pattern.compile("((\\d+),\\s*)?(\\d+)");
	
	/**
	 * ユニット定義パラメータ{@code "ln"}で対応する上位ジョブネットのスケジュールのルール番号のリストを返す.
	 * @param u ユニット定義
	 * @return 対応する上位ジョブネットのスケジュールのルール番号のリスト
	 */
	public static List<LinkedRule> getLinkedRules(final Unit u) {
		final List<LinkedRule> result = new ArrayList<LinkedRule>();
		for (final Param p : u.getParams("ln")) {
			result.add(getLinkedRule(p));
		}
		return result;
	}
	
	/**
	 * 対応する上位ジョブネットのスケジュールのルール番号を取得する.
	 * @param p ユニット定義パラメータ
	 * @return 対応する上位ジョブネットのスケジュールのルール番号
	 */
	public static LinkedRule getLinkedRule(final Param p) {
		final Matcher m = LN.matcher(p.getValue());
		if (!m.matches()) {
			return null;
		}
		
		final int n = m.group(2) == null ? 1 : Integer.parseInt(m.group(2));
		final int d = Integer.parseInt(m.group(3));
		return new LinkedRule(n, d);
	}
	
	/**
	 * ユニット定義パラメータ{@code "ncl"}の値を返す.
	 * @param unit ユニット定義
	 * @return 定義情報値
	 */
	public static Maybe<Boolean> getJobnetConnectorOrdering(Unit unit) {
		return getBoolValues(unit, "ncl");
	}
	/**
	 * ユニット定義パラメータ{@code "ncn"}の値を返す.
	 * 定義情報の詳細はJP1/AJS2の公式リファレンスを参照してください。
	 * @param unit ユニット定義
	 * @return 定義情報値
	 */
	public static Maybe<String> getJobnetConnectorName(Unit unit) {
		return getStringValues(unit, "ncn");
	}
	/**
	 * ユニット定義パラメータ{@code "ncs"}の値を返す.
	 * 定義情報の詳細はJP1/AJS2の公式リファレンスを参照してください。
	 * @param unit ユニット定義
	 * @return 定義情報値
	 */
	public static Maybe<ConnectorOrderingSyncOption> getJobnetConnectorOrderingSyncOption(Unit unit) {
		if (getJobnetConnectorOrdering(unit).getOrElse(false)) {
			return Maybe.wrap(getBoolValues(unit, "ncs").getOrElse(false)
					? ConnectorOrderingSyncOption.SYNC 
					: ConnectorOrderingSyncOption.ASYNC);
		} else {
			return nothing();
		}
	}
	
	// For Connectables
	/**
	 * ユニット定義パラメータ{@code "ncex"}の値を返す.
	 * 定義情報の詳細はJP1/AJS2の公式リファレンスを参照してください。
	 * @param unit ユニット定義
	 * @return 定義情報値
	 */
	public static Maybe<Boolean> getJobnetConnectorOrderingExchangeOption(Unit unit) {
		return getBoolValues(unit, "ncex");
	}
	/**
	 * ユニット定義パラメータ{@code "nchn"}の値を返す.
	 * 定義情報の詳細はJP1/AJS2の公式リファレンスを参照してください。
	 * @param unit ユニット定義
	 * @return 定義情報値
	 */
	public static Maybe<String> getJobnetConnectorHostName(Unit unit) {
		return getStringValues(unit, "nchn");
	}
	/**
	 * ユニット定義パラメータ{@code "ncsv"}の値を返す.
	 * 定義情報の詳細はJP1/AJS2の公式リファレンスを参照してください。
	 * @param unit ユニット定義
	 * @return 定義情報値
	 */
	public static Maybe<String> getJobnetConnectorServiceName(Unit unit) {
		return getStringValues(unit, "ncsv");
	}

	// For Executables
	/**
	 * ユニット定義パラメータ{@code "ha"}で指定された保留属性設定タイプを返す.
	 * @param unit ユニット定義
	 * @return 保留属性設定タイプ
	 */
	public static Maybe<HoldAttrType> getHoldAttrType(Unit unit) {
		final Maybe<String> p = getStringValues(unit, "ha");
		if (p.isOne()) {
			return Maybe.wrap(HoldAttrType.forCode(p.get()));
		} else {
			return Maybe.nothing();
		}
	}
	/**
	 * ユニット定義パラメータ{@code "fd"}で指定された実行所要時間の値を返す.
	 * 設定可能な値は1～1440。単位は分です。未設定の場合−1を返します。
	 * @param unit ユニット定義
	 * @return 実行所要時間
	 */
	public static Maybe<Integer> getFixedDuration(Unit unit) {
		return getIntValues(unit, "fd");
	}
	/**
	 * ユニット定義パラメータ{@code "ex"}で指定された実行ホスト名を返す.
	 * @param unit ユニット定義
	 * @return 実行ホスト名
	 */
	public static Maybe<String> getExecutionHostName(Unit unit) {
		return getStringValues(unit, "ex");
	}
	/**
	 * ユニット定義パラメータ{@code "eu"}で指定された
	 * ジョブ実行時のJP1ユーザの定義を返す.
	 * @param unit ユニット定義
	 * @return ジョブ実行時のJP1ユーザ
	 */
	public static Maybe<ExecutionUserType> getExecutionUserType(Unit unit) {
		final Maybe<String> v = getStringValues(unit, "eu");
		if (v.isOne()) {
			return Maybe.wrap(v.get().equals("def") 
					? ExecutionUserType.DEFINITION_USER 
					: ExecutionUserType.ENTRY_USER);
		} else {
			return Maybe.nothing();
		}
	}
	/**
	 * ユニット定義パラメータ{@code "etm"}で指定された
	 * 実行開始時刻からの相対分数で指定された実行打ち切り時間を返す.
	 * 設定可能な値は1～1440。単位は分です。未設定の場合−1を返します。
	 * @param unit ユニット定義
	 * @return 実行打ち切り時間
	 */
	public static Maybe<Integer> getExecutionTimeOut(Unit unit) {
		return getIntValues(unit, "etm");
	}
	
	// For Jobnets
	/**
	 * ユニット定義パラメータ{@code "ar"}の指定により関連線で結ばれた下位ユニット・ペアのリストを返す.
	 * @param unit ユニット定義
	 * @return 関連線で結ばれたユニットのペアのリスト
	 */
	public static Maybe<AnteroposteriorRelationship> getAnteroposteriorRelationships(Unit unit) {
		final List<AnteroposteriorRelationship> result = new ArrayList<AnteroposteriorRelationship>();
		for (final Param p : unit.getParams()) {
			if (p.getName().equals("ar")) {
				result.add(getAnteroposteriorRelationship(p));
			}
		}
		return Maybe.wrap(result);
	}
	
	/**
	 * ユニット定義パラメータ{@code "ar"}の指定により関連線で結ばれた下位ユニット・ペアを返す.
	 * @param p ユニット定義パラメータ
	 * @return 関連線で結ばれたユニットのペア
	 */
	public static AnteroposteriorRelationship getAnteroposteriorRelationship(final Param p) {
		final List<ParamValue> pvs = p.getValues();
		if (pvs.size() > 0) {
			if (pvs.get(0).getFormat() == ParamValueFormat.TUPLE) {
				final Tuple t = pvs.get(0).getTupleValue();

				return new AnteroposteriorRelationship(
						p.getUnit().getSubUnits(t.get("f").get()).get(0),
						p.getUnit().getSubUnits(t.get("t").get()).get(0),
						t.size() == 3 ? UnitConnectionType.forCode(t.get(2).get()) : UnitConnectionType.SEQUENTIAL);
			}
		}
		return null;
	}

	// For Judgments
	/**
	 * ユニット定義パラメータ{@code "ej"}で指定された判定条件タイプを返す.
	 * @param unit ユニット定義
	 * @return 判定条件タイプ
	 */
	public static Maybe<EvaluateConditionType> getEvaluateConditionType(Unit unit) {
		final Maybe<String> v = getStringValues(unit, "ej");
		if (v.isOne()) {
			return Maybe.wrap(EvaluateConditionType.forCode(v.get()));
		} else {
			return Maybe.nothing();
		}
	};
	
	/**
	 * ユニット定義パラメータ{@code "ej"}で指定された判定条件タイプを返す.
	 * @param p ユニット定義パラメータ
	 * @return 判定条件タイプ
	 */
	public static EvaluateConditionType getEvaluateConditionType(final Param p) {
		return EvaluateConditionType.forCode(p.getValue());
	}
	
	/**
	 * ユニット定義パラメータ{@code "ejc"}で指定された判定終了コードを返す.
	 * 指定可能な値は、0～4294967295です。指定されていない場合は0を返します。
	 * @param unit ユニット定義
	 * @return 判定終了コード
	 */
	public static Maybe<Integer> getEvaluableExitCode(Unit unit) {
		return getIntValues(unit, "ejc");
	};
	/**
	 * ユニット定義パラメータ{@code "ejf"}で指定された
	 * 終了判定ファイル名を返す.
	 * @param unit ユニット定義
	 * @return 終了判定ファイル名
	 */
	public static Maybe<String> getEvaluableFileName(Unit unit) {
		return getStringValues(unit, "ejf");
	};
	
	/**
	 * ユニット定義パラメータ{@code "ejv"}で指定された
	 * 判定対象変数名を返す.
	 * @param unit ユニット定義
	 * @return 判定対象変数名
	 */
	public static Maybe<String> getEvaluableVariableName(Unit unit) {
		return getStringValues(unit, "ejv");
	};
	
	/**
	 * ユニット定義パラメータ{@code "ejt"}で指定された
	 * 判定対象変数（文字列）の判定値を返す.
	 * @param unit ユニット定義
	 * @return 判定対象変数（文字列）の判定値
	 */
	public static Maybe<String> getEvaluableVariableStringValue(Unit unit) {
		return getStringValues(unit, "ejt");
	};
	
	/**
	 * ユニット定義パラメータ{@code "eji"}で指定された
	 * 判定対象変数（数値）の判定値を返す.
	 * @param unit ユニット定義
	 * @return 判定対象変数（数値）の判定値
	 */
	public static Maybe<Integer> getEvaluableVariableIntegerValue(Unit unit) {
		return getIntValues(unit, "eji");
	};
	
	// For Mail Agents
	/**
	 * ユニット定義パラメータ{@code "mlprf"}で指定された
	 * メールプロファイル名を返す.
	 * @param unit ユニット定義
	 * @return メールプロファイル名
	 */
	public static Maybe<String> getMailProfileName(Unit unit) {
		return getStringValues(unit, "mlprf");
	}
	/**
	 * ユニット定義パラメータ{@code "mladr"}で指定された
	 * 送信先メールアドレスのリストを返す.
	 * 設定されていない場合は空のリストを返します。
	 * @param unit ユニット定義
	 * @return 送信先メールアドレスのリスト
	 */
	public static List<MailAddress> getMailAddresses(Unit unit) {
		final ArrayList<MailAddress> l = new ArrayList<MailAddress>();
		final List<Param> ps = Units.getParams(unit, "mladr");
		final Pattern pat = Pattern.compile("^(TO|CC|BCC):\"(.+\"$)");
		
		for (final Param p : ps) {
			final Matcher mat = pat.matcher(p.getValue());
			if (mat.matches()) {
				final String t = mat.group(1);
				final String a = mat.group(2).replaceAll("#\"", "\"").replaceAll("##", "#");
				l.add(new MailAddress(){
					@Override
					public AddressType getType() {
						return t.equals("TO") ? AddressType.TO : (t.equals("CC") ? AddressType.CC : AddressType.BCC);
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
	public static Maybe<String> getMailSubject(Unit unit) {
		return getStringValues(unit, "mlsbj");
	}
	/**
	 * ユニット定義パラメータ{@code "mltxt"}で指定された
	 * メール本文を返す.
	 * @param unit ユニット定義
	 * @return メール本文
	 */
	public static Maybe<String> getMailBody(Unit unit) {
		return getStringValues(unit, "mltxt");
	}
	/**
	 * ユニット定義パラメータ{@code "mlafl"}で指定された
	 * メール添付ファイルリスト名を返す.
	 * @param unit ユニット定義
	 * @return メール添付ファイルリスト名
	 */
	public static Maybe<String> getAttachmentFileListPath(Unit unit) {
		return getStringValues(unit, "mlafl");
	}
	
	// For Mail Sends
	/**
	 * ユニット定義パラメータ{@code "mlftx"}で指定された
	 * メール本文ファイル名を返す.
	 * @param unit ユニット定義
	 * @return メール本文ファイル名
	 */
	public static Maybe<String> getMailBodyFilePath(Unit unit) {
		return getStringValues(unit, "mlftx");
	}
	/**
	 * ユニット定義パラメータ{@code "mlatf"}で指定された
	 * メール添付ファイル名を返す.
	 * @param unit ユニット定義
	 * @return メール添付ファイル名
	 */
	public static Maybe<String> getMailAttachmentFilePath(Unit unit) {
		return getStringValues(unit, "mlatf");
	}

	// For Unix/Pc Job
	/**
	 * ユニット定義パラメータ{@code "wth"}で指定された
	 * 対象ユニットの警告終了閾値を返す.
	 * @return 警告終了閾値（0〜2,147,483,647）
	 */
	public static Maybe<Integer> getWarningThreshold(Unit unit) {
		return getIntValues(unit, "wth");
	}
	/**
	 * ユニット定義パラメータ{@code tho"}で指定された
	 * 対象ユニットの異常終了閾値を返す.
	 * @return 異常終了閾値（0〜2,147,483,647）
	 */
	public static Maybe<Integer> getErrorThreshold(Unit unit) {
		return getIntValues(unit, "tho");
	}
	/**
	 * ユニット定義パラメータ{@code "jd"}で指定された
	 * 終了判定種別を返す.
	 * @param unit ユニット定義
	 * @return 終了判定種別
	 */
	public static Maybe<ResultJudgmentType> getResultJudgmentType(Unit unit) {
		final Maybe<String> v = getStringValues(unit, "jd");
		if (v.isOne()) {
			return Maybe.wrap(ResultJudgmentType.forCode(v.get()));
		} else {
			return Maybe.nothing();
		}
	}
	/**
	 * ユニット定義パラメータ{@code "un"}で指定された実行ユーザ名を返す.
	 * @param unit ユニット定義
	 * @return 実行ユーザ名
	 */
	public static Maybe<String> getExecutionUserName(Unit unit) {
		return getStringValues(unit, "un");
	}
	/**
	 * ユニット定義パラメータ{@code "sc"}で指定された
	 * スクリプトファイル名（UNIXジョブ）もしくは実行ファイル名（PCジョブ）を返す.
	 * @param unit ユニット定義
	 * @return スクリプトファイル名（UNIXジョブ）もしくは実行ファイル名（PCジョブ）
	 */
	public static Maybe<String> getScriptFilePath(Unit unit) {
		return getStringValues(unit, "sc");
	}
	/**
	 * ユニット定義パラメータ{@code "prm"}で指定された
	 * 実行ファイルに対するパラメータの設定値を返す.
	 * @param unit ユニット定義
	 * @return 実行ファイルに対するパラメータ
	 */
	public static Maybe<String> getParameter(Unit unit) {
		return getStringValues(unit, "prm");
	}
	/**
	 * ユニット定義パラメータ{@code "ts1"}で指定された
	 * 転送元ファイル名1（絶対パス）を返す.
	 * @param unit ユニット定義
	 * @return 転送元ファイル名1
	 */
	public static Maybe<String> getTransportSourceFilePath1(Unit unit) {
		return getStringValues(unit, "ts1");
	}
	/**
	 * ユニット定義パラメータ{@code "td1"}で指定された
	 * 転送先ファイル名1を返す.
	 * @param unit ユニット定義
	 * @return 転送先ファイル名1
	 */
	public static Maybe<String> getTransportDestinationFilePath1(Unit unit) {
		return getStringValues(unit, "td1");
	}
	/**
	 * ユニット定義パラメータ{@code "ts2"}で指定された
	 * 転送元ファイル名2（絶対パス）を返す.
	 * @param unit ユニット定義
	 * @return 転送元ファイル名2
	 */
	public static Maybe<String> getTransportSourceFilePath2(Unit unit) {
		return getStringValues(unit, "ts2");
	}
	/**
	 * ユニット定義パラメータ{@code "td2"}で指定された
	 * 転送先ファイル名2を返す.
	 * @param unit ユニット定義
	 * @return 転送先ファイル名2
	 */
	public static Maybe<String> getTransportDestinationFilePath2(Unit unit) {
		return getStringValues(unit, "td2");
	}
	/**
	 * ユニット定義パラメータ{@code "ts3"}で指定された
	 * 転送元ファイル名3（絶対パス）を返す.
	 * @param unit ユニット定義
	 * @return 転送元ファイル名3
	 */
	public static Maybe<String> getTransportSourceFilePath3(Unit unit) {
		return getStringValues(unit, "ts3");
	}
	/**
	 * ユニット定義パラメータ{@code "td3"}で指定された
	 * 転送先ファイル名3を返す.
	 * @param unit ユニット定義
	 * @return 転送先ファイル名3
	 */
	public static Maybe<String> getTransportDestinationFilePath3(Unit unit) {
		return getStringValues(unit, "td3");
	}
	/**
	 * ユニット定義パラメータ{@code "ts4"}で指定された
	 * 転送元ファイル名4（ファイルパス）を返す.
	 * @param unit ユニット定義
	 * @return 転送元ファイル名4
	 */
	public static Maybe<String> getTransportSourceFilePath4(Unit unit) {
		return getStringValues(unit, "ts4");
	}
	/**
	 * ユニット定義パラメータ{@code "td4"}で指定された
	 * 転送先ファイル名4を返す.
	 * @param unit ユニット定義
	 * @return 転送先ファイル名4
	 */
	public static Maybe<String> getTransportDestinationFilePath4(Unit unit) {
		return getStringValues(unit, "td4");
	}
	/**
	 * ユニット定義パラメータ{@code "te"}で指定された
	 * コマンドテキストを返す.
	 * @param unit ユニット定義
	 * @return コマンドテキスト
	 */
	public static Maybe<String> getCommandText(Unit unit) {
		return getStringValues(unit, "te");
	}
	/**
	 * ユニット定義パラメータ{@code "wkp"}で指定された
	 * 作業用パス名（絶対パス）を返す.
	 * @param unit ユニット定義
	 * @return 作業用パス名
	 */
	public static Maybe<String> getWorkPath(Unit unit) {
		return getStringValues(unit, "wkp");
	}
	/**
	 * ユニット定義パラメータ{@code "ev"}で指定された
	 * エージェントホスト上の環境変数ファイル名（絶対パスもしくは相対パス）を返す.
	 * @param unit ユニット定義
	 * @return 環境変数ファイル名
	 */
	public static Maybe<String> getEnvironmentVariableFilePath(Unit unit) {
		return getStringValues(unit, "ev");
	}
	/**
	 * ユニット定義パラメータ{@code "env"}で指定された
	 * 環境変数定義リストを返す.
	 * @param unit ユニット定義
	 * @return 環境変数定義リスト
	 */
	public static List<EnvironmentVariable> getEnvironmentVariable(Unit unit) {
		final List<EnvironmentVariable> l = new ArrayList<EnvironmentVariable>();
		final List<Param> p = Units.getParams(unit, "env");
		if (!p.isEmpty()) {
			try {
				l.addAll(envParamParser.parse(p.get(0).getValue()));
			} catch (ParseException e) {
				// Do nothing.
			}
		}
		return l;
	}
	/**
	 * ユニット定義パラメータ{@code "si"}で指定された
	 * ジョブ実行ホストの標準入力ファイル名（絶対パスもしくは相対パス）を返す.
	 * @param unit ユニット定義
	 * @return 標準入力ファイル名
	 */
	public static Maybe<String> getStandardInputFilePath(Unit unit) {
		return getStringValues(unit, "si");
	}
	/**
	 * ユニット定義パラメータ{@code "so"}で指定された
	 * 標準出力ファイル名（絶対パス）を返す.
	 * @param unit ユニット定義
	 * @return 標準出力ファイル名
	 */
	public static Maybe<String> getStandardOutputFilePath(Unit unit) {
		return getStringValues(unit, "so");
	}
	/**
	 * ユニット定義パラメータ{@code "se"}で指定された
	 * 標準エラー出力ファイル名（絶対パス）を返す.
	 * @param unit ユニット定義
	 * @return 標準エラー出力ファイル名
	 */
	public static Maybe<String> getStandardErrorFilePath(Unit unit) {
		return getStringValues(unit, "se");
	}
	/**
	 * ユニット定義パラメータ{@code "soa"}で指定された
	 * 標準出力ファイルの追加書きオプションを返す.
	 * @param unit ユニット定義
	 * @return 追加書きオプション
	 */
	public static Maybe<WriteOption> getStandardOutputWriteOption(Unit unit) {
		final Maybe<String> v = getStringValues(unit, "soa");
		if (v.isOne()) {
			return Maybe.wrap(WriteOption.valueOf(v.get().toUpperCase()));
		} else {
			return Maybe.nothing();
		}
	}
	/**
	 * ユニット定義パラメータ{@code "sea"}で指定された
	 * 標準エラー出力ファイルの追加書きオプションを返す.
	 * @param unit ユニット定義
	 * @return 追加書きオプション
	 */
	public static Maybe<WriteOption> getStandardErrorWriteOption(Unit unit) {
		final Maybe<String> v = getStringValues(unit, "sea");
		if (v.isOne()) {
			return Maybe.wrap(WriteOption.valueOf(v.get().toUpperCase()));
		} else {
			return Maybe.nothing();
		}
	}
	/**
	 * ユニット定義パラメータ{@code "jdf"}で指定された
	 * 終了判定ファイル名（絶対パス）を返す.
	 * @param unit ユニット定義
	 * @return 終了判定ファイル名
	 */
	public static Maybe<String> getResultJudgementFilePath(Unit unit) {
		return getStringValues(unit, "jdf");
	}
	/**
	 * ユニット定義パラメータ{@code "top1"}で指定された
	 * 転送先ファイル1の自動削除オプションを返す.
	 * @param unit ユニット定義
	 * @return 転送先ファイル1の自動削除オプション
	 */
	public static Maybe<DeleteOption> getTransportDestinationFileDeleteOption1(Unit unit) {
		final List<Param> p = Units.getParams(unit, "top1");
		if (!p.isEmpty()) {
			final String s = p.get(0).getValue();
			if (s.equals("sav")) {
				return wrap(DeleteOption.SAVE);
			} else if (s.equals("del")) {
				return wrap(DeleteOption.DELETE);
			}
		}
		final Maybe<String> ts = getTransportSourceFilePath1(unit);
		final Maybe<String> td = getTransportDestinationFilePath1(unit);
		if (!ts.isNothing() && !td.isNothing()) {
			return wrap(DeleteOption.SAVE);
		} else if (!ts.isNothing() && td.isNothing()) {
			return wrap(DeleteOption.DELETE);
		} else {
			return nothing();
		}
	}
	/**
	 * ユニット定義パラメータ{@code "top2"}で指定された
	 * 転送先ファイル2の自動削除オプションを返す.
	 * @param unit ユニット定義
	 * @return 転送先ファイル2の自動削除オプション
	 */
	public static Maybe<DeleteOption> getTransportDestinationFileDeleteOption2(Unit unit) {
		final List<Param> p = Units.getParams(unit, "top2");
		if (!p.isEmpty()) {
			final String s = p.get(0).getValue();
			if (s.equals("sav")) {
				return wrap(DeleteOption.SAVE);
			} else if (s.equals("del")) {
				return wrap(DeleteOption.DELETE);
			}
		}
		final Maybe<String> ts = getTransportSourceFilePath2(unit);
		final Maybe<String> td = getTransportDestinationFilePath2(unit);
		if (!ts.isNothing() && !td.isNothing()) {
			return wrap(DeleteOption.SAVE);
		} else if (!ts.isNothing() && td.isNothing()) {
			return wrap(DeleteOption.DELETE);
		} else {
			return nothing();
		}
	}
	/**
	 * ユニット定義パラメータ{@code "top3"}で指定された
	 * 転送先ファイル3の自動削除オプションを返す.
	 * @param unit ユニット定義
	 * @return 転送先ファイル3の自動削除オプション
	 */
	public static Maybe<DeleteOption> getTransportDestinationFileDeleteOption3(Unit unit) {
		final List<Param> p = Units.getParams(unit, "top3");
		if (!p.isEmpty()) {
			final String s = p.get(0).getValue();
			if (s.equals("sav")) {
				return wrap(DeleteOption.SAVE);
			} else if (s.equals("del")) {
				return wrap(DeleteOption.DELETE);
			}
		}
		final Maybe<String> ts = getTransportSourceFilePath3(unit);
		final Maybe<String> td = getTransportDestinationFilePath3(unit);
		if (!ts.isNothing() && !td.isNothing()) {
			return wrap(DeleteOption.SAVE);
		} else if (!ts.isNothing() && td.isNothing()) {
			return wrap(DeleteOption.DELETE);
		} else {
			return nothing();
		}
	}
	/**
	 * ユニット定義パラメータ{@code "top4"}で指定された
	 * 転送先ファイル4の自動削除オプションを返す.
	 * @param unit ユニット定義
	 * @return 転送先ファイル4の自動削除オプション
	 */
	public static Maybe<DeleteOption> getTransportDestinationFileDeleteOption4(Unit unit) {
		final List<Param> p = Units.getParams(unit, "top4");
		if (!p.isEmpty()) {
			final String s = p.get(0).getValue();
			if (s.equals("sav")) {
				return wrap(DeleteOption.SAVE);
			} else if (s.equals("del")) {
				return wrap(DeleteOption.DELETE);
			}
		}
		final Maybe<String> ts = getTransportSourceFilePath4(unit);
		final Maybe<String> td = getTransportDestinationFilePath4(unit);
		if (!ts.isNothing() && !td.isNothing()) {
			return wrap(DeleteOption.SAVE);
		} else if (!ts.isNothing() && td.isNothing()) {
			return wrap(DeleteOption.DELETE);
		} else {
			return nothing();
		}
	}
	/**
	 * ユニット定義パラメータ{@code "tmitv"}で指定された
	 * 実行間隔制御の待ち時間を返す.
	 * 指定できる値は1～1440。単位は分です。
	 * @param unit ユニット定義
	 * @return 待ち時間
	 */
	public static Maybe<Integer> getTimeInterval(Unit unit) {
		return getIntValues(unit, "tmitv");
	}
	
	/**
	 * ユニット定義パラメータ{@code "ets"}で指定された
	 * 実行打ち切り時間が経過したあとのジョブの状態を返す.
	 * @param u ユニット定義
	 * @return 実行打ち切り時間が経過したあとのジョブの状態
	 */
	public static Maybe<ExecutionTimedOutStatus> getExecutionTimedOutStatus(final Unit u) {
		final List<Param> p = u.getParams("ets");
		if (p.isEmpty()) {
			return Maybe.nothing();
		} else {
			return Maybe.wrap(getExecutionTimedOutStatus(p.get(0)));
		}
	}
	
	/**
	 * ユニット定義パラメータ{@code "ets"}で指定された
	 * 実行打ち切り時間が経過したあとのジョブの状態を返す.
	 * @param p ユニット定義パラメータ
	 * @return 実行打ち切り時間が経過したあとのジョブの状態
	 */
	public static ExecutionTimedOutStatus getExecutionTimedOutStatus(final Param p) {
		return ExecutionTimedOutStatus.forCode(p.getValue(0).getStringValue());
	}
}
