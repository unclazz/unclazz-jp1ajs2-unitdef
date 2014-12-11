package com.m12i.jp1ajs2.unitdef.ext;

import static com.m12i.jp1ajs2.unitdef.util.Maybe.*;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.m12i.jp1ajs2.unitdef.Param;
import com.m12i.jp1ajs2.unitdef.ParamValue;
import com.m12i.jp1ajs2.unitdef.Tuple;
import com.m12i.jp1ajs2.unitdef.Unit;
import com.m12i.jp1ajs2.unitdef.Units;
import com.m12i.jp1ajs2.unitdef.parser.Input;
import com.m12i.jp1ajs2.unitdef.parser.EnvParamParser;
import com.m12i.jp1ajs2.unitdef.parser.ParseError;
import com.m12i.jp1ajs2.unitdef.util.Maybe;

/**
 * ユニット種別ごとに定義された各種ユニット定義パラメータへのアクセスを提供するユーティリティ.
 * メソッド名はいずれも{@code "fd"}・{@code "eu"}・{@code "tmitv"}といった定義ファイルにおける縮約名から推論されたパラメータ名です。
 */
public final class Params {
	private Params() {}
	
	// For Collections
	private static final Pattern PARAM_EL_VALUE_3 = Pattern.compile("^\\+(\\d+)\\s*\\+(\\d+)$");
	private static final Pattern PARAM_SZ_VALUE_1 = Pattern.compile("^(\\d+)[^\\d]+(\\d+)$");

	public static Maybe<String> getStringValues(final Unit unit, final String paramName) {
		final List<String> list = new ArrayList<String>();
		for (final Param p : Units.getParams(unit, paramName)) {
			list.add(p.getValue());
		}
		return Maybe.wrap(list);
	}

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
					.getStringValue());
			m.matches();
			final Unit subunit = unit.getSubUnits(el.getValues().get(0)
					.getStringValue()).get();
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
		final Maybe<Param> sz = Units.getParams(unit, "sz");
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
		return getBoolValues(unit, "ncl");
	}
	/**
	 * 定義情報"ncn"の値を返す.
	 * 定義情報の詳細はJP1/AJS2の公式リファレンスを参照してください。
	 * @param unit ユニット
	 * @return 定義情報値
	 */
	public static Maybe<String> getJobnetConnectorName(Unit unit) {
		return getStringValues(unit, "ncn");
	}
	/**
	 * 定義情報"ncs"の値を返す.
	 * 定義情報の詳細はJP1/AJS2の公式リファレンスを参照してください。
	 * @param unit ユニット
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
	 * 定義情報"ncex"の値を返す.
	 * 定義情報の詳細はJP1/AJS2の公式リファレンスを参照してください。
	 * @param unit ユニット
	 * @return 定義情報値
	 */
	public static Maybe<Boolean> getJobnetConnectorOrderingExchangeOption(Unit unit) {
		return getBoolValues(unit, "ncex");
	}
	/**
	 * 定義情報"nchn"の値を返す.
	 * 定義情報の詳細はJP1/AJS2の公式リファレンスを参照してください。
	 * @param unit ユニット
	 * @return 定義情報値
	 */
	public static Maybe<String> getJobnetConnectorHostName(Unit unit) {
		return getStringValues(unit, "nchn");
	}
	/**
	 * 定義情報"ncsv"の値を返す.
	 * 定義情報の詳細はJP1/AJS2の公式リファレンスを参照してください。
	 * @param unit ユニット
	 * @return 定義情報値
	 */
	public static Maybe<String> getJobnetConnectorServiceName(Unit unit) {
		return getStringValues(unit, "ncsv");
	}

	// For Executables
	/**
	 * 保留属性設定タイプを返す.
	 * @param unit ユニット
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
	 * 実行所要時間の値を返す.
	 * 設定可能な値は1～1440。単位は分です。未設定の場合−1を返します。
	 * @param unit ユニット
	 * @return 実行所要時間
	 */
	public static Maybe<Integer> getFixedDuration(Unit unit) {
		return getIntValues(unit, "fd");
	}
	/**
	 * 実行ホスト名を返す.
	 * @param unit ユニット
	 * @return 実行ホスト名
	 */
	public static Maybe<String> getExecutionHostName(Unit unit) {
		return getStringValues(unit, "ex");
	}
	/**
	 * ジョブ実行時のJP1ユーザの定義を返す.
	 * @param unit ユニット
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
	 * 実行開始時刻からの相対分数で指定された実行打ち切り時間を返す.
	 * 設定可能な値は1～1440。単位は分です。未設定の場合−1を返します。
	 * @param unit ユニット
	 * @return 実行打ち切り時間
	 */
	public static Maybe<Integer> getExecutionTimeOut(Unit unit) {
		return getIntValues(unit, "etm");
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
					if (pvs.get(0).seemsToBeTuple()) {
						final Tuple t = pvs.get(0).getTupleValue();

						result.add(new AnteroposteriorRelationship(
								unit.getSubUnits(t.get("f").get()).get(),
								unit.getSubUnits(t.get("t").get()).get(),
								t.size() == 3 ? UnitConnectionType.codeCode(t.get(2).get()) : UnitConnectionType.SEQUENTIAL));
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
		final Maybe<String> v = getStringValues(unit, "ej");
		if (v.isOne()) {
			return Maybe.wrap(EvaluateConditionType.forCode(v.get()));
		} else {
			return Maybe.nothing();
		}
	};
	
	/**
	 * 判定終了コードを返す.
	 * 指定可能な値は、0～4294967295です。指定されていない場合は0を返します。
	 * @param unit ユニット
	 * @return 判定終了コード
	 */
	public static Maybe<Integer> getEvaluableExitCode(Unit unit) {
		return getIntValues(unit, "ejc");
	};
	/**
	 * 終了判定ファイル名を返す.
	 * @param unit ユニット
	 * @return 終了判定ファイル名
	 */
	public static Maybe<String> getEvaluableFileName(Unit unit) {
		return getStringValues(unit, "ejf");
	};
	
	/**
	 * 判定対象変数名を返す.
	 * @param unit ユニット
	 * @return 判定対象変数名
	 */
	public static Maybe<String> getEvaluableVariableName(Unit unit) {
		return getStringValues(unit, "ejv");
	};
	
	/**
	 * 判定対象変数（文字列）の判定値を返す.
	 * @param unit ユニット
	 * @return 判定対象変数（文字列）の判定値
	 */
	public static Maybe<String> getEvaluableVariableStringValue(Unit unit) {
		return getStringValues(unit, "ejt");
	};
	
	/**
	 * 判定対象変数（数値）の判定値を返す.
	 * @param unit ユニット
	 * @return 判定対象変数（数値）の判定値
	 */
	public static Maybe<Integer> getEvaluableVariableIntegerValue(Unit unit) {
		return getIntValues(unit, "eji");
	};
	
	// For Mail Agents
	/**
	 * メールプロファイル名を返す.
	 * @param unit ユニット
	 * @return メールプロファイル名
	 */
	public static Maybe<String> getMailProfileName(Unit unit) {
		return getStringValues(unit, "mlprf");
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
		return getStringValues(unit, "mlsbj");
	}
	/**
	 * メール本文を返す.
	 * @param unit ユニット
	 * @return メール本文
	 */
	public static Maybe<String> getMailBody(Unit unit) {
		return getStringValues(unit, "mltxt");
	}
	/**
	 * メール添付ファイルリスト名を返す.
	 * @param unit ユニット
	 * @return メール添付ファイルリスト名
	 */
	public static Maybe<String> getAttachmentFileListPath(Unit unit) {
		return getStringValues(unit, "mlafl");
	}
	
	// For Mail Sends
	/**
	 * メール本文ファイル名を返す.
	 * @param unit ユニット
	 * @return メール本文ファイル名
	 */
	public static Maybe<String> getMailBodyFilePath(Unit unit) {
		return getStringValues(unit, "mlftx");
	}
	/**
	 * メール添付ファイル名を返す.
	 * @param unit ユニット
	 * @return メール添付ファイル名
	 */
	public static Maybe<String> getMailAttachmentFilePath(Unit unit) {
		return getStringValues(unit, "mlatf");
	}

	// For Unix/Pc Job
	/**
	 * 対象ユニットの警告終了閾値を返す.
	 * @return 警告終了閾値（0〜2,147,483,647）
	 */
	public static Maybe<Integer> getWarningThreshold(Unit unit) {
		return getIntValues(unit, "wth");
	}
	/**
	 * 対象ユニットの異常終了閾値を返す.
	 * @return 異常終了閾値（0〜2,147,483,647）
	 */
	public static Maybe<Integer> getErrorThreshold(Unit unit) {
		return getIntValues(unit, "tho");
	}
	/**
	 * 終了判定種別を返す.
	 * @param unit ユニット
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
	 * 実行ユーザ名を返す.
	 * @param unit ユニット
	 * @return 実行ユーザ名
	 */
	public static Maybe<String> getExecutionUserName(Unit unit) {
		return getStringValues(unit, "un");
	}
	/**
	 * スクリプトファイル名（UNIXジョブ）もしくは実行ファイル名（PCジョブ）を返す.
	 * @param unit ユニット
	 * @return スクリプトファイル名（UNIXジョブ）もしくは実行ファイル名（PCジョブ）
	 */
	public static Maybe<String> getScriptFilePath(Unit unit) {
		return getStringValues(unit, "sc");
	}
	/**
	 * 実行ファイルに対するパラメータの設定値を返す.
	 * @param unit ユニット
	 * @return 実行ファイルに対するパラメータ
	 */
	public static Maybe<String> getParameter(Unit unit) {
		return getStringValues(unit, "prm");
	}
	/**
	 * 転送元ファイル名1（絶対パス）を返す.
	 * @param unit ユニット
	 * @return 転送元ファイル名1
	 */
	public static Maybe<String> getTransportSourceFilePath1(Unit unit) {
		return getStringValues(unit, "ts1");
	}
	/**
	 * 転送先ファイル名1を返す.
	 * @param unit ユニット
	 * @return 転送先ファイル名1
	 */
	public static Maybe<String> getTransportDestinationFilePath1(Unit unit) {
		return getStringValues(unit, "td1");
	}
	/**
	 * 転送元ファイル名2（絶対パス）を返す.
	 * @param unit ユニット
	 * @return 転送元ファイル名2
	 */
	public static Maybe<String> getTransportSourceFilePath2(Unit unit) {
		return getStringValues(unit, "ts2");
	}
	/**
	 * 転送先ファイル名2を返す.
	 * @param unit ユニット
	 * @return 転送先ファイル名2
	 */
	public static Maybe<String> getTransportDestinationFilePath2(Unit unit) {
		return getStringValues(unit, "td2");
	}
	/**
	 * 転送元ファイル名3（絶対パス）を返す.
	 * @param unit ユニット
	 * @return 転送元ファイル名3
	 */
	public static Maybe<String> getTransportSourceFilePath3(Unit unit) {
		return getStringValues(unit, "ts3");
	}
	/**
	 * 転送先ファイル名3を返す.
	 * @param unit ユニット
	 * @return 転送先ファイル名3
	 */
	public static Maybe<String> getTransportDestinationFilePath3(Unit unit) {
		return getStringValues(unit, "td3");
	}
	/**
	 * 転送元ファイル名4（ファイルパス）を返す.
	 * @param unit ユニット
	 * @return 転送元ファイル名4
	 */
	public static Maybe<String> getTransportSourceFilePath4(Unit unit) {
		return getStringValues(unit, "ts4");
	}
	/**
	 * 転送先ファイル名4を返す.
	 * @param unit ユニット
	 * @return 転送先ファイル名4
	 */
	public static Maybe<String> getTransportDestinationFilePath4(Unit unit) {
		return getStringValues(unit, "td4");
	}
	/**
	 * コマンドテキストを返す.
	 * @param unit ユニット
	 * @return コマンドテキスト
	 */
	public static Maybe<String> getCommandText(Unit unit) {
		return getStringValues(unit, "te");
	}
	/**
	 * 作業用パス名（絶対パス）を返す.
	 * @param unit ユニット
	 * @return 作業用パス名
	 */
	public static Maybe<String> getWorkPath(Unit unit) {
		return getStringValues(unit, "wkp");
	}
	/**
	 * エージェントホスト上の環境変数ファイル名（絶対パスもしくは相対パス）を返す.
	 * @param unit ユニット
	 * @return 環境変数ファイル名
	 */
	public static Maybe<String> getEnvironmentVariableFilePath(Unit unit) {
		return getStringValues(unit, "ev");
	}
	/**
	 * 環境変数定義リストを返す.
	 * @param unit ユニット
	 * @return 環境変数定義リスト
	 */
	public static List<EnvironmentVariable> getEnvironmentVariable(Unit unit) {
		final List<EnvironmentVariable> l = new ArrayList<EnvironmentVariable>();
		final Maybe<Param> p = Units.getParams(unit, "env");
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
		return getStringValues(unit, "si");
	}
	/**
	 * 標準出力ファイル名（絶対パス）を返す.
	 * @param unit ユニット
	 * @return 標準出力ファイル名
	 */
	public static Maybe<String> getStandardOutputFilePath(Unit unit) {
		return getStringValues(unit, "so");
	}
	/**
	 * 標準エラー出力ファイル名（絶対パス）を返す.
	 * @param unit ユニット
	 * @return 標準エラー出力ファイル名
	 */
	public static Maybe<String> getStandardErrorFilePath(Unit unit) {
		return getStringValues(unit, "se");
	}
	/**
	 * 標準出力ファイルの追加書きオプションを返す.
	 * @param unit ユニット
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
	 * 標準エラー出力ファイルの追加書きオプションを返す.
	 * @param unit ユニット
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
	 * 終了判定ファイル名（絶対パス）を返す.
	 * @param unit ユニット
	 * @return 終了判定ファイル名
	 */
	public static Maybe<String> getResultJudgementFilePath(Unit unit) {
		return getStringValues(unit, "jdf");
	}
	/**
	 * 転送先ファイル1の自動削除オプションを返す.
	 * @param unit ユニット
	 * @return 転送先ファイル1の自動削除オプション
	 */
	public static Maybe<DeleteOption> getTransportDestinationFileDeleteOption1(Unit unit) {
		final Maybe<Param> p = Units.getParams(unit, "top1");
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
		final Maybe<Param> p = Units.getParams(unit, "top2");
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
		final Maybe<Param> p = Units.getParams(unit, "top3");
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
		final Maybe<Param> p = Units.getParams(unit, "top4");
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
		return getIntValues(unit, "tmitv");
	}
}
