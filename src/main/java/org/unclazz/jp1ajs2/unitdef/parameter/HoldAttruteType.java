package org.unclazz.jp1ajs2.unitdef.parameter;

/**
 * ユニット定義パラメータha（保留属性）を表わすオブジェクト.
 */
public enum HoldAttruteType {
	/** ジョブネットの実行を保留する. */
	YES("y", "y：実行を保留する"),
	/** ジョブネットの実行を保留しない. */
	NO("n", "n：実行を保留しない"),
	/** 前回のジョブネットの終了結果が「警告検出終了」、「異常検出終了」、「繰り越し未実行」、「順序不正」、「中断」、または「強制終了」の場合にだけ実行を保留する. */
	IF_PREV_WARNING("w", "w：前回のジョブネットの終了結果が「警告検出終了」、「異常検出終了」、「繰り越し未実行」、「順序不正」、「中断」、または「強制終了」の場合にだけ、実行を保留する。このパラメーターは、「mp=y」と同時に指定できない。"),
	/** 前回のジョブネットの終了結果が「異常検出終了」、「繰り越し未実行」、「順序不正」、「中断」、または「強制終了」の場合にだけ実行を保留する. */
	IF_PREV_ERROR("a", "a：前回のジョブネットの終了結果が「異常検出終了」、「繰り越し未実行」、「順序不正」、「中断」、または「強制終了」の場合にだけ、実行を保留する。このパラメーターは、「mp=y」と同時に指定できない。");
	
	private String code;
	private String desc;
	
	private HoldAttruteType(final String code, final String desc) {
		this.code = code;
		this.desc = desc;
	}
	
	/**
	 * JP1ユニット定義で使用されるコードを返す.
	 * @return 略号文字列
	 */
	public final String getCode() {
		return code;
	}
	/**
	 * JP1定義ファイルの記述形式リファレンスにある設定値の説明テキストを返す.
	 * @return 説明テキスト
	 */
	public final String getDescription() {
		return desc;
	}
	/**
	 * コードをキーとして列挙体インスタンスを検索して返す.
	 * @param code 略号
	 * @return 保留属性設定タイプ
	 * @throws IllegalArgumentException 指定されたコード値に対応するインスタンスが存在しない場合
	 */
	public static final HoldAttruteType valueOfCode(final String code){
		for(final HoldAttruteType t : values()){
			if(t.getCode().equals(code)){
				return t;
			}
		}
		throw new IllegalArgumentException(String.format("Invalid code \"%s\".", code));
	}
}
