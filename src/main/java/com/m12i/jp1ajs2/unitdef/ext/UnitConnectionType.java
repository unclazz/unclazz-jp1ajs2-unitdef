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
	
	private final String abbr;
	private final String desc;
	
	private UnitConnectionType(final String abbr, final String desc){
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
	 * @return 接続種別
	 */
	public static UnitConnectionType searchByAbbr(final String abbr){
		for(final UnitConnectionType t : values()){
			if(t.getAbbr().equals(abbr)){
				return t;
			}
		}
		return null;
	}
	
}
