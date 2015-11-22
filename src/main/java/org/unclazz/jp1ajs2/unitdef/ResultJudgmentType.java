package org.unclazz.jp1ajs2.unitdef;


/**
 * 終了判定種別.
 * デフォルト値は{@code #DEPENDS_ON_EXIT_CODE}。
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

	private final String code;
	private final String desc;
	
	private ResultJudgmentType(final String code, final String desc){
		this.code = code;
		this.desc = desc;
	}
	
	/**
	 * JP1ユニット定義で使用されるコードを返す.
	 * @return コード
	 */
	public String getCode() {
		return code;
	}
	/**
	 * 設定値の説明テキストを返す.
	 * @return 説明テキスト
	 */
	public String getDescription() {
		return desc;
	}
	/**
	 * 略号をキーとして列挙体インスタンスを検索して返す.
	 * @param code コード
	 * @return 終了判定種別
	 */
	public static ResultJudgmentType forCode(final String code){
		for(final ResultJudgmentType t : values()){
			if(t.getCode().equals(code)){
				return t;
			}
		}
		return null;
	}
}
