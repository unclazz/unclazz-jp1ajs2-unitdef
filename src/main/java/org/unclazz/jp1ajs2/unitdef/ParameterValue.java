package org.unclazz.jp1ajs2.unitdef;

/**
 * ユニット定義パラメータを構成する個々の値を表わすインターフェース.
 */
public interface ParameterValue extends Component {
	/**
	 * ユニット定義ファイルから読み取られた文字シーケンスそのものを返す.
	 * 値全体が二重引用符により囲われていた場合、それらの引用符と#によるエスケープは解除された状態で返される。
	 * @return 文字シーケンス
	 */
	String getStringValue();
	/**
	 * 値がタプルである場合その情報にアクセスするための{@link Tuple}インスタンスを返す.
	 * 値の形式がタプルではない場合、このメソッドは空のタプルを返す。
	 * @return タプル
	 */
	Tuple getTuple();
	/**
	 * 値の種別を返す.
	 * @return 種別
	 */
	ParameterValueType getType();
}
