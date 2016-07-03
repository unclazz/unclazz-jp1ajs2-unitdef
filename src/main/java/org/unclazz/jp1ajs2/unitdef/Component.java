package org.unclazz.jp1ajs2.unitdef;

/**
 * ユニット定義を構成する部品を表すインターフェース.
 * <p>{@link Unit}、{@link Parameter}。{@link ParameterValue}などはいずれも
 * このインターフェースを拡張・実装して、ユニット定義ファイル上の表現形式へのアクセスを提供する。</p>
 */
public interface Component {
	/**
	 * オブジェクトの保持する情報を文字シーケンス化する.
	 * <p>このメソッドが返すシーケンスは{@link toString()}が返すそれのようなデバッグ用途でも利用されうるものではない。
	 * このメソッドが返すシーケンスは当該コンポーネントのユニット定義ファイル上における表現形式で表されたものでなくてはならない。</p>
	 * @return 文字シーケンス
	 */
	CharSequence serialize();
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
	boolean contentEquals(Component other);
}
