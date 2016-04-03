package org.unclazz.jp1ajs2.unitdef;

import org.unclazz.jp1ajs2.unitdef.query.ParameterValueQuery;
import org.unclazz.jp1ajs2.unitdef.util.CharSequential;

/**
 * ユニット定義パラメータを構成する個々の値を表わすインターフェース.
 */
public interface ParameterValue extends CharSequential {
	/**
	 * ユニット定義ファイルから読み取られた文字シーケンスそのものを返す.
	 * 値全体が二重引用符により囲われていた場合、それらの引用符と#によるエスケープは解除された状態で返される。
	 * @return 文字シーケンス
	 */
	CharSequence getRawCharSequence();
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
	/**
	 * クエリを使用して値を抽出・加工して返す.
	 * @param <T> クエリにより返される値の型
	 * @param q クエリ
	 * @return クエリにより抽出・加工された値
	 */
	<T> T query(ParameterValueQuery<T> q);
}
