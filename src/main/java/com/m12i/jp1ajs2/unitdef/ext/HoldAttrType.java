package com.m12i.jp1ajs2.unitdef.ext;


public enum HoldAttrType {
	/** ジョブネットの実行を保留する. */
	YES("y", "y：実行を保留する"),
	/** ジョブネットの実行を保留しない. */
	NO("n", "n：実行を保留しない"),
	/** 前回のジョブネットの終了結果が「警告検出終了」、「異常検出終了」、「繰り越し未実行」、「順序不正」、「中断」、または「強制終了」の場合にだけ実行を保留する. */
	IF_PREV_WARNING("w", "w：前回のジョブネットの終了結果が「警告検出終了」、「異常検出終了」、「繰り越し未実行」、「順序不正」、「中断」、または「強制終了」の場合にだけ、実行を保留する。このパラメーターは、「mp=y」と同時に指定できない。"),
	/** 前回のジョブネットの終了結果が「異常検出終了」、「繰り越し未実行」、「順序不正」、「中断」、または「強制終了」の場合にだけ実行を保留する. */
	IF_PREV_ERROR("a", "a：前回のジョブネットの終了結果が「異常検出終了」、「繰り越し未実行」、「順序不正」、「中断」、または「強制終了」の場合にだけ、実行を保留する。このパラメーターは、「mp=y」と同時に指定できない。");
	
	private String abbr;
	private String desc;
	
	private HoldAttrType(final String abbr, final String desc) {
		this.abbr = abbr;
		this.desc = desc;
	}
	
	/**
	 * JP1定義コード内で使用される略号を返す.
	 * @return 略号文字列
	 */
	public final String getAbbr() {
		return abbr;
	}
	/**
	 * JP1定義ファイルの記述形式リファレンスにある設定値の説明テキストを返す.
	 * @return 説明テキスト
	 */
	public final String getDesc() {
		return desc;
	}
	/**
	 * 略号をキーとして列挙体インスタンスを検索して返す.
	 * @param abbr 略号
	 * @return 保留属性設定タイプ
	 */
	public static final HoldAttrType searchByAbbr(final String abbr){
		for(final HoldAttrType t : values()){
			if(t.getAbbr().equals(abbr)){
				return t;
			}
		}
		return null;
	}

	public static final ValueResolver<HoldAttrType> VALUE_RESOLVER = new ValueResolver<HoldAttrType>() {
		@Override
		public HoldAttrType resolve(String rawValue) {
			return HoldAttrType.searchByAbbr(rawValue);
		}
	};
}
