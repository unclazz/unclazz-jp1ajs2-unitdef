package org.unclazz.jp1ajs2.unitdef;

/**
 * ユニット定義パラメータ値のフォーマット.<br>
 * <p>ユニット定義パラメータが定義ファイルや定義文字列のなかでどのように表現されているかを示す。
 * {@link #TUPLE}の場合、当該ユニット定義パラメータの値へのアクセスの仕方が変わる点で重要
 * （{@link ParameterValue#getTupleValue()}でタプルもどきの情報にアクセスすることができる）。
 * 一方、{@link #QUOTED_STRING}と{@link #RAW_STRING}のちがいは、
 * ユニット定義パラメータの値をJavaコードからアクセスしている限りは重要でないが、
 * {@link ParameterValue#toString()}などで文字列化を行う際には挙動が変わってくる点に注意。</p>
 */
public enum ParamValueFormat {
	/**
	 * タプルもどき.
	 */
	TUPLE,
	/**
	 * 二重引用符で囲われた文字列.
	 */
	QUOTED_STRING,
	/**
	 * 二重引用符で囲われていない文字列.
	 */
	RAW_STRING
}
