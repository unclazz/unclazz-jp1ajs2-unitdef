package com.m12i.jp1ajs2.unitdef.ext;


/**
 * 終了判定種別.
 * デフォルト値は{@code #DEPENDS_ON_EXIT_CODE}です。
 */
public enum ResultJudgmentType {
	/** すべて正常終了とする. */
	FORCE_NORMAL_END("nm", "すべて正常終了とする。"),
	/** すべて異常終了とする. */
	FORCE_ABNORMAL_END("ab", "すべて異常終了とする。"),
	/** 終了コードが指定値以下の場合は正常終了とする（デフォルト）. */
	DEPENDS_ON_EXIT_CODE("cod", "終了コードが指定値以下の場合は，正常終了とする。"),
	/** ジョブの実行開始から実行終了までの間にファイルが更新されている場合は正常終了とする. */
	IF_FILE_MODIFIED("mdf", "ジョブの実行開始から実行終了までの間にファイルが更新されている場合は，正常終了とする。"),
	/** ジョブの実行終了時にファイルが作成されている場合は正常終了とする. */
	IF_FILE_EXISTS("exf", "ジョブの実行終了時にファイルが作成されている場合は，正常終了とする。");

	private final String abbr;
	private final String desc;
	
	private ResultJudgmentType(final String abbr, final String desc){
		this.abbr = abbr;
		this.desc = desc;
	}
	
	/**
	 * JP1定義コード内で使用される略号を返す.
	 * @return 略号文字列
	 */
	public String getAbbr() {
		return abbr;
	}
	/**
	 * JP1定義ファイルの記述形式リファレンスにある設定値の説明テキストを返す.
	 * @return 説明テキスト
	 */
	public String getDesc() {
		return desc;
	}
	/**
	 * 略号をキーとして列挙体インスタンスを検索して返す.
	 * @param abbr 略号
	 * @return 終了判定種別
	 */
	public static ResultJudgmentType searchByAbbr(final String abbr){
		for(final ResultJudgmentType t : values()){
			if(t.getAbbr().equals(abbr)){
				return t;
			}
		}
		return null;
	}

	public static final ValueResolver<ResultJudgmentType> VALUE_RESOLVER = new ValueResolver<ResultJudgmentType>() {
		@Override
		public ResultJudgmentType resolve(String rawValue) {
			return ResultJudgmentType.searchByAbbr(rawValue);
		}
	};
}
