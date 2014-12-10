package com.m12i.jp1ajs2.unitdef.ext;

import static com.m12i.jp1ajs2.unitdef.Helpers.*;
import static com.m12i.jp1ajs2.unitdef.Maybe.*;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.m12i.jp1ajs2.unitdef.Maybe;
import com.m12i.jp1ajs2.unitdef.Param;
import com.m12i.jp1ajs2.unitdef.ParamValue;
import com.m12i.jp1ajs2.unitdef.Tuple;
import com.m12i.jp1ajs2.unitdef.Unit;
import com.m12i.jp1ajs2.unitdef.ParamValue.ParamValueType;
import com.m12i.jp1ajs2.unitdef.Units;
import com.m12i.jp1ajs2.unitdef.parser.Input;
import com.m12i.jp1ajs2.unitdef.parser.EnvParamParser;
import com.m12i.jp1ajs2.unitdef.parser.ParseError;

/**
 * ユニット種別ごとに定義された各種パラメータへのアクセスを提供するユーティリティ.
 * メソッド名はいずれも"fd"・"eu"・"tmitv"といった定義ファイルにおける縮約名から類推されたパラメータ名です。
 */
public final class Params {
	private Params() {}
	
	private static interface F1<A0,R> {
		R f(A0 a0);
	}
	
	private static final F1<Param, String> F1_Param_String = new F1<Param, String>() {
		@Override
		public String f(Param a0) {
			return a0.getValue();
		}
	};
	
	private static final F1<Param, Integer> F1_Param_Integer = new F1<Param, Integer>() {
		@Override
		public Integer f(Param a0) {
			return Integer.parseInt(a0.getValues().get(0).getStringValue());
		}
	};
	
	private static final F1<Param, Boolean> F1_Param_Boolean = new F1<Param, Boolean>() {
		@Override
		public Boolean f(Param a0) {
			final String v = a0.getValues().get(0).getStringValue().toLowerCase();
			if (v.equals("y") || v.equals("yes") || v.equals("on") || v.equals("t") || v.equals("true") || v.equals("1")) {
				return true;
			} else if(v.equals("n") || v.equals("no") || v.equals("off") || v.equals("f") || v.equals("false") || v.equals("0")) {
				return false;
			} else {
				throw new RuntimeException("Parameter is not convertible for boolean.");
			}
		}
	};
	
	private static class F1_Param_T<T> implements F1<Param, T> {
		private final ValueResolver<T> resolver;
		public F1_Param_T(ValueResolver<T> resolver) {
			this.resolver = resolver;
		}
		@Override
		public T f(Param a0) {
			return resolver.resolve(a0.getValue());
		}
	};
	
	private static<A0, R> Maybe<R> liftA(F1<A0, R> f, Maybe<A0> o) {
		if (o.isNothing()) {
			return nothing();
		} else {
			return wrap(f.f(o.get()));
		}
	}
	
	private static Maybe<String> wrapParamValue(Maybe<Param> p) {
		if (p == null || NOTHING.equals(p)) {
			return nothing();
		} else {
			return wrap(p.get().getValue());
		}
	}
	
	private static Maybe<Integer> wrapParamValueAsInt(Maybe<Param> p) {
		if (p == null || NOTHING.equals(p)) {
			return nothing();
		} else {
			return wrap(Integer.parseInt(p.get().getValues().get(0).getStringValue()));
		}
	}

	// For Collections
	private static final Pattern PARAM_EL_VALUE_3 = Pattern
			.compile("^\\+(\\d+)\\s*\\+(\\d+)$");
	private static final Pattern PARAM_SZ_VALUE_1 = Pattern.compile("^(\\d+)[^\\d]+(\\d+)$");

	/**
	 * ユニット構成定義情報"el"で指定されたサブユニットの位置情報のリストを返す.
	 * サブユニットが存在しない場合は空のリストを返します。
	 * JP1定義コードでは、サブユニットの位置情報や関連線の情報はサブユニット自身では保持しておらず、
	 * 親ユニット側のユニット定義パラメータとして保持されている点に注意してください。
	 * @param unit ユニット
	 * @return サブユニットの位置情報のリスト
	 */
	public static List<Element> getElements(final Unit unit) {
		final List<Element> result = new ArrayList<Element>();
		final Maybe<Param> els = Units.getParams(unit, "el");
		for (final Param el : els) {
			Matcher m = PARAM_EL_VALUE_3.matcher(el.getValues().get(2)
					.getUnclassifiedValue());
			m.matches();
			final Unit subunit = unit.getSubUnits(el.getValues().get(0)
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
	public static Maybe<MapSize> getMapSize(Unit unit) {
		final Maybe<Param> sz = findParamOne(unit, "sz");
		if (sz.isNothing()) {
			return nothing();
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
		return wrap(s);
	}
	/**
	 * 定義情報"ncl"の値を返す.
	 * 定義情報の詳細はJP1/AJS2の公式リファレンスを参照してください。
	 * @param unit ユニット
	 * @return 定義情報値
	 */
	public static Maybe<Boolean> getJobnetConnectorOrdering(Unit unit) {
		final Maybe<Param> p = findParamOne(unit, "ncl");
		return liftA(F1_Param_Boolean, p);
	}
	/**
	 * 定義情報"ncn"の値を返す.
	 * 定義情報の詳細はJP1/AJS2の公式リファレンスを参照してください。
	 * @param unit ユニット
	 * @return 定義情報値
	 */
	public static Maybe<String> getJobnetConnectorName(Unit unit) {
		final Maybe<Param> p = findParamOne(unit, "ncn");
		return liftA(F1_Param_String, p);
	}
	/**
	 * 定義情報"ncs"の値を返す.
	 * 定義情報の詳細はJP1/AJS2の公式リファレンスを参照してください。
	 * @param unit ユニット
	 * @return 定義情報値
	 */
	public static Maybe<ConnectorOrderingSyncOption> getJobnetConnectorOrderingSyncOption(Unit unit) {
		if (getJobnetConnectorOrdering(unit).getOrElse(false)) {
			final Maybe<Param> p = findParamOne(unit, "ncs");
			return liftA(new F1_Param_T<ConnectorOrderingSyncOption>(ConnectorOrderingSyncOption.VALUE_RESOLVER), p);
		} else {
			return nothing();
		}
	}
	
	// For Connectables
	/**
	 * 定義情報"ncex"の値を返す.
	 * 定義情報の詳細はJP1/AJS2の公式リファレンスを参照してください。
	 * @param unit ユニット
	 * @return 定義情報値
	 */
	public static Maybe<Boolean> getJobnetConnectorOrderingExchangeOption(Unit unit) {
		final Maybe<Param> r = findParamOne(unit, "ncex");
		return liftA(F1_Param_Boolean, r);
	}
	/**
	 * 定義情報"nchn"の値を返す.
	 * 定義情報の詳細はJP1/AJS2の公式リファレンスを参照してください。
	 * @param unit ユニット
	 * @return 定義情報値
	 */
	public static Maybe<String> getJobnetConnectorHostName(Unit unit) {
		final Maybe<Param> r = findParamOne(unit, "nchn");
		return liftA(F1_Param_String, r);
	}
	/**
	 * 定義情報"ncsv"の値を返す.
	 * 定義情報の詳細はJP1/AJS2の公式リファレンスを参照してください。
	 * @param unit ユニット
	 * @return 定義情報値
	 */
	public static Maybe<String> getJobnetConnectorServiceName(Unit unit) {
		final Maybe<Param> r = findParamOne(unit, "ncsv");
		return liftA(F1_Param_String, r);
	}

	// For Executables
	/**
	 * 保留属性設定タイプを返す.
	 * @param unit ユニット
	 * @return 保留属性設定タイプ
	 */
	public static Maybe<HoldAttrType> getHoldAttrType(Unit unit) {
		final Maybe<Param> p = findParamOne(unit, "ha");
		return liftA(new F1_Param_T<HoldAttrType>(HoldAttrType.VALUE_RESOLVER), p);
	}
	/**
	 * 実行所要時間の値を返す.
	 * 設定可能な値は1～1440。単位は分です。未設定の場合−1を返します。
	 * @param unit ユニット
	 * @return 実行所要時間
	 */
	public static Maybe<Integer> getFixedDuration(Unit unit) {
		return liftA(F1_Param_Integer, findParamOne(unit, "fd"));
	}
	/**
	 * 実行ホスト名を返す.
	 * @param unit ユニット
	 * @return 実行ホスト名
	 */
	public static Maybe<String> getExecutionHostName(Unit unit) {
		final Maybe<Param> p = findParamOne(unit, "ex");
		return liftA(F1_Param_String, p);
	}
	/**
	 * ジョブ実行時のJP1ユーザの定義を返す.
	 * @param unit ユニット
	 * @return ジョブ実行時のJP1ユーザ
	 */
	public static Maybe<ExecutionUserType> getExecutionUserType(Unit unit) {
		final Maybe<Param> p = findParamOne(unit, "eu");
		return liftA(new F1_Param_T<ExecutionUserType>(ExecutionUserType.VALUE_RESOLVER), p);
	}
	/**
	 * 実行開始時刻からの相対分数で指定された実行打ち切り時間を返す.
	 * 設定可能な値は1～1440。単位は分です。未設定の場合−1を返します。
	 * @param unit ユニット
	 * @return 実行打ち切り時間
	 */
	public static Maybe<Integer> getExecutionTimeOut(Unit unit) {
		return wrapParamValueAsInt(findParamOne(unit, "etm"));
	}
	
	// For Jobnets
	/**
	 * 引数で指定されたユニットのサブユニットの集合から関連線で結ばれたユニットのペアのリストを返す. このメソッドが返す{@link AnteroposteriorRelationship}
	 * はJP1定義にあらかじめ規定された概念ではありません。 JP1定義解析の便宜のため、このライブラリにおいて独自に規定しているものです。
	 * JP1ユニット定義パラメータの1つである"ar"の内容をもとに生成されます。
	 * 
	 * @param unit ユニット
	 * @return 関連線で結ばれたユニットのペアのリスト
	 */
	public static Maybe<AnteroposteriorRelationship> getAnteroposteriorRelationship(Unit unit) {
		final List<AnteroposteriorRelationship> result = new ArrayList<AnteroposteriorRelationship>();
		for (final Param p : unit.getParams()) {
			if (p.getName().equals("ar")) {
				final List<ParamValue> pvs = p.getValues();
				if (pvs.size() > 0) {
					if (pvs.get(0).is(ParamValueType.TUPLOID)) {
						final Tuple t = pvs.get(0).getTuploidValue();

						result.add(new AnteroposteriorRelationship(
								unit.getSubUnits(t.get("f").get()).get(),
								unit.getSubUnits(t.get("t").get()).get(),
								t.size() == 3 ? UnitConnectionType.searchByAbbr(t.get(2).get()) : UnitConnectionType.SEQUENTIAL));
					}
				}
			}
		}
		return Maybe.wrap(result);
	}

	// For Judgments
	/**
	 * 判定条件タイプを返す.
	 * @param unit ユニット
	 * @return 判定条件タイプ
	 */
	public static Maybe<EvaluateConditionType> getEvaluateConditionType(Unit unit) {
		final Maybe<Param> p = findParamOne(unit, "ej");
		return liftA(new F1_Param_T<EvaluateConditionType>(EvaluateConditionType.VALUE_RESOLVER), p);
	};
	
	/**
	 * 判定終了コードを返す.
	 * 指定可能な値は、0～4294967295です。指定されていない場合は0を返します。
	 * @param unit ユニット
	 * @return 判定終了コード
	 */
	public static Maybe<Integer> getEvaluableExitCode(Unit unit) {
		return wrapParamValueAsInt(findParamOne(unit, "ejc"));
	};
	/**
	 * 終了判定ファイル名を返す.
	 * @param unit ユニット
	 * @return 終了判定ファイル名
	 */
	public static Maybe<String> getEvaluableFileName(Unit unit) {
		return wrapParamValue(findParamOne(unit, "ejf"));
	};
	
	/**
	 * 判定対象変数名を返す.
	 * @param unit ユニット
	 * @return 判定対象変数名
	 */
	public static Maybe<String> getEvaluableVariableName(Unit unit) {
		return wrapParamValue(findParamOne(unit, "ejv"));
	};
	
	/**
	 * 判定対象変数（文字列）の判定値を返す.
	 * @param unit ユニット
	 * @return 判定対象変数（文字列）の判定値
	 */
	public static Maybe<String> getEvaluableVariableStringValue(Unit unit) {
		return wrapParamValue(findParamOne(unit, "ejt"));
	};
	
	/**
	 * 判定対象変数（数値）の判定値を返す.
	 * @param unit ユニット
	 * @return 判定対象変数（数値）の判定値
	 */
	public static Maybe<Integer> getEvaluableVariableIntegerValue(Unit unit) {
		return wrapParamValueAsInt(findParamOne(unit, "eji"));
	};
	
	// For Mail Agents
	/**
	 * メールプロファイル名を返す.
	 * @param unit ユニット
	 * @return メールプロファイル名
	 */
	public static Maybe<String> getMailProfileName(Unit unit) {
		return wrapParamValue(findParamOne(unit, "mlprf"));
	}
	/**
	 * 送信先メールアドレスのリストを返す.
	 * 設定されていない場合は空のリストを返します。
	 * @param unit ユニット
	 * @return 送信先メールアドレスのリスト
	 */
	public static List<MailAddress> getMailAddresses(Unit unit) {
		final ArrayList<MailAddress> l = new ArrayList<MailAddress>();
		final Maybe<Param> ps = Units.getParams(unit, "mladr");
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
	public static Maybe<String> getMailSubject(Unit unit) {
		return wrapParamValue(findParamOne(unit, "mlsbj"));
	}
	/**
	 * メール本文を返す.
	 * @param unit ユニット
	 * @return メール本文
	 */
	public static Maybe<String> getMailBody(Unit unit) {
		return wrapParamValue(findParamOne(unit, "mltxt"));
	}
	/**
	 * メール添付ファイルリスト名を返す.
	 * @param unit ユニット
	 * @return メール添付ファイルリスト名
	 */
	public static Maybe<String> getAttachmentFileListPath(Unit unit) {
		return wrapParamValue(findParamOne(unit, "mlafl"));
	}
	
	// For Mail Sends
	/**
	 * メール本文ファイル名を返す.
	 * @param unit ユニット
	 * @return メール本文ファイル名
	 */
	public static Maybe<String> getMailBodyFilePath(Unit unit) {
		return wrapParamValue(findParamOne(unit, "mlftx"));
	}
	/**
	 * メール添付ファイル名を返す.
	 * @param unit ユニット
	 * @return メール添付ファイル名
	 */
	public static Maybe<String> getMailAttachmentFilePath(Unit unit) {
		return wrapParamValue(findParamOne(unit, "mlatf"));
	}

	// For Unix/Pc Job
	/**
	 * 対象ユニットの警告終了閾値を返す.
	 * @return 警告終了閾値（0〜2,147,483,647）
	 */
	public static Maybe<Integer> getWarningThreshold(Unit unit) {
		return wrapParamValueAsInt(findParamOne(unit, "wth"));
	}
	/**
	 * 対象ユニットの異常終了閾値を返す.
	 * @return 異常終了閾値（0〜2,147,483,647）
	 */
	public static Maybe<Integer> getErrorThreshold(Unit unit) {
		return wrapParamValueAsInt(findParamOne(unit, "tho"));
	}
	/**
	 * 終了判定種別を返す.
	 * @param unit ユニット
	 * @return 終了判定種別
	 */
	public static Maybe<ResultJudgmentType> getResultJudgmentType(Unit unit) {
		final Maybe<Param> p = findParamOne(unit, "jd");
		return liftA(new F1_Param_T<ResultJudgmentType>(ResultJudgmentType.VALUE_RESOLVER), p);
	}
	/**
	 * 実行ユーザ名を返す.
	 * @param unit ユニット
	 * @return 実行ユーザ名
	 */
	public static Maybe<String> getExecutionUserName(Unit unit) {
		return wrapParamValue(findParamOne(unit, "un"));
	}
	/**
	 * スクリプトファイル名（UNIXジョブ）もしくは実行ファイル名（PCジョブ）を返す.
	 * @param unit ユニット
	 * @return スクリプトファイル名（UNIXジョブ）もしくは実行ファイル名（PCジョブ）
	 */
	public static Maybe<String> getScriptFilePath(Unit unit) {
		return wrapParamValue(findParamOne(unit, "sc"));
	}
	/**
	 * 実行ファイルに対するパラメータの設定値を返す.
	 * @param unit ユニット
	 * @return 実行ファイルに対するパラメータ
	 */
	public static Maybe<String> getParameter(Unit unit) {
		return wrapParamValue(findParamOne(unit, "prm"));
	}
	/**
	 * 転送元ファイル名1（絶対パス）を返す.
	 * @param unit ユニット
	 * @return 転送元ファイル名1
	 */
	public static Maybe<String> getTransportSourceFilePath1(Unit unit) {
		return wrapParamValue(findParamOne(unit, "ts1"));
	}
	/**
	 * 転送先ファイル名1を返す.
	 * @param unit ユニット
	 * @return 転送先ファイル名1
	 */
	public static Maybe<String> getTransportDestinationFilePath1(Unit unit) {
		return wrapParamValue(findParamOne(unit, "td1"));
	}
	/**
	 * 転送元ファイル名2（絶対パス）を返す.
	 * @param unit ユニット
	 * @return 転送元ファイル名2
	 */
	public static Maybe<String> getTransportSourceFilePath2(Unit unit) {
		return wrapParamValue(findParamOne(unit, "ts2"));
	}
	/**
	 * 転送先ファイル名2を返す.
	 * @param unit ユニット
	 * @return 転送先ファイル名2
	 */
	public static Maybe<String> getTransportDestinationFilePath2(Unit unit) {
		return wrapParamValue(findParamOne(unit, "td2"));
	}
	/**
	 * 転送元ファイル名3（絶対パス）を返す.
	 * @param unit ユニット
	 * @return 転送元ファイル名3
	 */
	public static Maybe<String> getTransportSourceFilePath3(Unit unit) {
		return wrapParamValue(findParamOne(unit, "ts3"));
	}
	/**
	 * 転送先ファイル名3を返す.
	 * @param unit ユニット
	 * @return 転送先ファイル名3
	 */
	public static Maybe<String> getTransportDestinationFilePath3(Unit unit) {
		return wrapParamValue(findParamOne(unit, "td3"));
	}
	/**
	 * 転送元ファイル名4（ファイルパス）を返す.
	 * @param unit ユニット
	 * @return 転送元ファイル名4
	 */
	public static Maybe<String> getTransportSourceFilePath4(Unit unit) {
		return wrapParamValue(findParamOne(unit, "ts4"));
	}
	/**
	 * 転送先ファイル名4を返す.
	 * @param unit ユニット
	 * @return 転送先ファイル名4
	 */
	public static Maybe<String> getTransportDestinationFilePath4(Unit unit) {
		return wrapParamValue(findParamOne(unit, "td4"));
	}
	/**
	 * コマンドテキストを返す.
	 * @param unit ユニット
	 * @return コマンドテキスト
	 */
	public static Maybe<String> getCommandText(Unit unit) {
		return wrapParamValue(findParamOne(unit, "te"));
	}
	/**
	 * 作業用パス名（絶対パス）を返す.
	 * @param unit ユニット
	 * @return 作業用パス名
	 */
	public static Maybe<String> getWorkPath(Unit unit) {
		return wrapParamValue(findParamOne(unit, "wkp"));
	}
	/**
	 * エージェントホスト上の環境変数ファイル名（絶対パスもしくは相対パス）を返す.
	 * @param unit ユニット
	 * @return 環境変数ファイル名
	 */
	public static Maybe<String> getEnvironmentVariableFilePath(Unit unit) {
		return wrapParamValue(findParamOne(unit, "ev"));
	}
	/**
	 * 環境変数定義リストを返す.
	 * @param unit ユニット
	 * @return 環境変数定義リスト
	 */
	public static List<EnvironmentVariable> getEnvironmentVariable(Unit unit) {
		final List<EnvironmentVariable> l = new ArrayList<EnvironmentVariable>();
		final Maybe<Param> p = findParamOne(unit, "env");
		if (p.isOne()) {
			try {
				l.addAll(new EnvParamParser().parse(Input.fromString(p.get().getValue())));
			} catch (ParseError e) {
				// Do nothing.
			}
		}
		return l;
	}
	/**
	 * ジョブ実行ホストの標準入力ファイル名（絶対パスもしくは相対パス）を返す.
	 * @param unit ユニット
	 * @return 標準入力ファイル名
	 */
	public static Maybe<String> getStandardInputFilePath(Unit unit) {
		return wrapParamValue(findParamOne(unit, "si"));
	}
	/**
	 * 標準出力ファイル名（絶対パス）を返す.
	 * @param unit ユニット
	 * @return 標準出力ファイル名
	 */
	public static Maybe<String> getStandardOutputFilePath(Unit unit) {
		return wrapParamValue(findParamOne(unit, "so"));
	}
	/**
	 * 標準エラー出力ファイル名（絶対パス）を返す.
	 * @param unit ユニット
	 * @return 標準エラー出力ファイル名
	 */
	public static Maybe<String> getStandardErrorFilePath(Unit unit) {
		return wrapParamValue(findParamOne(unit, "se"));
	}
	/**
	 * 標準出力ファイルの追加書きオプションを返す.
	 * @param unit ユニット
	 * @return 追加書きオプション
	 */
	public static Maybe<WriteOption> getStandardOutputWriteOption(Unit unit) {
		final Maybe<Param> p = findParamOne(unit, "soa");
		return liftA(new F1_Param_T<WriteOption>(WriteOption.VALUE_RESOLVER), p);
	}
	/**
	 * 標準エラー出力ファイルの追加書きオプションを返す.
	 * @param unit ユニット
	 * @return 追加書きオプション
	 */
	public static Maybe<WriteOption> getStandardErrorWriteOption(Unit unit) {
		final Maybe<Param> p = findParamOne(unit, "sea");
		return liftA(new F1_Param_T<WriteOption>(WriteOption.VALUE_RESOLVER), p);
	}
	/**
	 * 終了判定ファイル名（絶対パス）を返す.
	 * @param unit ユニット
	 * @return 終了判定ファイル名
	 */
	public static Maybe<String> getResultJudgementFilePath(Unit unit) {
		return wrapParamValue(findParamOne(unit, "jdf"));
	}
	/**
	 * 転送先ファイル1の自動削除オプションを返す.
	 * @param unit ユニット
	 * @return 転送先ファイル1の自動削除オプション
	 */
	public static Maybe<DeleteOption> getTransportDestinationFileDeleteOption1(Unit unit) {
		final Maybe<Param> p = findParamOne(unit, "top1");
		if (p.isOne()) {
			final String s = p.get().getValue();
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
	 * 転送先ファイル2の自動削除オプションを返す.
	 * @param unit ユニット
	 * @return 転送先ファイル2の自動削除オプション
	 */
	public static Maybe<DeleteOption> getTransportDestinationFileDeleteOption2(Unit unit) {
		final Maybe<Param> p = findParamOne(unit, "top2");
		if (p.isOne()) {
			final String s = p.get().getValue();
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
	 * 転送先ファイル3の自動削除オプションを返す.
	 * @param unit ユニット
	 * @return 転送先ファイル3の自動削除オプション
	 */
	public static Maybe<DeleteOption> getTransportDestinationFileDeleteOption3(Unit unit) {
		final Maybe<Param> p = findParamOne(unit, "top3");
		if (p.isOne()) {
			final String s = p.get().getValue();
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
	 * 転送先ファイル4の自動削除オプションを返す.
	 * @param unit ユニット
	 * @return 転送先ファイル4の自動削除オプション
	 */
	public static Maybe<DeleteOption> getTransportDestinationFileDeleteOption4(Unit unit) {
		final Maybe<Param> p = findParamOne(unit, "top4");
		if (p.isOne()) {
			final String s = p.get().getValue();
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
	 * 実行間隔制御の待ち時間を返す.
	 * 指定できる値は1～1440。単位は分です。
	 * @param unit ユニット
	 * @return 待ち時間
	 */
	public static Maybe<Integer> getTimeInterval(Unit unit) {
		return wrapParamValueAsInt(findParamOne(unit, "tmitv"));
	}
}
