package org.unclazz.jp1ajs2.unitdef;


/**
 * 判定条件タイプ.
 * 先行ジョブの終了コードと判定値を比較して判定する際の判定条件を定義する。
 * デフォルト値は{@link #EXIT_CODE_GT}。
 */
public enum EvaluateConditionType {
	/** 先行ジョブの終了コードが判定値より大きい場合（デフォルト）. */
	EXIT_CODE_GT("gt", "gt：先行ジョブの終了コードが判定値より大きいこと"),
	/** 先行ジョブの終了コードが判定値以上である場合. */
	EXIT_CODE_GE("ge", "ge：先行ジョブの終了コードが判定値以上であること"),
	/** 先行ジョブの終了コードが判定値より小さい場合. */
	EXIT_CODE_LT("lt", "lt：先行ジョブの終了コードが判定値より小さいこと"),
	/** 先行ジョブの終了コードが判定値以下である場合. */
	EXIT_CODE_LE("le", "le：先行ジョブの終了コードが判定値以下であること"),
	/** 先行ジョブの終了コードが判定値と等しい場合. */
	EXIT_CODE_EQ("eq", "eq：先行ジョブの終了コードが判定値と等しいこと"),
	/** 先行ジョブの終了コードが判定値と等しくない場合. */
	EXIT_CODE_NE("ne", "ne：先行ジョブの終了コードが判定値と等しくないこと"),
	/** 指定されたファイルが作成されている場合. */
	FILE_EXISTS("ef", "ef：ファイルが作成されてること"),
	/** 指定されたファイルが作成されていない場合. */
	FILE_NOT_EXISTS("nf", "nf：ファイルが作成されていないこと"),
	/** 指定された変数（数値）の値が判定値より大きい場合. */
	VAR_INT_GT("vgt", "vgt：指定された変数の値が判定値より大きいこと"),
	/** 指定された変数の値（数値）が判定値以上である場合. */
	VAR_INT_GE("vge", "vge：指定された変数の値が判定値以上であること（ejiを数値として処理）"),
	/** 指定された変数の値（数値）が判定値より小さい場合. */
	VAR_INT_LT("vlt", "vlt：指定された変数の値が判定値より小さいこと（ejiを数値として処理）"),
	/** 指定された変数の値（数値）が判定値以下である場合. */
	VAR_INT_LE("vle", "vle：指定された変数の値が判定値以下であること（ejiを数値として処理）"),
	/** 指定された変数の値（数値）が判定値と等しい場合. */
	VAR_INT_EQ("veq", "veq：指定された変数の値が判定値と等しいこと（ejiを数値として処理）"),
	/** 指定された変数の値（数値）が判定値と等しくない場合. */
	VAR_INT_NE("vne", "vne：指定された変数の値が判定値と等しくないこと（ejiを数値として処理）"),
	/** 指定された変数の値（文字列）が判定値と等しい場合. */
	VAR_STR_EQ("sce", "sce：指定された変数の値が判定値と等しいこと（ejtを文字列として処理）"),
	/** 指定された変数の値（文字列）が判定値を含んでいる場合. */
	VAR_STR_CONTAINS("spe", "spe：指定された変数の値が判定値を含んでいること（ejtを文字列として処理）"),
	/** 指定された変数の値（文字列）が判定値と等しくない場合. */
	VAR_STR_NE("sne", "sne：指定された変数の値が判定値と等しくないこと（ejtを文字列として処理）"),
	/** 指定された変数の値が存在する場合. */
	VAR_EXISTS("snn", "snn：指定された変数の値があること（ejtを処理しない）"),
	/** 指定された変数の値が存在しない場合. */
	VAR_NOT_EXISTS("snl", "snl：指定された変数の値がないこと（ejtを処理しません）");

	private String code;
	private String desc;
	private EvaluateConditionType(final String code, final String desc){
		this.code = code;
		this.desc = desc;
	}
	
	/**
	 * JP1定義コード内で使用されるコードを返す.
	 * @return 略号文字列
	 */
	public final String getCode(){
		return code;
	}
	/**
	 * JP1定義ファイルの記述形式リファレンスにある設定値の説明テキストを返す.
	 * @return 説明テキスト
	 */
	public final String getDescription(){
		return desc;
	}
	/**
	 * コードをキーとして列挙体インスタンスを検索して返す.
	 * @param code 略号
	 * @return 判定条件タイプ
	 */
	public static final EvaluateConditionType forCode(final String code){
		for(final EvaluateConditionType t : values()){
			if(t.getCode().equals(code)){
				return t;
			}
		}
		return null;
	}
	
	@Override
	public String toString() {
		return String.format("判定条件は`%s`", desc);
	}
}
