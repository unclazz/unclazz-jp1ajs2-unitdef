package org.unclazz.jp1ajs2.unitdef.parameter;

/**
 * 接続種別.
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
	 * コードをキーとして列挙体インスタンスを検索して返す.
	 * @param code コード
	 * @return 接続種別
	 */
	public static UnitConnectionType valueOfCode(final String code){
		for(final UnitConnectionType t : values()){
			if(t.getCode().equals(code)){
				return t;
			}
		}
		return null;
	}
	
}
