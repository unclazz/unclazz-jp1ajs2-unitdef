package com.m12i.jp1ajs2.unitdef.ext;

/**
 * 接続種別.
 * @author mizuki.fujitani
 *
 */
public enum UnitConnectionType {
	/** 順接続する. */
	SEQUENTIAL("seq", "順接続します。"), 
	/** 判定ジョブを従属ユニットとして条件接続する. */
	CONDITIONAL("con", "判定ジョブを従属ユニットとして，条件接続します。先行ユニット名に判定ジョブを指定した場合にだけ指定できます。");
	
	private final String code;
	private final String desc;
	
	private UnitConnectionType(final String code, final String desc){
		this.code = code;
		this.desc = desc;
	}
	/**
	 * JP1定義コード内で使用される略号を返す.
	 * @return 略号文字列
	 */
	public String getCode() {
		return code;
	}
	/**
	 * JP1定義ファイルの記述形式リファレンスにある設定値の説明テキストを返す.
	 * @return 説明テキスト
	 */
	public String getDescription() {
		return desc;
	}
	/**
	 * 略号をキーとして列挙体インスタンスを検索して返す.
	 * @param code 略号
	 * @return 接続種別
	 */
	public static UnitConnectionType codeCode(final String code){
		for(final UnitConnectionType t : values()){
			if(t.getCode().equals(code)){
				return t;
			}
		}
		return null;
	}
	
}
