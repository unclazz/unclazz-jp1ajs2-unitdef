package org.unclazz.jp1ajs2.unitdef.util;

/**
 * {@link CharSequence}インスタンス化が可能なオブジェクトを表わすインターフェース.
 */
public interface CharSequential {
	/**
	 * オブジェクトの保持する情報を文字シーケンス化する.
	 * @return 文字シーケンス
	 */
	CharSequence toCharSequence();
	/**
	 * オブジェクトが保持する情報の文字シーケンス表現と引数で指定された文字シーケンスを比較し
	 * それぞれの文字の並びが一致する場合{@code true}を返す.
	 * @param other 文字シーケンス
	 * @return 判定結果
	 */
	boolean contentEquals(CharSequence other);
	/**
	 * オブジェクトが保持する情報の文字シーケンス表現と引数で指定されたオブジェクトが保持する文字シーケンスを比較し
	 * それぞれの文字の並びが一致する場合{@code true}を返す.
	 * @param other 文字シーケンス化可能なオブジェクト
	 * @return 判定結果
	 */
	boolean contentEquals(CharSequential other);
}
