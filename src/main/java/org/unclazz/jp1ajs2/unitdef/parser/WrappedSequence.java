package org.unclazz.jp1ajs2.unitdef.parser;

import java.io.IOException;
import java.io.InputStream;

/**
 * 入力データ（文字列やリーダー）へのアクセスを汎化するためのインターフェース.
 */
public interface WrappedSequence {
	/**
	 * 次の1文字を読み込む.
	 * 文字列の終わりあるいはEOFに到達すると{@code -1}を返す。
	 * @return 次の1文字
	 * @throws IOException 読み取り中にエラーが発生した場合
	 */
	int read() throws IOException;
	/**
	 * ストリームをクローズする.
	 * 入力データが文字列である場合は事実上なにもしない。
	 * @throws IOException クローズ中にエラーが発生した場合
	 */
	void close() throws IOException;
	/**
	 * 現在読み取り位置にマークをする.
	 * {@link #reset()}メソッドを呼び出すことで読み取り位置がこのマークした位置に戻る。
	 * ただし引数で指定された数値は{@link InputStream#mark(int)}に渡される。
	 * @param readAheadLimit 先読み上限
	 * @throws IOException マーク設定中に例外が発生した場合
	 */
	void mark(int readAheadLimit) throws IOException;
	/**
	 * 事前にマークした位置に読み取り位置を戻す.
	 * @throws IOException 読み取り位置の変更中にエラーが発生した場合
	 */
	void reset() throws IOException;
}
