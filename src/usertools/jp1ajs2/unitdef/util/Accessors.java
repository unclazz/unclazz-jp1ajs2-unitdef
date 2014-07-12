package usertools.jp1ajs2.unitdef.util;

import static usertools.jp1ajs2.unitdef.util.Helpers.*;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import usertools.jp1ajs2.unitdef.core.Param;
import usertools.jp1ajs2.unitdef.core.ParamValue;
import usertools.jp1ajs2.unitdef.core.Tuple;
import usertools.jp1ajs2.unitdef.core.Unit;
import usertools.jp1ajs2.unitdef.core.ParamValue.ParamValueType;
import usertools.jp1ajs2.unitdef.ext.Arrow;
import usertools.jp1ajs2.unitdef.ext.ConnectorOrderingSyncOption;
import usertools.jp1ajs2.unitdef.ext.DeleteOption;
import usertools.jp1ajs2.unitdef.ext.Element;
import usertools.jp1ajs2.unitdef.ext.EvaluateConditionType;
import usertools.jp1ajs2.unitdef.ext.ExecutionUserType;
import usertools.jp1ajs2.unitdef.ext.HoldType;
import usertools.jp1ajs2.unitdef.ext.MailAddress;
import usertools.jp1ajs2.unitdef.ext.MapSize;
import usertools.jp1ajs2.unitdef.ext.ResultJudgmentType;
import usertools.jp1ajs2.unitdef.ext.UnitConnectionType;
import usertools.jp1ajs2.unitdef.ext.WriteOption;
import static usertools.jp1ajs2.unitdef.util.Option.*;

public class Accessors {
	private Accessors() {}
	
	private static<T> Option<T> wrapA2(Object a0, T a2) {
		if (a0 == null || NONE.equals(a0)) {
			return none();
		} else {
			return wrap(a2);
		}
	}
	
	private static Option<String> wrapParamValue(Option<Param> p) {
		if (p == null || NONE.equals(p)) {
			return none();
		} else {
			return wrap(p.get().getValue());
		}
	}
	
	private static Option<Integer> wrapParamValueAsInt(Option<Param> p) {
		if (p == null || NONE.equals(p)) {
			return none();
		} else {
			return wrap(Integer.parseInt(p.get().getValues().get(0).getStringValue()));
		}
	}

	// For Collections
	private static final Pattern PARAM_EL_VALUE_3 = Pattern
			.compile("^\\+(\\d+)\\s\\+(\\d+)$");
	private static final Pattern PARAM_SZ_VALUE_1 = Pattern.compile("^(\\d+)[^\\d]+(\\d+)$");

	/**
	 * ユニット構成定義情報"el"で指定されたサブユニットの位置情報のリストを返す.
	 * サブユニットが存在しない場合は空のリストを返します。
	 * JP1定義コードでは、サブユニットの位置情報や関連線の情報はサブユニット自身では保持しておらず、
	 * 親ユニット側のユニット定義パラメータとして保持されている点に注意してください。
	 * @param unit ユニット
	 * @return サブユニットの位置情報のリスト
	 */
	public static List<Element> elements(final Unit unit) {
		final List<Element> result = new ArrayList<Element>();
		final List<Param> els = findParamAll(unit, "el");
		for (final Param el : els) {
			Matcher m = PARAM_EL_VALUE_3.matcher(el.getValues().get(2)
					.getUnclassifiedValue());
			m.matches();
			final Unit subunit = unit.getSubUnit(el.getValues().get(0)
					.getUnclassifiedValue()).get();
			final int horizontalPixel = Integer.parseInt(m.group(1));
			final int verticalPixel = Integer.parseInt(m.group(2));
			result.add(new Element(subunit, horizontalPixel, verticalPixel));
		}
		return result;
	}
	/**
	 * ユニット構成定義情報"sz"で指定されたマップサイズを返す.
	 * ジョブネットにおいてのみ有効なパラメータです。
	 * @param unit ユニット
	 * @return マップサイズ
	 */
	public static Option<MapSize> size(Unit unit) {
		final Option<Param> sz = findParamOne(unit, "sz");
		if (sz.isNone()) {
			return none();
		}
		final Matcher m = PARAM_SZ_VALUE_1.matcher(sz.get().getValue());
		m.matches();
		final MapSize s = new MapSize() {
			@Override
			public int getWidth() {
				return Integer.parseInt(m.group(1));
			}
			@Override
			public int getHeight() {
				return Integer.parseInt(m.group(2));
			}
		};
		return some(s);
	}
	/**
	 * 定義情報"ncl"の値を返す.
	 * 定義情報の詳細はJP1/AJS2の公式リファレンスを参照してください。
	 * @param unit ユニット
	 * @return 定義情報値
	 */
	public static Option<Boolean> jobnetConnectorOrdering(Unit unit) {
		final Option<Param> p = findParamOne(unit, "ncl");
		if (p.isNone()) {
			return none();
		} else {
			return some(p.get().getValue().equals("y"));
		}
	}
	/**
	 * 定義情報"ncn"の値を返す.
	 * 定義情報の詳細はJP1/AJS2の公式リファレンスを参照してください。
	 * @param unit ユニット
	 * @return 定義情報値
	 */
	public static Option<String> jobnetConnectorName(Unit unit) {
		final Option<Param> p = findParamOne(unit, "ncn");
		return wrapA2(p, p.get().getValue());
	}
	/**
	 * 定義情報"ncs"の値を返す.
	 * 定義情報の詳細はJP1/AJS2の公式リファレンスを参照してください。
	 * @param unit ユニット
	 * @return 定義情報値
	 */
	public static Option<ConnectorOrderingSyncOption> jobnetConnectorOrderingSyncOption(Unit unit) {
		if (jobnetConnectorOrdering(unit).getOrElse(false)) {
			final Option<Param> p = findParamOne(unit, "ncs");
			return some(p.isSome() && p.get().getValue().equals("y") ? 
					ConnectorOrderingSyncOption.SYNC : ConnectorOrderingSyncOption.ASYNC);
		} else {
			return none();
		}
	}
	
	// For Connectables
	/**
	 * 定義情報"ncex"の値を返す.
	 * 定義情報の詳細はJP1/AJS2の公式リファレンスを参照してください。
	 * @param unit ユニット
	 * @return 定義情報値
	 */
	public static Option<Boolean> jobnetConnectorOrderingExchangeOption(Unit unit) {
		final Option<Param> r = findParamOne(unit, "ncex");
		if (r.isNone()) {
			return none();
		} else {
			return some(r.get().getValue().equals("y"));
		}
	}
	/**
	 * 定義情報"nchn"の値を返す.
	 * 定義情報の詳細はJP1/AJS2の公式リファレンスを参照してください。
	 * @param unit ユニット
	 * @return 定義情報値
	 */
	public static Option<String> jobnetConnectorHostName(Unit unit) {
		final Option<Param> r = findParamOne(unit, "nchn");
		return wrapA2(r, r.get().getValue());
	}
	/**
	 * 定義情報"ncsv"の値を返す.
	 * 定義情報の詳細はJP1/AJS2の公式リファレンスを参照してください。
	 * @param unit ユニット
	 * @return 定義情報値
	 */
	public static Option<String> jobnetConnectorServiceName(Unit unit) {
		final Option<Param> r = findParamOne(unit, "ncsv");
		return wrapA2(r, r.get().getValue());
	}

	// For Executables
	/**
	 * 保留属性設定タイプを返す.
	 * @param unit ユニット
	 * @return 保留属性設定タイプ
	 */
	public static Option<HoldType> holdType(Unit unit) {
		final Option<Param> p = findParamOne(unit, "ha");
		if (p == null) {
			return some(HoldType.NO);
		} else {
			return some(HoldType.searchByAbbr(p.get().getValues().get(0)
					.getStringValue()));
		}
	}
	/**
	 * 実行所要時間の値を返す.
	 * 設定可能な値は1～1440。単位は分です。未設定の場合−1を返します。
	 * @param unit ユニット
	 * @return 実行所要時間
	 */
	public static Option<Integer> fixedDuration(Unit unit) {
		return wrapParamValueAsInt(findParamOne(unit, "fd"));
	}
	/**
	 * 実行ホスト名を返す.
	 * @param unit ユニット
	 * @return 実行ホスト名
	 */
	public static Option<String> executionHostName(Unit unit) {
		final Option<Param> p = findParamOne(unit, "ex");
		if (p.isNone()) {
			return none();
		} else {
			return some(p.get().getValues().get(0).getStringValue());
		}
	}
	/**
	 * ジョブ実行時のJP1ユーザの定義を返す.
	 * @param unit ユニット
	 * @return ジョブ実行時のJP1ユーザ
	 */
	public static Option<ExecutionUserType> executionUserType(Unit unit) {
		final Option<Param> r = findParamOne(unit, "eu");
		if (r.isSome()) {
			if (r.get().getValue().equals("def")) {
				return some(ExecutionUserType.DEFINITION_USER);
			} else {
				return some(ExecutionUserType.ENTRY_USER);
			}
		} else {
			return none();
		}
	}
	/**
	 * 実行開始時刻からの相対分数で指定された実行打ち切り時間を返す.
	 * 設定可能な値は1～1440。単位は分です。未設定の場合−1を返します。
	 * @param unit ユニット
	 * @return 実行打ち切り時間
	 */
	public static Option<Integer> executionTimeOut(Unit unit) {
		return wrapParamValueAsInt(findParamOne(unit, "etm"));
	}
	
	// For Jobnets
	/**
	 * 引数で指定されたユニットのサブユニットの集合から関連線で結ばれたユニットのペアのリストを返す. このメソッドが返す{@link Arrow}
	 * はJP1定義にあらかじめ規定された概念ではありません。 JP1定義解析の便宜のため、このライブラリにおいて独自に規定しているものです。
	 * JP1ユニット定義パラメータの1つである"ar"の内容をもとに生成されます。
	 * 
	 * @param unit ユニット
	 * @return 関連線で結ばれたユニットのペアのリスト
	 */
	public static List<Arrow> arrows(Unit unit) {
		final List<Arrow> result = new ArrayList<Arrow>();
		for (final Param p : unit.getParams()) {
			if (p.getName().equals("ar")) {
				final List<ParamValue> pvs = p.getValues();
				if (pvs.size() > 0) {
					if (pvs.get(0).is(ParamValueType.TUPLOID)) {
						final Tuple t = pvs.get(0).getTuploidValue();

						result.add(new Arrow(
								unit.getSubUnit(t.get("f").get()).get(),
								unit.getSubUnit(t.get("t").get()).get(),
								t.size() == 3 ? UnitConnectionType.searchByAbbr(t.get(2).get()) : UnitConnectionType.SEQUENTIAL));
					}
				}
			}
		}
		return result;
	}

	// For Judgments
	/**
	 * 判定条件タイプを返す.
	 * @param unit ユニット
	 * @return 判定条件タイプ
	 */
	public static Option<EvaluateConditionType> evaluateConditionType(Unit unit) {
		final Option<Param> p = findParamOne(unit, "ej");
		if (p.isNone()) {
			return none();
		} else {
			return some(EvaluateConditionType.searchByAbbr(p.get().getValues().get(0)
					.getStringValue()));
		}
	};
	
	/**
	 * 判定終了コードを返す.
	 * 指定可能な値は、0～4294967295です。指定されていない場合は0を返します。
	 * @param unit ユニット
	 * @return 判定終了コード
	 */
	public static Option<Integer> evaluableExitCode(Unit unit) {
		return wrapParamValueAsInt(findParamOne(unit, "ejc"));
	};
	/**
	 * 終了判定ファイル名を返す.
	 * @param unit ユニット
	 * @return 終了判定ファイル名
	 */
	public static Option<String> evaluableFileName(Unit unit) {
		return wrapParamValue(findParamOne(unit, "ejf"));
	};
	
	/**
	 * 判定対象変数名を返す.
	 * @param unit ユニット
	 * @return 判定対象変数名
	 */
	public static Option<String> evaluableVariableName(Unit unit) {
		return wrapParamValue(findParamOne(unit, "ejv"));
	};
	
	/**
	 * 判定対象変数（文字列）の判定値を返す.
	 * @param unit ユニット
	 * @return 判定対象変数（文字列）の判定値
	 */
	public static Option<String> evaluableVariableStringValue(Unit unit) {
		return wrapParamValue(findParamOne(unit, "ejt"));
	};
	
	/**
	 * 判定対象変数（数値）の判定値を返す.
	 * @param unit ユニット
	 * @return 判定対象変数（数値）の判定値
	 */
	public static Option<Integer> evaluableVariableIntegerValue(Unit unit) {
		return wrapParamValueAsInt(findParamOne(unit, "eji"));
	};
	
	// For Mail Agents
	/**
	 * メールプロファイル名を返す.
	 * @param unit ユニット
	 * @return メールプロファイル名
	 */
	public static Option<String> mailProfileName(Unit unit) {
		return wrapParamValue(findParamOne(unit, "mlprf"));
	}
	/**
	 * 送信先メールアドレスのリストを返す.
	 * 設定されていない場合は空のリストを返します。
	 * @param unit ユニット
	 * @return 送信先メールアドレスのリスト
	 */
	public static List<MailAddress> mailAddresses(Unit unit) {
		final ArrayList<MailAddress> l = new ArrayList<MailAddress>();
		final List<Param> ps = findParamAll(unit, "mladr");
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
	 * メール件名を返す.
	 * @param unit ユニット
	 * @return メール件名
	 */
	public static Option<String> mailSubject(Unit unit) {
		return wrapParamValue(findParamOne(unit, "mlsbj"));
	}
	/**
	 * メール本文を返す.
	 * @param unit ユニット
	 * @return メール本文
	 */
	public static Option<String> mailBody(Unit unit) {
		return wrapParamValue(findParamOne(unit, "mltxt"));
	}
	/**
	 * メール添付ファイルリスト名を返す.
	 * @param unit ユニット
	 * @return メール添付ファイルリスト名
	 */
	public static Option<String> attachmentFileListPath(Unit unit) {
		return wrapParamValue(findParamOne(unit, "mlafl"));
	}
	
	// For Mail Sends
	/**
	 * メール本文ファイル名を返す.
	 * @param unit ユニット
	 * @return メール本文ファイル名
	 */
	public static Option<String> mailBodyFilePath(Unit unit) {
		return wrapParamValue(findParamOne(unit, "mlftx"));
	}
	/**
	 * メール添付ファイル名を返す.
	 * @param unit ユニット
	 * @return メール添付ファイル名
	 */
	public static Option<String> mailAttachmentFilePath(Unit unit) {
		return wrapParamValue(findParamOne(unit, "mlatf"));
	}

	// For Unix/Pc Job
	/**
	 * 対象ユニットの警告終了閾値を返す.
	 * @return 警告終了閾値（0〜2,147,483,647）
	 */
	public static Option<Integer> warningThreshold(Unit unit) {
		return wrapParamValueAsInt(findParamOne(unit, "wth"));
	}
	/**
	 * 対象ユニットの異常終了閾値を返す.
	 * @return 異常終了閾値（0〜2,147,483,647）
	 */
	public static Option<Integer> errorThreshold(Unit unit) {
		return wrapParamValueAsInt(findParamOne(unit, "tho"));
	}
	/**
	 * 終了判定種別を返す.
	 * @param unit ユニット
	 * @return 終了判定種別
	 */
	public static Option<ResultJudgmentType> resultJudgmentType(Unit unit) {
		final Option<Param> p = findParamOne(unit, "jd");
		if (p.isNone()) {
			return none();
		} else {
			return some(ResultJudgmentType.searchByAbbr(p.get().getValues().get(0)
					.getStringValue()));
		}
	}
	/**
	 * 実行ユーザ名を返す.
	 * @param unit ユニット
	 * @return 実行ユーザ名
	 */
	public static Option<String> executionUserName(Unit unit) {
		return wrapParamValue(findParamOne(unit, "un"));
	}
	/**
	 * スクリプトファイル名（UNIXジョブ）もしくは実行ファイル名（PCジョブ）を返す.
	 * @param unit ユニット
	 * @return スクリプトファイル名（UNIXジョブ）もしくは実行ファイル名（PCジョブ）
	 */
	public static Option<String> scriptFilePath(Unit unit) {
		return wrapParamValue(findParamOne(unit, "sc"));
	}
	/**
	 * 実行ファイルに対するパラメータの設定値を返す.
	 * @param unit ユニット
	 * @return 実行ファイルに対するパラメータ
	 */
	public static Option<String> parameter(Unit unit) {
		return wrapParamValue(findParamOne(unit, "prm"));
	}
	public static Option<String> transportSourceFilePath1(Unit unit) {
		return wrapParamValue(findParamOne(unit, "ts1"));
	}
	public static Option<String> transportDestinationFilePath1(Unit unit) {
		return wrapParamValue(findParamOne(unit, "td1"));
	}
	public static Option<String> transportSourceFilePath2(Unit unit) {
		return wrapParamValue(findParamOne(unit, "ts2"));
	}
	public static Option<String> transportDestinationFilePath2(Unit unit) {
		return wrapParamValue(findParamOne(unit, "td2"));
	}
	public static Option<String> transportSourceFilePath3(Unit unit) {
		return wrapParamValue(findParamOne(unit, "ts3"));
	}
	public static Option<String> transportDestinationFilePath3(Unit unit) {
		return wrapParamValue(findParamOne(unit, "td3"));
	}
	public static Option<String> transportSourceFilePath4(Unit unit) {
		return wrapParamValue(findParamOne(unit, "ts4"));
	}
	public static Option<String> transportDestinationFilePath4(Unit unit) {
		return wrapParamValue(findParamOne(unit, "td4"));
	}
	public static Option<String> commandText(Unit unit) {
		return wrapParamValue(findParamOne(unit, "te"));
	}
	public static Option<String> workPath(Unit unit) {
		return wrapParamValue(findParamOne(unit, "wkp"));
	}
	public static Option<String> environmentVariableFilePath(Unit unit) {
		return wrapParamValue(findParamOne(unit, "ev"));
	}
	public static Option<String> environmentVariable(Unit unit) {
		return wrapParamValue(findParamOne(unit, "env"));
	}
	public static Option<String> standardInputFilePath(Unit unit) {
		return wrapParamValue(findParamOne(unit, "si"));
	}
	public static Option<String> standardOutputFilePath(Unit unit) {
		return wrapParamValue(findParamOne(unit, "so"));
	}
	public static Option<String> standardErrorFilePath(Unit unit) {
		return wrapParamValue(findParamOne(unit, "se"));
	}
	public static Option<WriteOption> standardOutputWriteOption(Unit unit) {
		final Option<Param> r = findParamOne(unit, "soa");
		if (r.isSome() && r.get().getValue().equals("add")) {
			return some(WriteOption.ADD);
		} else {
			return some(WriteOption.NEW);
		}
	}
	public static Option<WriteOption> standardErrorWriteOption(Unit unit) {
		final Option<Param> r = findParamOne(unit, "sea");
		if (r.isSome() && r.get().getValue().equals("add")) {
			return some(WriteOption.ADD);
		} else {
			return some(WriteOption.NEW);
		}
	}
	public static Option<String> resultJudgementFilePath(Unit unit) {
		return wrapParamValue(findParamOne(unit, "jdf"));
	}
	public static Option<DeleteOption> transportDestinationFileDeleteOption1(Unit unit) {
		final Option<Param> p = findParamOne(unit, "top1");
		if (p.isSome()) {
			final String s = p.get().getValue();
			if (s.equals("sav")) {
				return some(DeleteOption.SAVE);
			} else if (s.equals("del")) {
				return some(DeleteOption.DELETE);
			}
		}
		final Option<String> ts = transportSourceFilePath1(unit);
		final Option<String> td = transportDestinationFilePath1(unit);
		if (!ts.isNone() && !td.isNone()) {
			return some(DeleteOption.SAVE);
		} else if (!ts.isNone() && td.isNone()) {
			return some(DeleteOption.DELETE);
		} else {
			return null;
		}
	}
	public static Option<DeleteOption> transportDestinationFileDeleteOption2(Unit unit) {
		final Option<Param> p = findParamOne(unit, "top2");
		if (p.isSome()) {
			final String s = p.get().getValue();
			if (s.equals("sav")) {
				return some(DeleteOption.SAVE);
			} else if (s.equals("del")) {
				return some(DeleteOption.DELETE);
			}
		}
		final Option<String> ts = transportSourceFilePath2(unit);
		final Option<String> td = transportDestinationFilePath2(unit);
		if (!ts.isNone() && !td.isNone()) {
			return some(DeleteOption.SAVE);
		} else if (!ts.isNone() && td.isNone()) {
			return some(DeleteOption.DELETE);
		} else {
			return none();
		}
	}
	public static Option<DeleteOption> transportDestinationFileDeleteOption3(Unit unit) {
		final Option<Param> p = findParamOne(unit, "top3");
		if (p.isSome()) {
			final String s = p.get().getValue();
			if (s.equals("sav")) {
				return some(DeleteOption.SAVE);
			} else if (s.equals("del")) {
				return some(DeleteOption.DELETE);
			}
		}
		final Option<String> ts = transportSourceFilePath3(unit);
		final Option<String> td = transportDestinationFilePath3(unit);
		if (!ts.isNone() && !td.isNone()) {
			return some(DeleteOption.SAVE);
		} else if (!ts.isNone() && td.isNone()) {
			return some(DeleteOption.DELETE);
		} else {
			return none();
		}
	}
	public static Option<DeleteOption> transportDestinationFileDeleteOption4(Unit unit) {
		final Option<Param> p = findParamOne(unit, "top4");
		if (p.isSome()) {
			final String s = p.get().getValue();
			if (s.equals("sav")) {
				return some(DeleteOption.SAVE);
			} else if (s.equals("del")) {
				return some(DeleteOption.DELETE);
			}
		}
		final Option<String> ts = transportSourceFilePath4(unit);
		final Option<String> td = transportDestinationFilePath4(unit);
		if (!ts.isNone() && !td.isNone()) {
			return some(DeleteOption.SAVE);
		} else if (!ts.isNone() && td.isNone()) {
			return some(DeleteOption.DELETE);
		} else {
			return null;
		}
	}
}
