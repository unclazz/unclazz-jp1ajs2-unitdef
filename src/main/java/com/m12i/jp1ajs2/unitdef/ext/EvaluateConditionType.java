package com.m12i.jp1ajs2.unitdef.ext;

import com.m12i.jp1ajs2.unitdef.util.ValueResolver;

/**
 * 判定条件タイプ.
 * 先行ジョブの終了コードと判定値を比較して判定する際の判定条件を定義します。
 * デフォルト値は{@link #EXIT_CODE_GT}です。
 */
public enum EvaluateConditionType {
	/** 先行ジョブの終了コードが判定値より大きい場合（デフォルト）. */
	EXIT_CODE_GT("gt", "gt：先行ジョブの終了コードが判定値より大きいです。「ejf=\"終了判定ファイル名\";」と同時に指定できません。"),
	/** 先行ジョブの終了コードが判定値以上である場合. */
	EXIT_CODE_GE("ge", "ge：先行ジョブの終了コードが判定値以上です。「ejf=\"終了判定ファイル名\";」と同時に指定できません。"),
	/** 先行ジョブの終了コードが判定値より小さい場合. */
	EXIT_CODE_LT("lt", "lt：先行ジョブの終了コードが判定値より小さいです。「ejf=\"終了判定ファイル名\";」と同時に指定できません。"),
	/** 先行ジョブの終了コードが判定値以下である場合. */
	EXIT_CODE_LE("le", "le：先行ジョブの終了コードが判定値以下です。「ejf=\"終了判定ファイル名\";」と同時に指定できません。"),
	/** 先行ジョブの終了コードが判定値と等しい場合. */
	EXIT_CODE_EQ("eq", "eq：先行ジョブの終了コードが判定値と等しいです。「ejf=\"終了判定ファイル名\";」と同時に指定できません。"),
	/** 先行ジョブの終了コードが判定値と等しくない場合. */
	EXIT_CODE_NE("ne", "ne：先行ジョブの終了コードが判定値と等しくないです。「ejf=\"終了判定ファイル名\";」と同時に指定できません。"),
	/** 指定されたファイルが作成されている場合. */
	FILE_EXISTS("ef", "ef：ファイルが作成されています。「ejc=判定終了コード;」と同時に指定できません。"),
	/** 指定されたファイルが作成されていない場合. */
	FILE_NOT_EXISTS("nf", "nf：ファイルが作成されていません。「ejc=判定終了コード;」と同時に指定できません。"),
	/** 指定された変数（数値）の値が判定値より大きい場合. */
	VAR_INT_GT("vgt", "vgt：指定された変数の値が判定値より大きいです。ejiを数値として処理します。"),
	/** 指定された変数の値（数値）が判定値以上である場合. */
	VAR_INT_GE("vge", "vge：指定された変数の値が判定値以上です。ejiを数値として処理します。"),
	/** 指定された変数の値（数値）が判定値より小さい場合. */
	VAR_INT_LT("vlt", "vlt：指定された変数の値が判定値より小さいです。ejiを数値として処理します。"),
	/** 指定された変数の値（数値）が判定値以下である場合. */
	VAR_INT_LE("vle", "vle：指定された変数の値が判定値以下です。ejiを数値として処理します。"),
	/** 指定された変数の値（数値）が判定値と等しい場合. */
	VAR_INT_EQ("veq", "veq：指定された変数の値が判定値と等しいです。ejiを数値として処理します。"),
	/** 指定された変数の値（数値）が判定値と等しくない場合. */
	VAR_INT_NE("vne", "vne：指定された変数の値が判定値と等しくないです。ejiを数値として処理します。"),
	/** 指定された変数の値（文字列）が判定値と等しい場合. */
	VAR_STR_EQ("sce", "sce：指定された変数の値が判定値と等しいです。ejtを文字列として処理します。"),
	/** 指定された変数の値（文字列）が判定値を含んでいる場合. */
	VAR_STR_CONTAINS("spe", "spe：指定された変数の値が判定値を含んでいます。ejtを文字列として処理します。"),
	/** 指定された変数の値（文字列）が判定値と等しくない場合. */
	VAR_STR_NE("sne", "sne：指定された変数の値が判定値と等しくないです。ejtを文字列として処理します。"),
	/** 指定された変数の値が存在する場合. */
	VAR_EXISTS("snn", "snn：指定された変数の値があります。ejtを処理しません。"),
	/** 指定された変数の値が存在しない場合. */
	VAR_NOT_EXISTS("snl", "snl：指定された変数の値がありません。ejtを処理しません。");

	private String abbr;
	private String desc;
	private EvaluateConditionType(final String abbr, final String desc){
		this.abbr = abbr;
		this.desc = desc;
	}
	
	/**
	 * JP1定義コード内で使用される略号を返す.
	 * @return 略号文字列
	 */
	public final String abbr(){
		return abbr;
	}
	/**
	 * JP1定義ファイルの記述形式リファレンスにある設定値の説明テキストを返す.
	 * @return 説明テキスト
	 */
	public final String desc(){
		return desc;
	}
	/**
	 * 略号をキーとして列挙体インスタンスを検索して返す.
	 * @param abbr 略号
	 * @return 判定条件タイプ
	 */
	public static final EvaluateConditionType searchByAbbr(final String abbr){
		for(final EvaluateConditionType t : values()){
			if(t.abbr().equals(abbr)){
				return t;
			}
		}
		return null;
	}
	
	public static final ValueResolver<EvaluateConditionType> VALUE_RESOLVER = new ValueResolver<EvaluateConditionType>() {
		@Override
		public EvaluateConditionType resolve(String rawValue) {
			return EvaluateConditionType.searchByAbbr(rawValue);
		}
	};
}
