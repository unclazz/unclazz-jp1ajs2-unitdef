package com.m12i.code.parse;

/**
 * {@link Reader}インスタンスをとってその内容を読み取り、結果を任意の型のオブジェクトとして返すパーサ.
 * @param <T> パースした結果得られるオブジェクトの型
 */
public interface Parser<T> {
	/**
	 * 引数で指定された対象を読み取って返す.
	 * @param p 読み取り対象
	 * @return 読み取り結果
	 * @throws ParseException 構文エラーが発生した場合、もしくは、読み取り中に予期せぬエラーが発生した場合
	 */
	T parse(Reader p) throws ParseException;
}
